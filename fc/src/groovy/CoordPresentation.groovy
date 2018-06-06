import java.math.BigDecimal
import java.math.RoundingMode
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
    final static String LAT = "Lat"
    final static String LON = "Lon"
    final static String GRAD = (char)176
    final static String GRADMIN = "'"
    final static String GRADSEC = '"'
    final static String GRADSEC2 = (char)698
    final static String NORTH = 'N'
    final static String SOUTH = 'S'
    final static String EAST = 'E'
    final static String WEST = 'W'
    
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
    static String IntegerMinuteStr2(BigDecimal decimalMin)
    // 12.9134 -> 13
    {
        decimalMin = decimalMin.setScale(0, RoundingMode.HALF_EVEN)
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
    static String IntegerSecondStr(BigDecimal decimalMin)
    // 12.9134 -> 12
    {
        int second_value = GetSecond(decimalMin).toInteger()
        DecimalFormat df = new DecimalFormat("00")
        return df.format(second_value)
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
            direction_value = NORTH
            if (grad_value < 0) {
                direction_value = SOUTH
            }
        } else {
            direction_value = EAST
            if (grad_value < 0) {
                direction_value = WEST
            }
        }
        if (grad_value < 0) {
            grad_value *= -1
        }
        if (minute_value < 0) {
            minute_value *= -1
        }
        return [invalid:false, direction:direction_value, grad:grad_value, minute:minute_value]
    }
    
    //--------------------------------------------------------------------------
    static BigDecimal GetDecimalGrad(Map gradDecimalMinute, boolean isLatitude)
    {
        String direction = NORTH
        if (!isLatitude) {
            direction = EAST
        }
        
        BigDecimal ret = gradDecimalMinute.grad + gradDecimalMinute.minute/60
        if (gradDecimalMinute.direction == direction) {
            return ret
        } else {
            return -ret
        }
    }
    
    //--------------------------------------------------------------------------
    Map GetDirectionGradDecimalMinute(String coordStr, boolean isPraefix, boolean isLatitude)
    {
        Map ret = [invalid:true, direction:NORTH, grad:0, minute:0]
        
        String praefix = LAT
        String direction1 = NORTH
        String direction2 = SOUTH
        if (!isLatitude) {
            praefix = LON
            direction1 = EAST
            direction2 = WEST
        }
        
        switch (this) {
            case CoordPresentation.DEGREEMINUTE:
                if (coordStr.endsWith(direction1) || coordStr.endsWith(direction2)) {
                    if (!isPraefix || coordStr.startsWith(praefix + ' ')) {
                        String s = coordStr
                        if (isPraefix) {
                            s = s.substring(praefix.size()).trim()
                        }
                        while (s.contains('  ')) {
                            s = s.replaceAll('  ', ' ')
                        }
                        def coord_values = s.split(' ')
                        if (coord_values.size() == 3) {
                            if (coord_values[0].endsWith(GRAD) && coord_values[1].endsWith(GRADMIN)) {
                                String grad_str = coord_values[0].trim()
                                grad_str = grad_str.substring(0,grad_str.size()-GRAD.size())
                                String min_str = coord_values[1].trim()
                                min_str = min_str.substring(0,min_str.size()-GRADMIN.size())
                                if (grad_str.isInteger() && min_str.isBigDecimal()) {
                                    ret.grad = grad_str.toInteger()
                                    ret.minute = min_str.toBigDecimal()
                                    ret.direction = coord_values[2].trim()
                                    ret.invalid = false
                                }
                            }
                        }
                    }
                }
                break
            case CoordPresentation.DEGREEMINUTESECOND:
                if (coordStr.endsWith(direction1) || coordStr.endsWith(direction2)) {
                    if (!isPraefix || coordStr.startsWith(praefix + ' ')) {
                        String s = coordStr
                        if (isPraefix) {
                            s = s.substring(praefix.size()).trim()
                        }
                        while (s.contains('  ')) {
                            s = s.replaceAll('  ', ' ')
                        }
                        def coord_values = s.split(' ')
                        if (coord_values.size() == 4) {
                            if (coord_values[0].endsWith(GRAD) && coord_values[1].endsWith(GRADMIN) && coord_values[2].endsWith(GRADSEC)) {
                                String grad_str = coord_values[0].trim()
                                grad_str = grad_str.substring(0,grad_str.size()-GRAD.size())
                                String min_str = coord_values[1].trim()
                                min_str = min_str.substring(0,min_str.size()-GRADMIN.size())
                                String sec_str = coord_values[2].trim()
                                sec_str = sec_str.substring(0,sec_str.size()-GRADSEC.size())
                                if (grad_str.isInteger() && min_str.isInteger() && sec_str.isBigDecimal()) {
                                    ret.grad = grad_str.toInteger()
                                    ret.minute = min_str.toBigDecimal() + sec_str.toBigDecimal() / 60
                                    ret.direction = coord_values[3].trim()
                                    ret.invalid = false
                                }
                            }
                        }
                    }
                }
                break
            case CoordPresentation.DEGREE:
                if (coordStr.endsWith(GRAD)) {
                    if (!isPraefix || coordStr.startsWith(praefix + ' ')) {
                        String s = coordStr
                        if (isPraefix) {
                            s = s.substring(praefix.size()).trim()
                        }
                        s = s.substring(0,s.size()-GRAD.size())
                        if (s.isBigDecimal()) {
                            return CoordPresentation.GetDirectionGradDecimalMinute(s.toBigDecimal(), isLatitude)
                        }
                    }
                }
                break
        }
        
        return ret
    }

    //--------------------------------------------------------------------------
    String GetCoordName(Coord coordInstance, boolean isLatitude)
    {
        String praefix = LAT
        if (!isLatitude) {
            praefix = LON
        }
        
        if (isLatitude) {
            switch (this) {
                case CoordPresentation.DEGREEMINUTE:
                    return "${praefix} ${IntegerGradStr(coordInstance.latGrad,isLatitude)}${GRAD} ${DecimalMinuteStr(coordInstance.latMinute)}${GRADMIN} ${coordInstance.latDirection}"
                case CoordPresentation.DEGREEMINUTESECOND:
                    return "${praefix} ${IntegerGradStr(coordInstance.latGrad,isLatitude)}${GRAD} ${IntegerMinuteStr(coordInstance.latMinute)}${GRADMIN} ${DecimalSecondStr(coordInstance.latMinute)}${GRADSEC} ${coordInstance.latDirection}"
                case CoordPresentation.DEGREE:
                    return "${praefix} ${DecimalGradStr(coordInstance.latMath())}${GRAD}"
            }
        } else {
            switch (this) {
                case CoordPresentation.DEGREEMINUTE:
                    return "${praefix} ${IntegerGradStr(coordInstance.lonGrad,isLatitude)}${GRAD} ${DecimalMinuteStr(coordInstance.lonMinute)}${GRADMIN} ${coordInstance.lonDirection}"
                case CoordPresentation.DEGREEMINUTESECOND:
                    return "${praefix} ${IntegerGradStr(coordInstance.lonGrad,isLatitude)}${GRAD} ${IntegerMinuteStr(coordInstance.lonMinute)}${GRADMIN} ${DecimalSecondStr(coordInstance.lonMinute)}${GRADSEC} ${coordInstance.lonDirection}"
                case CoordPresentation.DEGREE:
                    return "${praefix} ${DecimalGradStr(coordInstance.lonMath())}${GRAD}"
            }
        }
        
        return ""
    }

    //--------------------------------------------------------------------------
    String GetMapName(BigDecimal decimalGrad, boolean isLatitude)
    {
        Map v = GetDirectionGradDecimalMinute(decimalGrad, isLatitude)
        switch (this) {
            case CoordPresentation.DEGREEMINUTE:
                String min_str = IntegerMinuteStr2(v.minute)
                if (min_str == "60") {
                    v.grad++
                    min_str = "00"
                }
                return "${v.grad}${GRAD} ${min_str}${GRADMIN} ${v.direction}"
            case CoordPresentation.DEGREEMINUTESECOND:
                return "${v.grad}${GRAD} ${IntegerMinuteStr(v.minute)}${GRADMIN} ${IntegerSecondStr(v.minute)}${GRADSEC2} ${v.direction}"
            case CoordPresentation.DEGREE:
                return "${DecimalGradStr(decimalGrad)}${GRAD}"
        }
        return ""
    }

}
