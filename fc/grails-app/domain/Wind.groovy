import java.text.*

class Wind 
{
	float direction
	float speed 

	static belongsTo = [NavTestTask, FlightTestWind]

	static constraints = {
		direction(range:0.0f..<360.0f)
		speed(range:0.0f..<1000.0f)
	}

	def messageSource
	
	String name()
	{
		return "${directionFormat()}${messageSource.getMessage('fc.grad', null, null)} ${speedFormat()}${messageSource.getMessage('fc.knot', null, null)}"
	}

	String directionFormat()
	{
		DecimalFormat df = new DecimalFormat("000")
		return df.format(direction)
	}

	String speedFormat()
	{
		DecimalFormat df = new DecimalFormat("#0.0")
		return df.format(speed)
	}
	
}
