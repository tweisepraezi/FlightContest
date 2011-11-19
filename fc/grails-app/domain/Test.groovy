class Test 
{
	Crew crew
	PlanningTestTask planningtesttask
	FlightTestWind flighttestwind
	
	int viewpos = 0
	BigDecimal taskTAS = 0
	
	// planning
	boolean timeCalculated = false
	int timetableVersion = 0
	Date testingTime    = Date.parse("HH:mm","00:00")
	Date endTestingTime = Date.parse("HH:mm","00:00")
	Date takeoffTime    = Date.parse("HH:mm","00:00")
	Date startTime      = Date.parse("HH:mm","00:00")
	Date finishTime     = Date.parse("HH:mm","00:00")
    Date maxLandingTime = Date.parse("HH:mm","00:00")
	Date arrivalTime    = Date.parse("HH:mm","00:00")

	boolean arrivalTimeWarning = false
	boolean takeoffTimeWarning = false
	
	// results
	int     planningTestLegPenalties = 0
	boolean planningTestLegComplete = false
	boolean planningTestGivenTooLate = false
	boolean planningTestExitRoomTooLate = false
	int     planningTestPenalties = 0
	boolean planningTestComplete = false
	
    int     flightTestCheckPointPenalties = 0
	boolean flightTestCheckPointsComplete = false
	boolean flightTestTakeoffMissed = false
	boolean flightTestBadCourseStartLanding = false
	boolean flightTestLandingTooLate = false
    boolean flightTestGivenTooLate = false
    int     flightTestPenalties = 0
    boolean flightTestComplete = false
    
    int     observationTestRoutePhotoPenalties = 0
    int     observationTestTurnPointPhotoPenalties = 0
    int     observationTestGroundTargetPenalties = 0
    int     observationTestPenalties = 0
	boolean observationTestComplete = false
	
	int     landingTestPenalties = 0
    boolean landingTestComplete = false
    
	int     specialTestPenalties = 0 
	boolean specialTestComplete = false
	 
	int taskPenalties = 0
    int taskPosition = 0
    
	static belongsTo = [task:Task]
	
	static hasMany = [testlegplannings:TestLegPlanning,testlegflights:TestLegFlight,coordresults:CoordResult]
	
    static constraints = {
		crew(nullable:true)
		planningtesttask(nullable:true)
		flighttestwind(nullable:true)
    }

	static mapping = {
		testlegplannings sort:"id"
		testlegflights sort:"id"
	}
	
	void ResetPlanningTestResults()
	{
		planningTestLegPenalties = 0
		planningTestLegComplete = false
		planningTestGivenTooLate = false
		planningTestExitRoomTooLate = false
		planningTestPenalties = 0
		planningTestComplete = false
	}
	
	void ResetFlightTestResults()
	{
		flightTestCheckPointPenalties = 0
		flightTestCheckPointsComplete = false
		flightTestTakeoffMissed = false
		flightTestBadCourseStartLanding = false
		flightTestLandingTooLate = false
		flightTestGivenTooLate = false
		flightTestPenalties = 0
		flightTestComplete = false
	}
	
	void ResetObservationTestResults()
	{
		observationTestRoutePhotoPenalties = 0
		observationTestTurnPointPhotoPenalties = 0
		observationTestGroundTargetPenalties = 0
		observationTestPenalties = 0
		observationTestComplete = false
	}
	
	void ResetLandingTestResults()
	{
		landingTestPenalties = 0
		landingTestComplete = false
	}
	
	void ResetSpecialTestResults()
	{
		specialTestPenalties = 0
		specialTestComplete = false
	}
	
	void CalculateTestPenalties()
	{
		taskPosition = 0
    	taskPenalties = 0
		if (task.planningTestRun) {
	    	if (planningTestComplete) {
	    		taskPenalties += planningTestPenalties
	    	}
		}
		if (task.flightTestRun) {
	    	if (flightTestComplete) {
	    		taskPenalties += flightTestPenalties
	    	}
		}
		if (task.observationTestRun) {
			if (observationTestComplete) {
				taskPenalties += observationTestPenalties
			}
		}
		if (task.landingTestRun) {
	        if (landingTestComplete) {
	        	taskPenalties += landingTestPenalties
	        }
		}
		if (task.specialTestRun) {
			if (specialTestComplete) {
				taskPenalties += specialTestPenalties
			}
		}
		crew.contestPosition = 0
		crew.contestPenalties = 0
	}
}
