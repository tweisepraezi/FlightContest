import java.text.*

class Wind 
{
	BigDecimal direction
	BigDecimal speed 

	static belongsTo = [PlanningTestTask, FlightTestWind]

	static constraints = {
		direction(range:0..<360, scale:10)
		speed(range:0..<1000, scale:10)
	}

	String name()
	{
		return "${FcMath.GradStr(direction)}${getMsg('fc.grad')} ${FcMath.SpeedStr(speed)}${getMsg('fc.knot')}"
	}
}
