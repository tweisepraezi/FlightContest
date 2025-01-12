class TaskClass
// DB-2.0
{
	ResultClass resultclass
	
	boolean planningTestRun                  = false
	boolean flightTestRun                    = false
	boolean observationTestRun               = false
    Boolean observationTestTurnpointRun      = false // DB-2.13
    Boolean observationTestEnroutePhotoRun   = false // DB-2.13
    Boolean observationTestEnrouteCanvasRun  = false // DB-2.13
	boolean landingTestRun                   = false
	boolean landingTest1Run                  = false
	boolean landingTest2Run                  = false
	boolean landingTest3Run                  = false
	boolean landingTest4Run                  = false
	boolean specialTestRun                   = false

	boolean planningTestDistanceMeasure      = false
	boolean planningTestDirectionMeasure     = true
	Boolean flightTestCheckSecretPoints      = true  // DB-2.3
	Boolean flightTestCheckTakeOff           = true  // DB-2.3
	Boolean flightTestCheckLanding           = true  // DB-2.3
	String specialTestTitle                  = ""    // DB-2.3
	
	static belongsTo = [task:Task]

	static constraints = {
		resultclass(nullable:false)
		task(nullable:false)
		
		// DB-2.3 compatibility
		flightTestCheckSecretPoints(nullable:true)
		flightTestCheckTakeOff(nullable:true)
		flightTestCheckLanding(nullable:true)
		specialTestTitle(nullable:true)
        
        // DB-2.13 compatibility
        observationTestTurnpointRun(nullable:true)
        observationTestEnroutePhotoRun(nullable:true)
        observationTestEnrouteCanvasRun(nullable:true)
	}

	int GetResultColumns()
	{
		int result_columns = 0
		if (planningTestRun) {
			result_columns++
		}
		if (flightTestRun) {
			result_columns++
		}
		if (observationTestRun) {
            int observation_result_any_columns = GetObservationResultAnyColumns()
            if (observation_result_any_columns > 0) {
                result_columns += observation_result_any_columns
            } else {
                result_columns++
            }
		}
		if (landingTestRun) {
			int landing_result_any_columns = GetLandingResultAnyColumns()
			if (landing_result_any_columns > 0) {
				result_columns += landing_result_any_columns
			} else {
				result_columns++
			}
		}
		if (specialTestRun) {
			result_columns++
		}
		return result_columns
	}
	
    int GetObservationResultAnyColumns()
    {
        int result_columns = 0
        if (observationTestRun) {
            if (observationTestTurnpointRun) {
                result_columns++
            }
            if (observationTestEnroutePhotoRun) {
                result_columns++
            }
            if (observationTestEnrouteCanvasRun) {
                result_columns++
            }
        }
        return result_columns
    }
    
	int GetLandingResultAnyColumns()
	{
		int result_columns = 0
		if (landingTestRun) {
			if (landingTest1Run) {
				result_columns++
			}
			if (landingTest2Run) {
				result_columns++
			}
			if (landingTest3Run) {
				result_columns++
			}
			if (landingTest4Run) {
				result_columns++
			}
		}
		return result_columns
	}
    
	void CopyValues(TaskClass taskClassInstance)
	{
		planningTestRun = taskClassInstance.planningTestRun
		flightTestRun = taskClassInstance.flightTestRun
		observationTestRun = taskClassInstance.observationTestRun
        observationTestTurnpointRun = taskClassInstance.observationTestTurnpointRun
        observationTestEnroutePhotoRun = taskClassInstance.observationTestEnroutePhotoRun
        observationTestEnrouteCanvasRun = taskClassInstance.observationTestEnrouteCanvasRun
		landingTestRun = taskClassInstance.landingTestRun
		landingTest1Run = taskClassInstance.landingTest1Run
		landingTest2Run = taskClassInstance.landingTest2Run
		landingTest3Run = taskClassInstance.landingTest3Run
		landingTest4Run = taskClassInstance.landingTest4Run
		specialTestRun = taskClassInstance.specialTestRun
		planningTestDistanceMeasure = taskClassInstance.planningTestDistanceMeasure
		planningTestDirectionMeasure = taskClassInstance.planningTestDirectionMeasure
		flightTestCheckSecretPoints = taskClassInstance.flightTestCheckSecretPoints
		flightTestCheckTakeOff = taskClassInstance.flightTestCheckTakeOff
		flightTestCheckLanding = taskClassInstance.flightTestCheckLanding
		
		if (!this.save()) {
			throw new Exception("TaskClass.CopyValues could not save")
		}
	}
}
