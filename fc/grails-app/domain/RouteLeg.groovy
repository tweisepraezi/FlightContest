import java.math.RoundingMode;
import java.text.*

class RouteLeg 
{
	String title

	BigDecimal coordTrueTrack          // Grad
	BigDecimal coordDistance           // NM
	
    BigDecimal measureTrueTrack        // Grad
	BigDecimal measureDistance         // mm, Entfernung bis zum letzten TP/SP
	BigDecimal legMeasureDistance      // mm
	BigDecimal legDistance             // NM

    static constraints = {
		title(nullable:true)
		coordTrueTrack()
		coordDistance()
		legMeasureDistance(nullable:true,range:0..<10000)
		legDistance(nullable:true)
		measureTrueTrack(nullable:true,range:0..<360)
		measureDistance(nullable:true,range:0..<10000)
    }

	def messageSource

	String coordTrueTrackName()
	{
		return "${FcMath.RouteGradStr(coordTrueTrack)}${messageSource.getMessage('fc.grad', null, null)}"
	}
	
	String coordDistanceName()
	{
		return  "${FcMath.DistanceStr(coordDistance)}${messageSource.getMessage('fc.mile', null, null)}"
	}
	
    String measureDistanceName()
    {
    	if (measureDistance != null) {
    		return  "${FcMath.DistanceMeasureStr(measureDistance)}${messageSource.getMessage('fc.mm', null, null)}"
    	}
    	return "-"
    }
    
    String mapMeasureDistanceName()
    {
    	if (legMeasureDistance != null) {
    		return  "${FcMath.DistanceMeasureStr(legMeasureDistance)}${messageSource.getMessage('fc.mm', null, null)}"
    	}
    	return "-"
    }
    
    String mapDistanceName()
    {
    	if (legDistance != null) {
    		return  "${FcMath.DistanceStr(legDistance)}${messageSource.getMessage('fc.mile', null, null)}"
    	}
    	return "-"
    }
    
    String mapMeasureTrueTrackName()
    {
    	if (measureTrueTrack != null) {
    		return  "${FcMath.RouteGradStr(measureTrueTrack)}${messageSource.getMessage('fc.grad', null, null)}"
    	}
    	return "-"
    }
    
    BigDecimal testDistance()
    {
        if (legDistance != null) {
            return FcMath.RoundDistance(legDistance)
        } else {
            return FcMath.RoundDistance(coordDistance)
        }
    }
    
    BigDecimal testTrueTrack()
    {
        if (measureTrueTrack != null) {
            return measureTrueTrack
        } else {
            return coordTrueTrack
        }
    }
    
    String coordName()
    {
        return "${coordTrueTrackName()} ${coordDistanceName()}"
    }

    String mapName()
    {
        return "${coordTrueTrackName()} ${mapDistanceName()}"
    }

	String testName()
	{
		String trueTrackStr = "${FcMath.RouteGradStr(testTrueTrack())}${messageSource.getMessage('fc.grad', null, null)}"
		if (measureTrueTrack != null) {
			trueTrackStr += " ${messageSource.getMessage('fc.distance.map.short', null, null)}"
		} else {
			trueTrackStr += " ${messageSource.getMessage('fc.distance.coord.short', null, null)}"
		}
		
		String distanceStr = "${FcMath.DistanceStr(testDistance())}${messageSource.getMessage('fc.mile', null, null)}"
		if (legDistance != null) {
			distanceStr += " (${mapMeasureDistanceName()})"
			distanceStr += " ${messageSource.getMessage('fc.distance.map.short', null, null)}"
		} else {
            distanceStr += " ${messageSource.getMessage('fc.distance.coord.short', null, null)}"
		}
		
        return  "${trueTrackStr} ${distanceStr}"
	}
}
