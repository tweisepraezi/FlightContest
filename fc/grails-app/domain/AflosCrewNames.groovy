class AflosCrewNames implements Serializable 
{
	// listed in Datasources.groovy
	
	int startnum
	int logstart
	int points
	String name
	
	static mapping = {
		datasources(['aflos','aflostest','aflosupload'])
		table 'PLN_Data'
		
		startnum column: 'Start_Nr'
		logstart column: 'LogStart'
		points column: 'Points'
		name column: 'MARK'

        id composite:['startnum','logstart','points']
		version false
	}
	
	String viewName()
	{
		if (name) {
			return "$startnum: $name"
		}
		return "$startnum"
	}
}
