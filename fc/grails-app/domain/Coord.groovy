import java.text.*

class Coord
{
	CoordType type = CoordType.TO
	int titleNumber = 1
	String mark
	
	// Latitude (Geographische Breite)
	int latGrad
	BigDecimal latMinute = 0.0
	String latDirection = 'N'

    // Longitude (Geographische Laenge)
    int lonGrad
    BigDecimal lonMinute = 0.0
    String lonDirection = 'E'

    int altitude = 500 // Altitude (Höhe) in ft 
    int gatewidth = 2 // Gate-Breite (in NM)

    // Speicher für Landkarten-Entfernungsmessung
    BigDecimal mapmeasuredistance // mm
    BigDecimal mapdistance        // NM
    
    // Speicher für Landkarten-Kursmessung
    BigDecimal mapmeasuretruetrack     // Grad
    
    // Relative Position eines Secret-Points im Leg 
    BigDecimal secretLegRatio = 0
    
    // plan, results, penalties // TODO
    Date planCpTime                     = Date.parse("HH:mm","00:00")
    boolean planProcedureTurn           = false
    String resultLatitude               = ""
    String resultLongitude              = ""
    int resultAltitude                  = 0
    Date resultCpTime                   = Date.parse("HH:mm","00:00")
    String resultCpTimeInput            = "00:00:00"  // transient
    boolean resultCpNotFound            = false
    boolean resultEntered               = false
    boolean resultProcedureTurnNotFlown = false
    boolean resultProcedureTurnEntered  = false
    boolean resultMinAltitudeMissed     = false
    int resultBadCourseNum              = 0
    int penaltyCoord                    = 0           // Points
	
    static transients = ['resultCpTimeInput']
    
    static constraints = {
		type()
        titleNumber(range:1..<100)
		mark(nullable:true)
        latGrad(range:0..90)
        latMinute(range:0..<60, validator:{val,obj->
            if (val > 0 && obj.latGrad == 90 ) {
                return false
            }
        })
        latDirection(inList:['N','S'])
        
        lonGrad(range:0..180, validator:{val,obj->
            if (val == 180 && obj.lonDirection == 'W') {
                return false
            }
        })
        lonMinute(range:0..<60, validator:{val,obj->
            if (val > 0 && obj.lonGrad == 180 ) {
                return false
            }
        })
        lonDirection(inList:['E','W'])
        altitude(range:0..100000)
        gatewidth()
        mapmeasuredistance(nullable:true)
        mapdistance(nullable:true)
        mapmeasuretruetrack(nullable:true,range:0..<360)
        resultCpTimeInput(blank:false, validator:{ val, obj ->
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
	    resultAltitude(min:0)
	    resultBadCourseNum(min:0)
    }

	void ResetResults()
	{
	    resultLatitude              = ""
	    resultLongitude             = ""
	    resultAltitude              = 0
	    resultCpTime                = Date.parse("HH:mm","00:00")
	    resultCpTimeInput           = "00:00:00"
	    resultCpNotFound            = false
	    resultEntered               = false
	    resultProcedureTurnNotFlown = false
	    resultProcedureTurnEntered  = false
	    resultMinAltitudeMissed     = false
	    resultBadCourseNum          = 0
	    penaltyCoord                = 0
	}

	def messageSource
    
    String title()
	{
		switch (type) {
			case CoordType.TP:
            case CoordType.SECRET:
				return "${type.title}${titleNumber}"
			default:
				return type.title
		}
		
	}
	
    String titleWithRatio()
    {
        switch (type) {
            case CoordType.TP:
                return "${type.title}${titleNumber}"
            case CoordType.SECRET:
                if (secretLegRatio > 0) {
                    return " ${type.title}${titleNumber} (${FcMath.RatioStr(100*secretLegRatio)}${messageSource.getMessage('fc.percent', null, null)})"
                }
                return "${type.title}${titleNumber}"
            default:
                return type.title
        }
        
    }
    
	BigDecimal latMath()
	{
		BigDecimal ret = latGrad + latMinute/60
		if (latDirection == 'N') {
			return ret
		} else {
			return -ret
		}
	}
	
    String latName()
    {
        //return "${latGrad}${messageSource.getMessage('fc.grad', null, null)}${latMinute}${messageSource.getMessage('fc.min', null, null)}${latDirection}"
    	return "${latDirection} ${FcMath.GradStr(latGrad)}${messageSource.getMessage('fc.grad', null, null)} ${FcMath.MinuteStr(latMinute)}${messageSource.getMessage('fc.min', null, null)}"
    }

	BigDecimal lonMath()
	{
		BigDecimal ret = lonGrad + lonMinute/60
		if (lonDirection == 'E') {
			return ret
		} else {
			return -ret
		}
	}
	
    String lonName()
    {
        //return "${lonGrad}${messageSource.getMessage('fc.grad', null, null)}${lonMinute}${messageSource.getMessage('fc.min', null, null)}${lonDirection}"
        return "${lonDirection} ${FcMath.GradStr(lonGrad)}${messageSource.getMessage('fc.grad', null, null)} ${FcMath.MinuteStr(lonMinute)}${messageSource.getMessage('fc.min', null, null)}"
    }

	String name()
	{
		return "${latName()} ${lonName()}"
	}
}