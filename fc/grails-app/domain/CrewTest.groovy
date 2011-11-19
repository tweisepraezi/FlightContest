class CrewTest 
{
	Crew crew
	NavTestTask navtesttask
	FlightTestWind flighttestwind
	Aircraft aircraft
	float TAS
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
	
	// Results
	boolean penaltyComplete = false
	int penaltyNavTest = 0
	int penaltyFlightTest = 0
	int penaltyLandingTest = 0
	int penaltySpecialTest = 0
	int penaltySummary = 0
	int positionContestDay = 0
	
	ContestDayTask contestdaytask
	static belongsTo = ContestDayTask
	
	static hasMany = [crewtestlegs:CrewTestLeg]
	
    static constraints = {
		navtesttask(nullable:true)
		flighttestwind(nullable:true)
		aircraft(nullable:true)
		penaltyNavTest(nullable:true,range:0..<100000)
		penaltyFlightTest(nullable:true,range:0..<100000)
		penaltyLandingTest(nullable:true,range:0..<100000)
		penaltySpecialTest(nullable:true,range:0..<100000)
    }

	static mapping = {
		crewtestlegs sort:"id"
	}
}
