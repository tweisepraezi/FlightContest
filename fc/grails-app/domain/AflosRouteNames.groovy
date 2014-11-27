class AflosRouteNames
{
	// listed in Datasources.groovy
	
	int id
	String name
	int number
	
	static mapping = {
		datasources(['aflos','aflostest','aflosupload'])
		table 'PLN_Ref'
		
		id column: 'PLINEID'
		name column: 'MARK'
		number column: 'NUMVERT'
		
		version false
	}

	String viewName()
	{
		return "$id: $name"
	}
}
