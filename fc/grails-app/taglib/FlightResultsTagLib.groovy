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
            outln"""                <th>${message(code:'fc.aflos.checkpoint')}</th>"""
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
            CoordResult last_coordresult_instance = null
            for(CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
                
                // procedure turn
                if (coordresult_instance.planProcedureTurn && (attrs.t.task.procedureTurnDuration > 0)) {
                    leg_no++
                    outln"""    <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                    outln"""        <td>${coordresult_link(coordresult_instance, leg_no.toString(), true, createLink(controller:'coordResult',action:'editprocedureturn'),0)}</td>"""
                    outln"""        <td>${message(code:'fc.procedureturn')}</td>"""
                    outln"""        <td/>"""
                    outln"""        <td>${message(code:'fc.test.results.measured')}</td>"""
                    //outln"""      <td/>"""
                    //outln"""      <td/>"""
                    if (coordresult_instance.resultProcedureTurnEntered) {
                        if (coordresult_instance.resultProcedureTurnNotFlown) {
                            if (attrs.t.task.disabledCheckPointsProcedureTurn.contains(last_coordresult_instance.title()+',')) {
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
                    outln"""        <td/>"""
                    outln"""        <td>${message(code:'fc.test.results.penalty')}</td>"""
                    //outln"""      <td/>"""
                    //outln"""      <td/>"""
                    if (coordresult_instance.resultProcedureTurnEntered) {
                        if (last_coordresult_instance && attrs.t.task.disabledCheckPointsProcedureTurn.contains(last_coordresult_instance.title()+',')) {
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
                outln"""            <td>${coordresult_instance.mark}</td>"""
                outln"""            <td>${message(code:'fc.test.results.plan')}</td>"""
                //outln"""          <td>${coordresult_instance.latName()}</td>"""
                //outln"""          <td>${coordresult_instance.lonName()}</td>"""
                outln"""            <td>${FcMath.TimeStr(coordresult_instance.planCpTime)}</td>"""
                if (coordresult_instance.type.IsBadCourseCheckCoord()) {
                    outln"""        <td>0</td>"""
                } else {
                    outln"""        <td/>"""
                }
                outln"""            <td>${coordresult_instance.altitude}${message(code:'fc.foot')}</td>"""
                outln"""        </tr>"""
                outln"""        <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">"""
                outln"""            <td/>"""
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
                                if (attrs.t.task.disabledCheckPointsNotFound.contains(coordresult_instance.title()+',')) {
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
                        if (attrs.t.task.disabledCheckPointsBadCourse.contains(coordresult_instance.title()+',')) {
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
                            if (attrs.t.task.disabledCheckPointsMinAltitude.contains(coordresult_instance.title()+',')) {
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
                        if (attrs.t.task.disabledCheckPointsNotFound.contains(coordresult_instance.title()+',')) {
                            outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if ((coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            outln"""<td class="points">${coordresult_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        } else {
                            outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        }
                    } else if (attrs.t.task.disabledCheckPoints.contains(coordresult_instance.title()+',')) {
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
                            if (attrs.t.task.disabledCheckPointsBadCourse.contains(coordresult_instance.title()+',')) {
                                outln"""    <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                            } else {
                                penalty_badcourse_summary += coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
                                String points_class = "points"
                                if (!coordresult_instance.resultBadCourseNum) {
                                    points_class = "zeropoints"
                                }
                                outln"""<td class="${points_class}">${coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} ${message(code:'fc.points')}</td>"""
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
                        if (attrs.t.task.disabledCheckPointsMinAltitude.contains(coordresult_instance.title()+',')) {
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
            outln"""        </tbody>"""
            outln"""        <tfoot>"""
            outln"""            <tr>"""
            outln"""                <td/>"""
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
                        if (attrs.t.task.disabledCheckPointsNotFound.contains(last_coordresult_instance.title()+',')) {
                            outln"""<td>-</td>"""
                        } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
                        } else {
                            outln"""<td>-</td>"""
                        }
                    } else {
                        if (attrs.t.task.disabledCheckPoints.contains(last_coordresult_instance.title()+',')) {
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
                                if (attrs.t.task.disabledCheckPointsProcedureTurn.contains(last_coordresult_instance.title()+',')) {
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
                            if (attrs.t.task.disabledCheckPointsBadCourse.contains(last_coordresult_instance.title()+',')) {
                                if (last_coordresult_instance.resultBadCourseNum > 0) {
                                    outln"""<td>- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                } else {
                                    outln"""<td>-</td>"""
                                }
                            } else {
                                penalty_badcourse_summary += last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
                                if (last_coordresult_instance.resultBadCourseNum > 0) {
                                    outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                } else {
                                    outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()}</td>"""
                                }
                            }
                        } else {
                            outln"""<td/>"""
                        }
                    }
                    if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
                        if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                            if (last_coordresult_instance.resultAltitude) {
                                if (attrs.t.task.disabledCheckPointsMinAltitude.contains(last_coordresult_instance.title()+',')) {
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
                    if (attrs.t.task.disabledCheckPointsNotFound.contains(last_coordresult_instance.title()+',')) {
                        outln"""<td>-</td>"""
                    } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                        outln"""<td>${last_coordresult_instance.penaltyCoord}</td>"""
                    } else {
                        outln"""<td>-</td>"""
                    }
                } else {
                    if (attrs.t.task.disabledCheckPoints.contains(last_coordresult_instance.title()+',')) {
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
                        if (attrs.t.task.disabledCheckPointsBadCourse.contains(last_coordresult_instance.title()+',')) {
                            if (last_coordresult_instance.resultBadCourseNum > 0) {
                                outln"""<td>- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                            } else {
                                outln"""<td>-</td>"""
                            }
                        } else {
                            penalty_badcourse_summary += last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
                            if (last_coordresult_instance.resultBadCourseNum > 0) {
                                outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} (${last_coordresult_instance.resultBadCourseNum})</td>"""
                            } else {
                                outln"""<td>${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()}</td>"""
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
        if (attrs.t.flightTestOtherPenalties > 0) {
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
			outln"""<table class="flightresultlist">"""
			outln"""	<thead>"""
			outln"""		<tr class="name1">"""
			outln"""			<th>${message(code:'fc.title')}</th>"""
			outln"""			<th>${message(code:'fc.aflos.checkpoint')}</th>"""
			outln"""			<th colspan="3">${message(code:'fc.cptime')}</th>"""
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
			outln"""			<th/>"""
			outln"""			<th>${message(code:'fc.test.results.plan')}</th>"""
			outln"""			<th>${message(code:'fc.test.results.measured')}</th>"""
			outln"""			<th>${message(code:'fc.points')}</th>"""
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
			Integer penalty_coord_summary = 0
			Integer	penalty_procedureturn_summary = 0
			Integer	penalty_badcourse_summary = 0
			Integer penalty_altitude_summary = 0
			boolean check_secretpoints = attrs.t.IsFlightTestCheckSecretPoints()
			CoordResult last_coordresult_instance = null
			for (CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
				if (last_coordresult_instance) {
					if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
						penalty_coord_summary += last_coordresult_instance.penaltyCoord
					}
					outln"""<tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
					outln"""	<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
					outln"""	<td class="aflosname">${last_coordresult_instance.mark}</td>"""
					outln"""	<td class="plancptime">${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
					if (last_coordresult_instance.resultCpNotFound) {
						outln"""<td class="cptime">-</td>"""
					} else {
						outln"""<td class="cptime">${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
					}
                    if (last_coordresult_instance.resultCpNotFound) {
                        if (attrs.t.task.disabledCheckPointsNotFound.contains("${last_coordresult_instance.title()},")) {
                            outln"""<td class="penaltycp">-</td>"""
                        } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                            outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
                        } else {
                            outln"""<td class="penaltycp">-</td>"""
                        }
                    } else {
                        if (attrs.t.task.disabledCheckPoints.contains("${last_coordresult_instance.title()},")) {
                            outln"""<td class="penaltycp">-</td>"""
                        } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
    						outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
    					} else {
    						outln"""<td class="penaltycp">-</td>"""
    					}
                    }
					if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
						if (coordresult_instance.planProcedureTurn) {
							if (coordresult_instance.resultProcedureTurnEntered) {
								if (attrs.t.task.disabledCheckPointsProcedureTurn.contains("${last_coordresult_instance.title()},")) {
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
                            if (attrs.t.task.disabledCheckPointsBadCourse.contains(last_coordresult_instance.title()+',')) {
                                if (last_coordresult_instance.resultBadCourseNum > 0) {
                                    outln"""<td class="penaltybadcourse">- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                                } else {
                                    outln"""<td class="penaltybadcourse">-</td>"""
                                }
                            } else {
    							penalty_badcourse_summary += last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
    							if (last_coordresult_instance.resultBadCourseNum > 0) {
    								outln"""<td class="penaltybadcourse">${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} (${last_coordresult_instance.resultBadCourseNum})</td>"""
    							} else {
    								outln"""<td class="penaltybadcourse">${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()}</td>"""
    							}
                            }
						} else {
							outln"""<td class="penaltybadcourse"/>"""
						}
					}
					if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
						if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                            if (attrs.t.task.disabledCheckPointsMinAltitude.contains("${last_coordresult_instance.title()},")) {
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
				last_coordresult_instance = coordresult_instance
			}
			if (last_coordresult_instance) {
				if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
					penalty_coord_summary += last_coordresult_instance.penaltyCoord
				}
				outln"""    <tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
				outln"""		<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
				outln"""		<td class="aflosname">${last_coordresult_instance.mark}</td>"""
				outln"""		<td class="plancptime">${FcMath.TimeStr(last_coordresult_instance.planCpTime)}</td>"""
				if (last_coordresult_instance.resultCpNotFound) {
					outln"""	<td class="cptime">-</td>"""
				} else {
					outln"""	<td class="cptime">${FcMath.TimeStr(last_coordresult_instance.resultCpTime)}</td>"""
				}
                if (last_coordresult_instance.resultCpNotFound) {
                    if (attrs.t.task.disabledCheckPointsNotFound.contains("${last_coordresult_instance.title()},")) {
                        outln"""<td class="penaltycp">-</td>"""
                    } else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
                        outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
                    } else {
                        outln"""<td class="penaltycp">-</td>"""
                    }
                } else {
                    if (attrs.t.task.disabledCheckPoints.contains("${last_coordresult_instance.title()},")) {
                        outln"""<td class="penaltycp">-</td>"""
    				} else if ((last_coordresult_instance.type != CoordType.SECRET) || check_secretpoints) {
    					outln"""<td class="penaltycp">${last_coordresult_instance.penaltyCoord}</td>"""
    				} else {
    					outln"""<td class="penaltycp">-</td>"""
    				}
                }
				if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
					outln"""	<td class="penaltyprocedureturn"/>"""
				}
				if (attrs.t.GetFlightTestBadCoursePoints() > 0) {
					if (last_coordresult_instance.resultEntered && last_coordresult_instance.type.IsBadCourseCheckCoord()) {
                        if (attrs.t.task.disabledCheckPointsBadCourse.contains(last_coordresult_instance.title()+',')) {
                            if (last_coordresult_instance.resultBadCourseNum > 0) {
                                outln"""<td class="penaltybadcourse">- (${last_coordresult_instance.resultBadCourseNum})</td>"""
                            } else {
                                outln"""<td class="penaltybadcourse">-</td>"""
                            }
                        } else {
    						penalty_badcourse_summary += last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()
    						if (last_coordresult_instance.resultBadCourseNum > 0) {
    							outln"""<td class="penaltybadcourse">${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()} (${last_coordresult_instance.resultBadCourseNum})</td>"""
    						} else {
    						   	outln"""<td class="penaltybadcourse">${last_coordresult_instance.resultBadCourseNum*attrs.t.GetFlightTestBadCoursePoints()}</td>"""
    						}
                        }
					} else {
						outln"""<td class="penaltybadcourse"/>"""
					}
				}
				if (attrs.t.GetFlightTestMinAltitudeMissedPoints() > 0) {
					if (last_coordresult_instance.type.IsAltitudeCheckCoord()) {
                        if (attrs.t.task.disabledCheckPointsMinAltitude.contains("${last_coordresult_instance.title()},")) { // no min altitude check
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
			outln"""	</tbody>"""
			outln"""	<tfoot>"""
			outln"""		<tr class="summary">"""
			outln"""			<td class="tpname"/>"""
			outln"""			<td class="aflosname"/>"""
			outln"""			<td class="plancptime"/>"""
			outln"""			<td class="cptime"/>"""
			outln"""			<td class="penaltycp">${penalty_coord_summary}</td>"""
			if ((attrs.t.GetFlightTestProcedureTurnNotFlownPoints() > 0) && (attrs.t.task.procedureTurnDuration > 0)) {
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
        if (attrs.t.flightTestOtherPenalties > 0) {
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
			outln"""			<th>${message(code:'fc.title')}</th>"""
			outln"""			<th>${message(code:'fc.aflos.checkpoint')}</th>"""
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
			CoordResult last_coordresult_instance = null
			for (CoordResult coordresult_instance in CoordResult.findAllByTest(attrs.t,[sort:"id"])) {
				if (last_coordresult_instance) {
					outln"""<tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
					outln"""	<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
					outln"""	<td class="aflosname">${last_coordresult_instance.mark}</td>"""
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
				last_coordresult_instance = coordresult_instance
			}
			if (last_coordresult_instance) {
				outln"""	<tr class="value" id="${last_coordresult_instance.titlePrintCode()}">"""
				outln"""		<td class="tpname">${last_coordresult_instance.titlePrintCode()}</td>"""
				outln"""		<td class="aflosname">${last_coordresult_instance.mark}</td>"""
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
			outln"""	</tbody>"""
			outln"""</table>"""
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    private String coordresult_link(CoordResult coordResultInstance, String name, boolean procedureTurn, String link, long next_id)
    {
        String t = name
        if (procedureTurn) {
            if (coordResultInstance.resultProcedureTurnEntered) {
              t += """ <img src="/fc/images/skin/ok.png"/>"""
            } else {
                t += " ..."
            }
        } else {
            if (coordResultInstance.resultEntered) {
              t += """ <img src="/fc/images/skin/ok.png"/>"""
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
	private void outln(str)
	{
		out << """$str
"""
	}

}
