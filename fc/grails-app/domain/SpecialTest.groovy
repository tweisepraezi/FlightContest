class SpecialTest 
{
	String title

	static belongsTo = [task:Task]

	static hasMany = [specialtesttasks:SpecialTestTask]
	
	static constraints = {
		title()
		task(nullable:false)
	}
	
	static mapping = {
		specialtesttasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.specialtest', null, null)}-${task.idTitle}"
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
