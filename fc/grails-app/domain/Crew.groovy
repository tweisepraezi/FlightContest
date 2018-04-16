import java.math.BigDecimal;
import java.util.Map;

class Crew 
{
	String name
    String mark = ""                          // UNUSED: Crew.mark, migriert nach Test.aflosStartNum (Integer), DB-2.10
	// String country = ""                    entfernt mit DB-2.0
	Aircraft aircraft
	Team team                                 // Team, DB-2.0
	ResultClass resultclass                   // Klasse, DB-2.0
    BigDecimal tas = 90

	int viewpos = 0
    Integer startNum = 0                      // DB-2.2
    
	boolean disabled = false
    Boolean disabledTeam = false              // DB-2.8
    Boolean disabledContest = false           // DB-2.9
    Boolean increaseEnabled = false           // DB-2.13 
    
    String uuid = UUID.randomUUID().toString()// DB-2.10
    String email = ""                         // DB-2.10

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
    Boolean contestEqualPosition = false      // DB-2.8
    Integer contestAddPosition = 0            // DB-2.8
	Integer classPosition = 0                 // DB-2.1
	Boolean noClassPosition = false           // DB-2.3
    Boolean classEqualPosition = false        // DB-2.8
    Integer classAddPosition = 0              // DB-2.8

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
        
        // DB-2.8 compatibility
        contestEqualPosition(nullable:true)
        contestAddPosition(nullable:true)
        classEqualPosition(nullable:true)
        classAddPosition(nullable:true)
        disabledTeam(nullable:true)
        
        // DB-2.9 compatibility
        disabledContest(nullable:true)
        
        // DB-2.10 compatibility
        uuid(nullable:true)
        email(nullable:true)
        
        // DB-2.13 compatibility
        increaseEnabled(nullable:true)
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
	
	boolean IsActiveCrew(ResultFilter resultFilter)
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
	
    boolean IsProvisionalCrew(Map resultSettings)
    {
        for( Task task_instance in contest.GetResultTasks(contest.contestTaskResults)) {
            Test test_instance = Test.findByCrewAndTask(this,task_instance)
            if (test_instance) {
                if (test_instance.IsTestProvisional(resultSettings)) {
                    return true
                }
            }
        }
        return false
    }
    
	int GetOldAFLOSStartNum()
	{
        int aflos_start_num = 0
        if (mark) {
            String s = mark
            if (mark.contains(':')) {
                s = mark.substring(0, mark.indexOf(':') )
            }
            if (s.isInteger()) {
                aflos_start_num = s.toInteger()
            }
        } else {
            aflos_start_num = startNum
        }
        return aflos_start_num
	}
    
    Integer GetIncreaseFactor()
    {
        if (contest.resultClasses && contest.contestRuleForEachClass) {
            if (resultclass) {
                return resultclass.increaseFactor
            }
        }
        return contest.increaseFactor
    }
    
    boolean IsIncreaseEnabled()
    {
        return increaseEnabled && (GetIncreaseFactor() > 0)
    }
    
    long GetNextID()
    {
        long next_id = 0
        boolean set_next = false
        for (Crew crew_instance in Crew.findAllByContest(this.contest,[sort:'viewpos'])) {
            if (set_next) {
                next_id = crew_instance.id
                set_next = false
            }
            if (crew_instance.id == this.id) { // BUG: direkter Klassen-Vergleich geht nicht, wenn Route-Instance bereits woanders geändert
                set_next = true
            }
        }
        return next_id
    }
    
    static long GetNextID2(long crewID)
    {
        long next_id = 0
        if (crewID) {
            Crew crew_instance = Crew.get(crewID)
            if (crew_instance) {
                next_id = crew_instance.GetNextID()
            }
        }
        return next_id
    }
}
