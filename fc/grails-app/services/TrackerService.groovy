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
        
        BigDecimal latitude
        BigDecimal longitude
        for (Route route_instance in Route.findAllByContest(contest_instance,[sort:"idTitle"])) {
            for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
                if (coordroute_instance.type == CoordType.TO) {
                    latitude = coordroute_instance.latMath()
                    longitude = coordroute_instance.lonMath()
                    break
                }
            }
            if (latitude || longitude) {
                break
            }
        }
        if (!latitude || !longitude) {
			Map ret = ['instance':contest_instance, 'created':false, 'message':getMsg('fc.livetracking.contestcoords.notexists')]
			printerror ret
            return ret
        }
        
        Map contest_data = get_contest(contest_instance.title)
		if (contest_data) {
			Map ret = ['instance':contest_instance, 'created':false, 'message':getMsg('fc.livetracking.contestcreate.exists',[contest_instance.title, contest_data.id])]
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
            location "${latitude},${longitude}"
            start_time FcTime.UTCGetDateTime(contest_instance.liveTrackingContestDate, start_local_time, contest_instance.timeZone)
            finish_time FcTime.UTCGetDateTime(contest_instance.liveTrackingContestDate, end_local_time, contest_instance.timeZone)
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
        String contest_visibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
        String time_zone = ""
        Map contest = get_contest(contest_instance.title)
		if (contest) {
            contest_id = contest.id
            if (contest.start_time.size() >= 10) {
                contest_date = contest.start_time.substring(0,10)
            }
            contest_visibility = contest.share_string.toLowerCase()
            time_zone = contest.time_zone
		}
		if (contest_id) {
			contest_instance.liveTrackingContestID = contest_id
            contest_instance.liveTrackingContestDate = contest_date
            contest_instance.liveTrackingContestVisibility = contest_visibility
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
    private Map get_contest(String contestName)
    {
        String next = ""
        while (true) {
            String cursor = ""
            if (next) {
                cursor = "?cursor=${next}"
            }
            Map ret = call_rest("contests/${cursor}", "GET", 200, "", ALL_DATA)
            if (ret.ok && ret.data) {
                for (Map contest in ret.data.results) {
                    if (contest.name == contestName) {
                        return contest
                    }
                }
                next = ret.data.next
            }
            if (!next) {
                return [:]
            }
        }
        return [:]
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
            contest_instance.liveTrackingContestVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
            contest_instance.save()
            for (Task task_instance in Task.findAllByContest(contest_instance,[sort:"idTitle"])) {
                reset_task(task_instance)
            }
            for (Crew crew_instance in Crew.findAllByContest(contest_instance)) {
                reset_crew(crew_instance)
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
        contest_instance.liveTrackingContestVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
		contest_instance.save()
		for (Task task_instance in Task.findAllByContest(contest_instance,[sort:"idTitle"])) {
            reset_task(task_instance)
		}
        for (Crew crew_instance in Crew.findAllByContest(contest_instance)) {
            reset_crew(crew_instance)
        }
		Map ret = ['instance':contest_instance, 'connected':true, 'message':getMsg('fc.livetracking.contestdisconnect.done',[old_livetracking_contestid])]
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map setContestVisibility(Map params, String newVisibility)
    {
        printstart "setContestVisibility $newVisibility"
        
        Contest contest_instance = Contest.get(params.id)
        boolean error = false
        
        JsonBuilder json_builder = new JsonBuilder()
        json_builder {
            visibility newVisibility
        }
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/share/", "PUT", 200, json_builder.toString(), "")
        if (ret1.ok) {
            contest_instance.liveTrackingContestVisibility = newVisibility
            contest_instance.save()
            
            // set navigation tasks visiblities lower
            switch (newVisibility) {
                case Defs.LIVETRACKING_VISIBILITY_UNLISTED:
                    for (Task task_instance in Task.findAllByContest(contest_instance,[sort:"idTitle"])) {
                        if (task_instance.liveTrackingNavigationTaskID && task_instance.liveTrackingNavigationTaskVisibility == Defs.LIVETRACKING_VISIBILITY_PUBLIC) {
                            task_instance.liveTrackingNavigationTaskVisibility = Defs.LIVETRACKING_VISIBILITY_UNLISTED
                            task_instance.save()
                        }
                    }
                    break
                case Defs.LIVETRACKING_VISIBILITY_PRIVATE:
                    for (Task task_instance in Task.findAllByContest(contest_instance,[sort:"idTitle"])) {
                        if (task_instance.liveTrackingNavigationTaskID) {
                            switch (task_instance.liveTrackingNavigationTaskVisibility) {
                                case Defs.LIVETRACKING_VISIBILITY_PUBLIC:
                                case Defs.LIVETRACKING_VISIBILITY_UNLISTED:
                                    task_instance.liveTrackingNavigationTaskVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
                                    task_instance.save()
                                    break
                            }
                        }
                    }
                    break
            }
        } else {
            error = true
        }
        
        if (error) {
            Map ret = ['instance':contest_instance, 'error':true, 'message':getMsg('fc.livetracking.contestvisibility.notset',[ret1.errorMsg])]
            printerror ret
            return ret
        }
        Map ret = ['instance':contest_instance, 'error':false, 'message':getMsg('fc.livetracking.contestvisibility.set',[newVisibility])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map importTeams(Map params, boolean importAll)
    {
        printstart "importTeams"
		
        Contest contest_instance = Contest.get(params.id)
        int found_num = 0
		int import_num = 0
        int connected_num = 0
        int already_connected_num = 0
        int differency_num = 0
		
		Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/teams/", "GET", 200, "", ALL_DATA)
		if (ret1.ok && ret1.data) {
			
			List crew_list = []
			int start_num = 1
            for (Map team_datum in ret1.data) {
                Map new_crew = get_new_crew(team_datum.team, team_datum, start_num.toString(), contest_instance)
				crew_list += new_crew
				start_num++
				found_num++
			}
			
			if (found_num) {
				Map ret2 = fcService.importCrews2(crew_list, false, importAll, contest_instance)
                import_num = ret2.new_crew_num
                if (ret2.exist_crews) { // connect and check differencies
                    for (Map crew in ret2.exist_crews) {
                        Crew crew_instance = Crew.findByEmailAndContest(crew.email, contest_instance)
                        if (crew_instance) {
                            if (importAll || params["selectedCrewID${crew_instance.id}"] == "on") {
                                if (!crew_instance.liveTrackingTeamID) {
                                    crew_instance.liveTrackingTeamID = crew.liveTrackingTeamID
                                    connected_num++
                                } else {
                                    already_connected_num++
                                }
                                crew_instance.liveTrackingContestTeamID = crew.liveTrackingContestTeamID // every contest team mofification generate a new contest team id
                                crew_instance.liveTrackingDifferences = false
                                if (check_differencies(crew_instance, crew).different) {
                                    differency_num++
                                }
                                crew_instance.save()
                            }
                        }
                    }
                }
			}
			
			Map ret = ['instance':contest_instance, 'imported':connected_num > 0, 'different':differency_num > 0, 'message':getMsg('fc.livetracking.teams.import.done',[found_num, import_num, connected_num, already_connected_num, differency_num])]
			printdone ret
			return ret
		} else {
			Map ret = ['instance':contest_instance, 'imported':false, 'message':getMsg('fc.livetracking.teams.import.error',[ret1.errorMsg])]
			printdone ret
			return ret
		}
	}

    //--------------------------------------------------------------------------
	Map connectTeams(Contest contestInstance, Map params, session)
	{
		printstart "connectTeams"
		
		Contest contest_instance = Contest.get(contestInstance.id)
		int connect_num = 0
        int differency_num = 0
        String error_msg = ""
		 
        // import or connect selected teams if already exist in contest
        def ret9 = importTeams(params, false)
        
        Crew.findAllByContest(contest_instance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.email && !crew_instance.liveTrackingTeamID) {
                    Map team = get_team(crew_instance, true)
                    JsonBuilder team_json_builder = new JsonBuilder(team)
                    Map ret1 = call_rest("teams/", "POST", 201, team_json_builder.toString(), ALL_DATA)
                    if (team.crew.member2 && !(ret1.ok && ret1.data)) {
                        team_json_builder = new JsonBuilder(get_team(crew_instance, false))
                        ret1 = call_rest("teams/", "POST", 201, team_json_builder.toString(), ALL_DATA)
                    }
                    if (ret1.ok && ret1.data) {
                        Map contest_team = [
                            "air_speed": crew_instance.tas,
                            "tracker_device_id": crew_instance.trackerID,
                            "contest": contest_instance.liveTrackingContestID,
                            "team": ret1.data.id
                        ]
                        JsonBuilder contestteam_json_builder = new JsonBuilder(contest_team)
                        Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/contestteams/", "POST", 201, contestteam_json_builder.toString(), ALL_DATA)
                        if (ret2.ok && ret2.data) {
                            crew_instance.liveTrackingTeamID = ret1.data.id
                            crew_instance.liveTrackingContestTeamID = ret2.data.id
                            Map crew = get_new_crew(ret1.data, ret2.data, crew_instance.startNum.toString(), contest_instance)
                            crew_instance.liveTrackingDifferences = false
                            if (check_differencies(crew_instance, crew).different) {
                                differency_num++
                            }
                            crew_instance.save()
                            connect_num++
                        } else {
                            if (error_msg) {
                                error_msg += ", "
                            }
                            error_msg += ret2.errorMsg
                        }
                    } else {
                        if (error_msg) {
                            error_msg += ", "
                        }
                        error_msg += ret1.errorMsg
                    }
                    
                }
            }
        }

        String msg = ""
        boolean error = error_msg != ""
        if (ret9.imported) {
            msg = "${ret9.message} - "
            if (ret9.different) {
                error = true
            }
        }
        msg += getMsg('fc.livetracking.teams.connect.done',[connect_num, differency_num, error_msg])
		Map ret = ['error':error,'message':msg]
		printdone ret
        return ret
	}
	
    //--------------------------------------------------------------------------
    private Map get_team(Crew crewInstance, boolean withMember2)
    {
        Map team = ["aeroplane": ["registration": crewInstance.aircraft.registration, "colour":crewInstance.aircraft.colour, "type":crewInstance.aircraft.type]]
        team += ["crew": get_crew(crewInstance, withMember2)]
        if (crewInstance.team?.name) {
            team += ["club": ["name":crewInstance.team.name]]
        }
        return team
    }
    
    //--------------------------------------------------------------------------
    private Map get_crew(Crew crewInstance, boolean withMember2)
    {
        Contest contest_instance = crewInstance.contest
        String pilot_first = ""
        String pilot_last = ""
        String pilot_email = ""
        String navigator_first = ""
        String navigator_last = ""
        String navigator_email = ""
        
        List email_list = NetTools.EMailList(crewInstance.email)
        if (email_list.size() > 0) { // pilot
            pilot_email = email_list[0]
            if (email_list.size() > 1) { // navigator
                navigator_email = email_list[1]
            }
        }
        
        if (contest_instance.crewPilotNavigatorDelimiter && navigator_email) { // pilot & navigator
            List pilot_navigator = Tools.Split(crewInstance.name, contest_instance.crewPilotNavigatorDelimiter)
            if (contest_instance.crewSurnameForenameDelimiter) {
                if (pilot_navigator.size() > 0) { // pilot
                    Map pilot_name = get_person_name_with_surenameforename_delimiter(pilot_navigator[0], contest_instance.crewSurnameForenameDelimiter)
                    pilot_first = pilot_name.first
                    pilot_last = pilot_name.last
                    if (pilot_navigator.size() > 1) { // navigator
                        Map navigator_name = get_person_name_with_surenameforename_delimiter(pilot_navigator[1], contest_instance.crewSurnameForenameDelimiter)
                        navigator_first = navigator_name.first
                        navigator_last = navigator_name.last
                    }
                }
            } else {
                if (pilot_navigator.size() > 0) { // pilot
                    Map pilot_name = get_person_name_with_space_delimiter(pilot_navigator[0])
                    pilot_first = pilot_name.first
                    pilot_last = pilot_name.last
                    if (pilot_navigator.size() > 1) { // navigator
                        Map navigator_name = get_person_name_with_space_delimiter(pilot_navigator[1])
                        navigator_first = navigator_name.first
                        navigator_last = navigator_name.last
                    }
                }
            }
        } else { // only pilot
            if (contest_instance.crewSurnameForenameDelimiter) {
                Map pilot_name = get_person_name_with_surenameforename_delimiter(crewInstance.name, contest_instance.crewSurnameForenameDelimiter)
                pilot_first = pilot_name.first
                pilot_last = pilot_name.last
            } else {
                Map pilot_name = get_person_name_with_space_delimiter(crewInstance.name)
                pilot_first = pilot_name.first
                pilot_last = pilot_name.last
            }
        }
        
        if (pilot_first) {
            Map ret = ["member1": ["first_name": pilot_first, "last_name": pilot_last, "email":pilot_email]]
            if (withMember2 && navigator_first) {
                ret += ["member2": ["first_name": navigator_first, "last_name": navigator_last, "email":navigator_email]]
            }
            return ret
        }
        return ["member1": ["first_name": crewInstance.name, "last_name": EMPTY_NAME, "email":pilot_email]]
    }
        
    //--------------------------------------------------------------------------
    private Map get_person_name_with_surenameforename_delimiter(String crewName, String crewSurnameForenameDelimiter)
    {   // "Last, First1 First2" -> "First1 First2" & "Last"
        Map ret = [first:"", last:""]
        List names = Tools.Split(crewName, crewSurnameForenameDelimiter)
        if (names.size() > 0) {
            ret.last = names[0].trim()
            if (names.size() > 1) {
                ret.first = names[1].trim()
            } else {
                ret.first = ret.last
                ret.last = EMPTY_NAME
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map get_person_name_with_space_delimiter(String crewName)
    {   // "First1 First2 Last" -> "First1 First2" & "Last"
        Map ret = [first:"", last:""]
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
        } else {
            ret.last = EMPTY_NAME
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
	Map showTeamDifferencies(Contest contestInstance, Map params, session)
	{
		printstart "showTeamDifferencies"
		
        Crew crew_instance = Crew.get(params.id)
		Contest contest_instance = crew_instance.contest
        String differences = ""
        
        if (crew_instance.liveTrackingTeamID) {
            Map ret1 = call_rest("teams/${crew_instance.liveTrackingTeamID}/", "GET", 200, "", ALL_DATA)
            if (ret1.ok && ret1.data) {
                Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/contestteams/${crew_instance.liveTrackingContestTeamID}/", "GET", 200, "", ALL_DATA)
                if (ret2.ok && ret2.data) {
                    Map crew = get_new_crew(ret1.data, ret2.data, crew_instance.startNum.toString(), contest_instance)
                    crew_instance.liveTrackingDifferences = false
                    Map ret3 = check_differencies(crew_instance, crew)
                    if (ret3.different) {
                        differences = ret3.differences
                    }
                    crew_instance.save()
                }
            }
        }
                
        if (differences) {
            Map ret = ['error':true, 'message':getMsg('fc.livetracking.teams.differences',[differences])]
            printdone ret
            return ret
        }
        Map ret = ['error':false, 'message':getMsg('fc.livetracking.teams.differences.no')]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    private Map get_new_crew(Map teamData, Map contestTeamData, String startNum, Contest contestInstance)
    {
        String name = "${teamData.crew.member1.first_name} ${teamData.crew.member1.last_name}"
        if (teamData.crew.member2) {
            name += contestInstance.crewPilotNavigatorDelimiter
            name += " "
            name += "${teamData.crew.member2.first_name} ${teamData.crew.member2.last_name}"
        }
        String email = ""
        if (teamData.crew.member1.email) {
            email = teamData.crew.member1.email
        }
        if (teamData.crew.member2?.email) {
            if (email) {
                email += Defs.EMAIL_LIST_SEPARATOR
            }
            email += teamData.crew.member2.email
        }
        String teamname = ""
        if (teamData.club?.name) {
            teamname = teamData.club.name
            if (teamData.club?.country) {
                teamname += ", "
                teamname += teamData.club.country
            }
        } else if (teamData.country) {
            teamname = teamData.country
        }
        int tas = contestTeamData.air_speed
        String registration = teamData.aeroplane.registration
        String type = ""
        if (teamData.aeroplane.type) {
            type = teamData.aeroplane.type
        }
        String colour = ""
        if (teamData.aeroplane.colour) {
            colour = teamData.aeroplane.colour
        }
        return [startNum:startNum,
                name:name,
                email:email,
                teamname:teamname,
                resultclassname:"",
                tas:tas.toString(),
                registration:registration,
                type:type,
                colour:colour,
                liveTrackingTeamID:teamData.id,
                liveTrackingContestTeamID:contestTeamData.id,
                liveTrackingDifferences:false,
                trackerID:contestTeamData.tracker_device_id
               ]
    }
    
    //--------------------------------------------------------------------------
    private Map check_differencies(Crew crewInstance, Map crewData)
    {
        Map ret = [different:false, differences:""]
        if (crewInstance.name != crewData.name) {
            crewInstance.liveTrackingDifferences = true
            ret.differences += "'${crewInstance.name}' <> '${crewData.name}'"
            ret.different = true
        }
        BigDecimal data_tas = crewData.tas.toBigDecimal()
        if (crewInstance.tas != data_tas) {
            crewInstance.liveTrackingDifferences = true
            if (ret.differences) {
                ret.differences += ", "
            }
            ret.differences += "'${crewInstance.tas}' <> '${data_tas}'"
            ret.different = true
        }
        if (crewInstance.aircraft) {
            if (crewInstance.aircraft.registration != crewData.registration) {
                crewInstance.liveTrackingDifferences = true
                if (ret.differences) {
                    ret.differences += ", "
                }
                ret.differences += "'${crewInstance.aircraft.registration}' <> '${crewData.registration}'"
                ret.different = true
            }
            if (crewInstance.aircraft.type != crewData.type) {
                crewInstance.liveTrackingDifferences = true
                if (ret.differences) {
                    ret.differences += ", "
                }
                ret.differences += "'${crewInstance.aircraft.type}' <> '${crewData.type}'"
                ret.different = true
            }
            if (crewInstance.aircraft.colour != crewData.colour) {
                crewInstance.liveTrackingDifferences = true
                if (ret.differences) {
                    ret.differences += ", "
                }
                ret.differences += "'${crewInstance.aircraft.colour}' <> '${crewData.colour}'"
                ret.different = true
            }
        } else {
            crewInstance.liveTrackingDifferences = true
            if (ret.differences) {
                ret.differences += ", "
            }
            ret.differences += "'' <> '${crewData.registration}'"
            ret.different = true
        }
        String crew_team_name = ""
        if (crewInstance.team) {
            crew_team_name = crewInstance.team.name
        }
        String data_team_name = ""
        if (crewData.teamname) {
            data_team_name = crewData.teamname
        }
        if (crew_team_name != data_team_name) {
            crewInstance.liveTrackingDifferences = true
            if (ret.differences) {
                ret.differences += ", "
            }
            ret.differences += "'${crew_team_name}' <> '${data_team_name}'"
            ret.different = true
        }
        if (ret.different) {
            println "check_differencies $ret"
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
	Map disconnectTeams(Contest contestInstance, Map params, session)
	{
		printstart "disconnectTeams"
		
		Contest contest_instance = Contest.get(contestInstance.id)
		int disconnect_num = 0
        String error_msg = ""
		 
        Crew.findAllByContest(contest_instance,[sort:"viewpos"]).each { Crew crew_instance ->
            if (params["selectedCrewID${crew_instance.id}"] == "on") {
                if (crew_instance.liveTrackingTeamID) {

                    // remove team from contest
                    Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/contestteams/${crew_instance.liveTrackingContestTeamID}/", "DELETE", 204, "", "")
                    if (!ret1.ok) {
                        if (error_msg) {
                            error_msg += ", "
                        }
                        error_msg += ret1.errorMsg
                    }
                    reset_crew(crew_instance)
                    disconnect_num++
                }
            }
        }

		Map ret = ['error':error_msg != '','message':getMsg('fc.livetracking.teams.disconnect.done',[disconnect_num, error_msg])]
		printdone ret
        return ret
	}
	
    //--------------------------------------------------------------------------
    Map saveLiveTrackingTask(Map params)
    {
		printstart "saveLiveTrackingTask"
		
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
            
            task_instance.properties = params
            
            if (params["liveTrackingResultsFlightOn"]) {
                task_instance.liveTrackingResultsFlightOn = true
            } else {
                task_instance.liveTrackingResultsFlightOn = false
            }
            if (params["liveTrackingResultsPublishImmediately"]) {
                task_instance.liveTrackingResultsPublishImmediately = true
            } else {
                task_instance.liveTrackingResultsPublishImmediately = false
            }
            if (params["setLiveTrackingNavigationTaskDate"]) {
                task_instance.setLiveTrackingNavigationTaskDate = true
            } else {
                task_instance.setLiveTrackingNavigationTaskDate = false
            }
            
            if(!task_instance.hasErrors() && task_instance.save()) {
                if (!task_instance.liveTrackingResultsFlightOn) {
                    reset_tests2(task_instance, ResultType.Flight)
                }
				try {
	                Map ret = ['instance':task_instance,'saved':true,'message':getMsg('fc.saved',["${task_instance.name()}"])]
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
    Map createNavigationTask(Map params)
    {
        Map ret9 = saveLiveTrackingTask(params)
        if (!ret9.saved) {
            return ret9
        }
        
        printstart "createNavigationTask $params"
        
        Task task_instance = Task.get(params.id)
        Map ret4 = is_task_complete(task_instance, !task_instance.setLiveTrackingNavigationTaskDate)
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
            
        if (task_instance.setLiveTrackingNavigationTaskDate) {
            task_instance.liveTrackingNavigationTaskDate = FcTime.GetDateStr(new Date())
            if (task_instance.liveTrackingNavigationTaskDate < task_instance.contest.liveTrackingContestDate) {
                task_instance.liveTrackingNavigationTaskDate = task_instance.contest.liveTrackingContestDate
            }
            task_instance.save()
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
        List test_list = []
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.crew.liveTrackingTeamID && test_instance.IsFlightTestRun()) {
                Map contestant = get_contestant(test_instance, task_instance.liveTrackingNavigationTaskDate, tracks_available)
                contestant_list += contestant.data
                finish_date = contestant.arrivalTime
                test_list += test_instance
            }
        }
        if (false) { // write gpx to log
            log.println "GPX1"
            log.println new String(Base64.getDecoder().decode(route_data),"UTF-8")
            log.println "GPX2"
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
        JsonBuilder json_builder = new JsonBuilder()
        json_builder {
            name task_instance.GetMediaName(Media.Tracking)
            original_scorecard route_instance.liveTrackingScorecard
            start_time FcTime.UTCGetDateTime(task_instance.liveTrackingNavigationTaskDate, first_date, task_instance.contest.timeZone)
            finish_time FcTime.UTCGetDateTime(task_instance.liveTrackingNavigationTaskDate, finish_date, task_instance.contest.timeZone)
            contestant_set contestant_list
            route_file route_data
        }
        
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/importnavigationtaskteamid/", "POST", 201, json_builder.toString(), "id")
    
        gpxService.DeleteFile(upload_gpx_file_name)
        
        if (!ret1.data) {
            Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.navigationtaskcreate.error',[route_instance.liveTrackingScorecard, ret1.errorMsg])]
            printerror ret
            return ret
        }
        
        String error_msg = ""
        JsonBuilder json_builder2 = new JsonBuilder()
        json_builder2 {
            gatescore_set get_gate_score_overrides(score_values, gate_types)
            backtracking_penalty score_values.flightTestBadCoursePoints
            backtracking_grace_time_seconds score_values.flightTestBadCourseCorrectSecond
            backtracking_maximum_penalty score_values.flightTestBadCourseMaxPoints
        }
        Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${ret1.data}/scorecard/", "PUT", 200, json_builder2.toString(), "id")
        if (!ret2.ok) {
            error_msg = ret2.errorMsg
        }
    
        task_instance.liveTrackingTracksAvailable = tracks_available
        task_instance.liveTrackingNavigationTaskID = ret1.data.toInteger()
        task_instance.save()
        
        for (Test test_instance in test_list) {
            test_instance.taskLiveTrackingTeamID = test_instance.crew.liveTrackingTeamID
        }
        
        // get result task id and navigation tasktest id
        Map ret5 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/", "GET", 200, "", ALL_DATA)
        if (ret5.ok && ret5.data) {
            for (Map tasktest in ret5.data) {
                if (tasktest.navigation_task == task_instance.liveTrackingNavigationTaskID) {
                    task_instance.liveTrackingResultsTaskID = tasktest.task
                    task_instance.liveTrackingResultsFlightID = tasktest.id
                    task_instance.liveTrackingResultsFlightOn = false
                    task_instance.save()
                    break
                }
            }
        }
        
        Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.navigationtaskcreate.done',[task_instance.liveTrackingNavigationTaskID, error_msg])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map connectNavigationTask(Map params)
    {
        Map ret9 = saveLiveTrackingTask(params)
        if (!ret9.saved) {
            return ret9
        }
        
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
        String navigationtask_visibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
        Map ret3 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/", "GET", 200, "", ALL_DATA)
		if (ret3.ok && ret3.data) {
            for (Map navigationtask in ret3.data) {
				if (navigationtask.name == task_instance.GetMediaName(Media.Tracking)) {
					navigationtask_id = navigationtask.id
                    if (navigationtask.start_time.size() >= 10) {
                        navigationtask_date = navigationtask.start_time.substring(0,10)
                    }
                    navigationtask_visibility = navigationtask.share_string.toLowerCase()
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
        if (true) {
            task_instance.liveTrackingTracksAvailable = check_tracks_availability(task_instance)
        }
        task_instance.liveTrackingNavigationTaskVisibility = navigationtask_visibility
        task_instance.liveTrackingNavigationTaskDate = navigationtask_date
        
        String error_msg = ""

        // search assign teams
        Map ret7 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${navigationtask_id}/", "GET", 200, "", ALL_DATA)
        if (ret7.ok && ret7.data) {
            for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
                if (test_instance.crew.liveTrackingTeamID) {
                    for (Map contestant in ret7.data.contestant_set) {
                        if (test_instance.crew.liveTrackingTeamID == contestant.team.id) {
                            test_instance.taskLiveTrackingTeamID = contestant.team.id
                            test_instance.save()
                            break
                        }
                    }
                }
            }
        } else {
            error_msg = ret5.errorMsg
        }
        
        // get result task id and navigation tasktest id
        Map ret5 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/", "GET", 200, "", ALL_DATA)
        if (ret5.ok && ret5.data) {
            for (Map tasktest in ret5.data) {
                if (tasktest.navigation_task == task_instance.liveTrackingNavigationTaskID) {
                    task_instance.liveTrackingResultsTaskID = tasktest.task
                    task_instance.liveTrackingResultsFlightID = tasktest.id
                    task_instance.liveTrackingResultsFlightOn = false
                    break
                }
            }
        } else {
            error_msg = ret5.errorMsg
        }
        
        // get special result tasktest ids
        Map ret6 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/", "GET", 200, "", ALL_DATA)
        if (ret6.ok && ret6.data) {
            for (Map tasktest in ret6.data) {
                if (tasktest.task == task_instance.liveTrackingResultsTaskID) {
                    if (tasktest.name == Defs.LIVETRACKING_TASKTEST_PLANNING) {
                        task_instance.liveTrackingResultsPlanningID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_OBSERVATION) {
                        task_instance.liveTrackingResultsObservationID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING) {
                        task_instance.liveTrackingResultsLandingID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING1) {
                        task_instance.liveTrackingResultsLanding1ID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING2) {
                        task_instance.liveTrackingResultsLanding2ID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING3) {
                        task_instance.liveTrackingResultsLanding3ID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING4) {
                        task_instance.liveTrackingResultsLanding4ID = tasktest.id
                    } else if (tasktest.name == Defs.LIVETRACKING_TASKTEST_SPECIAL) {
                        task_instance.liveTrackingResultsSpecialID = tasktest.id
                    }
                }
            }
        } else {
            if (error_msg) {
                error_msg += ", "
            }
            error_msg += ret6.errorMsg
        }

        if (error_msg) {
            error_msg = ": " + error_msg
        }
        
        task_instance.save()
        
        Map ret = ['instance':task_instance, 'connected':true, 'message':getMsg('fc.livetracking.navigationtaskconnect.done',[navigationtask_id]) + error_msg]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    private boolean check_tracks_availability(Task taskInstance)
    {
        boolean tracks_available = true
        boolean test_found = false
        for (Test test_instance in Test.findAllByTask(taskInstance,[sort:'viewpos'])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.crew.liveTrackingTeamID) {
                if (test_instance.IsFlightTestRun() && test_instance.timeCalculated) {
                    test_found = true
                    if (!test_instance.flightTestComplete) {
                        tracks_available = false
                    }
                }
            }
        }
        if (test_found && tracks_available) {
            return true
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
			if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.crew.liveTrackingTeamID) {
	            if (!test_instance.flighttestwind) {
                    ret.message = getMsg('fc.flighttestwind.notassigned')
                    return ret
	            }
                enabled_crew_num++
            }
        }
        
        // Any crew configured for live tracking?
        if (!enabled_crew_num) {
            ret.message = getMsg('fc.livetracking.teams.minimum.one')
            return ret
        }

        // Time calculated for all crews?
        for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
			if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.crew.liveTrackingTeamID) {
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
        
        // delete all result tasktests
        Map ret5 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/", "GET", 200, "", ALL_DATA)
        if (ret5.ok && ret5.data) {
            for (Map tasktest in ret5.data) {
                if (tasktest.task == task_instance.liveTrackingResultsTaskID) {
                    Map ret6 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/${tasktest.id}/", "DELETE", 204, "", "")
                    if (ret6.ok) {
                        if (tasktest.id == task_instance.liveTrackingResultsPlanningID) {
                            task_instance.liveTrackingResultsPlanningID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsFlightID) {
                            task_instance.liveTrackingResultsFlightID = 0
                            task_instance.liveTrackingResultsFlightOn = false
                        } else if (tasktest.id == task_instance.liveTrackingResultsObservationID) {
                            task_instance.liveTrackingResultsObservationID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsLandingID) {
                            task_instance.liveTrackingResultsLandingID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsLanding1ID) {
                            task_instance.liveTrackingResultsLanding1ID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsLanding2ID) {
                            task_instance.liveTrackingResultsLanding2ID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsLanding3ID) {
                            task_instance.liveTrackingResultsLanding3ID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsLanding4ID) {
                            task_instance.liveTrackingResultsLanding4ID = 0
                        } else if (tasktest.id == task_instance.liveTrackingResultsSpecialID) {
                            task_instance.liveTrackingResultsSpecialID = 0
                        }
                        println "Result tasktest ${tasktest.id}, name:'${tasktest.name}', heading:'${tasktest.heading}' deleted"
                    }
                }
            }
        }
        
        // delete result task
        ret5 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasks/", "GET", 200, "", ALL_DATA)
        if (ret5.ok && ret5.data) {
            for (Map task in ret5.data) {
                if (task.id == task_instance.liveTrackingResultsTaskID) {
                    Map ret6 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasks/${task.id}/", "DELETE", 204, "", "")
                    if (ret6.ok) {
                        task_instance.liveTrackingResultsTaskID = 0
                        println "Result task ${task.id}, name:'${task.name}', heading:'${task.heading}' deleted"
                    }
                }
            }
        }
        
        task_instance.liveTrackingResultsPublishImmediately = false
        task_instance.save()
        
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/", "DELETE", 204, "", "")
        
        if (ret1.ok) {
            reset_task(task_instance)
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
    Map createTask(Map params)
    {
        Map ret9 = saveLiveTrackingTask(params)
        if (!ret9.saved) {
            return ret9
        }
        
        printstart "createTask"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        
        boolean connected = false
        String error_msg = ""
        JsonBuilder json_builder_createtask = new JsonBuilder()
        json_builder_createtask {
            name task_instance.name()
            heading task_instance.name()
            contest contest_instance.liveTrackingContestID
            index 1
        }
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasks/", "POST", 201, json_builder_createtask.toString(), "id")
        if (ret1.ok && ret1.data) {
            task_instance.liveTrackingResultsTaskID = ret1.data.toInteger()
            task_instance.save()
            
            Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.results.task.create.done')]
            printdone ret
            return ret
        } else {
            Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasks/", "GET", 200, "", ALL_DATA)
            if (ret2.ok && ret2.data) {
                for (Map task in ret2.data) {
                    if (task.heading == task_instance.name()) {
                        task_instance.liveTrackingResultsTaskID = task.id.toInteger()
                        task_instance.save()
                        connected = true
                        break
                    }
                }
            }
            error_msg = ret2.errorMsg
        }
        
        if (connected) {
            Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.results.task.connected.done')]
            printdone ret
            return ret
        }
        Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.results.task.create.error',[error_msg])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map deleteTask(Map params)
    {
        Map ret9 = saveLiveTrackingTask(params)
        if (!ret9.saved) {
            return ret9
        }
        
        printstart "deleteTask"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        
        boolean deleted = false
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasks/", "GET", 200, "", ALL_DATA)
        if (ret1.ok && ret1.data) {
            for (Map task in ret1.data) {
                if (task.id == task_instance.liveTrackingResultsTaskID) {
                    Map ret6 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasks/${task.id}/", "DELETE", 204, "", "")
                    if (ret6.ok) {
                        reset_task(task_instance)
                        task_instance.save()
                        println "Result task ${task.id}, name:'${task.name}', heading:'${task.heading}' deleted"
                        deleted = true
                        break
                    }
                }
            }
        }
        if (!deleted) {
            reset_task(task_instance)
            task_instance.save()
            
            Map ret = ['instance':task_instance, 'deleted':true, 'message':getMsg('fc.livetracking.results.task.discconect.done')]
            printdone ret
            return ret
        }
        
        Map ret = ['instance':task_instance, 'deleted':true, 'message':getMsg('fc.livetracking.results.task.delete.done')]
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    Map createTest(Map params, ResultType resultType)
    {
        Map ret9 = saveLiveTrackingTask(params)
        if (!ret9.saved) {
            return ret9
        }
        
        printstart "createTest $resultType $params"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        
        JsonBuilder json_builder_tasktest = new JsonBuilder()
        switch (resultType) {
            case ResultType.Planningtask:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_PLANNING
                    heading Defs.LIVETRACKING_TASKTEST_PLANNING
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Observation:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_OBSERVATION
                    heading Defs.LIVETRACKING_TASKTEST_OBSERVATION
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Landing:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_LANDING
                    heading Defs.LIVETRACKING_TASKTEST_LANDING
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Landing1:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_LANDING1
                    heading Defs.LIVETRACKING_TASKTEST_LANDING1
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Landing2:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_LANDING2
                    heading Defs.LIVETRACKING_TASKTEST_LANDING2
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Landing3:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_LANDING3
                    heading Defs.LIVETRACKING_TASKTEST_LANDING3
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Landing4:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_LANDING4
                    heading Defs.LIVETRACKING_TASKTEST_LANDING4
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
            case ResultType.Special:
                json_builder_tasktest {
                    name Defs.LIVETRACKING_TASKTEST_SPECIAL
                    heading Defs.LIVETRACKING_TASKTEST_SPECIAL
                    task task_instance.liveTrackingResultsTaskID
                    index 1
                }
                break
        }
        Map ret6 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/", "POST", 201, json_builder_tasktest.toString(), "id")
        if (ret6.ok && ret6.data) {
            switch (resultType) {
                case ResultType.Planningtask:
                    task_instance.liveTrackingResultsPlanningID = ret6.data.toInteger()
                    break
                case ResultType.Observation:
                    task_instance.liveTrackingResultsObservationID = ret6.data.toInteger()
                    break
                case ResultType.Landing:
                    task_instance.liveTrackingResultsLandingID = ret6.data.toInteger()
                    break
                case ResultType.Landing1:
                    task_instance.liveTrackingResultsLanding1ID = ret6.data.toInteger()
                    break
                case ResultType.Landing2:
                    task_instance.liveTrackingResultsLanding2ID = ret6.data.toInteger()
                    break
                case ResultType.Landing3:
                    task_instance.liveTrackingResultsLanding3ID = ret6.data.toInteger()
                    break
                case ResultType.Landing4:
                    task_instance.liveTrackingResultsLanding4ID = ret6.data.toInteger()
                    break
                case ResultType.Special:
                    task_instance.liveTrackingResultsSpecialID = ret6.data.toInteger()
                    break
            }
            task_instance.save()
        } else {
            boolean connected = false
            Map ret7 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/", "GET", 200, "", ALL_DATA)
            if (ret7.ok && ret7.data) {
                for (Map tasktest in ret7.data) {
                    if (tasktest.task == task_instance.liveTrackingResultsTaskID) {
                        switch (resultType) {
                            case ResultType.Planningtask:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_PLANNING) {
                                    task_instance.liveTrackingResultsPlanningID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Observation:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_OBSERVATION) {
                                    task_instance.liveTrackingResultsObservationID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Landing:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING) {
                                    task_instance.liveTrackingResultsLandingID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Landing1:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING1) {
                                    task_instance.liveTrackingResultsLanding1ID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Landing2:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING2) {
                                    task_instance.liveTrackingResultsLanding2ID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Landing3:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING3) {
                                    task_instance.liveTrackingResultsLanding3ID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Landing4:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_LANDING4) {
                                    task_instance.liveTrackingResultsLanding4ID = tasktest.id
                                    connected = true
                                }
                                break
                            case ResultType.Special:
                                if (tasktest.name == Defs.LIVETRACKING_TASKTEST_SPECIAL) {
                                    task_instance.liveTrackingResultsSpecialID = tasktest.id
                                    connected = true
                                }
                                break
                        }
                    }
                }
            }

            if (connected) {
                Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.results.tasktestcreate.connected')]
                printdone ret
                return ret
            } else {
                Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.results.tasktestcreate.error',[ret6.errorMsg])]
                printerror ret
                return ret
            }
        }
        
        Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.results.tasktestcreate.done')]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map deleteTest(Map params, ResultType resultType)
    {
        Map ret9 = saveLiveTrackingTask(params)
        if (!ret9.saved) {
            return ret9
        }
        
        printstart "deleteTest $resultType $params"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        
        Integer tasktest_id = 0
        switch (resultType) {
            case ResultType.Planningtask:
                tasktest_id = task_instance.liveTrackingResultsPlanningID
                task_instance.liveTrackingResultsPlanningID = 0
                break
            case ResultType.Observation:
                tasktest_id = task_instance.liveTrackingResultsObservationID
                task_instance.liveTrackingResultsObservationID = 0
                break
            case ResultType.Landing:
                tasktest_id = task_instance.liveTrackingResultsLandingID
                task_instance.liveTrackingResultsLandingID = 0
                break
            case ResultType.Landing1:
                tasktest_id = task_instance.liveTrackingResultsLanding1ID
                task_instance.liveTrackingResultsLanding1ID = 0
                break
            case ResultType.Landing2:
                tasktest_id = task_instance.liveTrackingResultsLanding2ID
                task_instance.liveTrackingResultsLanding2ID = 0
                break
            case ResultType.Landing3:
                tasktest_id = task_instance.liveTrackingResultsLanding3ID
                task_instance.liveTrackingResultsLanding3ID = 0
                break
            case ResultType.Landing4:
                tasktest_id = task_instance.liveTrackingResultsLanding4ID
                task_instance.liveTrackingResultsLanding4ID = 0
                break
            case ResultType.Special:
                tasktest_id = task_instance.liveTrackingResultsSpecialID
                task_instance.liveTrackingResultsSpecialID = 0
                break
        }
        task_instance.save()
        reset_tests2(task_instance, resultType)
        
        Map ret6 = call_rest("contests/${contest_instance.liveTrackingContestID}/tasktests/${tasktest_id}/", "DELETE", 204, "", "")
        if (!ret6.ok) {
            Map ret = ['instance':task_instance, 'created':false, 'message':getMsg('fc.livetracking.results.tasktestdelete.error',[ret6.errorMsg])]
            printerror ret
            return ret
        }
        
        Map ret = ['instance':task_instance, 'created':true, 'message':getMsg('fc.livetracking.results.tasktestdelete.done')]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    private void reset_tests2(Task taskInstance, ResultType resultType)
    {
        for (Test test_instance in Test.findAllByTask(taskInstance)) {
            switch (resultType) {
                case ResultType.Planningtask:
                    test_instance.planningTestLiveTrackingResultOk = false
                    test_instance.planningTestLiveTrackingResultError = false
                    break
                case ResultType.Flight:
                    test_instance.flightTestLiveTrackingResultOk = false
                    test_instance.flightTestLiveTrackingResultError = false
                    break
                case ResultType.Observation:
                    test_instance.observationTestLiveTrackingResultOk = false
                    test_instance.observationTestLiveTrackingResultError = false
                    break
                case ResultType.Landing:
                    test_instance.landingTestLiveTrackingResultOk = false
                    test_instance.landingTestLiveTrackingResultError = false
                    break
                case ResultType.Landing1:
                    test_instance.landingTest1LiveTrackingResultOk = false
                    test_instance.landingTest1LiveTrackingResultError = false
                    break
                case ResultType.Landing2:
                    test_instance.landingTest2LiveTrackingResultOk = false
                    test_instance.landingTest2LiveTrackingResultError = false
                    break
                case ResultType.Landing3:
                    test_instance.landingTest3LiveTrackingResultOk = false
                    test_instance.landingTest3LiveTrackingResultError = false
                    break
                case ResultType.Landing4:
                    test_instance.landingTest4LiveTrackingResultOk = false
                    test_instance.landingTest4LiveTrackingResultError = false
                    break
                case ResultType.Special:
                    test_instance.specialTestLiveTrackingResultOk = false
                    test_instance.specialTestLiveTrackingResultError = false
                    break
            }
        }
    }
    
    //--------------------------------------------------------------------------
    Map disconnectNavigationTask(Map params)
    {
        printstart "disconnectNavigationTask"
        
        Task task_instance = Task.get(params.id)
        Integer old_navigationtask_id = task_instance.liveTrackingNavigationTaskID
        
        reset_task(task_instance)
        
		Map ret = ['instance':task_instance, 'disconnected':true, 'message':getMsg('fc.livetracking.navigationtaskdisconnect.done',[old_navigationtask_id])]
		printdone ret
		return ret
    }
    
    //--------------------------------------------------------------------------
    Map setNavigationTaskVisibility(Map params, String newVisibility)
    {
        printstart "setNavigationTaskVisibility $newVisibility"
        
        Task task_instance = Task.get(params.id)
        Contest contest_instance = task_instance.contest
        
        boolean error = false
        
        JsonBuilder json_builder = new JsonBuilder()
        json_builder {
            visibility newVisibility
        }
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/share/", "PUT", 200, json_builder.toString(), "")
        if (ret1.ok) {
            task_instance.liveTrackingNavigationTaskVisibility = newVisibility
            task_instance.save()
            
            // set contest visiblity higher
            switch (newVisibility) {
                case Defs.LIVETRACKING_VISIBILITY_UNLISTED:
                    if (contest_instance.liveTrackingContestVisibility == Defs.LIVETRACKING_VISIBILITY_PRIVATE) {
                        contest_instance.liveTrackingContestVisibility = Defs.LIVETRACKING_VISIBILITY_UNLISTED
                        contest_instance.save()
                    }
                    break
                case Defs.LIVETRACKING_VISIBILITY_PUBLIC:
                    switch (contest_instance.liveTrackingContestVisibility) {
                        case Defs.LIVETRACKING_VISIBILITY_PRIVATE:
                        case Defs.LIVETRACKING_VISIBILITY_UNLISTED:
                            contest_instance.liveTrackingContestVisibility = Defs.LIVETRACKING_VISIBILITY_PUBLIC
                            contest_instance.save()
                            break
                    }
                    break
            }
        } else {
            error = true
        }
        
        if (error) {
            Map ret = ['instance':task_instance, 'error':true, 'message':getMsg('fc.livetracking.navigationtaskvisibility.notset',[ret1.errorMsg])]
            printerror ret
            return ret
        }
        Map ret = ['instance':task_instance, 'error':false, 'message':getMsg('fc.livetracking.navigationtaskvisibility.set',[newVisibility])]
        printdone ret
        return ret
    }
    
    //--------------------------------------------------------------------------
    private void reset_task(Task taskInstance)
    {
        reset_tests(taskInstance)
		taskInstance.liveTrackingNavigationTaskID = 0
        taskInstance.liveTrackingNavigationTaskVisibility = Defs.LIVETRACKING_VISIBILITY_PRIVATE
        taskInstance.liveTrackingResultsTaskID = 0
        taskInstance.liveTrackingResultsPlanningID = 0
        taskInstance.liveTrackingResultsFlightID = 0
        taskInstance.liveTrackingResultsFlightOn = false
        taskInstance.liveTrackingResultsObservationID = 0
        taskInstance.liveTrackingResultsLandingID = 0
        taskInstance.liveTrackingResultsLanding1ID = 0
        taskInstance.liveTrackingResultsLanding2ID = 0
        taskInstance.liveTrackingResultsLanding3ID = 0
        taskInstance.liveTrackingResultsLanding4ID = 0
        taskInstance.liveTrackingResultsSpecialID = 0
        taskInstance.liveTrackingTracksAvailable = false
        taskInstance.liveTrackingResultsPublishImmediately = false
        taskInstance.save()
    }
    
    //--------------------------------------------------------------------------
    private void reset_tests(Task taskInstance)
    {
        for (Test test_instance in Test.findAllByTask(taskInstance)) {
            test_instance.planningTestLiveTrackingResultOk = false
            test_instance.planningTestLiveTrackingResultError = false
            test_instance.flightTestLiveTrackingResultOk = false
            test_instance.flightTestLiveTrackingResultError = false
            test_instance.observationTestLiveTrackingResultOk = false
            test_instance.observationTestLiveTrackingResultError = false
            test_instance.landingTestLiveTrackingResultOk = false
            test_instance.landingTest1LiveTrackingResultOk = false
            test_instance.landingTest2LiveTrackingResultOk = false
            test_instance.landingTest3LiveTrackingResultOk = false
            test_instance.landingTest4LiveTrackingResultOk = false
            test_instance.landingTestLiveTrackingResultError = false
            test_instance.landingTest1LiveTrackingResultError = false
            test_instance.landingTest2LiveTrackingResultError = false
            test_instance.landingTest3LiveTrackingResultError = false
            test_instance.landingTest4LiveTrackingResultError = false
            test_instance.specialTestLiveTrackingResultOk = false
            test_instance.specialTestLiveTrackingResultError = false
            test_instance.taskLiveTrackingTeamID = 0
        }
    }
    
    //--------------------------------------------------------------------------
    private void reset_crew(Crew crewInstance)
    {
        crewInstance.liveTrackingTeamID = 0
        crewInstance.liveTrackingContestTeamID = 0
        crewInstance.liveTrackingDifferences = 0
        crewInstance.save()
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
                if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.taskLiveTrackingTeamID && test_instance.IsFlightTestRun() && test_instance.timeCalculated && test_instance.flightTestComplete) {
                    GregorianCalendar endtime_calendar = new GregorianCalendar() 
                    endtime_calendar.setTime(test_instance.finishTime)
                    endtime_calendar.add(Calendar.MINUTE, FP_MINUTES)
                    track_end_time = endtime_calendar.getTime()
                    break
                }
            }
        }
       
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.taskLiveTrackingTeamID && test_instance.IsFlightTestRun() && test_instance.timeCalculated && test_instance.flightTestComplete) {
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
        
        Map ret1 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/", "GET", 200, "", ALL_DATA)
        if (!ret1.data) {
            Map ret = [taskInstance:task_instance, error:true, message:getMsg('fc.livetracking.error',[ret1.errorMsg])]
			printerror ret.message
            return ret
        }

        // update crews
        int crew_num = 0
        int error_num = 0
        String error_str = ""
        for (Test test_instance in Test.findAllByTask(task_instance,[sort:'viewpos'])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.crew.liveTrackingTeamID && test_instance.timeCalculated && test_instance.id in test_instance_ids) {
                if (!test_instance.taskLiveTrackingTeamID) {
                    // add team to navigation task
                    Map contestant = get_contestant(test_instance, task_instance.liveTrackingNavigationTaskDate, false)
                    JsonBuilder json_builder = new JsonBuilder(contestant.data)
                    Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestantsteamid/", "POST", 201, json_builder.toString(), ALL_DATA)
                    if (ret2.ok) {
                        test_instance.taskLiveTrackingTeamID = test_instance.crew.liveTrackingTeamID
                        test_instance.save()
                    } else {
                        error_str += ret2.errorMsg
                        error_num++
                    }
                } else {
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
            Map ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/${contestant_id}/update_without_team/", "PUT", 200, json_builder.toString(), "")
            // ret2 = call_rest("contests/${contest_instance.liveTrackingContestID}/navigationtasks/${task_instance.liveTrackingNavigationTaskID}/contestants/${contestant_id}/", "PUT", 200, json_builder.toString(), "")
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
        
        Media media = Media.Tracking
        if (testInstance.flighttestwind.flighttest.route.corridorWidth) {
            media = Media.TrackingANR
        }
        
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
            gate_times += ["${testlegflight_instance.coordTitle.titleMediaCode(media)}":FcTime.UTCGetDateTime(trackingDate, leg_time, contest_instance.timeZone)] 
        }
        gate_times += ["LDG":ldg_time]
        
        ret.arrivalTime = testInstance.arrivalTime
        
        ret.data = [
            "contestant_number": testInstance.crew.startNum,
            "team": testInstance.crew.liveTrackingTeamID,
            "air_speed": testInstance.taskTAS,
            "tracker_device_id":  testInstance.taskTrackerID,
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
        if (contest_instance.resultClasses) {
            Map class_name = testInstance.GetResultClassName()
            ret.data += ["competition_class_longform": class_name.name,
                         "competition_class_shortform": class_name.shortName
                        ]
        }
                        
        return ret
    }
    
    /*
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
    */
    
    //--------------------------------------------------------------------------
    private List get_gate_score_overrides(Map scoreValues, Map gateType)
    {
        List gate_score_overrides = []
        
        // TO
        if (gateType.TO) {
            if (scoreValues.flightTestCheckTakeOff) {
                Map to_override = [gate_type:          "to",
                                   graceperiod_before: 0,
                                   graceperiod_after:  scoreValues.flightTestTakeoffCorrectSecond,
                                   missed_penalty:     scoreValues.flightTestTakeoffMissedPoints,
                                   maximum_penalty:    scoreValues.flightTestTakeoffMissedPoints
                                  ]
                if (scoreValues.flightTestTakeoffCheckSeconds) {
                    to_override += [penalty_per_second: scoreValues.flightTestTakeoffPointsPerSecond]
                } else {
                    to_override += [penalty_per_second: scoreValues.flightTestTakeoffMissedPoints]
                }
                gate_score_overrides += to_override
            } else {
                Map to_override = [gate_type:          "to",
                                   graceperiod_before: 0,
                                   graceperiod_after:  scoreValues.flightTestTakeoffCorrectSecond,
                                   missed_penalty:     0,
                                   maximum_penalty:    0,
                                   penalty_per_second: 0
                                  ]
                gate_score_overrides += to_override
            }
        }
        
        // SP
        if (gateType.SP) {
            Map sp_override = [gate_type:                          "sp",
                               graceperiod_before:                 scoreValues.flightTestCptimeCorrectSecond,
                               graceperiod_after:                  scoreValues.flightTestCptimeCorrectSecond,
                               penalty_per_second:                 scoreValues.flightTestCptimePointsPerSecond,
                               maximum_penalty:                    scoreValues.flightTestCptimeMaxPoints,
                               missed_penalty:                     scoreValues.flightTestCpNotFoundPoints,
                               bad_crossing_extended_gate_penalty: scoreValues.flightTestBadCoursePoints
                              ]
            gate_score_overrides += sp_override
        }
        
        // TP
        if (gateType.TP) {
            Map tp_override = [gate_type:                     "tp",
                               graceperiod_before:            scoreValues.flightTestCptimeCorrectSecond,
                               graceperiod_after:             scoreValues.flightTestCptimeCorrectSecond,
                               penalty_per_second:            scoreValues.flightTestCptimePointsPerSecond,
                               maximum_penalty:               scoreValues.flightTestCptimeMaxPoints,
                               missed_penalty:                scoreValues.flightTestCpNotFoundPoints,
                               missed_procedure_turn_penalty: scoreValues.flightTestProcedureTurnNotFlownPoints
                              ]
            gate_score_overrides += tp_override
        }
        
        // SC
        if (gateType.SC) {
            if (scoreValues.flightTestCheckSecretPoints) {
                Map secret_override = [gate_type:          "secret",
                                       graceperiod_before: scoreValues.flightTestCptimeCorrectSecond,
                                       graceperiod_after:  scoreValues.flightTestCptimeCorrectSecond,
                                       penalty_per_second: scoreValues.flightTestCptimePointsPerSecond,
                                       maximum_penalty:    scoreValues.flightTestCptimeMaxPoints,
                                       missed_penalty:     scoreValues.flightTestCpNotFoundPoints
                                      ]
                gate_score_overrides += secret_override
            } else {
                Map secret_override = [gate_type:          "secret",
                                       graceperiod_before: scoreValues.flightTestCptimeCorrectSecond,
                                       graceperiod_after:  scoreValues.flightTestCptimeCorrectSecond,
                                       penalty_per_second: 0,
                                       maximum_penalty:    0,
                                       missed_penalty:     0
                                      ]
                gate_score_overrides += secret_override
            }
        }
        
        // FP
        if (gateType.FP) {
            Map fp_override = [gate_type:          "fp",
                               graceperiod_before: scoreValues.flightTestCptimeCorrectSecond,
                               graceperiod_after:  scoreValues.flightTestCptimeCorrectSecond,
                               penalty_per_second: scoreValues.flightTestCptimePointsPerSecond,
                               maximum_penalty:    scoreValues.flightTestCptimeMaxPoints,
                               missed_penalty:     scoreValues.flightTestCpNotFoundPoints
                              ]
            gate_score_overrides += fp_override
        }
        
        // LDG
        if (gateType.LDG) {
            if (scoreValues.flightTestCheckLanding) {
                Map ldg_override = [gate_type:          "ldg",
                                    graceperiod_before: 1800, // 30 min
                                    graceperiod_after:  0,
                                    missed_penalty:     scoreValues.flightTestLandingToLatePoints,
                                    maximum_penalty:    scoreValues.flightTestLandingToLatePoints,
                                    penalty_per_second: scoreValues.flightTestLandingToLatePoints
                                   ]
                gate_score_overrides += ldg_override
            } else {
                Map ldg_override = [gate_type:          "ldg",
                                    graceperiod_before: 0,
                                    graceperiod_after:  0,
                                    missed_penalty:     0,
                                    maximum_penalty:    0,
                                    penalty_per_second: 0
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
            if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.taskLiveTrackingTeamID && test_instance.timeCalculated) {
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
                if (!ret2.ok) {
                    println "Error: $ret2.errorMsg"
                    //File gpx_file = new File("D:\\_temp\\gpxdata-${uuid}.gpx")
                    //gpx_file << gpx_data
                }
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
        
        printstart "importTrackerPointsAndCalcTest: crew '$test_instance.crew.name'"
        
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
    Map updateTestResult(Map params, ResultType resultType)
    {
        Test test_instance = Test.get(params.id)
        Task task_instance = test_instance.task
        Contest contest_instance = task_instance.contest
        
        if (test_instance.taskLiveTrackingTeamID && task_instance.liveTrackingResultsPublishImmediately) {
            update_test_result(test_instance, resultType)
        }    
        
        return [:]
    }
    
    //--------------------------------------------------------------------------
    Map updateTestResults(Map params)
    {
        printstart "updateTestResults"
        
        Task task_instance = Task.get(params.id)
        
        int result_num = 0
        int error_num = 0
        String error_str = ""
        for ( Test test_instance in Test.findAllByTask(task_instance,[sort:"viewpos"])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.taskLiveTrackingTeamID) {
                if (test_instance.planningTestComplete && !test_instance.planningTestLiveTrackingResultOk || test_instance.planningTestLiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Planningtask)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.flightTestComplete && !test_instance.flightTestLiveTrackingResultOk || test_instance.flightTestLiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Flight)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.observationTestComplete && !test_instance.observationTestLiveTrackingResultOk || test_instance.observationTestLiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Observation)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.landingTestComplete && !test_instance.landingTestLiveTrackingResultOk || test_instance.landingTestLiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Landing)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.landingTest1Complete && !test_instance.landingTest1LiveTrackingResultOk || test_instance.landingTest1LiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Landing1)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.landingTest2Complete && !test_instance.landingTest2LiveTrackingResultOk || test_instance.landingTest2LiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Landing2)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.landingTest3Complete && !test_instance.landingTest3LiveTrackingResultOk || test_instance.landingTest3LiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Landing3)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.landingTest4Complete && !test_instance.landingTest4LiveTrackingResultOk || test_instance.landingTest4LiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Landing4)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
                if (test_instance.specialTestComplete && !test_instance.specialTestLiveTrackingResultOk || test_instance.specialTestLiveTrackingResultError) {
                    Map ret = update_test_result(test_instance, ResultType.Special)
                    if (ret.ok) {
                        result_num++
                    }
                    if (ret.error) {
                        error_num++
                    }
                }
            }
        }
        
        printdone ""
        
        return [taskInstance:task_instance, error:error_num > 0, message:getMsg('fc.livetracking.results.updatetestresults.done',[result_num, error_num, error_str])]
    }
    
    //--------------------------------------------------------------------------
    private Map update_test_result(Test testInstance, ResultType resultType)
    {
        Map ret = [ok:false, error:false]
        
        Task task_instance = testInstance.task
        Contest contest_instance = task_instance.contest
        
        Integer tasktest_id = 0
        int test_penalties = 0
        switch (resultType) {
            case ResultType.Planningtask:
                tasktest_id = task_instance.liveTrackingResultsPlanningID
                test_penalties = testInstance.planningTestPenalties
                break
            case ResultType.Flight:
                if (task_instance.liveTrackingResultsFlightOn) {
                    tasktest_id = task_instance.liveTrackingResultsFlightID
                }
                test_penalties = testInstance.flightTestPenalties
                break
            case ResultType.Observation:
                tasktest_id = task_instance.liveTrackingResultsObservationID
                test_penalties = testInstance.observationTestPenalties
                break
            case ResultType.Landing:
                tasktest_id = task_instance.liveTrackingResultsLandingID
                test_penalties = testInstance.landingTestPenalties
                break
            case ResultType.Landing1:
                tasktest_id = task_instance.liveTrackingResultsLanding1ID
                test_penalties = testInstance.landingTest1Penalties
                break
            case ResultType.Landing2:
                tasktest_id = task_instance.liveTrackingResultsLanding2ID
                test_penalties = testInstance.landingTest2Penalties
                break
            case ResultType.Landing3:
                tasktest_id = task_instance.liveTrackingResultsLanding3ID
                test_penalties = testInstance.landingTest3Penalties
                break
            case ResultType.Landing4:
                tasktest_id = task_instance.liveTrackingResultsLanding4ID
                test_penalties = testInstance.landingTest4Penalties
                break
            case ResultType.Special:
                tasktest_id = task_instance.liveTrackingResultsSpecialID
                test_penalties = testInstance.specialTestPenalties
                break
        }
        
        if (tasktest_id) {
            printstart "updateTestResult $resultType for crew '$testInstance.crew.name': $test_penalties points."
        
            JsonBuilder json_builder_result = new JsonBuilder()
            json_builder_result {
                task_test tasktest_id
                points test_penalties
                team testInstance.taskLiveTrackingTeamID
            }
            Map ret5 = call_rest("contests/${contest_instance.liveTrackingContestID}/update_test_result/", "PUT", 200, json_builder_result.toString(), "")
            switch (resultType) {
                case ResultType.Planningtask:
                    if (ret5.ok) {
                        testInstance.planningTestLiveTrackingResultOk = true
                        testInstance.planningTestLiveTrackingResultError = false
                    } else {
                        testInstance.planningTestLiveTrackingResultOk = false
                        testInstance.planningTestLiveTrackingResultError = true
                    }
                    break
                case ResultType.Flight:
                    if (ret5.ok) {
                        testInstance.flightTestLiveTrackingResultOk = true
                        testInstance.flightTestLiveTrackingResultError = false
                    } else {
                        testInstance.flightTestLiveTrackingResultOk = false
                        testInstance.flightTestLiveTrackingResultError = true
                    }
                    break
                case ResultType.Observation:
                    if (ret5.ok) {
                        testInstance.observationTestLiveTrackingResultOk = true
                        testInstance.observationTestLiveTrackingResultError = false
                    } else {
                        testInstance.observationTestLiveTrackingResultOk = false
                        testInstance.observationTestLiveTrackingResultError = true
                    }
                    break
                case ResultType.Landing:
                    if (ret5.ok) {
                        testInstance.landingTestLiveTrackingResultOk = true
                        testInstance.landingTestLiveTrackingResultError = false
                    } else {
                        testInstance.landingTestLiveTrackingResultOk = false
                        testInstance.landingTestLiveTrackingResultError = true
                    }
                    break
                case ResultType.Landing1:
                    if (ret5.ok) {
                        testInstance.landingTest1LiveTrackingResultOk = true
                        testInstance.landingTest1LiveTrackingResultError = false
                    } else {
                        testInstance.landingTest1LiveTrackingResultOk = false
                        testInstance.landingTest1LiveTrackingResultError = true
                    }
                    break
                case ResultType.Landing2:
                    if (ret5.ok) {
                        testInstance.landingTest2LiveTrackingResultOk = true
                        testInstance.landingTest2LiveTrackingResultError = false
                    } else {
                        testInstance.landingTest2LiveTrackingResultOk = false
                        testInstance.landingTest2LiveTrackingResultError = true
                    }
                    break
                case ResultType.Landing3:
                    if (ret5.ok) {
                        testInstance.landingTest3LiveTrackingResultOk = true
                        testInstance.landingTest3LiveTrackingResultError = false
                    } else {
                        testInstance.landingTest3LiveTrackingResultOk = false
                        testInstance.landingTest3LiveTrackingResultError = true
                    }
                    break
                case ResultType.Landing4:
                    if (ret5.ok) {
                        testInstance.landingTest4LiveTrackingResultOk = true
                        testInstance.landingTest4LiveTrackingResultError = false
                    } else {
                        testInstance.landingTest4LiveTrackingResultOk = false
                        testInstance.landingTest4LiveTrackingResultError = true
                    }
                    break
                case ResultType.Special:
                    if (ret5.ok) {
                        testInstance.specialTestLiveTrackingResultOk = true
                        testInstance.specialTestLiveTrackingResultError = false
                    } else {
                        testInstance.specialTestLiveTrackingResultOk = false
                        testInstance.specialTestLiveTrackingResultError = true
                    }
                    break
            }
            testInstance.save()
            if (ret5.ok) {
                ret.ok = true
            } else {
                ret.error = true
            }
            
            printdone ""
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    List GetScorecards()
    {
        List scorecards = []
        Map ret = call_rest("scorecards/", "GET", 200, "", ALL_DATA)
        if (ret.ok && ret.data) {
            for (Map scorecard in ret.data) {
                scorecards += scorecard.shortcut_name
            }
        }
        return scorecards
    }
    
    //--------------------------------------------------------------------------
    private Map call_rest(String funcURL, String requestMethod, int successfulResponseCode, String outputData, String retDataKey, boolean showLog = true)
    {
        Map ret = [responseCode:null, data:null, ok:false, errorMsg:""]
        
        int max_output_size = 0 // 2000
        
        String url_path = "${BootStrap.global.GetLiveTrackingAPI()}/${funcURL}"
        
        if (showLog) {
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
                if (showLog) {
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
            
            if (showLog) {
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
                        if (showLog) {
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
                    if (showLog) {
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
            
            if (showLog) {
                printdone ""
            }
        } catch (Exception e) {
            ret.errorMsg += "Exception ${e.message}"
            if (showLog) {
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
