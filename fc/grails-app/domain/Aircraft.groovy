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
	
		if (!this.save()) {
			throw new Exception("Aircraft.CopyValues could not save")
		}
	}

    long GetNextID()
    {
        long next_id = 0
        boolean set_next = false
        for (Aircraft aircraft_instance in Aircraft.findAllByContest(this.contest,[sort:'registration'])) {
            if (set_next) {
                next_id = aircraft_instance.id
                set_next = false
            }
            if (aircraft_instance.id == this.id) {
                set_next = true
            }
        }
        return next_id
    }
    
    static long GetNextID2(long aircraftID)
    {
        long next_id = 0
        if (aircraftID) {
            Aircraft aircraft_instance = Aircraft.get(aircraftID)
            if (aircraft_instance) {
                next_id = aircraft_instance.GetNextID()
            }
        }
        return next_id
    }
}
