import java.util.List;
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
    Boolean observationTestTurnpointRun      = true  // DB-2.13
    Boolean observationTestEnroutePhotoRun   = true  // DB-2.13
    Boolean observationTestEnrouteCanvasRun  = false // DB-2.13
	boolean landingTestRun                   = true
	boolean landingTest1Run                  = false // DB-2.0
	boolean landingTest2Run                  = false // DB-2.0
	boolean landingTest3Run                  = false // DB-2.0
	boolean landingTest4Run                  = false // DB-2.0
    Integer landingTest1Points               = 1     // DB-2.9
    Integer landingTest2Points               = 2     // DB-2.9
    Integer landingTest3Points               = 3     // DB-2.9
    Integer landingTest4Points               = 4     // DB-2.9
	boolean specialTestRun                   = false
	String specialTestTitle                  = ""    // DB-2.3

	String firstTime                         = "11:00" // Local time of first planning test [hh:mm]
	int takeoffIntervalNormal                = 3     // space between takeoff [min]
	Integer takeoffIntervalSlowerAircraft    = 3     // DB-2.4, space between takeoff to slower aircraft [min]
	int takeoffIntervalFasterAircraft        = 30    // space between takeoff to faster aircraft [min]
	
	int planningTestDuration                 = 60    // duration of planning test [min]
	int preparationDuration                  = 15    // duration of aircraft preparation [min]
	int risingDuration                       = 5     // duration from takeoff to start point [min]
	String risingDurationFormula             = "wind+:3NM" // DB-2.7
    int maxLandingDuration                   = 5     // duration from finish point to landing [min]
	String maxLandingDurationFormula         = "wind+:6NM" // DB-2.7
    int parkingDuration                      = 5     // change DB-2.7: duration of aircraft parking after landing [min]
	                                                 // before DB-2.7: duration from finish point to aircraft parking [min] (> maxLandingDuration), 10 min
	String iLandingDurationFormula           = "wind+:2NM" // DB-2.7
	String iRisingDurationFormula            = "wind+:3NM" // DB-2.7
	
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
	
	String disabledCheckPoints               = ""    // list of check point without time check
    String disabledCheckPointsNotFound       = ""    // list of check point without not found check, DB-2.8
    String disabledCheckPointsMinAltitude    = ""    // list of check point without min altitude check, DB-2.8
    String disabledCheckPointsProcedureTurn  = ""    // list of check point without procedure turn check, DB-2.8
    String disabledCheckPointsBadCourse      = ""    // list of check point without bad course check, DB-2.8
    String disabledCheckPointsTurnpointObs   = ""    // list of check point without turnpoint observation check, DB-2.13
    String disabledEnroutePhotoObs           = ""    // list of enroute photos without enroute observation check, DB-2.13
    String disabledEnrouteCanvasObs          = ""    // list of enroute canvas without enroute observation check, DB-2.13
    
	Boolean bestOfAnalysis                   = false // DB-2.3
    Boolean increaseEnabled                  = true  // DB-2.13
    
	String printTimetableJuryPrintTitle      = ""    // DB-2.3
	Boolean printTimetableJuryNumber         = true  // DB-2.3
	Boolean printTimetableJuryCrew           = true  // DB-2.3
	Boolean printTimetableJuryAircraft       = true  // DB-2.3
	Boolean printTimetableJuryAircraftType   = true  // DB-2.3
	Boolean printTimetableJuryAircraftColour = false // DB-2.3
	Boolean printTimetableJuryTAS            = false // DB-2.3
	Boolean printTimetableJuryTeam           = false // DB-2.3
	Boolean printTimetableJuryClass          = false // DB-2.8
	Boolean printTimetableJuryShortClass     = true  // DB-2.8
	Boolean printTimetableJuryPlanning       = true  // DB-2.3
	Boolean printTimetableJuryPlanningEnd    = true  // DB-2.3
	Boolean printTimetableJuryTakeoff        = true  // DB-2.3
	Boolean printTimetableJuryStartPoint     = false // DB-2.3
	String  printTimetableJuryCheckPoints    = ""    // DB-2.3
	Boolean printTimetableJuryFinishPoint    = false // DB-2.3
	Boolean printTimetableJuryLanding        = true  // DB-2.3
	Boolean printTimetableJuryArrival        = true  // DB-2.3
	Boolean printTimetableJuryEmptyColumn1   = true  // DB-2.3
	String printTimetableJuryEmptyTitle1     = ""    // DB-2.3
	Boolean printTimetableJuryEmptyColumn2   = false // DB-2.3
	String printTimetableJuryEmptyTitle2     = ""    // DB-2.3
	Boolean printTimetableJuryEmptyColumn3   = false // DB-2.3
	String printTimetableJuryEmptyTitle3     = ""    // DB-2.3
	Boolean printTimetableJuryEmptyColumn4   = false // DB-2.8
	String printTimetableJuryEmptyTitle4     = ""    // DB-2.8
	Boolean printTimetableJuryLandscape      = true  // DB-2.3
	Boolean printTimetableJuryA3             = false // DB-2.3
	
	String printTimetablePrintTitle          = ""    // DB-2.3
	Boolean printTimetableNumber             = true  // DB-2.3
	Boolean printTimetableCrew               = true  // DB-2.3
	Boolean printTimetableAircraft           = true  // DB-2.3
	Boolean printTimetableTAS                = true  // DB-2.3
	Boolean printTimetableTeam               = false // DB-2.3
	Boolean printTimetableClass              = false // DB-2.8
	Boolean printTimetableShortClass         = true  // DB-2.8
	Boolean printTimetablePlanning           = true  // DB-2.3
	Boolean printTimetableTakeoff            = true  // DB-2.3
	Boolean printTimetableVersion            = true  // DB-2.3
	String printTimetableChange              = ""    // DB-2.3
    Boolean printTimetableLegTimes           = false // DB-2.8
	Boolean printTimetableLandscape          = false // DB-2.3
	Boolean printTimetableA3                 = false // DB-2.3
	
    String briefingTime                      = "10:00" // DB-2.8
    Boolean printTimetableOverviewLegTimes   = true  // DB-2.8
    Boolean printTimetableOverviewLandscape  = false // DB-2.8
    Boolean printTimetableOverviewA3         = false // DB-2.8
    
	Boolean hidePlanning                     = false // DB-2.3
	Boolean hideResults                      = false // DB-2.3
    
    String reserve                           = ""    // DB-2.12
	
	// transient values 
	static transients = ['printSummaryResults','printPlanningResults','printPlanningResultsScan',
                         'printFlightResults','printFlightMap',
                         'printObservationResults','printObservationResultsScan',
                         'printLandingResults','printSpecialResults','printAircraft','printTeam','printClass','printShortClass',
                         'printModifiedResults','printCompletedResults','printProvisionalResults']
    boolean printSummaryResults = true
	boolean printPlanningResults = true
    boolean printPlanningResultsScan = true
	boolean printFlightResults = true
    boolean printFlightMap = true
	boolean printObservationResults = true
    boolean printObservationResultsScan = true
	boolean printLandingResults = true
	boolean printSpecialResults = true
	boolean printAircraft = true
	boolean printTeam = false
	boolean printClass = false
	boolean printShortClass = false
    boolean printModifiedResults = true
    boolean printCompletedResults = true
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
		parkingDuration(range:0..240)
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

		// DB-2.7 compatibility
		risingDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
		maxLandingDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
		iLandingDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
		iRisingDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
		
		// DB-2.8 compatibility
		printTimetableClass(nullable:true)
		printTimetableShortClass(nullable:true)
		printTimetableJuryClass(nullable:true)
		printTimetableJuryShortClass(nullable:true)
		printTimetableJuryEmptyColumn4(nullable:true)
		printTimetableJuryEmptyTitle4(nullable:true)
        disabledCheckPointsNotFound(nullable:true)
        disabledCheckPointsMinAltitude(nullable:true)
        disabledCheckPointsProcedureTurn(nullable:true)
        disabledCheckPointsBadCourse(nullable:true)
        printTimetableLegTimes(nullable:true)
        briefingTime(nullable:true,blank:true, validator:{ val, obj ->
            if (!val) {
                return true
            }
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
        printTimetableOverviewLegTimes(nullable:true)
        printTimetableOverviewLandscape(nullable:true)
        printTimetableOverviewA3(nullable:true)
        
        // DB-2.9 compatibility
        landingTest1Points(nullable:true)
        landingTest2Points(nullable:true)
        landingTest3Points(nullable:true)
        landingTest4Points(nullable:true)
        
        // DB-2.12 compatibility
        reserve(nullable:true)
        
        // DB-2.13 compatibility
        increaseEnabled(nullable:true)
        disabledCheckPointsTurnpointObs(nullable:true)
        disabledEnroutePhotoObs(nullable:true)
        disabledEnrouteCanvasObs(nullable:true)
        observationTestTurnpointRun(nullable:true)
        observationTestEnroutePhotoRun(nullable:true)
        observationTestEnrouteCanvasRun(nullable:true)
	}

    static mapping = {
		tests sort:"id"
		taskclasses sort:"id"
	}
	
	static boolean DurationValid(val, obj)
	{
		if (val) {
			if (val.startsWith('time+:')) {
				if (val.endsWith('min')) {
					String f = val.substring(6,val.size()-3).replace(',','.')
					if (f.isInteger()) {
						return true
					}
				}
			} else if (val.startsWith('time:')) {
				if (val.endsWith('min')) {
					String f = val.substring(5,val.size()-3).replace(',','.')
					if (f.isInteger()) {
						return true
					}
				}
			} else if (val.startsWith('wind+:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(6,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(6,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('wind:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(5,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(5,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('nowind+:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(8,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(8,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('nowind:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(7,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(7,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('func+:')) {
				String f = val.substring(6,val.size())
				if (f) {
					return true
				}
			} else if (val.startsWith('func:')) {
				String f = val.substring(5,val.size())
				if (f) {
					return true
				}
			}
		}
		return false
	}
	
	void CopyValues(Task taskInstance)
	{
		title = taskInstance.title
	    idTitle = taskInstance.idTitle
		planningTestRun = taskInstance.planningTestRun
		flightTestRun = taskInstance.flightTestRun
		observationTestRun = taskInstance.observationTestRun
        observationTestTurnpointRun = taskInstance.observationTestTurnpointRun
        observationTestEnroutePhotoRun = taskInstance.observationTestEnroutePhotoRun
        observationTestEnrouteCanvasRun = taskInstance.observationTestEnrouteCanvasRun
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
		risingDurationFormula = taskInstance.risingDurationFormula
	    maxLandingDuration = taskInstance.maxLandingDuration
	    maxLandingDurationFormula = taskInstance.maxLandingDurationFormula
	    parkingDuration = taskInstance.parkingDuration
		iLandingDurationFormula = taskInstance.iLandingDurationFormula
		iRisingDurationFormula = taskInstance.iRisingDurationFormula
		minNextFlightDuration = taskInstance.minNextFlightDuration
		procedureTurnDuration = taskInstance.procedureTurnDuration
		addTimeValue = taskInstance.addTimeValue  
	    planningTestDistanceMeasure = taskInstance.planningTestDistanceMeasure
	    planningTestDirectionMeasure = taskInstance.planningTestDirectionMeasure
		flightTestCheckSecretPoints = taskInstance.flightTestCheckSecretPoints
		flightTestCheckTakeOff = taskInstance.flightTestCheckTakeOff
		flightTestCheckLanding = taskInstance.flightTestCheckLanding
		bestOfAnalysis = taskInstance.bestOfAnalysis
        increaseEnabled = taskInstance.increaseEnabled

		if (!this.save()) {
			throw new Exception("Task.CopyValues could not save")
		}
		
		// taskclasses:TaskClass
		TaskClass.findAllByTask(taskInstance,[sort:"id"]).each { TaskClass taskclass_instance ->
			ResultClass resultclass_instance = ResultClass.findByNameAndContest(taskclass_instance.resultclass.name,contest)
			if (resultclass_instance) {
				TaskClass new_taskclass_instance = new TaskClass()
				new_taskclass_instance.task = this
				new_taskclass_instance.resultclass = resultclass_instance
				new_taskclass_instance.CopyValues(taskclass_instance)
				new_taskclass_instance.save()
			}
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
        observationTestTurnpointRun = taskInstance.observationTestTurnpointRun
        observationTestEnroutePhotoRun = taskInstance.observationTestEnroutePhotoRun
        observationTestEnrouteCanvasRun = taskInstance.observationTestEnrouteCanvasRun
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
		risingDurationFormula = taskInstance.risingDurationFormula
	    maxLandingDuration = taskInstance.maxLandingDuration
	    maxLandingDurationFormula = taskInstance.maxLandingDurationFormula
	    parkingDuration = taskInstance.parkingDuration
		iLandingDurationFormula = taskInstance.iLandingDurationFormula
		iRisingDurationFormula = taskInstance.iRisingDurationFormula
		minNextFlightDuration = taskInstance.minNextFlightDuration
		procedureTurnDuration = taskInstance.procedureTurnDuration
		addTimeValue = taskInstance.addTimeValue  
	    planningTestDistanceMeasure = taskInstance.planningTestDistanceMeasure
	    planningTestDirectionMeasure = taskInstance.planningTestDirectionMeasure
		flightTestCheckSecretPoints = taskInstance.flightTestCheckSecretPoints
		flightTestCheckTakeOff = taskInstance.flightTestCheckTakeOff
		flightTestCheckLanding = taskInstance.flightTestCheckLanding
		bestOfAnalysis = taskInstance.bestOfAnalysis
        increaseEnabled = taskInstance.increaseEnabled

		if (taskInstance.contest.resultClasses) {
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(taskInstance.contest,[sort:"id"])) {
				TaskClass.findAllByTask(taskInstance,[sort:"id"]).each { TaskClass taskclass_instance ->
					if (taskclass_instance.resultclass == resultclass_instance) {
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestRun}"] = taskclass_instance.planningTestRun
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestRun}"] = taskclass_instance.flightTestRun
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestRun}"] = taskclass_instance.observationTestRun
                        taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestTurnpointRun}"] = taskclass_instance.observationTestTurnpointRun
                        taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestEnroutePhotoRun}"] = taskclass_instance.observationTestEnroutePhotoRun
                        taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestEnrouteCanvasRun}"] = taskclass_instance.observationTestEnrouteCanvasRun
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTestRun}"] = taskclass_instance.landingTestRun
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest1Run}"] = taskclass_instance.landingTest1Run
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest2Run}"] = taskclass_instance.landingTest2Run
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest3Run}"] = taskclass_instance.landingTest3Run
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest4Run}"] = taskclass_instance.landingTest4Run
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_SpecialTestRun}"] = taskclass_instance.specialTestRun
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestDistanceMeasure}"] = taskclass_instance.planningTestDistanceMeasure
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestDirectionMeasure}"] = taskclass_instance.planningTestDirectionMeasure
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckSecretPoints}"] = taskclass_instance.flightTestCheckSecretPoints
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckTakeOff}"] = taskclass_instance.flightTestCheckTakeOff
						taskclass_settings["${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckLanding}"] = taskclass_instance.flightTestCheckLanding
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
    
    String idNamePrintable()
    {
        return "${getPrintMsg('fc.task')}-${idTitle}"
    }
    
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
	
    String printName()
    {
        if(title) {
            return title
        } else {
            return idNamePrintable()
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
	
    String bestOfNamePrintable()
    {
        if ((contest.bestOfAnalysisTaskNum > 0) && bestOfAnalysis) {
            return "${name()} [${contest.bestOfAnalysisTaskNum}]"
        } else {
            return printName()
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
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.planningTestRun) {
                    return true
                }
            }
            return false
		}
		return planningTestRun
	}
	
	boolean IsFlightTestRun()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.flightTestRun) {
                    return true
                }
            }
            return false
        }
		return flightTestRun
	}
	
	boolean IsObservationTestRun()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.observationTestRun) {
                    return true
                }
            }
            return false
        }
		return observationTestRun
	}
	
    boolean IsObservationTestAnyRun()
    {
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.observationTestTurnpointRun || taskclass_instance.observationTestEnroutePhotoRun || taskclass_instance.observationTestEnrouteCanvasRun) {
                    return true
                }
            }
            return false
        }
        return observationTestTurnpointRun || observationTestEnroutePhotoRun || observationTestEnrouteCanvasRun
    }
    
    boolean IsObservationTestTurnpointRun()
    {
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.observationTestTurnpointRun) {
                    return true
                }
            }
            return false
        }
        return observationTestTurnpointRun
    }
    
    boolean IsObservationTestEnroutePhotoRun()
    {
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.observationTestEnroutePhotoRun) {
                    return true
                }
            }
            return false
        }
        return observationTestEnroutePhotoRun
    }
    
    boolean IsObservationTestEnrouteCanvasRun()
    {
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.observationTestEnrouteCanvasRun) {
                    return true
                }
            }
            return false
        }
        return observationTestEnrouteCanvasRun
    }
    
	boolean IsLandingTestRun()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.landingTestRun) {
                    return true
                }
            }
            return false
        }
		return landingTestRun
	}
	
	boolean IsLandingTestAnyRun()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.landingTest1Run || taskclass_instance.landingTest2Run || taskclass_instance.landingTest3Run || taskclass_instance.landingTest4Run) {
                    return true
                }
            }
            return false
        }
		return landingTest1Run || landingTest2Run || landingTest3Run || landingTest4Run
	}
	
	boolean IsLandingTest1Run()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.landingTest1Run) {
                    return true
                }
            }
            return false
        }
		return landingTest1Run
	}
	
	boolean IsLandingTest2Run()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.landingTest2Run) {
                    return true
                }
            }
            return false
        }
		return landingTest2Run
	}
	
	boolean IsLandingTest3Run()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.landingTest3Run) {
                    return true
                }
            }
            return false
        }
		return landingTest3Run
	}
	
	boolean IsLandingTest4Run()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.landingTest4Run) {
                    return true
                }
            }
            return false
        }
		return landingTest4Run
	}
	
	boolean IsSpecialTestRun()
	{
        if (contest.resultClasses) {
            for (TaskClass taskclass_instance in TaskClass.findAllByTask(this,[sort:"id"])) {
                if (taskclass_instance.specialTestRun) {
                    return true
                }
            }
            return false
        }
		return specialTestRun
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
            int observation_result_any_columns = GetObservationResultAnyColumns()
            if (observation_result_any_columns > 0) {
                result_columns += observation_result_any_columns
            } else {
                result_columns++
            }
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

	boolean IsFullMinute(String durationValue)
	{
		return durationValue.contains('+:')
	}
    
    Test GetFirstTest()
    {
        for (Test test_instance in Test.findAllByTask(this,[sort:"viewpos",order:"asc"])) {
            if (test_instance.timeCalculated && !test_instance.disabledCrew && !test_instance.crew.disabled) {
                return test_instance
            }
        }
        return null
    }

    Test GetFirstTestBefore()
    {
        for (Test test_instance in Test.findAllByTask(this,[sort:"viewpos",order:"asc"])) {
            if (test_instance.timeCalculated && !test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.GetMinutesBeforeStartTime()) {
                    return test_instance
                }
            }
        }
        return null
    }

    Test GetLastTest()
    {
        for (Test test_instance in Test.findAllByTask(this,[sort:"viewpos",order:"desc"])) {
            if (test_instance.timeCalculated && !test_instance.disabledCrew && !test_instance.crew.disabled) {
                return test_instance
            }
        }
        return null
    }
    
    String GetLandingPointsText(int landingTestPoints)
    {
        if (!contest.precisionFlying || (contest.resultClasses && contest.contestRuleForEachClass)) {
            switch (landingTestPoints) {
                case 1: return "fc.landingtest.points1"
                case 2: return "fc.landingtest.points2"
                case 3: return "fc.landingtest.points3"
                case 4: return "fc.landingtest.points4"
            }
        } else {
            switch (landingTestPoints) {
                case 1: return "fc.landingtest.points1.precision"
                case 2: return "fc.landingtest.points2.precision"
                case 3: return "fc.landingtest.points3.precision"
                case 4: return "fc.landingtest.points4.precision"
            }
        }
        return ""
    }
    
    boolean IsEMailPossible()
    {
        if (   BootStrap.global.IsEMailPossible() 
            && BootStrap.global.IsFTPPossible()
           )
        {
            for (Test test_instance in Test.findAllByTask(this,[sort:"viewpos",order:"desc"])) {
                if (test_instance.IsEMailPossible()) {
                    return true
                }
            }
        }
        return false
    }
    
    String GetIncreaseValues()
    {
        return contest.GetIncreaseValues()
    }
    
    boolean IsIncreaseEnabled()
    {
        if (GetIncreaseValues()) {
            return increaseEnabled
        }
        return false
    }
    
    List GetObservationResultClassIDs(boolean checkPenalties)
    {
        boolean taskclasses_different = false
        List result_class_ids = []
        if (contest.resultClasses) {
            TaskClass first_taskclass_instance = null
            ResultClass first_resultclass_instance = null
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(contest,[sort:"id"])) {
                TaskClass taskclass_instance = TaskClass.findByTaskAndResultclassAndObservationTestRun(this,resultclass_instance,true,[sort:"id"])
                if (taskclass_instance) {
                    if (first_taskclass_instance) {
                        if ((first_taskclass_instance.observationTestTurnpointRun != taskclass_instance.observationTestTurnpointRun) ||
                            (first_taskclass_instance.observationTestEnroutePhotoRun != taskclass_instance.observationTestEnroutePhotoRun) ||
                            (first_taskclass_instance.observationTestEnrouteCanvasRun != taskclass_instance.observationTestEnrouteCanvasRun))
                        {
                            taskclasses_different = true
                            result_class_ids += resultclass_instance.id
                        } else if (contest.contestRuleForEachClass && checkPenalties) {
                            if ((first_resultclass_instance.observationTestEnrouteValueUnit != resultclass_instance.observationTestEnrouteValueUnit) ||
                                (first_resultclass_instance.observationTestEnrouteCorrectValue != resultclass_instance.observationTestEnrouteCorrectValue) ||
                                (first_resultclass_instance.observationTestEnrouteInexactValue != resultclass_instance.observationTestEnrouteInexactValue))
                            {
                                taskclasses_different = true
                                result_class_ids += resultclass_instance.id
                            }
                        } 
                    } else {
                        first_taskclass_instance = taskclass_instance
                        first_resultclass_instance = resultclass_instance
                        result_class_ids += resultclass_instance.id
                    }
                }
            }
        }
        if (taskclasses_different) {
            return result_class_ids
        }
        return []
    }
    
    List GetPlanningTests()
    {
        List test_instances = []
        for (Test test_instance in Test.findAllByTaskAndScannedPlanningTestAndPlanningTestCompleteAndPlanningtesttaskIsNotNull(this,null,false,[sort:"viewpos"])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.IsPlanningTestRun()) {
                    test_instances += test_instance
                }
            }
        }
        return test_instances
    }
    
    List GetFlightTests()
    {
        List test_instances = []
        for (Test test_instance in Test.findAllByTaskAndFlightTestComplete(this,false,[sort:"viewpos"])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.IsFlightTestRun()) {
                    test_instances += test_instance
                }
            }
        }
        return test_instances
    }

    List GetObservationTests()
    {
        List test_instances = []
        for (Test test_instance in Test.findAllByTaskAndScannedObservationTestAndObservationTestComplete(this,null,false,[sort:"viewpos"])) {
            if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                if (test_instance.IsObservationTestRun()) {
                    test_instances += test_instance
                }
            }
        }
        return test_instances
    }

}
