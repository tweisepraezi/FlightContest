class LandingTestTask 
{
	String title
    int idTitle

	static belongsTo = [landingtest:LandingTest]
	
	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.landingtesttask', null, null)}-${landingtest.task.idTitle}.${idTitle}"
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
