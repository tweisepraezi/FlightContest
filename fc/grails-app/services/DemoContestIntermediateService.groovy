class DemoContestIntermediateService 
{
	def fcService
	
	static final String ROUTE_NAME = "Strecke 8"
	static final String ROUTE_NAME_NOITO = "Strecke 8 (noiTO)"
	static final String ROUTE_NAME_NOILDG = "Strecke 8 (noiLDG)"
	static final String ROUTE_NAME_NOITOLDG = "Strecke 8 (noiTOLDG)"
    static final String ROUTE_NAME_GPX = "Strecke_8.gpx"
    static final String ROUTE_NAME_NOITO_GPX = "Strecke_8_noiTO.gpx"
    static final String ROUTE_NAME_NOILDG_GPX = "Strecke_8_noiLDG.gpx"
    static final String ROUTE_NAME_NOITOLDG_GPX = "Strecke_8_noiTOLDG.gpx"

	static final String CREW_60 = "Crew 60kt"
	static final String CREW_90 = "Crew 90kt"
	static final String CREW_120 = "Crew 120kt"
	
	static final String START_TIME = "08:45"
	
	static final BigDecimal WIND_DIRECTION = 0
	static final BigDecimal WIND_SPEED = 15
	
	static final String WIND = "$WIND_DIRECTION/$WIND_SPEED"
	static final String NOWIND = "0/0"
	
	long CreateTest(String testName, String printPrefix, boolean testExists)
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
		Map contest = fcService.putContest(testName,printPrefix,false,0,ContestRules.R11,"2020-08-01","Europe/Berlin",testExists)
		
		// Routes
        Map route = [:]
        Map route_noito = [:]
        Map route_noildg = [:]
        Map route_noitoldg = [:]
		fcService.printstart ROUTE_NAME
        route = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_GPX)
		fcService.printdone ""
		fcService.printstart ROUTE_NAME_NOITO
        route_noito = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_NOITO_GPX)
		fcService.printdone ""
		fcService.printstart ROUTE_NAME_NOILDG
        route_noildg = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_NOILDG_GPX)
		fcService.printdone ""
		fcService.printstart ROUTE_NAME_NOITOLDG
        route_noitoldg = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_NOITOLDG_GPX)
		fcService.printdone ""

		// Crews and Aircrafts
		fcService.importCrewList(contest, "FC-CrewList-Test.xlsx", false) // false - mit Start-Nr. 13
		
		// Flight Tests
		Map task_normal = fcService.putTask(contest,"$ROUTE_NAME ($NOWIND)",START_TIME,2,"time:10min","time:10min",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_normal = fcService.putPlanningTest(task_normal,"")
        Map planningtesttask_normal = fcService.putPlanningTestTask(planningtest_normal,"",route,0,0)
        fcService.putplanningtesttaskTask(task_normal,planningtesttask_normal)
		Map flighttest_normal = fcService.putFlightTest(task_normal,"",route)
		Map flighttestwind_normal = fcService.putFlightTestWind(flighttest_normal,0,0,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_normal,flighttestwind_normal)
		fcService.runcalculatetimetableTask(task_normal)
		
		Map task_wind = fcService.putTask(contest,"$ROUTE_NAME ($WIND)",START_TIME,2,"time:10min","time:10min",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_wind = fcService.putPlanningTest(task_wind,"")
        Map planningtesttask_wind = fcService.putPlanningTestTask(planningtest_wind,"",route,WIND_DIRECTION,WIND_SPEED)
        fcService.putplanningtesttaskTask(task_wind,planningtesttask_wind)
		Map flighttest_wind = fcService.putFlightTest(task_wind,"",route)
		Map flighttestwind_wind = fcService.putFlightTestWind(flighttest_wind,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_wind,flighttestwind_wind)
		fcService.runcalculatetimetableTask(task_wind)
		
		Map task_wind_iup = fcService.putTask(contest,"$ROUTE_NAME ($WIND+)",START_TIME,2,"time:10min","time:10min",5,"wind+:1","wind+:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_wind_iup = fcService.putPlanningTest(task_wind_iup,"")
        Map planningtesttask_wind_iup = fcService.putPlanningTestTask(planningtest_wind_iup,"",route,WIND_DIRECTION,WIND_SPEED)
        fcService.putplanningtesttaskTask(task_wind_iup,planningtesttask_wind_iup)
		Map flighttest_wind_iup = fcService.putFlightTest(task_wind_iup,"",route)
		Map flighttestwind_wind_iup = fcService.putFlightTestWind(flighttest_wind_iup,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_wind_iup,flighttestwind_wind_iup)
		fcService.runcalculatetimetableTask(task_wind_iup)
		
		Map task_wind_iall = fcService.putTask(contest,"$ROUTE_NAME ($WIND+) All",START_TIME,2,"wind+:2NM","wind:2NM",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_wind_iall = fcService.putPlanningTest(task_wind_iall,"")
        Map planningtesttask_wind_iall = fcService.putPlanningTestTask(planningtest_wind_iall,"",route,WIND_DIRECTION,WIND_SPEED)
        fcService.putplanningtesttaskTask(task_wind_iall,planningtesttask_wind_iall)
		Map flighttest_wind_iall = fcService.putFlightTest(task_wind_iall,"",route)
		Map flighttestwind_wind_iall = fcService.putFlightTestWind(flighttest_wind_iall,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_wind_iall,flighttestwind_wind_iall)
		fcService.runcalculatetimetableTask(task_wind_iall)
		
		Map task_wind_noito = fcService.putTask(contest,"$ROUTE_NAME_NOITO ($WIND+) All",START_TIME,2,"wind+:2NM","wind:2NM",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_wind_noito = fcService.putPlanningTest(task_wind_noito,"")
        Map planningtesttask_wind_noito = fcService.putPlanningTestTask(planningtest_wind_noito,"",route_noito,WIND_DIRECTION,WIND_SPEED)
        fcService.putplanningtesttaskTask(task_wind_noito,planningtesttask_wind_noito)
		Map flighttest_wind_noito = fcService.putFlightTest(task_wind_noito,"",route_noito)
		Map flighttestwind_wind_noito = fcService.putFlightTestWind(flighttest_wind_noito,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_wind_noito,flighttestwind_wind_noito)
		fcService.runcalculatetimetableTask(task_wind_noito)
		
		Map task_wind_noildg = fcService.putTask(contest,"$ROUTE_NAME_NOILDG ($WIND+) All",START_TIME,2,"wind+:2NM","wind:2NM",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_wind_noildg = fcService.putPlanningTest(task_wind_noildg,"")
        Map planningtesttask_wind_noildg = fcService.putPlanningTestTask(planningtest_wind_noildg,"",route_noildg,WIND_DIRECTION,WIND_SPEED)
        fcService.putplanningtesttaskTask(task_wind_noildg,planningtesttask_wind_noildg)
		Map flighttest_wind_noildg = fcService.putFlightTest(task_wind_noildg,"",route_noildg)
		Map flighttestwind_wind_noildg = fcService.putFlightTestWind(flighttest_wind_noildg,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_wind_noildg,flighttestwind_wind_noildg)
		fcService.runcalculatetimetableTask(task_wind_noildg)
		
		Map task_wind_noitoldg = fcService.putTask(contest,"$ROUTE_NAME_NOITOLDG ($WIND+) All",START_TIME,2,"wind+:2NM","wind:2NM",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
        Map planningtest_wind_noitoldg = fcService.putPlanningTest(task_wind_noitoldg,"")
        Map planningtesttask_wind_noitoldg = fcService.putPlanningTestTask(planningtest_wind_noitoldg,"",route_noitoldg,WIND_DIRECTION,WIND_SPEED)
        fcService.putplanningtesttaskTask(task_wind_noitoldg,planningtesttask_wind_noitoldg)
		Map flighttest_wind_noitoldg = fcService.putFlightTest(task_wind_noitoldg,"",route_noitoldg)
		Map flighttestwind_wind_noitoldg = fcService.putFlightTestWind(flighttest_wind_noitoldg,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_wind_noitoldg,flighttestwind_wind_noitoldg)
		fcService.runcalculatetimetableTask(task_wind_noitoldg)
		
		fcService.printdone ""
		
		return contest.instance.id
	}
	
	Map RunTest(Contest lastContest, String contestName)
	{
		Map ret_test = [:]
		if (lastContest && lastContest.title == contestName) {
			fcService.printstart "runtest '$lastContest.title'"
			Route route
			Route route_noito
			Route route_noildg
			Route route_noitoldg
			Route.findAllByContest(lastContest).eachWithIndex { Route r, int i ->
				switch (i) {
					case 0:
						route = r
						break
					case 1:
						route_noito = r
						break
					case 2:
						route_noildg = r
						break
					case 3:
						route_noitoldg = r
						break
				}
			}
			Task task_normal
			Task task_wind
			Task task_wind_iup
			Task task_wind_iall
			Task task_wind_noito
			Task task_wind_noildg
			Task task_wind_noitoldg
			Task.findAllByContest(lastContest).eachWithIndex { Task task, int i ->
				switch (i) {
					case 0:
						task_normal = task
						break
					case 1:
						task_wind = task
						break
					case 2:
						task_wind_iup = task
						break
					case 3:
						task_wind_iall = task
						break
					case 4:
						task_wind_noito = task
						break
					case 5:
						task_wind_noildg = task
						break
					case 6:
						task_wind_noitoldg = task
						break
				}

			}
			Map ret = fcService.testData(
			   [[name:"Route",count:4,table:Route.findAllByContest(lastContest,[sort:"id"]),data:testRoute()],
				   
				[name:"CoordRoute '$ROUTE_NAME'",         count:10,table:CoordRoute.findAllByRoute(   route,         [sort:"id"]),data:testCoordRoute(ROUTE_NAME)],
				[name:"CoordRoute '$ROUTE_NAME_NOITO'",   count: 9,table:CoordRoute.findAllByRoute(   route_noito,   [sort:"id"]),data:testCoordRoute(ROUTE_NAME_NOITO)],
				[name:"CoordRoute '$ROUTE_NAME_NOILDG'",  count: 9,table:CoordRoute.findAllByRoute(   route_noildg,  [sort:"id"]),data:testCoordRoute(ROUTE_NAME_NOILDG)],
				[name:"CoordRoute '$ROUTE_NAME_NOITOLDG'",count: 8,table:CoordRoute.findAllByRoute(   route_noitoldg,[sort:"id"]),data:testCoordRoute(ROUTE_NAME_NOITOLDG)],
				
				[name:"RouteLegCoord '$ROUTE_NAME'",         count:9, table:RouteLegCoord.findAllByRoute(route,         [sort:"id"]),data:testRouteLegCoord(ROUTE_NAME)],
				[name:"RouteLegCoord '$ROUTE_NAME_NOITO'",   count:8, table:RouteLegCoord.findAllByRoute(route_noito,   [sort:"id"]),data:testRouteLegCoord(ROUTE_NAME_NOITO)],
				[name:"RouteLegCoord '$ROUTE_NAME_NOILDG'",  count:8, table:RouteLegCoord.findAllByRoute(route_noildg,  [sort:"id"]),data:testRouteLegCoord(ROUTE_NAME_NOILDG)],
				[name:"RouteLegCoord '$ROUTE_NAME_NOITOLDG'",count:7, table:RouteLegCoord.findAllByRoute(route_noitoldg,[sort:"id"]),data:testRouteLegCoord(ROUTE_NAME_NOITOLDG)],
				
				[name:"RouteLegTest '$ROUTE_NAME'",         count:7, table:RouteLegTest.findAllByRoute( route,         [sort:"id"]),data:testRouteLegTest(ROUTE_NAME)],
				[name:"RouteLegTest '$ROUTE_NAME_NOITO'",   count:6, table:RouteLegTest.findAllByRoute( route_noito,   [sort:"id"]),data:testRouteLegTest(ROUTE_NAME_NOITO)],
				[name:"RouteLegTest '$ROUTE_NAME_NOILDG'",  count:6, table:RouteLegTest.findAllByRoute( route_noildg,  [sort:"id"]),data:testRouteLegTest(ROUTE_NAME_NOILDG)],
				[name:"RouteLegTest '$ROUTE_NAME_NOITOLDG'",count:5, table:RouteLegTest.findAllByRoute( route_noitoldg,[sort:"id"]),data:testRouteLegTest(ROUTE_NAME_NOITOLDG)],
				
				[name:"Crew",count:3,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:testCrew()],
				[name:"Task",count:7,table:Task.findAllByContest(lastContest,[sort:"id"]),data:testTask()],
				
				[name:"TestLegPlanning '$ROUTE_NAME ($NOWIND) - $CREW_60'",     count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME ($NOWIND) - $CREW_90'",     count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME ($NOWIND) - $CREW_120'",    count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME,false,false)],
				
				[name:"TestLegFlight '$ROUTE_NAME ($NOWIND) - $CREW_60'",       count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME,false,false,false)],
				[name:"TestLegFlight '$ROUTE_NAME ($NOWIND) - $CREW_90'",       count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME,false,false,false)],
				[name:"TestLegFlight '$ROUTE_NAME ($NOWIND) - $CREW_120'",      count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME,false,false,false)],
				
				[name:"CoordResult '$ROUTE_NAME ($NOWIND) - $CREW_60'",         count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME,false,false,false)],
				[name:"CoordResult '$ROUTE_NAME ($NOWIND) - $CREW_90'",         count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME,false,false,false)],
				[name:"CoordResult '$ROUTE_NAME ($NOWIND) - $CREW_120'",        count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal,   Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME,false,false,false)],
				
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND) - $CREW_60'",       count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME,true,false)],
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND) - $CREW_90'",       count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME,true,false)],
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND) - $CREW_120'",      count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME,true,false)],
				
				[name:"TestLegFlight '$ROUTE_NAME ($WIND) - $CREW_60'",         count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME,true,false,false)],
				[name:"TestLegFlight '$ROUTE_NAME ($WIND) - $CREW_90'",         count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME,true,false,false)],
				[name:"TestLegFlight '$ROUTE_NAME ($WIND) - $CREW_120'",        count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME,true,false,false)],
				
				[name:"CoordResult '$ROUTE_NAME ($WIND) - $CREW_60'",           count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME,true,false,false)],
				[name:"CoordResult '$ROUTE_NAME ($WIND) - $CREW_90'",           count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME,true,false,false)],
				[name:"CoordResult '$ROUTE_NAME ($WIND) - $CREW_120'",          count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME,true,false,false)],
				
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND+) - $CREW_60'",      count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND+) - $CREW_90'",      count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND+) - $CREW_120'",     count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME,true,true)],
				
				[name:"TestLegFlight '$ROUTE_NAME ($WIND+) - $CREW_60'",        count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME,true,true,false)],
				[name:"TestLegFlight '$ROUTE_NAME ($WIND+) - $CREW_90'",        count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME,true,true,false)],
				[name:"TestLegFlight '$ROUTE_NAME ($WIND+) - $CREW_120'",       count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME,true,true,false)],
				
				[name:"CoordResult '$ROUTE_NAME ($WIND+) - $CREW_60'",          count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME,true,true,false)],
				[name:"CoordResult '$ROUTE_NAME ($WIND+) - $CREW_90'",          count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME,true,true,false)],
				[name:"CoordResult '$ROUTE_NAME ($WIND+) - $CREW_120'",         count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_iup, Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME,true,true,false)],
				
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND+) All - $CREW_60'",  count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME,true,false)],
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND+) All - $CREW_90'",  count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME,true,false)],
				[name:"TestLegPlanning '$ROUTE_NAME ($WIND+) All - $CREW_120'", count:7, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME,true,false)],
				
				[name:"TestLegFlight '$ROUTE_NAME ($WIND+) All - $CREW_60'",    count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME ($WIND+) All - $CREW_90'",    count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME ($WIND+) All - $CREW_120'",   count:7, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME,true,true,true)],
				
				[name:"CoordResult '$ROUTE_NAME ($WIND+) All - $CREW_60'",      count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME ($WIND+) All - $CREW_90'",      count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME ($WIND+) All - $CREW_120'",     count:10,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_iall,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME,true,true,true)],
				
				[name:"TestLegPlanning '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_60'",  count:6, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME_NOITO,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_90'",  count:6, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME_NOITO,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_120'", count:6, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_NOITO,true,true)],
				
				[name:"TestLegFlight '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_60'",    count:6, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME_NOITO,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_90'",    count:6, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME_NOITO,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_120'",   count:6, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME_NOITO,true,true,true)],
				
				[name:"CoordResult '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_60'",      count:9, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME_NOITO,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_90'",      count:9, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME_NOITO,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME_NOITO ($WIND+) All - $CREW_120'",     count:9, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noito,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME_NOITO,true,true,true)],

				[name:"TestLegPlanning '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_60'",  count:6, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME_NOILDG,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_90'",  count:6, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME_NOILDG,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_120'", count:6, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_NOILDG,true,true)],
				
				[name:"TestLegFlight '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_60'",    count:6, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME_NOILDG,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_90'",    count:6, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME_NOILDG,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_120'",   count:6, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME_NOILDG,true,true,true)],
				
				[name:"CoordResult '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_60'",      count:9, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME_NOILDG,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_90'",      count:9, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME_NOILDG,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME_NOILDG ($WIND+) All - $CREW_120'",     count:9, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noildg,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME_NOILDG,true,true,true)],

				[name:"TestLegPlanning '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_60'",  count:5, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60(ROUTE_NAME_NOITOLDG,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_90'",  count:5, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90(ROUTE_NAME_NOITOLDG,true,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_120'", count:5, table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_NOITOLDG,true,true)],
				
				[name:"TestLegFlight '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_60'",    count:5, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(ROUTE_NAME_NOITOLDG,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_90'",    count:5, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(ROUTE_NAME_NOITOLDG,true,true,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_120'",   count:5, table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(ROUTE_NAME_NOITOLDG,true,true,true)],
				
				[name:"CoordResult '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_60'",      count:8, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(ROUTE_NAME_NOITOLDG,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_90'",      count:8, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(ROUTE_NAME_NOITOLDG,true,true,true)],
				[name:"CoordResult '$ROUTE_NAME_NOITOLDG ($WIND+) All - $CREW_120'",     count:8, table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_wind_noitoldg,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(ROUTE_NAME_NOITOLDG,true,true,true)],

			   ]
			)
			fcService.printdone "Test '$lastContest.title'"
			ret_test.error = ret.error
			ret_test.message = ret.msgtext
		} else {
			ret_test.error = true
			ret_test.message = "No test found."
		}
		return ret_test
	}
	
	List testRoute() {
		[[title:ROUTE_NAME,mark:ROUTE_NAME],[title:ROUTE_NAME_NOITO,mark:ROUTE_NAME],[title:ROUTE_NAME_NOILDG,mark:ROUTE_NAME],[title:ROUTE_NAME_NOITOLDG,mark:ROUTE_NAME]]
	}
	
	List testCoordRoute(String routeName) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME) {
			l = [
				[mark:"T/O", type:CoordType.TO,   latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,           measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"SP",  type:CoordType.SP,   latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP1", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iFP", type:CoordType.iFP,  latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iLDG",type:CoordType.iLDG, latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iT/O",type:CoordType.iTO,  latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iSP", type:CoordType.iSP,  latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP2", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"FP",  type:CoordType.FP,   latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"LDG", type:CoordType.LDG,  latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
			]
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[mark:"T/O", type:CoordType.TO,   latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,            measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"SP",  type:CoordType.SP,   latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP1", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iFP", type:CoordType.iFP,  latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iLDG",type:CoordType.iLDG, latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iSP", type:CoordType.iSP,  latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:18.5200000370,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP2", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"FP",  type:CoordType.FP,   latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"LDG", type:CoordType.LDG,  latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,   latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,            measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"SP",  type:CoordType.SP,   latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP1", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iFP", type:CoordType.iFP,  latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iT/O",type:CoordType.iTO,  latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:18.5199999815,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iSP", type:CoordType.iSP,  latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP2", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"FP",  type:CoordType.FP,   latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"LDG", type:CoordType.LDG,  latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,   latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,            measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"SP",  type:CoordType.SP,   latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP1", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iFP", type:CoordType.iFP,  latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iSP", type:CoordType.iSP,  latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:27.7800000000,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP2", type:CoordType.TP,   latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2599999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"FP",  type:CoordType.FP,   latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"LDG", type:CoordType.LDG,  latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9, lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:9.2600000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
			]
		}
		return l
	}
	
	List testRouteLegCoord(String routeName) {
		List l
		if (routeName == ROUTE_NAME) {
			l = [
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
			]
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:2.0000000040, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.9999999980, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:3.0000000000, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:0.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
			]
		}
		return l
	}
	
	List testRouteLegTest(String routeName) {
		List l
		if (routeName == ROUTE_NAME) {
			l = [
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
			]
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:2,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:2,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:3,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack:0,coordDistance:1,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
			]
		}
		return l
	}
	
	List testCrew() {
		 [[startNum:1,name:CREW_60,mark:"",team:null,tas:60,contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-60",type:"",colour:""]],
		  [startNum:2,name:CREW_90,mark:"",team:null,tas:90,contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-90",type:"",colour:""]],
		  [startNum:3,name:CREW_120,mark:"",team:null,tas:120,contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:"D-120",type:"",colour:""]],
		 ]
	}
	
	List testTask() {
		  [[title:"$ROUTE_NAME ($NOWIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME ($WIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME ($WIND+)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,iLandingDurationFormula:"wind+:1",iRisingDurationFormula:"wind+:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME ($WIND+) All",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"wind+:2NM",maxLandingDurationFormula:"wind:2NM",parkingDuration:5,iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME_NOITO ($WIND+) All",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"wind+:2NM",maxLandingDurationFormula:"wind:2NM",parkingDuration:5,iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME_NOILDG ($WIND+) All",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"wind+:2NM",maxLandingDurationFormula:"wind:2NM",parkingDuration:5,iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME_NOITOLDG ($WIND+) All",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"wind+:2NM",maxLandingDurationFormula:"wind:2NM",parkingDuration:5,iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			minNextFlightDuration:30,procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		  ]
	}
	
	List testTestLegPlanning60(String routeName, boolean withWind, boolean withiFullMinute) {
        List l
        if (routeName == ROUTE_NAME) {
        	if (!withWind) {
        		l = [
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
        		]
        	} else {
        		l = [
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:withiFullMinute,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:withiFullMinute,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
        			[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
        		]
        	}
        } else if (routeName == ROUTE_NAME_NOITO) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0444444444,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        } else if (routeName == ROUTE_NAME_NOILDG) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0444444444,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        } else if (routeName == ROUTE_NAME_NOITOLDG) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0666666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        }
        return l
	}
	
	List testTestLegPlanning90(String routeName, boolean withWind, boolean withiFullMinute) {
		List l
        if (routeName == ROUTE_NAME) {
            if (!withWind) {
    			l = [
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    			]
    		} else {
    			l = [
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:withiFullMinute,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:withiFullMinute,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    			]
    		}
        } else if (routeName == ROUTE_NAME_NOITO) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0266666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        } else if (routeName == ROUTE_NAME_NOILDG) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0266666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        } else if (routeName == ROUTE_NAME_NOITOLDG) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0400000000,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        }
		return l
	}
	
	List testTestLegPlanning120(String routeName, boolean withWind, boolean withiFullMinute) {
		List l
        if (routeName == ROUTE_NAME) {
            if (!withWind) {
    			l = [
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    			]
    		} else {
    			l = [
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:withiFullMinute,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:withiFullMinute,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,          planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
    			]
    		}
        } else if (routeName == ROUTE_NAME_NOITO) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0190476190,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        } else if (routeName == ROUTE_NAME_NOILDG) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0190476190,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        } else if (routeName == ROUTE_NAME_NOITOLDG) {
            if (!withWind) {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            } else {
                l = [
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0285714286,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:true],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0,noPlanningTest:false],
                ]
            }
        }
		return l
	}
	
	List testTestLegFlight60(String routeName, boolean withWind, boolean withiFullMinute, boolean withiAll) {
		List l
		if (routeName == ROUTE_NAME) {
	        if (!withWind) {
			    l = [
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:60,planLegTime:0.0166666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    ]
			} else {
				if (!withiFullMinute) {
				    l = [
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				    ]
				} else {
					if (!withiAll) {
					    l = [
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:true, planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:true, planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    ]
					} else {
					    l = [
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					    ]
					}
				}
			}
		} else if (routeName == ROUTE_NAME_NOITO) {
		    l = [
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0444444444,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
		    ]
		} else if (routeName == ROUTE_NAME_NOILDG) {
		    l = [
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0444444444,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
		    ]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
		    l = [
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0666666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			    [planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:45,planLegTime:0.0222222222,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
		    ]
		}
		return l
	}
	
	List testTestLegFlight90(String routeName, boolean withWind, boolean withiFullMinute, boolean withiAll) {
		List l
		if (routeName == ROUTE_NAME) {
	        if (!withWind) {
				l = [
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:90,planLegTime:0.0111111111,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				]
			} else {
				if (!withiFullMinute) {
					l = [
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					if (!withiAll) {
						l = [
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:true, planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:true, planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						]
					} else {
						l = [
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						]
					}
				}
			}
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0266666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0266666667,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0400000000,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:75,planLegTime:0.0133333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		}
		return l
	}
	
	List testTestLegFlight120(String routeName, boolean withWind, boolean withiFullMinute, boolean withiAll) {
		List l
		if (routeName == ROUTE_NAME) {
	        if (!withWind) {
				l = [
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:120,planLegTime:0.0083333333,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				]
			} else {
				if (!withiFullMinute) {
					l = [
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					if (!withiAll) {
						l = [
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:true, planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:true, planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						]
					} else {
						l = [
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
							[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
						]
					}
				}
			}
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0190476190,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:2,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0190476190,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:3,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0285714286,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
				[planTrueTrack:0,planTestDistance:1,planTrueHeading:0,planGroundSpeed:105,planLegTime:0.0095238095,planFullMinute:false,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		}
		return l
	}
	
	List testCoordResult60(String routeName, boolean withWind, boolean withiFullMinute, boolean withiAll) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME) {
	        if (!withWind) {
				l = [
					[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
					[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
					[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:11:00"],
					[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:00"],
					[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:13:00"],
					[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:14:00"],
					[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:15:00"],
					[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:16:00"],
					[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:17:00"],
					[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:27:00"],
				]
	        } else {
				if (!withiFullMinute) {
					l = [
						[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
						[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
						[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:11:20"],
						[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:40"],
						[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:14:00"],
						[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:15:20"],
						[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:16:40"],
						[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:18:00"],
						[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:19:20"],
						[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:29:20"],
					]
				} else {
					if (!withiAll) {
						l = [
							[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
							[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
							[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:11:20"],
							[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:40"],
							[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:14:00"],
							[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:15:20"],
							[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:17:00"],
							[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:18:20"],
							[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:19:40"],
							[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:29:40"],
						]
					} else {
						l = [
							[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
							[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:04:00"],
							[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:05:20"],
							[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:06:40"],
							[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:08:00"],
							[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:09:20"],
							[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:40"],
							[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:00"],
							[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:13:20"],
							[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:17:20"],
						]
					}
				}
	        }
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:04:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:05:20"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:06:40"],
				[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:08:00"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:40"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:00"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:13:20"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:17:20"],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:04:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:05:20"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:06:40"],
				[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:09:20"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:40"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:00"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:13:20"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:17:20"],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:04:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:05:20"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:06:40"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:10:40"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:12:00"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:13:20"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:17:20"],
			]
		}
		return l
	}
	
	List testCoordResult90(String routeName, boolean withWind, boolean withiFullMinute, boolean withiAll) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME) {
	        if (!withWind) {
				l = [
					[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
					[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
					[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:40:40"],
					[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:41:20"],
					[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:42:00"],
					[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:42:40"],
					[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:43:20"],
					[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:44:00"],
					[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:44:40"],
					[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:54:40"],
				]
	        } else {
				if (!withiFullMinute) {
					l = [
						[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
						[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
						[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:40:48"],
						[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:41:36"],
						[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:42:24"],
						[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:43:12"],
						[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:44:00"],
						[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:44:48"],
						[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:45:36"],
						[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:55:36"],
					]
				} else {
					if (!withiAll) {
						l = [
							[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
							[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
							[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:40:48"],
							[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:41:36"],
							[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:43:00"],
							[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:43:48"],
							[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:45:00"],
							[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:45:48"],
							[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:46:36"],
							[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:56:36"],
						]
					} else {
						l = [
							[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
							[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:00"],
							[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:48"],
							[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:34:36"],
							[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:35:24"],
							[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:36:12"],
							[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:00"],
							[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:48"],
							[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:38:36"],
							[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:41:00"],
						]
					}
				}
	        }
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:48"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:34:36"],
				[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:35:24"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:00"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:48"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:38:36"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:41:00"],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:48"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:34:36"],
				[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:36:12"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:00"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:48"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:38:36"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:41:00"],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:33:48"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:34:36"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:00"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:37:48"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"10:38:36"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"10:41:00"],
			]
		}
		return l
	}
	
	List testCoordResult120(String routeName, boolean withWind, boolean withiFullMinute, boolean withiAll) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME) {
	        if (!withWind) {
				l = [
					[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
					[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:10:30"],
					[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:11:00"],
					[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:11:30"],
					[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:12:00"],
					[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:12:30"],
					[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:13:00"],
					[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:13:30"],
					[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:23:30"],
				]
	        } else {
				if (!withiFullMinute) {
					l = [
						[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
						[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
						[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:10:34"],
						[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:11:08"],
						[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:11:42"],
						[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:12:16"],
						[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:12:50"],
						[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:13:24"],
						[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:13:58"],
						[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:23:58"],
					]
				} else {
					if (!withiAll) {
						l = [
							[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
							[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
							[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:10:34"],
							[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:11:08"],
							[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:12:00"],
							[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:12:34"],
							[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:14:00"],
							[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:14:34"],
							[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:15:08"],
							[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:25:08"],
						]
					} else {
						l = [
							[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
							[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:00"],
							[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:34"],
							[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:03:08"],
							[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:03:42"],
							[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:04:16"],
							[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:04:50"],
							[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:24"],
							[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:58"],
							[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:07:41"],
						]
					}
				}
	        }
		} else if (routeName == ROUTE_NAME_NOITO) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:34"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:03:08"],
				[mark:"iLDG",type:CoordType.iLDG,latDirection:N,latGrad:48,latMinute:11.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:03:42"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:04:51"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:25"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:59"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:07:42"],
			]
		} else if (routeName == ROUTE_NAME_NOILDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:34"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:03:08"],
				[mark:"iT/O",type:CoordType.iTO, latDirection:N,latGrad:48,latMinute:12.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:04:17"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:04:51"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:25"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:59"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:07:42"],
			]
		} else if (routeName == ROUTE_NAME_NOITOLDG) {
			l = [
				[mark:"T/O", type:CoordType.TO,  latDirection:N,latGrad:48,latMinute: 7.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
				[mark:"SP",  type:CoordType.SP,  latDirection:N,latGrad:48,latMinute: 8.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:00"],
				[mark:"CP1", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute: 9.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:02:34"],
				[mark:"iFP", type:CoordType.iFP, latDirection:N,latGrad:48,latMinute:10.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:03:08"],
				[mark:"iSP", type:CoordType.iSP, latDirection:N,latGrad:48,latMinute:13.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:04:51"],
				[mark:"CP2", type:CoordType.TP,  latDirection:N,latGrad:48,latMinute:14.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:25"],
				[mark:"FP",  type:CoordType.FP,  latDirection:N,latGrad:48,latMinute:15.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:500,planProcedureTurn:false,planCpTime:"11:05:59"],
				[mark:"LDG", type:CoordType.LDG, latDirection:N,latGrad:48,latMinute:16.0, lonDirection:E,lonGrad:9,lonMinute:45, altitude:0,  planProcedureTurn:false,planCpTime:"11:07:42"],
			]
		}
		return l
	}
}
