class NavTest 
{
	String title

	// transient NavTestTask values
	static transients = ['taskTitle','route','TAS','direction','speed']
	String taskTitle
	Route route
	float TAS = 95.0f
	float direction
	float speed 
	
	ContestDayTask contestdaytask
	static belongsTo = ContestDayTask

	static hasMany = [navtesttasks:NavTestTask]
	
	static constraints = {
		title()
		contestdaytask(nullable:false)
	}

	static mapping = {
		navtesttasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.navtest', null, null)}-${contestdaytask.contestday.idTitle}.${contestdaytask.idTitle}"
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
