class Route 
{
	String title
    int idTitle
	
	Contest contest
	static belongsTo = Contest

	static hasMany = [routecoords:RouteCoord,routelegs:RouteLeg]

	static constraints = {
		contest(nullable:false)
	}

	static mapping = {
		routecoords sort:"id"
		routelegs sort:"id"
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
