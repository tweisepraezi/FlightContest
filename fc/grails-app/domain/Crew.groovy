class Crew 
{
	String name
    String mark = ""
	String country
	Aircraft aircraft
    BigDecimal tas = 90

	int viewpos = 0
	boolean disabled = false
	
	// transient aircraft values 
	static transients = ['registration','type','colour']
	String registration
	String type
	String colour
	
	// results
	int contestPenalties = 0
    int contestPosition = 0
	
	static belongsTo = [contest:Contest]

	static constraints = {
		name(blank:false)
		country()
		aircraft(nullable:true)
        tas(range:10.0f..<1000.0f)
		contest(nullable:false)
	}
	
	void CopyValues(Crew crewInstance)
	{
		name = crewInstance.name
	    mark = crewInstance.mark
		country = crewInstance.country
	    tas = crewInstance.tas
		viewpos = crewInstance.viewpos
		disabled = crewInstance.disabled
		// contestPenalties = crewInstance.contestPenalties
		// contestPosition = crewInstance.contestPosition

		if (crewInstance.aircraft) {
			Aircraft aircraft_instance = Aircraft.findByRegistrationAndContest(crewInstance.aircraft.registration, contest)
			aircraft = aircraft_instance
		}
		
		this.save()

		if (aircraft) {
			if (!aircraft.user1) {
	        	aircraft.user1 = this
				aircraft.save()
	        } else if (!aircraft.user2) {
	            aircraft.user2 = this
				aircraft.save()
	        }
		}
	}
}
