class AflosCheckPoints
{
    // listed in Datasources.groovy
    
	int id
    int startnum
	AflosRouteNames routename
	String mark
	String utc
	String latitude
	String longitude
    String altitude
    String direction
	String truetrack
	String speed
	
	static mapping = {
		table 'PNT_Check'

		id column: 'POINTID'
		startnum column: 'Start_Nr'
		routename column: 'Ref_Nr'
		mark column: 'MARK'
		utc column: 'UTC'
		latitude column: 'Latitude'
		longitude column: 'Longitude'
	    altitude column: 'Altitude'
	    direction column: 'Track'
		truetrack column: 'True_Course'
		speed column: 'Speed'
			
		version false
	}
}
