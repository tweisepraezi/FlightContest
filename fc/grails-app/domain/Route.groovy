class Route 
{
	String title
    int idTitle
	
	static belongsTo = [contest:Contest]

	static hasMany = [routecoords:RouteCoord,routelegs:RouteLegCoord,testlegs:RouteLegTest]

	static constraints = {
		contest(nullable:false)
	}

	static mapping = {
		routecoords sort:"id"
		routelegs sort:"id"
		testlegs sort:"id"
	}
	
	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.route', null, null)}-${idTitle}"
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
