import java.math.BigDecimal;

class FlightTest 
{
	String title
	Route route

	// transient Wind values
	static transients = ['direction','speed','TODirection','LDGDirection','iTOiLDGDirection','TODurationFormula','LDGDurationFormula','iLDGDurationFormula','iTODurationFormula']
	BigDecimal direction = 0.0
	BigDecimal speed = 0.0
    BigDecimal TODirection = 0.0
    BigDecimal LDGDirection = 0.0
    BigDecimal iTOiLDGDirection = 0.0
	String TODurationFormula = "wind+:3NM"        // DB-2.39
	String LDGDurationFormula = "wind+:6NM"       // DB-2.39
	String iLDGDurationFormula = "wind+:2NM"      // DB-2.39
	String iTODurationFormula = "wind+:3NM"       // DB-2.39

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
    FlightPlanDesign flightPlanDesign = FlightPlanDesign.TPList // DB-2.41
	
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
        TODurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        LDGDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        iLDGDurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
        iTODurationFormula(nullable:true, validator:{ val, obj -> return DurationValid(val,obj)})
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
        
        // DB-2.41 compatibility
        flightPlanDesign(nullable:true)
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
        if (route.corridorWidth) {
            return false
        }
        if (RouteTools.IsObservationSignOk(route)) {
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
    
    List GetPrintableANRPlanTests()
    {
        return Test.findAllByTaskAndDisabledCrewAndTimeCalculated(task, false, true, [sort:'taskTAS'])
    }
    
    boolean CanANRPlanPrinted()
    {
        if (!route.corridorWidth) {
            return false
        }
        if (GetPrintableANRPlanTests()) {
            return true
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
    
	static boolean DurationValid(val, obj)
	{
		if (val) {
			if (val.startsWith('time+:')) {
				if (val.endsWith('min')) {
					String f = val.substring(6,val.size()-3).replace(',','.')
					if (f.isInteger()) {
						return true
					}
				}
			} else if (val.startsWith('time:')) {
				if (val.endsWith('min')) {
					String f = val.substring(5,val.size()-3).replace(',','.')
					if (f.isInteger()) {
						return true
					}
				}
			} else if (val.startsWith('wind+:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(6,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(6,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('wind:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(5,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(5,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('nowind+:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(8,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(8,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('nowind:')) {
				if (val.endsWith('NM')) {
					String f = val.substring(7,val.size()-2).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				} else {
					String f = val.substring(7,val.size()).replace(',','.')
					if (f.isBigDecimal()) {
						return true
					}
				}
			} else if (val.startsWith('func+:')) {
				String f = val.substring(6,val.size())
				if (f) {
					return true
				}
			} else if (val.startsWith('func:')) {
				String f = val.substring(5,val.size())
				if (f) {
					return true
				}
			}
		}
		return false
	}
}
