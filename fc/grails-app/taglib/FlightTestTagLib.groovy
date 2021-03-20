class FlightTestTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def editFlightTest = { attrs, body ->
        outln"""<fieldset>"""
        outln"""    <div>"""
        checkBox("flightPlanShowLegDistance", attrs.flighttest.flightPlanShowLegDistance, 'fc.flighttest.flightplan.showlegdistance', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("flightPlanShowTrueTrack", attrs.flighttest.flightPlanShowTrueTrack, 'fc.flighttest.flightplan.showtruetrack', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("flightPlanShowTrueHeading", attrs.flighttest.flightPlanShowTrueHeading, 'fc.flighttest.flightplan.showtrueheading', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("flightPlanShowGroundSpeed", attrs.flighttest.flightPlanShowGroundSpeed, 'fc.flighttest.flightplan.showgroundspeed', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("flightPlanShowLocalTime", attrs.flighttest.flightPlanShowLocalTime, 'fc.flighttest.flightplan.showlocaltime', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("flightPlanShowElapsedTime", attrs.flighttest.flightPlanShowElapsedTime, 'fc.flighttest.flightplan.showelapsedtime', attrs)
        outln"""    </div>"""
        outln"""    <br/>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.flighttest.flightplan.submissionminutes')} [${message(code:'fc.time.min')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="submissionMinutes" name="submissionMinutes" value="${fieldValue(bean:attrs.flighttest,field:'submissionMinutes')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.flighttest.flightplan.addtpnum')}</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="flightPlanAddTPNum" name="flightPlanAddTPNum" value="${fieldValue(bean:attrs.flighttest,field:'flightPlanAddTPNum')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        outln"""    <div>"""
        checkBox("flightResultsShowCurvedPoints", attrs.flighttest.flightResultsShowCurvedPoints, 'fc.flighttest.flightresults.showcurvedpoints', attrs)
        outln"""    </div>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox(String name, boolean checked, String label, attrs)
    {
        outln"""<input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""            <input type="checkbox" id="${name}" name="${name}" checked="checked" tabIndex="${attrs.ti[0]++}"/>"""
        } else {
            outln"""            <input type="checkbox" id="${name}" name="${name}" tabIndex="${attrs.ti[0]++}"/>"""
        }
        outln"""<label>${message(code:label)}</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private radioEntry(String name, def value, boolean checked, String label, attrs)
    {
        if (checked) {
            outln"""    <label><input type="radio" name="${name}" value="${value}" checked="checked" tabIndex="${attrs.ti[0]++}"/>${label}</label>"""
        } else {
            outln"""    <label><input type="radio" name="${name}" value="${value}" tabIndex="${attrs.ti[0]++}"/>${label}</label>"""
        }
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
