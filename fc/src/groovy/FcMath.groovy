import java.text.*
import java.util.GregorianCalendar;
import java.math.*

class FcMath
{
	static BigDecimal AddDistance(BigDecimal distanceValue1, BigDecimal distanceValue2)
	{
		return distanceValue1.round(new MathContext(4)) + distanceValue2.round(new MathContext(4))
	}
	
	static BigDecimal RoundDistance(BigDecimal distanceValue)
	{
		return distanceValue.setScale(2, RoundingMode.HALF_EVEN)
	}
	
	static String DistanceStr(BigDecimal distanceValue)
	{
		if (distanceValue >= 0) {
			DecimalFormat df = new DecimalFormat("#0.00")
			return df.format(distanceValue)
		}
		return ""
	}

	static int RoundGrad(BigDecimal gradValue)
	{
		return gradValue.setScale(0, RoundingMode.HALF_EVEN).toInteger()
	}
	
	static int RoundDiffGrad(BigDecimal gradDiffValue)
	{
		return gradDiffValue.setScale(0, RoundingMode.HALF_DOWN).toInteger()
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
    
    static String GradStrComma(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000.#####")
            return df.format(gradValue)
        }
        return ""
    }
	
    static String GradStrMinus(BigDecimal gradValue)
    {
        DecimalFormat df = new DecimalFormat("0")
        return df.format(gradValue)
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

	static int Seconds(BigDecimal timeValue)
	{
		BigDecimal seconds = (timeValue * 3600).setScale(0, RoundingMode.HALF_EVEN)
		return seconds.toInteger()
	}
	
	static int Seconds(Date dateValue)
	{
		GregorianCalendar time = new GregorianCalendar()
		time.setTime(dateValue)
		int seconds = time.get(Calendar.SECOND) + 60 * time.get(Calendar.MINUTE) + 3600 * time.get(Calendar.HOUR_OF_DAY)
		return seconds
	}
	
	static BigDecimal Hours(int minutesValue)
	{
		return 	minutesValue / 60
	}
	
	static int RatioSeconds(BigDecimal timeValue, BigDecimal ratioValue)
	{
		int ret = Seconds(timeValue * ratioValue)
		return ret
	}
	
    //--------------------------------------------------------------------------
	static void SetFullMinute(CoordType coordType, GregorianCalendar time)
	{
		int second = time.get(Calendar.SECOND)
		if (second > 0) {
			time.add(Calendar.SECOND, 60 - second)
		}
	}
	
	static String TimeStr(BigDecimal timeValue)
	{
		GregorianCalendar time = new GregorianCalendar()
		time.set(Calendar.HOUR_OF_DAY, 0)
		time.set(Calendar.MINUTE, 0)
		if (timeValue >= 0) {
			time.set(Calendar.SECOND, Seconds(timeValue))
		}
		return time.getTime().format("HH:mm:ss")
	}
	
	static String TimeStr(Date dateValue)
	{
		if (dateValue) {
			return dateValue.format('HH:mm:ss')
		}
		return ""
	}
	
	static String TimeStrShort(Date dateValue)
	{
		if (dateValue) {
			return dateValue.format('HH:mm')
		}
		return ""
	}
	
    static String SpeedStr_Planning(BigDecimal speedValue)
    {
        if (speedValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.00")
            return df.format(speedValue)
        }
        return ""
    }
    
    static String SpeedStr_Flight(BigDecimal speedValue)
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
            DecimalFormat df = new DecimalFormat("0.0#")
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
		gradStr = "${gradStr.substring(0,gradStr.size()-1)}\u00b0" // ° -> \u00b0 
    	
    	String secondStr = "${a[1].substring(0,a[1].size()-1)}0'"
		
    	return "${a[2]} $gradStr $secondStr"
    }

    static String ConvertAFLOSTime(String aflosTime)
    {
        String[] at = aflosTime.split()
		
		String aflos_hour = at[0].replaceFirst('h','')
		String aflos_minute = at[1].replaceFirst('min','')
		String aflos_seconds = at[2].replaceFirst('sec','') // old at[2].replaceFirst(',000sec','')
		aflos_seconds = aflos_seconds.replaceFirst(',','.')
		BigDecimal seconds_decimal = aflos_seconds.toBigDecimal() 
		BigDecimal seconds_decimal_rounded = seconds_decimal.setScale(0, RoundingMode.HALF_EVEN)
		DecimalFormat df = new DecimalFormat("00")
		String seconds = df.format(seconds_decimal_rounded)

		return "$aflos_hour:$aflos_minute:$seconds"
    }
	
    static String GateWidthStr(Float gateWidthValue)
    {
        if (gateWidthValue) {
            DecimalFormat df = new DecimalFormat("0.#")
            return df.format(gateWidthValue)
        }
        return "0"
    }

}
