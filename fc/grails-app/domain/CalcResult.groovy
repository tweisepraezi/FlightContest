import java.text.DecimalFormat
import java.util.Date;

class CalcResult // DB-2.12
{
    String utc
    BigDecimal latitude
    BigDecimal longitude
    Integer altitude = 0
    
    CoordTitle coordTitle = null
    
    Boolean gateNotFound = false
    
    Boolean gateMissed = false
    Boolean gateFlyBy = false
    Boolean noGateMissed = false
    
    Boolean badTurn = false
    Boolean noBadTurn = false
    
    Boolean badCourse = false
    Integer badCourseSeconds = 0
    Boolean noBadCourse = false
    
    Boolean hide = false
    Boolean judgeDisabled = false
    
    static belongsTo = [loggerresult:LoggerResult]
    
    static constraints = {
        utc(size:0..30)
        latitude(range:-90..90, scale:10)
        longitude(range:-179.9999999999..180, scale:10)
        altitude()
        coordTitle(nullable:true)
        gateNotFound()
        gateMissed()
        gateFlyBy()
        noGateMissed()
        badTurn()
        noBadTurn()
        badCourse()
        badCourseSeconds(min:0)
        noBadCourse()
        hide()
        judgeDisabled()
    }
 
    //--------------------------------------------------------------------------
    boolean IsCoordTitleEqual(CoordType type, int titleNumber)
    {
        if (coordTitle) {
            if ((type == coordTitle.type) && (titleNumber == coordTitle.number)) {
                return true
            }
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    boolean IsJudgeAction()
    {
        if (badTurn || (badCourse && badCourseSeconds)) {
            return true
        }
        return false
    }
    
    //--------------------------------------------------------------------------
    String GetUTCTime()
    // "yyyy-mm-ddThh:mm:ssZ" -> hh:mm:ss
    {
        return FcTime.UTCGetTime(utc)
    }
    
    //--------------------------------------------------------------------------
    String GetLocalTime(String timeZone)
    // "yyyy-mm-ddThh:mm:ssZ" -> hh:mm:ss
    {
        Date utc_date = Date.parse("HH:mm:ss", GetUTCTime())
        Date local_date = FcTime.GetLocalTime(utc_date, timeZone)
        return FcMath.TimeStr(local_date)
    }
    
    //--------------------------------------------------------------------------
    String GetLatitudeStr()
    // N 052° 02,17190'
    {
        BigDecimal latitude_math = latitude
        boolean north = true
        if (latitude_math < 0) {
            latitude_math *= -1
            north = false
        }
        
        int grad_value = latitude_math.toInteger()
        DecimalFormat df_grad = new DecimalFormat("000")
        String grad = df_grad.format(grad_value)
        
        BigDecimal minute_value = 60*(latitude_math - grad_value)
        DecimalFormat df_minute = new DecimalFormat("00.00000")
        String minute = df_minute.format(minute_value)
        
        if (north) {
            return "N ${grad}\u00b0 ${minute}'"
        } else {
            return "S ${grad}\u00b0 ${minute}'"
        }
    }
    
    //--------------------------------------------------------------------------
    String GetLongitudeStr()
    // E 013° 44,23030'
    {
        BigDecimal longitude_math = longitude
        boolean east = true
        if (longitude_math < 0) {
            longitude_math *= -1
            east = false
        }
        
        int grad_value = longitude_math.toInteger()
        DecimalFormat df_grad = new DecimalFormat("000")
        String grad = df_grad.format(grad_value)
        
        BigDecimal minute_value = 60*(longitude_math - grad_value)
        DecimalFormat df_minute = new DecimalFormat("00.00000")
        String minute = df_minute.format(minute_value)
        
        if (east) {
            return "E ${grad}\u00b0 ${minute}'"
        } else {
            return "W ${grad}\u00b0 ${minute}'"
        }
    }
}
