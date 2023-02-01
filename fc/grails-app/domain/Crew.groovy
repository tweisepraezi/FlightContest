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
    Boolean pageBreak = false                 // DB-2.35
    
    String uuid = UUID.randomUUID().toString()// DB-2.10
    String email = ""                         // DB-2.10
	String trackerID = ""                     // DB-2.15

	// transient values 
	static transients = ['registration','type','colour','teamname','resultclassname'] 
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

    // Live-Tracking
    Integer liveTrackingTeamID = 0            // DB-2.15
    Integer liveTrackingContestTeamID = 0     // DB-2.24
    Boolean liveTrackingDifferences = false   // DB-2.24
    
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
		
		// DB-2.15 compatibility
		trackerID(nullable:true)
		liveTrackingTeamID(nullable:true)
        
        // DB-2.24 compatibility
        liveTrackingContestTeamID(nullable:true)
        liveTrackingDifferences(nullable:true)
		
		// DB-2.35 compatibility
		pageBreak(nullable:true)
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
        email = crewInstance.email
        trackerID = crewInstance.trackerID

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
    
    long GetNextCrewID()
    {
        boolean start_found = false
        for (Crew crew_instance in Crew.findAllByContest(this.contest,[sort:'viewpos'])) {
            if (start_found) {
                return crew_instance.id
            }
            if (crew_instance.id == this.id) {
                start_found = true
            }
        }
        return 0
    }
    
    long GetPrevCrewID()
    {
        boolean start_found = false
        for (Crew crew_instance in Crew.findAllByContest(this.contest,[sort:'viewpos', order:'desc'])) {
            if (start_found) {
                return crew_instance.id
            }
            if (crew_instance.id == this.id) {
                start_found = true
            }
        }
        return 0
    }
    
}
