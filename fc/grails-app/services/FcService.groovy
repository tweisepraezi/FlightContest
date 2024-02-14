import java.math.BigDecimal;
import java.text.DecimalFormat
import java.util.Map;
import java.util.Date
import groovy.json.JsonBuilder

import org.xhtmlrenderer.pdf.ITextRenderer
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.Cookie

import org.springframework.web.multipart.MultipartFile

class FcService
{
    boolean transactional = true
    def messageSource
	def logService
    def domainService
    def servletContext
    def gpxService
    def kmlService
    def calcService
    
	static int maxCookieAge = 31536000 // seconds (1 Jahr)
    
    
    //--------------------------------------------------------------------------
    Map startAflos(Map params,lastAflosController)
    {
        if (lastAflosController) {
        	return [controller:lastAflosController]
        }
        return [controller:'aflosRouteDefs']
    }
    
    //--------------------------------------------------------------------------
    Map getAircraft(Map params)
    {
        Aircraft aircraft_instance = Aircraft.get(params.id)
        if (!aircraft_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
        return ['instance':aircraft_instance]
    }

    //--------------------------------------------------------------------------
    Map updateAircraft(Map params)
    {
        Aircraft aircraft_instance = Aircraft.get(params.id)
        if(aircraft_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(aircraft_instance.version > version) {
                    aircraft_instance.errors.rejectValue("version", "aircraft.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':aircraft_instance]
                }
            }

            if (params.registration != aircraft_instance.registration) {
                Aircraft aircraft2Instance = Aircraft.findByRegistrationAndContest(params.registration,aircraft_instance.contest)
                if (aircraft2Instance) {
                    return ['instance':aircraft2Instance,'error':true,'message':getMsg('fc.aircraft.registration.error',["${params.registration}"])]
                }
            }
            
            aircraft_instance.properties = params

            if(!aircraft_instance.hasErrors() && aircraft_instance.save()) {
                return ['instance':aircraft_instance,'saved':true,'message':getMsg('fc.updated',["${aircraft_instance.registration}"])]
            } else {
                return ['instance':aircraft_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createAircraft(Map params)
    {
        Aircraft aircraft_instance = new Aircraft()
        aircraft_instance.properties = params
        return ['instance':aircraft_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveAircraft(Map params,Contest contestInstance)
    {
        Aircraft aircraft_instance = new Aircraft(params)
        aircraft_instance.contest = contestInstance
        
        if (params.registration) {
            Aircraft aircraft2Instance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
            if (aircraft2Instance) {
            	return ['instance':aircraft2Instance,'error':true,'message':getMsg('fc.aircraft.registration.error',["${params.registration}"])]
            }
        }
        
        if(!aircraft_instance.hasErrors() && aircraft_instance.save()) {
            return ['instance':aircraft_instance,'saved':true,'message':getMsg('fc.created',["${aircraft_instance.registration}"])]
        } else {
            return ['instance':aircraft_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteAircraft(Map params)
    {
        Aircraft aircraft_instance = Aircraft.get(params.id)
        if(aircraft_instance) {
			if (!Test.findByTaskAircraft(aircraft_instance)) {
	            try {
	                if (aircraft_instance.user1) {
	                    Crew crew_instance = Crew.get( aircraft_instance.user1.id )
	                    crew_instance.aircraft = null
	                    crew_instance.save()
	                    aircraft_instance.user1 = null 
	                }
	                if (aircraft_instance.user2) {
	                    Crew crew_instance = Crew.get( aircraft_instance.user2.id )
	                    crew_instance.aircraft = null
	                    crew_instance.save()
	                    aircraft_instance.user2 = null 
	                }
	                aircraft_instance.delete()
	                return ['deleted':true,'message':getMsg('fc.deleted',["${aircraft_instance.registration}"])]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.aircraft'),params.id])]
				}
			} else {
				return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.aircraft'),params.id])]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateContest(String showLanguage, Map params)
    {
		printstart "updateContest $params.resultfilter"
		
        Contest contest_instance = Contest.get(params.id)
        
        if (contest_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(contest_instance.version > version) {
                    contest_instance.errors.rejectValue("version", "contest.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':contest_instance]
                }
            }
			
			boolean calculate_points = false
            Map old_contestrulepoints_values = GetContestRulePointsValues(contest_instance)
			
			boolean result_classes = contest_instance.resultClasses
			ContestRules contest_rule = contest_instance.contestRule
            boolean contestrule_foreachclass = contest_instance.contestRuleForEachClass
			int bestofanalysis_task_num = contest_instance.bestOfAnalysisTaskNum
			
			int team_crew_num = contest_instance.teamCrewNum
			boolean team_planning_results = contest_instance.teamPlanningResults
			boolean team_flight_results = contest_instance.teamFlightResults
			boolean team_observation_results = contest_instance.teamObservationResults
			boolean team_landing_results = contest_instance.teamLandingResults
			boolean team_special_results = contest_instance.teamSpecialResults
			String team_class_results = contest_instance.teamClassResults
			String team_task_results = contest_instance.teamTaskResults
			
			boolean contest_planning_results = contest_instance.contestPlanningResults
			boolean contest_flight_results = contest_instance.contestFlightResults
			boolean contest_observation_results = contest_instance.contestObservationResults
			boolean contest_landing_results = contest_instance.contestLandingResults
			boolean contest_special_results = contest_instance.contestSpecialResults
			String contest_class_results = contest_instance.contestClassResults
			String contest_task_results = contest_instance.contestTaskResults
			String contest_team_results = contest_instance.contestTeamResults
            BigDecimal contest_landing_results_factor = contest_instance.contestLandingResultsFactor
            
            params.contestLandingResultsFactor = Languages.GetLanguageDecimal(showLanguage, params.contestLandingResultsFactor)
            
            contest_instance.properties = params

            if (!contest_instance.liveTrackingContestDate) {
                Map ret = ['instance':contest_instance, 'message':getMsg('fc.contest.contestdate.notexists')]
                printerror ret
                return ret
            }
            
			if (contest_instance.bestOfAnalysisTaskNum == null) {
				contest_instance.bestOfAnalysisTaskNum = 0
			}
			
			switch (params.resultfilter) {
				case ResultFilter.Contest.toString():
                    contest_instance.contestPrintTaskTestDetails = ""
                    for (Task task_instance in contest_instance.tasks) {
                        if (params["tasktestdetails_${task_instance.id}"] == "on") {
                            if (contest_instance.contestPrintTaskTestDetails) {
                                contest_instance.contestPrintTaskTestDetails += ","
                            }
                            contest_instance.contestPrintTaskTestDetails += "tasktestdetails_${task_instance.id}"
                        }
                    }
					contest_instance.contestClassResults = ""
					for (ResultClass resultclass_instance in contest_instance.resultclasses) {
						if (params["resultclass_${resultclass_instance.id}"] == "on") {
							if (contest_instance.contestClassResults) {
								contest_instance.contestClassResults += ","
							}
							contest_instance.contestClassResults += "resultclass_${resultclass_instance.id}"
						}
					}
					contest_instance.contestTaskResults = ""
					for (Task task_instance in contest_instance.tasks) {
						if (params["task_${task_instance.id}"] == "on") {
							if (contest_instance.contestTaskResults) {
								contest_instance.contestTaskResults += ","
							}
							contest_instance.contestTaskResults += "task_${task_instance.id}"
						}
					}
					contest_instance.contestTeamResults = ""
					for (Team team_instance in contest_instance.teams) {
						if (params["team_${team_instance.id}"] == "on") {
							if (contest_instance.contestTeamResults) {
								contest_instance.contestTeamResults += ","
							}
							contest_instance.contestTeamResults += "team_${team_instance.id}"
						}
					}
					if (params["team_no_team_crew"] == "on") {
						if (contest_instance.contestTeamResults) {
							contest_instance.contestTeamResults += ","
						}
						contest_instance.contestTeamResults += "team_no_team_crew"

					}
                    if (params["team_all_teams"] == "on") {
                        if (contest_instance.contestTeamResults) {
                            contest_instance.contestTeamResults += ","
                        }
                        contest_instance.contestTeamResults += "team_all_teams"

                    }
					break
				case ResultFilter.Team.toString():
					contest_instance.teamClassResults = ""
					for (ResultClass resultclass_instance in contest_instance.resultclasses) {
						if (params["resultclass_${resultclass_instance.id}"] == "on") {
							if (contest_instance.teamClassResults) {
								contest_instance.teamClassResults += ","
							}
							contest_instance.teamClassResults += "resultclass_${resultclass_instance.id}"
						}
					}
					contest_instance.teamTaskResults = ""
					for (Task task_instance in contest_instance.tasks) {
						if (params["task_${task_instance.id}"] == "on") {
							if (contest_instance.teamTaskResults) {
								contest_instance.teamTaskResults += ","
							}
							contest_instance.teamTaskResults += "task_${task_instance.id}"
						}
					}
					break
			}
			
            CalculateTimezone2(contest_instance)
            
			boolean set_result_classes = (result_classes != contest_instance.resultClasses) && contest_instance.resultClasses
			
			boolean modify_contest_rule = contest_instance.contestRule != contest_rule

			boolean modify_team_results = (contest_instance.teamCrewNum != team_crew_num) ||
			                              (contest_instance.teamPlanningResults != team_planning_results) ||
			                              (contest_instance.teamFlightResults != team_flight_results) ||
			 							  (contest_instance.teamObservationResults != team_observation_results) ||
										  (contest_instance.teamLandingResults != team_landing_results) ||
										  (contest_instance.teamSpecialResults != team_special_results) ||
										  (contest_instance.teamClassResults != team_class_results) ||
										  (contest_instance.teamTaskResults != team_task_results) ||
										  (contest_instance.bestOfAnalysisTaskNum != bestofanalysis_task_num) 
										  
			boolean modify_contest_results = (contest_instance.contestPlanningResults != contest_planning_results) ||
			                                 (contest_instance.contestFlightResults != contest_flight_results) ||
			 							     (contest_instance.contestObservationResults != contest_observation_results) ||
										     (contest_instance.contestLandingResults != contest_landing_results) ||
										     (contest_instance.contestSpecialResults != contest_special_results) ||
										     (contest_instance.contestClassResults != contest_class_results) ||
											 (contest_instance.contestTaskResults != contest_task_results) ||
											 (contest_instance.contestTeamResults != contest_team_results) ||
										     (contest_instance.bestOfAnalysisTaskNum != bestofanalysis_task_num) ||
                                             (contest_instance.contestLandingResultsFactor != contest_landing_results_factor)
											 
			boolean modify_result_classes_results = (contest_instance.bestOfAnalysisTaskNum != bestofanalysis_task_num) && contest_instance.resultClasses
			
            if (IsContestRulePointsValueModified(contest_instance,old_contestrulepoints_values)) {
                println "Contest rule value modfified."
                calculate_points = true
            }
            if (result_classes != contest_instance.resultClasses) {
                calculate_points = true
            }
            if (contest_instance.resultClasses && (contestrule_foreachclass != contest_instance.contestRuleForEachClass)) {
                calculate_points = true
            }
            
			if (modify_contest_rule) {
				println "Contest rule modfified."
				setContestRulePoints(contest_instance, contest_instance.contestRule)
                setContestRuleDefaults(contest_instance, contest_instance.contestRule, false)
                calculate_points = true
			}
			
			if (modify_team_results) {
				println "Team results modfified."
				for (Team team_instance in Team.findAllByContest(contest_instance,[sort:"id"])) {
					team_instance.contestPenalties = 0
					team_instance.contestPosition = 0
                    team_instance.contestEqualPosition = false
                    team_instance.contestAddPosition = 0
					team_instance.save()
				}
			}
			
			if (modify_contest_results) {
				println "Contest results modfified."
				for (Crew crew_instance in Crew.findAllByContest(contest_instance,[sort:"id"])) {
					crew_instance.contestPenalties = 0
					crew_instance.contestPosition = 0
					crew_instance.noContestPosition = false
                    crew_instance.contestEqualPosition = false
                    crew_instance.contestAddPosition = 0
					crew_instance.save()
				}
			}
			
			if (set_result_classes) {
				println "Contest with classes has been set."
				for (Task task_instance in Task.findAllByContest(contest_instance,[sort:"idTitle"])) {
					if (!TaskClass.findByTask(task_instance)) {
						for (ResultClass resultclass_instance in ResultClass.findAllByContest(contest_instance,[sort:"id"])) {
							TaskClass taskclass_instance = new TaskClass()
							taskclass_instance.task = task_instance
							taskclass_instance.resultclass = resultclass_instance
							taskclass_instance.planningTestRun = task_instance.planningTestRun
							taskclass_instance.flightTestRun = task_instance.flightTestRun
							taskclass_instance.observationTestRun = task_instance.observationTestRun
                            taskclass_instance.observationTestTurnpointRun = task_instance.observationTestTurnpointRun
                            taskclass_instance.observationTestEnroutePhotoRun = task_instance.observationTestEnroutePhotoRun
                            taskclass_instance.observationTestEnrouteCanvasRun = task_instance.observationTestEnrouteCanvasRun
							taskclass_instance.landingTestRun = task_instance.landingTestRun
							taskclass_instance.landingTest1Run = task_instance.landingTest1Run
							taskclass_instance.landingTest2Run = task_instance.landingTest2Run
							taskclass_instance.landingTest3Run = task_instance.landingTest3Run
							taskclass_instance.landingTest4Run = task_instance.landingTest4Run
							taskclass_instance.specialTestRun = task_instance.specialTestRun
							taskclass_instance.planningTestDistanceMeasure = task_instance.planningTestDistanceMeasure
							taskclass_instance.planningTestDirectionMeasure = task_instance.planningTestDirectionMeasure
							taskclass_instance.flightTestCheckSecretPoints = task_instance.flightTestCheckSecretPoints
							taskclass_instance.flightTestCheckTakeOff = task_instance.flightTestCheckTakeOff
							taskclass_instance.flightTestCheckLanding = task_instance.flightTestCheckLanding
							taskclass_instance.save()
						}
						println "TaskClasses for task '${task_instance.name()}' has been generated."
					}
				}
			}
			
			if (modify_result_classes_results) {
				println "Class results modfified."
				for (Crew crew_instance in Crew.findAllByContest(contest_instance,[sort:"id"])) {
					crew_instance.contestPenalties = 0
					crew_instance.classPosition = 0
					crew_instance.noClassPosition = false
                    crew_instance.classEqualPosition = false
                    crew_instance.classAddPosition = 0
					crew_instance.save()
				}
			}
			
			if (calculate_points) {
			    calculate_points_contest(contest_instance)
			}

            if(!contest_instance.hasErrors() && contest_instance.save()) {
                Map ret = ['instance':contest_instance,'saved':true,'message':getMsg('fc.updated',["${contest_instance.title}"])]
				printdone ret
				return ret
            } else {
				printerror ""
                return ['instance':contest_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
			printerror ret
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map updatecrewprintsettingsContest(Contest contestInstance, Map params, PrintSettings printSettings)
    {
		printstart "updatecrewprintsettingsContest $printSettings"
		
		if(params.version) {
			long version = params.version.toLong()
			if(contestInstance.version > version) {
				contestInstance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
				printerror ""
				return ['instance':contestInstance]
			}
		}
		
		String detail_name = getMsg('fc.setprintsettings.modifications')
		
		switch (printSettings) {
			case PrintSettings.CrewModified:
				contestInstance.properties = params
				
				if (params["printCrewNumber"]) {
					contestInstance.printCrewNumber = params.printCrewNumber == "on"
				}
				if (params["printCrewName"]) {
					contestInstance.printCrewName = params.printCrewName == "on"
				}
                if (params["printCrewEmail"]) {
                    contestInstance.printCrewEmail = params.printCrewEmail == "on"
                }
				if (params["printCrewTeam"]) {
					contestInstance.printCrewTeam = params.printCrewTeam == "on"
				}
				if (params["printCrewClass"]) {
					contestInstance.printCrewClass = params.printCrewClass == "on"
				}
				if (params["printCrewShortClass"]) {
					contestInstance.printCrewShortClass = params.printCrewShortClass == "on"
				}
				if (params["printCrewAircraft"]) {
					contestInstance.printCrewAircraft = params.printCrewAircraft == "on"
				}
				if (params["printCrewAircraftType"]) {
					contestInstance.printCrewAircraftType = params.printCrewAircraftType == "on"
				}
				if (params["printCrewAircraftColour"]) {
					contestInstance.printCrewAircraftColour = params.printCrewAircraftColour == "on"
				}
				if (params["printCrewTAS"]) {
					contestInstance.printCrewTAS = params.printCrewTAS == "on"
				}
                if (params["printCrewTrackerID"]) {
                    contestInstance.printCrewTrackerID = params.printCrewTrackerID == "on"
                }
                if (params["printCrewUUID"]) {
                    contestInstance.printCrewUUID = params.printCrewUUID == "on"
                }
                if (params["printCrewSortHelp"]) {
                    contestInstance.printCrewSortHelp = params.printCrewSortHelp == "on"
                }
				if (params["printCrewEmptyColumn1"]) {
					contestInstance.printCrewEmptyColumn1 = params.printCrewEmptyColumn1 == "on"
				}
				if (params["printCrewEmptyColumn2"]) {
					contestInstance.printCrewEmptyColumn2 = params.printCrewEmptyColumn2 == "on"
				}
				if (params["printCrewEmptyColumn3"]) {
					contestInstance.printCrewEmptyColumn3 = params.printCrewEmptyColumn3 == "on"
				}
				if (params["printCrewEmptyColumn4"]) {
					contestInstance.printCrewEmptyColumn4 = params.printCrewEmptyColumn4 == "on"
				}
				if (params["printCrewLandscape"]) {
					contestInstance.printCrewLandscape = params.printCrewLandscape == "on"
				}
				if (params["printCrewA3"]) {
					contestInstance.printCrewA3 = params.printCrewA3 == "on"
				}
				break
			case PrintSettings.CrewStandard:
				detail_name = getMsg('fc.standard')
				contestInstance.printCrewPrintTitle = ""
				contestInstance.printCrewNumber = true
				contestInstance.printCrewName = true
                contestInstance.printCrewEmail = false
				contestInstance.printCrewTeam = true
				contestInstance.printCrewClass = true
				contestInstance.printCrewShortClass = false
				contestInstance.printCrewAircraft = true
				contestInstance.printCrewAircraftType = false
				contestInstance.printCrewAircraftColour = false
				contestInstance.printCrewTAS = true
                contestInstance.printCrewTrackerID = false
                contestInstance.printCrewUUID = false
                contestInstance.printCrewSortHelp = false
				contestInstance.printCrewEmptyColumn1 = false
				contestInstance.printCrewEmptyTitle1 = ""
				contestInstance.printCrewEmptyColumn2 = false
				contestInstance.printCrewEmptyTitle2 = ""
				contestInstance.printCrewEmptyColumn3 = false
				contestInstance.printCrewEmptyTitle3 = ""
				contestInstance.printCrewEmptyColumn4 = false
				contestInstance.printCrewEmptyTitle4 = ""
				contestInstance.printCrewLandscape = false
				contestInstance.printCrewA3 = false
				break
			case PrintSettings.CrewNone:
				detail_name = getMsg('fc.setprintsettings.none')
				contestInstance.printCrewPrintTitle = ""
				contestInstance.printCrewNumber = false
				contestInstance.printCrewName = false
                contestInstance.printCrewEmail = false
				contestInstance.printCrewTeam = false
				contestInstance.printCrewClass = false
				contestInstance.printCrewShortClass = false
				contestInstance.printCrewAircraft = false
				contestInstance.printCrewAircraftType = false
				contestInstance.printCrewAircraftColour = false
				contestInstance.printCrewTAS = false
                contestInstance.printCrewTrackerID = false
                contestInstance.printCrewUUID = false
                contestInstance.printCrewSortHelp = false
				contestInstance.printCrewEmptyColumn1 = false
				contestInstance.printCrewEmptyTitle1 = ""
				contestInstance.printCrewEmptyColumn2 = false
				contestInstance.printCrewEmptyTitle2 = ""
				contestInstance.printCrewEmptyColumn3 = false
				contestInstance.printCrewEmptyTitle3 = ""
				contestInstance.printCrewEmptyColumn4 = false
				contestInstance.printCrewEmptyTitle4 = ""
				contestInstance.printCrewLandscape = false
				contestInstance.printCrewA3 = false
				break
			case PrintSettings.CrewAll:
				detail_name = getMsg('fc.setprintsettings.all')
				contestInstance.printCrewPrintTitle = ""
				contestInstance.printCrewNumber = true
				contestInstance.printCrewName = true
                contestInstance.printCrewEmail = true
				contestInstance.printCrewTeam = true
				contestInstance.printCrewClass = true
				contestInstance.printCrewShortClass = true
				contestInstance.printCrewAircraft = true
				contestInstance.printCrewAircraftType = true
				contestInstance.printCrewAircraftColour = true
				contestInstance.printCrewTAS = true
                contestInstance.printCrewTrackerID = true
                contestInstance.printCrewUUID = true
                contestInstance.printCrewSortHelp = true
				contestInstance.printCrewEmptyColumn1 = true
				contestInstance.printCrewEmptyTitle1 = ""
				contestInstance.printCrewEmptyColumn2 = true
				contestInstance.printCrewEmptyTitle2 = ""
				contestInstance.printCrewEmptyColumn3 = true
				contestInstance.printCrewEmptyTitle3 = ""
				contestInstance.printCrewEmptyColumn4 = true
				contestInstance.printCrewEmptyTitle4 = ""
				contestInstance.printCrewLandscape = true
				contestInstance.printCrewA3 = false
				break
			case PrintSettings.CrewLanding:
				detail_name = getMsg('fc.landingtest.setprintsettings')
				contestInstance.printCrewPrintTitle = getPrintMsg('fc.test.landing')
				contestInstance.printCrewNumber = true
				contestInstance.printCrewName = false
                contestInstance.printCrewEmail = false
				contestInstance.printCrewTeam = false
				contestInstance.printCrewClass = false
				contestInstance.printCrewShortClass = false
				contestInstance.printCrewAircraft = true
				contestInstance.printCrewAircraftType = true
				contestInstance.printCrewAircraftColour = false
				contestInstance.printCrewTAS = false
                contestInstance.printCrewTrackerID = false
                contestInstance.printCrewUUID = false
                contestInstance.printCrewSortHelp = false
				contestInstance.printCrewEmptyColumn1 = true
				contestInstance.printCrewEmptyTitle1 = getPrintMsg('fc.test.landing.field')
				contestInstance.printCrewEmptyColumn2 = true
				contestInstance.printCrewEmptyTitle2 = ""
				contestInstance.printCrewEmptyColumn3 = true
				contestInstance.printCrewEmptyTitle3 = ""
				contestInstance.printCrewEmptyColumn4 = true
				contestInstance.printCrewEmptyTitle4 = ""
				contestInstance.printCrewLandscape = false
				contestInstance.printCrewA3 = false
				break
		}
		
		if(!contestInstance.hasErrors() && contestInstance.save()) {
			Map ret = ['instance':contestInstance,'saved':true,'message':getMsg('fc.crew.printsettings.saved',[detail_name,"${contestInstance.name()}"])]
			printdone ret
			return ret
		} else {
			printerror ""
			return ['instance':contestInstance]
		}
    }
	
    //--------------------------------------------------------------------------
    Map calculatepointsContest(Map params)
    {
        Contest contest_instance = Contest.get(params.id)
        if (contest_instance) {
            calculate_points_contest(contest_instance)
            return ['instance':contest_instance,'message':getMsg('fc.contestrule.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
	private void calculate_points_contest(Contest contestInstance)
	{
		printstart "calculate_points_contest"
		Task.findAllByContest(contestInstance,[sort:"idTitle"]).each { Task task_instance ->
			Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
				calculateTestPenalties(test_instance,true)
                test_instance.flightTestLink = ""
				delete_uploadjobtest(test_instance)
                test_instance.crewResultsModified = true
				test_instance.save()
			}
		}
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map createContest(Map params)
    {
        Contest contest_instance = new Contest()
		contest_instance.title = getPrintMsg('fc.contest.new.title')
        contest_instance.properties = params
        contest_instance.liveTrackingContestDate = FcTime.GetDateStr(new Date())
        return ['instance':contest_instance,'created':true]
    }
    
    //--------------------------------------------------------------------------
	String getContestCopyTitle(Contest sourceContestInstance)
	{
		String new_title = sourceContestInstance.title
		if ( new_title.lastIndexOf(')') + 1 == new_title.length())
		{
			int i1 = new_title.lastIndexOf('(')
			if (i1 > 0) {
				new_title = new_title.substring(0,i1 - 1)
			}
		}
		new_title = getMsg('fc.contest.copytitle',["$new_title"])
		
		String new_title2 = new_title 
		int found_num = 1
		while (ContestTitleFound(new_title2)) {
			found_num++
			new_title2 = "$new_title ($found_num)"
		}
		return new_title2
	}
	
    //--------------------------------------------------------------------------
	private boolean ContestTitleFound(String newTitle)
	{
      	if (Contest.findByIdIsNotNull()) {
			for(Contest contest_instance in Contest.list()) {
			   if (contest_instance.title == newTitle) {
				   return true
			   }
			}
      	}
		return false
	}
	
    //--------------------------------------------------------------------------
    Map saveContest(Map params)
    {
        Contest contest_instance = new Contest(params)
        
		setContestRulePoints(contest_instance, contest_instance.contestRule)
        setContestRuleDefaults(contest_instance, contest_instance.contestRule, true)
		
		contest_instance.imageBottomLeftText = getPrintMsg('fc.contest.image.bottomleft.text')
		contest_instance.imageBottomRightText = getPrintMsg('fc.contest.image.bottomright.text')
		
		if (contest_instance.bestOfAnalysisTaskNum == null) {
			contest_instance.bestOfAnalysisTaskNum = 0
		}

        if (!contest_instance.liveTrackingContestDate) {
			return ['instance':contest_instance, 'message':getMsg('fc.contest.contestdate.notexists')]
        }
        
        CalculateTimezone2(contest_instance)
		
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    void CalculateTimezone2(Contest contestInstance)
    {
        if (contestInstance.timeZone2) {
            Date contest_date = Date.parse("yyyy-MM-dd", contestInstance.liveTrackingContestDate)
            int timezone_offset = contestInstance.timeZone2.getOffset(contest_date.getTime())
            
            int timezone_offset_hour = timezone_offset/3600000
            String timezone_offset_hour_str = ""
            if (timezone_offset_hour < 0) {
                timezone_offset_hour_str = "-"
            }
            if (timezone_offset_hour.abs() < 10) {
                timezone_offset_hour_str += "0"
            }
            timezone_offset_hour_str += timezone_offset_hour.abs()
            
            int timezone_offset_min = (timezone_offset_hour*3600000 - timezone_offset)/60000
            String timezone_offset_min_str = ""
            if (timezone_offset_min < 10) {
                timezone_offset_min_str += "0"
            }
            timezone_offset_min_str += timezone_offset_min
            
            contestInstance.timeZone = "${timezone_offset_hour_str}:${timezone_offset_min_str}"
        }
    }
    
    //--------------------------------------------------------------------------
    Map standardpointsContest(Map params)
    {
        Contest contest_instance = Contest.get(params.id)
        Map old_contestrulepoints_values = GetContestRulePointsValues(contest_instance)
        
        setContestRulePoints(contest_instance, contest_instance.contestRule)
        if (IsContestRulePointsValueModified(contest_instance,old_contestrulepoints_values)) {
            calculate_points_contest(contest_instance)
        }
        
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map standarddefaultsContest(Map params)
    {
        Contest contest_instance = Contest.get(params.id)
        Map old_contestruledefaults_values = GetContestRuleDefaultsValues(contest_instance)
        
        setContestRuleDefaults(contest_instance, contest_instance.contestRule, false)
        if (IsContestRuleDefaultsValueModified(contest_instance,old_contestruledefaults_values)) {
            // Nothing
        }
        
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
	private void setContestRulePoints(toInstance, ContestRules contestRule)
	{
		println "setContestRulePoints '${contestRule.ruleValues.ruleTitle}'"
		
		// General
        toInstance.ruleTitle = contestRule.ruleValues.ruleTitle
		toInstance.precisionFlying = contestRule.ruleValues.precisionFlying
        toInstance.increaseFactor = contestRule.ruleValues.increaseFactor
        toInstance.printLandingCalculatorValues = contestRule.ruleValues.printLandingCalculatorValues
        toInstance.printPointsGeneral = contestRule.ruleValues.printPointsGeneral
		toInstance.printPointsPlanningTest = contestRule.ruleValues.printPointsPlanningTest
		toInstance.printPointsFlightTest = contestRule.ruleValues.printPointsFlightTest
        toInstance.printPointsObservationTest = contestRule.ruleValues.printPointsObservationTest
		toInstance.printPointsLandingTest1 = contestRule.ruleValues.printPointsLandingTest1
		toInstance.printPointsLandingTest2 = contestRule.ruleValues.printPointsLandingTest2
		toInstance.printPointsLandingTest3 = contestRule.ruleValues.printPointsLandingTest3
		toInstance.printPointsLandingTest4 = contestRule.ruleValues.printPointsLandingTest4
        toInstance.printPointsLandingField = contestRule.ruleValues.printPointsLandingField
        toInstance.landingFieldImageName = contestRule.ruleValues.landingFieldImageName
        toInstance.printPointsTurnpointSign = contestRule.ruleValues.printPointsTurnpointSign
        toInstance.printPointsEnrouteCanvas = contestRule.ruleValues.printPointsEnrouteCanvas
        
		// PlanningTest
		toInstance.planningTestDirectionCorrectGrad = contestRule.ruleValues.planningTestDirectionCorrectGrad
		toInstance.planningTestDirectionPointsPerGrad = contestRule.ruleValues.planningTestDirectionPointsPerGrad
		toInstance.planningTestTimeCorrectSecond = contestRule.ruleValues.planningTestTimeCorrectSecond
		toInstance.planningTestTimePointsPerSecond = contestRule.ruleValues.planningTestTimePointsPerSecond
		toInstance.planningTestMaxPoints = contestRule.ruleValues.planningTestMaxPoints
		toInstance.planningTestPlanTooLatePoints = contestRule.ruleValues.planningTestPlanTooLatePoints
		toInstance.planningTestExitRoomTooLatePoints = contestRule.ruleValues.planningTestExitRoomTooLatePoints
        toInstance.planningTestForbiddenCalculatorsPoints = contestRule.ruleValues.planningTestForbiddenCalculatorsPoints
        
		// FlightTest
		toInstance.flightTestTakeoffMissedPoints = contestRule.ruleValues.flightTestTakeoffMissedPoints
		toInstance.flightTestTakeoffCorrectSecond = contestRule.ruleValues.flightTestTakeoffCorrectSecond
		toInstance.flightTestTakeoffCheckSeconds = contestRule.ruleValues.flightTestTakeoffCheckSeconds
		toInstance.flightTestTakeoffPointsPerSecond = contestRule.ruleValues.flightTestTakeoffPointsPerSecond
		toInstance.flightTestCptimeCorrectSecond = contestRule.ruleValues.flightTestCptimeCorrectSecond
		toInstance.flightTestCptimePointsPerSecond = contestRule.ruleValues.flightTestCptimePointsPerSecond
		toInstance.flightTestCptimeMaxPoints = contestRule.ruleValues.flightTestCptimeMaxPoints
		toInstance.flightTestCpNotFoundPoints = contestRule.ruleValues.flightTestCpNotFoundPoints
		toInstance.flightTestProcedureTurnNotFlownPoints = contestRule.ruleValues.flightTestProcedureTurnNotFlownPoints
		toInstance.flightTestMinAltitudeMissedPoints = contestRule.ruleValues.flightTestMinAltitudeMissedPoints
		toInstance.flightTestBadCourseCorrectSecond = contestRule.ruleValues.flightTestBadCourseCorrectSecond
		toInstance.flightTestBadCoursePoints = contestRule.ruleValues.flightTestBadCoursePoints
		toInstance.flightTestBadCourseMaxPoints = contestRule.ruleValues.flightTestBadCourseMaxPoints
		toInstance.flightTestBadCourseStartLandingPoints = contestRule.ruleValues.flightTestBadCourseStartLandingPoints
		toInstance.flightTestLandingToLatePoints = contestRule.ruleValues.flightTestLandingToLatePoints
		toInstance.flightTestGivenToLatePoints = contestRule.ruleValues.flightTestGivenToLatePoints
		toInstance.flightTestSafetyAndRulesInfringementPoints = contestRule.ruleValues.flightTestSafetyAndRulesInfringementPoints
		toInstance.flightTestInstructionsNotFollowedPoints = contestRule.ruleValues.flightTestInstructionsNotFollowedPoints
		toInstance.flightTestFalseEnvelopeOpenedPoints = contestRule.ruleValues.flightTestFalseEnvelopeOpenedPoints
		toInstance.flightTestSafetyEnvelopeOpenedPoints = contestRule.ruleValues.flightTestSafetyEnvelopeOpenedPoints
		toInstance.flightTestFrequencyNotMonitoredPoints = contestRule.ruleValues.flightTestFrequencyNotMonitoredPoints
        toInstance.flightTestForbiddenEquipmentPoints = contestRule.ruleValues.flightTestForbiddenEquipmentPoints
		
        // ObservationTest
        toInstance.observationTestEnrouteValueUnit = contestRule.ruleValues.observationTestEnrouteValueUnit
        toInstance.observationTestEnrouteCorrectValue = contestRule.ruleValues.observationTestEnrouteCorrectValue
        toInstance.observationTestEnrouteInexactValue = contestRule.ruleValues.observationTestEnrouteInexactValue
        toInstance.observationTestEnrouteInexactPoints = contestRule.ruleValues.observationTestEnrouteInexactPoints
        toInstance.observationTestEnrouteNotFoundPoints = contestRule.ruleValues.observationTestEnrouteNotFoundPoints
        toInstance.observationTestEnrouteFalsePoints = contestRule.ruleValues.observationTestEnrouteFalsePoints
        toInstance.observationTestTurnpointNotFoundPoints = contestRule.ruleValues.observationTestTurnpointNotFoundPoints
        toInstance.observationTestTurnpointFalsePoints = contestRule.ruleValues.observationTestTurnpointFalsePoints
                
		// LandingTest
		toInstance.landingTest1MaxPoints = contestRule.ruleValues.landingTest1MaxPoints
		toInstance.landingTest1NoLandingPoints = contestRule.ruleValues.landingTest1NoLandingPoints
		toInstance.landingTest1OutsideLandingPoints = contestRule.ruleValues.landingTest1OutsideLandingPoints
		toInstance.landingTest1RollingOutsidePoints = contestRule.ruleValues.landingTest1RollingOutsidePoints
		toInstance.landingTest1PowerInBoxPoints = contestRule.ruleValues.landingTest1PowerInBoxPoints
		toInstance.landingTest1GoAroundWithoutTouchingPoints = contestRule.ruleValues.landingTest1GoAroundWithoutTouchingPoints
		toInstance.landingTest1GoAroundInsteadStopPoints = contestRule.ruleValues.landingTest1GoAroundInsteadStopPoints
		toInstance.landingTest1AbnormalLandingPoints = contestRule.ruleValues.landingTest1AbnormalLandingPoints
		toInstance.landingTest1NotAllowedAerodynamicAuxiliariesPoints = contestRule.ruleValues.landingTest1NotAllowedAerodynamicAuxiliariesPoints
		toInstance.landingTest1PenaltyCalculator = contestRule.ruleValues.landingTest1PenaltyCalculator
	
		toInstance.landingTest2MaxPoints = contestRule.ruleValues.landingTest2MaxPoints
		toInstance.landingTest2NoLandingPoints = contestRule.ruleValues.landingTest2NoLandingPoints
		toInstance.landingTest2OutsideLandingPoints = contestRule.ruleValues.landingTest2OutsideLandingPoints
		toInstance.landingTest2RollingOutsidePoints = contestRule.ruleValues.landingTest2RollingOutsidePoints
		toInstance.landingTest2PowerInBoxPoints = contestRule.ruleValues.landingTest2PowerInBoxPoints
		toInstance.landingTest2GoAroundWithoutTouchingPoints = contestRule.ruleValues.landingTest2GoAroundWithoutTouchingPoints
		toInstance.landingTest2GoAroundInsteadStopPoints = contestRule.ruleValues.landingTest2GoAroundInsteadStopPoints
		toInstance.landingTest2AbnormalLandingPoints = contestRule.ruleValues.landingTest2AbnormalLandingPoints
		toInstance.landingTest2NotAllowedAerodynamicAuxiliariesPoints = contestRule.ruleValues.landingTest2NotAllowedAerodynamicAuxiliariesPoints
		toInstance.landingTest2PowerInAirPoints = contestRule.ruleValues.landingTest2PowerInAirPoints
		toInstance.landingTest2PenaltyCalculator = contestRule.ruleValues.landingTest2PenaltyCalculator
		
		toInstance.landingTest3MaxPoints = contestRule.ruleValues.landingTest3MaxPoints
		toInstance.landingTest3NoLandingPoints = contestRule.ruleValues.landingTest3NoLandingPoints
		toInstance.landingTest3OutsideLandingPoints = contestRule.ruleValues.landingTest3OutsideLandingPoints
		toInstance.landingTest3RollingOutsidePoints = contestRule.ruleValues.landingTest3RollingOutsidePoints
		toInstance.landingTest3PowerInBoxPoints = contestRule.ruleValues.landingTest3PowerInBoxPoints
		toInstance.landingTest3GoAroundWithoutTouchingPoints = contestRule.ruleValues.landingTest3GoAroundWithoutTouchingPoints
		toInstance.landingTest3GoAroundInsteadStopPoints = contestRule.ruleValues.landingTest3GoAroundInsteadStopPoints
		toInstance.landingTest3AbnormalLandingPoints = contestRule.ruleValues.landingTest3AbnormalLandingPoints
		toInstance.landingTest3NotAllowedAerodynamicAuxiliariesPoints = contestRule.ruleValues.landingTest3NotAllowedAerodynamicAuxiliariesPoints
		toInstance.landingTest3PowerInAirPoints = contestRule.ruleValues.landingTest3PowerInAirPoints
		toInstance.landingTest3FlapsInAirPoints = contestRule.ruleValues.landingTest3FlapsInAirPoints
		toInstance.landingTest3PenaltyCalculator = contestRule.ruleValues.landingTest3PenaltyCalculator
		
		toInstance.landingTest4MaxPoints = contestRule.ruleValues.landingTest4MaxPoints
		toInstance.landingTest4NoLandingPoints = contestRule.ruleValues.landingTest4NoLandingPoints
		toInstance.landingTest4OutsideLandingPoints = contestRule.ruleValues.landingTest4OutsideLandingPoints
		toInstance.landingTest4RollingOutsidePoints = contestRule.ruleValues.landingTest4RollingOutsidePoints
		toInstance.landingTest4PowerInBoxPoints = contestRule.ruleValues.landingTest4PowerInBoxPoints
		toInstance.landingTest4GoAroundWithoutTouchingPoints = contestRule.ruleValues.landingTest4GoAroundWithoutTouchingPoints
		toInstance.landingTest4GoAroundInsteadStopPoints = contestRule.ruleValues.landingTest4GoAroundInsteadStopPoints
		toInstance.landingTest4AbnormalLandingPoints = contestRule.ruleValues.landingTest4AbnormalLandingPoints
		toInstance.landingTest4NotAllowedAerodynamicAuxiliariesPoints = contestRule.ruleValues.landingTest4NotAllowedAerodynamicAuxiliariesPoints
		toInstance.landingTest4TouchingObstaclePoints = contestRule.ruleValues.landingTest4TouchingObstaclePoints
		toInstance.landingTest4PenaltyCalculator = contestRule.ruleValues.landingTest4PenaltyCalculator
	}
	
    //--------------------------------------------------------------------------
    private void setContestRuleDefaults(Contest contestInstance, ContestRules contestRule, boolean newContest)
    {
        printstart "setContestRuleDefaults '${contestRule.ruleValues.ruleTitle}'"
        
        List routes_with_disabled_procedureturns = []
        if (!newContest) {
            for (Route route_instance in Route.findAllByContest(contestInstance,[sort:"idTitle"])) {
                if (!route_instance.useProcedureTurns) {
                    routes_with_disabled_procedureturns += route_instance.id
                }
            }
        }
        
        contestInstance.flightPlanShowLegDistance = contestRule.ruleValues.flightPlanShowLegDistance
        contestInstance.flightPlanShowTrueTrack = contestRule.ruleValues.flightPlanShowTrueTrack
        contestInstance.flightPlanShowTrueHeading = contestRule.ruleValues.flightPlanShowTrueHeading
        contestInstance.flightPlanShowGroundSpeed = contestRule.ruleValues.flightPlanShowGroundSpeed
        contestInstance.flightPlanShowLocalTime = contestRule.ruleValues.flightPlanShowLocalTime
        contestInstance.flightPlanShowElapsedTime = contestRule.ruleValues.flightPlanShowElapsedTime
        contestInstance.flightTestSubmissionMinutes = contestRule.ruleValues.flightTestSubmissionMinutes
        contestInstance.minRouteLegs = contestRule.ruleValues.minRouteLegs
        contestInstance.maxRouteLegs = contestRule.ruleValues.maxRouteLegs
        contestInstance.scGateWidth = contestRule.ruleValues.scGateWidth
        contestInstance.unsuitableStartNum = contestRule.ruleValues.unsuitableStartNum
        contestInstance.turnpointRule = contestRule.ruleValues.turnpointRule
        contestInstance.turnpointMapMeasurement = contestRule.ruleValues.turnpointMapMeasurement
        contestInstance.enroutePhotoRule = contestRule.ruleValues.enroutePhotoRule
        contestInstance.enrouteCanvasRule = contestRule.ruleValues.enrouteCanvasRule
        contestInstance.enrouteCanvasMultiple = contestRule.ruleValues.enrouteCanvasMultiple
        contestInstance.minEnroutePhotos = contestRule.ruleValues.minEnroutePhotos
        contestInstance.maxEnroutePhotos = contestRule.ruleValues.maxEnroutePhotos
        contestInstance.minEnrouteCanvas = contestRule.ruleValues.minEnrouteCanvas
        contestInstance.maxEnrouteCanvas = contestRule.ruleValues.maxEnrouteCanvas
        contestInstance.minEnrouteTargets = contestRule.ruleValues.minEnrouteTargets
        contestInstance.maxEnrouteTargets = contestRule.ruleValues.maxEnrouteTargets
        contestInstance.useProcedureTurns = contestRule.ruleValues.useProcedureTurns
        contestInstance.liveTrackingScorecard = contestRule.ruleValues.liveTrackingScorecard
        
        if (!newContest) {
            for (long route_id in routes_with_disabled_procedureturns) {
                Route route_instance = Route.get(route_id)
                route_instance.useProcedureTurns = false
                println "Disable procedure turn of '${route_instance.name()}'"
            }
        }
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    Map GetContestRulePointsValues(fromInstance)
    {
        Map values = [:]
        
        // General
        values += [ruleTitle:fromInstance.ruleTitle]
        values += [precisionFlying:fromInstance.precisionFlying]
        values += [increaseFactor:fromInstance.increaseFactor]
        values += [printLandingCalculatorValues:fromInstance.printLandingCalculatorValues]
        values += [printPointsGeneral:fromInstance.printPointsGeneral]
        values += [printPointsPlanningTest:fromInstance.printPointsPlanningTest]
        values += [printPointsFlightTest:fromInstance.printPointsFlightTest]
        values += [printPointsObservationTest:fromInstance.printPointsObservationTest]
        values += [printPointsLandingTest1:fromInstance.printPointsLandingTest1]
        values += [printPointsLandingTest2:fromInstance.printPointsLandingTest2]
        values += [printPointsLandingTest3:fromInstance.printPointsLandingTest3]
        values += [printPointsLandingTest4:fromInstance.printPointsLandingTest4]
        values += [printPointsLandingField:fromInstance.printPointsLandingField]
        values += [landingFieldImageName:fromInstance.landingFieldImageName]
        values += [printPointsTurnpointSign:fromInstance.printPointsTurnpointSign]
        values += [printPointsEnrouteCanvas:fromInstance.printPointsEnrouteCanvas]
        
        // PlanningTest
        values += [planningTestDirectionCorrectGrad:fromInstance.planningTestDirectionCorrectGrad]
        values += [planningTestDirectionPointsPerGrad:fromInstance.planningTestDirectionPointsPerGrad]
        values += [planningTestTimeCorrectSecond:fromInstance.planningTestTimeCorrectSecond]
        values += [planningTestTimePointsPerSecond:fromInstance.planningTestTimePointsPerSecond]
        values += [planningTestMaxPoints:fromInstance.planningTestMaxPoints]
        values += [planningTestPlanTooLatePoints:fromInstance.planningTestPlanTooLatePoints]
        values += [planningTestExitRoomTooLatePoints:fromInstance.planningTestExitRoomTooLatePoints]
        values += [planningTestForbiddenCalculatorsPoints:fromInstance.planningTestForbiddenCalculatorsPoints]
        
        // FlightTest
        values += [flightTestTakeoffMissedPoints:fromInstance.flightTestTakeoffMissedPoints]
        values += [flightTestTakeoffCorrectSecond:fromInstance.flightTestTakeoffCorrectSecond]
        values += [flightTestTakeoffCheckSeconds:fromInstance.flightTestTakeoffCheckSeconds]
        values += [flightTestTakeoffPointsPerSecond:fromInstance.flightTestTakeoffPointsPerSecond]
        values += [flightTestCptimeCorrectSecond:fromInstance.flightTestCptimeCorrectSecond]
        values += [flightTestCptimePointsPerSecond:fromInstance.flightTestCptimePointsPerSecond]
        values += [flightTestCptimeMaxPoints:fromInstance.flightTestCptimeMaxPoints]
        values += [flightTestCpNotFoundPoints:fromInstance.flightTestCpNotFoundPoints]
        values += [flightTestProcedureTurnNotFlownPoints:fromInstance.flightTestProcedureTurnNotFlownPoints]
        values += [flightTestMinAltitudeMissedPoints:fromInstance.flightTestMinAltitudeMissedPoints]
        values += [flightTestBadCourseCorrectSecond:fromInstance.flightTestBadCourseCorrectSecond]
        values += [flightTestBadCoursePoints:fromInstance.flightTestBadCoursePoints]
        values += [flightTestBadCourseMaxPoints:fromInstance.flightTestBadCourseMaxPoints]
        values += [flightTestBadCourseStartLandingPoints:fromInstance.flightTestBadCourseStartLandingPoints]
        values += [flightTestLandingToLatePoints:fromInstance.flightTestLandingToLatePoints]
        values += [flightTestGivenToLatePoints:fromInstance.flightTestGivenToLatePoints]
        values += [flightTestSafetyAndRulesInfringementPoints:fromInstance.flightTestSafetyAndRulesInfringementPoints]
        values += [flightTestInstructionsNotFollowedPoints:fromInstance.flightTestInstructionsNotFollowedPoints]
        values += [flightTestFalseEnvelopeOpenedPoints:fromInstance.flightTestFalseEnvelopeOpenedPoints]
        values += [flightTestSafetyEnvelopeOpenedPoints:fromInstance.flightTestSafetyEnvelopeOpenedPoints]
        values += [flightTestFrequencyNotMonitoredPoints:fromInstance.flightTestFrequencyNotMonitoredPoints]
        values += [flightTestForbiddenEquipmentPoints:fromInstance.flightTestForbiddenEquipmentPoints]
        
        // ObservationTest
        values += [observationTestEnrouteValueUnit:fromInstance.observationTestEnrouteValueUnit]
        values += [observationTestEnrouteCorrectValue:fromInstance.observationTestEnrouteCorrectValue]
        values += [observationTestEnrouteInexactValue:fromInstance.observationTestEnrouteInexactValue]
        values += [observationTestEnrouteInexactPoints:fromInstance.observationTestEnrouteInexactPoints]
        values += [observationTestEnrouteNotFoundPoints:fromInstance.observationTestEnrouteNotFoundPoints]
        values += [observationTestEnrouteFalsePoints:fromInstance.observationTestEnrouteFalsePoints]
        values += [observationTestTurnpointNotFoundPoints:fromInstance.observationTestTurnpointNotFoundPoints]
        values += [observationTestTurnpointFalsePoints:fromInstance.observationTestTurnpointFalsePoints]
                
        // LandingTest
        values += [landingTest1MaxPoints:fromInstance.landingTest1MaxPoints]
        values += [landingTest1NoLandingPoints:fromInstance.landingTest1NoLandingPoints]
        values += [landingTest1OutsideLandingPoints:fromInstance.landingTest1OutsideLandingPoints]
        values += [landingTest1RollingOutsidePoints:fromInstance.landingTest1RollingOutsidePoints]
        values += [landingTest1PowerInBoxPoints:fromInstance.landingTest1PowerInBoxPoints]
        values += [landingTest1GoAroundWithoutTouchingPoints:fromInstance.landingTest1GoAroundWithoutTouchingPoints]
        values += [landingTest1GoAroundInsteadStopPoints:fromInstance.landingTest1GoAroundInsteadStopPoints]
        values += [landingTest1AbnormalLandingPoints:fromInstance.landingTest1AbnormalLandingPoints]
        values += [landingTest1NotAllowedAerodynamicAuxiliariesPoints:fromInstance.landingTest1NotAllowedAerodynamicAuxiliariesPoints]
        values += [landingTest1PenaltyCalculator:fromInstance.landingTest1PenaltyCalculator]
    
        values += [landingTest2MaxPoints:fromInstance.landingTest2MaxPoints]
        values += [landingTest2NoLandingPoints:fromInstance.landingTest2NoLandingPoints]
        values += [landingTest2OutsideLandingPoints:fromInstance.landingTest2OutsideLandingPoints]
        values += [landingTest2RollingOutsidePoints:fromInstance.landingTest2RollingOutsidePoints]
        values += [landingTest2PowerInBoxPoints:fromInstance.landingTest2PowerInBoxPoints]
        values += [landingTest2GoAroundWithoutTouchingPoints:fromInstance.landingTest2GoAroundWithoutTouchingPoints]
        values += [landingTest2GoAroundInsteadStopPoints:fromInstance.landingTest2GoAroundInsteadStopPoints]
        values += [landingTest2AbnormalLandingPoints:fromInstance.landingTest2AbnormalLandingPoints]
        values += [landingTest2NotAllowedAerodynamicAuxiliariesPoints:fromInstance.landingTest2NotAllowedAerodynamicAuxiliariesPoints]
        values += [landingTest2PowerInAirPoints:fromInstance.landingTest2PowerInAirPoints]
        values += [landingTest2PenaltyCalculator:fromInstance.landingTest2PenaltyCalculator]
        
        values += [landingTest3MaxPoints:fromInstance.landingTest3MaxPoints]
        values += [landingTest3NoLandingPoints:fromInstance.landingTest3NoLandingPoints]
        values += [landingTest3OutsideLandingPoints:fromInstance.landingTest3OutsideLandingPoints]
        values += [landingTest3RollingOutsidePoints:fromInstance.landingTest3RollingOutsidePoints]
        values += [landingTest3PowerInBoxPoints:fromInstance.landingTest3PowerInBoxPoints]
        values += [landingTest3GoAroundWithoutTouchingPoints:fromInstance.landingTest3GoAroundWithoutTouchingPoints]
        values += [landingTest3GoAroundInsteadStopPoints:fromInstance.landingTest3GoAroundInsteadStopPoints]
        values += [landingTest3AbnormalLandingPoints:fromInstance.landingTest3AbnormalLandingPoints]
        values += [landingTest3NotAllowedAerodynamicAuxiliariesPoints:fromInstance.landingTest3NotAllowedAerodynamicAuxiliariesPoints]
        values += [landingTest3PowerInAirPoints:fromInstance.landingTest3PowerInAirPoints]
        values += [landingTest3FlapsInAirPoints:fromInstance.landingTest3FlapsInAirPoints]
        values += [landingTest3PenaltyCalculator:fromInstance.landingTest3PenaltyCalculator]
        
        values += [landingTest4MaxPoints:fromInstance.landingTest4MaxPoints]
        values += [landingTest4NoLandingPoints:fromInstance.landingTest4NoLandingPoints]
        values += [landingTest4OutsideLandingPoints:fromInstance.landingTest4OutsideLandingPoints]
        values += [landingTest4RollingOutsidePoints:fromInstance.landingTest4RollingOutsidePoints]
        values += [landingTest4PowerInBoxPoints:fromInstance.landingTest4PowerInBoxPoints]
        values += [landingTest4GoAroundWithoutTouchingPoints:fromInstance.landingTest4GoAroundWithoutTouchingPoints]
        values += [landingTest4GoAroundInsteadStopPoints:fromInstance.landingTest4GoAroundInsteadStopPoints]
        values += [landingTest4AbnormalLandingPoints:fromInstance.landingTest4AbnormalLandingPoints]
        values += [landingTest4NotAllowedAerodynamicAuxiliariesPoints:fromInstance.landingTest4NotAllowedAerodynamicAuxiliariesPoints]
        values += [landingTest4TouchingObstaclePoints:fromInstance.landingTest4TouchingObstaclePoints]
        values += [landingTest4PenaltyCalculator:fromInstance.landingTest4PenaltyCalculator]
        
        return values
    }
    
    //--------------------------------------------------------------------------
    Map GetContestRuleDefaultsValues(fromInstance)
    {
        Map values = [:]
        
        values += [flightPlanShowLegDistance:fromInstance.flightPlanShowLegDistance]
        values += [flightPlanShowTrueTrack:fromInstance.flightPlanShowTrueTrack]
        values += [flightPlanShowTrueHeading:fromInstance.flightPlanShowTrueHeading]
        values += [flightPlanShowGroundSpeed:fromInstance.flightPlanShowGroundSpeed]
        values += [flightPlanShowLocalTime:fromInstance.flightPlanShowLocalTime]
        values += [flightPlanShowElapsedTime:fromInstance.flightPlanShowElapsedTime]
        values += [flightTestSubmissionMinutes:fromInstance.flightTestSubmissionMinutes]
        values += [scGateWidth:fromInstance.scGateWidth]
        values += [unsuitableStartNum:fromInstance.unsuitableStartNum]
        values += [turnpointRule:fromInstance.turnpointRule]
        values += [turnpointMapMeasurement:fromInstance.turnpointMapMeasurement]
        values += [enroutePhotoRule:fromInstance.enroutePhotoRule]
        values += [enrouteCanvasRule:fromInstance.enrouteCanvasRule]
        values += [enrouteCanvasMultiple:fromInstance.enrouteCanvasMultiple]
        values += [minRouteLegs:fromInstance.minRouteLegs]
        values += [maxRouteLegs:fromInstance.maxRouteLegs]
        values += [minEnroutePhotos:fromInstance.minEnroutePhotos]
        values += [maxEnroutePhotos:fromInstance.maxEnroutePhotos]
        values += [minEnrouteCanvas:fromInstance.minEnrouteCanvas]
        values += [maxEnrouteCanvas:fromInstance.maxEnrouteCanvas]
        values += [minEnrouteTargets:fromInstance.minEnrouteTargets]
        values += [maxEnrouteTargets:fromInstance.maxEnrouteTargets]
        values += [useProcedureTurns:fromInstance.useProcedureTurns]
        values += [liveTrackingScorecard:fromInstance.liveTrackingScorecard]
        
        return values
    }
    
    //--------------------------------------------------------------------------
    boolean IsContestRulePointsValueModified(fromInstance, Map oldContestRuleValues)
    {
        // General
        if (fromInstance.increaseFactor != oldContestRuleValues.increaseFactor) {return true;}
        /*
        if (fromInstance.ruleTitle != oldContestRuleValues.ruleTitle) {return true;}
        if (fromInstance.precisionFlying != oldContestRuleValues.precisionFlying) {return true;}
        if (fromInstance.printLandingCalculatorValues != oldContestRuleValues.printLandingCalculatorValues) {return true;}
        if (fromInstance.printPointsGeneral != oldContestRuleValues.printPointsGeneral) {return true;}
        if (fromInstance.printPointsPlanningTest != oldContestRuleValues.printPointsPlanningTest) {return true;}
        if (fromInstance.printPointsFlightTest != oldContestRuleValues.printPointsFlightTest) {return true;}
        if (fromInstance.printPointsObservationTest != oldContestRuleValues.printPointsObservationTest) {return true;}
        if (fromInstance.printPointsLandingTest1 != oldContestRuleValues.printPointsLandingTest1) {return true;}
        if (fromInstance.printPointsLandingTest2 != oldContestRuleValues.printPointsLandingTest2) {return true;}
        if (fromInstance.printPointsLandingTest3 != oldContestRuleValues.printPointsLandingTest3) {return true;}
        if (fromInstance.printPointsLandingTest4 != oldContestRuleValues.printPointsLandingTest4) {return true;}
        if (fromInstance.printPointsLandingField != oldContestRuleValues.printPointsLandingField) {return true;}
        if (fromInstance.landingFieldImageName != oldContestRuleValues.landingFieldImageName) {return true;}
        if (fromInstance.printPointsTurnpointSign != oldContestRuleValues.printPointsTurnpointSign) {return true;}
        if (fromInstance.printPointsEnrouteCanvas != oldContestRuleValues.printPointsEnrouteCanvas) {return true;}
        */
        
        // PlanningTest
        if (fromInstance.planningTestDirectionCorrectGrad != oldContestRuleValues.planningTestDirectionCorrectGrad) {return true}
        if (fromInstance.planningTestDirectionPointsPerGrad != oldContestRuleValues.planningTestDirectionPointsPerGrad) {return true}
        if (fromInstance.planningTestTimeCorrectSecond != oldContestRuleValues.planningTestTimeCorrectSecond) {return true}
        if (fromInstance.planningTestTimePointsPerSecond != oldContestRuleValues.planningTestTimePointsPerSecond) {return true}
        if (fromInstance.planningTestMaxPoints != oldContestRuleValues.planningTestMaxPoints) {return true}
        if (fromInstance.planningTestPlanTooLatePoints != oldContestRuleValues.planningTestPlanTooLatePoints) {return true}
        if (fromInstance.planningTestExitRoomTooLatePoints != oldContestRuleValues.planningTestExitRoomTooLatePoints) {return true}
        if (fromInstance.planningTestForbiddenCalculatorsPoints != oldContestRuleValues.planningTestForbiddenCalculatorsPoints) {return true}
        
        // FlightTest
        if (fromInstance.flightTestTakeoffMissedPoints != oldContestRuleValues.flightTestTakeoffMissedPoints) {return true}
        if (fromInstance.flightTestTakeoffCorrectSecond != oldContestRuleValues.flightTestTakeoffCorrectSecond) {return true}
        if (fromInstance.flightTestTakeoffCheckSeconds != oldContestRuleValues.flightTestTakeoffCheckSeconds) {return true}
        if (fromInstance.flightTestTakeoffPointsPerSecond != oldContestRuleValues.flightTestTakeoffPointsPerSecond) {return true}
        if (fromInstance.flightTestCptimeCorrectSecond != oldContestRuleValues.flightTestCptimeCorrectSecond) {return true}
        if (fromInstance.flightTestCptimePointsPerSecond != oldContestRuleValues.flightTestCptimePointsPerSecond) {return true}
        if (fromInstance.flightTestCptimeMaxPoints != oldContestRuleValues.flightTestCptimeMaxPoints) {return true}
        if (fromInstance.flightTestCpNotFoundPoints != oldContestRuleValues.flightTestCpNotFoundPoints) {return true}
        if (fromInstance.flightTestProcedureTurnNotFlownPoints != oldContestRuleValues.flightTestProcedureTurnNotFlownPoints) {return true}
        if (fromInstance.flightTestMinAltitudeMissedPoints != oldContestRuleValues.flightTestMinAltitudeMissedPoints) {return true}
        if (fromInstance.flightTestBadCourseCorrectSecond != oldContestRuleValues.flightTestBadCourseCorrectSecond) {return true}
        if (fromInstance.flightTestBadCoursePoints != oldContestRuleValues.flightTestBadCoursePoints) {return true}
        if (fromInstance.flightTestBadCourseMaxPoints != oldContestRuleValues.flightTestBadCourseMaxPoints) {return true}
        if (fromInstance.flightTestBadCourseStartLandingPoints != oldContestRuleValues.flightTestBadCourseStartLandingPoints) {return true}
        if (fromInstance.flightTestLandingToLatePoints != oldContestRuleValues.flightTestLandingToLatePoints) {return true}
        if (fromInstance.flightTestGivenToLatePoints != oldContestRuleValues.flightTestGivenToLatePoints) {return true}
        if (fromInstance.flightTestSafetyAndRulesInfringementPoints != oldContestRuleValues.flightTestSafetyAndRulesInfringementPoints) {return true}
        if (fromInstance.flightTestInstructionsNotFollowedPoints != oldContestRuleValues.flightTestInstructionsNotFollowedPoints) {return true}
        if (fromInstance.flightTestFalseEnvelopeOpenedPoints != oldContestRuleValues.flightTestFalseEnvelopeOpenedPoints) {return true}
        if (fromInstance.flightTestSafetyEnvelopeOpenedPoints != oldContestRuleValues.flightTestSafetyEnvelopeOpenedPoints) {return true}
        if (fromInstance.flightTestFrequencyNotMonitoredPoints != oldContestRuleValues.flightTestFrequencyNotMonitoredPoints) {return true}
        if (fromInstance.flightTestForbiddenEquipmentPoints != oldContestRuleValues.flightTestForbiddenEquipmentPoints) {return true}
        
        // ObservationTest
        if (fromInstance.observationTestEnrouteValueUnit != oldContestRuleValues.observationTestEnrouteValueUnit) {return true}
        if (fromInstance.observationTestEnrouteCorrectValue != oldContestRuleValues.observationTestEnrouteCorrectValue) {return true}
        if (fromInstance.observationTestEnrouteInexactValue != oldContestRuleValues.observationTestEnrouteInexactValue) {return true}
        if (fromInstance.observationTestEnrouteInexactPoints != oldContestRuleValues.observationTestEnrouteInexactPoints) {return true}
        if (fromInstance.observationTestEnrouteNotFoundPoints != oldContestRuleValues.observationTestEnrouteNotFoundPoints) {return true}
        if (fromInstance.observationTestEnrouteFalsePoints != oldContestRuleValues.observationTestEnrouteFalsePoints) {return true}
        if (fromInstance.observationTestTurnpointNotFoundPoints != oldContestRuleValues.observationTestTurnpointNotFoundPoints) {return true}
        if (fromInstance.observationTestTurnpointFalsePoints != oldContestRuleValues.observationTestTurnpointFalsePoints) {return true}
                
        // LandingTest
        if (fromInstance.landingTest1MaxPoints != oldContestRuleValues.landingTest1MaxPoints) {return true}
        if (fromInstance.landingTest1NoLandingPoints != oldContestRuleValues.landingTest1NoLandingPoints) {return true}
        if (fromInstance.landingTest1OutsideLandingPoints != oldContestRuleValues.landingTest1OutsideLandingPoints) {return true}
        if (fromInstance.landingTest1RollingOutsidePoints != oldContestRuleValues.landingTest1RollingOutsidePoints) {return true}
        if (fromInstance.landingTest1PowerInBoxPoints != oldContestRuleValues.landingTest1PowerInBoxPoints) {return true}
        if (fromInstance.landingTest1GoAroundWithoutTouchingPoints != oldContestRuleValues.landingTest1GoAroundWithoutTouchingPoints) {return true}
        if (fromInstance.landingTest1GoAroundInsteadStopPoints != oldContestRuleValues.landingTest1GoAroundInsteadStopPoints) {return true}
        if (fromInstance.landingTest1AbnormalLandingPoints != oldContestRuleValues.landingTest1AbnormalLandingPoints) {return true}
        if (fromInstance.landingTest1NotAllowedAerodynamicAuxiliariesPoints != oldContestRuleValues.landingTest1NotAllowedAerodynamicAuxiliariesPoints) {return true}
        if (fromInstance.landingTest1PenaltyCalculator != oldContestRuleValues.landingTest1PenaltyCalculator) {return true}

        if (fromInstance.landingTest2MaxPoints != oldContestRuleValues.landingTest2MaxPoints) {return true}
        if (fromInstance.landingTest2NoLandingPoints != oldContestRuleValues.landingTest2NoLandingPoints) {return true}
        if (fromInstance.landingTest2OutsideLandingPoints != oldContestRuleValues.landingTest2OutsideLandingPoints) {return true}
        if (fromInstance.landingTest2RollingOutsidePoints != oldContestRuleValues.landingTest2RollingOutsidePoints) {return true}
        if (fromInstance.landingTest2PowerInBoxPoints != oldContestRuleValues.landingTest2PowerInBoxPoints) {return true}
        if (fromInstance.landingTest2GoAroundWithoutTouchingPoints != oldContestRuleValues.landingTest2GoAroundWithoutTouchingPoints) {return true}
        if (fromInstance.landingTest2GoAroundInsteadStopPoints != oldContestRuleValues.landingTest2GoAroundInsteadStopPoints) {return true}
        if (fromInstance.landingTest2AbnormalLandingPoints != oldContestRuleValues.landingTest2AbnormalLandingPoints) {return true}
        if (fromInstance.landingTest2NotAllowedAerodynamicAuxiliariesPoints != oldContestRuleValues.landingTest2NotAllowedAerodynamicAuxiliariesPoints) {return true}
        if (fromInstance.landingTest2PowerInAirPoints != oldContestRuleValues.landingTest2PowerInAirPoints) {return true}
        if (fromInstance.landingTest2PenaltyCalculator != oldContestRuleValues.landingTest2PenaltyCalculator) {return true}

        if (fromInstance.landingTest3MaxPoints != oldContestRuleValues.landingTest3MaxPoints) {return true}
        if (fromInstance.landingTest3NoLandingPoints != oldContestRuleValues.landingTest3NoLandingPoints) {return true}
        if (fromInstance.landingTest3OutsideLandingPoints != oldContestRuleValues.landingTest3OutsideLandingPoints) {return true}
        if (fromInstance.landingTest3RollingOutsidePoints != oldContestRuleValues.landingTest3RollingOutsidePoints) {return true}
        if (fromInstance.landingTest3PowerInBoxPoints != oldContestRuleValues.landingTest3PowerInBoxPoints) {return true}
        if (fromInstance.landingTest3GoAroundWithoutTouchingPoints != oldContestRuleValues.landingTest3GoAroundWithoutTouchingPoints) {return true}
        if (fromInstance.landingTest3GoAroundInsteadStopPoints != oldContestRuleValues.landingTest3GoAroundInsteadStopPoints) {return true}
        if (fromInstance.landingTest3AbnormalLandingPoints != oldContestRuleValues.landingTest3AbnormalLandingPoints) {return true}
        if (fromInstance.landingTest3NotAllowedAerodynamicAuxiliariesPoints != oldContestRuleValues.landingTest3NotAllowedAerodynamicAuxiliariesPoints) {return true}
        if (fromInstance.landingTest3PowerInAirPoints != oldContestRuleValues.landingTest3PowerInAirPoints) {return true}
        if (fromInstance.landingTest3FlapsInAirPoints != oldContestRuleValues.landingTest3FlapsInAirPoints) {return true}
        if (fromInstance.landingTest3PenaltyCalculator != oldContestRuleValues.landingTest3PenaltyCalculator) {return true}

        if (fromInstance.landingTest4MaxPoints != oldContestRuleValues.landingTest4MaxPoints) {return true}
        if (fromInstance.landingTest4NoLandingPoints != oldContestRuleValues.landingTest4NoLandingPoints) {return true}
        if (fromInstance.landingTest4OutsideLandingPoints != oldContestRuleValues.landingTest4OutsideLandingPoints) {return true}
        if (fromInstance.landingTest4RollingOutsidePoints != oldContestRuleValues.landingTest4RollingOutsidePoints) {return true}
        if (fromInstance.landingTest4PowerInBoxPoints != oldContestRuleValues.landingTest4PowerInBoxPoints) {return true}
        if (fromInstance.landingTest4GoAroundWithoutTouchingPoints != oldContestRuleValues.landingTest4GoAroundWithoutTouchingPoints) {return true}
        if (fromInstance.landingTest4GoAroundInsteadStopPoints != oldContestRuleValues.landingTest4GoAroundInsteadStopPoints) {return true}
        if (fromInstance.landingTest4AbnormalLandingPoints != oldContestRuleValues.landingTest4AbnormalLandingPoints) {return true}
        if (fromInstance.landingTest4NotAllowedAerodynamicAuxiliariesPoints != oldContestRuleValues.landingTest4NotAllowedAerodynamicAuxiliariesPoints) {return true}
        if (fromInstance.landingTest4TouchingObstaclePoints != oldContestRuleValues.landingTest4TouchingObstaclePoints) {return true}
        if (fromInstance.landingTest4PenaltyCalculator != oldContestRuleValues.landingTest4PenaltyCalculator) {return true}
        
        return false
    }
    
    //--------------------------------------------------------------------------
    boolean IsContestRuleDefaultsValueModified(fromInstance, Map oldContestRuleValues)
    {
        if (fromInstance.minRouteLegs != oldContestRuleValues.minRouteLegs) {return true}
        if (fromInstance.maxRouteLegs != oldContestRuleValues.maxRouteLegs) {return true}
        if (fromInstance.scGateWidth != oldContestRuleValues.scGateWidth) {return true}
        if (fromInstance.unsuitableStartNum != oldContestRuleValues.unsuitableStartNum) {return true}
        if (fromInstance.turnpointRule != oldContestRuleValues.turnpointRule) {return true}
        if (fromInstance.turnpointMapMeasurement != oldContestRuleValues.turnpointMapMeasurement) {return true}
        if (fromInstance.enroutePhotoRule != oldContestRuleValues.enroutePhotoRule) {return true}
        if (fromInstance.enrouteCanvasRule != oldContestRuleValues.enrouteCanvasRule) {return true}
        if (fromInstance.enrouteCanvasMultiple != oldContestRuleValues.enrouteCanvasMultiple) {return true}
        if (fromInstance.minEnroutePhotos != oldContestRuleValues.minEnroutePhotos) {return true}
        if (fromInstance.maxEnroutePhotos != oldContestRuleValues.maxEnroutePhotos) {return true}
        if (fromInstance.minEnrouteCanvas != oldContestRuleValues.minEnrouteCanvas) {return true}
        if (fromInstance.maxEnrouteCanvas != oldContestRuleValues.maxEnrouteCanvas) {return true}
        if (fromInstance.minEnrouteTargets != oldContestRuleValues.minEnrouteTargets) {return true}
        if (fromInstance.maxEnrouteTargets != oldContestRuleValues.maxEnrouteTargets) {return true}
        if (fromInstance.useProcedureTurns != oldContestRuleValues.useProcedureTurns) {return true}
        if (fromInstance.liveTrackingScorecard != oldContestRuleValues.liveTrackingScorecard) {return true}
        
        return false
    }
    
    //--------------------------------------------------------------------------
    Map copyContest(Map params, Contest lastContestInstance)
    {
        Contest contest_instance = new Contest(params)
		contest_instance.copyContestSettings = false
		if (params?.copyContestSettings) {
			contest_instance.copyContestSettings = true
		}
		contest_instance.copyRoutes = false
		if (params?.copyRoutes) {
			contest_instance.copyRoutes = true
		}
		contest_instance.copyCrews = false
		if (params?.copyCrews) {
			contest_instance.copyCrews = true
		}
		contest_instance.copyTaskSettings = false
		if (params?.copyTaskSettings) {
			contest_instance.copyTaskSettings = true
		}
		contest_instance.CopyValues(lastContestInstance)
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteContest(Map params)
    {
        printstart "deleteContest"
        Contest contest_instance = Contest.get(params.id)
        if (contest_instance) {
            if (BootStrap.global.liveContestID == contest_instance.id) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted.live',[getMsg('fc.contest'),params.id])]
            }
            try {
            	Task.findAllByContest(contest_instance,[sort:"idTitle"]).each { Task task_instance ->
            		task_instance.delete()
            	}
                contest_instance.delete()
                
                // BUG: siehe CoordTitle belongsTo, auf Grund eines Bugs notwendiges Aufräumen überflüssiger CoordTitle-Objekte, DB-2.12
                if ((Contest.count() == 0) && CoordTitle.count() > 0) {
                    CoordTitle.findAll().each { CoordTitle coord_title ->
                        coord_title.delete()
                    }
                    println "Undeleted CoordTitle removed."
                }
                
                printdone ""
                return ['deleted':true,'message':getMsg('fc.deleted',["${contest_instance.title}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                printerror ""
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.contest'),params.id])]
            }
        } else {
            printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map anonymizeContest(Map params)
    {
        printstart "anonymizeContest"
        Contest contest_instance = Contest.get(params.id)
        if (contest_instance) {
            Crew crew_with_max_startnum = Crew.findByContest(contest_instance,[sort:'startNum', order:'desc'])
            String format_value = "0"
            if (crew_with_max_startnum) {
                for (int i = 1; i < crew_with_max_startnum.startNum.toString().size(); i++) {
                    format_value += "0"
                }
            }
            DecimalFormat df = new DecimalFormat(format_value)
            int i = 0
            for (Crew crew_instance in Crew.findAllByContest(contest_instance,[sort:'viewpos'])) {
                i++
                crew_instance.name = anonymize_crew(crew_instance.name, df.format(i), contest_instance)
                crew_instance.email = ""
                crew_instance.save()
            }
            i = 0
            for (Aircraft aircraft_instance in Aircraft.findAllByContest(contest_instance,[sort:'registration'])) {
                i++
                aircraft_instance.registration = "Aircraft-${df.format(i)}"
                aircraft_instance.type = ""
                aircraft_instance.colour = ""
                aircraft_instance.save()
            }
            i = 0
            for (Team team_instance in Team.findAllByContest(contest_instance,[sort:'name'])) {
                i++
                team_instance.name = "Team-${df.format(i)}"
                team_instance.save()
            }
            for (Task task_instance in Task.findAllByContest(contest_instance,[sort:"idTitle"])) {
                for (Test test_instance in Test.findAllByTask(task_instance,[sort:"id"])) {
                    test_instance.scannedPlanningTest = null
                    test_instance.scannedObservationTest = null
					delete_uploadjobtest(test_instance)
                    test_instance.save()
                }
            }
            printdone ""
            return ['anonymized':true,'message':getMsg('fc.anonymized',["${contest_instance.title}"])]
        } else {
            printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private String anonymize_crew(String crewName, String crewNumber, Contest contestInstance)
    {
        String pilot_firstname = "PilotFirst"
        String pilot_lastname = "PilotLast"
        String navigator_firstname = "NavFirst"
        String navigator_lastname = "NavLast"
        if (contestInstance.crewPilotNavigatorDelimiter) {
            if (contestInstance.crewSurnameForenameDelimiter) {
                return "${pilot_lastname}-${crewNumber}${contestInstance.crewSurnameForenameDelimiter} ${pilot_firstname}-${crewNumber}${contestInstance.crewPilotNavigatorDelimiter} ${navigator_lastname}-${crewNumber}${contestInstance.crewSurnameForenameDelimiter} ${navigator_firstname}-${crewNumber}"
            }
            return "${pilot_firstname}-${crewNumber} ${pilot_lastname}-${crewNumber}${contestInstance.crewPilotNavigatorDelimiter} ${navigator_firstname}-${crewNumber} ${navigator_lastname}-${crewNumber}"
        } else if (contestInstance.crewSurnameForenameDelimiter) { // no navigator
            return "${pilot_lastname}-${crewNumber}${contestInstance.crewSurnameForenameDelimiter} ${pilot_firstname}-${crewNumber}"
        }
        return "${pilot_firstname}-${crewNumber} ${pilot_lastname}-${crewNumber}"
    }
    
    //--------------------------------------------------------------------------
    void setaflosuploadedContest(Contest contestInstance)
    {
		contestInstance.aflosUpload = true
		contestInstance.save()
	}

    //--------------------------------------------------------------------------
    Map copyTask(Map params, Contest contestInstance)
    {
		printstart "copyTask"
        Task task_instance = Task.get(params.id)

        if (!task_instance) {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
			printerror ret
			return ret
        }
        
		Task new_task_instance = new Task()
		new_task_instance.contest = contestInstance
		Map taskclass_settings = new_task_instance.CopyValues2(task_instance)
		printdone ""
        return ['instance':new_task_instance,'taskclass_settings':taskclass_settings]
    }

    //--------------------------------------------------------------------------
    Map gettimetableprintableTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        if (!task_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
		
		println "gettimetableprintableTask (${task_instance.printName()})"
		
		// Calculate timetable version
		if (task_instance.timetableModified) {
			task_instance.timetableVersion++
			task_instance.timetableModified = false
			task_instance.save()
			println "gettimetableprintableTask: timetableVersion $task_instance.timetableVersion of '${task_instance.printName()}' saved."
		}

        return ['instance':task_instance]
    }

    //--------------------------------------------------------------------------
    Map updateTask(Map params)
    {
		printstart "updateTask"
		
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':task_instance]
                }
            }
            
			boolean firsttime_modified = false
			if (is_modified(params.firstTime,task_instance.firstTime)) {
				firsttime_modified = true
			}
			if (is_modified(params.takeoffIntervalNormal,task_instance.takeoffIntervalNormal)) {
				firsttime_modified = true
			}
			if (is_modified(params.takeoffIntervalSlowerAircraft,task_instance.takeoffIntervalSlowerAircraft)) {
				firsttime_modified = true
			}
			if (is_modified(params.takeoffIntervalFasterAircraft,task_instance.takeoffIntervalFasterAircraft)) {
				firsttime_modified = true
			}

			boolean duration_modified = false
			if (is_modified(params.planningTestDuration,task_instance.planningTestDuration)) {
				duration_modified = true
			}
			if (is_modified(params.preparationDuration,task_instance.preparationDuration)) {
				duration_modified = true
			}
			if (is_modified(params.parkingDuration,task_instance.parkingDuration)) {
				duration_modified = true
			}
			if (is_modified(params.procedureTurnDuration,task_instance.procedureTurnDuration)) {
				duration_modified = true
			}

			boolean nextflightduration_modified = false
			if (is_modified(params.minNextFlightDuration,task_instance.minNextFlightDuration)) {
				nextflightduration_modified = true
			}

			boolean calculate_penalties = false
			boolean recalculate_penalties = false
			if (!task_instance.contest.resultClasses) {
				if (is_modified(params.planningTestRun,task_instance.planningTestRun)) {
					calculate_penalties = true
				}
				if (is_modified(params.flightTestRun,task_instance.flightTestRun)) {
					calculate_penalties = true
				}
				if (is_modified(params.flightTestCheckSecretPoints,task_instance.flightTestCheckSecretPoints)) {
					calculate_penalties = true
				}
				if (is_modified(params.flightTestCheckTakeOff,task_instance.flightTestCheckTakeOff)) {
					calculate_penalties = true
					recalculate_penalties = true
				}
				if (is_modified(params.flightTestCheckLanding,task_instance.flightTestCheckLanding)) {
					calculate_penalties = true
					recalculate_penalties = true
				}
				if (is_modified(params.observationTestRun,task_instance.observationTestRun)) {
					calculate_penalties = true
				}
                if (is_modified(params.observationTestTurnpointRun,task_instance.observationTestTurnpointRun)) {
                    calculate_penalties = true
                }
                if (is_modified(params.observationTestEnroutePhotoRun,task_instance.observationTestEnroutePhotoRun)) {
                    calculate_penalties = true
                }
                if (is_modified(params.observationTestEnrouteCanvasRun,task_instance.observationTestEnrouteCanvasRun)) {
                    calculate_penalties = true
                }
				if (is_modified(params.landingTestRun,task_instance.landingTestRun)) {
					calculate_penalties = true
				}
				if (is_modified(params.landingTest1Run,task_instance.landingTest1Run)) {
					calculate_penalties = true
				}
				if (is_modified(params.landingTest2Run,task_instance.landingTest2Run)) {
					calculate_penalties = true
				}
				if (is_modified(params.landingTest3Run,task_instance.landingTest3Run)) {
					calculate_penalties = true
				}
				if (is_modified(params.landingTest4Run,task_instance.landingTest4Run)) {
					calculate_penalties = true
				}
                if (is_modified(params.landingTest1Points,task_instance.landingTest1Points)) {
                    calculate_penalties = true
                }
                if (is_modified(params.landingTest2Points,task_instance.landingTest2Points)) {
                    calculate_penalties = true
                }
                if (is_modified(params.landingTest3Points,task_instance.landingTest3Points)) {
                    calculate_penalties = true
                }
                if (is_modified(params.landingTest4Points,task_instance.landingTest4Points)) {
                    calculate_penalties = true
                }
				if (is_modified(params.specialTestRun,task_instance.specialTestRun)) {
					calculate_penalties = true
				}			
			}
			if (is_modified(params.bestOfAnalysis,task_instance.bestOfAnalysis)) {
				calculate_penalties = true
			}
            if (task_instance.GetIncreaseValues() != "") {
                if (is_modified(params.increaseEnabled,task_instance.increaseEnabled)) {
                    calculate_penalties = true
                }
            }

            task_instance.properties = params
			
			// save TaskClasses
			if (task_instance.contest.resultClasses) {
				for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance,[sort:"id"])) {
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_PlanningTestRun}"]) {
						taskclass_instance.planningTestRun = true
					} else {
						taskclass_instance.planningTestRun = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestRun}"]) {
						taskclass_instance.flightTestRun = true
					} else {
						taskclass_instance.flightTestRun = false
					}
                    if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestCheckSecretPoints}"]) {
                        taskclass_instance.flightTestCheckSecretPoints = true
                    } else { 
                        taskclass_instance.flightTestCheckSecretPoints = false
                    }
                    if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestCheckTakeOff}"]) {
                        taskclass_instance.flightTestCheckTakeOff = true
                    } else { 
                        taskclass_instance.flightTestCheckTakeOff = false
                    }
                    if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestCheckLanding}"]) {
                        taskclass_instance.flightTestCheckLanding = true
                    } else { 
                        taskclass_instance.flightTestCheckLanding = false
                    }
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestRun}"]) {
						taskclass_instance.observationTestRun = true
					} else {
						taskclass_instance.observationTestRun = false
					}
                    if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestTurnpointRun}"]) {
                        taskclass_instance.observationTestTurnpointRun = true
                    } else {
                        taskclass_instance.observationTestTurnpointRun = false
                    }
                    if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestEnroutePhotoRun}"]) {
                        taskclass_instance.observationTestEnroutePhotoRun = true
                    } else {
                        taskclass_instance.observationTestEnroutePhotoRun = false
                    }
                    if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestEnrouteCanvasRun}"]) {
                        taskclass_instance.observationTestEnrouteCanvasRun = true
                    } else {
                        taskclass_instance.observationTestEnrouteCanvasRun = false
                    }
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTestRun}"]) {
						taskclass_instance.landingTestRun = true
					} else {
						taskclass_instance.landingTestRun = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest1Run}"]) {
						taskclass_instance.landingTest1Run = true
					} else {
						taskclass_instance.landingTest1Run = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest2Run}"]) {
						taskclass_instance.landingTest2Run = true
					} else {
						taskclass_instance.landingTest2Run = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest3Run}"]) {
						taskclass_instance.landingTest3Run = true
					} else {
						taskclass_instance.landingTest3Run = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest4Run}"]) {
						taskclass_instance.landingTest4Run = true
					} else {
						taskclass_instance.landingTest4Run = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_SpecialTestRun}"]) {
						taskclass_instance.specialTestRun = true
					} else { 
						taskclass_instance.specialTestRun = false
					}
					if (taskclass_instance.isDirty()) {
						calculate_penalties = true
					}
					
					// parameter without penalties
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_PlanningTestDistanceMeasure}"]) {
						taskclass_instance.planningTestDistanceMeasure = true
					} else { 
						taskclass_instance.planningTestDistanceMeasure = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_PlanningTestDirectionMeasure}"]) {
						taskclass_instance.planningTestDirectionMeasure = true
					} else { 
						taskclass_instance.planningTestDirectionMeasure = false
					}
					if (params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_SpecialTestTitle}"] != null) {
						taskclass_instance.specialTestTitle = params["${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_SpecialTestTitle}"]
					}
					taskclass_instance.save()
				}
			}
			
			if (calculate_penalties) {
				println "Calculate penalties"
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
					calculateTestPenalties(test_instance,recalculate_penalties)
                    test_instance.flightTestLink = ""
					delete_uploadjobtest(test_instance)
					test_instance.crewResultsModified = true
		            test_instance.save()
		        }
			}
			
            if(!task_instance.hasErrors() && task_instance.save()) {
				try {
					if (firsttime_modified) {
						println "First time modified."
						reset_task_timecalculated(task_instance)
					}
					
					if (duration_modified) {
						println "Duration modified."
						calculate_task_time(task_instance, true)
					} else if (nextflightduration_modified) {
						println "Next flight duration modified."
						calculate_task_time(task_instance, false)
					}
					
	                Map ret = ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
					printdone ret
					return ret
				} catch (Exception e) {
					Map ret = ['instance':task_instance,'error':true,'message':e.getMessage()]
					printerror ret
					return ret
				}
            } else {
				printerror ""
                return ['instance':task_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
			printerror ret
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateprintsettingsTask(Map params, PrintSettings printSettings)
    {
		printstart "updateprintsettingsTask $printSettings"
		
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':task_instance]
                }
            }
            
			String settings_name = ""
			String detail_name = getMsg('fc.setprintsettings.modifications')
			
			switch (printSettings) {
                case PrintSettings.TimetableOverviewModified:
                    settings_name = getMsg('fc.task.timetableoverview')
                    task_instance.properties = params
                    task_instance.briefingTime = params.briefingTime
                    if (params["printTimetableOverviewLegTimes"]) {
                        task_instance.printTimetableOverviewLegTimes = params.printTimetableOverviewLegTimes == "on"
                    }
                    if (params["printTimetableOverviewLandscape"]) {
                        task_instance.printTimetableOverviewLandscape = params.printTimetableOverviewLandscape == "on"
                    }
                    if (params["printTimetableOverviewA3"]) {
                        task_instance.printTimetableOverviewA3 = params.printTimetableOverviewA3 == "on"
                    }
                    break
				case PrintSettings.TimetableJuryModified:
					settings_name = getMsg('fc.task.timetablejudge')
		            task_instance.properties = params
					task_instance.printTimetableJuryCheckPoints = ""
		            Test test_instance = Test.findByTask(task_instance)
					if (test_instance) {
						int leg_no = 0
						int leg_num = TestLegFlight.countByTest(test_instance)
						String leg_name = ""
						for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(test_instance,[sort:"id"])) {
							leg_no++
							leg_name = testlegflight_instance.coordTitle.titleCode()
			                if (leg_no==leg_num) {
			                } else {
								if (params[leg_name] == "on") {
									if (task_instance.printTimetableJuryCheckPoints) {
										task_instance.printTimetableJuryCheckPoints += ","
									}
									task_instance.printTimetableJuryCheckPoints += "${leg_no}"
								}
			                }
						}
					}
					break
				case PrintSettings.TimetableModified:
					settings_name = getMsg('fc.task.timetable')
		            task_instance.properties = params
					break
				case PrintSettings.TimetableJuryStandard:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.standard')
					task_instance.printTimetableJuryPrintTitle = ""
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = true
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = true
					task_instance.printTimetableJuryPlanning = true
					task_instance.printTimetableJuryPlanningEnd = true
					task_instance.printTimetableJuryTakeoff = true
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = true
					task_instance.printTimetableJuryArrival = true
                    task_instance.printTimetableJurySubmission = true
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = ""
					task_instance.printTimetableJuryEmptyColumn2 = false
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = true
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryNone:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.setprintsettings.none')
					task_instance.printTimetableJuryPrintTitle = ""
					task_instance.printTimetableJuryNumber = false
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = false
					task_instance.printTimetableJuryAircraftType = false
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = false
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = false
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = false
					task_instance.printTimetableJuryEmptyTitle1 = ""
					task_instance.printTimetableJuryEmptyColumn2 = false
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryAll:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.setprintsettings.all')
					task_instance.printTimetableJuryPrintTitle = ""
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = true
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = true
					task_instance.printTimetableJuryTAS = true
					task_instance.printTimetableJuryTeam = true
					task_instance.printTimetableJuryClass = true
					task_instance.printTimetableJuryShortClass = true
					task_instance.printTimetableJuryPlanning = true
					task_instance.printTimetableJuryPlanningEnd = true
					task_instance.printTimetableJuryTakeoff = true
					task_instance.printTimetableJuryStartPoint = true
					task_instance.printTimetableJuryCheckPoints = ""
		            Test test_instance = Test.findByTask(task_instance)
					if (test_instance) {
						int leg_no = 0
						int leg_num = TestLegFlight.countByTest(test_instance)
						for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(test_instance,[sort:"id"])) {
							leg_no++
			                if (leg_no!=leg_num) {
								if (task_instance.printTimetableJuryCheckPoints) {
									task_instance.printTimetableJuryCheckPoints += ","
								}
								task_instance.printTimetableJuryCheckPoints += "${leg_no}"
			                }
						}
					}
					task_instance.printTimetableJuryFinishPoint = true
					task_instance.printTimetableJuryLanding = true
					task_instance.printTimetableJuryArrival = true
                    task_instance.printTimetableJurySubmission = true
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = ""
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = true
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = true
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = true
					task_instance.printTimetableJuryLandscape = true
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryTower:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.setprintsettings.tower')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.tower')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = true
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = true
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = ""
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryIntermediateTower:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.setprintsettings.tower.intermediate')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.tower.intermediate')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = false
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
		            Test test_instance = Test.findByTask(task_instance)
					if (test_instance) {
						int leg_no = 0
						int leg_num = TestLegFlight.countByTest(test_instance)
						for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(test_instance,[sort:"id"])) {
							leg_no++
			                if (leg_no!=leg_num) {
								switch (testlegflight_instance.coordTitle.type) {
									case CoordType.iLDG:
									case CoordType.iTO:
										if (task_instance.printTimetableJuryCheckPoints) {
											task_instance.printTimetableJuryCheckPoints += ","
										}
										task_instance.printTimetableJuryCheckPoints += "${leg_no}"
										break
								}
			                }
						}
					}
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = false
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = ""
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryPlanning:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.planningtest.setprintsettings')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.planningtest')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = true
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = false
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = true
					task_instance.printTimetableJuryPlanning = true
					task_instance.printTimetableJuryPlanningEnd = true
					task_instance.printTimetableJuryTakeoff = false
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = false
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = getPrintMsg('fc.test.planning.solutiongiven')
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = getPrintMsg('fc.test.planning.exitroom')
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryDocumentsOutput:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.flighttest.documentsoutput.setprintsettings')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.flighttest.documentsoutput')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = true
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = false
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = true
					task_instance.printTimetableJuryPlanning = true
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = true
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = false
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = ""
					task_instance.printTimetableJuryEmptyColumn2 = false
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryTakeoff:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.flighttest.takeoff.setprintsettings')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.takeoff')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = true
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = false
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = "" // getPrintMsg('fc.test.takeoff.real')
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = false
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryLanding:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.landingtest.setprintsettings')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.landing')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = false
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = true
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = getPrintMsg('fc.test.landing.field') // getPrintMsg('fc.test.landing.real')
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = true
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = true
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = true
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryIntermediateLanding:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.landingtest.setprintsettings.intermediate')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.landing.intermediate')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = false
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
		            Test test_instance = Test.findByTask(task_instance)
					if (test_instance) {
						int leg_no = 0
						int leg_num = TestLegFlight.countByTest(test_instance)
						for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(test_instance,[sort:"id"])) {
							leg_no++
			                if (leg_no!=leg_num) {
								if (testlegflight_instance.coordTitle.type == CoordType.iLDG) {
									if (task_instance.printTimetableJuryCheckPoints) {
										task_instance.printTimetableJuryCheckPoints += ","
									}
									task_instance.printTimetableJuryCheckPoints += "${leg_no}"
								}
			                }
						}
					}
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = false
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = false
					task_instance.printTimetableJuryEmptyColumn1 = true
					task_instance.printTimetableJuryEmptyTitle1 = getPrintMsg('fc.test.landing.field') // getPrintMsg('fc.test.landing.real')
					task_instance.printTimetableJuryEmptyColumn2 = true
					task_instance.printTimetableJuryEmptyTitle2 = ""
					task_instance.printTimetableJuryEmptyColumn3 = true
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = true
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = true
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
				case PrintSettings.TimetableJuryArrival:
					settings_name = getMsg('fc.task.timetablejudge')
					detail_name = getMsg('fc.flighttest.arrival.setprintsettings')
					task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.arrival')
					task_instance.printTimetableJuryNumber = true
					task_instance.printTimetableJuryCrew = false
					task_instance.printTimetableJuryAircraft = true
					task_instance.printTimetableJuryAircraftType = true
					task_instance.printTimetableJuryAircraftColour = false
					task_instance.printTimetableJuryTAS = false
					task_instance.printTimetableJuryTeam = false
					task_instance.printTimetableJuryClass = false
					task_instance.printTimetableJuryShortClass = false
					task_instance.printTimetableJuryPlanning = false
					task_instance.printTimetableJuryPlanningEnd = false
					task_instance.printTimetableJuryTakeoff = false
					task_instance.printTimetableJuryStartPoint = false
					task_instance.printTimetableJuryCheckPoints = ""
					task_instance.printTimetableJuryFinishPoint = false
					task_instance.printTimetableJuryLanding = true
					task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = true
					task_instance.printTimetableJuryEmptyColumn1 = true
                    if (task_instance.flighttest.submissionMinutes) {
                        task_instance.printTimetableJuryEmptyTitle1 = getPrintMsg('fc.test.arrival.givingtime')
                    } else {
                        task_instance.printTimetableJuryEmptyTitle1 = getPrintMsg('fc.test.arrival.stoptime')
                    }
					task_instance.printTimetableJuryEmptyColumn2 = true
                    if (task_instance.flighttest.submissionMinutes) {
                        task_instance.printTimetableJuryEmptyTitle2 = ""
                    } else {
                        task_instance.printTimetableJuryEmptyTitle2 = getPrintMsg('fc.test.arrival.givingtime')
                    }
					task_instance.printTimetableJuryEmptyColumn3 = true
					task_instance.printTimetableJuryEmptyTitle3 = ""
					task_instance.printTimetableJuryEmptyColumn4 = false
					task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
					task_instance.printTimetableJuryLandscape = false
					task_instance.printTimetableJuryA3 = false
					break
                case PrintSettings.TimetableJuryDebriefing:
                    settings_name = getMsg('fc.task.timetablejudge')
                    detail_name = getMsg('fc.flighttest.debriefing.setprintsettings')
                    task_instance.printTimetableJuryPrintTitle = getPrintMsg('fc.test.debriefing')
                    task_instance.printTimetableJuryNumber = true
                    task_instance.printTimetableJuryCrew = true
                    task_instance.printTimetableJuryAircraft = true
                    task_instance.printTimetableJuryAircraftType = false
                    task_instance.printTimetableJuryAircraftColour = false
                    task_instance.printTimetableJuryTAS = false
                    task_instance.printTimetableJuryTeam = false
                    task_instance.printTimetableJuryClass = false
                    task_instance.printTimetableJuryShortClass = false
                    task_instance.printTimetableJuryPlanning = false
                    task_instance.printTimetableJuryPlanningEnd = false
                    task_instance.printTimetableJuryTakeoff = false
                    task_instance.printTimetableJuryStartPoint = false
                    task_instance.printTimetableJuryCheckPoints = ""
                    task_instance.printTimetableJuryFinishPoint = false
                    task_instance.printTimetableJuryLanding = true
                    task_instance.printTimetableJuryArrival = false
                    task_instance.printTimetableJurySubmission = true
                    task_instance.printTimetableJuryEmptyColumn1 = true
                    task_instance.printTimetableJuryEmptyTitle1 = getPrintMsg('fc.test.debriefing.exittime')
                    task_instance.printTimetableJuryEmptyColumn2 = true
                    task_instance.printTimetableJuryEmptyTitle2 = ""
                    task_instance.printTimetableJuryEmptyColumn3 = true
                    task_instance.printTimetableJuryEmptyTitle3 = ""
                    task_instance.printTimetableJuryEmptyColumn4 = false
                    task_instance.printTimetableJuryEmptyTitle4 = ""
                    task_instance.printTimetableJuryLandingField = false
                    task_instance.printTimetableJuryLandscape = false
                    task_instance.printTimetableJuryA3 = false
                    break
				case PrintSettings.TimetableStandard:
					settings_name = getMsg('fc.task.timetable')
					detail_name = getMsg('fc.standard')
					task_instance.printTimetablePrintTitle = ""
					task_instance.printTimetableNumber = true
					task_instance.printTimetableCrew = true
					task_instance.printTimetableAircraft = true
					task_instance.printTimetableTAS = true
					task_instance.printTimetableTeam = false
					task_instance.printTimetableClass = false
					task_instance.printTimetableShortClass = true
					task_instance.printTimetablePlanning = true
					task_instance.printTimetableTakeoff = true
					task_instance.printTimetableVersion = true
                    task_instance.printTimetableLegTimes = true
					task_instance.printTimetableLandscape = false
					task_instance.printTimetableA3 = false
					break
				case PrintSettings.TimetableNone:
					settings_name = getMsg('fc.task.timetable')
					detail_name = getMsg('fc.setprintsettings.none')
					task_instance.printTimetablePrintTitle = ""
					task_instance.printTimetableNumber = false
					task_instance.printTimetableCrew = false
					task_instance.printTimetableAircraft = false
					task_instance.printTimetableTAS = false
					task_instance.printTimetableTeam = false
					task_instance.printTimetableClass = false
					task_instance.printTimetableShortClass = false
					task_instance.printTimetablePlanning = false
					task_instance.printTimetableTakeoff = false
					task_instance.printTimetableVersion = false
                    task_instance.printTimetableLegTimes = false
					task_instance.printTimetableLandscape = false
					task_instance.printTimetableA3 = false
					break
				case PrintSettings.TimetableAll:
					settings_name = getMsg('fc.task.timetable')
					detail_name = getMsg('fc.setprintsettings.all')
					task_instance.printTimetablePrintTitle = ""
					task_instance.printTimetableNumber = true
					task_instance.printTimetableCrew = true
					task_instance.printTimetableAircraft = true
					task_instance.printTimetableTAS = true
					task_instance.printTimetableTeam = true
					task_instance.printTimetableClass = true
					task_instance.printTimetableShortClass = true
					task_instance.printTimetablePlanning = true
					task_instance.printTimetableTakeoff = true
					task_instance.printTimetableVersion = true
                    task_instance.printTimetableLegTimes = true
					task_instance.printTimetableLandscape = true
					task_instance.printTimetableA3 = false
					break
				case PrintSettings.TimetableRemoveChange:
					settings_name = getMsg('fc.task.timetable')
					detail_name = getMsg('fc.removechangeprintsettings')
					task_instance.printTimetableChange = ""
					break
				case PrintSettings.TimetableAddChange:
					settings_name = getMsg('fc.task.timetable')
					detail_name = getMsg('fc.addchangeprintsettings')
					int last_timetable_version = 0
					for (Test test_instance in Test.findAllByTask(task_instance,[sort:"timetableVersion"])) {
						if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
							if (test_instance.timetableVersion > 1) {
								if (last_timetable_version != test_instance.timetableVersion) {
									if (task_instance.printTimetableChange) {
										task_instance.printTimetableChange += "\r\n"
									}
									task_instance.printTimetableChange += "${getPrintMsg('fc.test.timetable.change')} ${getPrintMsg('fc.version')} ${test_instance.timetableVersion}:"
									last_timetable_version = test_instance.timetableVersion
								}
								if (task_instance.printTimetableChange) {
									task_instance.printTimetableChange += "\r\n"
								}
								task_instance.printTimetableChange += "  ${test_instance.crew.startNum}: ${test_instance.crew.name} ($test_instance.taskAircraft.registration)"
							}
						}
					}
					if (task_instance.printTimetableChange.size() > Task.TIMETABLECHANGESIZE) {
						task_instance.printTimetableChange = task_instance.printTimetableChange.substring(0,Task.TIMETABLECHANGESIZE) 
					}
					break
				case PrintSettings.LandingStartlistModified:
					settings_name = getMsg('fc.task.landingstartlist')
		            task_instance.properties = params
					break
				case PrintSettings.LandingStartlistStandard:
					settings_name = getMsg('fc.task.landingstartlist')
					detail_name = getMsg('fc.standard')
					task_instance.printLandingStartlistPrintTitle = ""
					task_instance.printLandingStartlistNumber = true
					task_instance.printLandingStartlistCrew = true
					task_instance.printLandingStartlistAircraft = true
					task_instance.printLandingStartlistAircraftType = true
					task_instance.printLandingStartlistAircraftColour = false
					task_instance.printLandingStartlistTAS = false
					task_instance.printLandingStartlistTeam = false
					task_instance.printLandingStartlistClass = false
					task_instance.printLandingStartlistShortClass = false
					task_instance.printLandingStartlistEmptyColumn1 = false
					task_instance.printLandingStartlistEmptyTitle1 = ""
					task_instance.printLandingStartlistEmptyColumn2 = false
					task_instance.printLandingStartlistEmptyTitle2 = ""
					task_instance.printLandingStartlistEmptyColumn3 = false
					task_instance.printLandingStartlistEmptyTitle3 = ""
					task_instance.printLandingStartlistEmptyColumn4 = false
					task_instance.printLandingStartlistEmptyTitle4 = ""
					task_instance.printLandingStartlistGroupCrewNum = 5
					task_instance.printLandingStartlistStartGroupCrews = ""
					task_instance.printLandingStartlistLandingField = false
					task_instance.printLandingStartlistLandscape = false
					task_instance.printLandingStartlistA3 = false
					break
				case PrintSettings.LandingStartlistNone:
					settings_name = getMsg('fc.task.landingstartlist')
					detail_name = getMsg('fc.setprintsettings.none')
					task_instance.printLandingStartlistPrintTitle = ""
					task_instance.printLandingStartlistNumber = false
					task_instance.printLandingStartlistCrew = false
					task_instance.printLandingStartlistAircraft = false
					task_instance.printLandingStartlistAircraftType = false
					task_instance.printLandingStartlistAircraftColour = false
					task_instance.printLandingStartlistTAS = false
					task_instance.printLandingStartlistTeam = false
					task_instance.printLandingStartlistClass = false
					task_instance.printLandingStartlistShortClass = false
					task_instance.printLandingStartlistEmptyColumn1 = false
					task_instance.printLandingStartlistEmptyTitle1 = ""
					task_instance.printLandingStartlistEmptyColumn2 = false
					task_instance.printLandingStartlistEmptyTitle2 = ""
					task_instance.printLandingStartlistEmptyColumn3 = false
					task_instance.printLandingStartlistEmptyTitle3 = ""
					task_instance.printLandingStartlistEmptyColumn4 = false
					task_instance.printLandingStartlistEmptyTitle4 = ""
					task_instance.printLandingStartlistGroupCrewNum = 0
					task_instance.printLandingStartlistStartGroupCrews = ""
					break
				case PrintSettings.LandingStartlistAll:
					settings_name = getMsg('fc.task.landingstartlist')
					detail_name = getMsg('fc.setprintsettings.all')
					task_instance.printLandingStartlistPrintTitle = ""
					task_instance.printLandingStartlistNumber = true
					task_instance.printLandingStartlistCrew = true
					task_instance.printLandingStartlistAircraft = true
					task_instance.printLandingStartlistAircraftType = true
					task_instance.printLandingStartlistAircraftColour = true
					task_instance.printLandingStartlistTAS = true
					task_instance.printLandingStartlistTeam = true
					task_instance.printLandingStartlistClass = true
					task_instance.printLandingStartlistShortClass = true
					task_instance.printLandingStartlistEmptyColumn1 = true
					task_instance.printLandingStartlistEmptyColumn2 = true
					task_instance.printLandingStartlistEmptyColumn3 = true
					task_instance.printLandingStartlistEmptyColumn4 = true
					task_instance.printLandingStartlistGroupCrewNum = 5
					break
			}
			
            if(!task_instance.hasErrors() && task_instance.save()) {
                Map ret = ['instance':task_instance,'saved':true,'message':getMsg('fc.task.settings.saved',[settings_name,detail_name,"${task_instance.name()}"])]
				printdone ret
				return ret
            } else {
				printerror ""
                return ['instance':task_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
			printerror ret
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
	private boolean is_modified(paramValue, boolean testValue)
	{
		if (paramValue) {
			if (!testValue) {
				return true
			}
		} else {
			if (testValue) {
				return true
			}
		}
		return false
	}
	
    //--------------------------------------------------------------------------
	private boolean is_modified(paramValue, int testValue)
	{
		if (paramValue) {
			if (paramValue != testValue.toString()) {
				return true
			}
		}
		return false
	}
	
    //--------------------------------------------------------------------------
	private boolean is_modified(paramValue, String testValue)
	{
		if (paramValue) {
			if (paramValue != testValue) {
				return true
			}
		}
		return false
	}
	
    //--------------------------------------------------------------------------
    Map createTask(Map params, Contest contestInstance)
    {
        Task task_instance = new Task()
        task_instance.contest = contestInstance
        task_instance.properties = params
        task_instance.liveTrackingNavigationTaskDate = contestInstance.liveTrackingContestDate
        return ['instance':task_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveTask(Map params,Contest contestInstance) 
    {
    	Task task_instance = new Task(params)
        
        task_instance.contest = contestInstance
        task_instance.idTitle = Task.countByContest(contestInstance) + 1
        
        if(!task_instance.hasErrors() && task_instance.save()) {
			if (contestInstance.resultClasses) {
				for (ResultClass resultclass_instance in ResultClass.findAllByContest(contestInstance,[sort:"id"])) {
					TaskClass taskclass_instance = new TaskClass()
					taskclass_instance.task = task_instance
					taskclass_instance.resultclass = resultclass_instance
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestRun}"]) {
						taskclass_instance.planningTestRun = true
					} else {
						taskclass_instance.planningTestRun = false
					}
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestDistanceMeasure}"]) {
                        taskclass_instance.planningTestDistanceMeasure = true
                    } else { 
                        taskclass_instance.planningTestDistanceMeasure = false
                    }
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestDirectionMeasure}"]) {
                        taskclass_instance.planningTestDirectionMeasure = true
                    } else { 
                        taskclass_instance.planningTestDirectionMeasure = false
                    }
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestRun}"]) {
						taskclass_instance.flightTestRun = true
					} else {
						taskclass_instance.flightTestRun = false
					}
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckSecretPoints}"]) {
                        taskclass_instance.flightTestCheckSecretPoints = true
                    } else { 
                        taskclass_instance.flightTestCheckSecretPoints = false
                    }
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckTakeOff}"]) {
                        taskclass_instance.flightTestCheckTakeOff = true
                    } else { 
                        taskclass_instance.flightTestCheckTakeOff = false
                    }
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckLanding}"]) {
                        taskclass_instance.flightTestCheckLanding = true
                    } else { 
                        taskclass_instance.flightTestCheckLanding = false
                    }
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestRun}"]) {
						taskclass_instance.observationTestRun = true
					} else {
						taskclass_instance.observationTestRun = false
					}
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestTurnpointRun}"]) {
                        taskclass_instance.observationTestTurnpointRun = true
                    } else {
                        taskclass_instance.observationTestTurnpointRun = false
                    }
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestEnroutePhotoRun}"]) {
                        taskclass_instance.observationTestEnroutePhotoRun = true
                    } else {
                        taskclass_instance.observationTestEnroutePhotoRun = false
                    }
                    if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestEnrouteCanvasRun}"]) {
                        taskclass_instance.observationTestEnrouteCanvasRun = true
                    } else {
                        taskclass_instance.observationTestEnrouteCanvasRun = false
                    }
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTestRun}"]) {
						taskclass_instance.landingTestRun = true
					} else {
						taskclass_instance.landingTestRun = false
					}
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest1Run}"]) {
						taskclass_instance.landingTest1Run = true
					} else {
						taskclass_instance.landingTest1Run = false
					}
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest2Run}"]) {
						taskclass_instance.landingTest2Run = true
					} else {
						taskclass_instance.landingTest2Run = false
					}
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest3Run}"]) {
						taskclass_instance.landingTest3Run = true
					} else {
						taskclass_instance.landingTest3Run = false
					}
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest4Run}"]) {
						taskclass_instance.landingTest4Run = true
					} else {
						taskclass_instance.landingTest4Run = false
					}
					if (params["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_SpecialTestRun}"]) {
						taskclass_instance.specialTestRun = true
					} else { 
						taskclass_instance.specialTestRun = false
					}
					taskclass_instance.save()
				}
			}
            Crew.findAllByContest(task_instance.contest,[sort:"viewpos"]).eachWithIndex { Crew crew_instance, int i ->
                Test test_instance = new Test()
                test_instance.crew = crew_instance
				test_instance.taskTAS = crew_instance.tas
				test_instance.taskAircraft = crew_instance.aircraft
                test_instance.taskTrackerID = crew_instance.trackerID
                test_instance.viewpos = i
                test_instance.task = task_instance
                test_instance.timeCalculated = false
                test_instance.loggerData = new LoggerDataTest()
                test_instance.loggerResult = new LoggerResult()
                test_instance.save()
            }
            return ['instance':task_instance,'saved':true,'fromcontestday':params.fromcontestday,'message':getMsg('fc.created',["${task_instance.name()}"])]
        } else {
            return ['instance':task_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            try {
            	// remove Tests
            	Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
            		test_instance.delete()
            	}

                task_instance.delete()
                
                // correct idTitle of other tasks
                Task.findAllByContest(task_instance.contest,[sort:"idTitle"]).eachWithIndex { Task task_instance2, int index -> 
                    task_instance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${task_instance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.task'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    void changeviewpositionTask(Map params, boolean addViewPos)
    {
		printstart "changeviewpositionTask addViewPos=$addViewPos"
        Task task_instance = Task.get(params.id.toLong())
        if (task_instance) {
            boolean task_found = false
            if (addViewPos) {
                for (Task task_instance2 in Task.findAllByContest(task_instance.contest,[sort:"idTitle"])) {
                    if (task_found) {
                        task_instance2.idTitle--
                        task_instance2.save()
                        task_found = false
                    } else if (task_instance2.id == task_instance.id) {
                        task_found = true
                    }
                }
                task_instance.idTitle++
                task_instance.save()
            } else {
                for (Task task_instance2 in Task.findAllByContest(task_instance.contest,[sort:"idTitle", order: "desc"])) {
                    if (task_found) {
                        task_instance2.idTitle++
                        task_instance2.save()
                        task_found = false
                    } else if (task_instance2.id == task_instance.id) {
                        task_found = true
                    }
                }
                task_instance.idTitle--
                task_instance.save()
            }
        }
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    Map savedisabledcheckpointsTask(Map params)
    {
		printstart "savedisabledcheckpointsTask"
		
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':task_instance]
                }
            }
            
			String last_disabledcheckpoints_timecheck = task_instance.disabledCheckPoints
            String last_disabledcheckpoints_notfound = task_instance.disabledCheckPointsNotFound
            String last_disabledcheckpoints_minaltitude = task_instance.disabledCheckPointsMinAltitude
            String last_disabledcheckpoints_procedureturn = task_instance.disabledCheckPointsProcedureTurn
            String last_disabledcheckpoints_badcourse = task_instance.disabledCheckPointsBadCourse
            String last_disabledcheckpoints_turnpointobs = task_instance.disabledCheckPointsTurnpointObs
            String last_disabledenroute_photoobs = task_instance.disabledEnroutePhotoObs
            String last_disabledenroute_canvasobs = task_instance.disabledEnrouteCanvasObs
            task_instance.properties = params
			
            set_disabledcheckpoints_from_dialog(task_instance,task_instance.flighttest.route, params)
			
			boolean modify_flighttest_results = (last_disabledcheckpoints_timecheck != task_instance.disabledCheckPoints) ||
                                                (last_disabledcheckpoints_notfound != task_instance.disabledCheckPointsNotFound) ||
                                                (last_disabledcheckpoints_minaltitude != task_instance.disabledCheckPointsMinAltitude) ||
                                                (last_disabledcheckpoints_procedureturn != task_instance.disabledCheckPointsProcedureTurn) ||
                                                (last_disabledcheckpoints_badcourse != task_instance.disabledCheckPointsBadCourse) ||
                                                (last_disabledcheckpoints_turnpointobs != task_instance.disabledCheckPointsTurnpointObs) ||
                                                (last_disabledenroute_photoobs != task_instance.disabledEnroutePhotoObs) ||
                                                (last_disabledenroute_canvasobs != task_instance.disabledEnrouteCanvasObs)
			if (modify_flighttest_results) {
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
                    boolean coordresult_modified = false
                    boolean observationresult_modified = false
					CoordResult.findAllByTest(test_instance,[sort:"id"]).each { CoordResult coordresult_instance ->
						calculateCoordResultInstancePenaltyCoord(coordresult_instance)
                        if (coordresult_instance.isDirty()) {
                            coordresult_modified = true
                        }
						coordresult_instance.save()
					}
                    for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(test_instance,[sort:"id"])) {
                        calculatePenaltyTurnpointDataInstance(turnpointdata_instance, test_instance)
                        if (turnpointdata_instance.isDirty()) {
                            observationresult_modified = true
                        }
                        turnpointdata_instance.save()
                    }
                    for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(test_instance,[sort:"id"])) {
                        calculatePenaltyEnrouteDataInstance(enroutephotodata_instance, test_instance, true)
                        if (enroutephotodata_instance.isDirty()) {
                            observationresult_modified = true
                        }
                        enroutephotodata_instance.save()
                    }
                    for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(test_instance,[sort:"id"])) {
                        calculatePenaltyEnrouteDataInstance(enroutecanvasdata_instance, test_instance, false)
                        if (enroutecanvasdata_instance.isDirty()) {
                            observationresult_modified = true
                        }
                        enroutecanvasdata_instance.save()
                    }
					calculateTestPenalties(test_instance,false)
                    if (coordresult_modified) {
                        test_instance.flightTestModified = true
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                    }
                    if (observationresult_modified) {
                        test_instance.observationTestModified = true
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                    }
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
				printdone ""
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
				printerror ""
                return ['instance':task_instance]
            }
        } else {
			printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetdisabledcheckpointsTask(Map params)
    {
		printstart "resetdisabledcheckpointsTask"
		
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':task_instance]
                }
            }
            
			String last_disabledcheckpoints_timecheck = task_instance.disabledCheckPoints
            String last_disabledcheckpoints_notfound = task_instance.disabledCheckPointsNotFound
            String last_disabledcheckpoints_minaltitude = task_instance.disabledCheckPointsMinAltitude
            String last_disabledcheckpoints_procedureturn = task_instance.disabledCheckPointsProcedureTurn
            String last_disabledcheckpoints_badcourse = task_instance.disabledCheckPointsBadCourse
            String last_disabledcheckpoints_turnpointobs = task_instance.disabledCheckPointsTurnpointObs
            String last_disabledenroute_photoobs = task_instance.disabledEnroutePhotoObs
            String last_disabledenroute_canvasobs = task_instance.disabledEnrouteCanvasObs
            task_instance.disabledCheckPoints = ""
            task_instance.disabledCheckPointsNotFound = ""
            task_instance.disabledCheckPointsMinAltitude = ""
            task_instance.disabledCheckPointsProcedureTurn = ""
            task_instance.disabledCheckPointsBadCourse = ""
            task_instance.disabledCheckPointsTurnpointObs = ""
            task_instance.disabledEnroutePhotoObs = ""
            task_instance.disabledEnrouteCanvasObs = ""
            
			boolean modify_flighttest_results = (last_disabledcheckpoints_timecheck != task_instance.disabledCheckPoints) ||
                                                (last_disabledcheckpoints_notfound != task_instance.disabledCheckPointsNotFound) ||
                                                (last_disabledcheckpoints_minaltitude != task_instance.disabledCheckPointsMinAltitude) ||
                                                (last_disabledcheckpoints_procedureturn != task_instance.disabledCheckPointsProcedureTurn) ||
                                                (last_disabledcheckpoints_badcourse != task_instance.disabledCheckPointsBadCourse) ||
                                                (last_disabledcheckpoints_turnpointobs != task_instance.disabledCheckPointsTurnpointObs) ||
                                                (last_disabledenroute_photoobs != task_instance.disabledEnroutePhotoObs) ||
                                                (last_disabledenroute_canvasobs != task_instance.disabledEnrouteCanvasObs)
			if (modify_flighttest_results) {
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
                    boolean coordresult_modified = false
                    boolean observationresult_modified = false
					CoordResult.findAllByTest(test_instance,[sort:"id"]).each { CoordResult coordresult_instance ->
						calculateCoordResultInstancePenaltyCoord(coordresult_instance)
                        if (coordresult_instance.isDirty()) {
                            coordresult_modified = true
                        }
						coordresult_instance.save()
					}
                    for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(test_instance,[sort:"id"])) {
                        calculatePenaltyTurnpointDataInstance(turnpointdata_instance, test_instance)
                        if (turnpointdata_instance.isDirty()) {
                            observationresult_modified = true
                        }
                        turnpointdata_instance.save()
                    }
                    for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(test_instance,[sort:"id"])) {
                        calculatePenaltyEnrouteDataInstance(enroutephotodata_instance, test_instance, true)
                        if (enroutephotodata_instance.isDirty()) {
                            observationresult_modified = true
                        }
                        enroutephotodata_instance.save()
                    }
                    for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(test_instance,[sort:"id"])) {
                        calculatePenaltyEnrouteDataInstance(enroutecanvasdata_instance, test_instance, false)
                        if (enroutecanvasdata_instance.isDirty()) {
                            observationresult_modified = true
                        }
                        enroutecanvasdata_instance.save()
                    }
					calculateTestPenalties(test_instance,false)
                    if (coordresult_modified) {
                        test_instance.flightTestModified = true
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                    }
                    if (observationresult_modified) {
                        test_instance.observationTestModified = true
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                    }
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
				printdone ""
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
				printerror ""
                return ['instance':task_instance]
            }
        } else {
			printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startplanningTask(Map params,contestInstance,lastTaskPlanning)
    {
    	Task task_instance = null
        if (lastTaskPlanning) {
            task_instance = Task.findByIdAndContestAndHidePlanning(lastTaskPlanning,contestInstance,false)
        }
        if (!task_instance) {
			if (contestInstance) {
	        	Task.findAllByContestAndHidePlanning(contestInstance,false,[sort:"idTitle"]).each { 
					if (!it.hidePlanning) {
		        		if (!task_instance) {
		        			task_instance = it
		        		}
					}
	        	}
			}
        }
        if (task_instance) {
            return ['taskid':task_instance.id]
        }
        return ['taskid':0]
    }
    
    //--------------------------------------------------------------------------
    Map startresultsTask(Map params,contestInstance,lastTaskResults)
    {
        Task task_instance = null
        if (lastTaskResults) {
            task_instance = Task.findByIdAndContestAndHideResults(lastTaskResults,contestInstance,false)
        }
        if (!task_instance) {
			if (contestInstance) {
	            Task.findAllByContestAndHideResults(contestInstance,false,[sort:"idTitle"]).each {
	                if (!task_instance) {
	                    task_instance = it
	                }
	            }
			}
        }
        if (task_instance) {
            return ['taskid':task_instance.id]
        }
        return ['taskid':0]
    }
    
    //--------------------------------------------------------------------------
    Map selectallTask(Map params)
    {
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            Map selected_testids = [selectedTestID:""]
            Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
				selected_testids["selectedTestID${test_instance.id}"] = "on"
            }
            task.selectedtestids = selected_testids
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map selectendTask(Map params) 
    {
        Map task = domainService.GetTask(params) 
        if (task.instance) {

			// search last selected test
			Test last_test_instance = null
			Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
				if (params["selectedTestID${test_instance.id}"] == "on") {
					last_test_instance = test_instance
				}
			}

			if (last_test_instance) {
				boolean found = false
	            Map selected_testids = [selectedTestID:""]
	            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
					if (found) {
						selected_testids["selectedTestID${test_instance.id}"] = "on"
					} else if (test_instance == last_test_instance) {
						selected_testids["selectedTestID${test_instance.id}"] = "on"
						found = true
					} else if (params["selectedTestID${test_instance.id}"] == "on") {
						selected_testids["selectedTestID${test_instance.id}"] = "on"
					}
	            }
	            task.selectedtestids = selected_testids
			}
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map assignplanningtesttaskTask(Map params)
    {
		printstart "assignplanningtesttaskTask"
		
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // PlanningTest exists?
        if (!task.instance.planningtest) {
            task.message = getMsg('fc.planningtest.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(task.instance.planningtest)) {
            task.message = getMsg('fc.planningtesttask.notfound')
            task.error = true
			printerror task.message
            return task
        }

        List test_instance_ids = [""]
        int assign_num = 0
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance_ids += test_instance.id.toString()
                assign_num++
            }
        }
        task.testinstanceids = test_instance_ids
        if (!assign_num) {
            task.message = getMsg('fc.planningtesttask.someonemustselected.assign')
            task.error = true
            printerror task.message
            return task
        }
        
        printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map setplanningtesttaskTask(Map params)
    {
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.planningtesttask.id)
            params.testInstanceIDs.each { String test_id ->
                if (test_id) {
                    Test test_instance = Test.get(test_id)
					if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
	                    test_instance.planningtesttask = planningtesttask_instance 
	                    calulateTestLegPlannings(test_instance)
						test_instance.ResetPlanningTestResults()
						test_instance.CalculateTestPenalties()
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
	                    test_instance.save()
					}
                }
            }
            task.message = getMsg('fc.task.selectplanningtesttask.assigned',[planningtesttask_instance.name()])
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map assignflighttestwindTask(Map params)
    {
		printstart "assignflighttestwindTask"
		
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
			printerror task.message
            return task
        }
        
        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
			printerror task.message
            return task
        }
        
        List test_instance_ids = [""]
        int assign_num = 0
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance_ids += test_instance.id.toString()
                assign_num++
            }
        }
        task.testinstanceids = test_instance_ids
        if (!assign_num) {
            task.message = getMsg('fc.flighttestwind.someonemustselected.assign')
            task.error = true
            printerror task.message
            return task
        }
        
        printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map setflighttestwindTask(Map params)
    {
		printstart "setflighttestwindTask"
        Map task = domainService.GetTask(params) 
        if (task.instance) {
            FlightTestWind flighttestwind_instance = FlightTestWind.get(params.flighttestwind.id)
            params.testInstanceIDs.each { String test_id ->
                if (test_id) {
                    Test test_instance = Test.get(test_id)
					if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
						setflighttestwindTest(test_instance, task.instance, flighttestwind_instance)
					}
                }
            }
            task.message = getMsg('fc.task.selectflighttestwind.assigned',[flighttestwind_instance.wind.name()])
        }
		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
	private void setflighttestwindTest(Test testInstance, Task taskInstance, FlightTestWind flightTestWind)
	{
		printstart "setflighttestwindTest: ${testInstance.crew.name}"
        testInstance.flighttestwind = flightTestWind
		calculate_testlegflight(testInstance)
        if (testInstance.timeCalculated) {
			GregorianCalendar testing_time = new GregorianCalendar()
			testing_time.setTime(testInstance.testingTime)
			calculate_test_time(testInstance, taskInstance, testing_time, null, true)
			calculate_coordresult(testInstance)
			taskInstance.timetableModified = true
			taskInstance.save()
        }
        testInstance.save()
		printdone ""
	}
	
    //--------------------------------------------------------------------------
	private void calculate_task_time(Task taskInstance, boolean calculateTimes)
	{
		printstart "calculate_task_time"
		Test.findAllByTask(taskInstance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				if (calculateTimes) {
					calculate_testlegflight(test_instance)
				}
		        if (test_instance.timeCalculated) {
					GregorianCalendar testing_time = new GregorianCalendar()
					testing_time.setTime(test_instance.testingTime)
					calculate_test_time(test_instance, taskInstance, testing_time, null, calculateTimes)
					if (calculateTimes) {
						calculate_coordresult(test_instance)
					}
					taskInstance.timetableModified = true
					taskInstance.save()
		        }
		        test_instance.save()
			}
		}
		printdone ""
	}
	
    //--------------------------------------------------------------------------
	private void reset_task_timecalculated(Task taskInstance)
	{
		printstart "reset_task_timecalculated"
		Test.findAllByTask(taskInstance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
		        test_instance.timeCalculated = false
				test_instance.ResetFlightTestResults()
				test_instance.CalculateTestPenalties()
                test_instance.flightTestLink = ""
				delete_uploadjobtest(test_instance)
                test_instance.crewResultsModified = true
		        test_instance.save()
			}
		}
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map calculatesequenceTask(Map params)
    {
		printstart "calculatesequenceTask"
		
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        /*
        // Have all crews an aircraft?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.taskAircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            return task
        }
        */

    	// set viewpos for aircraft of user1 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
	        	if (test_instance.taskAircraft) {
	        		if (test_instance.taskAircraft.user1 == test_instance.crew) {
	        			test_instance.viewpos = 4000+test_instance.taskTAS
	        		}
	        	}
			}
        }

        // set viewpos for aircraft of user2 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
	            if (test_instance.taskAircraft) {
	                if (test_instance.taskAircraft.user2 == test_instance.crew) {
	                    test_instance.viewpos = 3000+test_instance.taskTAS
	                }
	            }
			}
        }

        // set viewpos for user without aircraft 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
	            if (!test_instance.taskAircraft) {
	                test_instance.viewpos = 2000+test_instance.taskTAS
	            }
			}
        }

        // set viewpos for disabled user 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (test_instance.disabledCrew || test_instance.crew.disabled) {
                test_instance.viewpos = 1000+test_instance.taskTAS
			}
        }

        // set viewpos
        Test.findAllByTask(task.instance,[sort:"viewpos",order:"desc"]).eachWithIndex { Test test_instance, int i ->
            test_instance.viewpos = i
            test_instance.timeCalculated = false
			test_instance.ResetFlightTestResults()
			test_instance.CalculateTestPenalties()
            test_instance.flightTestLink = ""
			delete_uploadjobtest(test_instance)
            test_instance.crewResultsModified = true
            test_instance.save()
        }
        
        task.message = getMsg('fc.test.sequence.calculated')    
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map resetsequenceTask(Map params)
    {
		printstart "resetsequenceTask"
		
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

    	// set viewpos with crew viewpos
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
   			test_instance.viewpos = test_instance.crew.viewpos
            task_crew_correction(test_instance)
            test_instance.timeCalculated = false
			test_instance.ResetFlightTestResults()
			test_instance.CalculateTestPenalties()
            test_instance.flightTestLink = ""
			delete_uploadjobtest(test_instance)
            test_instance.crewResultsModified = true
            test_instance.save()
        }

        task.message = getMsg('fc.test.sequence.reset')    
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map moveupTask(Map params,session)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

		// moveable? (not top & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.viewpos == 0) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
            //task.message = getMsg('fc.test.moveborderreached')
            task.borderreached = true
            return task
        }
        if (notmovable) {
            task.message = getMsg('fc.test.notmovable')
            task.error = true
            return task
        }
        
		// move tasks up
		int movenum = 0
        int movefirstpos = -1
        Map selected_testids = [selectedTestID:""]
        borderreached = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance.viewpos--
                task_crew_correction(test_instance)
                test_instance.timeCalculated = false
                test_instance.save()
                selected_testids["selectedTestID${test_instance.id}"] = "on"
                if (test_instance.viewpos == 0) {
                    borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || test_instance.viewpos < movefirstpos) {
                    movefirstpos = test_instance.viewpos
                }
            }
        }
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] != "on") {
                if (test_instance.viewpos >= movefirstpos && test_instance.viewpos < movefirstpos + movenum) {
                    test_instance.viewpos += movenum
                    test_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos < session.showLimitStartPos) {
				if (movenum == 1) {
					if (session.showLimitStartPos > session.showLimitCrewNum) {
						session.showLimitStartPos -= session.showLimitCrewNum
					} else {
						session.showLimitStartPos = 0
					}
				} else {
					session.showLimitStartPos--
				}
			}
		}
		
		// restore selection if not top
        if (!borderreached) {
            task.selectedtestids = selected_testids
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map movedownTask(Map params,session)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

		// moveable? (not bottom & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
            //task.message = getMsg('fc.test.moveborderreached')
            task.borderreached = true
            return task
        }
        if (notmovable) {
            task.message = getMsg('fc.test.notmovable')
            task.error = true
            return task
        }
        
		// move tasks down
        int movenum = 0
        int movefirstpos = -1
        Map selected_testids = [selectedTestID:""]
        borderreached = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance.viewpos++
                task_crew_correction(test_instance)
                test_instance.timeCalculated = false
                test_instance.save()
                selected_testids["selectedTestID${test_instance.id}"] = "on"
                if (test_instance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
					borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || test_instance.viewpos < movefirstpos) {
                    movefirstpos = test_instance.viewpos
                }
            }
        }
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] != "on") {
                if (test_instance.viewpos >= movefirstpos && test_instance.viewpos < movefirstpos + movenum) {
                    test_instance.viewpos -= movenum
                    test_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos + movenum > session.showLimitStartPos + session.showLimitCrewNum) {
				if (movenum == 1) {
					int crew_num = Crew.countByContest(session.lastContest)
					if (session.showLimitStartPos + session.showLimitCrewNum < crew_num) {
						session.showLimitStartPos += session.showLimitCrewNum
					}
				} else {
					session.showLimitStartPos++
				}
			}
		}
		
		// restore selection if not bottom
        if (!borderreached) {
            task.selectedtestids = selected_testids
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map moveendTask(Map params,session)
    {
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }

		// moveable? (not bottom & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.viewpos + 1 == Crew.countByContest(task.instance.contest)) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
            //task.message = getMsg('fc.test.moveborderreached')
            task.borderreached = true
            return task
        }
        if (notmovable) {
            task.message = getMsg('fc.test.notmovable')
            task.error = true
            return task
        }
        
		// move tasks to end
        int movenum = 0
        int movefirstpos = -1
        Map selected_testids = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (movefirstpos == -1) {
                    movefirstpos = test_instance.viewpos
                }
                test_instance.viewpos = Crew.countByContest(task.instance.contest) + movenum
                task_crew_correction(test_instance)
                test_instance.timeCalculated = false
                test_instance.save()
                selected_testids["selectedTestID${test_instance.id}"] = "on"
                movenum++
            }
        }
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (test_instance.viewpos >= movefirstpos) {
                test_instance.viewpos -= movenum
                test_instance.save()
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos + movenum > session.showLimitStartPos + session.showLimitCrewNum) {
				if (movenum == 1) {
					int crew_num = Crew.countByContest(session.lastContest)
					if (session.showLimitStartPos + session.showLimitCrewNum < crew_num) {
						session.showLimitStartPos += session.showLimitCrewNum
					}
				} else {
					session.showLimitStartPos++
				}
			}
		}
		
        return task
    }
    
    //--------------------------------------------------------------------------
    private void task_crew_correction(Test test_instance) 
    { 
        if (test_instance.taskTAS != test_instance.crew.tas) { // taskTAS-Korrektur
            test_instance.taskTAS = test_instance.crew.tas
            calulateTestLegPlannings(test_instance)
            test_instance.ResetPlanningTestResults()
        }
        if (test_instance.taskAircraft != test_instance.crew.aircraft) { // taskAircraft-Korrektur
            test_instance.taskAircraft = test_instance.crew.aircraft
        }
        if (test_instance.taskTrackerID != test_instance.crew.trackerID) { // taskTrackerID-Korrektur
            test_instance.taskTrackerID = test_instance.crew.trackerID
        }
    }

    //--------------------------------------------------------------------------
    Map disableCrewsTask(Map params,session)
    {
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }

        int disable_num = 0
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    test_instance.disabledCrew = true
                    test_instance.save()
                    resetPositionCrew(test_instance.crew)
                    test_instance.crew.save()
                    disable_num++
                }
            }
        }
        task.message = getMsg('fc.crew.disabled',["${disable_num}"])
        return task
    }
    
    //--------------------------------------------------------------------------
    Map enableCrewsTask(Map params,session)
    {
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }

        int enable_num = 0
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (test_instance.disabledCrew && !test_instance.crew.disabled) {
                    test_instance.disabledCrew = false
                    test_instance.save()
                    resetPositionCrew(test_instance.crew)
                    test_instance.crew.save()
                    enable_num++
                }
            }
        }
        task.message = getMsg('fc.crew.enabled',["${enable_num}"])
        return task
    }
    
    //--------------------------------------------------------------------------
    Map SetPageBreakTask(Map params, boolean pageBreak, def session)
    {
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }

        for (Test test_instance in Test.findAllByTask(task.instance,[sort:"viewpos"])) {
            if (params["selectedTestID${test_instance.id}"] == "on") {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    test_instance.pageBreak = pageBreak
                    test_instance.save()
                }
            }
        }

		session.showPage = false
		session.showPagePos = 1
		session.showPageNum = 1
		
		if (pageBreak) {
			task.message = getMsg('fc.test.setpagebreak.done')
		} else {
			task.message = getMsg('fc.test.resetpagebreak.done')
		}
        return task
    }
    
    //--------------------------------------------------------------------------
    Map ResetAllPageBreakTask(Map params, def session)
    {
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }

        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
			test_instance.pageBreak = false
			test_instance.save()
        }

		session.showPage = false
		session.showPagePos = 1
		session.showPageNum = 1
		
		task.message = getMsg('fc.test.resetpagebreak.done')
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatetimetableTask(Map params)
    {
        printstart "calculatetimetableTask"
		
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        // FlightTest exists?
        if (!task.instance.flighttest) {
            task.message = getMsg('fc.flighttest.notfound')
            task.error = true
			printerror task.message
            return task
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
            task.message = getMsg('fc.flighttestwind.notfound')
            task.error = true
			printerror task.message
            return task
        }
        
		/*
        // FlightTestWind assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
	            if (!test_instance.flighttestwind) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.flighttestwind.notassigned')
            task.error = true
			printerror task.message
            return task
        }
        */

        /*
        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.taskAircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
            printerror task.message
            return task
        }
        */

		try {
	        calculate_testlegflights(task.instance)
	        int crew_num = calulateTimetable(task.instance)
			calulateTimetableWarnings(task.instance)
			
	        task.message = getMsg('fc.test.timetable.calculated',[crew_num])   
			printdone task.message    
	        return task
		} catch (Exception e) {
			task.message = e.getMessage()
			task.error = true
			printerror task
			return task
		}
    }
    
    //--------------------------------------------------------------------------
    Map timeaddTask(Map params)
    {
        printstart "timeaddTask"
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }
		try {
            save_addtimevalue_task(task.instance, params.addTimeValue)
	        Map selected_testids = [selectedTestID:""]
	        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
	            if (params["selectedTestID${test_instance.id}"] == "on") {
	                modify_testing_time(task.instance, test_instance, true) // true - add
	                selected_testids["selectedTestID${test_instance.id}"] = "on"
	            }
	        }
	        task.selectedtestids = selected_testids
	        calulateTimetableWarnings(task.instance)
			printdone ""
		} catch (Exception e) {
			task.message = e.getMessage()
			task.error = true
			printerror e.getMessage()
		}
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timesubtractTask(Map params)
    {
        printstart "timesubtractTask"
		
        Map task = domainService.GetTask(params) 
        if (!task.instance) {
            return task
        }
		try {
            save_addtimevalue_task(task.instance, params.addTimeValue)
	        Map selected_testids = [selectedTestID:""]
	        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
	            if (params["selectedTestID${test_instance.id}"] == "on") {
	                modify_testing_time(task.instance, test_instance, false) // false - subtract
	                selected_testids["selectedTestID${test_instance.id}"] = "on"
	            }
	        }
	        task.selectedtestids = selected_testids
	        calulateTimetableWarnings(task.instance)
			printdone ""
		} catch (Exception e) {
			task.message = e.getMessage()
			task.error = true
			printerror e.getMessage()
		}
        return task
    }
    
    //--------------------------------------------------------------------------
    private void save_addtimevalue_task(Task taskInstance, String addTimeValue)
    {
        if (addTimeValue.isInteger()) {
            int add_time_value = addTimeValue.toInteger()
            taskInstance.addTimeValue = add_time_value
			taskInstance.save()
        }
    }
    
    //--------------------------------------------------------------------------
    private void modify_testing_time(Task taskInstance, Test testInstance, boolean add)
    {
        if (testInstance.timeCalculated) {
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // add testingTime
            if (add) {
                time.add(Calendar.MINUTE, taskInstance.addTimeValue)
            } else {
                time.add(Calendar.MINUTE, -taskInstance.addTimeValue)
            }
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculate_times(time, taskInstance, testInstance)
			testInstance.ResetFlightTestResults()
			testInstance.CalculateTestPenalties()
            testInstance.flightTestLink = ""
			delete_uploadjobtest(testInstance)
            testInstance.crewResultsModified = true
            testInstance.save()
            
            calculate_coordresult(testInstance)
			
			taskInstance.timetableModified = true
			taskInstance.save()
			testInstance.timetableVersion = taskInstance.timetableVersion + 1 
        }
    }
  
    //--------------------------------------------------------------------------
    Map exporttimetablelabelTask(Map params, String uploadFileName)
    {
        printstart "exporttimetablelabelTask $uploadFileName"
        
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }
        try {
            List export_values = []
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    if (params["selectedTestID${test_instance.id}"] == "on") {
                        String result_class = ""
                        if (test_instance.crew.resultclass) {
                            result_class = test_instance.crew.resultclass.name
                        }
                        String team = ""
                        if (test_instance.crew.team) {
                            team = test_instance.crew.team.name
                        }
                        Map new_value = [startnum:    test_instance.crew.startNum,
                                         crew:        test_instance.crew.name,
                                         aircraft:    test_instance.crew.aircraft.registration,
                                         team:        team,
                                         resultclass: result_class,
                                         tas:         FcMath.SpeedStr_TAS(test_instance.crew.tas),
                                         testtime:    FcMath.TimeStrShort(test_instance.testingTime),
                                         takeofftime: FcMath.TimeStrShort(test_instance.takeoffTime),
                                        ]
                        export_values += new_value
                    }
                }
            }
            if (export_values.size() > 0) {
                File upload_file = new File(uploadFileName)
                BufferedWriter upload_writer =  upload_file.newWriter("UTF-8")
                upload_writer.writeLine "LAYOUT:Standard-Layout,Team-Layout 1,Team-Layout 2"
                upload_writer.writeLine "CONTEST:${task.instance.contest.title}"
                upload_writer.writeLine "TASK:${task.instance.printName()}"
                for (Map export_value in export_values) {
                    upload_writer.writeLine ""
                    upload_writer.writeLine "STARTNUM:${export_value.startnum}"
                    upload_writer.writeLine "AIRCRAFT:${export_value.aircraft}"
                    upload_writer.writeLine "CREW:${export_value.crew}"
                    upload_writer.writeLine "CLASS:${export_value.resultclass}"
                    upload_writer.writeLine "TEAM:${export_value.team}"
                    upload_writer.writeLine "TAS:${export_value.tas}"
                    upload_writer.writeLine "PLANNINGTIME:${export_value.testtime}"
                    upload_writer.writeLine "TAKEOFFTIME:${export_value.takeofftime}"
                }
                upload_writer.close()
            } else {
                task.message = getMsg('fc.test.timetable.export.someonemustselected')
                task.error = true
                printerror task.message
                return task
            }
            printdone ""
        } catch (Exception e) {
            task.message = e.getMessage()
            task.error = true
            printerror e.getMessage()
        }
        return task

    }
    
    //--------------------------------------------------------------------------
    Map exporttimetablestartlistTask(Map params, String uploadFileName)
    {
        printstart "exporttimetablestartlistTask $uploadFileName"
        
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }
        try {
            List export_values = []
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    if (params["selectedTestID${test_instance.id}"] == "on") {
                        String result_class = ""
                        if (test_instance.crew.resultclass) {
                            result_class = test_instance.crew.resultclass.name
                        }
                        String team = ""
                        if (test_instance.crew.team) {
                            team = test_instance.crew.team.name
                        }
                        Map new_value = [startnum:    test_instance.crew.startNum,
                                         crew:        test_instance.crew.name,
                                        ]
                        export_values += new_value
                    }
                }
            }
            if (export_values.size() > 0) {
                File upload_file = new File(uploadFileName)
                BufferedWriter upload_writer =  upload_file.newWriter("ISO-8859-1")
                for (Map export_value in export_values) {
					String export_str = "${export_value.startnum}_${export_value.crew.replace(' ','')}"
					if (task.instance.contest.crewPilotNavigatorDelimiter) {
						export_str = export_str.replace(task.instance.contest.crewPilotNavigatorDelimiter, '_')
					}
					if (task.instance.contest.crewSurnameForenameDelimiter) {
						export_str = export_str.replace(task.instance.contest.crewSurnameForenameDelimiter, '_')
					}
                    upload_writer.writeLine export_str
                }
                upload_writer.close()
            } else {
                task.message = getMsg('fc.test.timetable.export.someonemustselected')
                task.error = true
                printerror task.message
                return task
            }
            printdone ""
        } catch (Exception e) {
            task.message = e.getMessage()
            task.error = true
            printerror e.getMessage()
        }
        return task

    }
    
    //--------------------------------------------------------------------------
    Map exporttimetabledataTask(Map params, String uploadFileName)
    {
        printstart "exporttimetabledataTask $uploadFileName"
        
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }
        if (!task.instance.liveTrackingNavigationTaskDate) {
            return task + [error:true, message:getMsg('fc.livetracking.navigationtaskdate.notexists')]
        }
        try {
            List export_values = []
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    if (params["selectedTestID${test_instance.id}"] == "on") {
                        String result_class = ""
                        if (test_instance.crew.resultclass) {
                            result_class = test_instance.crew.resultclass.name
                        }
                        String team = ""
                        if (test_instance.crew.team) {
                            team = test_instance.crew.team.name
                        }
                        
                        Contest contest_instance = task.instance.contest
                        String to_time = FcTime.UTCGetDateTime(task.instance.liveTrackingNavigationTaskDate, test_instance.takeoffTime, contest_instance.timeZone)
                        String ldg_time = FcTime.UTCGetDateTime(task.instance.liveTrackingNavigationTaskDate, test_instance.maxLandingTime, contest_instance.timeZone)
                        Map gate_times = [:]
                        gate_times += ["TO":to_time]
                        gate_times += ["SP":FcTime.UTCGetDateTime(task.instance.liveTrackingNavigationTaskDate, test_instance.startTime, contest_instance.timeZone)]
                        Date leg_time = test_instance.startTime
                        for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(test_instance)) {
                            leg_time = testlegflight_instance.AddPlanLegTime(leg_time)
                            gate_times += ["${testlegflight_instance.coordTitle.titleMediaCode(Media.Tracking)}":FcTime.UTCGetDateTime(task.instance.liveTrackingNavigationTaskDate, leg_time, contest_instance.timeZone)] 
                        }
                        gate_times += ["LDG":ldg_time]
                        
                        Map new_value = [startnum:        test_instance.crew.startNum,
                                         registration:    test_instance.crew.aircraft.registration,
                                         crewname:        test_instance.crew.name,
                                         teamname:        team,
                                         resultclassname: result_class,
                                         tas:             FcMath.SpeedStr_TAS(test_instance.crew.tas),
                                         gate_times:      gate_times
                                        ]
                        export_values += new_value
                    }
                }
            }
            if (export_values.size() > 0) {
                JsonBuilder json_builder = new JsonBuilder(export_values)
                File upload_file = new File(uploadFileName)
                BufferedWriter upload_writer = upload_file.newWriter("UTF-8")
                upload_writer.writeLine json_builder.toString()
                upload_writer.close()
            } else {
                task.message = getMsg('fc.test.timetable.export.someonemustselected')
                task.error = true
                printerror task.message
                return task
            }
            printdone ""
        } catch (Exception e) {
            task.message = e.getMessage()
            task.error = true
            printerror e.getMessage()
        }
        return task

    }
    
    //--------------------------------------------------------------------------
    Map positionscalculatedTask(Map params)
    {
        Map task = domainService.GetTask(params)
        if (!task.instance) {
            return task
        }

        // Positions calculated?
        boolean no_position_error = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (test_instance.disabledCrew || test_instance.crew.disabled) {
                if (test_instance.taskPosition) {
                    no_position_error = true
                }
            } else {
                if (!test_instance.taskPosition) {
                    no_position_error = true
                }
            }
        }
        if (no_position_error) {
            task.message = getMsg('fc.results.positions2calculate')
            task.error = true
            return task
        }
        
        return task
    }

    //--------------------------------------------------------------------------
	Map timetablejudgeprintquestionTask(Map params)
	{
		Map task = domainService.GetTask(params)
		if (!task.instance) {
			return task
		}

		// FlightTest exists?
		if (!task.instance.flighttest) {
			task.message = getMsg('fc.flighttest.notfound')
			task.error = true
			return task
		}

		// FlightTestWind exists?
		if (!FlightTestWind.countByFlighttest(task.instance.flighttest)) {
			task.message = getMsg('fc.flighttestwind.notfound')
			task.error = true
			return task
		}
		
		// FlightTestWind assigned to all crews?
		boolean call_return = false
		Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				if (!test_instance.flighttestwind) {
					call_return = true
				}
			}
		}
		if (call_return) {
			task.message = getMsg('fc.flighttestwind.notassigned')
			task.error = true
			return task
		}

		// Have all crews an aircraft?
		call_return = false
		Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.taskAircraft) {
				call_return = true
			}
		}
		if (call_return) {
			task.message = getMsg('fc.aircraft.notassigned')
			task.error = true
			return task
		}

		// Timetable calculated?
		call_return = false
		Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				if (!test_instance.timeCalculated) {
					call_return = true
				}
			}
		}
		if (call_return) {
			task.message = getMsg('fc.test.timetable.newcalculate')
			task.error = true
			return task
		}
		
		// Warnings?
		call_return = false
		Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
					call_return = true
				}
			}
		}
		if (call_return) {
			task.message = getMsg('fc.test.flightplan.resolvewarnings')
			task.error = true
			return task
		}
		
		return task
	}
	
    //--------------------------------------------------------------------------
    Map saveTaskClass(Map params,Task taskInstance,ResultClass resultclassInstance)
    {
        TaskClass taskclass_instance = new TaskClass(params)
        taskclass_instance.task = taskInstance
		taskclass_instance.resultclass = resultclassInstance
		
        if(!taskclass_instance.hasErrors() && taskclass_instance.save()) {
            return ['instance':taskclass_instance,'saved':true,'message':getMsg('fc.created',["${taskclass_instance.resultclass.name}"])]
        } else {
            return ['instance':taskclass_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getTeam(Map params)
    {
        Team team_instance = Team.get(params.id)
        if (!team_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.team'),params.id])]
        }
        return ['instance':team_instance]
    }

    //--------------------------------------------------------------------------
    Map updateTeam(Map params)
    {
        printstart "updateTeam"
        
        Team team_instance = Team.get(params.id)
        if(team_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(team_instance.version > version) {
                    team_instance.errors.rejectValue("version", "team.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':team_instance]
                }
            }

            if (params.name != team_instance.name) {
                Team team_instance2 = Team.findByNameAndContest(params.name,team_instance.contest)
                if (team_instance2) {
                    return ['instance':team_instance2,'error':true,'message':getMsg('fc.team.name.error',["${params.name}"])]
                }
            }
            
            boolean old_disabled = team_instance.disabled
            team_instance.properties = params

            if (old_disabled != team_instance.disabled) {
                println "Team dis/enabled."
                resetPositionTeam(team_instance)
            }
            
            if(!team_instance.hasErrors() && team_instance.save()) {
                String msg = getMsg('fc.updated',["${team_instance.name}"])
                printdone msg
                return ['instance':team_instance,'saved':true,'message':msg]
            } else {
                printerror ""
                return ['instance':team_instance]
            }
        } else {
            printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.team'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createTeam(Map params)
    {
        Team team_instance = new Team()
        team_instance.properties = params
        return ['instance':team_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveTeam(Map params,Contest contestInstance)
    {
        Team team_instance = new Team(params)
        team_instance.contest = contestInstance
        
        if (params.name) {
            Team team_instance2 = Team.findByNameAndContest(params.name,contestInstance)
            if (team_instance2) {
            	return ['instance':team_instance2,'error':true,'message':getMsg('fc.team.name.error',["${params.name}"])]
            }
        }
        
        if(!team_instance.hasErrors() && team_instance.save()) {
            return ['instance':team_instance,'saved':true,'message':getMsg('fc.created',["${team_instance.name}"])]
        } else {
            return ['instance':team_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteTeam(Map params)
    {
        Team team_instance = Team.get(params.id)
        if(team_instance) {
            try {
                deleteTeamInstance(team_instance)
                return ['deleted':true,'message':getMsg('fc.deleted',["${team_instance.name}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.team'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.team'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteTeams(Contest contestInstance, Map params, session)
    {
        printstart "deleteTeams"
        
        int delete_num = 0
         
        Team.findAllByContest(contestInstance,[sort:"name"]).each { Team team_instance ->
            if (params["selectedTeamID${team_instance.id}"] == "on") {
                deleteTeamInstance(team_instance)
                delete_num++
            }
        }
        
        Map ret = ['deleted':delete_num > 0,'message':getMsg('fc.team.deleted',["${delete_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    private void deleteTeamInstance(Team teamInstance)
    {
        println "Team '$teamInstance.name' deleted."
        resetPositionTeam(teamInstance)
        Crew.findAllByTeam(teamInstance,[sort:"id"]).each { Crew crew_instance ->
            crew_instance.team = null
            crew_instance.save()
        }
        teamInstance.delete()
    }
    
    //--------------------------------------------------------------------------
    Map disableTeams(Contest contestInstance, Map params, session)
    {
        printstart "disableTeams"
        
        int disable_num = 0
         
        Team.findAllByContest(contestInstance,[sort:"name"]).each { Team team_instance ->
            if (params["selectedTeamID${team_instance.id}"] == "on") {
                if (!team_instance.disabled) {
                    team_instance.disabled = true
                    println "Team '$team_instance.name' disabled."
                    resetPositionTeam(team_instance)
                    team_instance.save()
                    disable_num++
                }
            }
        }
        
        Map ret = ['disabled':disable_num > 0,'message':getMsg('fc.team.disabled',["${disable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map enableTeams(Contest contestInstance, Map params, session)
    {
        printstart "enableTeams"
        
        int enable_num = 0
         
        Team.findAllByContest(contestInstance,[sort:"name"]).each { Team team_instance ->
            if (params["selectedTeamID${team_instance.id}"] == "on") {
                if (team_instance.disabled) {
                    team_instance.disabled = false
                    println "Team '$team_instance.name' enabled."
                    resetPositionTeam(team_instance)
                    team_instance.save()
                    enable_num++
                }
            }
        }
        
        Map ret = ['enabled':enable_num > 0,'message':getMsg('fc.team.enabled',["${enable_num}"])]
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    private void resetPositionTeam(Team teamInstance)
    {
        teamInstance.contestPenalties = 0
        teamInstance.contestPosition = 0
        teamInstance.contestEqualPosition = false
        teamInstance.contestAddPosition = 0
        for (Team team_instance in Team.findAllByContest(teamInstance.contest,[sort:"id"])) {
            boolean run = false
            if (team_instance != teamInstance) {
                run = true
            }
            if (run) {
                team_instance.contestPenalties = 0
                team_instance.contestPosition = 0
                team_instance.contestEqualPosition = false
                team_instance.contestAddPosition = 0
                team_instance.save()
            }
        }
    }   
     
    //--------------------------------------------------------------------------
    Map selectallTeam(Contest contestInstance)
    {
        Map team = [:] 
        Map selected_teamids = [selectedTeamID:""]
        Team.findAllByContest(contestInstance,[sort:"name"]).each { Team team_instance ->
            selected_teamids["selectedTeamID${team_instance.id}"] = "on"
        }
        team.selectedteamids = selected_teamids
        return team
    }
    
    //--------------------------------------------------------------------------
    Map getResultClass(Map params)
    {
        ResultClass resultclass_instance = ResultClass.get(params.id)
        if (!resultclass_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.resultclass'),params.id])]
        }
        return ['instance':resultclass_instance]
    }

    //--------------------------------------------------------------------------
    Map updateResultClass(String showLanguage, Map params)
    {
		printstart "updateResultClass $params.resultfilter"
		
        ResultClass resultclass_instance = ResultClass.get(params.id)
        if(resultclass_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(resultclass_instance.version > version) {
                    resultclass_instance.errors.rejectValue("version", "resultclass.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':resultclass_instance]
                }
            }

            if (params.name != resultclass_instance.name) {
                ResultClass resultclass_instance2 = ResultClass.findByNameAndContest(params.name,resultclass_instance.contest)
                if (resultclass_instance2) {
					printerror ""
                    return ['instance':resultclass_instance2,'error':true,'message':getMsg('fc.resultclass.name.error',["${params.name}"])]
                }
            }
            
			ContestRules contest_rule = resultclass_instance.contestRule
            Map old_contestrulepoints_values = GetContestRulePointsValues(resultclass_instance)
            
			boolean contest_planning_results = resultclass_instance.contestPlanningResults
			boolean contest_flight_results = resultclass_instance.contestFlightResults
			boolean contest_observation_results = resultclass_instance.contestObservationResults
			boolean contest_landing_results = resultclass_instance.contestLandingResults
			boolean contest_special_results = resultclass_instance.contestSpecialResults
			String contest_task_results = resultclass_instance.contestTaskResults
			String contest_team_results = resultclass_instance.contestTeamResults

            params.secretGateWidth = Languages.GetLanguageDecimal(showLanguage, params.secretGateWidth)
			
            resultclass_instance.properties = params

            resultclass_instance.contestPrintTaskTestDetails = ""
            for (Task task_instance in resultclass_instance.contest.tasks) {
                if (params["tasktestdetails_${task_instance.id}"] == "on") {
                    if (resultclass_instance.contestPrintTaskTestDetails) {
                        resultclass_instance.contestPrintTaskTestDetails += ","
                    }
                    resultclass_instance.contestPrintTaskTestDetails += "tasktestdetails_${task_instance.id}"
                }
            }
            
			switch (params.resultfilter) {
				case ResultFilter.ResultClass.toString():
					resultclass_instance.contestTaskResults = ""
					for (Task task_instance in resultclass_instance.contest.tasks) {
						if (params["task_${task_instance.id}"] == "on") {
							if (resultclass_instance.contestTaskResults) {
								resultclass_instance.contestTaskResults += ","
							}
							resultclass_instance.contestTaskResults += "task_${task_instance.id}"
						}
					}
					resultclass_instance.contestTeamResults = ""
					for (Team team_instance in resultclass_instance.contest.teams) {
						if (params["team_${team_instance.id}"] == "on") {
							if (resultclass_instance.contestTeamResults) {
								resultclass_instance.contestTeamResults += ","
							}
							resultclass_instance.contestTeamResults += "team_${team_instance.id}"
						}
					}
					if (params["team_no_team_crew"] == "on") {
						if (resultclass_instance.contestTeamResults) {
							resultclass_instance.contestTeamResults += ","
						}
						resultclass_instance.contestTeamResults += "team_no_team_crew"
					}
                    if (params["team_all_teams"] == "on") {
                        if (resultclass_instance.contestTeamResults) {
                            resultclass_instance.contestTeamResults += ","
                        }
                        resultclass_instance.contestTeamResults += "team_all_teams"
                    }
					break
			}
					
			boolean modify_contest_rule = resultclass_instance.contestRule != contest_rule

			boolean modify_contest_results = (resultclass_instance.contestPlanningResults != contest_planning_results) ||
			                                 (resultclass_instance.contestFlightResults != contest_flight_results) ||
			 							     (resultclass_instance.contestObservationResults != contest_observation_results) ||
										     (resultclass_instance.contestLandingResults != contest_landing_results) ||
										     (resultclass_instance.contestSpecialResults != contest_special_results) ||
											 (resultclass_instance.contestTaskResults != contest_task_results) ||
											 (resultclass_instance.contestTeamResults != contest_team_results)
											 
			if (modify_contest_rule) {
				println "Contest rule modfified."
				setContestRulePoints(resultclass_instance, resultclass_instance.contestRule)
			}
			
			if (modify_contest_results) {
				println "Contest results modfified."
				for (Crew crew_instance in Crew.findAllByContest(resultclass_instance.contest,[sort:"id"])) {
					if (crew_instance.resultclass.id == resultclass_instance.id) {
						crew_instance.contestPenalties = 0
						crew_instance.classPosition = 0
						crew_instance.noClassPosition = false
                        crew_instance.classEqualPosition = false
                        crew_instance.classAddPosition = 0
						crew_instance.save()
					}
				}
			}
			
            if (IsContestRulePointsValueModified(resultclass_instance,old_contestrulepoints_values)) {
                println "Contest rule value modfified."
			    calculate_points_resultclass(resultclass_instance)
			}

            if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
                Map ret = ['instance':resultclass_instance,'saved':true,'message':getMsg('fc.updated',["${resultclass_instance.name}"])]
				printdone ret
				return ret
            } else {
				printerror ""
                return ['instance':resultclass_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.resultclass'),params.id])]
			printerror ret
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map calculatepointsResultClass(Map params)
    {
        ResultClass resultclass_instance = ResultClass.get(params.id)
        if(resultclass_instance) {
			calculate_points_resultclass(resultclass_instance)
            return ['instance':resultclass_instance,'message':getMsg('fc.contestrule.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.resultclass'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
	private void calculate_points_resultclass(ResultClass resultclassInstance)
	{
		printstart "calculate_points_resultclass: $resultclassInstance.name"
		Task.findAllByContest(resultclassInstance.contest,[sort:"idTitle"]).each { Task task_instance ->
			Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
				if (test_instance.crew.resultclass.id == resultclassInstance.id) {
					calculateTestPenalties(test_instance,true)
                    test_instance.flightTestLink = ""
					delete_uploadjobtest(test_instance)
                    test_instance.crewResultsModified = true
					test_instance.save()
				}
			}
		}
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map createResultClass(Map params, Contest contestInstance)
    {
        ResultClass resultclass_instance = new ResultClass()
        resultclass_instance.properties = params
		resultclass_instance.contestRule = contestInstance.contestRule
        return ['instance':resultclass_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveResultClass(Map params,Contest contestInstance)
    {
        if (params.name) {
            ResultClass resultclass_instance2 = ResultClass.findByNameAndContest(params.name,contestInstance)
            if (resultclass_instance2) {
            	return ['instance':resultclass_instance2,'error':true,'message':getMsg('fc.resultclass.name.error',["${params.name}"])]
            }
        }
        
        ResultClass resultclass_instance = new ResultClass(params)
        resultclass_instance.contest = contestInstance
		resultclass_instance.contestTitle = params.contestTitle
		if (!params.shortName) {
			resultclass_instance.shortName = resultclass_instance.GetDefaultShortName()
		}
		setContestRulePoints(resultclass_instance, resultclass_instance.contestRule)
		
        if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
			// create TaskClasses
			if (contestInstance.resultClasses) {
				for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"idTitle"])) {
					TaskClass taskclass_instance = new TaskClass()
					taskclass_instance.task = task_instance
					taskclass_instance.resultclass = resultclass_instance
					taskclass_instance.save()
				}
			}
            return ['instance':resultclass_instance,'saved':true,'message':getMsg('fc.created',["${resultclass_instance.name}"])]
        } else {
            return ['instance':resultclass_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map standardpointsResultClass(Map params)
    {
        ResultClass resultclass_instance = ResultClass.get(params.id)
        Map old_contestrulepoints_values = GetContestRulePointsValues(resultclass_instance)
        
        setContestRulePoints(resultclass_instance, resultclass_instance.contestRule)
        if (IsContestRulePointsValueModified(resultclass_instance,old_contestrulepoints_values)) {
            calculate_points_resultclass(resultclass_instance)
        }
        
        if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
            return ['instance':resultclass_instance,'saved':true,'message':getMsg('fc.created',["${resultclass_instance.name}"])]
        } else {
            return ['instance':resultclass_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteResultClass(Map params)
    {
        ResultClass resultclass_instance = ResultClass.get(params.id)
        if(resultclass_instance) {
            try {
				TaskClass.findAllByResultclass(resultclass_instance,[sort:"id"]).each { TaskClass taskclass_instance ->
					taskclass_instance.delete()
				}
                Crew.findAllByResultclass(resultclass_instance,[sort:"id"]).each { Crew crew_instance ->
                    crew_instance.resultclass = null
                    crew_instance.save()
                }
                resultclass_instance.delete()
                return ['deleted':true,'message':getMsg('fc.deleted',["${resultclass_instance.name}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.resultclass'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.resultclass'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startresultsResultClass(Map params,contestInstance,lastResultClassResults)
    {
        ResultClass resultclass_instance = null
        if (lastResultClassResults) {
            resultclass_instance = ResultClass.findById(lastResultClassResults)
        }
        if (!resultclass_instance) {
			if (contestInstance) {
	            ResultClass.findAllByContest(contestInstance,[sort:"id"]).each {
	                if (!resultclass_instance) {
	                    resultclass_instance = it
	                }
	            }
			}
        }
        if (resultclass_instance) {
            return ['resultclassid':resultclass_instance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        
        if (route_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(route_instance.version > version) {
                    route_instance.errors.rejectValue("version", "route.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':route_instance]
                }
            }
            
            route_instance.showCoords = params.showCoords == "on"
            route_instance.showCoordObservations = params.showCoordObservations == "on"
            route_instance.showResultLegs = params.showResultLegs == "on"
            route_instance.showTestLegs = params.showTestLegs == "on"
            route_instance.showEnroutePhotos = params.showEnroutePhotos == "on"
            route_instance.showEnrouteCanvas = params.showEnrouteCanvas == "on"
            route_instance.properties = params
            
            if(!route_instance.hasErrors() && route_instance.save()) {
                return ['instance':route_instance,'saved':true,'message':getMsg('fc.updated',["${route_instance.name()}"])]
            } else {
                return ['instance':route_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createRoute(Map params, Contest contestInstance)
    {
        Route route_instance = new Route()
        route_instance.properties = params
        route_instance.useProcedureTurns = contestInstance.useProcedureTurns
        route_instance.liveTrackingScorecard = contestInstance.liveTrackingScorecard
        route_instance.turnpointRoute = contestInstance.turnpointRule.GetTurnpointRoute()
        route_instance.turnpointMapMeasurement = contestInstance.turnpointMapMeasurement
        route_instance.enroutePhotoMeasurement = contestInstance.enroutePhotoRule.GetEnrouteMeasurement()
        route_instance.enroutePhotoRoute = route_instance.enroutePhotoMeasurement.GetEnrouteRoute()
        route_instance.enrouteCanvasMeasurement = contestInstance.enrouteCanvasRule.GetEnrouteMeasurement()
        route_instance.enrouteCanvasRoute = route_instance.enrouteCanvasMeasurement.GetEnrouteRoute()
        return ['instance':route_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveRoute(Map params,Contest contestInstance)
    {
        Route route_instance = new Route(params)
        
        route_instance.contest = contestInstance
        route_instance.idTitle = Route.countByContest(contestInstance) + 1
        
        if (!route_instance.liveTrackingScorecard) {
            route_instance.liveTrackingScorecard = contestInstance.liveTrackingScorecard
        }
        
        String error_messages = ""
        if (route_instance.turnpointRoute == TurnpointRoute.Unassigned) {
            if (error_messages) {
                error_messages += ", "
            }
            error_messages += getMsg('fc.route.turnpointnotassigned')
        }
        if (route_instance.enroutePhotoMeasurement == EnrouteMeasurement.Unassigned || route_instance.enroutePhotoRoute == EnrouteRoute.Unassigned) {
            if (error_messages) {
                error_messages += ", "
            }
            error_messages += getMsg('fc.route.photomeasurementnotassigned')
        }
        if (route_instance.enrouteCanvasMeasurement == EnrouteMeasurement.Unassigned || route_instance.enrouteCanvasRoute == EnrouteRoute.Unassigned) {
            if (error_messages) {
                error_messages += ", "
            }
            error_messages += getMsg('fc.route.canvasmeasurementnotassigned')
        }
        
        if (error_messages) {
            return ['instance':route_instance,'error':true,'message':error_messages]
        } else if (!route_instance.hasErrors() && route_instance.save()) {
            return ['instance':route_instance,'saved':true,'message':getMsg('fc.created',["${route_instance.name()}"])]
        } else {
            return ['instance':route_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteRoute(Map params)
    {
		printstart "deleteRoute"
        Route route_instance = Route.get(params.id)
        if (route_instance) {
			if (route_instance.Used()) {
				Map ret = ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.route'),params.id])]
				printerror ret.message
				return ret
			}
            try {
				Contest contest_instance = route_instance.contest
                route_instance.delete()
                
                Route.findAllByContest(contest_instance,[sort:"idTitle"]).eachWithIndex { Route route_instance2, int index -> 
                    route_instance2.idTitle = index + 1
                }
                
                Map ret = ['deleted':true,'message':getMsg('fc.deleted',["${route_instance.name()}"])]
				printdone ret.message
				return ret
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                Map ret = ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.route'),params.id])]
				printerror ret.message
				return ret
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    void changeviewpositionRoute(Map params, boolean addViewPos)
    {
		printstart "changeviewpositionRoute addViewPos=$addViewPos"
        Route route_instance = Route.get(params.id.toLong())
        if (route_instance) {
            boolean task_found = false
            if (addViewPos) {
                for (Route route_instance2 in Route.findAllByContest(route_instance.contest,[sort:"idTitle"])) {
                    if (task_found) {
                        route_instance2.idTitle--
                        route_instance2.save()
                        task_found = false
                    } else if (route_instance2.id == route_instance.id) {
                        task_found = true
                    }
                }
                route_instance.idTitle++
                route_instance.save()
            } else {
                for (Route route_instance2 in Route.findAllByContest(route_instance.contest,[sort:"idTitle", order: "desc"])) {
                    if (task_found) {
                        route_instance2.idTitle++
                        route_instance2.save()
                        task_found = false
                    } else if (route_instance2.id == route_instance.id) {
                        task_found = true
                    }
                }
                route_instance.idTitle--
                route_instance.save()
            }
        }
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    Map copyRoute(Map params)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return route
        }

        Route new_route_instance = new Route()
		new_route_instance.contest = route.instance.contest
		new_route_instance.CopyValues(route.instance)
		new_route_instance.title = getRouteCopyTitle(route.instance)
        new_route_instance.idTitle = Route.countByContest(route.instance.contest)
        if(!new_route_instance.hasErrors() && new_route_instance.save()) {
            return ['instance':new_route_instance,'saved':true,'message':getMsg('fc.created',["${new_route_instance.title}"])]
        } else {
            return ['instance':new_route_instance]
        }
    }
    
    //--------------------------------------------------------------------------
	String getRouteCopyTitle(Route sourceRouteInstance)
	{
		String new_title = sourceRouteInstance.title
		if (!new_title) {
			return ""
		}
		
		if ( new_title.lastIndexOf(')') + 1 == new_title.length())
		{
			int i1 = new_title.lastIndexOf('(')
			if (i1 > 0) {
				new_title = new_title.substring(0,i1 - 1)
			}
		}
		new_title = getMsg('fc.contest.copytitle',["$new_title"])
		
		String new_title2 = new_title 
		int found_num = 1
		while (sourceRouteInstance.contest.RouteTitleFound(new_title2)) {
			found_num++
			new_title2 = "$new_title ($found_num)"
		}
		return new_title2
	}
	
    //--------------------------------------------------------------------------
    Map caculateroutelegsRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        
        if (route_instance) {
			if (route_instance.Used()) {
				return ['instance':route_instance,'error':true,'message':getMsg('fc.routeleg.calculatenotallowed.routeused')]
			}
            printstart "caculateroutelegsRoute"
            renumberCoordRoute(route_instance)
			calculateAllLegMeasureDistances(route_instance)
            calculateSecretLegRatio(route_instance)
        	calculateRouteLegs(route_instance)
            calculateEnrouteValues(route_instance)
            printdone ""
            return ['instance':route_instance,'calculated':true,'message':getMsg('fc.routeleg.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map recalculateResultsTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        boolean no_remove_existing_data = params?.no_remove_existing_data == "on"
        String loggerdata_startutc = params?.loggerdata_startutc
        String loggerdata_endutc = params?.loggerdata_endutc
        return recalculateResults(test_instance, loggerdata_startutc, loggerdata_endutc, no_remove_existing_data)
    }

    //--------------------------------------------------------------------------
    private Map recalculateResults(Test testInstance, String loggerDataStartUtc, String loggerDataEndUtc, boolean noRemoveExistingData)
    {
        printstart "recalculateResults: crew '$testInstance.crew.name', startutc $loggerDataStartUtc, endutc $loggerDataEndUtc, noRemoveExistingData $noRemoveExistingData"
        
        calcService.Calculate(testInstance, loggerDataStartUtc, loggerDataEndUtc)
        
        boolean errors = false
        boolean no_flightresults = false
        if (!testInstance.IsLoggerResult()) {
            if (!noRemoveExistingData) {
                set_noflightresults(testInstance)
            }
            no_flightresults = true
        } else {
            errors = importResults(testInstance, noRemoveExistingData, loggerDataStartUtc, loggerDataEndUtc)
        }

        testInstance.task.liveTrackingTracksAvailable = false
        testInstance.task.save()
        
        if (no_flightresults) {
            Map ret = ['error':true,'saved':true,'message':getMsg('fc.flightresults.recalculate.nologgerdata',[testInstance.crew.name])]
            printerror ret.message
            return ret
        } else if (errors) {
            Map ret = ['saved':true,'message':getMsg('fc.flightresults.recalculate.flightfailures',[testInstance.crew.name])]
            printdone ret.message
            return ret
        } else {
            Map ret = ['saved':true,'message':getMsg('fc.flightresults.recalculate.flightok',[testInstance.crew.name])]
            printdone ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map importDemoFcRoute(String fileExtension, Contest contestInstance, String originalFileName)
    {
        printstart "importDemoFcRoute '$originalFileName'"
        
        Map ret = [saved: false, error: false, message: "", instance: null]

        String webroot_dir = servletContext.getRealPath("/")
        
        // read file
        Map reader = import_fc_route(fileExtension, contestInstance, webroot_dir + "testdata/" + originalFileName)
        
        if (!reader.valid) {
            ret.error = true
            ret.message = getMsg('fc.route.fcfileimport.invalidroutefile',[originalFileName])
            printerror ret.message
        } else if (reader.errors) {
            ret.error = true
            ret.message = getMsg('fc.route.fcfileimport.readerrors',[originalFileName, reader.errors])
            printerror ret.message
        } else if (!reader.gatenum) {
            ret.error = true
            ret.message = getMsg('fc.route.fcfileimport.noroutedata',[originalFileName])
            printerror ret.message
        } else {
            ret.message = getMsg('fc.route.fcfileimport.routeok',[originalFileName])
            ret.saved =  true
            ret.instance = reader.route
            printdone ret.message
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map importFcRoute(String fileExtension, Contest contestInstance, def file)
    {
        Map ret = [found: false, error: false, message: ""]
        
        String original_filename = file.getOriginalFilename()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.route.fcfileimport.noroutefile',[original_filename, RouteFileTools.FC_ROUTE_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.route.fcfileimport.nofile')
        } else {
            printstart "importFcRoute '$original_filename'"
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                
                ret.found = true
                
                // upload file
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}-UPLOAD${fileExtension}"
                printstart "Upload $original_filename -> $upload_filename"
                file.transferTo(new File(webroot_dir, upload_filename))
                printdone ""
                
                // read file
                Map reader = import_fc_route(fileExtension, contestInstance, webroot_dir + upload_filename)
                
                // delete file
                DeleteFile(webroot_dir + upload_filename)
                
                if (!reader.valid) {
                    ret.error = true
                    ret.message = getMsg('fc.route.fcfileimport.invalidroutefile',[original_filename])
                } else if (reader.errors) {
                    ret.error = true
                    ret.message = getMsg('fc.route.fcfileimport.readerrors',[original_filename, reader.errors])
                } else if (!reader.gatenum) {
                    ret.error = true
                    ret.message = getMsg('fc.route.fcfileimport.noroutedata',[original_filename])
                } else {
                    ret.message = getMsg('fc.route.fcfileimport.routeok',[original_filename])
                }
            }
            printdone ""
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map import_fc_route(String fileExtension, Contest contestInstance, String loadFileName)
    {
        printstart "import_fc_route '$loadFileName'"
        
        // read file
        Map reader = RouteFileTools.ReadFcRouteFile(fileExtension, contestInstance, loadFileName)
        
        // calculate legs
        if (reader.valid && !reader.errors && reader.route) {
            printstart "Calculate legs"
            try {
                calculateAllLegMeasureDistances(reader.route)
                calculateSecretLegRatio(reader.route)
                calculateRouteLegs(reader.route)
                calculateEnrouteValues(reader.route)
                printdone ""
            } catch (Exception e) {
                reader.errors = e.getMessage()
                printerror reader.errors
            }
            printdone ""
        } else {
            printerror reader
        }
        
        return reader
    }
    
    //--------------------------------------------------------------------------
    Map importFileRoute(String fileExtension, Contest contestInstance, def file, Map importParams)
    {
        Map ret = [found: false, error: false, message: ""]
        
        String original_filename = file.getOriginalFilename()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.route.fileimport.noroutefile',[original_filename, RouteFileTools.FC_ROUTE_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.route.fileimport.nofile')
        } else {
            printstart "importFileRoute '$original_filename'"
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                
                ret.found = true
                
                // upload file
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ROUTE-${uuid}-UPLOAD${fileExtension}"
                printstart "Upload $original_filename -> $upload_filename"
                file.transferTo(new File(webroot_dir, upload_filename))
                printdone ""
                
                // read file
                Map reader = import_file_route(fileExtension, contestInstance, webroot_dir + upload_filename, original_filename, importParams)
                
                // delete file
                DeleteFile(webroot_dir + upload_filename)
                
                if (!reader.valid) {
                    ret.error = true
                    ret.message = getMsg('fc.route.fileimport.invalidroutefile',[original_filename])
                } else if (reader.errors) {
                    ret.error = true
                    ret.message = getMsg('fc.route.fileimport.readerrors',[original_filename, reader.errors])
                } else if (!reader.gatenum) {
                    ret.error = true
                    ret.message = getMsg('fc.route.fileimport.noroutedata',[original_filename])
                } else {
                    ret.message = getMsg('fc.route.fileimport.routeok',[original_filename])
                }
            }
            printdone ""
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map import_file_route(String fileExtension, Contest contestInstance, String loadFileName, String originalFileName, Map importParams)
    {
        printstart "import_file_route '$loadFileName' $importParams"
        
        // read file
        Map reader = RouteFileTools.ReadRouteFile(fileExtension, contestInstance, loadFileName, originalFileName, importParams)
        
        // calculate legs
        if (reader.valid && !reader.errors && reader.route) {
            printstart "Calculate legs"
            try {
                calculateAllLegMeasureDistances(reader.route)
                calculateSecretLegRatio(reader.route)
                calculateRouteLegs(reader.route)
                calculateEnrouteValues(reader.route)
                printdone ""
            } catch (Exception e) {
                reader.errors = e.getMessage()
                printerror reader.errors
            }
        }
        
        printdone ""
        return reader
    }
    
    //--------------------------------------------------------------------------
    Map importSignFile(String fileExtension, Route routeInstance, def file, ImportSign importSign, String folderName, String namePrefix)
    {
        Map ret = [found: false, error: false, message: ""]
        
        String original_filename = file.getOriginalFilename()
        if (!fileExtension) {
            ret.error = true
            if (importSign.IsEnroutePhoto() || importSign.IsEnrouteCanvas()) {
                ret.message = getMsg('fc.route.signfileimport.nosignfile',[original_filename, RouteFileTools.ENROUTE_SIGN_EXTENSIONS])
            } else {
                ret.message = getMsg('fc.route.signfileimport.nosignfile',[original_filename, RouteFileTools.TURNPOINT_EXTENSIONS])
            }
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.route.signfileimport.nofile')
        } else {
            printstart "importSignFile '$original_filename' Prefix='$namePrefix'"
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                
                ret.found = true
                
                // upload file
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ENROUTE-${uuid}-UPLOAD${fileExtension}"
                printstart "Upload $original_filename -> $upload_filename"
                file.transferTo(new File(webroot_dir, upload_filename))
                printdone ""
                
                // read file
                Map reader = import_sign_file(fileExtension, routeInstance, webroot_dir + upload_filename, original_filename, importSign, folderName, namePrefix)
                
                // delete file
                DeleteFile(webroot_dir + upload_filename)
                
                if (!reader.valid) {
                    ret.error = true
                    ret.message = getMsg('fc.route.signfileimport.invalidsignfile',[original_filename])
                } else if (reader.invalidlinenum) {
                    ret.error = true
                    ret.message = getMsg('fc.route.signfileimport.anyinvalidline',[original_filename, reader.invalidlinenum, reader.errors])
                } else if (reader.errors) {
                    ret.error = true
                    ret.message = getMsg('fc.route.signfileimport.readerrors',[original_filename, reader.errors])
                } else if (!reader.importedsignnum) {
                    ret.error = true
                    ret.message = getMsg('fc.route.signfileimport.nosigndata',[original_filename])
                } else {
                    ret.message = getMsg('fc.route.signfileimport.imported',[original_filename, reader.importedsignnum, reader.filesignnum])
                }
            }
            printdone ""
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map import_sign_file(String fileExtension, Route routeInstance, String loadFileName, String originalFileName, ImportSign importSign, String folderName, String namePrefix)
    {
        printstart "import_sign_file '$loadFileName' Folder='$folderName' Prefix='$namePrefix'"
        
        // read file
        Map reader = RouteFileTools.ReadImportSignFile(fileExtension, routeInstance, loadFileName, originalFileName, importSign, folderName, namePrefix)
        
        // calculate legs
        if (importSign == ImportSign.RouteCoord) {
            if (reader.valid && !reader.errors) {
                printstart "Calculate legs"
                try {
                    calculateAllLegMeasureDistances(routeInstance)
                    calculateSecretLegRatio(routeInstance)
                    calculateRouteLegs(routeInstance)
                    //calculateEnrouteValues(routeInstance)
                    printdone ""
                } catch (Exception e) {
                    reader.errors = e.getMessage()
                    printerror reader.errors
                }
            }
        }
        
        printdone ""
        return reader
    }
    
    //--------------------------------------------------------------------------
    Map importTurnpointPhotos(Map params, MultipartFile zipFile)
    {
        Map ret = [importNum:0, updateNum:0]
        
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return ret
        }

        // upload zip file
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ZIP-${uuid}-UPLOAD.zip"
        printstart "Upload ${zipFile.getOriginalFilename()} -> $upload_filename"
        zipFile.transferTo(new File(webroot_dir, upload_filename))
        printdone ""
        
        // read photos from zip file
        def zip_file = new java.util.zip.ZipFile(webroot_dir + upload_filename)
        zip_file.entries().findAll { !it.directory }.each {
            if (!it.isDirectory()) {
                String name = it.name.substring(0,it.name.indexOf("."))
                if (name) {
                    def photo_stream = zip_file.getInputStream(it)
                    
                    byte[] buff = new byte[8000]
                    int bytes_read = 0
                    ByteArrayOutputStream photo_stream2 = new ByteArrayOutputStream()
                    while((bytes_read = photo_stream.read(buff)) != -1) {
                        photo_stream2.write(buff, 0, bytes_read)
                    }

                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
                        if (coordroute_instance.type.IsTurnpointSignCoord()) {
                            if (name == coordroute_instance.titleExport()) {
                                if (!coordroute_instance.imagecoord) {
                                    ImageCoord imagecoord_instance = new ImageCoord(imageData:photo_stream2.toByteArray(), coord:coordroute_instance)
                                    imagecoord_instance.save()
                                    coordroute_instance.imagecoord = imagecoord_instance
                                    coordroute_instance.save()
                                } else {
                                    coordroute_instance.imagecoord.imageData = photo_stream2.toByteArray()
                                    coordroute_instance.imagecoord.save()
                                }
                                ret.importNum++
                                break
                            }
                        }
                    }
                }
            }
        }
        zip_file.close()
        
        // delete file
        DeleteFile(webroot_dir + upload_filename)
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map importEnroutePhotos(Map params, MultipartFile zipFile)
    {
        Map ret = [importNum:0, updateNum:0]
        
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return ret
        }

        // upload zip file
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ZIP-${uuid}-UPLOAD.zip"
        printstart "Upload ${zipFile.getOriginalFilename()} -> $upload_filename"
        zipFile.transferTo(new File(webroot_dir, upload_filename))
        printdone ""
        
        // read photos from zip file
        def zip_file = new java.util.zip.ZipFile(webroot_dir + upload_filename)
        zip_file.entries().findAll { !it.directory }.each {
            if (!it.isDirectory()) {
                String name = it.name.substring(0,it.name.indexOf("."))
                if (name) {
                    def photo_stream = zip_file.getInputStream(it)
                    
                    byte[] buff = new byte[8000]
                    int bytes_read = 0
                    ByteArrayOutputStream photo_stream2 = new ByteArrayOutputStream()
                    while((bytes_read = photo_stream.read(buff)) != -1) {
                        photo_stream2.write(buff, 0, bytes_read)
                    }

                    for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route.instance,[sort:"id"])) {
                        if (name == coordenroutephoto_instance.enroutePhotoName) {
                            if (!coordenroutephoto_instance.imagecoord) {
                                ImageCoord imagecoord_instance = new ImageCoord(imageData:photo_stream2.toByteArray(), coord:coordenroutephoto_instance)
                                imagecoord_instance.save()
                            
                                coordenroutephoto_instance.imagecoord = imagecoord_instance
                                coordenroutephoto_instance.save()
                            } else {
                                coordenroutephoto_instance.imagecoord.imageData = photo_stream2.toByteArray()
                                coordenroutephoto_instance.imagecoord.save()
                            }
                            ret.importNum++
                            break
                        }
                    }
                }
            }
        }
        zip_file.close()
        
        // delete file
        DeleteFile(webroot_dir + upload_filename)
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map importLoggerResultTest(String fileExtension, Test testInstance, String originalFileName)
    {
        printstart "importLoggerResultTest '$originalFileName'"
        
        Map ret = [saved: false, error: false, message: "", instance: null]

        String webroot_dir = servletContext.getRealPath("/")
        
        // read file
        Map reader = import_test(fileExtension, testInstance, webroot_dir + "testdata/" + originalFileName, false, true, 0)
        
        if (!reader.valid) {
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.invalidloggerfile',[originalFileName])
            printerror ret.message
        } else if (reader.errors) {
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.readerrors',[originalFileName, reader.errors])
            printerror ret.message
        } else if (reader.noflightresults) {
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nologgerdata',[originalFileName])
            printerror ret.message
        } else if (reader.flightfailures) {
            ret.message =  getMsg('fc.flightresults.loggerimport.flightfailures',[testInstance.crew.name])
            printdone ret.message
        } else {
            ret.message = getMsg('fc.flightresults.loggerimport.flightok',[originalFileName])
            printdone ret.message
        }
        
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map calculateLoggerResultTest(String fileExtension, Test testInstance, def file, boolean noRemoveExistingData, boolean interpolateMissingData, int correctSeconds)
    // fileExtension - '.gac', '.gpx'
    // Return        - found = true, wenn zutreffende Logger-Datei  
    {
        Map ret = [found: false, error: false, message: ""]
        
        String original_filename = file.getOriginalFilename()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nologgerfile',[original_filename, LoggerFileTools.LOGGER_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nofile')
        } else {
            printstart "calculateLoggerResultTest '$original_filename'"
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                
                ret.found = true
                
                // upload file
                String uuid = UUID.randomUUID().toString()
                String webroot_dir = servletContext.getRealPath("/")
                String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/LOGGER-${uuid}-UPLOAD${fileExtension}"
                printstart "Upload $original_filename -> $upload_filename"
                file.transferTo(new File(webroot_dir, upload_filename))
                printdone ""
                
                // read file
                Map reader = import_test(fileExtension, testInstance, webroot_dir + upload_filename, noRemoveExistingData, interpolateMissingData, correctSeconds)
                
                // delete file
                DeleteFile(webroot_dir + upload_filename)
                
                if (!reader.valid) {
                    ret.error = true
                    ret.message = getMsg('fc.flightresults.loggerimport.invalidloggerfile',[original_filename])
                } else if (reader.errors) {
                    ret.error = true
                    ret.message = getMsg('fc.flightresults.loggerimport.readerrors',[original_filename, reader.errors])
                } else if (reader.noflightresults) {
                    ret.error = true
                    ret.message = getMsg('fc.flightresults.loggerimport.nologgerdata',[original_filename])
                } else if (reader.flightfailures) {
                    ret.message =  getMsg('fc.flightresults.loggerimport.flightfailures',[testInstance.crew.name])
                } else {
                    ret.message = getMsg('fc.flightresults.loggerimport.flightok',[original_filename])
                }
            }
            printdone ""
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map calculateLoggerResultExternTest(String fileExtension, Test testInstance, String fileName, boolean removeFile, boolean interpolateMissingData, int correctSeconds)
    // fileExtension - '.gac', '.gpx'
    // Return        - found = true, wenn zutreffende Logger-Datei  
    {
        Map ret = [found: false, error: false, message: ""]
        
        String original_filename = fileName // file.getOriginalFilename()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nologgerfile',[original_filename, LoggerFileTools.LOGGER_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nofile')
        } else {
            printstart "calculateLoggerResultExternTest '$original_filename' remove=${removeFile}"
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                ret.found = true
                printdone ""
                
                // read file
                Map reader = import_test(fileExtension, testInstance, original_filename, false, interpolateMissingData, correctSeconds)
                
                if (!reader.valid) {
                    ret.error = true
                    ret.message = getMsg('fc.flightresults.loggerimport.invalidloggerfile',[original_filename])
                } else if (reader.errors) {
                    ret.error = true
                    ret.message = getMsg('fc.flightresults.loggerimport.readerrors',[original_filename, reader.errors])
                } else if (reader.noflightresults) {
                    ret.error = true
                    ret.message = getMsg('fc.flightresults.loggerimport.nologgerdata',[original_filename])
                } else if (reader.flightfailures) {
                    ret.message =  getMsg('fc.flightresults.loggerimport.flightfailures',[testInstance.crew.name])
                } else {
                    ret.message = getMsg('fc.flightresults.loggerimport.flightok',[original_filename])
                }
                
                if (removeFile) {
                    DeleteFile(fileName)
                }
            }
            printdone ""
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map import_test(String fileExtension, Test testInstance, String loadFileName, boolean noRemoveExistingData, boolean interpolateMissingData, int correctSeconds)
    {
        printstart "import_test '$loadFileName'"
        
        // read file
        Map reader = calcService.CalculateLoggerFile(fileExtension, testInstance, loadFileName, interpolateMissingData, correctSeconds)
                
        boolean no_flightresults = false
        boolean flight_failures = false
        if (reader.valid && !reader.errors) {
            if (!reader.trackpointnum) {
                no_flightresults = true
            } else if (!testInstance.IsLoggerResult()) {
                if (!noRemoveExistingData) {
                    set_noflightresults(testInstance)
                }
                no_flightresults = true
            } else {
                flight_failures = importResults(testInstance, noRemoveExistingData, testInstance.GetLoggerDataFirstUtc(), testInstance.GetLoggerDataLastUtc())
            }
            testInstance.task.liveTrackingTracksAvailable = false
            testInstance.task.save()
        }
        reader.noflightresults = no_flightresults
        reader.flightfailures = flight_failures
    
        printdone ""
        return reader
    }
    
    //--------------------------------------------------------------------------
    Map ImportResults(Test testInstance, boolean noRemoveExistingData, int trackPointNum) 
    {
        Map ret = [no_flightresults:false, flight_failures:false]
        if (!trackPointNum) {
            ret.no_flightresults = true
        } else if (!testInstance.IsLoggerResult()) {
            if (!noRemoveExistingData) {
                set_noflightresults(testInstance)
            }
            ret.no_flightresults = true
        } else {
            ret.flight_failures = importResults(testInstance, noRemoveExistingData, testInstance.GetLoggerDataFirstUtc(), testInstance.GetLoggerDataLastUtc())
        }
        testInstance.task.liveTrackingTracksAvailable = false
        testInstance.task.save()
        return ret
    }
    
    //--------------------------------------------------------------------------
    private boolean importResults(Test testInstance, boolean noRemoveExistingData, String loggerDataStartUtc, String loggerDataEndUtc)
    // Return true - Errors
    {
        printstart "importResults"
        
        // Import CheckPoints
        printstart "Import check points"
        int checkpoint_errors = 0
        int height_errors = 0
        int badcourse_seconds = 0
        int course_errors = 0
        CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
            if (!coordresult_instance.ignoreGate) {
                boolean found = false
                for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                    if (calcresult_instance.IsCoordTitleEqual(coordresult_instance.type, coordresult_instance.titleNumber)) {
                        found = true
                        
                        // get altitude
                        get_altitude_from_route(coordresult_instance)

                        // reset results
                        coordresult_instance.ResetResults(true) // true - with procedure turn

                        // set results	        			
                        coordresult_instance.resultCpTimeInput = calcresult_instance.GetUTCTime()
                        coordresult_instance.resultLatitude = calcresult_instance.GetLatitudeStr()
                        coordresult_instance.resultLongitude = calcresult_instance.GetLongitudeStr()
                        coordresult_instance.resultAltitude = calcresult_instance.altitude
                        if ((calcresult_instance.gateNotFound || calcresult_instance.gateMissed) && !calcresult_instance.judgeDisabled) {
                            coordresult_instance.resultCpNotFound = true
                            checkpoint_errors++
                        }
                        
                        // calculate results
                        if (coordresult_instance.planProcedureTurn) {
                            coordresult_instance.resultProcedureTurnEntered = true
                        }
                        calculateCoordResultInstance(coordresult_instance,true,false)
                        
                        // calculate verify values
                        if (!calcresult_instance.judgeDisabled) {
                            if (!(calcresult_instance.gateNotFound || calcresult_instance.gateMissed) && coordresult_instance.resultMinAltitudeMissed) {
                                height_errors++
                            }
                        }
                        
                        // save results
                        coordresult_instance.save()
                        
                        // log
                        if (coordresult_instance.planProcedureTurn) {
                            println "PROCEDURE TURN"
                        }
                        println "CheckPoint found ${coordresult_instance.title()}"
                    }
                }
                if (!found && !noRemoveExistingData) {
                    // reset results
                    coordresult_instance.ResetResults(true) // true - with procedure turn
                    
                    // calculate results
                    coordresult_instance.resultCpNotFound = true
                    if (coordresult_instance.planProcedureTurn) {
                        coordresult_instance.resultProcedureTurnEntered = true
                    }
                    calculateCoordResultInstance(coordresult_instance,true,false)
                    
                    // calculate verify values
                    checkpoint_errors++
                    
                    // save results
                    coordresult_instance.save()
                    
                    // log
                    if (coordresult_instance.planProcedureTurn) {
                        println "PROCEDURE TURN"
                    }
                    println "CheckPoint not found ${coordresult_instance.title()}"
                }
            }
    	}
        printdone ""
    	
    	// Import ErrorPoints
        printstart "Import error points"
        for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
            
			// Bad Course
			if (calcresult_instance.badCourse && calcresult_instance.badCourseSeconds && !calcresult_instance.judgeDisabled) {
                if (calcresult_instance.badCourseSeconds > testInstance.GetFlightTestBadCourseCorrectSecond()) {
                    println "BadCourse found (${calcresult_instance.utc}, ${calcresult_instance.badCourseSeconds}s)"
    				if (processAflosErrorPointBadCourse(testInstance, calcresult_instance.badCourseSeconds, calcresult_instance.GetUTCTime())) {
    					course_errors++
    				}
                } else {
                    println "BadCourse not relevant (${calcresult_instance.utc}, ${calcresult_instance.badCourseSeconds}s <= ${testInstance.GetFlightTestBadCourseCorrectSecond()})."
                }
			}
			
			//  Bad Turn
    		if (calcresult_instance.badTurn && !calcresult_instance.judgeDisabled) {
    			processAflosErrorPointBadTurn(testInstance, calcresult_instance.GetUTCTime())
                println "BadTurn found (${calcresult_instance.utc})."
    		}
    	}
        printdone ""
        
		if (testInstance.IsFlightTestCheckTakeOff() || testInstance.GetFlightTestTakeoffCheckSeconds()) {
			testInstance.flightTestTakeoffMissed = false
		}
		if (testInstance.IsFlightTestCheckLanding()) {
			testInstance.flightTestLandingTooLate = false
		}

        testInstance.flightTestModified = true
        testInstance.flightTestLink = ""
		delete_uploadjobtest(testInstance)
        testInstance.crewResultsModified = true
        testInstance.loggerDataStartUtc = loggerDataStartUtc
        testInstance.loggerDataEndUtc = loggerDataEndUtc
        
    	// Penalties berechnen
        calculateTestPenalties(testInstance,false)
        testInstance.save()

        printdone ""
        return checkpoint_errors || height_errors || course_errors
    }

    //--------------------------------------------------------------------------
    private void get_altitude_from_route(CoordResult coordResultInstance)
    {
        CoordRoute coordroute_instance = CoordRoute.findByRouteAndTypeAndTitleNumber(coordResultInstance.test.flighttestwind.flighttest.route, coordResultInstance.type, coordResultInstance.titleNumber)
        if (coordroute_instance) {
            coordResultInstance.altitude = coordroute_instance.altitude
            coordResultInstance.minAltitudeAboveGround = coordroute_instance.minAltitudeAboveGround
            coordResultInstance.maxAltitudeAboveGround = coordroute_instance.maxAltitudeAboveGround
        }
    }
    
    //--------------------------------------------------------------------------
    private boolean processAflosErrorPointBadCourse(Test testInstance, int badCourseSeconds, String badCourseStartTimeUTC)
    {
    	boolean course_error = false
    	Contest contest_instance = testInstance.task.contest
    	
        Date badcourse_starttime = Date.parse("HH:mm:ss", badCourseStartTimeUTC)
        GregorianCalendar badcourse_startcalendar = new GregorianCalendar()
        badcourse_startcalendar.setTime(badcourse_starttime)
        
        Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        GregorianCalendar timezone_calendar = new GregorianCalendar()
        timezone_calendar.setTime(timezone_date)
        
        badcourse_startcalendar.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
		if (contest_instance.timeZone.startsWith("-")) {
			badcourse_startcalendar.add(Calendar.MINUTE, -timezone_calendar.get(Calendar.MINUTE))
		} else {
        	badcourse_startcalendar.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
		}
        
        GregorianCalendar badcourse_endcalendar = badcourse_startcalendar.clone()
        badcourse_endcalendar.add(Calendar.SECOND, badCourseSeconds)
            
    	course_error = true
		int last_index = 0
		Date last_time = null
        CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
			switch (coordresult_instance.type) {
                case CoordType.SP:
				case CoordType.TP:
				case CoordType.SECRET:
				case CoordType.FP:
				case CoordType.iFP:
					if (coordresult_instance.resultCpTime != Date.parse("HH:mm","02:00")) { // Messung
		            	if (last_index != 0) {
							if (badcourse_endcalendar.getTime() <= coordresult_instance.resultCpTime) {
		        				coordresult_instance.resultBadCourseNum++
		        				coordresult_instance.save()
		        				println "${coordresult_instance.title()} at ${coordresult_instance.resultCpTime} (>= ${badcourse_endcalendar.getTime()}) relevant: Set BadCourseNum to $coordresult_instance.resultBadCourseNum)."
		        			} else {
		        				// println "${coordresult_instance.title()} at ${coordresult_instance.resultCpTime} (${badcourse_endcalendar.getTime()}) not relevant."
		        			}
		            	}
		
		            	if (badcourse_startcalendar.getTime() > coordresult_instance.resultCpTime) {
		                    last_index = i
		                    last_time = coordresult_instance.resultCpTime
		                } else {
		                    last_index = 0
							last_time = null
		                }
					}
					break
			}
        }
    		
        return course_error
    }
    
    //--------------------------------------------------------------------------
    private void processAflosErrorPointBadTurn(Test testInstance, String badTurnTimeUTC)
    {
        Contest contest_instance = testInstance.task.contest
        
        Date badturn_time = Date.parse("HH:mm:ss", badTurnTimeUTC)
        GregorianCalendar badturn_calendar = new GregorianCalendar()
        badturn_calendar.setTime(badturn_time)
        
        Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        GregorianCalendar timezone_calendar = new GregorianCalendar()
        timezone_calendar.setTime(timezone_date)
        
        badturn_calendar.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
		if (contest_instance.timeZone.startsWith("-")) {
			badturn_calendar.add(Calendar.MINUTE, -timezone_calendar.get(Calendar.MINUTE))
		} else {
			badturn_calendar.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
		}
        
        int last_index = 0
        Date last_time
        boolean calculatePenalties = false
        CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
            if (last_index != 0 && coordresult_instance.planProcedureTurn && badturn_calendar.getTime() < coordresult_instance.resultCpTime) {
                last_index = i
                
                if (badturn_calendar.getTime() > last_time) {
                    coordresult_instance.resultProcedureTurnEntered = true
                    coordresult_instance.resultProcedureTurnNotFlown = true
                    coordresult_instance.save()
                    calculatePenalties = true
                    println "${coordresult_instance.title()} (${coordresult_instance.resultCpTime}) relevant."
                } else {
                    println "${coordresult_instance.title()} (${coordresult_instance.resultCpTime}) not relevant."
                }
            }

            if (badturn_calendar.getTime() > coordresult_instance.resultCpTime) {
                last_index = i
                last_time = coordresult_instance.resultCpTime
            } else {
                last_index = 0
            }
            
        }
    }
    
    //--------------------------------------------------------------------------
    Map setnoflightresultsTest(Map params)
    {
        printstart "setnoflightresultsTest"
        
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            printerror ""
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                printerror ""
                return ['instance':test.instance]
            }
        }
        
        set_noflightresults(test.instance)

        printdone ""
        return ['instance':test.instance,'message':getMsg('fc.test.results.nodata',[test.instance.crew.name])]
    }
    
    //--------------------------------------------------------------------------
    private void set_noflightresults(Test testInstance)
    {
        // remove old calc results
        if (testInstance.IsLoggerResult()) {
            CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:"id"]).each { CalcResult calcresult_instance ->
                calcresult_instance.delete()
            }
        }
        
        CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
            // reset results
            coordresult_instance.ResetResults(true) // true - with procedure turn
            
            // calculate results
            coordresult_instance.resultCpNotFound = true
            if (coordresult_instance.planProcedureTurn) {
                coordresult_instance.resultProcedureTurnEntered = true
                coordresult_instance.resultProcedureTurnNotFlown = true
            }
            calculateCoordResultInstance(coordresult_instance,true,false)
            
            // save results
            coordresult_instance.save()
        }
        
        testInstance.flightTestModified = true
        testInstance.flightTestLink = ""
		delete_uploadjobtest(testInstance)
        testInstance.crewResultsModified = true
        
        // Penalties berechnen
        calculateTestPenalties(testInstance,false)
        testInstance.save()
        
        testInstance.task.liveTrackingTracksAvailable = false
        testInstance.task.save()
    }
    
    //--------------------------------------------------------------------------
    Map changeCalcResultTest(Map params, boolean enableCalcResult)
    {
        printstart "changeCalcResultTest (Disable: ${!enableCalcResult})"
        
        CalcResult calcresult_instance = CalcResult.get(params.calcresultid)
        set_calcresult_judgedisabled(calcresult_instance, !enableCalcResult)
        
        Test test_instance = calcresult_instance.loggerresult.test
        boolean errors = importResults(test_instance, false, "", "") // false - noRemoveExistingData
        
        if (errors) {
            Map ret = ['instance':test_instance,'saved':true,'message':getMsg('fc.flightresults.loggerresults.changed.naverrors',[test_instance.crew.name,test_instance.flightTestPenalties])]
            printdone ret.message
            return ret
        } else {
            Map ret = ['instance':test_instance,'saved':true,'message':getMsg('fc.flightresults.loggerresults.changed',[test_instance.crew.name,test_instance.flightTestPenalties])]
            printdone ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map getCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)

        if (!coordroute_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        // transient variables
        coordroute_instance.latGradDecimal = coordroute_instance.latMath()
        coordroute_instance.latMin = coordroute_instance.latMinute.toInteger()
        coordroute_instance.latSecondDecimal = CoordPresentation.GetSecond(coordroute_instance.latMinute)
        coordroute_instance.lonGradDecimal = coordroute_instance.lonMath()
        coordroute_instance.lonMin = coordroute_instance.lonMinute.toInteger()
        coordroute_instance.lonSecondDecimal = CoordPresentation.GetSecond(coordroute_instance.lonMinute)
        
        return ['instance':coordroute_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordRoute(String showLanguage, Map params)
    {
        printstart "updateCoordRoute $showLanguage $params"
        
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordroute_instance.version > version) {
                    coordroute_instance.errors.rejectValue("version", "coordRoute.optimistic.locking.failure", getMsg('fc.notupdated'))
                    printerror ""
                    return ['instance':coordroute_instance]
                }
            }
            
			if (coordroute_instance.route.Used()) {
                params.gatewidth2 = Languages.GetLanguageDecimal(showLanguage, params.gatewidth2)
                
                coordroute_instance.properties = params
                if (coordroute_instance.gatewidth2 == null) {
                    coordroute_instance.gatewidth2 = 0.0f
                }
                if (params.gateDirection) {
                    coordroute_instance.gateDirection = params.gateDirection.toBigDecimal()
                }
                if (params.minAltitudeAboveGround) {
                    coordroute_instance.minAltitudeAboveGround = params.minAltitudeAboveGround.toInteger()
			    }
                if (params.maxAltitudeAboveGround) {
                    coordroute_instance.maxAltitudeAboveGround = params.maxAltitudeAboveGround.toInteger()
			    }

                if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                    Map ret = ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordroute_instance]
                }
			} else {
                BigDecimal old_latmath = coordroute_instance.latMath()
                BigDecimal old_lonmath = coordroute_instance.lonMath()
                
                params = calculateCoordRoute(showLanguage, params, coordroute_instance.route.contest.coordPresentation)
    			
                coordroute_instance.properties = params
    			if (coordroute_instance.gatewidth2 == null) {
    				coordroute_instance.gatewidth2 = 0.0f
    			}
                if (params.gateDirection) {
                    coordroute_instance.gateDirection = params.gateDirection.toBigDecimal()
			    }
                if (!params.legDuration) {
                    coordroute_instance.legDuration = null
                }
                if (!params.measureTrueTrack) {
                    coordroute_instance.measureTrueTrack = null
                }
                if (!params.measureDistance) {
                    coordroute_instance.measureDistance = null
                    coordroute_instance.legMeasureDistance = null
                    coordroute_instance.legDistance = null
                }
                if (params.minAltitudeAboveGround) {
                    coordroute_instance.minAltitudeAboveGround = params.minAltitudeAboveGround.toInteger()
			    }
                if (params.maxAltitudeAboveGround) {
                    coordroute_instance.maxAltitudeAboveGround = params.maxAltitudeAboveGround.toInteger()
			    }
    
                if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                    calculateAllLegMeasureDistances(coordroute_instance.route)
                    calculateSecretLegRatio(coordroute_instance.route)
                    calculateRouteLegs(coordroute_instance.route)
                    if (FcMath.DecimalGradDiff(old_latmath, coordroute_instance.latMath()) || FcMath.DecimalGradDiff(old_lonmath,coordroute_instance.lonMath())) {
                        calculateEnrouteValues(coordroute_instance.route)
                    }
                    Map ret = ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordroute_instance]
                }
			}
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateCoordRouteObject(String showLanguage, Map params)
    {
        printstart "updateCoordRouteObject $showLanguage $params"
        
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordroute_instance.version > version) {
                    coordroute_instance.errors.rejectValue("version", "coordRoute.optimistic.locking.failure", getMsg('fc.notupdated'))
                    printerror ""
                    return ['instance':coordroute_instance]
                }
            }
            
			if (coordroute_instance.route.Used()) {
                // return ['instance':coordroute_instance,'error':true,'message':getMsg('fc.coordroute.update.notallowed.routeused')]
                
                coordroute_instance.properties = params
                if (!coordroute_instance.route.IsTurnpointSignUsed()) {
                    if (params.assignedSign) {
                        coordroute_instance.assignedSign = TurnpointSign.(params.assignedSign)
                    }
                    if (params.correctSign) {
                        coordroute_instance.correctSign = TurnpointCorrect.(params.correctSign)
                    }
                }

                if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                    Map ret = ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordroute_instance]
                }
			} else {
                coordroute_instance.properties = params
                if (params.assignedSign) {
                    coordroute_instance.assignedSign = TurnpointSign.(params.assignedSign)
                }
                if (params.correctSign) {
                    coordroute_instance.correctSign = TurnpointCorrect.(params.correctSign)
                }
    
                if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                    Map ret = ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordroute_instance]
                }
			}
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
	Map resetmeasureCoordRoute(Map params)
	{
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordroute_instance.version > version) {
                    coordroute_instance.errors.rejectValue("version", "coordRoute.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordroute_instance]
                }
            }
            
			if (coordroute_instance.route.Used()) {
				return ['instance':coordroute_instance,'error':true,'message':getMsg('fc.coordroute.update.notallowed.routeused')]
			}
			
			coordroute_instance.measureTrueTrack = null
            coordroute_instance.measureDistance = null
			coordroute_instance.legMeasureDistance = null
			coordroute_instance.legDistance = null
			
            if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                calculateSecretLegRatio(coordroute_instance.route)
                calculateRouteLegs(coordroute_instance.route)
                calculateEnrouteValues(coordroute_instance.route)
                return ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
            } else {
                return ['instance':coordroute_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
	}
	
    //--------------------------------------------------------------------------
    Map createCoordRoute(Map params)
    {
		Route route_instance = Route.get(params.routeid)
		if (route_instance.Used()) {
			return ['error':true,'message':getMsg('fc.coordroute.add.notallowed.routeused')]
		}
		
    	CoordRoute last_coordroute_instance = CoordRoute.findByRoute(route_instance,[sort:"id", order:"desc"])
        CoordRoute coordroute_instance = new CoordRoute()
    
        coordroute_instance.properties = params
        coordroute_instance.altitude = 1
    
        if (params.secret) {
            coordroute_instance.type = CoordType.SECRET
			coordroute_instance.gatewidth2 = route_instance.contest.scGateWidth
            if (last_coordroute_instance) {
                if (last_coordroute_instance.type.IsSecretAllowedCoord()) {
                   	coordroute_instance.titleNumber = findNextTitleNumber(last_coordroute_instance.route,CoordType.SECRET)
                } else {
                    return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
                }
            } else {
                return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
            }
        } else {
	        if (last_coordroute_instance) {
				coordroute_instance.type = last_coordroute_instance.type.GetNextValue()
	        	if (coordroute_instance.type == null) {
                	return ['error':true,'message':getMsg('fc.coordroute.add.notallowed')]
	        	} else if (coordroute_instance.type == CoordType.TP) {
	                coordroute_instance.titleNumber = findNextTitleNumber(last_coordroute_instance.route,CoordType.TP)
	        	} 
	        }
        }
        
        if (last_coordroute_instance) {
            coordroute_instance.latGrad = last_coordroute_instance.latGrad
            coordroute_instance.latMinute = last_coordroute_instance.latMinute
            coordroute_instance.latDirection = last_coordroute_instance.latDirection
            coordroute_instance.lonGrad = last_coordroute_instance.lonGrad
            coordroute_instance.lonMinute = last_coordroute_instance.lonMinute
            coordroute_instance.lonDirection = last_coordroute_instance.lonDirection
            
            // transient variables
            coordroute_instance.latGradDecimal = coordroute_instance.latMath()
            coordroute_instance.latMin = coordroute_instance.latMinute.toInteger()
            coordroute_instance.latSecondDecimal = CoordPresentation.GetSecond(coordroute_instance.latMinute)
            coordroute_instance.lonGradDecimal = coordroute_instance.lonMath()
            coordroute_instance.lonMin = coordroute_instance.lonMinute.toInteger()
            coordroute_instance.lonSecondDecimal = CoordPresentation.GetSecond(coordroute_instance.lonMinute)
        }
        
        return ['instance':coordroute_instance]
    }

    
    //--------------------------------------------------------------------------
    int findNextTitleNumber(Route route, CoordType type)
    {
    	int titleNumber = 0
    	CoordRoute.findAllByRoute(route,[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance ->
        	if (!titleNumber) {
	        	if (coordroute_instance.type == type) {
	        		titleNumber = coordroute_instance.titleNumber
	        	}
        	}
        }
    	return titleNumber + 1
    }
    
    //--------------------------------------------------------------------------
    Map saveCoordRoute(String showLanguage, Map params)
    {
		printstart "saveCoordRoute $showLanguage $params"
		
        Route route_instance = Route.get(params.routeid)
    	CoordRoute last_coordroute_instance = CoordRoute.findByRoute(route_instance,[sort:"id", order:"desc"]) // last
		CoordRoute before_last_coordroute_instance // before last
		
		// Summe der Distanzen vorangegangener Secrets-Points berechnen  
		BigDecimal last_mapmeasure_distance = null
        BigDecimal last_map_distance = null
		int last_legduration = 0
		BigDecimal last_coord_distance = 0
		CoordType last_coordtype = CoordType.UNKNOWN
        CoordRoute last_coordroute_test_instance
		CoordRoute last_coordroute_instance2
		BigDecimal first_coord_truetrack
		BigDecimal turn_true_track
		BigDecimal test_turn_true_track
		BigDecimal last_legdata_distance = 0
    	CoordRoute.findAllByRoute(route_instance,[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance -> // rückwärts
			if (!last_coordroute_test_instance) {
				if (coordroute_instance.type == CoordType.SECRET) {
	        		last_mapmeasure_distance = addMapMeasureDistance(last_mapmeasure_distance,coordroute_instance.legMeasureDistance)
                    last_map_distance = addMapDistance(last_map_distance,route_instance.Convert_mm2NM(coordroute_instance.legMeasureDistance))
					if (coordroute_instance.legDuration) {
						last_legduration += coordroute_instance.legDuration
					}
					if (last_coordroute_instance2) {
						Map leg_data_coord = calculateLegData(coordroute_instance, last_coordroute_instance2)
						last_legdata_distance += leg_data_coord.dis
						last_coord_distance += FcMath.RoundDistance(leg_data_coord.dis)
						println "Add last coord distance ${FcMath.RoundDistance(leg_data_coord.dis)}NM from ${coordroute_instance.title()}"
					}
					last_coordroute_instance2 = coordroute_instance
				} else if (coordroute_instance.type != CoordType.UNKNOWN) {
					if (last_coordroute_instance2) {
						Map leg_data_coord = calculateLegData(coordroute_instance, last_coordroute_instance2)
						last_legdata_distance += leg_data_coord.dis
						last_coord_distance += FcMath.RoundDistance(leg_data_coord.dis)
						println "Add last coord distance ${FcMath.RoundDistance(leg_data_coord.dis)}NM from ${coordroute_instance.title()}"
					}
		        	last_coordroute_test_instance = coordroute_instance // exit
			    }
				if (last_coordtype == CoordType.UNKNOWN) {
					last_coordtype = coordroute_instance.type
				}
				if (coordroute_instance.type == CoordType.SECRET) {
					first_coord_truetrack = coordroute_instance.coordTrueTrack
					println "Set first_coord_truetrack $first_coord_truetrack"
				}
        	}
			if (!before_last_coordroute_instance) {
				if (coordroute_instance != last_coordroute_instance) {
					before_last_coordroute_instance = coordroute_instance
				}
			}
        }
        
        params = calculateCoordRoute(showLanguage, params, route_instance.contest.coordPresentation)
		
		// new CoordRoute
    	CoordRoute coordroute_instance = new CoordRoute(params)
		if (coordroute_instance.gatewidth2 == null) {
			coordroute_instance.gatewidth2 = 0.0f
		}
        if (params.gateDirection) {
            coordroute_instance.gateDirection = params.gateDirection.toBigDecimal()
        }
        if (params.assignedSign) {
            coordroute_instance.assignedSign = TurnpointSign.(params.assignedSign)
        }
        if (params.correctSign) {
            coordroute_instance.correctSign = TurnpointCorrect.(params.correctSign)
        }
        if (params.minAltitudeAboveGround) {
            coordroute_instance.minAltitudeAboveGround = params.minAltitudeAboveGround.toInteger()
        }
        if (params.maxAltitudeAboveGround) {
            coordroute_instance.maxAltitudeAboveGround = params.maxAltitudeAboveGround.toInteger()
        }
        coordroute_instance.route = route_instance
		calculateLegMeasureDistance(coordroute_instance, true)
		
		// calculate coordTrueTrack/coordMeasureDistance
		if (last_coordroute_instance) {
			Map legdata_coord = calculateLegData(coordroute_instance, last_coordroute_instance)
			last_legdata_distance += legdata_coord.dis
			coordroute_instance.coordTrueTrack = legdata_coord.dir
			coordroute_instance.coordMeasureDistance = coordroute_instance.route.Convert_NM2mm(last_legdata_distance)
		} 
		
		// calculate turn_true_track / test_turn_true_track
		if (last_coordroute_instance && before_last_coordroute_instance) {
			if (last_coordroute_instance.measureTrueTrack) {
				turn_true_track = FcMath.RoundGrad(last_coordroute_instance.measureTrueTrack)
			}  else {
				turn_true_track = FcMath.RoundGrad(calculateLegData(last_coordroute_instance, before_last_coordroute_instance).dir)
			}
			switch (last_coordroute_instance.type) {
				case CoordType.TP:
		        case CoordType.iFP:
		        case CoordType.iLDG:
		        case CoordType.iTO:
		        case CoordType.iSP:
					test_turn_true_track = turn_true_track
					break
				case CoordType.SECRET:
					switch (coordroute_instance.type) {
						case CoordType.TP:
						case CoordType.iFP:
						case CoordType.FP:
							test_turn_true_track = FcMath.RoundGrad(last_coordroute_test_instance.coordTrueTrack)
							break
					}
					break
			}
		}
		
		// save CoordRoute
        if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
            calculateSecretLegRatio(coordroute_instance.route)
       		last_mapmeasure_distance = addMapMeasureDistance(last_mapmeasure_distance,coordroute_instance.legMeasureDistance)
            last_map_distance = addMapDistance(last_map_distance,route_instance.Convert_mm2NM(coordroute_instance.legMeasureDistance))
			BigDecimal last_measure_truetrack
			if (last_coordtype != CoordType.SECRET) {
				last_measure_truetrack = coordroute_instance.measureTrueTrack
			}
			println "Save ${coordroute_instance.titleCode()} ${coordroute_instance.title()}"
			println "  Last CoordRoute ${last_coordroute_instance?.titleCode()} ${last_coordroute_instance?.title()}"
			println "  turn_true_track: $turn_true_track"
			println "  test_turn_true_track: $test_turn_true_track"
			println "  last_measure_truetrack $last_measure_truetrack"
			println "  Last coord distance: ${last_coord_distance}NM"
        	newLeg(
				coordroute_instance.route, 
				coordroute_instance, 
				last_coordroute_instance, 
				last_coordroute_test_instance, 
				last_mapmeasure_distance,
                last_map_distance,
				last_legduration,
				last_coord_distance,
				first_coord_truetrack,
				turn_true_track,
				test_turn_true_track,
				last_measure_truetrack
			)
            Map ret = ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.created',["${coordroute_instance.name()}"])]
			printdone ret.message
			return ret
        } else {
			printerror ""
            return ['instance':coordroute_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map calculateCoordRoute(String showLanguage, Map params, CoordPresentation coordPresentation) 
    {
        params = calculateCoordCoord(showLanguage, params, coordPresentation)
        if (params.gatewidth2) {
            params.gatewidth2 = Languages.GetLanguageDecimal(showLanguage, params.gatewidth2)
        }
        if (params.measureDistance) {
            params.measureDistance = Languages.GetLanguageDecimal(showLanguage, params.measureDistance)
        }
        return params
    }
        
    //--------------------------------------------------------------------------
	private void calculateLegMeasureDistance(CoordRoute coordRouteInstance, boolean isNew)
	{
		if (coordRouteInstance.measureDistance) {
			println "calculateLegMeasureDistance"
			Route route_instance = coordRouteInstance.route
			coordRouteInstance.legMeasureDistance = coordRouteInstance.measureDistance
			boolean exit = false
			boolean check = isNew  
			CoordRoute.findAllByRoute(route_instance,[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance ->
				if (!exit) {
					if (check) {
						if (coordroute_instance.type == CoordType.SECRET) {
							if (coordroute_instance.legMeasureDistance) {
								coordRouteInstance.legMeasureDistance -= coordroute_instance.legMeasureDistance
							}
						} else {
							exit = true
						}
					} else {
						if (coordRouteInstance == coordroute_instance) {
							check = true
						}
					}
				}
			}
			coordRouteInstance.legDistance = route_instance.Convert_mm2NM(coordRouteInstance.legMeasureDistance)
		} else {
            println "calculateLegMeasureDistance (remove)"
            coordRouteInstance.legMeasureDistance = null
            coordRouteInstance.legDistance = null
		}
	}
	
    //--------------------------------------------------------------------------
	private void calculateAllLegMeasureDistances(Route routeInstance)
	{
    	printstart "calculateAllLegMeasureDistances '${routeInstance.name()}'"
		
        CoordRoute last_coordroute_instance = null
        CoordRoute last_coordroute_test_instance = null
        BigDecimal last_legdata_distance = 0
		CoordRoute.findAllByRoute(routeInstance,[sort:"id", order:"asc"]).each { CoordRoute coordroute_instance ->
			if (/*coordroute_instance.type == CoordType.SECRET && */ coordroute_instance.measureDistance) {
				BigDecimal leg_measure_distance = coordroute_instance.measureDistance
				boolean exit = false
				boolean check = false  
				CoordRoute.findAllByRoute(routeInstance,[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance2 ->
					if (!exit) {
						if (check) {
							if (coordroute_instance2.type == CoordType.SECRET) {
								if (coordroute_instance2.legMeasureDistance) {
									leg_measure_distance -= coordroute_instance2.legMeasureDistance
								}
							} else {
								exit = true
							}
						} else {
							if (coordroute_instance == coordroute_instance2) {
								check = true
							}
						}
					}
				}
				if (coordroute_instance.legMeasureDistance != leg_measure_distance) {
					BigDecimal old_leg_measure_distance = coordroute_instance.legMeasureDistance
					BigDecimal old_leg_distance = coordroute_instance.legDistance  
					coordroute_instance.legMeasureDistance = leg_measure_distance
					coordroute_instance.legDistance = routeInstance.Convert_mm2NM(leg_measure_distance)
					coordroute_instance.save()
					println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} modified: $old_leg_measure_distance -> $coordroute_instance.legMeasureDistance, $old_leg_distance -> $coordroute_instance.legDistance"
				}
			}
            
            // calculate coordTrueTrack/coordMeasureDistance
            if (last_coordroute_instance) {
                Map legdata_coord = calculateLegData(coordroute_instance, last_coordroute_instance)
                last_legdata_distance += legdata_coord.dis
                coordroute_instance.coordTrueTrack = legdata_coord.dir
                coordroute_instance.coordMeasureDistance = routeInstance.Convert_NM2mm(last_legdata_distance)
                coordroute_instance.save()
            }
            
            last_coordroute_instance = coordroute_instance
            if (coordroute_instance.type != CoordType.SECRET) {
                last_coordroute_test_instance = coordroute_instance
            }
            if (last_coordroute_instance == last_coordroute_test_instance) {
                last_legdata_distance = 0
            }
		}
		
		printdone ""
	}

	//--------------------------------------------------------------------------
    Map deleteCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {
			if (coordroute_instance.route.Used()) {
				return ['instance':coordroute_instance,'error':true,'message':getMsg('fc.coordroute.update.notallowed.routeused')]
			}
			
            try {
                Route route_instance = coordroute_instance.route
                removeAllRouteLegs(route_instance)
                coordroute_instance.delete()
                renumberCoordRoute(route_instance)
				calculateAllLegMeasureDistances(route_instance)
				calculateSecretLegRatio(route_instance)
                calculateRouteLegs(route_instance)
                calculateEnrouteValues(route_instance)
                return ['deleted':true,'message':getMsg('fc.deleted',["${coordroute_instance.name()}"]),'routeid':route_instance.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.coordroute'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getRouteLegCoord(Map params)
    {
        RouteLegCoord routelegcoord_instance = RouteLegCoord.get(params.id)

        if (!routelegcoord_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
        }
        
        return ['instance':routelegcoord_instance]
    }

    //--------------------------------------------------------------------------
    Map updateRouteLegCoord(Map params)
    {
        RouteLegCoord routelegcoord_instance = RouteLegCoord.get(params.id)
        
        if (routelegcoord_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(routelegcoord_instance.version > version) {
                    routelegcoord_instance.errors.rejectValue("version", "routeLegCoord.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':routelegcoord_instance]
                }
            }
            
            routelegcoord_instance.properties = params

            calculateRouteLegCoordMapDistances(routelegcoord_instance)
			calculateSecretLegRatio(routelegcoord_instance.route)
			
            if(!routelegcoord_instance.hasErrors() && routelegcoord_instance.save()) {
                return ['instance':routelegcoord_instance,'saved':true,'message':getMsg('fc.updated',["${routelegcoord_instance.coordName()}"])]
            } else {
                return ['instance':routelegcoord_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculateRouteLegCoordMapDistances(routeLegCoordInstance)
    {
    	printstart "calculateRouteLegCoordMapDistances $routeLegCoordInstance"
    	
		BigDecimal secrets_distance = 0
    		
    	// search coord for routeLegCoordInstance
    	CoordRoute found_coordroute_instance
    	RouteLegCoord.findAllByRoute(routeLegCoordInstance.route,[sort:"id"]).eachWithIndex { RouteLegCoord routelegcoord_instance, int i -> 
    		if (routelegcoord_instance == routeLegCoordInstance) {
    			CoordRoute.findAllByRoute(routeLegCoordInstance.route,[sort:"id"]).eachWithIndex { CoordRoute coordroute_instance, int j ->
    				if (i + 1 == j) {
    					found_coordroute_instance = coordroute_instance
    				}
					if (!found_coordroute_instance) {
						if (coordroute_instance.type == CoordType.SECRET) {
							if (coordroute_instance.legMeasureDistance) {
								secrets_distance += coordroute_instance.legMeasureDistance
							}
						} else {
							secrets_distance = 0
						}
					}
    			}
    		}
    	}

    	// calculate legDistance
		routeLegCoordInstance.legMeasureDistance = routeLegCoordInstance.measureDistance - secrets_distance 
    	routeLegCoordInstance.legDistance = routeLegCoordInstance.route.Convert_mm2NM(routeLegCoordInstance.legMeasureDistance)
    	
        // save legDistance to coord
    	if (found_coordroute_instance) {
			found_coordroute_instance.measureDistance = routeLegCoordInstance.measureDistance
    		found_coordroute_instance.legMeasureDistance = routeLegCoordInstance.legMeasureDistance
			found_coordroute_instance.legDistance = routeLegCoordInstance.legDistance
			found_coordroute_instance.measureTrueTrack = routeLegCoordInstance.measureTrueTrack
			found_coordroute_instance.save()
    	}
        
        // calculate legDistance in testlegs
        CoordRoute last_coordroute_instance
        CoordRoute last_coordroute_test_instance
        BigDecimal last_mapmeasure_distance = null
        BigDecimal lastMapMeasureTrueTrack = null
        int testlegpos = 0
        CoordRoute.findAllByRoute(routeLegCoordInstance.route,[sort:"id"]).each { CoordRoute coordroute_instance ->
       		last_mapmeasure_distance = addMapMeasureDistance(last_mapmeasure_distance,coordroute_instance.legMeasureDistance)
            lastMapMeasureTrueTrack = coordroute_instance.measureTrueTrack
            if (last_coordroute_instance && last_coordroute_test_instance) {
                if ( (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.TP)    ||
                     (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.iFP)   ||
                     (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.FP)    ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.TP)    ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.iFP)   ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.FP)    || 
					 (last_coordroute_test_instance.type == CoordType.iSP && coordroute_instance.type == CoordType.TP)   ||
					 (last_coordroute_test_instance.type == CoordType.iSP && coordroute_instance.type == CoordType.FP)   ||
					 (last_coordroute_test_instance.type == CoordType.iFP && coordroute_instance.type == CoordType.iLDG) ||
					 (last_coordroute_test_instance.type == CoordType.iFP && coordroute_instance.type == CoordType.iTO)  ||
					 (last_coordroute_test_instance.type == CoordType.iFP && coordroute_instance.type == CoordType.iSP)  ||
					 (last_coordroute_test_instance.type == CoordType.iLDG && coordroute_instance.type == CoordType.iTO) ||
					 (last_coordroute_test_instance.type == CoordType.iLDG && coordroute_instance.type == CoordType.iSP) ||
					 (last_coordroute_test_instance.type == CoordType.iTO && coordroute_instance.type == CoordType.iSP)
				   ) 
                {
                	RouteLegTest.findAllByRoute(routeLegCoordInstance.route,[sort:"id"]).eachWithIndex { RouteLegTest routelegtest_instance, int i ->
                		if (i == testlegpos) {
                			routelegtest_instance.legMeasureDistance = last_mapmeasure_distance 
                			routelegtest_instance.legDistance = routeLegCoordInstance.route.Convert_mm2NM(last_mapmeasure_distance)
                			routelegtest_instance.measureTrueTrack = lastMapMeasureTrueTrack
                			routelegtest_instance.save()
                		}
                	}
                    testlegpos++
                }
            }
            last_coordroute_instance = coordroute_instance
            switch (coordroute_instance.type) {
                case CoordType.SP:
                case CoordType.TP:
                case CoordType.FP:
                case CoordType.iFP:
                case CoordType.iLDG:
                case CoordType.iTO:
                case CoordType.iSP:
                    last_coordroute_test_instance = coordroute_instance
                    last_mapmeasure_distance = null
                    break
            }
        }
        printdone ""
    }

    //--------------------------------------------------------------------------
    Map getRouteLegTest(Map params)
    {
        RouteLegTest routelegtest_instance = RouteLegTest.get(params.id)

        if (!routelegtest_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.routeleg'),params.id])]
        }
        
        return ['instance':routelegtest_instance]
    }

    //--------------------------------------------------------------------------
    Map getCoordEnroutePhoto(Map params)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)

        if (!coordenroutephoto_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        if (coordenroutephoto_instance.type == CoordType.UNKNOWN) {
            CoordEnroutePhoto last_coordenroutephoto_instance = CoordEnroutePhoto.findByRouteAndTypeNotEqual(coordenroutephoto_instance.route,CoordType.UNKNOWN,[sort:"enrouteViewPos", order:"desc"])
            if (last_coordenroutephoto_instance) {
                if (coordenroutephoto_instance.route.enroutePhotoRoute == EnrouteRoute.InputNMFromTP) {
                    coordenroutephoto_instance.enrouteDistance = last_coordenroutephoto_instance.enrouteDistance
                }
                if (coordenroutephoto_instance.route.enroutePhotoRoute == EnrouteRoute.InputmmFromTP) {
                    coordenroutephoto_instance.measureDistance = last_coordenroutephoto_instance.measureDistance
                }
                coordenroutephoto_instance.type = last_coordenroutephoto_instance.type
                coordenroutephoto_instance.titleNumber = last_coordenroutephoto_instance.titleNumber
            }
        }

        // transient variables
        coordenroutephoto_instance.latGradDecimal = coordenroutephoto_instance.latMath()
        coordenroutephoto_instance.latMin = coordenroutephoto_instance.latMinute.toInteger()
        coordenroutephoto_instance.latSecondDecimal = CoordPresentation.GetSecond(coordenroutephoto_instance.latMinute)
        coordenroutephoto_instance.lonGradDecimal = coordenroutephoto_instance.lonMath()
        coordenroutephoto_instance.lonMin = coordenroutephoto_instance.lonMinute.toInteger()
        coordenroutephoto_instance.lonSecondDecimal = CoordPresentation.GetSecond(coordenroutephoto_instance.lonMinute)

        return ['instance':coordenroutephoto_instance]
    }

    //--------------------------------------------------------------------------
    Map createCoordEnroutePhoto(Map params)
    {
        Route route_instance = Route.get(params.routeid)
        if (route_instance.IsEnrouteSignUsed(true)) {
            return ['error':true,'message':getMsg('fc.coordroute.photo.add.notallowed.routeused')]
        }
        
        Coord last_coord_instance = CoordEnroutePhoto.findByRoute(route_instance,[sort:"id", order:"desc"])
        if (!last_coord_instance) {
            last_coord_instance = CoordRoute.findByRouteAndType(route_instance,CoordType.SP,[sort:"id"])
        }
        CoordEnroutePhoto last_coordenroutephoto_instance = CoordEnroutePhoto.findByRoute(route_instance,[sort:"enrouteViewPos", order:"desc"])
        CoordEnroutePhoto coordenroutephoto_instance = new CoordEnroutePhoto()
        coordenroutephoto_instance.properties = params
        
        if (last_coord_instance) {
            if (route_instance.enroutePhotoRoute.IsEnrouteRouteInputCoord()) {
                coordenroutephoto_instance.latGrad = last_coord_instance.latGrad
                coordenroutephoto_instance.latMinute = last_coord_instance.latMinute
                coordenroutephoto_instance.latDirection = last_coord_instance.latDirection
                coordenroutephoto_instance.lonGrad = last_coord_instance.lonGrad
                coordenroutephoto_instance.lonMinute = last_coord_instance.lonMinute
                coordenroutephoto_instance.lonDirection = last_coord_instance.lonDirection
                
                // transient variables
                coordenroutephoto_instance.latGradDecimal = coordenroutephoto_instance.latMath()
                coordenroutephoto_instance.latMin = coordenroutephoto_instance.latMinute.toInteger()
                coordenroutephoto_instance.latSecondDecimal = CoordPresentation.GetSecond(coordenroutephoto_instance.latMinute)
                coordenroutephoto_instance.lonGradDecimal = coordenroutephoto_instance.lonMath()
                coordenroutephoto_instance.lonMin = coordenroutephoto_instance.lonMinute.toInteger()
                coordenroutephoto_instance.lonSecondDecimal = CoordPresentation.GetSecond(coordenroutephoto_instance.lonMinute)
            }
            if (route_instance.enroutePhotoRoute == EnrouteRoute.InputNMFromTP) {
                coordenroutephoto_instance.enrouteDistance = last_coord_instance.enrouteDistance
                if (last_coordenroutephoto_instance) {
                    coordenroutephoto_instance.type = last_coordenroutephoto_instance.type
                    coordenroutephoto_instance.titleNumber = last_coordenroutephoto_instance.titleNumber
                }
            }
            if (route_instance.enroutePhotoRoute == EnrouteRoute.InputmmFromTP) {
                coordenroutephoto_instance.measureDistance = last_coord_instance.measureDistance
                if (last_coordenroutephoto_instance) {
                    coordenroutephoto_instance.type = last_coordenroutephoto_instance.type
                    coordenroutephoto_instance.titleNumber = last_coordenroutephoto_instance.titleNumber
                }
            }
        }
        
        return ['instance':coordenroutephoto_instance]
    }

    //--------------------------------------------------------------------------
    Map saveCoordEnroutePhoto(String showLanguage, Map params)
    {
        printstart "saveCoordEnroutePhoto $showLanguage $params"
        Route route_instance = Route.get(params.routeid)

        int start_num = 0
        int last_num = 0
        boolean add_num_range = false
        boolean add_char_range = false
        if (params.enroutePhotoName && params.enrouteLastPhotoName) {
            if (params.enroutePhotoName.isInteger() && params.enrouteLastPhotoName.isInteger()) {
                start_num = params.enroutePhotoName.toInteger() + 1
                last_num = params.enrouteLastPhotoName.toInteger()
                if (start_num > 0 && start_num <= last_num) {
                    add_num_range = true
                }
            } else if ((params.enroutePhotoName.toLowerCase() in 'a'..'z') && (params.enrouteLastPhotoName.toLowerCase() in 'a'..'z')) {
                start_num = (int)params.enroutePhotoName.toUpperCase().charAt(0) + 1
                last_num = (int)params.enrouteLastPhotoName.toUpperCase().charAt(0)
                if (start_num > 0 && start_num <= last_num) {
                    add_char_range = true
                }
            }
        }
        
        params = calculateCoordEnroute(showLanguage, params, route_instance, true)
        
        // new CoordEnroutePhoto
        CoordEnroutePhoto coordenroutephoto_instance = new CoordEnroutePhoto(params)
        coordenroutephoto_instance.route = route_instance
        
        if (!params.enroutePhotoName) {
            coordenroutephoto_instance.errors.rejectValue("enroutePhotoName", "", getMsg('fc.observation.enroute.photo.name.expected'))
            printerror "enroutePhotoName is empty."
            return ['instance':coordenroutephoto_instance,'error':true]
        }
        
        String enroute_photo_name = params.enroutePhotoName
        if (add_char_range) {
            enroute_photo_name = params.enroutePhotoName.toUpperCase()
        }
        if (CoordEnroutePhoto.findByEnroutePhotoNameAndRoute(enroute_photo_name,route_instance)) {
            coordenroutephoto_instance.errors.rejectValue("enroutePhotoName", "", getMsg('fc.observation.enroute.photo.name.uniqueerror',[enroute_photo_name]))
            printerror "enroutePhotoName is not unique."
            return ['instance':coordenroutephoto_instance,'error':true]
        }
        if (CoordEnroutePhoto.countByRoute(route_instance) >= route_instance.contest.maxEnroutePhotos) {
            coordenroutephoto_instance.errors.rejectValue("enroutePhotoName", "", getMsg('fc.observation.enroute.photo.maxerror',[route_instance.contest.maxEnroutePhotos.toString()]))
            printerror "Maximum number of enroute photos reached."
            return ['instance':coordenroutephoto_instance,'error':true]
        }
            
        coordenroutephoto_instance.enroutePhotoName = enroute_photo_name
        coordenroutephoto_instance.enrouteViewPos = CoordEnroutePhoto.countByRoute(route_instance) + 1

        if (route_instance.enroutePhotoRoute == EnrouteRoute.InputName) {
            coordenroutephoto_instance.type = CoordType.UNKNOWN
        }
        if (route_instance.enroutePhotoRoute.IsEnrouteRouteInputDistanceFromTP()) {
            CoordTitle coordtitle_instance = CoordTitle.get(params.enrouteCoordTitle.split(" : ")[1].toLong())
            coordenroutephoto_instance.type = coordtitle_instance.type
            coordenroutephoto_instance.titleNumber = coordtitle_instance.number
        }
        
        coordenroutephoto_instance.calculateCoordEnrouteValues(coordenroutephoto_instance.route.enroutePhotoRoute)
        
        // save CoordEnroutePhoto
        if(!coordenroutephoto_instance.hasErrors() && coordenroutephoto_instance.save()) {
            if (!(add_num_range || add_char_range)) {
                route_instance.calculateEnroutPhotoViewPos()
                Map ret = ['saved':true,'message':getMsg('fc.created',["${params.enroutePhotoName}"])]
                printdone ret.message
                return ret
            }
        } else {
            printerror ""
            return ['instance':coordenroutephoto_instance,'error':true]
        }
        
        if (add_num_range || add_char_range) {
            String start_enroute_photo_name = enroute_photo_name
            for (int num = start_num; num <= last_num; num++) {
                if (CoordEnroutePhoto.countByRoute(route_instance) >= route_instance.contest.maxEnroutePhotos) {
                    break
                }
                enroute_photo_name = num.toString()
                if (add_char_range) {
                    enroute_photo_name = Character.toChars(num).toString()
                }
                if (!CoordEnroutePhoto.findByEnroutePhotoNameAndRoute(enroute_photo_name,route_instance)) {
                    coordenroutephoto_instance = new CoordEnroutePhoto(params)
                    coordenroutephoto_instance.route = route_instance
                    coordenroutephoto_instance.enroutePhotoName = enroute_photo_name
                    coordenroutephoto_instance.enrouteViewPos = CoordEnroutePhoto.countByRoute(route_instance) + 1
                    if (route_instance.enroutePhotoRoute == EnrouteRoute.InputName) {
                        coordenroutephoto_instance.type = CoordType.UNKNOWN
                    }
                    coordenroutephoto_instance.save()
                }
            }
            route_instance.calculateEnroutPhotoViewPos()
            Map ret = ['saved':true,'message':getMsg('fc.created',["${start_enroute_photo_name}...${enroute_photo_name}"])]
            printdone ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateCoordEnroutePhoto(String showLanguage, Map params)
    {
        printstart "updateCoordEnroutePhoto $showLanguage $params"
        
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        
        if (coordenroutephoto_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutephoto_instance.version > version) {
                    coordenroutephoto_instance.errors.rejectValue("version", "coordEnroutePhoto.optimistic.locking.failure", getMsg('fc.notupdated'))
                    printerror ""
                    return ['instance':coordenroutephoto_instance,'error':true]
                }
            }
            
            if (!params.enroutePhotoName) {
                coordenroutephoto_instance.errors.rejectValue("enroutePhotoName", "", getMsg('fc.observation.enroute.photo.name.expected'))
                printerror "enroutePhotoName is empty."
                return ['instance':coordenroutephoto_instance,'error':true,'message':getMsg('fc.observation.enroute.photo.name.expected')]
            }
    
            CoordEnroutePhoto photoname_coordenroutephoto_instance = CoordEnroutePhoto.findByEnroutePhotoNameAndRoute(params.enroutePhotoName,coordenroutephoto_instance.route) 
            if (photoname_coordenroutephoto_instance && photoname_coordenroutephoto_instance != coordenroutephoto_instance) {
                coordenroutephoto_instance.errors.rejectValue("enroutePhotoName", "", getMsg('fc.observation.enroute.photo.name.uniqueerror',[params.enroutePhotoName]))
                printerror "enroutePhotoName is not unique."
                return ['instance':coordenroutephoto_instance,'error':true,'message':getMsg('fc.observation.enroute.photo.name.uniqueerror',[params.enroutePhotoName])]
            }
            
            if (coordenroutephoto_instance.route.IsEnrouteSignUsed(true)) {
                return ['instance':coordenroutephoto_instance,'error':true,'message':getMsg('fc.coordroute.photo.update.notallowed.routeused')]
                
                /*
                coordenroutephoto_instance.properties = params

                if(!coordenroutephoto_instance.hasErrors() && coordenroutephoto_instance.save()) {
                    Map ret = ['instance':coordenroutephoto_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutephoto_instance.enroutePhotoName}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordenroutephoto_instance]
                }
                */
            } else {
                params = calculateCoordEnroute(showLanguage, params, coordenroutephoto_instance.route, true)
                
                coordenroutephoto_instance.properties = params

                coordenroutephoto_instance.enroutePhotoName = params.enroutePhotoName
                
                if (coordenroutephoto_instance.route.enroutePhotoRoute.IsEnrouteRouteInputDistanceFromTP()) {
                    CoordTitle coordtitle_instance = CoordTitle.get(params.enrouteCoordTitle.split(" : ")[1].toLong())
                    coordenroutephoto_instance.type = coordtitle_instance.type
                    coordenroutephoto_instance.titleNumber = coordtitle_instance.number
                }

                if (!params.enrouteDistance) {
                    coordenroutephoto_instance.enrouteDistance = null
                }
                if (!params.measureDistance) {
                    coordenroutephoto_instance.measureDistance = null
                }

                coordenroutephoto_instance.calculateCoordEnrouteValues(coordenroutephoto_instance.route.enroutePhotoRoute)
                
                if(!coordenroutephoto_instance.hasErrors() && coordenroutephoto_instance.save()) {
                    coordenroutephoto_instance.route.calculateEnroutPhotoViewPos()
                    Map ret = ['instance':coordenroutephoto_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutephoto_instance.enroutePhotoName}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordenroutephoto_instance,'error':true,'message':coordenroutephoto_instance.errors]
                }
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetmeasureCoordEnroutePhoto(Map params)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        
        if (coordenroutephoto_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutephoto_instance.version > version) {
                    coordenroutephoto_instance.errors.rejectValue("version", "coordEnroutePhoto.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordenroutephoto_instance]
                }
            }
            
            if (coordenroutephoto_instance.route.IsEnrouteSignUsed(true)) {
                return ['instance':coordenroutephoto_instance,'error':true,'message':getMsg('fc.coordroute.photo.update.notallowed.routeused')]
            }
            
            coordenroutephoto_instance.measureDistance = null
            
            coordenroutephoto_instance.calculateCoordEnrouteValues(coordenroutephoto_instance.route.enroutePhotoRoute)

            if(!coordenroutephoto_instance.hasErrors() && coordenroutephoto_instance.save()) {
                return ['instance':coordenroutephoto_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutephoto_instance.enroutePhotoName}"])]
            } else {
                return ['instance':coordenroutephoto_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map addpositionCoordEnroutePhoto(Map params)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        
        if (coordenroutephoto_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutephoto_instance.version > version) {
                    coordenroutephoto_instance.errors.rejectValue("version", "coordEnroutePhoto.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordenroutephoto_instance]
                }
            }
            
            CoordEnroutePhoto coordenroutephoto_instance2 = CoordEnroutePhoto.findByRouteAndEnrouteViewPos(coordenroutephoto_instance.route, coordenroutephoto_instance.enrouteViewPos + 1)
            coordenroutephoto_instance.enrouteViewPos++
            
            if(!coordenroutephoto_instance.hasErrors() && coordenroutephoto_instance.save()) {
                coordenroutephoto_instance2.enrouteViewPos--
                coordenroutephoto_instance2.save()
                return ['instance':coordenroutephoto_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutephoto_instance.enroutePhotoName}"])]
            } else {
                return ['instance':coordenroutephoto_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map subpositionCoordEnroutePhoto(Map params)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        
        if (coordenroutephoto_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutephoto_instance.version > version) {
                    coordenroutephoto_instance.errors.rejectValue("version", "coordEnroutePhoto.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordenroutephoto_instance]
                }
            }
            
            CoordEnroutePhoto coordenroutephoto_instance2 = CoordEnroutePhoto.findByRouteAndEnrouteViewPos(coordenroutephoto_instance.route, coordenroutephoto_instance.enrouteViewPos - 1)
            coordenroutephoto_instance.enrouteViewPos--
            
            if(!coordenroutephoto_instance.hasErrors() && coordenroutephoto_instance.save()) {
                coordenroutephoto_instance2.enrouteViewPos++
                coordenroutephoto_instance2.save()
                return ['instance':coordenroutephoto_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutephoto_instance.enroutePhotoName}"])]
            } else {
                return ['instance':coordenroutephoto_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteCoordEnroutePhoto(Map params)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        
        if (coordenroutephoto_instance) {
            if (coordenroutephoto_instance.route.IsEnrouteSignUsed(true)) { 
                return ['instance':coordenroutephoto_instance,'error':true,'message':getMsg('fc.coordroute.photo.update.notallowed.routeused')]
            }
            
            try {
                Route route_instance = coordenroutephoto_instance.route
                
                int deleted_enrouteviewpos = coordenroutephoto_instance.enrouteViewPos
                coordenroutephoto_instance.delete()
                
                // correct all CoordEnroutePhoto's enrouteViewPos
                CoordEnroutePhoto.findAllByRoute(route_instance,[sort:"id"]).each { CoordEnroutePhoto coordenroutephoto_instance2 ->
                    if (coordenroutephoto_instance2.enrouteViewPos > deleted_enrouteviewpos) {
                        coordenroutephoto_instance2.enrouteViewPos--
                        coordenroutephoto_instance2.save()
                    }
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${coordenroutephoto_instance.enroutePhotoName}"]),'routeid':route_instance.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.coordroute'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map removeallCoordEnroutePhoto(Map params)
    {
        Route route_instance = Route.get(params.routeid)
        
        if (route_instance.IsEnrouteSignUsed(true)) {
            return ['instance':route_instance,'error':true,'message':getMsg('fc.coordroute.photo.update.notallowed.routeused')]
        }
        
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route_instance)) {
            if (coordenroutephoto_instance) {
                coordenroutephoto_instance.delete()
            }
        }
        
        return ['deleted':true,'message':getMsg('fc.coordroute.photo.removedall'),'routeid':route_instance.id]
    }
    
    //--------------------------------------------------------------------------
    Map assignnamerandomlyCoordEnroutePhoto(Map params, boolean randomly)
    {
        Route route_instance = Route.get(params.routeid)
        
        if (route_instance.IsEnrouteSignUsed(true)) {
            return ['instance':route_instance,'error':true,'message':getMsg('fc.coordroute.photo.update.notallowed.routeused')]
        }
        
        List page_coord_types = []
        List page_types = []
        CoordRoute last_coordroute_instance2 = null
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:'id'])) {
            if (coordroute_instance.type.IsTurnpointSignCoord()) {
                if (!page_coord_types) {
                    if (coordroute_instance.type.IsEnrouteSignCoord()) {
                        page_coord_types = [coordroute_instance.titlePrintCode()]
                    }
                }
                if (coordroute_instance.observationNextPrintPageEnroute) {
                    page_types += [page_coord_types]
                    page_coord_types = []
                    if (coordroute_instance.type.IsEnrouteSignCoord()) {
                        page_coord_types = [coordroute_instance.titlePrintCode()]
                    }
                } else {
                    if (coordroute_instance.type.IsEnrouteSignCoord()) {
                        page_coord_types += coordroute_instance.titlePrintCode()
                    }
                }
                last_coordroute_instance2 = coordroute_instance
            }
        }
        if (page_coord_types) {
            page_types += [page_coord_types]
        }
        
        int new_name = 0
        int sort_area = 1
        List name_list = []
        boolean name_type_number = false
        boolean check_name_type = true
		int page_types_pos = 0
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
            if (check_name_type) {
                if (coordenroutephoto_instance.enroutePhotoName.isInteger()) {
                    name_type_number = true
                }
                check_name_type = false
            }
            if (!(coordenroutephoto_instance.titlePrintCode() in page_types[page_types_pos])) {
                sort_area++
				page_types_pos++
            }
            name_list += [newname:new_name, uuid:UUID.randomUUID().toString(), sortarea:sort_area, viewpos:coordenroutephoto_instance.enrouteViewPos]
            new_name++
        }
        
        if (randomly) {
            name_list = name_list.asImmutable().toSorted { a, b -> a.uuid <=> b.uuid }
            name_list = name_list.asImmutable().toSorted { a, b -> a.sortarea <=> b.sortarea }
        } else {
            name_list = name_list.asImmutable().toSorted { a, b -> a.viewpos <=> b.viewpos }
        }
        
        int name_list_pos = 0
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
            if (name_type_number) {
                coordenroutephoto_instance.enroutePhotoName = (name_list[name_list_pos].newname+1).toString()
            } else {
                coordenroutephoto_instance.enroutePhotoName = (char)((int)'A' + name_list[name_list_pos].newname)
            }
            coordenroutephoto_instance.save()
            name_list_pos++
        }
        
        return ['done':true,'message':getMsg('fc.coordroute.photo.assignednamerandomly'),'routeid':route_instance.id]
    }
    
    //--------------------------------------------------------------------------
    Map setpositionCoordEnroutePhoto(Map params)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        if (coordenroutephoto_instance) {
            coordenroutephoto_instance.observationPositionTop = coordenroutephoto_instance.GetObservationPositionPercentTop(FcMath.toInteger(params.top))
            coordenroutephoto_instance.observationPositionLeft = coordenroutephoto_instance.GetObservationPositionPercentLeft(FcMath.toInteger(params.left))
            coordenroutephoto_instance.save()
            return ['done':true,'message':getMsg('fc.coordroute.photo.positionassigned')]
        }
        
        return ['done':false,'message':""]
    }
    
    //--------------------------------------------------------------------------
    Map importCoordEnroutePhoto(Map params, MultipartFile imageFile)
    {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)

        if (!coordenroutephoto_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        if (!coordenroutephoto_instance.imagecoord) {
            ImageCoord imagecoord_instance = new ImageCoord(imageData:imageFile.getBytes(), coord:coordenroutephoto_instance)
            imagecoord_instance.save()
        
            coordenroutephoto_instance.imagecoord = imagecoord_instance
            coordenroutephoto_instance.save()
        } else {
            coordenroutephoto_instance.imagecoord.imageData = imageFile.getBytes()
            coordenroutephoto_instance.imagecoord.save()
        }
        
        return ['instance':coordenroutephoto_instance]
    }

    //--------------------------------------------------------------------------
    void deleteEnroutePhotos(Map params)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return
        }
        
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route.instance,[sort:"enrouteViewPos"])) {
            if (coordenroutephoto_instance.imagecoord) {
                coordenroutephoto_instance.imagecoord.delete()
                coordenroutephoto_instance.imagecoord = null
                coordenroutephoto_instance.save()
            }
        }
    }
    
    //--------------------------------------------------------------------------
    Map setpositionTurnpointPhoto(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        if (coordroute_instance) {
            coordroute_instance.observationPositionTop = coordroute_instance.GetObservationPositionPercentTop(FcMath.toInteger(params.top))
            coordroute_instance.observationPositionLeft = coordroute_instance.GetObservationPositionPercentLeft(FcMath.toInteger(params.left))
            coordroute_instance.save()
            return ['done':true,'message':getMsg('fc.coordroute.photo.positionassigned')]
        }
        
        return ['done':false,'message':""]
    }
    
    //--------------------------------------------------------------------------
    Map importTurnpointPhoto(Map params, MultipartFile imageFile)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)

        if (!coordroute_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        if (!coordroute_instance.imagecoord) {
            ImageCoord imagecoord_instance = new ImageCoord(imageData:imageFile.getBytes(), coord:coordroute_instance)
            imagecoord_instance.save()
        
            coordroute_instance.imagecoord = imagecoord_instance
            coordroute_instance.save()
        } else {
            coordroute_instance.imagecoord.imageData = imageFile.getBytes()
            coordroute_instance.imagecoord.save()
        }
        
        return ['instance':coordroute_instance]
    }

    //--------------------------------------------------------------------------
    void deleteTurnpointPhotos(Map params)
    {
        Map route = domainService.GetRoute(params)
        if (!route.instance) {
            return
        }
        
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route.instance,[sort:"id"])) {
            if (coordroute_instance.imagecoord) {
                coordroute_instance.imagecoord.delete()
                coordroute_instance.imagecoord = null
                coordroute_instance.save()
            }
        }
    }
    
    //--------------------------------------------------------------------------
    Map getCoordEnrouteCanvas(Map params)
    {
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)

        if (!coordenroutecanvas_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        if (coordenroutecanvas_instance.type == CoordType.UNKNOWN) {
            CoordEnrouteCanvas last_coordenroutecanvas_instance = CoordEnrouteCanvas.findByRouteAndTypeNotEqual(coordenroutecanvas_instance.route,CoordType.UNKNOWN,[sort:"enrouteViewPos", order:"desc"])
            if (last_coordenroutecanvas_instance) {
                if (coordenroutecanvas_instance.route.enrouteCanvasRoute == EnrouteRoute.InputNMFromTP) {
                    coordenroutecanvas_instance.enrouteDistance = last_coordenroutecanvas_instance.enrouteDistance
                }
                if (coordenroutecanvas_instance.route.enrouteCanvasRoute == EnrouteRoute.InputmmFromTP) {
                    coordenroutecanvas_instance.measureDistance = last_coordenroutecanvas_instance.measureDistance
                }
                coordenroutecanvas_instance.type = last_coordenroutecanvas_instance.type
                coordenroutecanvas_instance.titleNumber = last_coordenroutecanvas_instance.titleNumber
            }
        }
        
        // transient variables
        coordenroutecanvas_instance.latGradDecimal = coordenroutecanvas_instance.latMath()
        coordenroutecanvas_instance.latMin = coordenroutecanvas_instance.latMinute.toInteger()
        coordenroutecanvas_instance.latSecondDecimal = CoordPresentation.GetSecond(coordenroutecanvas_instance.latMinute)
        coordenroutecanvas_instance.lonGradDecimal = coordenroutecanvas_instance.lonMath()
        coordenroutecanvas_instance.lonMin = coordenroutecanvas_instance.lonMinute.toInteger()
        coordenroutecanvas_instance.lonSecondDecimal = CoordPresentation.GetSecond(coordenroutecanvas_instance.lonMinute)
        
        return ['instance':coordenroutecanvas_instance]
    }

    //--------------------------------------------------------------------------
    Map createCoordEnrouteCanvas(Map params)
    {
        Route route_instance = Route.get(params.routeid)
        if (route_instance.IsEnrouteSignUsed(false)) {
            return ['error':true,'message':getMsg('fc.coordroute.canvas.add.notallowed.routeused')]
        }
        
        Coord last_coord_instance = CoordEnrouteCanvas.findByRoute(route_instance,[sort:"id", order:"desc"])
        if (!last_coord_instance) {
            last_coord_instance = CoordRoute.findByRouteAndType(route_instance,CoordType.SP,[sort:"id"])
        }
        CoordEnrouteCanvas last_coordenroutecanvas_instance = CoordEnrouteCanvas.findByRoute(route_instance,[sort:"enrouteViewPos", order:"desc"])
        CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas()
        coordenroutecanvas_instance.properties = params
        
        if (last_coord_instance) {
            if (route_instance.enrouteCanvasRoute.IsEnrouteRouteInputCoord()) {
                coordenroutecanvas_instance.latGrad = last_coord_instance.latGrad
                coordenroutecanvas_instance.latMinute = last_coord_instance.latMinute
                coordenroutecanvas_instance.latDirection = last_coord_instance.latDirection
                coordenroutecanvas_instance.lonGrad = last_coord_instance.lonGrad
                coordenroutecanvas_instance.lonMinute = last_coord_instance.lonMinute
                coordenroutecanvas_instance.lonDirection = last_coord_instance.lonDirection
                
                // transient variables
                coordenroutecanvas_instance.latGradDecimal = coordenroutecanvas_instance.latMath()
                coordenroutecanvas_instance.latMin = coordenroutecanvas_instance.latMinute.toInteger()
                coordenroutecanvas_instance.latSecondDecimal = CoordPresentation.GetSecond(coordenroutecanvas_instance.latMinute)
                coordenroutecanvas_instance.lonGradDecimal = coordenroutecanvas_instance.lonMath()
                coordenroutecanvas_instance.lonMin = coordenroutecanvas_instance.lonMinute.toInteger()
                coordenroutecanvas_instance.lonSecondDecimal = CoordPresentation.GetSecond(coordenroutecanvas_instance.lonMinute)
            }
            if (route_instance.enrouteCanvasRoute == EnrouteRoute.InputNMFromTP) {
                coordenroutecanvas_instance.enrouteDistance = last_coord_instance.enrouteDistance
                if (last_coordenroutecanvas_instance) {
                    coordenroutecanvas_instance.type = last_coordenroutecanvas_instance.type
                    coordenroutecanvas_instance.titleNumber = last_coordenroutecanvas_instance.titleNumber
                }
            }
            if (route_instance.enrouteCanvasRoute == EnrouteRoute.InputmmFromTP) {
                coordenroutecanvas_instance.measureDistance = last_coord_instance.measureDistance
                if (last_coordenroutecanvas_instance) {
                    coordenroutecanvas_instance.type = last_coordenroutecanvas_instance.type
                    coordenroutecanvas_instance.titleNumber = last_coordenroutecanvas_instance.titleNumber
                }
            }
        }
        
        return ['instance':coordenroutecanvas_instance]
    }

    //--------------------------------------------------------------------------
    Map saveCoordEnrouteCanvas(String showLanguage, Map params)
    {
        printstart "saveCoordEnrouteCanvas $showLanguage $params"
        Route route_instance = Route.get(params.routeid)

        if (route_instance.enrouteCanvasRoute != EnrouteRoute.InputName) {
            params = calculateCoordEnroute(showLanguage, params, route_instance, false)
            
			EnrouteCanvasSign enroute_canvassign = EnrouteCanvasSign.(params.enrouteCanvasSign)
			if (enroute_canvassign == EnrouteCanvasSign.NoSign) {
				params.enrouteDistance = 0
			}

            // new CoordEnrouteCanvas
            CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas(params)
            coordenroutecanvas_instance.route = route_instance
            
            coordenroutecanvas_instance.enrouteCanvasSign = enroute_canvassign
            if (coordenroutecanvas_instance.enrouteCanvasSign == EnrouteCanvasSign.None) {
                coordenroutecanvas_instance.errors.rejectValue("enrouteCanvasSign", "", getMsg('fc.observation.enroute.canvas.sign.expected'))
                printerror "enrouteCanvasSign is None."
                return ['instance':coordenroutecanvas_instance,'error':true, message:getMsg('fc.observation.enroute.canvas.sign.expected')]
            }
            if (CoordEnrouteCanvas.countByRoute(route_instance) >= route_instance.contest.maxEnrouteCanvas) {
                coordenroutecanvas_instance.errors.rejectValue("enrouteCanvasSign", "", getMsg('fc.observation.enroute.canvas.maxerror',[route_instance.contest.maxEnrouteCanvas.toString()]))
                printerror "Maximum number of enroute canvas reached."
                return ['instance':coordenroutecanvas_instance,'error':true, message:getMsg('fc.observation.enroute.canvas.maxerror',[route_instance.contest.maxEnrouteCanvas.toString()])]
            }
    
            coordenroutecanvas_instance.enrouteViewPos = CoordEnrouteCanvas.countByRoute(route_instance) + 1
    
            if (route_instance.enrouteCanvasRoute == EnrouteRoute.InputName) {
                coordenroutecanvas_instance.type = CoordType.UNKNOWN
            }
            if (route_instance.enrouteCanvasRoute.IsEnrouteRouteInputDistanceFromTP()) {
				if (enroute_canvassign == EnrouteCanvasSign.NoSign) {
					coordenroutecanvas_instance.type = CoordType.FP
					coordenroutecanvas_instance.titleNumber = 1
				} else {
					CoordTitle coordtitle_instance = CoordTitle.get(params.enrouteCoordTitle.split(" : ")[1].toLong())
					coordenroutecanvas_instance.type = coordtitle_instance.type
					coordenroutecanvas_instance.titleNumber = coordtitle_instance.number
				}
            }
            
			if (enroute_canvassign == EnrouteCanvasSign.NoSign) {
				coordenroutecanvas_instance.type = CoordType.FP
				coordenroutecanvas_instance.titleNumber = 1
				coordenroutecanvas_instance.calculateCoordEnrouteValues(EnrouteRoute.InputNMFromTP)
			} else {
				coordenroutecanvas_instance.calculateCoordEnrouteValues(coordenroutecanvas_instance.route.enrouteCanvasRoute)
			}
    
            // save CoordEnrouteCanvas
            if(!coordenroutecanvas_instance.hasErrors() && coordenroutecanvas_instance.save()) {
                route_instance.calculateEnroutCanvasViewPos()
                Map ret = ['saved':true,'message':getMsg('fc.created',["${params.enrouteCanvasSign}"])]
                printdone ret.message
                return ret
            } else {
                printerror ""
                return ['instance':coordenroutecanvas_instance,'error':true]
            }
        } else {
            if (CoordEnrouteCanvas.countByRoute(route_instance) >= route_instance.contest.maxEnrouteCanvas) {
                printerror "Maximum number of enroute canvas reached."
                return ['saved':true, 'error':true, 'message':getMsg('fc.observation.enroute.canvas.maxerror',[route_instance.contest.maxEnrouteCanvas.toString()])]
            }
            int num = 0
            for (EnrouteCanvasSign v in EnrouteCanvasSign.values()) {
                if ((params."enrouteCanvasSign_${v.canvasName}") == "on") {
                    if (CoordEnrouteCanvas.countByRoute(route_instance) >= route_instance.contest.maxEnrouteCanvas) {
                        break
                    }
                    CoordEnrouteCanvas coordenroutecanvas_instance = new CoordEnrouteCanvas(params)
                    coordenroutecanvas_instance.route = route_instance
                    coordenroutecanvas_instance.enrouteCanvasSign = v
                    coordenroutecanvas_instance.enrouteViewPos = CoordEnrouteCanvas.countByRoute(route_instance) + 1
                    if (route_instance.enrouteCanvasRoute == EnrouteRoute.InputName) {
                        coordenroutecanvas_instance.type = CoordType.UNKNOWN
                    }
                    coordenroutecanvas_instance.save()
                    num++
                }
            }
            Map ret = ['saved':true,'message':getMsg('fc.observation.enroute.canvas.sign.created',[num])]
            printdone ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateCoordEnrouteCanvas(String showLanguage, Map params)
    {
        printstart "updateCoordEnrouteCanvas $showLanguage $params"
        
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)
        
        if (coordenroutecanvas_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutecanvas_instance.version > version) {
                    coordenroutecanvas_instance.errors.rejectValue("version", "coordEnrouteCanvas.optimistic.locking.failure", getMsg('fc.notupdated'))
                    printerror ""
                    return ['instance':coordenroutecanvas_instance,'error':true]
                }
            }
            
            if (coordenroutecanvas_instance.route.IsEnrouteSignUsed(false)) {
                return ['instance':coordenroutecanvas_instance,'error':true,'message':getMsg('fc.coordroute.canvas.update.notallowed.routeused')]
                
                /*
                coordenroutecanvas_instance.properties = params

                if(!coordenroutecanvas_instance.hasErrors() && coordenroutecanvas_instance.save()) {
                    Map ret = ['instance':coordenroutecanvas_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutecanvas_instance.enrouteCanvasSign}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordenroutecanvas_instance]
                }
                */
            } else {
                params = calculateCoordEnroute(showLanguage, params, coordenroutecanvas_instance.route, false)
                
                coordenroutecanvas_instance.properties = params

                if (params.enrouteCanvasSign == EnrouteCanvasSign.None.toString()) {
                    coordenroutecanvas_instance.errors.rejectValue("enrouteCanvasSign", "", getMsg('fc.observation.enroute.canvas.sign.expected'))
                    printerror "enrouteCanvasSign is None."
                    return ['instance':coordenroutecanvas_instance,'error':true, message:getMsg('fc.observation.enroute.canvas.sign.expected')]
                }
                
                coordenroutecanvas_instance.enrouteCanvasSign = EnrouteCanvasSign.(params.enrouteCanvasSign)
                
                if (coordenroutecanvas_instance.route.enrouteCanvasRoute.IsEnrouteRouteInputDistanceFromTP()) {
                    CoordTitle coordtitle_instance = CoordTitle.get(params.enrouteCoordTitle.split(" : ")[1].toLong())
                    coordenroutecanvas_instance.type = coordtitle_instance.type
                    coordenroutecanvas_instance.titleNumber = coordtitle_instance.number
                }

                if (!params.enrouteDistance) {
                    coordenroutecanvas_instance.enrouteDistance = null
                }
                if (!params.measureDistance) {
                    coordenroutecanvas_instance.measureDistance = null
                }

                coordenroutecanvas_instance.calculateCoordEnrouteValues(coordenroutecanvas_instance.route.enrouteCanvasRoute)
                
                if(!coordenroutecanvas_instance.hasErrors() && coordenroutecanvas_instance.save()) {
                    coordenroutecanvas_instance.route.calculateEnroutCanvasViewPos()
                    Map ret = ['instance':coordenroutecanvas_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutecanvas_instance.enrouteCanvasSign}"])]
                    printdone ret.message
                    return ret
                } else {
                    printerror ""
                    return ['instance':coordenroutecanvas_instance,'error':true]
                }
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetmeasureCoordEnrouteCanvas(Map params)
    {
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)
        
        if (coordenroutecanvas_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutecanvas_instance.version > version) {
                    coordenroutecanvas_instance.errors.rejectValue("version", "coordEnrouteCanvas.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordenroutecanvas_instance]
                }
            }
            
            if (coordenroutecanvas_instance.route.IsEnrouteSignUsed(false)) {
                return ['instance':coordenroutecanvas_instance,'error':true,'message':getMsg('fc.coordroute.canvas.update.notallowed.routeused')]
            }
            
            coordenroutecanvas_instance.measureDistance = null
            
            coordenroutecanvas_instance.calculateCoordEnrouteValues(coordenroutecanvas_instance.route.enrouteCanvasRoute)

            if(!coordenroutecanvas_instance.hasErrors() && coordenroutecanvas_instance.save()) {
                return ['instance':coordenroutecanvas_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutecanvas_instance.enrouteCanvasSign}"])]
            } else {
                return ['instance':coordenroutecanvas_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map addpositionCoordEnrouteCanvas(Map params)
    {
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)
        
        if (coordenroutecanvas_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutecanvas_instance.version > version) {
                    coordenroutecanvas_instance.errors.rejectValue("version", "coordEnrouteCanvas.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordenroutecanvas_instance]
                }
            }
            
            CoordEnrouteCanvas coordenroutecanvas_instance2 = CoordEnrouteCanvas.findByRouteAndEnrouteViewPos(coordenroutecanvas_instance.route, coordenroutecanvas_instance.enrouteViewPos + 1)
            coordenroutecanvas_instance.enrouteViewPos++
            
            if(!coordenroutecanvas_instance.hasErrors() && coordenroutecanvas_instance.save()) {
                coordenroutecanvas_instance2.enrouteViewPos--
                coordenroutecanvas_instance2.save()
                return ['instance':coordenroutecanvas_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutecanvas_instance.enrouteCanvasSign}"])]
            } else {
                return ['instance':coordenroutecanvas_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map subpositionCoordEnrouteCanvas(Map params)
    {
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)
        
        if (coordenroutecanvas_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(coordenroutecanvas_instance.version > version) {
                    coordenroutecanvas_instance.errors.rejectValue("version", "coordEnrouteCanvas.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordenroutecanvas_instance]
                }
            }
            
            CoordEnrouteCanvas coordenroutecanvas_instance2 = CoordEnrouteCanvas.findByRouteAndEnrouteViewPos(coordenroutecanvas_instance.route, coordenroutecanvas_instance.enrouteViewPos - 1)
            coordenroutecanvas_instance.enrouteViewPos--
            
            if(!coordenroutecanvas_instance.hasErrors() && coordenroutecanvas_instance.save()) {
                coordenroutecanvas_instance2.enrouteViewPos++
                coordenroutecanvas_instance2.save()
                return ['instance':coordenroutecanvas_instance,'saved':true,'message':getMsg('fc.updated',["${coordenroutecanvas_instance.enrouteCanvasSign}"])]
            } else {
                return ['instance':coordenroutecanvas_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteCoordEnrouteCanvas(Map params)
    {
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)
        
        if (coordenroutecanvas_instance) {
            if (coordenroutecanvas_instance.route.IsEnrouteSignUsed(false)) { 
                return ['instance':coordenroutecanvas_instance,'error':true,'message':getMsg('fc.coordroute.canvas.update.notallowed.routeused')]
            }
            
            try {
                Route route_instance = coordenroutecanvas_instance.route
                
                int deleted_enrouteviewpos = coordenroutecanvas_instance.enrouteViewPos
                coordenroutecanvas_instance.delete()
                
                // correct all CoordEnrouteCanvas's enrouteViewPos
                CoordEnrouteCanvas.findAllByRoute(route_instance,[sort:"id"]).each { CoordEnrouteCanvas coordenroutecanvas_instance2 ->
                    if (coordenroutecanvas_instance2.enrouteViewPos > deleted_enrouteviewpos) {
                        coordenroutecanvas_instance2.enrouteViewPos--
                        coordenroutecanvas_instance2.save()
                    }
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${coordenroutecanvas_instance.enrouteCanvasSign}"]),'routeid':route_instance.id]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.coordroute'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map removeallCoordEnrouteCanvas(Map params)
    {
        Route route_instance = Route.get(params.routeid)
        
        if (route_instance.IsEnrouteSignUsed(false)) {
            return ['instance':route_instance,'error':true,'message':getMsg('fc.coordroute.canvas.update.notallowed.routeused')]
        }
        
        for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(route_instance)) {
            if (coordenroutecanvas_instance) {
                coordenroutecanvas_instance.delete()
            }
        }
        
        return ['deleted':true,'message':getMsg('fc.coordroute.canvas.removedall'),'routeid':route_instance.id]
    }
    
    //--------------------------------------------------------------------------
    private void calculateEnrouteValues(Route routeInstance)
    {
        printstart "calculateEnrouteValues"
        if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:'enrouteViewPos'])) {        
                coordenroutephoto_instance.calculateCoordEnrouteValues(coordenroutephoto_instance.route.enroutePhotoRoute)
                coordenroutephoto_instance.save()
            }
            routeInstance.calculateEnroutPhotoViewPos()
        }
        if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:'enrouteViewPos'])) {
				coordenroutecanvas_instance.calculateCoordEnrouteValues(coordenroutecanvas_instance.route.enrouteCanvasRoute)
				if (coordenroutecanvas_instance.enrouteCanvasSign == EnrouteCanvasSign.NoSign) {
					coordenroutecanvas_instance.type = CoordType.FP
					coordenroutecanvas_instance.titleNumber = 1
					coordenroutecanvas_instance.enrouteDistanceOk = true
				}
				coordenroutecanvas_instance.save()
            }
            routeInstance.calculateEnroutCanvasViewPos()
        }
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private Map calculateCoordEnroute(String showLanguage, Map params, Route routeInstance, boolean enroutePhoto) 
    {
        if (enroutePhoto) {
            if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInputCoord()) {
                params = calculateCoordCoord(showLanguage, params, routeInstance.contest.coordPresentation)
            }
        } else {
            if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputCoord()) {
                params = calculateCoordCoord(showLanguage, params, routeInstance.contest.coordPresentation)
            }
        }
        if (params.enrouteDistance) {
            params.enrouteDistance = Languages.GetLanguageDecimal(showLanguage, params.enrouteDistance)
        }
        if (params.measureDistance) {
            params.measureDistance = Languages.GetLanguageDecimal(showLanguage, params.measureDistance)
        }
        return params
    }
    
    //--------------------------------------------------------------------------
    private Map calculateCoordCoord(String showLanguage, Map params, CoordPresentation coordPresentation)
    {
        println "calculateCoordCoord"
        switch (coordPresentation) {
            case CoordPresentation.DEGREE:
                params.latGradDecimal = Languages.GetLanguageDecimal(showLanguage, params.latGradDecimal)
                params.lonGradDecimal = Languages.GetLanguageDecimal(showLanguage, params.lonGradDecimal)
                Map lat = CoordPresentation.GetDirectionGradDecimalMinute(FcMath.toBigDecimal(params.latGradDecimal), true)
                Map lon = CoordPresentation.GetDirectionGradDecimalMinute(FcMath.toBigDecimal(params.lonGradDecimal), false)
                params.latDirection = lat.direction
                params.lonDirection = lon.direction
                params.latGrad = lat.grad
                params.lonGrad = lon.grad
                params.latMinute = lat.minute
                params.lonMinute = lon.minute
                break
            case CoordPresentation.DEGREEMINUTE:
                params.latMinute = Languages.GetLanguageDecimal(showLanguage, params.latMinute)
                params.lonMinute = Languages.GetLanguageDecimal(showLanguage, params.lonMinute)
                break
            case CoordPresentation.DEGREEMINUTESECOND:
                params.latSecondDecimal = Languages.GetLanguageDecimal(showLanguage, params.latSecondDecimal)
                params.lonSecondDecimal = Languages.GetLanguageDecimal(showLanguage, params.lonSecondDecimal)
                params.latMinute = FcMath.toBigDecimal(params.latMin) + FcMath.toBigDecimal(params.latSecondDecimal) / 60
                params.lonMinute = FcMath.toBigDecimal(params.lonMin) + FcMath.toBigDecimal(params.lonSecondDecimal) / 60
                break
        }
        return params
    }

    //--------------------------------------------------------------------------
    Map getCrew(Map params)
    {
        Crew crew_instance = Crew.get(params.id)

        if (!crew_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
        
        return ['instance':crew_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCrew(Map params)
    {
		printstart "updateCrew: $params.name"
        Crew crew_instance = Crew.get(params.id)
        if (crew_instance) {

            if (params.version) {
                long version = params.version.toLong()
                if(crew_instance.version > version) {
                    crew_instance.errors.rejectValue("version", "crew.optimistic.locking.failure", getMsg('fc.notupdated'))
					printerror ""
                    return ['instance':crew_instance]
                }
            }
			
			if (!params.startNum) {
                crew_instance.errors.rejectValue("startNum", "", getMsg('fc.crew.startnum.emptyerror'))
				printerror "startNum is empty."
                return ['instance':crew_instance]
			}

			Crew startnum_crew_instance = Crew.findByStartNumAndContest(params.startNum,crew_instance.contest)
			if (startnum_crew_instance && startnum_crew_instance != crew_instance) {
	            crew_instance.errors.rejectValue("startNum", "", getMsg('fc.crew.startnum.uniqueerror',[params.startNum]))
				printerror "startNum is not unique."
	            return ['instance':crew_instance]
			}

            if (!crew_instance.hasErrors()) {

				boolean modify_tas = false
                if (params.tas) {
                    modify_tas = crew_instance.tas != FcMath.toBigDecimal(params.tas)
                }
				boolean modify_aircraft = crew_instance.aircraft.toString() != params.aircraft 
                boolean modify_tracker = false
                if (params.trackerID) {
                    modify_tracker = crew_instance.trackerID != params.trackerID
                }
				boolean old_disabled = crew_instance.disabled
                boolean old_disabled_team = crew_instance.disabledTeam
                boolean old_disabled_contest = crew_instance.disabledContest
                boolean old_increase_enabled = crew_instance.increaseEnabled
            	Aircraft old_aircraft_instance = crew_instance.aircraft
	            crew_instance.properties = params
				if (params.tas) {
					crew_instance.tas = FcMath.toBigDecimal(params.tas)
				}
		
				if (old_disabled != crew_instance.disabled) {
					println "Crew dis/enabled."
                    resetPositionCrew(crew_instance)
				}
                if (old_disabled_team != crew_instance.disabledTeam) {
                    println "Crew for team evaluation dis/enabled."
                    resetTeamPositionCrew(crew_instance)
                }
                if (old_disabled_contest != crew_instance.disabledContest) {
                    println "Crew for contest evaluation dis/enabled."
                    resetContestPositionCrew(crew_instance)
                }
                if (old_increase_enabled != crew_instance.increaseEnabled) {
                    println "Crew for increase dis/enabled."
                    resetPositionCrew(crew_instance)
                }
                
	            if (old_aircraft_instance) {
	            	if (crew_instance == old_aircraft_instance.user2) {
	            		old_aircraft_instance.user2 = null
	            	} else if (crew_instance == old_aircraft_instance.user1) {
	            		old_aircraft_instance.user1 = old_aircraft_instance.user2
	            		old_aircraft_instance.user2 = null
	            	}
	                old_aircraft_instance.save()
	            }
	
                if (crew_instance.aircraft) {
	                if (!crew_instance.aircraft.user1) {
		            	crew_instance.aircraft.user1 = crew_instance
		                crew_instance.aircraft.save()
		            } else if (!crew_instance.aircraft.user2) {
		            	crew_instance.aircraft.user2 = crew_instance
		            	crew_instance.aircraft.save()
		            } else {
		            	crew_instance.aircraft = null
		            }
                }
	
                if (crew_instance.save()) {

					int no_modify_tas_num = 0
					int no_modify_aircraft_num = 0
                    int no_modify_tracker_num = 0
                    if (old_increase_enabled != crew_instance.increaseEnabled) {
                        println "Increase modified."
                        Test.findAllByCrew(crew_instance,[sort:"id"]).each { Test test_instance ->
                            test_instance.CalculateTestPenalties()
                            test_instance.flightTestLink = ""
							delete_uploadjobtest(test_instance)
                            test_instance.crewResultsModified = true
                            test_instance.save()
                        }
                    }
					if (modify_tas) {
						println "TAS modified."
		                Test.findAllByCrew(crew_instance,[sort:"id"]).each { Test test_instance ->
							if (test_instance.planningtesttask || test_instance.timeCalculated) {
								println "'${test_instance.task.name()}' '$test_instance.crew.name': do nothing."
								no_modify_tas_num++
							} else {
								test_instance.taskTAS = crew_instance.tas 
								calulateTestLegPlannings(test_instance)
								println "Reset results"
								test_instance.ResetPlanningTestResults()
			                	test_instance.timeCalculated = false
								test_instance.ResetFlightTestResults()
								test_instance.CalculateTestPenalties()
                                test_instance.flightTestLink = ""
								delete_uploadjobtest(test_instance)
                                test_instance.crewResultsModified = true
			                    test_instance.save()
							}
		                }
					}
					if (modify_aircraft) {
						println "Aircraft modified."
						Test.findAllByCrew(crew_instance,[sort:"id"]).each { Test test_instance ->
							if (test_instance.planningtesttask || test_instance.timeCalculated) {
								println "'${test_instance.task.name()}' '$test_instance.crew.name': do nothing."
								no_modify_aircraft_num++
							} else {
								test_instance.taskAircraft = crew_instance.aircraft
								test_instance.save()
							}
						}
					}
                    if (modify_tracker) {
						println "Tracker modified."
						Test.findAllByCrew(crew_instance,[sort:"id"]).each { Test test_instance ->
							if (test_instance.timeCalculated) {
								println "'${test_instance.task.name()}' '$test_instance.crew.name': do nothing."
								no_modify_tracker_num++
							} else {
								test_instance.taskTrackerID = crew_instance.trackerID
								test_instance.save()
							}
						}
                    }

					printdone ""
					if (no_modify_tas_num) {
						return ['instance':crew_instance,'saved':true,'message':getMsg('fc.crew.updated.nomodify',["${crew_instance.name}",no_modify_tas_num.toString(),no_modify_aircraft_num.toString(),no_modify_tracker_num.toString()])]
					} else {
						return ['instance':crew_instance,'saved':true,'message':getMsg('fc.updated',["${crew_instance.name}"])]
					}
                } else {
					printerror ""
                    return ['instance':crew_instance]
                }
            } else {
				printerror ""
                return ['instance':crew_instance]
            }
        } else {
			printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createCrew(Map params)
    {
        Crew crew_instance = new Crew()
        crew_instance.properties = params
        return ['instance':crew_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveCrew(Map params, Contest contestInstance)
    {
		printstart "saveCrew [$params]"
		
        Crew crew_instance = new Crew(params)
		if (params.tas) {
			crew_instance.tas = FcMath.toBigDecimal(params.tas) 
		}
        crew_instance.contest = contestInstance
		crew_instance.viewpos = Crew.countByContest(contestInstance)
		
		if (!params.startNum) {
            crew_instance.errors.rejectValue("startNum", "", getMsg('fc.crew.startnum.emptyerror'))
			printerror "startNum is empty."
            return ['instance':crew_instance]
		}

		if (Crew.findByStartNumAndContest(params.startNum,contestInstance)) {
            crew_instance.errors.rejectValue("startNum", "", getMsg('fc.crew.startnum.uniqueerror',[params.startNum]))
			printerror "startNum is not unique."
            return ['instance':crew_instance]
		}
		
        if (!crew_instance.hasErrors() && crew_instance.save()) {
            
            if (params.registration) {
				println "Add aircraft $params.registration"
                Aircraft aircraft_instance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
                if (!aircraft_instance) {
                    aircraft_instance = new Aircraft(params)
                    aircraft_instance.contest = crew_instance.contest
					aircraft_instance.type = params.type
					aircraft_instance.colour = params.colour
                }
                if (!aircraft_instance.user1) {
                	aircraft_instance.user1 = crew_instance
                } else if (!aircraft_instance.user2) {
                    aircraft_instance.user2 = crew_instance
                } else {
                	aircraft_instance = null
                }
                if (aircraft_instance) {
                    if(!aircraft_instance.hasErrors() && aircraft_instance.save()) {
                        crew_instance.aircraft = aircraft_instance 
                        crew_instance.save(flush:true)
						println "saveCrew (aircraft): $crew_instance.name saved (flush:true)."
                    }
                }
            }

            if (params.teamname) {
				Team team_instance = Team.findByNameAndContest(params.teamname,contestInstance)
                if (!team_instance) {
                    team_instance = new Team(name:params.teamname)
                    team_instance.contest = crew_instance.contest 
                }
                if (team_instance) {
                    if(!team_instance.hasErrors() && team_instance.save()) {
						crew_instance.team = team_instance 
                        crew_instance.save(flush:true)
						println "saveCrew (team): $crew_instance.name saved (flush:true)."
                    }
                }
			}
			
            if (params.resultclassname) {
				ResultClass resultclass_instance = ResultClass.findByNameAndContest(params.resultclassname,contestInstance)
                if (!resultclass_instance) {
                    resultclass_instance = new ResultClass(name:params.resultclassname)
                    resultclass_instance.contest = crew_instance.contest
					resultclass_instance.contestRule = crew_instance.contest.contestRule
					setContestRulePoints(resultclass_instance, resultclass_instance.contestRule)
					resultclass_instance.shortName = resultclass_instance.GetDefaultShortName()
					resultclass_instance.save()
					println "saveCrew (new class): $resultclass_instance.name saved."
					
					// create TaskClasses
					if (contestInstance.resultClasses) {
						for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"idTitle"])) {
							TaskClass taskclass_instance = new TaskClass()
							taskclass_instance.task = task_instance
							taskclass_instance.resultclass = resultclass_instance
							taskclass_instance.save()
							println "saveCrew (new taskclass): Taskclass for task '${task_instance.name()}' saved."
						}
					}
                }
                if (resultclass_instance) {
                    if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
						crew_instance.resultclass = resultclass_instance 
                        crew_instance.save(flush:true)
						println "saveCrew (class): $crew_instance.name saved (flush:true)."
                    }
                }
			}
			
            Task.findAllByContest(contestInstance,[sort:"idTitle"]).each { Task task_instance ->
				//if (!task_instance.hidePlanning) {
	                Test test_instance = new Test()
	                test_instance.crew = crew_instance
                    test_instance.disabledCrew = true
					test_instance.taskTAS = crew_instance.tas
					test_instance.taskAircraft = crew_instance.aircraft
                    test_instance.taskTrackerID = crew_instance.trackerID
	                test_instance.viewpos = Crew.countByContest(contestInstance) - 1
	                test_instance.task = task_instance
	                test_instance.timeCalculated = false
                    test_instance.loggerData = new LoggerDataTest()
                    test_instance.loggerResult = new LoggerResult()
	                test_instance.save()
                    if (task_instance.flighttest?.route?.IsObservationSignOk() && task_instance.flighttest?.IsObservationSignUsed()) {
                        generate_observation(test_instance, task_instance.flighttest.route)
                    }
				//}
            }
			
            String msg
            if (crew_instance.aircraft) {
            	msg = getMsg('fc.crew.withaircraft.created',["${crew_instance.name}", "${crew_instance.aircraft.registration}"])
            } else {
                msg = getMsg('fc.created',["${crew_instance.name}"])
            }
			printdone msg
            return ['instance':crew_instance,'saved':true,'message':msg]
        } else {
			printerror "Not saved."
            return ['instance':crew_instance]
        }
            
    }
    
    //--------------------------------------------------------------------------
    Map deleteCrew(Map params) 
    {
		printstart "deleteCrew"
		
        Crew crew_instance = Crew.get(params.id)
        
        if(crew_instance) {
            try {
				delete_crew(crew_instance)
				Map ret = ['deleted':true,'message':getMsg('fc.deleted',["${crew_instance.name}"])]
				printdone ret 
                return ret
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                Map ret = ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.crew'),params.id])]
				printerror ret
                return ret
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.crew'),params.id])]
			printerror ret
            return ret
        }
    }

    //--------------------------------------------------------------------------
	Map deleteCrews(Contest contestInstance, Map params, session)
	{
		printstart "deleteCrews"
		
		Contest contest_instance = Contest.get(contestInstance.id)
		int delete_num = 0
		 
        Crew.findAllByContest(contest_instance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
				delete_crew(crew_instance)
				delete_num++
            }
        }

		Map ret = ['deleted':delete_num > 0,'message':getMsg('fc.crew.deleted',["${delete_num}"])]
		printdone ret
        return ret
	}
	
    //--------------------------------------------------------------------------
    Map disableCrews(Contest contestInstance, Map params, session)
    {
        printstart "disableCrews"
        
        int disable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (!crew_instance.disabled) {
                    crew_instance.disabled = true
                    println "Crew $crew_instance.name disabled."
                    resetPositionCrew(crew_instance)
                    crew_instance.save()
                    disable_num++
                }
            }
        }
        
        Map ret = ['disabled':disable_num > 0,'message':getMsg('fc.crew.disabled',["${disable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map enableCrews(Contest contestInstance, Map params, session)
    {
        printstart "enableCrews"
        
        int enable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.disabled) {
                    crew_instance.disabled = false
                    println "Crew $crew_instance.name enabled."
                    resetPositionCrew(crew_instance)
                    crew_instance.save()
                    enable_num++
                }
            }
        }
        
        Map ret = ['enabled':enable_num > 0,'message':getMsg('fc.crew.enabled',["${enable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map disableTeamCrews(Contest contestInstance, Map params, session)
    {
        printstart "disableTeamCrews"
        
        int disable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (!crew_instance.disabledTeam) {
                    crew_instance.disabledTeam = true
                    println "Crew $crew_instance.name for team evaluation disabled."
                    resetTeamPositionCrew(crew_instance)
                    crew_instance.save()
                    disable_num++
                }
            }
        }
        
        Map ret = ['disabled':disable_num > 0,'message':getMsg('fc.crew.disabled',["${disable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map enableTeamCrews(Contest contestInstance, Map params, session)
    {
        printstart "enableTeamCrews"
        
        int enable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.disabledTeam) {
                    crew_instance.disabledTeam = false
                    println "Crew $crew_instance.name for team evaluation enabled."
                    resetTeamPositionCrew(crew_instance)
                    crew_instance.save()
                    enable_num++
                }
            }
        }
        
        Map ret = ['enabled':enable_num > 0,'message':getMsg('fc.crew.enabled',["${enable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map disableContestCrews(Contest contestInstance, Map params, session)
    {
        printstart "disableContestCrews"
        
        int disable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (!crew_instance.disabledContest) {
                    crew_instance.disabledContest = true
                    println "Crew $crew_instance.name for contest evaluation disabled."
                    resetContestPositionCrew(crew_instance)
                    crew_instance.save()
                    disable_num++
                }
            }
        }
        
        Map ret = ['disabled':disable_num > 0,'message':getMsg('fc.crew.disabled',["${disable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map enableContestCrews(Contest contestInstance, Map params, session)
    {
        printstart "enableContestCrews"
        
        int enable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.disabledContest) {
                    crew_instance.disabledContest = false
                    println "Crew $crew_instance.name for contest evaluation enabled."
                    resetContestPositionCrew(crew_instance)
                    crew_instance.save()
                    enable_num++
                }
            }
        }
        
        Map ret = ['enabled':enable_num > 0,'message':getMsg('fc.crew.enabled',["${enable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map disableIncreaseCrews(Contest contestInstance, Map params, session)
    {
        printstart "disableIncreaseCrews"
        
        int disable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.increaseEnabled) {
                    crew_instance.increaseEnabled = false
                    println "Crew $crew_instance.name for increase disabled."
                    resetPositionCrew(crew_instance)
                    crew_instance.save()
                    Test.findAllByCrew(crew_instance,[sort:"id"]).each { Test test_instance ->
                        test_instance.CalculateTestPenalties()
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                        test_instance.save()
                    }
                    disable_num++
                }
            }
        }
        
        Map ret = ['disabled':disable_num > 0,'message':getMsg('fc.crew.disabled',["${disable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map enableIncreaseCrews(Contest contestInstance, Map params, session)
    {
        printstart "enableIncreaseCrews"
        
        int enable_num = 0
         
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (!crew_instance.increaseEnabled) {
                    crew_instance.increaseEnabled = true
                    println "Crew $crew_instance.name for increase enabled."
                    resetPositionCrew(crew_instance)
                    crew_instance.save()
                    Test.findAllByCrew(crew_instance,[sort:"id"]).each { Test test_instance ->
                        test_instance.CalculateTestPenalties()
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                        test_instance.save()
                    }
                    enable_num++
                }
            }
        }
        
        Map ret = ['enabled':enable_num > 0,'message':getMsg('fc.crew.enabled',["${enable_num}"])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
	private void delete_crew(Crew crewInstance)
	{
		println "Crew '$crewInstance.name' deleted"
        
        resetPositionCrew(crewInstance)
        
        if (crewInstance.aircraft) {
        	if (crewInstance == crewInstance.aircraft.user2) {
        		crewInstance.aircraft.user2 = null
        	} else {
                crewInstance.aircraft.user1 = crewInstance.aircraft.user2
                crewInstance.aircraft.user2 = null
        	}
            crewInstance.aircraft.save()
            
            crewInstance.aircraft = null 
        }
		
        // remove crew tests
        Test.findAllByCrew(crewInstance,[sort:"id"]).each { Test test_instance ->
            test_instance.delete()
        }
        
        // correct all test's viewpos
        Task.findAllByContest(crewInstance.contest,[sort:"idTitle"]).each { Task task_instance ->
        	Test.findAllByTask(task_instance,[sort:"viewpos"]).eachWithIndex { Test test_instance, int i ->
        		test_instance.viewpos = i
        		test_instance.save()
        	}
        }

		int deleted_viewpos = crewInstance.viewpos 
        crewInstance.delete()
        
        // correct all crew's viewpos
		Crew.findAllByContest(crewInstance.contest,[sort:"id"]).each { Crew crew_instance2 ->
			if (crew_instance2.viewpos > deleted_viewpos) {
				crew_instance2.viewpos--
				crew_instance2.save()
			}
		}
		
	}
	
    //--------------------------------------------------------------------------
    private void resetPositionCrew(Crew crewInstance)
    {
        crewInstance.planningPenalties = 0
        crewInstance.flightPenalties = 0
        crewInstance.observationPenalties = 0
        crewInstance.landingPenalties = 0
        crewInstance.specialPenalties = 0
        crewInstance.contestPenalties = 0
        crewInstance.contestPosition = 0
        crewInstance.noContestPosition = false
        crewInstance.contestEqualPosition = false
        crewInstance.contestAddPosition = 0
        crewInstance.classPosition = 0
        crewInstance.noClassPosition = false
        crewInstance.classEqualPosition = false
        crewInstance.classAddPosition = 0
        crewInstance.teamPenalties = 0
        if (crewInstance.team) {
            crewInstance.team.contestPenalties = 0
            crewInstance.team.contestPosition = 0
        }
        for (Crew crew_instance in Crew.findAllByContest(crewInstance.contest,[sort:"id"])) {
            boolean run = false
            if (crew_instance != crewInstance) {
                run = true
            }
            if (run) {
                
                crew_instance.planningPenalties = 0
                crew_instance.flightPenalties = 0
                crew_instance.observationPenalties = 0
                crew_instance.landingPenalties = 0
                crew_instance.specialPenalties = 0
                crew_instance.contestPenalties = 0
                crew_instance.contestPosition = 0
                crew_instance.noContestPosition = false
                crew_instance.contestEqualPosition = false
                crew_instance.contestAddPosition = 0
                crew_instance.classPosition = 0
                crew_instance.noClassPosition = false
                crew_instance.classEqualPosition = false
                crew_instance.classAddPosition = 0
                crew_instance.teamPenalties = 0
                if (crew_instance.team) {
                    crew_instance.team.contestPenalties = 0
                    crew_instance.team.contestPosition = 0
                }
                crew_instance.save()
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void resetContestPositionCrew(Crew crewInstance)
    {
        crewInstance.contestPenalties = 0
        crewInstance.contestPosition = 0
        crewInstance.noContestPosition = false
        crewInstance.contestEqualPosition = false
        crewInstance.contestAddPosition = 0
        crewInstance.classPosition = 0
        crewInstance.noClassPosition = false
        crewInstance.classEqualPosition = false
        crewInstance.classAddPosition = 0
        for (Crew crew_instance in Crew.findAllByContest(crewInstance.contest,[sort:"id"])) {
            boolean run = false
            if (crew_instance != crewInstance) {
                run = true
            }
            if (run) {
                
                crew_instance.contestPenalties = 0
                crew_instance.contestPosition = 0
                crew_instance.noContestPosition = false
                crew_instance.contestEqualPosition = false
                crew_instance.contestAddPosition = 0
                crew_instance.classPosition = 0
                crew_instance.noClassPosition = false
                crew_instance.classEqualPosition = false
                crew_instance.classAddPosition = 0
                crew_instance.save()
            }
        }

    }

    //--------------------------------------------------------------------------
    private void resetTeamPositionCrew(Crew crewInstance)
    {
        crewInstance.teamPenalties = 0
        if (crewInstance.team) {
            crewInstance.team.contestPenalties = 0
            crewInstance.team.contestPosition = 0
        }
        for (Crew crew_instance in Crew.findAllByContest(crewInstance.contest,[sort:"id"])) {
            boolean run = false
            if (crew_instance != crewInstance) {
                run = true
            }
            if (run) {
                crew_instance.teamPenalties = 0
                if (crew_instance.team) {
                    crew_instance.team.contestPenalties = 0
                    crew_instance.team.contestPosition = 0
                }
                crew_instance.save()
            }
        }
    }
    
    //--------------------------------------------------------------------------
    Map setaircraftCrew(Map crew,Map aircraft)
    {
		printstart "setaircraftCrew: $aircraft.instance.registration $crew.instance.name"
        crew.instance.aircraft = aircraft.instance
        Map p = [:]
        p.id = crew.instance.id
		p.name = crew.instance.name
        Map ret = updateCrew(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map selectallCrew(Contest contestInstance)
    {
        Map crew = [:] 
        Map selected_crewids = [selectedCrewID:""]
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
			selected_crewids["selectedCrewID${crew_instance.id}"] = "on"
        }
        crew.selectedcrewids = selected_crewids
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map calculatesequenceCrew(Contest contestInstance, Map params)
    {
		printstart "calculatesequenceCrew"
		
        Map crew = [:] 

		/*
        // Have all crews an aircraft?
        boolean call_return = false
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (!crew_instance.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            crew.message = getMsg('fc.aircraft.notassigned')
            crew.error = true
            return crew
        }
        */

    	// set viewpos for aircraft of user1 
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
			if (!crew_instance.disabled) {
	        	if (crew_instance.aircraft) {
	        		if (crew_instance.aircraft.user1 == crew_instance) {
	        			crew_instance.viewpos = 4000+crew_instance.tas
	        		}
	        	}
			}
        }

        // set viewpos for aircraft of user2 
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
			if (!crew_instance.disabled) {
	            if (crew_instance.aircraft) {
	                if (crew_instance.aircraft.user2 == crew_instance) {
	                    crew_instance.viewpos = 3000+crew_instance.tas
	                }
	            }
			}
        }

        // set viewpos for user without aircraft 
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
			if (!crew_instance.disabled) {
	            if (!crew_instance.aircraft) {
	                crew_instance.viewpos = 2000+crew_instance.tas
	            }
			}
        }

        // set viewpos for disabled user 
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
			if (crew_instance.disabled) {
                crew_instance.viewpos = 1000+crew_instance.tas
			}
        }

        // set viewpos
        Crew.findAllByContest(contestInstance,[sort:"viewpos",order:"desc"]).eachWithIndex { Crew crew_instance, int i ->
            crew_instance.viewpos = i
            crew_instance.save()
        }
        
        crew.message = getMsg('fc.test.sequence.calculated')    
		printdone crew.message    
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map moveupCrew(Contest contestInstance, Map params, session)
    {
		Map crew = [:]
		
		// moveable? (not top & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.viewpos == 0) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
            crew.borderreached = true
            return crew
        }
        if (notmovable) {
            crew.message = getMsg('fc.test.notmovable')
            crew.error = true
            return crew
        }
        
		// move tasks
		int movenum = 0
        int movefirstpos = -1
        Map selected_crewids = [selectedCrewID:""]
        borderreached = false
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                crew_instance.viewpos--
                crew_instance.save()
                selected_crewids["selectedCrewID${crew_instance.id}"] = "on"
                if (crew_instance.viewpos == 0) {
                    borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || crew_instance.viewpos < movefirstpos) {
                    movefirstpos = crew_instance.viewpos
                }
            }
        }
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] != "on") {
                if (crew_instance.viewpos >= movefirstpos && crew_instance.viewpos < movefirstpos + movenum) {
                    crew_instance.viewpos += movenum
                    crew_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos < session.showLimitStartPos) {
				if (movenum == 1) {
					if (session.showLimitStartPos > session.showLimitCrewNum) {
						session.showLimitStartPos -= session.showLimitCrewNum
					} else {
						session.showLimitStartPos = 0
					}
				} else {
					session.showLimitStartPos--
				}
			}
		}
		
		// restore selection if not top
        if (!borderreached) {
            crew.selectedcrewids = selected_crewids
        }
        return crew
    }
    
    //--------------------------------------------------------------------------
    Map movedownCrew(Contest contestInstance, Map params, session)
    {
        Map crew = [:] 

		// moveable? (not bottom & connected selection)
        boolean borderreached = false
        boolean notmovable = false
        boolean off2on = false
        boolean on2off = false
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.viewpos + 1 == Crew.countByContest(contestInstance)) {
                    borderreached = true
                }
                if (off2on && on2off) {
                    notmovable = true
                }
                off2on = true
            } else {
                if (off2on) {
                    on2off = true
                }
            }
        }
        if (borderreached) {
            crew.borderreached = true
            return crew
        }
        if (notmovable) {
            crew.message = getMsg('fc.test.notmovable')
            crew.error = true
            return crew
        }
        
		// move tasks
        int movenum = 0
        int movefirstpos = -1
        Map selected_crewids = [selectedCrewID:""]
        borderreached = false
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                crew_instance.viewpos++
                crew_instance.save()
                selected_crewids["selectedCrewID${crew_instance.id}"] = "on"
                if (crew_instance.viewpos + 1 == Crew.countByContest(contestInstance)) {
					borderreached = true
                }
                movenum++
                if (movefirstpos == -1 || crew_instance.viewpos < movefirstpos) {
                    movefirstpos = crew_instance.viewpos
                }
            }
        }
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] != "on") {
                if (crew_instance.viewpos >= movefirstpos && crew_instance.viewpos < movefirstpos + movenum) {
                    crew_instance.viewpos -= movenum
                    crew_instance.save()
                }
            }
        }

		// modify showLimitStartPos
		if (session.showLimit) {
			if (movefirstpos + movenum > session.showLimitStartPos + session.showLimitCrewNum) {
				if (movenum == 1) {
					int crew_num = Crew.countByContest(session.lastContest)
					if (session.showLimitStartPos + session.showLimitCrewNum < crew_num) {
						session.showLimitStartPos += session.showLimitCrewNum
					}
				} else {
					session.showLimitStartPos++
				}
			}
		}
		
		// restore selection if not bottom
        if (!borderreached) {
            crew.selectedcrewids = selected_crewids
        }
        return crew
    }
    
    //--------------------------------------------------------------------------
	void importCrewList(Map contest, String originalFileName, boolean noUnsuitableStartNum)
	{
		printstart "importCrewList '$originalFileName'"
        
        String webroot_dir = servletContext.getRealPath("/")
        String file_name = webroot_dir + "testdata/" + originalFileName
        
		//println file.getContentType() // "application/vnd.ms-excel", "application/octet-stream"
		if (file_name.toLowerCase().endsWith('.xlsx')) {
			def crews = importCrews(file_name, file_name, noUnsuitableStartNum, contest.instance)
			if (crews.saved) {
				printdone crews.message
			} else if (crews.error) {
				printerror crews.message
			}
		} else {
			printerror "No excel file (*.xls)."
		}
	}
	
    //--------------------------------------------------------------------------
    Map importCrews(String fileName, String loadFileName, boolean noUnsuitableStartNum, Contest contestInstance)
    {
		printstart "importCrews $fileName [$loadFileName]"
		int new_crew_num = 0
		int new_crew_error_num = 0
        
        Map ret3 = CrewTools.ImportExcelCrews(loadFileName, contestInstance)
        if (ret3.validFile) {
            Map ret2 = importCrews2(ret3.crewList, noUnsuitableStartNum, false, contestInstance)
            new_crew_num = ret2.new_crew_num
            new_crew_error_num = ret2.new_crew_error_num
        }
		
		Map ret = [:]
		if (!ret3.validFile) {
            ret = ['error':true,'message':getMsg('fc.notimported.excel.invalid',[fileName])]
		} else if (new_crew_num) {
			ret = ['saved':true,'message':getMsg('fc.imported.crews',[fileName,new_crew_num])]
		} else if (new_crew_error_num) {
			ret = ['error':true,'message':getMsg('fc.notimported.crews.error',[fileName,new_crew_error_num])]
		} else {
			ret = ['error':true,'message':getMsg('fc.notimported.crews',[fileName])]
		}
		printdone ret
		return ret
	}

    //--------------------------------------------------------------------------
	Map importCrews2(List crewList, boolean noUnsuitableStartNum, boolean emailPrimary, Contest contestInstance) {
		Map ret = [new_crew_num:0, new_crew_error_num:0, exist_crews:[]]
		crewList.each { Map crew_entry ->
			if (crew_entry.name && (!emailPrimary || crew_entry.email)) {
                
                if (crew_entry.startNum) {
                    if (crew_entry.startNum.contains('.')) {
                        crew_entry.startNum = crew_entry.startNum.substring(0,crew_entry.startNum.indexOf('.'))
                    }
                    try {
                        crew_entry.startNum = crew_entry.startNum.toInteger()
                    } catch (Exception e) {
                        crew_entry.startNum = 1
                    }
                    if (noUnsuitableStartNum) {
                        if (contestInstance.IsUnsuitableStartNum(crew_entry.startNum)) {
                            crew_entry.startNum++
                        }
                    }
                } else {
                    crew_entry.startNum = 1
                    for(Crew crew_instance in Crew.findAllByContest(contestInstance)) {
                        if (crew_instance.startNum >= crew_entry.startNum) {
                            crew_entry.startNum = crew_instance.startNum + 1
                            if (noUnsuitableStartNum) {
                                if (contestInstance.IsUnsuitableStartNum(crew_entry.startNum)) {
                                    crew_entry.startNum++
                                }
                            }
                        }
                    }
                }
                
                // doppelte Startnummer auflösen
                while (Crew.findByStartNumAndContest(crew_entry.startNum,contestInstance)) {
                    crew_entry.startNum++
                    if (noUnsuitableStartNum) {
                        if (contestInstance.IsUnsuitableStartNum(crew_entry.startNum)) {
                            crew_entry.startNum++
                        }
                    }
                }
                
                Crew crew = null
                if (emailPrimary) {
                    printstart "${crew_entry.email} (${crew_entry.name})"
                    crew = Crew.findByEmailAndContest(crew_entry.email, contestInstance)
                } else {
                    printstart crew_entry.name
                    crew = Crew.findByNameAndContest(crew_entry.name, contestInstance)
                }
                if (crew) {
                    printdone "Crew already exists."
                    ret.exist_crews += crew_entry
                } else {
                    Map ret2 = saveCrew(crew_entry,contestInstance)
                    printdone "Created $ret2"
                    if (ret2.saved) {
                        ret.new_crew_num++
                    } else {
                        ret.new_crew_error_num++
                    }
                }
			}
		}
		return ret
	}
	
    //--------------------------------------------------------------------------
    Map sortStartNumCrews(Contest contestInstance, Map params, session, boolean noUnsuitableStartNum, boolean withGaps)
    {
        printstart "sortStartNumCrews"
        
        // search for last crew num
        int last_crew_num = 0
        for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"viewpos"])) {
            if (crew_instance.startNum > last_crew_num) {
                last_crew_num = crew_instance.startNum
            }
        }
        println "Last crew num: $last_crew_num"
        
        // add last_crew num
        for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"viewpos"])) {
            crew_instance.startNum += last_crew_num
            crew_instance.save()
        }
        
        // set new crew num
        int crew_num = 0
        BigDecimal last_tas = null
        for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"viewpos"])) {
            crew_num++
            if (noUnsuitableStartNum) {
                if (contestInstance.IsUnsuitableStartNum(crew_num)) {
                    crew_num++
                }
            }
            if (withGaps) {
                if (last_tas && last_tas != crew_instance.tas) {
                    crew_num++
                }
            }
            crew_instance.startNum = crew_num
            crew_instance.save()
            last_tas = crew_instance.tas
        }

        Map ret = ['message':getMsg('fc.crew.sorted')]
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    Map exportCrews(Contest contestInstance, Map params, String uploadFileName)
    {
        String crew_command = params.crewcommand.toString()
        printstart "exportCrews $crew_command"
        
        Map ret = ['message':'','error':false]
        try {
            String layout = ""
            switch (crew_command) {
                case CrewCommands.EXPORTCREWS.toString():
                    layout = "Standard-Layout,Team-Layout 1,Team-Layout 2"
                    break
                case CrewCommands.EXPORTPILOTS.toString():
                    layout = "Team-Layout 1"
                    break
                case CrewCommands.EXPORTNAVIGATORS.toString():
                    layout = "Team-Layout 1"
                    break
            }
            
            List export_values = []
            Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
                if (!crew_instance.disabled) {
                    if (params["selectedCrewID${crew_instance.id}"] == "on") {
                        String result_class = ""
                        if (crew_instance.resultclass) {
                            result_class = crew_instance.resultclass.name
                        }
                        String team = ""
                        if (crew_instance.team) {
                            team = crew_instance.team.name
                        }
                        String crew = crew_instance.name
                        if (contestInstance.crewPilotNavigatorDelimiter) {
                            switch (crew_command) {
                                case CrewCommands.EXPORTPILOTS.toString():
                                    if (crew.contains(contestInstance.crewPilotNavigatorDelimiter)) {
                                        crew = Tools.Split(crew, contestInstance.crewPilotNavigatorDelimiter)[0].trim()
                                    }
                                    team = getMsg('fc.crew.exportpilots.text')
                                    break
                                case CrewCommands.EXPORTNAVIGATORS.toString():
                                    if (crew.contains(contestInstance.crewPilotNavigatorDelimiter)) {
                                        crew = Tools.Split(crew, contestInstance.crewPilotNavigatorDelimiter)[1].trim()
                                    } else {
                                        crew = ""
                                    }
                                    team = getMsg('fc.crew.exportnavigators.text')
                                    break
                            }
                        }
                        if (crew) {
                            Map new_value = [startnum:    crew_instance.startNum,
                                             crew:        crew,
                                             aircraft:    crew_instance.aircraft.registration,
                                             team:        team,
                                             resultclass: result_class,
                                             tas:         FcMath.SpeedStr_TAS(crew_instance.tas),
                                             testtime:    '',
                                             takeofftime: '',
                                            ]
                            export_values += new_value
                        }
                    }
                }
            }
            if (export_values.size() > 0) {
                File upload_file = new File(uploadFileName)
                BufferedWriter upload_writer =  upload_file.newWriter("UTF-8")
                upload_writer.writeLine "LAYOUT:${layout}"
                upload_writer.writeLine "CONTEST:${contestInstance.title}"
                for (Map export_value in export_values) {
                    upload_writer.writeLine ""
                    upload_writer.writeLine "STARTNUM:${export_value.startnum}"
                    upload_writer.writeLine "AIRCRAFT:${export_value.aircraft}"
                    upload_writer.writeLine "CREW:${export_value.crew}"
                    upload_writer.writeLine "CLASS:${export_value.resultclass}"
                    upload_writer.writeLine "TEAM:${export_value.team}"
                    upload_writer.writeLine "TAS:${export_value.tas}"
                }
                upload_writer.close()
                ret.message = getMsg('fc.crew.exported',["${export_values.size()}"])
            } else {
                ret.message = getMsg('fc.crew.export.someonemustselected')
                ret.error = true
                printerror ret.message
                return ret
            }
            printdone ""
        } catch (Exception e) {
            ret.message = e.getMessage()
            ret.error = true
            printerror e.getMessage()
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map getresultsprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getresultsprintableTest ($test_instance.crew.name)"
		
		boolean save_test = false
		if (test_instance.planningTestModified) {
			test_instance.planningTestVersion++
			test_instance.planningTestModified = false
			println "getresultsprintableTest: planningTestVersion $test_instance.planningTestVersion for saving."
			save_test = true
		}
		
		if (test_instance.flightTestModified) {
			test_instance.flightTestVersion++
			test_instance.flightTestModified = false
			println "getresultsprintableTest: flightTestVersion $test_instance.flightTestVersion for saving."
			save_test = true
		}
		
		if (test_instance.observationTestModified) {
			test_instance.observationTestVersion++
			test_instance.observationTestModified = false
			println "getresultsprintableTest: observationTestVersion $test_instance.observationTestVersion for saving."
			save_test = true
		}
		
		if (test_instance.landingTestModified) {
			test_instance.landingTestVersion++
			test_instance.landingTestModified = false
			println "getresultsprintableTest: landingTestVersion $test_instance.landingTestVersion for saving."
			save_test = true
		}
		
		if (test_instance.landingTest1Modified) {
			test_instance.landingTest1Version++
			test_instance.landingTest1Modified = false
			println "getresultsprintableTest: landingTest1Version $test_instance.landingTest1Version for saving."
			save_test = true
		}
		
		if (test_instance.landingTest2Modified) {
			test_instance.landingTest2Version++
			test_instance.landingTest2Modified = false
			println "getresultsprintableTest: landingTest2Version $test_instance.landingTest2Version for saving."
			save_test = true
		}
		
		if (test_instance.landingTest3Modified) {
			test_instance.landingTest3Version++
			test_instance.landingTest3Modified = false
			println "getresultsprintableTest: landingTest3Version $test_instance.landingTest3Version for saving."
			save_test = true
		}
		
		if (test_instance.landingTest4Modified) {
			test_instance.landingTest4Version++
			test_instance.landingTest4Modified = false
			println "getresultsprintableTest: landingTest4Version $test_instance.landingTest4Version for saving."
			save_test = true
		}
		
		if (test_instance.specialTestModified) {
			test_instance.specialTestVersion++
			test_instance.specialTestModified = false
			println "getresultsprintableTest: specialTestVersion $test_instance.specialTestVersion for saving."
			save_test = true
		}
		
		if (test_instance.crewResultsModified) {
			test_instance.crewResultsVersion++
			test_instance.crewResultsModified = false
			println "getresultsprintableTest: crewResultsVersion $test_instance.crewResultsVersion for saving."
			save_test = true
		}
		
		if (save_test) {
			test_instance.save()
			println "getresultsprintableTest: Test '($test_instance.crew.name)' saved."
		}
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map getflightplanprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getflightplanprintableTest ($test_instance.crew.name)"
		
		// Calculate timetable version
		if (test_instance.task.timetableModified) {
			test_instance.task.timetableVersion++
			test_instance.task.timetableModified = false
			test_instance.task.save()
			println "getflightplanprintableTest: timetableVersion $test_instance.task.timetableVersion of '${test_instance.task.printName()}' saved."
			test_instance.timetableVersion = test_instance.task.timetableVersion
			test_instance.save()
			println "getflightplanprintableTest: $test_instance,crew.name saved."
		}
		
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map getplanningtaskresultsprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getplanningtaskresultsprintableTest ($test_instance.crew.name)"
		
		// Calculate planning test version
		if (test_instance.planningTestModified) {
			test_instance.planningTestVersion++
			test_instance.planningTestModified = false
			test_instance.save()
			println "getplanningtaskresultsprintableTest: planningTestVersion $test_instance.planningTestVersion of '$test_instance.crew.name' saved."
		}
		
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map getflightresultsprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getflightresultsprintableTest ($test_instance.crew.name)"
		
		// Calculate flight test version
		if (test_instance.flightTestModified) {
			test_instance.flightTestVersion++
			test_instance.flightTestModified = false
			test_instance.save()
			println "getflightresultsprintableTest: flightTestVersion $test_instance.flightTestVersion of '$test_instance.crew.name' saved."
		}
		
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map getobservationresultsprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getobservationresultsprintableTest ($test_instance.crew.name)"
		
		// Calculate observation test version
		if (test_instance.observationTestModified) {
			test_instance.observationTestVersion++
			test_instance.observationTestModified = false
			test_instance.save()
			println "getobservationresultsprintableTest: observationTestVersion $test_instance.observationTestVersion of '$test_instance.crew.name' saved."
		}
		
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map getlandingresultsprintableTest(Map params, ResultType resultType)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getlandingresultsprintableTest ($test_instance.crew.name)"
		
		// Calculate landing test version
		switch (resultType) {
			case ResultType.Landing:
				if (test_instance.landingTestModified) {
					test_instance.landingTestVersion++
					test_instance.landingTestModified = false
					test_instance.save()
					println "getlandingresultsprintableTest: landingTestVersion $test_instance.landingTestVersion of '$test_instance.crew.name' saved."
				}
				break
			case ResultType.Landing1:
				if (test_instance.landingTest1Modified) {
					test_instance.landingTest1Version++
					test_instance.landingTest1Modified = false
					test_instance.save()
					println "getlandingresultsprintableTest: landingTest1Version $test_instance.landingTest1Version of '$test_instance.crew.name' saved."
				}
				break
			case ResultType.Landing2:
				if (test_instance.landingTest2Modified) {
					test_instance.landingTest2Version++
					test_instance.landingTest2Modified = false
					test_instance.save()
					println "getlandingresultsprintableTest: landingTest2Version $test_instance.landingTest2Version of '$test_instance.crew.name' saved."
				}
				break
			case ResultType.Landing3:
				if (test_instance.landingTest3Modified) {
					test_instance.landingTest3Version++
					test_instance.landingTest3Modified = false
					test_instance.save()
					println "getlandingresultsprintableTest: landingTest3Version $test_instance.landingTest3Version of '$test_instance.crew.name' saved."
				}
				break
			case ResultType.Landing4:
				if (test_instance.landingTest4Modified) {
					test_instance.landingTest4Version++
					test_instance.landingTest4Modified = false
					test_instance.save()
					println "getlandingresultsprintableTest: landingTest4Version $test_instance.landingTest4Version of '$test_instance.crew.name' saved."
				}
				break
		}
		
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map getspecialresultsprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getspecialresultsprintableTest ($test_instance.crew.name)"
		
		// Calculate special test version
		if (test_instance.specialTestModified) {
			test_instance.specialTestVersion++
			test_instance.specialTestModified = false
			test_instance.save()
			println "getspecialresultsprintableTest: specialTestVersion $test_instance.specialTestVersion of '$test_instance.crew.name' saved."
		}
		
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    Map readyplanningtaskresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }
		
		test.instance.properties = params
		if (test.instance.isDirty()) {
			test.instance.planningTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

        test.instance.planningTestComplete = true
        test.instance.planningTestLiveTrackingResultOk = false
        test.instance.planningTestLiveTrackingResultError = false
        test.instance.taskResultsTime = new Date()
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Planningtask)}"])} ${getMsg('fc.planningresults.points',["${test.instance.planningTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
	Map saveplanningtaskresultsTest(Map params)
	{
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		test.instance.properties = params
		if (test.instance.isDirty()) {
			test.instance.planningTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Planningtask)}"])} ${getMsg('fc.planningresults.points',["${test.instance.planningTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
	}
	
    //--------------------------------------------------------------------------
    Map openplanningtaskresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.planningTestComplete = false
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.reopened',["${test.instance.GetTitle(ResultType.Planningtask)}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map readyflightresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		test.instance.properties = params
		if (test.instance.isDirty()) {
			test.instance.flightTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

        test.instance.flightTestComplete = true
        test.instance.flightTestLiveTrackingResultOk = false
        test.instance.flightTestLiveTrackingResultError = false
        test.instance.taskResultsTime = new Date()
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Flight)}"])} ${getMsg('fc.flightresults.points',["${test.instance.flightTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map saveflightresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		test.instance.properties = params
		if (test.instance.isDirty()) {
			test.instance.flightTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}
		
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Flight)}"])} ${getMsg('fc.flightresults.points',["${test.instance.flightTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openflightresultsreTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.flightTestComplete = false
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.reopened',["${test.instance.GetTitle(ResultType.Flight)}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map readyobservationresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        Map observation_data = save_observation_data(params, test.instance)
        if (!observation_data.ok) {
            return ['instance':test.instance,'error':true,'message':getMsg('fc.observation.evaluationvalue.incomplete')]
        }
        
        test.instance.properties = params
		if (observation_data.modified || test.instance.isDirty()) {
			test.instance.observationTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

        test.instance.observationTestComplete = true
        test.instance.observationTestLiveTrackingResultOk = false
        test.instance.observationTestLiveTrackingResultError = false
        test.instance.taskResultsTime = new Date()
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Observation)}"])} ${getMsg('fc.observationresults.points',["${test.instance.observationTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openobservationresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.observationTestComplete = false
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.reopened',["${test.instance.GetTitle(ResultType.Observation)}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map saveobservationresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        Map observation_data = save_observation_data(params, test.instance)
        if (!observation_data.ok) {
            return ['instance':test.instance,'error':true,'message':getMsg('fc.observation.evaluationvalue.incomplete')]
        }
        
        test.instance.properties = params
		if (observation_data.modified || test.instance.isDirty()) {
			test.instance.observationTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Observation)}"])} ${getMsg('fc.observationresults.points',["${test.instance.observationTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map save_observation_data(Map params, Test testInstance)
    {
        boolean ok = true
        boolean modified = false
        
        if (params.observationTestEnroutePhotoValueUnit) {
            testInstance.observationTestEnroutePhotoValueUnit = (EnrouteValueUnit)params.observationTestEnroutePhotoValueUnit
        }
        if (params.observationTestEnrouteCanvasValueUnit) {
            testInstance.observationTestEnrouteCanvasValueUnit = (EnrouteValueUnit)params.observationTestEnrouteCanvasValueUnit
        }

        // TurnpointData
        if (testInstance.IsObservationTestTurnpointRun()) {
            switch (testInstance.GetTurnpointRoute()) {
                case TurnpointRoute.AssignPhoto:
                case TurnpointRoute.AssignCanvas:
                    for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(testInstance,[sort:"id"])) {
                        if (params["turnpointdataevaluation_${turnpointdata_instance.id}"]) { 
                            if (params["turnpointdataevaluation_${turnpointdata_instance.id}"] == TurnpointSign.None.title) {
                                turnpointdata_instance.evaluationSign = TurnpointSign.None
                            } else {
                                turnpointdata_instance.evaluationSign = TurnpointSign.GetTurnpointSign(params["turnpointdataevaluation_${turnpointdata_instance.id}"])
                            }
                            if (turnpointdata_instance.evaluationSign == TurnpointSign.Unevaluated) {
                                turnpointdata_instance.resultValue = EvaluationValue.Unevaluated
                            } else if (turnpointdata_instance.evaluationSign == TurnpointSign.None) {
                                if (turnpointdata_instance.tpSign == TurnpointSign.NoSign) {
                                    turnpointdata_instance.resultValue = EvaluationValue.Correct
                                } else {
                                    turnpointdata_instance.resultValue = EvaluationValue.NotFound
                                }
                            } else if (turnpointdata_instance.evaluationSign == turnpointdata_instance.tpSign) {
                                turnpointdata_instance.resultValue = EvaluationValue.Correct
                            } else {
                                turnpointdata_instance.resultValue = EvaluationValue.False
                            }
                            calculatePenaltyTurnpointDataInstance(turnpointdata_instance, testInstance)
                            if (turnpointdata_instance.isDirty()) {
                                modified = true
                            }
                            turnpointdata_instance.save()
                            if (turnpointdata_instance.evaluationSign == TurnpointSign.Unevaluated) {
                                ok = false
                            }
                        } else {
                            ok = false
                        }
                    }
                    break
                case TurnpointRoute.TrueFalsePhoto:
                    for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(testInstance,[sort:"id"])) {
                        if (params["turnpointdataevaluation_${turnpointdata_instance.id}"]) { 
                            turnpointdata_instance.evaluationValue = EvaluationValue.(params["turnpointdataevaluation_${turnpointdata_instance.id}"])
                            switch (turnpointdata_instance.evaluationValue) {
                                case EvaluationValue.Unevaluated:
                                    turnpointdata_instance.resultValue = EvaluationValue.Unevaluated
                                    break
                                case EvaluationValue.NotFound:
                                    turnpointdata_instance.resultValue = EvaluationValue.NotFound
                                    break
                                case EvaluationValue.Correct:
                                    switch (turnpointdata_instance.tpSignCorrect) {
                                        case TurnpointCorrect.True:
                                            turnpointdata_instance.resultValue = EvaluationValue.Correct
                                            break
                                        case TurnpointCorrect.False:
                                            turnpointdata_instance.resultValue = EvaluationValue.False
                                            break
                                    }
                                    break
                                case EvaluationValue.False:
                                    switch (turnpointdata_instance.tpSignCorrect) {
                                        case TurnpointCorrect.True:
                                            turnpointdata_instance.resultValue = EvaluationValue.False
                                            break
                                        case TurnpointCorrect.False:
                                            turnpointdata_instance.resultValue = EvaluationValue.Correct
                                            break
                                    }
                                    break
                            }
                            calculatePenaltyTurnpointDataInstance(turnpointdata_instance, testInstance)
                            if (turnpointdata_instance.isDirty()) {
                                modified = true
                            }
                            turnpointdata_instance.save()
                        } else {
                            ok = false
                        }
                    }
                    break
            }
        }
        
        // EnroutePhotoData
        if (testInstance.IsObservationTestEnroutePhotoRun()) {
            switch (testInstance.GetEnroutePhotoMeasurement()) {
                case EnrouteMeasurement.Map:
                    for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(testInstance,[sort:"id"])) {
                        if (params["${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}"]) {
                            enroutephotodata_instance.evaluationValue = EvaluationValue.(params["${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}"])
                            enroutephotodata_instance.resultValue = enroutephotodata_instance.evaluationValue
                            calculatePenaltyEnrouteDataInstance(enroutephotodata_instance, testInstance, true)
                            if (enroutephotodata_instance.isDirty()) {
                                modified = true
                            }
                            enroutephotodata_instance.save()
                        } else {
                            ok = false
                        }
                    }
                    break
                case EnrouteMeasurement.NMFromTP:
                case EnrouteMeasurement.mmFromTP:
                    Map enroute_data = save_enroute_data(params, testInstance, true)
                    if (!enroute_data.ret) { // true - enroutePhoto photo
                        ok = false
                    }
                    if (enroute_data.modified) {
                        modified = true
                    }
                    break
            }
        }
        
        // EnrouteCanvasData
        if (testInstance.IsObservationTestEnrouteCanvasRun()) {
            switch (testInstance.GetEnrouteCanvasMeasurement()) {
                case EnrouteMeasurement.Map:
                    for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(testInstance,[sort:"id"])) {
                        if (params["${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}"]) {
                            enroutecanvasdata_instance.evaluationValue = EvaluationValue.(params["${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}"])
							if (enroutecanvasdata_instance.canvasSign == EnrouteCanvasSign.NoSign) {
								if (enroutecanvasdata_instance.evaluationValue == EvaluationValue.NotFound) {
									enroutecanvasdata_instance.resultValue = EvaluationValue.Correct
								} else {
									enroutecanvasdata_instance.resultValue = EvaluationValue.False
								}
							} else {
								enroutecanvasdata_instance.resultValue = enroutecanvasdata_instance.evaluationValue
							}
                            calculatePenaltyEnrouteDataInstance(enroutecanvasdata_instance, testInstance, false)
                            if (enroutecanvasdata_instance.isDirty()) {
                                modified = true
                            }
                            enroutecanvasdata_instance.save()
                        } else {
                            ok = false
                        }
                    }
                    break
                case EnrouteMeasurement.NMFromTP:
                case EnrouteMeasurement.mmFromTP:
                    Map enroute_data = save_enroute_data(params, testInstance, false)
                    if (!enroute_data.ret) { // false - enroutePhoto canvas
                        ok = false
                    }
                    if (enroute_data.modified) {
                        modified = true
                    }
                    break
            }
        }

        return [ok: ok, modified: modified]
    }
    
    //--------------------------------------------------------------------------
    private Map save_enroute_data(Map params, Test testInstance, boolean enroutePhoto)
    {
        boolean ret = true
        boolean modified = false
        
        List enroute_data = []
        EnrouteMeasurement enroute_measurement = EnrouteMeasurement.None
        String coordtitle_id = ""
        String evaluationvalue_id = ""
        if (enroutePhoto) {
            enroute_data = EnroutePhotoData.findAllByTest(testInstance,[sort:"id"])
            enroute_measurement = testInstance.GetEnroutePhotoMeasurement()
            coordtitle_id = Defs.EnrouteID_PhotoCoordTitle
            evaluationvalue_id = Defs.EnrouteID_PhotoEvaluationValue
        } else {
            enroute_data = EnrouteCanvasData.findAllByTest(testInstance,[sort:"id"])
            enroute_measurement = testInstance.GetEnrouteCanvasMeasurement()
            coordtitle_id = Defs.EnrouteID_CanvasCoordTitle
            evaluationvalue_id = Defs.EnrouteID_CanvasEvaluationValue
        }
        for (EnrouteData enroutedata_instance in enroute_data) {
            if (params.("${coordtitle_id}${enroutedata_instance.id}") != Defs.EnrouteValue_Unevaluated) {
                if (params["${coordtitle_id}${enroutedata_instance.id}"] == Defs.EnrouteValue_NotFound) {
                    enroutedata_instance.evaluationType = CoordType.UNKNOWN
                    enroutedata_instance.evaluationNumber = 1
                } else if (params["${coordtitle_id}${enroutedata_instance.id}"] == Defs.EnrouteValue_False) {
                    enroutedata_instance.evaluationType = CoordType.UNKNOWN
                    enroutedata_instance.evaluationNumber = 2
                } else {
                    CoordTitle evaluation_coordtitle = CoordTitle.get(params["${coordtitle_id}${enroutedata_instance.id}"].split(" : ")[1].toLong())
                    enroutedata_instance.evaluationType = evaluation_coordtitle.type
                    enroutedata_instance.evaluationNumber = evaluation_coordtitle.number
                }
				BigDecimal evaluation_distance = 0
				String evaluation_distance_str = params["${evaluationvalue_id}${enroutedata_instance.id}"]
				if (evaluation_distance_str) {
					evaluation_distance_str = evaluation_distance_str.replace(',','.')
					if (evaluation_distance_str.isNumber()) {
						evaluation_distance = evaluation_distance_str.toBigDecimal()
					}
				}
                enroutedata_instance.evaluationDistance = evaluation_distance
                Map result_values = [:]
                if (enroute_measurement == EnrouteMeasurement.NMFromTP) {
                    result_values = testInstance.GetEnrouteDistanceNM(enroutedata_instance.distanceNM)
                } else {
                    result_values = testInstance.GetEnrouteDistancemm(enroutedata_instance.distancemm)
                }
                if (enroutedata_instance.IsEvaluationFromTPUnevaluated()) {
                   enroutedata_instance.resultValue = EvaluationValue.Unevaluated
				} else if (enroutedata_instance.IsEvaluationFromTPNotFound()) {
					if (!enroutePhoto && enroutedata_instance.canvasSign == EnrouteCanvasSign.NoSign) {
						enroutedata_instance.resultValue = EvaluationValue.Correct
					} else {
						enroutedata_instance.resultValue = EvaluationValue.NotFound
					}
				} else if (enroutedata_instance.IsEvaluationFromTPFalse()) {
                    enroutedata_instance.resultValue = EvaluationValue.False
                } else if ((enroutedata_instance.evaluationType == enroutedata_instance.tpType) && (enroutedata_instance.evaluationNumber == enroutedata_instance.tpNumber)) {
                    if (!enroutePhoto && enroutedata_instance.canvasSign == EnrouteCanvasSign.NoSign) {
						enroutedata_instance.resultValue = EvaluationValue.False
					} else if (evaluation_distance == 0) {
                        enroutedata_instance.resultValue = EvaluationValue.False
                    } else if (result_values.correct_min <= evaluation_distance && evaluation_distance <= result_values.correct_max) {
                        enroutedata_instance.resultValue = EvaluationValue.Correct
                    } else if (result_values.inexact_min <= evaluation_distance && evaluation_distance <= result_values.inexact_max) {
                        enroutedata_instance.resultValue = EvaluationValue.Inexact
                    } else {
                        enroutedata_instance.resultValue = EvaluationValue.False
                    }
                } else {
                    enroutedata_instance.resultValue = EvaluationValue.False
                }
                calculatePenaltyEnrouteDataInstance(enroutedata_instance, testInstance, enroutePhoto)
                if (enroutedata_instance.isDirty()) {
                    modified = true
                }
                enroutedata_instance.save()
            } else {
                ret = false
            }
        }
        return [ret: ret, modified: modified]
    }
    
    //--------------------------------------------------------------------------
    private void calculatePenaltyTurnpointDataInstance(TurnpointData turnpointDataInstance, Test testInstance)
    {
        if (DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsTurnpointObs).contains("${turnpointDataInstance.tpTitle()},")) {
            turnpointDataInstance.penaltyCoord = 0
        } else {
            switch (turnpointDataInstance.resultValue) {
                case EvaluationValue.Correct:
                    turnpointDataInstance.penaltyCoord = 0
                    break
                case EvaluationValue.NotFound:
                    turnpointDataInstance.penaltyCoord = testInstance.GetObservationTestTurnpointNotFoundPoints()
                    break
                case EvaluationValue.False:
                    turnpointDataInstance.penaltyCoord = testInstance.GetObservationTestTurnpointFalsePoints()
                    break
                default:
                    turnpointDataInstance.penaltyCoord = 0
                    break
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculatePenaltyEnrouteDataInstance(EnrouteData enrouteDataInstance, Test testInstance, boolean enroutePhoto)
    {
        boolean is_disabled = false
        if (enroutePhoto) {
            if (testInstance.task.disabledEnroutePhotoObs.contains("${enrouteDataInstance.photoName},")) {
                is_disabled = true
            }
        } else {
            if (testInstance.task.disabledEnrouteCanvasObs.contains("${enrouteDataInstance.canvasSign.canvasName},")) {
                is_disabled = true
            }
        }
        if (is_disabled) {
            enrouteDataInstance.penaltyCoord = 0
        } else {
            switch (enrouteDataInstance.resultValue) {
                case EvaluationValue.Correct:
                    enrouteDataInstance.penaltyCoord = 0
                    break
                case EvaluationValue.Inexact:
                    enrouteDataInstance.penaltyCoord = testInstance.GetObservationTestEnrouteInexactPoints()
                    break
                case EvaluationValue.NotFound:
                    enrouteDataInstance.penaltyCoord = testInstance.GetObservationTestEnrouteNotFoundPoints()
                    break
                case EvaluationValue.False:
                    enrouteDataInstance.penaltyCoord = testInstance.GetObservationTestEnrouteFalsePoints()
                    break
                default:
                    enrouteDataInstance.penaltyCoord = 0
                    break
            }
        }
    }
    
    //--------------------------------------------------------------------------
    Map readylandingresultsTest(Map params, ResultType resultType)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		switch (resultType) {
			case ResultType.Landing1:
				test.instance.landingTest1Measure = test.instance.landingTest1Measure.toUpperCase()
				break
			case ResultType.Landing2:
				test.instance.landingTest2Measure = test.instance.landingTest2Measure.toUpperCase() 
				break
			case ResultType.Landing3:
				test.instance.landingTest3Measure = test.instance.landingTest3Measure.toUpperCase() 
				break
			case ResultType.Landing4:
				test.instance.landingTest4Measure = test.instance.landingTest4Measure.toUpperCase() 
				break
		}
		if (test.instance.isDirty()) {
			switch (resultType) {
				case ResultType.Landing:
					test.instance.landingTestModified = true
					break
				case ResultType.Landing1:
					test.instance.landingTest1Modified = true
					break
				case ResultType.Landing2:
					test.instance.landingTest2Modified = true
					break
				case ResultType.Landing3:
					test.instance.landingTest3Modified = true
					break
				case ResultType.Landing4:
					test.instance.landingTest4Modified = true
					break
			}
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

		try {
			calculateTestPenalties(test.instance,false)
		} catch (Exception e) {
			return ['instance':test.instance,'error':true,'message':e.toString()]
		}
		
		String error = get_landing_measure_error(test.instance, resultType)
		if (!error) {
			switch (resultType) {
				case ResultType.Landing:
					test.instance.landingTestComplete = true
					test.instance.landingTestLiveTrackingResultOk = false
					test.instance.landingTestLiveTrackingResultError = false
					break
				case ResultType.Landing1:
					test.instance.landingTest1Complete = true
					test.instance.landingTest1LiveTrackingResultOk = false
					test.instance.landingTest1LiveTrackingResultError = false
					break
				case ResultType.Landing2:
					test.instance.landingTest2Complete = true
					test.instance.landingTest2LiveTrackingResultOk = false
					test.instance.landingTest2LiveTrackingResultError = false
					break
				case ResultType.Landing3:
					test.instance.landingTest3Complete = true
					test.instance.landingTest3LiveTrackingResultOk = false
					test.instance.landingTest3LiveTrackingResultError = false
					break
				case ResultType.Landing4:
					test.instance.landingTest4Complete = true
					test.instance.landingTest4LiveTrackingResultOk = false
					test.instance.landingTest4LiveTrackingResultError = false
					break
			}
            test.instance.taskResultsTime = new Date()
		}
		
        if(!test.instance.hasErrors() && test.instance.save()) {
			if (error) {
				return ['instance':test.instance,'error':true,'message':error]
			} else {
				String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Landing)}"])} ${getMsg('fc.landingresults.measures',["${get_landing_measures(test.instance, resultType)}"])} ${getMsg('fc.landingresults.points',["${test.instance.landingTestPenalties}"])}"
				return ['instance':test.instance,'saved':true,'message':msg]
			}
        } else {
            return ['instance':test.instance,'error':true,'message':error]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openlandingresultsTest(Map params, ResultType resultType)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

		switch (resultType) {
			case ResultType.Landing:
				test.instance.landingTestComplete = false
				break
			case ResultType.Landing1:
				test.instance.landingTest1Complete = false
				break
			case ResultType.Landing2:
				test.instance.landingTest2Complete = false
				break
			case ResultType.Landing3:
				test.instance.landingTest3Complete = false
				break
			case ResultType.Landing4:
				test.instance.landingTest4Complete = false
				break
		}
		try {
			calculateTestPenalties(test.instance,false)
		} catch (Exception e) {
			return ['instance':test.instance,'error':true,'message':e.toString()]
		}
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.reopened',["${test.instance.GetTitle(ResultType.Landing)}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map savelandingresultsTest(Map params, ResultType resultType)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		switch (resultType) {
			case ResultType.Landing1:
				test.instance.landingTest1Measure = test.instance.landingTest1Measure.toUpperCase()
				break
			case ResultType.Landing2:
				test.instance.landingTest2Measure = test.instance.landingTest2Measure.toUpperCase() 
				break
			case ResultType.Landing3:
				test.instance.landingTest3Measure = test.instance.landingTest3Measure.toUpperCase() 
				break
			case ResultType.Landing4:
				test.instance.landingTest4Measure = test.instance.landingTest4Measure.toUpperCase() 
				break
		}
		if (test.instance.isDirty()) {
			switch (resultType) {
				case ResultType.Landing:
					test.instance.landingTestModified = true
					break
				case ResultType.Landing1:
					test.instance.landingTest1Modified = true
					break
				case ResultType.Landing2:
					test.instance.landingTest2Modified = true
					break
				case ResultType.Landing3:
					test.instance.landingTest3Modified = true
					break
				case ResultType.Landing4:
					test.instance.landingTest4Modified = true
					break
			}
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}
		
		try {
			calculateTestPenalties(test.instance,false)
		} catch (Exception e) {
			return ['instance':test.instance,'error':true,'message':e.toString()]
		}
        
		String error = get_landing_measure_error(test.instance, resultType)
		
        if(!test.instance.hasErrors() && test.instance.save()) {
			if (error) {
				return ['instance':test.instance,'error':true,'message':error]
			} else {
				String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Landing)}"])} ${getMsg('fc.landingresults.measures',["${get_landing_measures(test.instance, resultType)}"])} ${getMsg('fc.landingresults.points',["${test.instance.landingTestPenalties}"])}"
				return ['instance':test.instance,'saved':true,'message':msg]
			}
        } else {
            return ['instance':test.instance,'error':true,'message':error]
        }
    }
    
    //--------------------------------------------------------------------------
	private static String get_landing_measures(Test testInstance, ResultType resultType)
	{
		String measures = ""
		switch (resultType) {
			case ResultType.Landing:
				break
			case ResultType.Landing1:
				if (testInstance.IsLandingTest1Run()) {
					if (testInstance.landingTest1Measure) {
						measures += " $testInstance.landingTest1Measure"
					} else {
						switch (testInstance.landingTest1Landing) {
							case 1:
								measures += " ?"
								break
							case 2:
								measures += " $Contest.LANDING_NO"
								break
							case 3:
								measures += " $Contest.LANDING_OUT"
								break
						}
					}
					if (testInstance.landingTest1RollingOutside || testInstance.landingTest1PowerInBox || testInstance.landingTest1GoAroundWithoutTouching ||
						testInstance.landingTest1GoAroundInsteadStop || testInstance.landingTest1AbnormalLanding || testInstance.landingTest1NotAllowedAerodynamicAuxiliaries ||
						testInstance.landingTest1PowerInAir || testInstance.landingTest1FlapsInAir || testInstance.landingTest1TouchingObstacle) 
					{
						measures += "+"
					}
				}
				break
			case ResultType.Landing2:
				if (testInstance.IsLandingTest2Run()) {
					if (testInstance.landingTest2Measure) {
						measures += " $testInstance.landingTest2Measure"
					} else {
						switch (testInstance.landingTest2Landing) {
							case 1:
								measures += " ?"
								break
							case 2:
								measures += " $Contest.LANDING_NO"
								break
							case 3:
								measures += " $Contest.LANDING_OUT"
								break
						}
					}
					if (testInstance.landingTest2RollingOutside || testInstance.landingTest2PowerInBox || testInstance.landingTest2GoAroundWithoutTouching ||
						testInstance.landingTest2GoAroundInsteadStop || testInstance.landingTest2AbnormalLanding || testInstance.landingTest2NotAllowedAerodynamicAuxiliaries ||
						testInstance.landingTest2PowerInAir || testInstance.landingTest2FlapsInAir || testInstance.landingTest2TouchingObstacle) 
					{
						measures += "+"
					}
				}
				break
			case ResultType.Landing3:
				if (testInstance.IsLandingTest3Run()) {
					if (testInstance.landingTest3Measure) {
						measures += " $testInstance.landingTest3Measure"
					} else {
						switch (testInstance.landingTest3Landing) {
							case 1:
								measures += " ?"
								break
							case 2:
								measures += " $Contest.LANDING_NO"
								break
							case 3:
								measures += " $Contest.LANDING_OUT"
								break
						}
					}
					if (testInstance.landingTest3RollingOutside || testInstance.landingTest3PowerInBox || testInstance.landingTest3GoAroundWithoutTouching ||
						testInstance.landingTest3GoAroundInsteadStop || testInstance.landingTest3AbnormalLanding || testInstance.landingTest3NotAllowedAerodynamicAuxiliaries ||
						testInstance.landingTest3PowerInAir || testInstance.landingTest3FlapsInAir || testInstance.landingTest3TouchingObstacle)
					{
						measures += "+"
					}
				}
				break
			case ResultType.Landing4:
				if (testInstance.IsLandingTest4Run()) {
					if (testInstance.landingTest4Measure) {
						measures += " $testInstance.landingTest4Measure"
					} else {
						switch (testInstance.landingTest4Landing) {
							case 1:
								measures += " ?"
								break
							case 2:
								measures += " $Contest.LANDING_NO"
								break
							case 3:
								measures += " $Contest.LANDING_OUT"
								break
						}
					}
					if (testInstance.landingTest4RollingOutside || testInstance.landingTest4PowerInBox || testInstance.landingTest4GoAroundWithoutTouching ||
						testInstance.landingTest4GoAroundInsteadStop || testInstance.landingTest4AbnormalLanding || testInstance.landingTest4NotAllowedAerodynamicAuxiliaries ||
						testInstance.landingTest4PowerInAir || testInstance.landingTest4FlapsInAir || testInstance.landingTest4TouchingObstacle)
					{
						measures += "+"
					}
				}
				break
		}
		return measures
	}
	
    //--------------------------------------------------------------------------
	private String get_landing_measure_error(Test testInstance, ResultType resultType)
	{
		String error = ""
		switch (resultType) {
			case ResultType.Landing:
				break
			case ResultType.Landing1:
				if (testInstance.IsLandingTest1Run()) {
					if (testInstance.landingTest1Landing == 1) {
						if (!testInstance.landingTest1Measure) {
							error += " ${getMsg('fc.landingresults.measure1.empty')}"
						} else if (FcMath.CalculateLandingPenalties(testInstance.landingTest1Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest1Points)) == -1) {
							error += " " + getMsg('fc.landingresults.measure1.invalid',[testInstance.landingTest1Measure])
						}
					}
				}
				break
			case ResultType.Landing2:
				if (testInstance.IsLandingTest2Run()) {
					if (testInstance.landingTest2Landing == 1) {
						if (!testInstance.landingTest2Measure) {
							if (error) {
								error += ", "
							}
							error += getMsg('fc.landingresults.measure2.empty')
						} else if (FcMath.CalculateLandingPenalties(testInstance.landingTest2Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest2Points)) == -1) {
							if (error) {
								error += ", "
							}
							error += getMsg('fc.landingresults.measure2.invalid',[testInstance.landingTest2Measure])
						}
					}
				}
				break
			case ResultType.Landing3:
				if (testInstance.IsLandingTest3Run()) {
					if (testInstance.landingTest3Landing == 1) {
						if (!testInstance.landingTest3Measure) {
							if (error) {
								error += ", "
							}
							error += getMsg('fc.landingresults.measure3.empty')
						} else if (FcMath.CalculateLandingPenalties(testInstance.landingTest3Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest3Points)) == -1) {
							if (error) {
								error += ", "
							}
							error += getMsg('fc.landingresults.measure3.invalid',[testInstance.landingTest3Measure])
						}
					}
				}
				break
			case ResultType.Landing4:
				if (testInstance.IsLandingTest4Run()) {
					if (testInstance.landingTest4Landing == 1) {
						if (!testInstance.landingTest4Measure) {
							if (error) {
								error += ", "
							}
							error += getMsg('fc.landingresults.measure4.empty')
						} else if (FcMath.CalculateLandingPenalties(testInstance.landingTest4Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest4Points)) == -1) {
							if (error) {
								error += ", "
							}
							error += getMsg('fc.landingresults.measure4.invalid',[testInstance.landingTest4Measure])
						}
					}
				}
				break
		}
		return error
	}

    //--------------------------------------------------------------------------
    Map readyspecialresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		if (test.instance.isDirty()) {
			test.instance.specialTestModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
			test.instance.crewResultsModified = true
		}

        test.instance.specialTestComplete = true
        test.instance.specialTestLiveTrackingResultOk = false
        test.instance.specialTestLiveTrackingResultError = false
        test.instance.taskResultsTime = new Date()
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Special)}"])} ${getMsg('fc.specialresults.points',["${test.instance.specialTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openspecialresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.specialTestComplete = false
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.reopened',["${test.instance.GetTitle(ResultType.Special)}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map savespecialresultsTest(Map params)
    {
        Map test = domainService.GetTest(params)
        if (!test.instance) {
            return test
        }

        if(params.version) {
            long version = params.version.toLong()
            if(test.instance.version > version) {
                test.instance.errors.rejectValue("version", "test.optimistic.locking.failure", getMsg('fc.notupdated'))
                return ['instance':test.instance]
            }
        }

        test.instance.properties = params
		if (test.instance.isDirty()) {
			test.instance.specialTestModified = true
			test.instance.crewResultsModified = true
            test.instance.flightTestLink = ""
			delete_uploadjobtest(test.instance)
		}

        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.GetTitle(ResultType.Special)}"])} ${getMsg('fc.specialresults.points',["${test.instance.specialTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculateTestPenalties(Test testInstance, boolean recalculatePenalties)
    {
    	printstart "calculateTestPenalties: '$testInstance.crew.name', recalculate: $recalculatePenalties"
    	
		if (recalculatePenalties) {
			// recalculate TestLegPlanning
			TestLegPlanning.findAllByTest(testInstance,[sort:"id"]).each { TestLegPlanning testlegplanning_instance  ->
                if (!testlegplanning_instance.noPlanningTest) {
                    calculateLegPlanningInstance(testlegplanning_instance,true)
                    testlegplanning_instance.save()
                }
			}
			
			// recalculate CoordResult
            CoordResult last_coordresult_instance = null
			CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
				calculateCoordResultInstance(coordresult_instance,false,true)
				if (testInstance.IsFlightTestCheckTakeOff() || testInstance.GetFlightTestTakeoffCheckSeconds()) {
					testInstance.flightTestTakeoffMissed = false
				}
				if (testInstance.IsFlightTestCheckLanding()) {
					testInstance.flightTestLandingTooLate = false
				}
                int last_badcoursenum = coordresult_instance.resultBadCourseNum
                coordresult_instance.resultBadCourseNum = get_calcresult_badcoursenum(testInstance, last_coordresult_instance, coordresult_instance) 
				coordresult_instance.save()
                if (last_badcoursenum && !coordresult_instance.resultBadCourseNum) {
                    set_calcresult_nobadcourse(testInstance, last_coordresult_instance, coordresult_instance, true) // noBadCourse
                }
                last_coordresult_instance = coordresult_instance 
			}
            
            // recalculate TurnpointData
            for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(testInstance,[sort:"id"])) {
                calculatePenaltyTurnpointDataInstance(turnpointdata_instance, testInstance)
                turnpointdata_instance.save()
            }
            
            // recalculate EnroutePhotoData
            for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(testInstance,[sort:"id"])) {
                calculatePenaltyEnrouteDataInstance(enroutephotodata_instance, testInstance, true)
                enroutephotodata_instance.save()
            }

            // recalculate EnrouteCanvasData
            for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(testInstance,[sort:"id"])) {
                calculatePenaltyEnrouteDataInstance(enroutecanvasdata_instance, testInstance, false)
                enroutecanvasdata_instance.save()
            }
		}

    	// planningTestPenalties
    	testInstance.planningTestLegPenalties = 0
        testInstance.planningTestLegComplete = false
        if (TestLegPlanning.findByTest(testInstance)) {
        	testInstance.planningTestLegComplete = true
        }
    	TestLegPlanning.findAllByTest(testInstance,[sort:"id"]).each { TestLegPlanning testlegplanning_instance ->
            if (!testlegplanning_instance.noPlanningTest) {
        		if (testlegplanning_instance.resultEntered) {
        			testInstance.planningTestLegPenalties += testlegplanning_instance.penaltyTrueHeading
        			testInstance.planningTestLegPenalties += testlegplanning_instance.penaltyLegTime
        		} else {
        			testInstance.planningTestLegComplete = false
        		}
            }
    	}
		if (testInstance.GetPlanningTestMaxPoints() > 0) {
	        if (testInstance.planningTestLegPenalties > testInstance.GetPlanningTestMaxPoints()) {
	            testInstance.planningTestLegPenalties = testInstance.GetPlanningTestMaxPoints()
	        }
		}
    	testInstance.planningTestPenalties = testInstance.planningTestLegPenalties
    	if (testInstance.planningTestGivenTooLate) {
    		testInstance.planningTestPenalties += testInstance.GetPlanningTestPlanTooLatePoints()
    	}
    	if (testInstance.planningTestExitRoomTooLate) {
    		testInstance.planningTestPenalties += testInstance.GetPlanningTestExitRoomTooLatePoints()
    	}
        if (testInstance.planningTestForbiddenCalculators) {
            testInstance.planningTestPenalties += testInstance.GetPlanningTestForbiddenCalculatorsPoints()
        }
		testInstance.planningTestPenalties += testInstance.planningTestOtherPenalties
    	
    	// flightTestPenalties
    	testInstance.flightTestCheckPointPenalties = 0
    	testInstance.flightTestCheckPointsComplete = false
    	if (CoordResult.findByTest(testInstance)) {
    		testInstance.flightTestCheckPointsComplete = true
    	}
		boolean check_secretpoints = testInstance.IsFlightTestCheckSecretPoints()
		CoordResult last_coordresult_instance = null
    	CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
    		if (coordresult_instance.resultEntered) {
				if ((coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
					testInstance.flightTestCheckPointPenalties += coordresult_instance.penaltyCoord
                    if (DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsNotFound).contains("${coordresult_instance.title()},")) {
                        set_calcresult_nogatemissed(testInstance, coordresult_instance, true)
                    } else if (testInstance.GetFlightTestCpNotFoundPoints() == 0) {
                        set_calcresult_nogatemissed(testInstance, coordresult_instance, true)
                    } else {
                        switch(coordresult_instance.type) {
                            case CoordType.TO:
                                if (testInstance.IsFlightTestCheckTakeOff() || testInstance.GetFlightTestTakeoffCheckSeconds()) {
                                    set_calcresult_hide(testInstance, coordresult_instance, false)
                                } else {
                                    set_calcresult_hide(testInstance, coordresult_instance, true)
                                }
                                break
                            case CoordType.LDG:
                                if (testInstance.IsFlightTestCheckLanding()) {
                                    set_calcresult_hide(testInstance, coordresult_instance, false)
                                } else {
                                    set_calcresult_hide(testInstance, coordresult_instance, true)
                                }
                                break
                            default:
                                set_calcresult_nogatemissed(testInstance, coordresult_instance, false)
                                break
                        }
                    }
				} else {
                    set_calcresult_nogatemissed(testInstance, coordresult_instance, true)
				}
    		} else {
				switch(coordresult_instance.type) {
					case CoordType.TO:
						if (testInstance.IsFlightTestCheckTakeOff() || testInstance.GetFlightTestTakeoffCheckSeconds()) {
							testInstance.flightTestCheckPointsComplete = false
						}
						break
					case CoordType.LDG:
						if (testInstance.IsFlightTestCheckLanding()) {
							testInstance.flightTestCheckPointsComplete = false
						}
						break
					default:
						testInstance.flightTestCheckPointsComplete = false
						break
				}
    		}
    		if (coordresult_instance.planProcedureTurn) {
	    		if (coordresult_instance.resultProcedureTurnEntered) {
		    		if (coordresult_instance.resultProcedureTurnNotFlown) {
						if (last_coordresult_instance && DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsProcedureTurn).contains("${last_coordresult_instance.title()},")) {
						    // no penalties
                            set_calcresult_nobadturn(testInstance, last_coordresult_instance, true, false) // noBadTurn, no hide
                        } else if (testInstance.GetFlightTestProcedureTurnNotFlownPoints() == 0) {
                            // no penalties
                            set_calcresult_nobadturn(testInstance, last_coordresult_instance, true, false) // noBadTurn, no hide
						} else if (testInstance.task.procedureTurnDuration == 0) {
                            // no penalties
                            set_calcresult_nobadturn(testInstance, last_coordresult_instance, true, true) // noBadTurn, hide
						} else {
							testInstance.flightTestCheckPointPenalties += testInstance.GetFlightTestProcedureTurnNotFlownPoints()
                            set_calcresult_nobadturn(testInstance, last_coordresult_instance, false, false) // no noBadTurn, no hide
						}
		    		}
	    		} else {
	    			testInstance.flightTestCheckPointsComplete = false
	    		}
    		}
    		if (coordresult_instance.resultAltitude && coordresult_instance.resultMinAltitudeMissed) {
                if (!DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsMinAltitude).contains("${coordresult_instance.title()},")) {
				    testInstance.flightTestCheckPointPenalties += testInstance.GetFlightTestMinAltitudeMissedPoints()
                }
    		}
    		if (coordresult_instance.resultBadCourseNum) {
                if (DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsBadCourse).contains("${coordresult_instance.title()},")) {
                    // no penalties
                    set_calcresult_nobadcourse(testInstance, last_coordresult_instance, coordresult_instance, true) // noBadCourse
                } else if (testInstance.GetFlightTestBadCoursePoints() == 0) {
                    // no penalties
                    set_calcresult_nobadcourse(testInstance, last_coordresult_instance, coordresult_instance, true) // noBadCourse
                } else {
                    testInstance.flightTestCheckPointPenalties += coordresult_instance.GetBadCoursePenalties()
                    set_calcresult_nobadcourse(testInstance, last_coordresult_instance, coordresult_instance, false) // no noBadCourse
                }
    		}
			last_coordresult_instance = coordresult_instance
    	}
    	testInstance.flightTestPenalties = testInstance.flightTestCheckPointPenalties
        if (testInstance.flightTestTakeoffMissed) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestTakeoffMissedPoints()
        }
        if (testInstance.flightTestBadCourseStartLanding) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestBadCourseStartLandingPoints()
        }
        if (testInstance.flightTestLandingTooLate) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestLandingToLatePoints()
        }
        if (testInstance.flightTestGivenTooLate) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestGivenToLatePoints()
        }
        if (testInstance.flightTestSafetyAndRulesInfringement) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestSafetyAndRulesInfringementPoints()
        }
        if (testInstance.flightTestInstructionsNotFollowed) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestInstructionsNotFollowedPoints()
        }
        if (testInstance.flightTestFalseEnvelopeOpened) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestFalseEnvelopeOpenedPoints()
        }
        if (testInstance.flightTestSafetyEnvelopeOpened) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestSafetyEnvelopeOpenedPoints()
        }
        if (testInstance.flightTestFrequencyNotMonitored) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestFrequencyNotMonitoredPoints()
        }
        if (testInstance.flightTestForbiddenEquipment) {
            testInstance.flightTestPenalties += testInstance.GetFlightTestForbiddenEquipmentPoints()
        }
		testInstance.flightTestPenalties += testInstance.flightTestOtherPenalties
    	
		// observationTestPenalties
        testInstance.observationTestPenalties = 0
        if (testInstance.IsObservationSignUsed()) { 
            testInstance.observationTestTurnPointPhotoPenalties = 0
            testInstance.observationTestRoutePhotoPenalties = 0
            testInstance.observationTestGroundTargetPenalties = 0
        }
        if (testInstance.IsObservationTestTurnpointRun()) {
            if (testInstance.GetTurnpointRoute().IsTurnpointSign()) {
                for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(testInstance,[sort:"id"])) {
                    if (!DisabledCheckPointsTools.Uncompress(testInstance.task.disabledCheckPointsTurnpointObs).contains("${turnpointdata_instance.tpTitle()},")) {
                        testInstance.observationTestTurnPointPhotoPenalties += turnpointdata_instance.penaltyCoord
                    }
                }
            }
            testInstance.observationTestPenalties += testInstance.observationTestTurnPointPhotoPenalties
        }
        if (testInstance.IsObservationTestEnroutePhotoRun()) {
            if (testInstance.GetEnroutePhotoMeasurement().IsEnrouteMeasurement()) {
                for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(testInstance,[sort:"id"])) {
                    if (!testInstance.task.disabledEnroutePhotoObs.contains("${enroutephotodata_instance.photoName},")) {
                        testInstance.observationTestRoutePhotoPenalties += enroutephotodata_instance.penaltyCoord
                    }
                }
            }
            testInstance.observationTestPenalties += testInstance.observationTestRoutePhotoPenalties
        }
        if (testInstance.IsObservationTestEnrouteCanvasRun()) {
            if (testInstance.GetEnrouteCanvasMeasurement().IsEnrouteMeasurement()) {
                for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(testInstance,[sort:"id"])) {
                    if (!testInstance.task.disabledEnrouteCanvasObs.contains("${enroutecanvasdata_instance.canvasSign.canvasName},")) {
                        testInstance.observationTestGroundTargetPenalties += enroutecanvasdata_instance.penaltyCoord
                    }
                }
            }
            testInstance.observationTestPenalties += testInstance.observationTestGroundTargetPenalties
        }
        testInstance.observationTestPenalties += testInstance.observationTestOtherPenalties
		
        // landingTestPenalties
		if (testInstance.IsLandingTestAnyRun()) {
			testInstance.landingTestPenalties = 0
			if (testInstance.IsLandingTest1Run()) {
				testInstance.landingTest1Penalties = 0
				if (testInstance.landingTest1Measure) {
					testInstance.landingTest1Landing = GetLanding(testInstance.landingTest1Measure)
				}
				switch (testInstance.landingTest1Landing) {
					case 1:
						testInstance.landingTest1MeasurePenalties = FcMath.CalculateLandingPenalties2(testInstance.landingTest1Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest1Points))
						testInstance.landingTest1Penalties += testInstance.landingTest1MeasurePenalties
						break
					case 2:
						testInstance.landingTest1Penalties += testInstance.GetLandingTestNoLandingPoints(testInstance.task.landingTest1Points)
						break
					case 3:
						testInstance.landingTest1Penalties += testInstance.GetLandingTestOutsideLandingPoints(testInstance.task.landingTest1Points)
						break
				}
				if (testInstance.landingTest1RollingOutside) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTestRollingOutsidePoints(testInstance.task.landingTest1Points)
				}
				if (testInstance.landingTest1PowerInBox) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTestPowerInBoxPoints(testInstance.task.landingTest1Points)
				}
				if (testInstance.landingTest1GoAroundWithoutTouching) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTestGoAroundWithoutTouchingPoints(testInstance.task.landingTest1Points)
				}
				if (testInstance.landingTest1GoAroundInsteadStop) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTestGoAroundInsteadStopPoints(testInstance.task.landingTest1Points)
				}
				if (testInstance.landingTest1AbnormalLanding) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTestAbnormalLandingPoints(testInstance.task.landingTest1Points)
				}
				if (testInstance.landingTest1NotAllowedAerodynamicAuxiliaries) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(testInstance.task.landingTest1Points)
				}
                if (testInstance.landingTest1PowerInAir) {
                    testInstance.landingTest1Penalties += testInstance.GetLandingTestPowerInAirPoints(testInstance.task.landingTest1Points)
                }
                if (testInstance.landingTest1FlapsInAir) {
                    testInstance.landingTest1Penalties += testInstance.GetLandingTestFlapsInAirPoints(testInstance.task.landingTest1Points)
                }
                if (testInstance.landingTest1TouchingObstacle) {
                    testInstance.landingTest1Penalties += testInstance.GetLandingTestTouchingObstaclePoints(testInstance.task.landingTest1Points)
                }
				if (testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest1Points) > 0) {
					if (testInstance.landingTest1Penalties > testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest1Points)) {
						testInstance.landingTest1Penalties = testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest1Points)
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest1Penalties
			}
			if (testInstance.IsLandingTest2Run()) {
				testInstance.landingTest2Penalties = 0
				if (testInstance.landingTest2Measure) {
					testInstance.landingTest2Landing = GetLanding(testInstance.landingTest2Measure)
				}
				switch (testInstance.landingTest2Landing) {
					case 1:
						testInstance.landingTest2MeasurePenalties = FcMath.CalculateLandingPenalties2(testInstance.landingTest2Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest2Points))
						testInstance.landingTest2Penalties += testInstance.landingTest2MeasurePenalties
						break
					case 2:
						testInstance.landingTest2Penalties += testInstance.GetLandingTestNoLandingPoints(testInstance.task.landingTest2Points)
						break
					case 3: 
						testInstance.landingTest2Penalties += testInstance.GetLandingTestOutsideLandingPoints(testInstance.task.landingTest2Points)
						break
				}
				if (testInstance.landingTest2RollingOutside) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestRollingOutsidePoints(testInstance.task.landingTest2Points)
				}
				if (testInstance.landingTest2PowerInBox) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestPowerInBoxPoints(testInstance.task.landingTest2Points)
				}
				if (testInstance.landingTest2GoAroundWithoutTouching) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestGoAroundWithoutTouchingPoints(testInstance.task.landingTest2Points)
				}
				if (testInstance.landingTest2GoAroundInsteadStop) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestGoAroundInsteadStopPoints(testInstance.task.landingTest2Points)
				}
				if (testInstance.landingTest2AbnormalLanding) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestAbnormalLandingPoints(testInstance.task.landingTest2Points)
				}
				if (testInstance.landingTest2NotAllowedAerodynamicAuxiliaries) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(testInstance.task.landingTest2Points)
				}
				if (testInstance.landingTest2PowerInAir) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTestPowerInAirPoints(testInstance.task.landingTest2Points)
				}
                if (testInstance.landingTest2FlapsInAir) {
                    testInstance.landingTest2Penalties += testInstance.GetLandingTestFlapsInAirPoints(testInstance.task.landingTest2Points)
                }
                if (testInstance.landingTest2TouchingObstacle) {
                    testInstance.landingTest2Penalties += testInstance.GetLandingTestTouchingObstaclePoints(testInstance.task.landingTest2Points)
                }
				if (testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest2Points) > 0) {
					if (testInstance.landingTest2Penalties > testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest2Points)) {
						testInstance.landingTest2Penalties = testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest2Points)
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest2Penalties
			}
			if (testInstance.IsLandingTest3Run()) {
				testInstance.landingTest3Penalties = 0
				if (testInstance.landingTest3Measure) {
					testInstance.landingTest3Landing = GetLanding(testInstance.landingTest3Measure)
				}
				switch (testInstance.landingTest3Landing) {
					case 1:
						testInstance.landingTest3MeasurePenalties = FcMath.CalculateLandingPenalties2(testInstance.landingTest3Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest3Points))
						testInstance.landingTest3Penalties += testInstance.landingTest3MeasurePenalties
						break
					case 2:
						testInstance.landingTest3Penalties += testInstance.GetLandingTestNoLandingPoints(testInstance.task.landingTest3Points)
						break
					case 3: 
						testInstance.landingTest3Penalties += testInstance.GetLandingTestOutsideLandingPoints(testInstance.task.landingTest3Points)
						break
				}
				if (testInstance.landingTest3RollingOutside) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestRollingOutsidePoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3PowerInBox) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestPowerInBoxPoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3GoAroundWithoutTouching) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestGoAroundWithoutTouchingPoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3GoAroundInsteadStop) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestGoAroundInsteadStopPoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3AbnormalLanding) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestAbnormalLandingPoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3NotAllowedAerodynamicAuxiliaries) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3PowerInAir) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestPowerInAirPoints(testInstance.task.landingTest3Points)
				}
				if (testInstance.landingTest3FlapsInAir) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTestFlapsInAirPoints(testInstance.task.landingTest3Points)
				}
                if (testInstance.landingTest3TouchingObstacle) {
                    testInstance.landingTest3Penalties += testInstance.GetLandingTestTouchingObstaclePoints(testInstance.task.landingTest3Points)
                }
				if (testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest3Points) > 0) {
					if (testInstance.landingTest3Penalties > testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest3Points)) {
						testInstance.landingTest3Penalties = testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest3Points)
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest3Penalties
			}
			if (testInstance.IsLandingTest4Run()) {
				testInstance.landingTest4Penalties = 0
				if (testInstance.landingTest4Measure) {
					testInstance.landingTest4Landing = GetLanding(testInstance.landingTest4Measure)
				}
				switch (testInstance.landingTest4Landing) {
					case 1:
						testInstance.landingTest4MeasurePenalties = FcMath.CalculateLandingPenalties2(testInstance.landingTest4Measure,testInstance.GetLandingTestPenaltyCalculator(testInstance.task.landingTest4Points))
						testInstance.landingTest4Penalties += testInstance.landingTest4MeasurePenalties
						break
					case 2:
						testInstance.landingTest4Penalties += testInstance.GetLandingTestNoLandingPoints(testInstance.task.landingTest4Points)
						break
					case 3: 
						testInstance.landingTest4Penalties += testInstance.GetLandingTestOutsideLandingPoints(testInstance.task.landingTest4Points)
						break
				}
				if (testInstance.landingTest4RollingOutside) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestRollingOutsidePoints(testInstance.task.landingTest4Points)
				}
				if (testInstance.landingTest4PowerInBox) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestPowerInBoxPoints(testInstance.task.landingTest4Points)
				}
				if (testInstance.landingTest4GoAroundWithoutTouching) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestGoAroundWithoutTouchingPoints(testInstance.task.landingTest4Points)
				}
				if (testInstance.landingTest4GoAroundInsteadStop) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestGoAroundInsteadStopPoints(testInstance.task.landingTest4Points)
				}
				if (testInstance.landingTest4AbnormalLanding) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestAbnormalLandingPoints(testInstance.task.landingTest4Points)
				}
				if (testInstance.landingTest4NotAllowedAerodynamicAuxiliaries) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(testInstance.task.landingTest4Points)
				}
                if (testInstance.landingTest4PowerInAir) {
                    testInstance.landingTest4Penalties += testInstance.GetLandingTestPowerInAirPoints(testInstance.task.landingTest4Points)
                }
                if (testInstance.landingTest4FlapsInAir) {
                    testInstance.landingTest4Penalties += testInstance.GetLandingTestFlapsInAirPoints(testInstance.task.landingTest4Points)
                }
				if (testInstance.landingTest4TouchingObstacle) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTestTouchingObstaclePoints(testInstance.task.landingTest4Points)
				}
				if (testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest4Points) > 0) {
					if (testInstance.landingTest4Penalties > testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest4Points)) {
						testInstance.landingTest4Penalties = testInstance.GetLandingTestMaxPoints(testInstance.task.landingTest4Points)
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest4Penalties
			}
			testInstance.landingTestPenalties += testInstance.landingTestOtherPenalties
		}
											
        // taskPenalties
    	testInstance.CalculateTestPenalties()

    	printdone "Planning:$testInstance.planningTestPenalties Flight:$testInstance.flightTestPenalties Observation:$testInstance.observationTestPenalties Landing:$testInstance.landingTestPenalties Summary:$testInstance.taskPenalties"
    }

    //--------------------------------------------------------------------------
    private void set_calcresult_nogatemissed(Test testInstance, CoordResult coordresultInstance, boolean noGateMissed)
    {
        if (testInstance.loggerResult) {
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.IsCoordTitleEqual(coordresultInstance.type, coordresultInstance.titleNumber)) {
                    calcresult_instance.noGateMissed = noGateMissed
                    calcresult_instance.save()
                    break
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void set_calcresult_hide(Test testInstance, CoordResult coordresultInstance, boolean isHide)
    {
        if (testInstance.loggerResult) {
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.IsCoordTitleEqual(coordresultInstance.type, coordresultInstance.titleNumber)) {
                    calcresult_instance.hide = isHide
                    calcresult_instance.save()
                    break
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void set_calcresult_nobadturn(Test testInstance, CoordResult coordresultInstance, boolean noBadTurn, boolean isHide)
    {
        if (testInstance.loggerResult) {
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.badTurn) {
                    if (calcresult_instance.GetLocalTime(testInstance.task.contest.timeZone) > coordresultInstance.GetResultLocalTime()) {
                        calcresult_instance.noBadTurn = noBadTurn
                        calcresult_instance.hide = isHide
                        calcresult_instance.save()
                        break
                    }
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void set_calcresult_nobadcourse(Test testInstance, CoordResult lastCoordresultInstance, CoordResult coordresultInstance, boolean noBadCourse)
    {
        if (testInstance.loggerResult) {
            boolean no_bad_course = noBadCourse
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.badCourse) {
                    String calcresult_local_time = calcresult_instance.GetLocalTime(testInstance.task.contest.timeZone)
                    if (calcresult_local_time > lastCoordresultInstance.GetResultLocalTime()) {
                        if (calcresult_instance.badCourseSeconds) {
                            no_bad_course = noBadCourse
                            if (calcresult_instance.badCourseSeconds <= testInstance.GetFlightTestBadCourseCorrectSecond()) {
                                no_bad_course = true
                            }
                        }
                        calcresult_instance.noBadCourse = no_bad_course
                        calcresult_instance.save()
                    }
                    if (calcresult_local_time >= coordresultInstance.GetResultLocalTime()) {
                        break
                    }
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void set_calcresult_judgedisabled(CalcResult calcresultInstance, boolean judgeDisabled)
    {
        calcresultInstance.judgeDisabled = judgeDisabled
        calcresultInstance.save()

        if (calcresultInstance.badCourse && calcresultInstance.badCourseSeconds) {
            Test test_instance = calcresultInstance.loggerresult.test
            String start_utc = calcresultInstance.utc
            boolean found = false
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(test_instance.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.badCourse) {
                    if (found && calcresult_instance.badCourseSeconds) { // next bad course
                        break
                    }
                    if (calcresult_instance.utc > start_utc) {
                        if (!found) {
                            if (calcresult_instance.badCourse && calcresult_instance.badCourseSeconds) {
                                break // bad course with 1s 
                            }
                        }
                        calcresult_instance.judgeDisabled = judgeDisabled
                        calcresult_instance.save()
                        found = true
                    }
                } else if (found) { // end of relevant bad course
                    break
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private int get_calcresult_badcoursenum(Test testInstance, CoordResult lastCoordresultInstance, CoordResult coordresultInstance)
    {
        int badcoursenum = 0
        if (testInstance.loggerResult) {
            if (lastCoordresultInstance && coordresultInstance) {
                for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:'utc'])) {
                    if (calcresult_instance.badCourse && calcresult_instance.badCourseSeconds && !calcresult_instance.judgeDisabled) {
                        String calcresult_local_time = calcresult_instance.GetLocalTime(testInstance.task.contest.timeZone)
                        if ((calcresult_local_time > lastCoordresultInstance.GetResultLocalTime()) && (calcresult_local_time < coordresultInstance.GetResultLocalTime())) {
                            if (calcresult_instance.badCourseSeconds > testInstance.GetFlightTestBadCourseCorrectSecond()) {
                                badcoursenum++
                            }
                        }
                        if (calcresult_local_time >= coordresultInstance.GetResultLocalTime()) {
                            break
                        }
                    }
                }
            }
        }
        return badcoursenum
    }
    
    //--------------------------------------------------------------------------
	private int GetLanding(String landingMeasure)
	{
		if (landingMeasure) {
			switch (landingMeasure) {
				case "NO":
					return 2
				case "OUT":
					return 3
			}
		}
		return 1
	}
	
    //--------------------------------------------------------------------------
    Map getTestLegFlight(Map params)
    {
        TestLegFlight testlegflight_instance = TestLegFlight.get(params.id)

        if (!testlegflight_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegflight'),params.id])]
        }
        
        return ['instance':testlegflight_instance]
    }

    //--------------------------------------------------------------------------
    Map getFlightTest(Map params)
    {
        FlightTest flighttest_instance = FlightTest.get(params.id)

        if (!flighttest_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
        
        return ['instance':flighttest_instance]
    }

    //--------------------------------------------------------------------------
    Map updateFlightTest(String showLanguage, Map params)
    {
		printstart "updateFlightTest"
		
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttest_instance.version > version) {
                    flighttest_instance.errors.rejectValue("version", "flightTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttest_instance]
                }
            }

			Route old_route = flighttest_instance.route
            params = calculateFlightTestWind(showLanguage, params)
            flighttest_instance.properties = params

			if (old_route != flighttest_instance.route) {
		        Test.findAllByTask(flighttest_instance.task,[sort:"id"]).each { Test test_instance ->
		        	test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
                    test_instance.flightTestLink = ""
					delete_uploadjobtest(test_instance)
                    test_instance.crewResultsModified = true
		            test_instance.save()
		        }
				println "Calculated times have been reset."
				
                set_disabledcheckpoints_from_route(flighttest_instance.task, flighttest_instance.route)
                if (flighttest_instance.route.IsObservationSignOk()) {
                    generate_observations(flighttest_instance.task, flighttest_instance.route)
                }
			}
            
            if(!flighttest_instance.hasErrors() && flighttest_instance.save()) {
                Map ret = ['instance':flighttest_instance,'saved':true,'message':getMsg('fc.updated',["${flighttest_instance.name()}"])]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':flighttest_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    private Map calculateFlightTestWind(String showLanguage, Map params) 
    {
        if (params.TODirection) {
            params.TODirection = Languages.GetLanguageDecimal(showLanguage, params.TODirection)
        }
        if (params.TOOffset) {
            params.TOOffset = Languages.GetLanguageDecimal(showLanguage, params.TOOffset)
        }
        if (params.TOOrthogonalOffset) {
            params.TOOrthogonalOffset = Languages.GetLanguageDecimal(showLanguage, params.TOOrthogonalOffset)
        }
        if (params.LDGDirection) {
            params.LDGDirection = Languages.GetLanguageDecimal(showLanguage, params.LDGDirection)
        }
        if (params.LDGOffset) {
            params.LDGOffset = Languages.GetLanguageDecimal(showLanguage, params.LDGOffset)
        }
        if (params.LDGOrthogonalOffset) {
            params.LDGOrthogonalOffset = Languages.GetLanguageDecimal(showLanguage, params.LDGOrthogonalOffset)
        }
        if (params.iTOiLDGDirection) {
            params.iTOiLDGDirection = Languages.GetLanguageDecimal(showLanguage, params.iTOiLDGDirection)
        }
        if (params.iTOiLDGOffset) {
            params.iTOiLDGOffset = Languages.GetLanguageDecimal(showLanguage, params.iTOiLDGOffset)
        }
        if (params.iTOiLDGOrthogonalOffset) {
            params.iTOiLDGOrthogonalOffset = Languages.GetLanguageDecimal(showLanguage, params.iTOiLDGOrthogonalOffset)
        }
        return params
    }
    
    //--------------------------------------------------------------------------
    Map createFlightTest(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        FlightTest flighttest_instance = new FlightTest()
        List flighttest_routes = Route.GetOkFlightTestRoutes(contestInstance)
        if (flighttest_routes) {
            set_new_flighttestwind(null, flighttest_routes[0], flighttest_instance)
        }
        flighttest_instance.properties = params
        return ['instance':flighttest_instance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTest(String showLanguage, Map params)
    {
		printstart "saveFlightTest $showLanguage $params"
		
        FlightTest flighttest_instance = new FlightTest(params)
		flighttest_instance.direction = params.direction.toBigDecimal()
		flighttest_instance.speed = params.speed.toBigDecimal()
        
        flighttest_instance.task = Task.get( params.taskid )
        
        if (!flighttest_instance.direction) {
        	flighttest_instance.direction = 0
        }
        if (!flighttest_instance.speed) {
        	flighttest_instance.speed = 0
        }
        
        flighttest_instance.flightPlanShowLegDistance = flighttest_instance.task.contest.flightPlanShowLegDistance
        flighttest_instance.flightPlanShowTrueTrack = flighttest_instance.task.contest.flightPlanShowTrueTrack
        flighttest_instance.flightPlanShowTrueHeading = flighttest_instance.task.contest.flightPlanShowTrueHeading
        flighttest_instance.flightPlanShowGroundSpeed = flighttest_instance.task.contest.flightPlanShowGroundSpeed
        flighttest_instance.flightPlanShowLocalTime = flighttest_instance.task.contest.flightPlanShowLocalTime
        flighttest_instance.flightPlanShowElapsedTime = flighttest_instance.task.contest.flightPlanShowElapsedTime
        flighttest_instance.submissionMinutes = flighttest_instance.task.contest.flightTestSubmissionMinutes
        
        if (!flighttest_instance.route) {
            Map ret = ['instance':flighttest_instance,error:true,'message':getMsg('fc.flighttest.noroute')]
            printerror ret.message
            return ret
        } else if (!flighttest_instance.hasErrors() && flighttest_instance.save()) {

            Task task_instance = Task.get( params.taskid )
            task_instance.flighttest = flighttest_instance
            task_instance.save()

            Wind windInstance = new Wind(direction:flighttest_instance.direction,speed:flighttest_instance.speed)
            windInstance.save()
            
            params = checkFlightTestWindParams(showLanguage, params)
        
            FlightTestWind flighttestwind_instance = new FlightTestWind(params)
            flighttestwind_instance.wind = windInstance
            flighttestwind_instance.flighttest = flighttest_instance
            flighttestwind_instance.idTitle = 1
            flighttestwind_instance.save()
			
            set_disabledcheckpoints_from_route(task_instance, flighttest_instance.route)
            if (flighttest_instance.route.IsObservationSignOk()) {
                generate_observations(task_instance, flighttest_instance.route)
            }

            Map ret = ['instance':flighttest_instance,'saved':true,'message':getMsg('fc.created',["${flighttest_instance.name()}"]),
                       'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                       'taskid':task_instance.id]
			printdone ret.message
			return ret
        } else {
			printerror ""
            return ['instance':flighttest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map addobservationsFlightTest(Map params)
    {
        printstart "addobservationsFlightTest"
        
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttest_instance.version > version) {
                    flighttest_instance.errors.rejectValue("version", "flightTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttest_instance]
                }
            }

            if (flighttest_instance.route.IsObservationSignOk()) {
                generate_observations(flighttest_instance.task, flighttest_instance.route)
            }
            
            Map ret = ['instance':flighttest_instance,'message':getMsg('fc.flighttestwind.addobservation.added')]
            printdone ret.message
            return ret
            
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map removeobservationsFlightTest(Map params)
    {
        printstart "removeobservationsFlightTest"
        
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttest_instance.version > version) {
                    flighttest_instance.errors.rejectValue("version", "flightTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttest_instance]
                }
            }

            if (flighttest_instance.IsObservationSignUsed()) {
                remove_observations(flighttest_instance.task)
            }
            
            Map ret = ['instance':flighttest_instance,'message':getMsg('fc.flighttestwind.removeobservation.removed')]
            printdone ret.message
            return ret
            
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
            printerror ret.message
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    private void set_disabledcheckpoints_from_route(Task taskInstance, Route routeInstance)
    {
        String disabled_checkpoints_timecheck = ""
        String disabled_checkpoints_notfound = ""
        CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
            if (coordroute_instance.type.IsCpCheckCoord()) {
                if (coordroute_instance.noTimeCheck) {
                    if (disabled_checkpoints_timecheck) {
                        disabled_checkpoints_timecheck += ",${coordroute_instance.title()}"
                    } else {
                        disabled_checkpoints_timecheck = coordroute_instance.title() 
                    }
                }
                if (coordroute_instance.noGateCheck) {
                    if (disabled_checkpoints_notfound) {
                        disabled_checkpoints_notfound += ",${coordroute_instance.title()}"
                    } else {
                        disabled_checkpoints_notfound = coordroute_instance.title() 
                    }
                }
            }
        }
        if (disabled_checkpoints_timecheck) {
            disabled_checkpoints_timecheck += ","
            println "disabledCheckPoints has been set with route settings: $disabled_checkpoints_timecheck"
        }
        if (disabled_checkpoints_notfound) {
            disabled_checkpoints_notfound += ","
            println "disabledCheckPointsNotFound has been set with route settings: $disabled_checkpoints_notfound"
        }
        taskInstance.disabledCheckPoints = DisabledCheckPointsTools.Compress(disabled_checkpoints_timecheck, routeInstance)
        taskInstance.disabledCheckPointsNotFound = DisabledCheckPointsTools.Compress(disabled_checkpoints_notfound, routeInstance)
        taskInstance.save()
    }
    
    //--------------------------------------------------------------------------
    private void set_disabledcheckpoints_from_dialog(Task taskInstance, Route routeInstance, Map params)
    {
        String disabled_checkpoints_timecheck = ""
        String disabled_checkpoints_notfound = ""
        String disabled_checkpoints_procedureturn = ""
        String disabled_checkpoints_badcourse = ""
        String disabled_checkpoints_minaltitude = ""
        String disabled_checkpoints_turnpointobs = ""
        String disabled_enroute_photoobs = ""
        String disabled_enroute_canvasobs = ""
        CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
            if (params.("${Defs.TurnpointID_TimeCheck}${coordroute_instance.title()}") == "on") {
                if (disabled_checkpoints_timecheck) {
                    disabled_checkpoints_timecheck += ",${coordroute_instance.title()}"
                } else {
                    disabled_checkpoints_timecheck = coordroute_instance.title() 
                }
            }
            if (params.("${Defs.TurnpointID_NotFound}${coordroute_instance.title()}") == "on") {
                if (disabled_checkpoints_notfound) {
                    disabled_checkpoints_notfound += ",${coordroute_instance.title()}"
                } else {
                    disabled_checkpoints_notfound = coordroute_instance.title() 
                }
            }
            if (params.("${Defs.TurnpointID_ProcedureTurn}${coordroute_instance.title()}") == "on") {
                if (disabled_checkpoints_procedureturn) {
                    disabled_checkpoints_procedureturn += ",${coordroute_instance.title()}"
                } else {
                    disabled_checkpoints_procedureturn = coordroute_instance.title() 
                }
            }
            if (params.("${Defs.TurnpointID_BadCourse}${coordroute_instance.title()}") == "on") {
                if (disabled_checkpoints_badcourse) {
                    disabled_checkpoints_badcourse += ",${coordroute_instance.title()}"
                } else {
                    disabled_checkpoints_badcourse = coordroute_instance.title() 
                }
            }
            if (params.("${Defs.TurnpointID_MinAltitude}${coordroute_instance.title()}") == "on") {
                if (disabled_checkpoints_minaltitude) {
                    disabled_checkpoints_minaltitude += ",${coordroute_instance.title()}"
                } else {
                    disabled_checkpoints_minaltitude = coordroute_instance.title() 
                }
            }
            if (params.("${Defs.TurnpointID_TurnpointObs}${coordroute_instance.title()}") == "on") {
                if (disabled_checkpoints_turnpointobs) {
                    disabled_checkpoints_turnpointobs += ",${coordroute_instance.title()}"
                } else {
                    disabled_checkpoints_turnpointobs = coordroute_instance.title() 
                }
            }
        }
        CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"id"]).each { CoordEnroutePhoto coordenroutephoto_instance ->
            if (params.("${Defs.EnrouteID_PhotoObs}${coordenroutephoto_instance.enroutePhotoName}") == "on") {
                if (disabled_enroute_photoobs) {
                    disabled_enroute_photoobs += ",${coordenroutephoto_instance.enroutePhotoName}"
                } else {
                    disabled_enroute_photoobs = coordenroutephoto_instance.enroutePhotoName
                }
            }
        }
        CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"id"]).each { CoordEnrouteCanvas coordenroutecanvas_instance ->
            if (params.("${Defs.EnrouteID_CanvasObs}${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}") == "on") {
                if (disabled_enroute_canvasobs) {
                    disabled_enroute_canvasobs += ",${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}"
                } else {
                    disabled_enroute_canvasobs = coordenroutecanvas_instance.enrouteCanvasSign.canvasName
                }
            }
        }
        if (disabled_checkpoints_timecheck) {
            disabled_checkpoints_timecheck += ","
        }
        if (disabled_checkpoints_notfound) {
            disabled_checkpoints_notfound += ","
        }
        if (disabled_checkpoints_procedureturn) {
            disabled_checkpoints_procedureturn += ","
        }
        if (disabled_checkpoints_badcourse) {
            disabled_checkpoints_badcourse += ","
        }
        if (disabled_checkpoints_minaltitude) {
            disabled_checkpoints_minaltitude += ","
        }
        if (disabled_checkpoints_turnpointobs) {
            disabled_checkpoints_turnpointobs += ","
        }
        if (disabled_enroute_photoobs) {
            disabled_enroute_photoobs += ","
        }
        if (disabled_enroute_canvasobs) {
            disabled_enroute_canvasobs += ","
        }
        println "disabledCheckpoints: '$disabled_checkpoints_timecheck'"
        println "disabledCheckPointsNotFound: '$disabled_checkpoints_notfound'"
        println "disabledCheckPointsProcedureTurn: '$disabled_checkpoints_procedureturn'"
        println "disabledCheckPointsBadCourse: '$disabled_checkpoints_badcourse'"
        println "disabledCheckPointsMinAltitude: '$disabled_checkpoints_minaltitude'"
        println "disabledCheckPointsTurnpointObs: '$disabled_checkpoints_turnpointobs'"
        println "disabledEnroutePhotoObs: '$disabled_enroute_photoobs'"
        println "disabledEnrouteCanvasObs: '$disabled_enroute_canvasobs'"
        taskInstance.disabledCheckPoints = DisabledCheckPointsTools.Compress(disabled_checkpoints_timecheck, routeInstance)
        taskInstance.disabledCheckPointsNotFound = DisabledCheckPointsTools.Compress(disabled_checkpoints_notfound, routeInstance)
        taskInstance.disabledCheckPointsProcedureTurn = DisabledCheckPointsTools.Compress(disabled_checkpoints_procedureturn, routeInstance)
        taskInstance.disabledCheckPointsBadCourse = DisabledCheckPointsTools.Compress(disabled_checkpoints_badcourse, routeInstance)
        taskInstance.disabledCheckPointsMinAltitude = DisabledCheckPointsTools.Compress(disabled_checkpoints_minaltitude, routeInstance)
        taskInstance.disabledCheckPointsTurnpointObs = DisabledCheckPointsTools.Compress(disabled_checkpoints_turnpointobs, routeInstance)
        taskInstance.disabledEnroutePhotoObs = disabled_enroute_photoobs
        taskInstance.disabledEnrouteCanvasObs = disabled_enroute_canvasobs
    }
    
    //--------------------------------------------------------------------------
    private void generate_observations(Task taskInstance, Route routeInstance)
    {
        printstart "generate_observations: ${taskInstance.name()}"
        boolean something_generated = false
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
            //if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                 generate_observation(test_instance, routeInstance)
                 something_generated = true
                 test_instance.ResetObservationTestResults()
                 test_instance.save()
            //}
        }
        if (something_generated) {
            printdone ""
        } else {
            printdone "Nothing to generate."
        }
    }
    
    //--------------------------------------------------------------------------
    private void generate_observation(Test testInstance, Route routeInstance)
    // turnpointdata:TurnpointData,enroutephotodata:EnroutePhotoData,enroutecanvasdata:EnrouteCanvasData
    {
        remove_observation(testInstance)

        // create TurnpointData
        if (routeInstance.IsTurnpointSign()) {
            printstart "Create TurnpointData"
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:'id'])) {
                if (coordroute_instance.IsTurnpointSign()) {
                    TurnpointData turnpointdata_instance = new TurnpointData()
                    turnpointdata_instance.test = testInstance
                    turnpointdata_instance.route = routeInstance
                    turnpointdata_instance.tpType = coordroute_instance.type
                    turnpointdata_instance.tpNumber = coordroute_instance.titleNumber
                    turnpointdata_instance.tpSign = coordroute_instance.assignedSign
                    turnpointdata_instance.tpSignCorrect = coordroute_instance.correctSign
                    turnpointdata_instance.evaluationSign = TurnpointSign.Unevaluated
                    turnpointdata_instance.evaluationValue = EvaluationValue.Unevaluated
                    turnpointdata_instance.resultValue = EvaluationValue.Unevaluated
                    turnpointdata_instance.save()
                }
            }
            printdone ""
        }
        
        // create EnroutePhotoData
        if (routeInstance.IsEnrouteSign(true)) {
            printstart "Create EnroutePhotoData"
            List coordenroutephoto_instances = CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])
            if (testInstance.GetEnroutePhotoMeasurement() != EnrouteMeasurement.Map) {
                coordenroutephoto_instances.sort { p1, p2 -> 
                    FcMath.GetSortPhotoName(p1.enroutePhotoName).compareToIgnoreCase(FcMath.GetSortPhotoName(p2.enroutePhotoName)) 
                }
            }
            for (CoordEnroutePhoto coordenroutephoto_instance in coordenroutephoto_instances) {
                EnroutePhotoData enroutephotodata_instance = new EnroutePhotoData()
                enroutephotodata_instance.test = testInstance
                enroutephotodata_instance.route = routeInstance
                enroutephotodata_instance.photoName = coordenroutephoto_instance.enroutePhotoName
                enroutephotodata_instance.canvasSign = EnrouteCanvasSign.None
                if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                    enroutephotodata_instance.distanceNM = FcMath.RoundDistance(coordenroutephoto_instance.enrouteDistance)
                    enroutephotodata_instance.distancemm = FcMath.RoundMeasureDistance(coordenroutephoto_instance.GetMeasureDistance())
                    enroutephotodata_instance.tpType = coordenroutephoto_instance.type
                    enroutephotodata_instance.tpNumber = coordenroutephoto_instance.titleNumber
                }
                enroutephotodata_instance.evaluationValue = EvaluationValue.Unevaluated
                enroutephotodata_instance.resultValue = EvaluationValue.Unevaluated
                enroutephotodata_instance.save()
            }
            printdone ""
        }
        
        // create EnrouteCanvasData
        if (routeInstance.IsEnrouteSign(false)) {
            printstart "Create EnrouteCanvasData"
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])) {
                EnrouteCanvasData enroutecanvasdata_instance = new EnrouteCanvasData()
                enroutecanvasdata_instance.test = testInstance
                enroutecanvasdata_instance.route = routeInstance
                enroutecanvasdata_instance.photoName = ""
                enroutecanvasdata_instance.canvasSign = coordenroutecanvas_instance.enrouteCanvasSign
                if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                    enroutecanvasdata_instance.distanceNM = FcMath.RoundDistance(coordenroutecanvas_instance.enrouteDistance)
                    enroutecanvasdata_instance.distancemm = FcMath.RoundMeasureDistance(coordenroutecanvas_instance.GetMeasureDistance())
                    enroutecanvasdata_instance.tpType = coordenroutecanvas_instance.type
                    enroutecanvasdata_instance.tpNumber = coordenroutecanvas_instance.titleNumber
                }
                enroutecanvasdata_instance.evaluationValue = EvaluationValue.Unevaluated
                enroutecanvasdata_instance.resultValue = EvaluationValue.Unevaluated
                enroutecanvasdata_instance.save()
            }
            printdone ""
        }
    }
    
    //--------------------------------------------------------------------------
    private void remove_observations(Task taskInstance)
    {
        printstart "remove_observations: ${taskInstance.name()}"
        boolean something_removed = false
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
            //if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                 remove_observation(test_instance)
                 something_removed = true
                 test_instance.ResetObservationTestResults()
                 test_instance.save()
            //}
        }
        if (something_removed) {
            printdone ""
        } else {
            printdone "Nothing to remove."
        }
    }
    
    //--------------------------------------------------------------------------
    private void remove_observation(Test testInstance)
    {
        // remove all TurnpointData
        if (TurnpointData.countByTest(testInstance)) {
            printstart "Remove all TurnpointData ${TurnpointData.countByTest(testInstance)} instances"
            TurnpointData.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { TurnpointData turnpointdata_instance, int i ->
                turnpointdata_instance.delete(flush:true)
                println "TurnpointData instance ${i+1} deleted (flush:true)."
            }
            printdone ""
        }
        
        // remove all EnroutePhotoData
        if (EnroutePhotoData.countByTest(testInstance)) {
            printstart "Remove all EnroutePhotoData ${EnroutePhotoData.countByTest(testInstance)} instances"
            EnroutePhotoData.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { EnroutePhotoData enroutephotodata_instance, int i ->
                enroutephotodata_instance.delete(flush:true)
                println "EnroutePhotoData instance ${i+1} deleted (flush:true)."
            }
            printdone ""
        }
        
        // remove all EnrouteCanvasData
        if (EnrouteCanvasData.countByTest(testInstance)) {
            printstart "Remove all EnrouteCanvasData ${EnrouteCanvasData.countByTest(testInstance)} instances"
            EnrouteCanvasData.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { EnrouteCanvasData enroutecanvasdata_instance, int i ->
                enroutecanvasdata_instance.delete(flush:true)
                println "EnrouteCanvasData instance ${i+1} deleted (flush:true)."
            }
            printdone ""
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTest(Map params)
    {
        FlightTest flighttest_instance = FlightTest.get(params.id)
        
        if (flighttest_instance) {
			boolean flighttest_used = false
			for (FlightTestWind fighttestwind_instance in flighttest_instance.flighttestwinds) {
				if (Test.findByFlighttestwind(fighttestwind_instance)) {
					flighttest_used = true
					break
				}
			}
			if (!flighttest_used) {
	            try {
                    remove_observations(flighttest_instance.task)
                    
	                Task task_instance = Task.get( flighttest_instance.task.id )
	                task_instance.flighttest = null
	                task_instance.save()
	                
	                flighttest_instance.delete()
	                
	                return ['deleted':true,'message':getMsg('fc.deleted',["${flighttest_instance.name()}"])]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.flighttest'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.flighttest.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)

        if (!flighttestwind_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
        
        flighttestwind_instance.direction = flighttestwind_instance.wind.direction
        flighttestwind_instance.speed = flighttestwind_instance.wind.speed
        
        return ['instance':flighttestwind_instance]
    }

    //--------------------------------------------------------------------------
    Map updateFlightTestWind(String showLanguage, Map params)
    {
		printstart "updateFlightTestWind $showLanguage $params"
		
        params = checkFlightTestWindParams(showLanguage, params)
        
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)
        
        if (flighttestwind_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttestwind_instance.version > version) {
                    flighttestwind_instance.errors.rejectValue("version", "flightTestWind.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttestwind_instance]
                }
            }
            
			//BigDecimal old_direction = flighttestwind_instance.wind.direction
			//BigDecimal old_speed = flighttestwind_instance.wind.speed  
            flighttestwind_instance.properties = params
            if (params.direction != null && params.speed != null) {
                flighttestwind_instance.direction = FcMath.toBigDecimal(params.direction)
                flighttestwind_instance.speed = FcMath.toBigDecimal(params.speed)
                flighttestwind_instance.wind.direction = flighttestwind_instance.direction
                flighttestwind_instance.wind.speed = flighttestwind_instance.speed
            }
            
            /*
			boolean duration_modified = false
			if (is_modified(params.TODurationFormula, flighttestwind_instance.TODurationFormula)) {
				duration_modified = true
			}
			if (is_modified(params.LDGDurationFormula, flighttestwind_instance.LDGDurationFormula)) {
				duration_modified = true
			}
			if (is_modified(params.iLDGDurationFormula, flighttestwind_instance.iLDGDurationFormula)) {
				duration_modified = true
			}
			if (is_modified(params.iTODurationFormula, flighttestwind_instance.iTODurationFormula)) {
				duration_modified = true
			}

            if (duration_modified) {
                int calulate_num = 0
                Test.findAllByFlighttestwind(flighttestwind_instance,[sort:"viewpos"]).each { Test test_instance ->
                    if (!test_instance.crew.disabled && !test_instance.disabledCrew) {
                        calculate_testlegflight(test_instance)
                        if (test_instance.timeCalculated) {
                            GregorianCalendar testing_time = new GregorianCalendar()
                            testing_time.setTime(test_instance.testingTime)
                            Task task_instance = test_instance.task
                            calculate_test_time(test_instance, task_instance, testing_time, null, true)
                            calculate_coordresult(test_instance)
                            task_instance.timetableModified = true
                            task_instance.save()
                        }
                        test_instance.save()
                        calulate_num++
                    }
                }
                if (calulate_num) {
                    println "$calulate_num new calculated times."
                } 
            }
            
			if (old_direction != flighttestwind_instance.wind.direction || old_speed != flighttestwind_instance.wind.speed) {
                int calulate_reset_num = 0
		        Test.findAllByTask(flighttestwind_instance.flighttest.task,[sort:"id"]).each { Test test_instance ->
                    if (!test_instance.crew.disabled && !test_instance.disabledCrew && (test_instance.flighttestwind == flighttestwind_instance) && test_instance.timeCalculated) {
                        test_instance.timeCalculated = false
                        test_instance.ResetFlightTestResults()
                        test_instance.CalculateTestPenalties()
                        test_instance.flightTestLink = ""
						delete_uploadjobtest(test_instance)
                        test_instance.crewResultsModified = true
                        test_instance.save()
                        calulate_reset_num++
                    }
		        }
                if (calulate_reset_num) {
                    println "$calulate_reset_num calculated times have been reset."
                } 
			}
            */
			
            if(!flighttestwind_instance.hasErrors() && flighttestwind_instance.save()) {
                Map ret = ['instance':flighttestwind_instance,'saved':true,'message':getMsg('fc.updated',["${flighttestwind_instance.name()}"]),
						   'flighttestid':flighttestwind_instance.flighttest.id]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':flighttestwind_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createFlightTestWind(Map params)
    {
        FlightTest flighttest_instance = FlightTest.get(params.flighttestid)
        FlightTestWind flighttestwind_instance = new FlightTestWind()
        set_new_flighttestwind(flighttest_instance, flighttest_instance.route, flighttestwind_instance)
        flighttestwind_instance.properties = params
        flighttestwind_instance.flighttest = flighttest_instance
        return ['instance':flighttestwind_instance]
    }

    //--------------------------------------------------------------------------
    private void set_new_flighttestwind(FlightTest flighttestInstance, Route routeInstance, directionInstance)
    {
        if (routeInstance) {
            CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
                switch (coordroute_instance.type) {
                    case CoordType.TO:
                        directionInstance.TODirection = coordroute_instance.gateDirection
                        break
                    case CoordType.LDG:
                        directionInstance.LDGDirection = coordroute_instance.gateDirection
                        break
                    case CoordType.iTO:
                    case CoordType.iLDG:
                        directionInstance.iTOiLDGDirection = coordroute_instance.gateDirection
                        break
                }
            }
        }
        if (flighttestInstance) {
            for (FlightTestWind flighttestwind_instance in FlightTestWind.findAllByFlighttest(flighttestInstance,[sort:"id"])) {
                directionInstance.TODirection = flighttestwind_instance.TODirection.toInteger()
                directionInstance.LDGDirection = flighttestwind_instance.LDGDirection.toInteger()
                directionInstance.iTOiLDGDirection = flighttestwind_instance.iTOiLDGDirection.toInteger()
                directionInstance.TODurationFormula = flighttestwind_instance.TODurationFormula
                directionInstance.LDGDurationFormula = flighttestwind_instance.LDGDurationFormula
                directionInstance.iLDGDurationFormula = flighttestwind_instance.iLDGDurationFormula
                directionInstance.iTODurationFormula = flighttestwind_instance.iTODurationFormula
                directionInstance.direction = flighttestwind_instance.wind.direction
                directionInstance.speed = flighttestwind_instance.wind.speed
            }
        }
    }
    
    //--------------------------------------------------------------------------
    Map saveFlightTestWind(String showLanguage, Map params)
    {
        printstart "saveFlightTestWind $showLanguage $params"
        
        params = checkFlightTestWindParams(showLanguage, params)
        
        FlightTestWind flighttestwind_instance = new FlightTestWind(params)
        
        flighttestwind_instance.flighttest = FlightTest.get(params.flighttestid)
        flighttestwind_instance.idTitle = FlightTestWind.countByFlighttest(flighttestwind_instance.flighttest) + 1 
        
        Wind windInstance = new Wind(params)
        if(!windInstance.hasErrors() && windInstance.save()) {
            flighttestwind_instance.wind = windInstance
        }
        
        if(!flighttestwind_instance.hasErrors() && flighttestwind_instance.save()) {
            Map ret = ['instance':flighttestwind_instance,'saved':true,'message':getMsg('fc.created',["${flighttestwind_instance.name()}"]),
                       'fromlistplanning':params.fromlistplanning,
                       'taskid':flighttestwind_instance.flighttest.task.id,
                       'flighttestid':flighttestwind_instance.flighttest.id]
            printdone ret
            return ret
        } else {
            printerror ""
            return ['instance':flighttestwind_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map checkFlightTestWindParams(String showLanguage, Map params)
    {
        if (showLanguage) {
            if (params.direction != null) {
                params.direction = Languages.GetLanguageDecimal(showLanguage, params.direction)
            }
            if (params.speed != null) {
                params.speed = Languages.GetLanguageDecimal(showLanguage, params.speed)
            }
            if (params.TODirection != null) {
                params.TODirection = Languages.GetLanguageDecimal(showLanguage, params.TODirection)
            }
            if (params.TOOffset != null) {
                params.TOOffset = Languages.GetLanguageDecimal(showLanguage, params.TOOffset)
            }
            if (params.TOOrthogonalOffset != null) {
                params.TOOrthogonalOffset = Languages.GetLanguageDecimal(showLanguage, params.TOOrthogonalOffset)
            }
            if (params.LDGDirection != null) {
                params.LDGDirection = Languages.GetLanguageDecimal(showLanguage, params.LDGDirection)
            }
            if (params.LDGOffset != null) {
                params.LDGOffset = Languages.GetLanguageDecimal(showLanguage, params.LDGOffset)
            }
            if (params.LDGOrthogonalOffset != null) {
                params.LDGOrthogonalOffset = Languages.GetLanguageDecimal(showLanguage, params.LDGOrthogonalOffset)
            }
            if (params.iTOiLDGDirection != null) {
                params.iTOiLDGDirection = Languages.GetLanguageDecimal(showLanguage, params.iTOiLDGDirection)
            }
            if (params.iTOiLDGOffset != null) {
                params.iTOiLDGOffset = Languages.GetLanguageDecimal(showLanguage, params.iTOiLDGOffset)
            }
            if (params.iTOiLDGOrthogonalOffset != null) {
                params.iTOiLDGOrthogonalOffset = Languages.GetLanguageDecimal(showLanguage, params.iTOiLDGOrthogonalOffset)
            }
        }
        return params
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)
        
        if (flighttestwind_instance) {
			if (!Test.findByFlighttestwind(flighttestwind_instance)) {
	            try {
                    FlightTestWind.findAllByFlighttest(flighttestwind_instance.flighttest).each{ FlightTestWind flighttestwind_instance2 ->
                        if (flighttestwind_instance2.idTitle > flighttestwind_instance.idTitle) {
                            flighttestwind_instance2.idTitle--
                            flighttestwind_instance2.save()
                        }
                    }
	                flighttestwind_instance.delete()
	                return ['deleted':true,'message':getMsg('fc.deleted',["${flighttestwind_instance.name()}"]),
	                        'flighttestid':flighttestwind_instance.flighttest.id]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.flighttestwind'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.flighttestwind.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.flighttestwind'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getPlanningTest(Map params)
    {
        PlanningTest planningtest_instance = PlanningTest.get(params.id)

        if (!planningtest_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
        
        return ['instance':planningtest_instance]
    }

    //--------------------------------------------------------------------------
    Map updatePlanningTest(Map params)
    {
		printstart "updatePlanningTest"
		
        PlanningTest planningtest_instance = PlanningTest.get(params.id)
        
        if (planningtest_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(planningtest_instance.version > version) {
                    planningtest_instance.errors.rejectValue("version", "planningTest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':planningtest_instance]
                }
            }
            
            planningtest_instance.properties = params
            if(!planningtest_instance.hasErrors() && planningtest_instance.save()) {
                Map ret = ['instance':planningtest_instance,'saved':true,'message':getMsg('fc.updated',["${planningtest_instance.name()}"])]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':planningtest_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createPlanningTest(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        PlanningTest planningtest_instance = new PlanningTest()
        planningtest_instance.properties = params
        return ['instance':planningtest_instance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTest(Map params, boolean checkRoute)
    {
        PlanningTest planningtest_instance = new PlanningTest(params)
		planningtest_instance.direction = params.direction.toBigDecimal()
		planningtest_instance.speed = params.speed.toBigDecimal()

        planningtest_instance.task = Task.get( params.taskid )
        
        if (!planningtest_instance.direction) {
            planningtest_instance.direction = 0
        }
        if (!planningtest_instance.speed) {
            planningtest_instance.speed = 0
        }
        
        if (checkRoute && !params.route) {
            Map ret = ['instance':planningtest_instance, error:true, 'message':getMsg('fc.planningtest.noroute')]
            printerror ret.message
            return ret
        } else if(!planningtest_instance.hasErrors() && planningtest_instance.save()) {

            if (params.route) {
                Wind windInstance = new Wind(direction:planningtest_instance.direction,speed:planningtest_instance.speed)
                windInstance.save()
                
                PlanningTestTask planningtesttask_instance = new PlanningTestTask(params)
                planningtesttask_instance.planningtest = planningtest_instance
                planningtesttask_instance.title = params.taskTitle
                planningtesttask_instance.idTitle = 1
                planningtesttask_instance.wind = windInstance
                if (planningtesttask_instance.hasErrors() || !planningtesttask_instance.save()) {
                    planningtest_instance.delete()
                    return ['instance':planningtest_instance]
                }
            }
            
            Task task_instance = Task.get( params.taskid )
            task_instance.planningtest = planningtest_instance
            task_instance.save()
            
            return ['instance':planningtest_instance,'saved':true,'message':getMsg('fc.created',["${planningtest_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':task_instance.id]
        } else {
            return ['instance':planningtest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deletePlanningTest(Map params)
    {
        PlanningTest planningtest_instance = PlanningTest.get(params.id)
        
        if (planningtest_instance) {
			boolean planningtest_used = false
			for (PlanningTestTask planningtesttask_instance in planningtest_instance.planningtesttasks) {
				if (Test.findByPlanningtesttask(planningtesttask_instance)) {
					planningtest_used = true
					break
				}
			}
			if (!planningtest_used) {			
	            try {
	                Task task_instance = Task.get( planningtest_instance.task.id )
	                task_instance.planningtest = null
	                task_instance.save()
	                
	                planningtest_instance.delete()
	                
	                return ['deleted':true,'message':getMsg('fc.deleted',["${planningtest_instance.name()}"])]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.planningtest'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.planningtest.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getPlanningTestTask(Map params)
    {
        PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.id)

        if (!planningtesttask_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
        
        planningtesttask_instance.direction = planningtesttask_instance.wind.direction
        planningtesttask_instance.speed = planningtesttask_instance.wind.speed
        
        return ['instance':planningtesttask_instance]
    }

    //--------------------------------------------------------------------------
    Map updatePlanningTestTask(Map params)
    {
		printstart "updatePlanningTestTask"
		
    	PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.id)
        
        if (planningtesttask_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(planningtesttask_instance.version > version) {
                    planningtesttask_instance.errors.rejectValue("version", "planningTestTask.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':planningtesttask_instance]
                }
            }
            
			Route old_route = planningtesttask_instance.route
			BigDecimal old_direction = planningtesttask_instance.wind.direction 
			BigDecimal old_speed = planningtesttask_instance.wind.speed
            planningtesttask_instance.properties = params
            if (params.direction != null && params.speed != null) {
                planningtesttask_instance.direction = FcMath.toBigDecimal(params.direction)
                planningtesttask_instance.speed = FcMath.toBigDecimal(params.speed)
                if (!planningtesttask_instance.direction) {
                	planningtesttask_instance.direction = 0
                }
                if (!planningtesttask_instance.speed) {
                	planningtesttask_instance.speed = 0
                }
                planningtesttask_instance.wind.direction = planningtesttask_instance.direction
                planningtesttask_instance.wind.speed = planningtesttask_instance.speed
            }
            
			if (old_route != planningtesttask_instance.route || old_direction != planningtesttask_instance.wind.direction || old_speed != planningtesttask_instance.wind.speed) {
	            Test.findAllByTask(planningtesttask_instance.planningtest.task,[sort:"id"]).each { Test test_instance ->
	                calulateTestLegPlannings(test_instance)
					test_instance.ResetPlanningTestResults()
					test_instance.CalculateTestPenalties()
                    test_instance.flightTestLink = ""
					delete_uploadjobtest(test_instance)
                    test_instance.crewResultsModified = true
	                test_instance.save()
	            }
				println "TestLegPlannings have been recalculated." 
			}

            if(!planningtesttask_instance.hasErrors() && planningtesttask_instance.save()) {
                Map ret = ['instance':planningtesttask_instance,'saved':true,'message':getMsg('fc.updated',["${planningtesttask_instance.name()}"]),
					       'planningtestid':planningtesttask_instance.planningtest.id]
				printdone ret.message
				return ret
            } else {
				printerror ""
                return ['instance':planningtesttask_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map createPlanningTestTask(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,'planningtestid':params.planningtestid]
        }
         
        PlanningTestTask planningtesttask_instance = new PlanningTestTask()
        planningtesttask_instance.properties = params
        return ['instance':planningtesttask_instance]
    }

    
    //--------------------------------------------------------------------------
    Map savePlanningTestTask(Map params)
    {
		printstart "savePlanningTestTask"
		
        PlanningTestTask planningtesttask_instance = new PlanningTestTask(params)
		planningtesttask_instance.direction = FcMath.toBigDecimal(params.direction)
		planningtesttask_instance.speed = FcMath.toBigDecimal(params.speed)

        planningtesttask_instance.planningtest = PlanningTest.get(params.planningtestid)
        planningtesttask_instance.idTitle = PlanningTestTask.countByPlanningtest(planningtesttask_instance.planningtest) + 1
        
        if (!planningtesttask_instance.direction) {
            planningtesttask_instance.direction = 0
        }
        if (!planningtesttask_instance.speed) {
            planningtesttask_instance.speed = 0
        }
        
        Wind windInstance = new Wind(direction:planningtesttask_instance.direction,speed:planningtesttask_instance.speed)
        if(!windInstance.hasErrors() && windInstance.save()) {
			println "Wind $planningtesttask_instance.direction, $planningtesttask_instance.speed -> ${windInstance.name()} saved."
            planningtesttask_instance.wind = windInstance
        } else {
			println "Error: Could not save wind ($planningtesttask_instance.direction, $planningtesttask_instance.speed)."
        }
        
        if(!planningtesttask_instance.hasErrors() && planningtesttask_instance.save()) {
			printdone ""
            return ['instance':planningtesttask_instance,'saved':true,'message':getMsg('fc.created',["${planningtesttask_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':planningtesttask_instance.planningtest.task.id,
                    'planningtestid':planningtesttask_instance.planningtest.id]
        } else {
			printerror ""
            return ['instance':planningtesttask_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deletePlanningTestTask(Map params)
    {
        PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.id)
        
        if (planningtesttask_instance) {
			if (!Test.findByPlanningtesttask(planningtesttask_instance)) {
				try {
	                PlanningTest planningtest_instance = planningtesttask_instance.planningtest 
	                    
	                planningtesttask_instance.delete()
	
	                PlanningTestTask.findAllByPlanningtest(planningtest_instance,[sort:"id"]).eachWithIndex { PlanningTestTask planningtesttask_instance2, int index  -> 
	                    planningtesttask_instance2.idTitle = index + 1
	                }
	                
	                return ['deleted':true,'message':getMsg('fc.deleted',["${planningtesttask_instance.name()}"]),
	                        'planningtestid':planningtesttask_instance.planningtest.id]
	            }
	            catch(org.springframework.dao.DataIntegrityViolationException e) {
	                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.planningtesttask'),params.id])]
	            }
			} else {
				return ['notdeleted':true,'message':getMsg('fc.planningtesttask.notdeleted')]
			}
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.planningtesttask'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map getTestLegPlanning(Map params)
    {
        TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)

        if (!testlegplanning_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanning'),params.id])]
        }
        
        return ['instance':testlegplanning_instance]
    }

    //--------------------------------------------------------------------------
    Map getTestLegPlanningResult(Map params)
    {
        TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)

        if (!testlegplanning_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
        
        // calculate resultLegTimeInput
        GregorianCalendar time = new GregorianCalendar()
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        time.set(Calendar.SECOND, FcMath.Seconds(testlegplanning_instance.resultLegTime))
        testlegplanning_instance.resultLegTimeInput = FcMath.TimeStr(time.getTime())

		return ['instance':testlegplanning_instance]
    }

    //--------------------------------------------------------------------------
    Map updateTestLegPlanningResult(String showLanguage, Map params)
    {
		printstart "updateTestLegPlanningResult $showLanguage $params"
		
    	TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)
        if(testlegplanning_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(testlegplanning_instance.version > version) {
                    testlegplanning_instance.errors.rejectValue("version", "testLegPlanning.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':testlegplanning_instance]
                }
            }

            params.resultTrueHeading = Languages.GetLanguageDecimal(showLanguage, params.resultTrueHeading)
            
            testlegplanning_instance.properties = params
			testlegplanning_instance.resultLegTimeInput = params.resultLegTimeInput 
	
            Map ret = calculateLegPlanningInstance(testlegplanning_instance,false)
            if (ret)
            {
            	return ret
            }
            
            if (testlegplanning_instance.isDirty()) {
                testlegplanning_instance.test.planningTestModified = true
                testlegplanning_instance.test.flightTestLink = ""
				delete_uploadjobtest(testlegplanning_instance.test)
                testlegplanning_instance.test.crewResultsModified = true
            }
            
            calculateTestPenalties(testlegplanning_instance.test,false)
            
            if(!testlegplanning_instance.hasErrors() && testlegplanning_instance.save()) {
				String msg = "${testlegplanning_instance.givenName()} ${getMsg('fc.testlegplanning.points',["${testlegplanning_instance.penaltyTrueHeading}","${testlegplanning_instance.penaltyLegTime}"])}"
				printdone msg
                return ['instance':testlegplanning_instance,'saved':true,'message':msg]
            } else {
				printerror ""
            	return ['instance':testlegplanning_instance]
            }
        } else {
            Map ret = ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
			printerror ret.message
			return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetTestLegPlanningResult(Map params)
    {
        TestLegPlanning testlegplanning_instance = TestLegPlanning.get(params.id)
        if(testlegplanning_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(testlegplanning_instance.version > version) {
                    testlegplanning_instance.errors.rejectValue("version", "testLegPlanning.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':testlegplanning_instance]
                }
            }

            // reset results
		    testlegplanning_instance.ResetResults()
            
            calculateTestPenalties(testlegplanning_instance.test,false)
            
            if(!testlegplanning_instance.hasErrors() && testlegplanning_instance.save()) {
                return ['instance':testlegplanning_instance,'saved':true,'message':getMsg('fc.updated',["${testlegplanning_instance.name()}"])]
            } else {
                return ['instance':testlegplanning_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map calculateLegPlanningInstance(TestLegPlanning testLegPlanningInstance, boolean recalculatePenalties)
    {
        // calculate resultLegTime
		if (!recalculatePenalties) {
	        try {
	            String input_time = FcTime.GetInputTimeStr(testLegPlanningInstance.resultLegTimeInput)
	            switch (input_time.size()) {
	                case 1:
	                    Date date = Date.parse("s", input_time)
	                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
	                    break
	                case 2:
	                    Date date = Date.parse("ss", input_time)
	                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
	                    break
	                case 4:
	                    Date date = Date.parse("m:ss", input_time)
	                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
	                    break
	                case 5:
	                    Date date = Date.parse("mm:ss", input_time)
	                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
	                    break
	                case 7:
	                    Date date = Date.parse("H:mm:ss", input_time)
	                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
	                    break
	                case 8:
	                    Date date = Date.parse("HH:mm:ss", input_time)
	                    testLegPlanningInstance.resultLegTime = (date.seconds + 60 * date.minutes + 3600 * date.hours).toBigDecimal() / 3600
	                    break
	                default:
	                    return ['instance':testLegPlanningInstance,'error':true,'message':getMsg('fc.testlegplanningresult.legtime.error')]
	            }
	        } catch (Exception e) {
	            return ['instance':testLegPlanningInstance,'error':true,'message':getMsg('fc.testlegplanningresult.value.error')]
	        }
		}
        
        Test test_instance = testLegPlanningInstance.test
        
        // calculate penaltyTrueHeading
        int plan_trueheading = FcMath.RoundGrad(testLegPlanningInstance.planTrueHeading)
		BigDecimal diff_trueheading_real = Math.abs(plan_trueheading - testLegPlanningInstance.resultTrueHeading)
		if (diff_trueheading_real > 180) {
			diff_trueheading_real = 360 - diff_trueheading_real
		}
		int diff_trueheading =  FcMath.RoundDiffGrad(diff_trueheading_real)
        if (diff_trueheading > test_instance.GetPlanningTestDirectionCorrectGrad()) {
            testLegPlanningInstance.penaltyTrueHeading = test_instance.GetPlanningTestDirectionPointsPerGrad() * (diff_trueheading - test_instance.GetPlanningTestDirectionCorrectGrad())
        } else {
            testLegPlanningInstance.penaltyTrueHeading = 0
        }
        
        // calculate penaltyLegTime
        int plan_legtime_seconds = FcMath.Seconds(testLegPlanningInstance.planLegTime)
        int result_legtime_seconds = FcMath.Seconds(testLegPlanningInstance.resultLegTime)

        int diff_legtime =  Math.abs(plan_legtime_seconds - result_legtime_seconds)
        if (diff_legtime > test_instance.GetPlanningTestTimeCorrectSecond()) {
            testLegPlanningInstance.penaltyLegTime = test_instance.GetPlanningTestTimePointsPerSecond() * (diff_legtime - test_instance.GetPlanningTestTimeCorrectSecond())
        } else {
            testLegPlanningInstance.penaltyLegTime = 0
        }
        
        testLegPlanningInstance.resultEntered = true
        
        return [:]
    }
    
    //--------------------------------------------------------------------------
    Map getCoordResult(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)

        if (!coordresult_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordresult'),params.id])]
        }
        
        // calculate resultCpTimeInput
        coordresult_instance.resultCpTimeInput = FcMath.TimeStr(coordresult_instance.resultCpTime)

        return ['instance':coordresult_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordResult(String showLanguage, Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            params.resultAltitude = Languages.GetLanguageDecimal(showLanguage, params.resultAltitude)
            
            coordresult_instance.properties = params
			coordresult_instance.resultCpTimeInput = params.resultCpTimeInput
				
            Map ret = calculateCoordResultInstance(coordresult_instance,false,false)
            if (ret)
            {
                return ret
            }
            
            if (coordresult_instance.isDirty()) {
                coordresult_instance.test.flightTestModified = true
                coordresult_instance.test.flightTestLink = ""
				delete_uploadjobtest(coordresult_instance.test)
                coordresult_instance.test.crewResultsModified = true
            }

            calculateTestPenalties(coordresult_instance.test,false)
	
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
				String altitude_points = "0"
				if (coordresult_instance.resultAltitude && coordresult_instance.resultMinAltitudeMissed) {
                    if (!DisabledCheckPointsTools.Uncompress(coordresult_instance.test.task.disabledCheckPointsMinAltitude).contains("${coordresult_instance.title()},")) {
                        altitude_points = coordresult_instance.test.GetFlightTestMinAltitudeMissedPoints().toString()
                    }
				}
                int badcourse_penalties = 0
                if (coordresult_instance.resultBadCourseNum) {
                    if (!DisabledCheckPointsTools.Uncompress(coordresult_instance.test.task.disabledCheckPointsBadCourse).contains("${coordresult_instance.title()},")) {
                        badcourse_penalties = coordresult_instance.GetBadCoursePenalties()
                    }
                }
				String msg = "${coordresult_instance.givenName()} ${getMsg('fc.coordresult.points',[altitude_points,"${coordresult_instance.penaltyCoord}",badcourse_penalties.toString()])}"
                return ['instance':coordresult_instance,'saved':true,'message':msg]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map updateCoordResultProcedureTurn(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            coordresult_instance.properties = params
			if (coordresult_instance.isDirty()) {
				coordresult_instance.test.flightTestModified = true
                coordresult_instance.test.flightTestLink = ""
				delete_uploadjobtest(coordresult_instance.test)
				coordresult_instance.test.crewResultsModified = true
			}
            coordresult_instance.resultProcedureTurnEntered = true
			
            calculateTestPenalties(coordresult_instance.test,false)
            
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
                return ['instance':coordresult_instance,'saved':true,'message':coordresult_instance.givenProcedureTurn()]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetCoordResult(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            // reset results
            coordresult_instance.ResetResults(false) // false - without procedure turn

            calculateTestPenalties(coordresult_instance.test,false)
            
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
                return ['instance':coordresult_instance,'saved':true,'message':getMsg('fc.updated',["${coordresult_instance.name()}"])]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetCoordResultProcedureTurn(Map params)
    {
        CoordResult coordresult_instance = CoordResult.get(params.id)
        if(coordresult_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(coordresult_instance.version > version) {
                    coordresult_instance.errors.rejectValue("version", "coordResult.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':coordresult_instance]
                }
            }

            // reset results
            coordresult_instance.ResetResultsProcedureTurn()

            calculateTestPenalties(coordresult_instance.test,false)
            
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
                return ['instance':coordresult_instance,'saved':true,'message':getMsg('fc.updated',["${coordresult_instance.name()}"])]
            } else {
                return ['instance':coordresult_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    private Map calculateCoordResultInstance(CoordResult coordResultInstance, boolean calculateUTC, boolean recalculatePenalties)
    {
		println "calculateCoordResultInstance: '${coordResultInstance.title()}' (${coordResultInstance.mark}) '$coordResultInstance.resultCpTimeInput'"
		
		coordResultInstance.penaltyCoord = 0
		
    	// calculate resultCpTime
		if (!recalculatePenalties) {
	        try {
	            String input_time = FcTime.GetInputTimeStr(coordResultInstance.resultCpTimeInput)
	            switch (input_time.size()) {
	                case 1:
	                    coordResultInstance.resultCpTime = Date.parse("H", input_time)
	                    break
	                case 2:
	                	coordResultInstance.resultCpTime = Date.parse("HH", input_time)
	                    break
	                case 4:
	                	coordResultInstance.resultCpTime = Date.parse("H:mm", input_time)
	                    break
	                case 5:
	                	coordResultInstance.resultCpTime = Date.parse("HH:mm", input_time)
	                    break
	                case 7:
	                	coordResultInstance.resultCpTime = Date.parse("H:mm:ss", input_time)
	                    break
	                case 8:
	                	coordResultInstance.resultCpTime = Date.parse("HH:mm:ss", input_time)
	                    break
	                default:
						Map ret = ['instance':coordResultInstance,'error':true,'message':getMsg('fc.coordresult.cptime.error')]
						println "  Error: $ret"
	                    return ret
	            }
	        } catch (Exception e) {
	            Map ret = ['instance':coordResultInstance,'error':true,'message':getMsg('fc.testlegplanningresult.value.error')]
				println "  Error: $ret"
				return ret
	        }
		}
        Contest contest_instance = coordResultInstance.test.task.contest
        if (calculateUTC) {
            coordResultInstance.resultCpTime = FcTime.GetLocalTime(coordResultInstance.resultCpTime, contest_instance.timeZone)
        }
		
        // calculate penaltyCoord
		Test test_instance = coordResultInstance.test
		switch(coordResultInstance.type) {
			case CoordType.TO:
				coordResultInstance.penaltyCoord = 0
				if (test_instance.IsFlightTestCheckTakeOff() || test_instance.GetFlightTestTakeoffCheckSeconds()) {
					if (coordResultInstance.resultCpNotFound) {
						// T/O nicht gemessen
						coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffMissedPoints()
						coordResultInstance.resultEntered = true
					} else {
						// T/O-Punktauswertung
						int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
						int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
					
						int diff_takeofftime_seconds = resultcptime_seconds - plancptime_seconds
						if (diff_takeofftime_seconds < 0) {
                            if (test_instance.GetFlightTestTakeoffCheckSeconds()) {
                                coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffPointsPerSecond() * (-diff_takeofftime_seconds)
                                if (coordResultInstance.penaltyCoord > test_instance.GetFlightTestTakeoffMissedPoints()) {
                                    coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffMissedPoints()
                                }
                            } else {
                                coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffMissedPoints()
                            }
						} else if (diff_takeofftime_seconds > test_instance.GetFlightTestTakeoffCorrectSecond()) {
							if (test_instance.GetFlightTestTakeoffCheckSeconds()) {
								coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffPointsPerSecond() * (diff_takeofftime_seconds - test_instance.GetFlightTestTakeoffCorrectSecond())
								if (coordResultInstance.penaltyCoord > test_instance.GetFlightTestTakeoffMissedPoints()) {
									coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffMissedPoints()
								}
							} else {
								coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffMissedPoints()
							}
						} 
					}
				}
				coordResultInstance.resultEntered = true
				break
			case CoordType.LDG:
				coordResultInstance.penaltyCoord = 0
				if (test_instance.IsFlightTestCheckLanding()) {
					if (coordResultInstance.resultCpNotFound) {
						// LDG nicht gemessen
						coordResultInstance.penaltyCoord = test_instance.GetFlightTestTakeoffMissedPoints()
						coordResultInstance.resultEntered = true
					} else {
						// LDG-Punkteauswertung
						int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
						int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
					
						int diff_landingtime_seconds = resultcptime_seconds - plancptime_seconds
						if (diff_landingtime_seconds > 0) {
							coordResultInstance.penaltyCoord = test_instance.GetFlightTestLandingToLatePoints()
						}
					}
				}
				coordResultInstance.resultEntered = true
				break
			default:
			    // CP-Punkteauswertung
				calculateCoordResultInstancePenaltyCoord(coordResultInstance)
				coordResultInstance.resultEntered = true
				break
		}
		
		// calculate resultMinAltitudeMissed
		coordResultInstance.resultMinAltitudeMissed = false
		if (coordResultInstance.type.IsAltitudeCheckCoord()) {
			if (coordResultInstance.resultAltitude) {
				if (coordResultInstance.type.IsAltitudeCheckCoord()) {
					Route route_instance = coordResultInstance.test.task.flighttest.route
                    int min_altitude = coordResultInstance.GetMinAltitudeAboveGround(route_instance.altitudeAboveGround)
                    if (coordResultInstance.resultAltitude < min_altitude) {
                        coordResultInstance.resultMinAltitudeMissed = true
                    } else {
                        int max_altitude = coordResultInstance.GetMaxAltitudeAboveGround()
                        if (max_altitude > 0 && coordResultInstance.resultAltitude > max_altitude) {
                            coordResultInstance.resultMinAltitudeMissed = true
                        }
                    }
				}
			}
		}
        
		println "  Ok: '$coordResultInstance.resultCpTime'"
        return [:]
    }
    
    //--------------------------------------------------------------------------
    private void calculateCoordResultInstancePenaltyCoord(CoordResult coordResultInstance)
    {
		if (coordResultInstance.type.IsCpTimeCheckCoord()) {
	        Test test_instance = coordResultInstance.test
			
	        if (coordResultInstance.resultCpNotFound) {
				if (DisabledCheckPointsTools.Uncompress(test_instance.task.disabledCheckPointsNotFound).contains("${coordResultInstance.title()},")) { // no not found check
					coordResultInstance.penaltyCoord = 0
				} else {
					coordResultInstance.penaltyCoord = test_instance.GetFlightTestCpNotFoundPoints()
				}
	        } else {
                if (DisabledCheckPointsTools.Uncompress(test_instance.task.disabledCheckPoints).contains("${coordResultInstance.title()},")) { // no time check
    				coordResultInstance.penaltyCoord = 0
    	        } else {
    		        int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
    		        int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
    		        
    		        int diff_cptime_seconds =  Math.abs(plancptime_seconds - resultcptime_seconds)
    		        if (diff_cptime_seconds > test_instance.GetFlightTestCptimeCorrectSecond()) {
    		            coordResultInstance.penaltyCoord = test_instance.GetFlightTestCptimePointsPerSecond() * (diff_cptime_seconds - test_instance.GetFlightTestCptimeCorrectSecond())
    		        } else {
    		            coordResultInstance.penaltyCoord = 0
    		        }
    				if (test_instance.GetFlightTestCptimeMaxPoints() > 0) {
    			        if (coordResultInstance.penaltyCoord > test_instance.GetFlightTestCptimeMaxPoints()) {
    			        	coordResultInstance.penaltyCoord = test_instance.GetFlightTestCptimeMaxPoints()
    			        }
    				}
    	        }
	        }
		}
    }
    
    //--------------------------------------------------------------------------
    Map getWind(Map params)
    {
        Wind windInstance = Wind.get(params.id)

        if(!windInstance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.wind'),params.id])]
        }
        
        return ['instance':windInstance]
    }
    
    //--------------------------------------------------------------------------
    private Map calculateLegData(CoordRoute newCoordRouteInstance, CoordRoute lastCoordRouteInstance)
    {
		printstart "calculateLegData"
		BigDecimal new_latitude = newCoordRouteInstance.latMath()
		BigDecimal new_longitude = newCoordRouteInstance.lonMath()
		BigDecimal last_latitude = lastCoordRouteInstance.latMath()
		BigDecimal last_longitude = lastCoordRouteInstance.lonMath()
		println "${last_latitude} -> ${new_latitude} (${new_latitude - last_latitude})"
		println "${last_longitude} -> ${new_longitude} (${new_longitude - last_longitude})"
		Map ret = AviationMath.calculateLeg(new_latitude,new_longitude,last_latitude,last_longitude)
		printdone "$ret"
		return ret
    }
    
    //--------------------------------------------------------------------------
    private Map newLeg(Route route, CoordRoute newCoordRouteInstance, 
		               CoordRoute lastCoordRouteInstance, CoordRoute lastCoordRouteTestInstance, 
					   BigDecimal lastMapMeasureDistance, BigDecimal lastMapDistance, int lastLegDuration, 
					   BigDecimal lastCoordDistance, BigDecimal firstCoordTrueTrack, 
					   BigDecimal turnTrueTrack, BigDecimal testTurnTrueTrack,
					   BigDecimal measureTrueTrack) 
    {
    	printstart "newLeg: ${route.name()} ${newCoordRouteInstance.title()}"
		
        // create routelegcoord_instance
    	if (lastCoordRouteInstance) {
    		Map legdata_coord = calculateLegData(newCoordRouteInstance, lastCoordRouteInstance)
			CoordTitle start_title = new CoordTitle(lastCoordRouteInstance.type,lastCoordRouteInstance.titleNumber)
			CoordTitle end_title = new CoordTitle(newCoordRouteInstance.type,newCoordRouteInstance.titleNumber)
			println "Coord: coordTrueTrack ${legdata_coord.dir}"
			println "Coord: coordDistance ${legdata_coord.dis}"
			println "Coord: ${start_title.name()}...${end_title.name()}"
			println "Coord: measureTrueTrack $newCoordRouteInstance.measureTrueTrack"
			println "Coord: turnTrueTrack $turnTrueTrack"
	        RouteLegCoord routelegcoord_instance = new RouteLegCoord()
            routelegcoord_instance.coordTrueTrack = legdata_coord.dir
            routelegcoord_instance.coordDistance = legdata_coord.dis
            routelegcoord_instance.route = route
			routelegcoord_instance.startTitle = start_title
			routelegcoord_instance.startTitle.save()
			routelegcoord_instance.endTitle = end_title
			routelegcoord_instance.endTitle.save()
            routelegcoord_instance.endCurved = newCoordRouteInstance.endCurved
            routelegcoord_instance.measureDistance = newCoordRouteInstance.measureDistance
            routelegcoord_instance.legMeasureDistance = newCoordRouteInstance.legMeasureDistance
            routelegcoord_instance.legDistance = newCoordRouteInstance.legDistance
            routelegcoord_instance.measureTrueTrack = newCoordRouteInstance.measureTrueTrack
            routelegcoord_instance.legDuration = newCoordRouteInstance.legDuration
            routelegcoord_instance.noPlanningTest = newCoordRouteInstance.noPlanningTest
            routelegcoord_instance.turnTrueTrack = turnTrueTrack
	        routelegcoord_instance.save()
			lastCoordDistance += FcMath.RoundDistance(legdata_coord.dis)
			if (firstCoordTrueTrack == null) {
				firstCoordTrueTrack = legdata_coord.dir
				println "New firstCoordTrueTrack $firstCoordTrueTrack"
			}
			turnTrueTrack = routelegcoord_instance.testTrueTrack()
			println "RouteLegCoord ${routelegcoord_instance.GetTitle()} saved."
        }

        // create routelegtest_instance
        if (lastCoordRouteInstance && lastCoordRouteTestInstance) {
            // OLD: Map legdata_test = calculateLegData(newCoordRouteInstance, lastCoordRouteTestInstance)
            
	        if ( (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.TP)    ||
	        	 (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.iFP)   ||
	        	 (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.FP)    ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.TP)    ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.iFP)   || 
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.FP)    ||
				 (lastCoordRouteTestInstance.type == CoordType.iSP && newCoordRouteInstance.type == CoordType.TP)   ||
				 (lastCoordRouteTestInstance.type == CoordType.iSP && newCoordRouteInstance.type == CoordType.FP)   ||
				 (lastCoordRouteTestInstance.type == CoordType.iFP && newCoordRouteInstance.type == CoordType.iLDG) ||
				 (lastCoordRouteTestInstance.type == CoordType.iFP && newCoordRouteInstance.type == CoordType.iTO)  ||
				 (lastCoordRouteTestInstance.type == CoordType.iFP && newCoordRouteInstance.type == CoordType.iSP)  ||
				 (lastCoordRouteTestInstance.type == CoordType.iLDG && newCoordRouteInstance.type == CoordType.iTO) ||
				 (lastCoordRouteTestInstance.type == CoordType.iLDG && newCoordRouteInstance.type == CoordType.iSP) ||
				 (lastCoordRouteTestInstance.type == CoordType.iTO && newCoordRouteInstance.type == CoordType.iSP)
			   )
	        {
				CoordTitle start_title = new CoordTitle(lastCoordRouteTestInstance.type,lastCoordRouteTestInstance.titleNumber)
				CoordTitle end_title = new CoordTitle(newCoordRouteInstance.type,newCoordRouteInstance.titleNumber)
				println "Test: coordTrueTrack ${firstCoordTrueTrack}"
				println "Test: coordDistance ${lastCoordDistance}"
				println "Test: ${start_title.name()}...${end_title.name()}"
				println "Test: firstCoordTrueTrack $firstCoordTrueTrack"
				println "Test: turnTrueTrack $testTurnTrueTrack"
				println "Test: measureTrueTrack $measureTrueTrack"
	        	RouteLegTest routelegtest_instance = new RouteLegTest()
                routelegtest_instance.coordTrueTrack = firstCoordTrueTrack
                routelegtest_instance.coordDistance = lastCoordDistance
                routelegtest_instance.route = route
				routelegtest_instance.startTitle = start_title
				routelegtest_instance.startTitle.save()
				routelegtest_instance.endTitle = end_title
				routelegtest_instance.endTitle.save()
                routelegtest_instance.endCurved = newCoordRouteInstance.endCurved
                routelegtest_instance.measureDistance = newCoordRouteInstance.measureDistance
                routelegtest_instance.legMeasureDistance = lastMapMeasureDistance
                routelegtest_instance.legDistance = lastMapDistance
                routelegtest_instance.measureTrueTrack = measureTrueTrack
                routelegtest_instance.legDuration = lastLegDuration
                routelegtest_instance.noPlanningTest = newCoordRouteInstance.noPlanningTest
                routelegtest_instance.turnTrueTrack = testTurnTrueTrack
	        	routelegtest_instance.save()
				testTurnTrueTrack = turnTrueTrack
				println "RouteLegTest ${routelegtest_instance.GetTitle()} saved."
	        }
        }
		Map ret = [lastCoordDistance:lastCoordDistance,firstCoordTrueTrack:firstCoordTrueTrack,turnTrueTrack:turnTrueTrack,testTurnTrueTrack:testTurnTrueTrack]
		printdone ""
		return ret
    }
    
    //--------------------------------------------------------------------------
    private void removeAllRouteLegs(Route routeInstance) 
    {
    	printstart "removeAllRouteLegs: '${routeInstance.name()}'"
    	
        RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"]).each { RouteLegCoord routelegcoord_instance ->
        	routelegcoord_instance.delete()
        }
        RouteLegTest.findAllByRoute(routeInstance,[sort:"id"]).each { RouteLegTest routelegtest_instance ->
        	routelegtest_instance.delete()
        }
		
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void calculateRouteLegs(Route routeInstance) 
    {
        printstart "calculateRouteLegs: '${routeInstance.name()}'"
        
        // remove all legs
    	removeAllRouteLegs(routeInstance)
        
        // calculate new legs
        CoordRoute last_coordroute_instance
        CoordRoute last_coordroute_test_instance
        BigDecimal last_mapmeasure_distance = null
        BigDecimal last_map_distance = null
		int last_legduration = 0
		BigDecimal last_coord_distance = 0
		BigDecimal first_coord_truetrack = null
		BigDecimal turn_true_track = null
		BigDecimal test_turn_true_track = null
		CoordType last_coordtype = CoordType.UNKNOWN
		BigDecimal last_measure_truetrack
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(routeInstance,[sort:"id"])) {
            if (!coordroute_instance.circleCenter) {
                last_mapmeasure_distance = addMapMeasureDistance(last_mapmeasure_distance,coordroute_instance.legMeasureDistance)
                last_map_distance = addMapDistance(last_map_distance,routeInstance.Convert_mm2NM(coordroute_instance.legMeasureDistance))
                if (coordroute_instance.legDuration) {
                    last_legduration += coordroute_instance.legDuration
                }
                if (last_coordtype != CoordType.SECRET) {
                    last_measure_truetrack = coordroute_instance.measureTrueTrack
                }
                Map r = newLeg(
                    routeInstance, 
                    coordroute_instance, 
                    last_coordroute_instance, 
                    last_coordroute_test_instance, 
                    last_mapmeasure_distance,
                    last_map_distance,
                    last_legduration,
                    last_coord_distance,
                    first_coord_truetrack,
                    turn_true_track,
                    test_turn_true_track,
                    last_measure_truetrack
                )
                last_coord_distance = r.lastCoordDistance
                first_coord_truetrack = r.firstCoordTrueTrack
                turn_true_track = r.turnTrueTrack
                test_turn_true_track = r.testTurnTrueTrack
                last_coordroute_instance = coordroute_instance
                switch (coordroute_instance.type) {
                    case CoordType.SP:
                    case CoordType.TP:
                    case CoordType.FP:
                    case CoordType.iFP:
                    case CoordType.iLDG:
                    case CoordType.iTO:
                    case CoordType.iSP:
                        last_coordroute_test_instance = coordroute_instance
                        last_mapmeasure_distance = null
                        last_map_distance = null
                        last_legduration = 0
                        last_coord_distance = 0
                        first_coord_truetrack = null
                        last_measure_truetrack = null
                        break
                }
                last_coordtype = coordroute_instance.type
            }
        }
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void renumberCoordRoute(Route routeInstance) 
    {
        int tp_num = 1
        int sc_num = 1
        CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
            int old_title_number = coordroute_instance.titleNumber
            if (coordroute_instance.type == CoordType.SECRET) {
                coordroute_instance.titleNumber = sc_num
                sc_num++
            } else if (coordroute_instance.type == CoordType.TP) {
                coordroute_instance.titleNumber = tp_num
                tp_num++
            } else {
                coordroute_instance.titleNumber = 1
            }
            if (coordroute_instance.titleNumber != old_title_number) {
                coordroute_instance.save()
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private void calculateSecretLegRatio(Route routeInstance)
    {
        printstart "calculateSecretLegRatio: '${routeInstance.name()}'"
        
        CoordRoute start_coordroute_instance
		CoordRoute last_coordroute_instance
        BigDecimal last_legdir = null
        
        CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
        	switch (coordroute_instance.type) {
        		case CoordType.SP:
        		case CoordType.TP:
        		case CoordType.iSP:
        			start_coordroute_instance = coordroute_instance
        			break
        		case CoordType.SECRET:
	        		
        			// search end_coordroute_instance, end_distance and secret_distance
	        		CoordRoute end_coordroute_instance
					CoordRoute last_coordroute_instance2
					BigDecimal end_distance = 0
					BigDecimal secret_distance = 0
	        		boolean leg_found = false
					boolean secret_found = false
	        		CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance2 ->
	                    if (leg_found) {
	                        switch (coordroute_instance2.type) {
	                        	case CoordType.TP:
                                case CoordType.FP:
                                case CoordType.iFP:
                                	end_coordroute_instance = coordroute_instance2
                                	leg_found = false
                                	break
	                        }
							if (coordroute_instance2.measureDistance) {
								end_distance += coordroute_instance2.legDistance
							} else {
								end_distance += calculateLegData(coordroute_instance2, last_coordroute_instance2).dis
							}
							if (secret_found) {
		                        switch (coordroute_instance2.type) {
									case CoordType.SECRET:
										if (coordroute_instance2.measureDistance) {
											secret_distance += coordroute_instance2.legDistance
										} else {
											secret_distance += calculateLegData(coordroute_instance2, last_coordroute_instance2).dis
										}
										if (coordroute_instance2 == coordroute_instance) {
											secret_found = false
										}
										break;
									default:
										secret_found = false
	                                	break
		                        }
							}
	                    }
	        			if (coordroute_instance2 == start_coordroute_instance) {
	        				leg_found = true
							secret_found = true
	        			}
						last_coordroute_instance2 = coordroute_instance2
	        		}

					// calculate secretLegRatio
	        		if (end_coordroute_instance) {
						if (secret_distance && end_distance) { // calculate from measure
							BigDecimal new_secret_leg_ratio = FcMath.RoundDistance(secret_distance) / FcMath.RoundDistance(end_distance)
							if (new_secret_leg_ratio != coordroute_instance.secretLegRatio) { 
								BigDecimal old_secret_leg_ratio = coordroute_instance.secretLegRatio 
								coordroute_instance.secretLegRatio = new_secret_leg_ratio
								coordroute_instance.save()
								println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} (modified from $old_secret_leg_ratio)"
							} else {
								println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()}"
							}
						}
	        		}
	        		break
        	}

        	// Set planProcedureTurn (CoordRoute)
			if (last_coordroute_instance) { // && routeInstance.useProcedureTurns
				BigDecimal legdir
				if (coordroute_instance.measureTrueTrack) {
					legdir = coordroute_instance.measureTrueTrack
				} else {
					Map leg_data = calculateLegData(coordroute_instance, last_coordroute_instance)
					legdir = leg_data.dir
				}
				if (last_legdir != null) {
                    if (last_coordroute_instance.type.IsProcedureTurnCoord()) {
    					BigDecimal rounded_legdir = FcMath.RoundGrad(legdir)
    					BigDecimal rounded_last_legdir = FcMath.RoundGrad(last_legdir)
    					if (IsCourseChangeGreaterEqual90(rounded_legdir,rounded_last_legdir)) {
    						coordroute_instance.planProcedureTurn = true
    						coordroute_instance.save()
    					}
                    }
				}
				last_legdir = legdir
			}
			last_coordroute_instance = coordroute_instance 
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
	private boolean IsCourseChangeGreaterEqual90(BigDecimal direction1, BigDecimal direction2)
	{
		BigDecimal course_change = AviationMath.courseChange(direction1, direction2)
		return course_change.abs() >= 90
	}
	
    //--------------------------------------------------------------------------
    private BigDecimal addMapMeasureDistance(BigDecimal lastMapMeasureDistance, BigDecimal addMeasureDistance)
    {
	    if (addMeasureDistance != null) {
	    	if (lastMapMeasureDistance != null) {
	    		lastMapMeasureDistance += addMeasureDistance
	    	} else {
	    		lastMapMeasureDistance = addMeasureDistance
	    	}
	    }
        return lastMapMeasureDistance
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal addMapDistance(BigDecimal lastMapDistance, BigDecimal addDistance)
    {
        if (addDistance != null) {
            if (lastMapDistance != null) {
                lastMapDistance += FcMath.RoundDistance(addDistance)
            } else {
                lastMapDistance = FcMath.RoundDistance(addDistance)
            }
        }
        return lastMapDistance
    }
    
    //--------------------------------------------------------------------------
    private void calulateTimetableWarnings(Task taskInstance)
    {
		printstart "calulateTimetableWarnings"
        Date first_date = null
        if (taskInstance.firstTime.size() > 5) {
            first_date = Date.parse("HH:mm:ss",taskInstance.firstTime)
        } else {
            first_date = Date.parse("HH:mm",taskInstance.firstTime)
        }
        Date last_arrival_time = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {

	            // calculate arrivalTimeWarning by arrivalTime
	            test_instance.arrivalTimeWarning = false
	            if (last_arrival_time > test_instance.arrivalTime) {
	                test_instance.arrivalTimeWarning = true
					println "Arrival time warning ($test_instance.crew.name)."
	            }
	            last_arrival_time = test_instance.arrivalTime
	            
	            // calculate takeoffTimeWarning by aircraft
	            test_instance.takeoffTimeWarning = false
	            boolean found_aircraft = false
	            GregorianCalendar last_takeoff_time = null
	            Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
					if (!test_instance2.disabledCrew && !test_instance2.crew.disabled) {
		                if (test_instance.taskAircraft == test_instance2.taskAircraft) {
		                	if (test_instance == test_instance2) {
		                		found_aircraft = true
		                	}
							if (!found_aircraft) {
			                	last_takeoff_time = new GregorianCalendar()
			                    last_takeoff_time.setTime(test_instance2.arrivalTime)
			                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.minNextFlightDuration)
			                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.preparationDuration)
			                }
		                }
					}
	            }
	            if (last_takeoff_time) {
	                if (test_instance.takeoffTime < last_takeoff_time.getTime()) {
	                    test_instance.takeoffTimeWarning = true
						println "Takeoff time warning by aircraft ($test_instance.crew.name): '$test_instance.takeoffTime' < '${last_takeoff_time.getTime()}'"
	                }
	            }
				
				// calculate takeoffTimeWarning by predecessor 
				boolean found_predecessor = false
				last_takeoff_time = null
				BigDecimal last_tasktas = 0
				Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
					if (!test_instance2.disabledCrew && !test_instance2.crew.disabled) {
						if (test_instance == test_instance2) {
							found_predecessor = true
						}
						if (!found_predecessor) {
							last_takeoff_time = new GregorianCalendar()
							last_takeoff_time.setTime(test_instance2.takeoffTime)
							last_tasktas =  test_instance2.taskTAS
						}
					}
				}
				if (last_takeoff_time) {
					last_takeoff_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalNormal)
					if (test_instance.takeoffTime < last_takeoff_time.getTime()) {
						test_instance.takeoffTimeWarning = true
						println "Takeoff time warning by predecessor ($test_instance.crew.name): '$test_instance.takeoffTime' < '${last_takeoff_time.getTime()}'"
					}
				}
	
	            test_instance.save()
			}
        }
		printdone ""
    }
 
    //--------------------------------------------------------------------------
    private int calulateTimetable(Task taskInstance)
    {
        printstart "calulateTimetable: ${taskInstance.name()}"
        
		int calculated_crew_num = 0
		
        Date first_date = null
        if (taskInstance.firstTime.size() > 5) {
            first_date = Date.parse("HH:mm:ss",taskInstance.firstTime)
        } else {
            first_date = Date.parse("HH:mm",taskInstance.firstTime)
        }
        GregorianCalendar first_time = new GregorianCalendar() 
        first_time.setTime(first_date)

        GregorianCalendar start_time = new GregorianCalendar()
        start_time.setTime(first_date)
        start_time.set(Calendar.HOUR_OF_DAY, first_time.get(Calendar.HOUR_OF_DAY))
        start_time.set(Calendar.MINUTE,      first_time.get(Calendar.MINUTE))
        start_time.set(Calendar.SECOND,      first_time.get(Calendar.SECOND))

        BigDecimal last_task_tas
        Date last_arrival_time = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				if (test_instance.flighttestwind) {
					printstart "$test_instance.crew.name"
					
					if (last_task_tas) {
				        if (test_instance.taskTAS > last_task_tas) { // faster aircraft
				        	start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalFasterAircraft - taskInstance.takeoffIntervalNormal)
							println "Faster aircraft: ${taskInstance.takeoffIntervalFasterAircraft - taskInstance.takeoffIntervalNormal} min added."
				        } else if (test_instance.taskTAS < last_task_tas) { // slower aircraft
							start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalSlowerAircraft - taskInstance.takeoffIntervalNormal)
							println "Slower aircraft: ${taskInstance.takeoffIntervalSlowerAircraft - taskInstance.takeoffIntervalNormal} min added."
						}
					}

					if (!test_instance.timeCalculated) {
						calculate_test_time(test_instance, taskInstance, start_time, last_arrival_time, true)
						calculate_coordresult(test_instance)
						calculated_crew_num++
					} else { // set start_time with last calculateted crew
						start_time.setTime(test_instance.testingTime)
					}
			
					// next 
					start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalNormal)
			        last_task_tas = test_instance.taskTAS
		            last_arrival_time = test_instance.arrivalTime
					
					printdone ""
				}
			}
        }

		if (calculated_crew_num) {
			taskInstance.timetableModified = true
			taskInstance.save()
			printdone ""
		} else {
			printdone "Nothing to calculate."
		}
		return calculated_crew_num
    }
 
    //--------------------------------------------------------------------------
	private void calculate_test_time(Test testInstance, Task taskInstance, GregorianCalendar startTime, Date lastArrivalTime, boolean calculateTimes)
	{
		if (calculateTimes) {
			printstart "calculate_test_time (times and warnings): ${testInstance.crew.name}"
		} else {
			printstart "calculate_test_time (warnings): ${testInstance.crew.name}"
		}
    	
		if (calculateTimes) {
			// calculate testingTime
			GregorianCalendar time = startTime.clone()
			testInstance.testingTime = time.getTime()
			
			// calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
			calculate_times(time, taskInstance, testInstance)
		}
        
        // calculate arrivalTimeWarning by arrivalTime
        testInstance.arrivalTimeWarning = false
		if (lastArrivalTime) {
	        if (lastArrivalTime > testInstance.arrivalTime) {
	            testInstance.arrivalTimeWarning = true
				println "Arrival time warning."
	        }
		}
        
        // calculate takeoffTimeWarning by aircraft
        testInstance.takeoffTimeWarning = false
        boolean found_aircraft = false
        GregorianCalendar last_takeoff_time = null
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
            if (testInstance.taskAircraft == test_instance2.taskAircraft) {
                if (testInstance == test_instance2) {
                    found_aircraft = true
                }
                if (!found_aircraft) {
                    last_takeoff_time = new GregorianCalendar()
                    last_takeoff_time.setTime(test_instance2.arrivalTime)
                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.minNextFlightDuration)
                    last_takeoff_time.add(Calendar.MINUTE, taskInstance.preparationDuration)
                }
            }
        }
        if (last_takeoff_time) {
            if (testInstance.takeoffTime < last_takeoff_time.getTime()) {
                testInstance.takeoffTimeWarning = true
				println "Takeoff time warning."
            }
        }

		if (calculateTimes) {
			testInstance.timeCalculated = true
			testInstance.timetableVersion = taskInstance.timetableVersion + 1 
			testInstance.ResetFlightTestResults()
			println "Times calculated."
		}
		testInstance.CalculateTestPenalties()
        testInstance.flightTestLink = ""
		delete_uploadjobtest(testInstance)
        testInstance.crewResultsModified = true
		testInstance.save()
		
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    private void calculate_times(GregorianCalendar time, Task taskInstance, Test testInstance) 
    {
		println "calculate_times"
		
        // calulate endTestingTime
        time.add(Calendar.MINUTE, taskInstance.planningTestDuration)
        testInstance.endTestingTime = time.getTime()
            
        // calulate takeoffTime
        time.add(Calendar.MINUTE, taskInstance.preparationDuration)
        testInstance.takeoffTime = time.getTime()
        
        // calulate startTime
		Map calculated_time = TimeCalculator(CoordType.SP, testInstance.flighttestwind.TODurationFormula, testInstance.flighttestwind.flighttest.route, testInstance.flighttestwind.wind, testInstance.taskTAS)
        time.add(Calendar.SECOND, FcMath.Seconds(calculated_time.hours))
		if (calculated_time.fullminute) {
			FcMath.SetFullMinute(time)
		}
        testInstance.startTime = time.getTime()
        
        // calculate finishTime
		Date legs_time = time.getTime()
		int start_seconds = FcMath.Seconds(legs_time)
		TestLegFlight.findAllByTest(testInstance,[sort:"id"]).each { TestLegFlight testlegflight_instance ->
			legs_time = testlegflight_instance.AddPlanLegTime(legs_time)
		}
		time.add(Calendar.SECOND, FcMath.Seconds(legs_time) - start_seconds)
        testInstance.finishTime = time.getTime()
        
        // calculate maxLandingTime
		calculated_time = TimeCalculator(CoordType.LDG, testInstance.flighttestwind.LDGDurationFormula, testInstance.flighttestwind.flighttest.route, testInstance.flighttestwind.wind, testInstance.taskTAS)
        time.add(Calendar.SECOND, FcMath.Seconds(calculated_time.hours))
		if (calculated_time.fullminute) {
			FcMath.SetFullMinute(time)
		}
        testInstance.maxLandingTime = time.getTime() 

        // calculate arrivalTime
        time.add(Calendar.MINUTE, taskInstance.parkingDuration)
        testInstance.arrivalTime = time.getTime()
    }
    
    //--------------------------------------------------------------------------
	private Map TimeCalculator(CoordType coordType, String givenFormula, Route routeInstance, Wind windInstance, BigDecimal valueTAS)
	// return seconds
	{
		BigDecimal ret_hours = -1 
		boolean ret_fullminute = false
		if (givenFormula.startsWith('time+:')) {
			if (givenFormula.endsWith('min')) {
				String f = givenFormula.substring(6,givenFormula.size()-3).replace(',','.')
				if (f.isInteger()) {
					ret_hours = f.toInteger() / 60
					ret_fullminute = true
				}
			}
		} else if (givenFormula.startsWith('time:')) {
			if (givenFormula.endsWith('min')) {
				String f = givenFormula.substring(5,givenFormula.size()-3).replace(',','.')
				if (f.isInteger()) {
					ret_hours = f.toInteger() / 60
				}
			}
		} else if (givenFormula.startsWith('wind+:')) {
			if (givenFormula.endsWith('NM')) {
				String f = givenFormula.substring(6,givenFormula.size()-2).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), true, true, routeInstance, windInstance, valueTAS)
					ret_fullminute = true
				}
			} else {
				String f = givenFormula.substring(6,givenFormula.size()).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), true, false, routeInstance, windInstance, valueTAS)
					ret_fullminute = true
				}
			}
		} else if (givenFormula.startsWith('wind:')) {
			if (givenFormula.endsWith('NM')) {
				String f = givenFormula.substring(5,givenFormula.size()-2).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), true, true, routeInstance, windInstance, valueTAS)
				}
			} else {
				String f = givenFormula.substring(5,givenFormula.size()).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), true, false, routeInstance, windInstance, valueTAS)
				}
			}
		} else if (givenFormula.startsWith('nowind+:')) {
			if (givenFormula.endsWith('NM')) {
				String f = givenFormula.substring(8,givenFormula.size()-2).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), false, true, routeInstance, windInstance, valueTAS)
					ret_fullminute = true
				}
			} else {
				String f = givenFormula.substring(8,givenFormula.size()).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), false, false, routeInstance, windInstance, valueTAS)
					ret_fullminute = true
				}
			}
		} else if (givenFormula.startsWith('nowind:')) {
			if (givenFormula.endsWith('NM')) {
				String f = givenFormula.substring(7,givenFormula.size()-2).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), false, true, routeInstance, windInstance, valueTAS)
				}
			} else {
				String f = givenFormula.substring(7,givenFormula.size()).replace(',','.')
				if (f.isBigDecimal()) {
					ret_hours = TimeCalculatorLeg(coordType, f.toBigDecimal(), false, false, routeInstance, windInstance, valueTAS)
				}
			}
		} else if (givenFormula.startsWith('func+:')) {
			String f = givenFormula.substring(6,givenFormula.size())
			if (f) {
				ret_fullminute = true
				ret_hours = TimeCalculatorFunc(coordType, f, routeInstance, windInstance, valueTAS)
			}
		} else if (givenFormula.startsWith('func:')) {
			String f = givenFormula.substring(5,givenFormula.size())
			if (f) {
				ret_hours = TimeCalculatorFunc(coordType, f, routeInstance, windInstance, valueTAS)
			}
		}

		// exception if not calculated 
		if (ret_hours == -1) {
			throw new Exception(getMsg('fc.task.error',["${coordType}"]))
		}
		
		// return Map
	    return [hours:ret_hours,fullminute:ret_fullminute]
	}
	
    //--------------------------------------------------------------------------
	private BigDecimal TimeCalculatorLeg(CoordType coordType, BigDecimal f, boolean withWind, boolean addDistance, Route routeInstance, Wind windInstance, BigDecimal valueTAS)
	// return hours
	{
		Wind wind = new Wind(direction:0,speed:0)
		if (withWind) {
			wind = windInstance
		}
		
		BigDecimal leg_distance = 0
		BigDecimal leg_truetrack = 0
		for (RouteLegCoord routelegcoord_instance in RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])) {
			if (routelegcoord_instance.endTitle.type == coordType) {
				leg_truetrack = routelegcoord_instance.testTrueTrack()
				leg_distance = routelegcoord_instance.testDistance()
				println "TimeCalculatorLeg $coordType: leg_truetrack:$leg_truetrack, leg_distance:$leg_distance"
				break
			}
		}

		if (addDistance) {
			println "TimeCalculatorLeg $coordType: tas:$valueTAS, wind:${wind.name()}, track:$leg_truetrack, dist:$leg_distance, add distance:$f"
			return LegTime(valueTAS,wind,leg_truetrack,leg_distance+f)
		} else {
			println "TimeCalculatorLeg $coordType: tas:$valueTAS, wind:${wind.name()}, track:$leg_truetrack, dist:$leg_distance, multiplier:$f"
			return f*LegTime(valueTAS,wind,leg_truetrack,leg_distance)
		}
	}
	
    //--------------------------------------------------------------------------
	private BigDecimal TimeCalculatorFunc(CoordType coordType, String calculatorValue, Route routeInstance, Wind windInstance, BigDecimal valueTAS) 
	// return hours
	{
		BigDecimal leg_distance = 0
		BigDecimal leg_truetrack = 0
		for (RouteLegCoord routelegcoord_instance in RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])) {
			if (routelegcoord_instance.endTitle.type == coordType) {
				leg_truetrack = routelegcoord_instance.testTrueTrack()
				leg_distance = routelegcoord_instance.testDistance()
				println "TimeCalculatorFunc $coordType: leg_truetrack:$leg_truetrack, leg_distance:$leg_distance"
				break
			}
		}

		println "TimeCalculatorFunc $coordType: tas:$valueTAS, wind:${windInstance.name()}, truetrack:$leg_truetrack, dist:$leg_distance, calculator:$calculatorValue"
		return FuncTime(valueTAS,windInstance,leg_truetrack,leg_distance,calculatorValue)
	}
	
	//--------------------------------------------------------------------------
	private BigDecimal FuncTime(BigDecimal tasValue, Wind windValue, BigDecimal trueTrackValue, BigDecimal distValue, String calculatorValue) // TODO: FuncTime
	// return hours
	{
		Map wind = [direction:windValue.direction,speed:windValue.speed]
		def legtime_func = this.&LegTime // closure
		//println "FuncTime: ${legtime_func}"
		Object time_calculator = Eval.me("{$calculatorValue}")
		BigDecimal ret_hours = Eval.me("time_calculator",time_calculator,"time_calculator($tasValue,$wind,$trueTrackValue,$distValue)")
		println "calculateTime: ${ret_hours}h"
		return ret_hours
	}
	
    //--------------------------------------------------------------------------
	private BigDecimal LegTime(BigDecimal tasValue, Wind windValue, BigDecimal trueTrackValue, BigDecimal distValue)
	// return hours
	{
		if (windValue == null) {
			windValue = new Wind(direction:0,speed:0)
		}
		
		println "LegTime: tas:$tasValue, wind:${windValue.name()}, truetrack:$trueTrackValue, dist:$distValue"
	    Map ret = AviationMath.calculateWind(windValue.direction, windValue.speed, tasValue, trueTrackValue, distValue)
		println "LegTime: legtime:${ret.legtime}h"
		return ret.legtime
	}
	
    //--------------------------------------------------------------------------
    private void calculate_testlegflights(Task taskInstance)
    {
        printstart "calculate_testlegflights: ${taskInstance.name()}"
		boolean something_calculated = false
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				if (test_instance.flighttestwind) {
					if (!test_instance.timeCalculated) {
						calculate_testlegflight(test_instance)
						something_calculated = true
					}
				}
			}
        }
		if (something_calculated) {
			printdone ""
		} else {
			printdone "Nothing to calculate."
		}
    }

    //--------------------------------------------------------------------------
    private void calculate_testlegflight(Test testInstance)
    {
        printstart "calculate_testlegflight: ${testInstance.crew.name}"
        
        // remove all CalcResults
        if (testInstance.IsLoggerResult()) {
            CalcResult.findAllByLoggerresult(testInstance.loggerResult,[sort:"id"]).each { CalcResult calcresult_instance ->
                calcresult_instance.delete()
            }
        }
        
        // remove all TestLegFlights
		printstart "Remove all TestLegFlight ${TestLegFlight.countByTest(testInstance)} instances"
        TestLegFlight.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { TestLegFlight testlegflight_instance, int i ->
            testlegflight_instance.delete(flush:true)
			println "TestLegFlight instance ${i+1} deleted (flush:true)."
        }
		printdone ""

        // calculate TestLegFlights
		printstart "Create and calculate TestLegFlight instances"
        RouteLegCoord.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route,[sort:"id"]).each { RouteLegCoord routelegcoord_instance ->
			switch (routelegcoord_instance.startTitle.type) {
				case CoordType.TO:
				case CoordType.FP:
					// nothing
					break
				case CoordType.SECRET:
		            TestLegFlight testlegflight_instance = new TestLegFlight()
		            calculateLeg(testInstance,testlegflight_instance, routelegcoord_instance, testInstance.flighttestwind.flighttest.route, testInstance.flighttestwind.wind, testInstance.taskTAS, 0, routelegcoord_instance.turnTrueTrack) // 0 - ohne ProcedureTurn
		            testlegflight_instance.test = testInstance
		            testlegflight_instance.save()
					break
				default:
		            TestLegFlight testlegflight_instance = new TestLegFlight()
		            calculateLeg(testInstance,testlegflight_instance, routelegcoord_instance, testInstance.flighttestwind.flighttest.route, testInstance.flighttestwind.wind, testInstance.taskTAS, testInstance.task.procedureTurnDuration, routelegcoord_instance.turnTrueTrack)
		            testlegflight_instance.test = testInstance
		            testlegflight_instance.save()
					break
			}
        }
		printdone ""
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculate_coordresult(Test testInstance)
    {
		printstart "calculate_coordresult: ${testInstance.crew.name}"
		
        // remove all coordResultInstances
		printstart "Remove all CoordResult instances"
        CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
            coordresult_instance.delete(flush:true)
			println "CoordResult instance ${i+1} deleted (flush:true)."
        }
		printdone ""

        // create coordResultInstances
		printstart "Create and calculate CoordResult instances"
        int coord_index = 0
        Route route_instance = testInstance.flighttestwind.flighttest.route
		CoordType last_coordtype = CoordType.UNKNOWN
        CoordRoute.findAllByRoute(route_instance,[sort:"id"]).each { CoordRoute coordroute_instance ->
            CoordResult coordresult_instance = new CoordResult()
            coordresult_instance.type = coordroute_instance.type
            coordresult_instance.titleNumber = coordroute_instance.titleNumber
            coordresult_instance.mark = coordroute_instance.mark
            coordresult_instance.latGrad = coordroute_instance.latGrad
            coordresult_instance.latMinute = coordroute_instance.latMinute
            coordresult_instance.latDirection = coordroute_instance.latDirection
            coordresult_instance.lonGrad = coordroute_instance.lonGrad
            coordresult_instance.lonMinute = coordroute_instance.lonMinute
            coordresult_instance.lonDirection = coordroute_instance.lonDirection
            coordresult_instance.altitude = coordroute_instance.altitude
            coordresult_instance.minAltitudeAboveGround = coordroute_instance.minAltitudeAboveGround
            coordresult_instance.maxAltitudeAboveGround = coordroute_instance.maxAltitudeAboveGround
            coordresult_instance.gatewidth2 = coordroute_instance.gatewidth2
            coordresult_instance.endCurved = coordroute_instance.endCurved
            coordresult_instance.ignoreGate = coordroute_instance.ignoreGate
            
			// Set planProcedureTurn (CoordResult)
			if (route_instance.useProcedureTurns && coordroute_instance.planProcedureTurn && last_coordtype.IsProcedureTurnCoord()) {
				coordresult_instance.planProcedureTurn = true
			}
			
			// planCpTime
            switch (coordroute_instance.type) {
                case CoordType.TO:
                    coordresult_instance.planCpTime = testInstance.takeoffTime
                    break
                case CoordType.SP:
                    coordresult_instance.planCpTime = testInstance.startTime
                    break
                case CoordType.LDG:
                    coordresult_instance.planCpTime = testInstance.maxLandingTime
                    break
                case CoordType.TP:
                case CoordType.FP:
                case CoordType.iFP:
                case CoordType.iLDG:
                case CoordType.iTO:
                case CoordType.iSP:
                case CoordType.SECRET:
                    coord_index++
                    Date cp_time = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { TestLegFlight testlegflight_instance, int leg_index ->
                        cp_time = testlegflight_instance.AddPlanLegTime(cp_time)
                        if (coord_index == leg_index + 1) {
                            coordresult_instance.planCpTime = cp_time
                        }
                    }
                    break
				/*
                case CoordType.SECRET:
                    Date cp_time = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { TestLegFlight testlegflight_instance, int leg_index ->
                        if (coord_index == leg_index) {
							cp_time = testlegflight_instance.AddPlanLegTime(cp_time,coordroute_instance.secretLegRatio)
                            coordresult_instance.planCpTime = cp_time
                        } else {
                            cp_time = testlegflight_instance.AddPlanLegTime(cp_time)
                        }
                    }
                    break
				*/
            }
            
			coordresult_instance.test = testInstance
            coordresult_instance.save()

			println "${coordresult_instance.title()} ${coordresult_instance.mark} ${FcMath.TimeStr(coordresult_instance.planCpTime)}"
			
			last_coordtype = coordroute_instance.type
        }
		printdone ""

		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void calulateTestLegPlannings(Test testInstance)
    {
        printstart "calulateTestLegPlannings: ${testInstance.crew.name}"
        
		if (!testInstance.planningtesttask) {
			printdone "No PlanningTestTask"
			return
		}
		
		Route route_instance = testInstance.planningtesttask.route
		
        // remove all TestLegPlannings
        TestLegPlanning.findAllByTest(testInstance,[sort:"id"]).each { TestLegPlanning testlegplanning_instance ->
            testlegplanning_instance.delete()
        }
        
        // calculate TestLegPlannings with results 
        RouteLegTest.findAllByRoute(route_instance,[sort:"id"]).each { RouteLegTest routelegtest_instance ->
            TestLegPlanning testlegplanning_instance = new TestLegPlanning()
            testlegplanning_instance.noPlanningTest = routelegtest_instance.noPlanningTest
            testlegplanning_instance.endCurved = routelegtest_instance.endCurved
            calculateLeg(testInstance,testlegplanning_instance, routelegtest_instance, route_instance, testInstance.planningtesttask.wind, testInstance.taskTAS, testInstance.planningtesttask.planningtest.task.procedureTurnDuration, routelegtest_instance.turnTrueTrack)
            testlegplanning_instance.test = testInstance
            if (!testInstance.IsPlanningTestDistanceMeasure()) {
                testlegplanning_instance.resultTestDistance = testlegplanning_instance.planTestDistance
            }
            if (!testInstance.IsPlanningTestDirectionMeasure()) {
                testlegplanning_instance.resultTrueTrack = testlegplanning_instance.planTrueTrack
            }
            testlegplanning_instance.save()
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculateLeg(Test testInstance, TestLeg testLegInstance, RouteLeg routeLegInstance, Route routeInstance,
		                      Wind windInstance, BigDecimal valueTAS, int procedureTurnDuration, BigDecimal lastTrueTrack) 
    {
		printstart "calculateLeg ${routeLegInstance.startTitle.name()}...${routeLegInstance.endTitle.name()}"
		
        // save route data
		testLegInstance.coordTitle = routeLegInstance.endTitle
        testLegInstance.planTestDistance = routeLegInstance.testDistance()
        testLegInstance.planTrueTrack = routeLegInstance.testTrueTrack()
        testLegInstance.endCurved = routeLegInstance.endCurved

        // calculate wind
	    Map ret = AviationMath.calculateWind(windInstance.direction, windInstance.speed, valueTAS,
		                                     testLegInstance.planTrueTrack, testLegInstance.planTestDistance)
	    testLegInstance.planTrueHeading = ret.trueheading
	    testLegInstance.planGroundSpeed = ret.groundspeed
		switch (testLegInstance.coordTitle.type) {
			case CoordType.iLDG:
                String ildgdurationformula = "time+:10min" // for PlanningTest
                if (testInstance.flighttestwind) {
                    ildgdurationformula = testInstance.flighttestwind.iLDGDurationFormula
                }
				Map calculated_time = TimeCalculator(CoordType.iLDG, ildgdurationformula, routeInstance, windInstance, valueTAS)
				testLegInstance.planLegTime = calculated_time.hours
				testLegInstance.planFullMinute = calculated_time.fullminute
				break
			case CoordType.iSP:
                String itodurationformula = "time+:10min" // for PlanningTest
                if (testInstance.flighttestwind) {
                    itodurationformula = testInstance.flighttestwind.iTODurationFormula
                }
				Map calculated_time = TimeCalculator(CoordType.iSP, itodurationformula, routeInstance, windInstance, valueTAS)
				testLegInstance.planLegTime = calculated_time.hours
				testLegInstance.planFullMinute = calculated_time.fullminute
				break
			default:
				if (routeLegInstance.legDuration) {
					testLegInstance.planLegTime = FcMath.Hours(routeLegInstance.legDuration)
				} else {
			    	testLegInstance.planLegTime = ret.legtime
				}
				break 
		}
		
        // Set planProcedureTurn (TestLeg)
        testLegInstance.planProcedureTurn = false
        testLegInstance.planProcedureTurnDuration = 0
        if ((procedureTurnDuration > 0) && (lastTrueTrack != null) && routeInstance.useProcedureTurns) {
			if (routeLegInstance.startTitle.type.IsProcedureTurnCoord()) {
	            if (IsCourseChangeGreaterEqual90(testLegInstance.planTrueTrack,lastTrueTrack)) {
	        	    testLegInstance.planProcedureTurn = true
	        	    testLegInstance.planProcedureTurnDuration = procedureTurnDuration
	            }
			}
        }

		if (routeLegInstance.legDuration) {
			println "Distance ${routeLegInstance.testDistance()}, TrueTrack ${routeLegInstance.testTrueTrack()}, Duration ${routeLegInstance.legDuration}min"
		} else {
			println "Distance ${routeLegInstance.testDistance()}, TrueTrack ${routeLegInstance.testTrueTrack()}"
		}
		printdone "Plan heading ${testLegInstance.planTrueHeading}, Plan ground speed ${testLegInstance.planGroundSpeed}, Plan leg time ${FcMath.TimeStr(testLegInstance.planLegTime)}"
    }

    //--------------------------------------------------------------------------
    Map putAircraft(Map contest, String registration, String type, String colour)
    {
		printstart "putAircraft"
        Map p = [:]
        p.registration = registration
        p.type = type
        p.colour = colour
        Map ret = saveAircraft(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putContest(String title, String printPrefix, boolean resultclasses, int teamCrewNum, ContestRules contestRule, String contestDate, String timeZone, boolean testExists)
    {
		printstart "putContest"
        Map p = [:]
        p.title = title
		p.printPrefix = printPrefix
		p.resultClasses = resultclasses
		p.teamCrewNum = teamCrewNum
		p.contestRule = contestRule
        p.liveTrackingContestDate = contestDate
        p.timeZone2 = TimeZone.getTimeZone(timeZone)
		p.testExists = testExists
        Map ret = saveContest(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putTask(Map contest, String title, String firstTime, int takeoffIntervalNormal, int parkingDuration,
		        boolean planningTestRun, boolean flightTestRun, boolean observationTestRun, boolean landingTestRun, boolean specialTestRun,
		 		boolean planningTestDistanceMeasure, boolean planningTestDirectionMeasure, 
				boolean flightTestCheckSecretPoints, boolean flightTestCheckTakeOff, boolean flightTestCheckLanding,
				boolean landingTest1Run, boolean landingTest2Run, boolean landingTest3Run, boolean landingTest4Run,
                boolean observationTestTurnpointRun, boolean observationTestEnroutePhotoRun, boolean observationTestEnrouteCanvasRun,
                boolean bestOfAnalysis, boolean increaseEnabled)
    {
		printstart "putTask"
        Map p = [:]
        p.title = title
		p.liveTrackingNavigationTaskDate = contest.instance.liveTrackingContestDate
		p.firstTime = firstTime
		p.takeoffIntervalNormal = takeoffIntervalNormal
		p.parkingDuration = parkingDuration
		p.planningTestRun = planningTestRun
		p.flightTestRun = flightTestRun
		p.observationTestRun = observationTestRun
        p.observationTestTurnpointRun = observationTestTurnpointRun
        p.observationTestEnroutePhotoRun = observationTestEnroutePhotoRun
        p.observationTestEnrouteCanvasRun = observationTestEnrouteCanvasRun
		p.landingTestRun = landingTestRun
		p.specialTestRun = specialTestRun
		p.planningTestDistanceMeasure = planningTestDistanceMeasure
		p.planningTestDirectionMeasure = planningTestDirectionMeasure
		p.flightTestCheckSecretPoints = flightTestCheckSecretPoints
		p.flightTestCheckTakeOff = flightTestCheckTakeOff
		p.flightTestCheckLanding = flightTestCheckLanding
		p.landingTest1Run = landingTest1Run
		p.landingTest2Run = landingTest2Run
		p.landingTest3Run = landingTest3Run
		p.landingTest4Run = landingTest4Run
		p.bestOfAnalysis = bestOfAnalysis
        p.increaseEnabled = increaseEnabled
        Map ret = saveTask(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putResultClass(Map contest, String name, String contestTitle, ContestRules contestRule)
    {
		printstart "putResultClass"
		
        ResultClass resultclass_instance = ResultClass.findByNameAndContest(name,contest.instance)
        if (resultclass_instance) {
			resultclass_instance.name = name
			resultclass_instance.contestTitle = contestTitle
			resultclass_instance.contestRule = contestRule
			setContestRulePoints(resultclass_instance, resultclass_instance.contestRule)
			if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
				Map ret = ['instance':resultclass_instance,'saved':true,'message':getMsg('fc.created',["${resultclass_instance.name}"])]
				printdone "$ret (modified)"
				return ret
			} else {
				Map ret = ['instance':resultclass_instance]
				printdone "$ret (modified)"
				return ret
			}
		} else {
	        Map p = [:]
	        p.name = name
			p.contestTitle = contestTitle
			p.contestRule = contestRule
	        Map ret = saveResultClass(p,contest.instance)
			printdone "$ret (new)"
			return ret
		}
    }
    
    //--------------------------------------------------------------------------
    void puttaskclassTask(Map task, Map resultClass,
		                  boolean planningTestRun, boolean flightTestRun, boolean observationTestRun, boolean landingTestRun, boolean specialTestRun,
		 		          boolean planningTestDistanceMeasure, boolean planningTestDirectionMeasure, 
						  boolean flightTestCheckSecretPoints, boolean flightTestCheckTakeOff, boolean flightTestCheckLanding,
						  boolean landingTest1Run, boolean landingTest2Run, boolean landingTest3Run, boolean landingTest4Run,
                          boolean observationTestTurnpointRun, boolean observationTestEnroutePhotoRun, boolean observationTestEnrouteCanvasRun
                          )
	{
		printstart "puttaskclassTask"
		
		Task task_instance = task.instance
		for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance,[sort:"id"])) {
			if (taskclass_instance.resultclass.id == resultClass.instance.id) {
				taskclass_instance.planningTestRun = planningTestRun
				taskclass_instance.flightTestRun = flightTestRun
				taskclass_instance.observationTestRun = observationTestRun
                taskclass_instance.observationTestTurnpointRun = observationTestTurnpointRun
                taskclass_instance.observationTestEnroutePhotoRun = observationTestEnroutePhotoRun
                taskclass_instance.observationTestEnrouteCanvasRun = observationTestEnrouteCanvasRun
				taskclass_instance.landingTestRun = landingTestRun
				taskclass_instance.specialTestRun = specialTestRun
				taskclass_instance.planningTestDistanceMeasure = planningTestDistanceMeasure
				taskclass_instance.planningTestDirectionMeasure = planningTestDirectionMeasure
				taskclass_instance.flightTestCheckSecretPoints = flightTestCheckSecretPoints
				taskclass_instance.flightTestCheckTakeOff = flightTestCheckTakeOff
				taskclass_instance.flightTestCheckLanding = flightTestCheckLanding
				taskclass_instance.landingTest1Run = landingTest1Run
				taskclass_instance.landingTest2Run = landingTest2Run
				taskclass_instance.landingTest3Run = landingTest3Run
				taskclass_instance.landingTest4Run = landingTest4Run
				taskclass_instance.save()
				break
			}
		}
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map putTeam(Map contest, String name) 
    {
		printstart "putTeam"
        Map p = [:]
        p.name = name
        Map ret = saveTeam(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putRoute(Map contest, String title, String mark, TurnpointRoute turnpointRoute, EnrouteRoute enroutePhotoRoute, 
                 EnrouteRoute enrouteCanvasRoute, EnrouteMeasurement enroutePhotoMeasurement, EnrouteMeasurement enrouteCanvasMeasurement)
    {
		printstart "putRoute"
        Map p = [:]
        p.title = title
        p.mark = mark
        p.turnpointRoute = turnpointRoute
        p.enroutePhotoRoute = enroutePhotoRoute
        p.enrouteCanvasRoute = enrouteCanvasRoute
        p.enroutePhotoMeasurement = enroutePhotoMeasurement
        p.enrouteCanvasMeasurement = enrouteCanvasMeasurement
        Map ret = saveRoute(p,contest.instance)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putCoordRoute(String showLanguage, Map route, CoordType type, int titleNumber, String mark, String latDirection, int latGrad, BigDecimal latMinute, String lonDirection, int lonGrad, BigDecimal lonMinute, int altitude, Float gatewidth2, BigDecimal measureDistance, BigDecimal measureTrueTrack)
    {
		printstart "putCoordRoute"
        Map p = [:]
        p.routeid = route.instance.id
        p.type = type
        if (titleNumber > 0) {
        	p.titleNumber = titleNumber
        }
        p.mark = mark
        p.latGrad = latGrad
        p.latMinute = latMinute.toString()
        p.latDirection = latDirection
        p.lonGrad = lonGrad
        p.lonMinute = lonMinute.toString()
        p.lonDirection = lonDirection
        p.altitude = altitude 
        p.gatewidth2 = gatewidth2.toString()
		p.measureDistance = measureDistance
        p.measureTrueTrack = measureTrueTrack
        Map ret = saveCoordRoute(showLanguage, p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putCrew(Map contest, int startNum, String name, String email, String teamname, String resultclassname, String registration, String type, String colour, BigDecimal tas)
    {
		printstart "putCrew"
        Map p = [:]
		p.startNum = startNum
        p.name = name
        p.email = email
		p.teamname = teamname
		p.resultclassname = resultclassname 
        p.registration = registration
        p.type = type
        p.colour = colour
        p.tas = tas.toString()
        Map ret = saveCrew(p,contest.instance)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(Map task, String title, Map route)
    {
		printstart "putFlightTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.route = route.instance
        p.direction = "0"
        p.speed = "0"
        Map ret = saveFlightTest("", p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTest(Map task, String title, Map route, BigDecimal direction, BigDecimal speed)
    {
		printstart "putFlightTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.route = route.instance
        p.direction = direction.toString()
        p.speed = speed.toString()
        Map ret = saveFlightTest("", p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTestWind(Map flighttest, BigDecimal direction, BigDecimal speed, 
                          BigDecimal toDirection, BigDecimal toOffset, BigDecimal toOrthogonalOffset,
                          BigDecimal ldgDirection, BigDecimal ldgOffset, BigDecimal ldgOrthogonalOffset,
                          BigDecimal itoildgDirection, BigDecimal itoildgOffset, BigDecimal itoildgOrthogonalOffset,
                          String toDurationFormula, String ldgDurationFormula, String ildgDurationFormula, String itoDurationFormula)
    {
		printstart "putFlightTestWind"
        Map p = [:]
        p.flighttestid = flighttest.instance.id
        p.direction = direction.toString()
        p.TODirection = toDirection
        p.TOOffset = toOffset
        p.TOOrthogonalOffset = toOrthogonalOffset
        p.LDGDirection = ldgDirection
        p.LDGOffset = ldgOffset
        p.LDGOrthogonalOffset = ldgOrthogonalOffset
        p.iTOiLDGDirection = itoildgDirection
        p.iTOiLDGOffset = itoildgOffset
        p.iTOiLDGOrthogonalOffset = itoildgOrthogonalOffset
        p.TODurationFormula = toDurationFormula
        p.LDGDurationFormula = ldgDurationFormula
        p.iLDGDurationFormula = ildgDurationFormula
        p.iTODurationFormula = itoDurationFormula
        p.speed = speed
        Map ret = saveFlightTestWind("", p)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putPlanningTest(Map task, String title)
    {
		printstart "putPlanningTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.direction = "0"
        p.speed = "0"
        Map ret = savePlanningTest(p,false)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putPlanningTest(Map task, String title, String taskTitle, Map route, BigDecimal direction, BigDecimal speed)
    {
		printstart "putPlanningTest"
        Map p = [:]
        p.taskid = task.instance.id
        p.title = title
        p.taskTitle = taskTitle
        p.route = route.instance
        p.direction = direction.toString()
        p.speed = speed.toString()
        Map ret = savePlanningTest(p,false)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putPlanningTestTask(Map planningtest, String title, Map route, BigDecimal direction, BigDecimal speed)
    {
		printstart "putPlanningTestTask" 
        Map p = [:]
        p.planningtestid = planningtest.instance.id
        p.title = title
        p.route = route.instance
        p.direction = direction.toString()
        p.speed = speed.toString()
        Map ret = savePlanningTestTask(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    void putplanningtesttaskTask(Map task, Map planningtesttask)
    {
		printstart "putplanningtesttaskTask"
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            test_instance.planningtesttask = planningtesttask.instance
            calulateTestLegPlannings(test_instance)
            test_instance.save()
			println "$test_instance.crew.name"
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    void putplanningtesttaskcrewsTask(Map task, Map planningtesttask, List crewList)
    {
		printstart "putplanningtesttaskTask"
	    Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			crewList.each { Map crew ->
				if (crew.instance == test_instance.crew) {
		            test_instance.planningtesttask = planningtesttask.instance
		            calulateTestLegPlannings(test_instance)
		            test_instance.save()
					println "$test_instance.crew.name"
				}
	        }
		}
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    void putplanningresultsTask(Map task, List crewResults)
    {
		printstart "putplanningresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
		    		TestLegPlanning.findAllByTest(test_instance,[sort:"id"]).eachWithIndex { TestLegPlanning testlegplanning_instance, int j  ->
		        		if (crew_result.givenValues[j]?.trueHeading && crew_result.givenValues[j]?.legTime) {
			    			testlegplanning_instance.resultTrueHeading = crew_result.givenValues[j].trueHeading
			        		testlegplanning_instance.resultLegTimeInput = crew_result.givenValues[j].legTime
			        		calculateLegPlanningInstance(testlegplanning_instance,false)
			        		testlegplanning_instance.save()
		        		}
		        	}
		            test_instance.planningTestGivenTooLate = crew_result.planningTestGivenTooLate
		            test_instance.planningTestExitRoomTooLate = crew_result.planningTestExitRoomTooLate
                    test_instance.planningTestForbiddenCalculators = crew_result.planningTestForbiddenCalculators
		            calculateTestPenalties(test_instance,false)
		           	test_instance.planningTestComplete = test_instance.planningTestLegComplete && crew_result.testComplete
		            calculateTestPenalties(test_instance,false)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void putflighttestwindTask(Map task, Map flighttestwind)
    {
		printstart "putflighttestwindTask"
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            test_instance.flighttestwind = flighttestwind.instance
            test_instance.save()
			println "$test_instance.crew.name"
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    void putflighttestwindcrewsTask(Map task, Map flighttestwind, List crewList)
    {
		printstart "putflighttestwindTask"
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			crewList.each { Map crew ->
				if (crew.instance == test_instance.crew) {
		            test_instance.flighttestwind = flighttestwind.instance
		            test_instance.save()
					println "$test_instance.crew.name"
				}
			}
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    void putflightresultsTask(Map task, List crewResults)
    {
		printstart "putflightresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					putflightresults(test_instance, crew_result)
				}
	        }
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void importflightresultsTask(Map task, List crewResults, String fileExtension)
    {
		printstart "importflightresultsTask $fileExtension"
		Task task_instance = task.instance

		crewResults.each { Map crew_result ->
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
                    Map ret = importLoggerResultTest(fileExtension, test_instance, crew_result.fileName)
					if (!ret.error) {
						putflightresults(test_instance, crew_result)
					}
				}
	        }
		}
		
		printdone ""
	}
	
    //--------------------------------------------------------------------------
	private void putflightresults(Test testInstance, Map crewResult)
	{
		if (crewResult.givenValues) {
			CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int j  ->
				boolean calculate = false
	    		if (crewResult.givenValues[j]?.cpNotFound) {
	    			coordresult_instance.resultCpNotFound = true
	    			calculate = true
	    		} else if (crewResult.givenValues[j]?.cpTime) {
	    			coordresult_instance.resultCpTimeInput =  crewResult.givenValues[j].cpTime
	                calculate = true
	    		}
	            if (crewResult.givenValues[j]?.procedureTurnNotFlown) {
	                calculate = true
	                coordresult_instance.resultProcedureTurnNotFlown = true
	            }
	            if (crewResult.givenValues[j]?.altitude) {
	                calculate = true
	                coordresult_instance.resultAltitude = crewResult.givenValues[j].altitude
	            }
	            if (crewResult.givenValues[j]?.badCourseNum) {
	                calculate = true
	                coordresult_instance.resultBadCourseNum = crewResult.givenValues[j].badCourseNum
	            }
	            if (calculate) {
	                calculateCoordResultInstance(coordresult_instance,false,false)
	                coordresult_instance.resultProcedureTurnEntered = true
	                coordresult_instance.save()
	            }
			}
        }
		if (crewResult.correctionValues) {
			crewResult.correctionValues.each { Map correction_value ->
				CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
					if (correction_value.mark == coordresult_instance.mark) {
						boolean calculate = false
			    		if (correction_value?.cpNotFound) {
			    			coordresult_instance.resultCpNotFound = true
			    			calculate = true
			    		} else if (correction_value?.cpFound) {
			    			coordresult_instance.resultCpNotFound = false
			    			if (correction_value?.cpTime) {
								coordresult_instance.resultCpTimeInput =  correction_value.cpTime
			    			}
			                calculate = true
			    		}
			            if (correction_value?.procedureTurnNotFlown) {
			                calculate = true
			                coordresult_instance.resultProcedureTurnNotFlown = true
			            } else if (correction_value?.procedureTurnFlown) {
			                calculate = true
			                coordresult_instance.resultProcedureTurnNotFlown = false
			            }
			            if (correction_value?.altitude) {
			                calculate = true
			                coordresult_instance.resultAltitude = correction_value.altitude
			            }
			            if (correction_value?.badCourseNum) {
			                calculate = true
			                coordresult_instance.resultBadCourseNum = correction_value.badCourseNum
			            }
			            if (calculate) {
			                calculateCoordResultInstance(coordresult_instance,false,false)
			                coordresult_instance.save()
			            }
					}
				}
			}
		}
        testInstance.flightTestTakeoffMissed = crewResult.flightTestTakeoffMissed
        testInstance.flightTestBadCourseStartLanding = crewResult.flightTestBadCourseStartLanding
        testInstance.flightTestLandingTooLate = crewResult.flightTestLandingTooLate
        testInstance.flightTestGivenTooLate = crewResult.flightTestGivenTooLate
        testInstance.flightTestSafetyAndRulesInfringement = crewResult.flightTestSafetyAndRulesInfringement
        testInstance.flightTestInstructionsNotFollowed = crewResult.flightTestInstructionsNotFollowed
        testInstance.flightTestFalseEnvelopeOpened = crewResult.flightTestFalseEnvelopeOpened
        testInstance.flightTestSafetyEnvelopeOpened = crewResult.flightTestSafetyEnvelopeOpened
        testInstance.flightTestFrequencyNotMonitored = crewResult.flightTestFrequencyNotMonitored
        testInstance.flightTestForbiddenEquipment = crewResult.flightTestForbiddenEquipment
        calculateTestPenalties(testInstance,false)
        testInstance.flightTestComplete = testInstance.flightTestCheckPointsComplete && crewResult.testComplete
        calculateTestPenalties(testInstance,false)
        testInstance.save()
	}
	
    //--------------------------------------------------------------------------
    void putobservationresultsTask(Map task, List crewResults)
    {
		printstart "putobservationresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
                    test_instance.observationTestTurnPointPhotoPenalties = crew_result.turnPointPhotos
					test_instance.observationTestRoutePhotoPenalties = crew_result.routePhotos
					test_instance.observationTestGroundTargetPenalties = crew_result.groundTargets
		            calculateTestPenalties(test_instance,false)
					test_instance.observationTestComplete = crew_result.testComplete
		            calculateTestPenalties(test_instance,false)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void putlandingresultsTask(Map task, List crewResults)
    {
		printstart "putlandingresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					if (test_instance.IsLandingTestAnyRun()) {
						test_instance.landingTest1Measure = crew_result.landingTest1Measure
						test_instance.landingTest1Landing = crew_result.landingTest1Landing
						test_instance.landingTest1RollingOutside = crew_result.landingTest1RollingOutside
						test_instance.landingTest1PowerInBox = crew_result.landingTest1PowerInBox
						test_instance.landingTest1GoAroundWithoutTouching = crew_result.landingTest1GoAroundWithoutTouching
						test_instance.landingTest1GoAroundInsteadStop = crew_result.landingTest1GoAroundInsteadStop
						test_instance.landingTest1AbnormalLanding = crew_result.landingTest1AbnormalLanding
                        test_instance.landingTest1NotAllowedAerodynamicAuxiliaries = crew_result.landingTest1NotAllowedAerodynamicAuxiliaries
                        test_instance.landingTest1PowerInAir = crew_result.landingTest1PowerInAir
                        test_instance.landingTest1FlapsInAir = crew_result.landingTest1FlapsInAir
                        test_instance.landingTest1TouchingObstacle = crew_result.landingTest1TouchingObstacle
						test_instance.landingTest1Complete = crew_result.testComplete
						test_instance.landingTest2Measure = crew_result.landingTest2Measure
						test_instance.landingTest2Landing = crew_result.landingTest2Landing
						test_instance.landingTest2RollingOutside = crew_result.landingTest2RollingOutside
						test_instance.landingTest2PowerInBox = crew_result.landingTest2PowerInBox
						test_instance.landingTest2GoAroundWithoutTouching = crew_result.landingTest2GoAroundWithoutTouching
						test_instance.landingTest2GoAroundInsteadStop = crew_result.landingTest2GoAroundInsteadStop
						test_instance.landingTest2AbnormalLanding = crew_result.landingTest2AbnormalLanding
                        test_instance.landingTest2NotAllowedAerodynamicAuxiliaries = crew_result.landingTest2NotAllowedAerodynamicAuxiliaries
						test_instance.landingTest2PowerInAir = crew_result.landingTest2PowerInAir
                        test_instance.landingTest2FlapsInAir = crew_result.landingTest2FlapsInAir
                        test_instance.landingTest2TouchingObstacle = crew_result.landingTest2TouchingObstacle
						test_instance.landingTest2Complete = crew_result.testComplete
						test_instance.landingTest3Measure = crew_result.landingTest3Measure
						test_instance.landingTest3Landing = crew_result.landingTest3Landing
						test_instance.landingTest3RollingOutside = crew_result.landingTest3RollingOutside
						test_instance.landingTest3PowerInBox = crew_result.landingTest3PowerInBox
						test_instance.landingTest3GoAroundWithoutTouching = crew_result.landingTest3GoAroundWithoutTouching
						test_instance.landingTest3GoAroundInsteadStop = crew_result.landingTest3GoAroundInsteadStop
						test_instance.landingTest3AbnormalLanding = crew_result.landingTest3AbnormalLanding
                        test_instance.landingTest3NotAllowedAerodynamicAuxiliaries = crew_result.landingTest3NotAllowedAerodynamicAuxiliaries
						test_instance.landingTest3PowerInAir = crew_result.landingTest3PowerInAir
						test_instance.landingTest3FlapsInAir = crew_result.landingTest3FlapsInAir
                        test_instance.landingTest3TouchingObstacle = crew_result.landingTest3TouchingObstacle
						test_instance.landingTest3Complete = crew_result.testComplete
						test_instance.landingTest4Measure = crew_result.landingTest4Measure
						test_instance.landingTest4Landing = crew_result.landingTest4Landing
						test_instance.landingTest4RollingOutside = crew_result.landingTest4RollingOutside
						test_instance.landingTest4PowerInBox = crew_result.landingTest4PowerInBox
						test_instance.landingTest4GoAroundWithoutTouching = crew_result.landingTest4GoAroundWithoutTouching
						test_instance.landingTest4GoAroundInsteadStop = crew_result.landingTest4GoAroundInsteadStop
						test_instance.landingTest4AbnormalLanding = crew_result.landingTest4AbnormalLanding
                        test_instance.landingTest4NotAllowedAerodynamicAuxiliaries = crew_result.landingTest4NotAllowedAerodynamicAuxiliaries
                        test_instance.landingTest4PowerInAir = crew_result.landingTest4PowerInAir
                        test_instance.landingTest4FlapsInAir = crew_result.landingTest4FlapsInAir
						test_instance.landingTest4TouchingObstacle = crew_result.landingTest4TouchingObstacle
						test_instance.landingTest4Complete = crew_result.testComplete
					} else {
						if (crew_result?.landingPenalties) {
							test_instance.landingTestPenalties = crew_result.landingPenalties
						} else {
							test_instance.landingTestPenalties = 0
						}
					}
		            calculateTestPenalties(test_instance,false)
					test_instance.landingTestComplete = crew_result.testComplete
		            calculateTestPenalties(test_instance,false)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    void putspecialresultsTask(Map task, List crewResults)
    {
		printstart "putspecialresultsTask"
		Task task_instance = task.instance
		
		crewResults.each { Map crew_result ->
	    	Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					test_instance.specialTestPenalties = crew_result.specialPenalties
		            calculateTestPenalties(test_instance,false)
					test_instance.specialTestComplete = crew_result.testComplete
		            calculateTestPenalties(test_instance,false)
		    		test_instance.save()
				}
	        }
		}
		
		printdone ""
    }

    //--------------------------------------------------------------------------
	void putsequenceTask(Map task, List crewSequence)
	{
		printstart "putsequenceTask"
		int view_pos = 0
		crewSequence.each { Map crew ->
			Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
				if (test_instance.crew == crew.instance) {
					test_instance.viewpos = view_pos
					test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
                    test_instance.flightTestLink = ""
					delete_uploadjobtest(test_instance)
                    test_instance.crewResultsModified = true
					test_instance.save()
					view_pos++
				}
			}
		}
		printdone ""
	}

    //--------------------------------------------------------------------------
	private void delete_uploadjobtest(Test testInstance)
	{
		UploadJobTest uploadjob_test = UploadJobTest.findByTest(testInstance)
		if (uploadjob_test) {
			testInstance.uploadjobtest = null
			testInstance.save()
			uploadjob_test.delete()
		}
	}
	
    //--------------------------------------------------------------------------
    Map runcalculatesequenceTask(Map task)
    {
		printstart "runcalculatesequenceTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatesequenceTask(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
	void puttimetableTask(Map task, List crewStartTimes)
	{
		printstart "puttimetableTask"
		Task task_instance = task.instance
        Date first_date = null
        if (task_instance.firstTime.size() > 5) {
            first_date = Date.parse("HH:mm:ss",task_instance.firstTime)
        } else {
            first_date = Date.parse("HH:mm",task_instance.firstTime)
        }
        
		calculate_testlegflights(task_instance)
		
        if (crewStartTimes) {
    		crewStartTimes.each { Map crew_start_time ->
    			printstart "$crew_start_time.crew.instance.name $crew_start_time.starttime"
    			Date last_arrival_time = first_date
    			
    	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
    				if (test_instance.crew == crew_start_time.crew.instance) {
    					Date start_date = Date.parse("HH:mm",crew_start_time.starttime)
    					GregorianCalendar start_time = new GregorianCalendar() 
    					start_time.setTime(start_date)
    					
    					if (!test_instance.timeCalculated) {
    						calculate_test_time(test_instance, task_instance, start_time, last_arrival_time, true)
    						calculate_coordresult(test_instance)
    						last_arrival_time = test_instance.arrivalTime
    					}
    				}
    	        }
    			
    			printdone ""
    		}
        } else {
            int crew_num = calulateTimetable(task_instance)
        }
        
        calulateTimetableWarnings(task_instance)
        
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map runcalculatetimetableTask(Map task)
    {
		printstart "runcalculatetimetableTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatetimetableTask(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map importMap(MultipartFile zipFile, String mapDirName, String mapName)
    {
        Map ret = [importOk:false]
        
        // upload zip file
        String uuid = UUID.randomUUID().toString()
        String webroot_dir = servletContext.getRealPath("/")
        String upload_filename = "${Defs.ROOT_FOLDER_GPXUPLOAD}/ZIP-${uuid}-UPLOAD.zip"
        printstart "Upload ${zipFile.getOriginalFilename()} -> $upload_filename"
        zipFile.transferTo(new File(webroot_dir, upload_filename))
        printdone ""
        
        // read map from zip file
        boolean png_found = false
        boolean pngw_found = false
        boolean pnginfo_found = false
        def zip_file = new java.util.zip.ZipFile(webroot_dir + upload_filename)
        zip_file.entries().findAll { !it.directory }.each {
            if (!it.isDirectory()) {
                String extension = it.name.substring(it.name.lastIndexOf('.')).toLowerCase()
                switch (extension) {
                    case ".png":
                        png_found = true
                        break
                    case ".pngw":
                        pngw_found = true
                        break
                    case ".pnginfo":
                        pnginfo_found = true
                        break
                }
            }
        }
        if (png_found && pngw_found && pnginfo_found) {
            zip_file.entries().findAll { !it.directory }.each {
                if (!it.isDirectory()) {
                    String extension = it.name.substring(it.name.lastIndexOf('.')).toLowerCase()
                    String save_file_name = "${mapDirName}/${mapName}${extension}"
                    println save_file_name
                    File save_file = new File(save_file_name)
                    save_file << zip_file.getInputStream(it)
                }
            }
            ret.importOk = true
        }
        zip_file.close()
        
        // delete file
        DeleteFile(webroot_dir + upload_filename)
        
        return ret
    }
    
    //--------------------------------------------------------------------------
	Map testData(List testData)
	{
		printstart "testData"
		int error_num = 0
		testData.each { Map table_test_data ->
			int table_error_num = 0
			
			// check count
			int table_count = table_test_data.table.size()
			String table_name = "$table_test_data.name [$table_count]"
			if (table_count != table_test_data.count) {
				table_error_num = add_error_num(table_error_num, table_name)
				println "Table count $table_test_data.count expected."
			}
			
			if (table_test_data.data) {
				// check data num
				if (table_test_data.data.size() != table_count) {
					table_error_num = add_error_num(table_error_num, table_name)
					println "Different testdata count ${table_test_data.data.size()}."
				}
				
				// check data
				table_test_data.data.eachWithIndex { Map test_datum, int test_datum_index ->
					def table_datum = table_test_data.table[test_datum_index]
					table_error_num = test_data(test_datum, test_datum_index, table_error_num, table_datum, table_name, table_datum)
				}
				 
			} else {
				table_error_num++
				println "No testdata."
			}
			if (table_error_num > 0) {
				printerror "$table_error_num errors."
			} else {
				println "$table_name Ok."
			}
			error_num += table_error_num
		}
		Map ret = [error:false,msgtext:""]
		if (error_num) {
			ret.error = true
			ret.msgtext = "$error_num errors." 
			printerror "Summary: $ret.msgtext"
		} else {
			ret.msgtext = "NO errors."
			printdone "Summary: $ret.msgtext"
		}
		return ret
	}
	
    //--------------------------------------------------------------------------
	private int test_data(Map testData, int testDataIndex, int tableErrorNum, tableDatum, tableName, recurseValue)
	{
		testData.each { key, value ->
			try {
                boolean ignore = false
				def value1 = recurseValue.(key.toString())
				switch (key) {
					case "planCpTime":
					case "resultCpTime":
					case "testingTime":
					case "endTestingTime":
					case "takeoffTime":
					case "startTime":
					case "finishTime":
					case "maxLandingTime":
					case "arrivalTime":
						value1 = FcMath.TimeStr(value1)
						break
                    case "latMinute":
                    case "lonMinute":
                        value1 = coord_format(value, value1)
                        break
                    case "resultLatitude":
                    case "resultLongitude":
                        value1 = latlon_format(value, value1)
                        break
                    case "mark":
                        ignore = true
                        break
				}
                if (!ignore) {
    				if (value == null) {
    					if (value1 != value) {
    						tableErrorNum = add_error_num(tableErrorNum, tableName)
    						println "${testDataIndex}: field '$key' has different value '$value1' ['$value' expected]."
    					} 
    				} else if (!value?.class) {
    					tableErrorNum = test_data(value, testDataIndex, tableErrorNum, tableDatum, tableName, value1)
    				} else if (value1 != value) {
    					tableErrorNum = add_error_num(tableErrorNum, tableName)
    					println "${testDataIndex}: field '$key' has different value '$value1' ['$value' expected]."
    				}
                }
			} catch (Exception e) {
				tableErrorNum = add_error_num(tableErrorNum, tableName)
				println "${testDataIndex}: Exception at field '$key': $e"
			}
		}
		return tableErrorNum
	}
	
    //--------------------------------------------------------------------------
    private static BigDecimal coord_format(BigDecimal expectedValue, BigDecimal coordValue)
    {
        String format_str = "0"
        String expected_str = expectedValue.toString()
        if (expected_str.contains(".")) {
            int expected_nachkomma = expected_str.size() - expected_str.indexOf(".") - 1
            if (expected_nachkomma > 0) {
                format_str += "."
                while (expected_nachkomma > 0) {
                    format_str += "0"
                    expected_nachkomma--
                }
            }
        }
        DecimalFormat df = new DecimalFormat(format_str)
        return df.format(coordValue).replaceAll(",", ".").toBigDecimal()
    }
    
    //--------------------------------------------------------------------------
    private static String latlon_format(String expectedValue, String actualValue)
    {
        String actual_value = actualValue.replaceAll(",", ".")
        return actual_value.substring(0,actual_value.size()-3) + expectedValue.substring(expectedValue.size()-3)
    }
    
    //--------------------------------------------------------------------------
	private int add_error_num(int errorNum, String printMessage)
	{
		errorNum++
		if (errorNum == 1) {
			printstart printMessage
		}
		return errorNum
	}
	
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
		def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
		def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
		return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
    }
    
    //--------------------------------------------------------------------------
    private String getPrintMsg(String code)
    {
		def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.printLanguage))
    }
    
	//--------------------------------------------------------------------------
	void SetCookie(response, String cookieName, String cookieValue)
	{
		Cookie cookie = new Cookie(cookieName, cookieValue)
        cookie.setMaxAge(maxCookieAge)
		cookie.setPath("/fc")
        response.addCookie(cookie)
		println "Set cookie '$cookieName' with '$cookieValue'."
    }
	
    //--------------------------------------------------------------------------
	private Cookie getCookie(String cookieName)
	{
        def cookies = RequestContextHolder.currentRequestAttributes().request.getCookies()
        if (cookies == null || cookieName == null || cookieName.length() == 0) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieName)) {
                return cookies[i]
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
	String GetCookie(String cookieName, String initValue)
	{
		Cookie cookie = getCookie(cookieName)
		if ( cookie ) {
			String value = cookie.getValue()
			println "Found cookie '$cookieName' with '$value'."
            return value
		} else {
			println "Cookie '$cookieName' not found."
			if (initValue) {
				return initValue
			}
			return ""
		}
    }
	
	//--------------------------------------------------------------------------
	void DeleteFile(String fileName)
	{
		print "Delete '$fileName'..."
		File file = new File(fileName)
		if (file.delete()) {
			println "Done."
		} else {
			println "Error."
		}
	}
	
    //--------------------------------------------------------------------------
    boolean Download(String downloadFileName, String returnFileName, OutputStream outputStream)
    {
        printstart "Download $downloadFileName -> $returnFileName"
        try {
            File download_file = new File(downloadFileName)
            outputStream.write(download_file.getBytes())
            outputStream.flush()
        } catch (Exception e) {
            printerror e.getMessage()
            return false
        }
        printdone ""
        return true
    }
    
	//--------------------------------------------------------------------------
	void printstart(out)
	{
		logService.printstart out
	}

	//--------------------------------------------------------------------------
	void printerror(out)
	{
		if (out) {
			logService.printend "Error: $out"
		} else {
			logService.printend "Error."
		}
	}

	//--------------------------------------------------------------------------
	void printdone(out)
	{
		if (out) {
			logService.printend "Done: $out"
		} else {
			logService.printend "Done."
		}
	}

	//--------------------------------------------------------------------------
	void print(out)
	{
		logService.print out
	}

	//--------------------------------------------------------------------------
	void println(out)
	{
		logService.println out
	}

}
