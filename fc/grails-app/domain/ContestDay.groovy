class ContestDay 
{
	String title
	int idTitle

	Contest contest
	static belongsTo = Contest
	
	static hasMany = [contestdaytasks:ContestDayTask]
	
	static constraints = {
		contest(nullable:false)
	}

	static mapping = {
		contestdaytasks sort:"id"
	}

	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.contestday', null, null)}-${idTitle}"
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
