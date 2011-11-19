class Contest 
{
	String title = "Wettbewerbsname"
	int mapScale = 200000
    String timeZone = "02:00" // Difference between UTC and local time [hh:mm]
	
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

	// boolean contestClasses = false      // enable contest with classes, DB-1.2
	
	// transient values
	static transients = ['taskTitle','copyContestSettings','copyRoutes','copyCrews','copyTaskSettings']
	String taskTitle
	boolean copyContestSettings = true
	boolean copyRoutes = true
	boolean copyCrews = true
	boolean copyTaskSettings = true
	
	static hasMany = [routes:Route, tasks:Task, crews:Crew, aircrafts:Aircraft]
	
	static constraints = {
		title(blank:false)
		mapScale(blank:false, range:1..1000000000)
        timeZone(blank:false, validator:{ val, obj ->
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
	}

    static mapping = {
        routes sort:"id"
		tasks sort:"id"
		crews sort:"id"
		aircrafts sort:"id"
	}

	void CopyValues(Contest contestInstance)
	{
		if (contestInstance) {
			if (copyContestSettings) {
				mapScale = contestInstance.mapScale
				timeZone = contestInstance.timeZone
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
}
