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
	boolean specialTestRun               = false
	
	String firstTime                     = "11:00" // Local time of first planning test [hh:mm]
	int takeoffIntervalNormal            = 3       // space between takeoff [min]
	int takeoffIntervalFasterAircraft    = 120     // space between takeoff to faster aircraft [min]
	
	int planningTestDuration             = 60      // duration of planning test [min]
	int preparationDuration              = 15      // duration of aircraft preparation [min]
	int risingDuration                   = 10      // duration from takeoff to start point [min]
    int maxLandingDuration               = 10      // duration from finish point to landing [min]
    int parkingDuration                  = 15      // duration from finish point to aircraft parking [min] (> maxLandingDuration)
	int minNextFlightDuration            = 30      // duration of aircraft maintenance between two flights [min]
	int procedureTurnDuration            = 1       // duration of Procedure Turn [min]

	int addTimeValue                     = 3       // add/subtract time value [min]  

    boolean planningTestDistanceMeasure  = false
    boolean planningTestDirectionMeasure = true
	
	boolean timetableModified            = true
	int timetableVersion                 = 0
	
	String disabledCheckPoints           = ""      // list of disabled check point titles, separated with ',', DB-1.1
	
    static belongsTo = [contest:Contest]
    
	static hasMany = [tests:Test]
	
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
		planningTestDistanceMeasure()
		planningTestDirectionMeasure()
		
		planningtest(nullable:true)
		flighttest(nullable:true)
		contest(nullable:false)
	}

    static mapping = {
		tests sort:"id"
	}
	
	void CopyValues(Task taskInstance)
	{
		title = taskInstance.title
	    idTitle = taskInstance.idTitle
		planningTestRun = taskInstance.planningTestRun
		flightTestRun = taskInstance.flightTestRun
		observationTestRun = taskInstance.observationTestRun
		landingTestRun = taskInstance.landingTestRun
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
}
