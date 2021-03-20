import java.math.BigDecimal;

class FlightTest 
{
	String title
	Route route

	// transient Wind values
	static transients = ['direction','speed','TODirection','LDGDirection','iTOiLDGDirection']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
    BigDecimal TODirection = 0.0
    BigDecimal LDGDirection = 0.0
    BigDecimal iTOiLDGDirection = 0.0

    // print styles
    Boolean flightPlanShowLegDistance = true      // DB-2.20
    Boolean flightPlanShowTrueTrack = true        // DB-2.20
    Boolean flightPlanShowTrueHeading = true      // DB-2.20
    Boolean flightPlanShowGroundSpeed = true      // DB-2.20
    Boolean flightPlanShowLocalTime = true        // DB-2.20
    Boolean flightPlanShowElapsedTime = false     // DB-2.20
    Integer submissionMinutes = 0                 // DB-2.20
    String flightPlanAddTPNum = ""                // DB-2.20
    Boolean flightResultsShowCurvedPoints = false // DB-2.20
	
	static belongsTo = [task:Task]

	static hasMany = [flighttestwinds:FlightTestWind]
	
	static constraints = {
		title()
		route(nullable:true)
        direction(range:0..360,scale:10)
        speed(range:0.0f..<1000.0f,scale:10)
        TODirection(range:0..360,scale:10)
        LDGDirection(range:0..360,scale:10)
        iTOiLDGDirection(range:0..360,scale:10)
		task(nullable:false)
        
        // DB-2.20 compatibility
        flightPlanShowLegDistance(nullable:true)
        flightPlanShowTrueTrack(nullable:true)
        flightPlanShowTrueHeading(nullable:true)
        flightPlanShowGroundSpeed(nullable:true)
        flightPlanShowLocalTime(nullable:true)
        flightPlanShowElapsedTime(nullable:true)
        submissionMinutes(nullable:true)
        flightPlanAddTPNum(nullable:true)
        flightResultsShowCurvedPoints(nullable:true)
	}

	static mapping = {
		flighttestwinds sort:"id"
	}

    String idName()
    {
		return "${getMsg('fc.flighttest')}-${task.idTitle}"
    }
    
	String name()
	{
		if(title) {
			return title
		} else {
            return idName()
		}
	}
    
    boolean IsObservationSignUsed()
    {
        Test test_instance1 = Test.findByTask(task)
        if (test_instance1) {
            if (test_instance1.IsObservationSignUsed()) {
                return true
            }
            for (Test test_instance in Test.findAllByTask(task)) {
                if (test_instance.IsObservationSignUsed()) {
                    return true
                }
            }
        }
        return false
    }
    
    boolean CanObservationsAdd()
    {
        if (route.IsObservationSignOk()) {
            if (!IsObservationSignUsed()) {
                if (!Test.findByTaskAndObservationTestComplete(task,true)) {
                    return true
                }
            }
        }
        return false
    }
    
    boolean CanObservationsRemove()
    {
        if (IsObservationSignUsed()) {
            if (!Test.findByTaskAndObservationTestComplete(task,true)) {
                return true
            }
        }
        return false
    }
    
    Map AddTPNum()
    {
        Map ret = [tp:[], addNumber:[]]
        if (flightPlanAddTPNum) {
            List v = flightPlanAddTPNum.split(':')
            if (v.size() == 2) {
                for (String v1 in v[0].split(',')) {
                    ret.tp += v1.trim().toUpperCase()
                }
                for (String v2 in v[1].split(',')) {
                    if (v2.isInteger()) {
                        ret.addNumber += v2.toInteger()
                    }
                }
            }
        }
        if (!ret.tp || !ret.addNumber || ret.tp.size() != ret.addNumber.size() || ret.tp.size() < 1) {
            return [:]
        }
        return ret
    }
    
}
