class ContestDayTask 
{
	String title
    int idTitle
	
	NavTest navtest
	FlightTest flighttest
	LandingTest landingtest
	SpecialTest specialtest
	
	String firstTime                  = "11:00" // Local time of first navigation test [hh:mm]
	int takeoffIntervalNormal         = 10      // space between takeoff to slower aircraft [min]
	int takeoffIntervalFasterAircraft = 120     // space between takeoff to faster aircraft [min]
	
	int navTestDuration               = 60      // duration of navigation test [min]
	int preparationDuration           = 15      // duration of aircraft preparation [min]
	int risingDuration                = 10      // duration from takeoff to start point [min]
	int landingDuration               = 15      // duration from finish point to aircraft parking [min]
	int minNextFlightDuration         = 30      // duration of aircraft maintenance between two flights [min]
	int procedureTurnDuration         =  1      // duration of Procedure Turn [min]

	int addTimeValue                  =  5      // add/subtract time value [min]  
	
	static hasMany = [crewtests:CrewTest]
	
	ContestDay contestday
	static belongsTo = ContestDay
	
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
		navTestDuration(range:0..240)
		preparationDuration(range:0..240)
		risingDuration(range:0..240)
		landingDuration(range:0..240)
		minNextFlightDuration(range:0..240)
		procedureTurnDuration(range:0..60)
		addTimeValue(range:1..60)
		
		navtest(nullable:true)
		flighttest(nullable:true)
		landingtest(nullable:true)
		specialtest(nullable:true)
		contestday(nullable:false)
	}

    static mapping = {
		crewtests sort:"id"
	}
	
	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.contestdaytask', null, null)}-${contestday.idTitle}.${idTitle}"
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
