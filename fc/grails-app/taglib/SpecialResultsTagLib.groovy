class SpecialResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def specialTestCrewResults = { attrs, body ->
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        if (attrs.t.specialTestComplete) {
            outln"""        <th colspan="4" class="table-head">${attrs.t.GetSpecialTestTitle(false)} (${message(code:'fc.version')} ${attrs.t.GetSpecialTestVersion()})</th>"""
        } else {
            outln"""        <th colspan="4" class="table-head">${attrs.t.GetSpecialTestTitle(false)} (${message(code:'fc.version')} ${attrs.t.GetSpecialTestVersion()}) [${message(code:'fc.provisional')}]</th>"""
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.penalties')}:</td>"""
        String points_class = "points"
        if (!attrs.t.specialTestPenalties) {
            points_class = "zeropoints"
        }
        outln"""            <td class="${points_class}">${attrs.t.specialTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	def specialTestPrintable = { attrs, body ->
        outln"""<table class="summary">"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""        	<td> </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="penalties">"""
        outln"""            <td>${message(code:'fc.penalties')}: ${attrs.t.specialTestPenalties} ${message(code:'fc.points')}</td>"""
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
