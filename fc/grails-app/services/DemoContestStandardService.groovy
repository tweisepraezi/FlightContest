import java.util.Map;

class DemoContestStandardService
{
	def fcService
    def evaluationService
    
	long CreateTest1(String testName, String printPrefix, boolean testExists, boolean aflosDB)
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
		Map contest = fcService.putContest(testName,printPrefix,200000,false,2,ContestRules.R1,aflosDB,testExists)

		// Teams
		Map team1 = fcService.putTeam(contest,"Deutschland")
		Map team2 = fcService.putTeam(contest,"Schweiz")
		
		// Crews and Aircrafts
		Map crew1 = fcService.putCrew(contest,3, "Besatzung 3", "crew3.fc@localhost", "Deutschland","K1","D-EAAA","","",85)
		Map crew2 = fcService.putCrew(contest,18,"Besatzung 18","crew18.fc@localhost","Deutschland","K1","D-EAAD","","",80)
		Map crew3 = fcService.putCrew(contest,19,"Besatzung 19","crew19.fc@localhost","Deutschland","K2","D-EAAE","","",80)
		Map crew4 = fcService.putCrew(contest,11,"Besatzung 11","crew11.fc@localhost","Schweiz","K2","D-EAAB","","",70)
		Map crew5 = fcService.putCrew(contest,13,"Besatzung 13","crew13.fc@localhost","Schweiz","K2","D-EAAC","","",70)
		
		// Route
        Map route1 = [:]
        fcService.printstart "Route"
        if (aflosDB) {
            route1 = fcService.importAflosRoute(contest,"Strecke 1","Strecke 1",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        } else {
            route1 = fcService.importFileRoute(RouteFileTools.GPX_EXTENSION, contest.instance, "Strecke_1.gpx")
        }
        fcService.printdone ""
        
        // Task
        Map task1 = fcService.putTask(contest,"","09:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false, false,true, true,true,true, true,true,true,true, false)
        
		// Planning Test
		Map planningtest1 = fcService.putPlanningTest(task1,"")
		Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
		fcService.putplanningtesttaskTask(task1,planningtesttask1)

		// Flight Test
		Map flighttest1 = fcService.putFlightTest(task1,"",route1)
		Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15,274,0,0,260,-0.05,0,0,0,0)
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
												 givenValues:[[trueHeading: 98.1,  legTime:"00:14:06"],
															  [trueHeading:205.12, legTime:"00:12:41"],
															  [trueHeading:154.123,legTime:"00:12:03"],
															  [trueHeading: 95,    legTime:"00:09:56"],
															  [trueHeading:223.995,legTime:"00:07:43"],
															  [trueHeading:232,    legTime:"00:04:25"]],
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
												 givenValues:[[trueHeading: 98,   legTime:"00:15:11"],
															  [trueHeading:203,   legTime:"00:13:35"],
															  [trueHeading:154,   legTime:"00:13:04"],
															  [trueHeading:101.75,legTime:"00:10:57"],
															  [trueHeading:220.5, legTime:"00:08:19"],
															  [trueHeading:230,   legTime:"00:04:42"]],
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
                                                  gac:"Crew_3.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew4,
												  startNum:11,
                                                  gac:"Crew_11.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew5,
												  startNum:13,
                                                  gac:"Crew_13.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew3,
												  startNum:19,
                                                  gac:"Crew_19.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew2,
												  startNum:18,
                                                  gac:"Crew_18.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												],aflosDB)
		fcService.putobservationresultsTask(task1, [
													[crew:crew1,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
													[crew:crew2,routePhotos:0,turnPointPhotos:0,groundTargets:10,testComplete:true],
													[crew:crew3,routePhotos:120,turnPointPhotos:0,groundTargets:10,testComplete:true],
													[crew:crew4,routePhotos:0,turnPointPhotos:0,groundTargets:0,testComplete:true],
													[crew:crew5,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
												   ])
		fcService.putlandingresultsTask(task1, [
												[crew:crew1,
												 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:true,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest1NotAllowedAerodynamicAuxiliaries:false,
												 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,
												 landingTest2NotAllowedAerodynamicAuxiliaries:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3NotAllowedAerodynamicAuxiliaries:false,landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4NotAllowedAerodynamicAuxiliaries:false,landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew2,
												 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest1NotAllowedAerodynamicAuxiliaries:false,
												 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,
												 landingTest2NotAllowedAerodynamicAuxiliaries:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'F',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3NotAllowedAerodynamicAuxiliaries:false,landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4NotAllowedAerodynamicAuxiliaries:false,landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew3,
												 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest1NotAllowedAerodynamicAuxiliaries:false,
												 landingTest2Measure:'A',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,
												 landingTest2NotAllowedAerodynamicAuxiliaries:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3NotAllowedAerodynamicAuxiliaries:false,landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4NotAllowedAerodynamicAuxiliaries:false,landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew4,
												 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest1NotAllowedAerodynamicAuxiliaries:false,
												 landingTest2Measure:'F',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,
												 landingTest2NotAllowedAerodynamicAuxiliaries:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'B',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3NotAllowedAerodynamicAuxiliaries:false,landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4NotAllowedAerodynamicAuxiliaries:false,landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew5,
												 landingTest1Measure:'E',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest1NotAllowedAerodynamicAuxiliaries:false,
												 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,
												 landingTest2NotAllowedAerodynamicAuxiliaries:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3NotAllowedAerodynamicAuxiliaries:false,landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4NotAllowedAerodynamicAuxiliaries:false,landingTest4TouchingObstacle:false,
												 testComplete:true],
											   ])
		evaluationService.runcalculatepositionsTask(task1)
		evaluationService.runcalculatecontestpositionsContest(contest,[],[task1],[team1,team2])
		evaluationService.runcalculateteampositionsContest(contest,[],[task1])

		fcService.printdone ""
		
		return contest.instance.id
	}

	long CreateTest2(String testName, String printPrefix, boolean testExists, boolean aflosDB)
	{
		fcService.printstart "Create test contest '$testName'"
		
		// Contest
		Map contest = fcService.putContest(testName,printPrefix,200000,true,2,ContestRules.R1,aflosDB,testExists)

		// Classes with properties
		Map resultclass1 = fcService.putResultClass(contest,"Pr\u00E4zi","Pr\u00E4zisionsflugmeisterschaft",ContestRules.R1)
		Map resultclass2 = fcService.putResultClass(contest,"Tourist","",ContestRules.R1)
		
		// Teams
		Map team1 = fcService.putTeam(contest,"Deutschland")
		Map team2 = fcService.putTeam(contest,"Schweiz")
		
		// Crews with Teams, ResultClasses and Aircrafts
		Map crew1 = fcService.putCrew(contest,3, "Besatzung 3", "crew3.fc@localhost", "Deutschland","Pr\u00E4zi","D-EAAA","","",85)
		Map crew2 = fcService.putCrew(contest,18,"Besatzung 18","crew18.fc@localhost","Deutschland","Tourist","D-EAAD","","",80)
		Map crew3 = fcService.putCrew(contest,19,"Besatzung 19","crew19.fc@localhost","Deutschland","Pr\u00E4zi","D-EAAE","","",80)
		Map crew4 = fcService.putCrew(contest,11,"Besatzung 11","crew11.fc@localhost","Schweiz","Tourist","D-EAAB","","",70)
		Map crew5 = fcService.putCrew(contest,13,"Besatzung 13","crew13.fc@localhost","Schweiz","Pr\u00E4zi","D-EAAC","","",70)
		
		// additional team
		Map team3 = fcService.putTeam(contest,'Polen')
		
        // Task
        Map task1 = fcService.putTask(contest,"","09:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false, false,true, true,true,true, false,false,false,false, false)
        
		// TaskClass properties
		fcService.puttaskclassTask(task1,resultclass1,true,true,false,false,false, false,true, true,true,true, true,true,true,true)
		fcService.puttaskclassTask(task1,resultclass2,false,false,true,true,true, true,false, true,true,true, true,true,true,true)

		// additional class
		Map resultclass3 = fcService.putResultClass(contest,"Observer","",ContestRules.R1)
		fcService.puttaskclassTask(task1,resultclass3,true,true,true,true,false, false,true, true,true,true, false,false,false,false)
		
		// Route
        fcService.printstart "Route"
        Map route1 = [:]
        if (aflosDB) {
            route1 = fcService.importAflosRoute(contest,"Strecke 1","Strecke 1",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        } else {
            route1 = fcService.importFileRoute(RouteFileTools.GPX_EXTENSION, contest.instance, "Strecke_1.gpx")
        }
        fcService.printdone ""
        
		// Planning Test
		Map planningtest1 = fcService.putPlanningTest(task1,"")
		Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
		fcService.putplanningtesttaskTask(task1,planningtesttask1)
		
		// Flight Test
		Map flighttest1 = fcService.putFlightTest(task1,"",route1)
		Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15,274,0,0,260,-0.05,0,0,0,0)
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
												 givenValues:[[trueHeading: 98.1,  legTime:"00:14:06"],
															  [trueHeading:205.12, legTime:"00:12:41"],
															  [trueHeading:154.123,legTime:"00:12:03"],
															  [trueHeading: 95,    legTime:"00:09:56"],
															  [trueHeading:223.995,legTime:"00:07:43"],
															  [trueHeading:232,    legTime:"00:04:25"]],
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
												 givenValues:[[trueHeading: 98,   legTime:"00:15:11"],
															  [trueHeading:203,   legTime:"00:13:35"],
															  [trueHeading:154,   legTime:"00:13:04"],
															  [trueHeading:101.75,legTime:"00:10:57"],
															  [trueHeading:220.5, legTime:"00:08:19"],
															  [trueHeading:230,   legTime:"00:04:42"]],
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
                                                  gac:"Crew_3.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew4,
												  startNum:11,
                                                  gac:"Crew_11.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew5,
												  startNum:13,
                                                  gac:"Crew_13.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew3,
												  startNum:19,
                                                  gac:"Crew_19.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												 [crew:crew2,
												  startNum:18,
                                                  gac:"Crew_18.gac",
												  takeoffMissed:false,
												  badCourseStartLanding:false,
												  landingTooLate:false,
												  givenTooLate:false,
												  safetyAndRulesInfringement:false,
												  instructionsNotFollowed:false,
												  falseEnvelopeOpened:false,
												  safetyEnvelopeOpened:false,
												  frequencyNotMonitored:false,
												  testComplete:true],
												],aflosDB)
		fcService.putobservationresultsTask(task1, [
													[crew:crew1,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
													[crew:crew2,routePhotos:0,turnPointPhotos:0,groundTargets:10,testComplete:true],
													[crew:crew3,routePhotos:120,turnPointPhotos:0,groundTargets:10,testComplete:true],
													[crew:crew4,routePhotos:0,turnPointPhotos:0,groundTargets:0,testComplete:true],
													[crew:crew5,routePhotos:20,turnPointPhotos:0,groundTargets:0,testComplete:true],
												   ])
		fcService.putlandingresultsTask(task1, [
												[crew:crew1,
												 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:true,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew2,
												 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'F',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew3,
												 landingTest1Measure:'B',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest2Measure:'A',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew4,
												 landingTest1Measure:'0',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest2Measure:'F',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'B',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'0',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4TouchingObstacle:false,
												 testComplete:true],
												[crew:crew5,
												 landingTest1Measure:'E',landingTest1Landing:1,landingTest1RollingOutside:false,landingTest1PowerInBox:false,
												 landingTest1GoAroundWithoutTouching:false,landingTest1GoAroundInsteadStop:false,landingTest1AbnormalLanding:false,
												 landingTest2Measure:'0',landingTest2Landing:1,landingTest2RollingOutside:false,landingTest2PowerInBox:false,
												 landingTest2GoAroundWithoutTouching:false,landingTest2GoAroundInsteadStop:false,landingTest2AbnormalLanding:false,landingTest2PowerInAir:false,
												 landingTest3Measure:'0',landingTest3Landing:1,landingTest3RollingOutside:false,landingTest3PowerInBox:false,
												 landingTest3GoAroundWithoutTouching:false,landingTest3GoAroundInsteadStop:false,landingTest3AbnormalLanding:false,
												 landingTest3PowerInAir:false,landingTest3FlapsInAir:false,
												 landingTest4Measure:'A',landingTest4Landing:1,landingTest4RollingOutside:false,landingTest4PowerInBox:false,
												 landingTest4GoAroundWithoutTouching:false,landingTest4GoAroundInsteadStop:false,landingTest4AbnormalLanding:false,
												 landingTest4TouchingObstacle:false,
												 testComplete:true],
											   ])
		evaluationService.runcalculatepositionsTask(task1)
		evaluationService.runcalculatepositionsResultClass(resultclass1,[task1],[team1,team2,team3])
		evaluationService.runcalculatepositionsResultClass(resultclass2,[task1],[team1,team2,team3])
		evaluationService.runcalculatepositionsResultClass(resultclass3,[task1],[team1,team2,team3])
		evaluationService.runcalculatecontestpositionsContest(contest,[resultclass1],[task1],[team1,team2,team3])
		evaluationService.runcalculateteampositionsContest(contest,[resultclass1,resultclass2,resultclass3],[task1])
		
		fcService.printdone ""
		
		return contest.instance.id
	}

	long CreateTest3(String testName, String printPrefix, boolean testExists, boolean aflosDB)
	{
        fcService.printstart "Create test contest '$testName'"
        
        // Contest
        Map contest = fcService.putContest(testName,printPrefix,200000,true,0,ContestRules.R1,aflosDB,testExists) // 0 - keine Team-Auswertung
		
		// Route 1
        Map route1 = [:]
		fcService.printstart "Route 1"
        if (aflosDB) {
            route1 = fcService.importAflosRoute(contest,"Strecke 1","Strecke 1",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        } else {
            route1 = fcService.importFileRoute(RouteFileTools.GPX_EXTENSION, contest.instance, "Strecke_1.gpx")
        }
		fcService.printdone ""
		
		// Route 2
        Map route2 = [:]
		fcService.printstart "Route 2"
        if (aflosDB) {
            route2 = fcService.importAflosRoute(contest,"Strecke 2","Strecke 2",SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK,false,[])
        } else {
            route2 = fcService.importFileRoute(RouteFileTools.GPX_EXTENSION, contest.instance, "Strecke_2.gpx")
        }
		fcService.printdone ""
		
		// Teams
		Map team1 = fcService.putTeam(contest,"Deutschland")
		
		// Crews with Teams, ResultClasses and Aircrafts
		// DMM
		Map crew3 = fcService.putCrew(contest,3,"Besatzung 3","","Deutschland","DMM","D-EAAA","","",85)
		Map crew18 = fcService.putCrew(contest,18,"Besatzung 18","","Deutschland","DMM","D-EAAD","","",80)
		Map crew19 = fcService.putCrew(contest,19,"Besatzung 19","","Deutschland","DMM","D-EAAE","","",80)
		Map crew11 = fcService.putCrew(contest,11,"Besatzung 11","","Deutschland","DMM","D-EAAB","","",70)
		Map crew13 = fcService.putCrew(contest,13,"Besatzung 13","","Deutschland","DMM","D-EAAC","","",70)
		// RuB-Wettbewerb
		Map crew1 = fcService.putCrew(contest,1,"Besatzung 1","","Deutschland","RuB-Wettbewerb","D-EABA","","",85)
		Map crew2 = fcService.putCrew(contest,2,"Besatzung 2","","Deutschland","RuB-Wettbewerb","D-EABB","","",70)
		Map crew4 = fcService.putCrew(contest,4,"Besatzung 4","","Deutschland","RuB-Wettbewerb","D-EABC","","",90)
		Map crew5 = fcService.putCrew(contest,5,"Besatzung 5","","Deutschland","RuB-Wettbewerb","D-EABD","","",90)
		Map crew7 = fcService.putCrew(contest,7,"Besatzung 7","","Deutschland","RuB-Wettbewerb","D-EABE","","",80)
		// RuB-Tourist
		Map crew8 = fcService.putCrew(contest,8,"Besatzung 8","","Deutschland","RuB-Tourist","D-EACA","","",70)
		Map crew9 = fcService.putCrew(contest,9,"Besatzung 9","","Deutschland","RuB-Tourist","D-EACB","","",85)
		Map crew10 = fcService.putCrew(contest,10,"Besatzung 10","","Deutschland","RuB-Tourist","D-EACC","","",75)
		Map crew12 = fcService.putCrew(contest,12,"Besatzung 12","","Deutschland","RuB-Tourist","D-EACD","","",90)
		Map crew14 = fcService.putCrew(contest,14,"Besatzung 14","","Deutschland","RuB-Tourist","D-EACE","","",100)
		
        // Classes with properties
        Map resultclass1 = fcService.putResultClass(contest,"DMM","Deutsche Motorflugmeisterschaft Navigationsflug",ContestRules.R1)
        Map resultclass2 = fcService.putResultClass(contest,"RuB-Wettbewerb","Rund um Berlin",ContestRules.R1)
		Map resultclass3 = fcService.putResultClass(contest,"RuB-Tourist","Rund um Berlin",ContestRules.R1)
		
		// Tasks - planningTestRun flightTestRun observationTestRun landingTestRun specialTestRun
		//         planningTestDistanceMeasure planningTestDirectionMeasure 
		//         landingTest1Run landingTest2Run landingTest3Run landingTest4Run
		
        // TaskClass properties
		
		// 1 - 23. August
        Map task1 = fcService.putTask(contest,"23. August","10:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false,  false,true, true,false,false, true,true,true,true, false)
		
        fcService.puttaskclassTask(task1,resultclass1,true,true,true,true,false,      false,true, true,false,false, true,true,true,true)
        fcService.puttaskclassTask(task1,resultclass2,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task1,resultclass3,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
		
		Map planningtest1 = fcService.putPlanningTest(task1,"")
		Map planningtesttask1 = fcService.putPlanningTestTask(planningtest1,"",route1,130,20)
		fcService.putplanningtesttaskcrewsTask(task1,planningtesttask1,[crew3,crew11,crew13,crew19,crew18])
		
		Map flighttest1 = fcService.putFlightTest(task1,"",route1)
		Map flighttestwind1 = fcService.putFlightTestWind(flighttest1,300,15,274,0,0,260,-0.05,0,0,0,0)
		fcService.putflighttestwindcrewsTask(task1,flighttestwind1,[crew3,crew11,crew13,crew19,crew18])
		
		fcService.putsequenceTask(task1,[crew3,crew11,crew13,crew19,crew18])
		fcService.runcalculatetimetableTask(task1)
		
		// 2 - 24. August
		Map task2 = fcService.putTask(contest,"24. August","10:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,false,false, false,true, false,true,true, false,false,false,false, false)
		
        fcService.puttaskclassTask(task2,resultclass1,true,true,true,false,false,     false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass2,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
        fcService.puttaskclassTask(task2,resultclass3,false,false,false,false,false,  false,true, true,false,false, false,false,false,false)
		
		Map planningtest2 = fcService.putPlanningTest(task2,"")
		Map planningtesttask2 = fcService.putPlanningTestTask(planningtest2,"",route2,130,20)
		fcService.putplanningtesttaskcrewsTask(task2,planningtesttask2,[crew3,crew11,crew13,crew19,crew18])
		
		Map flighttest2 = fcService.putFlightTest(task2,"",route2)
		Map flighttestwind2 = fcService.putFlightTestWind(flighttest2,300,15,274,0,0,260,-0.05,0,0,0,0)
		fcService.putflighttestwindcrewsTask(task2,flighttestwind2,[crew3,crew11,crew13,crew19,crew18])
		
		fcService.putsequenceTask(task2,[crew3,crew11,crew13,crew19,crew18])
		fcService.runcalculatetimetableTask(task2)

		// 3 - 25. August
		Map task3 = fcService.putTask(contest,"25. August","10:00",3,"time:8min","time:10min",5,"wind+:2NM","wind+:2NM",true,true,true,true,false,  false,true, true,false,false, true,false,false,false, false)
		
		fcService.puttaskclassTask(task3,resultclass1,true,true,true,true,false,      false,true, true,false,false, true,false,false,false)
		fcService.puttaskclassTask(task3,resultclass2,true,true,true,true,false,      false,true, true,false,false, true,false,false,false)
		fcService.puttaskclassTask(task3,resultclass3,true,true,true,true,false,      false,true, true,false,false, true,false,false,false)
		
		Map planningtest3 = fcService.putPlanningTest(task3,"")
		Map planningtesttask3 = fcService.putPlanningTestTask(planningtest3,"",route1,130,20)
		fcService.putplanningtesttaskTask(task3,planningtesttask3)
		
		Map flighttest3 = fcService.putFlightTest(task3,"",route1)
		Map flighttestwind3 = fcService.putFlightTestWind(flighttest3,300,15,274,0,0,260,-0.05,0,0,0,0)
		fcService.putflighttestwindTask(task3,flighttestwind3)
		
		fcService.putsequenceTask(task3,[crew14,crew4,crew5,crew12,crew3,crew1,crew9,crew18,crew19,crew7,crew10,crew11,crew13,crew2,crew8])
		fcService.runcalculatetimetableTask(task3)

        fcService.printdone ""
        
		return contest.instance.id
	}

	Map RunTest1(Contest lastContest, String contestName, boolean aflosDB)
	{
		Map ret_test = [:]
        if (lastContest && lastContest.title == contestName) {
            fcService.printstart "runtest '$lastContest.title'"
            Map ret = fcService.testData(
               [[name:"Route",count:1,table:Route.findAllByContest(lastContest,[sort:"id"]),data:test1Route()],
                [name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1CoordRoute()],
                [name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1RouteLegCoord()],
                [name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1RouteLegTest()],
                [name:"Crew",count:5,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:test1Crew()],
                [name:"Aircraft",count:5,table:Aircraft.findAllByContest(lastContest,[sort:"id"]),data:test1Aircraft()],
                [name:"Team",count:2,table:Team.findAllByContest(lastContest,[sort:"id"]),data:test1Team()],
                [name:"Task",count:1,table:Task.findAllByContest(lastContest,[sort:"id"]),data:test1Task()],
                [name:"TestLegPlanning 'Besatzung 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegPlanning3()],
                [name:"TestLegPlanning 'Besatzung 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegPlanning18()],
                [name:"TestLegPlanning 'Besatzung 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegPlanning19()],
                [name:"TestLegPlanning 'Besatzung 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegPlanning11()],
                [name:"TestLegPlanning 'Besatzung 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegPlanning13()],
                [name:"TestLegFlight 'Besatzung 3'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegFlight3()],
                [name:"TestLegFlight 'Besatzung 18'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegFlight18()],
                [name:"TestLegFlight 'Besatzung 19'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegFlight19()],
                [name:"TestLegFlight 'Besatzung 11'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegFlight11()],
                [name:"TestLegFlight 'Besatzung 13'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegFlight13()],
                [name:"CoordResult 'Besatzung 3'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1CoordResult3()],    
                [name:"CoordResult 'Besatzung 18'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1CoordResult18()],    
                [name:"CoordResult 'Besatzung 19'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1CoordResult19()],    
                [name:"CoordResult 'Besatzung 11'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1CoordResult11()],    
                [name:"CoordResult 'Besatzung 13'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1CoordResult13()],    
                [name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(lastContest),[sort:"id"]),data:test1Test()],
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
	
	Map RunTest2(Contest lastContest, String contestName, boolean aflosDB)
	{
		Map ret_test = [:]
		if (lastContest && lastContest.title == contestName) {
			fcService.printstart "runtest '$lastContest.title'"
			Map ret = fcService.testData(
			   [[name:"Route",count:1,table:Route.findAllByContest(lastContest,[sort:"id"]),data:test1Route()],
				[name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1CoordRoute()],
				[name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1RouteLegCoord()],
				[name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1RouteLegTest()],
				[name:"Crew",count:5,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:test2Crew()],
				[name:"Aircraft",count:5,table:Aircraft.findAllByContest(lastContest,[sort:"id"]),data:test1Aircraft()],
				[name:"ResultClass",count:3,table:ResultClass.findAllByContest(lastContest,[sort:"id"]),data:test2ResultClass()],
				[name:"Team",count:3,table:Team.findAllByContest(lastContest,[sort:"id"]),data:test2Team()],
				[name:"Task",count:1,table:Task.findAllByContest(lastContest,[sort:"id"]),data:test1Task()],
				[name:"TaskClass",count:3,table:TaskClass.findAllByTask(Task.findByContest(lastContest),[sort:"id"]),data:test2TaskClass()],
				[name:"TestLegPlanning 'Besatzung 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegPlanning3()],
				[name:"TestLegPlanning 'Besatzung 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test2TestLegPlanning18()],
				[name:"TestLegPlanning 'Besatzung 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegPlanning19()],
				[name:"TestLegPlanning 'Besatzung 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test2TestLegPlanning11()],
				[name:"TestLegPlanning 'Besatzung 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegPlanning13()],
				[name:"TestLegFlight 'Besatzung 3'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegFlight3()],
				[name:"TestLegFlight 'Besatzung 18'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegFlight18()],
				[name:"TestLegFlight 'Besatzung 19'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegFlight19()],
				[name:"TestLegFlight 'Besatzung 11'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegFlight11()],
				[name:"TestLegFlight 'Besatzung 13'",count:11,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegFlight13()],
				[name:"CoordResult 'Besatzung 3'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1CoordResult3()],
				[name:"CoordResult 'Besatzung 18'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1CoordResult18()],
				[name:"CoordResult 'Besatzung 19'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1CoordResult19()],
				[name:"CoordResult 'Besatzung 11'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1CoordResult11()],
				[name:"CoordResult 'Besatzung 13'",count:14,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1CoordResult13()],
				[name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(lastContest),[sort:"id"]),data:test2Test()],
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
	
	Map RunTest3(Contest lastContest, String contestName, boolean aflosDB)
	{
		Map ret_test = [:]
        if (lastContest && lastContest.title == contestName) {
            fcService.printstart "runtest '$lastContest.title'"
            Map ret = fcService.testData(
               [[name:"Route",count:1,table:Route.findAllByContest(lastContest,[sort:"id"]),data:test1Route()],
                [name:"CoordRoute",count:14,table:CoordRoute.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1CoordRoute()],
                [name:"RouteLegCoord",count:13,table:RouteLegCoord.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1RouteLegCoord()],
                [name:"RouteLegTest",count:6,table:RouteLegTest.findAllByRoute(Route.findByContest(lastContest),[sort:"id"]),data:test1RouteLegTest()],
                [name:"Crew",count:5,table:Crew.findAllByContest(lastContest,[sort:"id"]),data:test2Crew()],
                [name:"Aircraft",count:5,table:Aircraft.findAllByContest(lastContest,[sort:"id"]),data:test1Aircraft()],
                [name:"ResultClass",count:2,table:ResultClass.findAllByContest(lastContest,[sort:"id"]),data:test3ResultClass()],
                [name:"Team",count:2,table:Team.findAllByContest(lastContest,[sort:"id"]),data:test3Team()],
                [name:"Task",count:1,table:Task.findAllByContest(lastContest,[sort:"id"]),data:test1Task()],
                [name:"TaskClass",count:2,table:TaskClass.findAllByTask(Task.findByContest(lastContest),[sort:"id"]),data:test3TaskClass()],
                [name:"TestLegPlanning 'Besatzung 3'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegPlanning3()],
                [name:"TestLegPlanning 'Besatzung 18'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test2TestLegPlanning18()],
                [name:"TestLegPlanning 'Besatzung 19'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegPlanning19()],
                [name:"TestLegPlanning 'Besatzung 11'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test2TestLegPlanning11()],
                [name:"TestLegPlanning 'Besatzung 13'",count:6,table:TestLegPlanning.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegPlanning13()],
                [name:"TestLegFlight 'Besatzung 3'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1TestLegFlight3()],
                [name:"TestLegFlight 'Besatzung 18'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1TestLegFlight18()],
                [name:"TestLegFlight 'Besatzung 19'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1TestLegFlight19()],
                [name:"TestLegFlight 'Besatzung 11'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1TestLegFlight11()],
                [name:"TestLegFlight 'Besatzung 13'",count:6,table:TestLegFlight.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1TestLegFlight13()],
                [name:"CoordResult 'Besatzung 3'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 3")),[sort:"id"]),data:test1CoordResult3()],    
                [name:"CoordResult 'Besatzung 18'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 18")),[sort:"id"]),data:test1CoordResult18()],    
                [name:"CoordResult 'Besatzung 19'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 19")),[sort:"id"]),data:test1CoordResult19()],    
                [name:"CoordResult 'Besatzung 11'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 11")),[sort:"id"]),data:test1CoordResult11()],    
                [name:"CoordResult 'Besatzung 13'",count:12,table:CoordResult.findAllByTest(Test.findByTaskAndCrew(Task.findByContest(lastContest),Crew.findByContestAndName(lastContest,"Besatzung 13")),[sort:"id"]),data:test1CoordResult13()],    
                [name:"Test",count:5,table:Test.findAllByTask(Task.findByContest(lastContest),[sort:"id"]),data:test2Test()],
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
	
    List test1Route() {
      [[title:"Strecke 1",mark:"Strecke 1"]]
    }
	
    List test1CoordRoute() {
      [[type:CoordType.TO,    mark:"T/O",
        latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        coordTrueTrack:0,coordMeasureDistance:0,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SP,    mark:"SP",
        latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:48.2896815944,coordMeasureDistance:37.9424006312,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP1",
        latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:88.8048175589,coordMeasureDistance:99.4440780950,
        measureTrueTrack:89.0,measureDistance:99.0,legMeasureDistance:99.0,legDistance:10.6911447084,secretLegRatio:0.6598765432,planProcedureTurn:false],
       [type:CoordType.TP,    mark:"CP2",
        latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:88.9286028152,coordMeasureDistance:149.9577105678,
        measureTrueTrack:89.0,measureDistance:150.0,legMeasureDistance:51.0,legDistance:5.5075593952,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP3",
        latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:219.3292360731,coordMeasureDistance:46.1613175609,
        measureTrueTrack:219.0,measureDistance:46.0,legMeasureDistance:46.0,legDistance:4.9676025918,secretLegRatio:0.2849770642,planProcedureTurn:true],
       [type:CoordType.TP,    mark:"CP4",
        latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:219.1791259770,coordMeasureDistance:161.4133764552,
        measureTrueTrack:219.0,measureDistance:161.5,legMeasureDistance:115.5,legDistance:12.4730021598,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP5",
        latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:160.8781319787,coordMeasureDistance:69.4482392126,
        measureTrueTrack:161.0,measureDistance:68.5,legMeasureDistance:68.5,legDistance:7.3974082073,secretLegRatio:0.5481481481,planProcedureTurn:false],
       [type:CoordType.TP,    mark:"CP6",
        latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:160.9571657647,coordMeasureDistance:126.1284271762,
        measureTrueTrack:161.0,measureDistance:125.0,legMeasureDistance:56.5,legDistance:6.1015118790,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP7",
        latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:86.3563657892,coordMeasureDistance:19.8166630762,
        measureTrueTrack:86.0,measureDistance:19.5,legMeasureDistance:19.5,legDistance:2.1058315335,secretLegRatio:0.1834782609,planProcedureTurn:false],
       [type:CoordType.TP,    mark:"CP8",
        latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:86.6280647396,coordMeasureDistance:106.7215488986,
        measureTrueTrack:86.0,measureDistance:106.45,legMeasureDistance:86.95,legDistance:9.3898488121,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.SECRET,mark:"CP9",
        latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        coordTrueTrack:237.2378249684,coordMeasureDistance:25.9240833138,
        measureTrueTrack:237.0,measureDistance:25.5,legMeasureDistance:25.5,legDistance:2.7537796976,secretLegRatio:0.2420774648,planProcedureTurn:true],
       [type:CoordType.TP,    mark:"CP10",
        latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:237.1648386550,coordMeasureDistance:104.8406641113,
        measureTrueTrack:237.0,measureDistance:105.2,legMeasureDistance:79.7,legDistance:8.6069114471,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.FP,    mark:"FP",
        latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        coordTrueTrack:242.9619395538,coordMeasureDistance:62.0480855033,
        measureTrueTrack:244.0,measureDistance:62.4,legMeasureDistance:62.4,legDistance:6.7386609071,secretLegRatio:0,planProcedureTurn:false],
       [type:CoordType.LDG,   mark:"LDG",
        latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        coordTrueTrack:256.4547835436,coordMeasureDistance:33.4955075819,
        measureTrueTrack:null,measureDistance:null,legMeasureDistance:null,legDistance:null,secretLegRatio:0,planProcedureTurn:false]
      ]
    }
	
    List test1RouteLegCoord() {
      [[coordTrueTrack:48.2896815944, coordDistance:4.0974514721, measureDistance:null,  legMeasureDistance:null,  legDistance:null,         measureTrueTrack:null, turnTrueTrack:null],
       [coordTrueTrack:88.8048175589, coordDistance:10.7391013062,measureDistance:99.0,  legMeasureDistance:99.0,  legDistance:10.6911447084,measureTrueTrack:89.0, turnTrueTrack:48],
       [coordTrueTrack:88.9286028152, coordDistance:5.4550359042, measureDistance:150.0, legMeasureDistance:51.0,  legDistance:5.5075593952, measureTrueTrack:89.0, turnTrueTrack:89],
       [coordTrueTrack:219.3292360731,coordDistance:4.9850234947, measureDistance:46.0,  legMeasureDistance:46.0,  legDistance:4.9676025918, measureTrueTrack:219.0,turnTrueTrack:89],
       [coordTrueTrack:219.1791259770,coordDistance:12.4462266624,measureDistance:161.5, legMeasureDistance:115.5, legDistance:12.4730021598,measureTrueTrack:219.0,turnTrueTrack:219],
       [coordTrueTrack:160.8781319787,coordDistance:7.4998098502, measureDistance:68.5,  legMeasureDistance:68.5,  legDistance:7.3974082073, measureTrueTrack:161.0,turnTrueTrack:219],
       [coordTrueTrack:160.9571657647,coordDistance:6.1209706224, measureDistance:125.0, legMeasureDistance:56.5,  legDistance:6.1015118790, measureTrueTrack:161.0,turnTrueTrack:161],
       [coordTrueTrack:86.3563657892, coordDistance:2.1400284100, measureDistance:19.5,  legMeasureDistance:19.5,  legDistance:2.1058315335, measureTrueTrack:86.0, turnTrueTrack:161],
       [coordTrueTrack:86.6280647396, coordDistance:9.3849768707, measureDistance:106.45,legMeasureDistance:86.95, legDistance:9.3898488121, measureTrueTrack:86.0, turnTrueTrack:86],
       [coordTrueTrack:237.2378249684,coordDistance:2.7995770317, measureDistance:25.5,  legMeasureDistance:25.5,  legDistance:2.7537796976, measureTrueTrack:237.0,turnTrueTrack:86],
       [coordTrueTrack:237.1648386550,coordDistance:8.5223089414, measureDistance:105.2, legMeasureDistance:79.7,  legDistance:8.6069114471, measureTrueTrack:237.0,turnTrueTrack:237],
       [coordTrueTrack:242.9619395538,coordDistance:6.7006571818, measureDistance:62.4,  legMeasureDistance:62.4,  legDistance:6.7386609071, measureTrueTrack:244.0,turnTrueTrack:237],
       [coordTrueTrack:256.4547835436,coordDistance:3.6172254408, measureDistance:null,  legMeasureDistance:null,  legDistance:null,         measureTrueTrack:null, turnTrueTrack:244],
      ]
    }
	
    List test1RouteLegTest() {
      [[coordTrueTrack:88.8048175589, coordDistance:16.2,         measureDistance:150.0, legMeasureDistance:150.0, legDistance:16.1987041037,measureTrueTrack:89.0, turnTrueTrack:null],
       [coordTrueTrack:219.3292360731,coordDistance:17.44,        measureDistance:161.5, legMeasureDistance:161.5, legDistance:17.4406047516,measureTrueTrack:219.0,turnTrueTrack:89],
       [coordTrueTrack:160.8781319787,coordDistance:13.62,        measureDistance:125.0, legMeasureDistance:125.0, legDistance:13.4989200864,measureTrueTrack:161.0,turnTrueTrack:219],
       [coordTrueTrack:86.3563657892, coordDistance:11.52,        measureDistance:106.45,legMeasureDistance:106.45,legDistance:11.4956803456,measureTrueTrack:86.0, turnTrueTrack:161],
       [coordTrueTrack:237.2378249684,coordDistance:11.32,        measureDistance:105.2, legMeasureDistance:105.2, legDistance:11.3606911447,measureTrueTrack:237.0,turnTrueTrack:86],
       [coordTrueTrack:242.9619395538,coordDistance:6.7,          measureDistance:62.4,  legMeasureDistance:62.4,  legDistance:6.7386609071, measureTrueTrack:244.0,turnTrueTrack:237],
      ]
    }
	
    List test1Crew() {
       [[startNum:3, name:"Besatzung 3", email:"crew3.fc@localhost", mark:"",team:[name:"Deutschland"],tas:85,contestPenalties:218,contestPosition:3,noContestPosition:false,aircraft:[registration:"D-EAAA",type:"",colour:""]],
        [startNum:18,name:"Besatzung 18",email:"crew18.fc@localhost",mark:"",team:[name:"Deutschland"],tas:80,contestPenalties:133,contestPosition:1,noContestPosition:false,aircraft:[registration:"D-EAAD",type:"",colour:""]],
        [startNum:19,name:"Besatzung 19",email:"crew19.fc@localhost",mark:"",team:[name:"Deutschland"],tas:80,contestPenalties:568,contestPosition:5,noContestPosition:false,aircraft:[registration:"D-EAAE",type:"",colour:""]],
        [startNum:11,name:"Besatzung 11",email:"crew11.fc@localhost",mark:"",team:[name:"Schweiz"],    tas:70,contestPenalties:384,contestPosition:4,noContestPosition:false,aircraft:[registration:"D-EAAB",type:"",colour:""]],
        [startNum:13,name:"Besatzung 13",email:"crew13.fc@localhost",mark:"",team:[name:"Schweiz"],    tas:70,contestPenalties:135,contestPosition:2,noContestPosition:false,aircraft:[registration:"D-EAAC",type:"",colour:""]],
       ]
    }
	
    List test1Aircraft() {
       [[registration:"D-EAAA",type:"",colour:"",user1:[name:"Besatzung 3"],user2:null],
        [registration:"D-EAAD",type:"",colour:"",user1:[name:"Besatzung 18"],user2:null],
        [registration:"D-EAAE",type:"",colour:"",user1:[name:"Besatzung 19"],user2:null],
        [registration:"D-EAAB",type:"",colour:"",user1:[name:"Besatzung 11"],user2:null],
        [registration:"D-EAAC",type:"",colour:"",user1:[name:"Besatzung 13"],user2:null],
       ]
    }
	
    List test1Team() {
      [[name:"Deutschland",contestPenalties:351,contestPosition:1],
       [name:"Schweiz",    contestPenalties:519,contestPosition:2],
      ]
    }
	
    List test1Task() {
      [[title:"",firstTime:"09:00",takeoffIntervalNormal:3,takeoffIntervalSlowerAircraft:3,takeoffIntervalFasterAircraft:30,planningTestDuration:60,
        preparationDuration:15,risingDurationFormula:"time:8min",maxLandingDurationFormula:"time:10min",parkingDuration:5,minNextFlightDuration:30,
        procedureTurnDuration:1,addTimeValue:3,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true]
      ]
    }
	
    List test1TestLegPlanning3() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:97.8800763462,planGroundSpeed:68.8869647355,planLegTime:0.2351678589,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98.1,resultGroundSpeed:0,resultLegTime:0.235,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:205.3931519488,planGroundSpeed:82.2652474538,planLegTime:0.2119971743,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:205.12,resultGroundSpeed:0,resultLegTime:0.2113888889,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:154.0394779952,planGroundSpeed:67.2301943440,planLegTime:0.2008026324,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:154.123,resultGroundSpeed:0,resultLegTime:0.2008333333,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:95.4071472127,planGroundSpeed:69.4701053153,planLegTime:0.1655388307,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:95,resultGroundSpeed:0,resultLegTime:0.1655555556,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.9963642250,planGroundSpeed:88.6676760978,planLegTime:0.1281188422,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:223.995,resultGroundSpeed:0,resultLegTime:0.1286111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:231.5872962201,planGroundSpeed:91.1478274836,planLegTime:0.0739458107,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:232,resultGroundSpeed:0,resultLegTime:0.0736111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegPlanning18() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.4400045050,planGroundSpeed:63.8224392169,planLegTime:0.2538292205,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.5247409558,planGroundSpeed:77.1114052303,planLegTime:0.2261662843,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:204,resultGroundSpeed:0,resultLegTime:0.2272222222,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.6020844648,planGroundSpeed:62.1907217788,planLegTime:0.2170741811,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2175,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550175,planGroundSpeed:64.3975926711,planLegTime:0.1785781040,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:96,resultGroundSpeed:0,resultLegTime:0.1786111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772858,planGroundSpeed:83.5274990113,planLegTime:0.1360031144,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:223,resultGroundSpeed:0,resultLegTime:0.1361111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.7979111274,planGroundSpeed:86.0203790277,planLegTime:0.0783535259,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0788888889,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegPlanning19() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.4400045050,planGroundSpeed:63.8224392169,planLegTime:0.2538292205,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.5247409558,planGroundSpeed:77.1114052303,planLegTime:0.2261662843,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2263888889,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.6020844648,planGroundSpeed:62.1907217788,planLegTime:0.2170741811,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:154,resultGroundSpeed:0,resultLegTime:0.2177777778,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550175,planGroundSpeed:64.3975926711,planLegTime:0.1785781040,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:101.75,resultGroundSpeed:0,resultLegTime:0.1825,
        resultEntered:true,penaltyTrueHeading:8,penaltyLegTime:9
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772858,planGroundSpeed:83.5274990113,planLegTime:0.1360031144,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:220.5,resultGroundSpeed:0,resultLegTime:0.1386111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:4
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.7979111274,planGroundSpeed:86.0203790277,planLegTime:0.0783535259,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0783333333,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegPlanning11() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076,planGroundSpeed:53.6650595563,planLegTime:0.3018723940,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:99,resultGroundSpeed:0,resultLegTime:0.3,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:2
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.4010521006,planGroundSpeed:66.7338992916,planLegTime:0.2613364450,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2622222222,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.5379827855,planGroundSpeed:52.0946082636,planLegTime:0.2591439009,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2586111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.4477263633,planGroundSpeed:54.2206372592,planLegTime:0.2120963637,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:97,resultGroundSpeed:0,resultLegTime:0.2116666667,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012,planGroundSpeed:73.1838468701,planLegTime:0.1552255106,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1558333333,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.8698222549,planGroundSpeed:75.7082030901,planLegTime:0.0890260200,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0888888889,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegPlanning13() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076,planGroundSpeed:53.6650595563,planLegTime:0.3018723940,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:16.20,resultTrueHeading:100,resultGroundSpeed:0,resultLegTime:0.3008333333,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.4010521006,planGroundSpeed:66.7338992916,planLegTime:0.2613364450,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:17.44,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2602777778,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.5379827855,planGroundSpeed:52.0946082636,planLegTime:0.2591439009,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:13.50,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2597222222,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.4477263633,planGroundSpeed:54.2206372592,planLegTime:0.2120963637,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:11.50,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2125,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012,planGroundSpeed:73.1838468701,planLegTime:0.1552255106,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:11.36,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1552777778,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.8698222549,planGroundSpeed:75.7082030901,planLegTime:0.0890260200,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:6.74,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0891666667,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegFlight3() {
      [[planTrueTrack:89.0,planTestDistance:10.69,planTrueHeading:83.7852431943,planGroundSpeed:97.5056964422,planLegTime:0.1096346202,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:89.0,planTestDistance:5.51,planTrueHeading:83.7852431943,planGroundSpeed:97.5056964422,planLegTime:0.0565095189,
	    planProcedureTurn:false,planProcedureTurnDuration:0,
	    resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
	    resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
	   ],
       [planTrueTrack:219.0,planTestDistance:4.97,planTrueHeading:229.0378052961,planGroundSpeed:81.3523847013,planLegTime:0.0610922472,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:12.47,planTrueHeading:229.0378052961,planGroundSpeed:81.3523847013,planLegTime:0.1532837672,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:7.4,planTrueHeading:167.6483349505,planGroundSpeed:95.7490570122,planLegTime:0.0772853564,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:6.1,planTrueHeading:167.6483349505,planGroundSpeed:95.7490570122,planLegTime:0.0637081992,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:2.11,planTrueHeading:80.3367725496,planGroundSpeed:97.0206878228,planLegTime:0.0217479390,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:9.39,planTrueHeading:80.3367725496,planGroundSpeed:97.0206878228,planLegTime:0.0967834821,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:2.75,planTrueHeading:246.0465258632,planGroundSpeed:77.1328262775,planLegTime:0.0356527841,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:8.61,planTrueHeading:246.0465258632,planGroundSpeed:77.1328262775,planLegTime:0.1116256258,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.4126098880,planGroundSpeed:75.6975199169,planLegTime:0.0890385842,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegFlight18() {
      [[planTrueTrack:89.0,planTestDistance:10.69,planTrueHeading:83.4583310658,planGroundSpeed:92.4836079382,planLegTime:0.1155880511,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:89.0,planTestDistance:5.51,planTrueHeading:83.4583310658,planGroundSpeed:92.4836079382,planLegTime:0.0595781255,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:4.97,planTrueHeading:229.6723011898,planGroundSpeed:76.2696781875,planLegTime:0.0651635108,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:12.47,planTrueHeading:229.6723011898,planGroundSpeed:76.2696781875,planLegTime:0.1634987887,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:7.4,planTrueHeading:168.0659119292,planGroundSpeed:90.7130676105,planLegTime:0.0815758985,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:6.1,planTrueHeading:168.0659119292,planGroundSpeed:90.7130676105,planLegTime:0.0672449974,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:2.11,planTrueHeading:79.9815521701,planGroundSpeed:91.9946186466,planLegTime:0.0229361242,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:9.39,planTrueHeading:79.9815521701,planGroundSpeed:91.9946186466,planLegTime:0.1020711878,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:2.75,planTrueHeading:246.6171413877,planGroundSpeed:72.0658305143,planLegTime:0.0381595547,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:8.61,planTrueHeading:246.6171413877,planGroundSpeed:72.0658305143,planLegTime:0.1194740966,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.9425792987,planGroundSpeed:70.6396760565,planLegTime:0.0954138011,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegFlight19() {
      [[planTrueTrack:89.0,planTestDistance:10.69,planTrueHeading:83.4583310658,planGroundSpeed:92.4836079382,planLegTime:0.1155880511,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:89.0,planTestDistance:5.51,planTrueHeading:83.4583310658,planGroundSpeed:92.4836079382,planLegTime:0.0595781255,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:4.97,planTrueHeading:229.6723011898,planGroundSpeed:76.2696781875,planLegTime:0.0651635108,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:12.47,planTrueHeading:229.6723011898,planGroundSpeed:76.2696781875,planLegTime:0.1634987887,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:7.40,planTrueHeading:168.0659119292,planGroundSpeed:90.7130676105,planLegTime:0.0815758985,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:6.10,planTrueHeading:168.0659119292,planGroundSpeed:90.7130676105,planLegTime:0.0672449974,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:2.11,planTrueHeading:79.9815521701,planGroundSpeed:91.9946186466,planLegTime:0.0229361242,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:9.39,planTrueHeading:79.9815521701,planGroundSpeed:91.9946186466,planLegTime:0.1020711878,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:2.75,planTrueHeading:246.6171413877,planGroundSpeed:72.0658305143,planLegTime:0.0381595547,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:8.61,planTrueHeading:246.6171413877,planGroundSpeed:72.0658305143,planLegTime:0.1194740966,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:252.9425792987,planGroundSpeed:70.6396760565,planLegTime:0.0954138011,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegFlight11() {
      [[planTrueTrack:89.0,planTestDistance:10.69,planTrueHeading:82.6636259635,planGroundSpeed:82.4298858593,planLegTime:0.1296859736,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:89.0,planTestDistance:5.51,planTrueHeading:82.6636259635,planGroundSpeed:82.4298858593,planLegTime:0.0668446880,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:4.97,planTrueHeading:231.2189175362,planGroundSpeed:66.0677077289,planLegTime:0.0752258580,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:12.47,planTrueHeading:231.2189175362,planGroundSpeed:66.0677077289,planLegTime:0.1887457644,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:7.40,planTrueHeading:169.0816457025,planGroundSpeed:80.6254557575,planLegTime:0.0917824269,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:6.10,planTrueHeading:169.0816457025,planGroundSpeed:80.6254557575,planLegTime:0.0756584870,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:2.11,planTrueHeading:79.1178787047,planGroundSpeed:81.9311982840,planLegTime:0.0257533155,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:9.39,planTrueHeading:79.1178787047,planGroundSpeed:81.9311982840,planLegTime:0.1146083567,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:2.75,planTrueHeading:248.0070611206,planGroundSpeed:61.9023987550,planLegTime:0.0444247728,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:8.61,planTrueHeading:248.0070611206,planGroundSpeed:61.9023987550,planLegTime:0.1390899250,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:254.2329616616,planGroundSpeed:60.4986563560,planLegTime:0.1114074329,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1TestLegFlight13() {
      [[planTrueTrack:89.0,planTestDistance:10.69,planTrueHeading:82.6636259635,planGroundSpeed:82.4298858593,planLegTime:0.1296859736,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:89.0,planTestDistance:5.51,planTrueHeading:82.6636259635,planGroundSpeed:82.4298858593,planLegTime:0.0668446880,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:4.97,planTrueHeading:231.2189175362,planGroundSpeed:66.0677077289,planLegTime:0.0752258580,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:12.47,planTrueHeading:231.2189175362,planGroundSpeed:66.0677077289,planLegTime:0.1887457644,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:7.40,planTrueHeading:169.0816457025,planGroundSpeed:80.6254557575,planLegTime:0.0917824269,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:6.10,planTrueHeading:169.0816457025,planGroundSpeed:80.6254557575,planLegTime:0.0756584870,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:2.11,planTrueHeading:79.1178787047,planGroundSpeed:81.9311982840,planLegTime:0.0257533155,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:9.39,planTrueHeading:79.1178787047,planGroundSpeed:81.9311982840,planLegTime:0.1146083567,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:2.75,planTrueHeading:248.0070611206,planGroundSpeed:61.9023987550,planLegTime:0.0444247728,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:8.61,planTrueHeading:248.0070611206,planGroundSpeed:61.9023987550,planLegTime:0.1390899250,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:254.2329616616,planGroundSpeed:60.4986563560,planLegTime:0.1114074329,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:0,resultTestDistance:0,resultTrueHeading:0,resultGroundSpeed:0,resultLegTime:0,
        resultEntered:false,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test1CoordResult3() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:21:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,17190'",resultLongitude:"E 013\u00b0 44,23030'",resultAltitude:237,
        resultCpTime:"10:21:09",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:29:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,84870'",resultLongitude:"E 013\u00b0 49,21010'",resultAltitude:1375,
        resultCpTime:"10:29:03",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:35:35",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,29710'",resultLongitude:"E 014\u00b0 06,67800'",resultAltitude:1409,
        resultCpTime:"10:35:46",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:9
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:38:58",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,24970'",resultLongitude:"E 014\u00b0 15,53960'",resultAltitude:1609,
        resultCpTime:"10:38:56",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:43:38",planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,29270'",resultLongitude:"E 014\u00b0 10,54200'",resultAltitude:1399,
        resultCpTime:"10:43:39",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:52:50",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,69550'",resultLongitude:"E 013\u00b0 57,68880'",resultAltitude:1629,
        resultCpTime:"10:52:55",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:57:28",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,68110'",resultLongitude:"E 014\u00b0 01,81410'",resultAltitude:1496,
        resultCpTime:"10:57:41",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:11
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:01:17",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,88340'",resultLongitude:"E 014\u00b0 04,96680'",resultAltitude:1569,
        resultCpTime:"11:01:24",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:5
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:02:35",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,11140'",resultLongitude:"E 014\u00b0 08,27210'",resultAltitude:1707,
        resultCpTime:"11:02:55",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:18
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:08:23",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,52840'",resultLongitude:"E 014\u00b0 23,41510'",resultAltitude:1523,
        resultCpTime:"11:08:23",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:11:31",planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 37,97560'",resultLongitude:"E 014\u00b0 19,65030'",resultAltitude:1387,
        resultCpTime:"11:11:21",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:8
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:18:13",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,44220'",resultLongitude:"E 014\u00b0 08,05000'",resultAltitude:1615,
        resultCpTime:"11:18:17",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:23:34",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,34350'",resultLongitude:"E 013\u00b0 58,49930'",resultAltitude:1358,
        resultCpTime:"11:23:37",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:33:34",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,52040'",resultLongitude:"E 013\u00b0 52,92050'",resultAltitude:337,
        resultCpTime:"11:27:19",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
	
    List test1CoordResult18() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:03:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,17410'",resultLongitude:"E 013\u00b0 44,23230'",resultAltitude:227,
        resultCpTime:"13:03:07",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:11:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,83500'",resultLongitude:"E 013\u00b0 49,22100'",resultAltitude:1170,
        resultCpTime:"13:11:03",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:17:56",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,17800'",resultLongitude:"E 014\u00b0 06,68980'",resultAltitude:1236,
        resultCpTime:"13:17:55",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:21:30",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,13820'",resultLongitude:"E 014\u00b0 15,54840'",resultAltitude:1290,
        resultCpTime:"13:21:32",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:26:25",planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,27670'",resultLongitude:"E 014\u00b0 10,60120'",resultAltitude:1198,
        resultCpTime:"13:26:27",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:36:14",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,74450'",resultLongitude:"E 013\u00b0 57,59730'",resultAltitude:965,
        resultCpTime:"13:36:19",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:41:08",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,66360'",resultLongitude:"E 014\u00b0 01,74150'",resultAltitude:1225,
        resultCpTime:"13:41:07",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:45:10",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,83570'",resultLongitude:"E 014\u00b0 04,76570'",resultAltitude:1395,
        resultCpTime:"13:45:10",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:46:33",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,90780'",resultLongitude:"E 014\u00b0 08,31320'",resultAltitude:1405,
        resultCpTime:"13:46:41",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:6
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:52:40",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,52900'",resultLongitude:"E 014\u00b0 23,38740'",resultAltitude:2166,
        resultCpTime:"13:52:44",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:55:57",planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 38,01820'",resultLongitude:"E 014\u00b0 19,59790'",resultAltitude:1822,
        resultCpTime:"13:55:56",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"14:03:07",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,42810'",resultLongitude:"E 014\u00b0 08,03690'",resultAltitude:1384,
        resultCpTime:"14:03:09",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"14:08:50",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,43140'",resultLongitude:"E 013\u00b0 58,42820'",resultAltitude:1502,
        resultCpTime:"14:08:53",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"14:18:50",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,51960'",resultLongitude:"E 013\u00b0 52,90880'",resultAltitude:306,
        resultCpTime:"14:12:03",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
	
    List test1CoordResult19() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:06:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,17160'",resultLongitude:"E 013\u00b0 44,23470'",resultAltitude:205,
        resultCpTime:"12:06:05",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:14:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,00160'",resultLongitude:"E 013\u00b0 49,20850'",resultAltitude:1001,
        resultCpTime:"12:13:37",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:21
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:20:56",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,29600'",resultLongitude:"E 014\u00b0 06,67890'",resultAltitude:1490,
        resultCpTime:"12:20:40",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:14
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:24:30",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,37350'",resultLongitude:"E 014\u00b0 15,56770'",resultAltitude:1595,
        resultCpTime:"12:24:13",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:15
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:29:25",planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,12430'",resultLongitude:"E 014\u00b0 10,91000'",resultAltitude:1412,
        resultCpTime:"12:29:45",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:18
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:39:14",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,53060'",resultLongitude:"E 013\u00b0 58,04620'",resultAltitude:1435,
        resultCpTime:"12:39:32",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:16
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:44:08",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,66070'",resultLongitude:"E 014\u00b0 01,74090'",resultAltitude:1923,
        resultCpTime:"12:44:21",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:11
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:48:10",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,90240'",resultLongitude:"E 014\u00b0 05,07120'",resultAltitude:2058,
        resultCpTime:"12:48:05",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:49:33",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,64160'",resultLongitude:"E 014\u00b0 08,33550'",resultAltitude:1687,
        resultCpTime:"12:49:46",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:11
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:55:40",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,61940'",resultLongitude:"E 014\u00b0 23,36750'",resultAltitude:1774,
        resultCpTime:"12:55:56",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:14
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:58:57",planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 37,31980'",resultLongitude:"E 014\u00b0 20,31900'",resultAltitude:2359,
        resultCpTime:"12:58:58",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:06:07",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 32,96050'",resultLongitude:"E 014\u00b0 08,53140'",resultAltitude:1775,
        resultCpTime:"13:06:06",
        resultCpNotFound:true,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:200
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:11:50",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,21180'",resultLongitude:"E 013\u00b0 58,60650'",resultAltitude:1291,
        resultCpTime:"13:12:06",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:14
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"13:21:50",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,52080'",resultLongitude:"E 013\u00b0 52,91520'",resultAltitude:302,
        resultCpTime:"13:16:54",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
	
    List test1CoordResult11() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:45:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,17200'",resultLongitude:"E 013\u00b0 44,23130'",resultAltitude:209,
        resultCpTime:"10:45:17",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:53:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,66460'",resultLongitude:"E 013\u00b0 49,22740'",resultAltitude:1678,
        resultCpTime:"10:53:04",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:00:47",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,93310'",resultLongitude:"E 014\u00b0 06,67050'",resultAltitude:1816,
        resultCpTime:"11:00:57",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:8
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:04:48",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,15430'",resultLongitude:"E 014\u00b0 15,57610'",resultAltitude:1622,
        resultCpTime:"11:04:54",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:4
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:10:19",planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,22360'",resultLongitude:"E 014\u00b0 10,69030'",resultAltitude:1659,
        resultCpTime:"11:10:38",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:17
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:21:38",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,80050'",resultLongitude:"E 013\u00b0 57,51850'",resultAltitude:1571,
        resultCpTime:"11:21:46",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:6
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:27:08",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,63940'",resultLongitude:"E 014\u00b0 01,71880'",resultAltitude:1970,
        resultCpTime:"11:27:10",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:31:40",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,83070'",resultLongitude:"E 014\u00b0 04,75630'",resultAltitude:1827,
        resultCpTime:"11:31:46",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:4
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:33:13",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 37,95600'",resultLongitude:"E 014\u00b0 08,42100'",resultAltitude:1675,
        resultCpTime:"11:33:26",
        resultCpNotFound:true,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:200
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:40:06",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,39500'",resultLongitude:"E 014\u00b0 23,40800'",resultAltitude:1839,
        resultCpTime:"11:40:09",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:43:46",planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 38,01920'",resultLongitude:"E 014\u00b0 19,61850'",resultAltitude:2007,
        resultCpTime:"11:43:44",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:52:07",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,47440'",resultLongitude:"E 014\u00b0 07,98900'",resultAltitude:1574,
        resultCpTime:"11:52:14",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:5
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:58:48",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,43450'",resultLongitude:"E 013\u00b0 58,43000'",resultAltitude:1644,
        resultCpTime:"11:58:55",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:5
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:08:48",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,52090'",resultLongitude:"E 013\u00b0 52,91070'",resultAltitude:318,
        resultCpTime:"12:02:49",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
	
    List test1CoordResult13() {
      [[type:CoordType.TO,    mark:"T/O", latGrad:52,latMinute:2.1707,latDirection:'N',lonGrad:13,lonMinute:44.2321,lonDirection:'E',altitude:180,gatewidth2:0.01f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:51:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 02,17260'",resultLongitude:"E 013\u00b0 44,23400'",resultAltitude:214,
        resultCpTime:"10:51:06",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SP,    mark:"SP",  latGrad:52,latMinute:4.897,latDirection:'N',lonGrad:13,lonMinute:49.207,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"10:59:00",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 04,81010'",resultLongitude:"E 013\u00b0 49,21410'",resultAltitude:985,
        resultCpTime:"10:58:59",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP1", latGrad:52,latMinute:5.121,latDirection:'N',lonGrad:14,lonMinute:6.679,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:06:47",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,00180'",resultLongitude:"E 014\u00b0 06,69910'",resultAltitude:1293,
        resultCpTime:"11:06:50",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:1
       ],
       [type:CoordType.TP,    mark:"CP2", latGrad:52,latMinute:5.223,latDirection:'N',lonGrad:14,lonMinute:15.555,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:10:48",planProcedureTurn:false,
        resultLatitude:"N 052\u00b0 05,15960'",resultLongitude:"E 014\u00b0 15,55880'",resultAltitude:1240,
        resultCpTime:"11:10:53",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:3
       ],
       [type:CoordType.SECRET,mark:"CP3", latGrad:52,latMinute:1.367,latDirection:'N',lonGrad:14,lonMinute:10.417,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:16:19",planProcedureTurn:true,
        resultLatitude:"N 052\u00b0 01,26100'",resultLongitude:"E 014\u00b0 10,64230'",resultAltitude:1281,
        resultCpTime:"11:16:09",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:8
       ],
       [type:CoordType.TP,    mark:"CP4", latGrad:51,latMinute:51.719,latDirection:'N',lonGrad:13,lonMinute:57.662,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:27:38",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 51,74850'",resultLongitude:"E 013\u00b0 57,59490'",resultAltitude:1424,
        resultCpTime:"11:27:38",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP5", latGrad:51,latMinute:44.633,latDirection:'N',lonGrad:14,lonMinute:1.635,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:33:08",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 44,63130'",resultLongitude:"E 014\u00b0 01,65960'",resultAltitude:1307,
        resultCpTime:"11:32:51",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:15
       ],
       [type:CoordType.TP,    mark:"CP6", latGrad:51,latMinute:38.847,latDirection:'N',lonGrad:14,lonMinute:4.857,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:37:40",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,85720'",resultLongitude:"E 014\u00b0 04,88720'",resultAltitude:1149,
        resultCpTime:"11:37:38",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.SECRET,mark:"CP7", latGrad:51,latMinute:38.983,latDirection:'N',lonGrad:14,lonMinute:8.299,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:39:13",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 38,76200'",resultLongitude:"E 014\u00b0 08,31770'",resultAltitude:995,
        resultCpTime:"11:39:25",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:10
       ],
       [type:CoordType.TP,    mark:"CP8", latGrad:51,latMinute:39.535,latDirection:'N',lonGrad:14,lonMinute:23.4,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:46:06",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 39,46530'",resultLongitude:"E 014\u00b0 23,41820'",resultAltitude:895,
        resultCpTime:"11:46:10",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.SECRET,mark:"CP9", latGrad:51,latMinute:38.02,latDirection:'N',lonGrad:14,lonMinute:19.606,lonDirection:'E',altitude:500,gatewidth2:2.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:49:46",planProcedureTurn:true,
        resultLatitude:"N 051\u00b0 37,98390'",resultLongitude:"E 014\u00b0 19,65190'",resultAltitude:1212,
        resultCpTime:"11:49:50",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:true,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:2
       ],
       [type:CoordType.TP,    mark:"CP10",latGrad:51,latMinute:33.399,latDirection:'N',lonGrad:14,lonMinute:8.079,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"11:58:07",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 33,27590'",resultLongitude:"E 014\u00b0 08,20140'",resultAltitude:1389,
        resultCpTime:"11:58:13",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:4
       ],
       [type:CoordType.FP,    mark:"FP",  latGrad:51,latMinute:30.353,latDirection:'N',lonGrad:13,lonMinute:58.485,lonDirection:'E',altitude:500,gatewidth2:1.0,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:04:48",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 30,42410'",resultLongitude:"E 013\u00b0 58,43490'",resultAltitude:1299,
        resultCpTime:"12:04:50",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
       [type:CoordType.LDG,   mark:"LDG", latGrad:51,latMinute:29.5058,latDirection:'N',lonGrad:13,lonMinute:52.8361,lonDirection:'E',altitude:300,gatewidth2:0.02f,
        legMeasureDistance:null,legDistance:null,measureTrueTrack:null,secretLegRatio:0,
        planCpTime:"12:14:48",planProcedureTurn:false,
        resultLatitude:"N 051\u00b0 29,51970'",resultLongitude:"E 013\u00b0 52,91080'",resultAltitude:318,
        resultCpTime:"12:12:36",
        resultCpNotFound:false,resultBadCourseNum:0,
        resultProcedureTurnNotFlown:false,resultProcedureTurnEntered:false,resultMinAltitudeMissed:false,
        resultEntered:true,penaltyCoord:0
       ],
      ]
    }
	
    List test1Test() {
      [[crew:[name:"Besatzung 3"],viewpos:0,taskTAS:85,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"09:06:00",
        endTestingTime:"10:06:00",
        takeoffTime:"10:21:00",
        startTime:"10:29:00",
        finishTime:"11:23:34",
        maxLandingTime:"11:33:34",
        arrivalTime:"11:38:34",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:58,flightTestPenalties:58,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:140,
        taskPenalties:218,taskPosition:3
       ],
       [crew:[name:"Besatzung 18"],viewpos:4,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"11:48:00",
        endTestingTime:"12:48:00",
        takeoffTime:"13:03:00",
        startTime:"13:11:00",
        finishTime:"14:08:50",
        maxLandingTime:"14:18:50",
        arrivalTime:"14:23:50",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:13,flightTestPenalties:13,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:10,landingTestPenalties:110,
        taskPenalties:133,taskPosition:1
       ],
       [crew:[name:"Besatzung 19"],viewpos:3,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"10:51:00",
        endTestingTime:"11:51:00",
        takeoffTime:"12:06:00",
        startTime:"12:14:00",
        finishTime:"13:11:50",
        maxLandingTime:"13:21:50",
        arrivalTime:"13:26:50",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:21,planningTestPenalties:21,
        flightTestCheckPointPenalties:337,flightTestPenalties:337,
        observationTestRoutePhotoPenalties:120,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:130,landingTestPenalties:80,
        taskPenalties:568,taskPosition:5
       ],
       [crew:[name:"Besatzung 11"],viewpos:1,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"09:30:00",
        endTestingTime:"10:30:00",
        takeoffTime:"10:45:00",
        startTime:"10:53:00",
        finishTime:"11:58:48",
        maxLandingTime:"12:08:48",
        arrivalTime:"12:13:48",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:2,planningTestPenalties:2,
        flightTestCheckPointPenalties:252,flightTestPenalties:252,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:0,landingTestPenalties:130,
        taskPenalties:384,taskPosition:4
       ],
       [crew:[name:"Besatzung 13"],viewpos:2,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"09:36:00",
        endTestingTime:"10:36:00",
        takeoffTime:"10:51:00",
        startTime:"10:59:00",
        finishTime:"12:04:48",
        maxLandingTime:"12:14:48",
        arrivalTime:"12:19:48",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:45,flightTestPenalties:45,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:70,
        taskPenalties:135,taskPosition:2
       ],
      ]
    }
    
    List test2Crew() {
      [[startNum:3, name:"Besatzung 3", email:"crew3.fc@localhost", mark:"",team:[name:"Deutschland"],resultclass:[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],tas:85,contestPenalties:58, classPosition:2,noClassPosition:false,contestPosition:2,noContestPosition:false,aircraft:[registration:"D-EAAA",type:"",colour:""]],
       [startNum:18,name:"Besatzung 18",email:"crew18.fc@localhost",mark:"",team:[name:"Deutschland"],resultclass:[name:"Tourist",   shortName:"T",contestTitle:""],                                tas:80,contestPenalties:120,classPosition:1,noClassPosition:false,contestPosition:0,noContestPosition:true,aircraft:[registration:"D-EAAD",type:"",colour:""]],
       [startNum:19,name:"Besatzung 19",email:"crew19.fc@localhost",mark:"",team:[name:"Deutschland"],resultclass:[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],tas:80,contestPenalties:358,classPosition:3,noClassPosition:false,contestPosition:3,noContestPosition:false,aircraft:[registration:"D-EAAE",type:"",colour:""]],
       [startNum:11,name:"Besatzung 11",email:"crew11.fc@localhost",mark:"",team:[name:"Schweiz"],    resultclass:[name:"Tourist",   shortName:"T",contestTitle:""],                                tas:70,contestPenalties:130,classPosition:2,noClassPosition:false,contestPosition:0,noContestPosition:true,aircraft:[registration:"D-EAAB",type:"",colour:""]],
       [startNum:13,name:"Besatzung 13",email:"crew13.fc@localhost",mark:"",team:[name:"Schweiz"],    resultclass:[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],tas:70,contestPenalties:45, classPosition:1,noClassPosition:false,contestPosition:1,noContestPosition:false,aircraft:[registration:"D-EAAC",type:"",colour:""]],
      ]
    }
	
    List test2ResultClass() {
      [[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],
       [name:"Tourist",   shortName:"T",contestTitle:""],
       [name:"Observer",  shortName:"O",contestTitle:""],
      ]
    }
	
    List test2Team() {
      [[name:"Deutschland",contestPenalties:178,contestPosition:2],
       [name:"Schweiz",    contestPenalties:175,contestPosition:1],
       [name:'Polen',      contestPenalties:-1, contestPosition:0],
      ]    
	}
	
    List test2TaskClass() {
      [[resultclass:[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],planningTestRun:true,flightTestRun:true,observationTestRun:false,landingTestRun:false,specialTestRun:false,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
       [resultclass:[name:"Tourist",shortName:"T",contestTitle:""],planningTestRun:false,flightTestRun:false,observationTestRun:true,landingTestRun:true,specialTestRun:true,planningTestDistanceMeasure:true,planningTestDirectionMeasure:false],
       [resultclass:[name:"Observer",shortName:"O",contestTitle:""],planningTestRun:true,flightTestRun:true,observationTestRun:true,landingTestRun:true,specialTestRun:false,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
      ]
    }
	
    List test2TestLegPlanning18() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:98.4400045050,planGroundSpeed:63.8224392169,planLegTime:0.2538292205,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:89.0,resultTestDistance:0,resultTrueHeading:98,resultGroundSpeed:0,resultLegTime:0.2530555556,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:204.5247409558,planGroundSpeed:77.1114052303,planLegTime:0.2261662843,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:219.0,resultTestDistance:0,resultTrueHeading:204,resultGroundSpeed:0,resultLegTime:0.2272222222,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:153.6020844648,planGroundSpeed:62.1907217788,planLegTime:0.2170741811,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:161.0,resultTestDistance:0,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2175,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:96.0009550175,planGroundSpeed:64.3975926711,planLegTime:0.1785781040,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:86.0,resultTestDistance:0,resultTrueHeading:96,resultGroundSpeed:0,resultLegTime:0.1786111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:223.1679772858,planGroundSpeed:83.5274990113,planLegTime:0.1360031144,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:237.0,resultTestDistance:0,resultTrueHeading:223,resultGroundSpeed:0,resultLegTime:0.1361111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:230.7979111274,planGroundSpeed:86.0203790277,planLegTime:0.0783535259,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:244.0,resultTestDistance:0,resultTrueHeading:230,resultGroundSpeed:0,resultLegTime:0.0788888889,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test2TestLegPlanning11() {
      [[planTrueTrack:89.0,planTestDistance:16.20,planTrueHeading:99.8037402076,planGroundSpeed:53.6650595563,planLegTime:0.3018723940,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:89.0,resultTestDistance:0,resultTrueHeading:99,resultGroundSpeed:0,resultLegTime:0.3,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:2
       ],
       [planTrueTrack:219.0,planTestDistance:17.44,planTrueHeading:202.4010521006,planGroundSpeed:66.7338992916,planLegTime:0.2613364450,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:219.0,resultTestDistance:0,resultTrueHeading:203,resultGroundSpeed:0,resultLegTime:0.2622222222,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:161.0,planTestDistance:13.50,planTrueHeading:152.5379827855,planGroundSpeed:52.0946082636,planLegTime:0.2591439009,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:161.0,resultTestDistance:0,resultTrueHeading:153,resultGroundSpeed:0,resultLegTime:0.2586111111,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:86.0,planTestDistance:11.50,planTrueHeading:97.4477263633,planGroundSpeed:54.2206372592,planLegTime:0.2120963637,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:86.0,resultTestDistance:0,resultTrueHeading:97,resultGroundSpeed:0,resultLegTime:0.2116666667,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:237.0,planTestDistance:11.36,planTrueHeading:221.1434427012,planGroundSpeed:73.1838468701,planLegTime:0.1552255106,
        planProcedureTurn:true,planProcedureTurnDuration:1,
        resultTrueTrack:237.0,resultTestDistance:0,resultTrueHeading:221,resultGroundSpeed:0,resultLegTime:0.1558333333,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
       [planTrueTrack:244.0,planTestDistance:6.74,planTrueHeading:228.8698222549,planGroundSpeed:75.7082030901,planLegTime:0.0890260200,
        planProcedureTurn:false,planProcedureTurnDuration:0,
        resultTrueTrack:244.0,resultTestDistance:0,resultTrueHeading:229,resultGroundSpeed:0,resultLegTime:0.0888888889,
        resultEntered:true,penaltyTrueHeading:0,penaltyLegTime:0
       ],
      ]
    }
	
    List test2Test() {
      [[crew:[name:"Besatzung 3"],viewpos:0,taskTAS:85,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"09:06:00",
        endTestingTime:"10:06:00",
        takeoffTime:"10:21:00",
        startTime:"10:29:00",
        finishTime:"11:23:34",
        maxLandingTime:"11:33:34",
        arrivalTime:"11:38:34",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:58,flightTestPenalties:58,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:140,
        taskPenalties:58,taskPosition:2
       ],
       [crew:[name:"Besatzung 18"],viewpos:4,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"11:48:00",
        endTestingTime:"12:48:00",
        takeoffTime:"13:03:00",
        startTime:"13:11:00",
        finishTime:"14:08:50",
        maxLandingTime:"14:18:50",
        arrivalTime:"14:23:50",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:13,flightTestPenalties:13,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:10,landingTestPenalties:110,
        taskPenalties:120,taskPosition:1
       ],
       [crew:[name:"Besatzung 19"],viewpos:3,taskTAS:80,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"10:51:00",
        endTestingTime:"11:51:00",
        takeoffTime:"12:06:00",
        startTime:"12:14:00",
        finishTime:"13:11:50",
        maxLandingTime:"13:21:50",
        arrivalTime:"13:26:50",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:21,planningTestPenalties:21,
        flightTestCheckPointPenalties:337,flightTestPenalties:337,
        observationTestRoutePhotoPenalties:120,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:10,observationTestPenalties:130,landingTestPenalties:80,
        taskPenalties:358,taskPosition:3
       ],
       [crew:[name:"Besatzung 11"],viewpos:1,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"09:30:00",
        endTestingTime:"10:30:00",
        takeoffTime:"10:45:00",
        startTime:"10:53:00",
        finishTime:"11:58:48",
        maxLandingTime:"12:08:48",
        arrivalTime:"12:13:48",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:2,planningTestPenalties:2,
        flightTestCheckPointPenalties:252,flightTestPenalties:252,
        observationTestRoutePhotoPenalties:0,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:0,landingTestPenalties:130,
        taskPenalties:130,taskPosition:2
       ],
       [crew:[name:"Besatzung 13"],viewpos:2,taskTAS:70,
        flighttestwind:[wind:[direction:300,speed:15]],
        timeCalculated:true,
        testingTime:"09:36:00",
        endTestingTime:"10:36:00",
        takeoffTime:"10:51:00",
        startTime:"10:59:00",
        finishTime:"12:04:48",
        maxLandingTime:"12:14:48",
        arrivalTime:"12:19:48",
        arrivalTimeWarning:false,takeoffTimeWarning:false,
        planningTestGivenTooLate:false,planningTestExitRoomTooLate:false,
        planningTestLegComplete:true,planningTestComplete:true,
        flightTestTakeoffMissed:false,flightTestBadCourseStartLanding:false,
        flightTestLandingTooLate:false,flightTestGivenTooLate:false,
		flightTestSafetyAndRulesInfringement:false, flightTestInstructionsNotFollowed:false,
		flightTestFalseEnvelopeOpened:false,flightTestSafetyEnvelopeOpened:false,flightTestFrequencyNotMonitored:false,
        flightTestCheckPointsComplete:true,flightTestComplete:true,
        planningTestLegPenalties:0,planningTestPenalties:0,
        flightTestCheckPointPenalties:45,flightTestPenalties:45,
        observationTestRoutePhotoPenalties:20,observationTestTurnPointPhotoPenalties:0,
        observationTestGroundTargetPenalties:0,observationTestPenalties:20,landingTestPenalties:70,
        taskPenalties:45,taskPosition:1
       ],
      ]
    }
    
    List test3Team() {
      [[name:"Deutschland",contestPenalties:175,contestPosition:1],
       [name:"Schweiz",    contestPenalties:175,contestPosition:1],
      ]    
	}
	
    List test3TaskClass() {
      [[resultclass:[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],planningTestRun:true,flightTestRun:true,observationTestRun:false,landingTestRun:false,specialTestRun:false,planningTestDistanceMeasure:false,planningTestDirectionMeasure:true],
       [resultclass:[name:"Tourist",shortName:"T",contestTitle:""],planningTestRun:false,flightTestRun:false,observationTestRun:true,landingTestRun:true,specialTestRun:true,planningTestDistanceMeasure:true,planningTestDirectionMeasure:false],
      ]
    }
	
	List test3ResultClass() {
		[[name:"Pr\u00E4zi",shortName:"P",contestTitle:"Pr\u00E4zisionsflugmeisterschaft"],
		 [name:"Tourist",shortName:"T",contestTitle:""],
		]
    }
}
