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

    static String RatioStr(BigDecimal ratioValue)
    {
        if (ratioValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.0")
            return df.format(ratioValue)
        }
        return ""
    }

    static String ConvertAFLOSCoordValue(String aflosCoordValue)
    // "51° 26,9035' N" -> "N 051° 27,00000'" 
    {
    	String[] a = aflosCoordValue.split()
    	
    	String gradStr = a[0]
    	while (gradStr.size() < 4) {
    		gradStr = "0$gradStr"
    	}
    	
    	String secondStr = "${a[1].substring(0,a[1].size()-1)}0'"
    	
    	return "${a[2]} $gradStr $secondStr"
    }

    static String ConvertAFLOSTime(String aflosTime)
    {
        String[] at = aflosTime.split()
	    return "${at[0].replaceFirst('h','')}:${at[1].replaceFirst('min','')}:${at[2].replaceFirst(',000sec','')}"
    }
}
