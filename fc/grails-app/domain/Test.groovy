import java.util.Map;

class Test 
{
	Crew crew
    Boolean disabledCrew = false                           // DB-2.9
	PlanningTestTask planningtesttask
	FlightTestWind flighttestwind
	
	int viewpos = 0
	BigDecimal taskTAS = 0
	Aircraft taskAircraft                                  // DB-2.3
    Integer aflosStartNum = 0                              // DB-2.10
    
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
    String  flightTestLink = ""                            // DB-2.10

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
	Boolean landingTest1NotAllowedAerodynamicAuxiliaries = false // DB-2.7
    Boolean landingTest1PowerInAir = false                 // DB-2.9
    Boolean landingTest1FlapsInAir = false                 // DB-2.9
    Boolean landingTest1TouchingObstacle = false           // DB-2.9

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
		planningTestOtherPenalties()
		flightTestOtherPenalties()
		landingTestOtherPenalties(min:0)
		landingTestPenalties(min:0)
		
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
                if (!landingTestComplete) {
                    provisional = true
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
    
    String GetPrintLandingCalculatorValues()
    {
        if (task.contest.resultClasses && task.contest.contestRuleForEachClass) {
            if (crew.resultclass) {
                return crew.resultclass.printLandingCalculatorValues
            }
            return false
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
        return 0
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
	
    int GetAFLOSStartNum()
    {
        int start_num = aflosStartNum
        if (start_num <= 0) {
            start_num = crew.startNum
        }
        return start_num
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
			case ResultType.Special:	
				result_version = GetSpecialTestVersion()
                provisional = !specialTestComplete
				break
			case ResultType.Crew:
				result_version = GetCrewResultsVersion()
                // TODO: provisional = !
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
        return "${task.contest.title}: ${GetTitle(ResultType.Flight,true)}" // true - print 
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
    
	long GetNextTestID(ResultType resultType)
	{
		long nexttest_id = 0
		boolean set_next = false
		for (Test test_instance2 in Test.findAllByTask(this.task,[sort:'viewpos'])) {
			if (set_next) {
				if (!test_instance2.disabledCrew && !test_instance2.crew.disabled) {
					boolean get_next = false
					switch (resultType) {
						case ResultType.Planningtask:
							get_next = test_instance2.IsPlanningTestRun()
							break
						case ResultType.Flight:
							get_next = test_instance2.IsFlightTestRun()
							break
						case ResultType.Observation:
							get_next = test_instance2.IsObservationTestRun()
							break
						case ResultType.Landing:
							get_next = test_instance2.IsLandingTestRun()
							break
						case ResultType.Special:
							get_next = test_instance2.IsSpecialTestRun()
							break
						case ResultType.Crew:
							get_next = true
							break
					}
					if (get_next) {
						nexttest_id = test_instance2.id
						set_next = false
					}
				}
			}
            if (test_instance2.id == this.id) { // BUG: direkter Klassen-Vergleich geht nicht, wenn Test-Instance bereits woanders geändert
				set_next = true
            }
		}
		return nexttest_id
	}
	
	static long GetNext2TestID(long testID, ResultType resultType)
	{
		long next2test_id = 0
		if (testID) {
			Test test_instance = Test.get(testID)
			if (test_instance) {
				next2test_id = test_instance.GetNextTestID(resultType)
			}
		}
		return next2test_id
	}
	
	long GetNext2TestID(ResultType resultType)
	{
		return GetNext2TestID(this.id, resultType)
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
    
    boolean IsEMailPossible()
    {
        if (aflosStartNum && NetTools.EMailList(crew.email) && BootStrap.global.IsEMailPossible() && BootStrap.global.IsFTPPossible()) {
            return true
        }
        return false
    }
    
    String EMailAddress()
    {
        return crew.email
    }
}
