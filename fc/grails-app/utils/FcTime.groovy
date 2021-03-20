import java.util.Date;

class FcTime
{
    
    final static String GPX_DATE = "2015-01-01"
    final static String UTC_GPX_DATE = "${GPX_DATE}T00:00:00Z"     // UTC date of all FC operations
    
    //--------------------------------------------------------------------------
    static String UTCGetDateTime(String utcDateDateTime, Date localTime, String timeZone)
    // utcDateDateTime - Datum
    // localTime       - Uhrzeit (Lokal Time)
    // timeZone        - Zeitzone
    // Return          - UTC von localTime mit Datum
    {
        GregorianCalendar time_zone = getTimeZone(timeZone)
        GregorianCalendar time = new GregorianCalendar()
        time.setTime(localTime)
        time.add(Calendar.HOUR_OF_DAY, -time_zone.get(Calendar.HOUR_OF_DAY))
        if (timeZone.startsWith("-")) {
            time.add(Calendar.MINUTE, time_zone.get(Calendar.MINUTE))
        } else {
            time.add(Calendar.MINUTE, -time_zone.get(Calendar.MINUTE))
        }
        return "${utcDateDateTime.substring(0,10)}T${time.getTime().format("HH:mm:ss")}Z"
    }
    
    //--------------------------------------------------------------------------
    static String UTCGetValidDateTime(String utcDateTime)
    // "yyyy-mm-ddThh:mm:ssZ"
    {
        boolean valid = true
        try {
            Date utc_date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", utcDateTime)
            return utcDateTime
        } catch (Exception e) {
        }
        String utc_data_time = utcDateTime.replace('.',':')
        try {
            Date utc_date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", utc_data_time)
            return utc_data_time
        } catch (Exception e) {
        }
        return ""
    }
    
    //--------------------------------------------------------------------------
    static String UTCGetTime(String utcDateTime)
    // "yyyy-mm-ddThh:mm:ssZ" -> hh:mm:ss
    {
        return utcDateTime.substring(11,19)
    }
    
    //--------------------------------------------------------------------------
    static String UTCReplaceDate(String utcDateTime, String newDate)
    // "yyyy-mm-ddThh:mm:ssZ", "yyyy-mm-dd" -> "yyyy-mm-ddThh:mm:ssZ"
    {
        if (newDate && utcDateTime && (newDate.size() == 10) && (utcDateTime.size() == 20)) {
            return "${newDate}T${utcDateTime.substring(11,19)}Z"
        }
        return utcDateTime
    }
    
    //--------------------------------------------------------------------------
    static Date GetLocalTime(Date utcDate, String timeZone)
    {
        GregorianCalendar time_zone = getTimeZone(timeZone)
        GregorianCalendar time = new GregorianCalendar()
        time.setTime(utcDate)
        time.add(Calendar.HOUR_OF_DAY, time_zone.get(Calendar.HOUR_OF_DAY))
        if (timeZone.startsWith("-")) {
            time.add(Calendar.MINUTE, -time_zone.get(Calendar.MINUTE))
        } else {
            time.add(Calendar.MINUTE, time_zone.get(Calendar.MINUTE))
        }
        return time.getTime()
    }

    //--------------------------------------------------------------------------
    static String UTCGetLocalTime(String utcDateTime, String timeZone)
    // utcDateTime - yyyy-mm-ddThh:mm:ssZ
    // timeZone
    // Return: Local time hh:mm:ss
    {
        Date utc_date = getDate(utcDateTime)
        Date local_date = GetLocalTime(utc_date, timeZone)
        return local_date.format("HH:mm:ss")
    }
    
    //--------------------------------------------------------------------------
    static String UTCGetNextDateTime(String utcLastDateTime, String utcTime)
    // utcLastDateTime - yyyy-mm-ddThh:mm:ssZ
    // utcTime         - hh:mm:ss
    // Return          - yyyy-mm-ddThh:mm:ssZ mit Folgedatum, wenn 00:00 überschritten wird
    {
        String utc_last_time = utcLastDateTime.substring(11,19) // hh:mm:ss
        if (utcTime < utc_last_time) { // next day
            Date utc_last_date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", utcLastDateTime)
            GregorianCalendar utc_last_calendar = new GregorianCalendar()
            utc_last_calendar.setTime(utc_last_date)
            utc_last_calendar.add(Calendar.DAY_OF_MONTH, 1)
            return """${utc_last_calendar.getTime().format("yyyy-MM-dd'T'")}${utcTime}Z"""
        }
        return "${utcLastDateTime.substring(0,11)}${utcTime}Z"
    }
    
    //--------------------------------------------------------------------------
    static int UTCTimeDiffSeconds(String utcStartDateTime, String utcEndDateTime)
    // utcStartDateTime - "yyyy-mm-ddThh:mm:ssZ"
    // utcEndDateTime   - "yyyy-mm-ddThh:mm:ssZ"
    {
        Date start_date = getDate(utcStartDateTime)
        Date end_date = getDate(utcEndDateTime)
        int start_seconds = getSeconds(start_date)
        int end_seconds = getSeconds(end_date)
        return end_seconds - start_seconds
    }
    
    //--------------------------------------------------------------------------
    static String UTCAddSeconds(String utcDateTime, int secondValue)
    {
        Date utc_date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", utcDateTime)
        GregorianCalendar utc_calendar = new GregorianCalendar()
        utc_calendar.setTime(utc_date)
        utc_calendar.set(Calendar.SECOND, secondValue + utc_calendar.get(Calendar.SECOND))
        return utc_calendar.getTime().format("yyyy-MM-dd'T'HH:mm:ss'Z'")
    }
    
    //--------------------------------------------------------------------------
    private static Date getDate(String utcDateTime)
    {
        return Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", utcDateTime)
    }
    
    //--------------------------------------------------------------------------
    private static GregorianCalendar getTimeZone(String timeZone)
    {
        GregorianCalendar time_zone = new GregorianCalendar()
        Date timezone_date = Date.parse("HH:mm", timeZone)
        time_zone.setTime(timezone_date)
        return time_zone
    }
    
    //--------------------------------------------------------------------------
    private static int getSeconds(Date dateValue)
    {
        GregorianCalendar time = new GregorianCalendar()
        time.setTime(dateValue)
        int seconds = time.get(Calendar.SECOND) + 60 * time.get(Calendar.MINUTE) + 3600 * time.get(Calendar.HOUR_OF_DAY)
        return seconds
    }
    
    //--------------------------------------------------------------------------
	static String GetTimeStr(Date dateValue)
	{
		if (dateValue) {
			return dateValue.format("yyyy-MM-dd'T'HH:mm:ss")
		}
		return ""
	}
}
