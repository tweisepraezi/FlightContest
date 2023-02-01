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

    long GetNextAircraftID()
    {
        boolean start_found = false
        for (Aircraft aircraft_instance in Aircraft.findAllByContest(this.contest,[sort:'registration'])) {
            if (start_found) {
                return aircraft_instance.id
            }
            if (aircraft_instance.id == this.id) {
                start_found = true
            }
        }
        return 0
    }
    
    long GetPrevAircraftID()
    {
        boolean start_found = false
        for (Aircraft aircraft_instance in Aircraft.findAllByContest(this.contest,[sort:'registration', order:'desc'])) {
            if (start_found) {
                return aircraft_instance.id
            }
            if (aircraft_instance.id == this.id) {
                start_found = true
            }
        }
        return 0
    }
}
