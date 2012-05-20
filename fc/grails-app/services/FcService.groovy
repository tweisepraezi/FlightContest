import java.util.Date;

import org.xhtmlrenderer.pdf.ITextRenderer
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.http.Cookie

class FcService
{
    boolean transactional = true
    def messageSource
	def logService
	def excelImportService
	
    static int mmPerNM = 1852000
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
            return ['message':getMsg('fc.notfound',[getMsg('fc.aircraft'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map printAircrafts(Map params,printparams)
    {
        Map aircrafts = [:]

        // Print aircrafts
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/aircraft/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            aircrafts.content = content 
        }
        catch (Throwable e) {
            aircrafts.message = getMsg('fc.aircraft.printerror',["$e"])
            aircrafts.error = true
        }
        return aircrafts
    }
    
    //--------------------------------------------------------------------------
    Map updateContest(Map params)
    {
		printstart "updateContest $params.resultfilter"
		
        Contest contest_instance = Contest.get(params.id)
        
        if (contest_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(contest_instance.version > version) {
                    contest_instance.errors.rejectValue("version", "contest.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':contest_instance]
                }
            }
			
			ContestRules contest_rule = contest_instance.contestRule
			
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
			
            contest_instance.properties = params
			
			switch (params.resultfilter) {
				case ResultFilter.Contest.toString():
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
			
			boolean modify_contest_rule = contest_instance.contestRule != contest_rule

			boolean modify_team_results = (contest_instance.teamCrewNum != team_crew_num) ||
			                              (contest_instance.teamPlanningResults != team_planning_results) ||
			                              (contest_instance.teamFlightResults != team_flight_results) ||
			 							  (contest_instance.teamObservationResults != team_observation_results) ||
										  (contest_instance.teamLandingResults != team_landing_results) ||
										  (contest_instance.teamSpecialResults != team_special_results) ||
										  (contest_instance.teamClassResults != team_class_results) ||
										  (contest_instance.teamTaskResults != team_task_results)
										  
			boolean modify_contest_results = (contest_instance.contestPlanningResults != contest_planning_results) ||
			                                 (contest_instance.contestFlightResults != contest_flight_results) ||
			 							     (contest_instance.contestObservationResults != contest_observation_results) ||
										     (contest_instance.contestLandingResults != contest_landing_results) ||
										     (contest_instance.contestSpecialResults != contest_special_results) ||
										     (contest_instance.contestClassResults != contest_class_results) ||
											 (contest_instance.contestTaskResults != contest_task_results)
											 
			if (modify_contest_rule) {
				println "Contest rule modfified."
				setContestRule(contest_instance, contest_instance.contestRule)
			}
			
			if (modify_team_results) {
				println "Team results modfified."
				for (Team team_instance in Team.findAllByContest(contest_instance,[sort:"id"])) {
					team_instance.contestPenalties = 0
					team_instance.contestPosition = 0
					team_instance.save()
				}
			}
			
			if (modify_contest_results) {
				println "Contest results modfified."
				for (Crew crew_instance in Crew.findAllByContest(contest_instance,[sort:"id"])) {
					crew_instance.contestPenalties = 0
					crew_instance.contestPosition = 0
					crew_instance.noContestPosition = false
					crew_instance.save()
				}
			}

            if(!contest_instance.hasErrors() && contest_instance.save()) {
				printdone ""
                return ['instance':contest_instance,'saved':true,'message':getMsg('fc.updated',["${contest_instance.title}"])]
            } else {
				printerror ""
                return ['instance':contest_instance]
            }
        } else {
			printerror ""
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    Map calculatepointsContest(Map params)
    {
        Contest contest_instance = Contest.get(params.id)
        
        if (contest_instance) {
            
			// Alle Berechungen erneut ausführen
			Task.findAllByContest(contest_instance,[sort:"id"]).each { Task task_instance ->
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
					calculateTestPenalties(test_instance,true)
		            test_instance.save()
		        }
			}

            return ['instance':contest_instance,'message':getMsg('fc.contestrule.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map createContest(Map params)
    {
        Contest contest_instance = new Contest()
        contest_instance.properties = params
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
        setContestRule(contest_instance, contest_instance.contestRule)
        if(!contest_instance.hasErrors() && contest_instance.save()) {
            return ['instance':contest_instance,'saved':true,'message':getMsg('fc.created',["${contest_instance.title}"])]
        } else {
            return ['instance':contest_instance]
        }
    }
    
    //--------------------------------------------------------------------------
	private void setContestRule(toInstance, ContestRules contestRule)
	{
		println "setContestRule '${getMsg(contestRule.titleCode)}'"
		
		// PlanningTest
		toInstance.planningTestDirectionCorrectGrad = contestRule.ruleValues.planningTestDirectionCorrectGrad
		toInstance.planningTestDirectionPointsPerGrad = contestRule.ruleValues.planningTestDirectionPointsPerGrad
		toInstance.planningTestTimeCorrectSecond = contestRule.ruleValues.planningTestTimeCorrectSecond
		toInstance.planningTestTimePointsPerSecond = contestRule.ruleValues.planningTestTimePointsPerSecond
		toInstance.planningTestMaxPoints = contestRule.ruleValues.planningTestMaxPoints
		toInstance.planningTestPlanTooLatePoints = contestRule.ruleValues.planningTestPlanTooLatePoints
		toInstance.planningTestExitRoomTooLatePoints = contestRule.ruleValues.planningTestExitRoomTooLatePoints
	
		// FlightTest
		toInstance.flightTestTakeoffMissedPoints = contestRule.ruleValues.flightTestTakeoffMissedPoints
		toInstance.flightTestCptimeCorrectSecond = contestRule.ruleValues.flightTestCptimeCorrectSecond
		toInstance.flightTestCptimePointsPerSecond = contestRule.ruleValues.flightTestCptimePointsPerSecond
		toInstance.flightTestCptimeMaxPoints = contestRule.ruleValues.flightTestCptimeMaxPoints
		toInstance.flightTestCpNotFoundPoints = contestRule.ruleValues.flightTestCpNotFoundPoints
		toInstance.flightTestProcedureTurnNotFlownPoints = contestRule.ruleValues.flightTestProcedureTurnNotFlownPoints
		toInstance.flightTestMinAltitudeMissedPoints = contestRule.ruleValues.flightTestMinAltitudeMissedPoints
		toInstance.flightTestBadCourseCorrectSecond = contestRule.ruleValues.flightTestBadCourseCorrectSecond
		toInstance.flightTestBadCoursePoints = contestRule.ruleValues.flightTestBadCoursePoints
		toInstance.flightTestBadCourseStartLandingPoints = contestRule.ruleValues.flightTestBadCourseStartLandingPoints
		toInstance.flightTestLandingToLatePoints = contestRule.ruleValues.flightTestLandingToLatePoints
		toInstance.flightTestGivenToLatePoints = contestRule.ruleValues.flightTestGivenToLatePoints
		
		// LandingTest
		toInstance.landingTest1MaxPoints = contestRule.ruleValues.landingTest1MaxPoints
		toInstance.landingTest1NoLandingPoints = contestRule.ruleValues.landingTest1NoLandingPoints
		toInstance.landingTest1OutsideLandingPoints = contestRule.ruleValues.landingTest1OutsideLandingPoints
		toInstance.landingTest1RollingOutsidePoints = contestRule.ruleValues.landingTest1RollingOutsidePoints
		toInstance.landingTest1PowerInBoxPoints = contestRule.ruleValues.landingTest1PowerInBoxPoints
		toInstance.landingTest1GoAroundWithoutTouchingPoints = contestRule.ruleValues.landingTest1GoAroundWithoutTouchingPoints
		toInstance.landingTest1GoAroundInsteadStopPoints = contestRule.ruleValues.landingTest1GoAroundInsteadStopPoints
		toInstance.landingTest1AbnormalLandingPoints = contestRule.ruleValues.landingTest1AbnormalLandingPoints
		toInstance.landingTest1PenaltyCalculator = contestRule.ruleValues.landingTest1PenaltyCalculator
	
		toInstance.landingTest2MaxPoints = contestRule.ruleValues.landingTest2MaxPoints
		toInstance.landingTest2NoLandingPoints = contestRule.ruleValues.landingTest2NoLandingPoints
		toInstance.landingTest2OutsideLandingPoints = contestRule.ruleValues.landingTest2OutsideLandingPoints
		toInstance.landingTest2RollingOutsidePoints = contestRule.ruleValues.landingTest2RollingOutsidePoints
		toInstance.landingTest2PowerInBoxPoints = contestRule.ruleValues.landingTest2PowerInBoxPoints
		toInstance.landingTest2GoAroundWithoutTouchingPoints = contestRule.ruleValues.landingTest2GoAroundWithoutTouchingPoints
		toInstance.landingTest2GoAroundInsteadStopPoints = contestRule.ruleValues.landingTest2GoAroundInsteadStopPoints
		toInstance.landingTest2AbnormalLandingPoints = contestRule.ruleValues.landingTest2AbnormalLandingPoints
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
		toInstance.landingTest4TouchingObstaclePoints = contestRule.ruleValues.landingTest4TouchingObstaclePoints
		toInstance.landingTest4PenaltyCalculator = contestRule.ruleValues.landingTest4PenaltyCalculator
	}
	
    //--------------------------------------------------------------------------
    Map copyContest(Map params, Contest lastContestInstance)
    {
        Contest contest_instance = new Contest(params)
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
        Contest contest_instance = Contest.get(params.id)
        
        if (contest_instance) {
            try {
            	Task.findAllByContest(contest_instance,[sort:"id"]).each { Task task_instance ->
            		task_instance.delete()
            	}
                contest_instance.delete()
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${contest_instance.title}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.contest'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.contest'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    void setaflosuploadedContest(Contest contestInstance)
    {
		contestInstance.aflosUpload = true
		contestInstance.save()
	}

    //--------------------------------------------------------------------------
    Map calculatecontestpositionsContest(Contest contestInstance, List contestClasses, List contestTasks)
    {
		printstart "calculatecontestpositionsContest"
		
		Map contest = [:]
		
		if (contestClasses && contestInstance.resultClasses) {
			println "Set contest result classes with $contestClasses"
			contestInstance.contestClassResults = ""
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(contestInstance,[sort:"id"])) {
				for (Map contest_class in contestClasses) {
					if (contest_class.instance == resultclass_instance) {
						if (contestInstance.contestClassResults) {
							contestInstance.contestClassResults += ","
						}
						contestInstance.contestClassResults += "resultclass_${resultclass_instance.id}"
					}
				}
			}
			contestInstance.save()
		}
		
		if (contestTasks) {
			println "Set contest result tasks with $contestTasks"
			contestInstance.contestTaskResults = ""
			for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
				for (Map contest_task in contestTasks) {
					if (contest_task.instance == task_instance) {
						if (contestInstance.contestTaskResults) {
							contestInstance.contestTaskResults += ","
						}
						contestInstance.contestTaskResults += "task_${task_instance.id}"
					}
				}
			}
			contestInstance.save()
		}
		
		if (contestInstance.resultClasses && !contestInstance.contestClassResults) {
			contest.message = getMsg('fc.contest.resultsettings.noclassselected')
			contest.error = true
			printerror contest.message
			return contest
		}
		if (!contestInstance.contestTaskResults) {
			contest.message = getMsg('fc.contest.resultsettings.notaskselected')
			contest.error = true
			printerror contest.message
			return contest
		}
		
		println "Calculate contest results with classes: '${GetResultClassNames(contestInstance, contestInstance.contestClassResults)}'"
		println "Calculate contest results with tasks: '${GetResultTaskNames(contestInstance, contestInstance.contestTaskResults)}'"
		
		calculate_crew_penalties(contestInstance,null,contestInstance.GetResultTasks(contestInstance.contestTaskResults),contestInstance.GetResultSettings(),ResultFilter.Contest)
		
		// calculate positions
       	calculatepositions_contest(contestInstance, null)
        
        contest.message = getMsg('fc.results.positionscalculated')
		printdone contest.message      
        return contest
    }
    
	//--------------------------------------------------------------------------
	private void calculate_crew_penalties(Contest contestInstance, ResultClass resultclassInstance, List tastSettings, Map resultSettings, ResultFilter resultFilter)
	{
		printstart "calculate_crew_penalties $resultFilter"
		for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
			if (!resultclassInstance || (crew_instance.resultclass == resultclassInstance)) {
				crew_instance.planningPenalties = 0
				crew_instance.flightPenalties = 0
				crew_instance.observationPenalties = 0
				crew_instance.landingPenalties = 0
				crew_instance.specialPenalties = 0
				switch (resultFilter) {
					case ResultFilter.Contest:
						crew_instance.contestPenalties = 0
						for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
							if (task_instance in tastSettings) {
								Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
								if (test_instance.IsPlanningTestRun()) {
									crew_instance.planningPenalties += test_instance.planningTestPenalties
									if (resultSettings["Planning"]) {
										crew_instance.contestPenalties += test_instance.planningTestPenalties
									}
								}
								if (test_instance.IsFlightTestRun()) {
									crew_instance.flightPenalties += test_instance.flightTestPenalties
									if (resultSettings["Flight"]) {
										crew_instance.contestPenalties += test_instance.flightTestPenalties
									}
								}
								if (test_instance.IsObservationTestRun()) {
									crew_instance.observationPenalties += test_instance.observationTestPenalties
									if (resultSettings["Observation"]) {
										crew_instance.contestPenalties += test_instance.observationTestPenalties
									}
								}
								if (test_instance.IsLandingTestRun()) {
									crew_instance.landingPenalties += test_instance.landingTestPenalties
									if (resultSettings["Landing"]) {
										crew_instance.contestPenalties += test_instance.landingTestPenalties
									}
								}
								if (test_instance.IsSpecialTestRun()) {
									crew_instance.specialPenalties += test_instance.specialTestPenalties
									if (resultSettings["Special"]) {
										crew_instance.contestPenalties += test_instance.specialTestPenalties
									}
								}
							}
						}
						println "$crew_instance.name (${crew_instance.resultclass?.name}): contestPenalties = $crew_instance.contestPenalties"
						break
					case ResultFilter.Team:
						if (crew_instance.IsActiveCrew(ResultFilter.Team)) { 
							crew_instance.teamPenalties = 0
							for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
								if (task_instance in tastSettings) {
									Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
									if (test_instance.IsPlanningTestRun()) {
										crew_instance.planningPenalties += test_instance.planningTestPenalties
										if (resultSettings["Planning"]) {
											crew_instance.teamPenalties += test_instance.planningTestPenalties
										}
									}
									if (test_instance.IsFlightTestRun()) {
										crew_instance.flightPenalties += test_instance.flightTestPenalties
										if (resultSettings["Flight"]) {
											crew_instance.teamPenalties += test_instance.flightTestPenalties
										}
									}
									if (test_instance.IsObservationTestRun()) {
										crew_instance.observationPenalties += test_instance.observationTestPenalties
										if (resultSettings["Observation"]) {
											crew_instance.teamPenalties += test_instance.observationTestPenalties
										}
									}
									if (test_instance.IsLandingTestRun()) {
										crew_instance.landingPenalties += test_instance.landingTestPenalties
										if (resultSettings["Landing"]) {
											crew_instance.teamPenalties += test_instance.landingTestPenalties
										}
									}
									if (test_instance.IsSpecialTestRun()) {
										crew_instance.specialPenalties += test_instance.specialTestPenalties
										if (resultSettings["Special"]) {
											crew_instance.teamPenalties += test_instance.specialTestPenalties
										}
									}
								}
							}
						} else {
							crew_instance.teamPenalties = 100000
						}
						println "$crew_instance.name (${crew_instance.resultclass?.name}): teamPenalties = $crew_instance.teamPenalties"
						break
				}
				crew_instance.save()
			}
		}
		printdone ""
	}
	
	//--------------------------------------------------------------------------
	private void calculatepositions_contest(Contest contestInstance, ResultClass resultclassInstance)
	{
		printstart "calculatepositions_contest ${resultclassInstance?.name}"
		
        int act_penalty = -1
        int max_position = Crew.countByContest(contestInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
				if (!crew_instance.disabled) {
					if (resultclassInstance) {
						if (crew_instance.resultclass) {
							if (resultclassInstance.id == crew_instance.resultclass.id) { // BUG: direkter Klassen-Vergleich geht nicht
				                if (crew_instance.contestPenalties > act_penalty) {
				                    if (crew_instance.contestPenalties < min_penalty) {
				                        min_penalty = crew_instance.contestPenalties 
				                    }
				                }
							}
						}
					} else if (contestInstance.resultClasses) {
						if (crew_instance.IsActiveCrew(ResultFilter.Contest)) {
			                if (crew_instance.contestPenalties > act_penalty) {
			                    if (crew_instance.contestPenalties < min_penalty) {
			                        min_penalty = crew_instance.contestPenalties 
			                    }
			                }
						}
					} else {
		                if (crew_instance.contestPenalties > act_penalty) {
		                    if (crew_instance.contestPenalties < min_penalty) {
		                        min_penalty = crew_instance.contestPenalties 
		                    }
		                }
					}
				}
            }
            act_penalty = min_penalty 

            // set position
            int set_position = -1
            for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
				if (crew_instance.disabled) {
					if (resultclassInstance) {
						crew_instance.classPosition = 0
						//println "$crew_instance.name: classPosition = 0 (disabled)"
					} else {
						crew_instance.contestPosition = 0
						crew_instance.noContestPosition = false
						//println "$crew_instance.name: contestPosition = 0 (disabled)"
					}
					crew_instance.save()
				} else if (resultclassInstance && !crew_instance.resultclass) {
					if (resultclassInstance) {
						crew_instance.classPosition = 0
						//println "$crew_instance.name: classPosition = 0 (no assigned class)"
					} else {
						crew_instance.contestPosition = 0
						crew_instance.noContestPosition = false
						//println "$crew_instance.name: contestPosition = 0 (no assigned class)"
					}
					crew_instance.save()
				} else if (contestInstance.resultClasses && !resultclassInstance && !crew_instance.IsActiveCrew(ResultFilter.Contest)) {
					crew_instance.contestPosition = 0
					if (!crew_instance.noContestPosition) {
						println "$crew_instance.name ($crew_instance.resultclass.name): no contest position"
					}
					crew_instance.noContestPosition = true
					crew_instance.save()
				} else if (!resultclassInstance || (resultclassInstance.id == crew_instance.resultclass.id)) { // BUG: direkter Klassen-Vergleich geht nicht
	                if (crew_instance.contestPenalties == act_penalty) {
						if (resultclassInstance) {
	                    	crew_instance.classPosition = act_position
							println "$crew_instance.name ($crew_instance.resultclass.name): classPosition = $act_position ($crew_instance.contestPenalties Punkte)"
						} else if (contestInstance.resultClasses) {
	                    	crew_instance.contestPosition = act_position
							crew_instance.noContestPosition = false
							println "$crew_instance.name ($crew_instance.resultclass.name): contestPosition = $act_position ($crew_instance.contestPenalties Punkte)"
						} else {
	                    	crew_instance.contestPosition = act_position
							crew_instance.noContestPosition = false
							println "$crew_instance.name: contestPosition = $act_position ($crew_instance.contestPenalties Punkte)"
						}
	                    crew_instance.save()
	                    set_position++
	                }
				}
            }
			
            if (set_position > 0) {
                act_position += set_position
            }
        }
		
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map calculateteampositionsContest(Contest contestInstance, List teamClasses, List teamTasks)
    {
		printstart "calculateteampositionsContest"
		
		Map contest = [:]
		
		if (teamClasses && contestInstance.resultClasses) {
			println "Set team result classes with $teamClasses"
			contestInstance.teamClassResults = ""
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(contestInstance,[sort:"id"])) {
				for (Map team_class in teamClasses) {
					if (team_class.instance == resultclass_instance) {
						if (contestInstance.teamClassResults) {
							contestInstance.teamClassResults += ","
						}
						contestInstance.teamClassResults += "resultclass_${resultclass_instance.id}"
					}
				}
			}
			contestInstance.save()
		}
		
		if (teamTasks) {
			println "Set team result tasks with $teamTasks"
			contestInstance.teamTaskResults = ""
			for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
				for (Map team_task in teamTasks) {
					if (team_task.instance == task_instance) {
						if (contestInstance.teamTaskResults) {
							contestInstance.teamTaskResults += ","
						}
						contestInstance.teamTaskResults += "task_${task_instance.id}"
					}
				}
			}
			contestInstance.save()
		}
		
		if (contestInstance.resultClasses && !contestInstance.teamClassResults) {
			contest.message = getMsg('fc.contest.teamresultsettings.noclassselected')
			contest.error = true
			printerror contest.message
			return contest
		}
		if (!contestInstance.teamTaskResults) {
			contest.message = getMsg('fc.contest.teamresultsettings.notaskselected')
			contest.error = true
			printerror contest.message
			return contest
		}
		
		println "Calculate team results with classes: '${GetResultClassNames(contestInstance, contestInstance.teamClassResults)}'"
		println "Calculate team results with tasks: '${GetResultTaskNames(contestInstance, contestInstance.teamTaskResults)}'"
		
		Map team_result_settings = contestInstance.GetTeamResultSettings()
		calculate_crew_penalties(contestInstance,null,contestInstance.GetResultTasks(contestInstance.teamTaskResults),team_result_settings,ResultFilter.Team)

		// calculate team penalties
		for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
			printstart team_instance.name
			team_instance.contestPenalties = 0
			int crew_num = 0
			for (Crew crew_instance in Crew.findAllByTeamAndDisabled(team_instance,false,[sort:"teamPenalties"])) {
				if (crew_instance.IsActiveCrew(ResultFilter.Team)) {
					crew_num++
					if (crew_num > contestInstance.teamCrewNum) {
						break
					}
					if (team_result_settings["Planning"]) {
						team_instance.contestPenalties += crew_instance.planningPenalties
					}
					if (team_result_settings["Flight"]) {
						team_instance.contestPenalties += crew_instance.flightPenalties
					}
					if (team_result_settings["Observation"]) {
						team_instance.contestPenalties += crew_instance.observationPenalties
					}
					if (team_result_settings["Landing"]) {
						team_instance.contestPenalties += crew_instance.landingPenalties
					}
					if (team_result_settings["Special"]) {
						team_instance.contestPenalties += crew_instance.specialPenalties
					}
					println "$crew_instance.name (${crew_instance.resultclass?.name}): $crew_instance.teamPenalties"
				}
			}
			team_instance.save()
			printdone "$team_instance.contestPenalties"
		}
		
		// calculate team positions
		calculatepositions_team(contestInstance)
        
        contest.message = getMsg('fc.results.positionscalculated')
		printdone contest.message      
        return contest
    }
    
	//--------------------------------------------------------------------------
	private String GetResultClassNames(Contest contestInstance, String resultClassIDs)
	{
		String s = ""
		if (contestInstance.resultClasses) {
			String resultclass_ids = "$resultClassIDs,"
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(contestInstance,[sort:"id"])) {
				if (resultclass_ids.contains("resultclass_${resultclass_instance.id},")) {
					if (s) {
						s += ","
					}
					s += resultclass_instance.name
				}
			}
		}
		return s
	}
	
	//--------------------------------------------------------------------------
	private String GetResultTaskNames(Contest contestInstance, String resultTaskIDs)
	{
		String s = ""
		String task_ids = "$resultTaskIDs,"
		for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
			if (task_ids.contains("task_${task_instance.id},")) {
				if (s) {
					s += ","
				}
				s += task_instance.name()
			}
		}
		return s
	}
	
	//--------------------------------------------------------------------------
	private void calculatepositions_team(Contest contestInstance)
	{
		printstart "calculatepositions_team"
		
        int act_penalty = -1
        int max_position = Team.countByContest(contestInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
				if (team_instance.IsActiveTeam()) {
	                if (team_instance.contestPenalties > act_penalty) {
	                    if (team_instance.contestPenalties < min_penalty) {
	                        min_penalty = team_instance.contestPenalties 
	                    }
	                }
				}
            }
            act_penalty = min_penalty 

            // set position
            int set_position = -1
            for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
				if (team_instance.IsActiveTeam()) {
	                if (team_instance.contestPenalties == act_penalty) {
	                    team_instance.contestPosition = act_position
						println "$team_instance.name: contestPosition = $act_position ($team_instance.contestPenalties Punkte)"
	                    team_instance.save()
	                    set_position++
	                }
				}
            }
            if (set_position > 0) {
                act_position += set_position
            }
        }
		
		printdone ""
	}
	
	//--------------------------------------------------------------------------
    Map printresultsContest(Contest contestInstance, printparams)
    {
		Map contest = [:]
		
        // Positions calculated?
        boolean call_return = false
        Crew.findAllByContest(contestInstance,[sort:"id"]).each { Crew crew_instance ->
            if (crew_instance.disabled || crew_instance.noContestPosition) {
				if (crew_instance.contestPosition) {
					call_return = true
				}
            } else {
				if (!crew_instance.contestPosition) {
					call_return = true
				}
            }
        }
        if (call_return) {
            contest.message = getMsg('fc.results.positions2calculate')
            contest.error = true
            return contest
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/listresultsprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content 
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
	//--------------------------------------------------------------------------
    Map printpointsContest(Contest contestInstance, printparams)
    {
		Map contest = [:]
		
        // Print points
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/pointsprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content 
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
	//--------------------------------------------------------------------------
    Map printteamresultsContest(Contest contestInstance, printparams)
    {
		Map contest = [:]
		
        // Positions calculated?
        boolean call_return = false
        for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
			if (team_instance.IsActiveTeam()) {
				if (!team_instance.contestPosition) {
					call_return = true
				}
			}
        }
        if (call_return) {
            contest.message = getMsg('fc.results.positions2calculate')
            contest.error = true
            return contest
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/contest/listteamresultsprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            contest.content = content 
        }
        catch (Throwable e) {
            contest.message = getMsg('fc.print.error',["$e"])
            contest.error = true
        }
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map getTask(Map params)
    {
        Task task_instance = Task.get(params.id)

        if (!task_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
        
        return ['instance':task_instance]
    }

    //--------------------------------------------------------------------------
    Map gettimetableprintableTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        if (!task_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
		
		println "gettimetableprintableTask (${task_instance.name()})"
		
		// Calculate timetable version
		if (task_instance.timetableModified) {
			task_instance.timetableVersion++
			task_instance.timetableModified = false
			task_instance.save()
			println "gettimetableprintableTask: timetableVersion $task_instance.timetableVersion of '${task_instance.name()}' saved."
		}

        return ['instance':task_instance]
    }

    //--------------------------------------------------------------------------
    Map updateTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':task_instance]
                }
            }
            
			boolean calculate_penalties = false
			if (!task_instance.contest.resultClasses) {
				if (is_modified(params.planningTestRun,task_instance.planningTestRun)) {
					calculate_penalties = true
				}
				if (is_modified(params.flightTestRun,task_instance.flightTestRun)) {
					calculate_penalties = true
				}
				if (is_modified(params.observationTestRun,task_instance.observationTestRun)) {
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
				if (is_modified(params.specialTestRun,task_instance.specialTestRun)) {
					calculate_penalties = true
				}			
			}
			
            task_instance.properties = params
			
			// save TaskClasses
			if (task_instance.contest.resultClasses) {
				for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance,[sort:"id"])) {
					if (params["taskclass_${taskclass_instance.resultclass.id}_planningTestRun"]) {
						taskclass_instance.planningTestRun = true
					} else {
						taskclass_instance.planningTestRun = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_flightTestRun"]) {
						taskclass_instance.flightTestRun = true
					} else {
						taskclass_instance.flightTestRun = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_observationTestRun"]) {
						taskclass_instance.observationTestRun = true
					} else {
						taskclass_instance.observationTestRun = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_landingTestRun"]) {
						taskclass_instance.landingTestRun = true
					} else {
						taskclass_instance.landingTestRun = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_landingTest1Run"]) {
						taskclass_instance.landingTest1Run = true
					} else {
						taskclass_instance.landingTest1Run = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_landingTest2Run"]) {
						taskclass_instance.landingTest2Run = true
					} else {
						taskclass_instance.landingTest2Run = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_landingTest3Run"]) {
						taskclass_instance.landingTest3Run = true
					} else {
						taskclass_instance.landingTest3Run = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_landingTest4Run"]) {
						taskclass_instance.landingTest4Run = true
					} else {
						taskclass_instance.landingTest4Run = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_specialTestRun"]) {
						taskclass_instance.specialTestRun = true
					} else { 
						taskclass_instance.specialTestRun = false
					}
					if (taskclass_instance.isDirty()) {
						calculate_penalties = true
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_planningTestDistanceMeasure"]) {
						taskclass_instance.planningTestDistanceMeasure = true
					} else { 
						taskclass_instance.planningTestDistanceMeasure = false
					}
					if (params["taskclass_${taskclass_instance.resultclass.id}_planningTestDirectionMeasure"]) {
						taskclass_instance.planningTestDirectionMeasure = true
					} else { 
						taskclass_instance.planningTestDirectionMeasure = false
					}
					taskclass_instance.save()
				}
			}
			if (calculate_penalties) {
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
					calculateTestPenalties(test_instance,false)
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
				
				
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
                return ['instance':task_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
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
    Map createTask(Map params)
    {
        Task task_instance = new Task()
        task_instance.properties = params
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
					if (params["taskclass_${resultclass_instance.id}_planningTestRun"]) {
						taskclass_instance.planningTestRun = true
					} else {
						taskclass_instance.planningTestRun = false
					}
					if (params["taskclass_${resultclass_instance.id}_flightTestRun"]) {
						taskclass_instance.flightTestRun = true
					} else {
						taskclass_instance.flightTestRun = false
					}
					if (params["taskclass_${resultclass_instance.id}_observationTestRun"]) {
						taskclass_instance.observationTestRun = true
					} else {
						taskclass_instance.observationTestRun = false
					}
					if (params["taskclass_${resultclass_instance.id}_landingTestRun"]) {
						taskclass_instance.landingTestRun = true
					} else {
						taskclass_instance.landingTestRun = false
					}
					if (params["taskclass_${resultclass_instance.id}_landingTest1Run"]) {
						taskclass_instance.landingTest1Run = true
					} else {
						taskclass_instance.landingTest1Run = false
					}
					if (params["taskclass_${resultclass_instance.id}_landingTest2Run"]) {
						taskclass_instance.landingTest2Run = true
					} else {
						taskclass_instance.landingTest2Run = false
					}
					if (params["taskclass_${resultclass_instance.id}_landingTest3Run"]) {
						taskclass_instance.landingTest3Run = true
					} else {
						taskclass_instance.landingTest3Run = false
					}
					if (params["taskclass_${resultclass_instance.id}_landingTest4Run"]) {
						taskclass_instance.landingTest4Run = true
					} else {
						taskclass_instance.landingTest4Run = false
					}
					if (params["taskclass_${resultclass_instance.id}_specialTestRun"]) {
						taskclass_instance.specialTestRun = true
					} else { 
						taskclass_instance.specialTestRun = false
					}
					if (params["taskclass_${resultclass_instance.id}_planningTestDistanceMeasure"]) {
						taskclass_instance.planningTestDistanceMeasure = true
					} else { 
						taskclass_instance.planningTestDistanceMeasure = false
					}
					if (params["taskclass_${resultclass_instance.id}_planningTestDirectionMeasure"]) {
						taskclass_instance.planningTestDirectionMeasure = true
					} else { 
						taskclass_instance.planningTestDirectionMeasure = false
					}
					taskclass_instance.save()
				}
			}
            Crew.findAllByContest(task_instance.contest,[sort:"viewpos"]).eachWithIndex { Crew crew_instance, int i ->
                Test test_instance = new Test()
                test_instance.crew = crew_instance
				test_instance.taskTAS = crew_instance.tas
                test_instance.viewpos = i
                test_instance.task = task_instance
                test_instance.timeCalculated = false
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
                Task.findAllByContest(task_instance.contest,[sort:"id"]).eachWithIndex { Task task_instance2, int index -> 
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
    Map savedisabledcheckpointsTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':task_instance]
                }
            }
            
			String last_disabledcheckpoints = task_instance.disabledCheckPoints
            task_instance.properties = params
			
			task_instance.disabledCheckPoints = ""
			CoordRoute.findAllByRoute(task_instance.flighttest.route,[sort:"id"]).each { CoordRoute coordroute_instance ->
				if (params.(coordroute_instance.title()) == "on") {
					if (task_instance.disabledCheckPoints) {
						task_instance.disabledCheckPoints += ",${coordroute_instance.title()}"
					} else {
						task_instance.disabledCheckPoints = coordroute_instance.title() 
					}
				}
			}
			
			boolean modify_flighttest_results = last_disabledcheckpoints != task_instance.disabledCheckPoints
			if (modify_flighttest_results) {
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
					CoordResult.findAllByTest(test_instance,[sort:"id"]).each { CoordResult coordresult_instance ->
						calculateCoordResultInstancePenaltyCoord(coordresult_instance)
						coordresult_instance.save()
					}
					calculateTestPenalties(test_instance,false)
					test_instance.flightTestModified = true
					test_instance.crewResultsModified = true
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
                return ['instance':task_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map resetdisabledcheckpointsTask(Map params)
    {
        Task task_instance = Task.get(params.id)
        
        if (task_instance) {
            
            if(params.version) {
                long version = params.version.toLong()
                if(task_instance.version > version) {
                    task_instance.errors.rejectValue("version", "task.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':task_instance]
                }
            }
            
			String last_disabledcheckpoints = task_instance.disabledCheckPoints
            task_instance.disabledCheckPoints = ""
			
			boolean modify_flighttest_results = last_disabledcheckpoints != task_instance.disabledCheckPoints
			if (modify_flighttest_results) {
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
					CoordResult.findAllByTest(test_instance,[sort:"id"]).each { CoordResult coordresult_instance ->
						calculateCoordResultInstancePenaltyCoord(coordresult_instance)
						coordresult_instance.save()
					}
					calculateTestPenalties(test_instance,false)
					test_instance.flightTestModified = true
					test_instance.crewResultsModified = true
		            test_instance.save()
		        }
			}	
			
            if(!task_instance.hasErrors() && task_instance.save()) {
                return ['instance':task_instance,'saved':true,'message':getMsg('fc.updated',["${task_instance.name()}"])]
            } else {
                return ['instance':task_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startplanningTask(Map params,contestInstance,lastTaskPlanning)
    {
    	Task task_instance = null
        if (lastTaskPlanning) {
            task_instance = Task.findById(lastTaskPlanning)
        }
        if (!task_instance) {
			if (contestInstance) {
	        	Task.findAllByContest(contestInstance,[sort:"id"]).each {
	        		if (!task_instance) {
	        			task_instance = it
	        		}
	        	}
			}
        }
        if (task_instance) {
            return ['taskid':task_instance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map startresultsTask(Map params,contestInstance,lastTaskResults)
    {
        Task task_instance = null
        if (lastTaskResults) {
            task_instance = Task.findById(lastTaskResults)
        }
        if (!task_instance) {
			if (contestInstance) {
	            Task.findAllByContest(contestInstance,[sort:"id"]).each {
	                if (!task_instance) {
	                    task_instance = it
	                }
	            }
			}
        }
        if (task_instance) {
            return ['taskid':task_instance.id]
        }
    }
    
    //--------------------------------------------------------------------------
    Map selectallTask(Map params)
    {
        Map task = getTask(params) 
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
        Map task = getTask(params) 
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
		
        Map task = getTask(params) 
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

        // Multiple PlanningTestTasks?  
        if (PlanningTestTask.countByPlanningtest(task.instance.planningtest) > 1) {
            List test_instance_ids = [""]
            Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    test_instance_ids += test_instance.id.toString()
                }
            }
            task.testinstanceids = test_instance_ids
			printdone ""
            return task
        }

        // set single PlanningTestTask to all selected Tests
        PlanningTestTask planningtesttask_instance = PlanningTestTask.findByPlanningtest(task.instance.planningtest) 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
				if (!test_instance.crew.disabled) {
	                test_instance.planningtesttask = planningtesttask_instance
	                calulateTestLegPlannings(test_instance)
					test_instance.ResetPlanningTestResults()
					test_instance.CalculateTestPenalties()
	                test_instance.save()
				}
            }
        }
        
		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map setplanningtesttaskTask(Map params)
    {
        Map task = getTask(params) 
        if (task.instance) {
            PlanningTestTask planningtesttask_instance = PlanningTestTask.get(params.planningtesttask.id)
            params.testInstanceIDs.each { String test_id ->
                if (test_id) {
                    Test test_instance = Test.get(test_id)
					if (!test_instance.crew.disabled) {
	                    test_instance.planningtesttask = planningtesttask_instance 
	                    calulateTestLegPlannings(test_instance)
						test_instance.ResetPlanningTestResults()
						test_instance.CalculateTestPenalties()
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
		
        Map task = getTask(params) 
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

        // Multiple FlightTestWinds?  
        if (FlightTestWind.countByFlighttest(task.instance.flighttest) > 1) {
            List test_instance_ids = [""]
            Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
                if (params["selectedTestID${test_instance.id}"] == "on") {
                    test_instance_ids += test_instance.id.toString()
                }
            }
            task.testinstanceids = test_instance_ids
			printdone ""
            return task
        }

        // set single FlightTestWind to all selected Tests
        FlightTestWind flighttestwind_instance = FlightTestWind.findByFlighttest(task.instance.flighttest)
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
				if (!test_instance.crew.disabled) {
					setflighttestwindTest(test_instance, task.instance, flighttestwind_instance)
				}
            }
        }

		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map setflighttestwindTask(Map params)
    {
		printstart "setflighttestwindTask"
        Map task = getTask(params) 
        if (task.instance) {
            FlightTestWind flighttestwind_instance = FlightTestWind.get(params.flighttestwind.id)
            params.testInstanceIDs.each { String test_id ->
                if (test_id) {
                    Test test_instance = Test.get(test_id)
					if (!test_instance.crew.disabled) {
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
		calulateTestLegFlight(testInstance)
        if (testInstance.timeCalculated) {
			GregorianCalendar testing_time = new GregorianCalendar()
			testing_time.setTime(testInstance.testingTime)
			calculateTime(testInstance, taskInstance, testing_time, null)
			calulateCoordResult(testInstance)
			taskInstance.timetableModified = true
			taskInstance.save()
        }
        testInstance.save()
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    Map calculatesequenceTask(Map params)
    {
		printstart "calculatesequenceTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

        /*
        // Have all crews an aircraft?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.crew.aircraft) {
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
			if (!test_instance.crew.disabled) {
	        	if (test_instance.crew.aircraft) {
	        		if (test_instance.crew.aircraft.user1 == test_instance.crew) {
	        			test_instance.viewpos = 4000+test_instance.taskTAS
	        		}
	        	}
			}
        }

        // set viewpos for aircraft of user2 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (test_instance.crew.aircraft) {
	                if (test_instance.crew.aircraft.user2 == test_instance.crew) {
	                    test_instance.viewpos = 3000+test_instance.taskTAS
	                }
	            }
			}
        }

        // set viewpos for user without aircraft 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.crew.aircraft) {
	                test_instance.viewpos = 2000+test_instance.taskTAS
	            }
			}
        }

        // set viewpos for disabled user 
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (test_instance.crew.disabled) {
                test_instance.viewpos = 1000+test_instance.taskTAS
			}
        }

        // set viewpos
        Test.findAllByTask(task.instance,[sort:"viewpos",order:"desc"]).eachWithIndex { Test test_instance, int i ->
            test_instance.viewpos = i
            test_instance.timeCalculated = false
			test_instance.ResetFlightTestResults()
			test_instance.CalculateTestPenalties()
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
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

    	// set viewpos with crew viewpos
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
   			test_instance.viewpos = test_instance.crew.viewpos
            test_instance.timeCalculated = false
			test_instance.ResetFlightTestResults()
			test_instance.CalculateTestPenalties()
            test_instance.save()
        }

        task.message = getMsg('fc.test.sequence.reset')    
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map moveupTask(Map params,session)
    {
        Map task = getTask(params) 
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
                    test_instance.timeCalculated = false
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
        Map task = getTask(params) 
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
        Map task = getTask(params) 
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
    Map calculatetimetableTask(Map params)
    {
        printstart "calculatetimetableTask"
		
        Map task = getTask(params) 
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
			if (!test_instance.crew.disabled) {
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
            if (!test_instance.crew.aircraft) {
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

        calulateTestLegFlights(task.instance)
        int crew_num = calulateTimetable(task.instance)
        
        task.message = getMsg('fc.test.timetable.calculated',[crew_num])   
		printdone task.message    
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timeaddTask(Map params)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        Map selected_testids = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                addTestingTime(task.instance,test_instance)
                selected_testids["selectedTestID${test_instance.id}"] = "on"
            }
        }
        task.selectedtestids = selected_testids
        return task
    }
    
    //--------------------------------------------------------------------------
    Map timesubtractTask(Map params)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        Map selected_testids = [selectedTestID:""]
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                subtractTestingTime(task.instance,test_instance)
                selected_testids["selectedTestID${test_instance.id}"] = "on"
            }
        }
        task.selectedtestids = selected_testids
           return task
    }
    
    //--------------------------------------------------------------------------
    Map printtimetableTask(Map params,printparams, boolean isJury)
    {
        Map task = getTask(params) 
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
			if (!test_instance.crew.disabled) {
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
            if (!test_instance.crew.aircraft) {
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
			if (!test_instance.crew.disabled) {
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
			if (!test_instance.crew.disabled) {
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
        
        // Print timetable
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
			String url = ""
			if (isJury) {
				url = "${printparams.baseuri}/task/timetablejuryprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
			} else {
				url = "${printparams.baseuri}/task/timetableprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
			}
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printflightplansTask(Map params,printparams)
    {
		printstart "printflightplansTask"
		
        Map task = getTask(params) 
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
        
        // FlightTestWind assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
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

        // Have all crews an aircraft?
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
            if (!test_instance.crew.aircraft) {
                call_return = true
            }
        }
        if (call_return) {
            task.message = getMsg('fc.aircraft.notassigned')
            task.error = true
			printerror task.message
            return task
        }

        // Timetable calculated?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.timeCalculated) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.test.timetable.newcalculate')
            task.error = true
			printerror task.message
            return task
        }        
        
        // Warnings?  
        call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (test_instance.arrivalTimeWarning || test_instance.takeoffTimeWarning) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.test.flightplan.resolvewarnings')
            task.error = true
			printerror task.message
            return task
        }        
        
		// Someone selected?
		boolean someone_selected = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
				if (!test_instance.crew.disabled) {
					someone_selected = true
				}
            }
        }
		if (!someone_selected) {
            task.message = getMsg('fc.test.flightplan.someonemustselected')
            task.error = true
			printerror task.message
            return task
		}
		
        // Print flightplans
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
	            if (params["selectedTestID${test_instance.id}"] == "on") {
					if (!test_instance.crew.disabled) {
		                String url = "${printparams.baseuri}/test/flightplanprintable/${test_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
		                println "Print: $url"
		                renderer.setDocument(url)
		                renderer.layout()
		                if (first_pdf) {
		                    renderer.createPDF(content,false)
		                    first_pdf = false
		                } else {
		                    renderer.writeNextDocument(1)
		                }
					}
	            }
            }
            renderer.finishPDF()

            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.test.flightplan.printerror',["$e"])
            task.error = true
			printerror task.message
        }
		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtasksTask(Map params,printparams)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // PlanningTest exists?
        if (!task.instance.planningtest) {
            task.message = getMsg('fc.planningtest.notfound')
            task.error = true
            return task
        }

        // PlanningTestTask exists?
        if (!PlanningTestTask.countByPlanningtest(task.instance.planningtest)) {
            task.message = getMsg('fc.planningtesttask.notfound')
               task.error = true
            return task
        }

        // PlanningTestTask assigned to all crews?
        boolean call_return = false
        Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
	            if (!test_instance.planningtesttask) {
	                call_return = true
	            }
			}
        }
        if (call_return) {
            task.message = getMsg('fc.planningtesttask.notassigned')
            task.error = true
            return task
        }

		// Someone selected?
		boolean someone_selected = false
        Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
				if (!test_instance.crew.disabled) {
					someone_selected = true
				}
            }
        }
		if (!someone_selected) {
            task.message = getMsg('fc.planningtesttask.someonemustselected')
            task.error = true
			printerror task.message
            return task
		}
		
        // Print PlanningTasks
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Test.findAllByTask(task.instance,[sort:"viewpos"]).each { Test test_instance ->
	            if (params["selectedTestID${test_instance.id}"] == "on") {
					if (!test_instance.crew.disabled) {
		                String url = "${printparams.baseuri}/test/planningtaskprintable/${test_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&results=no"
		                println "Print: $url"
		                renderer.setDocument(url)
		                renderer.layout()
		                if (first_pdf) {
		                    renderer.createPDF(content,false)
		                    first_pdf = false
		                } else {
		                    renderer.writeNextDocument(1)
		                }
					}
	            }
            }
            renderer.finishPDF()
            task.content = content
        }
        catch (Throwable e) {
            task.message = getMsg('fc.planningtesttask.printerror',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatepositionsTask(Map params)
    {
		printstart "calculatepositionsTask"
		
        Map task = getTask(params) 
        if (!task.instance) {
			printerror ""
            return task
        }

		// calculate positions
		if (task.instance.contest.resultClasses) {
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(task.instance.contest,[sort:"id"])) {
				calculatepositions_task(task.instance, resultclass_instance)
			}
		} else {
        	calculatepositions_task(task.instance, null)
		}
        
        task.message = getMsg('fc.results.positionscalculated')  
		printdone task.message      
        return task
    }
    
    //--------------------------------------------------------------------------
    private void calculatepositions_task(Task taskInstance, ResultClass resultclassInstance)
    {
        int act_penalty = -1
        int max_position = Test.countByTask(taskInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
				if (!test_instance.crew.disabled) {
					if ((resultclassInstance == null) || (resultclassInstance == test_instance.crew.resultclass)) {
		                if (test_instance.taskPenalties > act_penalty) {
		                    if (test_instance.taskPenalties < min_penalty) {
		                        min_penalty = test_instance.taskPenalties 
		                    }
		                }
					}
				}
            }
            act_penalty = min_penalty 
            
            // set position
            int set_position = -1
            for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
				if (test_instance.crew.disabled) {
                    test_instance.taskPosition = 0
                    test_instance.save()
				} else if (resultclassInstance && !test_instance.crew.resultclass) {
                    test_instance.taskPosition = 0
                    test_instance.save()
				} else if (!resultclassInstance || (resultclassInstance == test_instance.crew.resultclass)) {
	                if (test_instance.taskPenalties == act_penalty) {
	                    test_instance.taskPosition = act_position
	                    test_instance.save()
	                    set_position++
	                }
				}
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
	}
	
    //--------------------------------------------------------------------------
	Map positionscalculatedTask(Map params)
	{
		Map task = getTask(params)
		if (!task.instance) {
			return task
		}

		// Positions calculated?
		boolean call_return = false
		Test.findAllByTask(task.instance,[sort:"id"]).each { Test test_instance ->
			if (test_instance.crew.disabled) {
				if (test_instance.taskPosition) {
					call_return = true
				}
			} else {
				if (!test_instance.taskPosition) {
					call_return = true
				}
			}
		}
		if (call_return) {
			task.message = getMsg('fc.results.positions2calculate')
			task.error = true
			return task
		}
		
		return task
	}

    //--------------------------------------------------------------------------
    Map printresultsTask(Map params,printparams)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // Print task results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/task/listresultsprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
        }
        return task
    }
    
    //--------------------------------------------------------------------------
    Map printresultclassresultsTask(Map params,printparams)
    {
        Map task = getTask(params) 
        if (!task.instance) {
            return task
        }

        // Print task results of selected result classes
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(task.instance.contest,[sort:"id"])) {
				if (params["resultclass_${resultclass_instance.id}"] == "on") {
		            String url = "${printparams.baseuri}/task/listresultsprintable/${task.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&resultclassid=${resultclass_instance.id}"
		            println "Print: $url"
		            renderer.setDocument(url)
		            renderer.layout()
					if (first_pdf) {
						renderer.createPDF(content,false)
		                first_pdf = false
		            } else {
		                renderer.writeNextDocument(1)
		            }
					task.found = true
				}
			}
            renderer.finishPDF()
			
            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.print.error',["$e"])
            task.error = true
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
            
            team_instance.properties = params

            if(!team_instance.hasErrors() && team_instance.save()) {
                return ['instance':team_instance,'saved':true,'message':getMsg('fc.updated',["${team_instance.name}"])]
            } else {
                return ['instance':team_instance]
            }
        } else {
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
                Crew.findAllByTeam(team_instance,[sort:"id"]).each { Crew crew_instance ->
                    crew_instance.team = null
                    crew_instance.save()
                }
                team_instance.delete()
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
    Map printTeams(Map params,printparams)
    {
        Map teams = [:]

        // Print teams
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/team/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            teams.content = content 
        }
        catch (Throwable e) {
            teams.message = getMsg('fc.team.printerror',["$e"])
            teams.error = true
        }
        return teams
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
    Map updateResultClass(Map params)
    {
        ResultClass resultclass_instance = ResultClass.get(params.id)
        if(resultclass_instance) {
            if(params.version) {
                long version = params.version.toLong()
                if(resultclass_instance.version > version) {
                    resultclass_instance.errors.rejectValue("version", "resultclass.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':resultclass_instance]
                }
            }

            if (params.name != resultclass_instance.name) {
                ResultClass resultclass_instance2 = ResultClass.findByNameAndContest(params.name,resultclass_instance.contest)
                if (resultclass_instance2) {
                    return ['instance':resultclass_instance2,'error':true,'message':getMsg('fc.resultclass.name.error',["${params.name}"])]
                }
            }
            
			ContestRules contest_rule = resultclass_instance.contestRule
			
			boolean contest_planning_results = resultclass_instance.contestPlanningResults
			boolean contest_flight_results = resultclass_instance.contestFlightResults
			boolean contest_observation_results = resultclass_instance.contestObservationResults
			boolean contest_landing_results = resultclass_instance.contestLandingResults
			boolean contest_special_results = resultclass_instance.contestSpecialResults
			String contest_task_results = resultclass_instance.contestTaskResults
			
            resultclass_instance.properties = params

			resultclass_instance.contestTaskResults = ""
			for (Task task_instance in resultclass_instance.contest.tasks) {
				if (params["task_${task_instance.id}"] == "on") {
					if (resultclass_instance.contestTaskResults) {
						resultclass_instance.contestTaskResults += ","
					}
					resultclass_instance.contestTaskResults += "task_${task_instance.id}"
				}
			}
					
			boolean modify_contest_rule = resultclass_instance.contestRule != contest_rule

			boolean modify_contest_results = (resultclass_instance.contestPlanningResults != contest_planning_results) ||
			                                 (resultclass_instance.contestFlightResults != contest_flight_results) ||
			 							     (resultclass_instance.contestObservationResults != contest_observation_results) ||
										     (resultclass_instance.contestLandingResults != contest_landing_results) ||
										     (resultclass_instance.contestSpecialResults != contest_special_results) ||
											 (resultclass_instance.contestTaskResults != contest_task_results)
										  
			if (modify_contest_rule) {
				println "Contest rule modfified."
				setContestRule(resultclass_instance, resultclass_instance.contestRule)
			}
			
			if (modify_contest_results) {
				println "Contest results modfified."
				for (Crew crew_instance in Crew.findAllByContest(resultclass_instance.contest,[sort:"id"])) {
					if (crew_instance.resultclass == resultclass_instance) {
						crew_instance.contestPenalties = 0
						crew_instance.classPosition = 0
						crew_instance.save()
					}
				}
			}
			
            if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
                return ['instance':resultclass_instance,'saved':true,'message':getMsg('fc.updated',["${resultclass_instance.name}"])]
            } else {
                return ['instance':resultclass_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.resultclass'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map calculatepointsResultClass(Map params)
    {
        ResultClass resultclass_instance = ResultClass.get(params.id)
        if(resultclass_instance) {
			
			// Alle Berechungen in Klasse erneut ausführen
			Task.findAllByContest(resultclass_instance.contest,[sort:"id"]).each { Task task_instance ->
		        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
					if (test_instance.crew.resultclass == resultclass_instance) {
						calculateTestPenalties(test_instance,true)
						test_instance.save()
					}
		        }
			}
			
            return ['instance':resultclass_instance,'message':getMsg('fc.contestrule.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.resultclass'),params.id])]
        }
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
		setContestRule(resultclass_instance, resultclass_instance.contestRule)
		
        if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
			// create TaskClasses
			if (contestInstance.resultClasses) {
				for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
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
    Map calculatepositionsResultClass(ResultClass resultclassInstance, List contestTasks)
    {
		printstart "calculatepositionsResultClass $resultclassInstance.name"
		
		Map resultclass = [:]
		
		if (contestTasks) {
			println "Set class result tasks with $contestTasks"
			resultclassInstance.contestTaskResults = ""
			for (Task task_instance in Task.findAllByContest(resultclassInstance.contest,[sort:"id"])) {
				for (Map contest_task in contestTasks) {
					if (contest_task.instance == task_instance) {
						if (resultclassInstance.contestTaskResults) {
							resultclassInstance.contestTaskResults += ","
						}
						resultclassInstance.contestTaskResults += "task_${task_instance.id}"
					}
				}
			}
			resultclassInstance.save()
		}
		
		if (!resultclassInstance.contestTaskResults) {
			resultclass.message = getMsg('fc.resultclass.resultsettings.notaskselected')
			resultclass.error = true
			printerror resultclass.message
			return resultclass
		}
		
		println "Calculate class results with tasks: '${GetResultTaskNames(resultclassInstance.contest, resultclassInstance.contestTaskResults)}'"
		
		calculate_crew_penalties(resultclassInstance.contest,resultclassInstance,resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults),resultclassInstance.GetClassResultSettings(),ResultFilter.Contest)
		
		// calculate positions
		calculatepositions_contest(resultclassInstance.contest, resultclassInstance)
        
        resultclass.message = getMsg('fc.results.positionscalculated')

		printdone resultclass.message      
        return resultclass
    }
    
	//--------------------------------------------------------------------------
    Map printresultsResultClass(ResultClass resultclassInstance, printparams)
    {
		println "printresultsResultClass"
		
		Map resultclass = [:]
		
        // Positions calculated? 
        boolean call_return = false
        Crew.findAllByContest(resultclassInstance.contest,[sort:"id"]).each { Crew crew_instance ->
            if (crew_instance.disabled) {
				if (crew_instance.classPosition) {
					call_return = true
				}
            } else if (crew_instance.resultclass == resultclassInstance) {
				if (!crew_instance.classPosition) {
					call_return = true
				}
            }
        }
        if (call_return) {
            resultclass.message = getMsg('fc.results.positions2calculate')
            resultclass.error = true
            return resultclass
        }
        
        // Print positions
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/resultClass/listresultsprintable/${resultclassInstance.id}?lang=${printparams.lang}&resultclassid=${resultclassInstance.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            resultclass.content = content 
        }
        catch (Throwable e) {
            resultclass.message = getMsg('fc.print.error',["$e"])
            resultclass.error = true
        }
        return resultclass
    }
    
	//--------------------------------------------------------------------------
    Map printpointsResultClass(ResultClass resultclassInstance, printparams)
    {
		println "printpointsResultClass"
		
		Map resultclass = [:]
		
        // Print points
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/resultClass/pointsprintable/${resultclassInstance.id}?lang=${printparams.lang}&resultclassid=${resultclassInstance.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            resultclass.content = content 
        }
        catch (Throwable e) {
            resultclass.message = getMsg('fc.print.error',["$e"])
            resultclass.error = true
        }
        return resultclass
    }
    
    //--------------------------------------------------------------------------
    Map printResultClasses(Map params,printparams)
    {
        Map resultclasses = [:]

        // Print resultclasses
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/resultClass/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            resultclasses.content = content 
        }
        catch (Throwable e) {
            resultclasses.message = getMsg('fc.resultclass.printerror',["$e"])
            resultclasses.error = true
        }
        return resultclasses
    }
	
    //--------------------------------------------------------------------------
    Map getRoute(Map params)
    {
        Route route_instance = Route.get(params.id)

        if(!route_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
        
        return ['instance':route_instance]
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
    Map createRoute(Map params)
    {
        Route route_instance = new Route()
        route_instance.properties = params
        return ['instance':route_instance]
    }
    
    //--------------------------------------------------------------------------
    Map saveRoute(Map params,Contest contestInstance)
    {
        Route route_instance = new Route(params)
        
        route_instance.contest = contestInstance
        route_instance.idTitle = Route.countByContest(contestInstance) + 1
        
        if(!route_instance.hasErrors() && route_instance.save()) {
            return ['instance':route_instance,'saved':true,'message':getMsg('fc.created',["${route_instance.name()}"])]
        } else {
            return ['instance':route_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        Contest contest_instance = route_instance.contest
        
        if (route_instance) {
            try {
                route_instance.delete()
                
                Route.findAllByContest(contest_instance,[sort:"id"]).eachWithIndex { Route route_instance2, int index -> 
                    route_instance2.idTitle = index + 1
                }
                
                return ['deleted':true,'message':getMsg('fc.deleted',["${route_instance.name()}"])]
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                return ['notdeleted':true,'message':getMsg('fc.notdeleted',[getMsg('fc.route'),params.id])]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map copyRoute(Map params)
    {
        Map route = getRoute(params)
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
		while (RouteTitleFound(new_title2, sourceRouteInstance.contest)) {
			found_num++
			new_title2 = "$new_title ($found_num)"
		}
		return new_title2
	}
	
    //--------------------------------------------------------------------------
	private boolean RouteTitleFound(String newTitle, Contest contestInstance)
	{
		for(Route route_instance in Route.findAllByContest(contestInstance,[sort:"id"])) {
		   if (route_instance.title == newTitle) {
			   return true
		   }
		}
		return false
	}
	
    //--------------------------------------------------------------------------
    Map printRoute(Map params,printparams)
    {
        Map route = getRoute(params)
        if (!route.instance) {
            return route
        }
        
        // Print route
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/route/showprintable/${route.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            route.content = content 
        }
        catch (Throwable e) {
            route.message = getMsg('fc.route.printerror',["$e"])
            route.error = true
        }
        return route
    }
    
    //--------------------------------------------------------------------------
    Map printRoutes(Map params,printparams)
    {
        Map routes = [:]

        // Print routes
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            Route.findAllByContest(printparams.contest,[sort:"id"]).each { Route route_instance ->
	            String url = "${printparams.baseuri}/route/showprintable/${route_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
	            println "Print: $url"
	            renderer.setDocument(url)
	            renderer.layout()
                if (first_pdf) {
                	renderer.createPDF(content,false)
                    first_pdf = false
                } else {
                    renderer.writeNextDocument(1)
                }
	            routes.found = true
            }
            renderer.finishPDF()
            
            routes.content = content 
        }
        catch (Throwable e) {
        	routes.message = getMsg('fc.route.printerror',["$e"])
            routes.error = true
        }
        return routes
    }
    
    //--------------------------------------------------------------------------
    Map caculateroutelegsRoute(Map params)
    {
        Route route_instance = Route.get(params.id)
        
        if (route_instance) {
			calculateAllCoordMapDistances(route_instance)
            calculateSecretLegRatio(route_instance)
        	calculateRouteLegs(route_instance)
            return ['instance':route_instance,'calculated':true,'message':getMsg('fc.routeleg.calculated')]
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
    }
    
    //--------------------------------------------------------------------------
    Map existAnyAflosRoute(Contest contestInstance)
    {
		if (contestInstance.aflosTest) {
	    	if (AflosRouteNames.aflostest.count() < 2) {
	            return ['error':true,'message':getMsg('fc.route.import.notfound')]
	    	}
		} else if (contestInstance.aflosUpload) {
	    	if (AflosRouteNames.aflosupload.count() < 2) {
	            return ['error':true,'message':getMsg('fc.route.import.notfound')]
	    	}
		} else {
	    	if (AflosRouteNames.aflos.count() < 2) {
	            return ['error':true,'message':getMsg('fc.route.import.notfound')]
	    	}
		}
    	return [:]
    }
    
    //--------------------------------------------------------------------------
    Map importAflosRoute(Map params, Contest contestInstance)
    {
        AflosRouteNames aflosroutenames_instance = null
		if (contestInstance.aflosTest) {
			printstart "importAflosRoute (aflostest)"
			aflosroutenames_instance = AflosRouteNames.aflostest.get(params.aflosroutenames.id)
		} else if (contestInstance.aflosUpload) {
			printstart "importAflosRoute (aflosupload)"
			aflosroutenames_instance = AflosRouteNames.aflosupload.get(params.aflosroutenames.id)
		} else {
			printstart "importAflosRoute (aflos)"
			aflosroutenames_instance = AflosRouteNames.aflos.get(params.aflosroutenames.id)
		}
    	if (aflosroutenames_instance) {
    	
    		Route route_instance = new Route()
	        
	        route_instance.contest = contestInstance
	        route_instance.idTitle = Route.countByContest(contestInstance) + 1
	        route_instance.title = aflosroutenames_instance.name
	        route_instance.mark = aflosroutenames_instance.name
	        
	        if(!route_instance.hasErrors() && route_instance.save()) {
				List aflosroutedefs_instances = null
				if (contestInstance.aflosTest) {
					aflosroutedefs_instances = AflosRouteDefs.aflostest.findAllByRoutename(aflosroutenames_instance,[sort:"id"])
				} else if (contestInstance.aflosUpload) {
					aflosroutedefs_instances = AflosRouteDefs.aflosupload.findAllByRoutename(aflosroutenames_instance,[sort:"id"])
				} else {
                	aflosroutedefs_instances = AflosRouteDefs.aflos.findAllByRoutename(aflosroutenames_instance,[sort:"id"])
				}
                CoordRoute last_coordroute_instance
                CoordRoute last_coordroute_test_instance
                int tpNum = 0
                int tcNum = 0
                aflosroutedefs_instances.each { AflosRouteDefs aflosroutedefs_instance ->
                	CoordRoute coordroute_instance = new CoordRoute()
                    
                    // set latitude
                    aflosroutedefs_instance.latitude.split().eachWithIndex{ String latValue, int i ->
                		switch(i) {
                			case 0: 
                				coordroute_instance.latDirection = latValue
                				break
                			case 1:
                				coordroute_instance.latGrad = latValue.toInteger()
                				break
                			case 2:
                				coordroute_instance.latMinute = latValue.replace(',','.').toBigDecimal()
                				break
                		}
                	}
                    
                    // set longitude
                    aflosroutedefs_instance.longitude.split().eachWithIndex{ String lonValue, int i ->
                        switch(i) {
                            case 0: 
                                coordroute_instance.lonDirection = lonValue
                                break
                            case 1:
                                coordroute_instance.lonGrad = lonValue.toInteger()
                                break
                            case 2:
                                coordroute_instance.lonMinute = lonValue.replace(',','.').toBigDecimal()
                                break
                        }
                    }
    
                    // set type and title
                    coordroute_instance.type = CoordType.UNKNOWN
                    CoordType.each { CoordType type ->
                    	if (coordroute_instance.type == CoordType.UNKNOWN) { // Typ der Koordinate noch nicht ermittelt
	                    	if (type != CoordType.UNKNOWN && aflosroutedefs_instance.mark && aflosroutedefs_instance.mark.startsWith(type.aflosMark)) {  
	                    		if (type.aflosGateWidth == 0 || (type.aflosGateWidth == aflosroutedefs_instance.gatewidth)) { // Übereinstimmung AFLOS-Kooordinate mit Typ
	                    			switch (type) {
	                    				case CoordType.SECRET:
	                    					if (params.aflosroutenames.secretcoordrouteidentification == SecretCoordRouteIdentification.NONE.toString()) {
	                    						coordroute_instance.type = CoordType.TP 
	                    					} else {
	                    						coordroute_instance.type = CoordType.SECRET 
	                    					}
                                            break
	                    				case CoordType.TP:
                                            coordroute_instance.type = CoordType.TP 
                                            break
                                        default:
                                        	coordroute_instance.type = type
                                        	break
	                    			}

	                    			switch (coordroute_instance.type) {
		                    			case CoordType.TP:
		                    				tpNum++
		                    				coordroute_instance.titleNumber = tpNum
		                    				break
		                    			case CoordType.SECRET:
		                    				tcNum++
	                                        coordroute_instance.titleNumber = tcNum
		                    				break
		                    		}
	                    		}
	                    	}
                    	}
                    }
                    
                    // set other
                    coordroute_instance.altitude = aflosroutedefs_instance.altitude.toInteger()
                    coordroute_instance.mark = aflosroutedefs_instance.mark
                    coordroute_instance.gatewidth = aflosroutedefs_instance.gatewidth
                    coordroute_instance.route = route_instance
                    
					// calculate coordTrueTrack/coordMeasureDistance
					if (last_coordroute_test_instance) {
						Map legdata_coord = calculateLegData(coordroute_instance, last_coordroute_test_instance)
						coordroute_instance.coordTrueTrack = legdata_coord.dir
						coordroute_instance.coordMeasureDistance = calculateMapMeasure(coordroute_instance.route.contest,legdata_coord.dis)
					}
					 
                    coordroute_instance.save()
                    
                    newLeg(coordroute_instance.route, coordroute_instance, last_coordroute_instance, last_coordroute_test_instance, null, null, null)
                    last_coordroute_instance = coordroute_instance
                    switch (coordroute_instance.type) {
	                    case CoordType.SP:
	                    case CoordType.TP:
	                    case CoordType.FP:
	                        last_coordroute_test_instance = coordroute_instance
	                        break
	                }
                }
                calculateSecretLegRatio(route_instance)
                Map ret = ['instance':route_instance,'saved':true,'message':getMsg('fc.imported',["${route_instance.name()}"])]
				printdone ret
				return ret
	        } else {
	            Map ret = ['error':true,'message':getMsg('fc.notimported',["${aflosroutenames_instance.name}"])]
				printerror ret.message
				return ret
	        }
        } else {
            Map ret = ['error':true,'message':getMsg('fc.notimported',["${params.aflosroutenames.id}"])]
			printerror ret.message
			return ret
    	}
    }
    
    //--------------------------------------------------------------------------
    Map existAnyAflosCrew(Contest contestInstance)
    {
		if (contestInstance.aflosTest) {
	    	if (AflosCrewNames.aflostest.count() > 0) {
	            return [:]
	        }
		} else if (contestInstance.aflosUpload) {
	    	if (AflosCrewNames.aflosupload.count() > 0) {
	            return [:]
	        }
		} else {
	    	if (AflosCrewNames.aflos.count() > 0) {
	            return [:]
	        }
		}
        return ['error':true,'message':getMsg('fc.aflos.points.notfound')]
    }
    
    //--------------------------------------------------------------------------
    Map importAflosResults(Map params)
    {
		Test test_instance = Test.get(params.id)
		int start_num = params.afloscrewnames.startnum.toInteger()
		return importAflosResults(test_instance, start_num)
	}
	
    //--------------------------------------------------------------------------
    private Map importAflosResults(Test testInstance, int startNum)
    {
        printstart "importAflosResults: crew '$testInstance.crew.name', startnum $startNum"
		
		Contest contest_instance = testInstance.crew.contest
		
		AflosCrewNames afloscrewnames_instance = null
		if (contest_instance.aflosTest) {
			afloscrewnames_instance = AflosCrewNames.aflostest.findByStartnumAndPointsNotEqual(startNum,0)
			//aflostest.
		} else if (contest_instance.aflosUpload) {
			afloscrewnames_instance = AflosCrewNames.aflosupload.findByStartnumAndPointsNotEqual(startNum,0)
			//aflosupload.
		} else {
			afloscrewnames_instance = AflosCrewNames.aflos.findByStartnumAndPointsNotEqual(startNum,0)
			//aflos.
		}
        if (!afloscrewnames_instance) {
        	Map ret = ['error':true,'message':getMsg('fc.aflos.points.crewnotfound',[startNum])]
			printerror ret.message
			return ret
        }
        AflosRouteNames aflosroutenames_instance = null
		if (contest_instance.aflosTest) {
			aflosroutenames_instance = AflosRouteNames.aflostest.findByName(testInstance.flighttestwind.flighttest.route.mark)
		} else if (contest_instance.aflosUpload) {
			aflosroutenames_instance = AflosRouteNames.aflosupload.findByName(testInstance.flighttestwind.flighttest.route.mark)
		} else {
			aflosroutenames_instance = AflosRouteNames.aflos.findByName(testInstance.flighttestwind.flighttest.route.mark)
		}
        if (!aflosroutenames_instance) {
        	Map ret = ['error':true,'message':getMsg('fc.aflos.points.routenotfound',[testInstance.flighttestwind.flighttest.route.mark])]
			printerror ret.message
			return ret
        }
        
        println "AFLOS crew: '${afloscrewnames_instance.viewName()}', AFLOS route: '$aflosroutenames_instance.name'"

        AflosErrors aflos_error = null
		if (contest_instance.aflosTest) {
			aflos_error = AflosErrors.aflostest.findByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance)
		} else if (contest_instance.aflosUpload) {
			aflos_error = AflosErrors.aflosupload.findByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance)
		} else {
			aflos_error = AflosErrors.aflos.findByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance)
		}
        if (!aflos_error) {
			Map ret = ['error':true,'message':getMsg('fc.aflos.points.notcomplete',[afloscrewnames_instance.viewName()])]
			printerror ret.message
			return ret
        }
		/*
        if (aflos_error.mark.contains("Check Error")) {
        	Map ret = ['error':true,'message':getMsg('fc.aflos.points.errors',[afloscrewnames_instance.viewName()])]
			printerror ret.message
			return ret
        }
        */

        try {
	        // Import AflosCheckPoints
            int checkpoint_errors = 0
            int height_errors = 0
	        CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
	        	boolean found = false
				List afloscheckpoints_instances = null
				if (contest_instance.aflosTest) {
					afloscheckpoints_instances = AflosCheckPoints.aflostest.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance,[sort:"id"])
				} else if (contest_instance.aflosUpload) {
					afloscheckpoints_instances = AflosCheckPoints.aflosupload.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance,[sort:"id"])
				} else {
					afloscheckpoints_instances = AflosCheckPoints.aflos.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance,[sort:"id"])
				}
	        	afloscheckpoints_instances.each { AflosCheckPoints afloscheckpoints_instance ->
	        		if (afloscheckpoints_instance.mark == "P${coordresult_instance.mark}") {
	        			// reset results
	        			coordresult_instance.ResetResults()
	        			
	        			// read utc '09h 36min 05,000sec'
	        			coordresult_instance.resultCpTimeInput = FcMath.ConvertAFLOSTime(afloscheckpoints_instance.utc) 
						
	        			// read latitude '51° 26,9035' N'
	        			coordresult_instance.resultLatitude = FcMath.ConvertAFLOSCoordValue(afloscheckpoints_instance.latitude)
 
	        			// read longitude '013° 51,7278' E'
	                    coordresult_instance.resultLongitude = FcMath.ConvertAFLOSCoordValue(afloscheckpoints_instance.longitude)
 
	        			// read altitude '01992 ft'
	        			coordresult_instance.resultAltitude = afloscheckpoints_instance.altitude.split()[0].toInteger()
	        			
	        			// calculate results
                        if (coordresult_instance.planProcedureTurn) {
                            coordresult_instance.resultProcedureTurnEntered = true
                        }
	        			calculateCoordResultInstance(coordresult_instance,true,false)
	        			
                        // calculate verify values 
                        if (coordresult_instance.resultMinAltitudeMissed) {
                            height_errors++
                        }

	        			// save results
	                    coordresult_instance.save()
	                    found = true
	                    
	                    // log
	                    if (coordresult_instance.planProcedureTurn) {
	                        println "Procedure turn"
	                    }
	                    println "Found AflosCheckPoint $afloscheckpoints_instance.mark UTC: $coordresult_instance.resultCpTimeInput Local: ${FcMath.TimeStr(coordresult_instance.resultCpTime)}"
	        		}
	        	}
	        	if (!found) {
	        		// reset results
                    coordresult_instance.ResetResults()
                    
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
                        println "Procedure turn"
                    }
                    println "Not found AflosCheckPoint $coordresult_instance.mark"
	        	}
	    	}
	    	
	    	// Import AflosErrorPoints
	    	int badcourse_num = 0
	    	int course_errors = 0
	    	String badcourse_starttimeutc
			List afloserrorpoints_instances = null
			if (contest_instance.aflosTest) {
				afloserrorpoints_instances = AflosErrorPoints.aflostest.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance,[sort:"id"])
			} else if (contest_instance.aflosUpload) {
				afloserrorpoints_instances = AflosErrorPoints.aflosupload.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance,[sort:"id"])
			} else {
				afloserrorpoints_instances = AflosErrorPoints.aflos.findAllByStartnumAndRoutename(afloscrewnames_instance.startnum,aflosroutenames_instance,[sort:"id"])
			}
	    	afloserrorpoints_instances.each { AflosErrorPoints afloserrorpoints_instance ->
	    		if (badcourse_num == 0) {
	    			if (afloserrorpoints_instance.mark.contains("-Bad Course")) {
		    			badcourse_num = 1
		    			badcourse_starttimeutc = afloserrorpoints_instance.utc
		    		}
	    		} else {
	    			if (!afloserrorpoints_instance.mark.trim()) {
	    				badcourse_num++
	    			} else {
	    				// process error point
	    				if (processAflosErrorPointBadCourse(testInstance, badcourse_num, badcourse_starttimeutc)) {
	    					course_errors++
	    				}
	    				
	    				// search for next error point 
	    				if (afloserrorpoints_instance.mark.contains("-Bad Course")) {
	    					badcourse_num = 1
	                        badcourse_starttimeutc = afloserrorpoints_instance.utc
	    				} else {
	    					badcourse_num = 0
	    					badcourse_starttimeutc = ""
	    				}
	    			}
	    		}
	    		if (afloserrorpoints_instance.mark.contains("-Bad Turn")) {
	    			processAflosErrorPointBadTurn(testInstance, afloserrorpoints_instance.utc)
	    		}
	    	}
	        if (badcourse_num > 0) {
	            // process error point
	        	if (processAflosErrorPointBadCourse(testInstance, badcourse_num, badcourse_starttimeutc)) {
	        		course_errors++
	        	}
	        }
	        
	    	// Penalties berechnen
	        calculateTestPenalties(testInstance,false)
	        testInstance.save()
	        
	        // save imported crew
	        testInstance.crew.mark = afloscrewnames_instance.viewName()
	        testInstance.crew.save()
	        
	        if (aflos_error.mark == "Flight O.K.") {
	        	Map ret = ['saved':true,'message':getMsg('fc.aflos.points.imported',[afloscrewnames_instance.viewName()])]
				printdone ret.message
				return ret
	        } else {
	        	if (aflos_error.dropOutErrors == 0 && 
	        		checkpoint_errors == aflos_error.checkPointErrors && 
	        		height_errors == aflos_error.heightErrors && 
	        		course_errors == aflos_error.courseErrors)
	        	{
	        		Map ret = ['saved':true,'message':getMsg('fc.aflos.points.imported.naverrors',[afloscrewnames_instance.viewName()])]
					printdone ret.message
					return ret
	        	} else {
	        		if (checkpoint_errors != aflos_error.checkPointErrors) {
	        			println "Evaluation error: $checkpoint_errors bad checkpoints <> $aflos_error.checkPointErrors bad AFLOS checkpoints"
	        		}
                    if (height_errors != aflos_error.heightErrors) {
                        println "Evaluation error: $height_errors bad heights <> $aflos_error.heightErrors bad AFLOS heights"
                    }
                    if (course_errors != aflos_error.courseErrors) {
                        println "Evaluation error: $course_errors bad courses <> $aflos_error.courseErrors bad AFLOS courses"
                    }
	        		Map ret = ['error':true,'saved':true,'message':getMsg('fc.aflos.points.imported.naverrors.differences',[afloscrewnames_instance.viewName()])]
					printerror ret.message
					return ret
	        	}
	        }
    	}
        catch (Exception e) {
        	Map ret = ['error':true,'message':getMsg('fc.notimported.msg',[afloscrewnames_instance.viewName(),e.getMessage()])]
			printerror ret.message
			return ret
        }
    }

    //--------------------------------------------------------------------------
    Map setnoflightresultsTest(Map params)
    {
        Map test = getTest(params)
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

		CoordResult.findAllByTest(test.instance,[sort:"id"]).each { CoordResult coordresult_instance ->
			// reset results
	        coordresult_instance.ResetResults()
			
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
		
    	// Penalties berechnen
        calculateTestPenalties(test.instance,false)
        test.instance.save()
        
        return ['instance':test.instance]
	}
	
    //--------------------------------------------------------------------------
    private boolean processAflosErrorPointBadCourse(Test testInstance, int badCourseNum, String badCourseStartTimeUTC)
    {
    	boolean course_error = false
    	Contest contest_instance = testInstance.task.contest
    	
        Date badCourseStartTime = Date.parse("HH:mm:ss",FcMath.ConvertAFLOSTime(badCourseStartTimeUTC))
        GregorianCalendar badCourseStartCalendar = new GregorianCalendar()
        badCourseStartCalendar.setTime(badCourseStartTime)
        
        Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        GregorianCalendar timezone_calendar = new GregorianCalendar()
        timezone_calendar.setTime(timezone_date)
        
        badCourseStartCalendar.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
		if (contest_instance.timeZone.startsWith("-")) {
			badCourseStartCalendar.add(Calendar.MINUTE, -timezone_calendar.get(Calendar.MINUTE))
		} else {
        	badCourseStartCalendar.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
		}
        
        GregorianCalendar badCourseEndCalendar = badCourseStartCalendar.clone()
        badCourseEndCalendar.add(Calendar.SECOND, badCourseNum)
            
        println "Found AflosErrorPointBadCourse ($badCourseNum, ${FcMath.TimeStr(badCourseStartCalendar.getTime())}...${FcMath.TimeStr(badCourseEndCalendar.getTime())}): "

        if (badCourseNum > testInstance.GetFlightTestBadCourseCorrectSecond()) {
        	course_error = true
    		int last_index = 0
    		Date last_time
    		boolean calculatePenalties = false
            CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
            	if (last_index != 0 && badCourseEndCalendar.getTime() < coordresult_instance.resultCpTime) {
        			// process
        			last_index = i
        			
        			if (badCourseEndCalendar.getTime() > last_time) {
        				coordresult_instance.resultBadCourseNum++
        				coordresult_instance.save()
        				calculatePenalties = true
        				println "  $coordresult_instance.mark relevant."
        			} else {
        				println "  $coordresult_instance.mark not relevant."
        			}
            	}

            	if (badCourseStartCalendar.getTime() > coordresult_instance.resultCpTime) {
                    last_index = i
                    last_time = coordresult_instance.resultCpTime
                } else {
                    last_index = 0
                }
            }
    		
    	} else {
    		println "  Not relevant (number)."
    	}
        return course_error
    }
    
    //--------------------------------------------------------------------------
    private void processAflosErrorPointBadTurn(Test testInstance, String badTurnTimeUTC)
    {
        Contest contest_instance = testInstance.task.contest
        
        Date badturn_time = Date.parse("HH:mm:ss",FcMath.ConvertAFLOSTime(badTurnTimeUTC))
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
        
        print "Found AflosErrorPointBadTurn (${FcMath.TimeStr(badturn_calendar.getTime())}): "

        int last_index = 0
        Date last_time
        boolean calculatePenalties = false
        CoordResult.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { CoordResult coordresult_instance, int i ->
            if (last_index != 0 && coordresult_instance.planProcedureTurn && badturn_calendar.getTime() < coordresult_instance.resultCpTime) {
                // process
                last_index = i
                
                if (badturn_calendar.getTime() > last_time) {
                    coordresult_instance.resultProcedureTurnEntered = true
                    coordresult_instance.resultProcedureTurnNotFlown = true
                    coordresult_instance.save()
                    calculatePenalties = true
                    println "  $coordresult_instance.mark relevant."
                } else {
                    println "  $coordresult_instance.mark not relevant."
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
    Map getCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)

        if (!coordroute_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
        }
        
        return ['instance':coordroute_instance]
    }

    //--------------------------------------------------------------------------
    Map updateCoordRoute(Map params)
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
            
            coordroute_instance.properties = params
			calculateCoordMapDistance(coordroute_instance, false)
			if (coordroute_instance.measureTrueTrack || coordroute_instance.measureDistance) {
				coordroute_instance.measureEntered = true
			}

            if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                calculateSecretLegRatio(coordroute_instance.route)
                calculateRouteLegs(coordroute_instance.route)
                return ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.updated',["${coordroute_instance.name()}"])]
            } else {
                return ['instance':coordroute_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.coordroute'),params.id])]
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
            
			coordroute_instance.measureEntered = false
			coordroute_instance.measureTrueTrack = null
            coordroute_instance.measureDistance = null
			coordroute_instance.legMeasureDistance = null
			coordroute_instance.legDistance = null
			
            if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
                calculateSecretLegRatio(coordroute_instance.route)
                calculateRouteLegs(coordroute_instance.route)
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
    	CoordRoute last_coordroute_instance = CoordRoute.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])
        CoordRoute coordroute_instance = new CoordRoute()
    
        coordroute_instance.properties = params
        coordroute_instance.altitude = 1
    
        if (params.secret) {
            coordroute_instance.type = CoordType.SECRET
            if (last_coordroute_instance) {
                switch (last_coordroute_instance.type) {
                	case CoordType.SP:
                    case CoordType.TP:
                    case CoordType.SECRET:
                    	coordroute_instance.titleNumber = findNextTitleNumber(last_coordroute_instance.route,CoordType.SECRET)
                    	break
                    default:
                        return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
                }
            } else {
                return ['error':true,'message':getMsg('fc.coordroute.addsecret.notallowed')]
            }
        } else {
	        if (last_coordroute_instance) {
	        	switch (last_coordroute_instance.type) {
	        		case CoordType.TO:
	        			coordroute_instance.type = CoordType.SP
	        			break
	        		case CoordType.SP:
	                    coordroute_instance.type = CoordType.TP
	                    break
	        		case CoordType.TP:
                    case CoordType.SECRET:
	                    coordroute_instance.type = CoordType.TP
	                    coordroute_instance.titleNumber = findNextTitleNumber(last_coordroute_instance.route,CoordType.TP) 
	                    break
	                case CoordType.FP:
	                    coordroute_instance.type = CoordType.LDG
	                    break
	                case CoordType.LDG:
	                	return ['error':true,'message':getMsg('fc.coordroute.add.notallowed')]
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
    Map saveCoordRoute(Map params)
    {
    	CoordRoute last_coordroute_instance = CoordRoute.findByRoute(Route.get(params.routeid),[sort:"id", order:"desc"])

		// Summe der Distanzen vorangegangener Secrets-Points berechnen  
		BigDecimal lastMapMeasureDistance = null
        CoordRoute last_coordroute_test_instance 
    	CoordRoute.findAllByRoute(Route.get(params.routeid),[sort:"id", order:"desc"]).each { CoordRoute coordroute_instance ->
			if (!last_coordroute_test_instance) {
		        switch (coordroute_instance.type) {
			        case CoordType.SP:
			        case CoordType.TP:
			        case CoordType.FP:
			        	last_coordroute_test_instance = coordroute_instance // exit
			            break
			        default:
		        		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordroute_instance.legMeasureDistance)
		                break
			    }
        	}
        }
        
    	CoordRoute coordroute_instance = new CoordRoute(params)
        
        coordroute_instance.route = Route.get(params.routeid)
		calculateCoordMapDistance(coordroute_instance, true)
		if (coordroute_instance.measureTrueTrack || coordroute_instance.measureDistance) {
			coordroute_instance.measureEntered = true
		}
		
		// calculate coordTrueTrack/coordMeasureDistance
		if (last_coordroute_test_instance) {
			Map legdata_coord = calculateLegData(coordroute_instance, last_coordroute_test_instance)
			coordroute_instance.coordTrueTrack = legdata_coord.dir
			coordroute_instance.coordMeasureDistance = calculateMapMeasure(coordroute_instance.route.contest,legdata_coord.dis)
		} 

        if(!coordroute_instance.hasErrors() && coordroute_instance.save()) {
            calculateSecretLegRatio(coordroute_instance.route)
       		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordroute_instance.legMeasureDistance)
        	newLeg(
				coordroute_instance.route, 
				coordroute_instance, 
				last_coordroute_instance, 
				last_coordroute_test_instance, 
				coordroute_instance.measureDistance, 
				lastMapMeasureDistance, 
				coordroute_instance.measureTrueTrack
			)
            return ['instance':coordroute_instance,'saved':true,'message':getMsg('fc.created',["${coordroute_instance.name()}"])]
        } else {
            return ['instance':coordroute_instance]
        }
    }
    
    //--------------------------------------------------------------------------
	private void calculateCoordMapDistance(CoordRoute coordRouteInstance, boolean isNew)
	{
		if (coordRouteInstance.measureDistance) {
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
			coordRouteInstance.legDistance = calculateMapDistance(route_instance.contest, coordRouteInstance.legMeasureDistance)
		}
	}
	
    //--------------------------------------------------------------------------
	private void calculateAllCoordMapDistances(Route routeInstance)
	{
    	printstart "calculateAllCoordMapDistances '${routeInstance.name()}'"
		
		CoordRoute.findAllByRoute(routeInstance,[sort:"id", order:"asc"]).each { CoordRoute coordroute_instance ->
			if (coordroute_instance.type == CoordType.SECRET && coordroute_instance.measureDistance) {
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
					coordroute_instance.legDistance = calculateMapDistance(routeInstance.contest, leg_measure_distance)
					coordroute_instance.save()
					println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} modified: $old_leg_measure_distance -> $coordroute_instance.legMeasureDistance, $old_leg_distance -> $coordroute_instance.legDistance"
				}
			}
		}
		
		printdone ""
	}

	//--------------------------------------------------------------------------
    Map deleteCoordRoute(Map params)
    {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        
        if (coordroute_instance) {
            try {
                Route route_instance = coordroute_instance.route
                removeAllRouteLegs(route_instance)
                coordroute_instance.delete()
				calculateAllCoordMapDistances(route_instance)
				calculateSecretLegRatio(route_instance)
                calculateRouteLegs(route_instance)
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
    	
    	Contest contest_instance = routeLegCoordInstance.route.contest
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
    	routeLegCoordInstance.legDistance = calculateMapDistance(contest_instance,routeLegCoordInstance.legMeasureDistance)
    	
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
        BigDecimal lastMapMeasureDistance = null
        BigDecimal lastMapMeasureTrueTrack = null
        int testlegpos = 0
        CoordRoute.findAllByRoute(routeLegCoordInstance.route,[sort:"id"]).each { CoordRoute coordroute_instance ->
       		lastMapMeasureDistance = addMapDistance(lastMapMeasureDistance,coordroute_instance.legMeasureDistance)
            lastMapMeasureTrueTrack = coordroute_instance.measureTrueTrack
            if (last_coordroute_instance && last_coordroute_test_instance) {
                if ( (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.TP) ||
                     (last_coordroute_test_instance.type == CoordType.SP && coordroute_instance.type == CoordType.FP) ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.TP) ||
                     (last_coordroute_test_instance.type == CoordType.TP && coordroute_instance.type == CoordType.FP) ) 
                {
                	RouteLegTest.findAllByRoute(routeLegCoordInstance.route,[sort:"id"]).eachWithIndex { RouteLegTest routelegtest_instance, int i ->
                		if (i == testlegpos) {
                			routelegtest_instance.legMeasureDistance = lastMapMeasureDistance 
                			routelegtest_instance.legDistance = calculateMapDistance(contest_instance,lastMapMeasureDistance)
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
                    last_coordroute_test_instance = coordroute_instance
                    lastMapMeasureDistance = null
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

            if (!crew_instance.hasErrors()) {

				boolean modify_tas = crew_instance.tas.toString() != params.tas
				boolean old_disabled = crew_instance.disabled
            	Aircraft old_aircraft_instance = crew_instance.aircraft
	            crew_instance.properties = params
	            
				if (old_disabled != crew_instance.disabled) {
					println "Crew dis/enabled."
					crew_instance.planningPenalties = 0
					crew_instance.flightPenalties = 0
					crew_instance.observationPenalties = 0
					crew_instance.landingPenalties = 0
					crew_instance.specialPenalties = 0
					crew_instance.contestPenalties = 0
					crew_instance.contestPosition = 0
					crew_instance.noContestPosition = false
					crew_instance.classPosition = 0
					crew_instance.teamPenalties = 0
					if (crew_instance.team) {
						crew_instance.team.contestPenalties = 0
						crew_instance.team.contestPosition = 0
					}
					for (Crew crew_instance2 in Crew.findAllByContest(crew_instance.contest,[sort:"id"])) {
						boolean run = false
						if (crew_instance2 != crew_instance) {
							if (crew_instance.contest.resultClasses) {
								if (crew_instance.resultclass && crew_instance.resultclass == crew_instance2.resultclass) {
									run = true
								}
							} else {
								run = true
							}
						}
						if (run) {
							
							crew_instance2.planningPenalties = 0
							crew_instance2.flightPenalties = 0
							crew_instance2.observationPenalties = 0
							crew_instance2.landingPenalties = 0
							crew_instance2.specialPenalties = 0
							crew_instance2.contestPenalties = 0
							crew_instance2.contestPosition = 0
							crew_instance2.noContestPosition = false
							crew_instance2.classPosition = 0
							crew_instance2.teamPenalties = 0
							if (crew_instance2.team) {
								crew_instance2.team.contestPenalties = 0
								crew_instance2.team.contestPosition = 0
							}
							crew_instance2.save()
						}
					}
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
			                    test_instance.save()
							}
		                }
					}
	
					printdone ""
					if (no_modify_tas_num) {
						return ['instance':crew_instance,'saved':true,'message':getMsg('fc.crew.updated.tasnomodify',["${crew_instance.name}",no_modify_tas_num.toString()])]
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
        Crew crew_instance = new Crew(params)
        crew_instance.contest = contestInstance
		crew_instance.viewpos = Crew.countByContest(contestInstance)
        if (!crew_instance.hasErrors() && crew_instance.save()) {
            
            if (params.registration) {
                Aircraft aircraft_instance = Aircraft.findByRegistrationAndContest(params.registration,contestInstance)
                if (!aircraft_instance) {
                    aircraft_instance = new Aircraft(params)
                    aircraft_instance.contest = crew_instance.contest 
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
					setContestRule(resultclass_instance, resultclass_instance.contestRule)
					resultclass_instance.save()
					println "saveCrew (new class): $resultclass_instance.name saved."
					
					// create TaskClasses
					if (contestInstance.resultClasses) {
						for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
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
			
            Task.findAllByContest(contestInstance,[sort:"id"]).each { Task task_instance ->
                Test test_instance = new Test()
                test_instance.crew = crew_instance
				test_instance.taskTAS = crew_instance.tas
                test_instance.viewpos = Crew.countByContest(contestInstance) - 1
                test_instance.task = task_instance
                test_instance.timeCalculated = false
                test_instance.save()
            }
			
            String msg
            if (crew_instance.aircraft) {
            	msg = getMsg('fc.crew.withaircraft.created',["${crew_instance.name}", "${crew_instance.aircraft.registration}"])
            } else {
                msg = getMsg('fc.created',["${crew_instance.name}"])
            }

            return ['instance':crew_instance,'saved':true,'message':msg]
        } else {
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
				deleteCrewInstance(crew_instance)
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
		
		int delete_num = 0
		 
        Crew.findAllByContest(contestInstance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
				deleteCrewInstance(crew_instance)
				delete_num++
            }
        }
		
		Map ret = ['deleted':delete_num > 0,'message':getMsg('fc.crew.deleted',["${delete_num}"])]
		printdone ret
        return ret
	}
	
    //--------------------------------------------------------------------------
	void deleteCrewInstance(Crew crewInstance)
	{
		println "Delete '$crewInstance.name'"
		
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
        Task.findAllByContest(crewInstance.contest,[sort:"id"]).each { Task task_instance ->
        	Test.findAllByTask(task_instance,[sort:"viewpos"]).eachWithIndex { Test test_instance, int i ->
        		test_instance.viewpos = i
        		test_instance.save()
        	}
        }

        crewInstance.delete()
        
        // correct all crew's viewpos
		Crew.findAllByContest(crewInstance.contest,[sort:"id"]).eachWithIndex { Crew crew_instance2, int i ->
			crew_instance2.viewpos = i
			crew_instance2.save()
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
    Map printCrews(Map params,printparams)
    {
        Map crews = [:]

        // Print crews
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            String url = "${printparams.baseuri}/crew/listprintable?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            crews.content = content 
        }
        catch (Throwable e) {
            crews.message = getMsg('fc.crew.printerror',["$e"])
            crews.error = true
        }
        return crews
    }
    
    //--------------------------------------------------------------------------
    Map importCrews(String fileName, String loadFileName, Contest contestInstance)
    {
		printstart "importCrews $fileName [$loadFileName]"
		int new_crew_num = 0
		int new_crew_error_num = 0
		int exist_crew_num = 0
		try {
			CrewExcelImporter crew_importer = new CrewExcelImporter(loadFileName)
			
			List crews = []
			int i = 0
			List crew_list = excelImportService.convertColumnMapConfigManyRows(crew_importer.workbook, crew_importer.CONFIG_CREWS_COLUMN_MAP, null, null, [:], -1)
			for (Map crew_entry in crew_list) {
				if (i == 0) {
					int col_num = 0
					crew_entry.each { key, value ->
						if (key == value) {
							col_num++
						}
					}
					if (col_num != crew_importer.CONFIG_CREWS_COLUMN_NUM) {
						return []
					}
				} else {
					Map new_entry = [:]
					crew_importer.CONFIG_CREWS_COLUMN_MAP.columnMap.each { key, value ->
						new_entry += [(value):""]
					}
					crew_entry.each { key, value ->
						if (value) {
							new_entry[(key)] = value.toString()
						}
					}
					crews += new_entry
				}
				i++
			}
	
			crews.each { Map crew_entry ->
				if (crew_entry.name) {
					String crew_name = crew_entry.name.trim()
					if (crew_name) {
						if (crew_entry.name2) {
							String crew_name2 = crew_entry.name2.trim()
							if (crew_name2) {
								crew_name += ", " + crew_name2
							}
						}
						crew_entry.name = crew_name
						if (crew_entry.tas) {
							crew_entry.tas = crew_entry.tas.replace('.',',')
						}
						printstart crew_name
						Crew crew = Crew.findByNameAndContest(crew_entry.name, contestInstance)
						if (crew) {
							printdone "Crew already exists."
							exist_crew_num++
						} else {
							Map ret = saveCrew(crew_entry,contestInstance)
							printdone "Created $ret"
							if (ret.saved) {
								new_crew_num++
							} else {
								new_crew_error_num++
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Map ret = ['error':true,'message':getMsg('fc.notimported.msg',[fileName,e.getMessage()])]
			printerror ret
			return ret
		}
		
		Map ret
		if (new_crew_num) {
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
    Map getTest(Map params)
    {
        Test test_instance = Test.get(params.id)

        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
        
        return ['instance':test_instance]
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
			println "getflightplanprintableTest: timetableVersion $test_instance.task.timetableVersion of '${test_instance.task.name()}' saved."
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
    Map getlandingresultsprintableTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
		
		println "getlandingresultsprintableTest ($test_instance.crew.name)"
		
		// Calculate landing test version
		if (test_instance.landingTestModified) {
			test_instance.landingTestVersion++
			test_instance.landingTestModified = false
			test_instance.save()
			println "getlandingresultsprintableTest: landingTestVersion $test_instance.landingTestVersion of '$test_instance.crew.name' saved."
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
    Map printflightplanTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flightplan
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/flightplanprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.print.error',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtaskTest(Map params,printparams,withResult)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flightplan
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/planningtaskprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&results="
            if (withResult) {
            	url += "yes"
            } else {
                url += "no"
            }
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.print.error',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printplanningtaskresultsTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print planning test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/planningtaskresultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.planningresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printflightresultsTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print flight test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/flightresultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.flightresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printobservationresultsTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print observation test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/observationresultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.observationresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printlandingresultsTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print landing test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/landingresultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.landingresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printspecialresultsTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
        // Print special test results
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/specialresultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.specialresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printcrewresultsTest(Map params,printparams)
    {
        Map test = getTest(params)
        if (!test.instance) {
            return test
        }
        
		test.instance.printPlanningResults = params.printPlanningResults == "on"
		test.instance.printFlightResults = params.printFlightResults == "on"
		test.instance.printObservationResults = params.printObservationResults == "on"
		test.instance.printLandingResults = params.printLandingResults == "on"
		test.instance.printSpecialResults = params.printSpecialResults == "on"

        // Print crewresults
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            String url = "${printparams.baseuri}/test/crewresultsprintable/${test.instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&printPlanningResults=${test.instance.printPlanningResults}&printFlightResults=${test.instance.printFlightResults}&printObservationResults=${test.instance.printObservationResults}&printLandingResults=${test.instance.printLandingResults}&printSpecialResults=${test.instance.printSpecialResults}"
            println "Print: $url"
            renderer.setDocument(url)
            renderer.layout()
            renderer.createPDF(content,false)
            renderer.finishPDF()
            test.content = content
        }
        catch (Throwable e) {
            test.message = getMsg('fc.crewresults.printerror',["$e"])
            test.error = true
        }
        return test
    }
    
    //--------------------------------------------------------------------------
    Map printcrewresultsTask(Map params,printparams)
    {
		printstart "printcrewresultsTask"
        Map task = getTask(params) 
        if (!task.instance) {
			printerror "No task."
            return task
        }

		task.instance.printPlanningResults = params.printPlanningResults == "on"
		task.instance.printFlightResults = params.printFlightResults == "on"
		task.instance.printObservationResults = params.printObservationResults == "on"
		task.instance.printLandingResults = params.printLandingResults == "on"
		task.instance.printSpecialResults = params.printSpecialResults == "on"

        // Print all crewresults
        try {
            ITextRenderer renderer = new ITextRenderer();
            ByteArrayOutputStream content = new ByteArrayOutputStream()
            boolean first_pdf = true
            for ( Test test_instance in Test.findAllByTask(task.instance,[sort:"viewpos"])) {
				if (!test_instance.crew.disabled) {
					if (isprintcrewresult(params,test_instance)) {
						printstart "Print $test_instance.crew.name"
		                String url = "${printparams.baseuri}/test/crewresultsprintable/${test_instance.id}?lang=${printparams.lang}&contestid=${printparams.contest.id}&printPlanningResults=${task.instance.printPlanningResults}&printFlightResults=${task.instance.printFlightResults}&printObservationResults=${task.instance.printObservationResults}&printLandingResults=${task.instance.printLandingResults}&printSpecialResults=${task.instance.printSpecialResults}"
		                println "Print: $url"
		                renderer.setDocument(url)
		                renderer.layout()
		                if (first_pdf) {
		                    renderer.createPDF(content,false)
		                    first_pdf = false
		                } else {
		                    renderer.writeNextDocument(1)
		                }
						printdone ""
					}
				}
            }
            renderer.finishPDF()

            task.content = content 
        }
        catch (Throwable e) {
            task.message = getMsg('fc.crewresults.printerror',["$e"])
            task.error = true
			printerror task.message 
        }
		printdone ""
        return task
    }
    
    //--------------------------------------------------------------------------
	boolean isprintcrewresult(Map params, Test testInstance) 
	{
		if (testInstance.task.contest.resultClasses) {
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(testInstance.task.contest,[sort:"id"])) {
				if (params["resultclass_${resultclass_instance.id}"] == "on") {
					if (testInstance.crew.resultclass) {
						if (testInstance.crew.resultclass.id == resultclass_instance.id) {  // BUG: direkter Klassen-Vergleich geht nicht
							return true
						}
					}
				}
			}
			return false
		}
		return true
	}
	
    //--------------------------------------------------------------------------
    Map completeplanningtaskresultsTest(Map params)
    {
        Map test = getTest(params)
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
			test.instance.crewResultsModified = true
		}

        test.instance.planningTestComplete = true
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.planningresults.points',["${test.instance.planningTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
	Map saveplanningtaskresultsTest(Map params)
	{
        Map test = getTest(params)
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
			test.instance.crewResultsModified = true
		}

        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.planningresults.points',["${test.instance.planningTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
	}
	
    //--------------------------------------------------------------------------
    Map openplanningtaskresultsTest(Map params)
    {
        Map test = getTest(params)
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
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map completeflightresultsTest(Map params)
    {
        Map test = getTest(params)
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
			test.instance.crewResultsModified = true
		}

        test.instance.flightTestComplete = true
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.flightresults.points',["${test.instance.flightTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map saveflightresultsTest(Map params)
    {
        Map test = getTest(params)
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
			test.instance.crewResultsModified = true
		}
		
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.flightresults.points',["${test.instance.flightTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openflightresultsreTest(Map params)
    {
        Map test = getTest(params)
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
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map completeobservationresultsTest(Map params)
    {
        Map test = getTest(params)
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
			test.instance.observationTestModified = true
			test.instance.crewResultsModified = true
		}

        test.instance.observationTestComplete = true
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.observationresults.points',["${test.instance.observationTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openobservationresultsTest(Map params)
    {
        Map test = getTest(params)
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
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map saveobservationresultsTest(Map params)
    {
        Map test = getTest(params)
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
			test.instance.observationTestModified = true
			test.instance.crewResultsModified = true
		}

        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.observationresults.points',["${test.instance.observationTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map completelandingresultsTest(Map params)
    {
        Map test = getTest(params)
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
		test.instance.landingTest1Measure = test.instance.landingTest1Measure.toUpperCase() 
		test.instance.landingTest2Measure = test.instance.landingTest2Measure.toUpperCase() 
		test.instance.landingTest3Measure = test.instance.landingTest3Measure.toUpperCase() 
		test.instance.landingTest4Measure = test.instance.landingTest4Measure.toUpperCase() 
		if (test.instance.isDirty()) {
			test.instance.landingTestModified = true
			test.instance.crewResultsModified = true
		}

		try {
			calculateTestPenalties(test.instance,false)
		} catch (Exception e) {
			return ['instance':test.instance,'error':true,'message':e.toString()]
		}
		test.instance.landingTestComplete = true
		
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.landingresults.points',["${test.instance.landingTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openlandingresultsTest(Map params)
    {
        Map test = getTest(params)
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

        test.instance.landingTestComplete = false
		try {
			calculateTestPenalties(test.instance,false)
		} catch (Exception e) {
			return ['instance':test.instance,'error':true,'message':e.toString()]
		}
        
        if(!test.instance.hasErrors() && test.instance.save()) {
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map savelandingresultsTest(Map params)
    {
        Map test = getTest(params)
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
		test.instance.landingTest1Measure = test.instance.landingTest1Measure.toUpperCase() 
		test.instance.landingTest2Measure = test.instance.landingTest2Measure.toUpperCase() 
		test.instance.landingTest3Measure = test.instance.landingTest3Measure.toUpperCase() 
		test.instance.landingTest4Measure = test.instance.landingTest4Measure.toUpperCase() 
		if (test.instance.isDirty()) {
			test.instance.landingTestModified = true
			test.instance.crewResultsModified = true
		}
		
		try {
			calculateTestPenalties(test.instance,false)
		} catch (Exception e) {
			return ['instance':test.instance,'error':true,'message':e.toString()]
		}
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.landingresults.points',["${test.instance.landingTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map completespecialresultsTest(Map params)
    {
        Map test = getTest(params)
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
		}

        test.instance.specialTestComplete = true
        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.specialresults.points',["${test.instance.specialTestPenalties}"])}"
            return ['instance':test.instance,'saved':true,'message':msg]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map openspecialresultsTest(Map params)
    {
        Map test = getTest(params)
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
            return ['instance':test.instance,'saved':true,'message':getMsg('fc.updated',["${test.instance.crew.name}"])]
        } else {
            return ['instance':test.instance,'error':true]
        }
    }
    
    //--------------------------------------------------------------------------
    Map savespecialresultsTest(Map params)
    {
        Map test = getTest(params)
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
		}

        calculateTestPenalties(test.instance,false)
        
        if(!test.instance.hasErrors() && test.instance.save()) {
			String msg = "${getMsg('fc.updated',["${test.instance.crew.name}"])} ${getMsg('fc.specialresults.points',["${test.instance.specialTestPenalties}"])}"
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
				calculateLegPlanningInstance(testlegplanning_instance,true)
				testlegplanning_instance.save()
			}
			
			// recalculate CoordResult
			CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
				calculateCoordResultInstance(coordresult_instance,false,true)
				coordresult_instance.save()
			}
		}

    	// planningTestPenalties
    	testInstance.planningTestLegPenalties = 0
        testInstance.planningTestLegComplete = false
        if (TestLegPlanning.findByTest(testInstance)) {
        	testInstance.planningTestLegComplete = true
        }
    	TestLegPlanning.findAllByTest(testInstance,[sort:"id"]).each { TestLegPlanning testlegplanning_instance ->
    		if (testlegplanning_instance.resultEntered) {
    			testInstance.planningTestLegPenalties += testlegplanning_instance.penaltyTrueHeading
    			testInstance.planningTestLegPenalties += testlegplanning_instance.penaltyLegTime
    		} else {
    			testInstance.planningTestLegComplete = false
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
		testInstance.planningTestPenalties += testInstance.planningTestOtherPenalties
    	
    	// flightTestPenalties
    	testInstance.flightTestCheckPointPenalties = 0
    	testInstance.flightTestCheckPointsComplete = false
    	if (CoordResult.findByTest(testInstance)) {
    		testInstance.flightTestCheckPointsComplete = true
    	}
    	CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
    		if (coordresult_instance.resultEntered) {
  				testInstance.flightTestCheckPointPenalties += coordresult_instance.penaltyCoord
    		} else {
    			testInstance.flightTestCheckPointsComplete = false
    		}
    		if (coordresult_instance.planProcedureTurn) {
	    		if (coordresult_instance.resultProcedureTurnEntered) {
		    		if (coordresult_instance.resultProcedureTurnNotFlown) {
		    			testInstance.flightTestCheckPointPenalties += testInstance.GetFlightTestProcedureTurnNotFlownPoints()
		    		}
	    		} else {
	    			testInstance.flightTestCheckPointsComplete = false
	    		}
    		}
    		if (coordresult_instance.resultAltitude && coordresult_instance.resultMinAltitudeMissed) {
    			testInstance.flightTestCheckPointPenalties += testInstance.GetFlightTestMinAltitudeMissedPoints()
    		}
    		if (coordresult_instance.resultBadCourseNum) {
    			testInstance.flightTestCheckPointPenalties += coordresult_instance.resultBadCourseNum * testInstance.GetFlightTestBadCoursePoints()  
    		}
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
		testInstance.flightTestPenalties += testInstance.flightTestOtherPenalties
    	
		// observationTestPenalties
		testInstance.observationTestPenalties = testInstance.observationTestRoutePhotoPenalties +
		                                        testInstance.observationTestTurnPointPhotoPenalties +
												testInstance.observationTestGroundTargetPenalties
		
        // landingTestPenalties
		if (testInstance.IsLandingTestAnyRun()) {
			testInstance.landingTestPenalties = 0
			if (testInstance.IsLandingTest1Run()) {
				testInstance.landingTest1Penalties = 0
				switch (testInstance.landingTest1Landing) {
					case 1:
						testInstance.landingTest1MeasurePenalties = calculateLandingPenalties(testInstance.landingTest1Measure,testInstance.GetLandingTest1PenaltyCalculator())
						testInstance.landingTest1Penalties += testInstance.landingTest1MeasurePenalties
						break
					case 2:
						testInstance.landingTest1Penalties += testInstance.GetLandingTest1NoLandingPoints()
						break
					case 3:
						testInstance.landingTest1Penalties += testInstance.GetLandingTest1OutsideLandingPoints()
						break
				}
				if (testInstance.landingTest1RollingOutside) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTest1RollingOutsidePoints()
				}
				if (testInstance.landingTest1PowerInBox) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTest1PowerInBoxPoints()
				}
				if (testInstance.landingTest1GoAroundWithoutTouching) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTest1GoAroundWithoutTouchingPoints()
				}
				if (testInstance.landingTest1GoAroundInsteadStop) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTest1GoAroundInsteadStopPoints()
				}
				if (testInstance.landingTest1AbnormalLanding) {
					testInstance.landingTest1Penalties += testInstance.GetLandingTest1AbnormalLandingPoints()
				}
				if (testInstance.GetLandingTest1MaxPoints() > 0) {
					if (testInstance.landingTest1Penalties > testInstance.GetLandingTest1MaxPoints()) {
						testInstance.landingTest1Penalties = testInstance.GetLandingTest1MaxPoints()
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest1Penalties
			}
			if (testInstance.IsLandingTest2Run()) {
				testInstance.landingTest2Penalties = 0 
				switch (testInstance.landingTest2Landing) {
					case 1:
						testInstance.landingTest2MeasurePenalties = calculateLandingPenalties(testInstance.landingTest2Measure,testInstance.GetLandingTest2PenaltyCalculator())
						testInstance.landingTest2Penalties += testInstance.landingTest2MeasurePenalties
						break
					case 2:
						testInstance.landingTest2Penalties += testInstance.GetLandingTest2NoLandingPoints()
						break
					case 3: 
						testInstance.landingTest2Penalties += testInstance.GetLandingTest2OutsideLandingPoints()
						break
				}
				if (testInstance.landingTest2RollingOutside) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTest2RollingOutsidePoints()
				}
				if (testInstance.landingTest2PowerInBox) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTest2PowerInBoxPoints()
				}
				if (testInstance.landingTest2GoAroundWithoutTouching) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTest2GoAroundWithoutTouchingPoints()
				}
				if (testInstance.landingTest2GoAroundInsteadStop) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTest2GoAroundInsteadStopPoints()
				}
				if (testInstance.landingTest2AbnormalLanding) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTest2AbnormalLandingPoints()
				}
				if (testInstance.landingTest2PowerInAir) {
					testInstance.landingTest2Penalties += testInstance.GetLandingTest2PowerInAirPoints()
				}
				if (testInstance.GetLandingTest2MaxPoints() > 0) {
					if (testInstance.landingTest2Penalties > testInstance.GetLandingTest2MaxPoints()) {
						testInstance.landingTest2Penalties = testInstance.GetLandingTest2MaxPoints()
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest2Penalties
			}
			if (testInstance.IsLandingTest3Run()) {
				testInstance.landingTest3Penalties = 0 
				switch (testInstance.landingTest3Landing) {
					case 1:
						testInstance.landingTest3MeasurePenalties = calculateLandingPenalties(testInstance.landingTest3Measure,testInstance.GetLandingTest3PenaltyCalculator())
						testInstance.landingTest3Penalties += testInstance.landingTest3MeasurePenalties
						break
					case 2:
						testInstance.landingTest3Penalties += testInstance.GetLandingTest3NoLandingPoints()
						break
					case 3: 
						testInstance.landingTest3Penalties += testInstance.GetLandingTest3OutsideLandingPoints()
						break
				}
				if (testInstance.landingTest3RollingOutside) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3RollingOutsidePoints()
				}
				if (testInstance.landingTest3PowerInBox) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3PowerInBoxPoints()
				}
				if (testInstance.landingTest3GoAroundWithoutTouching) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3GoAroundWithoutTouchingPoints()
				}
				if (testInstance.landingTest3GoAroundInsteadStop) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3GoAroundInsteadStopPoints()
				}
				if (testInstance.landingTest3AbnormalLanding) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3AbnormalLandingPoints()
				}
				if (testInstance.landingTest3PowerInAir) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3PowerInAirPoints()
				}
				if (testInstance.landingTest3FlapsInAir) {
					testInstance.landingTest3Penalties += testInstance.GetLandingTest3FlapsInAirPoints()
				}
				if (testInstance.GetLandingTest3MaxPoints() > 0) {
					if (testInstance.landingTest3Penalties > testInstance.GetLandingTest3MaxPoints()) {
						testInstance.landingTest3Penalties = testInstance.GetLandingTest3MaxPoints()
					}
				}
				testInstance.landingTestPenalties += testInstance.landingTest3Penalties
			}
			if (testInstance.IsLandingTest4Run()) {
				testInstance.landingTest4Penalties = 0
				switch (testInstance.landingTest4Landing) {
					case 1:
						testInstance.landingTest4MeasurePenalties = calculateLandingPenalties(testInstance.landingTest4Measure,testInstance.GetLandingTest4PenaltyCalculator())
						testInstance.landingTest4Penalties += testInstance.landingTest4MeasurePenalties
						break
					case 2:
						testInstance.landingTest4Penalties += testInstance.GetLandingTest4NoLandingPoints()
						break
					case 3: 
						testInstance.landingTest4Penalties += testInstance.GetLandingTest4OutsideLandingPoints()
						break
				}
				if (testInstance.landingTest4RollingOutside) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTest4RollingOutsidePoints()
				}
				if (testInstance.landingTest4PowerInBox) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTest4PowerInBoxPoints()
				}
				if (testInstance.landingTest4GoAroundWithoutTouching) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTest4GoAroundWithoutTouchingPoints()
				}
				if (testInstance.landingTest4GoAroundInsteadStop) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTest4GoAroundInsteadStopPoints()
				}
				if (testInstance.landingTest4AbnormalLanding) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTest4AbnormalLandingPoints()
				}
				if (testInstance.landingTest4TouchingObstacle) {
					testInstance.landingTest4Penalties += testInstance.GetLandingTest4TouchingObstaclePoints()
				}
				if (testInstance.GetLandingTest4MaxPoints() > 0) {
					if (testInstance.landingTest4Penalties > testInstance.GetLandingTest4MaxPoints()) {
						testInstance.landingTest4Penalties = testInstance.GetLandingTest4MaxPoints()
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
	private int calculateLandingPenalties(String measureValue, String calculatorValue)
	{
		String landing_measure = "'${measureValue}'"
		Object penalty_calculator = Eval.me(calculatorValue)
		int penalty_result = Eval.me("penalty_calculator",penalty_calculator,"penalty_calculator(${landing_measure})")
		return penalty_result
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
    Map updateFlightTest(Map params)
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
            flighttest_instance.properties = params

			if (old_route != flighttest_instance.route) {
		        Test.findAllByTask(flighttest_instance.task,[sort:"id"]).each { Test test_instance ->
		        	test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
		            test_instance.save()
		        }
				println "Calculated times have been reset." 
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
    Map createFlightTest(Map params, Contest contestInstance)
    {
        if (!Route.countByContest(contestInstance)) {
            return ['message':getMsg('fc.route.notfound'),'error':true,
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':params.task.id]
        }
         
        FlightTest flighttest_instance = new FlightTest()
        flighttest_instance.properties = params
        return ['instance':flighttest_instance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTest(Map params)
    {
        FlightTest flighttest_instance = new FlightTest(params)
        
        flighttest_instance.task = Task.get( params.taskid )
        
        if (!flighttest_instance.direction) {
        	flighttest_instance.direction = 0
        }
        if (!flighttest_instance.speed) {
        	flighttest_instance.speed = 0
        }
        
        if(!flighttest_instance.hasErrors() && flighttest_instance.save()) {

            Task task_instance = Task.get( params.taskid )
            task_instance.flighttest = flighttest_instance
            task_instance.save()

            Wind windInstance = new Wind(direction:flighttest_instance.direction,speed:flighttest_instance.speed)
            windInstance.save()
            
            FlightTestWind flighttestwind_instance = new FlightTestWind(params)
            flighttestwind_instance.wind = windInstance
            flighttestwind_instance.flighttest = flighttest_instance
            flighttestwind_instance.save()

            return ['instance':flighttest_instance,'saved':true,'message':getMsg('fc.created',["${flighttest_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,'fromtask':params.fromtask,
                    'taskid':task_instance.id]
        } else {
            return ['instance':flighttest_instance]
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
    Map updateFlightTestWind(Map params)
    {
		printstart "updateFlightTestWind"
		
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)
        
        if (flighttestwind_instance) {

            if(params.version) {
                long version = params.version.toLong()
                if(flighttestwind_instance.version > version) {
                    flighttestwind_instance.errors.rejectValue("version", "flightTestWind.optimistic.locking.failure", getMsg('fc.notupdated'))
                    return ['instance':flighttestwind_instance]
                }
            }
            
			BigDecimal old_direction = flighttestwind_instance.wind.direction
			BigDecimal old_speed = flighttestwind_instance.wind.speed  
            flighttestwind_instance.properties = params

            flighttestwind_instance.wind.direction = flighttestwind_instance.direction
            flighttestwind_instance.wind.speed = flighttestwind_instance.speed
            
			if (old_direction != flighttestwind_instance.wind.direction || old_speed != flighttestwind_instance.wind.speed) {
		        Test.findAllByTask(flighttestwind_instance.flighttest.task,[sort:"id"]).each { Test test_instance ->
		        	test_instance.timeCalculated = false
					test_instance.ResetFlightTestResults()
					test_instance.CalculateTestPenalties()
		            test_instance.save()
		        }
				println "Calculated times have been reset." 
			}
			
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
        FlightTestWind flighttestwind_instance = new FlightTestWind()
        flighttestwind_instance.properties = params
        return ['instance':flighttestwind_instance]
    }

    
    //--------------------------------------------------------------------------
    Map saveFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = new FlightTestWind(params)
        
        flighttestwind_instance.flighttest = FlightTest.get(params.flighttestid)
        
        Wind windInstance = new Wind(params)
        if(!windInstance.hasErrors() && windInstance.save()) {
            flighttestwind_instance.wind = windInstance
        }
        
        if(!flighttestwind_instance.hasErrors() && flighttestwind_instance.save()) {
            return ['instance':flighttestwind_instance,'saved':true,'message':getMsg('fc.created',["${flighttestwind_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':flighttestwind_instance.flighttest.task.id,
                    'flighttestid':flighttestwind_instance.flighttest.id]
        } else {
            return ['instance':flighttestwind_instance]
        }
    }
    
    //--------------------------------------------------------------------------
    Map deleteFlightTestWind(Map params)
    {
        FlightTestWind flighttestwind_instance = FlightTestWind.get(params.id)
        
        if (flighttestwind_instance) {
			if (!Test.findByFlighttestwind(flighttestwind_instance)) {
	            try {
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
    Map savePlanningTest(Map params)
    {
        PlanningTest planningtest_instance = new PlanningTest(params)
        
        planningtest_instance.task = Task.get( params.taskid )
        
        if (!planningtest_instance.direction) {
            planningtest_instance.direction = 0
        }
        if (!planningtest_instance.speed) {
            planningtest_instance.speed = 0
        }
        
        if(!planningtest_instance.hasErrors() && planningtest_instance.save()) {

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
            
            if (!planningtesttask_instance.direction) {
            	planningtesttask_instance.direction = 0
            }
            if (!planningtesttask_instance.speed) {
            	planningtesttask_instance.speed = 0
            }
            
            planningtesttask_instance.wind.direction = planningtesttask_instance.direction
            planningtesttask_instance.wind.speed = planningtesttask_instance.speed

			if (old_route != planningtesttask_instance.route || old_direction != planningtesttask_instance.wind.direction || old_speed != planningtesttask_instance.wind.speed) {
	            Test.findAllByTask(planningtesttask_instance.planningtest.task,[sort:"id"]).each { Test test_instance ->
	                calulateTestLegPlannings(test_instance)
					test_instance.ResetPlanningTestResults()
					test_instance.CalculateTestPenalties()
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
        PlanningTestTask planningtesttask_instance = new PlanningTestTask(params)

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
            planningtesttask_instance.wind = windInstance
        }
        
        if(!planningtesttask_instance.hasErrors() && planningtesttask_instance.save()) {
            return ['instance':planningtesttask_instance,'saved':true,'message':getMsg('fc.created',["${planningtesttask_instance.name()}"]),
                    'fromlistplanning':params.fromlistplanning,
                    'taskid':planningtesttask_instance.planningtest.task.id,
                    'planningtestid':planningtesttask_instance.planningtest.id]
        } else {
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
    Map updateTestLegPlanningResult(Map params)
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

            testlegplanning_instance.properties = params
			if (testlegplanning_instance.isDirty()) {
				testlegplanning_instance.test.planningTestModified = true
				testlegplanning_instance.test.crewResultsModified = true
			}
	
            Map ret = calculateLegPlanningInstance(testlegplanning_instance,false)
            if (ret)
            {
            	return ret
            }
            
            calculateTestPenalties(testlegplanning_instance.test,false)
            
            if(!testlegplanning_instance.hasErrors() && testlegplanning_instance.save()) {
				String msg = "${getMsg('fc.updated',["${testlegplanning_instance.name()}"])} ${getMsg('fc.testlegplanning.points',["${testlegplanning_instance.penaltyTrueHeading}","${testlegplanning_instance.penaltyLegTime}"])}"
                return ['instance':testlegplanning_instance,'saved':true,'message':msg]
            } else {
            	return ['instance':testlegplanning_instance]
            }
        } else {
            return ['message':getMsg('fc.notfound',[getMsg('fc.testlegplanningresult'),params.id])]
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
	            String input_time = testLegPlanningInstance.resultLegTimeInput.replace('.',':')
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
        int plan_trueheading = FcMath.Grads(testLegPlanningInstance.planTrueHeading)
        if (plan_trueheading > 180) {
            plan_trueheading -= 360
        }
        int result_trueheading = testLegPlanningInstance.resultTrueHeading
        if (result_trueheading > 180) {
            result_trueheading -= 360
        }
        int diff_trueheading =  Math.abs(plan_trueheading - result_trueheading)
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
    Map updateCoordResult(Map params)
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
				coordresult_instance.test.crewResultsModified = true
			}
				
            Map ret = calculateCoordResultInstance(coordresult_instance,false,false)
            if (ret)
            {
                return ret
            }
            
            calculateTestPenalties(coordresult_instance.test,false)
	
            if(!coordresult_instance.hasErrors() && coordresult_instance.save()) {
				String altitude_points = "0"
				if (coordresult_instance.resultAltitude && coordresult_instance.resultMinAltitudeMissed) {
					altitude_points = coordresult_instance.test.GetFlightTestMinAltitudeMissedPoints().toString()
				}
				String msg = "${getMsg('fc.updated',["${coordresult_instance.name()}"])} ${getMsg('fc.coordresult.points',[altitude_points,"${coordresult_instance.penaltyCoord}","${coordresult_instance.resultBadCourseNum * coordresult_instance.test.GetFlightTestBadCoursePoints()}"])}"
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
				coordresult_instance.test.crewResultsModified = true
			}
            coordresult_instance.resultProcedureTurnEntered = true
			
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
            coordresult_instance.ResetResults()

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
	            String input_time = coordResultInstance.resultCpTimeInput.replace('.',':')
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
			GregorianCalendar result_cptime = new GregorianCalendar() 
			result_cptime.setTime(coordResultInstance.resultCpTime)
        	Date timezone_date = Date.parse("HH:mm",contest_instance.timeZone)
        	GregorianCalendar timezone_calendar = new GregorianCalendar()
        	timezone_calendar.setTime(timezone_date)
        	result_cptime.add(Calendar.HOUR_OF_DAY, timezone_calendar.get(Calendar.HOUR_OF_DAY))
			if (contest_instance.timeZone.startsWith("-")) {
        		result_cptime.add(Calendar.MINUTE, -timezone_calendar.get(Calendar.MINUTE))
			} else {
        		result_cptime.add(Calendar.MINUTE, timezone_calendar.get(Calendar.MINUTE))
			}
        	coordResultInstance.resultCpTime = result_cptime.getTime()
        }
		
        // calculate penaltyCoord
		Test test_instance = coordResultInstance.test
		String cp = "${coordResultInstance.test.task.disabledCheckPoints},"
        if (cp.contains("${coordResultInstance.title()},")) {
			coordResultInstance.penaltyCoord = 0
        } else if (coordResultInstance.resultCpNotFound) {
        	coordResultInstance.penaltyCoord = test_instance.GetFlightTestCpNotFoundPoints()
        } else {
	        int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
	        int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
	        
	        int diffCpTime =  Math.abs(plancptime_seconds - resultcptime_seconds)
	        if (diffCpTime > test_instance.GetFlightTestCptimeCorrectSecond()) {
	            coordResultInstance.penaltyCoord = test_instance.GetFlightTestCptimePointsPerSecond() * (diffCpTime - test_instance.GetFlightTestCptimeCorrectSecond())
	        } else {
	            coordResultInstance.penaltyCoord = 0
	        }
			if (test_instance.GetFlightTestCptimeMaxPoints() > 0) {
		        if (coordResultInstance.penaltyCoord > test_instance.GetFlightTestCptimeMaxPoints()) {
		        	coordResultInstance.penaltyCoord = test_instance.GetFlightTestCptimeMaxPoints()
		        }
			}
        }
        coordResultInstance.resultEntered = true
        
        // calculate resultMinAltitudeMissed
        if (coordResultInstance.resultAltitude) {
        	coordResultInstance.resultMinAltitudeMissed = coordResultInstance.resultAltitude < coordResultInstance.altitude
        }
        
		println "  Ok: '$coordResultInstance.resultCpTime'"
        return [:]
    }
    
    //--------------------------------------------------------------------------
    private void calculateCoordResultInstancePenaltyCoord(CoordResult coordResultInstance)
    {
        Test test_instance = coordResultInstance.test
		String cp = "${coordResultInstance.test.task.disabledCheckPoints},"
		
        if (cp.contains("${coordResultInstance.title()},")) {
			coordResultInstance.penaltyCoord = 0
        } else if (coordResultInstance.resultCpNotFound) {
        	coordResultInstance.penaltyCoord = test_instance.GetFlightTestCpNotFoundPoints()
        } else {
	        int plancptime_seconds = FcMath.Seconds(coordResultInstance.planCpTime)
	        int resultcptime_seconds = FcMath.Seconds(coordResultInstance.resultCpTime)
	        
	        int diffCpTime =  Math.abs(plancptime_seconds - resultcptime_seconds)
	        if (diffCpTime > test_instance.GetFlightTestCptimeCorrectSecond()) {
	            coordResultInstance.penaltyCoord = test_instance.GetFlightTestCptimePointsPerSecond() * (diffCpTime - test_instance.GetFlightTestCptimeCorrectSecond())
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
		return AviationMath.calculateLeg(newCoordRouteInstance.latMath(),
			      				         newCoordRouteInstance.lonMath(),
										 lastCoordRouteInstance.latMath(),
										 lastCoordRouteInstance.lonMath())
    }
    
    //--------------------------------------------------------------------------
    private void newLeg(Route route, CoordRoute newCoordRouteInstance, CoordRoute lastCoordRouteInstance, CoordRoute lastCoordRouteTestInstance, BigDecimal measureDistance, BigDecimal lastMapMeasureDistance, BigDecimal lastMapMeasureTrueTrack) 
    {
    	printstart "newLeg: ${route.name()} ${newCoordRouteInstance.title()}"
    	
        // create routelegcoord_instance
    	if (lastCoordRouteInstance) {
    		Map legDataCoord = calculateLegData(newCoordRouteInstance, lastCoordRouteInstance)
	
	        String title_coord = "${lastCoordRouteInstance.titleCode()} -> ${newCoordRouteInstance.titleCode()}"
	        
	        RouteLegCoord routelegcoord_instance = new RouteLegCoord([coordTrueTrack:legDataCoord.dir,
	                                                                  coordDistance:legDataCoord.dis,
	                                                                  route:route,
	                                                                  title:title_coord,
																	  measureDistance:measureDistance,
	                                                                  legMeasureDistance:newCoordRouteInstance.legMeasureDistance,
	                                                                  legDistance:newCoordRouteInstance.legDistance,
	                                                                  measureTrueTrack:newCoordRouteInstance.measureTrueTrack
	                                                      ]) 
	        routelegcoord_instance.save()
        }

        // create routelegtest_instance
        if (lastCoordRouteInstance && lastCoordRouteTestInstance) {
            Map legDataTest = calculateLegData(newCoordRouteInstance, lastCoordRouteTestInstance)
            
	        String title_test = "${lastCoordRouteTestInstance.titleCode()} -> ${newCoordRouteInstance.titleCode()}"
            
	        if ( (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.TP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.SP && newCoordRouteInstance.type == CoordType.FP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.TP) ||
	        	 (lastCoordRouteTestInstance.type == CoordType.TP && newCoordRouteInstance.type == CoordType.FP) )
	        {
	        	RouteLegTest routelegtest_instance = new RouteLegTest([coordTrueTrack:legDataTest.dir,
	        	                                                       coordDistance:legDataTest.dis,
	        	                                                       route:route,
	        	                                                       title:title_test,
																	   measureDistance:measureDistance,
	                                                                   legMeasureDistance:lastMapMeasureDistance,
	                                                                   legDistance:calculateMapDistance(route.contest,lastMapMeasureDistance),
	                                                                   measureTrueTrack:lastMapMeasureTrueTrack
	                                                        ])
	        	routelegtest_instance.save()
	        }
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal calculateMapDistance(Contest contestInstance, BigDecimal mapMeasureDistance)
    {
    	if (mapMeasureDistance == null) {
    		return null
    	}
  		return contestInstance.mapScale * mapMeasureDistance / mmPerNM
    }

    //--------------------------------------------------------------------------
    private BigDecimal calculateMapMeasure(Contest contestInstance, BigDecimal distanceValue)
    {
    	if (distanceValue == null) {
    		return null
    	}
  		return distanceValue * mmPerNM / contestInstance.mapScale 
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
        BigDecimal last_mapmeasuredistance = null
        CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
      		last_mapmeasuredistance = addMapDistance(last_mapmeasuredistance,coordroute_instance.legMeasureDistance)
            newLeg(
				routeInstance, 
				coordroute_instance, 
				last_coordroute_instance, 
				last_coordroute_test_instance, 
				coordroute_instance.measureDistance, 
				last_mapmeasuredistance, 
				coordroute_instance.measureTrueTrack
			)
            last_coordroute_instance = coordroute_instance
            switch (coordroute_instance.type) {
	            case CoordType.SP:
	            case CoordType.TP:
	            case CoordType.FP:
	            	last_coordroute_test_instance = coordroute_instance
	            	last_mapmeasuredistance = null
	            	break
            }
        }
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculateSecretLegRatio(Route routeInstance)
    {
        printstart "calculateSecretLegRatio: '${routeInstance.name()}'"
        
        CoordRoute start_coordroute_instance
        CoordRoute start_coordroute_instance2
        CoordType last_coordtype
        BigDecimal last_legdirection = null
        
        CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance ->
        	switch (coordroute_instance.type) {
        		case CoordType.SP:
        		case CoordType.TP:
        			start_coordroute_instance = coordroute_instance
        			break
        		case CoordType.SECRET:
	        		
        			// search end_coordroute_instance, end_distance and secret_distance
	        		CoordRoute end_coordroute_instance
					BigDecimal end_distance = 0
					BigDecimal secret_distance = 0
	        		boolean leg_found = false
					boolean secret_found = false
					boolean no_end_distance = false
	        		CoordRoute.findAllByRoute(routeInstance,[sort:"id"]).each { CoordRoute coordroute_instance2 ->
	                    if (leg_found) {
	                        switch (coordroute_instance2.type) {
	                        	case CoordType.TP:
                                case CoordType.FP:
                                	end_coordroute_instance = coordroute_instance2
                                	leg_found = false
                                	break
	                        }
							if (!no_end_distance) {
								if (coordroute_instance2.measureDistance) {
									end_distance += coordroute_instance2.legDistance
								} else {
									end_distance = 0
									no_end_distance = true
								}
							}
							if (secret_found) {
		                        switch (coordroute_instance2.type) {
									case CoordType.SECRET:
										if (coordroute_instance2.measureDistance) {
											secret_distance += coordroute_instance2.legDistance
										} else {
											secret_distance = 0
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
	        		}

					// calculate secretLegRatio
	        		if (end_coordroute_instance) {
						if (secret_distance && end_distance) { // calculate from measure
							BigDecimal new_secret_leg_ratio = FcMath.RoundDistance(secret_distance) / FcMath.RoundDistance(end_distance)
							if (new_secret_leg_ratio != coordroute_instance.secretLegRatio) { 
								BigDecimal old_secret_leg_ratio = coordroute_instance.secretLegRatio 
								coordroute_instance.secretLegRatio = new_secret_leg_ratio
								coordroute_instance.save()
								println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} (Measure) (modified from $old_secret_leg_ratio)"
							} else {
								println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} (Measure)"
							}
						} else { // calulate from coordinates
			                Map end_legdata = calculateLegData(end_coordroute_instance, start_coordroute_instance)
			        		Map secret_legdata = calculateLegData(coordroute_instance, start_coordroute_instance)
			        		
			        		// calculate secretLegRatio
			        		if (end_legdata.dis > 0) {
								BigDecimal new_secret_leg_ratio = FcMath.RoundDistance(secret_legdata.dis) / FcMath.RoundDistance(end_legdata.dis)
								if (new_secret_leg_ratio != coordroute_instance.secretLegRatio) {
									BigDecimal old_secret_leg_ratio = coordroute_instance.secretLegRatio 
									coordroute_instance.secretLegRatio = new_secret_leg_ratio
									coordroute_instance.save()
									println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} (Coordinate) (modified from $old_secret_leg_ratio)"
								} else {
									println "$coordroute_instance.mark ${coordroute_instance.titleWithRatio()} (Coordinate)"
								}
			        		}
						}
	        		}
	        		break
        	}

        	// calculate planProcedureTurn
        	if (last_coordtype == CoordType.TP) {
                Map leg_data = calculateLegData(coordroute_instance, start_coordroute_instance2)
                if (last_legdirection != null) {
                    BigDecimal diffTrack = leg_data.dir - last_legdirection
                    if (diffTrack < 0) {
                        diffTrack += 360
                    }
                    if (diffTrack >= 90 && diffTrack < 270) {
                        coordroute_instance.planProcedureTurn = true
                        coordroute_instance.save()
                    }
                }
                last_legdirection = leg_data.dir
        	}
            switch (coordroute_instance.type) {
            	case CoordType.SP:
            		start_coordroute_instance2 = coordroute_instance 
            		break
                case CoordType.TP:
                case CoordType.FP:
                    start_coordroute_instance2 = coordroute_instance 
		            break
            }
        	last_coordtype = coordroute_instance.type
        }
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private BigDecimal addMapDistance(lastMapMeasureDistance, addmeasuredistance)
    {
	    if (addmeasuredistance != null) {
	    	if (lastMapMeasureDistance != null) {
	    		lastMapMeasureDistance += addmeasuredistance
	    	} else {
	    		lastMapMeasureDistance = addmeasuredistance
	    	}
	    }
    }
    
    //--------------------------------------------------------------------------
    private void addTestingTime(Task taskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // add testingTime
            time.add(Calendar.MINUTE, taskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
			testInstance.ResetFlightTestResults()
			testInstance.CalculateTestPenalties()
            testInstance.save()
            
            calulateCoordResult(testInstance)
            calulateTimetableWarnings(taskInstance)
			
			taskInstance.timetableModified = true
			taskInstance.save()
			testInstance.timetableVersion = taskInstance.timetableVersion + 1 
        }
    }
  
    //--------------------------------------------------------------------------
    private void subtractTestingTime(Task taskInstance, Test testInstance)
    {
        if (testInstance.timeCalculated) {
            
            GregorianCalendar time = new GregorianCalendar() 
            time.setTime(testInstance.testingTime)
            
            // subtract testingTime
            time.add(Calendar.MINUTE, -taskInstance.addTimeValue)
            testInstance.testingTime = time.getTime()
            
            // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
            calculateTimes(time, taskInstance, testInstance)
			testInstance.ResetFlightTestResults()
			testInstance.CalculateTestPenalties()
            testInstance.save()
            
            calulateCoordResult(testInstance)
            calulateTimetableWarnings(taskInstance)

			taskInstance.timetableModified = true
			taskInstance.save()
			testInstance.timetableVersion = taskInstance.timetableVersion + 1 
		}
    }

    //--------------------------------------------------------------------------
    private void calulateTimetableWarnings(Task taskInstance)
    {
        Date first_date = Date.parse("HH:mm",taskInstance.firstTime)
        Date last_arrival_time = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
            
            // calculate arrivalTimeWarning by arrivalTime
            test_instance.arrivalTimeWarning = false
            if (last_arrival_time > test_instance.arrivalTime) {
                test_instance.arrivalTimeWarning = true
            }
            last_arrival_time = test_instance.arrivalTime
            
            // calculate takeoffTimeWarning by aircraft
            test_instance.takeoffTimeWarning = false
            boolean found_aircraft = false
            GregorianCalendar last_takeoff_time = null
            Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
                if (test_instance.crew.aircraft == test_instance2.crew.aircraft) {
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
            if (last_takeoff_time) {
                if (test_instance.takeoffTime < last_takeoff_time.getTime()) {
                    test_instance.takeoffTimeWarning = true
                }
            }
            
            test_instance.save()
        }

    }
 
    //--------------------------------------------------------------------------
    private int calulateTimetable(Task taskInstance)
    {
        printstart "calulateTimetable: ${taskInstance.name()}"
        
		int calculated_crew_num = 0
		
        Date first_date = Date.parse("HH:mm",taskInstance.firstTime)
        GregorianCalendar first_time = new GregorianCalendar() 
        first_time.setTime(first_date)

        GregorianCalendar start_time = new GregorianCalendar()
        start_time.set(Calendar.HOUR_OF_DAY, first_time.get(Calendar.HOUR_OF_DAY))
        start_time.set(Calendar.MINUTE,      first_time.get(Calendar.MINUTE))
        start_time.set(Calendar.SECOND,      0)

        BigDecimal last_task_tas = 9000.0
        Date last_arrival_time = first_date
        
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
				if (test_instance.flighttestwind) {
			        if (test_instance.taskTAS > last_task_tas) { // faster aircraft
			        	start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalFasterAircraft - taskInstance.takeoffIntervalNormal)
			        }
			        
					if (!test_instance.timeCalculated) {
						calculateTime(test_instance, taskInstance, start_time, last_arrival_time)
						calulateCoordResult(test_instance)
						calculated_crew_num++
					}
			
					// next 
			        start_time.add(Calendar.MINUTE, taskInstance.takeoffIntervalNormal)
			        last_task_tas = test_instance.taskTAS
		            last_arrival_time = test_instance.arrivalTime
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
	private void calculateTime(Test testInstance, Task taskInstance, GregorianCalendar startTime, Date lastArrivalTime)
	{
    	printstart "calculateTime: ${testInstance.crew.name}"
    	
        // calculate testingTime
        GregorianCalendar time = startTime.clone()
        testInstance.testingTime = time.getTime()
        
        // calulate endTestingTime, takeoffTime, startTime, finishTime, arrivalTime
        calculateTimes(time, taskInstance, testInstance)
        
        // calculate arrivalTimeWarning by arrivalTime
        testInstance.arrivalTimeWarning = false
		if (lastArrivalTime) {
	        if (lastArrivalTime > testInstance.arrivalTime) {
	            testInstance.arrivalTimeWarning = true
	        }
		}
        
        // calculate takeoffTimeWarning by aircraft
        testInstance.takeoffTimeWarning = false
        boolean found_aircraft = false
        GregorianCalendar last_takeoff_time = null
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance2 ->
            if (testInstance.crew.aircraft == test_instance2.crew.aircraft) {
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
            }
        }

        testInstance.timeCalculated = true
		testInstance.timetableVersion = taskInstance.timetableVersion + 1 
		testInstance.ResetFlightTestResults()
		testInstance.CalculateTestPenalties()
        testInstance.save()
		
		printdone ""
	}
	
    //--------------------------------------------------------------------------
    private void calculateTimes(GregorianCalendar time, Task taskInstance, Test testInstance) 
    {
        // calulate endTestingTime
        time.add(Calendar.MINUTE, taskInstance.planningTestDuration)
        testInstance.endTestingTime = time.getTime()
            
        // calulate takeoffTime
        time.add(Calendar.MINUTE, taskInstance.preparationDuration)
        testInstance.takeoffTime = time.getTime()
        
        // calulate startTime
        time.add(Calendar.MINUTE, taskInstance.risingDuration)
        testInstance.startTime = time.getTime()
        
        // calculate finishTime
        time.add(Calendar.SECOND, getLegsPlanTimeSeconds(testInstance))
        testInstance.finishTime = time.getTime()
        GregorianCalendar finish_time = time.clone()
        
        // calculate maxLandingTime
        finish_time.add(Calendar.MINUTE, taskInstance.maxLandingDuration)
        testInstance.maxLandingTime = finish_time.getTime() 

        // calculate arrivalTime
        time.add(Calendar.MINUTE, taskInstance.parkingDuration)
        testInstance.arrivalTime = time.getTime()
        
    }
    
    //--------------------------------------------------------------------------
    private int getLegsPlanTimeSeconds(Test testInstance)
    {
		Date legs_time = Date.parse("HH:mm","00:00")
        TestLegFlight.findAllByTest(testInstance,[sort:"id"]).each { TestLegFlight testlegflight_instance ->
			legs_time = testlegflight_instance.AddPlanLegTime(legs_time)
        }
        return FcMath.Seconds(legs_time)
    }

    //--------------------------------------------------------------------------
    private void calulateTestLegFlights(Task taskInstance)
    {
        printstart "calulateTestLegFlights: ${taskInstance.name()}"
		boolean something_calculated = false
        Test.findAllByTask(taskInstance,[sort:"viewpos"]).each { Test test_instance ->
			if (!test_instance.crew.disabled) {
				if (test_instance.flighttestwind) {
					if (!test_instance.timeCalculated) {
						calulateTestLegFlight(test_instance)
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
    private void calulateTestLegFlight(Test testInstance)
    {
        printstart "calulateTestLegFlight: ${testInstance.crew.name}"
        
        // remove all TestLegFlights
        TestLegFlight.findAllByTest(testInstance,[sort:"id"]).each { TestLegFlight testlegflight_instance ->
            testlegflight_instance.delete(flush:true)
			println "calulateTestLegFlight: TestLegFlight instance deleted (flush:true)."
        }

        // calculate TestLegFlights
        BigDecimal last_truetrack = null
        RouteLegTest.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route,[sort:"id"]).each { RouteLegTest routelegtest_instance ->
            
            TestLegFlight testlegflight_instance = new TestLegFlight()
            calculateLeg(testlegflight_instance, routelegtest_instance, testInstance.flighttestwind.wind, testInstance.taskTAS, testInstance.task.procedureTurnDuration, last_truetrack)
            testlegflight_instance.test = testInstance
            testlegflight_instance.save()

            last_truetrack = testlegflight_instance.planTrueTrack
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calulateCoordResult(Test testInstance)
    {
		printstart "calulateCoordResult: ${testInstance.crew.name}"
		
        // remove all coordResultInstances
        CoordResult.findAllByTest(testInstance,[sort:"id"]).each { CoordResult coordresult_instance ->
            coordresult_instance.delete(flush:true)
			println "calulateCoordResult: CoordResult instance deleted (flush:true)."
        }

        // create coordResultInstances
        int coord_index = 0
        CoordRoute.findAllByRoute(testInstance?.flighttestwind?.flighttest?.route,[sort:"id"]).each { CoordRoute coordroute_instance ->
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
            coordresult_instance.gatewidth = coordroute_instance.gatewidth
            coordresult_instance.planProcedureTurn = coordroute_instance.planProcedureTurn
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
                    coord_index++
                    Date cp_time = testInstance.startTime
                    TestLegFlight.findAllByTest(testInstance,[sort:"id"]).eachWithIndex { TestLegFlight testlegflight_instance, int leg_index ->
                        cp_time = testlegflight_instance.AddPlanLegTime(cp_time)
                        if (coord_index == leg_index + 1) {
                            coordresult_instance.planCpTime = cp_time
                        }
                    }
                    break
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
            }
            coordresult_instance.test = testInstance
            switch (coordroute_instance.type) {
                case CoordType.TO:
                case CoordType.LDG:
                    // ignore Takeoff and Landing
                    break
                default:
                    coordresult_instance.save()
                    break
            }
        }
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
        BigDecimal last_truetrack = null
        RouteLegTest.findAllByRoute(route_instance,[sort:"id"]).each { RouteLegTest routelegtest_instance ->
            TestLegPlanning testlegplanning_instance = new TestLegPlanning()
            calculateLeg(testlegplanning_instance, routelegtest_instance, testInstance.planningtesttask.wind, testInstance.taskTAS, testInstance.planningtesttask.planningtest.task.procedureTurnDuration, last_truetrack)
            testlegplanning_instance.test = testInstance
            if (!testInstance.IsPlanningTestDistanceMeasure()) {
                testlegplanning_instance.resultTestDistance = testlegplanning_instance.planTestDistance
            }
            if (!testInstance.IsPlanningTestDirectionMeasure()) {
                testlegplanning_instance.resultTrueTrack = testlegplanning_instance.planTrueTrack
            }
            testlegplanning_instance.save()
            last_truetrack = testlegplanning_instance.planTrueTrack
        }
		
		printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculateLeg(TestLeg testLeg, RouteLeg routeLegInstance, Wind windInstance, BigDecimal valueTAS, int procedureTurnDuration, BigDecimal lastTrueTrack) 
    {
        // save route data
        testLeg.planTestDistance = routeLegInstance.testDistance()
        testLeg.planTrueTrack = routeLegInstance.testTrueTrack()

        // calculate wind
	    Map ret = AviationMath.calculateWind(windInstance.direction, windInstance.speed, valueTAS,
		                                     testLeg.planTrueTrack, testLeg.planTestDistance)
	    testLeg.planTrueHeading = ret.trueheading
	    testLeg.planGroundSpeed = ret.groundspeed
	    testLeg.planLegTime = ret.legtime 
	   
        // calculate procedure turn
        testLeg.planProcedureTurn = false
        testLeg.planProcedureTurnDuration = 0
        if (lastTrueTrack != null) {
            BigDecimal diffTrack = testLeg.planTrueTrack - lastTrueTrack
            if (diffTrack < 0) {
                diffTrack += 360
            }
            if (diffTrack >= 90 && diffTrack < 270) {
        	    testLeg.planProcedureTurn = true
        	    testLeg.planProcedureTurnDuration = procedureTurnDuration
            }
        }

		println "calculateLeg: ${routeLegInstance.testDistance()}, ${routeLegInstance.testTrueTrack()} -> ${testLeg.planTrueHeading}, ${testLeg.planGroundSpeed}, ${FcMath.TimeStr(testLeg.planLegTime)}"
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
    Map putContest(String title, int mapScale, boolean resultclasses, int teamCrewNum, ContestRules contestRule, boolean aflosTest, boolean testExists)
    {
		printstart "putContest"
        Map p = [:]
        p.title = title
        p.mapScale = mapScale
		p.resultClasses = resultclasses
		p.teamCrewNum = teamCrewNum
		p.contestRule = contestRule
		p.aflosTest = aflosTest
		p.testExists = testExists
        Map ret = saveContest(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putTask(Map contest, String title, String firstTime, int takeoffIntervalNormal, int risingDuration,
		        boolean planningTestRun, boolean flightTestRun, boolean observationTestRun, boolean landingTestRun, boolean specialTestRun,
		 		boolean planningTestDistanceMeasure, boolean planningTestDirectionMeasure,
				boolean landingTest1Run, boolean landingTest2Run, boolean landingTest3Run, boolean landingTest4Run)
    {
		printstart "putTask"
        Map p = [:]
        p.title = title
		p.firstTime = firstTime
		p.takeoffIntervalNormal = takeoffIntervalNormal
		p.risingDuration = risingDuration
		p.planningTestRun = planningTestRun
		p.flightTestRun = flightTestRun
		p.observationTestRun = observationTestRun
		p.landingTestRun = landingTestRun
		p.specialTestRun = specialTestRun
		p.planningTestDistanceMeasure = planningTestDistanceMeasure
		p.planningTestDirectionMeasure = planningTestDirectionMeasure
		p.landingTest1Run = landingTest1Run
		p.landingTest2Run = landingTest2Run
		p.landingTest3Run = landingTest3Run
		p.landingTest4Run = landingTest4Run
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
			setContestRule(resultclass_instance, resultclass_instance.contestRule)
			if(!resultclass_instance.hasErrors() && resultclass_instance.save()) {
				Map ret = ['instance':resultclass_instance,'saved':true,'message':getMsg('fc.created',["${resultclass_instance.name}"])]
				printdone ret
				return ret
			} else {
				Map ret = ['instance':resultclass_instance]
				printdone ret
				return ret
			}
		} else {
	        Map p = [:]
	        p.name = name
			p.contestTitle = contestTitle
			p.contestRule = contestRule
	        Map ret = saveResultClass(p,contest.instance)
			printdone ret
			return ret
		}
    }
    
    //--------------------------------------------------------------------------
    void puttaskclassTask(Map task, Map resultClass,
		                  boolean planningTestRun, boolean flightTestRun, boolean observationTestRun, boolean landingTestRun, boolean specialTestRun,
		 		          boolean planningTestDistanceMeasure, boolean planningTestDirectionMeasure,
						  boolean landingTest1Run, boolean landingTest2Run, boolean landingTest3Run, boolean landingTest4Run)
	{
		printstart "puttaskclassTask"
		
		Task task_instance = task.instance
		for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance,[sort:"id"])) {
			if (taskclass_instance.resultclass == resultClass.instance) {
				taskclass_instance.planningTestRun = planningTestRun
				taskclass_instance.flightTestRun = flightTestRun
				taskclass_instance.observationTestRun = observationTestRun
				taskclass_instance.landingTestRun = landingTestRun
				taskclass_instance.specialTestRun = specialTestRun
				taskclass_instance.planningTestDistanceMeasure = planningTestDistanceMeasure
				taskclass_instance.planningTestDirectionMeasure = planningTestDirectionMeasure
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
    Map putRoute(Map contest, String title, String mark)
    {
		printstart "putRoute"
        Map p = [:]
        p.title = title
        p.mark = mark
        Map ret = saveRoute(p,contest.instance)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map putCoordRoute(Map route, CoordType type, int titleNumber, String mark, String latDirection, int latGrad, BigDecimal latMinute, String lonDirection, int lonGrad, BigDecimal lonMinute, int altitude, int gatewidth, BigDecimal measureDistance, BigDecimal measureTrueTrack)
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
        p.latMinute = latMinute
        p.latDirection = latDirection
        p.lonGrad = lonGrad
        p.lonMinute = lonMinute
        p.lonDirection = lonDirection
        p.altitude = altitude 
        p.gatewidth = gatewidth
		p.measureDistance = measureDistance
        p.measureTrueTrack = measureTrueTrack 
        Map ret = saveCoordRoute(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putCrew(Map contest, String name, String teamname, String resultclassname, String registration, String type, String colour, BigDecimal tas)
    {
		printstart "putCrew"
        Map p = [:]
        p.name = name
		p.teamname = teamname
		p.resultclassname = resultclassname 
        p.registration = registration
        p.type = type
        p.colour = colour
        p.tas = tas
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
        p.direction = 0
        p.speed = 0
        Map ret = saveFlightTest(p)
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
        p.direction = direction
        p.speed = speed
        Map ret = saveFlightTest(p)
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map putFlightTestWind(Map flighttest, BigDecimal direction, BigDecimal speed)
    {
		printstart "putFlightTestWind"
        Map p = [:]
        p.flighttestid = flighttest.instance.id
        p.direction = direction
        p.speed = speed
        Map ret = saveFlightTestWind(p)
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
        p.direction = 0
        p.speed = 0
        Map ret = savePlanningTest(p)
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
        p.direction = direction
        p.speed = speed
        Map ret = savePlanningTest(p)
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
        p.direction = direction
        p.speed = speed
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
		            test_instance.planningTestGivenTooLate = crew_result.givenTooLate
		            test_instance.planningTestExitRoomTooLate = crew_result.exitRoomTooLate
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
    void importflightresultsTask(Map task, List crewResults)
    {
		printstart "importflightresultsTask"
		Task task_instance = task.instance

		crewResults.each { Map crew_result ->
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_result.crew.instance) {
					Map ret = importAflosResults(test_instance, crew_result.startNum)
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
        testInstance.flightTestTakeoffMissed = crewResult.takeoffMissed
        testInstance.flightTestBadCourseStartLanding = crewResult.badCourseStartLanding
        testInstance.flightTestLandingTooLate = crewResult.landingTooLate
        testInstance.flightTestGivenTooLate = crewResult.givenTooLate
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
					test_instance.observationTestRoutePhotoPenalties = crew_result.routePhotos
					test_instance.observationTestTurnPointPhotoPenalties = crew_result.turnPointPhotos
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
						test_instance.landingTest2Measure = crew_result.landingTest2Measure
						test_instance.landingTest2Landing = crew_result.landingTest2Landing
						test_instance.landingTest2RollingOutside = crew_result.landingTest2RollingOutside
						test_instance.landingTest2PowerInBox = crew_result.landingTest2PowerInBox
						test_instance.landingTest2GoAroundWithoutTouching = crew_result.landingTest2GoAroundWithoutTouching
						test_instance.landingTest2GoAroundInsteadStop = crew_result.landingTest2GoAroundInsteadStop
						test_instance.landingTest2AbnormalLanding = crew_result.landingTest2AbnormalLanding
						test_instance.landingTest2PowerInAir = crew_result.landingTest2PowerInAir
						test_instance.landingTest3Measure = crew_result.landingTest3Measure
						test_instance.landingTest3Landing = crew_result.landingTest3Landing
						test_instance.landingTest3RollingOutside = crew_result.landingTest3RollingOutside
						test_instance.landingTest3PowerInBox = crew_result.landingTest3PowerInBox
						test_instance.landingTest3GoAroundWithoutTouching = crew_result.landingTest3GoAroundWithoutTouching
						test_instance.landingTest3GoAroundInsteadStop = crew_result.landingTest3GoAroundInsteadStop
						test_instance.landingTest3AbnormalLanding = crew_result.landingTest3AbnormalLanding
						test_instance.landingTest3PowerInAir = crew_result.landingTest3PowerInAir
						test_instance.landingTest3FlapsInAir = crew_result.landingTest3FlapsInAir
						test_instance.landingTest4Measure = crew_result.landingTest4Measure
						test_instance.landingTest4Landing = crew_result.landingTest4Landing
						test_instance.landingTest4RollingOutside = crew_result.landingTest4RollingOutside
						test_instance.landingTest4PowerInBox = crew_result.landingTest4PowerInBox
						test_instance.landingTest4GoAroundWithoutTouching = crew_result.landingTest4GoAroundWithoutTouching
						test_instance.landingTest4GoAroundInsteadStop = crew_result.landingTest4GoAroundInsteadStop
						test_instance.landingTest4AbnormalLanding = crew_result.landingTest4AbnormalLanding
						test_instance.landingTest4TouchingObstacle = crew_result.landingTest4TouchingObstacle
					} else {
						test_instance.landingTestPenalties = crew_result.landingPenalties
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
					test_instance.save()
					view_pos++
				}
			}
		}
		printdone ""
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
		Date first_date = Date.parse("HH:mm",task_instance.firstTime)

		calulateTestLegFlights(task_instance)
		
		crewStartTimes.each { Map crew_start_time ->
			printstart "$crew_start_time.crew.instance.name $crew_start_time.starttime"
			Date last_arrival_time = first_date
			
	        Test.findAllByTask(task_instance,[sort:"viewpos"]).each { Test test_instance ->
				if (test_instance.crew == crew_start_time.crew.instance) {
					Date start_date = Date.parse("HH:mm",crew_start_time.starttime)
					GregorianCalendar start_time = new GregorianCalendar() 
					start_time.setTime(start_date)
					
					if (!test_instance.timeCalculated) {
						calculateTime(test_instance, task_instance, start_time, last_arrival_time)
						calulateCoordResult(test_instance)
						last_arrival_time = test_instance.arrivalTime
					}
				}
	        }
			
			printdone ""
		}
		
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
    Map runcalculatepositionsTask(Map task)
    {
		printstart "runcalculatepositionsTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatepositionsTask(p)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculatecontestpositionsContest(Map contest, List contestClasses, List contestTasks)
    {
		printstart "runcalculatecontestpositionsContest [$contestClasses] [$contestTasks]"
        Map ret = calculatecontestpositionsContest(contest.instance,contestClasses,contestTasks)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculatepositionsResultClass(Map resultclass, List contestTasks)
    {
		printstart "runcalculatepositionsResultClass"
        Map ret = calculatepositionsResultClass(resultclass.instance,contestTasks)
		printdone ret
		return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculateteampositionsContest(Map contest, List teamClasses, List teamTasks)
    {
		printstart "runcalculateteampositionsContest [$teamClasses] [$teamTasks]"
        Map ret = calculateteampositionsContest(contest.instance,teamClasses,teamTasks)
		printdone ret
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
			def value1 = recurseValue.(key.toString())
			if (value == null) {
				if (value1 != value) {
					tableErrorNum = add_error_num(tableErrorNum, tableName)
					println "${testDataIndex+1}: field '$key' has different value '$value1' ['$value' expected]."
				} 
			} else if (!value?.class) {
				tableErrorNum = test_data(value, testDataIndex, tableErrorNum, tableDatum, tableName, value1)
			} else if (value1 != value) {
				tableErrorNum = add_error_num(tableErrorNum, tableName)
				println "${testDataIndex+1}: field '$key' has different value '$value1' ['$value' expected]."
			} 
		}
		return tableErrorNum
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
    void WritePDF(response,content) {
		printstart "WritePDF"
        byte[] b = content.toByteArray()
        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=print.pdf")
        response.setContentLength(b.length)
        response.getOutputStream().write(b)
		content.close()
		printdone ""
    }
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        if (args) {
            return messageSource.getMessage(code, args.toArray(), null)
        } else {
            return messageSource.getMessage(code, null, null)
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
           return messageSource.getMessage(code, null, null)
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
