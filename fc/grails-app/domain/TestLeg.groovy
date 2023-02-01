import java.text.*

class TestLeg
{
	CoordTitle coordTitle               // DB-2.5
	
	// plan
	BigDecimal planTrueTrack = 0        // Grad
	BigDecimal planTestDistance = 0     // NM 
	
	BigDecimal planTrueHeading = 0      // Grad
	BigDecimal planGroundSpeed = 0      // NM
	BigDecimal planLegTime = 0          // h
	Boolean planFullMinute = false      // DB-2.7

	boolean planProcedureTurn = false
	int planProcedureTurnDuration = 0   // min
	
    Boolean noPlanningTest = false      // DB-2.8
    Boolean endCurved = false           // DB-2.8
    
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
		planTrueTrack(blank:false, range:0..360, scale:10)
		planTestDistance(blank:false, min:0.toBigDecimal(), scale:10)
		planTrueHeading(blank:false, range:0..360, scale:10)
		planGroundSpeed(blank:false, min:0.toBigDecimal(), scale:10)
		planLegTime(blank:false, min:0.toBigDecimal(), scale:10)
		
		resultTrueTrack(scale:10)
		resultTestDistance(scale:10)
		resultTrueHeading(scale:10)
		resultGroundSpeed(scale:10)
		resultLegTime(scale:10)
		
		resultLegTimeInput(blank:false, validator:{ val, obj ->
			String v = FcTime.GetInputTimeStr(val)
        	switch(v.size()) {
                case 1:
                    try {
                        Date t = Date.parse("s",v)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 2:
                    try {
                        Date t = Date.parse("ss",v)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 4:
                    try {
                        Date t = Date.parse("m:ss",v)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 5:
                    try {
                        Date t = Date.parse("mm:ss",v)
                    } catch(Exception e) {
                        return false
                    }
                    return true
                case 7:
                    try {
                        Date t = Date.parse("H:mm:ss",v)
                    } catch(Exception e) {
                        return false
                    }
                    return true
        		case 8:
		            try {
		           		Date t = Date.parse("HH:mm:ss",v)
		            } catch(Exception e) {
		                return false
		            }
		            return true
            }
            return false
		})
		
		// DB-2.5 compatibility
		coordTitle(nullable:true)
		
		// DB-2.7 compatibility
		planFullMinute(nullable:true)
        
        // DB-2.8 compatibility
        noPlanningTest(nullable:true)
        endCurved(nullable:true)
	}
	
    void ResetResults()
    {
        resultTrueTrack = 0
        resultTestDistance = 0 
        resultTrueHeading = 0
        resultGroundSpeed = 0
        resultLegTime = 0
        resultLegTimeInput = "00:00:00"
        resultEntered = false
        penaltyTrueHeading = 0
        penaltyLegTime = 0
    }

	String name()
	{
		return "${FcMath.GradStr(planTrueHeading)}${getMsg('fc.grad')} ${FcMath.SpeedStr_Planning(planGroundSpeed)}${getMsg('fc.mile')} ${FcMath.TimeStr(planLegTime)}"
	}
	
	String givenName()
	{
		return "${getMsg('fc.test.results.given')} ${coordTitle.titleCode()}: ${FcMath.GradStrComma(resultTrueHeading)}${getMsg('fc.grad')} ${FcMath.TimeStr(resultLegTime)}."
	}
	
	String planLegTimeStr()
	{
		return FcMath.TimeStr(planLegTime)
	}
	
    String resultLegTimeStr()
    {
		return FcMath.TimeStr(resultLegTime)
    }
    
	Date AddPlanLegTime(Date initTime)
	{
		GregorianCalendar time = new GregorianCalendar() 
		time.setTime(initTime)
		
		if (planLegTime >= 0) {
			time.add(Calendar.SECOND, FcMath.Seconds(planLegTime))
		}
		if (planFullMinute) {
			FcMath.SetFullMinute(time)
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
			time.add(Calendar.SECOND, FcMath.RatioSeconds(planLegTime, partRatio))
        }
		if (planFullMinute) {
			FcMath.SetFullMinute(time)
		}
        if (planProcedureTurn && planProcedureTurnDuration) {
            time.add(Calendar.SECOND, 60 * planProcedureTurnDuration )
        }

        return time.getTime()
    }
	
	BigDecimal AddPlanLegTime(BigDecimal timeValue, Date initTime)
	{
        GregorianCalendar time = new GregorianCalendar()
        time.setTime(initTime)
		int start_seconds = FcMath.Seconds(initTime)
		
		time.add(Calendar.SECOND, FcMath.Seconds(timeValue))
		
		if (planLegTime >= 0) {
			time.add(Calendar.SECOND, FcMath.Seconds(planLegTime))
		}
		
		if (planFullMinute) {
			FcMath.SetFullMinute(time)
		}
		int end_seconds = FcMath.Seconds(time.getTime())

		//return (FcMath.Seconds(timeValue) + FcMath.Seconds(planLegTime))/3600
		return (end_seconds - start_seconds)/3600
	}
}
