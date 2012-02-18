class TaskClass
// DB-2.0
{
	ResultClass resultclass
	
	boolean planningTestRun              = true
	boolean flightTestRun                = true
	boolean observationTestRun           = true
	boolean landingTestRun               = true
	boolean landingTest1Run              = false
	boolean landingTest2Run              = false
	boolean landingTest3Run              = false
	boolean landingTest4Run              = false
	boolean specialTestRun               = false

	boolean planningTestDistanceMeasure  = false
	boolean planningTestDirectionMeasure = true

	static belongsTo = [task:Task]

	static constraints = {
		resultclass(nullable:false)
		task(nullable:false)
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
			result_columns++
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
		landingTestRun = taskClassInstance.landingTestRun
		landingTest1Run = taskClassInstance.landingTest1Run
		landingTest2Run = taskClassInstance.landingTest2Run
		landingTest3Run = taskClassInstance.landingTest3Run
		landingTest4Run = taskClassInstance.landingTest4Run
		specialTestRun = taskClassInstance.specialTestRun
		planningTestDistanceMeasure = taskClassInstance.planningTestDistanceMeasure
		planningTestDirectionMeasure = taskClassInstance.planningTestDirectionMeasure
		
		this.save()
	}
}
