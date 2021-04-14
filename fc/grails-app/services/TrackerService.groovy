import java.util.Map
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import org.springframework.web.context.request.RequestContextHolder

// https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html

class TrackerService
{
    def messageSource
    def logService
	def fcService
    def gpxService
    def calcService
    def grailsApplication
    
    final static String ALL_DATA = "*"
    final static String EMPTY_NAME = "."
    final static String ANONYMOUS_EMAIL_DOMAIN = "anonymous.flightcontest.de"
    final static int FP_MINUTES = 2
    
    //--------------------------------------------------------------------------
    Map createContest(Map params)
    {
        printstart "createContest"
        
        Contest contest_instance = Contest.get(params.id)
        if (params.liveTrackingContestDate) {
            contest_instance.liveTrackingContestDate = params.liveTrackingContestDate
            contest_instance.save()
        }
        
        if (!contest_instance.liveTrackingContestDate) {
			Map ret = ['instance':contest_instance, 'created':false, 'message':getMsg('fc.livetracking.contestdate.notexists')]
			printerror ret
            return ret
        }
        
        BigDecimal latitude2
        BigDecimal longitude2
        for (Route route_instance in Route.findAllByContest(contest_instance)) {
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
                if (coordroute_instance.type == CoordType.TO) {
                    latitude2 = coordroute_instance.latMath()
                    longitude2 = coordroute_instance.lonMath()
                    break
                }
            }
            if (latitude2 || longitude2) {
                break
            }
        }
        if (!latitude2 || !longitude2) {
			Map ret = ['instance':contest_instance, 'created':false, 'message':getMsg('fc.livetracking.contestcoords.notexists')]
			printerror ret
            return ret
        }
        
		Integer contest_id = 0
        Map ret3 = call_rest("contests/", "GET", 200, "", ALL_DATA)
		if (ret3.ok && ret3.data) {
            for (Map contest in ret3.data) {
				if (contest.name == contest_instance.title) {
					contest_id = contest.id
				}
			}
		}
		
		if (contest_id) {
			Map ret = ['instance':contest_instance, 'created':false, 'message':getMsg('fc.livetracking.contestcreate.exists',[contest_instance.title, contest_id])]
			printerror ret
			return ret
        }
        
        String start_local_time_str = "06:00"
        if (grailsApplication.config.flightcontest?.livetracking?.contest?.startLocalTimeOfDay && grailsApplication.config.flightcontest?.livetracking?.contest?.startLocalTimeOfDay instanceof String) {
            start_local_time_str = grailsApplication.config.flightcontest?.livetracking?.contest?.startLocalTimeOfDay
        }
        Date start_local_time = Date.parse("HH:mm", start_local_time_str)
        if (!start_local_time) {
            start_local_time = Date.parse("HH:mm","06:00")
        }
        String end_local_time_str = "22:00"
        if (grailsApplication.config.flightcontest?.livetracking?.contest?.endLocalTimeOfDay && grailsApplication.config.flightcontest?.livetracking?.contest?.endLocalTimeOfDay instanceof String) {
            end_local_time_str = grailsApplication.config.flightcontest?.livetracking?.contest?.endLocalTimeOfDay
        }
        Date end_local_time = Date.parse("HH:mm", end_local_time_str)
        if (!end_local_time) {
            end_local_time = Date.parse("HH:mm","22:00")
        }
        boolean is_public2 = false
        if (grailsApplication.config.flightcontest?.livetracking?.contest?.createPublic) {
            is_public2 = true
        }
        String time_zone2 = "Europe/Oslo"
        if (contest_instance.timeZone2) {
            time_zone2 = contest_instance.timeZone2.getID()
        } else if (grailsApplication.config.flightcontest?.livetracking?.contest?.timeZone && grailsApplication.config.flightcontest?.livetracking?.contest?.timeZone instanceof String) {
            time_zone2 = grailsApplication.config.flightcontest.livetracking.contest.timeZone
        }
        JsonBuilder json_builder = new JsonBuilder()
        json_builder {
            name contest_instance.title
            time_zone time_zone2
            latitude latitude2
            longitude longitude2
            start_time FcTime.UTCGetDateTime(contest_instance.liveTrackingContestDate, start_local_time, contest_instance.timeZone)
            finish_time FcTime.UTCGetDateTime(contest_instance.liveTrackingContestDate, end_local_time, contest_instance.timeZone)
            is_public is_public2
        }
        
        Map ret1 = call_rest("contests/", "POST", 201, json_builder.toString(), "id")
        if (!ret1.data) {
            Map ret = ['instance':contest_instance, 'created':false, 'message':getMsg('fc.livetracking.contestcreate.error',[ret1.errorMsg])]
            printerror ret
            return ret
        }
        
        contest_instance.liveTrackingContestID = ret1.data.toInteger()
        contest_instance.save()
        
        Map ret = ['instance':contest_instance, 'created':true, 'message':getMsg('fc.livetracking.contestcreate.done',[contest_instance.liveTrackingContestID])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map connectContest(Map params)
    {
        printstart "connectContest"
        
        Contest contest_instance = Contest.get(params.id)
        
		Integer contest_id = 0
        String contest_date = ""
        String time_zone = ""
        Map ret3 = call_rest("contests/", "GET", 200, "", ALL_DATA)
		if (ret3.ok && ret3.data) {
            for (Map contest in ret3.data) {
				if (contest.name == contest_instance.title) {
					contest_id = contest.id
                    if (contest.start_time.size() >= 10) {
                        contest_date = contest.start_time.substring(0,10)
                    }
                    time_zone = contest.time_zone
					break
				}
			}
		}
		
		if (contest_id) {
			contest_instance.liveTrackingContestID = contest_id
            contest_instance.liveTrackingContestDate = contest_date
            contest_instance.timeZone2 = TimeZone.getTimeZone(time_zone)
            fcService.CalculateTimezone2(contest_instance)
			contest_instance.save()
			Map ret = ['instance':contest_instance, 'connected':true, 'message':getMsg('fc.livetracking.contestconnect.done',[contest_id])]
			printdone ret
			return ret
		} else {
			Map ret = ['instance':contest_instance, 'connected':false, 'message':getMsg('fc.livetracking.contestconnect.notexists',[contest_instance.title])]
			printerror ret
			return ret
			
		}
    }
    
    //--------------------------------------------------------------------------
    Map deleteContest(Map params)
    {
        printstart "deleteContest"
        
        Contest contest_instance = Contest.get(params.id)
        Integer old_tacking_id = contest_instance.liveTrackingContestID
        
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/", "DELETE", 204, "", "")
        
        if (ret1.ok) {
            contest_instance.liveTrackingContestID = 0
			contest_instance.liveTrackingManagedCrews = false
            contest_instance.save()
            for (Task task_instance in Task.findAllByContest(contest_instance)) {
                task_instance.liveTrackingNavigationTaskID = 0
                task_instance.liveTrackingTracksAvailable = false
                //task_instance.liveTrackingNavigationTaskDate = ""
                task_instance.save()
            }
            Map ret = ['instance':contest_instance, 'deleted':true, 'message':getMsg('fc.livetracking.contestdelete.done',[old_tacking_id])]
            printdone ret
            return ret
        } else {
            Map ret = ['instance':contest_instance, 'deleted':false, 'message':getMsg('fc.livetracking.contestdelete.error',[old_tacking_id,ret1.errorMsg])]
            printerror ret
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map disconnectContest(Map params)
    {
        printstart "disconnectContest"
        
        Contest contest_instance = Contest.get(params.id)
        
		Integer old_livetracking_contestid = contest_instance.liveTrackingContestID
		contest_instance.liveTrackingContestID = 0
		contest_instance.liveTrackingManagedCrews = false
		contest_instance.save()
		for (Task task_instance in Task.findAllByContest(contest_instance)) {
			task_instance.liveTrackingNavigationTaskID = 0
            task_instance.liveTrackingTracksAvailable = false
            //task_instance.liveTrackingNavigationTaskDate = ""
			task_instance.save()
		}
		Map ret = ['instance':contest_instance, 'connected':true, 'message':getMsg('fc.livetracking.contestdisconnect.done',[old_livetracking_contestid])]
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map importTeams(Map params)
    {
        printstart "importTeams"
		
        Contest contest_instance = Contest.get(params.id)
		
		int import_num = 0
		
		Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/teams/", "GET", 200, "", ALL_DATA)
		if (ret1.ok && ret1.data) {
			
			List crew_list = []
			int start_num = 1
            for (Map team_datum in ret1.data) {
				String name = "${team_datum.team.crew.member1.first_name} ${team_datum.team.crew.member1.last_name}"
				String name2 = ""
				if (team_datum.team.crew.member2) {
					name2 = "${team_datum.team.crew.member2.first_name} ${team_datum.team.crew.member2.last_name}"
				}
				String email = ""
				if (team_datum.team.crew.member1.email) {
					email = team_datum.team.crew.member1.email
				}
				if (team_datum.team.crew.member2?.email) {
					if (email) {
						email += Defs.EMAIL_LIST_SEPARATOR
					}
					email += team_datum.team.crew.member2.email
				}
				String teamname = ""
				if (team_datum.team.club?.name) {
					teamname = team_datum.team.club.name
					if (team_datum.team.club?.country) {
						teamname += ", "
						teamname += team_datum.team.club.country
					}
				} else if (team_datum.team.country) {
					teamname = team_datum.team.country
				}
				int tas = team_datum.air_speed
				String registration = team_datum.team.aeroplane.registration
				String type = ""
				if (team_datum.team.aeroplane.type) {
					type = team_datum.team.aeroplane.type
				}
				String colour = ""
				if (team_datum.team.aeroplane.colour) {
					colour = team_datum.team.aeroplane.colour
				}
				String tracker_id = team_datum.tracker_device_id
				//if (!tracker_id) {
				//	tracker_id = get_random_tracker_id(contest_instance.liveTrackingContestID, start_num)
				//}
				Map new_crew = [startNum:start_num.toString(),
							    name:name,
								name2:name2,
								email:email,
								teamname:teamname,
								resultclassname:"",
								tas:tas.toString(),
								registration:registration,
								type:type, colour:colour,
                				liveTrackingTeamID:team_datum.team.id,
								trackerID:tracker_id
							   ]
				crew_list += new_crew
				start_num++
				import_num++
			}
			
			if (import_num) {
				import_num = fcService.importCrews2(crew_list, false, contest_instance).new_crew_num
				contest_instance.liveTrackingManagedCrews = true
				contest_instance.save()
			}
			
			Map ret = ['instance':contest_instance, 'imported':true, 'message':getMsg('fc.livetracking.teamsimport.done',[import_num])]
			printdone ret
			return ret
		} else {
			Map ret = ['instance':contest_instance, 'imported':false, 'message':getMsg('fc.livetracking.teamsimport.error',[])]
			printdone ret
			return ret
		}
	}

	//--------------------------------------------------------------------------
	private String get_random_tracker_id(Integer liveTrackingContestID, int startNum) {
		return "tracker-${liveTrackingContestID}-${startNum}-${FcTime.GetTimeStr(new Date())}"
	}
	
	//--------------------------------------------------------------------------
    Map createNavigationTask(Map params)
    {
        printstart "createNavigationTask"
        
        Task task_instance = Task.get(params.id)
        if (params.liveTrackingNavigationTaskDate) {
            task_instance.liveTrackingNavigationTaskDate = params.liveTrackingNavigationTaskDate
            task_instance.save()
        }
        Map ret4 = is_task_complete(task_instance, true)
        if (!ret4.ok) {
            Map ret = ['instance':task_instance, 'created':false, 'message':ret4.message]
            printerror ret
            return ret
        }
        
        Contest contest_instance = task_instance.contest
		Integer navigationtask_id = 0
        Map ret3 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/", "GET", 200, "", ALL_DATA)
		if (ret3.ok && ret3.data) {
            for (Map navigationtask in ret3.data) {
				if (navigationtask.name == task_instance.GetMediaName(Media.Tracking)) {
					navigationtask_id = navigationtask.id
					break
				}
			}
		}
		if (navigationtask_id) {
            Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.navigationtaskcreate.exists',[task_instance.GetMediaName(Media.Tracking),navigationtask_id])]
            printerror ret
            return ret
        }
            
        Route route_instance = task_instance.flighttest.route
        boolean tracks_available = check_tracks_availability(task_instance)
        Task task_instance2 = null
        if (tracks_available) {
            task_instance2 = task_instance // "notimecheck" and "nogatecheck" from "Results -> Disable check points"
        }
        String uuid = UUID.randomUUID().toString()
        String upload_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
        Map gpx_converter = gpxService.ConvertRoute2GPX(route_instance, upload_gpx_file_name, [isTracking:true, isPrint:false, showPoints:false, wrEnrouteSign:false, gpxExport:true, taskInstance:task_instance2])
        if (!gpx_converter.ok) {
            Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.navigationtaskcreate.error',[route_instance.liveTrackingScorecard, getMsg('fc.livetracking.navigationtaskgpxcreate.error')])]
            printerror ret
            return ret
        }
        
        String gpx_data = new String(BootStrap.tempData.GetData(upload_gpx_file_name))
        String route_data = Base64.getEncoder().encodeToString(gpx_data.getBytes())
        Date first_date = null
        if (task_instance.firstTime.size() > 5) {
            first_date = Date.parse("HH:mm:ss",task_instance.firstTime)
        } else {
            first_date = Date.parse("HH:mm",task_instance.firstTime)
        }
        Date finish_date = null
        List contestant_list = []
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.crew.disabled && !test_instance.disabledCrew && test_instance.IsFlightTestRun()) {
                Map contestant = get_contestant(test_instance, task_instance.liveTrackingNavigationTaskDate, tracks_available)
                contestant_list += contestant.data
                finish_date = contestant.arrivalTime
            }
        }
        
        if (!contestant_list) {
            Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.navigationtaskcreate.error',[route_instance.liveTrackingScorecard, getMsg('fc.livetracking.navigationtaskcontestantlistcreate.error')])]
            printerror ret
            return ret
        }
        
        Map gate_types = [:]
        boolean class_evaluation = false
        if (contest_instance.resultClasses) {
            if (contest_instance.contestRuleForEachClass) {
                class_evaluation = true
                gate_types = [TO:false, SP:false, TP:false, SC:false, FP:false, LDG:false]
            } else {
                gate_types = [TO:false, SP:true, TP:true, SC:false, FP:true, LDG:false]
            }
        } else {
            gate_types = [TO:true, SP:true, TP:true, SC:true, FP:true, LDG:true]
        }
        Map score_values = [flightTestTakeoffCorrectSecond:        contest_instance.flightTestTakeoffCorrectSecond,
                            flightTestTakeoffPointsPerSecond:      contest_instance.flightTestTakeoffPointsPerSecond,
                            flightTestTakeoffMissedPoints:         contest_instance.flightTestTakeoffMissedPoints,
                            flightTestCptimeCorrectSecond:         contest_instance.flightTestCptimeCorrectSecond,
                            flightTestCptimePointsPerSecond:       contest_instance.flightTestCptimePointsPerSecond,
                            flightTestCptimeMaxPoints:             contest_instance.flightTestCptimeMaxPoints,
                            flightTestCpNotFoundPoints:            contest_instance.flightTestCpNotFoundPoints,
                            flightTestProcedureTurnNotFlownPoints: contest_instance.flightTestProcedureTurnNotFlownPoints,
                            flightTestBadCourseCorrectSecond:      contest_instance.flightTestBadCourseCorrectSecond,
                            flightTestBadCoursePoints:             contest_instance.flightTestBadCoursePoints,
                            flightTestBadCourseMaxPoints:          contest_instance.flightTestBadCourseMaxPoints,
                            flightTestLandingToLatePoints:         contest_instance.flightTestLandingToLatePoints,
                            flightTestCheckSecretPoints:           task_instance.flightTestCheckSecretPoints,
                            flightTestCheckTakeOff:                task_instance.flightTestCheckTakeOff,
                            flightTestCheckLanding:                task_instance.flightTestCheckLanding
                           ]
        boolean is_public2 = false
        if (grailsApplication.config.flightcontest?.livetracking?.navigationtask?.createPublic) {
            is_public2 = true
        }
        JsonBuilder json_builder = new JsonBuilder()
        json_builder {
            name task_instance.GetMediaName(Media.Tracking)
            is_public is_public2
            scorecard route_instance.liveTrackingScorecard
            start_time FcTime.UTCGetDateTime(task_instance.liveTrackingNavigationTaskDate, first_date, task_instance.contest.timeZone)
            finish_time FcTime.UTCGetDateTime(task_instance.liveTrackingNavigationTaskDate, finish_date, task_instance.contest.timeZone)
            if (!contest_instance.resultClasses || !class_evaluation) {
                gate_score_override  get_gate_score_overrides(score_values, gate_types)
                track_score_override get_track_score_overrides(score_values)
            }
            contestant_set contestant_list
            route_file route_data
        }
        
        Map ret1 = [:]
        if (contest_instance.liveTrackingManagedCrews) {
            ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/importnavigationtaskteamid/", "POST", 201, json_builder.toString(), "id")
        } else {
            ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/importnavigationtask/", "POST", 201, json_builder.toString(), "id")
        }
    
        gpxService.DeleteFile(upload_gpx_file_name)
        
        if (!ret1.data) {
            Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.navigationtaskcreate.error',[route_instance.liveTrackingScorecard, ret1.errorMsg])]
            printerror ret
            return ret
        }
        
        task_instance.liveTrackingTracksAvailable = tracks_available
        task_instance.liveTrackingNavigationTaskID = ret1.data.toInteger()
        task_instance.save()
        
        Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.navigationtaskcreate.done',[task_instance.liveTrackingNavigationTaskID])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map connectNavigationTask(Map params)
    {
        printstart "connectNavigationTask"
        
        Task task_instance = Task.get(params.id)
        Map ret4 = is_task_complete(task_instance, false)
        if (!ret4.ok) {
            Map ret = ['instance':task_instance, 'connected':false, 'message':ret4.message]
            printerror ret
            return ret
        }
        
        Contest contest_instance = task_instance.contest
		Integer navigationtask_id = 0
        String navigationtask_date = ""
        Map ret3 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/", "GET", 200, "", ALL_DATA)
		if (ret3.ok && ret3.data) {
            for (Map navigationtask in ret3.data) {
				if (navigationtask.name == task_instance.GetMediaName(Media.Tracking)) {
					navigationtask_id = navigationtask.id
                    if (navigationtask.start_time.size() >= 10) {
                        navigationtask_date = navigationtask.start_time.substring(0,10)
                    }
					break
				}
			}
		}
		if (!navigationtask_id) {
			Map ret = ['instance':task_instance, 'connected':false, 'message':getMsg('fc.livetracking.navigationtaskconnect.notexists',[task_instance.GetMediaName(Media.Tracking)])]
			printerror ret
			return ret
        }
        
        task_instance.liveTrackingNavigationTaskID = navigationtask_id
        task_instance.liveTrackingNavigationTaskDate = navigationtask_date
        task_instance.save()
        
        Map ret = ['instance':task_instance, 'connected':true, 'message':getMsg('fc.livetracking.navigationtaskconnect.done',[navigationtask_id])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    private boolean check_tracks_availability(Task taskInstance)
    {
        if (!taskInstance.contest.liveTrackingManagedCrews) {
            boolean tracks_available = true
            for (Test test_instance in Test.findAllByTask(taskInstance,[sort:'viewpos'])) {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    if (test_instance.IsFlightTestRun() && test_instance.timeCalculated) {
                        if (!test_instance.flightTestComplete) {
                            tracks_available = false
                        }
                    }
                }
            }
            if (tracks_available) {
                return true
            }
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    private Map is_task_complete(Task taskInstance, boolean checkDate)
    {
        Map ret = [ok:false, message:""]
        
        if (!taskInstance.IsFlightTestRun()) {
            ret.message = getMsg('fc.flighttest.notactive')
            return ret
        }
        
        // Date exists?
        if (checkDate && !taskInstance.liveTrackingNavigationTaskDate) {
            ret.message = getMsg('fc.livetracking.navigationtaskdate.notexists')
            return ret
        }
        
        // FlightTest exists?
        if (!taskInstance.flighttest) {
            ret.message = getMsg('fc.flighttest.notfound')
            return ret
        }

        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(taskInstance.flighttest)) {
            ret.message = getMsg('fc.flighttestwind.notfound')
            return ret
        }
        
        // FlightTestWind assigned to all crews?
        int enabled_crew_num = 0
        for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
	            if (!test_instance.flighttestwind) {
                    ret.message = getMsg('fc.flighttestwind.notassigned')
                    return ret
	            }
                enabled_crew_num++
            }
        }
        
        // Any crew enabled?
        if (!enabled_crew_num) {
            ret.message = getMsg('fc.test.enableanycrew')
            return ret
        }

        // Time calculated for all crews?
        for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
			if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (!test_instance.timeCalculated) {
                    ret.message = getMsg('fc.test.timetable.newcalculate')
                    return ret
                }
            }
        }
        
        ret.ok = true
        return ret
    }
        
    //--------------------------------------------------------------------------
    Map deleteNavigationTask(Map params)
    {
        printstart "deleteNavigationTask"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        Integer old_navigationtask_id = task_instance.liveTrackingNavigationTaskID
        
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/", "DELETE", 204, "", "")
        
        if (ret1.ok) {
            task_instance.liveTrackingNavigationTaskID = 0
            task_instance.liveTrackingTracksAvailable = false
            task_instance.save()
            Map ret = ['instance':task_instance, 'deleted':true, 'message':getMsg('fc.livetracking.navigationtaskdelete.done',[old_navigationtask_id])]
            printdone ret
            return ret
        } else {
            Map ret = ['instance':task_instance, 'deleted':false, 'message':getMsg('fc.livetracking.navigationtaskdelete.error',[old_navigationtask_id,ret1.errorMsg])]
            printerror ret
            return ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map disconnectNavigationTask(Map params)
    {
        printstart "disconnectNavigationTask"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        Integer old_navigationtask_id = task_instance.liveTrackingNavigationTaskID
        
		task_instance.liveTrackingNavigationTaskID = 0
        task_instance.liveTrackingTracksAvailable = false
		task_instance.save()
		Map ret = ['instance':task_instance, 'disconnected':true, 'message':getMsg('fc.livetracking.navigationtaskdisconnect.done',[old_navigationtask_id])]
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map addTracksNavigationTask(Map params, boolean incompleteTracks)
    {
        printstart "addTracksNavigationTask"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        
        int add_num = 0
        int error_num = 0
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/", "GET", 200, "", ALL_DATA)
        if (!ret1.data) {
            Map ret = ['instance':task_instance, 'error':true, message:getMsg('fc.livetracking.error',[ret1.errorMsg])]
			printerror ret.message
            return ret
        }
        
        Date track_end_time = null
        if (incompleteTracks) {
            Date end_time = null
            for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
                if (!test_instance.crew.disabled && !test_instance.disabledCrew && test_instance.IsFlightTestRun() && test_instance.timeCalculated && test_instance.flightTestComplete) {
                    GregorianCalendar endtime_calendar = new GregorianCalendar() 
                    endtime_calendar.setTime(test_instance.finishTime)
                    endtime_calendar.add(Calendar.MINUTE, FP_MINUTES)
                    track_end_time = endtime_calendar.getTime()
                    break
                }
            }
        }
       
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.crew.disabled && !test_instance.disabledCrew && test_instance.IsFlightTestRun() && test_instance.timeCalculated && test_instance.flightTestComplete) {
                if (add_track(test_instance, ret1.data, track_end_time)) {
                    add_num++
                } else {
                    error_num++
                }
            }
        }
        
        Map ret = ['instance':task_instance, 'error':error_num > 0, 'message':getMsg('fc.livetracking.navigationtaskaddtracks.done',[add_num,error_num])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map updateCrewsNavigationTask(Map params)
    {
		printstart "updateCrewsNavigationTask"
		
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest

        // FlightTest exists?
        if (!task_instance.flighttest) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.flighttest.notfound')]
			printerror ret.message
            return ret
        }
        
        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task_instance.flighttest)) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.flighttestwind.notfound')]
			printerror ret.message
            return ret
        }
        
        // Any crew selected?
        List test_instance_ids = []
        int assign_num = 0
        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance_ids += test_instance.id
                assign_num++
            }
        }
        if (!assign_num) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.livetracking.someonemustselected.assign')]
			printerror ret.message
            return ret
        }
        
        // update crews
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/", "GET", 200, "", ALL_DATA)
        if (!ret1.data) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.livetracking.error',[ret1.errorMsg])]
			printerror ret.message
            return ret
        }

        int crew_num = 0
        int error_num = 0
        String error_str = ""
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.crew.disabled && !test_instance.disabledCrew && test_instance.timeCalculated) {
                if (test_instance.id in test_instance_ids) {
                    Map ret = update_crew(test_instance, ret1.data)
                    if (ret.ok) {
                        crew_num++
                    } else {
                        if (error_str) {
                            error_str += ", "
                        }
                        error_str += ret.errorMsg
                        error_num++
                    }
                }
            }
        }
        
        printdone ""
        return [taskInstance:task_instance, error:error_num > 0, message:getMsg('fc.livetracking.navigationtaskupdatecrews.done',[crew_num, error_num, error_str])]
    }
    
    //--------------------------------------------------------------------------
    private Map update_crew(Test testInstance, def contestantList) {
        Map ret = [ok:false, errorMsg:'']
        println "update_crew ${testInstance.crew.name}"

        // get contestant_id
        int contestant_id = 0
        Task task_instance = testInstance.task
        Contest contest_instance = task_instance.contest
        for (Map contestant in contestantList) {
            if (contestant.contestant_number == testInstance.crew.startNum) {
                if (contestant_id) {
                    println "Multiple ids found"
                    return false
                }
                contestant_id = contestant.id
                println "Contestant id $contestant.id"
                
            }
        }
        if (contestant_id) {
            Map contestant = get_contestant(testInstance, task_instance.liveTrackingNavigationTaskDate, false)
            JsonBuilder json_builder = new JsonBuilder(contestant.data)
            Map ret2 = [:]
            if (contest_instance.liveTrackingManagedCrews) {
                ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/${contestant_id}/update_without_team/", "PUT", 200, json_builder.toString(), "")
            } else {
                ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/${contestant_id}/", "PUT", 200, json_builder.toString(), "")
            }
            if (ret2.ok) {
                ret.ok = true
            } else {
                ret.errorMsg = ret2.errorMsg
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map get_contestant(Test testInstance, String trackingDate, boolean createTrackerID) {
        Map ret = [data:[:], arrivalTime:null]
        Contest contest_instance = testInstance.task.contest
        
        int minutes_between_trackerstart_takeoff = 5
        if (grailsApplication.config.flightcontest?.livetracking?.contestant?.minutesBetweenTrackerStartAndTakeoff && grailsApplication.config.flightcontest?.livetracking?.contestant?.minutesBetweenTrackerStartAndTakeoff instanceof Integer) {
            minutes_between_trackerstart_takeoff = grailsApplication.config.flightcontest?.livetracking?.contestant?.minutesBetweenTrackerStartAndTakeoff
        }
        
        GregorianCalendar tracker_start_calendar = new GregorianCalendar()
        tracker_start_calendar.setTime(testInstance.takeoffTime)
        tracker_start_calendar.add(Calendar.MINUTE, -minutes_between_trackerstart_takeoff)
        String tracker_start_time = FcTime.UTCGetDateTime(trackingDate, tracker_start_calendar.getTime(), contest_instance.timeZone)
        
        String to_time = FcTime.UTCGetDateTime(trackingDate, testInstance.takeoffTime, contest_instance.timeZone)
        String ldg_time = FcTime.UTCGetDateTime(trackingDate, testInstance.maxLandingTime, contest_instance.timeZone)
        String arrival_time = FcTime.UTCGetDateTime(trackingDate, testInstance.arrivalTime, contest_instance.timeZone)
        
        Map gate_times = [:]
        gate_times += ["TO":to_time]
        gate_times += ["SP":FcTime.UTCGetDateTime(trackingDate, testInstance.startTime, contest_instance.timeZone)]
        Date leg_time = testInstance.startTime
        for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(testInstance)) {
            leg_time = testlegflight_instance.AddPlanLegTime(leg_time)
            gate_times += ["${testlegflight_instance.coordTitle.titleMediaCode(Media.Tracking)}":FcTime.UTCGetDateTime(trackingDate, leg_time, contest_instance.timeZone)] 
        }
        gate_times += ["LDG":ldg_time]
        
        ret.arrivalTime = testInstance.arrivalTime
        
        def team = null
        if (contest_instance.liveTrackingManagedCrews) {
            team = testInstance.crew.liveTrackingTeamID
        } else {
            team = ["aeroplane": ["registration": testInstance.taskAircraft.registration]]
            team += ["crew": get_crew(testInstance)]
            if (testInstance.crew.team?.name) {
                team += ["club": ["name":testInstance.crew.team.name]]
            }
        }
        
        String tracker_id = testInstance.taskTrackerID
        if (!tracker_id && createTrackerID) {
            // tracker_id = get_random_tracker_id(contest_instance.liveTrackingContestID, testInstance.crew.startNum)
        }
        
        ret.data = [
            "contestant_number": testInstance.crew.startNum,
            "team": team,
            "air_speed": testInstance.taskTAS,
            "tracker_device_id":  tracker_id,
            "tracker_start_time": tracker_start_time,
            "takeoff_time": to_time,
            "minutes_to_starting_point": (60*FcMath.TimeDiff(testInstance.takeoffTime,testInstance.startTime)).toInteger(),
            "finished_by_time": arrival_time,
            "wind_direction": testInstance.flighttestwind.wind.direction,
            "wind_speed": testInstance.flighttestwind.wind.speed,
            "gate_times": gate_times
        ]
        Map gate_types = [:]
        boolean class_evaluation = false
        if (contest_instance.resultClasses) {
            if (contest_instance.contestRuleForEachClass) {
                gate_types = [TO:true, SP:true, TP:true, SC:true, FP:true, LDG:true]
                class_evaluation = true
            } else {
                gate_types = [TO:true, SP:false, TP:false, SC:true, FP:false, LDG:true]
            }
        } else {
            gate_types = [TO:false, SP:false, TP:false, SC:false, FP:false, LDG:false]
        }
        Map score_values = [flightTestTakeoffCorrectSecond:        testInstance.GetFlightTestTakeoffCorrectSecond(),
                            flightTestTakeoffPointsPerSecond:      testInstance.GetFlightTestTakeoffCheckSeconds(),
                            flightTestTakeoffMissedPoints:         testInstance.GetFlightTestTakeoffMissedPoints(),
                            flightTestCptimeCorrectSecond:         testInstance.GetFlightTestCptimeCorrectSecond(),
                            flightTestCptimePointsPerSecond:       testInstance.GetFlightTestCptimePointsPerSecond(),
                            flightTestCptimeMaxPoints:             testInstance.GetFlightTestCptimeMaxPoints(),
                            flightTestCpNotFoundPoints:            testInstance.GetFlightTestCpNotFoundPoints(),
                            flightTestProcedureTurnNotFlownPoints: testInstance.GetFlightTestProcedureTurnNotFlownPoints(),
                            flightTestBadCourseCorrectSecond:      testInstance.GetFlightTestBadCourseCorrectSecond(),
                            flightTestBadCoursePoints:             testInstance.GetFlightTestBadCoursePoints(),
                            flightTestBadCourseMaxPoints:          testInstance.GetFlightTestBadCourseMaxPoints(),
                            flightTestLandingToLatePoints:         testInstance.GetFlightTestLandingToLatePoints(),
                            flightTestCheckSecretPoints:           testInstance.IsFlightTestCheckSecretPoints(),
                            flightTestCheckTakeOff:                testInstance.IsFlightTestCheckTakeOff(),
                            flightTestCheckLanding:                testInstance.IsFlightTestCheckLanding()
                           ]
        ret.data += ["gate_score_override": get_gate_score_overrides(score_values, gate_types)]
        if (class_evaluation) {
            ret.data += ["track_score_override": get_track_score_overrides(score_values)]
        }
        if (contest_instance.resultClasses) {
            Map class_name = testInstance.GetResultClassName()
            ret.data += ["competition_class_longform": class_name.name,
                         "competition_class_shortform": class_name.shortName
                        ]
        }
                        
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map get_crew(Test testInstance)
    {
        Contest contest_instance = testInstance.task.contest
        String pilot_first = ""
        String pilot_last = ""
        String pilot_email = ""
        String navigator_first = ""
        String navigator_last = ""
        String navigator_email = ""
        
        List email_list = NetTools.EMailList(testInstance.crew.email)
        if (email_list.size() > 0) { // pilot
            pilot_email = email_list[0]
            if (email_list.size() > 1) { // navigator
                navigator_email = email_list[1]
            }
        }
        
        if (contest_instance.crewPilotNavigatorDelimiter) { // pilot & navigator
            List pilot_navigator = Tools.Split(testInstance.crew.name, contest_instance.crewPilotNavigatorDelimiter)
            if (contest_instance.crewSurnameForenameDelimiter) {
                if (pilot_navigator.size() > 0) { // pilot
                    Map pilot_name = get_person_name_with_surenameforename_delimiter(pilot_navigator[0], contest_instance.crewSurnameForenameDelimiter)
                    pilot_first = pilot_name.first
                    pilot_last = pilot_name.last
                    if (!pilot_email) {
                        pilot_email = pilot_name.email
                    }
                    if (pilot_navigator.size() > 1) { // navigator
                        Map navigator_name = get_person_name_with_surenameforename_delimiter(pilot_navigator[1], contest_instance.crewSurnameForenameDelimiter)
                        navigator_first = navigator_name.first
                        navigator_last = navigator_name.last
                        if (!navigator_email) {
                            navigator_email = navigator_name.email
                        }
                    }
                }
            } else {
                if (pilot_navigator.size() > 0) { // pilot
                    Map pilot_name = get_person_name_with_space_delimiter(pilot_navigator[0])
                    pilot_first = pilot_name.first
                    pilot_last = pilot_name.last
                    if (!pilot_email) {
                        pilot_email = pilot_name.email
                    }
                    if (pilot_navigator.size() > 1) { // navigator
                        Map navigator_name = get_person_name_with_space_delimiter(pilot_navigator[1])
                        navigator_first = navigator_name.first
                        navigator_last = navigator_name.last
                        if (!navigator_email) {
                            navigator_email = navigator_name.email
                        }
                    }
                }
            }
        } else { // only pilot
            if (contest_instance.crewSurnameForenameDelimiter) {
                Map pilot_name = get_person_name_with_surenameforename_delimiter(testInstance.crew.name, contest_instance.crewSurnameForenameDelimiter)
                pilot_first = pilot_name.first
                pilot_last = pilot_name.last
                if (!pilot_email) {
                    pilot_email = pilot_name.email
                }
            } else {
                Map pilot_name = get_person_name_with_space_delimiter(testInstance.crew.name)
                pilot_first = pilot_name.first
                pilot_last = pilot_name.last
                if (!pilot_email) {
                    pilot_email = pilot_name.email
                }
            }
        }
        
        if (pilot_first) {
            Map ret = ["member1": ["first_name": pilot_first, "last_name": pilot_last, "email":pilot_email]]
            if (navigator_first) {
                ret += ["member2": ["first_name": navigator_first, "last_name": navigator_last, "email":navigator_email]]
            }
            return ret
        }
        if (!pilot_email) {
            pilot_email = get_person_email(testInstance.crew.name,"")
        }
        return ["member1": ["first_name": testInstance.crew.name, "last_name": EMPTY_NAME, "email":pilot_email]]
    }
        
    //--------------------------------------------------------------------------
    private Map get_person_name_with_surenameforename_delimiter(String crewName, String crewSurnameForenameDelimiter)
    {   // "Last, First1 First2" -> "First1 First2" & "Last"
        Map ret = [first:"", last:"", email:""]
        List names = Tools.Split(crewName, crewSurnameForenameDelimiter)
        if (names.size() > 0) {
            ret.last = names[0].trim()
            if (names.size() > 1) {
                ret.first = names[1].trim()
                ret.email = get_person_email(ret.first, ret.last)
            } else {
                ret.first = ret.last
                ret.last = EMPTY_NAME
                ret.email = get_person_email(ret.last, "")
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map get_person_name_with_space_delimiter(String crewName)
    {   // "First1 First2 Last" -> "First1 First2" & "Last"
        Map ret = [first:"", last:"", email:""]
        List names = Tools.Split(crewName, " ")
        for (String name in names) {
            String name2 = name.trim()
            if (name2) {
                if (!ret.first) {
                    ret.first = name2
                } else if (!ret.last) {
                    ret.last = name2
                } else {
                    ret.first += " "
                    ret.first += ret.last
                    ret.last = name2
                }
            }
        }
        if (ret.last) {
            ret.email = get_person_email(ret.first, ret.last)
        } else {
            ret.last = EMPTY_NAME
            ret.email = get_person_email(ret.first)
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private String get_person_email(String firstName, String lastName)
    {
        String email = firstName.replace(' ', '')
        if (lastName) {
            email += Defs.EMAIL_NAME_SEPARATOR
            email += lastName.replace(' ', '')
        }
        email += Defs.EMAIL_AT_CHAR
        email += ANONYMOUS_EMAIL_DOMAIN
        return email.toLowerCase()
    }
    
    //--------------------------------------------------------------------------
    private Map get_track_score_overrides(Map scoreValues)
    {
        Map ret = [
            bad_course_grace_time:      scoreValues.flightTestBadCourseCorrectSecond,
            bad_course_penalty:         scoreValues.flightTestBadCoursePoints
        ]
        if (scoreValues.flightTestBadCourseMaxPoints > 0) {
            ret += [bad_course_maximum_penalty: scoreValues.flightTestBadCourseMaxPoints]
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private List get_gate_score_overrides(Map scoreValues, Map gateType)
    {
        List gate_score_overrides = []
        
        // TO
        if (gateType.TO) {
            if (scoreValues.flightTestCheckTakeOff) {
                Map to_override = [for_gate_types: ["to"],
                                   checkpoint_grace_period_before: 0,
                                   checkpoint_grace_period_after: scoreValues.flightTestTakeoffCorrectSecond,
                                   checkpoint_not_found:          scoreValues.flightTestTakeoffMissedPoints,
                                   checkpoint_maximum_penalty:    scoreValues.flightTestTakeoffMissedPoints
                                  ]
                if (scoreValues.flightTestTakeoffCheckSeconds) {
                    to_override += [checkpoint_penalty_per_second: scoreValues.flightTestTakeoffPointsPerSecond]
                } else {
                    to_override += [checkpoint_penalty_per_second: scoreValues.flightTestTakeoffMissedPoints]
                }
                gate_score_overrides += to_override
            } else {
                Map to_override = [for_gate_types: ["to"],
                                   checkpoint_grace_period_before: 0,
                                   checkpoint_grace_period_after:  scoreValues.flightTestTakeoffCorrectSecond,
                                   checkpoint_not_found:           0,
                                   checkpoint_maximum_penalty:     0,
                                   checkpoint_penalty_per_second:  0
                                  ]
                gate_score_overrides += to_override
            }
        }
        
        // SP
        if (gateType.SP) {
            Map sp_override = [for_gate_types: ["sp"],
                               checkpoint_grace_period_before:     scoreValues.flightTestCptimeCorrectSecond,
                               checkpoint_grace_period_after:      scoreValues.flightTestCptimeCorrectSecond,
                               checkpoint_penalty_per_second:      scoreValues.flightTestCptimePointsPerSecond,
                               checkpoint_maximum_penalty:         scoreValues.flightTestCptimeMaxPoints,
                               checkpoint_not_found:               scoreValues.flightTestCpNotFoundPoints,
                               bad_crossing_extended_gate_penalty: scoreValues.flightTestBadCoursePoints
                              ]
            gate_score_overrides += sp_override
        }
        
        // TP
        if (gateType.TP) {
            Map tp_override = [for_gate_types: ["tp"],
                               checkpoint_grace_period_before: scoreValues.flightTestCptimeCorrectSecond,
                               checkpoint_grace_period_after:  scoreValues.flightTestCptimeCorrectSecond,
                               checkpoint_penalty_per_second:  scoreValues.flightTestCptimePointsPerSecond,
                               checkpoint_maximum_penalty:     scoreValues.flightTestCptimeMaxPoints,
                               checkpoint_not_found:           scoreValues.flightTestCpNotFoundPoints,
                               missing_procedure_turn_penalty: scoreValues.flightTestProcedureTurnNotFlownPoints
                              ]
            gate_score_overrides += tp_override
        }
        
        // SC
        if (gateType.SC) {
            if (scoreValues.flightTestCheckSecretPoints) {
                Map secret_override = [for_gate_types: ["secret"],
                                       checkpoint_grace_period_before: scoreValues.flightTestCptimeCorrectSecond,
                                       checkpoint_grace_period_after:  scoreValues.flightTestCptimeCorrectSecond,
                                       checkpoint_penalty_per_second:  scoreValues.flightTestCptimePointsPerSecond,
                                       checkpoint_maximum_penalty:     scoreValues.flightTestCptimeMaxPoints,
                                       checkpoint_not_found:           scoreValues.flightTestCpNotFoundPoints
                                      ]
                gate_score_overrides += secret_override
            } else {
                Map secret_override = [for_gate_types: ["secret"],
                                       checkpoint_grace_period_before: scoreValues.flightTestCptimeCorrectSecond,
                                       checkpoint_grace_period_after:  scoreValues.flightTestCptimeCorrectSecond,
                                       checkpoint_penalty_per_second:  0,
                                       checkpoint_maximum_penalty:     0,
                                       checkpoint_not_found:           0
                                      ]
                gate_score_overrides += secret_override
            }
        }
        
        // FP
        if (gateType.FP) {
            Map fp_override = [for_gate_types: ["fp"],
                               checkpoint_grace_period_before: scoreValues.flightTestCptimeCorrectSecond,
                               checkpoint_grace_period_after:  scoreValues.flightTestCptimeCorrectSecond,
                               checkpoint_penalty_per_second:  scoreValues.flightTestCptimePointsPerSecond,
                               checkpoint_maximum_penalty:     scoreValues.flightTestCptimeMaxPoints,
                               checkpoint_not_found:           scoreValues.flightTestCpNotFoundPoints
                              ]
            gate_score_overrides += fp_override
        }
        
        // LDG
        if (gateType.LDG) {
            if (scoreValues.flightTestCheckLanding) {
                Map ldg_override = [for_gate_types: ["ldg"],
                                   checkpoint_grace_period_before: 1800, // 30 min
                                   checkpoint_grace_period_after:  0,
                                   checkpoint_not_found:           scoreValues.flightTestLandingToLatePoints,
                                   checkpoint_maximum_penalty:     scoreValues.flightTestLandingToLatePoints,
                                   checkpoint_penalty_per_second:  scoreValues.flightTestLandingToLatePoints
                                  ]
                gate_score_overrides += ldg_override
            } else {
                Map ldg_override = [for_gate_types: ["ldg"],
                                   checkpoint_grace_period_before: 0,
                                   checkpoint_grace_period_after:  0,
                                   checkpoint_not_found:           0,
                                   checkpoint_maximum_penalty:     0,
                                   checkpoint_penalty_per_second:  0
                                  ]
                gate_score_overrides += ldg_override
            }
        }
        
        return gate_score_overrides
    }
    
    //--------------------------------------------------------------------------
    Map addTrackCrewsNavigationTask(Map params)
    {
		printstart "addTrackCrewsNavigationTask"
		
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest

        // FlightTest exists?
        if (!task_instance.flighttest) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.flighttest.notfound')]
			printerror ret.message
            return ret
        }
        
        // FlightTestWind exists?
        if (!FlightTestWind.countByFlighttest(task_instance.flighttest)) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.flighttestwind.notfound')]
			printerror ret.message
            return ret
        }
        
        // Any crew selected?
        List test_instance_ids = []
        int assign_num = 0
        Test.findAllByTask(task_instance,[sort:"id"]).each { Test test_instance ->
            if (params["selectedTestID${test_instance.id}"] == "on") {
                test_instance_ids += test_instance.id
                assign_num++
            }
        }
        if (!assign_num) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.livetracking.someonemustselected.assign')]
			printerror ret.message
            return ret
        }
        
        // add navigation tracks
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/", "GET", 200, "", ALL_DATA)
        if (!ret1.data) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.livetracking.error',[ret1.errorMsg])]
			printerror ret.message
            return ret
        }

        int crew_num = 0
        int error_num = 0
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.crew.disabled && !test_instance.disabledCrew && test_instance.timeCalculated) {
                if (test_instance.id in test_instance_ids) {
                    if (add_track(test_instance, ret1.data, null)) {
                        crew_num++
                    } else {
                        error_num++
                    }
                }
            }
        }
        
        printdone ""
        return [taskInstance:task_instance, error:error_num > 0, message:getMsg('fc.livetracking.navigationtaskaddtrackcrews.done',[crew_num, error_num])]
    }
    
    //--------------------------------------------------------------------------
    private boolean add_track(Test testInstance, def contestantList, Date trackEndTime) {
        println "add_track ${testInstance.crew.name}"

        // get contestant_id
        int contestant_id = 0
        Task task_instance = testInstance.task
        Contest contest_instance = task_instance.contest
        for (Map contestant in contestantList) {
            if (contestant.contestant_number == testInstance.crew.startNum) {
                if (contestant_id) {
                    println "Multiple ids found"
                    return false
                }
                contestant_id = contestant.id
                println "Contestant id $contestant.id"
                
            }
        }
        if (contestant_id) {
            String uuid = UUID.randomUUID().toString()
            String upload_gpx_file_name = "${GpxService.GPXDATA}-${uuid}"
            String gpx_end_time = ""
            if (trackEndTime) {
                gpx_end_time = FcTime.UTCGetDateTime(task_instance.liveTrackingNavigationTaskDate, trackEndTime, contest_instance.timeZone)
            }
            Map gpx_converter = gpxService.ConvertTest2GPX(
                testInstance,
                upload_gpx_file_name,
                [isTracking:true, isPrint:false, showPoints:false, wrEnrouteSign:false, gpxExport:false, gpxDate:task_instance.liveTrackingNavigationTaskDate, gpxEndTime:gpx_end_time]
            )
            if (gpx_converter.ok && gpx_converter.track) {
                String gpx_data = new String(BootStrap.tempData.GetData(upload_gpx_file_name))
                String track_data = Base64.getEncoder().encodeToString(gpx_data.getBytes())
                JsonBuilder json_builder = new JsonBuilder()
                json_builder {
                    track_file track_data
                }
                Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/${contestant_id}/gpx_track/", "POST", 201, json_builder.toString(), "")
                gpxService.DeleteFile(upload_gpx_file_name)
                if (ret2.ok) {
                    return true
                }
            }
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    Map importTrackerPointsAndCalcTest(Map params)
    {
        Test test_instance = Test.get(params.id)
        Task task_instance = test_instance.task
        Contest contest_instance = task_instance.contest
        
        printstart "importTrackerPointsAndCalcTest: crew '$test_instance.crew.name', $params"
        
        boolean interpolate_missing_data = params?.interpolate_missing_data == "on"
        
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/", "GET", 200, "", ALL_DATA)
        if (!ret1.data) {
            Map ret = ['error':true,'saved':false,'message':getMsg('fc.livetracking.error',[ret1.errorMsg])]
            printerror ret.message
            return ret
        }
        
        int contestant_id = 0
        for (Map contestant in ret1.data) {
            if (contestant.contestant_number == test_instance.crew.startNum) {
                contestant_id = contestant.id
                break
            }
        }
        if (!contestant_id) {
            Map ret = ['error':true,'saved':false,'message':getMsg('fc.livetracking.imported.nodata',[test_instance.crew.name])]
            printerror ret.message
            return ret
        }
        
        Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/${contestant_id}/track/", "GET", 200, "", ALL_DATA)
        if (!ret2.data) {
            Map ret = ['error':true,'saved':false,'message':getMsg('fc.livetracking.error',[ret2.errorMsg])]
            printerror ret.message
            return ret
        }
        
        println "Import track points from tracker (interpolate_missing_data=$interpolate_missing_data)"
        
        // write track points
        int track_point_num = 0
        boolean first = true
        BigDecimal last_latitude = null
        BigDecimal last_longitude = null
        def last_altitude = null
        def last_track = null
        String last_utc = FcTime.UTC_GPX_DATE
        for (Map track_point in ret2.data.track) {
            
            boolean ignore_line = false
            
            // remove old track points
            if (first) {
                if (test_instance.IsLoggerData()) {
                    TrackPoint.findAllByLoggerdata(test_instance.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                        trackpoint_instance.delete()
                    }
                }
            }
            
            // UTC
            String valid_utc_time = ""
            if (track_point.time) {
                valid_utc_time = FcTime.UTCGetValidDateTime(track_point.time)
            }
            if (valid_utc_time) {
                String utc_time = FcTime.UTCGetTime(valid_utc_time)
                String utc = FcTime.UTCGetNextDateTime(last_utc, utc_time)
                
                // Latitude (Geographische Breite: -90 (S)... +90 Grad (N))
                BigDecimal latitude = track_point.latitude.toBigDecimal()

                // Longitude (Geographische Laenge: -179.999 (W) ... +180 Grad (E))
                BigDecimal longitude = track_point.longitude.toBigDecimal()

                // Altitude (Höhe) in ft
                BigDecimal altitude_meter = 0
                if (track_point.altitude) {
                    altitude_meter = track_point.altitude.toBigDecimal()
                }
                int altitude = FcMath.RoundAltitude(altitude_meter * GpxService.ftPerMeter)
                
                // Track in Grad
                def track = null
                if (last_latitude != null && last_longitude != null) {
                    if ((latitude == last_latitude) && (longitude == last_longitude)) {
                        track = last_track
                    } else {
                        Map leg = AviationMath.calculateLeg(latitude,longitude,last_latitude,last_longitude)
                        track = FcMath.RoundGrad(leg.dir)
                    }
                }
                
                if (LoggerFileTools.REMOVE_IDENTICAL_TIMES) {
                    if (utc == last_utc) {
                        ignore_line = true
                    }
                }
                
                // save track point
                if (!ignore_line) {
                    if (interpolate_missing_data) {
                        track_point_num += LoggerFileTools.InterpolateMissingTrackpoints(last_utc, last_latitude, last_longitude, last_altitude, utc, latitude, longitude, altitude, track, test_instance.loggerData)
                    }
                    
                    TrackPoint trackpoint_instance = new TrackPoint()
                    trackpoint_instance.loggerdata = test_instance.loggerData
                    trackpoint_instance.utc = utc
                    trackpoint_instance.latitude = latitude
                    trackpoint_instance.longitude = longitude
                    trackpoint_instance.altitude = altitude
                    trackpoint_instance.track = track
                    trackpoint_instance.interpolated = false
                    trackpoint_instance.save()
                    
                    track_point_num++
                    
                    last_utc = utc
                    last_latitude = latitude
                    last_longitude = longitude
                    last_altitude = altitude
                    last_track = track
                }
                
                first = false
            }
        }
		
        calcService.Calculate(test_instance, "", "")
        
        Map ret3 = fcService.ImportResults(test_instance, false, track_point_num) // false - noRemoveExistingData
        
        if (ret3.no_flightresults) {
            Map ret = ['error':true,'saved':false,'message':getMsg('fc.livetracking.imported.nodata',[test_instance.crew.name])]
            printerror ret.message
            return ret
        } else if (ret3.flight_failures) {
            Map ret = ['error':false,'saved':true,'message':getMsg('fc.livetracking.imported.failures',[test_instance.crew.name])]
            printerror ret.message
            return ret
        }
            
        Map ret = ['saved':true,'message':getMsg('fc.livetracking.imported',[test_instance.crew.name])]
        printdone ret.message
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map call_rest(String funcURL, String requestMethod, int successfulResponseCode, String outputData, String retDataKey)
    {
        Map ret = [responseCode:null, data:null, ok:false, errorMsg:""]
        
        boolean show_log = true
        int max_output_size = 0 // 2000
        
        String url_path = "${BootStrap.global.GetLiveTrackingAPI()}/${funcURL}"
        
        if (show_log) {
            printstart "${requestMethod} ${url_path}"
        }
            
        try {
            def connection = url_path.toURL().openConnection()
            connection.requestMethod = requestMethod
            connection.setRequestProperty("Authorization", "Token ${BootStrap.global.GetLiveTrackingToken()}")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true
            connection.doInput = true
    
            if (outputData) {
                if (show_log) {
                    if (max_output_size && outputData.size() > max_output_size) {
                        println "${outputData.substring(0, max_output_size)}..."
                    } else {
                        println outputData
                    }
                }
                byte[] output_bytes = outputData.getBytes("UTF-8")
                OutputStream os = connection.getOutputStream()
                os.write(output_bytes)
                os.close()
            }
            
            if (show_log) {
                if (connection.responseCode == successfulResponseCode) {
                    println "responseCode=${connection.responseCode} Ok."
                } else {
                    println "responseCode=${connection.responseCode} Error."
                }
            }
            ret.responseCode = connection.responseCode
            
            if (ret.responseCode == successfulResponseCode) {
                ret.ok = true
                if (retDataKey) {
                    InputStream inputstream_instance = connection.getInputStream()
                    BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
                    def input_data = new JsonSlurper().parse(input_reader)
                    if (input_data) {
                        if (show_log) {
                            println "Json data: ${input_data}"
                        }
                        if (retDataKey == ALL_DATA) {
                            ret.data = input_data
                        } else {
                            ret.data = input_data.(retDataKey.toString())
                        }
                    }
                    input_reader.close()
                    inputstream_instance.close()
                    if (show_log) {
                        if (connection.responseCode == successfulResponseCode) {
                            println "responseCode2=${ret.responseCode} ${ret.data} Ok."
                        } else {
                            println "responseCode2=${ret.responseCode} ${ret.data} Error."
                        }
                    }
                }
            } else {
                ret.errorMsg = "Response code ${ret.responseCode}, "
                ret.errorMsg += "${connection.getResponseMessage()}, "
                InputStream inputstream_instance = connection.getErrorStream()
                BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
                while (true) {
                    String line = input_reader.readLine()
                    if (line == null) {
                        break
                    }
                    ret.errorMsg += line
                }
                input_reader.close()
                inputstream_instance.close()
            }
            
            if (show_log) {
                printdone ""
            }
        } catch (Exception e) {
            ret.errorMsg += "Exception ${e.message}"
            if (show_log) {
                printerror "Exception ${e.message}"
            }
        }
        
        return ret
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
