class PlanningTest 
{
	String title

	// transient PlanningTestTask values
	static transients = ['taskTitle','route','direction','speed']
	String taskTitle
	Route route
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
	
	static belongsTo = [task:Task]

	static hasMany = [planningtesttasks:PlanningTestTask]
	
	static constraints = {
		title()
		route(nullable:true)
        direction(range:0..360,scale:10)
        speed(range:0.0f..<1000.0f,scale:10)
		task(nullable:false)
	}

	static mapping = {
		planningtesttasks sort:"id"
	}

    String idName()
    {
		return "${getMsg('fc.planningtest')}-${task.idTitle}"
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
