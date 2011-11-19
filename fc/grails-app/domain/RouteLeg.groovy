class RouteLeg 
{
	float trueTrack
	float distance 
	
	Route route
	static belongsTo = Route

    static constraints = {
		trueTrack()
		distance()
    }

	def messageSource
	
	String name()
	{
		return "${trueTrack}${messageSource.getMessage('fc.grad', null, null)} ${distance}${messageSource.getMessage('fc.mile', null, null)}"
	}
}
