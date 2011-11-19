class Global {

	Languages language = Languages.de 
	
    static constraints = {
		language()
    }
	
	static transients = ['language']
}
