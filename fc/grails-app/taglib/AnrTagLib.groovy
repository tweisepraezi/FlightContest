class AnrTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']

    // --------------------------------------------------------------------------------------------------------------------
    def anrMap = { attrs, body ->
        Route route_instance = attrs.t.flighttestwind.flighttest.route
        if (route_instance.defaultPrintMap) {
            Map map = MapTools.GetMap(route_instance.defaultPrintMap, servletContext, session)
            if (map) {
                outln"""<img class="mapanr" src="${map.localref}" ></img>"""
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def anrCrew = { attrs, body ->
        if (attrs.showCrew) {
            outln"""<table class="crewinfoanr">"""
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
        } else {
            outln"""<div class="nocrewinfo"/>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def anrFlightPlan = { attrs, body ->
        Route route_instance = attrs.t.flighttestwind.flighttest.route
        
        // Info
        if (attrs.showCrew) {
            outln"""<div class="flightplanstartnumanr">${message(code:'fc.test.flightplan')} ${attrs.t.GetStartNum()}</div>"""
            outln"""<table class="flightplaninfoanr">"""
            outln"""    <tbody>"""
            outln"""        <tr class="outputtime">"""
            outln"""            <td class="title">${message(code:'fc.flighttest.documentsoutput')}:</td>"""
            outln"""            <td class="space"></td>"""
            outln"""            <td class="value">${attrs.t.GetTestingTime().format('HH:mm:ss')}</td>"""
            outln"""        </tr>"""
            outln"""        <tr class="takeoff">"""
            outln"""            <td class="title">${message(code:'fc.test.takeoff')}:</td>"""
            outln"""            <td class="space"></td>"""
            outln"""            <td class="value">${attrs.t.takeoffTime.format('HH:mm:ss')}</td>"""
            outln"""        </tr>"""
            if (attrs.t.flighttestwind.wind.speed) {
                outln"""    <tr class="empty">"""
                outln"""        <td colnum="3"> </td>"""
                outln"""    </tr>"""
                outln"""    <tr class="wind">"""
                outln"""        <td class="title">${message(code:'fc.wind')}:</td>"""
                outln"""        <td class="space"></td>"""
                outln"""        <td class="value">${attrs.t.flighttestwind.wind.printName()}</td>"""
                outln"""    </tr>"""
            }
            outln"""    </tbody>"""
            outln"""</table>"""
            outln"""<br/>"""
        } else if (attrs.showResults) {
            outln"""<div class="flightplantasanr">${message(code:'fc.tas')}: ${fieldValue(bean:attrs.t, field:'taskTAS')}${message(code:'fc.knot')}</div>"""
            if (attrs.t.flighttestwind.wind.speed) {
                outln"""<table class="flightplaninfoanr">"""
                outln"""    <tbody>"""
                outln"""        <tr class="wind">"""
                outln"""            <td class="title">${message(code:'fc.wind')}:</td>"""
                outln"""            <td class="space"></td>"""
                outln"""            <td class="value">${attrs.t.flighttestwind.wind.printName()}</td>"""
                outln"""        </tr>"""
                outln"""    </tbody>"""
                outln"""</table>"""
                outln"""<br/>"""
            }
        }
        
        // List
        boolean show_localtime = !attrs.showDuration
        outln"""<table class="flightplanlistanr"  >"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        outln"""            <th></th>"""
        outln"""            <th>${message(code:'fc.distance.short')}</th>"""
        outln"""            <th>${message(code:'fc.truetrack.anr')}</th>"""
        outln"""            <th>${message(code:'fc.legtime.eet')}</th>"""
        outln"""            <th>${message(code:'fc.legtime.eto')}</th>"""
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        outln"""        <tr class="value" id="${message(code:CoordType.TO.code)}">"""
        outln"""            <td class="tpname">${message(code:CoordType.TO.code)}</td>"""
        outln"""            <td class="distance"/>"""
        outln"""            <td class="truetrack"/>"""
        outln"""            <td class="legtime"/>"""
        if (attrs.showCrew) {
            if (show_localtime) {
                outln"""    <td class="tptime">${FcMath.TimeStr(attrs.t.takeoffTime)}</td>"""
            } else {
                outln"""    <td class="tptime">${FcMath.TimeStr(Date.parse("HH:mm","00:00"))}</td>"""
            }
        } else {
            if (attrs.showResults) {
                outln"""    <td class="tptime">${FcMath.TimeStr(Date.parse("HH:mm","00:00"))}</td>"""
            } else {
                outln"""    <td class="tptime"></td>"""
            }
        }
        outln"""        </tr>"""
        outln"""        <tr class="value" id="${message(code:CoordType.SP.code)}">"""
        outln"""            <td class="tpname">${message(code:CoordType.SP.code)}</td>"""
        outln"""            <td class="distance"/>"""
        outln"""            <td class="truetrack"/>"""
        if (attrs.showResults || (attrs.showCrew && attrs.t.flighttestwind.flighttest.flightPlanShowLocalTime)) {
            outln"""        <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,attrs.t.startTime))}</td>"""
        } else {
            outln"""        <td class="legtime"></td>"""
        }
        if (attrs.showCrew) {
            if (show_localtime) {
                outln"""    <td class="tptime">${FcMath.TimeStr(attrs.t.startTime)}</td>"""
            } else {
                outln"""    <td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,attrs.t.startTime))}</td>"""
            }
        } else {
            if (attrs.showResults) {
                outln"""    <td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,attrs.t.startTime))}</td>"""
            } else {
                outln"""    <td class="tptime"></td>"""
            }
        }

        outln"""        </tr>"""
        boolean leg_firstvalue = true
        BigDecimal leg_distance = 0
        BigDecimal leg_plantruetrack = 0
        BigDecimal leg_plantrueheading = 0
        BigDecimal leg_plangroundspeed = 0
        BigDecimal leg_duration = 0
        Date leg_time = attrs.t.startTime
        BigDecimal total_distance = 0
        Map add_tpnum = attrs.t.task.flighttest.AddTPNum()
        int add_tpnum_index = 0
        int add_tpnum_addnum = 0
        for (TestLegFlight testlegflight_instance in TestLegFlight.findAllByTest(attrs.t,[sort:"id"])) {    
            leg_distance = FcMath.AddDistance(leg_distance,testlegflight_instance.planTestDistance)
            if (leg_firstvalue) {
                leg_plantruetrack = testlegflight_instance.planTrueTrack
                leg_plantrueheading = testlegflight_instance.planTrueHeading
                leg_plangroundspeed = testlegflight_instance.planGroundSpeed
            }
            leg_duration = testlegflight_instance.AddPlanLegTime(leg_duration,leg_time)
            leg_time = testlegflight_instance.AddPlanLegTime(leg_time)
            total_distance = FcMath.AddDistance(total_distance,testlegflight_instance.planTestDistance)
            if (add_tpnum && add_tpnum_index < add_tpnum.tp.size() && add_tpnum.tp[add_tpnum_index] == testlegflight_instance.coordTitle.name()) {
                add_tpnum_addnum = add_tpnum_addnum + add_tpnum.addNumber[add_tpnum_index]
                add_tpnum_index++
            }
            if (testlegflight_instance.coordTitle.type != CoordType.SECRET) {
                outln"""<tr class="value" id="${testlegflight_instance.coordTitle.titlePrintCode()}">"""
                if (add_tpnum_addnum) {
                    outln"""<td class="tpname">${testlegflight_instance.coordTitle.titlePrintCode2(add_tpnum_addnum)}</td>"""
                } else {
                    outln"""<td class="tpname">${testlegflight_instance.coordTitle.titlePrintCode()}</td>"""
                }
                if (attrs.showResults || (attrs.showCrew && attrs.t.flighttestwind.flighttest.flightPlanShowLegDistance)) {
                    outln"""<td class="distance">${FcMath.DistanceStr1(leg_distance)}${message(code:'fc.mile')}</td>"""
                } else {
                    outln"""<td class="distance"></td>"""
                }
                if (attrs.showResults || (attrs.showCrew && attrs.t.flighttestwind.flighttest.flightPlanShowTrueTrack)) {
                    outln"""<td class="truetrack">${FcMath.GradStr(leg_plantruetrack)}${message(code:'fc.grad')}</td>"""
                } else {
                    outln"""<td class="truetrack"></td>"""
                }
                if (attrs.showResults || (attrs.showCrew && attrs.t.flighttestwind.flighttest.flightPlanShowLocalTime)) {
                    outln"""<td class="legtime">${FcMath.TimeStr(leg_duration)}</td>"""
                    if (attrs.showCrew) {
                        if (show_localtime) {
                            outln"""<td class="tptime">${FcMath.TimeStr(leg_time)}</td>"""
                        } else {
                            outln"""<td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,leg_time))}</td>"""
                        }
                    } else {
                        if (attrs.showResults) {
                            outln"""<td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,leg_time))}</td>"""
                        } else {
                            outln"""    <td class="tptime"></td>"""
                        }
                    }
                } else if (testlegflight_instance.coordTitle.type == CoordType.FP) {
                    outln"""<td class="legtime"></td>"""
                    if (attrs.showCrew) {
                        if (show_localtime) {
                            outln"""<td class="tptime">${FcMath.TimeStr(leg_time)}</td>"""
                        } else {
                            outln"""<td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,leg_time))}</td>"""
                        }
                    } else {
                        if (attrs.showResults) {
                            outln"""<td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,leg_time))}</td>"""
                        } else {
                            outln"""    <td class="tptime"></td>"""
                        }
                    }
                } else {
                    outln"""<td class="legtime"></td>"""
                    outln"""<td class="tptime"></td>"""
                }
                outln"""</tr>"""
                leg_distance = 0
                leg_duration = 0
                leg_firstvalue = true
            } else {
                leg_firstvalue  = false
            }
        }
        outln"""        <tr class="value" id="${message(code:CoordType.LDG.code)}">"""
        outln"""            <td class="tpname">${message(code:CoordType.LDG.code)}</td>"""
        outln"""            <td class="distance"/>"""
        outln"""            <td class="truetrack"/>"""
        if (attrs.showResults || (attrs.showCrew && attrs.t.flighttestwind.flighttest.flightPlanShowLocalTime)) {
            outln"""        <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(leg_time,attrs.t.maxLandingTime))}</td>"""
        } else {
            outln"""        <td class="legtime"></td>"""
        }
        if (attrs.showCrew) {
            if (show_localtime) {
                outln"""        <td class="tptime">${FcMath.TimeStr(attrs.t.maxLandingTime)}</td>"""
            } else {
                outln"""        <td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,attrs.t.maxLandingTime))}</td>"""
            }
        } else {
            if (attrs.showResults) {
                outln"""        <td class="tptime">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.takeoffTime,attrs.t.maxLandingTime))}</td>"""
            } else {
                outln"""    <td class="tptime"></td>"""
            }
        }
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
        
        // Summary
        outln"""<br/>"""
        outln"""<table class="flightplansummaryanr">"""
        outln"""    <tbody>"""
        outln"""        <tr class="corridorwidth">"""
        outln"""            <td class="title">${message(code:'fc.corridorwidth')}:</td>"""
        outln"""            <td class="space"></td>"""
        outln"""            <td class="value">${FcMath.DistanceStr(route_instance.corridorWidth)}${message(code:'fc.mile')}</td>"""
        outln"""        </tr>"""
        outln"""        <tr class="distance">"""
        outln"""            <td class="title">${message(code:'fc.distance.sp2fp')}:</td>"""
        outln"""            <td class="space"></td>"""
        outln"""            <td class="value">${FcMath.DistanceStr1(total_distance)}${message(code:'fc.mile')}</td>"""
        outln"""        </tr>"""
        if (attrs.showCrew || attrs.showResults) {
            outln"""    <tr class="duration">"""
            outln"""        <td class="title">${message(code:'fc.legtime.sp2fp')}:</td>"""
            outln"""        <td class="space"></td>"""
            outln"""        <td class="value">${FcMath.TimeStr(FcMath.TimeDiff(attrs.t.startTime,attrs.t.finishTime))}</td>"""
            outln"""    </tr>"""
        }
        outln"""    </tbody>"""
        outln"""</table>"""
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
