class Contest 
{
	String title
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
	int flightTestCptimePointsPerSecond = 3
	int flightTestCptimeMaxPoints = 200
	int flightTestCpNotFoundPoints = 200
	int flightTestProcedureTurnNotFlownPoints = 200
	int flightTestMinAltitudeMissedPoints = 500
	int flightTestBadCourseCorrectSecond = 5
	int flightTestBadCoursePoints = 200
	int flightTestBadCourseStartLandingPoints = 200
	int flightTestLandingToLatePoints = 200
	int flightTestGivenToLatePoints = 100

	// transient values
	static transients = ['taskTitle']
	String taskTitle
	
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

	def messageSource
	
	String idName()
	{
		return "${messageSource.getMessage('fc.contest', null, null)}-${id}"
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
