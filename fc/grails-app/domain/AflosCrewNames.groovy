class AflosCrewNames implements Serializable 
{
	// listed in Datasources.groovy
	
	int startnum
	int logstart
	int points
	String name
	
	static mapping = {
		table 'PLN_Data'
		
		startnum column: 'Start_Nr'
		logstart column: 'LogStart'
		points column: 'Points'
		name column: 'MARK'

        id composite:['startnum','logstart','points']
		
		version false

// access to instance
// def p = AflosCrewNames.get(new AflosCrewNames(startnum:1, logstart:20, points:3681))

	}
}
