class SpecialTestTask 
{
	String title
    int idTitle
	
	static belongsTo = [specialtest:SpecialTest]
	
	def messageSource
	
    String idName()
    {
		return "${messageSource.getMessage('fc.specialtesttask', null, null)}-${specialtest.task.idTitle}.${idTitle}"
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
