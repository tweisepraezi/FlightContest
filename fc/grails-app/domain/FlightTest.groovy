class FlightTest 
{
	String title
	Route route

	// transient Wind values
	static transients = ['direction','speed']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
	
	static belongsTo = [contestdaytask:ContestDayTask]

	static hasMany = [flighttestwinds:FlightTestWind]
	
	static constraints = {
		title()
		route(nullable:true) // TODO
        direction(range:0.0f..<360.0f)
        speed(range:0.0f..<1000.0f)
		contestdaytask(nullable:false)
	}

	static mapping = {
		flighttestwinds sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.flighttest', null, null)}-${contestdaytask.contestday.idTitle}.${contestdaytask.idTitle}"
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
