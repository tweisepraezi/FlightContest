class AflosRouteDefs
{
    // listed in Datasources.groovy
    
	int id
    AflosRouteNames routename
	String mark
	String latitude
	String longitude
    String altitude
	BigDecimal truetrack
	BigDecimal coordDistance
    int gatewidth
	
	static mapping = {
		table 'PNT_Ref'

		id column: 'POINTID'
		routename column: 'Ref_Nr'
		mark column: 'MARK'
		latitude column: 'Latitude'
		longitude column: 'Longitude'
	    altitude column: 'Altitude'
		truetrack column: 'True_Course'
		coordDistance column: 'Distance'
        gatewidth column: 'Gate_Width'
			
		version false
	}
}
