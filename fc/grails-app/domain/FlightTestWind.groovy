class FlightTestWind
{
	Wind wind
	
	// transient Wind values
	static transients = ['direction','speed']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
	
	static belongsTo = [flighttest:FlightTest]

	static constraints = {
		direction(range:0.0f..<360.0f,scale:10)
		speed(range:0.0f..<1000.0f,scale:10)
	}
	
	String name()
	{
		return wind.name()
	}
}
