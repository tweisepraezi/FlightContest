import java.util.Map;

class Contest 
{
	String title = "Wettbewerbsname"
	int mapScale = 200000
    String timeZone = "02:00"                           // Difference between UTC and local time [hh:mm]
	boolean resultClasses = false          	            // Klassen, DB-2.0
	ContestRules contestRule                            // Wettbewerbsordnung, DB-2.0
	boolean testExists = false                          // Integrierter Test vorhanden, DB-2.0
	boolean aflosTest = false                           // Nutzung der AFLOS-Test-Datenbank, DB-2.0
	boolean aflosUpload = false                         // Nutzung einer geuploadeten AFLO-Datenbank,DB-2.0 
	
	// Wettbewerbs-Auswertung
	boolean contestPlanningResults = true               // Planungstest berüchsichtigen, DB-2.0
	boolean contestFlightResults = true                 // Navigationstest berüchsichtigen, DB-2.0
	boolean contestObservationResults = true            // Beobachtungstest berüchsichtigen, DB-2.0
	boolean contestLandingResults = true                // Landetest berüchsichtigen, DB-2.0
	boolean contestSpecialResults = false               // Sondertest berücksichtigen, DB-2.0

	// Team-Auswertung
	int teamCrewNum = 0                    	            // Anzahl von Besatzungen, DB-2.0
	String teamClassResults = ""		                // Zu berücksichtigende Klassen, DB-2.0
	boolean teamPlanningResults = true     	            // Planungstest berüchsichtigen, DB-2.0
	boolean teamFlightResults = true       	            // Navigationstest berüchsichtigen, DB-2.0
	boolean teamObservationResults = true  	            // Beobachtungstest berüchsichtigen, DB-2.0
	boolean teamLandingResults = true      	            // Landetest berüchsichtigen, DB-2.0
	boolean teamSpecialResults = false     	            // Sondertest berücksichtigen, DB-2.0
	int teamContestTitle = 0               	            // Wettbewerbstitel beim Ausdruck, DB-2.0
	
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
	int landingTest1MaxPoints = 300                     // DB-2.0
	int landingTest1NoLandingPoints = 300               // DB-2.0
	int landingTest1OutsideLandingPoints = 200          // DB-2.0
	int landingTest1RollingOutsidePoints = 200          // DB-2.0
	int landingTest1PowerInBoxPoints = 50               // DB-2.0
	int landingTest1GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest1GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest1AbnormalLandingPoints = 150         // DB-2.0
	String landingTest1PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0

	int landingTest2MaxPoints = 300                     // DB-2.0
	int landingTest2NoLandingPoints = 300               // DB-2.0
	int landingTest2OutsideLandingPoints = 200          // DB-2.0
	int landingTest2RollingOutsidePoints = 200          // DB-2.0
	int landingTest2PowerInBoxPoints = 50               // DB-2.0
	int landingTest2GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest2GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest2AbnormalLandingPoints = 150         // DB-2.0
	int landingTest2PowerInAirPoints = 200              // DB-2.0
	String landingTest2PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
	
	int landingTest3MaxPoints = 300                     // DB-2.0
	int landingTest3NoLandingPoints = 300               // DB-2.0
	int landingTest3OutsideLandingPoints = 200          // DB-2.0
	int landingTest3RollingOutsidePoints = 200          // DB-2.0
	int landingTest3PowerInBoxPoints = 50               // DB-2.0
	int landingTest3GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest3GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest3AbnormalLandingPoints = 150         // DB-2.0
	int landingTest3PowerInAirPoints = 200              // DB-2.0
	int landingTest3FlapsInAirPoints = 200              // DB-2.0
	String landingTest3PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
	
	int landingTest4MaxPoints = 300                     // DB-2.0
	int landingTest4NoLandingPoints = 300               // DB-2.0
	int landingTest4OutsideLandingPoints = 200          // DB-2.0
	int landingTest4RollingOutsidePoints = 200          // DB-2.0
	int landingTest4PowerInBoxPoints = 50               // DB-2.0
	int landingTest4GoAroundWithoutTouchingPoints = 200 // DB-2.0
	int landingTest4GoAroundInsteadStopPoints = 200     // DB-2.0
	int landingTest4AbnormalLandingPoints = 150         // DB-2.0
	int landingTest4TouchingObstaclePoints = 400        // DB-2.0
	String landingTest4PenaltyCalculator = "{f -> switch(f.toUpperCase()){case '0':return 0;case 'A':return 20;case 'B':return 40;case 'C':return 60;case 'D':return 80;case 'E':return 50;case 'F':return 90;case 'OUT':return 200;default:return 200;}}" // DB-2.0
	
	// transient values
	static transients = ['taskTitle','copyContestSettings','copyRoutes','copyCrews','copyTaskSettings']
	String taskTitle
	boolean copyContestSettings = true
	boolean copyRoutes = true
	boolean copyCrews = true
	boolean copyTaskSettings = true
	
	static hasMany = [routes:Route, tasks:Task, crews:Crew, aircrafts:Aircraft, teams:Team, resultclasses:ResultClass]
	
	static constraints = {
		title(blank:false)
		mapScale(blank:false, range:1..1000000000)
        timeZone(blank:false, validator:{ val, obj ->
			if (val.startsWith("-")) {
				if (val.size() > 6) {
					return false
				}
	            try {
	                Date t = Date.parse("HH:mm",val.substring(1))
	            } catch(Exception e) {
	                return false
	            }
			} else {
	            if (val.size() > 5) {
	                return false
	            }
	            try {
	                Date t = Date.parse("HH:mm",val)
	            } catch(Exception e) {
	                return false
	            }
			}
            return true
        })
		teamCrewNum(blank:false, min:0)
		
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

    static mapping = {
        routes sort:"id"
		tasks sort:"id"
		crews sort:"id"
		aircrafts sort:"id"
		teams sort:"id"
		resultclasses sort:"id"
	}

	void CopyValues(Contest contestInstance)
	{
		if (contestInstance) {
			if (copyContestSettings) {
				mapScale = contestInstance.mapScale
				timeZone = contestInstance.timeZone
				resultClasses = contestInstance.resultClasses
				contestRule = contestInstance.contestRule
				teamCrewNum = contestInstance.teamCrewNum
				
				planningTestDirectionCorrectGrad = contestInstance.planningTestDirectionCorrectGrad 
				planningTestDirectionPointsPerGrad = contestInstance.planningTestDirectionPointsPerGrad
				planningTestTimeCorrectSecond = contestInstance.planningTestTimeCorrectSecond
				planningTestTimePointsPerSecond = contestInstance.planningTestTimePointsPerSecond
				planningTestMaxPoints = contestInstance.planningTestMaxPoints
				planningTestPlanTooLatePoints = contestInstance.planningTestPlanTooLatePoints
				planningTestExitRoomTooLatePoints = contestInstance.planningTestExitRoomTooLatePoints
				
				flightTestTakeoffMissedPoints = contestInstance.flightTestTakeoffMissedPoints
				flightTestCptimeCorrectSecond = contestInstance.flightTestCptimeCorrectSecond
				flightTestCptimePointsPerSecond = contestInstance.flightTestCptimePointsPerSecond
				flightTestCptimeMaxPoints = contestInstance.flightTestCptimeMaxPoints
				flightTestCpNotFoundPoints = contestInstance.flightTestCpNotFoundPoints
				flightTestProcedureTurnNotFlownPoints = contestInstance.flightTestProcedureTurnNotFlownPoints
				flightTestMinAltitudeMissedPoints = contestInstance.flightTestMinAltitudeMissedPoints
				flightTestBadCourseCorrectSecond = contestInstance.flightTestBadCourseCorrectSecond
				flightTestBadCoursePoints = contestInstance.flightTestBadCoursePoints
				flightTestBadCourseStartLandingPoints = contestInstance.flightTestBadCourseStartLandingPoints
				flightTestLandingToLatePoints = contestInstance.flightTestLandingToLatePoints
				flightTestGivenToLatePoints = contestInstance.flightTestGivenToLatePoints
				
				landingTest1MaxPoints = contestInstance.landingTest1MaxPoints
				landingTest1NoLandingPoints = contestInstance.landingTest1NoLandingPoints
				landingTest1OutsideLandingPoints = contestInstance.landingTest1OutsideLandingPoints
				landingTest1RollingOutsidePoints = contestInstance.landingTest1RollingOutsidePoints
				landingTest1PowerInBoxPoints = contestInstance.landingTest1PowerInBoxPoints
				landingTest1GoAroundWithoutTouchingPoints = contestInstance.landingTest1GoAroundWithoutTouchingPoints
				landingTest1GoAroundInsteadStopPoints = contestInstance.landingTest1GoAroundInsteadStopPoints
				landingTest1AbnormalLandingPoints = contestInstance.landingTest1AbnormalLandingPoints
				landingTest1PenaltyCalculator = contestInstance.landingTest1PenaltyCalculator
				
				
				landingTest2MaxPoints = contestInstance.landingTest2MaxPoints
				landingTest2NoLandingPoints = contestInstance.landingTest2NoLandingPoints
				landingTest2OutsideLandingPoints = contestInstance.landingTest2OutsideLandingPoints
				landingTest2RollingOutsidePoints = contestInstance.landingTest2RollingOutsidePoints
				landingTest2PowerInBoxPoints = contestInstance.landingTest2PowerInBoxPoints
				landingTest2GoAroundWithoutTouchingPoints = contestInstance.landingTest2GoAroundWithoutTouchingPoints
				landingTest2GoAroundInsteadStopPoints = contestInstance.landingTest2GoAroundInsteadStopPoints
				landingTest2AbnormalLandingPoints = contestInstance.landingTest2AbnormalLandingPoints
				landingTest2PowerInAirPoints = contestInstance.landingTest2PowerInAirPoints
				landingTest2PenaltyCalculator = contestInstance.landingTest2PenaltyCalculator
				
				landingTest3MaxPoints = contestInstance.landingTest3MaxPoints
				landingTest3NoLandingPoints = contestInstance.landingTest3NoLandingPoints
				landingTest3OutsideLandingPoints = contestInstance.landingTest3OutsideLandingPoints
				landingTest3RollingOutsidePoints = contestInstance.landingTest3RollingOutsidePoints
				landingTest3PowerInBoxPoints = contestInstance.landingTest3PowerInBoxPoints
				landingTest3GoAroundWithoutTouchingPoints = contestInstance.landingTest3GoAroundWithoutTouchingPoints
				landingTest3GoAroundInsteadStopPoints = contestInstance.landingTest3GoAroundInsteadStopPoints
				landingTest3AbnormalLandingPoints = contestInstance.landingTest3AbnormalLandingPoints
				landingTest3PowerInAirPoints = contestInstance.landingTest3PowerInAirPoints
				landingTest3FlapsInAirPoints = contestInstance.landingTest3FlapsInAirPoints
				landingTest3PenaltyCalculator = contestInstance.landingTest3PenaltyCalculator
				
				landingTest4MaxPoints = contestInstance.landingTest4MaxPoints
				landingTest4NoLandingPoints = contestInstance.landingTest4NoLandingPoints
				landingTest4OutsideLandingPoints = contestInstance.landingTest4OutsideLandingPoints
				landingTest4RollingOutsidePoints = contestInstance.landingTest4RollingOutsidePoints
				landingTest4PowerInBoxPoints = contestInstance.landingTest4PowerInBoxPoints
				landingTest4GoAroundWithoutTouchingPoints = contestInstance.landingTest4GoAroundWithoutTouchingPoints
				landingTest4GoAroundInsteadStopPoints = contestInstance.landingTest4GoAroundInsteadStopPoints
				landingTest4AbnormalLandingPoints = contestInstance.landingTest4AbnormalLandingPoints
				landingTest4TouchingObstaclePoints = contestInstance.landingTest4TouchingObstaclePoints
				landingTest4PenaltyCalculator = contestInstance.landingTest4PenaltyCalculator
			} 
			
			this.save()
			
			if (copyRoutes) { // routes:Route
				Route.findAllByContest(contestInstance).each { Route route_instance ->
					Route new_route_instance = new Route()
					new_route_instance.contest = this
					new_route_instance.CopyValues(route_instance)
					new_route_instance.save()
				}
			}
			
			if (copyCrews) {
				// teams:Team
				Team.findAllByContest(contestInstance).each { Team team_instance ->
					Team new_team_instance = new Team()
					new_team_instance.contest = this
					new_team_instance.CopyValues(team_instance)
					new_team_instance.save()
				}
			
				// resultclasses:ResultClass
				ResultClass.findAllByContest(contestInstance).each { ResultClass resultclass_instance ->
					ResultClass new_resultclass_instance = new ResultClass()
					new_resultclass_instance.contest = this
					new_resultclass_instance.CopyValues(resultclass_instance)
					new_resultclass_instance.save()
				}

				// aircrafts:Aircraft
				Aircraft.findAllByContest(contestInstance).each { Aircraft aircraft_instance ->
					Aircraft new_aircraft_instance = new Aircraft()
					new_aircraft_instance.contest = this
					new_aircraft_instance.CopyValues(aircraft_instance)
					new_aircraft_instance.save()
				}
				
				// crews:Crew
				Crew.findAllByContest(contestInstance).each { Crew crew_instance ->
					Crew new_crew_instance = new Crew()
					new_crew_instance.contest = this
					new_crew_instance.CopyValues(crew_instance)
					new_crew_instance.save()
				}
			}
			
			if (copyTaskSettings) { // tasks:Task
				Task.findAllByContest(contestInstance).each { Task task_instance ->
					Task new_task_instance = new Task()
					new_task_instance.contest = this
					new_task_instance.CopyValues(task_instance)
					new_task_instance.save()
				}
			}
		}
	}
	
	String idName()
	{
		return "${getMsg('fc.contest')}-${id}"
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
		for (Task task_instance in Task.findAllByContest(this)) {
			if (task_instance.IsPlanningTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsFlightTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this)) {
			if (task_instance.IsFlightTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsObservationTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this)) {
			if (task_instance.IsObservationTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsLandingTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this)) {
			if (task_instance.IsLandingTestRun()) {
				return true
			}
		}
		return false
	}
	
	boolean IsSpecialTestRun()
	{
		for (Task task_instance in Task.findAllByContest(this)) {
			if (task_instance.IsSpecialTestRun()) {
				return true
			}
		}
		return false
	}
	
	String GetPrintContestTitle()
	{
		if (teamContestTitle > 1) {
			int i = 1
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(this)) {
				if (resultclass_instance.contestTitle) {
					i++
					if (i == teamContestTitle) {
						return resultclass_instance.contestTitle
					}
				}
			}
		}
		return title
	}
	
	String GetListTitle(String msgID)
	{
		if (teamContestTitle > 1) {
			int i = 1
			for (ResultClass resultclass_instance in ResultClass.findAllByContest(this)) {
				if (resultclass_instance.contestTitle) {
					i++
					if (i == teamContestTitle) {
						return "${getMsg(msgID)} - ${resultclass_instance.contestTitle}"
					}
				}
			}
		}
		return "${getMsg(msgID)} - ${title}"
	}
	
	String GetResultTitle(Map resultSettings, boolean isPrint)
	{
		String ret = ""
		if (resultSettings["Planning"]) {
			if (isPrint) {
				ret += getPrintMsg('fc.planningresults.planning')
			} else {
				ret += getMsg('fc.planningresults.planning')
			}
		}
		if (resultSettings["Flight"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.flightresults.flight')
			} else {
				ret += getMsg('fc.flightresults.flight')
			}
		}
		if (resultSettings["Observation"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.observationresults.observations')
			} else {
				ret += getMsg('fc.observationresults.observations')
			}
		}
		if (resultSettings["Landing"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.landingresults.landing')
			} else {
				ret += getMsg('fc.landingresults.landing')
			}
		}
		if (resultSettings["Special"]) {
			if (ret) {
				ret += ", "
			} 
			if (isPrint) {
				ret += getPrintMsg('fc.specialresults.other')
			} else {
				ret += getMsg('fc.specialresults.other')
			}
		}
		if (!ret) {
			if (isPrint) {
				ret = getPrintMsg('fc.results.none')
			} else {
				ret = getMsg('fc.results.none')
			}
		}
		return ret
	}
	
	boolean IsTeamResultsProvisional(Map resultSettings)
	{
        for (Team team_instance in Team.findAllByContest(this,[sort:'contestPosition'])) {
			if (team_instance.IsActiveTeam()) {
                int crew_num = 0
            	for ( Crew crew_instance in Crew.findAllByTeamAndDisabled(team_instance,false,[sort:'teamPenalties'])) {
            		if (crew_instance.IsActiveCrew() && (crew_num < teamCrewNum)) {
            			crew_num++
                        for ( Task task_instance in Task.findAllByContest(this)) {
                        	Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
                        	if (test_instance.AreResultsProvisional(resultSettings)) {
								return true
                        	}
                        }
            		}
            	}
			}
        }
		return false
	}
	
	Map GetTeamResultSettings()
	{
		Map ret = [:]
		if (teamPlanningResults && IsPlanningTestRun()) {
			ret += [Planning:true]
		}
		if (teamFlightResults && IsFlightTestRun()) {
			ret += [Flight:true]
		}
		if (teamObservationResults && IsObservationTestRun()) {
			ret += [Observation:true]
		}
		if (teamLandingResults && IsLandingTestRun()) {
			ret += [Landing:true]
		}
		if (teamSpecialResults && IsSpecialTestRun()) {
			ret += [Special:true]
		}
		return ret
	}
	
	boolean AreResultsProvisional(Map resultSettings)
	{
		//println "XX Contest.AreResultsProvisional $resultSettings"
	    for (Crew crew_instance in Crew.findAllByContestAndDisabled(this,false,[sort:'contestPosition'])) {
            for (Task task_instance in Task.findAllByContest(this)) {
            	Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
            	if (test_instance.AreResultsProvisional(resultSettings)) {
					//println "-> true (Contest.AreResultsProvisional $task_instance.title $test_instance.crew.name)"
					return true
				}
            }
	    }
		//println "-> false (Contest.AreResultsProvisional)"
		return false
	}
	
	Map GetResultSettings()
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
