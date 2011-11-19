class Contest 
{
	String title

	// transient values
	static transients = ['contestDayTitle','contestDayTaskTitle']
	String contestDayTitle
	String contestDayTaskTitle
	
	int lastContestDayTask = 0
	
	static hasMany = [contestdays:ContestDay, crews:Crew, aircrafts:Aircraft, routes:Route]
	
	static constraints = {
		title(blank:false)
	}

    static mapping = {
		contestdays sort:"id"
		crews sort:"id"
		aircrafts sort:"id"
		routes sort:"id"
	}

	def messageSource
	
	String idName()
	{
		return "${messageSource.getMessage('fc.contest', null, null)}-${id}"
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
