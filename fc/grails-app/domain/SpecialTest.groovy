class SpecialTest 
{
	String title

	static belongsTo = [contestdaytask:ContestDayTask]

	static hasMany = [specialtesttasks:SpecialTestTask]
	
	static constraints = {
		title()
		contestdaytask(nullable:false)
	}
	
	static mapping = {
		specialtesttasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.specialtest', null, null)}-${contestdaytask.contestday.idTitle}.${contestdaytask.idTitle}"
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
