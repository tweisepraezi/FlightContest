import java.util.Map;

class ResultClass
// DB-2.0
{
	String name
	String shortName = ""                               // DB-2.8
	String contestTitle = ""
	ContestRules contestRule = ContestRules.R1          // Wettbewerbsordnung
	Boolean precisionFlying = false                     // DB-2.3
    Integer increaseFactor = 0                          // DB-2.13
	
	// Klassen-Auswertung
	String contestTaskResults = ""                      // Zu berücksichtigende Aufgaben, DB-2.1
	String contestTeamResults = ""                      // Zu berücksichtigende Teams, DB-2.3
	boolean contestPlanningResults = true               // Planungstest berüchsichtigen
	boolean contestFlightResults = true                 // Navigationstest berüchsichtigen
	boolean contestObservationResults = true            // Beobachtungstest berüchsichtigen
	boolean contestLandingResults = true                // Landetest berüchsichtigen
	boolean contestSpecialResults = false               // Sondertest berücksichtigen
	String contestPrintSubtitle = ""                    // Ausdruck-Untertitel, DB-2.3
	Boolean contestPrintLandscape = true                // Ausdruck quer, DB-2.1
	Boolean contestPrintTaskDetails = false             // Ausdruck der Aufgabensummen in Liste, DB-2.1
    String contestPrintTaskTestDetails = ""             // Ausdruck der Aufgabendetails in Liste, DB-2.8
    Boolean contestPrintObservationDetails = false      // Ausdruck der Beobachtungsdetails in Liste, DB-2.13
    Boolean contestPrintLandingDetails = false          // Ausdruck der Landedetails in Liste, DB-2.8
	Boolean contestPrintTaskNamesInTitle = false        // Ausdruck der Aufgabennamen im Title, DB-2.1
	Boolean contestPrintAircraft = true                 // Ausdruck des Flugzeuges in Liste, DB-2.8
	Boolean contestPrintTeam = false                    // Ausdruck des Teams in Liste, DB-2.8
	Boolean contestPrintClass = false                   // Ausdruck der Klasse in Liste, DB-2.8
	Boolean contestPrintShortClass = false              // Ausdruck des kurzen Klassennamens in Liste, DB-2.8
	Boolean contestPrintProvisional = false             // Ausdruck "vorläufig", DB-2.3
	Boolean contestPrintA3 = false                      // Ausdruck A3, DB-2.3
    Boolean contestPrintEqualPositions = false          // Ausdruck gleicher Positionen, DB-2.8
	
	// PlanningTest
	int planningTestDirectionCorrectGrad = 2
	int planningTestDirectionPointsPerGrad = 2
	int planningTestTimeCorrectSecond = 5
	int planningTestTimePointsPerSecond = 1
	int planningTestMaxPoints = 350
	int planningTestPlanTooLatePoints = 50
	int planningTestExitRoomTooLatePoints = 100
    Integer planningTestForbiddenCalculatorsPoints = 0  // DB-2.13
    
	// FlightTest
	int flightTestTakeoffMissedPoints = 200
	Integer flightTestTakeoffCorrectSecond = 60         // DB-2.3
	Boolean flightTestTakeoffCheckSeconds = false       // DB-2.3
	Integer flightTestTakeoffPointsPerSecond = 3        // DB-2.3
	int flightTestCptimeCorrectSecond = 2
	int flightTestCptimePointsPerSecond = 1
	int flightTestCptimeMaxPoints = 200
	int flightTestCpNotFoundPoints = 200
	int flightTestProcedureTurnNotFlownPoints = 200
	int flightTestMinAltitudeMissedPoints = 500
	int flightTestBadCourseCorrectSecond = 5
	int flightTestBadCoursePoints = 200
	int flightTestBadCourseStartLandingPoints = 500
	int flightTestLandingToLatePoints = 200
	int flightTestGivenToLatePoints = 100
	Integer flightTestSafetyAndRulesInfringementPoints = 0  // DB-2.3
	Integer flightTestInstructionsNotFollowedPoints = 0 // DB-2.3
	Integer flightTestFalseEnvelopeOpenedPoints = 0     // DB-2.3
	Integer flightTestSafetyEnvelopeOpenedPoints = 0    // DB-2.3
	Integer flightTestFrequencyNotMonitoredPoints = 0   // DB-2.3
    Integer flightTestForbiddenEquipmentPoints = 0      // DB-2.13

    // ObservationTest
    EnrouteValueUnit observationTestEnrouteValueUnit = EnrouteValueUnit.mm // DB-2.13
    Float observationTestEnrouteCorrectValue = 5.0f     // DB-2.13
    Float observationTestEnrouteInexactValue = 10.0f    // DB-2.13
    Integer observationTestEnrouteInexactPoints = 10    // DB-2.13
    Integer observationTestEnrouteNotFoundPoints = 20   // DB-2.13
    Integer observationTestEnrouteFalsePoints = 40      // DB-2.13
    Integer observationTestTurnpointNotFoundPoints = 40 // DB-2.13
    Integer observationTestTurnpointFalsePoints = 80    // DB-2.13
            
	// LandingTest
	int landingTest1MaxPoints = 300
	int landingTest1NoLandingPoints = 300
	int landingTest1OutsideLandingPoints = 200
	int landingTest1RollingOutsidePoints = 200
	int landingTest1PowerInBoxPoints = 50
	int landingTest1GoAroundWithoutTouchingPoints = 200
	int landingTest1GoAroundInsteadStopPoints = 200
	int landingTest1AbnormalLandingPoints = 150
	Integer landingTest1NotAllowedAerodynamicAuxiliariesPoints = 0 // DB-2.7
	String landingTest1PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}"
	
	int landingTest2MaxPoints = 300
	int landingTest2NoLandingPoints = 300
	int landingTest2OutsideLandingPoints = 200
	int landingTest2RollingOutsidePoints = 200
	int landingTest2PowerInBoxPoints = 50
	int landingTest2GoAroundWithoutTouchingPoints = 200
	int landingTest2GoAroundInsteadStopPoints = 200
	int landingTest2AbnormalLandingPoints = 150
	Integer landingTest2NotAllowedAerodynamicAuxiliariesPoints = 0 // DB-2.7
	int landingTest2PowerInAirPoints = 200
	String landingTest2PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}"
	
	int landingTest3MaxPoints = 300
	int landingTest3NoLandingPoints = 300
	int landingTest3OutsideLandingPoints = 200
	int landingTest3RollingOutsidePoints = 200
	int landingTest3PowerInBoxPoints = 50
	int landingTest3GoAroundWithoutTouchingPoints = 200
	int landingTest3GoAroundInsteadStopPoints = 200
	int landingTest3AbnormalLandingPoints = 150
	Integer landingTest3NotAllowedAerodynamicAuxiliariesPoints = 0 // DB-2.7
	int landingTest3PowerInAirPoints = 200
	int landingTest3FlapsInAirPoints = 200
	String landingTest3PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}"
	
	int landingTest4MaxPoints = 300
	int landingTest4NoLandingPoints = 300
	int landingTest4OutsideLandingPoints = 200
	int landingTest4RollingOutsidePoints = 200
	int landingTest4PowerInBoxPoints = 50
	int landingTest4GoAroundWithoutTouchingPoints = 200
	int landingTest4GoAroundInsteadStopPoints = 200
	int landingTest4AbnormalLandingPoints = 150
	Integer landingTest4NotAllowedAerodynamicAuxiliariesPoints = 0 // DB-2.7
	int landingTest4TouchingObstaclePoints = 400
	String landingTest4PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}"
	
	// Points print settings
	String printPointsPrintTitle = ""                   // DB-2.3
    Boolean printPointsGeneral = true                   // DB-2.13
	Boolean printPointsPlanningTest = true              // DB-2.3
	Boolean printPointsFlightTest = true                // DB-2.3
    Boolean printPointsObservationTest = true           // DB-2.13
	Boolean printPointsLandingTest1 = true              // DB-2.3
	Boolean printPointsLandingTest2 = true              // DB-2.3
	Boolean printPointsLandingTest3 = true              // DB-2.3
	Boolean printPointsLandingTest4 = true              // DB-2.3
    Boolean printPointsLandingField = true              // DB-2.13
    String landingFieldImageName = ""                   // DB-2.13
    Boolean printPointsTurnpointSign = false            // DB-2.13
    Boolean printPointsEnrouteCanvas = false            // DB-2.13
    String printLandingCalculatorValues = ""            // DB-2.8
	Boolean printPointsZero = false                     // DB-2.3
	Boolean printPointsLandscape = false                // DB-2.3
	Boolean printPointsA3 = false                       // DB-2.3

	static belongsTo = [contest:Contest]

	static constraints = {
		name(blank:false)
		contest(nullable:false)
		
		planningTestDirectionCorrectGrad(blank:false, min:0)
		planningTestDirectionPointsPerGrad(blank:false, min:0)
		planningTestTimeCorrectSecond(blank:false, min:0)
		planningTestTimePointsPerSecond(blank:false, min:0)
		planningTestMaxPoints(blank:false, min:0)
		planningTestPlanTooLatePoints(blank:false, min:0)
		planningTestExitRoomTooLatePoints(blank:false, min:0)
		
	    flightTestTakeoffMissedPoints(blank:false, min:0)
	    flightTestCptimeCorrectSecond(blank:false, min:0)
	    flightTestCptimePointsPerSecond(blank:false, min:0)
	    flightTestCptimeMaxPoints(blank:false, min:0)
	    flightTestCpNotFoundPoints(blank:false, min:0)
	    flightTestProcedureTurnNotFlownPoints(blank:false, min:0)
	    flightTestMinAltitudeMissedPoints(blank:false, min:0)
	    flightTestBadCourseCorrectSecond(blank:false, min:0)
	    flightTestBadCoursePoints(blank:false, min:0)
	    flightTestBadCourseStartLandingPoints(blank:false, min:0)
	    flightTestLandingToLatePoints(blank:false, min:0)
	    flightTestGivenToLatePoints(blank:false, min:0)
		
		landingTest1MaxPoints(blank:false, min:0)
		landingTest1NoLandingPoints(blank:false, min:0)
		landingTest1OutsideLandingPoints(blank:false, min:0)
		landingTest1RollingOutsidePoints(blank:false, min:0)
		landingTest1PowerInBoxPoints(blank:false, min:0)
		landingTest1GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest1GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest1AbnormalLandingPoints(blank:false, min:0)
		landingTest1PenaltyCalculator(blank:false, size:0..4096)
		
		landingTest2MaxPoints(blank:false, min:0)
		landingTest2NoLandingPoints(blank:false, min:0)
		landingTest2OutsideLandingPoints(blank:false, min:0)
		landingTest2RollingOutsidePoints(blank:false, min:0)
		landingTest2PowerInBoxPoints(blank:false, min:0)
		landingTest2GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest2GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest2AbnormalLandingPoints(blank:false, min:0)
		landingTest2PowerInAirPoints(blank:false, min:0)
		landingTest2PenaltyCalculator(blank:false, size:0..4096)
		
		landingTest3MaxPoints(blank:false, min:0)
		landingTest3NoLandingPoints(blank:false, min:0)
		landingTest3OutsideLandingPoints(blank:false, min:0)
		landingTest3RollingOutsidePoints(blank:false, min:0)
		landingTest3PowerInBoxPoints(blank:false, min:0)
		landingTest3GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest3GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest3AbnormalLandingPoints(blank:false, min:0)
		landingTest3PowerInAirPoints(blank:false, min:0)
		landingTest3FlapsInAirPoints(blank:false, min:0)
		landingTest3PenaltyCalculator(blank:false, size:0..4096)
		
		landingTest4MaxPoints(blank:false, min:0)
		landingTest4NoLandingPoints(blank:false, min:0)
		landingTest4OutsideLandingPoints(blank:false, min:0)
		landingTest4RollingOutsidePoints(blank:false, min:0)
		landingTest4PowerInBoxPoints(blank:false, min:0)
		landingTest4GoAroundWithoutTouchingPoints(blank:false, min:0)
		landingTest4GoAroundInsteadStopPoints(blank:false, min:0)
		landingTest4AbnormalLandingPoints(blank:false, min:0)
		landingTest4TouchingObstaclePoints(blank:false, min:0)
		landingTest4PenaltyCalculator(blank:false, size:0..4096)
		
		// DB-2.1 compatibility
		contestTaskResults(nullable:true)
		contestPrintLandscape(nullable:true)
		contestPrintTaskDetails(nullable:true)
		contestPrintTaskNamesInTitle(nullable:true)
		
		// DB-2.3 compatibility
		precisionFlying(nullable:true)
		contestPrintSubtitle(nullable:true)
		contestPrintProvisional(nullable:true)
		contestPrintA3(nullable:true)
		contestTeamResults(nullable:true)
		flightTestTakeoffCorrectSecond(nullable:true, min:0)
		flightTestTakeoffCheckSeconds(nullable:true)
		flightTestTakeoffPointsPerSecond(nullable:true, min:0)
		flightTestSafetyAndRulesInfringementPoints(nullable:true, min:0)
		flightTestInstructionsNotFollowedPoints(nullable:true, min:0)
		flightTestFalseEnvelopeOpenedPoints(nullable:true, min:0)
		flightTestSafetyEnvelopeOpenedPoints(nullable:true, min:0)
		flightTestFrequencyNotMonitoredPoints(nullable:true, min:0)
		printPointsPrintTitle(nullable:true)
		printPointsPlanningTest(nullable:true)
		printPointsFlightTest(nullable:true)
		printPointsLandingTest1(nullable:true)
		printPointsLandingTest2(nullable:true)
		printPointsLandingTest3(nullable:true)
		printPointsLandingTest4(nullable:true)
		printPointsZero(nullable:true)
		printPointsLandscape(nullable:true)
		printPointsA3(nullable:true)
		
		// DB-2.7 compatibility
		landingTest1NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		landingTest2NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		landingTest3NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)
		landingTest4NotAllowedAerodynamicAuxiliariesPoints(nullable:true, min:0)

		// DB-2.8 compatibility
		shortName(nullable:true,blank:false)
		contestPrintAircraft(nullable:true)
		contestPrintTeam(nullable:true)
		contestPrintClass(nullable:true)
		contestPrintShortClass(nullable:true)
        contestPrintTaskTestDetails(nullable:true)
        contestPrintLandingDetails(nullable:true)
        contestPrintEqualPositions(nullable:true)
        printLandingCalculatorValues(nullable:true)
        
        // DB-2.13 compatibility
        planningTestForbiddenCalculatorsPoints(nullable:true, min:0)
        flightTestForbiddenEquipmentPoints(nullable:true, min:0)
        observationTestEnrouteValueUnit(nullable:true)
        observationTestEnrouteCorrectValue(nullable:true, min:0.0f)
        observationTestEnrouteInexactValue(nullable:true, min:0.0f)
        observationTestEnrouteInexactPoints(nullable:true, min:0)
        observationTestEnrouteNotFoundPoints(nullable:true, min:0)
        observationTestEnrouteFalsePoints(nullable:true, min:0)
        observationTestTurnpointNotFoundPoints(nullable:true, min:0)
        observationTestTurnpointFalsePoints(nullable:true, min:0)
        increaseFactor(nullable:true, min:0)
        printPointsGeneral(nullable:true)
        printPointsObservationTest(nullable:true)
        contestPrintObservationDetails(nullable:true)
        printPointsLandingField(nullable:true)
        landingFieldImageName(nullable:true)
        printPointsTurnpointSign(nullable:true)
        printPointsEnrouteCanvas(nullable:true)
	}

	String GetPrintContestTitle()
	{
		if (contestTitle) {
			return contestTitle
		}
		return contest.title
	}
	
	String GetPrintTitle(String msgID)
	{
		if (contestTitle) {
			return "${getPrintMsg(msgID)}"
		}
		return "${getPrintMsg(msgID)} ${name}"
	}
	
	String GetPrintTitle2(String msgID)
	{
		if (contestTitle) {
			return "${getPrintMsg(msgID)} - ${contestTitle}" 
		}
		return "${getPrintMsg(msgID)} - ${name}"
	}
	
	String GetListTitle(String msgID)
	{
		if (contestTitle) {
			return "${getMsg(msgID)} - ${contestTitle}" 
		}
		return "${getMsg(msgID)} - ${name}"
	}
	
	boolean IsPlanningTestRun(Task taskInstance)
	{
		TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
		return taskclass_instance.planningTestRun
	}
	
    boolean IsPlanningTestRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsPlanningTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsFlightTestRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.flightTestRun
    }
    
    boolean IsFlightTestRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsFlightTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsObservationTestRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.observationTestRun
    }
    
    boolean IsObservationTestRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsObservationTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsObservationTestTurnpointRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.observationTestTurnpointRun
    }
    
    boolean IsObservationTestTurnpointRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsObservationTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsObservationTestEnroutePhotoRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.observationTestEnroutePhotoRun
    }
    
    boolean IsObservationTestEnroutePhotoRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsObservationTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsObservationTestEnrouteCanvasRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.observationTestEnrouteCanvasRun
    }
    
    boolean IsObservationTestEnrouteCanvasRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsObservationTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsLandingTestRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.landingTestRun
    }
    
    boolean IsLandingTestRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsLandingTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsLandingTest1Run(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.landingTest1Run
    }
    
    boolean IsLandingTest1Run()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsLandingTest1Run(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsLandingTest2Run(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.landingTest2Run
    }
    
    boolean IsLandingTest2Run()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsLandingTest2Run(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsLandingTest3Run(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.landingTest3Run
    }
    
    boolean IsLandingTest3Run()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsLandingTest3Run(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsLandingTest4Run(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.landingTest4Run
    }
    
    boolean IsLandingTest4Run()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsLandingTest4Run(task_instance)) {
                return true
            }
        }
        return false
    }
    
    boolean IsSpecialTestRun(Task taskInstance)
    {
        TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(taskInstance,this,[sort:"id"])
        return taskclass_instance.specialTestRun
    }
    
    boolean IsSpecialTestRun()
    {
        for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
            if (IsSpecialTestRun(task_instance)) {
                return true
            }
        }
        return false
    }
    
	Map GetTeamResultSettings()
	{
		Map ret = [:]
		for (Task task_instance in Task.findAllByContest(contest,[sort:"id"])) {
			for(TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance,[sort:"id"])) {
				if (taskclass_instance.resultclass == this) {
					if (taskclass_instance.planningTestRun) {
						ret += [Planning:true]
					}
					if (taskclass_instance.flightTestRun) {
						ret += [Flight:true]
					}
					if (taskclass_instance.observationTestRun) {
						ret += [Observation:true]
					}
					if (taskclass_instance.landingTestRun) {
						ret += [Landing:true]
					}
					if (taskclass_instance.specialTestRun) {
						ret += [Special:true]
					}
				}
			}
		}
		return ret
	}
	
	void CopyValues(ResultClass resultClassInstance)
	{
		name = resultClassInstance.name
        shortName = resultClassInstance.shortName
		contestTitle = resultClassInstance.contestTitle
        
		contestRule = resultClassInstance.contestRule
        
		precisionFlying = resultClassInstance.precisionFlying
        increaseFactor = resultClassInstance.increaseFactor
        landingFieldImageName = resultClassInstance.landingFieldImageName
        printLandingCalculatorValues = resultClassInstance.printLandingCalculatorValues
		
		planningTestDirectionCorrectGrad = resultClassInstance.planningTestDirectionCorrectGrad 
		planningTestDirectionPointsPerGrad = resultClassInstance.planningTestDirectionPointsPerGrad
		planningTestTimeCorrectSecond = resultClassInstance.planningTestTimeCorrectSecond
		planningTestTimePointsPerSecond = resultClassInstance.planningTestTimePointsPerSecond
		planningTestMaxPoints = resultClassInstance.planningTestMaxPoints
		planningTestPlanTooLatePoints = resultClassInstance.planningTestPlanTooLatePoints
		planningTestExitRoomTooLatePoints = resultClassInstance.planningTestExitRoomTooLatePoints
        planningTestForbiddenCalculatorsPoints = resultClassInstance.planningTestForbiddenCalculatorsPoints
        
		flightTestTakeoffMissedPoints = resultClassInstance.flightTestTakeoffMissedPoints
		flightTestTakeoffCorrectSecond = resultClassInstance.flightTestTakeoffCorrectSecond
		flightTestTakeoffCheckSeconds = resultClassInstance.flightTestTakeoffCheckSeconds
		flightTestTakeoffPointsPerSecond = resultClassInstance.flightTestTakeoffPointsPerSecond
		flightTestCptimeCorrectSecond = resultClassInstance.flightTestCptimeCorrectSecond
		flightTestCptimePointsPerSecond = resultClassInstance.flightTestCptimePointsPerSecond
		flightTestCptimeMaxPoints = resultClassInstance.flightTestCptimeMaxPoints
		flightTestCpNotFoundPoints = resultClassInstance.flightTestCpNotFoundPoints
		flightTestProcedureTurnNotFlownPoints = resultClassInstance.flightTestProcedureTurnNotFlownPoints
		flightTestMinAltitudeMissedPoints = resultClassInstance.flightTestMinAltitudeMissedPoints
		flightTestBadCourseCorrectSecond = resultClassInstance.flightTestBadCourseCorrectSecond
		flightTestBadCoursePoints = resultClassInstance.flightTestBadCoursePoints
		flightTestBadCourseStartLandingPoints = resultClassInstance.flightTestBadCourseStartLandingPoints
		flightTestLandingToLatePoints = resultClassInstance.flightTestLandingToLatePoints
		flightTestGivenToLatePoints = resultClassInstance.flightTestGivenToLatePoints
		flightTestSafetyAndRulesInfringementPoints = resultClassInstance.flightTestSafetyAndRulesInfringementPoints
		flightTestInstructionsNotFollowedPoints = resultClassInstance.flightTestInstructionsNotFollowedPoints
		flightTestFalseEnvelopeOpenedPoints = resultClassInstance.flightTestFalseEnvelopeOpenedPoints
		flightTestSafetyEnvelopeOpenedPoints = resultClassInstance.flightTestSafetyEnvelopeOpenedPoints
		flightTestFrequencyNotMonitoredPoints = resultClassInstance.flightTestFrequencyNotMonitoredPoints
        flightTestForbiddenEquipmentPoints = resultClassInstance.flightTestForbiddenEquipmentPoints

        observationTestEnrouteValueUnit = resultClassInstance.observationTestEnrouteValueUnit
        observationTestEnrouteCorrectValue = resultClassInstance.observationTestEnrouteCorrectValue
        observationTestEnrouteInexactValue = resultClassInstance.observationTestEnrouteInexactValue
        observationTestEnrouteInexactPoints = resultClassInstance.observationTestEnrouteInexactPoints
        observationTestEnrouteNotFoundPoints = resultClassInstance.observationTestEnrouteNotFoundPoints
        observationTestEnrouteFalsePoints = resultClassInstance.observationTestEnrouteFalsePoints
        observationTestTurnpointNotFoundPoints = resultClassInstance.observationTestTurnpointNotFoundPoints
        observationTestTurnpointFalsePoints = resultClassInstance.observationTestTurnpointFalsePoints
                
		landingTest1MaxPoints = resultClassInstance.landingTest1MaxPoints
		landingTest1NoLandingPoints = resultClassInstance.landingTest1NoLandingPoints
		landingTest1OutsideLandingPoints = resultClassInstance.landingTest1OutsideLandingPoints
		landingTest1RollingOutsidePoints = resultClassInstance.landingTest1RollingOutsidePoints
		landingTest1PowerInBoxPoints = resultClassInstance.landingTest1PowerInBoxPoints
		landingTest1GoAroundWithoutTouchingPoints = resultClassInstance.landingTest1GoAroundWithoutTouchingPoints
		landingTest1GoAroundInsteadStopPoints = resultClassInstance.landingTest1GoAroundInsteadStopPoints
		landingTest1AbnormalLandingPoints = resultClassInstance.landingTest1AbnormalLandingPoints
		landingTest1NotAllowedAerodynamicAuxiliariesPoints = resultClassInstance.landingTest1NotAllowedAerodynamicAuxiliariesPoints
		landingTest1PenaltyCalculator = resultClassInstance.landingTest1PenaltyCalculator
		
		landingTest2MaxPoints = resultClassInstance.landingTest2MaxPoints
		landingTest2NoLandingPoints = resultClassInstance.landingTest2NoLandingPoints
		landingTest2OutsideLandingPoints = resultClassInstance.landingTest2OutsideLandingPoints
		landingTest2RollingOutsidePoints = resultClassInstance.landingTest2RollingOutsidePoints
		landingTest2PowerInBoxPoints = resultClassInstance.landingTest2PowerInBoxPoints
		landingTest2GoAroundWithoutTouchingPoints = resultClassInstance.landingTest2GoAroundWithoutTouchingPoints
		landingTest2GoAroundInsteadStopPoints = resultClassInstance.landingTest2GoAroundInsteadStopPoints
		landingTest2AbnormalLandingPoints = resultClassInstance.landingTest2AbnormalLandingPoints
		landingTest2NotAllowedAerodynamicAuxiliariesPoints = resultClassInstance.landingTest2NotAllowedAerodynamicAuxiliariesPoints
		landingTest2PowerInAirPoints = resultClassInstance.landingTest2PowerInAirPoints
		landingTest2PenaltyCalculator = resultClassInstance.landingTest2PenaltyCalculator
		
		landingTest3MaxPoints = resultClassInstance.landingTest3MaxPoints
		landingTest3NoLandingPoints = resultClassInstance.landingTest3NoLandingPoints
		landingTest3OutsideLandingPoints = resultClassInstance.landingTest3OutsideLandingPoints
		landingTest3RollingOutsidePoints = resultClassInstance.landingTest3RollingOutsidePoints
		landingTest3PowerInBoxPoints = resultClassInstance.landingTest3PowerInBoxPoints
		landingTest3GoAroundWithoutTouchingPoints = resultClassInstance.landingTest3GoAroundWithoutTouchingPoints
		landingTest3GoAroundInsteadStopPoints = resultClassInstance.landingTest3GoAroundInsteadStopPoints
		landingTest3AbnormalLandingPoints = resultClassInstance.landingTest3AbnormalLandingPoints
		landingTest3NotAllowedAerodynamicAuxiliariesPoints = resultClassInstance.landingTest3NotAllowedAerodynamicAuxiliariesPoints
		landingTest3PowerInAirPoints = resultClassInstance.landingTest3PowerInAirPoints
		landingTest3FlapsInAirPoints = resultClassInstance.landingTest3FlapsInAirPoints
		landingTest3PenaltyCalculator = resultClassInstance.landingTest3PenaltyCalculator
		
		landingTest4MaxPoints = resultClassInstance.landingTest4MaxPoints
		landingTest4NoLandingPoints = resultClassInstance.landingTest4NoLandingPoints
		landingTest4OutsideLandingPoints = resultClassInstance.landingTest4OutsideLandingPoints
		landingTest4RollingOutsidePoints = resultClassInstance.landingTest4RollingOutsidePoints
		landingTest4PowerInBoxPoints = resultClassInstance.landingTest4PowerInBoxPoints
		landingTest4GoAroundWithoutTouchingPoints = resultClassInstance.landingTest4GoAroundWithoutTouchingPoints
		landingTest4GoAroundInsteadStopPoints = resultClassInstance.landingTest4GoAroundInsteadStopPoints
		landingTest4AbnormalLandingPoints = resultClassInstance.landingTest4AbnormalLandingPoints
		landingTest4NotAllowedAerodynamicAuxiliariesPoints = resultClassInstance.landingTest4NotAllowedAerodynamicAuxiliariesPoints
		landingTest4TouchingObstaclePoints = resultClassInstance.landingTest4TouchingObstaclePoints
		landingTest4PenaltyCalculator = resultClassInstance.landingTest4PenaltyCalculator
		
		if (!this.save()) {
			throw new Exception("ResultClass.CopyValues could not save")
		}
	}
	
	boolean IsClassResultsProvisional(Map resultSettings, String resultTaskIDs)
	{
		if (contestPrintProvisional) {
			return true
		}
	    for (Crew crew_instance in Crew.findAllByContestAndDisabled(contest,false,[sort:'classPosition'])) {
	    	if (crew_instance.resultclass == this) {
                for (Task task_instance in contest.GetResultTasks(resultTaskIDs)) {
                	Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
					if (test_instance && !test_instance.disabledCrew) {
	                	if (test_instance.IsTestClassResultsProvisional(resultSettings,this)) {
							return true
						}
					}
                }
	    	}
	    }
		return false
	}
	
	Map GetClassResultSettings()
	{
		Map ret = [:]
		if (contestPrintTaskNamesInTitle) {
			String task_names = ""
			for (Task task_instance in contest.GetResultTasks(contestTaskResults)) {
				if (task_names) {
					task_names += ", "
				}
				task_names += task_instance.name()
			}
			if (task_names) {
				ret += [Tasks:task_names]
			}
		}
		if (contestPlanningResults && IsPlanningTestRun()) {
			ret += [Planning:true]
		}
		if (contestFlightResults && IsFlightTestRun()) {
			ret += [Flight:true]
		}
		if (contestObservationResults && IsObservationTestRun()) {
			ret += [Observation:true]
		}
		if (contestLandingResults && IsLandingTestRun()) {
			ret += [Landing:true]
		}
		if (contestSpecialResults && IsSpecialTestRun()) {
			ret += [Special:true]
		}
		return ret
	}

	String GetDefaultShortName()
	{
		return name.substring(0,1)
	}

    long GetNextID()
    {
        long next_id = 0
        boolean set_next = false
        for (ResultClass resultclass_instance in ResultClass.findAllByContest(this.contest,[sort:'name'])) {
            if (set_next) {
                next_id = resultclass_instance.id
                set_next = false
            }
            if (resultclass_instance.id == this.id) { // BUG: direkter Klassen-Vergleich geht nicht, wenn Route-Instance bereits woanders geändert
                set_next = true
            }
        }
        return next_id
    }
    
    static long GetNextID2(long resultClassID)
    {
        long next_id = 0
        if (resultClassID) {
            ResultClass resultclass_instance = ResultClass.get(resultClassID)
            if (resultclass_instance) {
                next_id = resultclass_instance.GetNextID()
            }
        }
        return next_id
    }
}
