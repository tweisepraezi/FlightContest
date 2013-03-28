class Crew 
{
	String name
    String mark = ""
	// String country = ""                    entfernt mit DB-2.0
	Aircraft aircraft
	Team team                                 // Team, DB-2.0
	ResultClass resultclass                   // Klasse, DB-2.0
    BigDecimal tas = 90

	int viewpos = 0
	boolean disabled = false
	Integer startNum = 0                      // DB-2.2
	
	// transient values 
	static transients = ['name2','registration','type','colour','teamname','resultclassname'] 
	String name2 
	String registration
	String type
	String colour
	String teamname
	String resultclassname
	
	// results
	int planningPenalties = 0                 // DB-2.0
	int flightPenalties = 0                   // DB-2.0
	int observationPenalties = 0              // DB-2.0
	int landingPenalties = 0                  // DB-2.0
	int specialPenalties = 0                  // DB-2.0
	
	int contestPenalties = 0
	int teamPenalties = 0                     // DB-2.0
	
    int contestPosition = 0
	Boolean noContestPosition = false         // DB-2.1
	Integer classPosition = 0                 // DB-2.1
	Boolean noClassPosition = false           // DB-2.3
	
	static belongsTo = [contest:Contest]

	static constraints = {
		name(blank:false)
		aircraft(nullable:true)
		team(nullable:true)
		resultclass(nullable:true)
        tas(range:10.0f..<1000.0f,scale:10)
		contest(nullable:false)
		
		// DB-2.1 compatibility
		noContestPosition(nullable:true)
		classPosition(nullable:true)
		
		// DB-2.2 compatibility
		startNum(nullable:true,min:1)
		
		// DB-2.3 compatibility
		noClassPosition(nullable:true)
	}
	
	int GetResultPenalties(Map resultSettings)
	{
		int penalties = 0
		if (resultSettings["Planning"]) {
			penalties += planningPenalties
		}
		if (resultSettings["Flight"]) {
			penalties += flightPenalties
		}
		if (resultSettings["Observation"]) {
			penalties += observationPenalties
		}
		if (resultSettings["Landing"]) {
			penalties += landingPenalties
		}
		if (resultSettings["Special"]) {
			penalties += specialPenalties
		}
		return penalties
	}
	
	void CopyValues(Crew crewInstance)
	{
		name = crewInstance.name
	    mark = crewInstance.mark
	    tas = crewInstance.tas
		viewpos = crewInstance.viewpos
		disabled = crewInstance.disabled
		startNum = crewInstance.startNum

		if (crewInstance.aircraft) {
			Aircraft aircraft_instance = Aircraft.findByRegistrationAndContest(crewInstance.aircraft.registration, contest)
			aircraft = aircraft_instance
		}
		if (crewInstance.team) {
			Team team_instance = Team.findByNameAndContest(crewInstance.team.name, contest)
			team = team_instance
		}
		if (crewInstance.resultclass) {
			ResultClass resultclass_instance = ResultClass.findByNameAndContest(crewInstance.resultclass.name, contest)
			resultclass = resultclass_instance
		}

		if (!this.save()) {
			throw new Exception("Crew.CopyValues could not save")
		}

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
	
	private boolean IsActiveCrew(ResultFilter resultFilter)
	{
		if (contest.resultClasses) {
			if (resultclass) {
				switch (resultFilter) {
					case ResultFilter.Contest:
						for(String contest_class_result in contest.contestClassResults.split(',')) {
							if (contest_class_result == "resultclass_${resultclass.id}") {
								return true
							}
						}
						break
					case ResultFilter.Team:
						for(String team_class_result in contest.teamClassResults.split(',')) {
							if (team_class_result == "resultclass_${resultclass.id}") {
								return true
							}
						}
						break
				}
			}
			return false
		}
		return true
	}
	
	String GetAFLOSStartNum()
	{
		if (mark.contains(':')) {
			return mark.substring(0, mark.indexOf(':') )
		}
		return startNum.toString()
	}
}
