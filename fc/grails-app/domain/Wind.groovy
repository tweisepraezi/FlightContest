import java.text.*

class Wind 
{
	BigDecimal direction
	BigDecimal speed 

	static belongsTo = [PlanningTestTask, FlightTestWind]

	static constraints = {
		direction(range:0..<360)
		speed(range:0..<1000)
	}

	def messageSource
	
	String name()
	{
		return "${FcMath.GradStr(direction)}${messageSource.getMessage('fc.grad', null, null)} ${FcMath.SpeedStr(speed)}${messageSource.getMessage('fc.knot', null, null)}"
	}
}
