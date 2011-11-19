class FlightTest 
{
	String title
	Route route

	// transient Wind values
	static transients = ['direction','speed']
	float direction
	float speed 
	
	ContestDayTask contestdaytask
	static belongsTo = ContestDayTask

	static hasMany = [flighttestwinds:FlightTestWind]
	
	static constraints = {
		title()
		route()
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
