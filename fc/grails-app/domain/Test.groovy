import java.util.Map;

class Test 
{
	Crew crew
	PlanningTestTask planningtesttask
	FlightTestWind flighttestwind
	
	int viewpos = 0
	BigDecimal taskTAS = 0
	Aircraft taskAircraft                                  // DB-2.3
	
	// planning
	boolean timeCalculated = false
	int timetableVersion = 0
	Date testingTime    = Date.parse("HH:mm","00:00")
	Date endTestingTime = Date.parse("HH:mm","00:00")
	Date takeoffTime    = Date.parse("HH:mm","00:00")
	Date startTime      = Date.parse("HH:mm","00:00")
	Date finishTime     = Date.parse("HH:mm","00:00")
    Date maxLandingTime = Date.parse("HH:mm","00:00")
	Date arrivalTime    = Date.parse("HH:mm","00:00")

	boolean arrivalTimeWarning = false
	boolean takeoffTimeWarning = false
	
	// results
	int     planningTestLegPenalties = 0
	boolean planningTestLegComplete = false
	boolean planningTestGivenTooLate = false
	boolean planningTestExitRoomTooLate = false
	int     planningTestOtherPenalties = 0                 // DB-2.0
	int     planningTestPenalties = 0
	boolean planningTestComplete = false
	boolean planningTestModified = true                    // Änderungsanzeige, DB-2.0
	int     planningTestVersion = 0                        // Änderungsanzeige, DB-2.0

    int     flightTestCheckPointPenalties = 0
	boolean flightTestCheckPointsComplete = false
	boolean flightTestTakeoffMissed = false
	boolean flightTestBadCourseStartLanding = false
	boolean flightTestLandingTooLate = false
    boolean flightTestGivenTooLate = false
	Boolean flightTestSafetyAndRulesInfringement = false   // DB-2.3
	Boolean flightTestInstructionsNotFollowed = false      // DB-2.3
	Boolean flightTestFalseEnvelopeOpened = false          // DB-2.3
	Boolean flightTestSafetyEnvelopeOpened = false         // DB-2.3
	Boolean flightTestFrequencyNotMonitored = false        // DB-2.3
    int     flightTestOtherPenalties = 0                   // DB-2.0
    int     flightTestPenalties = 0
    boolean flightTestComplete = false
	boolean flightTestModified = true                      // Änderungsanzeige, DB-2.0
	int     flightTestVersion = 0                          // Änderungsanzeige, DB-2.0

    int     observationTestRoutePhotoPenalties = 0
    int     observationTestTurnPointPhotoPenalties = 0
    int     observationTestGroundTargetPenalties = 0
    int     observationTestPenalties = 0
	boolean observationTestComplete = false
	boolean observationTestModified = true                 // Änderungsanzeige, DB-2.0
	int     observationTestVersion = 0                     // Änderungsanzeige, DB-2.0

	String  landingTest1Measure = ""                       // DB-2.0
	int     landingTest1MeasurePenalties = 0               // DB-2.0
	int     landingTest1Penalties = 0                      // DB-2.0
	int     landingTest1Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest1RollingOutside = false             // DB-2.0
	boolean landingTest1PowerInBox = false                 // DB-2.0
	boolean landingTest1GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest1GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest1AbnormalLanding = false            // DB-2.0
	
	String  landingTest2Measure = ""                       // DB-2.0
	int     landingTest2MeasurePenalties = 0               // DB-2.0
	int     landingTest2Penalties = 0                      // DB-2.0
	int     landingTest2Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest2RollingOutside = false             // DB-2.0
	boolean landingTest2PowerInBox = false                 // DB-2.0
	boolean landingTest2GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest2GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest2AbnormalLanding = false            // DB-2.0
	boolean landingTest2PowerInAir = false                 // DB-2.0
	
	String  landingTest3Measure = ""                       // DB-2.0
	int     landingTest3MeasurePenalties = 0               // DB-2.0
	int     landingTest3Penalties = 0                      // DB-2.0
	int     landingTest3Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest3RollingOutside = false             // DB-2.0
	boolean landingTest3PowerInBox = false                 // DB-2.0
	boolean landingTest3GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest3GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest3AbnormalLanding = false            // DB-2.0
	boolean landingTest3PowerInAir = false                 // DB-2.0
	boolean landingTest3FlapsInAir = false                 // DB-2.0
	
	String  landingTest4Measure = ""                       // DB-2.0
	int     landingTest4MeasurePenalties = 0               // DB-2.0
	int     landingTest4Penalties = 0                      // DB-2.0
	int     landingTest4Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest4RollingOutside = false             // DB-2.0
	boolean landingTest4PowerInBox = false                 // DB-2.0
	boolean landingTest4GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest4GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest4AbnormalLanding = false            // DB-2.0
	boolean landingTest4TouchingObstacle = false           // DB-2.0
	
	int     landingTestOtherPenalties = 0                  // DB-2.0
	int     landingTestPenalties = 0
    boolean landingTestComplete = false
	boolean landingTestModified = true                     // Änderungsanzeige, DB-2.0
	int     landingTestVersion = 0                         // Änderungsanzeige, DB-2.0

	int     specialTestPenalties = 0 
	boolean specialTestComplete = false
	boolean specialTestModified = true                     // Änderungsanzeige, DB-2.0
	int     specialTestVersion = 0                         // Änderungsanzeige, DB-2.0

	boolean crewResultsModified = true                     // Änderungsanzeige, DB-2.0
	int     crewResultsVersion = 0                         // Änderungsanzeige, DB-2.0
	
	int taskPenalties = 0
    int taskPosition = 0
    
	// transient values 
	static transients = ['printPlanningResults','printFlightResults','printObservationResults','printLandingResults','printSpecialResults','printProvisionalResults',]
	boolean printPlanningResults = true
	boolean printFlightResults = true
	boolean printObservationResults = true
	boolean printLandingResults = true
	boolean printSpecialResults = true
	boolean printProvisionalResults = false
	
	static belongsTo = [task:Task]
	
	static hasMany = [testlegplannings:TestLegPlanning,testlegflights:TestLegFlight,coordresults:CoordResult]
	
    static constraints = {
		crew(nullable:true)
		planningtesttask(nullable:true)
		flighttestwind(nullable:true)
		taskTAS(scale:10)
		planningTestOtherPenalties(min:0)
		flightTestOtherPenalties(min:0)
		landingTestOtherPenalties(min:0)
		landingTestPenalties(min:0)
		
		// DB-2.3 compatibility
		taskAircraft(nullable:true)
		flightTestSafetyAndRulesInfringement(nullable:true)
		flightTestInstructionsNotFollowed(nullable:true)
		flightTestFalseEnvelopeOpened(nullable:true)
		flightTestSafetyEnvelopeOpened(nullable:true)
		flightTestFrequencyNotMonitored(nullable:true)
    }

	static mapping = {
		testlegplannings sort:"id"
		testlegflights sort:"id"
		coordresults sort:"id"
	}
	
	void ResetPlanningTestResults()
	{
		planningTestLegPenalties = 0
		planningTestLegComplete = false
		planningTestGivenTooLate = false
		planningTestExitRoomTooLate = false
		planningTestPenalties = 0
		planningTestComplete = false
	}
	
	void ResetFlightTestResults()
	{
		flightTestCheckPointPenalties = 0
		flightTestCheckPointsComplete = false
		flightTestTakeoffMissed = false
		flightTestBadCourseStartLanding = false
		flightTestLandingTooLate = false
		flightTestGivenTooLate = false
		flightTestSafetyAndRulesInfringement = false
		flightTestInstructionsNotFollowed = false
		flightTestFalseEnvelopeOpened = false
		flightTestSafetyEnvelopeOpened = false
		flightTestFrequencyNotMonitored = false
		flightTestPenalties = 0
		flightTestComplete = false
	}
	
	void ResetObservationTestResults()
	{
		observationTestRoutePhotoPenalties = 0
		observationTestTurnPointPhotoPenalties = 0
		observationTestGroundTargetPenalties = 0
		observationTestPenalties = 0
		observationTestComplete = false
	}
	
	void ResetLandingTestResults()
	{
		landingTest1Penalties = 0
		landingTest2Penalties = 0
		landingTest3Penalties = 0
		landingTest4Penalties = 0
		landingTestPenalties = 0
		landingTestComplete = false
	}
	
	void ResetSpecialTestResults()
	{
		specialTestPenalties = 0
		specialTestComplete = false
	}

	int GetPlanningTestVersion()
	{
		if (planningTestModified) {
			return planningTestVersion + 1 	
		}
		return planningTestVersion
	}

	int GetFlightTestVersion()
	{	
		if (flightTestModified) {
			return flightTestVersion + 1
		}
		return flightTestVersion
	}

	int GetObservationTestVersion()
	{	
		if (observationTestModified) {
			return observationTestVersion + 1
		}
		return observationTestVersion
	}

	int GetLandingTestVersion()
	{	
		if (landingTestModified) {
			return landingTestVersion + 1
		}
		return landingTestVersion
	}

	int GetSpecialTestVersion()
	{	
		if (specialTestModified) {
			return specialTestVersion + 1
		}
		return specialTestVersion
	}

	int GetCrewResultsVersion()
	{	
		if (crewResultsModified) {
			return crewResultsVersion + 1
		}
		return crewResultsVersion
	}
	
	void CalculateTestPenalties()
	{
		taskPosition = 0
    	taskPenalties = 0
		if (IsPlanningTestRun()) {
    		taskPenalties += planningTestPenalties
		}
		if (IsFlightTestRun()) {
    		taskPenalties += flightTestPenalties
		}
		if (IsObservationTestRun()) {
			taskPenalties += observationTestPenalties
		}
		if (IsLandingTestRun()) {
        	taskPenalties += landingTestPenalties
		}
		if (IsSpecialTestRun()) {
			taskPenalties += specialTestPenalties
		}
		crew.planningPenalties = 0
		crew.flightPenalties = 0
		crew.observationPenalties = 0
		crew.landingPenalties = 0
		crew.specialPenalties = 0
		crew.contestPenalties = 0
		crew.contestPosition = 0
		crew.noContestPosition = false
		crew.classPosition = 0
		crew.noClassPosition = false
		crew.teamPenalties = 0
		if (crew.team) {
			crew.team.contestPenalties = 0
			crew.team.contestPosition = 0
		}
		crewResultsModified = true
	}
	
	boolean IsPlanningTestRun()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.planningTestRun
				}
			}
			return false
		}
		return task.planningTestRun
	}
	
	boolean IsFlightTestRun()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.flightTestRun
				}
			}
			return false
		}
		return task.flightTestRun
	}
	
	boolean IsObservationTestRun()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.observationTestRun
				}
			}
			return false
		}
		return task.observationTestRun
	}
	
	boolean IsLandingTestRun()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.landingTestRun
				}
			}
			return false
		}
		return task.landingTestRun
	}
	
	boolean IsLandingTest1Run()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.landingTest1Run
				}
			}
			return false
		}
		return task.landingTest1Run
	}
	
	boolean IsLandingTest2Run()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.landingTest2Run
				}
			}
			return false
		}
		return task.landingTest2Run
	}
	
	boolean IsLandingTest3Run()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.landingTest3Run
				}
			}
			return false
		}
		return task.landingTest3Run
	}
	
	boolean IsLandingTest4Run()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.landingTest4Run
				}
			}
			return false
		}
		return task.landingTest4Run
	}
	
	boolean IsLandingTestAnyRun()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.landingTest1Run || taskclass_instance.landingTest2Run || taskclass_instance.landingTest3Run || taskclass_instance.landingTest4Run
				}
			}
			return false
		}
		return task.landingTest1Run || task.landingTest2Run || task.landingTest3Run || task.landingTest4Run
	}
	
	boolean IsPrecisionFlying()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.precisionFlying
			}
			return false
		}
		return task.contest.precisionFlying
	}
	
	boolean IsSpecialTestRun()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.specialTestRun
				}
			}
			return false
		}
		return task.specialTestRun
	}

	boolean IsPlanningTestDistanceMeasure()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.planningTestDistanceMeasure
				}
			}
			return false
		}
		return task.planningTestDistanceMeasure
	}

	boolean IsPlanningTestDirectionMeasure()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.planningTestDirectionMeasure
				}
			}
			return false
		}
		return task.planningTestDirectionMeasure
	}

	boolean IsFlightTestCheckSecretPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.flightTestCheckSecretPoints
				}
			}
			return false
		}
		return task.flightTestCheckSecretPoints
	}

	boolean IsFlightTestCheckTakeOff()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.flightTestCheckTakeOff
				}
			}
			return false
		}
		return task.flightTestCheckTakeOff
	}

	boolean IsFlightTestCheckLanding()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					return taskclass_instance.flightTestCheckLanding
				}
			}
			return false
		}
		return task.flightTestCheckLanding
	}

	Map GetResultSettings()
	{
		Map ret = [:]
		if (printPlanningResults && IsPlanningTestRun()) {
			ret += [Planning:true]
		}
		if (printFlightResults && IsFlightTestRun()) {
			ret += [Flight:true]
		}
		if (printObservationResults && IsObservationTestRun()) {
			ret += [Observation:true]
		}
		if (printLandingResults && IsLandingTestRun()) {
			ret += [Landing:true]
		}
		if (printSpecialResults && IsSpecialTestRun()) {
			ret += [Special:true]
		}
		return ret
	}
	
	int GetResultPenalties(Map resultSettings)
	{
		int penalties = 0
		if (resultSettings["Planning"]) {
			if (IsPlanningTestRun()) {
				penalties += planningTestPenalties
			}
		}
		if (resultSettings["Flight"]) {
			if (IsFlightTestRun()) {
				penalties += flightTestPenalties
			}
		}
		if (resultSettings["Observation"]) {
			if (IsObservationTestRun()) {
				penalties += observationTestPenalties
			}
		}
		if (resultSettings["Landing"]) {
			if (IsLandingTestRun()) {
				penalties += landingTestPenalties
			}
		}
		if (resultSettings["Special"]) {
			if (IsSpecialTestRun()) {
				penalties += specialTestPenalties
			}
		}
		return penalties
	}
	
	boolean IsTestResultsProvisional(Map resultSettings)
	{
		if (printProvisionalResults) {
			return true
		}
		boolean provisional = false
		if (resultSettings["Planning"]) {
			if (printPlanningResults && IsPlanningTestRun()) {
				if (!planningTestComplete) {
					provisional = true
				}
			}
		}
		if (resultSettings["Flight"]) {
			if (printFlightResults && IsFlightTestRun()) {
				if (!flightTestComplete) {
					provisional = true
				}
			}
		}
		if (resultSettings["Observation"]) {
			if (printObservationResults && IsObservationTestRun()) {
				if (!observationTestComplete) {
					provisional = true
				}
			}
		}
		if (resultSettings["Landing"]) {
			if (printLandingResults && IsLandingTestRun()) {
				if (!landingTestComplete) {
					provisional = true
				}
			}
		}
		if (resultSettings["Special"]) {
			if (printSpecialResults && IsSpecialTestRun()) {
				if (!specialTestComplete) {
					provisional = true
				}
			}
		}
		return provisional
	}

	boolean IsTestClassResultsProvisional(Map resultSettings, ResultClass resultclassInstance)
	{
		if (crew.resultclass == resultclassInstance) {
			if (IsTestResultsProvisional(resultSettings)) {
				return true
			}
		}
		return false
	}
	
	int GetPlanningTestDirectionCorrectGrad()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestDirectionCorrectGrad 
			}
		}
		return task.contest.planningTestDirectionCorrectGrad
	}
	
	int GetPlanningTestDirectionPointsPerGrad()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestDirectionPointsPerGrad
			}
		}
		return task.contest.planningTestDirectionPointsPerGrad
	}
	
	int GetPlanningTestTimeCorrectSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestTimeCorrectSecond
			}
		}
		return task.contest.planningTestTimeCorrectSecond
	}
	
	int GetPlanningTestTimePointsPerSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestTimePointsPerSecond
			}
		}
		return task.contest.planningTestTimePointsPerSecond
	}
	
	int GetPlanningTestMaxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestMaxPoints
			}
		}
		return task.contest.planningTestMaxPoints
	}
	
	int GetPlanningTestPlanTooLatePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestPlanTooLatePoints
			}
		}
		return task.contest.planningTestPlanTooLatePoints
	}
	
	int GetPlanningTestExitRoomTooLatePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestExitRoomTooLatePoints
			}
		}
		return task.contest.planningTestExitRoomTooLatePoints
	}
	
	int GetFlightTestTakeoffMissedPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffMissedPoints
			}
		}
		return task.contest.flightTestTakeoffMissedPoints
	}
	
	int GetFlightTestTakeoffCorrectSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffCorrectSecond
			}
		}
		return task.contest.flightTestTakeoffCorrectSecond
	}
	
	boolean GetFlightTestTakeoffCheckSeconds()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffCheckSeconds && (crew.resultclass.flightTestTakeoffPointsPerSecond > 0)
			}
			return false
		}
		return task.contest.flightTestTakeoffCheckSeconds && (task.contest.flightTestTakeoffPointsPerSecond > 0)
	}
	
	int GetFlightTestTakeoffPointsPerSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffPointsPerSecond
			}
		}
		return task.contest.flightTestTakeoffPointsPerSecond
	}
	
	int GetFlightTestCptimeCorrectSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCptimeCorrectSecond
			}
		}
		return task.contest.flightTestCptimeCorrectSecond
	}
	
	int GetFlightTestCptimePointsPerSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCptimePointsPerSecond
			}
		}
		return task.contest.flightTestCptimePointsPerSecond
	}
	
	int GetFlightTestCptimeMaxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCptimeMaxPoints
			}
		}
		return task.contest.flightTestCptimeMaxPoints
	}
	
	int GetFlightTestCpNotFoundPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCpNotFoundPoints
			}
		}
		return task.contest.flightTestCpNotFoundPoints
	}
	
	int GetFlightTestProcedureTurnNotFlownPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestProcedureTurnNotFlownPoints
			}
		}
		return task.contest.flightTestProcedureTurnNotFlownPoints
	}
	
	int GetFlightTestMinAltitudeMissedPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestMinAltitudeMissedPoints
			}
		}
		return task.contest.flightTestMinAltitudeMissedPoints
	}
	
	int GetFlightTestBadCourseCorrectSecond()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCourseCorrectSecond
			}
		}
		return task.contest.flightTestBadCourseCorrectSecond
	}
	
	int GetFlightTestBadCoursePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCoursePoints
			}
		}
		return task.contest.flightTestBadCoursePoints
	}
	
	int GetFlightTestBadCourseStartLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCourseStartLandingPoints
			}
		}
		return task.contest.flightTestBadCourseStartLandingPoints
	}
	
	int GetFlightTestLandingToLatePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestLandingToLatePoints
			}
		}
		return task.contest.flightTestLandingToLatePoints
	}
	
	int GetFlightTestGivenToLatePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestGivenToLatePoints
			}
		}
		return task.contest.flightTestGivenToLatePoints
	}
	
	int GetFlightTestSafetyAndRulesInfringementPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestSafetyAndRulesInfringementPoints
			}
		}
		return task.contest.flightTestSafetyAndRulesInfringementPoints
	}
	
	int GetFlightTestInstructionsNotFollowedPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestInstructionsNotFollowedPoints
			}
		}
		return task.contest.flightTestInstructionsNotFollowedPoints
	}
	
	int GetFlightTestFalseEnvelopeOpenedPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestFalseEnvelopeOpenedPoints
			}
		}
		return task.contest.flightTestFalseEnvelopeOpenedPoints
	}
	
	int GetFlightTestSafetyEnvelopeOpenedPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestSafetyEnvelopeOpenedPoints
			}
		}
		return task.contest.flightTestSafetyEnvelopeOpenedPoints
	}
	
	int GetFlightTestFrequencyNotMonitoredPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestFrequencyNotMonitoredPoints
			}
		}
		return task.contest.flightTestFrequencyNotMonitoredPoints
	}
	
	int GetLandingTest1MaxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1MaxPoints
			}
		}
		return task.contest.landingTest1MaxPoints
	}
	
	int GetLandingTest1NoLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1NoLandingPoints
			}
		}
		return task.contest.landingTest1NoLandingPoints
	}
	
	int GetLandingTest1OutsideLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1OutsideLandingPoints
			}
		}
		return task.contest.landingTest1OutsideLandingPoints
	}
	
	int GetLandingTest1RollingOutsidePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1RollingOutsidePoints
			}
		}
		return task.contest.landingTest1RollingOutsidePoints
	}
	
	int GetLandingTest1PowerInBoxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1PowerInBoxPoints
			}
		}
		return task.contest.landingTest1PowerInBoxPoints
	}
	
	int GetLandingTest1GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest1GoAroundWithoutTouchingPoints
	}
	
	int GetLandingTest1GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest1GoAroundInsteadStopPoints
	}
	
	int GetLandingTest1AbnormalLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1AbnormalLandingPoints
			}
		}
		return task.contest.landingTest1AbnormalLandingPoints
	}
	
	String GetLandingTest1PenaltyCalculator()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1PenaltyCalculator
			}
		}
		return task.contest.landingTest1PenaltyCalculator
	}

	int GetLandingTest2MaxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2MaxPoints
			}
		}
		return task.contest.landingTest2MaxPoints
	}
	
	int GetLandingTest2NoLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2NoLandingPoints
			}
		}
		return task.contest.landingTest2NoLandingPoints
	}
	
	int GetLandingTest2OutsideLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2OutsideLandingPoints
			}
		}
		return task.contest.landingTest2OutsideLandingPoints
	}
	
	int GetLandingTest2RollingOutsidePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2RollingOutsidePoints
			}
		}
		return task.contest.landingTest2RollingOutsidePoints
	}
	
	int GetLandingTest2PowerInBoxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2PowerInBoxPoints
			}
		}
		return task.contest.landingTest2PowerInBoxPoints
	}
	
	int GetLandingTest2GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest2GoAroundWithoutTouchingPoints
	}
	
	int GetLandingTest2GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest2GoAroundInsteadStopPoints
	}
	
	int GetLandingTest2AbnormalLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2AbnormalLandingPoints
			}
		}
		return task.contest.landingTest2AbnormalLandingPoints
	}
	
	int GetLandingTest2PowerInAirPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2PowerInAirPoints
			}
		}
		return task.contest.landingTest2PowerInAirPoints
	}
	
	String GetLandingTest2PenaltyCalculator()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2PenaltyCalculator
			}
		}
		return task.contest.landingTest2PenaltyCalculator
	}

	int GetLandingTest3MaxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3MaxPoints
			}
		}
		return task.contest.landingTest3MaxPoints
	}
	
	int GetLandingTest3NoLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3NoLandingPoints
			}
		}
		return task.contest.landingTest3NoLandingPoints
	}
	
	int GetLandingTest3OutsideLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3OutsideLandingPoints
			}
		}
		return task.contest.landingTest3OutsideLandingPoints
	}
	
	int GetLandingTest3RollingOutsidePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3RollingOutsidePoints
			}
		}
		return task.contest.landingTest3RollingOutsidePoints
	}
	
	int GetLandingTest3PowerInBoxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3PowerInBoxPoints
			}
		}
		return task.contest.landingTest3PowerInBoxPoints
	}
	
	int GetLandingTest3GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest3GoAroundWithoutTouchingPoints
	}
	
	int GetLandingTest3GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest3GoAroundInsteadStopPoints
	}
	
	int GetLandingTest3AbnormalLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3AbnormalLandingPoints
			}
		}
		return task.contest.landingTest3AbnormalLandingPoints
	}
	
	int GetLandingTest3PowerInAirPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3PowerInAirPoints
			}
		}
		return task.contest.landingTest3PowerInAirPoints
	}
	
	int GetLandingTest3FlapsInAirPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3FlapsInAirPoints
			}
		}
		return task.contest.landingTest3FlapsInAirPoints
	}
	
	String GetLandingTest3PenaltyCalculator()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3PenaltyCalculator
			}
		}
		return task.contest.landingTest3PenaltyCalculator
	}

	int GetLandingTest4MaxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4MaxPoints
			}
		}
		return task.contest.landingTest4MaxPoints
	}
	
	int GetLandingTest4NoLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4NoLandingPoints
			}
		}
		return task.contest.landingTest4NoLandingPoints
	}
	
	int GetLandingTest4OutsideLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4OutsideLandingPoints
			}
		}
		return task.contest.landingTest4OutsideLandingPoints
	}
	
	int GetLandingTest4RollingOutsidePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4RollingOutsidePoints
			}
		}
		return task.contest.landingTest4RollingOutsidePoints
	}
	
	int GetLandingTest4PowerInBoxPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4PowerInBoxPoints
			}
		}
		return task.contest.landingTest4PowerInBoxPoints
	}
	
	int GetLandingTest4GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest4GoAroundWithoutTouchingPoints
	}
	
	int GetLandingTest4GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest4GoAroundInsteadStopPoints
	}
	
	int GetLandingTest4AbnormalLandingPoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4AbnormalLandingPoints
			}
		}
		return task.contest.landingTest4AbnormalLandingPoints
	}
	
	int GetLandingTest4TouchingObstaclePoints()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4TouchingObstaclePoints
			}
		}
		return task.contest.landingTest4TouchingObstaclePoints
	}
	
	String GetLandingTest4PenaltyCalculator()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4PenaltyCalculator
			}
		}
		return task.contest.landingTest4PenaltyCalculator
	}

	boolean IsSpecialTestTitle()
	{
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					if (taskclass_instance.specialTestTitle) {
						return true
					}
				}
			}
		} else {
			if (task.specialTestTitle) { 
				return true
			}
		}
		return false
	}

	String GetSpecialTestTitle()
	{
		String print_title = getMsg('fc.specialresults')
		if (task.contest.resultClasses) {
			if (crew.resultclass) {
				TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
				if (taskclass_instance) {
					if (taskclass_instance.specialTestTitle) {
						print_title = taskclass_instance.specialTestTitle
					}
				}
			}
		} else {
			if (task.specialTestTitle) { 
				print_title = task.specialTestTitle
			}
		}
		return "${print_title}"
	}

	int GetViewPos()
	{
		return viewpos+1
	}
	
	int GetStartNum()
	{
		return crew.startNum  
	}
}
