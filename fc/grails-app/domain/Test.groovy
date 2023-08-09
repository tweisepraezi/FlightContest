import java.util.List;
import java.util.Map;

class Test 
{
    def grailsApplication

	Crew crew
    Boolean disabledCrew = false                           // DB-2.9
	PlanningTestTask planningtesttask
	FlightTestWind flighttestwind
	
	int viewpos = 0
	BigDecimal taskTAS = 0
	Aircraft taskAircraft                                  // DB-2.3
    String taskTrackerID = ""                              // DB-2.15
    Integer taskLiveTrackingTeamID = 0                     // DB-2.25
    Integer aflosStartNum = 0                              // DB-2.10
    Boolean showAflosMark = false                          // DB-2.12
    Boolean pageBreak = false                              // DB-2.35

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
    Boolean planningTestForbiddenCalculators = false       // DB-2.13
	int     planningTestOtherPenalties = 0                 // DB-2.0
	int     planningTestPenalties = 0
	boolean planningTestComplete = false
    Boolean planningTestLiveTrackingResultOk = false       // DB-2.23
    Boolean planningTestLiveTrackingResultError = false    // DB-2.23
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
    Boolean flightTestForbiddenEquipment = false           // DB-2.13
    int     flightTestOtherPenalties = 0                   // DB-2.0
    int     flightTestPenalties = 0
    boolean flightTestComplete = false
    Boolean flightTestLiveTrackingResultOk = false         // DB-2.23
    Boolean flightTestLiveTrackingResultError = false      // DB-2.23
	boolean flightTestModified = true                      // Änderungsanzeige, DB-2.0
	int     flightTestVersion = 0                          // Änderungsanzeige, DB-2.0
    String  flightTestLink = ""                            // DB-2.10

    int     observationTestTurnPointPhotoPenalties = 0
    int     observationTestRoutePhotoPenalties = 0
    int     observationTestGroundTargetPenalties = 0
    Integer observationTestOtherPenalties = 0              // DB-2.13
    int     observationTestPenalties = 0
	boolean observationTestComplete = false
    Boolean observationTestLiveTrackingResultOk = false    // DB-2.23
    Boolean observationTestLiveTrackingResultError = false // DB-2.23
	boolean observationTestModified = true                 // Änderungsanzeige, DB-2.0
	int     observationTestVersion = 0                     // Änderungsanzeige, DB-2.0
    EnrouteValueUnit observationTestEnroutePhotoValueUnit  // DB-2.13
    EnrouteValueUnit observationTestEnrouteCanvasValueUnit // DB-2.13
    
	String  landingTest1Measure = ""                       // DB-2.0
	int     landingTest1MeasurePenalties = 0               // DB-2.0
	int     landingTest1Penalties = 0                      // DB-2.0
	int     landingTest1Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest1RollingOutside = false             // DB-2.0
	boolean landingTest1PowerInBox = false                 // DB-2.0
	boolean landingTest1GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest1GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest1AbnormalLanding = false            // DB-2.0
	Boolean landingTest1NotAllowedAerodynamicAuxiliaries = false // DB-2.7
    Boolean landingTest1PowerInAir = false                 // DB-2.9
    Boolean landingTest1FlapsInAir = false                 // DB-2.9
    Boolean landingTest1TouchingObstacle = false           // DB-2.9
	Boolean landingTest1Complete = false                   // DB-2.35
    Boolean landingTest1LiveTrackingResultOk = false       // DB-2.35
    Boolean landingTest1LiveTrackingResultError = false    // DB-2.35
	Boolean landingTest1Modified = true                    // DB-2.35
	Integer landingTest1Version = 0                        // DB-2.35

	String  landingTest2Measure = ""                       // DB-2.0
	int     landingTest2MeasurePenalties = 0               // DB-2.0
	int     landingTest2Penalties = 0                      // DB-2.0
	int     landingTest2Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest2RollingOutside = false             // DB-2.0
	boolean landingTest2PowerInBox = false                 // DB-2.0
	boolean landingTest2GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest2GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest2AbnormalLanding = false            // DB-2.0
	Boolean landingTest2NotAllowedAerodynamicAuxiliaries = false // DB-2.7
	boolean landingTest2PowerInAir = false                 // DB-2.0
    Boolean landingTest2FlapsInAir = false                 // DB-2.9
    Boolean landingTest2TouchingObstacle = false           // DB-2.9
	Boolean landingTest2Complete = false                   // DB-2.35
    Boolean landingTest2LiveTrackingResultOk = false       // DB-2.35
    Boolean landingTest2LiveTrackingResultError = false    // DB-2.35
	Boolean landingTest2Modified = true                    // DB-2.35
	Integer landingTest2Version = 0                        // DB-2.35

	String  landingTest3Measure = ""                       // DB-2.0
	int     landingTest3MeasurePenalties = 0               // DB-2.0
	int     landingTest3Penalties = 0                      // DB-2.0
	int     landingTest3Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest3RollingOutside = false             // DB-2.0
	boolean landingTest3PowerInBox = false                 // DB-2.0
	boolean landingTest3GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest3GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest3AbnormalLanding = false            // DB-2.0
	Boolean landingTest3NotAllowedAerodynamicAuxiliaries = false // DB-2.7
	boolean landingTest3PowerInAir = false                 // DB-2.0
	boolean landingTest3FlapsInAir = false                 // DB-2.0
    Boolean landingTest3TouchingObstacle = false           // DB-2.9
	Boolean landingTest3Complete = false                   // DB-2.35
    Boolean landingTest3LiveTrackingResultOk = false       // DB-2.35
    Boolean landingTest3LiveTrackingResultError = false    // DB-2.35
	Boolean landingTest3Modified = true                    // DB-2.35
	Integer landingTest3Version = 0                        // DB-2.35
    
	String  landingTest4Measure = ""                       // DB-2.0
	int     landingTest4MeasurePenalties = 0               // DB-2.0
	int     landingTest4Penalties = 0                      // DB-2.0
	int     landingTest4Landing = 1                        // 1-Landing, 2-NoLanding, 3-OutsideLanding, DB-2.0
	boolean landingTest4RollingOutside = false             // DB-2.0
	boolean landingTest4PowerInBox = false                 // DB-2.0
	boolean landingTest4GoAroundWithoutTouching = false    // DB-2.0
	boolean landingTest4GoAroundInsteadStop = false        // DB-2.0
	boolean landingTest4AbnormalLanding = false            // DB-2.0
	Boolean landingTest4NotAllowedAerodynamicAuxiliaries = false // DB-2.7
    Boolean landingTest4PowerInAir = false                 // DB-2.9
    Boolean landingTest4FlapsInAir = false                 // DB-2.9
	boolean landingTest4TouchingObstacle = false           // DB-2.0
	Boolean landingTest4Complete = false                   // DB-2.35
    Boolean landingTest4LiveTrackingResultOk = false       // DB-2.35
    Boolean landingTest4LiveTrackingResultError = false    // DB-2.35
	Boolean landingTest4Modified = true                    // DB-2.35
	Integer landingTest4Version = 0                        // DB-2.35
	
	int     landingTestOtherPenalties = 0                  // DB-2.0
	int     landingTestPenalties = 0
    boolean landingTestComplete = false
    Boolean landingTestLiveTrackingResultOk = false        // DB-2.23
    Boolean landingTestLiveTrackingResultError = false     // DB-2.23
	boolean landingTestModified = true                     // Änderungsanzeige, DB-2.0
	int     landingTestVersion = 0                         // Änderungsanzeige, DB-2.0

	int     specialTestPenalties = 0 
	boolean specialTestComplete = false
    Boolean specialTestLiveTrackingResultOk = false        // DB-2.23
    Boolean specialTestLiveTrackingResultError = false     // DB-2.23
	boolean specialTestModified = true                     // Änderungsanzeige, DB-2.0
	int     specialTestVersion = 0                         // Änderungsanzeige, DB-2.0

	boolean crewResultsModified = true                     // Änderungsanzeige, DB-2.0
	int     crewResultsVersion = 0                         // Änderungsanzeige, DB-2.0
	
	int taskPenalties = 0
    int taskPosition = 0
    Date taskResultsTime                                   // DB-2.36
    
    LoggerDataTest loggerData = null                       // DB-2.12
    String loggerDataStartUtc = ""                         // DB-2.12
    String loggerDataEndUtc = ""                           // DB-2.12
    LoggerResult loggerResult = null                       // DB-2.12
    
    String reserve = ""                                    // DB-2.12
    
    final static int SCANNEDIMAGEMAXSIZE = 1048576         // 1MB, DB-2.13
    
    byte[] scannedPlanningTest                             // DB-2.13
    byte[] scannedObservationTest                          // DB-2.13

	// transient values 
	static transients = ['printSummaryResults','printPlanningResults','printPlanningResultsScan',
                         'printFlightResults','printFlightMap',
                         'printObservationResults','printObservationResultsScan',
                         'printLandingResults','printSpecialResults','printProvisionalResults','addShowTimeValue']
    boolean printSummaryResults = true
	boolean printPlanningResults = true
    boolean printPlanningResultsScan = true
	boolean printFlightResults = true
    boolean printFlightMap = true
	boolean printObservationResults = true
    boolean printObservationResultsScan = true
	boolean printLandingResults = true
	boolean printSpecialResults = true
	boolean printProvisionalResults = false
    int addShowTimeValue = 0                               // add/subtract time value [min] 
	
	static belongsTo = [task:Task]
	
	static hasMany = [testlegplannings:TestLegPlanning,testlegflights:TestLegFlight,coordresults:CoordResult,turnpointdata:TurnpointData,enroutephotodata:EnroutePhotoData,enroutecanvasdata:EnrouteCanvasData]
	
	static hasOne = [uploadjobtest:UploadJobTest]          // DB-2.21
	
    static constraints = {
		crew(nullable:true)
		planningtesttask(nullable:true)
		flighttestwind(nullable:true)
		taskTAS(scale:10)
		planningTestOtherPenalties()
		flightTestOtherPenalties()
		landingTestOtherPenalties()
		landingTestPenalties()
		
		// DB-2.3 compatibility
		taskAircraft(nullable:true)
		flightTestSafetyAndRulesInfringement(nullable:true)
		flightTestInstructionsNotFollowed(nullable:true)
		flightTestFalseEnvelopeOpened(nullable:true)
		flightTestSafetyEnvelopeOpened(nullable:true)
		flightTestFrequencyNotMonitored(nullable:true)
		
		// DB-2.7 compatibility
		landingTest1NotAllowedAerodynamicAuxiliaries(nullable:true)
		landingTest2NotAllowedAerodynamicAuxiliaries(nullable:true)
		landingTest3NotAllowedAerodynamicAuxiliaries(nullable:true)
		landingTest4NotAllowedAerodynamicAuxiliaries(nullable:true)
        
        // DB-2.9 compatibility
        disabledCrew(nullable:true)
        landingTest1PowerInAir(nullable:true)
        landingTest1FlapsInAir(nullable:true)
        landingTest1TouchingObstacle(nullable:true)
        landingTest2FlapsInAir(nullable:true)
        landingTest2TouchingObstacle(nullable:true)
        landingTest3TouchingObstacle(nullable:true)
        landingTest4PowerInAir(nullable:true)
        landingTest4FlapsInAir(nullable:true)
        
        // DB-2.10 compatibility
        aflosStartNum(nullable:true)
        flightTestLink(nullable:true)
        
        // DB-2.12 compatibility
        loggerData(nullable:true)
        loggerDataStartUtc(nullable:true)
        loggerDataEndUtc(nullable:true)
        loggerResult(nullable:true)
        showAflosMark(nullable:true)
        reserve(nullable:true)
        
        // DB-2.13 compatibility
        planningTestForbiddenCalculators(nullable:true)
        flightTestForbiddenEquipment(nullable:true)
        observationTestEnroutePhotoValueUnit(nullable:true)
        observationTestEnrouteCanvasValueUnit(nullable:true)
        observationTestOtherPenalties(nullable:true)
        scannedPlanningTest(nullable:true,maxSize:SCANNEDIMAGEMAXSIZE)
        scannedObservationTest(nullable:true,maxSize:SCANNEDIMAGEMAXSIZE)
        
        // DB-2.15 compatibility
        taskTrackerID(nullable:true)
		
		// DB-2.21 compatibility
		uploadjobtest(nullable:true)
        
        // DB-2.23 compatibility
        planningTestLiveTrackingResultOk(nullable:true)
        planningTestLiveTrackingResultError(nullable:true)
        flightTestLiveTrackingResultOk(nullable:true)
        flightTestLiveTrackingResultError(nullable:true)
        observationTestLiveTrackingResultOk(nullable:true)
        observationTestLiveTrackingResultError(nullable:true)
        landingTestLiveTrackingResultOk(nullable:true)
        landingTestLiveTrackingResultError(nullable:true)
        specialTestLiveTrackingResultOk(nullable:true)
        specialTestLiveTrackingResultError(nullable:true)
        
        // DB-2.25 compatibility
        taskLiveTrackingTeamID(nullable:true)

		// DB-2.35 compatibility
		landingTest1Complete(nullable:true)
		landingTest2Complete(nullable:true)
		landingTest3Complete(nullable:true)
		landingTest4Complete(nullable:true)
		landingTest1LiveTrackingResultOk(nullable:true)
		landingTest2LiveTrackingResultOk(nullable:true)
		landingTest3LiveTrackingResultOk(nullable:true)
		landingTest4LiveTrackingResultOk(nullable:true)
		landingTest1LiveTrackingResultError(nullable:true)
		landingTest2LiveTrackingResultError(nullable:true)
		landingTest3LiveTrackingResultError(nullable:true)
		landingTest4LiveTrackingResultError(nullable:true)
		landingTest1Modified(nullable:true)
		landingTest2Modified(nullable:true)
		landingTest3Modified(nullable:true)
		landingTest4Modified(nullable:true)
		landingTest1Version(nullable:true)
		landingTest2Version(nullable:true)
		landingTest3Version(nullable:true)
		landingTest4Version(nullable:true)
		pageBreak(nullable:true)
        
        // DB-2.36 compatibility
        taskResultsTime(nullable:true)
    }

	static mapping = {
		testlegplannings sort:"id"
		testlegflights sort:"id"
		coordresults sort:"id"
        enroutedata sort:"id"
        turnpointdata sort:"id"
	}
	
	void ResetPlanningTestResults()
	{
		planningTestLegPenalties = 0
		planningTestLegComplete = false
		planningTestGivenTooLate = false
		planningTestExitRoomTooLate = false
        planningTestForbiddenCalculators = false
		planningTestPenalties = 0
		planningTestComplete = false
        planningTestLiveTrackingResultOk = false
        planningTestLiveTrackingResultError = false
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
        flightTestForbiddenEquipment = false
		flightTestPenalties = 0
		flightTestComplete = false
        flightTestLiveTrackingResultOk = false
        flightTestLiveTrackingResultError = false
	}
	
	void ResetObservationTestResults()
	{
        observationTestTurnPointPhotoPenalties = 0
		observationTestRoutePhotoPenalties = 0
		observationTestGroundTargetPenalties = 0
		observationTestPenalties = 0
		observationTestComplete = false
        observationTestLiveTrackingResultOk = false
        observationTestLiveTrackingResultError = false
        observationTestEnroutePhotoValueUnit = null
        observationTestEnrouteCanvasValueUnit = null
	}
	
	void ResetLandingTestResults()
	{
		landingTest1Penalties = 0
		landingTest2Penalties = 0
		landingTest3Penalties = 0
		landingTest4Penalties = 0
		landingTestPenalties = 0
		landingTestComplete = false
		landingTest1Complete = false
		landingTest2Complete = false
		landingTest3Complete = false
		landingTest4Complete = false
        landingTestLiveTrackingResultOk = false
        landingTest1LiveTrackingResultOk = false
        landingTest2LiveTrackingResultOk = false
        landingTest3LiveTrackingResultOk = false
        landingTest4LiveTrackingResultOk = false
        landingTestLiveTrackingResultError = false
        landingTest1LiveTrackingResultError = false
        landingTest2LiveTrackingResultError = false
        landingTest3LiveTrackingResultError = false
        landingTest4LiveTrackingResultError = false
	}
	
	void ResetSpecialTestResults()
	{
		specialTestPenalties = 0
		specialTestComplete = false
        specialTestLiveTrackingResultOk = false
        specialTestLiveTrackingResultError = false
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

	int GetLandingTest1Version()
	{	
		if (landingTest1Modified) {
			return landingTest1Version + 1
		}
		return landingTest1Version
	}

	int GetLandingTest2Version()
	{	
		if (landingTest2Modified) {
			return landingTest2Version + 1
		}
		return landingTest2Version
	}

	int GetLandingTest3Version()
	{	
		if (landingTest3Modified) {
			return landingTest3Version + 1
		}
		return landingTest3Version
	}

	int GetLandingTest4Version()
	{	
		if (landingTest4Modified) {
			return landingTest4Version + 1
		}
		return landingTest4Version
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
        	taskPenalties += FcMath.GetLandingPenalties(task.contest.contestLandingResultsFactor, landingTestPenalties)
		}
		if (IsSpecialTestRun()) {
			taskPenalties += specialTestPenalties
		}
        if (IsIncreaseEnabled()) {
            taskPenalties += GetIncreasePenalties(taskPenalties)
        }
		crew.planningPenalties = 0
		crew.flightPenalties = 0
		crew.observationPenalties = 0
		crew.landingPenalties = 0
		crew.specialPenalties = 0
		crew.contestPenalties = 0
		crew.contestPosition = 0
		crew.noContestPosition = false
        crew.contestEqualPosition = false
        crew.contestAddPosition = 0
		crew.classPosition = 0
		crew.noClassPosition = false
        crew.classEqualPosition = false
        crew.classAddPosition = 0
		crew.teamPenalties = 0
		if (crew.team) {
			crew.team.contestPenalties = 0
			crew.team.contestPosition = 0
            crew.team.contestEqualPosition = false
            crew.team.contestAddPosition = 0
		}
		// crewResultsModified = true
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
	
    boolean IsObservationTestTurnpointRun()
    {
        if (task.contest.resultClasses) {
            if (crew.resultclass) {
                TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
                if (taskclass_instance) {
                    return taskclass_instance.observationTestTurnpointRun
                }
            }
            return false
        }
        return task.observationTestTurnpointRun
    }
    
    boolean IsObservationTestEnroutePhotoRun()
    {
        if (task.contest.resultClasses) {
            if (crew.resultclass) {
                TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
                if (taskclass_instance) {
                    return taskclass_instance.observationTestEnroutePhotoRun
                }
            }
            return false
        }
        return task.observationTestEnroutePhotoRun
    }
    
    boolean IsObservationTestEnrouteCanvasRun()
    {
        if (task.contest.resultClasses) {
            if (crew.resultclass) {
                TaskClass taskclass_instance = TaskClass.findByTaskAndResultclass(task,crew.resultclass)
                if (taskclass_instance) {
                    return taskclass_instance.observationTestEnrouteCanvasRun
                }
            }
            return false
        }
        return task.observationTestEnrouteCanvasRun
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

    String GetSpecialTestTitle(boolean isPrint)
    {
        String print_title = GetMsg('fc.specialresults',isPrint)
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
				penalties += FcMath.GetLandingPenalties(task.contest.contestLandingResultsFactor, landingTestPenalties)
			}
		}
		if (resultSettings["Special"]) {
			if (IsSpecialTestRun()) {
				penalties += specialTestPenalties
			}
		}
        if (IsIncreaseEnabled()) {
            penalties += GetIncreasePenalties(penalties)
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
				if (IsLandingTestAnyRun()) {
					if (IsLandingTest1Run()) {
						if (!landingTest1Complete) {
							provisional = true
						}
					}
					if (IsLandingTest2Run()) {
						if (!landingTest2Complete) {
							provisional = true
						}
					}
					if (IsLandingTest3Run()) {
						if (!landingTest3Complete) {
							provisional = true
						}
					}
					if (IsLandingTest4Run()) {
						if (!landingTest4Complete) {
							provisional = true
						}
					}
				} else {
					if (!landingTestComplete) {
						provisional = true
					}
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

    int GetDetailNum()
    {
        int detail_num = 0
        if (printPlanningResults) {
            detail_num++
        }
        // printPlanningResultsScan not relevant
        if (printFlightResults) {
            detail_num++
        }
        // printFlightMap not relevant
        if (printObservationResults) {
            detail_num++
        }
        // printObservationResultsScan not relevant
        if (printLandingResults) {
            detail_num++
        }
        if (printSpecialResults) {
            detail_num++
        }
        return detail_num
    }
    
	boolean IsTestClassResultsProvisional(Map resultSettings, ResultClass resultclassInstance)
	{
		if (crew.resultclass.id == resultclassInstance.id) {
			if (IsTestResultsProvisional(resultSettings)) {
				return true
			}
		}
		return false
	}
	
    boolean IsTestProvisional(Map resultSettings)
    {
        boolean provisional = false
        if (resultSettings["Planning"]) {
            if (IsPlanningTestRun()) {
                if (!planningTestComplete) {
                    provisional = true
                }
            }
        }
        if (resultSettings["Flight"]) {
            if (IsFlightTestRun()) {
                if (!flightTestComplete) {
                    provisional = true
                }
            }
        }
        if (resultSettings["Observation"]) {
            if (IsObservationTestRun()) {
                if (!observationTestComplete) {
                    provisional = true
                }
            }
        }
        if (resultSettings["Landing"]) {
            if (IsLandingTestRun()) {
				if (IsLandingTestAnyRun()) {
					if (IsLandingTest1Run()) {
						if (!landingTest1Complete) {
							provisional = true
						}
					}
					if (IsLandingTest2Run()) {
						if (!landingTest2Complete) {
							provisional = true
						}
					}
					if (IsLandingTest3Run()) {
						if (!landingTest3Complete) {
							provisional = true
						}
					}
					if (IsLandingTest4Run()) {
						if (!landingTest4Complete) {
							provisional = true
						}
					}
				} else {
					if (!landingTestComplete) {
						provisional = true
					}
				}
            }
        }
        if (resultSettings["Special"]) {
            if (IsSpecialTestRun()) {
                if (!specialTestComplete) {
                    provisional = true
                }
            }
        }
        return provisional
    }

    Map GetResultClassName() {
        Map ret = [name:"", shortName:""]
        if (task.contest.resultClasses) {
            if (crew.resultclass) {
                ret.name = crew.resultclass.name
                ret.shortName = crew.resultclass.shortName
            }
        }
        return ret
    }
    
    boolean IsPrecisionFlying()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.precisionFlying
            }
            return false
        }
        return task.contest.precisionFlying
    }
    
    Integer GetIncreaseFactor()
    {
        return crew.GetIncreaseFactor()
    }
    
    String GetPrintLandingCalculatorValues()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.printLandingCalculatorValues
            }
            return ""
        }
        return task.contest.printLandingCalculatorValues
    }

	int GetPlanningTestDirectionCorrectGrad()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestDirectionCorrectGrad 
			}
		}
		return task.contest.planningTestDirectionCorrectGrad
	}
	
	int GetPlanningTestDirectionPointsPerGrad()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestDirectionPointsPerGrad
			}
		}
		return task.contest.planningTestDirectionPointsPerGrad
	}
	
	int GetPlanningTestTimeCorrectSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestTimeCorrectSecond
			}
		}
		return task.contest.planningTestTimeCorrectSecond
	}
	
	int GetPlanningTestTimePointsPerSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestTimePointsPerSecond
			}
		}
		return task.contest.planningTestTimePointsPerSecond
	}
	
	int GetPlanningTestMaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestMaxPoints
			}
		}
		return task.contest.planningTestMaxPoints
	}
	
	int GetPlanningTestPlanTooLatePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestPlanTooLatePoints
			}
		}
		return task.contest.planningTestPlanTooLatePoints
	}
	
	int GetPlanningTestExitRoomTooLatePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.planningTestExitRoomTooLatePoints
			}
		}
		return task.contest.planningTestExitRoomTooLatePoints
	}
	
    int GetPlanningTestForbiddenCalculatorsPoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.planningTestForbiddenCalculatorsPoints
            }
        }
        return task.contest.planningTestForbiddenCalculatorsPoints
    }
    
	int GetFlightTestTakeoffMissedPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffMissedPoints
			}
		}
		return task.contest.flightTestTakeoffMissedPoints
	}
	
	int GetFlightTestTakeoffCorrectSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffCorrectSecond
			}
		}
		return task.contest.flightTestTakeoffCorrectSecond
	}
	
	boolean GetFlightTestTakeoffCheckSeconds()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffCheckSeconds && (crew.resultclass.flightTestTakeoffPointsPerSecond > 0)
			}
			return false
		}
		return task.contest.flightTestTakeoffCheckSeconds && (task.contest.flightTestTakeoffPointsPerSecond > 0)
	}
	
	int GetFlightTestTakeoffPointsPerSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestTakeoffPointsPerSecond
			}
		}
		return task.contest.flightTestTakeoffPointsPerSecond
	}
	
	int GetFlightTestCptimeCorrectSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCptimeCorrectSecond
			}
		}
		return task.contest.flightTestCptimeCorrectSecond
	}
	
	int GetFlightTestCptimePointsPerSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCptimePointsPerSecond
			}
		}
		return task.contest.flightTestCptimePointsPerSecond
	}
	
	int GetFlightTestCptimeMaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCptimeMaxPoints
			}
		}
		return task.contest.flightTestCptimeMaxPoints
	}
	
	int GetFlightTestCpNotFoundPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestCpNotFoundPoints
			}
		}
		return task.contest.flightTestCpNotFoundPoints
	}
	
	int GetFlightTestProcedureTurnNotFlownPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestProcedureTurnNotFlownPoints
			}
		}
		return task.contest.flightTestProcedureTurnNotFlownPoints
	}
	
	int GetFlightTestMinAltitudeMissedPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestMinAltitudeMissedPoints
			}
		}
		return task.contest.flightTestMinAltitudeMissedPoints
	}
	
	int GetFlightTestBadCourseCorrectSecond()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCourseCorrectSecond
			}
		}
		return task.contest.flightTestBadCourseCorrectSecond
	}
	
	int GetFlightTestBadCoursePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCoursePoints
			}
		}
		return task.contest.flightTestBadCoursePoints
	}
	
	int GetFlightTestBadCourseMaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCourseMaxPoints
			}
		}
		return task.contest.flightTestBadCourseMaxPoints
	}
	
	int GetFlightTestBadCourseStartLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestBadCourseStartLandingPoints
			}
		}
		return task.contest.flightTestBadCourseStartLandingPoints
	}
	
	int GetFlightTestLandingToLatePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestLandingToLatePoints
			}
		}
		return task.contest.flightTestLandingToLatePoints
	}
	
	int GetFlightTestGivenToLatePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestGivenToLatePoints
			}
		}
		return task.contest.flightTestGivenToLatePoints
	}
	
	int GetFlightTestSafetyAndRulesInfringementPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestSafetyAndRulesInfringementPoints
			}
		}
		return task.contest.flightTestSafetyAndRulesInfringementPoints
	}
	
	int GetFlightTestInstructionsNotFollowedPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestInstructionsNotFollowedPoints
			}
		}
		return task.contest.flightTestInstructionsNotFollowedPoints
	}
	
	int GetFlightTestFalseEnvelopeOpenedPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestFalseEnvelopeOpenedPoints
			}
		}
		return task.contest.flightTestFalseEnvelopeOpenedPoints
	}
	
	int GetFlightTestSafetyEnvelopeOpenedPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestSafetyEnvelopeOpenedPoints
			}
		}
		return task.contest.flightTestSafetyEnvelopeOpenedPoints
	}
	
	int GetFlightTestFrequencyNotMonitoredPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.flightTestFrequencyNotMonitoredPoints
			}
		}
		return task.contest.flightTestFrequencyNotMonitoredPoints
	}
	
    int GetFlightTestForbiddenEquipmentPoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.flightTestForbiddenEquipmentPoints
            }
        }
        return task.contest.flightTestForbiddenEquipmentPoints
    }
    
    EnrouteValueUnit GetObservationTestEnrouteValueUnit()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestEnrouteValueUnit
            }
        }
        return task.contest.observationTestEnrouteValueUnit
    }
    
    Float GetObservationTestEnrouteCorrectValue()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestEnrouteCorrectValue
            }
        }
        return task.contest.observationTestEnrouteCorrectValue
    }
    
    Float GetObservationTestEnrouteInexactValue()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestEnrouteInexactValue
            }
        }
        return task.contest.observationTestEnrouteInexactValue
    }
    
    Float GetObservationTestEnrouteInexactPoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestEnrouteInexactPoints
            }
        }
        return task.contest.observationTestEnrouteInexactPoints
    }
    
    Integer GetObservationTestEnrouteNotFoundPoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestEnrouteNotFoundPoints
            }
        }
        return task.contest.observationTestEnrouteNotFoundPoints
    }
    
    Integer GetObservationTestEnrouteFalsePoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestEnrouteFalsePoints
            }
        }
        return task.contest.observationTestEnrouteFalsePoints
    }
    
    Integer GetObservationTestTurnpointNotFoundPoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestTurnpointNotFoundPoints
            }
        }
        return task.contest.observationTestTurnpointNotFoundPoints
    }
    
    Integer GetObservationTestTurnpointFalsePoints()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.observationTestTurnpointFalsePoints
            }
        }
        return task.contest.observationTestTurnpointFalsePoints
    }
    
    String GetPrecisionFlyingLandingText(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return "fc.landingtest.landing1.precision"
            case 2: return "fc.landingtest.landing2.precision"
            case 3: return "fc.landingtest.landing3.precision"
            case 4: return "fc.landingtest.landing4.precision"
        }
        return ""
    }
    
    int GetLandingTestMaxPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1MaxPoints()
            case 2: return GetLandingTest2MaxPoints()
            case 3: return GetLandingTest3MaxPoints()
            case 4: return GetLandingTest4MaxPoints()
        }
        return 0
    }

    int GetLandingTestNoLandingPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1NoLandingPoints()
            case 2: return GetLandingTest2NoLandingPoints()
            case 3: return GetLandingTest3NoLandingPoints()
            case 4: return GetLandingTest4NoLandingPoints()
        }
        return 0
    }
    
    int GetLandingTestOutsideLandingPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1OutsideLandingPoints()
            case 2: return GetLandingTest2OutsideLandingPoints()
            case 3: return GetLandingTest3OutsideLandingPoints()
            case 4: return GetLandingTest4OutsideLandingPoints()
        }
        return 0
    }
    
    int GetLandingTestRollingOutsidePoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1RollingOutsidePoints()
            case 2: return GetLandingTest2RollingOutsidePoints()
            case 3: return GetLandingTest3RollingOutsidePoints()
            case 4: return GetLandingTest4RollingOutsidePoints()
        }
        return 0
    }
    
    int GetLandingTestPowerInBoxPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1PowerInBoxPoints()
            case 2: return GetLandingTest2PowerInBoxPoints()
            case 3: return GetLandingTest3PowerInBoxPoints()
            case 4: return GetLandingTest4PowerInBoxPoints()
        }
        return 0
    }
    
    int GetLandingTestGoAroundWithoutTouchingPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1GoAroundWithoutTouchingPoints()
            case 2: return GetLandingTest2GoAroundWithoutTouchingPoints()
            case 3: return GetLandingTest3GoAroundWithoutTouchingPoints()
            case 4: return GetLandingTest4GoAroundWithoutTouchingPoints()
        }
        return 0
    }
    
    int GetLandingTestGoAroundInsteadStopPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1GoAroundInsteadStopPoints()
            case 2: return GetLandingTest2GoAroundInsteadStopPoints()
            case 3: return GetLandingTest3GoAroundInsteadStopPoints()
            case 4: return GetLandingTest4GoAroundInsteadStopPoints()
        }
        return 0
    }
    
    int GetLandingTestAbnormalLandingPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1AbnormalLandingPoints()
            case 2: return GetLandingTest2AbnormalLandingPoints()
            case 3: return GetLandingTest3AbnormalLandingPoints()
            case 4: return GetLandingTest4AbnormalLandingPoints()
        }
        return 0
    }
    
    int GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1NotAllowedAerodynamicAuxiliariesPoints()
            case 2: return GetLandingTest2NotAllowedAerodynamicAuxiliariesPoints()
            case 3: return GetLandingTest3NotAllowedAerodynamicAuxiliariesPoints()
            case 4: return GetLandingTest4NotAllowedAerodynamicAuxiliariesPoints()
        }
        return 0
    }
    
    int GetLandingTestPowerInAirPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1PowerInAirPoints()
            case 2: return GetLandingTest2PowerInAirPoints()
            case 3: return GetLandingTest3PowerInAirPoints()
            case 4: return GetLandingTest4PowerInAirPoints()
        }
        return 0
    }
    
    int GetLandingTestFlapsInAirPoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1FlapsInAirPoints()
            case 2: return GetLandingTest2FlapsInAirPoints()
            case 3: return GetLandingTest3FlapsInAirPoints()
            case 4: return GetLandingTest4FlapsInAirPoints()
        }
        return 0
    }

    int GetLandingTestTouchingObstaclePoints(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1TouchingObstaclePoints()
            case 2: return GetLandingTest2TouchingObstaclePoints()
            case 3: return GetLandingTest3TouchingObstaclePoints()
            case 4: return GetLandingTest4TouchingObstaclePoints()
        }
        return 0
    }
    
    String GetLandingTestPenaltyCalculator(int landingTestPoints)
    {
        switch (landingTestPoints) {
            case 1: return GetLandingTest1PenaltyCalculator()
            case 2: return GetLandingTest2PenaltyCalculator()
            case 3: return GetLandingTest3PenaltyCalculator()
            case 4: return GetLandingTest4PenaltyCalculator()
        }
        return ""
    }
    
	private int GetLandingTest1MaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1MaxPoints
			}
		}
		return task.contest.landingTest1MaxPoints
	}
	
	private int GetLandingTest1NoLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1NoLandingPoints
			}
		}
		return task.contest.landingTest1NoLandingPoints
	}
	
	private int GetLandingTest1OutsideLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1OutsideLandingPoints
			}
		}
		return task.contest.landingTest1OutsideLandingPoints
	}
	
	private int GetLandingTest1RollingOutsidePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1RollingOutsidePoints
			}
		}
		return task.contest.landingTest1RollingOutsidePoints
	}
	
	private int GetLandingTest1PowerInBoxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1PowerInBoxPoints
			}
		}
		return task.contest.landingTest1PowerInBoxPoints
	}
	
	private int GetLandingTest1GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest1GoAroundWithoutTouchingPoints
	}
	
	private int GetLandingTest1GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest1GoAroundInsteadStopPoints
	}
	
	private int GetLandingTest1AbnormalLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1AbnormalLandingPoints
			}
		}
		return task.contest.landingTest1AbnormalLandingPoints
	}
	
	private int GetLandingTest1NotAllowedAerodynamicAuxiliariesPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1NotAllowedAerodynamicAuxiliariesPoints
			}
		}
		return task.contest.landingTest1NotAllowedAerodynamicAuxiliariesPoints
	}
	
    private int GetLandingTest1PowerInAirPoints()
    {
        return 0
    }
    
    private int GetLandingTest1FlapsInAirPoints()
    {
        return 0
    }

    private int GetLandingTest1TouchingObstaclePoints()
    {
        return 0
    }
    
	private String GetLandingTest1PenaltyCalculator()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest1PenaltyCalculator
			}
		}
		return task.contest.landingTest1PenaltyCalculator
	}

	private int GetLandingTest2MaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2MaxPoints
			}
		}
		return task.contest.landingTest2MaxPoints
	}
	
	private int GetLandingTest2NoLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2NoLandingPoints
			}
		}
		return task.contest.landingTest2NoLandingPoints
	}
	
	private int GetLandingTest2OutsideLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2OutsideLandingPoints
			}
		}
		return task.contest.landingTest2OutsideLandingPoints
	}
	
	private int GetLandingTest2RollingOutsidePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2RollingOutsidePoints
			}
		}
		return task.contest.landingTest2RollingOutsidePoints
	}
	
	private int GetLandingTest2PowerInBoxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2PowerInBoxPoints
			}
		}
		return task.contest.landingTest2PowerInBoxPoints
	}
	
	private int GetLandingTest2GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest2GoAroundWithoutTouchingPoints
	}
	
	private int GetLandingTest2GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest2GoAroundInsteadStopPoints
	}
	
	private int GetLandingTest2AbnormalLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2AbnormalLandingPoints
			}
		}
		return task.contest.landingTest2AbnormalLandingPoints
	}
	
	private int GetLandingTest2NotAllowedAerodynamicAuxiliariesPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2NotAllowedAerodynamicAuxiliariesPoints
			}
		}
		return task.contest.landingTest2NotAllowedAerodynamicAuxiliariesPoints
	}
	
	private int GetLandingTest2PowerInAirPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2PowerInAirPoints
			}
		}
		return task.contest.landingTest2PowerInAirPoints
	}
	
    private int GetLandingTest2FlapsInAirPoints()
    {
        return 0
    }

    private int GetLandingTest2TouchingObstaclePoints()
    {
        return 0
    }
    
	private String GetLandingTest2PenaltyCalculator()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest2PenaltyCalculator
			}
		}
		return task.contest.landingTest2PenaltyCalculator
	}

	private int GetLandingTest3MaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3MaxPoints
			}
		}
		return task.contest.landingTest3MaxPoints
	}
	
	private int GetLandingTest3NoLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3NoLandingPoints
			}
		}
		return task.contest.landingTest3NoLandingPoints
	}
	
	private int GetLandingTest3OutsideLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3OutsideLandingPoints
			}
		}
		return task.contest.landingTest3OutsideLandingPoints
	}
	
	private int GetLandingTest3RollingOutsidePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3RollingOutsidePoints
			}
		}
		return task.contest.landingTest3RollingOutsidePoints
	}
	
	private int GetLandingTest3PowerInBoxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3PowerInBoxPoints
			}
		}
		return task.contest.landingTest3PowerInBoxPoints
	}
	
	private int GetLandingTest3GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest3GoAroundWithoutTouchingPoints
	}
	
	private int GetLandingTest3GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest3GoAroundInsteadStopPoints
	}
	
	private int GetLandingTest3AbnormalLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3AbnormalLandingPoints
			}
		}
		return task.contest.landingTest3AbnormalLandingPoints
	}
	
	private int GetLandingTest3NotAllowedAerodynamicAuxiliariesPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3NotAllowedAerodynamicAuxiliariesPoints
			}
		}
		return task.contest.landingTest3NotAllowedAerodynamicAuxiliariesPoints
	}
	
	private int GetLandingTest3PowerInAirPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3PowerInAirPoints
			}
		}
		return task.contest.landingTest3PowerInAirPoints
	}
	
	private int GetLandingTest3FlapsInAirPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3FlapsInAirPoints
			}
		}
		return task.contest.landingTest3FlapsInAirPoints
	}
	
    private int GetLandingTest3TouchingObstaclePoints()
    {
        return 0
    }
    
	private String GetLandingTest3PenaltyCalculator()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest3PenaltyCalculator
			}
		}
		return task.contest.landingTest3PenaltyCalculator
	}

	private int GetLandingTest4MaxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4MaxPoints
			}
		}
		return task.contest.landingTest4MaxPoints
	}
	
	private int GetLandingTest4NoLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4NoLandingPoints
			}
		}
		return task.contest.landingTest4NoLandingPoints
	}
	
	private int GetLandingTest4OutsideLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4OutsideLandingPoints
			}
		}
		return task.contest.landingTest4OutsideLandingPoints
	}
	
	private int GetLandingTest4RollingOutsidePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4RollingOutsidePoints
			}
		}
		return task.contest.landingTest4RollingOutsidePoints
	}
	
	private int GetLandingTest4PowerInBoxPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4PowerInBoxPoints
			}
		}
		return task.contest.landingTest4PowerInBoxPoints
	}
	
	private int GetLandingTest4GoAroundWithoutTouchingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4GoAroundWithoutTouchingPoints
			}
		}
		return task.contest.landingTest4GoAroundWithoutTouchingPoints
	}
	
	private int GetLandingTest4GoAroundInsteadStopPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4GoAroundInsteadStopPoints
			}
		}
		return task.contest.landingTest4GoAroundInsteadStopPoints
	}
	
	private int GetLandingTest4AbnormalLandingPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4AbnormalLandingPoints
			}
		}
		return task.contest.landingTest4AbnormalLandingPoints
	}
	
	private int GetLandingTest4NotAllowedAerodynamicAuxiliariesPoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4NotAllowedAerodynamicAuxiliariesPoints
			}
		}
		return task.contest.landingTest4NotAllowedAerodynamicAuxiliariesPoints
	}
	
    private int GetLandingTest4PowerInAirPoints()
    {
        return 0
    }
    
    private int GetLandingTest4FlapsInAirPoints()
    {
        return 0
    }
    
	private int GetLandingTest4TouchingObstaclePoints()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4TouchingObstaclePoints
			}
		}
		return task.contest.landingTest4TouchingObstaclePoints
	}
	
	private String GetLandingTest4PenaltyCalculator()
	{
		if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
			if (crew.resultclass) {
				return crew.resultclass.landingTest4PenaltyCalculator
			}
		}
		return task.contest.landingTest4PenaltyCalculator
	}

	int GetViewPos()
	{
		return viewpos+1
	}
	
	int GetStartNum()
	{
		return crew.startNum  
	}
	
    String name()
    {
        return "${crew.startNum} - ${taskAircraft.registration} - ${crew.name}"
    }
    
    String GetTitle(ResultType resultType)
    {
        return GetTitle(resultType, false) // false - no print
    }
    
	String GetTitle(ResultType resultType, boolean isPrint)
	{
		int result_version = 0
        boolean provisional = true
		switch (resultType) {
			case ResultType.Planningtask:
				result_version = GetPlanningTestVersion()
                provisional = !planningTestComplete
				break
			case ResultType.Flight:
				result_version = GetFlightTestVersion()
                provisional = !flightTestComplete
				break
			case ResultType.Observation:
				result_version = GetObservationTestVersion()
                provisional = !observationTestComplete
				break
			case ResultType.Landing:
				result_version = GetLandingTestVersion()
                provisional = !landingTestComplete
				break
			case ResultType.Landing1:
				result_version = GetLandingTest1Version()
                provisional = !landingTest1Complete
				break
			case ResultType.Landing2:
				result_version = GetLandingTest2Version()
                provisional = !landingTest2Complete
				break
			case ResultType.Landing3:
				result_version = GetLandingTest3Version()
                provisional = !landingTest3Complete
				break
			case ResultType.Landing4:
				result_version = GetLandingTest4Version()
                provisional = !landingTest4Complete
				break
			case ResultType.Special:	
				result_version = GetSpecialTestVersion()
                provisional = !specialTestComplete
				break
			case ResultType.Crew:
				result_version = GetCrewResultsVersion()
                provisional = IsTestResultsProvisional(GetResultSettings())
				break
		}
		String result_type = ""
		switch (resultType) {
			case ResultType.Planningtask:
				result_type = "${GetMsg('fc.planningresults',isPrint)}"
				break
			case ResultType.Flight:
				result_type = "${GetMsg('fc.flightresults',isPrint)}"
				break
			case ResultType.Observation:
				result_type = "${GetMsg('fc.observationresults',isPrint)}"
				break
			case ResultType.Landing:
				result_type = "${GetMsg('fc.landingresults',isPrint)}"
				break
			case ResultType.Special:	
				result_type = "${GetSpecialTestTitle(isPrint)}"
				break
			case ResultType.Crew:
				result_type = "${GetMsg('fc.crewresults',isPrint)}"
				break
		}
        String ret = "${GetStartNum()} - "
		if (taskAircraft) {
            ret += "${taskAircraft.registration} - "
		}
        if (isPrint) {
            ret += "${task.printName()}"
        } else {
            ret += "${task.name()}"
        }
        ret += " - ${result_type} (${GetMsg('fc.version',isPrint)} ${result_version})"
        if (provisional) {
            ret += " [${GetMsg('fc.provisional',isPrint)}]"
        }
        return ret
	}
	
    private String GetMsg(String msgID, boolean isPrint)
    {
        if (isPrint) {
            return getPrintMsg(msgID)
        }
        return getMsg(msgID)
    }
    
    String GetEMailTitle(ResultType resultType)
    {
        return "${task.contest.title}: ${GetTitle(resultType,true)}" // true - print 
    }
    
    String GetFileName(ResultType resultType)
    {
        int result_version = 0
        String result_type = ""
        switch (resultType) {
            case ResultType.Planningtask:
                result_version = GetPlanningTestVersion()
                result_type = "planningtaskresults"
                break
            case ResultType.Flight:
                result_version = GetFlightTestVersion()
                result_type = "navigationresults"
                break
            case ResultType.Observation:
                result_version = GetObservationTestVersion()
                result_type = "observationresults"
                break
            case ResultType.Landing:
                result_version = GetLandingTestVersion()
                result_type = "landingresults"
                break
            case ResultType.Landing1:
                result_version = GetLandingTest1Version()
                result_type = "landingresults1"
                break
            case ResultType.Landing2:
                result_version = GetLandingTest2Version()
                result_type = "landingresults2"
                break
            case ResultType.Landing3:
                result_version = GetLandingTest3Version()
                result_type = "landingresults3"
                break
            case ResultType.Landing4:
                result_version = GetLandingTest4Version()
                result_type = "landingresults4"
                break
            case ResultType.Special:    
                result_version = GetSpecialTestVersion()
                result_type = "specialresults"
                break
            case ResultType.Crew:
                result_version = GetCrewResultsVersion()
                result_type = "crewresults"
                break
        }
        return "${result_type}-task${task.idTitle}-${result_version}"
    }
    
	long GetNextTestID(ResultType resultType, def session)
	{
		boolean start_found = false
		int page_pos = 1
		boolean show_test = false
		for (Test test_instance2 in Test.findAllByTask(this.task,[sort:'viewpos'])) {
			if (session.showPage) {
				if (test_instance2.pageBreak) {
					page_pos++
				}
				if (page_pos == session.showPagePos) {
					show_test = true
				} else {
					show_test = false
				}
			} else {
				show_test = true
			}
			if (show_test) {
				if (start_found) {
					if (!test_instance2.disabledCrew && !test_instance2.crew.disabled) {
						boolean test_found = false
						switch (resultType) {
							case ResultType.Planningtask:
								test_found = test_instance2.IsPlanningTestRun()
								break
							case ResultType.Flight:
								test_found = test_instance2.IsFlightTestRun()
								break
							case ResultType.Observation:
								test_found = test_instance2.IsObservationTestRun()
								break
							case ResultType.Landing:
								test_found = test_instance2.IsLandingTestRun()
								break
							case ResultType.Landing1:
								test_found = test_instance2.IsLandingTest1Run()
								break
							case ResultType.Landing2:
								test_found = test_instance2.IsLandingTest2Run()
								break
							case ResultType.Landing3:
								test_found = test_instance2.IsLandingTest3Run()
								break
							case ResultType.Landing4:
								test_found = test_instance2.IsLandingTest4Run()
								break
							case ResultType.Special:
								test_found = test_instance2.IsSpecialTestRun()
								break
							case ResultType.Crew:
								test_found = true
								break
						}
						if (test_found) {
							return test_instance2.id
						}
					}
				}
				if (test_instance2.id == this.id) {
					start_found = true
				}
			}
		}
		return 0
	}
	
	long GetPrevTestID(ResultType resultType, def session)
	{
		boolean start_found = false
		int page_pos = 1
		if (session.showPage) {
			page_pos = session.showPageNum
		}
		boolean show_test = false
		for (Test test_instance2 in Test.findAllByTask(this.task,[sort:'viewpos', order:'desc'])) {
			if (session.showPage) {
				if (page_pos == session.showPagePos) {
					show_test = true
				} else {
					show_test = false
				}
				if (test_instance2.pageBreak) {
					page_pos--
				}
			} else {
				show_test = true
			}
			if (show_test) {
				if (start_found) {
					if (!test_instance2.disabledCrew && !test_instance2.crew.disabled) {
						boolean test_found = false
						switch (resultType) {
							case ResultType.Planningtask:
								test_found = test_instance2.IsPlanningTestRun()
								break
							case ResultType.Flight:
								test_found = test_instance2.IsFlightTestRun()
								break
							case ResultType.Observation:
								test_found = test_instance2.IsObservationTestRun()
								break
							case ResultType.Landing:
								test_found = test_instance2.IsLandingTestRun()
								break
							case ResultType.Landing1:
								test_found = test_instance2.IsLandingTest1Run()
								break
							case ResultType.Landing2:
								test_found = test_instance2.IsLandingTest2Run()
								break
							case ResultType.Landing3:
								test_found = test_instance2.IsLandingTest3Run()
								break
							case ResultType.Landing4:
								test_found = test_instance2.IsLandingTest4Run()
								break
							case ResultType.Special:
								test_found = test_instance2.IsSpecialTestRun()
								break
							case ResultType.Crew:
								test_found = true
								break
						}
						if (test_found) {
							return test_instance2.id
						}
					}
				}
				if (test_instance2.id == this.id) {
					start_found = true
				}
			}
		}
		return 0
	}
	
    int GetTestPos()
    {
        int test_pos = 0
        for (Test test_instance in Test.findAllByTask(this.task,[sort:"viewpos"])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
				test_pos++
				if (test_instance.id == this.id) {
					return test_pos
				}
            }
        }
        return 0
    }
    
    String GetIntermediateLandingTime(boolean viewShortTime)
    {
        Date tp_time = startTime
        for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(this,[sort:"id"])) {
            tp_time = testlegflight_instance.AddPlanLegTime(tp_time)
            if (testlegflight_instance.coordTitle.type == CoordType.iLDG) {
                if (viewShortTime) {
                    return FcMath.TimeStrShort(tp_time)
                } else if (task.IsFullMinute(task.iLandingDurationFormula)) {
                    return FcMath.TimeStrShort(tp_time)
                } else {
                    return FcMath.TimeStr(tp_time)
                }
            }
        }
        return ""
    }
    
    String EMailAddress()
    {
        return crew.email
    }
    
    boolean IsEMailPossible()
    {
        return NetTools.EMailList(crew.email) && BootStrap.global.IsEMailPossible() && BootStrap.global.IsFTPPossible()
    }
    
    boolean IsSendEMailPossible()
    {
        return IsEMailPossible() && !crewResultsModified && !GetFlightTestUploadJobStatus().InWork() && !IsTestResultsProvisional(GetResultSettings())
    }
    
	UploadJobStatus GetFlightTestUploadJobStatus()
	{
		UploadJobTest uploadjob_test = UploadJobTest.findByTest(this)
		if (uploadjob_test) {
			return uploadjob_test.uploadJobStatus
		}
		return UploadJobStatus.None
	}
	
	String GetFlightTestUploadLink()
	{
		UploadJobTest uploadjob_test = UploadJobTest.findByTest(this)
		if (uploadjob_test) {
			if (uploadjob_test.uploadJobStatus == UploadJobStatus.Done) {
				return uploadjob_test.uploadJobLink
			}
		}
		return ""
	}
	
    boolean IsShowMapPossible()
    {
        if (IsLoggerData()) {
            return true
        }
        return false
    }
    
    boolean IsTrackerImportPossible()
    {
        if (BootStrap.global.IsLiveTrackingPossible() && task.contest.liveTrackingContestID && task.contest.liveTrackingScorecard && task.liveTrackingNavigationTaskID) {
            return true
        }
        return false
    }
    
    boolean IsLoggerData()
    {
        return TrackPoint.countByLoggerdata(loggerData) > 0
    }
    
    boolean IsLoggerResult()
    {
        return CalcResult.countByLoggerresult(loggerResult) > 0
            
    }
    
    boolean IsLoggerResultWithoutRunwayMissed()
    {
        if (IsLoggerResult()) {
            for (CalcResult calc_result in CalcResult.findAllByLoggerresultAndGateNotFound(loggerResult,true)) {
                if (calc_result.coordTitle.type.IsRunwayCoord()) {
                    return false
                }
            }
            return true
        }
        return false
    }
    
    Map GetTrackPoints(String loggerDataStartUtc, String loggerDataEndUtc)
    {
        Map ret = [trackPoints:[], startUtc:"", endUtc:""]
        List track_points = []
        String start_utc = loggerDataStartUtc
        String end_utc = loggerDataEndUtc
        if (IsLoggerData()) {
            boolean valid_utc = true
            TrackPoint.findAllByLoggerdata(loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                if (valid_utc) {
                    boolean add_trackpoint = true
                    try {
                        Date utc_date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", trackpoint_instance.utc)
                    } catch (Exception e) {
                        valid_utc = false
                        add_trackpoint = false
                    }
                    if (add_trackpoint) {
                        if (loggerDataStartUtc) {
                            if (trackpoint_instance.utc < loggerDataStartUtc) {
                                add_trackpoint = false
                            }
                        }
                        if (loggerDataEndUtc) {
                            if (trackpoint_instance.utc > loggerDataEndUtc) {
                                add_trackpoint = false
                            }
                        }
                    }
                    if (add_trackpoint) {
                        if (!start_utc) {
                            start_utc = trackpoint_instance.utc
                        }
                        if (!loggerDataEndUtc) {
                            end_utc = trackpoint_instance.utc
                        }
                        Map new_point = [utc:       trackpoint_instance.utc,
                                         latitude:  trackpoint_instance.latitude,
                                         longitude: trackpoint_instance.longitude,
                                         altitude:  trackpoint_instance.altitude,
                                         track:     trackpoint_instance.track
                                        ]
                        track_points += new_point
                    }
                }
            }
        }
        return [trackPoints:track_points, startUtc:start_utc, endUtc:end_utc]
    }
    
    String GetLoggerDataFirstUtc()
    {
        if (IsLoggerData()) {
            return TrackPoint.findByLoggerdata(loggerData,[sort:"id"]).utc
        }
        return ""
    }
    
    String GetLoggerDataLastUtc()
    {
        if (IsLoggerData()) {
            return TrackPoint.findByLoggerdata(loggerData,[sort:"id", order:"desc"]).utc
        }
        return ""

    }

    BigDecimal GetTO2SPTime()
    {
        return FcMath.TimeDiff(takeoffTime, startTime)
    }
    
    BigDecimal GetFP2LDGTime()
    {
        return FcMath.TimeDiff(finishTime, maxLandingTime)
    }
    
    BigDecimal GetiFP2iLDGTime()
    {
        Date ifp_time =null
        Date ildg_time = null
        Date tp_time = startTime
        for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(this,[sort:"id"])) {
            tp_time = testlegflight_instance.AddPlanLegTime(tp_time)
            if (testlegflight_instance.coordTitle.type == CoordType.iFP) {
                ifp_time = tp_time
            }
            if (testlegflight_instance.coordTitle.type == CoordType.iLDG) {
                ildg_time = tp_time
            }
            if (ifp_time && ildg_time) {
                return FcMath.TimeDiff(ifp_time, ildg_time)
            }
        }
        return 0
    }
    
    BigDecimal GetiLDG2iSPTime()
    {
        Date ildg_time = null
        Date isp_time =null
        Date tp_time = startTime
        for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(this,[sort:"id"])) {
            tp_time = testlegflight_instance.AddPlanLegTime(tp_time)
            if (testlegflight_instance.coordTitle.type == CoordType.iLDG) {
                ildg_time = tp_time
            }
            if (testlegflight_instance.coordTitle.type == CoordType.iSP) {
                isp_time = tp_time
            }
            if (ildg_time && isp_time) {
                return FcMath.TimeDiff(ildg_time, isp_time)
            }
        }
        return 0
    }
    
    boolean IsIncreaseEnabled()
    {
        return task.increaseEnabled && crew.IsIncreaseEnabled()
    }
    
    int GetIncreasePenalties(int addPenalties)
    {
        return crew.GetIncreaseFactor() * addPenalties / 100
    }
    
    String GetIncreaseValue()
    {
        return crew.GetIncreaseFactor().toString() + "%"
    }
    
    TurnpointRoute GetTurnpointRoute()
    {
        if (task.flighttest) {
            return task.flighttest.route.turnpointRoute
        }
        return TurnpointRoute.None
    }
    
    EnrouteMeasurement GetEnroutePhotoMeasurement(boolean ignoreObservationTestEnroutePhotoValueUnit = false)
    {
        if (!ignoreObservationTestEnroutePhotoValueUnit) {
            if (observationTestEnroutePhotoValueUnit) {
                return observationTestEnroutePhotoValueUnit.GetEnrouteMeasurement()
            }
        }
        if (task.flighttest) {
            return task.flighttest.route.enroutePhotoMeasurement
        }
        return EnrouteMeasurement.None
    }
    
    EnrouteMeasurement GetEnroutePhotoResultMeasurement()
    {
        if (task.flighttest) {
            return task.flighttest.route.enroutePhotoRoute.GetEnrouteResultMeasurement()
        }
        return EnrouteMeasurement.None
    }
    
    EnrouteMeasurement GetEnrouteCanvasMeasurement(boolean ignoreObservationTestEnrouteCanvasValueUnit = false)
    {
        if (!ignoreObservationTestEnrouteCanvasValueUnit) {
            if (observationTestEnrouteCanvasValueUnit) {
                return observationTestEnrouteCanvasValueUnit.GetEnrouteMeasurement()
            }
        }
        if (task.flighttest) {
            return task.flighttest.route.enrouteCanvasMeasurement
        }
        return EnrouteMeasurement.None
    }
    
    EnrouteMeasurement GetEnrouteCanvasResultMeasurement()
    {
        if (task.flighttest) {
            return task.flighttest.route.enrouteCanvasRoute.GetEnrouteResultMeasurement()
        }
        return EnrouteMeasurement.None
    }
    
    boolean IsObservationJudgeSign()
    {
        if (task.flighttest) {
            if (task.flighttest.route.turnpointMapMeasurement && IsObservationTestTurnpointRun()) {
                return true
            }
            if (GetEnroutePhotoMeasurement(true) == EnrouteMeasurement.Map && IsObservationTestEnroutePhotoRun()) {
                return true
            }
            if (GetEnrouteCanvasMeasurement(true) == EnrouteMeasurement.Map && IsObservationTestEnrouteCanvasRun()) {
                return true
            }
        }
        return false
    }

    boolean IsObservationTestInexactValue()
    {
        float v = GetObservationTestEnrouteInexactValue()
        return (v > 0) && (GetObservationTestEnrouteInexactPoints() > 0)
    }

    String GetObservationTestEnrouteCorrectValueStr(boolean isPrint)
    {
        return FcMath.EnrouteValueStr(GetObservationTestEnrouteCorrectValue()) + GetMsg(GetObservationTestEnrouteValueUnit().code,isPrint)
    }
    
    String GetObservationTestEnrouteInexactValueStr(boolean isPrint)
    {
        return FcMath.EnrouteValueStr(GetObservationTestEnrouteInexactValue()) + GetMsg(GetObservationTestEnrouteValueUnit().code,isPrint)
    }
    
    Map GetEnrouteDistanceNM(BigDecimal distanceNM)
    {
        Map ret = [correct_min:0, correct_max:0, inexact_min:0, inexact_max:0]
        
        int inexact_points = GetObservationTestEnrouteInexactPoints()
        BigDecimal correct_check_value = GetObservationTestEnrouteCorrectValue()
        BigDecimal inexact_check_value = GetObservationTestEnrouteInexactValue()
        
        if (GetObservationTestEnrouteValueUnit() == EnrouteValueUnit.mm) {
            correct_check_value = FcMath.RoundDistance(task.flighttest.route.Convert_mm2NM(correct_check_value, true))
            inexact_check_value = FcMath.RoundDistance(task.flighttest.route.Convert_mm2NM(inexact_check_value, true))
        }
        
        return [inexact_points: inexact_points,
                correct_min: distanceNM - correct_check_value, 
                correct_max: distanceNM + correct_check_value,
                inexact_min: distanceNM - inexact_check_value,
                inexact_max: distanceNM + inexact_check_value
               ]
    }
    
    Map GetEnrouteDistancemm(BigDecimal distancemm)
    {
        Map ret = [correct_min:0, correct_max:0, inexact_min:0, inexact_max:0]
        
        int inexact_points = GetObservationTestEnrouteInexactPoints()
        BigDecimal correct_check_value = GetObservationTestEnrouteCorrectValue()
        BigDecimal inexact_check_value = GetObservationTestEnrouteInexactValue()
        
        if (GetObservationTestEnrouteValueUnit() == EnrouteValueUnit.NM) {
            correct_check_value = task.flighttest.route.Convert_NM2mm(correct_check_value, true)
            inexact_check_value = task.flighttest.route.Convert_NM2mm(inexact_check_value, true)
        }
        
        return [inexact_points: inexact_points,
                correct_min: distancemm - correct_check_value, 
                correct_max: distancemm + correct_check_value,
                inexact_min: distancemm - inexact_check_value,
                inexact_max: distancemm + inexact_check_value
               ]
    }
    
    String GetEnrouteDistanceResultsNM(BigDecimal distanceNM)
    {
        String s = ""
        Map nm_values = GetEnrouteDistanceNM(distanceNM)
        if (nm_values.inexact_points) {
            s += "${FcMath.DistanceStr(nm_values.inexact_min)}/"
        }
        s += "${FcMath.DistanceStr(nm_values.correct_min)} - ${FcMath.DistanceStr(nm_values.correct_max)}"
        if (nm_values.inexact_points) {
            s += "/${FcMath.DistanceStr(nm_values.inexact_max)}"
        }
        return s
    }
    
    String GetEnrouteDistanceResultsmm(BigDecimal distancemm)
    {
        String s = ""
        Map mm_values = GetEnrouteDistancemm(distancemm)
        if (mm_values.inexact_points) {
            s += "${FcMath.DistanceMeasureStr(mm_values.inexact_min)}/"
        }
        s += "${FcMath.DistanceMeasureStr(mm_values.correct_min)} - ${FcMath.DistanceMeasureStr(mm_values.correct_max)}"
        if (mm_values.inexact_points) {
            s += "/${FcMath.DistanceMeasureStr(mm_values.inexact_max)}"
        }
        return s
    }
    
    EnrouteValueUnit GetObservationTestEnroutePhotoValueUnit()
    {
        if (observationTestEnroutePhotoValueUnit) {
            return observationTestEnroutePhotoValueUnit
        }
        return task.flighttest.route.enroutePhotoMeasurement.GetEnrouteValueUnit()
    }
    
    EnrouteValueUnit GetObservationTestEnrouteCanvasValueUnit()
    {
        if (observationTestEnrouteCanvasValueUnit) {
            return observationTestEnrouteCanvasValueUnit
        }
        return task.flighttest.route.enrouteCanvasMeasurement.GetEnrouteValueUnit()
    }
    
    boolean IsObservationSignUsed()
    {
        if (TurnpointData.findByTest(this) || EnroutePhotoData.findByTest(this) || EnrouteCanvasData.findByTest(this)) {
            return true
        }
        return false
    }
    
    static Test GetFirstTest(Task taskInstance, ResultClass resultclassInstance)
    {
        if (resultclassInstance) {
            for (Test test_instance in Test.findAllByTask(taskInstance)) {
                if (test_instance.crew.resultclass.id == resultclassInstance.id) {
                    return test_instance
                }
            }
        }
        return Test.findByTask(taskInstance)
    }
    
    Map GetPrintCrewResultsDefaultParams()
    {
        Map ret = [printSummaryResults:'on']
        if (IsPlanningTestRun()) {
            ret += [printPlanningResults:'on', printPlanningResultsScan:'on']
        }
        if (IsFlightTestRun()) {
            ret += [printFlightResults:'on', printFlightMap:'on']
        }
        if (IsObservationTestRun()) {
            ret += [printObservationResults:'on', printObservationResultsScan:'on']
        }
        if (IsLandingTestRun()) {
            ret += [printLandingResults:'on']
        }
        if (IsSpecialTestRun()) {
            ret += [printSpecialResults:'on']
        }
        return ret
    }
    
    Date GetMaxSubmissionTime()
    {
        GregorianCalendar time = new GregorianCalendar()
        time.setTime(finishTime)
        if (task.flighttest.submissionMinutes) {
            time.add(Calendar.MINUTE, task.flighttest.submissionMinutes)
        }
        Integer add_minutes2 = GetMinutesAddSubmission()
        if (add_minutes2) {
            time.add(Calendar.MINUTE, add_minutes2)
        }
        // FcMath.SetFullMinute(time)
        return time.getTime()
    }
    
    List GetCurvedPointIds()
    {
        List curved_point_ids = []
        boolean curved_point = false
        for (CoordResult coordresult_instance in CoordResult.findAllByTest(this,[sort:"id",order:"desc"])) {
            if (curved_point) {
                if (coordresult_instance.type != CoordType.SECRET) {
                    curved_point = false
                } else {
                    curved_point_ids += coordresult_instance.id
                }
            }
            if (coordresult_instance.endCurved) {
                curved_point = true
            }
        }
        return curved_point_ids
    }

    Float GetSecretGateWidth()
    {
        if (crew.resultclass) {
            return crew.resultclass.secretGateWidth
        }
        return null
    }
    
    Integer GetMinutesBeforeStartTime()
    {
        if (crew.resultclass) {
            return crew.resultclass.minutesBeforeStartTime
        }
        return null
    }
    
    Date GetTestingTime()
    {
        Integer sub_minutes = GetMinutesBeforeStartTime()
        
        GregorianCalendar time = new GregorianCalendar()
        time.setTime(testingTime) 
        if (sub_minutes) {
            time.add(Calendar.MINUTE, -sub_minutes)
        }
        
        return time.getTime()
    }
    
    Integer GetMinutesAddSubmission()
    {
        if (crew.resultclass) {
            return crew.resultclass.minutesAddSubmission
        }
        return null
    }
}
