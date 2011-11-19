class RouteCoord
{
	// Latitude (Geographische Breite)
	int latGrad
	BigDecimal latMinute = 0.0
	String latDirection = 'N'

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
        return "${latGrad}${messageSource.getMessage('fc.grad', null, null)}${latMinute}${messageSource.getMessage('fc.min', null, null)}${latDirection}"
    }

    // Longitude (Geographische Laenge)
	int lonGrad
	BigDecimal lonMinute = 0.0
	String lonDirection = 'E'

	BigDecimal lonMath()
	{
		BigDecimal ret = lonGrad + lonMinute/60
		if (lonDirection == 'E') {
			return ret
		} else {
			return -ret
		}
	}
	
	Route route
	static belongsTo = Route

    String lonName()
    {
        return "${lonGrad}${messageSource.getMessage('fc.grad', null, null)}${lonMinute}${messageSource.getMessage('fc.min', null, null)}${lonDirection}"
    }
	
	// Sonstiges
	static constraints = {
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
	}

	def messageSource
	
	String name()
	{
		return "${latName()} ${lonName()}"
	}
}
