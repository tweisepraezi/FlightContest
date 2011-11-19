class SpecialTestTask 
{
	String title
    int idTitle
	
	SpecialTest specialtest
	static belongsTo = SpecialTest
	
	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.specialtesttask', null, null)}-${specialtest.contestdaytask.contestday.idTitle}.${specialtest.contestdaytask.idTitle}.${idTitle}"
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
