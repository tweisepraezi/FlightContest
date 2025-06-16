import java.util.List;

enum ContestRules 
{
    R6 ([
            ruleTitle:"FAI Air Rally Flying - Edition 2025",         // FAI-RF
            
            // General
            increaseFactor:0,                                        // FAI-RF
            printPointsGeneral:false,                                // FAI-RF
            printPointsPlanningTest:false,                           // FAI-RF
            printPointsFlightTest:true,                              // FAI-RF
            printPointsObservationTest:true,                         // FAI-RF
            printPointsLandingTest1:true,                            // FAI-RF
            printPointsLandingTest2:false,                           // FAI-RF
            printPointsLandingTest3:false,                           // FAI-RF
            printPointsLandingTest4:false,                           // FAI-RF
            printPointsLandingField:true,                            // FAI-RF
            printPointsTurnpointSign:false,                          // FAI-RF
            printPointsEnrouteCanvas:true,                           // FAI-RF
            printIgnoreEnrouteCanvas:[EnrouteCanvasSign.S08],        // FAI-RF
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                      // FAI-RF -
            planningTestDirectionPointsPerGrad:2,                    // FAI-RF -
            planningTestTimeCorrectSecond:5,                         // FAI-RF -
            planningTestTimePointsPerSecond:1,                       // FAI-RF -
            planningTestMaxPoints:200,                               // FAI-RF -
            planningTestPlanTooLatePoints:50,                        // FAI-RF -
            planningTestExitRoomTooLatePoints:100,                   // FAI-RF -
            planningTestForbiddenCalculatorsPoints:0,                // FAI-RF -
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                       // FAI-RF A4.1
            flightTestTakeoffCheckSeconds:true,                      // FAI-RF A4.1
            flightTestTakeoffPointsPerSecond:3,                      // FAI-RF A4.1
            flightTestTakeoffMissedPoints:100,                       // FAI-RF A4.1
            flightTestCptimeCorrectSecond:2,                         // FAI-RF A4.2
            flightTestCptimePointsPerSecond:3,                       // FAI-RF A4.2
            flightTestCptimeMaxPoints:100,                           // FAI-RF A4.2
            flightTestCpNotFoundPoints:100,                          // FAI-RF A4.4
            flightTestProcedureTurnNotFlownPoints:0,                 // FAI-RF -
            flightTestMinAltitudeMissedPoints:200,                   // FAI-RF A4.4
            flightTestBadCourseCorrectSecond:5,                      // FAI-RF A4.5
            flightTestBadCoursePoints:100,                           // FAI-RF A4.5
            flightTestBadCourseMaxPoints:1000,                       // FAI-RF A4.5
            flightTestBadCourseStartLandingPoints:200,               // FAI-RF A4.4
            flightTestBadCourseStartLandingSeparatePoints:false,     // FAI-RF
			flightTestOutsideCorridorCorrectSecond:0,                // FAI-RF -
			flightTestOutsideCorridorPointsPerSecond:0,              // FAI-RF -
            flightTestLandingToLatePoints:0,                         // FAI-RF -
            flightTestGivenToLatePoints:300,                         // FAI-RF A4.7
            flightTestSafetyAndRulesInfringementPoints:600,          // FAI-RF A4.4
            flightTestInstructionsNotFollowedPoints:0,               // FAI-RF
            flightTestFalseEnvelopeOpenedPoints:0,                   // FAI-RF
            flightTestSafetyEnvelopeOpenedPoints:400,                // FAI-RF A4.4
            flightTestFrequencyNotMonitoredPoints:200,               // FAI-RF A4.8
            flightTestForbiddenEquipmentPoints:0,                    // FAI-RF
            flightTestExitRoomTooLatePoints:0,                       // FAI-RF -

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // FAI-RF A4.3
            observationTestTurnpointFalsePoints:100,                 // FAI-RF A4.3
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM,     // FAI-RF A4.3
            observationTestEnrouteCorrectValue:0.5f,                 // FAI-RF A4.3
            observationTestEnrouteInexactValue:1.0f,                 // FAI-RF A4.3
            observationTestEnrouteInexactPoints:15,                  // FAI-RF A4.3
            observationTestEnrouteNotFoundPoints:30,                 // FAI-RF A4.3
            observationTestEnrouteFalsePoints:50,                    // FAI-RF A4.3
            
            // LandingTest
            landingTest1:ContestRulesLanding.FAI_RF.data,            // FAI-RF
            landingTest2:ContestRulesLanding.FAI_RF.data,            // FAI-RF
            landingTest3:ContestRulesLanding.FAI_RF.data,            // FAI-RF
            landingTest4:ContestRulesLanding.FAI_RF.data,            // FAI-RF

            // Defaults (other)
            precisionFlying:false,                                   // FAI-RF
            anrFlying:false,                                         // FAI-RF
            flightTestLastGateNoBadCourseSeconds:45,                 // FAI-RF A3.1.9 defines 45s
            showPlanningTest:false,                                  // FAI-RF 
            activateFlightTestCheckLanding:true,                     // FAI-RF 
            showObservationTest:true,                                // FAI-RF 

            // Defaults
            flightPlanShowLegDistance:false,                         // FAI-RF
            flightPlanShowTrueTrack:false,                           // FAI-RF
            flightPlanShowTrueHeading:false,                         // FAI-RF
            flightPlanShowGroundSpeed:true,                          // FAI-RF
            flightPlanShowLocalTime:false,                           // FAI-RF
            flightPlanShowElapsedTime:true,                          // FAI-RF
            flightTestSubmissionMinutes:20,                          // FAI-RF
            scGateWidth:1.0f,                                        // FAI-RF
            unsuitableStartNum:"13",                                 // FAI-RF
            turnpointRule:TurnpointRule.TrueFalsePhoto,              // FAI-RF A3.4.4
            turnpointMapMeasurement:false,                           // FAI-RF
            enroutePhotoRule:EnrouteRule.NMFromTP,                   // FAI-RF
            enrouteCanvasRule:EnrouteRule.NMFromTP,                  // FAI-RF
            enrouteCanvasMultiple:true,                              // FAI-RF
            minRouteLegs:10,                                         // FAI-RF A3.1.2
            maxRouteLegs:16,                                         // FAI-RF A3.1.2
            minEnroutePhotos:15,                                     // FAI-RF A3.4.3
            maxEnroutePhotos:20,                                     // FAI-RF A3.4.3
            minEnrouteCanvas:0,                                      // FAI-RF
            maxEnrouteCanvas:5,                                      // FAI-RF A3.4.6
            minEnrouteTargets:15,                                    // FAI-RF
            maxEnrouteTargets:25,                                    // FAI-RF
            useProcedureTurns:false,                                 // FAI-RF -
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_RF     // FAI-RF
        ]                                                            // FAI-RF
    ),

	R1 ([
            ruleTitle:"Wettbewerbsordnung Rallyeflug Deutschland - Ausgabe 2025", // DE-RF
            
			// General
            increaseFactor:0,                                        // DE-RF
            printPointsGeneral:false,                                // DE-RF
			printPointsPlanningTest:false,                           // DE-RF
			printPointsFlightTest:true,                              // DE-RF
            printPointsObservationTest:true,                         // DE-RF
			printPointsLandingTest1:true,                            // DE-RF
			printPointsLandingTest2:false,                           // DE-RF
			printPointsLandingTest3:false,                           // DE-RF
			printPointsLandingTest4:false,                           // DE-RF
            printPointsLandingField:true,                            // DE-RF
            printPointsTurnpointSign:false,                          // DE-RF
            printPointsEnrouteCanvas:true,                           // DE-RF
            printIgnoreEnrouteCanvas:[EnrouteCanvasSign.S08],        // DE-RF
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                      // DE-RF -
			planningTestDirectionPointsPerGrad:2,                    // DE-RF -
			planningTestTimeCorrectSecond:5,                         // DE-RF -
			planningTestTimePointsPerSecond:1,                       // DE-RF -
			planningTestMaxPoints:200,                               // DE-RF -
			planningTestPlanTooLatePoints:50,                        // DE-RF -
			planningTestExitRoomTooLatePoints:100,                   // DE-RF -
            planningTestForbiddenCalculatorsPoints:0,                // DE-RF -
		
			// FlightTest
			flightTestTakeoffCorrectSecond:60,                       // DE-RF 4
			flightTestTakeoffCheckSeconds:true,                      // DE-RF
            flightTestTakeoffMissedPoints:100,                       // DE-RF 4
			flightTestTakeoffPointsPerSecond:3,                      // DE-RF 4
			flightTestCptimeCorrectSecond:2,                         // DE-RF 4
			flightTestCptimePointsPerSecond:3,                       // DE-RF 4
			flightTestCptimeMaxPoints:100,                           // DE-RF 4
			flightTestCpNotFoundPoints:100,                          // DE-RF 4
			flightTestProcedureTurnNotFlownPoints:0,                 // DE-RF -
			flightTestMinAltitudeMissedPoints:200,                   // DE-RF 4
			flightTestBadCourseCorrectSecond:5,                      // DE-RF 3.3.6
			flightTestBadCoursePoints:100,                           // DE-RF 4
            flightTestBadCourseMaxPoints:1000,                       // DE-RF 4
			flightTestBadCourseStartLandingPoints:200,               // DE-RF 4
            flightTestBadCourseStartLandingSeparatePoints:false,     // DE-RF
			flightTestOutsideCorridorCorrectSecond:0,                // DE-RF -
			flightTestOutsideCorridorPointsPerSecond:0,              // DE-RF -
			flightTestLandingToLatePoints:200,                       // DE-RF 4
			flightTestGivenToLatePoints:300,                         // DE-RF 4
			flightTestSafetyAndRulesInfringementPoints:0,            // DE-RF
			flightTestInstructionsNotFollowedPoints:0,               // DE-RF
			flightTestFalseEnvelopeOpenedPoints:0,                   // DE-RF
			flightTestSafetyEnvelopeOpenedPoints:400,                // DE-RF 4
			flightTestFrequencyNotMonitoredPoints:0,                 // DE-RF
            flightTestForbiddenEquipmentPoints:0,                    // DE-RF
            flightTestExitRoomTooLatePoints:0,                       // DE-RF -
		
            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // DE-RF 4
            observationTestTurnpointFalsePoints:100,                 // DE-RF 4
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM,     // DE-RF 4
            observationTestEnrouteCorrectValue:0.5f,                 // DE-RF 4
            observationTestEnrouteInexactValue:1.0f,                 // DE-RF 4
            observationTestEnrouteInexactPoints:15,                  // DE-RF 4
            observationTestEnrouteNotFoundPoints:30,                 // DE-RF 4
            observationTestEnrouteFalsePoints:50,                    // DE-RF 4
            
			// LandingTest
            landingTest1:ContestRulesLanding.DE_RF_ANR.data,         // DE-RF
            landingTest2:ContestRulesLanding.DE_RF_ANR.data,         // DE-RF
            landingTest3:ContestRulesLanding.DE_RF_ANR.data,         // DE-RF
            landingTest4:ContestRulesLanding.DE_RF_ANR.data,         // DE-RF

            // Defaults (other)
			precisionFlying:false,                                   // DE-RF
            anrFlying:false,                                         // DE-RF
            flightTestLastGateNoBadCourseSeconds:45,                 // DE-RF
            showPlanningTest:false,                                  // DE-RF
            activateFlightTestCheckLanding:true,                     // DE-RF
            showObservationTest:true,                                // DE-RF

            // Defaults
            flightPlanShowLegDistance:false,                         // DE-RF
            flightPlanShowTrueTrack:false,                           // DE-RF
            flightPlanShowTrueHeading:false,                         // DE-RF
            flightPlanShowGroundSpeed:true,                          // DE-RF
            flightPlanShowLocalTime:false,                           // DE-RF
            flightPlanShowElapsedTime:true,                          // DE-RF
            flightTestSubmissionMinutes:20,                          // DE-RF
            scGateWidth:1.0f,                                        // DE-RF
            unsuitableStartNum:"13",                                 // DE-RF
            turnpointRule:TurnpointRule.TrueFalsePhoto,              // DE-RF 3.4.6
            turnpointMapMeasurement:false,                           // DE-RF
            enroutePhotoRule:EnrouteRule.NMFromTP,                   // DE-RF 3.4.8
            enrouteCanvasRule:EnrouteRule.NMFromTP,                  // DE-RF 3.4.8
            enrouteCanvasMultiple:true,                              // DE-RF
            minRouteLegs:10,                                         // DE-RF 3.1
            maxRouteLegs:16,                                         // DE-RF 3.1
            minEnroutePhotos:15,                                     // DE-RF 3.4.1
            maxEnroutePhotos:20,                                     // DE-RF 3.4.1
            minEnrouteCanvas:0,                                      // DE-RF 3.4.1
            maxEnrouteCanvas:5,                                      // DE-RF 3.4.1
            minEnrouteTargets:10,                                    // DE-RF (3.4.1)
            maxEnrouteTargets:25,                                    // DE-RF (3.4.1)
            useProcedureTurns:false,                                 // DE-RF -
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_RF     // DE-RF
		]
	),

    R10 ([
            ruleTitle:"Navigationsflug-Reglement \u00D6sterreich - Ausgabe 2023", // AT-RF
            
            // General
            increaseFactor:0,                                        // AT-RF
            printPointsGeneral:false,                                // AT-RF
            printPointsPlanningTest:false,                           // AT-RF
            printPointsFlightTest:true,                              // AT-RF
            printPointsObservationTest:true,                         // AT-RF
            printPointsLandingTest1:true,                            // AT-RF
            printPointsLandingTest2:false,                           // AT-RF
            printPointsLandingTest3:false,                           // AT-RF
            printPointsLandingTest4:false,                           // AT-RF
            printPointsLandingField:true,                            // AT-RF
            printPointsTurnpointSign:true,                           // AT-RF
            printPointsEnrouteCanvas:false,                          // AT-RF
            printIgnoreEnrouteCanvas:[],                             // AT-RF
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                      // AT-RF -
            planningTestDirectionPointsPerGrad:2,                    // AT-RF -
            planningTestTimeCorrectSecond:5,                         // AT-RF -
            planningTestTimePointsPerSecond:1,                       // AT-RF -
            planningTestMaxPoints:200,                               // AT-RF -
            planningTestPlanTooLatePoints:50,                        // AT-RF -
            planningTestExitRoomTooLatePoints:100,                   // AT-RF -
            planningTestForbiddenCalculatorsPoints:0,                // AT-RF -
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                       // AT-RF 5.3a
            flightTestTakeoffCheckSeconds:true,                      // AT-RF 5.3a
            flightTestTakeoffPointsPerSecond:3,                      // AT-RF 5.3a
            flightTestTakeoffMissedPoints:100,                       // AT-RF 5.3a
            flightTestCptimeCorrectSecond:2,                         // AT-RF 5.3b
            flightTestCptimePointsPerSecond:3,                       // AT-RF 5.3b
            flightTestCptimeMaxPoints:100,                           // AT-RF 5.3b
            flightTestCpNotFoundPoints:100,                          // AT-RF 5.3b, 5.3d
            flightTestProcedureTurnNotFlownPoints:0,                 // AT-RF -
            flightTestMinAltitudeMissedPoints:200,                   // AT-RF 5.3d
            flightTestBadCourseCorrectSecond:5,                      // AT-RF 5.3d
            flightTestBadCoursePoints:100,                           // AT-RF 5.3d
            flightTestBadCourseMaxPoints:0,                          // AT-RF 
            flightTestBadCourseStartLandingPoints:200,               // AT-RF 5.3d
            flightTestBadCourseStartLandingSeparatePoints:false,     // AT-RF 
			flightTestOutsideCorridorCorrectSecond:0,                // AT-RF - 
			flightTestOutsideCorridorPointsPerSecond:0,              // AT-RF - 
            flightTestLandingToLatePoints:300,                       // AT-RF 5.3h
            flightTestGivenToLatePoints:300,                         // AT-RF 5.3f
            flightTestSafetyAndRulesInfringementPoints:600,          // AT-RF 5.3d
            flightTestInstructionsNotFollowedPoints:0,               // AT-RF 
            flightTestFalseEnvelopeOpenedPoints:0,                   // AT-RF 
            flightTestSafetyEnvelopeOpenedPoints:400,                // AT-RF 5.3i
            flightTestFrequencyNotMonitoredPoints:200,               // AT-RF 5.3g
            flightTestForbiddenEquipmentPoints:600,                  // AT-RF 5.3d
            flightTestExitRoomTooLatePoints:0,                       // AT-RF -

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // AT-RF 5.3c
            observationTestTurnpointFalsePoints:100,                 // AT-RF 5.3c
            observationTestEnrouteValueUnit:EnrouteValueUnit.NM,     // AT-RF 5.3c
            observationTestEnrouteCorrectValue:0.5f,                 // AT-RF 5.3c
            observationTestEnrouteInexactValue:1.0f,                 // AT-RF 5.3c
            observationTestEnrouteInexactPoints:15,                  // AT-RF 5.3c
            observationTestEnrouteNotFoundPoints:30,                 // AT-RF 5.3c
            observationTestEnrouteFalsePoints:50,                    // AT-RF 5.3c
            
            // LandingTest
            landingTest1:ContestRulesLanding.AT_RF.data,             // AT-RF
            landingTest2:ContestRulesLanding.AT_RF.data,             // AT-RF
            landingTest3:ContestRulesLanding.AT_RF.data,             // AT-RF
            landingTest4:ContestRulesLanding.AT_RF.data,             // AT-RF

            // Defaults (other)
            precisionFlying:false,                                   // AT-RF
            anrFlying:false,                                         // AT-RF
            flightTestLastGateNoBadCourseSeconds:45,                 // AT-RF
            showPlanningTest:false,                                  // AT-RF
            activateFlightTestCheckLanding:true,                     // AT-RF
            showObservationTest:true,                                // AT-RF 

            // Defaults
            flightPlanShowLegDistance:false,                         // AT-RF
            flightPlanShowTrueTrack:false,                           // AT-RF
            flightPlanShowTrueHeading:false,                         // AT-RF
            flightPlanShowGroundSpeed:true,                          // AT-RF
            flightPlanShowLocalTime:false,                           // AT-RF
            flightPlanShowElapsedTime:true,                          // AT-RF
            flightTestSubmissionMinutes:20,                          // AT-RF
            scGateWidth:1.0f,                                        // AT-RF
            unsuitableStartNum:"13",                                 // AT-RF
            turnpointRule:TurnpointRule.TrueFalsePhoto,              // AT-RF 5.2.5c
            turnpointMapMeasurement:false,                           // AT-RF
            enroutePhotoRule:EnrouteRule.NMFromTP,                   // AT-RF
            enrouteCanvasRule:EnrouteRule.NMFromTP,                  // AT-RF 
            enrouteCanvasMultiple:true,                              // AT-RF 
            minRouteLegs:11,                                         // AT-RF 5.2.5b, 5.2.5c
            maxRouteLegs:16,                                         // AT-RF 5.2.5b, 5.2.5c
            minEnroutePhotos:12,                                     // AT-RF 5.2.5b
            maxEnroutePhotos:20,                                     // AT-RF 5.2.5b
            minEnrouteCanvas:0,                                      // AT-RF 5.2.5a
            maxEnrouteCanvas:5,                                      // AT-RF 5.2.5a
            minEnrouteTargets:12,                                    // AT-RF 5.2.5d
            maxEnrouteTargets:20,                                    // AT-RF 5.2.5d
            useProcedureTurns:false,                                 // AT-RF -
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_RF     // AT-RF 
        ]
    ),

	R12 ([
            ruleTitle:"FAI Air Navigation Race - Edition 2024",      // FAI-ANR
            
			// General
            increaseFactor:0,                                        // FAI-ANR
            printPointsGeneral:false,                                // FAI-ANR
			printPointsPlanningTest:false,                           // FAI-ANR
			printPointsFlightTest:true,                              // FAI-ANR
            printPointsObservationTest:false,                        // FAI-ANR
			printPointsLandingTest1:true,                            // FAI-ANR
			printPointsLandingTest2:true,                            // FAI-ANR
			printPointsLandingTest3:true,                            // FAI-ANR
			printPointsLandingTest4:true,                            // FAI-ANR
            printPointsLandingField:true,                            // FAI-ANR
            printPointsTurnpointSign:false,                          // FAI-ANR
            printPointsEnrouteCanvas:false,                          // FAI-ANR
            printIgnoreEnrouteCanvas:[],                             // FAI-ANR
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                      // FAI-ANR -
			planningTestDirectionPointsPerGrad:2,                    // FAI-ANR -
			planningTestTimeCorrectSecond:5,                         // FAI-ANR -
			planningTestTimePointsPerSecond:1,                       // FAI-ANR -
			planningTestMaxPoints:200,                               // FAI-ANR -
			planningTestPlanTooLatePoints:50,                        // FAI-ANR -
			planningTestExitRoomTooLatePoints:100,                   // FAI-ANR -
            planningTestForbiddenCalculatorsPoints:0,                // FAI-ANR -
		
			// FlightTest
            flightTestTakeoffCorrectSecond:60,                       // FAI-ANR A8.1.2
            flightTestTakeoffCheckSeconds:false,                     // FAI-ANR A8.1.2
            flightTestTakeoffPointsPerSecond:0,                      // FAI-ANR A8.1.2
			flightTestTakeoffMissedPoints:200,                       // FAI-ANR A8.1.2
			flightTestCptimeCorrectSecond:2,                         // FAI-ANR A8.1.3
			flightTestCptimePointsPerSecond:3,                       // FAI-ANR A8.1.3
			flightTestCptimeMaxPoints:200,                           // FAI-ANR A8.1.3
			flightTestCpNotFoundPoints:200,                          // FAI-ANR A8.1.3
			flightTestProcedureTurnNotFlownPoints:0,                 // FAI-ANR -
			flightTestMinAltitudeMissedPoints:500,                   // FAI-ANR A8.1.4
			flightTestBadCourseCorrectSecond:5,                      // FAI-ANR 
			flightTestBadCoursePoints:200,                           // FAI-ANR A8.1.5
            flightTestBadCourseMaxPoints:400,                        // FAI-ANR A8.1.5
			flightTestBadCourseStartLandingPoints:200,               // FAI-ANR A8.1.5
            flightTestBadCourseStartLandingSeparatePoints:true,      // FAI-ANR A8.1.5
			flightTestOutsideCorridorCorrectSecond:5,                // FAI-ANR A8.1.6
			flightTestOutsideCorridorPointsPerSecond:3,              // FAI-ANR A8.1.6
			flightTestLandingToLatePoints:0,                         // FAI-ANR -
			flightTestGivenToLatePoints:0,                           // FAI-ANR -
			flightTestSafetyAndRulesInfringementPoints:0,            // FAI-ANR
			flightTestInstructionsNotFollowedPoints:0,               // FAI-ANR
			flightTestFalseEnvelopeOpenedPoints:0,                   // FAI-ANR
			flightTestSafetyEnvelopeOpenedPoints:0,                  // FAI-ANR
			flightTestFrequencyNotMonitoredPoints:0,                 // FAI-ANR
            flightTestForbiddenEquipmentPoints:0,                    // FAI-ANR
            flightTestExitRoomTooLatePoints:100,                     // FAI-ANR A8.1.1

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // FAI-ANR -
            observationTestTurnpointFalsePoints:100,                 // FAI-ANR -
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // FAI-ANR -
            observationTestEnrouteCorrectValue:5.0f,                 // FAI-ANR -
            observationTestEnrouteInexactValue:0.0f,                 // FAI-ANR -
            observationTestEnrouteInexactPoints:0,                   // FAI-ANR -
            observationTestEnrouteNotFoundPoints:20,                 // FAI-ANR -
            observationTestEnrouteFalsePoints:30,                    // FAI-ANR -
            
			// LandingTest
            landingTest1:ContestRulesLanding.FAI_PF_ANR_NORMAL.data, // FAI-ANR
            landingTest2:ContestRulesLanding.FAI_PF_ANR_IDLE.data,   // FAI-ANR
            landingTest3:ContestRulesLanding.FAI_PF_ANR_IDLE_NO_FLAPS.data, // FAI-ANR
            landingTest4:ContestRulesLanding.FAI_PF_ANR_OBSTACLE.data, // FAI-ANR

            // Defaults (other)
			precisionFlying:false,                                   // FAI-ANR
            anrFlying:true,                                          // FAI-ANR 
            flightTestLastGateNoBadCourseSeconds:10,                 // FAI-ANR like Precision
            showPlanningTest:false,                                  // FAI-ANR 
            activateFlightTestCheckLanding:false,                    // FAI-ANR 
            showObservationTest:false,                               // FAI-ANR 

            // Defaults                                              // FAI-ANR
            flightPlanShowLegDistance:false,                         // FAI-ANR
            flightPlanShowTrueTrack:false,                           // FAI-ANR
            flightPlanShowTrueHeading:false,                         // FAI-ANR
            flightPlanShowGroundSpeed:false,                         // FAI-ANR
            flightPlanShowLocalTime:false,                           // FAI-ANR
            flightPlanShowElapsedTime:false,                         // FAI-ANR
            flightTestSubmissionMinutes:null,                        // FAI-ANR
            scGateWidth:0.0f,                                        // FAI-ANR
            unsuitableStartNum:"13",                                 // FAI-ANR
            turnpointRule:TurnpointRule.None,                        // FAI-ANR -
            turnpointMapMeasurement:false,                           // FAI-ANR -
            enroutePhotoRule:EnrouteRule.None,                       // FAI-ANR -
            enrouteCanvasRule:EnrouteRule.None,                      // FAI-ANR -
            enrouteCanvasMultiple:true,                              // FAI-ANR -
            minRouteLegs:0,                                          // FAI-ANR
            maxRouteLegs:11,                                         // FAI-ANR
            minEnroutePhotos:0,                                      // FAI-ANR -
            maxEnroutePhotos:0,                                      // FAI-ANR -
            minEnrouteCanvas:0,                                      // FAI-ANR -
            maxEnrouteCanvas:0,                                      // FAI-ANR -
            minEnrouteTargets:0,                                     // FAI-ANR -
            maxEnrouteTargets:0,                                     // FAI-ANR -
            useProcedureTurns:false,                                 // FAI-ANR -
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_ANR    // FAI-ANR 
		]
	),

	R13 ([
            ruleTitle:"Wettbewerbsordnung Air Navigation Race Deutschland - Ausgabe 2025", // DE-ANR
            
			// General
            increaseFactor:0,                                        // DE-ANR
            printPointsGeneral:false,                                // DE-ANR
			printPointsPlanningTest:false,                           // DE-ANR
			printPointsFlightTest:true,                              // DE-ANR
            printPointsObservationTest:false,                        // DE-ANR
			printPointsLandingTest1:true,                            // DE-ANR
			printPointsLandingTest2:false,                           // DE-ANR
			printPointsLandingTest3:false,                           // DE-ANR
			printPointsLandingTest4:false,                           // DE-ANR
            printPointsLandingField:true,                            // DE-ANR
            printPointsTurnpointSign:false,                          // DE-ANR
            printPointsEnrouteCanvas:false,                          // DE-ANR
            printIgnoreEnrouteCanvas:[],                             // DE-ANR
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                      // DE-ANR -
			planningTestDirectionPointsPerGrad:2,                    // DE-ANR -
			planningTestTimeCorrectSecond:5,                         // DE-ANR -
			planningTestTimePointsPerSecond:1,                       // DE-ANR -
			planningTestMaxPoints:200,                               // DE-ANR -
			planningTestPlanTooLatePoints:50,                        // DE-ANR -
			planningTestExitRoomTooLatePoints:100,                   // DE-ANR -
            planningTestForbiddenCalculatorsPoints:0,                // DE-ANR -
		
			// FlightTest
            flightTestTakeoffCorrectSecond:60,                       // DE-ANR 3
            flightTestTakeoffCheckSeconds:false,                     // DE-ANR 3
            flightTestTakeoffPointsPerSecond:0,                      // DE-ANR 3
			flightTestTakeoffMissedPoints:200,                       // DE-ANR 3
			flightTestCptimeCorrectSecond:2,                         // DE-ANR 3
			flightTestCptimePointsPerSecond:3,                       // DE-ANR 3
			flightTestCptimeMaxPoints:200,                           // DE-ANR 3
			flightTestCpNotFoundPoints:200,                          // DE-ANR 3
			flightTestProcedureTurnNotFlownPoints:0,                 // DE-ANR -
			flightTestMinAltitudeMissedPoints:500,                   // DE-ANR 3
			flightTestBadCourseCorrectSecond:5,                      // DE-ANR
			flightTestBadCoursePoints:200,                           // DE-ANR 3
            flightTestBadCourseMaxPoints:400,                        // DE-ANR 3
			flightTestBadCourseStartLandingPoints:200,               // DE-ANR 3
            flightTestBadCourseStartLandingSeparatePoints:true,      // DE-ANR 3
			flightTestOutsideCorridorCorrectSecond:5,                // DE-ANR 3
			flightTestOutsideCorridorPointsPerSecond:3,              // DE-ANR 3
			flightTestLandingToLatePoints:0,                         // DE-ANR -
			flightTestGivenToLatePoints:0,                           // DE-ANR -
			flightTestSafetyAndRulesInfringementPoints:0,            // DE-ANR
			flightTestInstructionsNotFollowedPoints:0,               // DE-ANR
			flightTestFalseEnvelopeOpenedPoints:0,                   // DE-ANR -
			flightTestSafetyEnvelopeOpenedPoints:0,                  // DE-ANR
			flightTestFrequencyNotMonitoredPoints:0,                 // DE-ANR
            flightTestForbiddenEquipmentPoints:0,                    // DE-ANR
            flightTestExitRoomTooLatePoints:100,                     // DE-ANR 3
                                                                    
            // ObservationTest                                      
            observationTestTurnpointNotFoundPoints:50,               // DE-ANR -
            observationTestTurnpointFalsePoints:100,                 // DE-ANR -
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // DE-ANR -
            observationTestEnrouteCorrectValue:5.0f,                 // DE-ANR -
            observationTestEnrouteInexactValue:0.0f,                 // DE-ANR -
            observationTestEnrouteInexactPoints:0,                   // DE-ANR -
            observationTestEnrouteNotFoundPoints:20,                 // DE-ANR -
            observationTestEnrouteFalsePoints:30,                    // DE-ANR -
                                                                    
			// LandingTest                                          
            landingTest1:ContestRulesLanding.DE_RF_ANR.data,         // DE-ANR
            landingTest2:ContestRulesLanding.DE_RF_ANR.data,         // DE-ANR
            landingTest3:ContestRulesLanding.DE_RF_ANR.data,         // DE-ANR
            landingTest4:ContestRulesLanding.DE_RF_ANR.data,         // DE-ANR
            
            // Defaults (other)
			precisionFlying:false,                                   // DE-ANR
            anrFlying:true,                                          // DE-ANR
            flightTestLastGateNoBadCourseSeconds:10,                 // DE-ANR
            showPlanningTest:false,                                  // DE-ANR
            activateFlightTestCheckLanding:false,                    // DE-ANR
            showObservationTest:false,                               // DE-ANR

            // Defaults
            flightPlanShowLegDistance:false,                         // DE-ANR
            flightPlanShowTrueTrack:false,                           // DE-ANR
            flightPlanShowTrueHeading:false,                         // DE-ANR
            flightPlanShowGroundSpeed:false,                         // DE-ANR
            flightPlanShowLocalTime:false,                           // DE-ANR
            flightPlanShowElapsedTime:false,                         // DE-ANR
            flightTestSubmissionMinutes:null,                        // DE-ANR
            scGateWidth:0.0f,                                        // DE-ANR
            unsuitableStartNum:"13",                                 // DE-ANR
            turnpointRule:TurnpointRule.None,                        // DE-ANR -
            turnpointMapMeasurement:false,                           // DE-ANR -
            enroutePhotoRule:EnrouteRule.None,                       // DE-ANR -
            enrouteCanvasRule:EnrouteRule.None,                      // DE-ANR -
            enrouteCanvasMultiple:true,                              // DE-ANR -
            minRouteLegs:0,                                          // DE-ANR
            maxRouteLegs:11,                                         // DE-ANR
            minEnroutePhotos:0,                                      // DE-ANR -
            maxEnroutePhotos:0,                                      // DE-ANR -
            minEnrouteCanvas:0,                                      // DE-ANR -
            maxEnrouteCanvas:0,                                      // DE-ANR -
            minEnrouteTargets:0,                                     // DE-ANR -
            maxEnrouteTargets:0,                                     // DE-ANR -
            useProcedureTurns:false,                                 // DE-ANR -
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_ANR    // DE-ANR
		]
	),

	R4 ([
            ruleTitle:"FAI Precision Flying - Edition 2025",         // FAI-PF
            
			// General
            increaseFactor:0,                                        // FAI-PF
            printPointsGeneral:false,                                // FAI-PF
			printPointsPlanningTest:true,                            // FAI-PF
			printPointsFlightTest:true,                              // FAI-PF
            printPointsObservationTest:true,                         // FAI-PF
			printPointsLandingTest1:true,                            // FAI-PF
			printPointsLandingTest2:true,                            // FAI-PF
			printPointsLandingTest3:true,                            // FAI-PF
			printPointsLandingTest4:true,                            // FAI-PF
            printPointsLandingField:true,                            // FAI-PF
            printPointsTurnpointSign:true,                           // FAI-PF
            printPointsEnrouteCanvas:true,                           // FAI-PF
            printIgnoreEnrouteCanvas:[EnrouteCanvasSign.S08],        // FAI-PF
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                      // FAI-PF A10.1.1
			planningTestDirectionPointsPerGrad:2,                    // FAI-PF A10.1.1
			planningTestTimeCorrectSecond:5,                         // FAI-PF A10.1.1
			planningTestTimePointsPerSecond:1,                       // FAI-PF A10.1.1
			planningTestMaxPoints:200,                               // FAI-PF A10.1.1
			planningTestPlanTooLatePoints:50,                        // FAI-PF A10.1.1
			planningTestExitRoomTooLatePoints:100,                   // FAI-PF A10.1.1
            planningTestForbiddenCalculatorsPoints:0,                // FAI-PF 
		
			// FlightTest
            flightTestTakeoffCorrectSecond:60,                       // FAI-PF A10.1.2
            flightTestTakeoffCheckSeconds:false,                     // FAI-PF 
            flightTestTakeoffPointsPerSecond:0,                      // FAI-PF 
			flightTestTakeoffMissedPoints:200,                       // FAI-PF A10.1.2
			flightTestCptimeCorrectSecond:2,                         // FAI-PF A10.1.3
			flightTestCptimePointsPerSecond:3,                       // FAI-PF A10.1.3
			flightTestCptimeMaxPoints:100,                           // FAI-PF A10.1.3
			flightTestCpNotFoundPoints:100,                          // FAI-PF A10.1.3
			flightTestProcedureTurnNotFlownPoints:200,               // FAI-PF A10.1.4
			flightTestMinAltitudeMissedPoints:500,                   // FAI-PF A10.1.5
			flightTestBadCourseCorrectSecond:5,                      // FAI-PF A2.2.13
			flightTestBadCoursePoints:200,                           // FAI-PF A10.1.6
            flightTestBadCourseMaxPoints:0,                          // FAI-PF 
			flightTestBadCourseStartLandingPoints:200,               // FAI-PF A10.1.6
            flightTestBadCourseStartLandingSeparatePoints:false,     // FAI-PF 
			flightTestOutsideCorridorCorrectSecond:0,                // FAI-PF - 
			flightTestOutsideCorridorPointsPerSecond:0,              // FAI-PF - 
			flightTestLandingToLatePoints:200,                       // FAI-PF A10.1.6
			flightTestGivenToLatePoints:100,                         // FAI-PF A10.1.7
			flightTestSafetyAndRulesInfringementPoints:0,            // FAI-PF 
			flightTestInstructionsNotFollowedPoints:0,               // FAI-PF 
			flightTestFalseEnvelopeOpenedPoints:0,                   // FAI-PF 
			flightTestSafetyEnvelopeOpenedPoints:0,                  // FAI-PF 
			flightTestFrequencyNotMonitoredPoints:0,                 // FAI-PF 
            flightTestForbiddenEquipmentPoints:0,                    // FAI-PF 
            flightTestExitRoomTooLatePoints:0,                       // FAI-PF - planningTestExitRoomTooLatePoints

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // FAI-PF A10.2.1
            observationTestTurnpointFalsePoints:100,                 // FAI-PF A10.2.1
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // FAI-PF A10.2.1
            observationTestEnrouteCorrectValue:5.0f,                 // FAI-PF A10.2.1
            observationTestEnrouteInexactValue:0.0f,                 // FAI-PF 
            observationTestEnrouteInexactPoints:0,                   // FAI-PF 
            observationTestEnrouteNotFoundPoints:20,                 // FAI-PF A10.2.1
            observationTestEnrouteFalsePoints:30,                    // FAI-PF A10.2.1
            
			// LandingTest
            landingTest1:ContestRulesLanding.FAI_PF_ANR_NORMAL.data, // FAI-PF
            landingTest2:ContestRulesLanding.FAI_PF_ANR_IDLE.data,   // FAI-PF
            landingTest3:ContestRulesLanding.FAI_PF_ANR_IDLE_NO_FLAPS.data, // FAI-PF
            landingTest4:ContestRulesLanding.FAI_PF_ANR_OBSTACLE.data, // FAI-PF
            
            // Defaults (other)
			precisionFlying:true,                                    // FAI-PF
            anrFlying:false,                                         // FAI-PF
            flightTestLastGateNoBadCourseSeconds:10,                 // FAI-PF A2.2.13, defines 0.5NM around gate
            showPlanningTest:true,                                   // FAI-PF 
            activateFlightTestCheckLanding:true,                     // FAI-PF 
            showObservationTest:true,                                // FAI-PF 

            // Defaults
            flightPlanShowLegDistance:true,                          // FAI-PF 
            flightPlanShowTrueTrack:true,                            // FAI-PF 
            flightPlanShowTrueHeading:true,                          // FAI-PF 
            flightPlanShowGroundSpeed:true,                          // FAI-PF
            flightPlanShowLocalTime:true,                            // FAI-PF
            flightPlanShowElapsedTime:false,                         // FAI-PF
            flightTestSubmissionMinutes:null,                        // FAI-PF
            scGateWidth:1.0f,                                        // FAI-PF
            unsuitableStartNum:"13",                                 // FAI-PF
            turnpointRule:TurnpointRule.AssignCanvas,                // FAI-PF
            turnpointMapMeasurement:true,                            // FAI-PF
            enroutePhotoRule:EnrouteRule.Map,                        // FAI-PF
            enrouteCanvasRule:EnrouteRule.Map,                       // FAI-PF
            enrouteCanvasMultiple:true,                              // FAI-PF
            minRouteLegs:0,                                          // FAI-PF
            maxRouteLegs:8,                                          // FAI-PF A2.2.3
            minEnroutePhotos:8,                                      // FAI-PF A3.2
            maxEnroutePhotos:10,                                     // FAI-PF A3.2
            minEnrouteCanvas:8,                                      // FAI-PF A3.2
            maxEnrouteCanvas:15,                                     // FAI-PF A3.2
            minEnrouteTargets:16,                                    // FAI-PF A3.2
            maxEnrouteTargets:25,                                    // FAI-PF A3.2
            useProcedureTurns:true,                                  // FAI-PF 
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_PF     // FAI-PF 
		]
	),

    R11 ([
            ruleTitle:"Wettbewerbsordnung Navigationsflug Deutschland - Ausgabe 2017", // DE-PF
            
            // General
            increaseFactor:0,                                        // DE-PF
            printPointsGeneral:false,                                // DE-PF
            printPointsPlanningTest:true,                            // DE-PF
            printPointsFlightTest:true,                              // DE-PF
            printPointsObservationTest:true,                         // DE-PF
            printPointsLandingTest1:true,                            // DE-PF
            printPointsLandingTest2:false,                           // DE-PF
            printPointsLandingTest3:false,                           // DE-PF
            printPointsLandingTest4:false,                           // DE-PF
            printPointsLandingField:true,                            // DE-PF
            printPointsTurnpointSign:false,                          // DE-PF
            printPointsEnrouteCanvas:false,                          // DE-PF
            printIgnoreEnrouteCanvas:[],                             // DE-PF
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                      // DE-PF 4.1.1
            planningTestDirectionPointsPerGrad:2,                    // DE-PF 4.1.1
            planningTestTimeCorrectSecond:5,                         // DE-PF 4.1.1
            planningTestTimePointsPerSecond:1,                       // DE-PF 4.1.1
            planningTestMaxPoints:200,                               // DE-PF 4.1.1
            planningTestPlanTooLatePoints:50,                        // DE-PF 4.1.1
            planningTestExitRoomTooLatePoints:100,                   // DE-PF 4.1.1
            planningTestForbiddenCalculatorsPoints:0,                // DE-PF
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                       // DE-PF 4.1.2
            flightTestTakeoffCheckSeconds:false,                     // DE-PF
            flightTestTakeoffPointsPerSecond:0,                      // DE-PF
            flightTestTakeoffMissedPoints:200,                       // DE-PF 4.1.2
            flightTestCptimeCorrectSecond:2,                         // DE-PF 4.1.3
            flightTestCptimePointsPerSecond:1,                       // DE-PF 4.1.3
            flightTestCptimeMaxPoints:200,                           // DE-PF 4.1.3
            flightTestCpNotFoundPoints:200,                          // DE-PF 4.1.3
            flightTestProcedureTurnNotFlownPoints:200,               // DE-PF 4.1.4
            flightTestMinAltitudeMissedPoints:500,                   // DE-PF 4.1.5
            flightTestBadCourseCorrectSecond:5,                      // DE-PF 3.2.9
            flightTestBadCoursePoints:200,                           // DE-PF 4.1.6
            flightTestBadCourseMaxPoints:0,                          // DE-PF
            flightTestBadCourseStartLandingPoints:500,               // DE-PF 4.1.6
            flightTestBadCourseStartLandingSeparatePoints:false,     // DE-PF
			flightTestOutsideCorridorCorrectSecond:0,                // DE-PF -
			flightTestOutsideCorridorPointsPerSecond:0,              // DE-PF -
            flightTestLandingToLatePoints:200,                       // DE-PF 4.1.6
            flightTestGivenToLatePoints:100,                         // DE-PF 4.1.6
            flightTestSafetyAndRulesInfringementPoints:0,            // DE-PF
            flightTestInstructionsNotFollowedPoints:0,               // DE-PF
            flightTestFalseEnvelopeOpenedPoints:0,                   // DE-PF
            flightTestSafetyEnvelopeOpenedPoints:0,                  // DE-PF
            flightTestFrequencyNotMonitoredPoints:0,                 // DE-PF
            flightTestForbiddenEquipmentPoints:0,                    // DE-PF
            flightTestExitRoomTooLatePoints:0,                       // DE-PF - planningTestExitRoomTooLatePoints

            // ObservationTest
            observationTestTurnpointNotFoundPoints:40,               // DE-PF 4.2
            observationTestTurnpointFalsePoints:80,                  // DE-PF 4.2
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // DE-PF 4.2
            observationTestEnrouteCorrectValue:5.0f,                 // DE-PF 4.2
            observationTestEnrouteInexactValue:10.0f,                // DE-PF 4.2
            observationTestEnrouteInexactPoints:10,                  // DE-PF 4.2
            observationTestEnrouteNotFoundPoints:20,                 // DE-PF 4.2
            observationTestEnrouteFalsePoints:40,                    // DE-PF 4.2
            
            // LandingTest
            landingTest1:ContestRulesLanding.DE_PF.data,             // DE-PF
            landingTest2:ContestRulesLanding.DE_PF.data,             // DE-PF
            landingTest3:ContestRulesLanding.DE_PF.data,             // DE-PF
            landingTest4:ContestRulesLanding.DE_PF.data,             // DE-PF
            
            // Defaults (other)
            precisionFlying:false,                                   // DE-PF
            anrFlying:false,                                         // DE-PF
            flightTestLastGateNoBadCourseSeconds:10,                 // DE-PF 
            showPlanningTest:true,                                   // DE-PF
            activateFlightTestCheckLanding:true,                     // DE-PF
            showObservationTest:true,                                // DE-PF

            // Defaults
            flightPlanShowLegDistance:true,                          // DE-PF
            flightPlanShowTrueTrack:true,                            // DE-PF
            flightPlanShowTrueHeading:true,                          // DE-PF
            flightPlanShowGroundSpeed:true,                          // DE-PF
            flightPlanShowLocalTime:true,                            // DE-PF
            flightPlanShowElapsedTime:false,                         // DE-PF
            flightTestSubmissionMinutes:null,                        // DE-PF
            scGateWidth:2.0f,                                        // DE-PF
            unsuitableStartNum:"13",                                 // DE-PF
            turnpointRule:TurnpointRule.AssignTrueFalse,             // DE-PF
            turnpointMapMeasurement:false,                           // DE-PF
            enroutePhotoRule:EnrouteRule.MapOrMeasurement,           // DE-PF
            enrouteCanvasRule:EnrouteRule.None,                      // DE-PF
            enrouteCanvasMultiple:true,                              // DE-PF
            minRouteLegs:0,                                          // DE-PF
            maxRouteLegs:8,                                          // DE-PF 3.2.3
            minEnroutePhotos:0,                                      // DE-PF
            maxEnroutePhotos:20,                                     // DE-PF
            minEnrouteCanvas:0,                                      // DE-PF
            maxEnrouteCanvas:15,                                     // DE-PF
            minEnrouteTargets:0,                                     // DE-PF
            maxEnrouteTargets:35,                                    // DE-PF
            useProcedureTurns:true,                                  // DE-PF
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_PF     // DE-PF
        ]
    ),

	R5 ([
            ruleTitle:"Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft - Ausgabe 2017", // CH-PF
            
			// General
            increaseFactor:0,                                        // CH-PF
            printPointsGeneral:false,                                // CH-PF
			printPointsPlanningTest:true,                            // CH-PF
			printPointsFlightTest:true,                              // CH-PF
            printPointsObservationTest:true,                         // CH-PF
			printPointsLandingTest1:true,                            // CH-PF
			printPointsLandingTest2:true,                            // CH-PF
			printPointsLandingTest3:true,                            // CH-PF
			printPointsLandingTest4:true,                            // CH-PF
            printPointsLandingField:true,                            // CH-PF
            printPointsTurnpointSign:true,                           // CH-PF
            printPointsEnrouteCanvas:true,                           // CH-PF
            printIgnoreEnrouteCanvas:[],                             // CH-PF
            
			// PlanningTest
			planningTestDirectionCorrectGrad:2,                      // CH-PF Anhang 5.1
			planningTestDirectionPointsPerGrad:2,                    // CH-PF Anhang 5.1
			planningTestTimeCorrectSecond:5,                         // CH-PF Anhang 5.1
			planningTestTimePointsPerSecond:1,                       // CH-PF Anhang 5.1
			planningTestMaxPoints:200,                               // CH-PF Anhang 5.1
			planningTestPlanTooLatePoints:50,                        // CH-PF Anhang 5.1
			planningTestExitRoomTooLatePoints:100,                   // CH-PF Anhang 5.1
            planningTestForbiddenCalculatorsPoints:0,                // CH-PF
		
			// FlightTest
			flightTestTakeoffCorrectSecond:60,                       // CH-PF Anhang 5.2
			flightTestTakeoffCheckSeconds:false,                     // CH-PF
			flightTestTakeoffPointsPerSecond:0,                      // CH-PF
            flightTestTakeoffMissedPoints:200,                       // CH-PF Anhang 5.2
			flightTestCptimeCorrectSecond:2,                         // CH-PF Anhang 5.2
			flightTestCptimePointsPerSecond:3,                       // CH-PF Anhang 5.2
			flightTestCptimeMaxPoints:100,                           // CH-PF Anhang 5.2
			flightTestCpNotFoundPoints:100,                          // CH-PF Anhang 5.2
			flightTestProcedureTurnNotFlownPoints:200,               // CH-PF Anhang 5.2
			flightTestMinAltitudeMissedPoints:500,                   // CH-PF Anhang 5.2
			flightTestBadCourseCorrectSecond:5,                      // CH-PF 4.7.15
			flightTestBadCoursePoints:200,                           // CH-PF Anhang 5.2
            flightTestBadCourseMaxPoints:0,                          // CH-PF
			flightTestBadCourseStartLandingPoints:200,               // CH-PF Anhang 5.2
            flightTestBadCourseStartLandingSeparatePoints:false,     // CH-PF
			flightTestOutsideCorridorCorrectSecond:0,                // CH-PF -
			flightTestOutsideCorridorPointsPerSecond:0,              // CH-PF -
			flightTestLandingToLatePoints:200,                       // CH-PF Anhang 5.2
			flightTestGivenToLatePoints:100,                         // CH-PF Anhang 5.2
			flightTestSafetyAndRulesInfringementPoints:0,            // CH-PF
			flightTestInstructionsNotFollowedPoints:0,               // CH-PF
			flightTestFalseEnvelopeOpenedPoints:0,                   // CH-PF
			flightTestSafetyEnvelopeOpenedPoints:0,                  // CH-PF
			flightTestFrequencyNotMonitoredPoints:0,                 // CH-PF
            flightTestForbiddenEquipmentPoints:0,                    // CH-PF
            flightTestExitRoomTooLatePoints:0,                       // CH-PF - planningTestExitRoomTooLatePoints

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // CH-PF Anhang 5.3
            observationTestTurnpointFalsePoints:100,                 // CH-PF Anhang 5.3
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // CH-PF Anhang 5.3
            observationTestEnrouteCorrectValue:5.0f,                 // CH-PF Anhang 5.3
            observationTestEnrouteInexactValue:0.0f,                 // CH-PF
            observationTestEnrouteInexactPoints:0,                   // CH-PF
            observationTestEnrouteNotFoundPoints:20,                 // CH-PF Anhang 5.3
            observationTestEnrouteFalsePoints:30,                    // CH-PF Anhang 5.3
            
			// LandingTest
            landingTest1:ContestRulesLanding.CH_PF_NORMAL.data,      // CH-PF
            landingTest2:ContestRulesLanding.CH_PF_IDLE.data,        // CH-PF
            landingTest3:ContestRulesLanding.CH_PF_IDLE_NO_FLAPS.data, // CH-PF
            landingTest4:ContestRulesLanding.CH_PF_OBSTACLE.data,    // CH-PF
            
            // Defaults (other)
			precisionFlying:true,                                    // CH-PF
            anrFlying:false,                                         // CH-PF
            flightTestLastGateNoBadCourseSeconds:10,                 // CH-PF 4.7.15 defines 800m after gate passing
            showPlanningTest:true,                                   // CH-PF
            activateFlightTestCheckLanding:true,                     // CH-PF
            showObservationTest:true,                                // CH-PF

            // Defaults
            flightPlanShowLegDistance:true,                          // CH-PF
            flightPlanShowTrueTrack:true,                            // CH-PF
            flightPlanShowTrueHeading:true,                          // CH-PF
            flightPlanShowGroundSpeed:true,                          // CH-PF
            flightPlanShowLocalTime:true,                            // CH-PF
            flightPlanShowElapsedTime:false,                         // CH-PF
            flightTestSubmissionMinutes:null,                        // CH-PF
            scGateWidth:1.0f,                                        // CH-PF
            unsuitableStartNum:"13",                                 // CH-PF
            turnpointRule:TurnpointRule.AssignCanvas,                // CH-PF
            turnpointMapMeasurement:true,                            // CH-PF
            enroutePhotoRule:EnrouteRule.Map,                        // CH-PF
            enrouteCanvasRule:EnrouteRule.Map,                       // CH-PF
            enrouteCanvasMultiple:true,                              // CH-PF
            minRouteLegs:5,                                          // CH-PF 4.7.12
            maxRouteLegs:8,                                          // CH-PF 4.7.12
            minEnroutePhotos:8,                                      // CH-PF 4.8.1
            maxEnroutePhotos:10,                                     // CH-PF 4.8.1
            minEnrouteCanvas:8,                                      // CH-PF 4.8.1
            maxEnrouteCanvas:15,                                     // CH-PF 4.8.1
            minEnrouteTargets:16,                                    // CH-PF 4.8.1
            maxEnrouteTargets:25,                                    // CH-PF 4.8.1
            useProcedureTurns:true,                                  // CH-PF
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_PF     // CH-PF
		]
	),

    R7 ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Ausgabe 2023", // AT-PF1
            
            // General
            increaseFactor:20,                                       // AT-PF1 4.1
            printPointsGeneral:true,                                 // AT-PF1
            printPointsPlanningTest:true,                            // AT-PF1
            printPointsFlightTest:true,                              // AT-PF1
            printPointsObservationTest:true,                         // AT-PF1
            printPointsLandingTest1:true,                            // AT-PF1
            printPointsLandingTest2:true,                            // AT-PF1
            printPointsLandingTest3:true,                            // AT-PF1
            printPointsLandingTest4:true,                            // AT-PF1
            printPointsLandingField:true,                            // AT-PF1
            printPointsTurnpointSign:true,                           // AT-PF1
            printPointsEnrouteCanvas:true,                           // AT-PF1
            printIgnoreEnrouteCanvas:[EnrouteCanvasSign.S08],        // AT-PF1
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                      // AT-PF1 4.5
            planningTestDirectionPointsPerGrad:2,                    // AT-PF1 4.5
            planningTestTimeCorrectSecond:5,                         // AT-PF1 4.5
            planningTestTimePointsPerSecond:1,                       // AT-PF1 4.5
            planningTestMaxPoints:350,                               // AT-PF1 4.5
            planningTestPlanTooLatePoints:50,                        // AT-PF1 4.5
            planningTestExitRoomTooLatePoints:100,                   // AT-PF1 4.5
            planningTestForbiddenCalculatorsPoints:300,              // AT-PF1 4.5
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                       // AT-PF1 4.5
            flightTestTakeoffCheckSeconds:false,                     // AT-PF1 4.5
            flightTestTakeoffPointsPerSecond:0,                      // AT-PF1 4.5
            flightTestTakeoffMissedPoints:200,                       // AT-PF1 4.5
            flightTestCptimeCorrectSecond:2,                         // AT-PF1 4.5
            flightTestCptimePointsPerSecond:3,                       // AT-PF1 4.5
            flightTestCptimeMaxPoints:100,                           // AT-PF1 4.5
            flightTestCpNotFoundPoints:100,                          // AT-PF1 4.5
            flightTestProcedureTurnNotFlownPoints:200,               // AT-PF1 4.5
            flightTestMinAltitudeMissedPoints:500,                   // AT-PF1 4.5
            flightTestBadCourseCorrectSecond:5,                      // AT-PF1 4.5
            flightTestBadCoursePoints:200,                           // AT-PF1 4.5
            flightTestBadCourseMaxPoints:0,                          // AT-PF1
            flightTestBadCourseStartLandingPoints:200,               // AT-PF1 4.5
            flightTestBadCourseStartLandingSeparatePoints:false,     // AT-PF1
			flightTestOutsideCorridorCorrectSecond:0,                // AT-PF1 -
			flightTestOutsideCorridorPointsPerSecond:0,              // AT-PF1 -
            flightTestLandingToLatePoints:200,                       // AT-PF1 4.5
            flightTestGivenToLatePoints:100,                         // AT-PF1 4.5
            flightTestSafetyAndRulesInfringementPoints:0,            // AT-PF1
            flightTestInstructionsNotFollowedPoints:0,               // AT-PF1
            flightTestFalseEnvelopeOpenedPoints:0,                   // AT-PF1
            flightTestSafetyEnvelopeOpenedPoints:0,                  // AT-PF1
            flightTestFrequencyNotMonitoredPoints:0,                 // AT-PF1
            flightTestForbiddenEquipmentPoints:0,                    // AT-PF1
            flightTestExitRoomTooLatePoints:0,                       // AT-PF1 - planningTestExitRoomTooLatePoints

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // AT-PF1 4.5
            observationTestTurnpointFalsePoints:100,                 // AT-PF1 4.5
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // AT-PF1 4.5
            observationTestEnrouteCorrectValue:5.0f,                 // AT-PF1 4.5
            observationTestEnrouteInexactValue:0.0f,                 // AT-PF1
            observationTestEnrouteInexactPoints:0,                   // AT-PF1
            observationTestEnrouteNotFoundPoints:20,                 // AT-PF1 4.5
            observationTestEnrouteFalsePoints:30,                    // AT-PF1 4.5
            
            // LandingTest
            landingTest1:ContestRulesLanding.AT_PF1_NORMAL.data,     // AT-PF1
            landingTest2:ContestRulesLanding.AT_PF1_IDLE.data,       // AT-PF1
            landingTest3:ContestRulesLanding.AT_PF1_IDLE_NO_FLAPS.data, // AT-PF1
            landingTest4:ContestRulesLanding.AT_PF1_OBSTACLE.data,   // AT-PF1
            
            // Defaults (other)
            precisionFlying:true,                                    // AT-PF1
            anrFlying:false,                                         // AT-PF1
            flightTestLastGateNoBadCourseSeconds:10,                 // AT-PF1 4.2.2j defines 0.5NM
            showPlanningTest:true,                                   // AT-PF1
            activateFlightTestCheckLanding:true,                     // AT-PF1
            showObservationTest:true,                                // AT-PF1

            // Defaults
            flightPlanShowLegDistance:true,                          // AT-PF1
            flightPlanShowTrueTrack:true,                            // AT-PF1
            flightPlanShowTrueHeading:true,                          // AT-PF1
            flightPlanShowGroundSpeed:true,                          // AT-PF1
            flightPlanShowLocalTime:true,                            // AT-PF1
            flightPlanShowElapsedTime:false,                         // AT-PF1
            flightTestSubmissionMinutes:null,                        // AT-PF1
            scGateWidth:1.0f,                                        // AT-PF1
            unsuitableStartNum:"13",                                 // AT-PF1
            turnpointRule:TurnpointRule.AssignCanvas,                // AT-PF1
            turnpointMapMeasurement:true,                            // AT-PF1
            enroutePhotoRule:EnrouteRule.Map,                        // AT-PF1
            enrouteCanvasRule:EnrouteRule.Map,                       // AT-PF1
            enrouteCanvasMultiple:true,                              // AT-PF1
            minRouteLegs:0,                                          // AT-PF1
            maxRouteLegs:8,                                          // AT-PF1 4.2.2c
            minEnroutePhotos:8,                                      // AT-PF1 4.3.a
            maxEnroutePhotos:10,                                     // AT-PF1 4.3.a
            minEnrouteCanvas:5,                                      // AT-PF1 4.3.a
            maxEnrouteCanvas:8,                                      // AT-PF1 4.3.a
            minEnrouteTargets:13,                                    // AT-PF1 4.3.a
            maxEnrouteTargets:18,                                    // AT-PF1 4.3.a
            useProcedureTurns:true,                                  // AT-PF1
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_PF     // AT-PF1
        ]
    ),

    R8 ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 2 - Ausgabe 2023", // AT-PF2
            
            // General
            increaseFactor:20,                                       // AT-PF2 4.1
            printPointsGeneral:true,                                 // AT-PF2
            printPointsPlanningTest:true,                            // AT-PF2
            printPointsFlightTest:true,                              // AT-PF2
            printPointsObservationTest:true,                         // AT-PF2
            printPointsLandingTest1:true,                            // AT-PF2
            printPointsLandingTest2:true,                            // AT-PF2
            printPointsLandingTest3:true,                            // AT-PF2
            printPointsLandingTest4:true,                            // AT-PF2
            printPointsLandingField:true,                            // AT-PF2
            printPointsTurnpointSign:true,                           // AT-PF2
            printPointsEnrouteCanvas:true,                           // AT-PF2
            printIgnoreEnrouteCanvas:[EnrouteCanvasSign.S08],        // AT-PF2
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                      // AT-PF2 4.5
            planningTestDirectionPointsPerGrad:2,                    // AT-PF2 4.5
            planningTestTimeCorrectSecond:5,                         // AT-PF2 4.5
            planningTestTimePointsPerSecond:1,                       // AT-PF2 4.5
            planningTestMaxPoints:350,                               // AT-PF2 4.5
            planningTestPlanTooLatePoints:50,                        // AT-PF2 4.5
            planningTestExitRoomTooLatePoints:100,                   // AT-PF2 4.5
            planningTestForbiddenCalculatorsPoints:300,              // AT-PF2 4.5
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                       // AT-PF2 4.5
            flightTestTakeoffCheckSeconds:false,                     // AT-PF2 4.5
            flightTestTakeoffPointsPerSecond:0,                      // AT-PF2 4.5
            flightTestTakeoffMissedPoints:200,                       // AT-PF2 4.5
            flightTestCptimeCorrectSecond:2,                         // AT-PF2 4.5
            flightTestCptimePointsPerSecond:3,                       // AT-PF2 4.5
            flightTestCptimeMaxPoints:100,                           // AT-PF2 4.5
            flightTestCpNotFoundPoints:100,                          // AT-PF2 4.5
            flightTestProcedureTurnNotFlownPoints:200,               // AT-PF2 4.5
            flightTestMinAltitudeMissedPoints:500,                   // AT-PF2 4.5
            flightTestBadCourseCorrectSecond:5,                      // AT-PF2 4.5
            flightTestBadCoursePoints:200,                           // AT-PF2 4.5
            flightTestBadCourseMaxPoints:0,                          // AT-PF2
            flightTestBadCourseStartLandingPoints:200,               // AT-PF2 4.5
            flightTestBadCourseStartLandingSeparatePoints:false,     // AT-PF2
			flightTestOutsideCorridorCorrectSecond:0,                // AT-PF2 -
			flightTestOutsideCorridorPointsPerSecond:0,              // AT-PF2 -
            flightTestLandingToLatePoints:200,                       // AT-PF2 4.5
            flightTestGivenToLatePoints:100,                         // AT-PF2 4.5
            flightTestSafetyAndRulesInfringementPoints:0,            // AT-PF2
            flightTestInstructionsNotFollowedPoints:0,               // AT-PF2
            flightTestFalseEnvelopeOpenedPoints:0,                   // AT-PF2
            flightTestSafetyEnvelopeOpenedPoints:0,                  // AT-PF2
            flightTestFrequencyNotMonitoredPoints:0,                 // AT-PF2
            flightTestForbiddenEquipmentPoints:0,                    // AT-PF2
            flightTestExitRoomTooLatePoints:0,                       // AT-PF2 - planningTestExitRoomTooLatePoints

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // AT-PF2 4.5
            observationTestTurnpointFalsePoints:100,                 // AT-PF2 4.5
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // AT-PF2 4.5
            observationTestEnrouteCorrectValue:5.0f,                 // AT-PF2 4.5
            observationTestEnrouteInexactValue:0.0f,                 // AT-PF2
            observationTestEnrouteInexactPoints:0,                   // AT-PF2
            observationTestEnrouteNotFoundPoints:20,                 // AT-PF2 4.5
            observationTestEnrouteFalsePoints:30,                    // AT-PF2 4.5
            
            // LandingTest
            landingTest1:ContestRulesLanding.AT_PF2_NORMAL.data,     // AT-PF2
            landingTest2:ContestRulesLanding.AT_PF2_IDLE.data,       // AT-PF2
            landingTest3:ContestRulesLanding.AT_PF2_IDLE_NO_FLAPS.data, // AT-PF2
            landingTest4:ContestRulesLanding.AT_PF2_OBSTACLE.data,   // AT-PF2
            
            // Defaults (other)
            precisionFlying:true,                                    // AT-PF2
            anrFlying:false,                                         // AT-PF2
            flightTestLastGateNoBadCourseSeconds:10,                 // AT-PF2 4.2.2j defines 0.5NM
            showPlanningTest:true,                                   // AT-PF2
            activateFlightTestCheckLanding:true,                     // AT-PF2
            showObservationTest:true,                                // AT-PF2

            // Defaults
            flightPlanShowLegDistance:true,                          // AT-PF2
            flightPlanShowTrueTrack:true,                            // AT-PF2
            flightPlanShowTrueHeading:true,                          // AT-PF2
            flightPlanShowGroundSpeed:true,                          // AT-PF2
            flightPlanShowLocalTime:true,                            // AT-PF2
            flightPlanShowElapsedTime:false,                         // AT-PF2
            flightTestSubmissionMinutes:null,                        // AT-PF2
            scGateWidth:1.0f,                                        // AT-PF2
            unsuitableStartNum:"13",                                 // AT-PF2
            turnpointRule:TurnpointRule.AssignCanvas,                // AT-PF2
            turnpointMapMeasurement:true,                            // AT-PF2
            enroutePhotoRule:EnrouteRule.Map,                        // AT-PF2
            enrouteCanvasRule:EnrouteRule.Map,                       // AT-PF2
            enrouteCanvasMultiple:true,                              // AT-PF2
            minRouteLegs:0,                                          // AT-PF2
            maxRouteLegs:8,                                          // AT-PF2 4.2.2c
            minEnroutePhotos:8,                                      // AT-PF2 4.3a
            maxEnroutePhotos:10,                                     // AT-PF2 4.3a
            minEnrouteCanvas:5,                                      // AT-PF2 4.3a
            maxEnrouteCanvas:8,                                      // AT-PF2 4.3a
            minEnrouteTargets:13,                                    // AT-PF2 4.3a
            maxEnrouteTargets:18,                                    // AT-PF2 4.3a
            useProcedureTurns:true,                                  // AT-PF2
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_PF     // AT-PF2
        ]
    ),

    R9 ([
            ruleTitle:"Pr\u00E4zisionsflug-Reglement \u00D6sterreich - Landefeld Typ 3 - Ausgabe 2023", // AT-PF3
            
            // General
            increaseFactor:20,                                       // AT-PF3 4.1
            printPointsGeneral:true,                                 // AT-PF3
            printPointsPlanningTest:true,                            // AT-PF3
            printPointsFlightTest:true,                              // AT-PF3
            printPointsObservationTest:true,                         // AT-PF3
            printPointsLandingTest1:true,                            // AT-PF3
            printPointsLandingTest2:true,                            // AT-PF3
            printPointsLandingTest3:true,                            // AT-PF3
            printPointsLandingTest4:true,                            // AT-PF3
            printPointsLandingField:true,                            // AT-PF3
            printPointsTurnpointSign:true,                           // AT-PF3
            printPointsEnrouteCanvas:true,                           // AT-PF3
            printIgnoreEnrouteCanvas:[EnrouteCanvasSign.S08],        // AT-PF3
            
            // PlanningTest
            planningTestDirectionCorrectGrad:2,                      // AT-PF3 4.5
            planningTestDirectionPointsPerGrad:2,                    // AT-PF3 4.5
            planningTestTimeCorrectSecond:5,                         // AT-PF3 4.5
            planningTestTimePointsPerSecond:1,                       // AT-PF3 4.5
            planningTestMaxPoints:350,                               // AT-PF3 4.5
            planningTestPlanTooLatePoints:50,                        // AT-PF3 4.5
            planningTestExitRoomTooLatePoints:100,                   // AT-PF3 4.5
            planningTestForbiddenCalculatorsPoints:300,              // AT-PF3 4.5
        
            // FlightTest
            flightTestTakeoffCorrectSecond:60,                       // AT-PF3 4.5
            flightTestTakeoffCheckSeconds:false,                     // AT-PF3
            flightTestTakeoffPointsPerSecond:0,                      // AT-PF3
            flightTestTakeoffMissedPoints:200,                       // AT-PF3 4.5
            flightTestCptimeCorrectSecond:2,                         // AT-PF3 4.5
            flightTestCptimePointsPerSecond:3,                       // AT-PF3 4.5
            flightTestCptimeMaxPoints:100,                           // AT-PF3 4.5
            flightTestCpNotFoundPoints:100,                          // AT-PF3 4.5
            flightTestProcedureTurnNotFlownPoints:200,               // AT-PF3 4.5
            flightTestMinAltitudeMissedPoints:500,                   // AT-PF3 4.5
            flightTestBadCourseCorrectSecond:5,                      // AT-PF3 4.5
            flightTestBadCoursePoints:200,                           // AT-PF3 4.5
            flightTestBadCourseMaxPoints:0,                          // AT-PF3
            flightTestBadCourseStartLandingPoints:200,               // AT-PF3 4.5
            flightTestBadCourseStartLandingSeparatePoints:false,     // AT-PF3
			flightTestOutsideCorridorCorrectSecond:0,                // AT-PF3 -
			flightTestOutsideCorridorPointsPerSecond:0,              // AT-PF3 -
            flightTestLandingToLatePoints:200,                       // AT-PF3 4.5
            flightTestGivenToLatePoints:100,                         // AT-PF3 4.5
            flightTestSafetyAndRulesInfringementPoints:0,            // AT-PF3
            flightTestInstructionsNotFollowedPoints:0,               // AT-PF3
            flightTestFalseEnvelopeOpenedPoints:0,                   // AT-PF3
            flightTestSafetyEnvelopeOpenedPoints:0,                  // AT-PF3
            flightTestFrequencyNotMonitoredPoints:0,                 // AT-PF3
            flightTestForbiddenEquipmentPoints:0,                    // AT-PF3
            flightTestExitRoomTooLatePoints:0,                       // AT-PF3 - planningTestExitRoomTooLatePoints

            // ObservationTest
            observationTestTurnpointNotFoundPoints:50,               // AT-PF3 4.5
            observationTestTurnpointFalsePoints:100,                 // AT-PF3 4.5
            observationTestEnrouteValueUnit:EnrouteValueUnit.mm,     // AT-PF3 4.5
            observationTestEnrouteCorrectValue:5.0f,                 // AT-PF3 4.5
            observationTestEnrouteInexactValue:0.0f,                 // AT-PF3
            observationTestEnrouteInexactPoints:0,                   // AT-PF3
            observationTestEnrouteNotFoundPoints:20,                 // AT-PF3 4.5
            observationTestEnrouteFalsePoints:30,                    // AT-PF3 4.5
            
            // LandingTest
            landingTest1:ContestRulesLanding.AT_PF3_NORMAL.data,     // AT-PF3
            landingTest2:ContestRulesLanding.AT_PF3_IDLE.data,       // AT-PF3
            landingTest3:ContestRulesLanding.AT_PF3_IDLE_NO_FLAPS.data, // AT-PF3
            landingTest4:ContestRulesLanding.AT_PF3_OBSTACLE.data,   // AT-PF3
            
            // Defaults (other)
            precisionFlying:true,                                    // AT-PF3
            anrFlying:false,                                         // AT-PF3
            flightTestLastGateNoBadCourseSeconds:10,                 // AT-PF3 4.2.2j defines 0.5NM
            showPlanningTest:true,                                   // AT-PF3
            activateFlightTestCheckLanding:true,                     // AT-PF3
            showObservationTest:true,                                // AT-PF3

            // Defaults
            flightPlanShowLegDistance:true,                          // AT-PF3
            flightPlanShowTrueTrack:true,                            // AT-PF3
            flightPlanShowTrueHeading:true,                          // AT-PF3
            flightPlanShowGroundSpeed:true,                          // AT-PF3
            flightPlanShowLocalTime:true,                            // AT-PF3
            flightPlanShowElapsedTime:false,                         // AT-PF3
            flightTestSubmissionMinutes:null,                        // AT-PF3
            scGateWidth:1.0f,                                        // AT-PF3
            unsuitableStartNum:"13",                                 // AT-PF3
            turnpointRule:TurnpointRule.AssignCanvas,                // AT-PF3
            turnpointMapMeasurement:true,                            // AT-PF3
            enroutePhotoRule:EnrouteRule.Map,                        // AT-PF3
            enrouteCanvasRule:EnrouteRule.Map,                       // AT-PF3
            enrouteCanvasMultiple:true,                              // AT-PF3
            minRouteLegs:0,                                          // AT-PF3
            maxRouteLegs:8,                                          // AT-PF3 4.2.2c
            minEnroutePhotos:8,                                      // AT-PF3 4.3a
            maxEnroutePhotos:10,                                     // AT-PF3 4.3a
            minEnrouteCanvas:5,                                      // AT-PF3 4.3a
            maxEnrouteCanvas:8,                                      // AT-PF3 4.3a
            minEnrouteTargets:13,                                    // AT-PF3 4.3a
            maxEnrouteTargets:18,                                    // AT-PF3 4.3a
            useProcedureTurns:true,                                  // AT-PF3
            liveTrackingScorecard:Defs.LIVETRACKING_SCORECARD_PF     // AT-PF3
        ]
    ),
    
    Empty([ruleTitle:''])

	ContestRules(Map ruleValues) 
	{
		this.ruleValues = ruleValues
	}
	
	final Map ruleValues
    
    static List GetContestRules(boolean withEmpty)
    {
        List ret = []
        if (withEmpty) {
            ret += Empty
        }
        ret += R1  // DE-RF   Wettbewerbsordnung Rallyeflug Deutschland
        ret += R13 // DE-ANR  Wettbewerbsordnung Air Navigation Race Deutschland
        //ret += R11 // DE-PF   Wettbewerbsordnung Navigationsflug Deutschland
        ret += R6  // FAI-RF  FAI Air Rally Flying
        ret += R12 // FAI-ANR FAI Air Navigation Race
        ret += R4  // FAI-PF  FAI Precision Flying
        ret += R5  // CH-PF   Wettkampfreglement Pr\u00E4zisionsflug-Schweizermeisterschaft
        ret += R10 // AT-RF   Navigationsflug-Reglement \u00D6sterreich
        ret += R7  // AT-PF   Pr\u00E4zisionsflug-Reglement \u00D6sterreich
        //ret += R8  // AT-PF   Pr\u00E4zisionsflug-Reglement \u00D6sterreich
        //ret += R9  // AT-PF   Pr\u00E4zisionsflug-Reglement \u00D6sterreich
        return ret
    }
    
}