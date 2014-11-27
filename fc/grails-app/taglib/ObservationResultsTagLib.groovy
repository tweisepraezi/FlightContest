class ObservationResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def observationTestCrewResults = { attrs, body ->
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        if (attrs.t.observationTestComplete) {
            outln"""        <th colspan="4" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${attrs.t.GetObservationTestVersion()})</th>"""
        } else {
            outln"""        <th colspan="4" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${attrs.t.GetObservationTestVersion()}) [${message(code:'fc.provisional')}]</th>"""
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.observationresults.turnpointphotopenalties')}:</td>"""
        outln"""            <td>${attrs.t.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.observationresults.routephotopenalties')}:</td>"""
        outln"""            <td>${attrs.t.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.observationresults.groundtargetpenalties')}:</td>"""
        outln"""            <td>${attrs.t.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.penalties')}:</td>"""
        String points_class = "points"
        if (!attrs.t.observationTestPenalties) {
            points_class = "zeropoints"
        }
        outln"""            <td class="${points_class}">${attrs.t.observationTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	def observationTestPrintable = { attrs, body ->
        outln"""<table class="summary">"""
        outln"""    <tbody>"""
        outln"""        <tr class="turnpointphotopenalties">"""
        outln"""            <td>${message(code:'fc.observationresults.turnpointphotopenalties')}: ${attrs.t.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr class="routephotopenalties">"""
        outln"""            <td>${message(code:'fc.observationresults.routephotopenalties')}: ${attrs.t.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr class="groundtargetpenalties">"""
        outln"""            <td>${message(code:'fc.observationresults.groundtargetpenalties')}: ${attrs.t.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""        	<td> </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="penalties">"""
        outln"""            <td>${message(code:'fc.penalties')}: ${attrs.t.observationTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
