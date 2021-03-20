import java.util.List;
import java.util.Map;

class DemoContestCurvedService 
{
	def fcService
	
	static final String ROUTE_NAME = "Strecke 9"
	static final String ROUTE_NAME_CURVED = "Strecke 9 (Curved)"
	static final String ROUTE_NAME_NORMAL = "Strecke 9 (Normal)"
    static final String ROUTE_NAME_CURVED_GPX = "Strecke_9_Curved.gpx"
    static final String ROUTE_NAME_NORMAL_GPX = "Strecke_9_Normal.gpx"

	static final String CREW_60 = "Crew 60kt"
	static final String CREW_90 = "Crew 90kt"
	static final String CREW_120 = "Crew 120kt"
	
	static final String START_TIME = "08:45"
	
	static final BigDecimal WIND_DIRECTION = 270
	static final BigDecimal WIND_SPEED = 15
	
	static final String WIND = "$WIND_DIRECTION/$WIND_SPEED"
	static final String NOWIND = "0/0"
	
	long CreateTest(String testName, String printPrefix, boolean testExists)
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
		Map contest = fcService.putContest(testName,printPrefix,false,0,ContestRules.R11,"2020-08-01","Europe/Berlin",testExists)
		
		// Routes
		fcService.printstart ROUTE_NAME
        Map route_curved = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_CURVED_GPX)
        Map route_normal = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_NORMAL_GPX)
        
		fcService.printdone ""
		
		// Crews and Aircrafts
		fcService.importCrewList(contest, "FC-CrewList-Test.xlsx", false) // false - mit Start-Nr. 13
		
		// Task Curved
		Map task_curved = fcService.putTask(contest,"$ROUTE_NAME_CURVED ($NOWIND)",START_TIME,2,"time:10min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
		Map planningtest_curved = fcService.putPlanningTest(task_curved,"")
		Map planningtesttask_curved = fcService.putPlanningTestTask(planningtest_curved,"",route_curved,0,0)
		fcService.putplanningtesttaskTask(task_curved,planningtesttask_curved)
		Map flighttest_curved = fcService.putFlightTest(task_curved,"",route_curved)
		Map flighttestwind_curved = fcService.putFlightTestWind(flighttest_curved,0,0,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_curved,flighttestwind_curved)
		fcService.runcalculatetimetableTask(task_curved)
		
		// Task Normal
		Map task_normal = fcService.putTask(contest,"$ROUTE_NAME_NORMAL ($NOWIND)",START_TIME,2,"time:10min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
		Map planningtest_normal = fcService.putPlanningTest(task_normal,"")
		Map planningtesttask_normal = fcService.putPlanningTestTask(planningtest_normal,"",route_normal,0,0)
		fcService.putplanningtesttaskTask(task_normal,planningtesttask_normal)
		Map flighttest_normal = fcService.putFlightTest(task_normal,"",route_normal)
		Map flighttestwind_normal = fcService.putFlightTestWind(flighttest_normal,0,0,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_normal,flighttestwind_normal)
		fcService.runcalculatetimetableTask(task_normal)
		
		// Task Curved + Wind
		Map task_curved_wind = fcService.putTask(contest,"$ROUTE_NAME_CURVED ($WIND)",START_TIME,2,"time:10min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
		Map planningtest_curved_wind = fcService.putPlanningTest(task_curved_wind,"")
		Map planningtesttask_curved_wind = fcService.putPlanningTestTask(planningtest_curved_wind,"",route_curved,WIND_DIRECTION,WIND_SPEED)
		fcService.putplanningtesttaskTask(task_curved_wind,planningtesttask_curved_wind)
		Map flighttest_curved_wind = fcService.putFlightTest(task_curved_wind,"",route_curved)
		Map flighttestwind_curved_wind = fcService.putFlightTestWind(flighttest_curved_wind,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_curved_wind,flighttestwind_curved_wind)
		fcService.runcalculatetimetableTask(task_curved_wind)
		
		// Task Normal + Wind
		Map task_normal_wind = fcService.putTask(contest,"$ROUTE_NAME_NORMAL ($WIND)",START_TIME,2,"time:10min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false,false,false, false,false)
		Map planningtest_normal_wind = fcService.putPlanningTest(task_normal_wind,"")
		Map planningtesttask_normal_wind = fcService.putPlanningTestTask(planningtest_normal_wind,"",route_normal,WIND_DIRECTION,WIND_SPEED)
		fcService.putplanningtesttaskTask(task_normal_wind,planningtesttask_normal_wind)
		Map flighttest_normal_wind = fcService.putFlightTest(task_normal_wind,"",route_normal)
		Map flighttestwind_normal_wind = fcService.putFlightTestWind(flighttest_normal_wind,WIND_DIRECTION,WIND_SPEED,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_normal_wind,flighttestwind_normal_wind)
		fcService.runcalculatetimetableTask(task_normal_wind)
		
		fcService.printdone ""
		
		return contest.instance.id
	}
	
	Map RunTest(Contest lastContest, String contestName)
	{
		Map ret_test = [:]
		if (lastContest && lastContest.title == contestName) {
			fcService.printstart "runtest '$lastContest.title'"
			Route route_curved
			Route route_normal
			Route.findAllByContest(lastContest).eachWithIndex { Route r, int i ->
				switch (i) {
					case 0:
						route_curved = r
						break
					case 1:
						route_normal = r
						break
				}
			}
			Task task_curved
			Task task_normal
			Task task_curved_wind
			Task task_normal_wind
			Task.findAllByContest(lastContest).eachWithIndex { Task task, int i ->
				switch (i) {
					case 0:
						task_curved = task
						break
					case 1:
						task_normal = task
						break
					case 2:
						task_curved_wind = task
						break
					case 3:
						task_normal_wind = task
						break
				}

			}
			Map ret = fcService.testData(
			   [[name:"Route",count:2,table:Route.findAllByContest(lastContest,[sort:"id"]),data:testRoute()],
				[name:"CoordRoute '$ROUTE_NAME_CURVED'",   count:8,table:CoordRoute.findAllByRoute(   route_curved,[sort:"id"]),data:testCoordRoute(   ROUTE_NAME_CURVED)],
				[name:"CoordRoute '$ROUTE_NAME_NORMAL'",   count:8,table:CoordRoute.findAllByRoute(   route_normal,[sort:"id"]),data:testCoordRoute(   ROUTE_NAME_NORMAL)],
				[name:"RouteLegCoord '$ROUTE_NAME_CURVED'",count:7,table:RouteLegCoord.findAllByRoute(route_curved,[sort:"id"]),data:testRouteLegCoord(ROUTE_NAME_CURVED)],
				[name:"RouteLegCoord '$ROUTE_NAME_NORMAL'",count:7,table:RouteLegCoord.findAllByRoute(route_normal,[sort:"id"]),data:testRouteLegCoord(ROUTE_NAME_NORMAL)],
				[name:"RouteLegTest '$ROUTE_NAME_CURVED'", count:1,table:RouteLegTest.findAllByRoute( route_curved,[sort:"id"]),data:testRouteLegTest( ROUTE_NAME_CURVED)],
				[name:"RouteLegTest '$ROUTE_NAME_NORMAL'", count:5,table:RouteLegTest.findAllByRoute( route_normal,[sort:"id"]),data:testRouteLegTest( ROUTE_NAME_NORMAL)],
				
				[name:"Crew",count:3,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:testCrew()],
				[name:"Task",count:4,table:Task.findAllByContest(lastContest,[sort:"id"]),data:testTask()],
				
				[name:"TestLegPlanning '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_60'", count:1,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60( ROUTE_NAME_CURVED,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_90'", count:1,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90( ROUTE_NAME_CURVED,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_120'",count:1,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_CURVED,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_60'", count:5,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60( ROUTE_NAME_NORMAL,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_90'", count:5,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90( ROUTE_NAME_NORMAL,false,false)],
				[name:"TestLegPlanning '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_120'",count:5,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_NORMAL,false,false)],
				
				[name:"TestLegPlanning '$ROUTE_NAME_CURVED ($WIND) - $CREW_60'",   count:1,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60( ROUTE_NAME_CURVED,false,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_CURVED ($WIND) - $CREW_90'",   count:1,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90( ROUTE_NAME_CURVED,false,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_CURVED ($WIND) - $CREW_120'",  count:1,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_CURVED,false,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NORMAL ($WIND) - $CREW_60'",   count:5,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegPlanning60( ROUTE_NAME_NORMAL,false,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NORMAL ($WIND) - $CREW_90'",   count:5,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegPlanning90( ROUTE_NAME_NORMAL,false,true)],
				[name:"TestLegPlanning '$ROUTE_NAME_NORMAL ($WIND) - $CREW_120'",  count:5,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegPlanning120(ROUTE_NAME_NORMAL,false,true)],
				
				[name:"TestLegFlight '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_60'",   count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(   ROUTE_NAME_CURVED,false)],
				[name:"TestLegFlight '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_90'",   count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(   ROUTE_NAME_CURVED,false)],
				[name:"TestLegFlight '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_120'",  count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(  ROUTE_NAME_CURVED,false)],
				[name:"TestLegFlight '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_60'",   count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(   ROUTE_NAME_NORMAL,false)],
				[name:"TestLegFlight '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_90'",   count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(   ROUTE_NAME_NORMAL,false)],
				[name:"TestLegFlight '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_120'",  count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(  ROUTE_NAME_NORMAL,false)],
				
				[name:"TestLegFlight '$ROUTE_NAME_CURVED ($WIND) - $CREW_60'",     count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(   ROUTE_NAME_CURVED,true)],
				[name:"TestLegFlight '$ROUTE_NAME_CURVED ($WIND) - $CREW_90'",     count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(   ROUTE_NAME_CURVED,true)],
				[name:"TestLegFlight '$ROUTE_NAME_CURVED ($WIND) - $CREW_120'",    count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(  ROUTE_NAME_CURVED,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NORMAL ($WIND) - $CREW_60'",     count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(   ROUTE_NAME_NORMAL,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NORMAL ($WIND) - $CREW_90'",     count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testTestLegFlight90(   ROUTE_NAME_NORMAL,true)],
				[name:"TestLegFlight '$ROUTE_NAME_NORMAL ($WIND) - $CREW_120'",    count:5,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testTestLegFlight120(  ROUTE_NAME_NORMAL,true)],
				
				[name:"CoordResult '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_60'",     count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(     ROUTE_NAME_CURVED,false)],
				[name:"CoordResult '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_90'",     count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(     ROUTE_NAME_CURVED,false)],
				[name:"CoordResult '$ROUTE_NAME_CURVED ($NOWIND) - $CREW_120'",    count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_curved,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(    ROUTE_NAME_CURVED,false)],
				[name:"CoordResult '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_60'",     count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(     ROUTE_NAME_NORMAL,false)],
				[name:"CoordResult '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_90'",     count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(     ROUTE_NAME_NORMAL,false)],
				[name:"CoordResult '$ROUTE_NAME_NORMAL ($NOWIND) - $CREW_120'",    count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal,     Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(    ROUTE_NAME_NORMAL,false)],
				
				[name:"CoordResult '$ROUTE_NAME_CURVED ($WIND) - $CREW_60'",       count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(     ROUTE_NAME_CURVED,true)],
				[name:"CoordResult '$ROUTE_NAME_CURVED ($WIND) - $CREW_90'",       count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(     ROUTE_NAME_CURVED,true)],
				[name:"CoordResult '$ROUTE_NAME_CURVED ($WIND) - $CREW_120'",      count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_curved_wind,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(    ROUTE_NAME_CURVED,true)],
				[name:"CoordResult '$ROUTE_NAME_NORMAL ($WIND) - $CREW_60'",       count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(     ROUTE_NAME_NORMAL,true)],
				[name:"CoordResult '$ROUTE_NAME_NORMAL ($WIND) - $CREW_90'",       count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_90)), [sort:"id"]),data:testCoordResult90(     ROUTE_NAME_NORMAL,true)],
				[name:"CoordResult '$ROUTE_NAME_NORMAL ($WIND) - $CREW_120'",      count:8,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_normal_wind,Crew.findByContestAndName(lastContest,CREW_120)),[sort:"id"]),data:testCoordResult120(    ROUTE_NAME_NORMAL,true)],
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
		[[title:ROUTE_NAME_CURVED,mark:ROUTE_NAME],[title:ROUTE_NAME_NORMAL,mark:ROUTE_NAME]]
	}
	
	List testCoordRoute(String routeName) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME_CURVED) {
			l = [
				[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,  coordMeasureDistance:0,             measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,  coordMeasureDistance:92.5999999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,  coordMeasureDistance:92.6000000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.2,planProcedureTurn:false],
				[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90, coordMeasureDistance:185.1971440349,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.4,planProcedureTurn:false],
				[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90, coordMeasureDistance:277.7942879958,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.6,planProcedureTurn:false],
				[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90, coordMeasureDistance:370.3914320122,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.8,planProcedureTurn:false],
				[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,gatewidth2:1.0,coordTrueTrack:180,coordMeasureDistance:462.9914320308,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:180,coordMeasureDistance:92.5999999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
			]
		} else if (routeName == ROUTE_NAME_NORMAL) {
			l = [
				[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,  coordMeasureDistance:0,             measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,  coordMeasureDistance:92.5999999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,  coordMeasureDistance:92.6000000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90, coordMeasureDistance:92.5971440164, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:true],
				[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90, coordMeasureDistance:92.5971439609, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90, coordMeasureDistance:92.5971440164, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
				[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,gatewidth2:1.0,coordTrueTrack:180,coordMeasureDistance:92.6000000185, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:true],
				[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:180,coordMeasureDistance:92.5999999630, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,  planProcedureTurn:false],
			]
		}
		return l
	}
	
	List testRouteLegCoord(String routeName) {
		List l = [
			[coordTrueTrack:0,  coordDistance: 9.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
			[coordTrueTrack:0,  coordDistance:10.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
			[coordTrueTrack:90, coordDistance: 9.9996915784, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:0],
			[coordTrueTrack:90, coordDistance: 9.9996915724, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
			[coordTrueTrack:90, coordDistance: 9.9996915784, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
			[coordTrueTrack:180,coordDistance:10.0000000020, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
			[coordTrueTrack:180,coordDistance: 9.9999999960, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:180],
		]
		return l
	}
	
	List testRouteLegTest(String routeName) {
		List l
		if (routeName == ROUTE_NAME_CURVED) {
			l = [
				[coordTrueTrack:  0,coordDistance:50,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
			]
		} else if (routeName == ROUTE_NAME_NORMAL) {
			l = [
				[coordTrueTrack:  0,coordDistance:10,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:null],
				[coordTrueTrack: 90,coordDistance:10,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:0],
				[coordTrueTrack: 90,coordDistance:10,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:90],
				[coordTrueTrack: 90,coordDistance:10,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:90],
				[coordTrueTrack:180,coordDistance:10,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,turnTrueTrack:90],
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
		  [[title:"$ROUTE_NAME_CURVED ($NOWIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
			procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME_NORMAL ($NOWIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
			procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME_CURVED ($WIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
			procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		   [title:"$ROUTE_NAME_NORMAL ($WIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"time:10min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
			procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		  ]
	}
	
	List testTestLegPlanning60(String routeName, boolean isFlight, boolean withWind) {
		List l
		if (routeName == ROUTE_NAME_CURVED) {
			if (!isFlight) {
				if (!withWind) {
					l = [
					  [planTrueTrack:  0,planTestDistance:50,planTrueHeading:  0,planGroundSpeed:60.0,planLegTime:0.8333333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					l = [
					  [planTrueTrack:  0,planTestDistance:50,planTrueHeading:345.5224878141,planGroundSpeed:58.0947501931,planLegTime:0.8606629658,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				}
			} else {
				if (!withWind) {
					l = [
					  [planTrueTrack:  0,planTestDistance:10,planTrueHeading:  0,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack:180,planTestDistance:10,planTrueHeading:180,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					l = [
					  [planTrueTrack:  0,planTestDistance:10,planTrueHeading:345.5224878141,planGroundSpeed:58.0947501931,planLegTime:0.1721325932,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:75.0,         planLegTime:0.1333333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:75.0,         planLegTime:0.1333333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:75.0,         planLegTime:0.1333333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					  [planTrueTrack:180,planTestDistance:10,planTrueHeading:194.4775121859,planGroundSpeed:58.0947501931,planLegTime:0.1721325932,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				}
			}
		} else if (routeName == ROUTE_NAME_NORMAL) {
			if (!withWind) {
				l = [
				  [planTrueTrack:  0,planTestDistance:10,planTrueHeading:  0,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
				  [planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:true, planProcedureTurnDuration:1],
				  [planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
				  [planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
				  [planTrueTrack:180,planTestDistance:10,planTrueHeading:180,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:true, planProcedureTurnDuration:1],
				]
			} else {
				l = [
				  [planTrueTrack:  0,planTestDistance:10,planTrueHeading:345.5224878141,planGroundSpeed:58.0947501931,planLegTime:0.1721325932,planProcedureTurn:false,planProcedureTurnDuration:0],
				  [planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:75.0,         planLegTime:0.1333333333,planProcedureTurn:true, planProcedureTurnDuration:1],
				  [planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:75.0,         planLegTime:0.1333333333,planProcedureTurn:false,planProcedureTurnDuration:0],
				  [planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:75.0,         planLegTime:0.1333333333,planProcedureTurn:false,planProcedureTurnDuration:0],
				  [planTrueTrack:180,planTestDistance:10,planTrueHeading:194.4775121859,planGroundSpeed:58.0947501931,planLegTime:0.1721325932,planProcedureTurn:true, planProcedureTurnDuration:1],
				]
			}
		}
		return l
	}
	
	List testTestLegFlight60(String routeName, boolean withWind) {
		return testTestLegPlanning60(routeName,true,withWind)
	}
	
	List testCoordResult60(String routeName, boolean withWind) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME_CURVED) {
			if (!withWind) {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
					[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:20:00"],
					[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:false,planCpTime:"10:30:00"],
					[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
					[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"10:50:00"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:10:00"],
				]
			} else {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
					[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:20:20"],
					[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:false,planCpTime:"10:28:20"],
					[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"10:36:20"],
					[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"10:44:20"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"10:54:40"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:04:40"],
				]
			}
		} else if (routeName == ROUTE_NAME_NORMAL) {
			if (!withWind) {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
					[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:20:00"],
					[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:true, planCpTime:"10:31:00"],
					[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"10:41:00"],
					[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"10:51:00"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:true, planCpTime:"11:02:00"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:12:00"],
				]
			} else {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:10:00"],
					[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:20:20"],
					[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:true, planCpTime:"10:29:20"],
					[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"10:37:20"],
					[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"10:45:20"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:true, planCpTime:"10:56:40"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:06:40"],
				]
			}
		}
		return l
	}
	
	List testTestLegPlanning90(String routeName, boolean isFlight, boolean withWind) {
		List l
		if (routeName == ROUTE_NAME_CURVED) {
			if (!isFlight) {
				if (!withWind) {
					l = [
						[planTrueTrack:  0,planTestDistance:50,planTrueHeading:  0,planGroundSpeed:90.0,planLegTime:0.5555555556,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					l = [
						[planTrueTrack:  0,planTestDistance:50,planTrueHeading:350.4059317712,planGroundSpeed:88.7411967460,planLegTime:0.5634361698,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				}
			} else {
				if (!withWind) {
					l = [
						[planTrueTrack:  0,planTestDistance:10,planTrueHeading:  0,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:180,planTestDistance:10,planTrueHeading:180,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					l = [
						[planTrueTrack:  0,planTestDistance:10,planTrueHeading:350.4059317712,planGroundSpeed:88.7411967460,planLegTime:0.1126872340,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:105.0,        planLegTime:0.0952380952,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:105.0,        planLegTime:0.0952380952,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:105.0,        planLegTime:0.0952380952,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:180,planTestDistance:10,planTrueHeading:189.5940682288,planGroundSpeed:88.7411967460,planLegTime:0.1126872340,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				}
			}
		} else if (routeName == ROUTE_NAME_NORMAL) {
			if (!withWind) {
				l = [
					[planTrueTrack:  0,planTestDistance:10,planTrueHeading:  0,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:true, planProcedureTurnDuration:1],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:180,planTestDistance:10,planTrueHeading:180,planGroundSpeed:90.0,planLegTime:0.1111111111,planProcedureTurn:true, planProcedureTurnDuration:1],
				]
			} else {
				l = [
					[planTrueTrack:  0,planTestDistance:10,planTrueHeading:350.4059317712,planGroundSpeed:88.7411967460,planLegTime:0.1126872340,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:105.0,        planLegTime:0.0952380952,planProcedureTurn:true, planProcedureTurnDuration:1],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:105.0,        planLegTime:0.0952380952,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:105.0,        planLegTime:0.0952380952,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:180,planTestDistance:10,planTrueHeading:189.5940682288,planGroundSpeed:88.7411967460,planLegTime:0.1126872340,planProcedureTurn:true, planProcedureTurnDuration:1],
				]
			}
		}
		return l
	}
	
	List testTestLegFlight90(String routeName, boolean withWind) {
		return testTestLegPlanning90(routeName,true,withWind)
	}
	
	List testCoordResult90(String routeName, boolean withWind) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME_CURVED) {
			if (!withWind) {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
					[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:46:40"],
					[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:false,planCpTime:"10:53:20"],
					[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:06:40"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:13:20"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:23:20"],
				]
			} else {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
					[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:46:46"],
					[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:false,planCpTime:"10:52:29"],
					[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"10:58:12"],
					[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:03:55"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:10:41"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:20:41"],
				]
			}
		} else if (routeName == ROUTE_NAME_NORMAL) {
			if (!withWind) {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
					[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:46:40"],
					[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:true, planCpTime:"10:54:20"],
					[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"11:01:00"],
					[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:07:40"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:true, planCpTime:"11:15:20"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:25:20"],
				]
			} else {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"10:30:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:40:00"],
					[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"10:46:46"],
					[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:true, planCpTime:"10:53:29"],
					[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"10:59:12"],
					[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:04:55"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:true, planCpTime:"11:12:41"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:22:41"],
				]
			}
		}
		return l
	}
	
	List testTestLegPlanning120(String routeName, boolean isFlight, boolean withWind) {
		List l
		if (routeName == ROUTE_NAME_CURVED) {
			if (!isFlight) {
				if (!withWind) {
					l = [
						[planTrueTrack:  0,planTestDistance:50,planTrueHeading:  0,planGroundSpeed:120.0,planLegTime:0.4166666667,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					l = [
						[planTrueTrack:  0,planTestDistance:50,planTrueHeading:352.8192442185,planGroundSpeed:119.0588089979,planLegTime:0.4199605256,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				}
			} else {
				if (!withWind) {
					l = [
						[planTrueTrack:  0,planTestDistance:10,planTrueHeading:  0,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:180,planTestDistance:10,planTrueHeading:180,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				} else {
					l = [
						[planTrueTrack:  0,planTestDistance:10,planTrueHeading:352.8192442185,planGroundSpeed:119.0588089979,planLegTime:0.0839921051,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:135.0,         planLegTime:0.0740740741,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:135.0,         planLegTime:0.0740740741,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:135.0,         planLegTime:0.0740740741,planProcedureTurn:false,planProcedureTurnDuration:0],
						[planTrueTrack:180,planTestDistance:10,planTrueHeading:187.1807557815,planGroundSpeed:119.0588089979,planLegTime:0.0839921051,planProcedureTurn:false,planProcedureTurnDuration:0],
					]
				}
			}
		} else if (routeName == ROUTE_NAME_NORMAL) {
			if (!withWind) {
				l = [
					[planTrueTrack:  0,planTestDistance:10,planTrueHeading:  0,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:true, planProcedureTurnDuration:1],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading: 90,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:180,planTestDistance:10,planTrueHeading:180,planGroundSpeed:120.0,planLegTime:0.0833333333,planProcedureTurn:true, planProcedureTurnDuration:1],
				]
			} else {
				l = [
					[planTrueTrack:  0,planTestDistance:10,planTrueHeading:352.8192442185,planGroundSpeed:119.0588089979,planLegTime:0.0839921051,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:135.0,         planLegTime:0.0740740741,planProcedureTurn:true, planProcedureTurnDuration:1],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:135.0,         planLegTime:0.0740740741,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack: 90,planTestDistance:10,planTrueHeading:90,            planGroundSpeed:135.0,         planLegTime:0.0740740741,planProcedureTurn:false,planProcedureTurnDuration:0],
					[planTrueTrack:180,planTestDistance:10,planTrueHeading:187.1807557815,planGroundSpeed:119.0588089979,planLegTime:0.0839921051,planProcedureTurn:true, planProcedureTurnDuration:1],
				]
			}
		}
		return l
	}
	
	List testTestLegFlight120(String routeName, boolean withWind) {
		return testTestLegPlanning120(routeName,true,withWind)
	}
	
	List testCoordResult120(String routeName, boolean withWind) {
		List l
        String N = CoordPresentation.NORTH
        String E = CoordPresentation.EAST
		if (routeName == ROUTE_NAME_CURVED) {
			if (!withWind) {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
					[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:15:00"],
					[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:false,planCpTime:"11:20:00"],
					[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"11:25:00"],
					[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:30:00"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:35:00"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:45:00"],
				]
			} else {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
					[mark:"CP", type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:15:02"],
					[mark:"CP1",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:false,planCpTime:"11:19:29"],
					[mark:"CP2",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"11:23:56"],
					[mark:"CP3",type:CoordType.SECRET,latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:28:23"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:33:25"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:43:25"],
				]
			}
		} else if (routeName == ROUTE_NAME_NORMAL) {
			if (!withWind) {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
					[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:15:00"],
					[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:true, planCpTime:"11:21:00"],
					[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"11:26:00"],
					[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:31:00"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:true, planCpTime:"11:37:00"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:47:00"],
				]
			} else {
				l = [
					[mark:"T/O",type:CoordType.TO,    latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:00:00"],
					[mark:"SP", type:CoordType.SP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:10:00"],
					[mark:"CP", type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:45.0, altitude:500,planProcedureTurn:false,planCpTime:"11:15:02"],
					[mark:"CP1",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:9,  lonMinute:55.0, altitude:500,planProcedureTurn:true, planCpTime:"11:20:29"],
					[mark:"CP2",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute: 5.0, altitude:500,planProcedureTurn:false,planCpTime:"11:24:56"],
					[mark:"CP3",type:CoordType.TP,    latDirection:N,latGrad:0,latMinute:27.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:false,planCpTime:"11:29:23"],
					[mark:"FP", type:CoordType.FP,    latDirection:N,latGrad:0,latMinute:17.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:500,planProcedureTurn:true, planCpTime:"11:35:25"],
					[mark:"LDG",type:CoordType.LDG,   latDirection:N,latGrad:0,latMinute: 7.0, lonDirection:E,lonGrad:10, lonMinute:15.0, altitude:0,  planProcedureTurn:false,planCpTime:"11:45:25"],
				]
			}
		}
		return l
	}

}
