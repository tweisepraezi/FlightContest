import java.util.Map;

class Task 
{
	String title
    int idTitle
	
	PlanningTest planningtest
	FlightTest flighttest
	
	boolean planningTestRun              = true
	boolean flightTestRun                = true
	boolean observationTestRun           = true
	boolean landingTestRun               = true
	boolean landingTest1Run              = false   // DB-2.0
	boolean landingTest2Run              = false   // DB-2.0
	boolean landingTest3Run              = false   // DB-2.0
	boolean landingTest4Run              = false   // DB-2.0
	boolean specialTestRun               = false
	
	String firstTime                     = "11:00" // Local time of first planning test [hh:mm]
	int takeoffIntervalNormal            = 3       // space between takeoff [min]
	int takeoffIntervalFasterAircraft    = 30      // space between takeoff to faster aircraft [min]
	
	int planningTestDuration             = 60      // duration of planning test [min]
	int preparationDuration              = 15      // duration of aircraft preparation [min]
	int risingDuration                   = 5       // duration from takeoff to start point [min]
    int maxLandingDuration               = 5       // duration from finish point to landing [min]
    int parkingDuration                  = 10      // duration from finish point to aircraft parking [min] (> maxLandingDuration)
	
	int minNextFlightDuration            = 30      // duration of aircraft maintenance between two flights [min]
	int procedureTurnDuration            = 1       // duration of Procedure Turn [min]

	int addTimeValue                     = 3       // add/subtract time value [min]  

    boolean planningTestDistanceMeasure  = false
    boolean planningTestDirectionMeasure = true
	
	boolean timetableModified            = true
	int timetableVersion                 = 0
	
	String disabledCheckPoints           = ""      // list of disabled check point titles, separated with ',', DB-1.1
	
	// transient values 
	static transients = ['printPlanningResults','printFlightResults','printObservationResults','printLandingResults','printSpecialResults']
	boolean printPlanningResults = true
	boolean printFlightResults = true
	boolean printObservationResults = true
	boolean printLandingResults = true
	boolean printSpecialResults = true
	
    static belongsTo = [contest:Contest]
    
	static hasMany = [tests:Test, taskclasses:TaskClass]
	
	static constraints = {
		firstTime(blank:false, validator:{ val, obj ->
			if (val.size() > 5) {
				return false
			}
			try {
				Date t = Date.parse("HH:mm",val)
			} catch(Exception e) {
				return false
			}
			return true
		})
		
		takeoffIntervalNormal(range:0..240)
		takeoffIntervalFasterAircraft(range:0..240)
		planningTestDuration(range:0..240)
		preparationDuration(range:0..240)
		risingDuration(range:0..240)
        maxLandingDuration(range:0..240)
		parkingDuration(range:0..240, validator:{ val, obj ->
			if (val < obj.maxLandingDuration) {
				return false
			}
		})
		minNextFlightDuration(range:0..240)
		procedureTurnDuration(range:0..60)
		addTimeValue(range:1..60)
		
		planningtest(nullable:true)
		flighttest(nullable:true)
		contest(nullable:false)
	}

    static mapping = {
		tests sort:"id"
		taskclasses sort:"id"
	}
	
	void CopyValues(Task taskInstance)
	{
		title = taskInstance.title
	    idTitle = taskInstance.idTitle
		planningTestRun = taskInstance.planningTestRun
		flightTestRun = taskInstance.flightTestRun
		observationTestRun = taskInstance.observationTestRun
		landingTestRun = taskInstance.landingTestRun
		landingTest1Run = taskInstance.landingTest1Run
		landingTest2Run = taskInstance.landingTest2Run
		landingTest3Run = taskInstance.landingTest3Run
		landingTest4Run = taskInstance.landingTest4Run
		specialTestRun = taskInstance.specialTestRun
		firstTime = taskInstance.firstTime
		takeoffIntervalNormal = taskInstance.takeoffIntervalNormal
		takeoffIntervalFasterAircraft = taskInstance.takeoffIntervalFasterAircraft
		planningTestDuration = taskInstance.planningTestDuration
		preparationDuration = taskInstance.preparationDuration
		risingDuration = taskInstance.risingDuration
	    maxLandingDuration = taskInstance.maxLandingDuration
	    parkingDuration = taskInstance.parkingDuration
		minNextFlightDuration = taskInstance.minNextFlightDuration
		procedureTurnDuration = taskInstance.procedureTurnDuration
		addTimeValue = taskInstance.addTimeValue  
	    planningTestDistanceMeasure = taskInstance.planningTestDistanceMeasure
	    planningTestDirectionMeasure = taskInstance.planningTestDirectionMeasure
		// timetableModified = taskInstance.timetableModified
		// timetableVersion = taskInstance.timetableVersion

		//PlanningTest planningtest
		//FlightTest flighttest

		this.save()
		
		// taskclasses:TaskClass
		TaskClass.findAllByTask(taskInstance,[sort:"id"]).each { TaskClass taskclass_instance ->
			TaskClass new_taskclass_instance = new TaskClass()
			new_taskclass_instance.task = this
			new_taskclass_instance.resultclass = ResultClass.findByNameAndContest(taskclass_instance.resultclass.name,contest)
			new_taskclass_instance.CopyValues(taskclass_instance)
			new_taskclass_instance.save()
		}
		
		Crew.findAllByContest(contest,[sort:"viewpos"]).eachWithIndex { Crew crew_instance, int i ->
			Test test_instance = new Test()
			test_instance.crew = crew_instance
			test_instance.taskTAS = crew_instance.tas
			test_instance.viewpos = i
			test_instance.task = this
			test_instance.timeCalculated = false
			test_instance.save()
		}
	}
	
    String idName()
    {
		return "${getMsg('fc.task')}-${idTitle}"
    }
    
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
	
	boolean IsPlanningTestRun()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.planningTestRun) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return planningTestRun
	}
	
	boolean IsFlightTestRun()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.flightTestRun) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return flightTestRun
	}
	
	boolean IsObservationTestRun()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.observationTestRun) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return observationTestRun
	}
	
	boolean IsLandingTestRun()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.landingTestRun) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return landingTestRun
	}
	
	boolean IsLandingTestAnyRun()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.landingTest1Run || taskclass_instance.landingTest2Run || taskclass_instance.landingTest3Run || taskclass_instance.landingTest4Run) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return landingTest1Run || landingTest2Run || landingTest3Run || landingTest4Run
	}
	
	boolean IsLandingTest1Run()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.landingTest1Run) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return landingTest1Run
	}
	
	boolean IsLandingTest2Run()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.landingTest2Run) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return landingTest2Run
	}
	
	boolean IsLandingTest3Run()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.landingTest3Run) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return landingTest3Run
	}
	
	boolean IsLandingTest4Run()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.landingTest4Run) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return landingTest4Run
	}
	
	boolean IsSpecialTestRun()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.specialTestRun) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return specialTestRun
	}
	
	boolean IsPlanningTestDistanceMeasure()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.planningTestDistanceMeasure) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return planningTestDistanceMeasure
	}
	
	boolean IsPlanningTestDirectionMeasure()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.planningTestDirectionMeasure) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return planningTestDirectionMeasure
	}
	
	boolean AreResultsProvisional(Map resultSettings)
	{
		//println "XX Task.AreResultsProvisional $title $resultSettings"
		for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
			if (test_instance.AreResultsProvisional(resultSettings)) {
				//println "-> true (Task.AreResultsProvisional $title $test_instance.crew.name)"
				return true
			}
		}
		//println "-> false (Task.AreResultsProvisional)"
		return false
	}
	
	boolean AreClassResultsProvisional(Map resultSettings, ResultClass resultclassInstance)
	{
		//println "XX Task.AreClassResultsProvisional $title $resultclassInstance.name $resultSettings"
		for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
			if (test_instance.crew.resultclass == resultclassInstance) {
				if (test_instance.AreClassResultsProvisional(resultSettings,resultclassInstance)) {
					//println "-> true (Task.AreClassResultsProvisional $title $test_instance.crew.name)"
					return true
				}
			}
		}
		//println "-> false (Task.AreClassResultsProvisional)"
		return false
	}
	
	Map GetResultSettings()
	{
		Map ret = [:]
		if (IsPlanningTestRun()) {
			ret += [Planning:true]
		}
		if (IsFlightTestRun()) {
			ret += [Flight:true]
		}
		if (IsObservationTestRun()) {
			ret += [Observation:true]
		}
		if (IsLandingTestRun()) {
			ret += [Landing:true]
		}
		if (IsSpecialTestRun()) {
			ret += [Special:true]
		}
		return ret
	}
	
	Map GetClassResultSettings(ResultClass resultclassInstance)
	{
		Map ret = [:]
		for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
			if (taskclass_instance.resultclass == resultclassInstance) {
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
				return ret
			}
		}
		return ret
	}
	
	int GetResultColumns()
	{
		int result_columns = 0
		if (IsPlanningTestRun()) {
			result_columns++
		}
		if (IsFlightTestRun()) {
			result_columns++
		}
		if (IsObservationTestRun()) {
			result_columns++
		}
		if (IsLandingTestRun()) {
			int landing_result_any_columns = GetLandingResultAnyColumns()
			if (landing_result_any_columns > 0) {
				result_columns += landing_result_any_columns
			} else {
				result_columns++
			}
		}
		if (IsSpecialTestRun()) {
			result_columns++
		}
		return result_columns
	}
	
	int GetLandingResultAnyColumns()
	{
		int result_columns = 0
		if (IsLandingTestRun()) {
			if (IsLandingTest1Run()) {
				result_columns++
			}
			if (IsLandingTest2Run()) {
				result_columns++
			}
			if (IsLandingTest3Run()) {
				result_columns++
			}
			if (IsLandingTest4Run()) {
				result_columns++
			}
		}
		return result_columns
	}
	
	TaskClass GetTaskClass(ResultClass resultClass)
	{
		for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
			
			if (resultClass == taskclass_instance.resultclass)
			{
				return taskclass_instance
			}
		}
		return null
	}

}
