import java.text.*

class RouteLeg 
{
	String title
	BigDecimal trueTrack               // Grad
	BigDecimal coordDistance           // NM
	
	BigDecimal mapmeasuredistance      // mm
	BigDecimal mapdistance             // NM
    BigDecimal mapmeasuretruetrack     // Grad
	
    static constraints = {
		title(nullable:true)
		trueTrack()
		coordDistance()
		mapmeasuredistance(nullable:true,range:0..<10000)
		mapdistance(nullable:true)
		mapmeasuretruetrack(nullable:true,range:0..<360)
    }

	def messageSource

	String trueTrackName()
	{
		return "${FcMath.RouteGradStr(trueTrack)}${messageSource.getMessage('fc.grad', null, null)}"
	}
	
	String coordDistanceName()
	{
		return  "${FcMath.DistanceStr(coordDistance)}${messageSource.getMessage('fc.mile', null, null)}"
	}
	
    String mapMeasureDistanceName()
    {
    	if (mapmeasuredistance != null) {
    		return  "${FcMath.DistanceMeasureStr(mapmeasuredistance)}${messageSource.getMessage('fc.mm', null, null)}"
    	}
    	return "-"
    }
    
    String mapDistanceName()
    {
    	if (mapdistance != null) {
    		return  "${FcMath.DistanceStr(mapdistance)}${messageSource.getMessage('fc.mile', null, null)}"
    	}
    	return "-"
    }
    
    String mapMeasureTrueTrackName()
    {
    	if (mapmeasuretruetrack != null) {
    		return  "${FcMath.RouteGradStr(mapmeasuretruetrack)}${messageSource.getMessage('fc.grad', null, null)}"
    	}
    	return "-"
    }
    
    BigDecimal testDistance()
    {
        if (mapdistance != null) {
            return mapdistance
        } else {
            return coordDistance
        }
    }
    
    BigDecimal testTrueTrack()
    {
        if (mapmeasuretruetrack != null) {
            return mapmeasuretruetrack
        } else {
            return trueTrack
        }
    }
    
    String coordName()
    {
        return "${trueTrackName()} ${coordDistanceName()}"
    }

    String mapName()
    {
        return "${trueTrackName()} ${mapDistanceName()}"
    }

	String testName()
	{
		String trueTrackStr = "${FcMath.RouteGradStr(testTrueTrack())}${messageSource.getMessage('fc.grad', null, null)}"
		if (mapmeasuretruetrack != null) {
			trueTrackStr += " ${messageSource.getMessage('fc.distance.map.short', null, null)}"
		} else {
			trueTrackStr += " ${messageSource.getMessage('fc.distance.coord.short', null, null)}"
		}
		
		String distanceStr = "${FcMath.DistanceStr(testDistance())}${messageSource.getMessage('fc.mile', null, null)}"
		if (mapdistance != null) {
			distanceStr += " ${messageSource.getMessage('fc.distance.map.short', null, null)}"
		} else {
            distanceStr += " ${messageSource.getMessage('fc.distance.coord.short', null, null)}"
		}
		
        return  "${trueTrackStr} ${distanceStr}"
	}
}
