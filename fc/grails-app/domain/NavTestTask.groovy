class NavTestTask 
{
	String title
    int idTitle

	Route route
	Wind wind
	float TAS = 95.0f
	
	// transient Wind values
	static transients = ['direction','speed']
	float direction
	float speed 
	
	static hasMany = [navtesttasklegs:NavTestTaskLeg]
	
	NavTest navtest
	static belongsTo = NavTest

	static constraints = {
		route(blank:false)
		direction(range:0.0f..<360.0f)
		speed(range:0.0f..<1000.0f)
	}
	
	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.navtesttask', null, null)}-${navtest.contestdaytask.contestday.idTitle}.${navtest.contestdaytask.idTitle}.${idTitle}"
    }
    
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}

	static mapping = {
		navtesttasklegs sort:"id"
	}
}
