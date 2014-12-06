class CrewTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def crewPrintable = { attrs, body ->
        outln"""<tr>"""
        if (attrs.contest.printCrewNumber) {
            outln"""<td class="num">${attrs.crew.startNum}</td>"""
        }
        if (attrs.contest.printCrewName) {
            outln"""<td class="crew">${attrs.crew.name}</td>"""
        }
        if (attrs.contest.printCrewTeam) {
            if (attrs.crew.team) {                          
                outln"""<td class="team">${attrs.crew.team.name}</td>"""
            } else {
                outln"""<td class="team">-</td>"""
            }
        }
        if (attrs.contest.printCrewClass) {
            if (attrs.contest.resultClasses) {
                if (attrs.crew.resultclass) {                          
                    outln"""<td class="resultclass">${attrs.crew.resultclass.name}</td>"""
                } else {
                    outln"""<td class="resultclass">-</td>"""
                }
            }
        }
        if (attrs.contest.printCrewShortClass) {
            if (attrs.contest.resultClasses) {
                if (attrs.crew.resultclass) {                          
                    outln"""<td class="shortresultclass">${attrs.crew.resultclass.shortName}</td>"""
                } else {
                    outln"""<td class="shortresultclass">-</td>"""
                }
            }
        }
        if (attrs.contest.printCrewAircraft) {
            if (attrs.crew.aircraft) {
                if (attrs.crew.aircraft.user1 && attrs.crew.aircraft.user2) {
                    outln"""<td class="aircraft">${attrs.crew.aircraft.registration}${HTMLFilter.NoWrapStr(' *')}</td>"""
                } else {
                    outln"""<td class="aircraft">${attrs.crew.aircraft.registration}</td>"""
                }
            } else {
                outln"""<td class="aircraft">${message(code:'fc.noassigned')}</td>"""
            }
        }
        if (attrs.contest.printCrewAircraftType) {
            if (attrs.crew.aircraft) {
                outln"""<td class="aircrafttype">${attrs.crew.aircraft.type}</td>"""
            } else {
                outln"""<td class="aircrafttype"/>"""
            }
        }
        if (attrs.contest.printCrewAircraftColour) {
            if (attrs.crew.aircraft) {
                outln"""<td class="aircraftcolor">${attrs.crew.aircraft.colour}</td>"""
            } else {
                outln"""<td class="aircraftcolor"/>"""
            }
        }
        if (attrs.contest.printCrewTAS) {
            outln"""<td class="tas">${fieldValue(bean:attrs.crew, field:'tas')}${message(code:'fc.knot')}</td>"""
        }
        if (attrs.contest.printCrewEmptyColumn1) {
            outln"""<td class="empty1"/>"""
        }
        if (attrs.contest.printCrewEmptyColumn2) {
            outln"""<td class="empty2"/>"""
        }
        if (attrs.contest.printCrewEmptyColumn3) {
            outln"""<td class="empty3"/>"""
        }
        if (attrs.contest.printCrewEmptyColumn4) {
            outln"""<td class="empty4"/>"""
        }
        outln"""</tr>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
