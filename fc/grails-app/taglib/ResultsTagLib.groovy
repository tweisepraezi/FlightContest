class ResultsTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def crewTestPrintable = { attrs, body ->
        outln"""<table class="crewtest">"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td class="title">${message(code:'fc.crew')}:</td><td class="crew">${attrs.t.crew.name}</td>"""
        if (attrs.t.taskAircraft) {
            outln"""        <td class="title">${message(code:'fc.aircraft.registration')}:</td><td class="aircraft">${attrs.t.taskAircraft.registration}</td>"""
        } else {
            outln"""        <td class="title">${message(code:'fc.aircraft.registration')}:</td><td class="aircraft">${message(code:'fc.noassigned')}</td>"""
        }
        outln"""        </tr>"""
        outln"""        <tr>"""
        if (attrs.t.crew.team) {
            outln"""        <td class="title">${message(code:'fc.team')}:</td><td class="team">${attrs.t.crew.team.name}</td>"""
        } else {
            outln"""        <td class="title"/><td class="team"/>"""
        }
        if (attrs.t.taskAircraft) {
            if (attrs.t.taskAircraft.type) {
                outln"""    <td class="title">${message(code:'fc.aircraft.type')}:</td><td class="aircrafttype">${attrs.t.taskAircraft.type}</td>"""
            } else {
                outln"""    <td class="title"/><td class="aircrafttype"/>"""
            }
        } else {
            outln"""        <td class="title">${message(code:'fc.aircraft.type')}:</td><td class="aircrafttype">${message(code:'fc.noassigned')}</td>"""
        }
        outln"""        </tr>"""
        outln"""        <tr>"""
        if (attrs.t.task.contest.resultClasses && attrs.t.crew.resultclass) {
            outln"""        <td class="title">${message(code:'fc.resultclass')}:</td><td class="resultclass">${attrs.t.crew.resultclass.name}</td>"""
        } else {
            outln"""        <td class="title"/><td class="resultclass"/>"""
        }
        outln"""            <td class="title">${message(code:'fc.tas')}:</td><td class="tas">${fieldValue(bean:attrs.t, field:'taskTAS')}${message(code:'fc.knot')}</td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
