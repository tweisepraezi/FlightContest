class Route 
{
	String title
    int idTitle
    String mark = ""
	
	static belongsTo = [contest:Contest]

	static hasMany = [coords:CoordRoute,routelegs:RouteLegCoord,testlegs:RouteLegTest]

	static constraints = {
		contest(nullable:false)
	}

	static mapping = {
		coords sort:"id"
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
