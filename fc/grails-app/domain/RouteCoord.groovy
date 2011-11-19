import java.text.*

class RouteCoord
{
	RouteCoordType type = RouteCoordType.TO
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

    // Speicher fürLandkarten-Entfernungsmessung
    BigDecimal mapmeasuredistance = 0  // mm
    BigDecimal mapdistance = 0         // NM
    
    static belongsTo = [route:Route]

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
        mapmeasuredistance()
        mapdistance()
    }

    def messageSource
    
    String title()
	{
		switch (type) {
			case RouteCoordType.SECRET:
			case RouteCoordType.TP:
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
