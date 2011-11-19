import java.text.*

class RouteLeg 
{
	String title
	BigDecimal trueTrack               // Grad
	BigDecimal coordDistance           // NM
	BigDecimal mapmeasuredistance = 0  // mm
	BigDecimal mapdistance = 0         // NM
	
    static constraints = {
		title(nullable:true)
		trueTrack()
		coordDistance()
		mapmeasuredistance(range:0..<10000)
		mapdistance()
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
        return  "${FcMath.DistanceMeasureStr(mapmeasuredistance)}${messageSource.getMessage('fc.mm', null, null)}"
    }
    
    String mapDistanceName()
    {
        return  "${FcMath.DistanceStr(mapdistance)}${messageSource.getMessage('fc.mile', null, null)}"
    }
    
    BigDecimal testDistance()
    {
        if (mapdistance > 0) {
            return mapdistance
        } else {
            return coordDistance
        }
    }
    
    String testDistanceName()
    {
        if (mapdistance > 0) {
        	return  "${FcMath.DistanceStr(testDistance())}${messageSource.getMessage('fc.mile', null, null)} ${messageSource.getMessage('fc.distance.map.short', null, null)}"
        } else {
        	return  "${FcMath.DistanceStr(testDistance())}${messageSource.getMessage('fc.mile', null, null)} ${messageSource.getMessage('fc.distance.coord.short', null, null)}"
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
		return "${trueTrackName()} ${testDistanceName()}"
	}
}
