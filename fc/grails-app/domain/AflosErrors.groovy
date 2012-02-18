class AflosErrors
{
    // listed in Datasources.groovy
    
	int id
    int startnum
	AflosRouteNames routename
	String mark
	int checkPointErrors
	int courseErrors
	int heightErrors
	int dropOutErrors
	
	static mapping = {
		datasources(['aflos','aflostest','aflosupload'])
		table 'STAT_Error'

		id column: 'ID'
		startnum column: 'Start_Nr'
		routename column: 'Ref_Nr'
		mark column: 'MARK'
		checkPointErrors column: 'Check_Error'
		courseErrors column: 'Course_Error'
		heightErrors column: 'Height_Error'
		dropOutErrors column: 'DropOut_Error'
			
		version false
	}
}
