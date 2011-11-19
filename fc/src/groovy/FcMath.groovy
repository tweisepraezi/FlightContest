import java.text.*
import java.math.*

class FcMath
{
	static BigDecimal AddDistance(BigDecimal distanceValue1, BigDecimal distanceValue2)
	{
		return distanceValue1.round(new MathContext(4)) + distanceValue2.round(new MathContext(4))
	}
	
	static String DistanceStr(BigDecimal distanceValue)
	{
		if (distanceValue >= 0) {
			DecimalFormat df = new DecimalFormat("#0.00")
			return df.format(distanceValue)
		}
		return ""
	}

    static String GradStr(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000")
            return df.format(gradValue)
        }
        return ""
    }
	
    static String GradStr(int gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000")
            return df.format(gradValue)
        }
        return ""
    }
    
    static String RouteGradStr(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000.00")
            return df.format(gradValue)
        }
        return ""
    }
    
    static String MinuteStr(BigDecimal minValue)
    {
    	if (minValue >= 0) {
    		DecimalFormat df = new DecimalFormat("00.00000")
    		return df.format(minValue)
    	}
    	return ""
    }

    static String SpeedStr(BigDecimal speedValue)
    {
        if (speedValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.0")
            return df.format(speedValue)
        }
        return ""
    }
    
    static String DistanceMeasureStr(BigDecimal distanceValue)
    {
        if (distanceValue >= 0) {
            DecimalFormat df = new DecimalFormat("0.0")
            return df.format(distanceValue)
        }
        return ""
    }
}
