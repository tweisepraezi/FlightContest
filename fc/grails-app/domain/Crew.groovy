class Crew 
{
	String name1
	String name2
	String country
	Aircraft ownAircraft
	Aircraft usedAircraft
	float usedTAS = 90.0f

	// transient own aircraft values 
	static transients = ['registration','type','colour','defaultTAS']
	String registration
	String type
	String colour
	float defaultTAS = 80.0f
	
	Contest contest
	static belongsTo = Contest

	static constraints = {
		name1(blank:false)
		name2()
		country()
		ownAircraft(nullable:true, unique:true)
		usedAircraft(nullable:true)
		usedTAS(range:10.0f..<1000.0f)
		contest(nullable:false)
		defaultTAS(range:10.0f..<1000.0f)
	}
	
	String name() {
		if(name2) {
			return "$name1 + $name2"
		} else {
			return name1
		}
	}

}
