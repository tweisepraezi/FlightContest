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
		coordTrueTrack(scale:10)
		coordDistance(scale:10)
		measureTrueTrack(nullable:true,range:0..<360,scale:10)
		measureDistance(nullable:true,range:0..<10000,scale:10)
		legMeasureDistance(nullable:true,range:0..<10000,scale:10)
		legDistance(nullable:true,scale:10)
    }

	void CopyValues(RouteLeg routeLegInstance)
	{
		title = routeLegInstance.title
		coordTrueTrack = routeLegInstance.coordTrueTrack
		coordDistance = routeLegInstance.coordDistance
		measureTrueTrack = routeLegInstance.measureTrueTrack
		measureDistance = routeLegInstance.measureDistance
		legMeasureDistance = routeLegInstance.legMeasureDistance
		legDistance = routeLegInstance.legDistance
		
		if (!this.save()) {
			throw new Exception("RouteLeg.CopyValues could not save")
		}
	}
	
	String coordTrueTrackName()
	{
		return "${FcMath.RouteGradStr(coordTrueTrack)}${getMsg('fc.grad')}"
	}
	
	String coordDistanceName()
	{
		return  "${FcMath.DistanceStr(coordDistance)}${getMsg('fc.mile')}"
	}
	
    String measureDistanceName()
    {
    	if (measureDistance != null) {
    		return  "${FcMath.DistanceMeasureStr(measureDistance)}${getMsg('fc.mm')}"
    	}
    	return "-"
    }
    
    String mapMeasureDistanceName()
    {
    	if (legMeasureDistance != null) {
    		return  "${FcMath.DistanceMeasureStr(legMeasureDistance)}${getMsg('fc.mm')}"
    	}
    	return "-"
    }
    
    String mapDistanceName()
    {
    	if (legDistance != null) {
    		return  "${FcMath.DistanceStr(legDistance)}${getMsg('fc.mile')}"
    	}
    	return "-"
    }
    
    String mapMeasureTrueTrackName()
    {
    	if (measureTrueTrack != null) {
    		return  "${FcMath.RouteGradStr(measureTrueTrack)}${getMsg('fc.grad')}"
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
            return FcMath.RoundGrad(measureTrueTrack)
        } else {
            return FcMath.RoundGrad(coordTrueTrack)
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
		String trueTrackStr = "${FcMath.RouteGradStr(testTrueTrack())}${getMsg('fc.grad')}"
		if (measureTrueTrack != null) {
			trueTrackStr += " ${getMsg('fc.distance.map.short')}"
		} else {
			trueTrackStr += " ${getMsg('fc.distance.coord.short')}"
		}
		
		String distanceStr = "${FcMath.DistanceStr(testDistance())}${getMsg('fc.mile')}"
		if (legDistance != null) {
			distanceStr += " (${mapMeasureDistanceName()})"
			distanceStr += " ${getMsg('fc.distance.map.short')}"
		} else {
            distanceStr += " ${getMsg('fc.distance.coord.short')}"
		}
		
        return  "${trueTrackStr} ${distanceStr}"
	}
}
