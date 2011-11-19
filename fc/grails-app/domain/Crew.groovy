class Crew 
{
	String name
    String mark = ""
	String country
	Aircraft aircraft
    BigDecimal tas = 90

	// transient aircraft values 
	static transients = ['registration','type','colour']
	String registration
	String type
	String colour
	
	static belongsTo = [contest:Contest]

	static constraints = {
		name(blank:false)
		country()
		aircraft(nullable:true)
        tas(range:10.0f..<1000.0f)
		contest(nullable:false)
	}
	
}
