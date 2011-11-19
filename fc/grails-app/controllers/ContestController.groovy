import java.util.Date;
import java.util.Map;

class ContestController {
    
	def fcService

	def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
		fcService.printstart "Start contest"
		
		String showLanguage = fcService.GetCookie("ShowLanguage",Languages.de.toString())
		if (showLanguage) {
			if (session?.showLanguage != showLanguage) {
				fcService.println "Redirect contest/start $showLanguage"
				redirect(controller:'contest',action:'start',params:[lang:showLanguage])
			}
			session.showLanguage = showLanguage
		}
		String showLimitCrewNum = fcService.GetCookie("ShowLimitCrewNum","10")
		if (showLimitCrewNum) {
			session.showLimitCrewNum = showLimitCrewNum.toInteger() 
		}
		String lastContestID = fcService.GetCookie("LastContestID","")
		if (lastContestID) {
			Contest last_contest = Contest.findById(lastContestID.toInteger())
			if (last_contest) {
				session.lastContest = last_contest
			}
		}
        if (session?.lastContest) {
			// save return action
			session.contestReturnAction = actionName 
			session.contestReturnController = controllerName
			session.contestReturnID = params.id
			fcService.printdone "last contest"
            return [contestInstance:session.lastContest]
        }
		fcService.printdone ""
        return [:]
    }

    def show = {
        if (session?.lastContest) {
        	return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def edit = {
        if (session?.lastContest) {
			// assign return action
			if (session.contestReturnAction) {
				return [contestInstance:session.lastContest,contestReturnAction:session.contestReturnAction,contestReturnController:session.contestReturnController,contestReturnID:session.contestReturnID]
			}
        	return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def update = {
        def contest = fcService.updateContest(params)
        if (contest.saved) {
        	flash.message = contest.message
        	session.lastContest = contest.instance
			fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
			// process return action
			if (params.contestReturnAction) {
				redirect(action:params.contestReturnAction,controller:params.contestReturnController,id:params.contestReturnID)
			} else {
        		redirect(action:start,id:contest.instance.id)
			}
        } else if (contest.instance) {
        	render(view:'edit',model:[contestInstance:contest.instance])
        } else {
        	flash.message = contest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def contest = fcService.createContest(params)
        if (contest.created) {
	        return [contestInstance:contest.instance]
    	} else {
    		flash.message = contest.message
    		redirect(action:start)
    	}
    }

    def save = {
        def contest = fcService.saveContest(params) 
        if (contest.saved) {
        	session.lastContest = contest.instance
			fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
			session.showLimit = false
			session.showLimitStartPos = 0
            session.lastTaskPlanning = null
            session.lastTaskResults = null
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

	def copyquestion = {
        if (session?.lastContest) {
			Map new_params = params + [title:fcService.getContestCopyTitle(session.lastContest)]
			def contest = fcService.createContest(new_params)
			if (contest.created) {
				return [contestInstance:contest.instance]
			} else {
				flash.message = contest.message
				redirect(action:start)
			}
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
	}
	
    def copy = {
        def contest = fcService.copyContest(params,session.lastContest) 
        if (contest.saved) {
        	session.lastContest = contest.instance
			fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
			session.showLimit = false
			session.showLimitStartPos = 0
            session.lastTaskPlanning = null
            session.lastTaskResults = null
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def tasks = {
		fcService.printstart "List tasks"
        if (session?.lastContest) {
            def contestTasksList = Task.findAllByContest(session.lastContest)
			// save return action
			session.taskReturnAction = actionName 
			session.taskReturnController = controllerName
			session.taskReturnID = params.id
			session.planningtestReturnAction = actionName
			session.planningtestReturnController = controllerName
			session.planningtestReturnID = params.id
			session.flighttestReturnAction = actionName
			session.flighttestReturnController = controllerName
			session.flighttestReturnID = params.id
			fcService.printdone "last contest"
            return [contestInstance:session.lastContest,contestTasks:contestTasksList]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

	def activate = {
		session.lastContest = Contest.get(params.id)
		fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
		session.showLimit = false
		session.showLimitStartPos = 0
		session.lastTaskPlanning = null
        session.lastTaskResults = null
        redirect(action:start)
	}

	def deletequestion = {
        if (session?.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
	}
	
    def delete = {
        def contest = fcService.deleteContest(params)
        if (contest.deleted) {
        	if (Contest.count() == 1) {
        		session.lastContest = Contest.findByIdIsNotNull([sort:"id",order:"desc"])
				fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
				// session.lastContest = null
				// fcService.SetCookie(response, "LastContestID",  "")
				session.showLimit = false
				session.showLimitStartPos = 0
        		session.lastTaskPlanning = null
        		session.lastTaskResults = null
        		flash.message = contest.message
                redirect(action:start)
        	} else {
        		session.lastContest = null
				fcService.SetCookie(response, "LastContestID",  "")
				session.showLimit = false
				session.showLimitStartPos = 0
                session.lastTaskPlanning = null
                session.lastTaskResults = null
                flash.message = contest.message
                redirect(controller:'global')
        	}
        } else if (contest.notdeleted) {
        	flash.message = contest.message
            redirect(action:start,id:params.id)
        } else {
        	flash.message = contest.message
        	redirect(action:start)
        }
    }

    def change = {
		session.lastContest = null
		fcService.SetCookie(response, "LastContestID",  "")
        redirect(action:start)
    }

    def updatecontest = {
        session.lastContest = Contest.get(params.contestid)
		fcService.SetCookie(response, "LastContestID",  session.lastContest.id.toString())
		session.showLimit = false
		session.showLimitStartPos = 0
        session.lastTaskPlanning = null
        session.lastTaskResults = null
        redirect(action:start)
    }
        
	def cancel = {
		// process return action
		if (params.positionsReturnAction) {
			redirect(action:params.positionsReturnAction,controller:params.positionsReturnController,id:params.positionsReturnID)
		} else if (params.contestReturnAction) {
			redirect(action:params.contestReturnAction,controller:params.contestReturnController,id:params.contestReturnID)
		} else {
			redirect(action:start)
		}
	}
	
	def createtask = {
		redirect(controller:'task',action:'create',params:['contest.id':session.lastContest.id,'contestid':session.lastContest.id,'fromcontest':true])
	}

	def positions = {
        if (session?.lastContest) {
			// save return action
			session.crewReturnAction = actionName
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			// assign return action
			if (session.positionsReturnAction) {
				return [contestInstance:session.lastContest,,positionsReturnAction:session.positionsReturnAction,positionsReturnController:session.positionsReturnController,positionsReturnID:session.positionsReturnID]
			}
        	return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

	def calculatepositions = {
        def contest = fcService.calculatepositionsContest(session.lastContest) 
        flash.message = contest.message
		if (contest.error) {
			flash.error = true
		}
        redirect(controller:"contest",action:"positions")
	}

	def printpositions = {
        if (session?.lastContest) {
	        def contest = fcService.printresultsContest(session.lastContest,GetPrintParams()) 
	        if (contest.error) {
	        	flash.message = contest.message
	           	flash.error = true
				redirect(controller:"contest",action:"positions")
	        } else if (contest.content) {
	        	fcService.WritePDF(response,contest.content)
		    } else {
				redirect(action:start)
		    }
        } else {
            redirect(action:start)
        }
	}
	
	def positionsprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        	return [contestInstance:session.lastContest]
        } else {
            redirect(action:start)
        }
    }

	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
               ]
    }
	
	void create_test1(String testName) 
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
        Map contest = fcService.putContest(testName,200000)
        Map task1 = fcService.putTask(contest,"","11:00",3,10)

        // Crews and Aircrafts
        Map crew1 = fcService.putCrew(contest,"Becker","Deutschland","D-EBEC","C172","rot",110)
        Map crew2 = fcService.putCrew(contest,"Meier","Deutschland","D-EMEI","C172","blau",130)
        Map crew3 = fcService.putCrew(contest,"Schulze","Deutschland","","","",140)
        Map crew4 = fcService.putCrew(contest,"Schmidt","Deutschland","","","",140)
        Map crew5 = fcService.putCrew(contest,"Richter","Deutschland","","","",140)
        Map crew6 = fcService.putCrew(contest,"M\u00fcller","Deutschland","D-MUEL","","",60)

        Map aircraft3 = fcService.putAircraft(contest,"D-ESCH","C172","gelb")

        fcService.setaircraftCrew(crew3,aircraft3)
        fcService.setaircraftCrew(crew4,aircraft3)
        fcService.setaircraftCrew(crew5,['instance':crew1.instance.aircraft])
        
        // Routes
		fcService.printstart "Route 1"
        Map route1 = fcService.putRoute(contest,"","")
    	fcService.putCoordRoute(route1,CoordType.SP,    0,'SP', 'N',52,20,'E',14,00,501,1,null,null)
        fcService.putCoordRoute(route1,CoordType.SECRET,1,'CP1','N',52,20,'E',14,20,501,1,217,31)
    	fcService.putCoordRoute(route1,CoordType.TP,    1,'CP2','N',52,20,'E',14,40,501,1,null,0)
    	fcService.putCoordRoute(route1,CoordType.FP,    0,'FP', 'N',52,35,'E',13,50,501,1,220,209)
		fcService.printdone ""
    	
		fcService.printstart "Route 2"
        Map route2 = fcService.putRoute(contest,"","")
    	fcService.putCoordRoute(route2,CoordType.SP,    0,'SP', 'N',54,00,'E',16,00,502,1,null,null)
    	fcService.putCoordRoute(route2,CoordType.TP,    1,'CP1','N',53,30,'E',16,10,502,1,null,null)
    	fcService.putCoordRoute(route2,CoordType.FP,    0,'FP', 'N',52,55,'E',15,50,502,1,null,null)
		fcService.printdone ""
		
		fcService.printstart "Route 3"
        Map route3 = fcService.putRoute(contest,"Strecke-3 (LWB-2009 mit unbekannten Zeitkontrollen)","LWB2009mitUnbekZK")
    	fcService.putCoordRoute(route3,CoordType.SP,    0,'SP', 'N',51,27.00000,'E',13,51.81000,503,1,null,null)
        fcService.putCoordRoute(route3,CoordType.SECRET,1,'CP1','N',51,23.97991,'E',14,02.22366,503,2,67.1,115)
    	fcService.putCoordRoute(route3,CoordType.TP,    1,'CP2','N',51,23.13000,'E',14,05.15000,503,1,18.9,115)
        fcService.putCoordRoute(route3,CoordType.SECRET,2,'CP3','N',51,21.16505,'E',14,13.16782,503,2,49.4,111)
    	fcService.putCoordRoute(route3,CoordType.TP,    2,'CP4','N',51,18.23000,'E',14,25.10000,503,1,73.6,111)
        fcService.putCoordRoute(route3,CoordType.SECRET,3,'CP5','N',51,19.40009,'E',14,22.70287,503,2,17.4,308)
    	fcService.putCoordRoute(route3,CoordType.TP,    3,'CP6','N',51,26.59000,'E',14,08.03000,503,1,106.6,308)
        fcService.putCoordRoute(route3,CoordType.SECRET,4,'CP7','N',51,27.65617,'E',14,10.01799,503,2,15.3,49)
    	fcService.putCoordRoute(route3,CoordType.TP,    4,'CP8','N',51,31.34000,'E',14,16.81000,503,1,52.7,49)
        fcService.putCoordRoute(route3,CoordType.SECRET,5,'CP9','N',51,30.83523,'E',14,10.55785,503,2,37.1,263)
    	fcService.putCoordRoute(route3,CoordType.FP,    0,'FP', 'N',51,29.37668,'E',13,52.75530,503,1,105.9,263)
		fcService.printdone ""
		
		fcService.printstart "Route 4"
        Map route4 = fcService.putRoute(contest,"Strecke-4 (LWB-2009)","")
        fcService.putCoordRoute(route4,CoordType.SP,    0,'SP', 'N',51,27.00000,'E',13,51.81000,504,2,null,null)
        fcService.putCoordRoute(route4,CoordType.TP,    1,'CP1','N',51,23.13000,'E',14,05.15000,504,2,86,115)
        fcService.putCoordRoute(route4,CoordType.TP,    2,'CP2','N',51,18.23000,'E',14,25.10000,504,2,123,111)
        fcService.putCoordRoute(route4,CoordType.TP,    3,'CP3','N',51,26.59000,'E',14,08.03000,504,2,124,308)
        fcService.putCoordRoute(route4,CoordType.TP,    4,'CP4','N',51,31.34000,'E',14,16.81000,504,2,68,49)
        fcService.putCoordRoute(route4,CoordType.FP,    0,'FP', 'N',51,29.38331,'E',13,52.76663,504,2,143,263)
		fcService.printdone ""
		
    	// Planning Test
    	Map planningtest1 = fcService.putPlanningTest(task1,"")
    	Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route3,90,10)
    	fcService.putplanningtesttaskTask(task1,planningtesttask1)

    	// Flight Test
    	Map flighttest1 = fcService.putFlightTest(task1,"",route3)
    	Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,234,8)
        fcService.putflighttestwindTask(task1,flighttestwind1)
        
    	// Calculation
        fcService.runcalculatesequenceTask(task1)
        fcService.runcalculatetimetableTask(task1)

        // Results
        fcService.putplanningresultsTask(task1,[[crew:crew3,
			                                     givenTooLate:true,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:113,legTime:"00:04:15"],
                                                              [trueHeading:106,legTime:"00:06:06"],
                                                              [trueHeading:311,legTime:"00:05:12"],
                                                              [trueHeading:52, legTime:"00:03:20"],
                                                              [trueHeading:262,legTime:"00:06:11"]],
                                                 testComplete:true],
                                                [crew:crew2,
			                                     givenTooLate:false,
                                                 exitRoomTooLate:true,
                                                 givenValues:[[trueHeading:113,legTime:"00:04:37"],
				                                              [trueHeading:115,legTime:"00:06:36"],
				                                              [trueHeading:311,legTime:"00:05:58"],
				                                              [trueHeading:52, legTime:"00:03:16"],
				                                              [trueHeading:262,legTime:"00:06:37"]],
				                                 testComplete:true],
                                                [crew:crew1,
			                                     givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:113,legTime:"00:05:32"],
				                                              [trueHeading:110,legTime:"00:07:55"],
				                                              [trueHeading:311,legTime:"00:06:50"],
				                                              [trueHeading:52, legTime:"00:04:19"],
				                                              [trueHeading:262,legTime:"00:07:44"]],
				                                 testComplete:true],
                                                [crew:crew6,
			                                     givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:113,legTime:"00:10:58"],
			                                                  [trueHeading:110,legTime:"00:15:25"],
			                                                  [trueHeading:311,legTime:"00:11:59"],
			                                                  [trueHeading:52, legTime:"00:08:27"],
			                                                  [trueHeading:262,legTime:""]],
			                                     testComplete:true],
                                                [crew:crew5,
			                                     givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:113,legTime:""],
			                                                  [trueHeading:110,legTime:""],
			                                                  [trueHeading:311,legTime:""],
			                                                  [trueHeading:52, legTime:""],
			                                                  [trueHeading:262,legTime:""]],
			                                     testComplete:true],
                                                [crew:crew4,
			                                     givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading:113,legTime:"00:04:09"],
			                                                  [trueHeading:110,legTime:"00:06:00"],
			                                                  [trueHeading:311,legTime:"00:05:56"],
			                                                  [trueHeading:52, legTime:"00:03:20"],
			                                                  [trueHeading:262,legTime:"00:06:00"]],
			                                     testComplete:false]])
        fcService.putflightresultsTask(task1,  [[crew:crew3,
			                                     takeoffMissed:true,
                                                 badCourseStartLanding:true,
                                                 landingTooLate:true,
                                                 givenTooLate:true,
                                                 givenValues:[[cpTime:"12:25:00",altitude:1001],
                                                              [cpTime:"12:28:03",altitude:1001],
                                                              [cpTime:"12:28:55",altitude:1001,badCourseNum:2],
                                                              [cpTime:"12:31:09",altitude:1001],
                                                              [cpTime:"12:34:29",altitude:1001],
                                                              [cpTime:"12:36:13",altitude:1001,procedureTurnNotFlown:true],
                                                              [cpNotFound:true],
                                                              [cpTime:"12:42:52",altitude:401],
                                                              [cpTime:"12:45:10",altitude:1001],
                                                              [cpTime:"12:46:03",altitude:1001],
                                                              [cpTime:"12:51:12",altitude:1001]],
                                                 testComplete:true],
                                                [crew:crew2,
			                                     takeoffMissed:true,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:false,
                                                 givenTooLate:false,
                                                 givenValues:[[cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002],
                                                              [cpTime:"12:00:00",altitude:1002]],
                                                 testComplete:true],
                                                [crew:crew1,
			                                     takeoffMissed:false,
                                                 badCourseStartLanding:true,
                                                 landingTooLate:false,
                                                 givenTooLate:false,
                                                 givenValues:[[cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003],
                                                              [cpTime:"12:00:00",altitude:1003]],
                                                 testComplete:true],
                                                [crew:crew6,
			                                     takeoffMissed:false,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:true,
                                                 givenTooLate:false,
                                                 givenValues:[[cpNotFound:true,cpTime:"12:40:03"],
                                                              [cpTime:"12:46:40",altitude:1004],
                                                              [cpTime:"12:48:53",altitude:1004],
                                                              [cpTime:"12:53:55",altitude:1004],
                                                              [cpTime:"13:00:59",altitude:1004],
                                                              [cpTime:"13:04:12",altitude:1004,procedureTurnNotFlown:true],
                                                              [cpTime:"13:16:20",altitude:1004],
                                                              [cpTime:"13:18:42",altitude:1004],
                                                              [cpTime:"13:23:49",altitude:1004],
                                                              [cpTime:"13:29:05",altitude:1004],
                                                              [cpTime:""]],
                                                 testComplete:true],
                                                [crew:crew5,
			                                     takeoffMissed:false,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:false,
                                                 givenTooLate:true,
                                                 givenValues:[],
                                                 testComplete:true],
                                                [crew:crew4,
			                                     takeoffMissed:false,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:false,
                                                 givenTooLate:false,
                                                 givenValues:[],
                                                 testComplete:false]])


    	fcService.runcalculatepositionsTask(task1)
        
        //session.lastContest = contest.instance
		//session.showLimit = false
		//session.showLimitStartPos = 0
        //session.lastTaskPlanning = null
        //session.lastTaskResults = null
		
		fcService.printdone ""
        
    }

    void create_test2(String testName) 
    {
		fcService.printstart "Create test contest '$testName'"
		
        // Contest
        Map contest = fcService.putContest(testName,200000)
        Map task1 = fcService.putTask(contest,"","11:00",3,10)
    
        // Crews and Aircrafts
        (1..100).each {
            fcService.putCrew(contest,"Name-${it.toString()}","Deutschland","D-${it.toString()}","C172","rot",110)
        }
		
		fcService.printdone ""
    }

    void create_test3(String testName) 
    {
		fcService.printstart "Create test contest '$testName'"
		
        // Contest
        Map contest = fcService.putContest(testName,200000)
        Map task1 = fcService.putTask(contest,"","11:00",3,10)
    
        // Crews and Aircrafts
        (1..20).each {
            fcService.putCrew(contest,"Name-${it.toString()}","Deutschland","D-${it.toString()}","C172","rot",110)
        }
		
		fcService.printdone ""
    }

	void create_test4(String testName) 
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
        Map contest = fcService.putContest(testName,200000)
        Map task1 = fcService.putTask(contest,"20. Februar 2011","09:00",3,8)

        // Crews and Aircrafts
        Map crew1 = fcService.putCrew(contest,"Mannschaft 3","","D-EAAA","","",85)
		Map crew2 = fcService.putCrew(contest,"Mannschaft 18","","D-EAAD","","",80)
		Map crew3 = fcService.putCrew(contest,"Mannschaft 19","","D-EAAE","","",80)
		Map crew4 = fcService.putCrew(contest,"Mannschaft 11","","D-EAAB","","",70)
		Map crew5 = fcService.putCrew(contest,"Mannschaft 13","","D-EAAC","","",70)
		
        // Route
		fcService.printstart "Route"
        Map route1 = fcService.putRoute(contest,"Strecke 1","Strecke 1")
    	fcService.putCoordRoute(route1,CoordType.TO,    0,'T/O', 'N',52, 2.18, 'E',13,44.0,    0,1,  null,  null)
    	fcService.putCoordRoute(route1,CoordType.SP,    0,'SP',  'N',52, 4.897,'E',13,49.207,500,1,  null,  null)
		fcService.putCoordRoute(route1,CoordType.SECRET,1,'CP1', 'N',52, 5.121,'E',14, 6.679,500,2,  99.0,  89.0)
		fcService.putCoordRoute(route1,CoordType.TP,    1,'CP2', 'N',52, 5.223,'E',14,15.555,500,1, 150.0,  89.0)
		fcService.putCoordRoute(route1,CoordType.SECRET,2,'CP3', 'N',52, 1.367,'E',14,10.417,500,2,  46.0, 219.0)
		fcService.putCoordRoute(route1,CoordType.TP,    2,'CP4', 'N',51,51.719,'E',13,57.662,500,1, 161.5, 219.0)
		fcService.putCoordRoute(route1,CoordType.SECRET,3,'CP5', 'N',51,44.633,'E',14, 1.635,500,2,  68.5, 161.0)
		fcService.putCoordRoute(route1,CoordType.TP,    3,'CP6', 'N',51,38.847,'E',14, 4.857,500,1, 125.0, 161.0)
		fcService.putCoordRoute(route1,CoordType.SECRET,4,'CP7', 'N',51,38.983,'E',14, 8.299,500,2,  19.5,  86.0)
		fcService.putCoordRoute(route1,CoordType.TP,    4,'CP8', 'N',51,39.535,'E',14,23.4,  500,1, 106.45, 86.0)
		fcService.putCoordRoute(route1,CoordType.SECRET,5,'CP9', 'N',51,38.02, 'E',14,19.606,500,2,  25.5, 237.0)
		fcService.putCoordRoute(route1,CoordType.TP,    5,'CP10','N',51,33.399,'E',14, 8.079,500,1, 105.2, 237.0)
		fcService.putCoordRoute(route1,CoordType.FP,    0,'FP',  'N',51,30.353,'E',13,58.485,500,1,  62.4, 244.0)
		fcService.putCoordRoute(route1,CoordType.LDG,   0,'LDG', 'N',51,29.5,  'E',13,53.0,    0,1,  null,  null)
		fcService.printdone ""
		
    	// Planning Test
    	Map planningtest1 = fcService.putPlanningTest(task1,"")
    	Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
    	fcService.putplanningtesttaskTask(task1,planningtesttask1)

    	// Flight Test
    	Map flighttest1 = fcService.putFlightTest(task1,"",route1)
    	Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15)
        fcService.putflighttestwindTask(task1,flighttestwind1)
        
    	// Planning
        fcService.putsequenceTask(task1,[crew1,crew4,crew5,crew3,crew2])
        fcService.puttimetableTask(task1,[[crew:crew1,starttime:'9:06'],
			                              [crew:crew4,starttime:'9:30'],
										  [crew:crew5,starttime:'9:36'],
										  [crew:crew3,starttime:'10:51'],
										  [crew:crew2,starttime:'11:48']
										 ])

		// Results
        fcService.putplanningresultsTask(task1,[[crew:crew1,
			                                     givenTooLate:false,
                                                 exitRoomTooLate:false,
                                                 givenValues:[[trueHeading: 98,legTime:"00:14:06"],
                                                              [trueHeading:205,legTime:"00:12:41"],
                                                              [trueHeading:154,legTime:"00:12:03"],
                                                              [trueHeading: 95,legTime:"00:09:56"],
                                                              [trueHeading:224,legTime:"00:07:43"],
															  [trueHeading:232,legTime:"00:04:25"]],
                                                 testComplete:true],
											    [crew:crew4,
												 givenTooLate:false,
												 exitRoomTooLate:false,
												 givenValues:[[trueHeading: 99,legTime:"00:18:00"],
															  [trueHeading:203,legTime:"00:15:44"],
															  [trueHeading:153,legTime:"00:15:31"],
															  [trueHeading: 97,legTime:"00:12:42"],
															  [trueHeading:221,legTime:"00:09:21"],
															  [trueHeading:229,legTime:"00:05:20"]],
												 testComplete:true],
											    [crew:crew5,
												 givenTooLate:false,
												 exitRoomTooLate:false,
												 givenValues:[[trueHeading:100,legTime:"00:18:03"],
															  [trueHeading:203,legTime:"00:15:37"],
															  [trueHeading:153,legTime:"00:15:35"],
															  [trueHeading: 98,legTime:"00:12:45"],
															  [trueHeading:221,legTime:"00:09:19"],
															  [trueHeading:229,legTime:"00:05:21"]],
												 testComplete:true],
											    [crew:crew3,
												 givenTooLate:false,
												 exitRoomTooLate:false,
												 givenValues:[[trueHeading: 98,legTime:"00:15:11"],
															  [trueHeading:203,legTime:"00:13:35"],
															  [trueHeading:154,legTime:"00:13:04"],
															  [trueHeading:102,legTime:"00:10:57"],
															  [trueHeading:222,legTime:"00:08:19"],
															  [trueHeading:230,legTime:"00:04:42"]],
												 testComplete:true],
											    [crew:crew2,
												 givenTooLate:false,
												 exitRoomTooLate:false,
												 givenValues:[[trueHeading: 98,legTime:"00:15:11"],
															  [trueHeading:204,legTime:"00:13:38"],
															  [trueHeading:153,legTime:"00:13:03"],
															  [trueHeading: 96,legTime:"00:10:43"],
															  [trueHeading:223,legTime:"00:08:10"],
															  [trueHeading:230,legTime:"00:04:44"]],
												 testComplete:true],
											   ])
		  
		fcService.importflightresultsTask(task1,[[crew:crew1,
			                                      startNum:3,
												  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
                                                  testComplete:true],
			                                     [crew:crew4,
											      startNum:11,
												  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  correctionValues:[[mark:"CP7",cpFound:true,cpTime:"11:33:25"]
                                                                   ],
                                                  testComplete:true],
			                                     [crew:crew5,
												  startNum:13,
												  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
                                                  testComplete:true],
			                                     [crew:crew3,
												  startNum:19,
												  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
												  correctionValues:[[mark:"CP10",cpFound:true,cpTime:"13:06:07"]
                                                                   ],
                                                  testComplete:true],
			                                     [crew:crew2,
												  startNum:18,
												  takeoffMissed:false,
                                                  badCourseStartLanding:false,
                                                  landingTooLate:false,
                                                  givenTooLate:false,
                                                  testComplete:true],
			                                    ])
		/*                       
        fcService.putflightresultsTask(task1,  [[crew:crew1,
                                                 takeoffMissed:false,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:false,
                                                 givenTooLate:false,
                                                 givenValues:[[cpTime:"12:25:00",altitude:1001],
                                                              [cpTime:"12:28:03",altitude:1001],
                                                              [cpTime:"12:28:55",altitude:1001],
                                                              [cpTime:"12:31:09",altitude:1001],
                                                              [cpTime:"12:34:29",altitude:1001],
                                                              [cpTime:"12:36:13",altitude:1001],
                                                              [cpNotFound:true],
                                                              [cpTime:"12:42:52",altitude:401],
                                                              [cpTime:"12:45:10",altitude:1001],
                                                              [cpTime:"12:46:03",altitude:1001],
                                                              [cpTime:"12:51:12",altitude:1001]],
                                                 testComplete:true],
											   ]
									  )
        */
		fcService.putobservationresultsTask(task1, [
			                                        [crew:crew1,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
												    [crew:crew2,routePhotos:0,turnPointPhotos:0,groundTargets:10,testComplete:true],
												    [crew:crew3,routePhotos:120,turnPointPhotos:0,groundTargets:10,testComplete:true],
												    [crew:crew4,routePhotos:0,turnPointPhotos:0,groundTargets:0,testComplete:true],
												    [crew:crew5,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
			                                       ])
		fcService.putlandingresultsTask(task1, [
			                                    [crew:crew1,landingPenalties:140,testComplete:true],
											    [crew:crew2,landingPenalties:110,testComplete:true],
											    [crew:crew3,landingPenalties: 80,testComplete:true],
											    [crew:crew4,landingPenalties:130,testComplete:true],
											    [crew:crew5,landingPenalties: 70,testComplete:true],
											   ])
		fcService.runcalculatepositionsTask(task1)
		fcService.runcalculatepositionsContest(contest)

		fcService.printdone ""
	}
	
	void run_test4(String contestName)
	{
		if (session?.lastContest && session.lastContest.title == contestName) {
			fcService.printstart "runtest '$session.lastContest.title'"
			Map ret = fcService.testData(
			   [[name:"Route",count:1,table:Route.findAllByContest(session.lastContest),
				 data:[[title:"Strecke 1",mark:"Strecke 1"]
					  ]],
				[name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(session.lastContest)),
				 data:[[type:CoordType.TO,    mark:"T/O", 
					    latGrad:52,latMinute:2.18,latDirection:'N',lonGrad:13,lonMinute:44.0,lonDirection:'E',altitude:0,gatewidth:1,
						coordTrueTrack:0,coordMeasureDistance:0,
						measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0],
					   [type:CoordType.SP,    mark:"SP",  
						latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:0,coordMeasureDistance:0,
						measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0],
					   [type:CoordType.SECRET,mark:"CP1", 
						latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth:2,
						coordTrueTrack:88.8048175589345163416510331444442272186279296875,coordMeasureDistance:99.44407809500728955498516370425932109355926513671875,
						measureTrueTrack:89.0,measureDistance:99.0,legMeasureDistance:99.0,legDistance:10.6911447084,secretLegRatio:0.6598765432],
					   [type:CoordType.TP,    mark:"CP2", 
						latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:88.846516611713269639949430711567401885986328125,coordMeasureDistance:149.957851348257154171506044804118573665618896484375,
						measureTrueTrack:89.0,measureDistance:150.0,legMeasureDistance:51.0,legDistance:5.5075593952,secretLegRatio:0],
					   [type:CoordType.SECRET,mark:"CP3", 
						latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth:2,
						coordTrueTrack:219.32923607314438640969456173479557037353515625,coordMeasureDistance:46.161317560899687375552957746549509465694427490234375,
						measureTrueTrack:219.0,measureDistance:46.0,legMeasureDistance:46.0,legDistance:4.9676025918,secretLegRatio:0.2849770642],
					   [type:CoordType.TP,    mark:"CP4", 
						latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:219.222179062052447306996327824890613555908203125,coordMeasureDistance:161.4135491992470104349877146887592971324920654296875,
						measureTrueTrack:219.0,measureDistance:161.5,legMeasureDistance:115.5,legDistance:12.4730021598,secretLegRatio:0],
					   [type:CoordType.SECRET,mark:"CP5",
						latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth:2,
						coordTrueTrack:160.87813197872839054980431683361530303955078125,coordMeasureDistance:69.44823921255598381918616723851300776004791259765625,
						measureTrueTrack:161.0,measureDistance:68.5,legMeasureDistance:68.5,legDistance:7.3974082073,secretLegRatio:0.5481481481],
					   [type:CoordType.TP,    mark:"CP6", 
						latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:160.913569732346928731203661300241947174072265625,coordMeasureDistance:126.12845752345615014888835503370501101016998291015625,
						measureTrueTrack:161.0,measureDistance:125.0,legMeasureDistance:56.5,legDistance:6.1015118790,secretLegRatio:0],
					   [type:CoordType.SECRET,mark:"CP7", 
						latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth:2,
						coordTrueTrack:86.3563657891783549302999745123088359832763671875,coordMeasureDistance:19.816663076211590226449743568082340061664581298828125,
						measureTrueTrack:86.0,measureDistance:19.5,legMeasureDistance:19.5,legDistance:2.1058315335,secretLegRatio:0.1834782609],
					   [type:CoordType.TP,    mark:"CP8", 
						latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:86.5776194402431116259322152473032474517822265625,coordMeasureDistance:106.72152965656120418458385756821371614933013916015625,
						measureTrueTrack:86.0,measureDistance:106.45,legMeasureDistance:86.95,legDistance:9.3898488121,secretLegRatio:0],
					   [type:CoordType.SECRET,mark:"CP9", 
						latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth:2,
						coordTrueTrack:237.237824968379385381922475062310695648193359375,coordMeasureDistance:25.92408331375019731268594114226289093494415283203125,
						measureTrueTrack:237.0,measureDistance:25.5,legMeasureDistance:25.5,legDistance:2.7537796976,secretLegRatio:0.2420774648],
					   [type:CoordType.TP,    mark:"CP10",
						latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:237.182909476209061949703027494251728057861328125,coordMeasureDistance:104.84071469014971331290553280268795788288116455078125,
						measureTrueTrack:237.0,measureDistance:105.2,legMeasureDistance:79.7,legDistance:8.6069114471,secretLegRatio:0],
					   [type:CoordType.FP,    mark:"FP",  
						latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth:1,
						coordTrueTrack:242.9619395538157959890668280422687530517578125,coordMeasureDistance:62.04808550333981681745854075415991246700286865234375,
						measureTrueTrack:244.0,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071,secretLegRatio:0],
					   [type:CoordType.LDG,   mark:"LDG", 
						latGrad:51,latMinute:29.5,latDirection:'N',lonGrad:13,lonMinute:53.0,lonDirection:'E',altitude:0,gatewidth:1,
						coordTrueTrack:255.973961329648545870441012084484100341796875,coordMeasureDistance:32.590723202697524190085687223472632467746734619140625,
						measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0],
					  ]],
				[name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(session.lastContest)),
				 data:[[coordTrueTrack:49.68012281908000460362018202431499958038330078125,coordDistance:4.19902918471270947264883943716995418071746826171875,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null],
					   [coordTrueTrack:88.8048175589345163416510331444442272186279296875,coordDistance:10.7391013061562947683569291257299482822418212890625,measureDistance:99.0,legMeasureDistance:99.0,legDistance:10.6911447084,measureTrueTrack:89.0],
					   [coordTrueTrack:88.9286028152201168950341525487601757049560546875,coordDistance:5.45503590418789219285145009052939713001251220703125,measureDistance:150.0,legMeasureDistance:51.0,legDistance:5.5075593952,measureTrueTrack:89.0],
					   [coordTrueTrack:219.32923607314438640969456173479557037353515625,coordDistance:4.98502349469759042932537340675480663776397705078125,measureDistance:46.0,legMeasureDistance:46.0,legDistance:4.9676025918,measureTrueTrack:219.0],
					   [coordTrueTrack:219.179125976950359699912951327860355377197265625,coordDistance:12.4462266624492041700023037265054881572723388671875,measureDistance:161.5,legMeasureDistance:115.5,legDistance:12.4730021598,measureTrueTrack:219.0],
					   [coordTrueTrack:160.87813197872839054980431683361530303955078125,coordDistance:7.4998098501680328098473182762973010540008544921875,measureDistance:68.5,legMeasureDistance:68.5,legDistance:7.3974082073,measureTrueTrack:161.0],
					   [coordTrueTrack:160.957165764680127040264778770506381988525390625,coordDistance:6.12097062242749956340048811398446559906005859375,measureDistance:125.0,legMeasureDistance:56.5,legDistance:6.1015118790,measureTrueTrack:161.0],
					   [coordTrueTrack:86.3563657891783549302999745123088359832763671875,coordDistance:2.14002840995805510004856841987930238246917724609375,measureDistance:19.5,legMeasureDistance:19.5,legDistance:2.1058315335,measureTrueTrack:86.0],
					   [coordTrueTrack:86.6280647395592069415215519256889820098876953125,coordDistance:9.384976870669046178363714716397225856781005859375,measureDistance:106.45,legMeasureDistance:86.95,legDistance:9.3898488121,measureTrueTrack:86.0],
					   [coordTrueTrack:237.237824968379385381922475062310695648193359375,coordDistance:2.7995770317224835110891945078037679195404052734375,measureDistance:25.5,legMeasureDistance:25.5,legDistance:2.7537796976,measureTrueTrack:237.0],
					   [coordTrueTrack:237.16483865501277250587008893489837646484375,coordDistance:8.522308941417431782383573590777814388275146484375,measureDistance:105.2,legMeasureDistance:79.7,legDistance:8.6069114471,measureTrueTrack:237.0],
					   [coordTrueTrack:242.9619395538157959890668280422687530517578125,coordDistance:6.7006571817861573236996264313347637653350830078125,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071,measureTrueTrack:244.0],
					   [coordTrueTrack:255.973961329648545870441012084484100341796875,coordDistance:3.51951654456776719115396190318278968334197998046875,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null],
					  ]],
				[name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(session.lastContest)),
				 data:[[coordTrueTrack:88.846516611713269639949430711567401885986328125,coordDistance:16.19415241341869915459028561599552631378173828125,
					    measureTrueTrack:89.0,measureDistance:150.0,legMeasureDistance:150.0,legDistance:16.1987041037],
					   [coordTrueTrack:219.222179062052447306996327824890613555908203125,coordDistance:17.431268812013716029696297482587397098541259765625,
						measureTrueTrack:219.0,measureDistance:161.5,legMeasureDistance:161.5,legDistance:17.4406047516],
					   [coordTrueTrack:160.913569732346928731203661300241947174072265625,coordDistance:13.6207837498332775538756322930566966533660888671875,
						measureTrueTrack:161.0,measureDistance:125.0,legMeasureDistance:125.0,legDistance:13.4989200864],
					   [coordTrueTrack:86.5776194402431116259322152473032474517822265625,coordDistance:11.5250032026523978601062481175176799297332763671875,
						measureTrueTrack:86.0,measureDistance:106.45,legMeasureDistance:106.45,legDistance:11.4956803456],
					   [coordTrueTrack:237.182909476209061949703027494251728057861328125,coordDistance:11.3218914352213513296874225488863885402679443359375,
						measureTrueTrack:237.0,measureDistance:105.2,legMeasureDistance:105.2,legDistance:11.3606911447],
					   [coordTrueTrack:242.9619395538157959890668280422687530517578125,coordDistance:6.7006571817861573236996264313347637653350830078125,
						measureTrueTrack:244.0,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071],
					  ]],
                [name:"Crew",count:5,table:Crew.findAllByContest(session.lastContest),
				 data:[[name:"Mannschaft 3",mark:"Mannschaft 3 (3)",country:"",tas:85,contestPenalties:215,contestPosition:4,aircraft:[registration:"D-EAAA",type:"",colour:""]],
					   [name:"Mannschaft 18",mark:"Mannschaft 18 (18)",country:"",tas:80,contestPenalties:133,contestPosition:1,aircraft:[registration:"D-EAAD",type:"",colour:""]],
					   [name:"Mannschaft 19",mark:"Mannschaft 19 (19)",country:"",tas:80,contestPenalties:368,contestPosition:5,aircraft:[registration:"D-EAAE",type:"",colour:""]],
					   [name:"Mannschaft 11",mark:"Mannschaft 11 (11)",country:"",tas:70,contestPenalties:192,contestPosition:3,aircraft:[registration:"D-EAAB",type:"",colour:""]],
					   [name:"Mannschaft 13",mark:"Mannschaft 13 (13)",country:"",tas:70,contestPenalties:135,contestPosition:2,aircraft:[registration:"D-EAAC",type:"",colour:""]],
					  ]],
			    [name:"Aircraft",count:5,table:Aircraft.findAllByContest(session.lastContest),
				 data:[[registration:"D-EAAA",type:"",colour:"",user1:[name:"Mannschaft 3"],user2:null],
					   [registration:"D-EAAD",type:"",colour:"",user1:[name:"Mannschaft 18"],user2:null],
					   [registration:"D-EAAE",type:"",colour:"",user1:[name:"Mannschaft 19"],user2:null],
					   [registration:"D-EAAB",type:"",colour:"",user1:[name:"Mannschaft 11"],user2:null],
					   [registration:"D-EAAC",type:"",colour:"",user1:[name:"Mannschaft 13"],user2:null],
					  ]],
				[name:"Task",count:1,table:Task.findAllByContest(session.lastContest),
				 data:[[title:"20. Februar 2011",firstTime:"09:00",takeoffIntervalNormal:3,takeoffIntervalFasterAircraft:120,planningTestDuration:60,
					    preparationDuration:15,risingDuration:8,maxLandingDuration:10,parkingDuration:15,minNextFlightDuration:30,
						procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true]
					  ]],
				[name:"TestLegPlanning 'Mannschaft 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 3"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:97.88007634617225249940020148642361164093017578125,planGroundSpeed:68.8869647354760901105363239719618602129033849354002736,planLegTime:0.2351678588570061332385423370565033803586269885749822,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.235,
						resultEntered:true,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:205.3931519487941788071339033194817602634429931640625,planGroundSpeed:82.2652474537755103844591045534164004171095768821633133,planLegTime:0.2119971742599991713403080117369670194409925075849946,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:205,resultGroundSpeed:0,resultLegTime:0.2113888889,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:154.03947799517919126088827397325076162815093994140625,planGroundSpeed:67.230194344041439386176038945770208163830299352093756387,planLegTime:0.200802632384424970154470577556611169405193123916384241,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:154,resultGroundSpeed:0,resultLegTime:0.2008333333,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:95.4071472126693453930101895821280777454376220703125,planGroundSpeed:69.47010531530361913743256499630813077381319581961784077,planLegTime:0.16553883066399579479350904675014946842846206630286349,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:95,resultGroundSpeed:0,resultLegTime:0.1655555556,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.9963642250049300486125503084622323513031005859375,planGroundSpeed:88.66767609777776466207175825386822573194719180016599861,planLegTime:0.12811884217505402984770293605095139681610122081441608,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:224,resultGroundSpeed:0,resultLegTime:0.1286111111,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:231.5872962200848608205205891863442957401275634765625,planGroundSpeed:91.14782748355877166129495705393359950041028724500288329,planLegTime:0.07394581073493781252201298096934323630214557404724495,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:232,resultGroundSpeed:0,resultLegTime:0.0736111111,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegPlanning 'Mannschaft 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 18"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.440004505002871582064472022466361522674560546875,planGroundSpeed:63.82243921688676168623171009182070945012551755317653673,planLegTime:0.25382922054965342727885044311575891310328265893297290,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
						resultEntered:true,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.524740955810759857058656052686274051666259765625,planGroundSpeed:77.11140523034704409517511667918804085666952081507392252,planLegTime:0.22616628432465035401479005757504435687442414950432324,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:204,resultGroundSpeed:0,resultLegTime:0.2272222222,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.602084464827679965992501820437610149383544921875,planGroundSpeed:62.190721778786559020339407839885348246734163407416042206,planLegTime:0.217074181065460641240544440032193588437970511066669902,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2175,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550174744532569093280471861362457275390625,planGroundSpeed:64.3975926710640598136318931482756061706538502879034229,planLegTime:0.1785781039788359252647439531852431804278743005683503,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:96,resultGroundSpeed:0,resultLegTime:0.1786111111,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772857658772267086533247493207454681396484375,planGroundSpeed:83.52749901132380313757886004883250899735602699951202109,planLegTime:0.13600311435710444900214867121467010008641357360534690,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:223,resultGroundSpeed:0,resultLegTime:0.1361111111,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.79791112741248326756249298341572284698486328125,planGroundSpeed:86.02037902772706327033403627718632062395421489524622980,planLegTime:0.07835352594560745691032566298681745088343372573440887,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0788888889,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegPlanning 'Mannschaft 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 19"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.440004505002871582064472022466361522674560546875,planGroundSpeed:63.82243921688676168623171009182070945012551755317653673,planLegTime:0.25382922054965342727885044311575891310328265893297290,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
						resultEntered:true,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.524740955810759857058656052686274051666259765625,planGroundSpeed:77.11140523034704409517511667918804085666952081507392252,planLegTime:0.22616628432465035401479005757504435687442414950432324,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2263888889,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.602084464827679965992501820437610149383544921875,planGroundSpeed:62.190721778786559020339407839885348246734163407416042206,planLegTime:0.217074181065460641240544440032193588437970511066669902,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:154,resultGroundSpeed:0,resultLegTime:0.2177777778,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550174744532569093280471861362457275390625,planGroundSpeed:64.3975926710640598136318931482756061706538502879034229,planLegTime:0.1785781039788359252647439531852431804278743005683503,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:102,resultGroundSpeed:0,resultLegTime:0.1825,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:8,penaltyLegTime:9
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772857658772267086533247493207454681396484375,planGroundSpeed:83.52749901132380313757886004883250899735602699951202109,planLegTime:0.13600311435710444900214867121467010008641357360534690,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:222,resultGroundSpeed:0,resultLegTime:0.1386111111,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:4
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.79791112741248326756249298341572284698486328125,planGroundSpeed:86.02037902772706327033403627718632062395421489524622980,planLegTime:0.07835352594560745691032566298681745088343372573440887,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0783333333,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegPlanning 'Mannschaft 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 11"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076070438994292999268509447574615478515625,planGroundSpeed:53.66505955633031012517014955119849856513816778208080337,planLegTime:0.30187239395486805395780365835150241677269161539778934,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:99,resultGroundSpeed:0,resultLegTime:0.3,
						resultEntered:true,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:2
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.401052100558285218312448705546557903289794921875,planGroundSpeed:66.7338992915650943900299701588384298043146551586548864,planLegTime:0.2613364449723432876907710141466682836869737713313894,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2622222222,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.537982785457767676007279078476130962371826171875,planGroundSpeed:52.094608263631007499923887653701165628241532949554374838,planLegTime:0.259143900875146854446522125708905557237691173787622544,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2586111111,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.44772636330056769793372950516641139984130859375,planGroundSpeed:54.22063725916762843762574891839119611644133955688747707,planLegTime:0.21209636369693495892387294932893863447361831390044327,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:97,resultGroundSpeed:0,resultLegTime:0.2116666667,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012429591030695519293658435344696044921875,planGroundSpeed:73.18384687007705099362186592712926092801086914127863814,planLegTime:0.15522551062623636198820641444065160580366423102075607,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1558333333,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.869822254899698776853256276808679103851318359375,planGroundSpeed:75.70820309006225448366289557781292387342108860864810641,planLegTime:0.08902601996750756234458080551652546202733183069250427,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0888888889,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegPlanning 'Mannschaft 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 13"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076070438994292999268509447574615478515625,planGroundSpeed:53.66505955633031012517014955119849856513816778208080337,planLegTime:0.30187239395486805395780365835150241677269161539778934,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:100,resultGroundSpeed:0,resultLegTime:0.3008333333,
						resultEntered:true,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.401052100558285218312448705546557903289794921875,planGroundSpeed:66.7338992915650943900299701588384298043146551586548864,planLegTime:0.2613364449723432876907710141466682836869737713313894,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2602777778,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.537982785457767676007279078476130962371826171875,planGroundSpeed:52.094608263631007499923887653701165628241532949554374838,planLegTime:0.259143900875146854446522125708905557237691173787622544,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2597222222,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.44772636330056769793372950516641139984130859375,planGroundSpeed:54.22063725916762843762574891839119611644133955688747707,planLegTime:0.21209636369693495892387294932893863447361831390044327,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2125,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012429591030695519293658435344696044921875,planGroundSpeed:73.18384687007705099362186592712926092801086914127863814,planLegTime:0.15522551062623636198820641444065160580366423102075607,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
					    resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1552777778,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.869822254899698776853256276808679103851318359375,planGroundSpeed:75.70820309006225448366289557781292387342108860864810641,planLegTime:0.08902601996750756234458080551652546202733183069250427,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
					    resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0891666667,
					    resultEntered:true,resultLegTimeInput:"00:00:00",
					    penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegFlight 'Mannschaft 3'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 3"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:83.78524319426659960896586198941804468631744384765625,planGroundSpeed:97.50569644216899803698227620631620348934197087240299968,planLegTime:0.16614413917455870938522852997168631570219624875702492,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:229.037805296122773057732047163881361484527587890625,planGroundSpeed:81.35238470128115009595874356066192973289597399745259474,planLegTime:0.21437601447134163909135889978239178000184289702106715,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:167.64833495048416889261488904594443738460540771484375,planGroundSpeed:95.74905701223896182863182662048299896361419109183262412,planLegTime:0.14099355566785775502831335882385968138430994778460819,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:80.336772549608053139991170610301196575164794921875,planGroundSpeed:97.0206878227871711439728570316287748689661703352950197,planLegTime:0.1185314210615089478095112695128736419484621581975387,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:246.04652586315273055106445099227130413055419921875,planGroundSpeed:77.13282627752161654091567948108224064402279094139152696,planLegTime:0.14727840983198330658664707998498207159486075144981413,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.41260988799166398166562430560588836669921875,planGroundSpeed:75.69751991686760258415063596892203864677334770273753585,planLegTime:0.08903858418878175884925116083257019101380758465444425,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegFlight 'Mannschaft 18'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 18"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:83.4583310658219712507843723869882524013519287109375,planGroundSpeed:92.48360793820438405893389036363630633816692071138812135,planLegTime:0.17516617659233733370961308267377437141436526224880241,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:229.672301189808120369661992299370467662811279296875,planGroundSpeed:76.26967818745944406967599762246241828148698135276692812,planLegTime:0.22866229954629010558244038614245008806133545755814406,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:168.06591192921958377581859167548827826976776123046875,planGroundSpeed:90.71306761052560610701052773226184386863737601114197398,planLegTime:0.14882089599220619751474103910126857881978745428233380,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.981552170102798271500432747416198253631591796875,planGroundSpeed:91.9946186465608117609779447356230329969152925305316369,planLegTime:0.1250073120492241279310031867735954489371300712908364,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:246.61714138769740856105272541753947734832763671875,planGroundSpeed:72.06583051432667747562791131021794029837527257328601704,planLegTime:0.15763365132858121322989307333620406295783375433217255,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.9425792987483436746742881950922310352325439453125,planGroundSpeed:70.63967605652988895040743787955291076821827292318915591,planLegTime:0.09541380108547310466573970042330969196306459159216825,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegFlight 'Mannschaft 19'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 19"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:83.4583310658219712507843723869882524013519287109375,planGroundSpeed:92.48360793820438405893389036363630633816692071138812135,planLegTime:0.17516617659233733370961308267377437141436526224880241,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:229.672301189808120369661992299370467662811279296875,planGroundSpeed:76.26967818745944406967599762246241828148698135276692812,planLegTime:0.22866229954629010558244038614245008806133545755814406,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:168.06591192921958377581859167548827826976776123046875,planGroundSpeed:90.71306761052560610701052773226184386863737601114197398,planLegTime:0.14882089599220619751474103910126857881978745428233380,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.981552170102798271500432747416198253631591796875,planGroundSpeed:91.9946186465608117609779447356230329969152925305316369,planLegTime:0.1250073120492241279310031867735954489371300712908364,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:246.61714138769740856105272541753947734832763671875,planGroundSpeed:72.06583051432667747562791131021794029837527257328601704,planLegTime:0.15763365132858121322989307333620406295783375433217255,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.9425792987483436746742881950922310352325439453125,planGroundSpeed:70.63967605652988895040743787955291076821827292318915591,planLegTime:0.09541380108547310466573970042330969196306459159216825,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegFlight 'Mannschaft 11'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 11"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:82.66362596354392433539715057122521102428436279296875,planGroundSpeed:82.42988585929108532776277971649488737740977194625457777,planLegTime:0.19653066155706700950079772714578272593279705910498963,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:231.2189175362467832286483826464973390102386474609375,planGroundSpeed:66.06770772888904686600295352538320904450628978474915197,planLegTime:0.26397162243868968724752435699518097196097616174632743,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:169.0816457025025574267829142627306282520294189453125,planGroundSpeed:80.6254557574695548063154755324314004656560570429166136,planLegTime:0.1674409139541427139951389027104785483841453546790642,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.1178787046691951445609447546303272247314453125,planGroundSpeed:81.93119828403365769121062040131782599368396067831813229,planLegTime:0.14036167224275862331751084488424425210312841188239581,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:248.0070611206381787638974856236018240451812744140625,planGroundSpeed:61.90239875500874727297846931823328568346272466449040346,planLegTime:0.18351469778997572784516676169963383744626343978613464,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:254.2329616615647172039871293236501514911651611328125,planGroundSpeed:60.49865635603555263587162025332108527160194821684401692,planLegTime:0.11140743292437757709899258256663516565797150203198460,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"TestLegFlight 'Mannschaft 13'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 13"))),
				 data:[[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:82.66362596354392433539715057122521102428436279296875,planGroundSpeed:82.42988585929108532776277971649488737740977194625457777,planLegTime:0.19653066155706700950079772714578272593279705910498963,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:231.2189175362467832286483826464973390102386474609375,planGroundSpeed:66.06770772888904686600295352538320904450628978474915197,planLegTime:0.26397162243868968724752435699518097196097616174632743,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:169.0816457025025574267829142627306282520294189453125,planGroundSpeed:80.6254557574695548063154755324314004656560570429166136,planLegTime:0.1674409139541427139951389027104785483841453546790642,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:79.1178787046691951445609447546303272247314453125,planGroundSpeed:81.93119828403365769121062040131782599368396067831813229,planLegTime:0.14036167224275862331751084488424425210312841188239581,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:248.0070611206381787638974856236018240451812744140625,planGroundSpeed:61.90239875500874727297846931823328568346272466449040346,planLegTime:0.18351469778997572784516676169963383744626343978613464,
					    planProcedureTurn:true,planProcedureTurnDuration:1,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
				       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:254.2329616615647172039871293236501514911651611328125,planGroundSpeed:60.49865635603555263587162025332108527160194821684401692,planLegTime:0.11140743292437757709899258256663516565797150203198460,
					    planProcedureTurn:false,planProcedureTurnDuration:0,
						resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
						resultEntered:false,resultLegTimeInput:"00:00:00",
						penaltyTrueHeading:0,penaltyLegTime:0
					   ],
					  ]],
				[name:"CoordResult 'Mannschaft 3'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 3"))),
				 data:[[type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:29:00"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 04,84870'",resultLongitude:"E 013\u00b0 49,21010'",resultAltitude:1375,
						resultCpTime:Date.parse("HH:mm:ss","10:29:03"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:1
					   ],
					   [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:35:35"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,29710'",resultLongitude:"E 014\u00b0 06,67800'",resultAltitude:1409,
						resultCpTime:Date.parse("HH:mm:ss","10:35:46"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:9
					   ],
					   [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:38:58"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,24970'",resultLongitude:"E 014\u00b0 15,53960'",resultAltitude:1609,
						resultCpTime:Date.parse("HH:mm:ss","10:38:56"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:43:38"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 01,29270'",resultLongitude:"E 014\u00b0 10,54200'",resultAltitude:1399,
						resultCpTime:Date.parse("HH:mm:ss","10:43:39"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:52:50"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 51,69550'",resultLongitude:"E 013\u00b0 57,68880'",resultAltitude:1629,
						resultCpTime:Date.parse("HH:mm:ss","10:52:55"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:3
					   ],
					   [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:57:28"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 44,68110'",resultLongitude:"E 014\u00b0 01,81410'",resultAltitude:1496,
						resultCpTime:Date.parse("HH:mm:ss","10:57:41"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:11
					   ],
					   [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:01:18"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,88340'",resultLongitude:"E 014\u00b0 04,96680'",resultAltitude:1569,
						resultCpTime:Date.parse("HH:mm:ss","11:01:24"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:4
					   ],
					   [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:02:36"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 39,11140'",resultLongitude:"E 014\u00b0 08,27210'",resultAltitude:1707,
						resultCpTime:Date.parse("HH:mm:ss","11:02:55"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:17
					   ],
					   [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:08:25"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 39,52840'",resultLongitude:"E 014\u00b0 23,41510'",resultAltitude:1523,
						resultCpTime:Date.parse("HH:mm:ss","11:08:23"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:11:33"),planProcedureTurn:true,
						resultLatitude:"N 051\u00b0 37,97560'",resultLongitude:"E 014\u00b0 19,65030'",resultAltitude:1387,
						resultCpTime:Date.parse("HH:mm:ss","11:11:21"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:10
					   ],
					   [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:18:15"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 33,44220'",resultLongitude:"E 014\u00b0 08,05000'",resultAltitude:1615,
						resultCpTime:Date.parse("HH:mm:ss","11:18:17"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:23:36"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 30,34350'",resultLongitude:"E 013\u00b0 58,49930'",resultAltitude:1358,
						resultCpTime:Date.parse("HH:mm:ss","11:23:37"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					  ]],	
				[name:"CoordResult 'Mannschaft 18'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 18"))),
				 data:[[type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:11:00"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 04,83500'",resultLongitude:"E 013\u00b0 49,22100'",resultAltitude:1170,
						resultCpTime:Date.parse("HH:mm:ss","13:11:03"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:1
					   ],
					   [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:17:56"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,17800'",resultLongitude:"E 014\u00b0 06,68980'",resultAltitude:1236,
						resultCpTime:Date.parse("HH:mm:ss","13:17:55"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:21:31"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,13820'",resultLongitude:"E 014\u00b0 15,54840'",resultAltitude:1290,
						resultCpTime:Date.parse("HH:mm:ss","13:21:32"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:26:26"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 01,27670'",resultLongitude:"E 014\u00b0 10,60120'",resultAltitude:1198,
						resultCpTime:Date.parse("HH:mm:ss","13:26:27"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:36:14"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 51,74450'",resultLongitude:"E 013\u00b0 57,59730'",resultAltitude:965,
						resultCpTime:Date.parse("HH:mm:ss","13:36:19"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:3
					   ],
					   [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:41:08"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 44,66360'",resultLongitude:"E 014\u00b0 01,74150'",resultAltitude:1225,
						resultCpTime:Date.parse("HH:mm:ss","13:41:07"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:45:10"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,83570'",resultLongitude:"E 014\u00b0 04,76570'",resultAltitude:1395,
						resultCpTime:Date.parse("HH:mm:ss","13:45:10"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:46:33"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,90780'",resultLongitude:"E 014\u00b0 08,31320'",resultAltitude:1405,
						resultCpTime:Date.parse("HH:mm:ss","13:46:41"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:6
					   ],
					   [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:52:40"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 39,52900'",resultLongitude:"E 014\u00b0 23,38740'",resultAltitude:2166,
						resultCpTime:Date.parse("HH:mm:ss","13:52:44"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:2
					   ],
					   [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:55:57"),planProcedureTurn:true,
						resultLatitude:"N 051\u00b0 38,01820'",resultLongitude:"E 014\u00b0 19,59790'",resultAltitude:1822,
						resultCpTime:Date.parse("HH:mm:ss","13:55:56"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","14:03:07"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 33,42810'",resultLongitude:"E 014\u00b0 08,03690'",resultAltitude:1384,
						resultCpTime:Date.parse("HH:mm:ss","14:03:09"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","14:08:50"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 30,43140'",resultLongitude:"E 013\u00b0 58,42820'",resultAltitude:1502,
						resultCpTime:Date.parse("HH:mm:ss","14:08:53"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:1
					   ],
					  ]],	
				[name:"CoordResult 'Mannschaft 19'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 19"))),
				 data:[[type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:14:00"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,00160'",resultLongitude:"E 013\u00b0 49,20850'",resultAltitude:1001,
						resultCpTime:Date.parse("HH:mm:ss","12:13:37"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:21
					   ],
					   [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:20:56"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,29600'",resultLongitude:"E 014\u00b0 06,67890'",resultAltitude:1490,
						resultCpTime:Date.parse("HH:mm:ss","12:20:40"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:14
					   ],
					   [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:24:31"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,37350'",resultLongitude:"E 014\u00b0 15,56770'",resultAltitude:1595,
						resultCpTime:Date.parse("HH:mm:ss","12:24:13"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:16
					   ],
					   [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:29:26"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 01,12430'",resultLongitude:"E 014\u00b0 10,91000'",resultAltitude:1412,
						resultCpTime:Date.parse("HH:mm:ss","12:29:45"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:17
					   ],
					   [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:39:14"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 51,53060'",resultLongitude:"E 013\u00b0 58,04620'",resultAltitude:1435,
						resultCpTime:Date.parse("HH:mm:ss","12:39:32"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:16
					   ],
					   [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:44:08"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 44,66070'",resultLongitude:"E 014\u00b0 01,74090'",resultAltitude:1923,
						resultCpTime:Date.parse("HH:mm:ss","12:44:21"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:11
					   ],
					   [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:48:10"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,90240'",resultLongitude:"E 014\u00b0 05,07120'",resultAltitude:2058,
						resultCpTime:Date.parse("HH:mm:ss","12:48:05"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:3
					   ],
					   [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:49:33"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,64160'",resultLongitude:"E 014\u00b0 08,33550'",resultAltitude:1687,
						resultCpTime:Date.parse("HH:mm:ss","12:49:46"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:11
					   ],
					   [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:55:40"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 39,61940'",resultLongitude:"E 014\u00b0 23,36750'",resultAltitude:1774,
						resultCpTime:Date.parse("HH:mm:ss","12:55:56"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:14
					   ],
					   [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:58:57"),planProcedureTurn:true,
						resultLatitude:"N 051\u00b0 37,31980'",resultLongitude:"E 014\u00b0 20,31900'",resultAltitude:2359,
						resultCpTime:Date.parse("HH:mm:ss","12:58:58"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:06:07"),planProcedureTurn:false,
						resultLatitude:"",resultLongitude:"",resultAltitude:0,
						resultCpTime:Date.parse("HH:mm:ss","13:06:07"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","13:11:50"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 30,21180'",resultLongitude:"E 013\u00b0 58,60650'",resultAltitude:1291,
						resultCpTime:Date.parse("HH:mm:ss","13:12:06"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:14
					   ],
					  ]],	
				[name:"CoordResult 'Mannschaft 11'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 11"))),
				 data:[[type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:53:00"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 04,66460'",resultLongitude:"E 013\u00b0 49,22740'",resultAltitude:1678,
						resultCpTime:Date.parse("HH:mm:ss","10:53:04"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:2
					   ],
					   [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:00:47"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 04,93310'",resultLongitude:"E 014\u00b0 06,67050'",resultAltitude:1816,
						resultCpTime:Date.parse("HH:mm:ss","11:00:57"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:8
					   ],
					   [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:04:48"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,15430'",resultLongitude:"E 014\u00b0 15,57610'",resultAltitude:1622,
						resultCpTime:Date.parse("HH:mm:ss","11:04:54"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:4
					   ],
					   [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:10:19"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 01,22360'",resultLongitude:"E 014\u00b0 10,69030'",resultAltitude:1659,
						resultCpTime:Date.parse("HH:mm:ss","11:10:38"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:17
					   ],
					   [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:21:38"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 51,80050'",resultLongitude:"E 013\u00b0 57,51850'",resultAltitude:1571,
						resultCpTime:Date.parse("HH:mm:ss","11:21:46"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:6
					   ],
					   [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:27:08"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 44,63940'",resultLongitude:"E 014\u00b0 01,71880'",resultAltitude:1970,
						resultCpTime:Date.parse("HH:mm:ss","11:27:10"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:31:41"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,83070'",resultLongitude:"E 014\u00b0 04,75630'",resultAltitude:1827,
						resultCpTime:Date.parse("HH:mm:ss","11:31:46"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:3
					   ],
					   [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:33:14"),planProcedureTurn:false,
						resultLatitude:"",resultLongitude:"",resultAltitude:0,
						resultCpTime:Date.parse("HH:mm:ss","11:33:25"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:9
					   ],
					   [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:40:06"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 39,39500'",resultLongitude:"E 014\u00b0 23,40800'",resultAltitude:1839,
						resultCpTime:Date.parse("HH:mm:ss","11:40:09"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:1
					   ],
					   [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:43:46"),planProcedureTurn:true,
						resultLatitude:"N 051\u00b0 38,01920'",resultLongitude:"E 014\u00b0 19,61850'",resultAltitude:2007,
						resultCpTime:Date.parse("HH:mm:ss","11:43:44"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:52:07"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 33,47440'",resultLongitude:"E 014\u00b0 07,98900'",resultAltitude:1574,
						resultCpTime:Date.parse("HH:mm:ss","11:52:14"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:5
					   ],
					   [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:58:48"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 30,43450'",resultLongitude:"E 013\u00b0 58,43000'",resultAltitude:1644,
						resultCpTime:Date.parse("HH:mm:ss","11:58:55"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:5
					   ],
					  ]],	
				[name:"CoordResult 'Mannschaft 13'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(session.lastContest),Crew.findByContestAndName(session.lastContest,"Mannschaft 13"))),
				 data:[[type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","10:59:00"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 04,81010'",resultLongitude:"E 013\u00b0 49,21410'",resultAltitude:985,
						resultCpTime:Date.parse("HH:mm:ss","10:58:59"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:06:47"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,00180'",resultLongitude:"E 014\u00b0 06,69910'",resultAltitude:1293,
						resultCpTime:Date.parse("HH:mm:ss","11:06:50"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:1
					   ],
					   [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:10:48"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 05,15960'",resultLongitude:"E 014\u00b0 15,55880'",resultAltitude:1240,
						resultCpTime:Date.parse("HH:mm:ss","11:10:53"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:3
					   ],
					   [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:16:19"),planProcedureTurn:false,
						resultLatitude:"N 052\u00b0 01,26100'",resultLongitude:"E 014\u00b0 10,64230'",resultAltitude:1281,
						resultCpTime:Date.parse("HH:mm:ss","11:16:09"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:8
					   ],
					   [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:27:38"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 51,74850'",resultLongitude:"E 013\u00b0 57,59490'",resultAltitude:1424,
						resultCpTime:Date.parse("HH:mm:ss","11:27:38"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					   [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:33:08"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 44,63130'",resultLongitude:"E 014\u00b0 01,65960'",resultAltitude:1307,
						resultCpTime:Date.parse("HH:mm:ss","11:32:51"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:15
					   ],
					   [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:37:41"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,85720'",resultLongitude:"E 014\u00b0 04,88720'",resultAltitude:1149,
						resultCpTime:Date.parse("HH:mm:ss","11:37:38"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:1
					   ],
					   [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:39:14"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 38,76200'",resultLongitude:"E 014\u00b0 08,31770'",resultAltitude:995,
						resultCpTime:Date.parse("HH:mm:ss","11:39:25"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:9
					   ],
					   [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:46:06"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 39,46530'",resultLongitude:"E 014\u00b0 23,41820'",resultAltitude:895,
						resultCpTime:Date.parse("HH:mm:ss","11:46:10"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:2
					   ],
					   [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth:2,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:49:46"),planProcedureTurn:true,
						resultLatitude:"N 051\u00b0 37,98390'",resultLongitude:"E 014\u00b0 19,65190'",resultAltitude:1212,
						resultCpTime:Date.parse("HH:mm:ss","11:49:50"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:2
					   ],
					   [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","11:58:07"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 33,27590'",resultLongitude:"E 014\u00b0 08,20140'",resultAltitude:1389,
						resultCpTime:Date.parse("HH:mm:ss","11:58:13"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:4
					   ],
					   [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth:1,
					    legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
					    planCpTime:Date.parse("HH:mm:ss","12:04:48"),planProcedureTurn:false,
						resultLatitude:"N 051\u00b0 30,42410'",resultLongitude:"E 013\u00b0 58,43490'",resultAltitude:1299,
						resultCpTime:Date.parse("HH:mm:ss","12:04:50"),resultCpTimeInput:"00:00:00",
						resultCpNotFound:false,resultBadCourseNum:0,
						resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
						resultEntered:true,penaltyCoord:0
					   ],
					  ]],	
				[name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(session.lastContest)),
				 data:[[crew:[name:"Mannschaft 3"],viewpos:0,taskTAS:85,
					    flighttestwind:[wind:[direction:300,speed:15]],
					    timeCalculated:true,
						testingTime:Date.parse("HH:mm","09:06"),
						endTestingTime:Date.parse("HH:mm","10:06"),
						takeoffTime:Date.parse("HH:mm","10:21"),
						startTime:Date.parse("HH:mm","10:29"),
						finishTime:Date.parse("HH:mm:ss","11:23:36"),
						maxLandingTime:Date.parse("HH:mm:ss","11:33:36"),
						arrivalTime:Date.parse("HH:mm:ss","11:38:36"),
						arrivalTimeWarning:false,takeoffTimeWarning:false,
						planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
						planningTestLegComplete:true,planningTestComplete:true,
						flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
						flightTestLandingTooLate:false,flightTestGivenTooLate:false,
						flightTestCheckPointsComplete:true,flightTestComplete:true,
						planningTestLegPenalties:0,planningTestPenalties:0,
						flightTestCheckPointPenalties:55,flightTestPenalties:55,
						observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
						observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:140,
						taskPenalties:215,taskPosition:4
					   ],
					   [crew:[name:"Mannschaft 18"],viewpos:4,taskTAS:80,
						flighttestwind:[wind:[direction:300,speed:15]],
					    timeCalculated:true,
						testingTime:Date.parse("HH:mm","11:48"),
						endTestingTime:Date.parse("HH:mm","12:48"),
						takeoffTime:Date.parse("HH:mm","13:03"),
						startTime:Date.parse("HH:mm","13:11"),
						finishTime:Date.parse("HH:mm:ss","14:08:50"),
						maxLandingTime:Date.parse("HH:mm:ss","14:18:50"),
						arrivalTime:Date.parse("HH:mm:ss","14:23:50"),
						arrivalTimeWarning:false,takeoffTimeWarning:false,
						planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
						planningTestLegComplete:true,planningTestComplete:true,
						flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
						flightTestLandingTooLate:false,flightTestGivenTooLate:false,
						flightTestCheckPointsComplete:true,flightTestComplete:true,
						planningTestLegPenalties:0,planningTestPenalties:0,
						flightTestCheckPointPenalties:13,flightTestPenalties:13,
						observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
						observationTestGroundTargetPenalties:10,observationTestPenalties:10,landingTestPenalties:110,
						taskPenalties:133,taskPosition:1
					   ],
					   [crew:[name:"Mannschaft 19"],viewpos:3,taskTAS:80,
						flighttestwind:[wind:[direction:300,speed:15]],
					    timeCalculated:true,
						testingTime:Date.parse("HH:mm","10:51"),
						endTestingTime:Date.parse("HH:mm","11:51"),
						takeoffTime:Date.parse("HH:mm","12:06"),
						startTime:Date.parse("HH:mm","12:14"),
						finishTime:Date.parse("HH:mm:ss","13:11:50"),
						maxLandingTime:Date.parse("HH:mm:ss","13:21:50"),
						arrivalTime:Date.parse("HH:mm:ss","13:26:50"),
						arrivalTimeWarning:false,takeoffTimeWarning:false,
						planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
						planningTestLegComplete:true,planningTestComplete:true,
						flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
						flightTestLandingTooLate:false,flightTestGivenTooLate:false,
						flightTestCheckPointsComplete:true,flightTestComplete:true,
						planningTestLegPenalties:21,planningTestPenalties:21,
						flightTestCheckPointPenalties:137,flightTestPenalties:137,
						observationTestRoutePhotoPenalties:120,observationTestTurnPointPhotoPenalties:0,
						observationTestGroundTargetPenalties:10,observationTestPenalties:130,landingTestPenalties:80,
						taskPenalties:368,taskPosition:5
					   ],
					   [crew:[name:"Mannschaft 11"],viewpos:1,taskTAS:70,
						flighttestwind:[wind:[direction:300,speed:15]],
					    timeCalculated:true,
						testingTime:Date.parse("HH:mm","09:30"),
						endTestingTime:Date.parse("HH:mm","10:30"),
						takeoffTime:Date.parse("HH:mm","10:45"),
						startTime:Date.parse("HH:mm","10:53"),
						finishTime:Date.parse("HH:mm:ss","11:58:48"),
						maxLandingTime:Date.parse("HH:mm:ss","12:08:48"),
						arrivalTime:Date.parse("HH:mm:ss","12:13:48"),
						arrivalTimeWarning:false,takeoffTimeWarning:false,
						planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
						planningTestLegComplete:true,planningTestComplete:true,
						flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
						flightTestLandingTooLate:false,flightTestGivenTooLate:false,
						flightTestCheckPointsComplete:true,flightTestComplete:true,
						planningTestLegPenalties:2,planningTestPenalties:2,
						flightTestCheckPointPenalties:60,flightTestPenalties:60,
						observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
						observationTestGroundTargetPenalties:0,observationTestPenalties:0,landingTestPenalties:130,
						taskPenalties:192,taskPosition:3
					   ],
					   [crew:[name:"Mannschaft 13"],viewpos:2,taskTAS:70,
						flighttestwind:[wind:[direction:300,speed:15]],
					    timeCalculated:true,
						testingTime:Date.parse("HH:mm","09:36"),
						endTestingTime:Date.parse("HH:mm","10:36"),
						takeoffTime:Date.parse("HH:mm","10:51"),
						startTime:Date.parse("HH:mm","10:59"),
						finishTime:Date.parse("HH:mm:ss","12:04:48"),
						maxLandingTime:Date.parse("HH:mm:ss","12:14:48"),
						arrivalTime:Date.parse("HH:mm:ss","12:19:48"),
						arrivalTimeWarning:false,takeoffTimeWarning:false,
						planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
						planningTestLegComplete:true,planningTestComplete:true,
						flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
						flightTestLandingTooLate:false,flightTestGivenTooLate:false,
						flightTestCheckPointsComplete:true,flightTestComplete:true,
						planningTestLegPenalties:0,planningTestPenalties:0,
						flightTestCheckPointPenalties:45,flightTestPenalties:45,
						observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
						observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:70,
						taskPenalties:135,taskPosition:2
					   ],
					  ]],
				]
			)
			fcService.printdone "Test '$session.lastContest.title'"
			flash.error = ret.error
			flash.message = ret.msgtext
		} else {
			flash.error = true
            flash.message = "No test found."
		}
	}

	def createtest = {
		//create_test2 "Test-Wettbewerb (100 Mannschaften)"
		//create_test1 "Test-Wettbewerb 2010"
		//create_test3 "Test-Wettbewerb (20 Mannschaften)"
		create_test4 "Test Wettbewerb 2011"
		redirect(controller:'contest',action:start)
	}
			
	def runtest = {
		run_test4 "Test Wettbewerb 2011"
        redirect(controller:'contest',action:start)
    }

}
