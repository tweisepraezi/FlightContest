import java.util.List;
import java.util.Map;

class DemoContestProcedureTurnService 
{
	def fcService
	
	static final String ROUTE_NAME = "Strecke 10"
	static final String ROUTE_NAME_PT = "Strecke 10 (Procedure Turn)"
    static final String ROUTE_NAME_PT_GPX = "Strecke_10_ProcedureTurn.gpx"
    
	static final String CREW_60 = "Crew 60kt"
	static final String AIRCRAFT_60 = "D-60"
	
	static final String START_TIME = "08:45"
	
	static final String NOWIND = "0/0"
	
	long CreateTest(String testName, String printPrefix, boolean testExists, boolean aflosDB)
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
		Map contest = fcService.putContest(testName,printPrefix,200000,false,0,ContestRules.R1,aflosDB,testExists)
		
		// Routes
        Map route_pt = [:]
		fcService.printstart ROUTE_NAME
        if (aflosDB) {
            route_pt = fcService.importAflosRoute(contest,ROUTE_NAME,ROUTE_NAME_PT,SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        } else {
            route_pt = fcService.importDemoFcRoute(RouteFileTools.GPX_EXTENSION, contest.instance, ROUTE_NAME_PT_GPX)
        }
		fcService.printdone ""
		
		// Crews and Aircrafts
        fcService.putCrew(contest,1,CREW_60,"","","",AIRCRAFT_60,"","",60)
		
		// Task Procdure Turn
		Map task_pt = fcService.putTask(contest,"$ROUTE_NAME_PT ($NOWIND)",START_TIME,2,"wind:1","wind:1",5,"wind:1","wind:1",true,true,false,false,false, false,true, true,true,true, false,false,false,false, false)
		Map flighttest_pt = fcService.putFlightTest(task_pt,"",route_pt)
		Map flighttestwind_pt = fcService.putFlightTestWind(flighttest_pt,0,0,0,0,0,0,0,0,0,0,0)
		fcService.putflighttestwindTask(task_pt,flighttestwind_pt)
		fcService.runcalculatetimetableTask(task_pt)
		
		fcService.printdone ""
		
		return contest.instance.id
	}
	
	Map RunTest(Contest lastContest, String contestName, boolean aflosDB)
	{
		Map ret_test = [:]
		if (lastContest && lastContest.title == contestName) {
			fcService.printstart "runtest '$lastContest.title'"
			Route route_pt
			Route.findAllByContest(lastContest).eachWithIndex { Route r, int i ->
				switch (i) {
					case 0:
						route_pt = r
						break
				}
			}
			Task task_pt
			Task.findAllByContest(lastContest).eachWithIndex { Task task, int i ->
				switch (i) {
					case 0:
						task_pt = task
						break
				}

			}
			Map ret = fcService.testData(
			   [[name:"Route",count:1,table:Route.findAllByContest(lastContest,[sort:"id"]),data:testRoute()],
				[name:"CoordRoute '$ROUTE_NAME_PT'",   count:11,table:CoordRoute.findAllByRoute(   route_pt,[sort:"id"]),data:testCoordRoute(   ROUTE_NAME_PT)],
				[name:"RouteLegCoord '$ROUTE_NAME_PT'",count:10,table:RouteLegCoord.findAllByRoute(route_pt,[sort:"id"]),data:testRouteLegCoord(ROUTE_NAME_PT)],
				[name:"RouteLegTest '$ROUTE_NAME_PT'", count:7,table:RouteLegTest.findAllByRoute( route_pt,[sort:"id"]),data:testRouteLegTest( ROUTE_NAME_PT)],
				
				[name:"Crew",count:1,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:testCrew()],
				[name:"Task",count:1,table:Task.findAllByContest(lastContest,[sort:"id"]),data:testTask()],
				
				[name:"TestLegFlight '$ROUTE_NAME_PT ($NOWIND) - $CREW_60'",   count:8,table:TestLegFlight.findAllByTest(  Test.findByTaskAndCrew(task_pt,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testTestLegFlight60(   ROUTE_NAME_PT,false)],
				
				[name:"CoordResult '$ROUTE_NAME_PT ($NOWIND) - $CREW_60'",     count:11,table:CoordResult.findAllByTest(    Test.findByTaskAndCrew(task_pt,     Crew.findByContestAndName(lastContest,CREW_60)), [sort:"id"]),data:testCoordResult60(     ROUTE_NAME_PT,false)],
				
			   ],aflosDB
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
		[[title:ROUTE_NAME_PT,mark:ROUTE_NAME]]
	}
	
	List testCoordRoute(String routeName) {
		List l
		if (routeName == ROUTE_NAME_PT) {
			l = [
				[mark:"T/O", type:CoordType.TO,    latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute: 5.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,             coordMeasureDistance:0,             measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"SP",  type:CoordType.SP,    latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:15.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90.0000000000, coordMeasureDistance:92.5998080504, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP1", type:CoordType.TP,    latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:14.0, altitude:500,gatewidth2:1.0,coordTrueTrack:354.2894414098,coordMeasureDistance:93.0618426031, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"iFP", type:CoordType.iFP,   latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:26.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90.0000000000, coordMeasureDistance:111.1186413380,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"iLDG",type:CoordType.iLDG,  latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:25.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:185.7105585561,coordMeasureDistance:93.0618425975, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"iT/O",type:CoordType.iTO,   latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:35.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:90.0000000000, coordMeasureDistance:92.5998079948, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"iSP", type:CoordType.iSP,   latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:34.0, altitude:500,gatewidth2:1.0,coordTrueTrack:354.2894414439,coordMeasureDistance:93.0618425975, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"CP2", type:CoordType.TP,    latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:46.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90.0000000000, coordMeasureDistance:111.1186413380,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"CP3", type:CoordType.SECRET,latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:185.7105585902,coordMeasureDistance:93.0618426031, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0.5012468828,planProcedureTurn:true],
				[mark:"FP",  type:CoordType.FP,    latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:55.0, altitude:500,gatewidth2:1.0,coordTrueTrack:90.0000000000, coordMeasureDistance:185.6616506535,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"LDG", type:CoordType.LDG,   latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:54.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:354.2894414098,coordMeasureDistance:93.0618426031, measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
			]
		}
		return l
	}
	
	List testRouteLegCoord(String routeName) {
		List l
		if (routeName == ROUTE_NAME_PT) {
			l = [
				[coordTrueTrack:90.0000000000, coordDistance: 9.9999792711, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
				[coordTrueTrack:354.2894414098,coordDistance:10.0498750111, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
				[coordTrueTrack:90.0000000000, coordDistance:11.9998532762, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:354],
				[coordTrueTrack:185.7105585561,coordDistance:10.0498750105, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
				[coordTrueTrack:90.0000000000, coordDistance: 9.9999792651, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:186],
				[coordTrueTrack:354.2894414439,coordDistance:10.0498750105, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
				[coordTrueTrack:90.0000000000, coordDistance:11.9998532762, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:354],
				[coordTrueTrack:185.7105585902,coordDistance:10.0498750111, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
				[coordTrueTrack:90.0000000000, coordDistance: 9.9999792711, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:186],
				[coordTrueTrack:354.2894414098,coordDistance:10.0498750111, measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
			]
		}
		return l
	}
	
	List testRouteLegTest(String routeName) {
		List l
		if (routeName == ROUTE_NAME_PT) {
			l = [
				[coordTrueTrack:354.2894414098,coordDistance:10.05,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:null],
				[coordTrueTrack:90.0000000000, coordDistance:12.00,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:354],
				[coordTrueTrack:185.7105585561,coordDistance:10.05,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
				[coordTrueTrack:90.0000000000, coordDistance:10.00,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:186],
				[coordTrueTrack:354.2894414439,coordDistance:10.05,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
				[coordTrueTrack:90.0000000000, coordDistance:12.00,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:354],
				[coordTrueTrack:185.7105585902,coordDistance:20.05,measureDistance:null,legMeasureDistance:null,legDistance:null,measureTrueTrack:null,turnTrueTrack:90],
			]
		}
		return l
	}
	
	List testCrew() {
		 [[startNum:1,name:CREW_60,mark:"",team:null,tas:60,contestPenalties:0,contestPosition:0,noContestPosition:false,aircraft:[registration:AIRCRAFT_60,type:"",colour:""]],
		 ]
	}
	
	List testTask() {
		  [[title:"$ROUTE_NAME_PT ($NOWIND)",firstTime:START_TIME,takeoffIntervalNormal:2,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
			preparationDuration:15,risingDurationFormula:"wind:1",maxLandingDurationFormula:"wind:1",parkingDuration:5,minNextFlightDuration:30,
			iLandingDurationFormula:"wind:1",iRisingDurationFormula:"wind:1",
			procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
		  ]
	}
	
	List testTestLegFlight60(String routeName, boolean withWind) {
		List l
		if (routeName == ROUTE_NAME_PT) {
			l = [
			  [planTrueTrack:354,planTestDistance:10.05,planTrueHeading:354,planGroundSpeed:60.0,planLegTime:0.1675000000,planProcedureTurn:false,planProcedureTurnDuration:0],
			  [planTrueTrack: 90,planTestDistance:12.00,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.2000000000,planProcedureTurn:true, planProcedureTurnDuration:1],
			  [planTrueTrack:186,planTestDistance:10.05,planTrueHeading:186,planGroundSpeed:60.0,planLegTime:0.1675000000,planProcedureTurn:false,planProcedureTurnDuration:0],
			  [planTrueTrack: 90,planTestDistance:10.00,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
			  [planTrueTrack:354,planTestDistance:10.05,planTrueHeading:354,planGroundSpeed:60.0,planLegTime:0.1675000000,planProcedureTurn:false,planProcedureTurnDuration:0],
			  [planTrueTrack: 90,planTestDistance:12.00,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.2000000000,planProcedureTurn:false,planProcedureTurnDuration:0],
			  [planTrueTrack:186,planTestDistance:10.05,planTrueHeading:186,planGroundSpeed:60.0,planLegTime:0.1675000000,planProcedureTurn:true, planProcedureTurnDuration:1],
			  [planTrueTrack: 90,planTestDistance:10.00,planTrueHeading: 90,planGroundSpeed:60.0,planLegTime:0.1666666667,planProcedureTurn:false,planProcedureTurnDuration:0],
			]
		}
		return l
	}
	
	List testCoordResult60(String routeName, boolean withWind) {
		List l
		if (routeName == ROUTE_NAME_PT) {
			l = [
				[mark:"T/O", type:CoordType.TO,    latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute: 5.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"SP",  type:CoordType.SP,    latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:15.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP1", type:CoordType.TP,    latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:14.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iFP", type:CoordType.iFP,   latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:26.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"iLDG",type:CoordType.iLDG,  latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:25.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iT/O",type:CoordType.iTO,   latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:35.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"iSP", type:CoordType.iSP,   latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:34.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP2", type:CoordType.TP,    latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:46.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"CP3", type:CoordType.SECRET,latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:45.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:true],
				[mark:"FP",  type:CoordType.FP,    latDirection:'N',latGrad:0,latMinute: 7.0, lonDirection:'E',lonGrad:9,lonMinute:55.0, altitude:500,gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
				[mark:"LDG", type:CoordType.LDG,   latDirection:'N',latGrad:0,latMinute:17.0, lonDirection:'E',lonGrad:9,lonMinute:54.0, altitude:0,  gatewidth2:1.0,coordTrueTrack:0,coordMeasureDistance:0,measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
			]
		}
		return l
	}
	

}
