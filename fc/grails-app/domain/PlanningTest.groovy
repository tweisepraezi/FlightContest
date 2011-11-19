class PlanningTest 
{
	String title

	// transient PlanningTestTask values
	static transients = ['taskTitle','route','direction','speed']
	String taskTitle
	Route route
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
	
	static belongsTo = [contestdaytask:ContestDayTask]

	static hasMany = [planningtesttasks:PlanningTestTask]
	
	static constraints = {
		title()
		route(nullable:true) // TODO
        direction(range:0.0f..<360.0f)
        speed(range:0.0f..<1000.0f)
		contestdaytask(nullable:false)
	}

	static mapping = {
		planningtesttasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.planningtest', null, null)}-${contestdaytask.contestday.idTitle}.${contestdaytask.idTitle}"
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
