class CrewTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def crewDetails = { attrs, body ->
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.crew')}:</td>"""
        String s = """      <td>${attrs.t.crew.startNum}: ${crew(attrs.t.crew,createLink(controller:'crew',action:'edit'))}"""
        if (attrs.t.aflosStartNum) {
             s += """ (${message(code:'fc.aflos')}: ${attrs.t.aflosStartNum})"""
        }
        s += """            </td>"""
        outln s
        outln"""        </tr>"""
        if (attrs.t.crew.team) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.team')}:</td>"""
            outln"""        <td>${team(attrs.t.crew.team, createLink(controller:'team',action:'edit'))}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.task.contest.resultClasses && attrs.t.crew.resultclass) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.resultclass')}:</td>"""
            outln"""        <td>${resultclass(attrs.t.crew.resultclass,createLink(controller:'resultClass',action:'edit'))}</td>"""
            outln"""    </tr>"""
        }
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.aircraft')}:</td>"""
        outln"""            <td>"""
        if (attrs.t.taskAircraft) {
            outln"""            ${aircraft(attrs.t.taskAircraft,createLink(controller:'aircraft',action:'edit'))}"""
        } else {
            outln"""            ${message(code:'fc.noassigned')}"""
        }
        outln"""            </td>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>"""
        if (attrs.t.taskAircraft) {
            outln"""        <td>${attrs.t.taskAircraft.type}</td>"""
        } else {
            outln"""        <td>${message(code:'fc.noassigned')}</td>"""
        }
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.route')}:</td>"""
        if (attrs.t.flighttestwind?.flighttest) {
            outln"""        <td>${route(attrs.t.flighttestwind.flighttest.route,createLink(controller:'route',action:'show'))}</td>"""
        } else {
            outln"""        <td>${message(code:'fc.noassigned')}</td>"""
        }
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def crewPrintable = { attrs, body ->
        outln"""<tr class="value" id="${attrs.crew.startNum}">"""
        if (attrs.contest.printCrewNumber) {
            outln"""<td class="num">${attrs.crew.startNum}</td>"""
        }
        if (attrs.contest.printCrewName) {
            outln"""<td class="crew">${attrs.crew.name}</td>"""
        }
        if (attrs.contest.printCrewEmail) {
            outln"""<td class="email">${attrs.crew.email}</td>"""
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
        if (attrs.contest.printCrewUUID) {
            outln"""<td class="id">${fieldValue(bean:attrs.crew, field:'uuid')}</td>"""
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
    private String crew(Crew crewInstance, String link) {
        if (crewInstance) {
            return """<a href="${link}/${crewInstance.id}">${crewInstance.name}</a>""" // .encodeAsHTML()
        }
        return ""
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    private String team(Team teamInstance, String link) {
        if (teamInstance) {
            return """<a href="${link}/${teamInstance.id}">${teamInstance.name}</a>""" // .encodeAsHTML()
        }
        return ""
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    private String resultclass(ResultClass resultclassInstance, String link) {
        if (resultclassInstance) {
            return """<a href="${link}/${resultclassInstance.id}">${resultclassInstance.name}</a>""" // .encodeAsHTML()
        }
        return ""
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    private String aircraft(Aircraft aircraftInstance, String link) {
        if (aircraftInstance) {
            return """<a href="${link}/${aircraftInstance.id}">${aircraftInstance.registration}</a>""" // .encodeAsHTML()
        }
        return ""
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    private String route(Route routeInstance, String link) {
        if (routeInstance) {
            return """<a href="${link}/${routeInstance.id}">${routeInstance.name()}</a>""" // .encodeAsHTML()
        }
        return ""
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
