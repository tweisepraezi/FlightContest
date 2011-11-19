

class ContestController {
    
	def fcService

	def index = { redirect(action:start,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def start = {
        if (session.lastContest) {
            return [contestInstance:session.lastContest]
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
        	session.lastContest = contest.instance
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
            session.lastTaskPlanning = null
            session.lastTaskResults = null
        	flash.message = contest.message
        	redirect(action:start,id:contest.instance.id)
        } else {
            render(view:'create',model:[contestInstance:contest.instance])
        }
    }

    def tasks = {
        if (session.lastContest) {
            def contestTasksList = Task.findAllByContest(session.lastContest)
            return [contestInstance:session.lastContest,contestTasks:contestTasksList]
        }
        return [:]
    }

	def activate = {
		session.lastContest = Contest.get(params.id)
		session.lastTaskPlanning = null
        session.lastTaskResults = null
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
        		session.lastTaskPlanning = null
        		session.lastTaskResults = null
        		flash.message = contest.message
                redirect(action:start)
        	} else {
        		session.lastContest = null
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
        redirect(action:start)
    }

    def updatecontest = {
        session.lastContest = Contest.get(params.contestid)
        session.lastTaskPlanning = null
        session.lastTaskResults = null
        redirect(action:start)
    }
        
	def cancel = {
		redirect(action:start)
	}
	
	def createtask = {
		redirect(controller:'task',action:'create',params:['contest.id':session.lastContest.id,'contestid':session.lastContest.id,'fromcontest':true])
	}

	void create_test1() 
	{
		// Contest
        def contest = fcService.putContest("Test-Wettbewerb 2010",200000)
        def task1 = fcService.putTask(contest,"")

        // Crews and Aircrafts
        def crew1 = fcService.putCrew(contest,"Becker","Deutschland","D-EBEC","C172","rot",110.0f)
        def crew2 = fcService.putCrew(contest,"Meier","Deutschland","D-EMEI","C172","blau",130.0f)
        def crew3 = fcService.putCrew(contest,"Schulze","Deutschland","","","",140.0f)
        def crew4 = fcService.putCrew(contest,"Schmidt","Deutschland","","","",140.0f)
        def crew5 = fcService.putCrew(contest,"Richter","Deutschland","","","",140.0f)
        def crew6 = fcService.putCrew(contest,"M\u00fcller","Deutschland","D-MUEL","","",60.0f)
        
        def aircraft3 = fcService.putAircraft(contest,"D-ESCH","C172","gelb")
        
        fcService.setaircraftCrew(crew3,aircraft3)
        fcService.setaircraftCrew(crew4,aircraft3)
        fcService.setaircraftCrew(crew5,['instance':crew1.instance.aircraft])
        
        // Routes
        def route1 = fcService.putRoute(contest,"","")
    	fcService.putCoordRoute(route1,CoordType.SP,    0,'SP', 'N',52,20,'E',14,00,501,1,null,null)
        fcService.putCoordRoute(route1,CoordType.SECRET,1,'CP1','N',52,20,'E',14,20,501,1,217,31)
    	fcService.putCoordRoute(route1,CoordType.TP,    1,'CP2','N',52,20,'E',14,40,501,1,null,0)
    	fcService.putCoordRoute(route1,CoordType.FP,    0,'FP', 'N',52,35,'E',13,50,501,1,220,209)
    	
        def route2 = fcService.putRoute(contest,"","")
    	fcService.putCoordRoute(route2,CoordType.SP,    0,'SP', 'N',54,00,'E',16,00,502,1,null,null)
    	fcService.putCoordRoute(route2,CoordType.TP,    1,'CP1','N',53,30,'E',16,10,502,1,null,null)
    	fcService.putCoordRoute(route2,CoordType.FP,    0,'FP', 'N',52,55,'E',15,50,502,1,null,null)

        def route3 = fcService.putRoute(contest,"Strecke-3 (LWB-2009 mit unbekannten Zeitkontrollen)","LWB2009mitUnbekZK")
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

        def route4 = fcService.putRoute(contest,"Strecke-4 (LWB-2009)","")
        fcService.putCoordRoute(route4,CoordType.SP,    0,'SP', 'N',51,27.00000,'E',13,51.81000,504,2,null,null)
        fcService.putCoordRoute(route4,CoordType.TP,    1,'CP1','N',51,23.13000,'E',14,05.15000,504,2,86,115)
        fcService.putCoordRoute(route4,CoordType.TP,    2,'CP2','N',51,18.23000,'E',14,25.10000,504,2,123,111)
        fcService.putCoordRoute(route4,CoordType.TP,    3,'CP3','N',51,26.59000,'E',14,08.03000,504,2,124,308)
        fcService.putCoordRoute(route4,CoordType.TP,    4,'CP4','N',51,31.34000,'E',14,16.81000,504,2,68,49)
        fcService.putCoordRoute(route4,CoordType.FP,    0,'FP', 'N',51,29.38331,'E',13,52.76663,504,2,143,263)

    	// Planning Test
    	def planningtest1 = fcService.putPlanningTest(task1,"")
    	def planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route3,90,10)
    	fcService.putplanningtesttaskTask(task1,planningtesttask1)

    	// Flight Test
    	def flighttest1 = fcService.putFlightTest(task1,"",route3)
    	def flighttestwind1 = fcService.putFlightTestWind(flighttest1,234,8)
        fcService.putflighttestwindTask(task1,flighttestwind1)
        
    	// Landing Test
    	def landingtest1 = fcService.putLandingTest(task1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	fcService.putLandingTestTask(landingtest1,"")
    	
    	// Special Test
    	def specialtest1 = fcService.putSpecialTest(task1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	fcService.putSpecialTestTask(specialtest1,"")
    	
    	// Calculation
        fcService.runcalculatesequenceTask(task1)
        fcService.runcalculatetimetableTask(task1)

        // Results
        fcService.putplanningresultsTask(task1,[[givenTooLate:true,
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
			                                     
        fcService.putflightresultsTask(task1,  [[takeoffMissed:true,
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
                                                [takeoffMissed:true,
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
                                                [takeoffMissed:false,
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
                                                [takeoffMissed:false,
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
                                                [takeoffMissed:false,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:false,
                                                 givenTooLate:true,
                                                 givenValues:[],
                                                 testComplete:true],
                                                [takeoffMissed:false,
                                                 badCourseStartLanding:false,
                                                 landingTooLate:false,
                                                 givenTooLate:false,
                                                 givenValues:[],
                                                 testComplete:false]])


    	fcService.runcalculatepositionsTask(task1)
        
        //session.lastContest = contest.instance
        //session.lastTaskPlanning = null
        //session.lastTaskResults = null
        
    }

    void create_test2() 
    {
        // Contest
        def contest = fcService.putContest("Test-Wettbewerb (100 Mannschaften)",200000)
        def task1 = fcService.putTask(contest,"")
    
        // Crews and Aircrafts
        (1..100).each {
            fcService.putCrew(contest,"Name-${it.toString()}","Deutschland","D-${it.toString()}","C172","rot",110.0f)
        }
    }

    void create_test3() 
    {
        // Contest
        def contest = fcService.putContest("Test-Wettbewerb (20 Mannschaften)",200000)
        def task1 = fcService.putTask(contest,"")
    
        // Crews and Aircrafts
        (1..20).each {
            fcService.putCrew(contest,"Name-${it.toString()}","Deutschland","D-${it.toString()}","C172","rot",110.0f)
        }
    }

    def createtest = {
        create_test2()
        create_test1()
        create_test3()
        redirect(controller:'contest',action:start)
    }
            
}
