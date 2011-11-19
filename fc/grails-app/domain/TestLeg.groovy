import java.text.*

class TestLeg
{
	// plan
	BigDecimal planTrueTrack = 0        // Grad
	BigDecimal planTestDistance = 0     // NM 
	
	BigDecimal planTrueHeading = 0      // Grad
	BigDecimal planGroundSpeed = 0      // NM
	BigDecimal planLegTime = 0          // h

	boolean planProcedureTurn = false
	int planProcedureTurnDuration = 0   // min
	
	// results
    BigDecimal resultTrueTrack = 0      // Grad
    BigDecimal resultTestDistance = 0   // NM 
    
    BigDecimal resultTrueHeading = 0    // Grad
    BigDecimal resultGroundSpeed = 0    // NM
    BigDecimal resultLegTime = 0        // h
	
    boolean resultEntered = false
    String resultLegTimeInput = "00:00:00"
	
	// penalties
	int penaltyTrueHeading = 0          // Points
	int penaltyLegTime = 0              // Points
	
	
    static transients = ['resultLegTimeInput']
    
    static constraints = {
		planTrueTrack(blank:false, range:0..<360)
		planTestDistance(blank:false, min:0.toBigDecimal())
		planTrueHeading(blank:false, range:0..<360)
		planGroundSpeed(blank:false, min:0.toBigDecimal())
		planLegTime(blank:false, min:0.toBigDecimal())
		
		resultLegTimeInput(blank:false, validator:{ val, obj ->
        	switch(val.size()) {
                case 1:
                    try {
                        Date t = Date.parse("s",val)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 2:
                    try {
                        Date t = Date.parse("ss",val)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 4:
                    try {
                        Date t = Date.parse("m:ss",val)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 5:
                    try {
                        Date t = Date.parse("mm:ss",val)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 7:
                    try {
                        Date t = Date.parse("H:mm:ss",val)
                    } catch(Exception e) {
                        return false
                    }
                    return true
        		case 8:
		            try {
		           		Date t = Date.parse("HH:mm:ss",val)
		            } catch(Exception e) {
		                return false
		            }
		            return true
            }
            return false
		})
	}
	
	def messageSource
	
	String name()
	{
		GregorianCalendar time = new GregorianCalendar()
		time.set(Calendar.HOUR_OF_DAY, 0)
		time.set(Calendar.MINUTE, 0)
		if (planLegTime >= 0) {
			time.set(Calendar.SECOND, (3600 * planLegTime).toDouble().round().toInteger())
		}
		String time_str = time.getTime().format("HH:mm:ss")
		
		return "${FcMath.GradStr(planTrueHeading)}${messageSource.getMessage('fc.grad', null, null)} ${FcMath.SpeedStr(planGroundSpeed)}${messageSource.getMessage('fc.mile', null, null)} ${time_str}"
	}
	
	String planLegTimeStr()
	{
		GregorianCalendar time = new GregorianCalendar()
		time.set(Calendar.HOUR_OF_DAY, 0)
		time.set(Calendar.MINUTE, 0)
        if (planLegTime >= 0) {
			time.set(Calendar.SECOND, (3600 * planLegTime).toDouble().round().toInteger())
        }
		return time.getTime().format("HH:mm:ss")
	}
	
    String resultLegTimeStr()
    {
        GregorianCalendar time = new GregorianCalendar()
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        if (resultLegTime >= 0) {
            time.set(Calendar.SECOND, (3600 * resultLegTime).toDouble().round().toInteger())
        }
        return time.getTime().format("HH:mm:ss")
    }
    
	Date AddPlanLegTime(Date initTime)
	{
		GregorianCalendar time = new GregorianCalendar() 
		time.setTime(initTime)
		
		if (planLegTime >= 0) {
			time.add(Calendar.SECOND, (3600 * planLegTime).toDouble().round().toInteger() )
		}
	    if (planProcedureTurn && planProcedureTurnDuration) {
            time.add(Calendar.SECOND, 60 * planProcedureTurnDuration )
	    }

		return time.getTime()
	}
	
    Date AddPlanLegTime(Date initTime, BigDecimal partRatio)
    {
        GregorianCalendar time = new GregorianCalendar() 
        time.setTime(initTime)
        
        if (planLegTime >= 0) {
            time.add(Calendar.SECOND, (3600 * planLegTime * partRatio).toDouble().round().toInteger() )
        }
        if (planProcedureTurn && planProcedureTurnDuration) {
            time.add(Calendar.SECOND, 60 * planProcedureTurnDuration )
        }

        return time.getTime()
    }
}
