class Contest 
{
	String title
	int mapScale = 200000
	
	// PlanningTest
	int planningTestDirectionCorrectGrad = 2
	int planningTestDirectionPointsPerGrad = 2
	int planningTestTimeCorrectSecond = 5
	int planningTestTimePointsPerSecond = 1
	int planningTestMaxPoints = 350
	int planningTestPlanTooLatePoints = 50
	int planningTestExitRoomTooLatePoints = 100
	
	// transient values
	static transients = ['contestDayTitle','contestDayTaskTitle']
	String contestDayTitle
	String contestDayTaskTitle
	
	static hasMany = [crews:Crew, aircrafts:Aircraft, routes:Route, contestdays:ContestDay]
	
	static constraints = {
		title(blank:false)
		mapScale(blank:false, range:1..1000000000)
		planningTestDirectionCorrectGrad(blank:false, min:0)
		planningTestDirectionPointsPerGrad(blank:false, min:0)
		planningTestTimeCorrectSecond(blank:false, min:0)
		planningTestTimePointsPerSecond(blank:false, min:0)
		planningTestMaxPoints(blank:false, min:0)
		planningTestPlanTooLatePoints(blank:false, min:0)
		planningTestExitRoomTooLatePoints(blank:false, min:0)
	}

    static mapping = {
		contestdays sort:"id"
		crews sort:"id"
		aircrafts sort:"id"
		routes sort:"id"
	}

	def messageSource
	
	String idName()
	{
		return "${messageSource.getMessage('fc.contest', null, null)}-${id}"
	}
	
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
}
