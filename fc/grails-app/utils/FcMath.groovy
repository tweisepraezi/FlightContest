import java.text.*
import java.util.Date;
import java.util.GregorianCalendar;
import java.math.*

class FcMath
{
    //--------------------------------------------------------------------------
	static BigDecimal AddDistance(BigDecimal distanceValue1, BigDecimal distanceValue2)
	{
		return distanceValue1.round(new MathContext(4)) + distanceValue2.round(new MathContext(4))
	}
	
    //--------------------------------------------------------------------------
	static BigDecimal RoundDistance(BigDecimal distanceValue)
    // NM
	{
		return distanceValue.setScale(2, RoundingMode.HALF_EVEN)
	}
	
    //--------------------------------------------------------------------------
    static BigDecimal RoundMeasureDistance(BigDecimal measureValue)
    // mm
    {
        return measureValue.setScale(1, RoundingMode.HALF_EVEN)
    }
    
    //--------------------------------------------------------------------------
	static BigDecimal RoundTrackpointDistance(BigDecimal distanceValue)
    // m
	{
		return distanceValue.setScale(0, RoundingMode.HALF_EVEN)
	}
	
    //--------------------------------------------------------------------------
	static String DistanceStr(BigDecimal distanceValue)
	{
		if (distanceValue != null) {
			DecimalFormat df = new DecimalFormat("#0.00")
			return df.format(distanceValue)
		}
		return ""
	}

    //--------------------------------------------------------------------------
    static String DistanceStr2(BigDecimal distanceValue)
    {
        if (distanceValue != null) {
            DecimalFormat df = new DecimalFormat("#0.00")
            return df.format(distanceValue).replaceAll(',','.')
        }
        return ""
    }

    //--------------------------------------------------------------------------
    static String DistanceMeasureStr(BigDecimal distanceValue)
    {
        if (distanceValue != null) {
            DecimalFormat df = new DecimalFormat("0.0#")
            return df.format(distanceValue)
        }
        return ""
    }

    //--------------------------------------------------------------------------
    static String DistanceMeasureStr2(BigDecimal distanceValue)
    {
        if (distanceValue != null) {
            DecimalFormat df = new DecimalFormat("0.0#")
            return df.format(distanceValue).replaceAll(',','.')
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal GradDiff(BigDecimal gradValue1, BigDecimal gradValue2)
    {
        BigDecimal grad_diff = gradValue2 - gradValue1
        if (grad_diff > 180) {
            grad_diff -= 360
        } else if (grad_diff < -180) {
            grad_diff += 360
        }
        return grad_diff
    }
    
    //--------------------------------------------------------------------------
	static int RoundGrad(BigDecimal gradValue)
	{
        int round_grad = gradValue.setScale(0, RoundingMode.HALF_EVEN).toInteger()
        if (round_grad < 0) {
            round_grad += 360
        } else if (round_grad >= 360) {
            round_grad -= 360
        }
		return round_grad
	}
	
    //--------------------------------------------------------------------------
	static int RoundDiffGrad(BigDecimal gradDiffValue)
	{
		return gradDiffValue.setScale(0, RoundingMode.HALF_DOWN).toInteger()
	}
	
    //--------------------------------------------------------------------------
    static String GradStr(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000")
            return df.format(gradValue)
        }
        return ""
    }
	
    //--------------------------------------------------------------------------
    static String GradStr(int gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000")
            return df.format(gradValue)
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static String GradStrComma(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000.#####")
            return df.format(gradValue)
        }
        return ""
    }
	
    //--------------------------------------------------------------------------
    static String GradStrMinus(BigDecimal gradValue)
    {
        DecimalFormat df = new DecimalFormat("0")
        return df.format(gradValue)
    }
	
    //--------------------------------------------------------------------------
    static String RouteGradStr(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000.00")
            return df.format(gradValue)
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static String RouteGradStr2(BigDecimal gradValue)
    {
        if (gradValue >= 0) {
            DecimalFormat df = new DecimalFormat("000.00")
            return df.format(gradValue).replaceAll(',','.')
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static int RoundAltitude(BigDecimal altitudeValue)
    {
        return altitudeValue.setScale(0, RoundingMode.HALF_EVEN).toInteger()
    }
    
    //--------------------------------------------------------------------------
    static String RoundAltitudem(BigDecimal altitudeValue)
    // altitudeValue in m
    {
        DecimalFormat df = new DecimalFormat("0.00")
        return df.format(altitudeValue).replace(',','.')
    }
    
    //--------------------------------------------------------------------------
	static int Seconds(BigDecimal timeValue)
	{
		BigDecimal seconds = (timeValue * 3600).setScale(0, RoundingMode.HALF_EVEN)
		return seconds.toInteger()
	}
	
    //--------------------------------------------------------------------------
	static int Seconds(Date dateValue)
	{
		GregorianCalendar time = new GregorianCalendar()
		time.setTime(dateValue)
		int seconds = time.get(Calendar.SECOND) + 60 * time.get(Calendar.MINUTE) + 3600 * time.get(Calendar.HOUR_OF_DAY)
		return seconds
	}
	
    //--------------------------------------------------------------------------
	static BigDecimal Hours(int minutesValue)
	{
		return 	minutesValue / 60
	}
	
    //--------------------------------------------------------------------------
	static int RatioSeconds(BigDecimal timeValue, BigDecimal ratioValue)
	{
		int ret = Seconds(timeValue * ratioValue)
		return ret
	}
	
    //--------------------------------------------------------------------------
	static void SetFullMinute(GregorianCalendar time)
	{
		int second = time.get(Calendar.SECOND)
		if (second > 0) {
			time.add(Calendar.SECOND, 60 - second)
		}
	}
	
    //--------------------------------------------------------------------------
    static BigDecimal TimeDiff(Date startTime, Date endTime)
    {
        int start_seconds = Seconds(startTime)
        int end_seconds = Seconds(endTime)
        return (end_seconds - start_seconds)/3600
    }
    
    //--------------------------------------------------------------------------
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
	
    //--------------------------------------------------------------------------
    static String TimeStrShort(BigDecimal timeValue)
    {
        GregorianCalendar time = new GregorianCalendar()
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        if (timeValue >= 0) {
            time.set(Calendar.SECOND, Seconds(timeValue))
        }
        return time.getTime().format("HH:mm")
    }
    
    //--------------------------------------------------------------------------
    static String TimeStrMin(BigDecimal timeValue)
    {
        GregorianCalendar time = new GregorianCalendar()
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        if (timeValue >= 0) {
            time.set(Calendar.SECOND, Seconds(timeValue))
        }
        return time.getTime().format("m:ss")
    }
    
    //--------------------------------------------------------------------------
	static String TimeStr(Date dateValue)
	{
		if (dateValue) {
			return dateValue.format('HH:mm:ss')
		}
		return ""
	}
	
    //--------------------------------------------------------------------------
	static String TimeStrShort(Date dateValue)
	{
		if (dateValue) {
			return dateValue.format('HH:mm')
		}
		return ""
	}
	
    //--------------------------------------------------------------------------
    static String SpeedStr_TAS(BigDecimal speedValue)
    {
        if (speedValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.#")
            return df.format(speedValue)
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static String SpeedStr_Planning(BigDecimal speedValue)
    {
        if (speedValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.00")
            return df.format(speedValue)
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static String SpeedStr_Flight(BigDecimal speedValue)
    {
        if (speedValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.0")
            return df.format(speedValue)
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static String RatioStr(BigDecimal ratioValue)
    {
        if (ratioValue >= 0) {
            DecimalFormat df = new DecimalFormat("#0.0")
            return df.format(ratioValue)
        }
        return ""
    }

    //--------------------------------------------------------------------------
    static String GateWidthStr(Float gateWidthValue)
    {
        if (gateWidthValue) {
            DecimalFormat df = new DecimalFormat("0.#")
            return df.format(gateWidthValue)
        }
        return "0"
    }

    //--------------------------------------------------------------------------
    static String EnrouteValueStr(Float enrouteValue)
    {
        if (enrouteValue) {
            DecimalFormat df = new DecimalFormat("0.#")
            return df.format(enrouteValue)
        }
        return "0"
    }

    //--------------------------------------------------------------------------
    static int CalculateLandingPenalties(String measureValue, String calculatorValue)
    {
        int penalty_result = 0
        if (measureValue) {
            try {
                String landing_measure = "'${measureValue}'"
                Object penalty_calculator = Eval.me(calculatorValue)
                penalty_result = Eval.me("penalty_calculator",penalty_calculator,"penalty_calculator(${landing_measure})")
            } catch (Exception e) {
                penalty_result = -1
            }
        }
        return penalty_result
    }
    
    //--------------------------------------------------------------------------
    static int CalculateLandingPenalties2(String measureValue, String calculatorValue)
    {
        int ret = CalculateLandingPenalties(measureValue, calculatorValue)
        if (ret == -1) {
            ret = 0
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    static String GetPrintLandingCalculatorValues(String printValues, String calculatorValue)
    {
        String s = ""
        for (String value in printValues.split(',')) {
            if (s) {
                s += ", "
            }
            s += "${value}: ${CalculateLandingPenalties2(value, calculatorValue)}"
        }
        return s
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal toBigDecimal(String stringValue)
    {
        stringValue = stringValue.replace(',','.')
        return stringValue.toBigDecimal()
    }
    
    //--------------------------------------------------------------------------
    static String GetSortPhotoName(String photoName)
    {
        if (photoName.isInteger() && (photoName.toInteger() >= 0)) {
            while (photoName.size() < 4) {
                photoName = "0${photoName}"
            }
        }
        return photoName
    }
    
    //--------------------------------------------------------------------------
    static int GetLandingPenalties(BigDecimal landingResultsFactor, int landingPenalties)
    {
        if (landingResultsFactor) {
            return (landingResultsFactor*landingPenalties).toInteger()
        }
        return landingPenalties
    }
    
    //--------------------------------------------------------------------------
    static int toInteger(String numberValue)
    {
        if (numberValue.isBigDecimal()) {
            return numberValue.toBigDecimal().setScale(0, RoundingMode.HALF_EVEN).toInteger()
        }
        return 0
    }
}