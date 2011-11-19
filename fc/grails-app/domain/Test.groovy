class Test 
{
	Crew crew
	PlanningTestTask planningtesttask
	FlightTestWind flighttestwind
	int viewpos = 0
	
	boolean timeCalculated = false
	Date testingTime    = Date.parse("HH:mm","00:00")
	Date endTestingTime = Date.parse("HH:mm","00:00")
	Date takeoffTime    = Date.parse("HH:mm","00:00")
	Date startTime      = Date.parse("HH:mm","00:00")
	Date finishTime     = Date.parse("HH:mm","00:00")
	Date arrivalTime    = Date.parse("HH:mm","00:00")

	boolean arrivalTimeWarning = false
	boolean takeoffTimeWarning = false
	
	// Penalties
	int planningTestLegPenalties = 0
	boolean planningTestLegComplete = false
	boolean planningTestTooLate = false
	boolean planningTestExitRoomTooLate = false
	int planningTestPenalties = 0
	boolean planningTestComplete = false
	
	int flightTestPenalties = 0
    boolean flightTestComplete = false
    
	int landingTestPenalties = 0
    boolean landingTestComplete = false
    
	int specialTestPenalties = 0
    boolean specialTestComplete = false
    
	int testPenalties = 0

    int positionContestDay = 0
    
	static belongsTo = [contestdaytask:ContestDayTask]
	
	static hasMany = [testlegplannings:TestLegPlanning,testlegflights:TestLegFlight]
	
    static constraints = {
		crew(nullable:true) // XXX
		planningtesttask(nullable:true)
		flighttestwind(nullable:true)
		planningTestPenalties(nullable:true,range:0..<100000)
		flightTestPenalties(nullable:true,range:0..<100000)
		landingTestPenalties(nullable:true,range:0..<100000)
		specialTestPenalties(nullable:true,range:0..<100000)
    }

	static mapping = {
		testlegplannings sort:"id"
		testlegflights sort:"id"
	}
}
