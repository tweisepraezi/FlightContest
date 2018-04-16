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
        if (contestPenalties == -1) {
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

    long GetNextID()
    {
        long next_id = 0
        boolean set_next = false
        for (Team team_instance in Team.findAllByContest(this.contest,[sort:'name'])) {
            if (set_next) {
                next_id = team_instance.id
                set_next = false
            }
            if (team_instance.id == this.id) { // BUG: direkter Klassen-Vergleich geht nicht, wenn Route-Instance bereits woanders ge�ndert
                set_next = true
            }
        }
        return next_id
    }
    
    static long GetNextID2(long teamID)
    {
        long next_id = 0
        if (teamID) {
            Team team_instance = Team.get(teamID)
            if (team_instance) {
                next_id = team_instance.GetNextID()
            }
        }
        return next_id
    }
}
