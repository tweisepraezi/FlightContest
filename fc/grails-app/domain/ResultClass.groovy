import java.util.Map;

class ResultClass
// DB-2.0
{
	String name
	String contestTitle = ""
	ContestRules contestRule                 // Wettbewerbsordnung
	
	// Klassen-Auswertung
	boolean contestPlanningResults = true    // Planungstest berüchsichtigen
	boolean contestFlightResults = true      // Navigationstest berüchsichtigen
	boolean contestObservationResults = true // Beobachtungstest berüchsichtigen
	boolean contestLandingResults = true     // Landetest berüchsichtigen
	boolean contestSpecialResults = false    // Sondertest berücksichtigen

	// PlanningTest
	int planningTestDirectionCorrectGrad = 2
	int planningTestDirectionPointsPerGrad = 2
	int planningTestTimeCorrectSecond = 5
	int planningTestTimePointsPerSecond = 1
	int planningTestMaxPoints = 350
	int planningTestPlanTooLatePoints = 50
	int planningTestExitRoomTooLatePoints = 100

	// FlightTest
	int flightTestTakeoffMissedPoints = 200
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
	
	// LandingTest
	int landingTest1MaxPoints = 300
	int landingTest1NoLandingPoints = 300
	int landingTest1OutsideLandingPoints = 200
	int landingTest1RollingOutsidePoints = 200
	int landingTest1PowerInBoxPoints = 50
	int landingTest1GoAroundWithoutTouchingPoints = 200
	int landingTest1GoAroundInsteadStopPoints = 200
	int landingTest1AbnormalLandingPoints = 150
	String landingTest1PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}"
	
	int landingTest2MaxPoints = 300
	int landingTest2NoLandingPoints = 300
	int landingTest2OutsideLandingPoints = 200
	int landingTest2RollingOutsidePoints = 200
	int landingTest2PowerInBoxPoints = 50
	int landingTest2GoAroundWithoutTouchingPoints = 200
	int landingTest2GoAroundInsteadStopPoints = 200
	int landingTest2AbnormalLandingPoints = 150
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
	int landingTest4TouchingObstaclePoints = 400
	String landingTest4PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}"
	
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
	
	String GetListTitle(String msgID)
	{
		if (contestTitle) {
			return "${getMsg(msgID)} - ${contestTitle}" 
		}
		return "${getMsg(msgID)} - ${name}"
	}
	
	boolean IsPlanningTestRun()
	{
		for (Task task_instance in Task.findAllByContest(contest)) {
			for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance)) {
				if (taskclass_instance.resultclass == this) {
					if (taskclass_instance.planningTestRun) {
						return true
					}
				}
			}
		}
		return false
	}
	
	boolean IsFlightTestRun()
	{
		for (Task task_instance in Task.findAllByContest(contest)) {
			for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance)) {
				if (taskclass_instance.resultclass == this) {
					if (taskclass_instance.flightTestRun) {
						return true
					}
				}
			}
		}
		return false
	}
	
	boolean IsObservationTestRun()
	{
		for (Task task_instance in Task.findAllByContest(contest)) {
			for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance)) {
				if (taskclass_instance.resultclass == this) {
					if (taskclass_instance.observationTestRun) {
						return true
					}
				}
			}
		}
		return false
	}
	
	boolean IsLandingTestRun()
	{
		for (Task task_instance in Task.findAllByContest(contest)) {
			for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance)) {
				if (taskclass_instance.resultclass == this) {
					if (taskclass_instance.landingTestRun) {
						return true
					}
				}
			}
		}
		return false
	}
	
	boolean IsSpecialTestRun()
	{
		for (Task task_instance in Task.findAllByContest(contest)) {
			for (TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance)) {
				if (taskclass_instance.resultclass == this) {
					if (taskclass_instance.specialTestRun) {
						return true
					}
				}
			}
		}
		return false
	}
	
	Map GetTeamResultSettings()
	{
		Map ret = [:]
		for (Task task_instance in Task.findAllByContest(contest)) {
			for(TaskClass taskclass_instance in TaskClass.findAllByTask(task_instance)) {
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
		contestTitle = resultClassInstance.contestTitle
		contestRule = resultClassInstance.contestRule
		
		planningTestDirectionCorrectGrad = resultClassInstance.planningTestDirectionCorrectGrad 
		planningTestDirectionPointsPerGrad = resultClassInstance.planningTestDirectionPointsPerGrad
		planningTestTimeCorrectSecond = resultClassInstance.planningTestTimeCorrectSecond
		planningTestTimePointsPerSecond = resultClassInstance.planningTestTimePointsPerSecond
		planningTestMaxPoints = resultClassInstance.planningTestMaxPoints
		planningTestPlanTooLatePoints = resultClassInstance.planningTestPlanTooLatePoints
		planningTestExitRoomTooLatePoints = resultClassInstance.planningTestExitRoomTooLatePoints
		
		flightTestTakeoffMissedPoints = resultClassInstance.flightTestTakeoffMissedPoints
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
		
		landingTest1MaxPoints = resultClassInstance.landingTest1MaxPoints
		landingTest1NoLandingPoints = resultClassInstance.landingTest1NoLandingPoints
		landingTest1OutsideLandingPoints = resultClassInstance.landingTest1OutsideLandingPoints
		landingTest1RollingOutsidePoints = resultClassInstance.landingTest1RollingOutsidePoints
		landingTest1PowerInBoxPoints = resultClassInstance.landingTest1PowerInBoxPoints
		landingTest1GoAroundWithoutTouchingPoints = resultClassInstance.landingTest1GoAroundWithoutTouchingPoints
		landingTest1GoAroundInsteadStopPoints = resultClassInstance.landingTest1GoAroundInsteadStopPoints
		landingTest1AbnormalLandingPoints = resultClassInstance.landingTest1AbnormalLandingPoints
		landingTest1PenaltyCalculator = resultClassInstance.landingTest1PenaltyCalculator
		
		landingTest2MaxPoints = resultClassInstance.landingTest2MaxPoints
		landingTest2NoLandingPoints = resultClassInstance.landingTest2NoLandingPoints
		landingTest2OutsideLandingPoints = resultClassInstance.landingTest2OutsideLandingPoints
		landingTest2RollingOutsidePoints = resultClassInstance.landingTest2RollingOutsidePoints
		landingTest2PowerInBoxPoints = resultClassInstance.landingTest2PowerInBoxPoints
		landingTest2GoAroundWithoutTouchingPoints = resultClassInstance.landingTest2GoAroundWithoutTouchingPoints
		landingTest2GoAroundInsteadStopPoints = resultClassInstance.landingTest2GoAroundInsteadStopPoints
		landingTest2AbnormalLandingPoints = resultClassInstance.landingTest2AbnormalLandingPoints
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
		landingTest4TouchingObstaclePoints = resultClassInstance.landingTest4TouchingObstaclePoints
		landingTest4PenaltyCalculator = resultClassInstance.landingTest4PenaltyCalculator
		
		this.save()
	}
	
	boolean AreClassResultsProvisional(Map resultSettings)
	{
		//println "XX ResultClass.AreClassResultsProvisional $name $resultSettings"
	    for (Crew crew_instance in Crew.findAllByContestAndDisabled(contest,false,[sort:'contestPosition'])) {
	    	if (crew_instance.resultclass == this) {
                for (Task task_instance in Task.findAllByContest(contest)) {
                	Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
                	if (test_instance.AreClassResultsProvisional(resultSettings,this)) {
						//println "-> true (ResultClass.AreClassResultsProvisional $task_instance.title $test_instance.crew.name)"
						return true
					}
                }
	    	}
	    }
		//println "-> false (ResultClass.AreClassResultsProvisional)"
		return false
	}
	
	Map GetClassResultSettings()
	{
		Map ret = [:]
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
	
}
