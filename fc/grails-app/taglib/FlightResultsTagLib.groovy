class FlightResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def flightTestResults = { attrs, body ->
        if (CoordResult.countByTest(attrs.t)) {
            boolean check_secretpoints = attrs.t.IsFlightTestCheckSecretPoints()
            outln"""<div>"""
            outln"""    <table>"""
            outln"""        <thead>"""
            outln"""            <tr>"""
            outln"""                <th colspan="9" class="table-head">${message(code:'fc.flightresults.coordresultlist')}</th>"""
            outln"""            </tr>"""
            outln"""            <tr>"""
            outln"""                <th>${message(code:'fc.number')}</th>"""
            outln"""                <th>${message(code:'fc.title')}</th>"""
            outln"""                <th/>"""
            //outln"""              <th>${message(code:'fc.latitude')}</th>"""
            //outln"""              <th>${message(code:'fc.longitude')}</th>"""
            outln"""                <th>${message(code:'fc.cptime')}</th>"""
            outln"""                <th>${message(code:'fc.badcoursenum')}</th>"""
            outln"""                <th>${message(code:'fc.altitude')}</th>"""
            outln"""            </tr>"""
            outln"""        </thead>"""
            outln"""        <tbody>"""
            int leg_no = 0
            int penalty_coord_summary = 0
            int penalty_badcourse_summary = 0
            int penalty_altitude_summary = 0
			Route route_instance = attrs.t.task.flighttest.route
            CoordResult last_coordresult_instance = null
            for(CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
                if (!coordresult_instance.ignoreGate) {
                
                    // procedure turn
                    if (coordresult_instance.planProcedureTurn && (attrs.t.task.procedureTurnDuration > 0)) {
                        leg_no++
                        outln"""    <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                        outln"""        <td>${coordresult_link(coordresult_instance, leg_no.toString(), true, createLink(controller:'coordResult',action:'editprocedureturn'),0)}</td>"""
                        outln"""        <td>${message(code:'fc.procedureturn')}</td>"""
                        outln"""        <td>${message(code:'fc.test.results.measured')}</td>"""
                        //outln"""      <td/>"""
                        //outln"""      <td/>"""
                        if (coordresult_instance.resultProcedureTurnEntered) {
                            if (coordresult_instance.resultProcedureTurnNotFlown) {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsProcedureTurn).contains(last_coordresult_instance.title()+',')) {
                                    outln"""<td>${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>"""
                                } else if (attrs.t.task.procedureTurnDuration == 0) {
                                    outln"""<td>${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>"""
                                } else {
                                    outln"""<td class="errors">${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>"""
                                }
                            } else {
                                outln"""<td>${message(code:'fc.flighttest.procedureturnflown.short')}</td>"""
                            }
                        } else {
                            outln"""    <td>${message(code:'fc.unknown')}</td>"""
                        }
                        outln"""        <td/>"""
                        outln"""        <td/>"""
                        outln"""    </tr>"""
                        outln"""    <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                        outln"""        <td/>"""
                        outln"""        <td/>"""
                        outln"""        <td>${message(code:'fc.test.results.penalty')}</td>"""
                        //outln"""      <td/>"""
                        //outln"""      <td/>"""
                        if (coordresult_instance.resultProcedureTurnEntered) {
                            if (last_coordresult_instance && DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsProcedureTurn).contains(last_coordresult_instance.title()+',')) {
                                outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                            } else if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() == 0) || (attrs.t.task.procedureTurnDuration == 0)) {
                                outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                            } else if (coordresult_instance.resultProcedureTurnNotFlown) {
                                penalty_coord_summary += attrs.t.GetFlightTestProcedureTurnNotFlownPoints()
                                outln"""<td class="points">${attrs.t.GetFlightTestProcedureTurnNotFlownPoints()} ${message(code:'fc.points')}</td>"""
                            } else {
                                outln"""<td class="zeropoints">0 ${message(code:'fc.points')}</td>"""
                            }
                        } else {
                            outln"""    <td>${message(code:'fc.unknown')}</td>"""
                        }
                        outln"""        <td/>"""
                        outln"""        <td/>"""
                        outln"""    </tr>"""
                    }
                    leg_no++
                    
                    // check point
                    outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    long next_id = 0
                    boolean set_next = false
                    for (CoordResult coordresult_instance2 in CoordResult.findAllByTest(attrs.t,[sort:"id"])) { // search next_id
                        if (set_next) {
                            next_id = coordresult_instance2.id
                            set_next = false
                        }
                        if (coordresult_instance2 == coordresult_instance) {
                            set_next = true
                        }
                    }
                    outln"""            <td>${coordresult_link(coordresult_instance, leg_no.toString(), false, createLink(controller:'coordResult',action:'edit'), next_id)}</td>"""
                    outln"""            <td>${coordresult_instance.titleCode()}</td>"""
                    outln"""            <td>${message(code:'fc.test.results.plan')}</td>"""
                    //outln"""          <td>${coordresult_instance.latName()}</td>"""
                    //outln"""          <td>${coordresult_instance.lonName()}</td>"""
                    outln"""            <td>${FcMath.TimeStr(coordresult_instance.planCpTime)}</td>"""
                    if (coordresult_instance.type.IsBadCourseCheckCoord()) {
                        outln"""        <td>0</td>"""
                    } else {
                        outln"""        <td/>"""
                    }
                    outln"""            <td>${get_check_altitude_str(route_instance, coordresult_instance)}</td>"""
                    outln"""        </tr>"""
                    outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    outln"""            <td/>"""
                    outln"""            <td/>"""
                    outln"""            <td>${message(code:'fc.test.results.measured')}</td>"""
                    //outln"""          <td>${coordresult_instance.resultLatitude}</td>"""
                    //outln"""          <td>${coordresult_instance.resultLongitude}</td>"""
                    if (coordresult_instance.resultEntered) {
                        if (coordresult_instance.resultCpNotFound) {
                            if ((coordresult_instance.type == CoordType.TO) && (!(coordresult_instance.test.IsFlightTestCheckTakeOff() || coordresult_instance.test.GetFlightTestTakeoffCheckSeconds()))) {
                                outln"""<td/>"""
                            } else if ((coordresult_instance.type == CoordType.LDG) && !coordresult_instance.test.IsFlightTestCheckLanding()) {
                                outln"""<td/>"""
                            } else if (coordresult_instance.type.IsCpTimeCheckCoord()) {
                                if ((coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                                    if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsNotFound).contains(coordresult_instance.title()+',')) {
                                        outln"""<td>${coordresult_instance.GetCpNotFoundShortName()}</td>"""
                                    } else {
                                        outln"""<td class="errors">${coordresult_instance.GetCpNotFoundShortName()}</td>"""
                                    }
                                } else {
                                    outln"""<td>${coordresult_instance.GetCpNotFoundShortName()}</td>"""
                                }
                            } else {
                                outln"""<td>${coordresult_instance.GetCpNotFoundShortName()}</td>"""
                            }
                        } else {
                            outln"""    <td>${FcMath.TimeStr(coordresult_instance.resultCpTime)}</td>"""
                        }
                        if (coordresult_instance.type.IsBadCourseCheckCoord()) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsBadCourse).contains(coordresult_instance.title()+',')) {
                                outln"""<td>${coordresult_instance.resultBadCourseNum}</td>"""
                            } else if (coordresult_instance.resultBadCourseNum) {
                                outln"""<td class="errors">${coordresult_instance.resultBadCourseNum}</td>"""
                            } else {
                                outln"""<td>0</td>"""
                            }
                        } else {
                            outln"""    <td/>"""
                        }
                        if (!coordresult_instance.resultAltitude) {
                            outln"""    <td/>"""
                        } else {
                            if (!coordresult_instance.resultMinAltitudeMissed) {
                                outln"""<td>${coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
                            } else {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsMinAltitude).contains(coordresult_instance.title()+',')) {
                                    outln"""<td>${coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
                                } else {
                                    outln"""<td class="errors">${coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
                                }
                            }
                        }
                    } else {
                        outln"""        <td>${message(code:'fc.unknown')}</td>"""
                        if (coordresult_instance.type.IsBadCourseCheckCoord()) {
                            outln"""    <td>${message(code:'fc.unknown')}</td>"""
                        } else {
                            outln"""    <td/>"""
                        }
                        outln"""        <td/>"""
                    }
                    outln"""        </tr>"""
                    outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    outln"""            <td/>"""
                    outln"""            <td/>"""
                    outln"""            <td>${message(code:'fc.test.results.penalty')}</td>"""
                    //outln"""          <td/>"""
                    //outln"""          <td/>"""
                    if (coordresult_instance.resultEntered) {
                        if ((coordresult_instance.type == CoordType.TO) && (!(coordresult_instance.test.IsFlightTestCheckTakeOff() || coordresult_instance.test.GetFlightTestTakeoffCheckSeconds()))) {
                            outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if ((coordresult_instance.type == CoordType.LDG) && !coordresult_instance.test.IsFlightTestCheckLanding()) {
                            outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if (coordresult_instance.resultCpNotFound) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsNotFound).contains(coordresult_instance.title()+',')) {
                                outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                            } else if ((coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                                outln"""<td class="points">${coordresult_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                            } else {
                                outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                            }
                        } else if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPoints).contains(coordresult_instance.title()+',')) {
                            outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if ((coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            String points_class = "points"
                            if (!coordresult_instance.penaltyCoord) {
                                points_class = "zeropoints"
                            }
                            outln"""    <td class="${points_class}">${coordresult_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        } else {
                            outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        }
                        
                        
                        if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
                            if (coordresult_instance.type.IsBadCourseCheckCoord()) {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsBadCourse).contains(coordresult_instance.title()+',')) {
                                    outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                                } else {
                                    int badcourse_penalties = coordresult_instance.GetBadCoursePenalties()
                                    penalty_badcourse_summary += badcourse_penalties
                                    String points_class = "points"
                                    if (!coordresult_instance.resultBadCourseNum) {
                                        points_class = "zeropoints"
                                    }
                                    outln"""<td class="${points_class}">${badcourse_penalties} ${message(code:'fc.points')}</td>"""
                                }
                            } else {
                                outln"""<td/>"""
                            }
                        } else {
                            outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        }
                    } else {
                        outln"""        <td>${message(code:'fc.unknown')}</td>"""
                        if (coordresult_instance.type.IsBadCourseCheckCoord()) {
                            outln"""    <td>${message(code:'fc.unknown')}</td>"""
                        } else {
                            outln"""    <td/>"""
                        }
                    }
                    if (coordresult_instance.type.IsAltitudeCheckCoord()) {
                        if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsMinAltitude).contains(coordresult_instance.title()+',')) {
                                outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                            } else if (!coordresult_instance.resultAltitude) {
                                outln"""<td/>"""
                            } else {
                                if (coordresult_instance.resultMinAltitudeMissed) {
                                    penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
                                    outln"""<td class="points">${attrs.t.GetFlightTestMinAltitudeMissedPoints()} ${message(code:'fc.points')}</td>"""
                                } else {
                                    outln"""<td class="zeropoints">0 ${message(code:'fc.points')}</td>"""
                                }
                            }
                        } else {
                           outln"""     <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        }
                    } else {
                        outln"""        <td/>"""
                    }
                    outln"""        </tr>"""
                    if ((coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                        penalty_coord_summary += coordresult_instance.penaltyCoord
                    }
                    last_coordresult_instance = coordresult_instance
                }
            }
            outln"""        </tbody>"""
            outln"""        <tfoot>"""
            outln"""            <tr>"""
            outln"""                <td/>"""
            outln"""                <td/>"""
            outln"""                <td>${message(code:'fc.test.results.summary')}</td>"""
            //outln"""              <td/>"""
            //outln"""              <td/>"""
            outln"""                <td>${penalty_coord_summary} ${message(code:'fc.points')}</td>"""
            outln"""                <td>${penalty_badcourse_summary} ${message(code:'fc.points')}</td>"""
            outln"""                <td>${penalty_altitude_summary} ${message(code:'fc.points')}</td>"""
            outln"""        </tfoot>"""
            outln"""    </table>"""
            outln"""</div>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def flightTestLoggerResults = { attrs, body ->
        if (attrs.t.IsLoggerResult()) {
            
            boolean notfound_message = false
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(attrs.t.loggerResult,[sort:'utc'])) {
                if (calcresult_instance.coordTitle) {
                    switch (calcresult_instance.coordTitle.type) {
                        case CoordType.TO:
                        case CoordType.LDG:
                        case CoordType.iTO:
                        case CoordType.iLDG:
                            if (calcresult_instance.gateNotFound) {
                                notfound_message = true
                            }
                            break
                    }
                }
            }
            if (notfound_message) {
                outln"""<div class="errors">"""
                outln"""    <p class="errors">${message(code:'fc.flightresults.loggerresults.toldgnotfound')}</p>"""
                outln"""</div>"""
            }
            
            boolean show_judgeactions = attrs.allowJudgeActions == "true"
            outln"""<table>"""
            outln"""    <thead>"""
            outln"""        <tr>"""
            if (show_judgeactions) {
                outln"""        <th colspan="10" class="table-head">${message(code:'fc.flightresults.calculatedpoints')}</th>"""
            } else {
                outln"""        <th colspan="9" class="table-head">${message(code:'fc.flightresults.allcalculatedpoints')}</th>"""
            }
            outln"""        </tr>"""
            outln"""        <tr>"""
            outln"""            <th>${message(code:'fc.title')}</th>"""
            outln"""            <th>${message(code:'fc.time.local')}</th>"""
            outln"""            <th>${message(code:'fc.latitude')}</th>"""
            outln"""            <th>${message(code:'fc.longitude')}</th>"""
            outln"""            <th>${message(code:'fc.altitude')}</th>"""
            outln"""            <th>${message(code:'fc.task.disabledcheckpoints.notfound')}</th>"""
            outln"""            <th>${message(code:'fc.flighttest.procedureturnnotflown.short2')}</th>"""
            outln"""            <th>${message(code:'fc.task.disabledcheckpoints.badcourse')}</th>"""
            if (show_judgeactions) {
                outln"""        <th></th>"""
            }
            outln"""            <th>${message(code:'fc.offlinemap')}</th>"""
            outln"""        </tr>"""
            outln"""    </thead>"""
            outln"""    <tbody>"""
            int leg_no = 0
			String to_utc = ""
            for (CalcResult calcresult_instance in CalcResult.findAllByLoggerresult(attrs.t.loggerResult,[sort:'utc'])) {
                leg_no++
                boolean show_point = true
                if (attrs.showAll != "true") {
                    if (calcresult_instance.badCourse && !calcresult_instance.badCourseSeconds) {
                        show_point = false
                    }
                }
                if (show_point) {
                    wr_calcresult(calcresult_instance, leg_no, attrs.t.task.contest.timeZone, show_judgeactions, to_utc)
					to_utc = ""
                }
				if (calcresult_instance.coordTitle) {
					to_utc = calcresult_instance.utc
					/*
					switch (calcresult_instance.coordTitle.type) {
						case CoordType.TO:
						case CoordType.FP:
							to_utc = calcresult_instance.utc
							break
					}
					*/
				}
            }
            outln"""    </tbody>"""
            outln"""<tfoot>"""
            outln"""    <tr>"""
            if (show_judgeactions) {
                outln"""    <td colspan="9"/>"""
            } else {
                outln"""    <td colspan="8"/>"""
            }
            outln"""        <td><input type="text" id="addShowTimeValue" name="addShowTimeValue" value="${fieldValue(bean:attrs.t,field:'addShowTimeValue')}" size="3" /> ${message(code:'fc.time.min')}</td>"""
            outln"""    </tr>"""
            outln"""</tfoot>"""
            outln"""</table>"""
            outln"""<script>"""
            outln"""\$(document).on('keyup', '#addShowTimeValue', function() {"""
            outln"""    var add_showtime_value = \$("#addShowTimeValue").val();"""
            outln"""    if (add_showtime_value) {"""
            outln"""        \$("[href]").each(function(){"""
            outln"""            if (\$(this).attr("href").indexOf("showofflinemap_test") > 0) {"""
            outln"""                var href = \$(this).attr("href");"""
            outln"""                if (href.lastIndexOf("&addShowTimeValue=") > 0) {"""
            outln"""                    href = href.substring(0, href.lastIndexOf("&addShowTimeValue="));"""
            outln"""                }"""
            outln"""                href = href + "&addShowTimeValue=" + add_showtime_value;"""
            outln"""                \$(this).attr("href", href);"""
            outln"""            }"""
            outln"""        });"""
            outln"""    }"""
            outln"""});"""
            outln"""</script>"""
         }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void wr_calcresult(CalcResult calcresultInstance, int legNo, String timeZone, boolean showJudgeActions, String lastUtc)
    {
        String show_class = ""
        if (calcresultInstance.hide) {
            show_class = "hide"
        } else if (calcresultInstance.judgeDisabled) {
            show_class = "judgedisabled"
        } else if (calcresultInstance.gateNotFound) {
            if (calcresultInstance.noGateMissed) {
                show_class = "warnings"
            } else {
                show_class = "errors"
            }
        } else if (calcresultInstance.gateMissed) {
            if (calcresultInstance.noGateMissed) {
                show_class = "warnings"
            } else {
                show_class = "errors"
            }
        } else if (calcresultInstance.badTurn) {
            if (calcresultInstance.noBadTurn) {
                show_class = "warnings"
            } else {
                show_class = "errors"
            }
        } else if (calcresultInstance.badCourseSeconds) {
            if (calcresultInstance.noBadCourse) {
                show_class = "warnings"
            } else {
                show_class = "errors"
            }
        }
        outln"""    <tr class="${(legNo % 2) == 0 ? 'odd' : ''}">"""
        if (calcresultInstance.coordTitle) {
            outln"""    <td class="${show_class}">${calcresultInstance.coordTitle.titleCode()}</td>"""
        } else {
            outln"""    <td class="${show_class}">-</td>"""
        }
        outln"""        <td class="${show_class}">${calcresultInstance.GetLocalTime(timeZone)}</td>"""
        outln"""        <td class="${show_class}">${calcresultInstance.latitude}${message(code:'fc.grad')}</td>"""
        outln"""        <td class="${show_class}">${calcresultInstance.longitude}${message(code:'fc.grad')}</td>"""
        outln"""        <td class="${show_class}">${calcresultInstance.altitude}${message(code:'fc.foot')}</td>"""
        if (calcresultInstance.gateNotFound || calcresultInstance.gateMissed) {
            String gatemissed_msg = message(code:'fc.yes')
            if (calcresultInstance.noGateMissed) {
                gatemissed_msg = message(code:'fc.no')
            }
            if (calcresultInstance.gateFlyBy) {
                gatemissed_msg += " (${message(code:'fc.task.disabledcheckpoints.flyby')})"
            }
            if (calcresultInstance.gateNotFound) {
                gatemissed_msg += " (${message(code:'fc.task.disabledcheckpoints.gatenotfound')})"
            }
            outln"""    <td class="${show_class}">${gatemissed_msg}</td>"""
        } else {
            outln"""    <td class="${show_class}">-</td>"""
        }
        if (calcresultInstance.badTurn) {
            String badturn_msg = message(code:'fc.yes')
            if (calcresultInstance.noBadTurn) {
                badturn_msg = message(code:'fc.no')
            }
            outln"""    <td class="${show_class}">${badturn_msg}</td>"""
        } else {
            outln"""    <td class="${show_class}">-</td>"""
        }
        if (calcresultInstance.badCourse) {
            String badcourse_msg = message(code:'fc.yes')
            if (calcresultInstance.noBadCourse) {
                badcourse_msg = message(code:'fc.no')
            }
            if (calcresultInstance.badCourseSeconds) {
                outln"""<td class="${show_class}">${badcourse_msg} (${calcresultInstance.badCourseSeconds}${message(code:'fc.time.s')})</td>"""
            } else {
                outln"""<td class="${show_class}">${badcourse_msg}</td>"""
            }
        } else {
            outln"""    <td class="${show_class}">-</td>"""
        }
        if (showJudgeActions) {
            if (calcresultInstance.IsJudgeAction()) {
                if (calcresultInstance.judgeDisabled) {
                    outln"""<td><a href="${createLink(controller:'test',action:'judgeenablecalcresult',params:[calcresultid:calcresultInstance.id])}">+</a></td>"""
                } else {
                    outln"""<td><a href="${createLink(controller:'test',action:'judgedisablecalcresult',params:[calcresultid:calcresultInstance.id])}">-</a></td>"""
                }
            } else {
                outln"""    <td/>"""
            }
        }
        if (calcresultInstance.coordTitle) {
            outln"""    <td><a class="showOfflineMapTest" href="${createLink(controller:'test',action:'showofflinemap_test',params:[id:calcresultInstance.loggerresult.test.id, showCoord:calcresultInstance.coordTitle.titleCode(), showUtc:calcresultInstance.utc, lastUtc:lastUtc])}" target="_blank">...</a></td>"""
        } else {
            outln"""    <td></td>"""
        }
        outln"""    </tr>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def flightTestCrewResults = { attrs, body ->
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        if (attrs.t.flightTestComplete) {
            outln"""        <th colspan="7" class="table-head">${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${attrs.t.GetFlightTestVersion()})</th>"""
        } else {
            outln"""        <th colspan="7" class="table-head">${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${attrs.t.GetFlightTestVersion()}) [${message(code:'fc.provisional')}]</th>"""
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        if (CoordResult.countByTest(attrs.t)) {
            outln"""<thead>"""
            outln"""    <tr>"""
            outln"""        <th>${message(code:'fc.title')}</th>"""
            outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
            outln"""        <th>${message(code:'fc.test.results.measured')}</th>"""
            outln"""        <th>${message(code:'fc.points')}</th>"""
            if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
                outln"""    <th>${message(code:'fc.procedureturn')}</th>"""
            }
            if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
                outln"""    <th>${message(code:'fc.badcoursenum')}</th>"""
            }
            if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                outln"""    <th>${message(code:'fc.altitude')}</th>"""
            }
            outln"""    </tr>"""
            outln"""</thead>"""
            outln"""<tbody>"""
            Integer penalty_coord_summary = 0
            Integer penalty_procedureturn_summary = 0
            Integer penalty_badcourse_summary = 0
            Integer penalty_altitude_summary = 0
            boolean check_secretpoints = attrs.t.IsFlightTestCheckSecretPoints()
            CoordResult last_coordresult_instance = null
            for( CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
                if (last_coordresult_instance) {
                    if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                        penalty_coord_summary += last_coordresult_instance.penaltyCoord
                    }
                    outln"""<tr>"""
                    outln"""    <td>${last_coordresult_instance.titleCode()}</td>"""
                    outln"""    <td>${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
                    if (last_coordresult_instance.resultCpNotFound) {
                        outln"""<td>-</td>"""
                    } else {
                        outln"""<td>${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
                    }
                    if (last_coordresult_instance.resultCpNotFound) {
                        if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsNotFound).contains(last_coordresult_instance.title()+',')) {
                            outln"""<td>-</td>"""
                        } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
                        } else {
                            outln"""<td>-</td>"""
                        }
                    } else {
                        if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPoints).contains(last_coordresult_instance.title()+',')) {
                            outln"""<td>-</td>"""
                        } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
                        } else {
                            outln"""<td>-</td>"""
                        }
                    }
                    if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
                        if (coordresult_instance.planProcedureTurn) {
                            if (coordresult_instance.resultProcedureTurnEntered) {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsProcedureTurn).contains(last_coordresult_instance.title()+',')) {
                                    outln"""<td>-</td>"""
                                } else if (attrs.t.task.procedureTurnDuration == 0) {
                                    outln"""<td>-</td>"""
                                } else if (coordresult_instance.resultProcedureTurnNotFlown) {
                                    penalty_procedureturn_summary += attrs.t.GetFlightTestProcedureTurnNotFlownPoints()
                                    outln"""<td>${attrs.t.GetFlightTestProcedureTurnNotFlownPoints()}</td>"""
                                } else {
                                    outln"""<td>0</td>"""
                                }
                            } else {
                                outln"""<td/>"""
                            }
                        } else {
                            outln"""<td/>"""
                        }
                    }
                    if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
                        if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsBadCourse).contains(last_coordresult_instance.title()+',')) {
                                if (last_coordresult_instance.resultBadCourseNum > 0) {
                                    outln"""<td>- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                } else {
                                    outln"""<td>-</td>"""
                                }
                            } else {
                                int badcourse_penalties = last_coordresult_instance.GetBadCoursePenalties()
                                penalty_badcourse_summary += badcourse_penalties
                                if (last_coordresult_instance.resultBadCourseNum > 0) {
                                    outln"""<td>${badcourse_penalties} (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                } else {
                                    outln"""<td>${badcourse_penalties}</td>"""
                                }
                            }
                        } else {
                            outln"""<td/>"""
                        }
                    }
                    if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                        if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                            if (last_coordresult_instance.resultAltitude) {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsMinAltitude).contains(last_coordresult_instance.title()+',')) {
                                    outln"""<td>-</td>"""
                                } else if (last_coordresult_instance.resultMinAltitudeMissed) {
                                    penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
                                    outln"""<td>${attrs.t.GetFlightTestMinAltitudeMissedPoints()} (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
                                } else {
                                    outln"""<td>0</td>"""
                                }
                            } else {
                                outln"""<td>0</td>"""
                            }
                        } else {
                            outln"""<td/>"""
                        }
                    }
                    outln"""</tr>"""
                }
                last_coordresult_instance = coordresult_instance
            }
            if (last_coordresult_instance) {
                if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                    penalty_coord_summary += last_coordresult_instance.penaltyCoord
                }
                outln"""<tr>"""
                outln"""    <td>${last_coordresult_instance.titleCode()}</td>"""
                outln"""    <td>${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
                if (last_coordresult_instance.resultCpNotFound) {
                    outln"""<td>-</td>"""
                } else {
                    outln"""<td>${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
                }
                if (last_coordresult_instance.resultCpNotFound) {
                    if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsNotFound).contains(last_coordresult_instance.title()+',')) {
                        outln"""<td>-</td>"""
                    } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                        outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
                    } else {
                        outln"""<td>-</td>"""
                    }
                } else {
                    if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPoints).contains(last_coordresult_instance.title()+',')) {
                        outln"""<td>-</td>"""
                    } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                        outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
                    } else {
                        outln"""<td>-</td>"""
                    }
                }
                if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
                    outln"""    <td/>"""
                }
                if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
                    if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
                        if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsBadCourse).contains(last_coordresult_instance.title()+',')) {
                            if (last_coordresult_instance.resultBadCourseNum > 0) {
                                outln"""<td>- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                            } else {
                                outln"""<td>-</td>"""
                            }
                        } else {
                            int badcourse_penalties = last_coordresult_instance.GetBadCoursePenalties()
                            penalty_badcourse_summary += badcourse_penalties
                            if (last_coordresult_instance.resultBadCourseNum > 0) {
                                outln"""<td>${badcourse_penalties} (${last_coordresult_instance.resultBadCourseNum})</td>"""
                            } else {
                                outln"""<td>${badcourse_penalties}</td>"""
                            }
                        }
                    } else {
                        outln"""<td/>"""
                    }
                }
                if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                    if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                        if (last_coordresult_instance.resultAltitude) {
                            if (last_coordresult_instance.resultMinAltitudeMissed) {
                                penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
                                outln"""<td>${attrs.t.GetFlightTestMinAltitudeMissedPoints()}</td>"""
                            } else {
                                outln"""<td>0</td>"""
                            }
                        } else {
                            outln"""<td>0</td>"""
                        }
                    } else {
                        outln"""<td/>"""
                    }
                }
                outln"""</tr>"""
            }
            outln"""</tbody>"""
            outln"""<tfoot>"""
            outln"""    <tr>"""
            outln"""        <td/>"""
            outln"""        <td/>"""
            outln"""        <td/>"""
            outln"""        <td>${penalty_coord_summary}</td>"""
            if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
                outln"""    <td>${penalty_procedureturn_summary}</td>"""
            }
            if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
                outln"""    <td>${penalty_badcourse_summary}</td>"""
            }
            if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                outln"""    <td>${penalty_altitude_summary}</td>"""
            }
            outln"""    </tr>"""
            outln"""</tfoot>"""
        }
        outln"""</table>"""
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.flightresults.checkpointpenalties')}:</td>"""
        outln"""            <td>${attrs.t.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        if (attrs.t.flightTestTakeoffMissed) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.takeoffmissed')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestTakeoffMissedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestLandingTooLate) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.landingtolate')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestLandingToLatePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestBadCourseStartLanding) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.badcoursestartlanding')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestGivenTooLate) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.giventolate')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestSafetyAndRulesInfringement) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.safetyandrulesinfringement')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestSafetyAndRulesInfringementPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestInstructionsNotFollowed) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.instructionsnotfollowed')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestInstructionsNotFollowedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestFalseEnvelopeOpened) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.falseenvelopeopened')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestFalseEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestSafetyEnvelopeOpened) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.safetyenvelopeopened')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestSafetyEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestFrequencyNotMonitored) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.frequencynotmonitored')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestFrequencyNotMonitoredPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestForbiddenEquipment) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.forbiddenequipment')}:</td>"""
            outln"""        <td>${attrs.t.GetFlightTestForbiddenEquipmentPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestOtherPenalties != 0) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.flighttest.otherpenalties')}:</td>"""
            outln"""        <td>${attrs.t.flightTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td class="detailtitle">${message(code:'fc.penalties')}:</td>"""
        String points_class = "points"
        if (!attrs.t.flightTestPenalties) {
             points_class = "zeropoints"
        }
        outln"""            <td class="${points_class}">${attrs.t.flightTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
	def flightTestPrintable = { attrs, body ->
		if (CoordResult.countByTest(attrs.t)) {
            Route route_instance = attrs.t.flighttestwind.flighttest.route
			outln"""<table class="flightresultlist">"""
			outln"""	<thead>"""
			outln"""		<tr class="name1">"""
			outln"""			<th>${message(code:'fc.tpname')}</th>"""
			outln"""			<th colspan="3">${message(code:'fc.cptime')}</th>"""
			if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0) && route_instance.useProcedureTurns) {
				outln"""		<th>${message(code:'fc.procedureturn')}</th>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
				outln"""		<th>${message(code:'fc.badcoursenum')}</th>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
				outln"""		<th>${message(code:'fc.altitude')}</th>"""
			}
			outln"""		</tr>"""
			outln"""		<tr class="name2">"""
			outln"""			<th/>"""
			outln"""			<th>${message(code:'fc.test.results.plan')}</th>"""
			outln"""			<th>${message(code:'fc.test.results.measured')}</th>"""
			outln"""			<th>${message(code:'fc.points')}</th>"""
			if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0) && route_instance.useProcedureTurns) {
                outln"""		<th/>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
                outln"""		<th/>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                outln"""		<th/>"""
			}
			outln"""		</tr>"""
			outln"""	</thead>"""
			outln"""	<tbody>"""
			Integer penalty_coord_summary = 0
			Integer	penalty_procedureturn_summary = 0
			Integer	penalty_badcourse_summary = 0
			Integer penalty_altitude_summary = 0
			boolean check_secretpoints = attrs.t.IsFlightTestCheckSecretPoints()
            List curved_point_ids = attrs.t.GetCurvedPointIds()
            boolean show_curved_point = attrs.t.task.flighttest.flightResultsShowCurvedPoints
			CoordResult last_coordresult_instance = null
			for (CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
				if (last_coordresult_instance) {
					if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
						penalty_coord_summary += last_coordresult_instance.penaltyCoord
					}
                    if (show_curved_point || !last_coordresult_instance.HideSecret(curved_point_ids)) {
    					outln"""<tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
    					outln"""	<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
    					outln"""	<td class="plancptime">${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
    					if (last_coordresult_instance.resultCpNotFound) {
    						outln"""<td class="cptime">-</td>"""
    					} else {
    						outln"""<td class="cptime">${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
    					}
                        if (last_coordresult_instance.resultCpNotFound) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsNotFound).contains("${last_coordresult_instance.title()},")) {
                                outln"""<td class="penaltycp">-</td>"""
                            } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                                outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
                            } else {
                                outln"""<td class="penaltycp">-</td>"""
                            }
                        } else {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPoints).contains("${last_coordresult_instance.title()},")) {
                                outln"""<td class="penaltycp">-</td>"""
                            } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
        						outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
        					} else {
        						outln"""<td class="penaltycp">-</td>"""
        					}
                        }
    					if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0) && route_instance.useProcedureTurns) {
    						if (coordresult_instance.planProcedureTurn) {
    							if (coordresult_instance.resultProcedureTurnEntered) {
    								if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsProcedureTurn).contains("${last_coordresult_instance.title()},")) {
    									outln"""<td class="penaltyprocedureturn">-</td>"""
    								} else if (attrs.t.task.procedureTurnDuration == 0) {
                                        outln"""<td class="penaltyprocedureturn">-</td>"""
    								} else if (coordresult_instance.resultProcedureTurnNotFlown) {
    									penalty_procedureturn_summary += attrs.t.GetFlightTestProcedureTurnNotFlownPoints()
    									outln"""<td class="penaltyprocedureturn">${attrs.t.GetFlightTestProcedureTurnNotFlownPoints()}</td>"""
    								} else {
    									outln"""<td class="penaltyprocedureturn">0</td>"""
    								}
    							} else {
    								outln"""<td class="penaltyprocedureturn"/>"""
    							}
    						} else {
    							outln"""<td class="penaltyprocedureturn"/>"""
    						}
    					}
    					if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
    						if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsBadCourse).contains(last_coordresult_instance.title()+',')) {
                                    if (last_coordresult_instance.resultBadCourseNum > 0) {
                                        outln"""<td class="penaltybadcourse">- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                    } else {
                                        outln"""<td class="penaltybadcourse">-</td>"""
                                    }
                                } else {
                                    int badcourse_penalties = last_coordresult_instance.GetBadCoursePenalties()
        							penalty_badcourse_summary += badcourse_penalties
        							if (last_coordresult_instance.resultBadCourseNum > 0) {
        								outln"""<td class="penaltybadcourse">${badcourse_penalties} (${last_coordresult_instance.resultBadCourseNum})</td>"""
        							} else {
        								outln"""<td class="penaltybadcourse">${badcourse_penalties}</td>"""
        							}
                                }
    						} else {
    							outln"""<td class="penaltybadcourse"/>"""
    						}
    					}
    					if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
    						if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                                if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsMinAltitude).contains("${last_coordresult_instance.title()},")) {
                                    outln"""<td class="penaltyaltitudemissed">-</td>"""
                                } else if (last_coordresult_instance.resultAltitude && last_coordresult_instance.resultMinAltitudeMissed) {
    								penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
    								outln"""<td class="penaltyaltitudemissed">${attrs.t.GetFlightTestMinAltitudeMissedPoints()} (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
    							} else {
    								if (last_coordresult_instance.resultCpNotFound) {
    									outln"""<td class="penaltyaltitudemissed">0 (-)</td>"""
    								} else {
    									outln"""<td class="penaltyaltitudemissed">0 (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
    								}
    							}
    						} else {
    							outln"""<td class="penaltyaltitudemissed"/>"""
    						}
    					}
    					outln"""</tr>"""
                    }
				}
				last_coordresult_instance = coordresult_instance
			}
			if (last_coordresult_instance) {
				if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
					penalty_coord_summary += last_coordresult_instance.penaltyCoord
				}
                if (show_curved_point || !last_coordresult_instance.HideSecret(curved_point_ids)) {
    				outln"""    <tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
    				outln"""		<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
    				outln"""		<td class="plancptime">${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
    				if (last_coordresult_instance.resultCpNotFound) {
    					outln"""	<td class="cptime">-</td>"""
    				} else {
    					outln"""	<td class="cptime">${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
    				}
                    if (last_coordresult_instance.resultCpNotFound) {
                        if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsNotFound).contains("${last_coordresult_instance.title()},")) {
                            outln"""<td class="penaltycp">-</td>"""
                        } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
                        } else {
                            outln"""<td class="penaltycp">-</td>"""
                        }
                    } else {
                        if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPoints).contains("${last_coordresult_instance.title()},")) {
                            outln"""<td class="penaltycp">-</td>"""
        				} else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
        					outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
        				} else {
        					outln"""<td class="penaltycp">-</td>"""
        				}
                    }
    				if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0) && route_instance.useProcedureTurns) {
    					outln"""	<td class="penaltyprocedureturn"/>"""
    				}
    				if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
    					if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsBadCourse).contains(last_coordresult_instance.title()+',')) {
                                if (last_coordresult_instance.resultBadCourseNum > 0) {
                                    outln"""<td class="penaltybadcourse">- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                } else {
                                    outln"""<td class="penaltybadcourse">-</td>"""
                                }
                            } else {
                                int badcourse_penalties = last_coordresult_instance.GetBadCoursePenalties()
        						penalty_badcourse_summary += badcourse_penalties
        						if (last_coordresult_instance.resultBadCourseNum > 0) {
        							outln"""<td class="penaltybadcourse">${badcourse_penalties} (${last_coordresult_instance.resultBadCourseNum})</td>"""
        						} else {
        						   	outln"""<td class="penaltybadcourse">${badcourse_penalties}</td>"""
        						}
                            }
    					} else {
    						outln"""<td class="penaltybadcourse"/>"""
    					}
    				}
    				if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
    					if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                            if (DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsMinAltitude).contains("${last_coordresult_instance.title()},")) { // no min altitude check
                                outln"""<td class="penaltyaltitudemissed"/>"""
                            } else if (last_coordresult_instance.resultAltitude && last_coordresult_instance.resultMinAltitudeMissed) {
    							penalty_altitude_summary += attrs.t.GetFlightTestMinAltitudeMissedPoints()
    							outln"""<td class="penaltyaltitudemissed">${attrs.t.GetFlightTestMinAltitudeMissedPoints()} (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
    						} else {
    							if (last_coordresult_instance.resultCpNotFound) {
    								outln"""<td class="penaltyaltitudemissed">0 (-)</td>"""
    							} else {
    								outln"""<td class="penaltyaltitudemissed">0 (${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')})</td>"""
    							}
    						}
    					} else {
    						outln"""<td class="penaltyaltitudemissed"/>"""
    					}
    				}
    				outln"""</tr>"""
                }
			}
			outln"""	</tbody>"""
			outln"""	<tfoot>"""
			outln"""		<tr class="summary">"""
            outln"""	        <td class="tpname" colspan="3">${message(code:'fc.test.results.summary')}</td>"""
			outln"""			<td class="penaltycp">${penalty_coord_summary}</td>"""
			if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0) && route_instance.useProcedureTurns) {
				outln"""		<td class="penaltyprocedureturn">${penalty_procedureturn_summary}</td>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
				outln"""		<td class="penaltybadcourse">${penalty_badcourse_summary}</td>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
				outln"""		<td class="penaltyaltitudemissed">${penalty_altitude_summary}</td>"""
			}
			outln"""		</tr>"""
			outln"""	</tfoot>"""
			outln"""</table>"""
			outln"""<br/>"""
		}
        outln"""<table class="summary">"""
        outln"""    <tbody>"""
        outln"""        <tr class="checkpointpenalties">"""
        outln"""            <td>${message(code:'fc.flightresults.checkpointpenalties')}: ${attrs.t.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        if (attrs.t.flightTestTakeoffMissed) {
			outln"""	<tr class="takeoffmissed">"""
            outln"""       	<td>${message(code:'fc.flighttest.takeoffmissed')}: ${attrs.t.GetFlightTestTakeoffMissedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""   	</tr>"""
        }
        if (attrs.t.flightTestLandingTooLate) {
            outln"""	<tr class="landingtolate">"""
            outln"""	    <td>${message(code:'fc.flighttest.landingtolate')}: ${attrs.t.GetFlightTestLandingToLatePoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestBadCourseStartLanding) {
        	outln"""	<tr class="badcoursestartlanding">"""
         	outln"""		<td>${message(code:'fc.flighttest.badcoursestartlanding')}: ${attrs.t.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>"""
        	outln"""	</tr>"""
        }
        if (attrs.t.flightTestGivenTooLate) {
        	outln"""	<tr class="giventolate">"""
            outln"""		<td>${message(code:'fc.flighttest.giventolate')}: ${attrs.t.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>"""
        	outln"""	</tr>"""
        }
        if (attrs.t.flightTestSafetyAndRulesInfringement) {
            outln"""	<tr class="safetyandrulesinfringement">"""
            outln"""	    <td>${message(code:'fc.flighttest.safetyandrulesinfringement')}: ${attrs.t.GetFlightTestSafetyAndRulesInfringementPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestInstructionsNotFollowed) {
            outln"""	<tr class="instructionsnotfollowed">"""
            outln"""	    <td>${message(code:'fc.flighttest.instructionsnotfollowed')}: ${attrs.t.GetFlightTestInstructionsNotFollowedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestFalseEnvelopeOpened) {
            outln"""	<tr class="falseenvelopeopened">"""
            outln"""	    <td>${message(code:'fc.flighttest.falseenvelopeopened')}: ${attrs.t.GetFlightTestFalseEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestSafetyEnvelopeOpened) {
            outln"""	<tr class="safetyenvelopeopened">"""
            outln"""	    <td>${message(code:'fc.flighttest.safetyenvelopeopened')}: ${attrs.t.GetFlightTestSafetyEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestFrequencyNotMonitored) {
            outln"""	<tr class="frequencynotmonitored">"""
            outln"""	    <td>${message(code:'fc.flighttest.frequencynotmonitored')}: ${attrs.t.GetFlightTestFrequencyNotMonitoredPoints()} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        if (attrs.t.flightTestForbiddenEquipment) {
            outln"""    <tr class="frequencynotmonitored">"""
            outln"""        <td>${message(code:'fc.flighttest.forbiddenequipment')}: ${attrs.t.GetFlightTestForbiddenEquipmentPoints()} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.flightTestOtherPenalties != 0) {
            outln"""	<tr class="otherpenalties">"""
            outln"""	    <td>${message(code:'fc.flighttest.otherpenalties')}: ${attrs.t.flightTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""	</tr>"""
        }
        outln"""	    <tr>"""
        outln"""	      	<td> </td>"""
        outln"""	    </tr>"""
        outln"""	</tbody>"""
        outln"""	<tfoot>"""
        outln"""	    <tr class="penalties">"""
        outln"""	    	<td>${message(code:'fc.penalties')}: ${attrs.t.flightTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""	    </tr>"""
        outln"""	</tfoot>"""
    	outln"""</table>"""
	}
	
    // --------------------------------------------------------------------------------------------------------------------
	def flightTestMeasurementPrintable = { attrs, body ->
		if (CoordResult.countByTest(attrs.t)) {
			outln"""<br/>"""
			outln"""<table class="flightmeasurementlist">"""
			outln"""	<thead>"""
			outln"""		<tr class="name1">"""
			outln"""			<th>${message(code:'fc.tpname')}</th>"""
			outln"""			<th colspan="2">${message(code:'fc.cptime')}</th>"""
			if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
				outln"""		<th>${message(code:'fc.procedureturn')}</th>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
				outln"""		<th>${message(code:'fc.badcoursenum')}</th>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
				outln"""		<th>${message(code:'fc.altitude')}</th>"""
			}
			outln"""		</tr>"""
			outln"""		<tr class="name2">"""
			outln"""			<th/>"""
			outln"""			<th>${message(code:'fc.test.results.plan')}</th>"""
			outln"""			<th>${message(code:'fc.test.results.measured')}</th>"""
			if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
				outln"""		<th/>"""
			}
			if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
				outln"""		<th/>"""
			}
			if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
				outln"""		<th/>"""
			}
			outln"""		</tr>"""
			outln"""	</thead>"""
			outln"""	<tbody>"""
            List curved_point_ids = attrs.t.GetCurvedPointIds()
            boolean show_curved_point = attrs.t.task.flighttest.flightResultsShowCurvedPoints
			CoordResult last_coordresult_instance = null
			for (CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
				if (last_coordresult_instance) {
                    if (show_curved_point || !last_coordresult_instance.HideSecret(curved_point_ids)) {
    					outln"""<tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
    					outln"""	<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
    					outln"""	<td class="plancptime">${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
    					if (last_coordresult_instance.resultCpNotFound) {
    						outln"""<td class="cptime">-</td>"""
    					} else {
    						outln"""<td class="cptime">${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
    					}
    					if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
    						if (coordresult_instance.planProcedureTurn) {
    							if (coordresult_instance.resultProcedureTurnEntered) {
                                    if (coordresult_instance.resultProcedureTurnNotFlown) {
    									outln"""<td class="procedureturn">${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>"""
    								} else {
    									outln"""<td class="procedureturn">${message(code:'fc.flighttest.procedureturnflown.short')}</td>"""
    								}
    							} else {
    								outln"""<td class="procedureturn"/>"""
    							}
    						} else {
    							outln"""<td class="procedureturn"/>"""
    						}
    					}
    					if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
    						if (last_coordresult_instance.resultEntered) {
    							if (last_coordresult_instance.type.IsBadCourseCheckCoord()) {
    								outln"""<td class="badcoursenum">${last_coordresult_instance.resultBadCourseNum}</td>"""
    							} else {
    								outln"""<td class="badcoursenum"/>"""
    							}
    						} else {
    							outln"""<td class="badcoursenum"/>"""
    						}
    					}
    					if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
    						if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
    							if (last_coordresult_instance.resultCpNotFound) {
    								outln"""<td class="altitude">-</td>"""
    							} else {
    								outln"""<td class="altitude">${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
    							}
    						} else {
    							outln"""<td class="altitude"/>"""
    						}
    					}
    					outln"""</tr>"""
                    }
				}
				last_coordresult_instance = coordresult_instance
			}
			if (last_coordresult_instance) {
                if (show_curved_point || !last_coordresult_instance.HideSecret(curved_point_ids)) {
    				outln"""	<tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
    				outln"""		<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
    				outln"""		<td class="plancptime">${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
    				if (last_coordresult_instance.resultCpNotFound) {
    					outln"""	<td class="cptime">-</td>"""
    				} else {
    					outln"""	<td class="cptime">${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
    				}
    				if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
    					outln"""	<td class="procedureturn"/>"""
    				}
    				if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
    					if (last_coordresult_instance.resultEntered) {
    						if (last_coordresult_instance.type.IsBadCourseCheckCoord()) {
    							outln"""<td class="badcoursenum">${last_coordresult_instance.resultBadCourseNum}</td>"""
    						} else {
    						   	outln"""<td class="badcoursenum"/>"""
    						}
    					} else {
    					   	outln"""<td class="badcoursenum"/>"""
    					}
    				}
    				if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
    					if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
    						if (last_coordresult_instance.resultCpNotFound) {
    							outln"""<td class="altitude">-</td>"""
    						} else {
    							outln"""<td class="altitude">${last_coordresult_instance.resultAltitude}${message(code:'fc.foot')}</td>"""
    						}
    					} else {
    						outln"""<td class="altitude"/>"""
    					}
    				}
    				outln"""	</tr>"""
                }
			}
			outln"""	</tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    def flightTestMapPrintable = { attrs, body ->
        if (attrs.flightMapFileName) {
            outln"""<img src="${attrs.flightMapFileName}" style="width:100%;" />"""
            outln"""<div>${message(code:'fc.offlinemap.noevaluation')}</div>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private String coordresult_link(CoordResult coordResultInstance, String name, boolean procedureTurn, String link, long next_id)
    {
        String t = name
        if (procedureTurn) {
            if (coordResultInstance.resultProcedureTurnEntered) {
              t += """ <img src="${createLinkTo(dir:'',file:'images/skin/ok.png')}"/>"""
            } else {
                t += " ..."
            }
        } else {
            if (coordResultInstance.resultEntered) {
              t += """ <img src="${createLinkTo(dir:'',file:'images/skin/ok.png')}"/>"""
            } else {
                t += " ..."
            }
        }
        if (next_id) {
            return """<a href="${link}/${coordResultInstance.id}?name=${name}&next=${next_id}">${t}</a>"""
        }
        return """<a href="${link}/${coordResultInstance.id}?name=${name}">${t}</a>"""
    }

    // --------------------------------------------------------------------------------------------------------------------
    def flightTestLoggerDataPrintable = { attrs, body ->
        if (attrs.t.IsLoggerData()) {
            String time_zone = attrs.t.task.contest.timeZone
            CoordPresentation coord_presentation = attrs.t.task.contest.coordPresentation
            LoggerResult logger_result = attrs.t.loggerResult
            outln"""<table class="loggerdatalist">"""
            outln"""    <thead>"""
            outln"""        <tr class="title">"""
            outln"""            <th class="time">${message(code:'fc.time.local')}</th>"""
            outln"""            <th class="latitude">${message(code:'fc.latitude')}</th>"""
            outln"""            <th class="longitude">${message(code:'fc.longitude')}</th>"""
            outln"""            <th class="altitude">${message(code:'fc.altitude')}</th>"""
            outln"""            <th class="distance">${message(code:'fc.distance.short')}</th>"""
            outln"""            <th class="track">${message(code:'fc.truetrack.short')}</th>"""
            outln"""            <th class="track">${message(code:'fc.groundspeed.short')}</th>"""
            outln"""            <th class="info">${message(code:'fc.info')}</th>"""
            outln"""        </tr>"""
            outln"""    </thead>"""
            outln"""    <tbody>"""
            int bc_sec = 0
            String last_utc = ""
            BigDecimal last_latitude = null
            BigDecimal last_longitude = null
            TrackPoint.findAllByLoggerdata(attrs.t.loggerData,[sort:"id"]).each { TrackPoint trackpoint_instance ->
                Map r = GetLoggerResult(logger_result,trackpoint_instance.utc,bc_sec)
                bc_sec = r.bcsec
                outln"""    <tr class="${r.trclass}">"""
                if (trackpoint_instance.interpolated) {
                    outln"""    <td class="time">${FcTime.UTCGetLocalTime(trackpoint_instance.utc,time_zone)} i</td>"""
                } else {
                    outln"""    <td class="time">${FcTime.UTCGetLocalTime(trackpoint_instance.utc,time_zone)}</td>"""
                }
                outln"""        <td class="latitude">${coord_presentation.GetCoordName(trackpoint_instance.latitude,true)}</td>"""
                outln"""        <td class="longitude">${coord_presentation.GetCoordName(trackpoint_instance.longitude,false)}</td>"""
                outln"""        <td class="altitude">${trackpoint_instance.altitude}${message(code:'fc.foot')}</td>"""
                if (trackpoint_instance.track != null) {
                    Map leg = AviationMath.calculateLeg(trackpoint_instance.latitude,trackpoint_instance.longitude,last_latitude,last_longitude)
                    int diff_seconds = FcTime.UTCTimeDiffSeconds(last_utc, trackpoint_instance.utc)
                    BigDecimal ground_speed = leg.dis * 3600 / diff_seconds
                    outln"""    <td class="distance">${FcMath.RoundTrackpointDistance(leg.dis*GpxService.kmPerNM*1000)}${message(code:'fc.m')}</td>"""
                    outln"""    <td class="truetrack">${trackpoint_instance.track}${message(code:'fc.grad')}</td>"""
                    outln"""    <td class="groundspeed">${FcMath.SpeedStr_Flight(ground_speed)}${message(code:'fc.knot')}</td>"""
                } else {
                    outln"""    <td class="distance">-</td>"""
                    outln"""    <td class="truetrack">-</td>"""
                    outln"""    <td class="groundspeed">-</td>"""
                }
                outln"""        <td class="${r.tdclass}">${r.info}</td>"""
                outln "     </tr>"
                last_utc = trackpoint_instance.utc
                last_latitude = trackpoint_instance.latitude
                last_longitude = trackpoint_instance.longitude
            }
            outln"""    </tbody>"""
            outln"""</table>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private Map GetLoggerResult(LoggerResult loggerResult, String utc, int bcSec)
    {
        Map ret = [info:'', trclass:'value', tdclass:'info', bcsec:0]
        if (loggerResult) {
            CalcResult calcresult_instance = CalcResult.findByLoggerresultAndUtc(loggerResult,utc,[sort:'utc'])
            if (calcresult_instance) {
                if (calcresult_instance.coordTitle) {
                    ret.info = calcresult_instance.coordTitle.titlePrintCode()
                    ret.trclass = "tpvalue"
                    return ret
                }
                if (calcresult_instance.badCourse) {
                    if (calcresult_instance.badCourseSeconds) {
                        bcSec = 0
                    }
                    bcSec++
                    ret.bcsec = bcSec 
                    ret.info = "BC ${bcSec}${message(code:'fc.time.s')}"
                    if (calcresult_instance.noBadCourse) {
                        ret.tdclass = "nobadcourseinfo"
                    } else {
                        ret.tdclass = "badcourseinfo"
                    }
                    return ret
                }
            }
        }
        return ret
    }
    
	// --------------------------------------------------------------------------------------------------------------------
    private String get_check_altitude_str(Route routeInstance, CoordResult coordresultInstance)
    {
        String ret_str = ""
        int min_altitude = coordresultInstance.GetMinAltitudeAboveGround(routeInstance.altitudeAboveGround)
        if (coordresultInstance.type.IsAltitudeCheckCoord()) {
            if (min_altitude != coordresultInstance.altitude) {
                ret_str += Defs.ALTITUDE_GND
            } else {
                ret_str += Defs.ALTITUDE_MINIMUM
            }
        } else {
            ret_str += Defs.ALTITUDE_GND
        }
        ret_str += "${coordresultInstance.altitude}${message(code:'fc.foot')}"
        if (coordresultInstance.type.IsAltitudeCheckCoord()) {
            if (min_altitude != coordresultInstance.altitude) {
                ret_str += " ${Defs.ALTITUDE_MINIMUM}${min_altitude}${message(code:'fc.foot')}"
            }
            int max_altitude = coordresultInstance.GetMaxAltitudeAboveGround()
            if (max_altitude) {
                ret_str += " ${Defs.ALTITUDE_MAXIMUM}${max_altitude}${message(code:'fc.foot')}"
            }
        }
        return ret_str
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
