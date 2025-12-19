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
            if (attrs.t.flighttestwind.IsCorridor()) {
                outln"""        <td>${flighttestwind(attrs.t.flighttestwind,createLink(controller:'flightTestWind',action:'edit'))}</td>"""
            } else {
                outln"""        <td>${route(attrs.t.flighttestwind.GetRoute(),createLink(controller:'route',action:'show'))}</td>"""
            }
        } else {
            outln"""        <td>${message(code:'fc.noassigned')}</td>"""
        }
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def crewPrintable = { attrs, body ->
		if (attrs.pageBreak) {
			outln"""<div style="page-break-before:always"/>"""
		}
        outln"""<tr class="value" id="${attrs.crew.startNum}">"""
        if (attrs.contest.printCrewNumber) {
            outln"""<td class="num">${attrs.crew.startNum}</td>"""
        }
        if (attrs.contest.printCrewName) {
            outln"""<td class="crew">${attrs.crew.name}</td>"""
        }
        if (attrs.contest.printCrewEmail) {
            if (attrs.crew.email) {
                outln"""<td class="email">${attrs.crew.email}</td>"""
            } else {
                outln"""<td class="email"/>"""
            }
        }
        if (attrs.contest.printCrewTeam) {
            if (attrs.crew.team) {                          
                outln"""<td class="team">${attrs.crew.team.name} ${attrs.teamOrderProblem}</td>"""
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
            outln"""<td class="tas">${fieldValue(bean:attrs.crew, field:'tas')}${message(code:'fc.knot')} ${attrs.tasOrderProblem}</td>"""
        }
        if (attrs.contest.printCrewTrackerID) {
            if (attrs.crew.trackerID) {
                outln"""<td class="trackerID">${attrs.crew.trackerID}</td>"""
            } else {
                outln"""<td class="trackerID"/>"""
            }
        }
        if (attrs.contest.printCrewUUID) {
            outln"""<td class="id">${fieldValue(bean:attrs.crew, field:'uuid')}</td>"""
        }
        if (attrs.contest.printCrewSortHelp) {
            if (attrs.crew.aircraft.user1 && attrs.crew.aircraft.user2) {
                int second_viewpos = 0
                if (attrs.crew.id == attrs.crew.aircraft.user1.id) {
                    outln"""<td class="sorthelpstartnum">${attrs.crew.aircraft.user2.startNum}</td>"""
                    second_viewpos = attrs.crew.aircraft.user2.viewpos + 1
                } else if (attrs.crew.id == attrs.crew.aircraft.user2.id) {
                    outln"""<td class="sorthelpstartnum">${attrs.crew.aircraft.user1.startNum}</td>"""
                    second_viewpos = attrs.crew.aircraft.user1.viewpos + 1
                }
                if (attrs.pageBreakPos && attrs.viewPos) {
                    int diff_viewpos = 0
                    if (attrs.pageBreakPos > attrs.viewPos) {
                        diff_viewpos = second_viewpos + 1 - attrs.pageBreakPos - attrs.viewPos
                    } else {
                        diff_viewpos =  second_viewpos + attrs.pageBreakPos - attrs.viewPos - 1
                    }
                    outln"""<td class="sorthelporderdifference">${diff_viewpos}</td>"""
                } else {
                    outln"""<td class="sorthelporderdifference"></td>"""
                }
            } else {
                outln"""<td class="sorthelpstartnum"></td>"""
                outln"""<td class="sorthelporderdifference"></td>"""
            }
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
    private String flighttestwind(FlightTestWind flightTestWindInstance, String link) {
        if (flightTestWindInstance) {
            return """<a href="${link}/${flightTestWindInstance.id}">${flightTestWindInstance.name()}</a>""" // .encodeAsHTML()
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
