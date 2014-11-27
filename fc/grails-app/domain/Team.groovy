class Team
// DB-2.0
{
	String name

	// results
	int contestPenalties = 0
    int contestPosition = 0
    Boolean contestEqualPosition = false      // DB-2.8
    Integer contestAddPosition = 0            // DB-2.8

	Boolean disabled = false               // DB-2.8
	
	static belongsTo = [contest:Contest]

	static constraints = {
		name(blank:false)
		contest(nullable:false)

		// DB-2.8 compatibility
        disabled(nullable:true)
        contestEqualPosition(nullable:true)
        contestAddPosition(nullable:true)
	}

	void CopyValues(Team teamInstance)
	{
		name = teamInstance.name
        disabled = teamInstance.disabled
        
		if (!this.save()) {
			throw new Exception("Team.CopyValues could not save")
		}
	}
	
	boolean IsActiveTeam()
	{
        if (disabled) {
            return false
        }
		int crew_num = 0
		for (Crew crew_instance in Crew.findAllByTeamAndDisabledAndDisabledTeam(this,false,false,[sort:"id"])) {
			if (contest.resultClasses) {
				if (crew_instance.resultclass) {
					for(String team_class_result in crew_instance.contest.teamClassResults.split(',')) {
						if (team_class_result == "resultclass_${crew_instance.resultclass.id}") {
							crew_num++
						}
					}
				}
			} else {
				crew_num++
			}
			if (crew_num >= crew_instance.contest.teamCrewNum) {
				return true
			}
		}
		return false	
	}
}
