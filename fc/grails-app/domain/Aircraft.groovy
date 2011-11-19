class Aircraft 
{
	String registration
	String type
	String colour
	Crew user1
	Crew user2

	static belongsTo = [contest:Contest]

	static constraints = {
		registration(blank:false)
		type()
		colour()
		user1(nullable:true)
        user2(nullable:true)
		contest(nullable:false)
	}

	void CopyValues(Aircraft aircraftInstance)
	{
		registration = aircraftInstance.registration
		type = aircraftInstance.type
		colour = aircraftInstance.colour
	
		this.save()
	}
}
