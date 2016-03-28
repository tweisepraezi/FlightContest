class PlanningTestTask 
{
	String title
    int idTitle

	Route route
	Wind wind
	
	// transient Wind values
	static transients = ['direction','speed']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
	
	static belongsTo = [planningtest:PlanningTest]

	static constraints = {
		route(blank:false)
		direction(range:0.0f..<360.0f,scale:10)
		speed(range:0.0f..<1000.0f,scale:10)
	}
	
    String idName()
    {
		return "${getMsg('fc.planningtesttask')}-${planningtest.task.idTitle}.${idTitle}"
    }
    
	String name()
	{
		if(title) {
			return "${title} (${idTitle})"
		} else {
            return idName()
		}
	}
    
    boolean Used()
    {
        for (Test test_instance in Test.findAllByTask(planningtest.task,[sort:"id"])) {
            if (!test_instance.crew.disabled && !test_instance.disabledCrew && (test_instance.planningtesttask == this)) {
                return true
            }
        }
        return false
    }
}
