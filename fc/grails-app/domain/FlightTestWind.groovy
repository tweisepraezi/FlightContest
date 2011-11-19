class FlightTestWind
{
	Wind wind
	
	// transient Wind values
	static transients = ['direction','speed']
	float direction
	float speed 
	
	FlightTest flighttest
	static belongsTo = FlightTest

	static constraints = {
		direction(range:0.0f..<360.0f)
		speed(range:0.0f..<1000.0f)
	}
	
	String name()
	{
		return wind.name()
	}
}
