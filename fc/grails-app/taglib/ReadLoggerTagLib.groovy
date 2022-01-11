import com.fazecast.jSerialComm.*

class ReadLoggerTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def readLogger = { attrs, body ->
        Date start_time = null
        Date end_time = null
        for (Test test_instance in attrs.t.GetFlightTests()) {
            if (!start_time || start_time > test_instance.takeoffTime) {
                start_time = test_instance.takeoffTime
            }
            if (!end_time || end_time < test_instance.arrivalTime) {
                end_time = test_instance.arrivalTime
            }
        }
        String utc_start_time = FcTime.UTCAddSeconds(FcTime.UTCGetDateTime(attrs.t.liveTrackingNavigationTaskDate, start_time, attrs.t.contest.timeZone), -300)
        String utc_end_time = FcTime.UTCAddSeconds(FcTime.UTCGetDateTime(attrs.t.liveTrackingNavigationTaskDate, end_time, attrs.t.contest.timeZone), 300)
        
        outln"""${message(code:'fc.task.readlogger.logger')}: <a href="/fc/docs/help_${session.showLanguage}.html#read-logger" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>"""
        outln "<br/>"
        radioEntry("loggertype", "skytraq", attrs.loggertype == "skytraq", "Renkforce GT-730FL-S", attrs)
        outln "<br/>"
        radioEntry("loggertype", "dg-100", attrs.loggertype == "dg-100", "GlobalSat DG-100", attrs)
        outln "<br/>"
        radioEntry("loggertype", "dg-200", attrs.loggertype == "dg-200", "GlobalSat DG-200", attrs)
        outln "<br/>"
        outln "<br/>"
            
        outln"""<p><input type="date" value="${fieldValue(bean:attrs.t,field:'liveTrackingNavigationTaskDate')}" readonly/> ${FcTime.UTCGetLocalTime(utc_start_time, attrs.t.contest.timeZone)}...${FcTime.UTCGetLocalTime(utc_end_time, attrs.t.contest.timeZone)}</p>"""
        
        for (SerialPort com_port in SerialPort.getCommPorts()) {
            radioEntry("port", com_port.getSystemPortName(), attrs.port == com_port.getSystemPortName(), "${com_port.getSystemPortName()} [${com_port.getPortDescription()}]", attrs)
            outln "<br/>"
        }
        outln "<br/>"
        
        outln"""<input type="hidden" name="start_time" value="${FcTime.GetCompactTime(utc_start_time)}"/>"""
        outln"""<input type="hidden" name="end_time" value="${FcTime.GetCompactTime(utc_end_time)}"/>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private radioEntry(String name, String value, boolean checked, String label, attrs)
    {
        if (checked) {
            outln"""<label><input type="radio" name="${name}" value="${value}" checked="checked" tabIndex="${attrs.ti[0]}"/>${label}</label>"""
        } else {
            outln"""<label><input type="radio" name="${name}" value="${value}" tabIndex="${attrs.ti[0]}"/>${label}</label>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
