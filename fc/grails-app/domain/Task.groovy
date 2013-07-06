import java.util.Map;

class Task 
{
	String title
    int idTitle
	
	PlanningTest planningtest
	FlightTest flighttest
	
	boolean planningTestRun                  = true
	boolean flightTestRun                    = true
	boolean observationTestRun               = true
	boolean landingTestRun                   = true
	boolean landingTest1Run                  = false // DB-2.0
	boolean landingTest2Run                  = false // DB-2.0
	boolean landingTest3Run                  = false // DB-2.0
	boolean landingTest4Run                  = false // DB-2.0
	boolean specialTestRun                   = false
	String specialTestTitle                  = ""    // DB-2.3

	String firstTime                         = "11:00" // Local time of first planning test [hh:mm]
	int takeoffIntervalNormal                = 3     // space between takeoff [min]
	Integer takeoffIntervalSlowerAircraft    = 3     // DB-2.4, space between takeoff to slower aircraft [min]
	int takeoffIntervalFasterAircraft        = 30    // space between takeoff to faster aircraft [min]
	
	int planningTestDuration                 = 60    // duration of planning test [min]
	int preparationDuration                  = 15    // duration of aircraft preparation [min]
	int risingDuration                       = 5     // duration from takeoff to start point [min]
    int maxLandingDuration                   = 5     // duration from finish point to landing [min]
    int parkingDuration                      = 10    // duration from finish point to aircraft parking [min] (> maxLandingDuration)
	
	int minNextFlightDuration                = 30    // duration of aircraft maintenance between two flights [min]
	int procedureTurnDuration                = 1     // duration of Procedure Turn [min]

	int addTimeValue                         = 3     // add/subtract time value [min]  

    boolean planningTestDistanceMeasure      = false
    boolean planningTestDirectionMeasure     = true
	Boolean flightTestCheckSecretPoints      = true  // DB-2.3
	Boolean flightTestCheckTakeOff           = true  // DB-2.3
	Boolean flightTestCheckLanding           = true  // DB-2.3
	
	boolean timetableModified                = true
	int timetableVersion                     = 0
	
	String disabledCheckPoints               = ""    // list of disabled check point titles, separated with ',', DB-1.1
	
	Boolean bestOfAnalysis                   = false // DB-2.3
	
	String printTimetableJuryPrintTitle      = ""    // DB-2.3
	Boolean printTimetableJuryNumber         = true  // DB-2.3
	Boolean printTimetableJuryCrew           = true  // DB-2.3
	Boolean printTimetableJuryAircraft       = true  // DB-2.3
	Boolean printTimetableJuryAircraftType   = false // DB-2.3
	Boolean printTimetableJuryAircraftColour = false // DB-2.3
	Boolean printTimetableJuryTAS            = true  // DB-2.3
	Boolean printTimetableJuryTeam           = false // DB-2.3
	Boolean printTimetableJuryPlanning       = true  // DB-2.3
	Boolean printTimetableJuryPlanningEnd    = true  // DB-2.3
	Boolean printTimetableJuryTakeoff        = true  // DB-2.3
	Boolean printTimetableJuryStartPoint     = true  // DB-2.3
	String  printTimetableJuryCheckPoints    = ""    // DB-2.3
	Boolean printTimetableJuryFinishPoint    = true  // DB-2.3
	Boolean printTimetableJuryLanding        = true  // DB-2.3
	Boolean printTimetableJuryArrival        = true  // DB-2.3
	Boolean printTimetableJuryEmptyColumn1   = false // DB-2.3
	String printTimetableJuryEmptyTitle1     = ""    // DB-2.3
	Boolean printTimetableJuryEmptyColumn2   = false // DB-2.3
	String printTimetableJuryEmptyTitle2     = ""    // DB-2.3
	Boolean printTimetableJuryEmptyColumn3   = false // DB-2.3
	String printTimetableJuryEmptyTitle3     = ""    // DB-2.3
	Boolean printTimetableJuryLandscape      = true  // DB-2.3
	Boolean printTimetableJuryA3             = false // DB-2.3
	
	String printTimetablePrintTitle          = ""    // DB-2.3
	Boolean printTimetableNumber             = true  // DB-2.3
	Boolean printTimetableCrew               = true  // DB-2.3
	Boolean printTimetableAircraft           = true  // DB-2.3
	Boolean printTimetableTAS                = true  // DB-2.3
	Boolean printTimetableTeam               = false // DB-2.3
	Boolean printTimetablePlanning           = true  // DB-2.3
	Boolean printTimetableTakeoff            = true  // DB-2.3
	Boolean printTimetableVersion            = true  // DB-2.3
	String printTimetableChange              = ""    // DB-2.3
	Boolean printTimetableLandscape          = false // DB-2.3
	Boolean printTimetableA3                 = false // DB-2.3
	
	Boolean hidePlanning                     = false // DB-2.3
	Boolean hideResults                      = false // DB-2.3
	
	// transient values 
	static transients = ['printPlanningResults','printFlightResults','printObservationResults','printLandingResults','printSpecialResults','printProvisionalResults']
	boolean printPlanningResults = true
	boolean printFlightResults = true
	boolean printObservationResults = true
	boolean printLandingResults = true
	boolean printSpecialResults = true
	boolean printProvisionalResults = false
	
    static belongsTo = [contest:Contest]
    
	static hasMany = [tests:Test, taskclasses:TaskClass]
	
	static final int TIMETABLECHANGESIZE = 10000     // DB-2.3
	
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
		
		// DB-2.3 compatibility
		flightTestCheckSecretPoints(nullable:true)
		flightTestCheckTakeOff(nullable:true)
		flightTestCheckLanding(nullable:true)
		bestOfAnalysis(nullable:true)
		printTimetableJuryPrintTitle(nullable:true)
		printTimetableJuryNumber(nullable:true)
		printTimetableJuryCrew(nullable:true)
		printTimetableJuryAircraft(nullable:true)
		printTimetableJuryAircraftType(nullable:true)
		printTimetableJuryAircraftColour(nullable:true)
		printTimetableJuryTAS(nullable:true)
		printTimetableJuryTeam(nullable:true)
		printTimetableJuryPlanning(nullable:true)
		printTimetableJuryPlanningEnd(nullable:true)
		printTimetableJuryTakeoff(nullable:true)
		printTimetableJuryStartPoint(nullable:true)
		printTimetableJuryCheckPoints(nullable:true)
		printTimetableJuryFinishPoint(nullable:true)
		printTimetableJuryLanding(nullable:true)
		printTimetableJuryArrival(nullable:true)
		printTimetableJuryEmptyColumn1(nullable:true)
		printTimetableJuryEmptyTitle1(nullable:true)
		printTimetableJuryEmptyColumn2(nullable:true)
		printTimetableJuryEmptyTitle2(nullable:true)
		printTimetableJuryEmptyColumn3(nullable:true)
		printTimetableJuryEmptyTitle3(nullable:true)
		printTimetableJuryLandscape(nullable:true)
		printTimetableJuryA3(nullable:true)
		printTimetablePrintTitle(nullable:true)
		printTimetableNumber(nullable:true)
		printTimetableCrew(nullable:true)
		printTimetableAircraft(nullable:true)
		printTimetableTAS(nullable:true)
		printTimetableTeam(nullable:true)
		printTimetablePlanning(nullable:true)
		printTimetableTakeoff(nullable:true)
		printTimetableVersion(nullable:true)
		printTimetableChange(nullable:true,maxSize:TIMETABLECHANGESIZE)
		printTimetableLandscape(nullable:true)
		printTimetableA3(nullable:true)
		specialTestTitle(nullable:true)
		hidePlanning(nullable:true)
		hideResults(nullable:true)
		
		// DB-2.4 compatibility
		takeoffIntervalSlowerAircraft(nullable:true,range:0..240)
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
		takeoffIntervalSlowerAircraft = taskInstance.takeoffIntervalSlowerAircraft
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
		flightTestCheckSecretPoints = taskInstance.flightTestCheckSecretPoints
		flightTestCheckTakeOff = taskInstance.flightTestCheckTakeOff
		flightTestCheckLanding = taskInstance.flightTestCheckLanding
		bestOfAnalysis = taskInstance.bestOfAnalysis

		if (!this.save()) {
			throw new Exception("Task.CopyValues could not save")
		}
		
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
			test_instance.taskAircraft = crew_instance.aircraft
			test_instance.viewpos = i
			test_instance.task = this
			test_instance.timeCalculated = false
			test_instance.save()
		}
	}
	
	Map CopyValues2(Task taskInstance)
	{
		Map taskclass_settings = [:]
		
		//title = taskInstance.title
	    //idTitle = taskInstance.idTitle
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
		takeoffIntervalSlowerAircraft = taskInstance.takeoffIntervalSlowerAircraft
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
		flightTestCheckSecretPoints = taskInstance.flightTestCheckSecretPoints
		flightTestCheckTakeOff = taskInstance.flightTestCheckTakeOff
		flightTestCheckLanding = taskInstance.flightTestCheckLanding
		bestOfAnalysis = taskInstance.bestOfAnalysis

		if (taskInstance.contest.resultClasses) {
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(taskInstance.contest,[sort:"id"])) {
				TaskClass.findAllByTask(taskInstance,[sort:"id"]).each { TaskClass taskclass_instance ->
					if (taskclass_instance.resultclass == resultclass_instance) {
						taskclass_settings["taskclass_${resultclass_instance.id}_planningTestRun"] = taskclass_instance.planningTestRun
						taskclass_settings["taskclass_${resultclass_instance.id}_flightTestRun"] = taskclass_instance.flightTestRun
						taskclass_settings["taskclass_${resultclass_instance.id}_observationTestRun"] = taskclass_instance.observationTestRun
						taskclass_settings["taskclass_${resultclass_instance.id}_landingTestRun"] = taskclass_instance.landingTestRun
						taskclass_settings["taskclass_${resultclass_instance.id}_landingTest1Run"] = taskclass_instance.landingTest1Run
						taskclass_settings["taskclass_${resultclass_instance.id}_landingTest2Run"] = taskclass_instance.landingTest2Run
						taskclass_settings["taskclass_${resultclass_instance.id}_landingTest3Run"] = taskclass_instance.landingTest3Run
						taskclass_settings["taskclass_${resultclass_instance.id}_landingTest4Run"] = taskclass_instance.landingTest4Run
						taskclass_settings["taskclass_${resultclass_instance.id}_specialTestRun"] = taskclass_instance.specialTestRun
						taskclass_settings["taskclass_${resultclass_instance.id}_planningTestDistanceMeasure"] = taskclass_instance.planningTestDistanceMeasure
						taskclass_settings["taskclass_${resultclass_instance.id}_planningTestDirectionMeasure"] = taskclass_instance.planningTestDirectionMeasure
						taskclass_settings["taskclass_${resultclass_instance.id}_flightTestCheckSecretPoints"] = taskclass_instance.flightTestCheckSecretPoints
						taskclass_settings["taskclass_${resultclass_instance.id}_flightTestCheckTakeOff"] = taskclass_instance.flightTestCheckTakeOff
						taskclass_settings["taskclass_${resultclass_instance.id}_flightTestCheckLanding"] = taskclass_instance.flightTestCheckLanding
					}
				}
			}
		}
		return taskclass_settings
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
	
	String bestOfName()
	{
		if ((contest.bestOfAnalysisTaskNum > 0) && bestOfAnalysis) {
			return "${name()} [${contest.bestOfAnalysisTaskNum}]"
		} else {
			return name()
		}
	}
	
	int GetTimeTableVersion()
	{
		if (timetableModified) {
			return timetableVersion + 1
		}
		return timetableVersion
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
	
	boolean IsFlightTestCheckSecretPoints()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.flightTestCheckSecretPoints) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return flightTestCheckSecretPoints
	}
	
	boolean IsFlightTestCheckTakeOff()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.flightTestCheckTakeOff) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return flightTestCheckTakeOff
	}
	
	boolean IsFlightTestCheckLanding()
	{
		if (contest.resultClasses) {
			for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
				if (test_instance.crew.resultclass) {
					for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
						
						if (test_instance.crew.resultclass == taskclass_instance.resultclass)
						{
							if (taskclass_instance.flightTestCheckLanding) {
								return true
							}
						}
					}
				}
			}
			return false
		}
		return flightTestCheckLanding
	}
	
	boolean IsTaskResultsProvisional(Map resultSettings)
	{
		if (printProvisionalResults) {
			return true
		}
		for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
			if (test_instance.IsTestResultsProvisional(resultSettings)) {
				return true
			}
		}
		return false
	}
	
	boolean IsTaskClassResultsProvisional(Map resultSettings, ResultClass resultclassInstance)
	{
		if (printProvisionalResults) {
			return true
		}
		for (Test test_instance in Test.findAllByTask(this,[sort:"id"])) {
			if (test_instance.crew.resultclass == resultclassInstance) {
				if (test_instance.IsTestClassResultsProvisional(resultSettings,resultclassInstance)) {
					return true
				}
			}
		}
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
