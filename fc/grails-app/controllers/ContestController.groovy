

class ContestController {
    
	def fcService

	def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
        if (session.lastContest) {
        	def contestDayList = ContestDay.findAllByContest(session.lastContest)
            return [contestInstance:session.lastContest,contestDayInstanceList:contestDayList]
        }
        return [:]
    }

    def show = {
        if (session.lastContest) {
        	return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
    }

    def edit = {
        if (session.lastContest) {
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
        	redirect(action:start,id:contest.instance.id)
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
            session.lastContestDayTaskPlanning = null
            session.lastContestDayTaskResults = null
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

	def activate = {
		session.lastContest = Contest.get(params.id)
		session.lastContestDayTaskPlanning = null
        session.lastContestDayTaskResults = null
        redirect(action:start)
	}

	def deletequestion = {
        if (session.lastContest) {
            return [contestInstance:session.lastContest]
        } else {
            flash.message = contest.message
            redirect(action:start)
        }
	}
	
    def delete = {
        def contest = fcService.deleteContest(params)
        if (contest.deleted) {
        	if (Contest.count() > 0) {
        		session.lastContest = Contest.findByIdIsNotNull()
        		session.lastContestDayTaskPlanning = null
        		session.lastContestDayTaskResults = null
        		flash.message = contest.message
                redirect(action:start)
        	} else {
        		session.lastContest = null
                session.lastContestDayTaskPlanning = null
                session.lastContestDayTaskResults = null
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

	def cancel = {
		redirect(action:start)
	}
	
	def createday = {
		redirect(controller:'contestDay',action:'create',params:['contest.id':session.lastContest.id,'contestid':session.lastContest.id,'fromcontest':true])
	}

	void create_test1() 
	{
		// Contest
        def contest = fcService.putContest("Test-Wettbewerb 2010",200000)
        def contestday1 = fcService.putContestDay(contest,"")
        def contestdaytask1 = fcService.putContestDayTask(contestday1,"")

        // Crews and Aircrafts
        def crew1 = fcService.putCrew(contest,"Becker","Deutschland","D-EBEC","C172","rot",110.0f)
        def crew2 = fcService.putCrew(contest,"Meier","Deutschland","D-EMEI","C172","blau",130.0f)
        def crew3 = fcService.putCrew(contest,"Schulze","Deutschland","","","",140.0f)
        def crew4 = fcService.putCrew(contest,"Schmidt","Deutschland","","","",140.0f)
        def crew5 = fcService.putCrew(contest,"Richter","Deutschland","","","",140.0f)
        def crew6 = fcService.putCrew(contest,"R\u00f6thke/Weise","Deutschland","D-MWCB","","",60.0f)
        
        def aircraft3 = fcService.putAircraft(contest,"D-ESCH","C172","gelb")
        
        fcService.setaircraftCrew(crew3,aircraft3)
        fcService.setaircraftCrew(crew4,aircraft3)
        fcService.setaircraftCrew(crew5,['instance':crew1.instance.aircraft])
        
        // Routes
        def route1 = fcService.putRoute(contest,"")
    	fcService.putRouteCoord(route1,RouteCoordType.SP,0,'N',52,00,'E',14,00,1000,1,0)
        fcService.putRouteCoord(route1,RouteCoordType.SECRET,1,'N',52,20,'E',14,20,1000,1,217)
    	fcService.putRouteCoord(route1,RouteCoordType.TP,1,'N',52,20,'E',14,20,1000,1,0)
    	fcService.putRouteCoord(route1,RouteCoordType.FP,0,'N',52,35,'E',13,50,1000,1,220)
    	
        def route2 = fcService.putRoute(contest,"")
    	fcService.putRouteCoord(route2,RouteCoordType.SP,0,'N',54,00,'E',16,00,500,2,0)
    	fcService.putRouteCoord(route2,RouteCoordType.TP,1,'N',53,30,'E',16,10,500,2,0)
    	fcService.putRouteCoord(route2,RouteCoordType.FP,0,'N',52,55,'E',15,50,500,2,0)

        def route3 = fcService.putRoute(contest,"Strecke-3 (LWB-2009)")
    	fcService.putRouteCoord(route3,RouteCoordType.SP,0,'N',51,27.00000,'E',13,51.81000,500,2,0)
    	fcService.putRouteCoord(route3,RouteCoordType.TP,1,'N',51,23.13000,'E',14,05.15000,500,2,86)
    	fcService.putRouteCoord(route3,RouteCoordType.TP,2,'N',51,18.23000,'E',14,25.10000,500,2,123)
    	fcService.putRouteCoord(route3,RouteCoordType.TP,3,'N',51,26.59000,'E',14,08.03000,500,2,124)
    	fcService.putRouteCoord(route3,RouteCoordType.TP,4,'N',51,31.34000,'E',14,16.81000,500,2,68)
    	fcService.putRouteCoord(route3,RouteCoordType.FP,0,'N',51,29.38331,'E',13,52.76663,500,2,143)

    	// Planning Test
    	def planningtest1 = fcService.putPlanningTest(contestdaytask1,"")
    	def planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route3,90,10)
    	fcService.putplanningtesttaskContestDayTask(contestdaytask1,planningtesttask1)

    	// Flight Test
    	def flighttest1 = fcService.putFlightTest(contestdaytask1,"",route3)
    	def flighttestwind1 = fcService.putFlightTestWind(flighttest1,234,8)
        fcService.putflighttestwindContestDayTask(contestdaytask1,flighttestwind1)
        
    	// Landing Test
    	def landingtest1 = fcService.putLandingTest(contestdaytask1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	
    	// Special Test
    	def specialtest1 = fcService.putSpecialTest(contestdaytask1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	
    	// Calculation
        fcService.runcalculatesequenceContestDayTask(contestdaytask1)
        fcService.runcalculatetimetableContestDayTask(contestdaytask1)

        // Results
        fcService.putplanningresultsContestDayTask(contestdaytask1,[[givenTooLate:true,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:15"],
                                                                                  [trueHeading:106,legTime:"00:06:06"],
                                                                                  [trueHeading:311,legTime:"00:05:12"],
                                                                                  [trueHeading:52, legTime:"00:03:20"],
                                                                                  [trueHeading:262,legTime:"00:06:11"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:true,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:37"],
				                                                                  [trueHeading:115,legTime:"00:06:36"],
				                                                                  [trueHeading:311,legTime:"00:05:58"],
				                                                                  [trueHeading:52, legTime:"00:03:16"],
				                                                                  [trueHeading:262,legTime:"00:06:37"]],
				                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:05:32"],
				                                                                  [trueHeading:110,legTime:"00:07:55"],
				                                                                  [trueHeading:311,legTime:"00:06:50"],
				                                                                  [trueHeading:52, legTime:"00:04:19"],
				                                                                  [trueHeading:262,legTime:"00:07:44"]],
				                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:10:58"],
			                                                                      [trueHeading:110,legTime:"00:15:25"],
			                                                                      [trueHeading:311,legTime:"00:11:59"],
			                                                                      [trueHeading:52, legTime:"00:08:27"],
			                                                                      [trueHeading:262,legTime:""]],
			                                                         testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:""],
			                                                                      [trueHeading:110,legTime:""],
			                                                                      [trueHeading:311,legTime:""],
			                                                                      [trueHeading:52, legTime:""],
			                                                                      [trueHeading:262,legTime:""]],
			                                                         testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:09"],
			                                                                      [trueHeading:110,legTime:"00:06:00"],
			                                                                      [trueHeading:311,legTime:"00:05:56"],
			                                                                      [trueHeading:52, legTime:"00:03:20"],
			                                                                      [trueHeading:262,legTime:"00:06:00"]],
			                                                         testComplete:false]])

    	//fcService.putResults(contestdaytask1,crew1,190,90,60)
    	//fcService.putResults(contestdaytask1,crew2,220,20,50)
    	//fcService.putResults(contestdaytask1,crew3,210,60,40)
    	//fcService.putResults(contestdaytask1,crew4,315,40,20)
    	//fcService.putResults(contestdaytask1,crew5,175,70,80)

    	fcService.runcalculatepositionsContestDayTask(contestdaytask1)
        
        //session.lastContest = contest.instance
        //session.lastContestDayTaskPlanning = null
        //session.lastContestDayTaskResults = null
        
    }

    void create_test2() 
    {
        // Contest
        def contest = fcService.putContest("Test-Wettbewerb (100 Mannschaften)",200000)
        def contestday1 = fcService.putContestDay(contest,"")
        def contestdaytask1 = fcService.putContestDayTask(contestday1,"")
    
        // Crews and Aircrafts
        (1..100).each {
            fcService.putCrew(contest,"Name-${it.toString()}","Deutschland","D-${it.toString()}","C172","rot",110.0f)
        }
        
        // Routes
        def route1 = fcService.putRoute(contest,"")
        fcService.putRouteCoord(route1,RouteCoordType.SP,0,'N',52,00,'E',14,00,1000,1,0)
        fcService.putRouteCoord(route1,RouteCoordType.SECRET,1,'N',52,20,'E',14,20,1000,1,217)
        fcService.putRouteCoord(route1,RouteCoordType.TP,1,'N',52,20,'E',14,20,1000,1,0)
        fcService.putRouteCoord(route1,RouteCoordType.FP,0,'N',52,35,'E',13,50,1000,1,220)

        /*
        // Planning Test
        def planningtest1 = fcService.putPlanningTest(contestdaytask1,"")
        def planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route3,90,10)
        fcService.putplanningtesttaskContestDayTask(contestdaytask1,planningtesttask1)
    
        // Flight Test
        def flighttest1 = fcService.putFlightTest(contestdaytask1,"",route3)
        def flighttestwind1 = fcService.putFlightTestWind(flighttest1,234,8)
        fcService.putflighttestwindContestDayTask(contestdaytask1,flighttestwind1)
        
        // Landing Test
        def landingtest1 = fcService.putLandingTest(contestdaytask1,"")
        fcService.putLandingTestTask(landingtest1,"")
        fcService.putLandingTestTask(landingtest1,"")
        fcService.putLandingTestTask(landingtest1,"")
        
        // Special Test
        def specialtest1 = fcService.putSpecialTest(contestdaytask1,"")
        fcService.putSpecialTestTask(specialtest1,"")
        fcService.putSpecialTestTask(specialtest1,"")
        fcService.putSpecialTestTask(specialtest1,"")
        
        // Calculation
        fcService.runcalculatesequenceContestDayTask(contestdaytask1)
        fcService.runcalculatetimetableContestDayTask(contestdaytask1)
    
        // Results
        fcService.putplanningresultsContestDayTask(contestdaytask1,[[givenTooLate:true,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:15"],
                                                                                  [trueHeading:106,legTime:"00:06:06"],
                                                                                  [trueHeading:311,legTime:"00:05:12"],
                                                                                  [trueHeading:52, legTime:"00:03:20"],
                                                                                  [trueHeading:262,legTime:"00:06:11"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:true,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:37"],
                                                                                  [trueHeading:115,legTime:"00:06:36"],
                                                                                  [trueHeading:311,legTime:"00:05:58"],
                                                                                  [trueHeading:52, legTime:"00:03:16"],
                                                                                  [trueHeading:262,legTime:"00:06:37"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:05:32"],
                                                                                  [trueHeading:110,legTime:"00:07:55"],
                                                                                  [trueHeading:311,legTime:"00:06:50"],
                                                                                  [trueHeading:52, legTime:"00:04:19"],
                                                                                  [trueHeading:262,legTime:"00:07:44"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:10:58"],
                                                                                  [trueHeading:110,legTime:"00:15:25"],
                                                                                  [trueHeading:311,legTime:"00:11:59"],
                                                                                  [trueHeading:52, legTime:"00:08:27"],
                                                                                  [trueHeading:262,legTime:""]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:""],
                                                                                  [trueHeading:110,legTime:""],
                                                                                  [trueHeading:311,legTime:""],
                                                                                  [trueHeading:52, legTime:""],
                                                                                  [trueHeading:262,legTime:""]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:09"],
                                                                                  [trueHeading:110,legTime:"00:06:00"],
                                                                                  [trueHeading:311,legTime:"00:05:56"],
                                                                                  [trueHeading:52, legTime:"00:03:20"],
                                                                                  [trueHeading:262,legTime:"00:06:00"]],
                                                                     testComplete:false]])
    
        //fcService.putResults(contestdaytask1,crew1,190,90,60)
        //fcService.putResults(contestdaytask1,crew2,220,20,50)
        //fcService.putResults(contestdaytask1,crew3,210,60,40)
        //fcService.putResults(contestdaytask1,crew4,315,40,20)
        //fcService.putResults(contestdaytask1,crew5,175,70,80)
    
        fcService.runcalculatepositionsContestDayTask(contestdaytask1)
        */
        
    }

    void create_test3() 
    {
        // Contest
        def contest = fcService.putContest("Test-Wettbewerb (20 Mannschaften)",200000)
        def contestday1 = fcService.putContestDay(contest,"")
        def contestdaytask1 = fcService.putContestDayTask(contestday1,"")
    
        // Crews and Aircrafts
        (1..20).each {
            fcService.putCrew(contest,"Name-${it.toString()}","Deutschland","D-${it.toString()}","C172","rot",110.0f)
        }
        
        // Routes
        def route1 = fcService.putRoute(contest,"")
        fcService.putRouteCoord(route1,RouteCoordType.SP,0,'N',52,00,'E',14,00,1000,1,0)
        fcService.putRouteCoord(route1,RouteCoordType.SECRET,1,'N',52,20,'E',14,20,1000,1,217)
        fcService.putRouteCoord(route1,RouteCoordType.TP,1,'N',52,20,'E',14,20,1000,1,0)
        fcService.putRouteCoord(route1,RouteCoordType.FP,0,'N',52,35,'E',13,50,1000,1,220)

        /*
        // Planning Test
        def planningtest1 = fcService.putPlanningTest(contestdaytask1,"")
        def planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route3,90,10)
        fcService.putplanningtesttaskContestDayTask(contestdaytask1,planningtesttask1)
    
        // Flight Test
        def flighttest1 = fcService.putFlightTest(contestdaytask1,"",route3)
        def flighttestwind1 = fcService.putFlightTestWind(flighttest1,234,8)
        fcService.putflighttestwindContestDayTask(contestdaytask1,flighttestwind1)
        
        // Landing Test
        def landingtest1 = fcService.putLandingTest(contestdaytask1,"")
        fcService.putLandingTestTask(landingtest1,"")
        fcService.putLandingTestTask(landingtest1,"")
        fcService.putLandingTestTask(landingtest1,"")
        
        // Special Test
        def specialtest1 = fcService.putSpecialTest(contestdaytask1,"")
        fcService.putSpecialTestTask(specialtest1,"")
        fcService.putSpecialTestTask(specialtest1,"")
        fcService.putSpecialTestTask(specialtest1,"")
        
        // Calculation
        fcService.runcalculatesequenceContestDayTask(contestdaytask1)
        fcService.runcalculatetimetableContestDayTask(contestdaytask1)
    
        // Results
        fcService.putplanningresultsContestDayTask(contestdaytask1,[[givenTooLate:true,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:15"],
                                                                                  [trueHeading:106,legTime:"00:06:06"],
                                                                                  [trueHeading:311,legTime:"00:05:12"],
                                                                                  [trueHeading:52, legTime:"00:03:20"],
                                                                                  [trueHeading:262,legTime:"00:06:11"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:true,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:37"],
                                                                                  [trueHeading:115,legTime:"00:06:36"],
                                                                                  [trueHeading:311,legTime:"00:05:58"],
                                                                                  [trueHeading:52, legTime:"00:03:16"],
                                                                                  [trueHeading:262,legTime:"00:06:37"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:05:32"],
                                                                                  [trueHeading:110,legTime:"00:07:55"],
                                                                                  [trueHeading:311,legTime:"00:06:50"],
                                                                                  [trueHeading:52, legTime:"00:04:19"],
                                                                                  [trueHeading:262,legTime:"00:07:44"]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:10:58"],
                                                                                  [trueHeading:110,legTime:"00:15:25"],
                                                                                  [trueHeading:311,legTime:"00:11:59"],
                                                                                  [trueHeading:52, legTime:"00:08:27"],
                                                                                  [trueHeading:262,legTime:""]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:""],
                                                                                  [trueHeading:110,legTime:""],
                                                                                  [trueHeading:311,legTime:""],
                                                                                  [trueHeading:52, legTime:""],
                                                                                  [trueHeading:262,legTime:""]],
                                                                     testComplete:true],
                                                                    [givenTooLate:false,
                                                                     exitRoomTooLate:false,
                                                                     givenValues:[[trueHeading:113,legTime:"00:04:09"],
                                                                                  [trueHeading:110,legTime:"00:06:00"],
                                                                                  [trueHeading:311,legTime:"00:05:56"],
                                                                                  [trueHeading:52, legTime:"00:03:20"],
                                                                                  [trueHeading:262,legTime:"00:06:00"]],
                                                                     testComplete:false]])
    
        //fcService.putResults(contestdaytask1,crew1,190,90,60)
        //fcService.putResults(contestdaytask1,crew2,220,20,50)
        //fcService.putResults(contestdaytask1,crew3,210,60,40)
        //fcService.putResults(contestdaytask1,crew4,315,40,20)
        //fcService.putResults(contestdaytask1,crew5,175,70,80)
    
        fcService.runcalculatepositionsContestDayTask(contestdaytask1)
        */
        
    }

    def createtest = {
        create_test2()
        create_test1()
        create_test3()
        redirect(controller:'contest',action:start)
    }
            
}
