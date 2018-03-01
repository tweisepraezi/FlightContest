import java.util.List;

enum ContestRules 
{
	R1 ('fc.contestrule.de.nav.2018',
		[
			// General
			precisionFlying:false,
            increaseFactor:0,
            printPointsGeneral:false,
			printPointsPlanningTest:false,
			printPointsFlightTest:true,
            printPointsObservationTest:true,
			printPointsLandingTest1:true,
			printPointsLandingTest2:false,
			printPointsLandingTest3:false,
			printPointsLandingTest4:false,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/de_nav.jpg',
            printPointsTurnpointSign:false,
            printPointsEnrouteCanvas:false,
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,
			planningTestDirectionPointsPerGrad:2,
			planningTestTimeCorrectSecond:2,
			planningTestTimePointsPerSecond:3,
			planningTestMaxPoints:100,
			planningTestPlanTooLatePoints:50,
			planningTestExitRoomTooLatePoints:100,
            planningTestForbiddenCalculatorsPoints:0,
		
			// FlightTest
			flightTestTakeoffCorrectSecond:60,                   // 4
			flightTestTakeoffCheckSeconds:true,
			flightTestTakeoffPointsPerSecond:3,                  // 4
            flightTestTakeoffMissedPoints:100,                   // 4
			flightTestCptimeCorrectSecond:2,                     // 4
			flightTestCptimePointsPerSecond:3,                   // 4
			flightTestCptimeMaxPoints:100,                       // 4
			flightTestCpNotFoundPoints:100,                      // 4
			flightTestProcedureTurnNotFlownPoints:0,
			flightTestMinAltitudeMissedPoints:200,               // 4
			flightTestBadCourseCorrectSecond:5,                  // 3.3.6
			flightTestBadCoursePoints:100,                       // 4
            flightTestBadCourseMaxPoints:1000,                   // 4
			flightTestBadCourseStartLandingPoints:200,           // 4
			flightTestLandingToLatePoints:200,                   // 4
			flightTestGivenToLatePoints:300,                     // 4
			flightTestSafetyAndRulesInfringementPoints:0,
			flightTestInstructionsNotFollowedPoints:0,
			flightTestFalseEnvelopeOpenedPoints:0,
			flightTestSafetyEnvelopeOpenedPoints:400,            // 4
			flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,
		
            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,           // 4
            observationTestTurnpointFalsePoints:100,             // 4
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM, // 4
            observationTestEnrouteCorrectValue:0.5f,             // 4
            observationTestEnrouteInexactValue:1.0f,             // 4
            observationTestEnrouteInexactPoints:15,              // 4
            observationTestEnrouteNotFoundPoints:30,             // 4
            observationTestEnrouteFalsePoints:50,                // 4
            
			// LandingTest
			landingTest1MaxPoints:300,                           // 4
			landingTest1NoLandingPoints:300,                     // 4
			landingTest1OutsideLandingPoints:200,                // 4
			landingTest1RollingOutsidePoints:200,                // 4
			landingTest1PowerInBoxPoints:50,                     // 4
			landingTest1GoAroundWithoutTouchingPoints:200,       // 4
			landingTest1GoAroundInsteadStopPoints:200,           // 4
			landingTest1AbnormalLandingPoints:150,               // 4
			landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest1PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 4
		
			landingTest2MaxPoints:300,                           // 4
			landingTest2NoLandingPoints:300,                     // 4
			landingTest2OutsideLandingPoints:200,                // 4
			landingTest2RollingOutsidePoints:200,                // 4
			landingTest2PowerInBoxPoints:50,                     // 4
			landingTest2GoAroundWithoutTouchingPoints:200,       // 4
			landingTest2GoAroundInsteadStopPoints:200,           // 4
			landingTest2AbnormalLandingPoints:150,               // 4
			landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest2PowerInAirPoints:0,
			landingTest2PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 4
			
			landingTest3MaxPoints:300,                           // 4
			landingTest3NoLandingPoints:300,                     // 4
			landingTest3OutsideLandingPoints:200,                // 4
			landingTest3RollingOutsidePoints:200,                // 4
			landingTest3PowerInBoxPoints:50,                     // 4
			landingTest3GoAroundWithoutTouchingPoints:200,       // 4
			landingTest3GoAroundInsteadStopPoints:200,           // 4
			landingTest3AbnormalLandingPoints:150,               // 4
			landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest3PowerInAirPoints:0,
			landingTest3FlapsInAirPoints:0,
			landingTest3PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 4
			
			landingTest4MaxPoints:300,                           // 4
			landingTest4NoLandingPoints:300,                     // 4
			landingTest4OutsideLandingPoints:200,                // 4
			landingTest4RollingOutsidePoints:200,                // 4
			landingTest4PowerInBoxPoints:50,                     // 4
			landingTest4GoAroundWithoutTouchingPoints:200,       // 4
			landingTest4GoAroundInsteadStopPoints:200,           // 4
			landingTest4AbnormalLandingPoints:150,               // 4
			landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest4TouchingObstaclePoints:0,
			landingTest4PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 4
            
            // Defaults
            printStyle:"body {--flightplan: hide-distance hide-truetrack hide-trueheading disable-local-time show-elapsed-time; }\nbody {--submission: 20; }",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.TrueFalsePhoto,          // 3.4.6
            turnpointMapMeasurement:false,
            enroutePhotoRule:EnrouteRule.NMFromTP,               // 3.4.8
            enrouteCanvasRule:EnrouteRule.NMFromTP,              // 3.4.8
            enrouteCanvasMultiple:false,
            minRouteLegs:10,                                     // 3.1
            maxRouteLegs:16,                                     // 3.1
            minEnroutePhotos:15,                                 // 3.4.1
            maxEnroutePhotos:20,                                 // 3.4.1
            minEnrouteCanvas:0,                                  // 3.4.1
            maxEnrouteCanvas:5,                                  // 3.4.1
            minEnrouteTargets:10,                                // (3.4.1)
            maxEnrouteTargets:25                                 // (3.4.1)
		]
	), // fc.contestrule.de.nav.2018

	R2 ('fc.contestrule.de.precision.2005',
		[
			// General
			precisionFlying:true,
            increaseFactor:0,
            printPointsGeneral:false,
			printPointsPlanningTest:true,
			printPointsFlightTest:true,
            printPointsObservationTest:true,
			printPointsLandingTest1:true,
			printPointsLandingTest2:true,
			printPointsLandingTest3:true,
			printPointsLandingTest4:true,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/de_precision.jpg',
            printPointsTurnpointSign:true,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,
			planningTestDirectionPointsPerGrad:2,
			planningTestTimeCorrectSecond:5,
			planningTestTimePointsPerSecond:1,
			planningTestMaxPoints:350,
			planningTestPlanTooLatePoints:50,
			planningTestExitRoomTooLatePoints:100,
            planningTestForbiddenCalculatorsPoints:0,
		
			// FlightTest
			flightTestTakeoffCorrectSecond:60,
			flightTestTakeoffCheckSeconds:false,
			flightTestTakeoffPointsPerSecond:0,
            flightTestTakeoffMissedPoints:200,
			flightTestCptimeCorrectSecond:2,
			flightTestCptimePointsPerSecond:3,
			flightTestCptimeMaxPoints:200,
			flightTestCpNotFoundPoints:200,
			flightTestProcedureTurnNotFlownPoints:200,
			flightTestMinAltitudeMissedPoints:500,
			flightTestBadCourseCorrectSecond:5,
			flightTestBadCoursePoints:200,
            flightTestBadCourseMaxPoints:0,
			flightTestBadCourseStartLandingPoints:200,
			flightTestLandingToLatePoints:200,
			flightTestGivenToLatePoints:100,
			flightTestSafetyAndRulesInfringementPoints:0,
			flightTestInstructionsNotFollowedPoints:0,
			flightTestFalseEnvelopeOpenedPoints:0,
			flightTestSafetyEnvelopeOpenedPoints:0,
			flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,
            observationTestTurnpointFalsePoints:100,
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,
            observationTestEnrouteCorrectValue:5.0f,
            observationTestEnrouteInexactValue:0.0f,
            observationTestEnrouteInexactPoints:0,
            observationTestEnrouteNotFoundPoints:20,
            observationTestEnrouteFalsePoints:30,
            
			// LandingTest
			landingTest1MaxPoints:400,
			landingTest1NoLandingPoints:200,
			landingTest1OutsideLandingPoints:200,
			landingTest1RollingOutsidePoints:200,
			landingTest1PowerInBoxPoints:50,
			landingTest1GoAroundWithoutTouchingPoints:0,
			landingTest1GoAroundInsteadStopPoints:0,
			landingTest1AbnormalLandingPoints:150,
			landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest1PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(7*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 175;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}",
            
			landingTest2MaxPoints:200,
			landingTest2NoLandingPoints:150,
			landingTest2OutsideLandingPoints:150,
			landingTest2RollingOutsidePoints:150,
			landingTest2PowerInBoxPoints:50,
			landingTest2GoAroundWithoutTouchingPoints:0,
			landingTest2GoAroundInsteadStopPoints:0,
			landingTest2AbnormalLandingPoints:150,
			landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest2PowerInAirPoints:200,
			landingTest2PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 100;case 'D':return 50;case 'E':return 60;case 'F':return 70;case 'G':return 80;case 'H':return 90;default:return null;}}}",
			
			landingTest3MaxPoints:200,
			landingTest3NoLandingPoints:150,
			landingTest3OutsideLandingPoints:150,
			landingTest3RollingOutsidePoints:150,
			landingTest3PowerInBoxPoints:50,
			landingTest3GoAroundWithoutTouchingPoints:0,
			landingTest3GoAroundInsteadStopPoints:0,
			landingTest3AbnormalLandingPoints:150,
			landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest3PowerInAirPoints:200,
			landingTest3FlapsInAirPoints:200,
			landingTest3PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 100;case 'D':return 50;case 'E':return 60;case 'F':return 70;case 'G':return 80;case 'H':return 90;default:return null;}}}",
			
			landingTest4MaxPoints:400,
			landingTest4NoLandingPoints:200,
			landingTest4OutsideLandingPoints:200,
			landingTest4RollingOutsidePoints:200,
			landingTest4PowerInBoxPoints:50,
			landingTest4GoAroundWithoutTouchingPoints:0,
			landingTest4GoAroundInsteadStopPoints:0,
			landingTest4AbnormalLandingPoints:150,
			landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest4TouchingObstaclePoints:400,
			landingTest4PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(7*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 175;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}",
            
            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignCanvas,
            turnpointMapMeasurement:true,
            enroutePhotoRule:EnrouteRule.Map,
            enrouteCanvasRule:EnrouteRule.Map,
            enrouteCanvasMultiple:false,
            minRouteLegs:0,
            maxRouteLegs:8,
            minEnroutePhotos:8,
            maxEnroutePhotos:10,
            minEnrouteCanvas:8,
            maxEnrouteCanvas:15,
            minEnrouteTargets:16,
            maxEnrouteTargets:25
		]
	), // fc.contestrule.de.precision.2005

	R3 ('fc.contestrule.fai.rally.2016.unlimited',
		[
			// General
			precisionFlying:false,
            increaseFactor:0,
            printPointsGeneral:false,
			printPointsPlanningTest:false,
			printPointsFlightTest:true,
            printPointsObservationTest:true,
			printPointsLandingTest1:true,
			printPointsLandingTest2:false,
			printPointsLandingTest3:false,
			printPointsLandingTest4:false,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/fai_rally.jpg',
            printPointsTurnpointSign:false,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,
			planningTestDirectionPointsPerGrad:2,
			planningTestTimeCorrectSecond:5,
			planningTestTimePointsPerSecond:1,
			planningTestMaxPoints:350,
			planningTestPlanTooLatePoints:50,
			planningTestExitRoomTooLatePoints:100,
            planningTestForbiddenCalculatorsPoints:0,
		
			// FlightTest
			flightTestTakeoffCorrectSecond:60,
			flightTestTakeoffCheckSeconds:true,
			flightTestTakeoffPointsPerSecond:3,
            flightTestTakeoffMissedPoints:100,
			flightTestCptimeCorrectSecond:2,
			flightTestCptimePointsPerSecond:3,
			flightTestCptimeMaxPoints:100,
			flightTestCpNotFoundPoints:100,
			flightTestProcedureTurnNotFlownPoints:0,
			flightTestMinAltitudeMissedPoints:200,
			flightTestBadCourseCorrectSecond:5,
			flightTestBadCoursePoints:200,
            flightTestBadCourseMaxPoints:0,
			flightTestBadCourseStartLandingPoints:0,
			flightTestLandingToLatePoints:0,
			flightTestGivenToLatePoints:300,
			flightTestSafetyAndRulesInfringementPoints:600,
			flightTestInstructionsNotFollowedPoints:200,
			flightTestFalseEnvelopeOpenedPoints:100,
			flightTestSafetyEnvelopeOpenedPoints:300,
			flightTestFrequencyNotMonitoredPoints:200,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,
            observationTestTurnpointFalsePoints:100,
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM,
            observationTestEnrouteCorrectValue:0.5f,
            observationTestEnrouteInexactValue:1.0f,
            observationTestEnrouteInexactPoints:15,
            observationTestEnrouteNotFoundPoints:30,
            observationTestEnrouteFalsePoints:50,
            
			// LandingTest
			landingTest1MaxPoints:300,
			landingTest1NoLandingPoints:300,
			landingTest1OutsideLandingPoints:200,
			landingTest1RollingOutsidePoints:200,
			landingTest1PowerInBoxPoints:50,
			landingTest1GoAroundWithoutTouchingPoints:200,
			landingTest1GoAroundInsteadStopPoints:200,
			landingTest1AbnormalLandingPoints:150,
			landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest1PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}",
		
			landingTest2MaxPoints:300,
			landingTest2NoLandingPoints:300,
			landingTest2OutsideLandingPoints:200,
			landingTest2RollingOutsidePoints:200,
			landingTest2PowerInBoxPoints:50,
			landingTest2GoAroundWithoutTouchingPoints:200,
			landingTest2GoAroundInsteadStopPoints:200,
			landingTest2AbnormalLandingPoints:150,
			landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest2PowerInAirPoints:0,
			landingTest2PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}",
			
			landingTest3MaxPoints:300,
			landingTest3NoLandingPoints:300,
			landingTest3OutsideLandingPoints:200,
			landingTest3RollingOutsidePoints:200,
			landingTest3PowerInBoxPoints:50,
			landingTest3GoAroundWithoutTouchingPoints:200,
			landingTest3GoAroundInsteadStopPoints:200,
			landingTest3AbnormalLandingPoints:150,
			landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest3PowerInAirPoints:0,
			landingTest3FlapsInAirPoints:0,
			landingTest3PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}",
			
			landingTest4MaxPoints:300,
			landingTest4NoLandingPoints:300,
			landingTest4OutsideLandingPoints:200,
			landingTest4RollingOutsidePoints:200,
			landingTest4PowerInBoxPoints:50,
			landingTest4GoAroundWithoutTouchingPoints:200,
			landingTest4GoAroundInsteadStopPoints:200,
			landingTest4AbnormalLandingPoints:150,
			landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest4TouchingObstaclePoints:0,
			landingTest4PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}",

            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.TrueFalsePhoto,
            turnpointMapMeasurement:false,
            enroutePhotoRule:EnrouteRule.NMFromTP,
            enrouteCanvasRule:EnrouteRule.NMFromTP,
            enrouteCanvasMultiple:false,
            minRouteLegs:10,
            maxRouteLegs:16,
            minEnroutePhotos:15,
            maxEnroutePhotos:20,
            minEnrouteCanvas:0,
            maxEnrouteCanvas:5,
            minEnrouteTargets:15,
            maxEnrouteTargets:20
		]
	), // fc.contestrule.fai.rally.2016.unlimited

	R4 ('fc.contestrule.fai.precision.2018',
		[
			// General
			precisionFlying:true,
            increaseFactor:0,
            printPointsGeneral:false,
			printPointsPlanningTest:true,
			printPointsFlightTest:true,
            printPointsObservationTest:true,
			printPointsLandingTest1:true,
			printPointsLandingTest2:true,
			printPointsLandingTest3:true,
			printPointsLandingTest4:true,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/fai_precision1.jpg,images/landingfield/fai_precision2.jpg',
            printPointsTurnpointSign:true,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                   // A10.1.1
			planningTestDirectionPointsPerGrad:2,                 // A10.1.1
			planningTestTimeCorrectSecond:5,                      // A10.1.1
			planningTestTimePointsPerSecond:1,                    // A10.1.1
			planningTestMaxPoints:200,                            // A10.1.1
			planningTestPlanTooLatePoints:50,                     // A10.1.1
			planningTestExitRoomTooLatePoints:100,                // A10.1.1
            planningTestForbiddenCalculatorsPoints:0,
		
			// FlightTest
            flightTestTakeoffCorrectSecond:60,                    // A10.1.2
            flightTestTakeoffCheckSeconds:false,
            flightTestTakeoffPointsPerSecond:0,
			flightTestTakeoffMissedPoints:200,                    // A10.1.2
			flightTestCptimeCorrectSecond:2,                      // A10.1.3
			flightTestCptimePointsPerSecond:3,                    // A10.1.3
			flightTestCptimeMaxPoints:100,                        // A10.1.3
			flightTestCpNotFoundPoints:100,                       // A10.1.3
			flightTestProcedureTurnNotFlownPoints:200,            // A10.1.4
			flightTestMinAltitudeMissedPoints:500,                // A10.1.5
			flightTestBadCourseCorrectSecond:5,                   // A2.2.13
			flightTestBadCoursePoints:200,                        // A10.1.6
            flightTestBadCourseMaxPoints:0,
			flightTestBadCourseStartLandingPoints:200,            // A10.1.6
			flightTestLandingToLatePoints:200,                    // A10.1.6
			flightTestGivenToLatePoints:100,                      // A10.1.7
			flightTestSafetyAndRulesInfringementPoints:0,
			flightTestInstructionsNotFollowedPoints:0,
			flightTestFalseEnvelopeOpenedPoints:0,
			flightTestSafetyEnvelopeOpenedPoints:0,
			flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,            // A10.2.1
            observationTestTurnpointFalsePoints:100,              // A10.2.1
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,  // A10.2.1
            observationTestEnrouteCorrectValue:5.0f,              // A10.2.1
            observationTestEnrouteInexactValue:0.0f,
            observationTestEnrouteInexactPoints:0,
            observationTestEnrouteNotFoundPoints:20,              // A10.2.1
            observationTestEnrouteFalsePoints:30,                 // A10.2.1
            
			// LandingTest
			landingTest1MaxPoints:400,                            // A10.3
			landingTest1NoLandingPoints:300,                      // A10.3
			landingTest1OutsideLandingPoints:300,                 // A10.3
			landingTest1RollingOutsidePoints:200,                 // A10.3
			landingTest1PowerInBoxPoints:50,                      // A10.3
			landingTest1GoAroundWithoutTouchingPoints:0,
			landingTest1GoAroundInsteadStopPoints:0,
			landingTest1AbnormalLandingPoints:200,                // A10.3
			landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest1PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // A10.3
		
			landingTest2MaxPoints:200,                            // A10.3
			landingTest2NoLandingPoints:200,                      // A10.3
			landingTest2OutsideLandingPoints:200,                 // A10.3
			landingTest2RollingOutsidePoints:150,                 // A10.3
			landingTest2PowerInBoxPoints:50,                      // A10.3
			landingTest2GoAroundWithoutTouchingPoints:0,
			landingTest2GoAroundInsteadStopPoints:0,
			landingTest2AbnormalLandingPoints:200,                // A10.3
			landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest2PowerInAirPoints:200,                     // A10.3
			landingTest2PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // A10.3
			
			landingTest3MaxPoints:200,                            // A10.3
			landingTest3NoLandingPoints:200,                      // A10.3
			landingTest3OutsideLandingPoints:200,                 // A10.3
			landingTest3RollingOutsidePoints:150,                 // A10.3
			landingTest3PowerInBoxPoints:50,                      // A10.3
			landingTest3GoAroundWithoutTouchingPoints:0,
			landingTest3GoAroundInsteadStopPoints:0,
			landingTest3AbnormalLandingPoints:200,                // A10.3
			landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest3PowerInAirPoints:200,                     // A10.3
			landingTest3FlapsInAirPoints:200,                     // A10.3
			landingTest3PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // A10.3
			
			landingTest4MaxPoints:400,                            // A10.3
			landingTest4NoLandingPoints:300,                      // A10.3
			landingTest4OutsideLandingPoints:300,                 // A10.3
			landingTest4RollingOutsidePoints:200,                 // A10.3
			landingTest4PowerInBoxPoints:50,                      // A10.3
			landingTest4GoAroundWithoutTouchingPoints:0,
			landingTest4GoAroundInsteadStopPoints:0,
			landingTest4AbnormalLandingPoints:200,                // A10.3
			landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
			landingTest4TouchingObstaclePoints:400,               // A10.3
			landingTest4PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // A10.3

            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignCanvas,
            turnpointMapMeasurement:true,
            enroutePhotoRule:EnrouteRule.Map,
            enrouteCanvasRule:EnrouteRule.Map,
            enrouteCanvasMultiple:false,
            minRouteLegs:0,
            maxRouteLegs:8,                                       // A2.2.3
            minEnroutePhotos:8,                                   // A3.2
            maxEnroutePhotos:10,                                  // A3.2
            minEnrouteCanvas:8,                                   // A3.2
            maxEnrouteCanvas:15,                                  // A3.2
            minEnrouteTargets:16,                                 // A3.2
            maxEnrouteTargets:25                                  // A3.2
		]
	), // fc.contestrule.fai.precision.2018

	R5 ('fc.contestrule.ch.precision.2017',
		[
			// General
			precisionFlying:true,
            increaseFactor:0,
            printPointsGeneral:false,
			printPointsPlanningTest:true,
			printPointsFlightTest:true,
            printPointsObservationTest:true,
			printPointsLandingTest1:true,
			printPointsLandingTest2:true,
			printPointsLandingTest3:true,
			printPointsLandingTest4:true,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/ch_precision.jpg',
            printPointsTurnpointSign:true,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"A,-1,1,D,E,F,G,H",
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                     // Anhang 5.1
			planningTestDirectionPointsPerGrad:2,                   // Anhang 5.1
			planningTestTimeCorrectSecond:5,                        // Anhang 5.1
			planningTestTimePointsPerSecond:1,                      // Anhang 5.1
			planningTestMaxPoints:200,                              // Anhang 5.1
			planningTestPlanTooLatePoints:50,                       // Anhang 5.1
			planningTestExitRoomTooLatePoints:100,                  // Anhang 5.1
            planningTestForbiddenCalculatorsPoints:0,
		
			// FlightTest
			flightTestTakeoffCorrectSecond:60,                      // Anhang 5.2
			flightTestTakeoffCheckSeconds:false,
			flightTestTakeoffPointsPerSecond:0,
            flightTestTakeoffMissedPoints:200,                      // Anhang 5.2
			flightTestCptimeCorrectSecond:2,                        // Anhang 5.2
			flightTestCptimePointsPerSecond:3,                      // Anhang 5.2
			flightTestCptimeMaxPoints:100,                          // Anhang 5.2
			flightTestCpNotFoundPoints:100,                         // Anhang 5.2
			flightTestProcedureTurnNotFlownPoints:200,              // Anhang 5.2
			flightTestMinAltitudeMissedPoints:500,                  // Anhang 5.2
			flightTestBadCourseCorrectSecond:5,                     // 4.7.15
			flightTestBadCoursePoints:200,                          // Anhang 5.2
            flightTestBadCourseMaxPoints:0,
			flightTestBadCourseStartLandingPoints:200,              // Anhang 5.2
			flightTestLandingToLatePoints:200,                      // Anhang 5.2
			flightTestGivenToLatePoints:100,                        // Anhang 5.2
			flightTestSafetyAndRulesInfringementPoints:0,
			flightTestInstructionsNotFollowedPoints:0,
			flightTestFalseEnvelopeOpenedPoints:0,
			flightTestSafetyEnvelopeOpenedPoints:0,
			flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,              // Anhang 5.3
            observationTestTurnpointFalsePoints:100,                // Anhang 5.3
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,    // Anhang 5.3
            observationTestEnrouteCorrectValue:5.0f,                // Anhang 5.3
            observationTestEnrouteInexactValue:0.0f,
            observationTestEnrouteInexactPoints:0,
            observationTestEnrouteNotFoundPoints:20,                // Anhang 5.3
            observationTestEnrouteFalsePoints:30,                   // Anhang 5.3
            
			// LandingTest
			landingTest1MaxPoints:400,                              // Anhang 5.4
			landingTest1NoLandingPoints:300,                        // Anhang 5.4
			landingTest1OutsideLandingPoints:300,                   // Anhang 5.4
			landingTest1RollingOutsidePoints:200,                   // Anhang 5.4
			landingTest1PowerInBoxPoints:50,                        // Anhang 5.4
			landingTest1GoAroundWithoutTouchingPoints:0,
			landingTest1GoAroundInsteadStopPoints:0,
			landingTest1AbnormalLandingPoints:200,                  // Anhang 5.4
			landingTest1NotAllowedAerodynamicAuxiliariesPoints:200, // Anhang 5.4
			landingTest1PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // Anhang 2
		
			landingTest2MaxPoints:200,                              // Anhang 5.4
			landingTest2NoLandingPoints:200,                        // Anhang 5.4
			landingTest2OutsideLandingPoints:200,                   // Anhang 5.4
			landingTest2RollingOutsidePoints:150,                   // Anhang 5.4
			landingTest2PowerInBoxPoints:50,                        // Anhang 5.4
			landingTest2GoAroundWithoutTouchingPoints:0,
			landingTest2GoAroundInsteadStopPoints:0,
			landingTest2AbnormalLandingPoints:200,                  // Anhang 5.4
			landingTest2NotAllowedAerodynamicAuxiliariesPoints:200, // Anhang 5.4
			landingTest2PowerInAirPoints:200,                       // Anhang 5.4
			landingTest2PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // Anhang 2
			
			landingTest3MaxPoints:200,                              // Anhang 5.4
			landingTest3NoLandingPoints:200,                        // Anhang 5.4
			landingTest3OutsideLandingPoints:200,                   // Anhang 5.4
			landingTest3RollingOutsidePoints:150,                   // Anhang 5.4
			landingTest3PowerInBoxPoints:50,                        // Anhang 5.4
			landingTest3GoAroundWithoutTouchingPoints:0,
			landingTest3GoAroundInsteadStopPoints:0,
			landingTest3AbnormalLandingPoints:200,                  // Anhang 5.4
			landingTest3NotAllowedAerodynamicAuxiliariesPoints:200, // Anhang 5.4			
			landingTest3PowerInAirPoints:200,                       // Anhang 5.4
			landingTest3FlapsInAirPoints:200,                       // Anhang 5.4
			landingTest3PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 3*i}else{return -(6*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 150;case 'D':return 75;case 'E':return 90;case 'F':return 105;case 'G':return 120;case 'H':return 135;default:return null;}}}", // Anhang 2
			
			landingTest4MaxPoints:400,                              // Anhang 5.4
			landingTest4NoLandingPoints:300,                        // Anhang 5.4
			landingTest4OutsideLandingPoints:300,                   // Anhang 5.4
			landingTest4RollingOutsidePoints:200,                   // Anhang 5.4
			landingTest4PowerInBoxPoints:50,                        // Anhang 5.4
			landingTest4GoAroundWithoutTouchingPoints:0,
			landingTest4GoAroundInsteadStopPoints:0,
			landingTest4AbnormalLandingPoints:200,                  // Anhang 5.4
			landingTest4NotAllowedAerodynamicAuxiliariesPoints:200, // Anhang 5.4
			landingTest4TouchingObstaclePoints:400,                 // Anhang 5.4
			landingTest4PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 5*i}else{return -(10*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'A':return 250;case 'D':return 125;case 'E':return 150;case 'F':return 175;case 'G':return 200;case 'H':return 225;default:return null;}}}", // Anhang 2

            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignCanvas,
            turnpointMapMeasurement:true,
            enroutePhotoRule:EnrouteRule.Map,
            enrouteCanvasRule:EnrouteRule.Map,
            enrouteCanvasMultiple:false,
            minRouteLegs:5,                                         // 4.7.12
            maxRouteLegs:8,                                         // 4.7.12
            minEnroutePhotos:8,                                     // 4.8.1
            maxEnroutePhotos:10,                                    // 4.8.1
            minEnrouteCanvas:8,                                     // 4.8.1
            maxEnrouteCanvas:15,                                    // 4.8.1
            minEnrouteTargets:16,                                   // 4.8.1
            maxEnrouteTargets:25                                    // 4.8.1
		]
	), // fc.contestrule.ch.precision.2017

    R6 ('fc.contestrule.fai.rally.2018',
        [
            // General
            precisionFlying:false,
            increaseFactor:0,
            printPointsGeneral:false,
            printPointsPlanningTest:false,
            printPointsFlightTest:true,
            printPointsObservationTest:true,
            printPointsLandingTest1:true,
            printPointsLandingTest2:false,
            printPointsLandingTest3:false,
            printPointsLandingTest4:false,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/fai_rally.jpg',
            printPointsTurnpointSign:false,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,
            planningTestDirectionPointsPerGrad:2,
            planningTestTimeCorrectSecond:2,
            planningTestTimePointsPerSecond:3,
            planningTestMaxPoints:350,
            planningTestPlanTooLatePoints:50,
            planningTestExitRoomTooLatePoints:100,
            planningTestForbiddenCalculatorsPoints:0,
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                   // A4.1
            flightTestTakeoffCheckSeconds:true,                  // A4.1
            flightTestTakeoffPointsPerSecond:3,                  // A4.1
            flightTestTakeoffMissedPoints:100,                   // A4.1
            flightTestCptimeCorrectSecond:2,                     // A4.2
            flightTestCptimePointsPerSecond:3,                   // A4.2
            flightTestCptimeMaxPoints:100,                       // A4.2
            flightTestCpNotFoundPoints:100,                      // A4.4
            flightTestProcedureTurnNotFlownPoints:0,
            flightTestMinAltitudeMissedPoints:200,               // A4.4
            flightTestBadCourseCorrectSecond:5,                  // A4.5
            flightTestBadCoursePoints:100,                       // A4.5
            flightTestBadCourseMaxPoints:1000,                   // A4.5
            flightTestBadCourseStartLandingPoints:0,
            flightTestLandingToLatePoints:0,
            flightTestGivenToLatePoints:300,                     // A4.6
            flightTestSafetyAndRulesInfringementPoints:600,      // A4.4
            flightTestInstructionsNotFollowedPoints:200,         // A4.4
            flightTestFalseEnvelopeOpenedPoints:0,
            flightTestSafetyEnvelopeOpenedPoints:400,            // A4.4
            flightTestFrequencyNotMonitoredPoints:200,           // A4.7
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,           // A4.3
            observationTestTurnpointFalsePoints:100,             // A4.3
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM, // A4.3
            observationTestEnrouteCorrectValue:0.5f,             // A4.3
            observationTestEnrouteInexactValue:1.0f,             // A4.3
            observationTestEnrouteInexactPoints:15,              // A4.3
            observationTestEnrouteNotFoundPoints:30,             // A4.3
            observationTestEnrouteFalsePoints:50,                // A4.3
            
            // LandingTest
            landingTest1MaxPoints:300,                           // A4.6
            landingTest1NoLandingPoints:300,                     // A4.6
            landingTest1OutsideLandingPoints:200,                // A4.6
            landingTest1RollingOutsidePoints:200,                // A4.6
            landingTest1PowerInBoxPoints:50,                     // A4.6
            landingTest1GoAroundWithoutTouchingPoints:200,       // A4.6
            landingTest1GoAroundInsteadStopPoints:200,           // A4.6
            landingTest1AbnormalLandingPoints:150,               // A4.6
            landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest1PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // A4.7
        
            landingTest2MaxPoints:300,                           // A4.6
            landingTest2NoLandingPoints:300,                     // A4.6
            landingTest2OutsideLandingPoints:200,                // A4.6
            landingTest2RollingOutsidePoints:200,                // A4.6
            landingTest2PowerInBoxPoints:50,                     // A4.6
            landingTest2GoAroundWithoutTouchingPoints:200,       // A4.6
            landingTest2GoAroundInsteadStopPoints:200,           // A4.6
            landingTest2AbnormalLandingPoints:150,               // A4.6
            landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest2PowerInAirPoints:0,
            landingTest2PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // A4.7
            
            landingTest3MaxPoints:300,                           // A4.6
            landingTest3NoLandingPoints:300,                     // A4.6
            landingTest3OutsideLandingPoints:200,                // A4.6
            landingTest3RollingOutsidePoints:200,                // A4.6
            landingTest3PowerInBoxPoints:50,                     // A4.6
            landingTest3GoAroundWithoutTouchingPoints:200,       // A4.6
            landingTest3GoAroundInsteadStopPoints:200,           // A4.6
            landingTest3AbnormalLandingPoints:150,               // A4.6
            landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest3PowerInAirPoints:0,
            landingTest3FlapsInAirPoints:0,
            landingTest3PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // A4.7
            
            landingTest4MaxPoints:300,                           // A4.6
            landingTest4NoLandingPoints:300,                     // A4.6
            landingTest4OutsideLandingPoints:200,                // A4.6
            landingTest4RollingOutsidePoints:200,                // A4.6
            landingTest4PowerInBoxPoints:50,                     // A4.6
            landingTest4GoAroundWithoutTouchingPoints:200,       // A4.6
            landingTest4GoAroundInsteadStopPoints:200,           // A4.6
            landingTest4AbnormalLandingPoints:150,               // A4.6
            landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest4TouchingObstaclePoints:0,
            landingTest4PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // A4.7

            // Defaults
            printStyle:"body {--flightplan: hide-distance hide-truetrack hide-trueheading disable-local-time show-elapsed-time; }\nbody {--submission: 20; }",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.None,
            turnpointMapMeasurement:false,
            enroutePhotoRule:EnrouteRule.NMFromTP,
            enrouteCanvasRule:EnrouteRule.NMFromTP,
            enrouteCanvasMultiple:false,
            minRouteLegs:10,                                     // A3.1.1
            maxRouteLegs:16,                                     // A3.1.1
            minEnroutePhotos:15,                                 // A3.4.3
            maxEnroutePhotos:20,                                 // A3.4.3
            minEnrouteCanvas:0,
            maxEnrouteCanvas:5,                                  // A3.4.6
            minEnrouteTargets:15,
            maxEnrouteTargets:25
        ]
    ), // fc.contestrule.fai.rally.2018

    R7 ('fc.contestrule.at.precision.2017.landing1',
        [
            // General
            precisionFlying:true,
            increaseFactor:20,                                   // 4.1
            printPointsGeneral:true,
            printPointsPlanningTest:true,
            printPointsFlightTest:true,
            printPointsObservationTest:true,
            printPointsLandingTest1:true,
            printPointsLandingTest2:true,
            printPointsLandingTest3:true,
            printPointsLandingTest4:true,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/at_precision_landing1.jpg',
            printPointsTurnpointSign:true,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"C,A,I,II,III",
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                  // 4.5
            planningTestDirectionPointsPerGrad:2,                // 4.5
            planningTestTimeCorrectSecond:5,                     // 4.5
            planningTestTimePointsPerSecond:1,                   // 4.5
            planningTestMaxPoints:350,                           // 4.5
            planningTestPlanTooLatePoints:50,                    // 4.5
            planningTestExitRoomTooLatePoints:100,               // 4.5
            planningTestForbiddenCalculatorsPoints:300,          // 4.5
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                   // 4.5
            flightTestTakeoffCheckSeconds:false,                 // 4.5
            flightTestTakeoffPointsPerSecond:0,                  // 4.5
            flightTestTakeoffMissedPoints:200,                   // 4.5
            flightTestCptimeCorrectSecond:2,                     // 4.5
            flightTestCptimePointsPerSecond:3,                   // 4.5
            flightTestCptimeMaxPoints:100,                       // 4.5
            flightTestCpNotFoundPoints:100,                      // 4.5
            flightTestProcedureTurnNotFlownPoints:200,           // 4.5
            flightTestMinAltitudeMissedPoints:500,               // 4.5
            flightTestBadCourseCorrectSecond:5,                  // 4.5
            flightTestBadCoursePoints:200,                       // 4.5
            flightTestBadCourseMaxPoints:0,
            flightTestBadCourseStartLandingPoints:200,           // 4.5
            flightTestLandingToLatePoints:200,                   // 4.5
            flightTestGivenToLatePoints:100,                     // 4.5
            flightTestSafetyAndRulesInfringementPoints:0,
            flightTestInstructionsNotFollowedPoints:0,
            flightTestFalseEnvelopeOpenedPoints:0,
            flightTestSafetyEnvelopeOpenedPoints:0,
            flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,           // 4.5
            observationTestTurnpointFalsePoints:100,             // 4.5
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm, // 4.5
            observationTestEnrouteCorrectValue:5.0f,             // 4.5
            observationTestEnrouteInexactValue:0.0f,
            observationTestEnrouteInexactPoints:0,
            observationTestEnrouteNotFoundPoints:20,             // 4.5
            observationTestEnrouteFalsePoints:30,                // 4.5
            
            // LandingTest
            landingTest1MaxPoints:100,                           // 4.5
            landingTest1NoLandingPoints:100,
            landingTest1OutsideLandingPoints:100,
            landingTest1RollingOutsidePoints:0,
            landingTest1PowerInBoxPoints:50,                     // 4.5
            landingTest1GoAroundWithoutTouchingPoints:0,
            landingTest1GoAroundInsteadStopPoints:0,
            landingTest1AbnormalLandingPoints:100,               // 4.5
            landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest1PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // 4.5
        
            landingTest2MaxPoints:200,                           // 4.5
            landingTest2NoLandingPoints:200,
            landingTest2OutsideLandingPoints:200,
            landingTest2RollingOutsidePoints:0,
            landingTest2PowerInBoxPoints:50,                     // 4.5
            landingTest2GoAroundWithoutTouchingPoints:0,
            landingTest2GoAroundInsteadStopPoints:0,
            landingTest2AbnormalLandingPoints:100,               // 4.5
            landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest2PowerInAirPoints:100,                    // 4.5
            landingTest2PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // 4.5
            
            landingTest3MaxPoints:200,                           // 4.5
            landingTest3NoLandingPoints:200,
            landingTest3OutsideLandingPoints:200,
            landingTest3RollingOutsidePoints:0,
            landingTest3PowerInBoxPoints:50,                     // 4.5
            landingTest3GoAroundWithoutTouchingPoints:0,
            landingTest3GoAroundInsteadStopPoints:0,
            landingTest3AbnormalLandingPoints:100,               // 4.5
            landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest3PowerInAirPoints:100,                    // 4.5
            landingTest3FlapsInAirPoints:100,                    // 4.5
            landingTest3PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // 4.5
            
            landingTest4MaxPoints:200,                           // 4.5
            landingTest4NoLandingPoints:200,
            landingTest4OutsideLandingPoints:200,
            landingTest4RollingOutsidePoints:0,
            landingTest4PowerInBoxPoints:50,                     // 4.5
            landingTest4GoAroundWithoutTouchingPoints:0,
            landingTest4GoAroundInsteadStopPoints:0,
            landingTest4AbnormalLandingPoints:100,               // 4.5
            landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest4TouchingObstaclePoints:100,              // 4.5
            landingTest4PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'A':return 20;case 'I':return 10;case 'II':return 30;case 'III':return 50;default:return null;}}", // 4.5

            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignCanvas,
            turnpointMapMeasurement:true,
            enroutePhotoRule:EnrouteRule.Map,
            enrouteCanvasRule:EnrouteRule.Map,
            enrouteCanvasMultiple:false,
            minRouteLegs:0,
            maxRouteLegs:8,                                      // 4.2.2c
            minEnroutePhotos:8,                                  // 4.3.a
            maxEnroutePhotos:10,                                 // 4.3.a
            minEnrouteCanvas:5,                                  // 4.3.a
            maxEnrouteCanvas:8,                                  // 4.3.a
            minEnrouteTargets:13,                                // 4.3.a
            maxEnrouteTargets:18                                 // 4.3.a
        ]
    ), // fc.contestrule.at.precision.2017.landing1

    R8 ('fc.contestrule.at.precision.2017.landing2',
        [
            // General
            precisionFlying:true,
            increaseFactor:20,                                   // 4.1
            printPointsGeneral:true,
            printPointsPlanningTest:true,
            printPointsFlightTest:true,
            printPointsObservationTest:true,
            printPointsLandingTest1:true,
            printPointsLandingTest2:true,
            printPointsLandingTest3:true,
            printPointsLandingTest4:true,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/at_precision_landing2.jpg',
            printPointsTurnpointSign:true,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"C,B,A,-5,5,I,II,III,IV,V,VI",
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                  // 4.5
            planningTestDirectionPointsPerGrad:2,                // 4.5
            planningTestTimeCorrectSecond:5,                     // 4.5
            planningTestTimePointsPerSecond:1,                   // 4.5
            planningTestMaxPoints:350,                           // 4.5
            planningTestPlanTooLatePoints:50,                    // 4.5
            planningTestExitRoomTooLatePoints:100,               // 4.5
            planningTestForbiddenCalculatorsPoints:300,          // 4.5
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                   // 4.5
            flightTestTakeoffCheckSeconds:false,                 // 4.5
            flightTestTakeoffPointsPerSecond:0,                  // 4.5
            flightTestTakeoffMissedPoints:200,                   // 4.5
            flightTestCptimeCorrectSecond:2,                     // 4.5
            flightTestCptimePointsPerSecond:3,                   // 4.5
            flightTestCptimeMaxPoints:100,                       // 4.5
            flightTestCpNotFoundPoints:100,                      // 4.5
            flightTestProcedureTurnNotFlownPoints:200,           // 4.5
            flightTestMinAltitudeMissedPoints:500,               // 4.5
            flightTestBadCourseCorrectSecond:5,                  // 4.5
            flightTestBadCoursePoints:200,                       // 4.5
            flightTestBadCourseMaxPoints:0,
            flightTestBadCourseStartLandingPoints:200,           // 4.5
            flightTestLandingToLatePoints:200,                   // 4.5
            flightTestGivenToLatePoints:100,                     // 4.5
            flightTestSafetyAndRulesInfringementPoints:0,
            flightTestInstructionsNotFollowedPoints:0,
            flightTestFalseEnvelopeOpenedPoints:0,
            flightTestSafetyEnvelopeOpenedPoints:0,
            flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,           // 4.5
            observationTestTurnpointFalsePoints:100,             // 4.5
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm, // 4.5
            observationTestEnrouteCorrectValue:5.0f,             // 4.5
            observationTestEnrouteInexactValue:0.0f,
            observationTestEnrouteInexactPoints:0,
            observationTestEnrouteNotFoundPoints:20,             // 4.5
            observationTestEnrouteFalsePoints:30,                // 4.5
            
            // LandingTest
            landingTest1MaxPoints:100,                           // 4.5
            landingTest1NoLandingPoints:100,
            landingTest1OutsideLandingPoints:100,
            landingTest1RollingOutsidePoints:0,
            landingTest1PowerInBoxPoints:50,                     // 4.5
            landingTest1GoAroundWithoutTouchingPoints:0,
            landingTest1GoAroundInsteadStopPoints:0,
            landingTest1AbnormalLandingPoints:100,               // 4.5
            landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest1PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // 4.5
        
            landingTest2MaxPoints:200,                           // 4.5
            landingTest2NoLandingPoints:200,
            landingTest2OutsideLandingPoints:200,
            landingTest2RollingOutsidePoints:0,
            landingTest2PowerInBoxPoints:50,                     // 4.5
            landingTest2GoAroundWithoutTouchingPoints:0,
            landingTest2GoAroundInsteadStopPoints:0,
            landingTest2AbnormalLandingPoints:100,               // 4.5
            landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest2PowerInAirPoints:100,                    // 4.5
            landingTest2PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // 4.5
            
            landingTest3MaxPoints:200,                           // 4.5
            landingTest3NoLandingPoints:200,
            landingTest3OutsideLandingPoints:200,
            landingTest3RollingOutsidePoints:0,
            landingTest3PowerInBoxPoints:50,                     // 4.5
            landingTest3GoAroundWithoutTouchingPoints:0,
            landingTest3GoAroundInsteadStopPoints:0,
            landingTest3AbnormalLandingPoints:100,               // 4.5
            landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest3PowerInAirPoints:100,                    // 4.5
            landingTest3FlapsInAirPoints:100,                    // 4.5
            landingTest3PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // 4.5
            
            landingTest4MaxPoints:200,                           // 4.5
            landingTest4NoLandingPoints:200,
            landingTest4OutsideLandingPoints:200,
            landingTest4RollingOutsidePoints:0,
            landingTest4PowerInBoxPoints:50,                     // 4.5
            landingTest4GoAroundWithoutTouchingPoints:0,
            landingTest4GoAroundInsteadStopPoints:0,
            landingTest4AbnormalLandingPoints:100,               // 4.5
            landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest4TouchingObstaclePoints:100,              // 4.5
            landingTest4PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(4*i)}}else{switch(x.toUpperCase()){case '0':return 0;case 'C':return 60;case 'B':return 40;case 'A':return 20;case 'I':return 10;case 'II':return 20;case 'III':return 30;case 'IV':return 40;case 'V':return 50;case 'VI':return 60;default:return null;}}}", // 4.5

            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignCanvas,
            turnpointMapMeasurement:true,
            enroutePhotoRule:EnrouteRule.Map,
            enrouteCanvasRule:EnrouteRule.Map,
            enrouteCanvasMultiple:false,
            minRouteLegs:0,
            maxRouteLegs:8,                                      // 4.2.2c
            minEnroutePhotos:8,                                  // 4.3a
            maxEnroutePhotos:10,                                 // 4.3a
            minEnrouteCanvas:5,                                  // 4.3a
            maxEnrouteCanvas:8,                                  // 4.3a
            minEnrouteTargets:13,                                // 4.3a
            maxEnrouteTargets:18                                 // 4.3a
        ]
    ), // fc.contestrule.at.precision.2017.landing2

    R9 ('fc.contestrule.at.precision.2017.landing3',
        [
            // General
            precisionFlying:true,
            increaseFactor:20,                                   // 4.1
            printPointsGeneral:true,
            printPointsPlanningTest:true,
            printPointsFlightTest:true,
            printPointsObservationTest:true,
            printPointsLandingTest1:true,
            printPointsLandingTest2:true,
            printPointsLandingTest3:true,
            printPointsLandingTest4:true,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/at_precision_landing3.jpg',
            printPointsTurnpointSign:true,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"-20,-10,-1,1,10,20,30",
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                  // 4.5
            planningTestDirectionPointsPerGrad:2,                // 4.5
            planningTestTimeCorrectSecond:5,                     // 4.5
            planningTestTimePointsPerSecond:1,                   // 4.5
            planningTestMaxPoints:350,                           // 4.5
            planningTestPlanTooLatePoints:50,                    // 4.5
            planningTestExitRoomTooLatePoints:100,               // 4.5
            planningTestForbiddenCalculatorsPoints:300,          // 4.5
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                   // 4.5
            flightTestTakeoffCheckSeconds:false,
            flightTestTakeoffPointsPerSecond:0,
            flightTestTakeoffMissedPoints:200,                   // 4.5
            flightTestCptimeCorrectSecond:2,                     // 4.5
            flightTestCptimePointsPerSecond:3,                   // 4.5
            flightTestCptimeMaxPoints:100,                       // 4.5
            flightTestCpNotFoundPoints:100,                      // 4.5
            flightTestProcedureTurnNotFlownPoints:200,           // 4.5
            flightTestMinAltitudeMissedPoints:500,               // 4.5
            flightTestBadCourseCorrectSecond:5,                  // 4.5
            flightTestBadCoursePoints:200,                       // 4.5
            flightTestBadCourseMaxPoints:0,
            flightTestBadCourseStartLandingPoints:200,           // 4.5
            flightTestLandingToLatePoints:200,                   // 4.5
            flightTestGivenToLatePoints:100,                     // 4.5
            flightTestSafetyAndRulesInfringementPoints:0,
            flightTestInstructionsNotFollowedPoints:0,
            flightTestFalseEnvelopeOpenedPoints:0,
            flightTestSafetyEnvelopeOpenedPoints:0,
            flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,           // 4.5
            observationTestTurnpointFalsePoints:100,             // 4.5
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm, // 4.5
            observationTestEnrouteCorrectValue:5.0f,             // 4.5
            observationTestEnrouteInexactValue:0.0f,
            observationTestEnrouteInexactPoints:0,
            observationTestEnrouteNotFoundPoints:20,             // 4.5
            observationTestEnrouteFalsePoints:30,                // 4.5
            
            // LandingTest
            landingTest1MaxPoints:100,                           // 4.5
            landingTest1NoLandingPoints:100,
            landingTest1OutsideLandingPoints:100,
            landingTest1RollingOutsidePoints:0,
            landingTest1PowerInBoxPoints:50,                     // 4.5
            landingTest1GoAroundWithoutTouchingPoints:0,
            landingTest1GoAroundInsteadStopPoints:0,
            landingTest1AbnormalLandingPoints:100,               // 4.5
            landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest1PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // 4.5
        
            landingTest2MaxPoints:200,                           // 4.5
            landingTest2NoLandingPoints:200,
            landingTest2OutsideLandingPoints:200,
            landingTest2RollingOutsidePoints:0,
            landingTest2PowerInBoxPoints:50,                     // 4.5
            landingTest2GoAroundWithoutTouchingPoints:0,
            landingTest2GoAroundInsteadStopPoints:0,
            landingTest2AbnormalLandingPoints:100,               // 4.5
            landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest2PowerInAirPoints:100,                    // 4.5
            landingTest2PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // 4.5
            
            landingTest3MaxPoints:200,                           // 4.5
            landingTest3NoLandingPoints:200,
            landingTest3OutsideLandingPoints:200,
            landingTest3RollingOutsidePoints:0,
            landingTest3PowerInBoxPoints:50,                     // 4.5
            landingTest3GoAroundWithoutTouchingPoints:0,
            landingTest3GoAroundInsteadStopPoints:0,
            landingTest3AbnormalLandingPoints:100,               // 4.5
            landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest3PowerInAirPoints:100,                    // 4.5
            landingTest3FlapsInAirPoints:100,                    // 4.5
            landingTest3PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // 4.5
            
            landingTest4MaxPoints:200,                           // 4.5
            landingTest4NoLandingPoints:200,
            landingTest4OutsideLandingPoints:200,
            landingTest4RollingOutsidePoints:0,
            landingTest4PowerInBoxPoints:50,                     // 4.5
            landingTest4GoAroundWithoutTouchingPoints:0,
            landingTest4GoAroundInsteadStopPoints:0,
            landingTest4AbnormalLandingPoints:100,               // 4.5
            landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest4TouchingObstaclePoints:100,              // 4.5
            landingTest4PenaltyCalculator:"{x -> if(x.isInteger()){i=x.toInteger();if(i>0){return 2*i}else{return -(3*i)}}else{return null;}}", // 4.5

            // Defaults
            printStyle:"",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignCanvas,
            turnpointMapMeasurement:true,
            enroutePhotoRule:EnrouteRule.Map,
            enrouteCanvasRule:EnrouteRule.Map,
            enrouteCanvasMultiple:false,
            minRouteLegs:0,
            maxRouteLegs:8,                                      // 4.2.2c
            minEnroutePhotos:8,                                  // 4.3a
            maxEnroutePhotos:10,                                 // 4.3a
            minEnrouteCanvas:5,                                  // 4.3a
            maxEnrouteCanvas:8,                                  // 4.3a
            minEnrouteTargets:13,                                // 4.3a
            maxEnrouteTargets:18                                 // 4.3a
        ]
    ), // fc.contestrule.at.precision.2017.landing3

    R10 ('fc.contestrule.at.nav.2017',
        [
            // General
            precisionFlying:false,
            increaseFactor:0,
            printPointsGeneral:false,
            printPointsPlanningTest:false,
            printPointsFlightTest:true,
            printPointsObservationTest:true,
            printPointsLandingTest1:true,
            printPointsLandingTest2:false,
            printPointsLandingTest3:false,
            printPointsLandingTest4:false,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/at_nav.jpg',
            printPointsTurnpointSign:false,
            printPointsEnrouteCanvas:true,
            printLandingCalculatorValues:"Y,X,A,B,C,D,E,F,G,H",
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,
            planningTestDirectionPointsPerGrad:2,
            planningTestTimeCorrectSecond:5,
            planningTestTimePointsPerSecond:1,
            planningTestMaxPoints:350,
            planningTestPlanTooLatePoints:50,
            planningTestExitRoomTooLatePoints:100,
            planningTestForbiddenCalculatorsPoints:0,
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                   // 5.3a
            flightTestTakeoffCheckSeconds:true,                  // 5.3a
            flightTestTakeoffPointsPerSecond:3,                  // 5.3a
            flightTestTakeoffMissedPoints:100,                   // 5.3a
            flightTestCptimeCorrectSecond:5,                     // 5.3b
            flightTestCptimePointsPerSecond:2,                   // 5.3b
            flightTestCptimeMaxPoints:100,                       // 5.3b
            flightTestCpNotFoundPoints:100,                      // 5.3b, 5.3d
            flightTestProcedureTurnNotFlownPoints:0,
            flightTestMinAltitudeMissedPoints:200,               // 5.3d
            flightTestBadCourseCorrectSecond:5,                  // 5.3d
            flightTestBadCoursePoints:200,                       // 5.3d
            flightTestBadCourseMaxPoints:0,
            flightTestBadCourseStartLandingPoints:0,
            flightTestLandingToLatePoints:300,                   // 5.3h
            flightTestGivenToLatePoints:300,                     // 5.3f
            flightTestSafetyAndRulesInfringementPoints:600,      // 5.3d
            flightTestInstructionsNotFollowedPoints:200,         // 5.3d
            flightTestFalseEnvelopeOpenedPoints:100,             // 5.3d
            flightTestSafetyEnvelopeOpenedPoints:300,            // 5.3i
            flightTestFrequencyNotMonitoredPoints:200,           // 5.3g
            flightTestForbiddenEquipmentPoints:600,              // 5.3d

            // ObservationTest
            observationTestTurnpointNotFoundPoints:0,
            observationTestTurnpointFalsePoints:0,
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM, // 5.3c
            observationTestEnrouteCorrectValue:0.5f,             // 5.3c
            observationTestEnrouteInexactValue:1.0f,             // 5.3c
            observationTestEnrouteInexactPoints:10,              // 5.3c
            observationTestEnrouteNotFoundPoints:20,             // 5.3c
            observationTestEnrouteFalsePoints:40,                // 5.3c
            
            // LandingTest
            landingTest1MaxPoints:300,                           // 5.3e
            landingTest1NoLandingPoints:300,                     // 5.3e
            landingTest1OutsideLandingPoints:200,                // 5.3e
            landingTest1RollingOutsidePoints:200,                // 5.3e
            landingTest1PowerInBoxPoints:50,                     // 5.3e
            landingTest1GoAroundWithoutTouchingPoints:200,       // 5.3e
            landingTest1GoAroundInsteadStopPoints:200,           // 5.3e
            landingTest1AbnormalLandingPoints:150,               // 5.3e
            landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest1PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 5.3e
        
            landingTest2MaxPoints:300,                           // 5.3e
            landingTest2NoLandingPoints:300,                     // 5.3e
            landingTest2OutsideLandingPoints:200,                // 5.3e
            landingTest2RollingOutsidePoints:200,                // 5.3e
            landingTest2PowerInBoxPoints:50,                     // 5.3e
            landingTest2GoAroundWithoutTouchingPoints:200,       // 5.3e
            landingTest2GoAroundInsteadStopPoints:200,           // 5.3e
            landingTest2AbnormalLandingPoints:150,               // 5.3e
            landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest2PowerInAirPoints:0,
            landingTest2PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 5.3e
            
            landingTest3MaxPoints:300,                           // 5.3e
            landingTest3NoLandingPoints:300,                     // 5.3e
            landingTest3OutsideLandingPoints:200,                // 5.3e
            landingTest3RollingOutsidePoints:200,                // 5.3e
            landingTest3PowerInBoxPoints:50,                     // 5.3e
            landingTest3GoAroundWithoutTouchingPoints:200,       // 5.3e
            landingTest3GoAroundInsteadStopPoints:200,           // 5.3e
            landingTest3AbnormalLandingPoints:150,               // 5.3e
            landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest3PowerInAirPoints:0,
            landingTest3FlapsInAirPoints:0,
            landingTest3PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 5.3e
            
            landingTest4MaxPoints:300,                           // 5.3e
            landingTest4NoLandingPoints:300,                     // 5.3e
            landingTest4OutsideLandingPoints:200,                // 5.3e
            landingTest4RollingOutsidePoints:200,                // 5.3e
            landingTest4PowerInBoxPoints:50,                     // 5.3e
            landingTest4GoAroundWithoutTouchingPoints:200,       // 5.3e
            landingTest4GoAroundInsteadStopPoints:200,           // 5.3e
            landingTest4AbnormalLandingPoints:150,               // 5.3e
            landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest4TouchingObstaclePoints:0,
            landingTest4PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 10;case 'B':return 20;case 'C':return 30;case 'D':return 40;case 'E':return 60;case 'F':return 80;case 'G':return 100;case 'H':return 120;case 'X':return 60;case 'Y':return 120;default:return null;}}", // 5.3e

            // Defaults
            printStyle:"body {--flightplan: hide-distance hide-truetrack hide-trueheading disable-local-time show-elapsed-time; }\nbody {--submission: 20; }",
            scGateWidth:1.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.None,
            turnpointMapMeasurement:false,
            enroutePhotoRule:EnrouteRule.NMFromTP,
            enrouteCanvasRule:EnrouteRule.NMFromTP,
            enrouteCanvasMultiple:false,
            minRouteLegs:11,                                     // 5.2.5b, 5.2.5c
            maxRouteLegs:16,                                     // 5.2.5b, 5.2.5c
            minEnroutePhotos:12,                                 // 5.2.5b
            maxEnroutePhotos:20,                                 // 5.2.5b
            minEnrouteCanvas:0,                                  // 5.2.5a
            maxEnrouteCanvas:5,                                  // 5.2.5a
            minEnrouteTargets:12,                                // 5.2.5d
            maxEnrouteTargets:20                                 // 5.2.5d
        ]
    ), // fc.contestrule.at.nav.2017

    R11 ('fc.contestrule.de.nav.2017',
        [
            // General
            precisionFlying:false,
            increaseFactor:0,
            printPointsGeneral:false,
            printPointsPlanningTest:true,
            printPointsFlightTest:true,
            printPointsObservationTest:true,
            printPointsLandingTest1:true,
            printPointsLandingTest2:false,
            printPointsLandingTest3:false,
            printPointsLandingTest4:false,
            printPointsLandingField:true,
            landingFieldImageName:'images/landingfield/de_nav2017*.jpg',
            printPointsTurnpointSign:false,
            printPointsEnrouteCanvas:false,
            printLandingCalculatorValues:"F,E,A,B,C,D",
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                  // 4.1.1
            planningTestDirectionPointsPerGrad:2,                // 4.1.1
            planningTestTimeCorrectSecond:5,                     // 4.1.1
            planningTestTimePointsPerSecond:1,                   // 4.1.1
            planningTestMaxPoints:200,                           // 4.1.1
            planningTestPlanTooLatePoints:50,                    // 4.1.1
            planningTestExitRoomTooLatePoints:100,               // 4.1.1
            planningTestForbiddenCalculatorsPoints:0,
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                   // 4.1.2
            flightTestTakeoffCheckSeconds:false,
            flightTestTakeoffPointsPerSecond:0,
            flightTestTakeoffMissedPoints:200,                   // 4.1.2
            flightTestCptimeCorrectSecond:2,                     // 4.1.3
            flightTestCptimePointsPerSecond:1,                   // 4.1.3
            flightTestCptimeMaxPoints:200,                       // 4.1.3
            flightTestCpNotFoundPoints:200,                      // 4.1.3
            flightTestProcedureTurnNotFlownPoints:200,           // 4.1.4
            flightTestMinAltitudeMissedPoints:500,               // 4.1.5
            flightTestBadCourseCorrectSecond:5,                  // 3.2.9
            flightTestBadCoursePoints:200,                       // 4.1.6
            flightTestBadCourseMaxPoints:0,
            flightTestBadCourseStartLandingPoints:500,           // 4.1.6
            flightTestLandingToLatePoints:200,                   // 4.1.6
            flightTestGivenToLatePoints:100,                     // 4.1.6
            flightTestSafetyAndRulesInfringementPoints:0,
            flightTestInstructionsNotFollowedPoints:0,
            flightTestFalseEnvelopeOpenedPoints:0,
            flightTestSafetyEnvelopeOpenedPoints:0,
            flightTestFrequencyNotMonitoredPoints:0,
            flightTestForbiddenEquipmentPoints:0,
        
            // ObservationTest
            observationTestTurnpointNotFoundPoints:40,           // 4.2
            observationTestTurnpointFalsePoints:80,              // 4.2
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm, // 4.2
            observationTestEnrouteCorrectValue:5.0f,             // 4.2
            observationTestEnrouteInexactValue:10.0f,            // 4.2
            observationTestEnrouteInexactPoints:10,              // 4.2
            observationTestEnrouteNotFoundPoints:20,             // 4.2
            observationTestEnrouteFalsePoints:40,                // 4.2
            
            // LandingTest
            landingTest1MaxPoints:300,                           // 4.3
            landingTest1NoLandingPoints:300,                     // 4.3
            landingTest1OutsideLandingPoints:200,                // 4.3
            landingTest1RollingOutsidePoints:200,                // 4.3
            landingTest1PowerInBoxPoints:100,                    // 4.3
            landingTest1GoAroundWithoutTouchingPoints:200,       // 4.3
            landingTest1GoAroundInsteadStopPoints:200,           // 4.3
            landingTest1AbnormalLandingPoints:200,               // 4.3
            landingTest1NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest1PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return null;}}", // 4.3
        
            landingTest2MaxPoints:300,                           // 4.3
            landingTest2NoLandingPoints:300,                     // 4.3
            landingTest2OutsideLandingPoints:200,                // 4.3
            landingTest2RollingOutsidePoints:200,                // 4.3
            landingTest2PowerInBoxPoints:100,                    // 4.3
            landingTest2GoAroundWithoutTouchingPoints:200,       // 4.3
            landingTest2GoAroundInsteadStopPoints:200,           // 4.3
            landingTest2AbnormalLandingPoints:200,               // 4.3
            landingTest2NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest2PowerInAirPoints:0,
            landingTest2PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return null;}}", // 4.3
            
            landingTest3MaxPoints:300,                           // 4.3
            landingTest3NoLandingPoints:300,                     // 4.3
            landingTest3OutsideLandingPoints:200,                // 4.3
            landingTest3RollingOutsidePoints:200,                // 4.3
            landingTest3PowerInBoxPoints:100,                    // 4.3
            landingTest3GoAroundWithoutTouchingPoints:200,       // 4.3
            landingTest3GoAroundInsteadStopPoints:200,           // 4.3
            landingTest3AbnormalLandingPoints:200,               // 4.3
            landingTest3NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest3PowerInAirPoints:0,
            landingTest3FlapsInAirPoints:0,
            landingTest3PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return null;}}", // 4.3
            
            landingTest4MaxPoints:300,                           // 4.3
            landingTest4NoLandingPoints:300,                     // 4.3
            landingTest4OutsideLandingPoints:200,                // 4.3
            landingTest4RollingOutsidePoints:200,                // 4.3
            landingTest4PowerInBoxPoints:100,                    // 4.3
            landingTest4GoAroundWithoutTouchingPoints:200,       // 4.3
            landingTest4GoAroundInsteadStopPoints:200,           // 4.3
            landingTest4AbnormalLandingPoints:200,               // 4.3
            landingTest4NotAllowedAerodynamicAuxiliariesPoints:0,
            landingTest4TouchingObstaclePoints:0,
            landingTest4PenaltyCalculator:"{x -> switch(x.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;default:return null;}}", // 4.3
            
            // Defaults
            printStyle:"",
            scGateWidth:2.0f,
            unsuitableStartNum:"13",
            turnpointRule:TurnpointRule.AssignTrueFalse,
            turnpointMapMeasurement:false,
            enroutePhotoRule:EnrouteRule.MapOrMeasurement,
            enrouteCanvasRule:EnrouteRule.None,
            enrouteCanvasMultiple:false,
            minRouteLegs:0,
            maxRouteLegs:8,                                      // 3.2.3
            minEnroutePhotos:0,
            maxEnroutePhotos:20,
            minEnrouteCanvas:0,
            maxEnrouteCanvas:15,
            minEnrouteTargets:0,
            maxEnrouteTargets:35
        ]
    ) // fc.contestrule.de.nav.2017


	ContestRules(String titleCode, Map ruleValues) 
	{
		this.titleCode = titleCode
		this.ruleValues = ruleValues
	}
	
	final String titleCode
	final Map ruleValues
    
    static List GetContestRules()
    {
        List ret = []
        ret += R1  // fc.contestrule.de.nav.2018
        ret += R11 // fc.contestrule.de.nav.2017
        ret += R4  // fc.contestrule.fai.precision.2018
        ret += R6  // fc.contestrule.fai.rally.2018
        ret += R5  // fc.contestrule.ch.precision.2017
        ret += R7  // fc.contestrule.at.precision.2017.landing1
        ret += R8  // fc.contestrule.at.precision.2017.landing2
        ret += R9  // fc.contestrule.at.precision.2017.landing3
        ret += R10 // fc.contestrule.at.nav.2017
        return ret
    }
    
}