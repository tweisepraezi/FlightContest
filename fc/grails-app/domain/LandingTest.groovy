class LandingTest 
{
	String title

	static belongsTo = [contestdaytask:ContestDayTask]

	static hasMany = [landingtesttasks:LandingTestTask]
	
	static constraints = {
		title()
		contestdaytask(nullable:false)
	}

	static mapping = {
		landingtesttasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.landingtest', null, null)}-${contestdaytask.contestday.idTitle}.${contestdaytask.idTitle}"
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
