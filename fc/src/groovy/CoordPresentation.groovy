import java.text.DecimalFormat
import java.util.List;

enum CoordPresentation
{
    DEGREE (            'fc.coordpresentation.degree'),
    DEGREEMINUTE (      'fc.coordpresentation.degree.minute'),
    DEGREEMINUTESECOND ('fc.coordpresentation.degree.minute.second')
    
    //--------------------------------------------------------------------------
    CoordPresentation(String code)
    {
        this.code = code
    }

    //--------------------------------------------------------------------------
    static final String LAT = "Lat"
    static final String LON = "Lon"
    
    //--------------------------------------------------------------------------
    final String code
    
    //--------------------------------------------------------------------------
    static List GetValues()
    {
        List l = []
        l += CoordPresentation.DEGREE
        l += CoordPresentation.DEGREEMINUTE
        l += CoordPresentation.DEGREEMINUTESECOND
        return l
    }
    
    //--------------------------------------------------------------------------
    static List GetCodes()
    {
        List l = []
        l += CoordPresentation.DEGREE.code
        l += CoordPresentation.DEGREEMINUTE.code
        l += CoordPresentation.DEGREEMINUTESECOND.code
        return l
    }
    
    //--------------------------------------------------------------------------
    static String IntegerGradStr(int gradValue, boolean isLatitude)
    // 1 -> 001
    {
        DecimalFormat df = null
        if (isLatitude) {
            df = new DecimalFormat("00")
        } else {
            df = new DecimalFormat("000")
        }
        return df.format(gradValue)
    }
    
    //--------------------------------------------------------------------------
    static String DecimalGradStr(BigDecimal decimalGrad)
    {
        DecimalFormat df = new DecimalFormat("0.00000")
        return df.format(decimalGrad)
    }

    //--------------------------------------------------------------------------
    static String IntegerMinuteStr(BigDecimal decimalMin)
    // 12.9134 -> 12
    {
        int min_value = decimalMin.toInteger()
        DecimalFormat df = new DecimalFormat("00")
        return df.format(min_value)
    }

    //--------------------------------------------------------------------------
    static String DecimalMinuteStr(BigDecimal decimalMin)
    {
        DecimalFormat df = new DecimalFormat("00.00000")
        return df.format(decimalMin)
    }
    
    //--------------------------------------------------------------------------
    static String DecimalSecondStr(BigDecimal decimalMin)
    // 12.25 -> 15
    {
        BigDecimal second_value = GetSecond(decimalMin)
        DecimalFormat df = new DecimalFormat("00.0000")
        return df.format(second_value)
    }

    //--------------------------------------------------------------------------
    static BigDecimal GetSecond(BigDecimal decimalMin)
    // 12.25 -> 15
    {
        int min_value = decimalMin.toInteger()
        return 60 * (decimalMin - min_value)
    }
    
    //--------------------------------------------------------------------------
    static Map GetDirectionGradDecimalMinute(BigDecimal decimalGrad, boolean isLatitude)
    {
        int grad_value = decimalGrad.toInteger()
        BigDecimal minute_value = 60 * (decimalGrad - grad_value)
        String direction_value = ''
        if (isLatitude) {
            direction_value = 'N'
            if (grad_value < 0) {
                direction_value = 'S'
            }
        } else {
            direction_value = 'E'
            if (grad_value < 0) {
                direction_value = 'W'
            }
        }
        if (grad_value < 0) {
            grad_value *= -1
        }
        if (minute_value < 0) {
            minute_value *= -1
        }
        return [direction:direction_value, grad:grad_value, minute:minute_value]
    }
}
