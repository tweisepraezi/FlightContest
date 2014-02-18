class ObservationResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
	// --------------------------------------------------------------------------------------------------------------------
	def observationTestPrintable = { attrs, body ->
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.observationresults.turnpointphotopenalties')}: ${attrs.t.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.observationresults.routephotopenalties')}: ${attrs.t.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:'fc.observationresults.groundtargetpenalties')}: ${attrs.t.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""        	<td> </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
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
