class LandingTest 
{
	String title

	static belongsTo = [task:Task]

	static hasMany = [landingtesttasks:LandingTestTask]
	
	static constraints = {
		title()
		task(nullable:false)
	}

	static mapping = {
		landingtesttasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.landingtest', null, null)}-${task.idTitle}"
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
