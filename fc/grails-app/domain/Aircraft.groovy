class Aircraft 
{
	String registration
	String type
	String colour
	float defaultTAS = 80.0f
	Crew owner

	Contest contest
	static belongsTo = Contest

	static constraints = {
		registration(blank:false, unique:true)
		type()
		colour()
		defaultTAS(range:10.0f..<1000.0f)
		owner(nullable:true, unique:true)
		contest(nullable:false)
	}

}
