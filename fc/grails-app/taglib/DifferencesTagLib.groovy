class DifferencesTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def listDifferences = { attrs, body ->
        Route route_instance = attrs.t.flighttest.route
        List curved_point_ids = route_instance.GetCurvedPointIds()
        boolean show_curved_point = route_instance.showCurvedPoints
        int offset = attrs.t.showOffset
        
        outln"""<table style="overflow-x: auto; display: block;">"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        outln"""            <th>${message(code:'fc.test.results.position.short')}</th>"""
        outln"""            <th>${message(code:'fc.crew')}</th>"""
        outln"""            <th>${message(code:'fc.aircraft')}</th>"""
        outln"""            <th>${message(code:'fc.team')}</th>"""
        if (route_instance.contest.resultClasses) {
            outln"""         <th>${message(code:'fc.resultclass')}</th>"""
        }
        List value_list = []
        int pos = 0
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
            if (coordroute_instance.type.IsCpCheckCoord()) {
                if (attrs.t.showTurnPoints && (show_curved_point || !coordroute_instance.HideSecret(curved_point_ids))) {
                    if (pos >= offset) {
                        outln"""    <th>${coordroute_instance.titleCode()}</th>"""
                    }
                    pos++
                    value_list += [enroute:false,value:0,unevaluated:0]
                }
                if (attrs.t.showTurnPointSigns && coordroute_instance.IsTurnpointSign()) {
                    if (pos >= offset) {
                        String turnpoint_sign = coordroute_instance.GetTurnpointSign()
                        if (!attrs.t.showTurnPoints) {
                            outln"""<th>${coordroute_instance.titleCode()} ${turnpoint_sign}</th>"""
                        } else {
                            outln"""<th>${turnpoint_sign}</th>"""
                        }
                    }
                    pos++
                    value_list += [enroute:true,value:0,unevaluated:0]
                }
                if (attrs.t.showEnroutePhotos && route_instance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                    for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRouteAndTypeAndTitleNumber(route_instance,coordroute_instance.type,coordroute_instance.titleNumber,[sort:"enrouteViewPos"])) {
                        if (pos >= offset) {
                            outln"""    <th>${coordenroutephoto_instance.enroutePhotoName}</th>"""
                        }
                        pos++
                        value_list += [enroute:true,value:0,unevaluated:0]
                    }
                }
                if (attrs.t.showEnrouteCanavas && route_instance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                    for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(route_instance,coordroute_instance.type,coordroute_instance.titleNumber,[sort:"enrouteViewPos"])) {
                        if (pos >= offset) {
                            outln"""    <th><img src="${createLinkTo(dir:'',file:coordenroutecanvas_instance.enrouteCanvasSign.imageName)}" style="height:9px;"/></th>"""
                        }
                        pos++
                        value_list += [enroute:true,value:0,unevaluated:0]
                    }
                }
            }
        }
        if (attrs.t.showEnroutePhotos && route_instance.enroutePhotoRoute == EnrouteRoute.InputName) {
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
                if (pos >= offset) {
                    outln"""    <th>${coordenroutephoto_instance.enroutePhotoName}</th>"""
                }
                pos++
                value_list += [enroute:true,value:0,unevaluated:0]
            }
        }
        if (attrs.t.showEnrouteCanavas && route_instance.enrouteCanvasRoute == EnrouteRoute.InputName) {
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
                if (pos >= offset) {
                    outln"""    <th><img src="${createLinkTo(dir:'',file:coordenroutecanvas_instance.enrouteCanvasSign.imageName)}" style="height:9px;"/></th>"""
                }
                pos++
                value_list += [enroute:true,value:0,unevaluated:0]
            }
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        int i = 0
        int crew_num = 0
        String sort_value = "taskPosition"
        while (true) {
            for (Test test_instance in Test.findAllByTask(attrs.t,[sort:sort_value])) {
                boolean show_crew = false
                if (sort_value == "taskPosition" && test_instance.taskPosition) {
                    show_crew = true
                } else if (sort_value == "viewpos" && !test_instance.taskPosition) {
                    show_crew = true
                }
                if (!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.timeCalculated && show_crew) {
                    crew_num++
                    int j = 0
                    outln"""<tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
                    outln"""    <td style="white-space:nowrap;">${test_instance.taskPosition}</td>"""
                    outln"""    <td style="white-space:nowrap;">${test_instance.crew.startNum} - ${test_instance.crew.name}</td>"""
                    if (test_instance.taskAircraft) {
                        outln"""<td style="white-space:nowrap;">${test_instance.taskAircraft.registration}</td>"""
                    } else {
                        outln"""<td style="white-space:nowrap;">-</td>"""
                    }
                    if (test_instance.crew.team) {
                        if (test_instance.crew.disabledTeam) {
                            outln"""<td class="team">(${test_instance.crew.team.name})</td>"""
                        } else {
                            outln"""<td class="team">${test_instance.crew.team.name}</td>"""
                        }
                    } else {
                        outln"""<td class="team">-</td>"""
                    }
                    if (route_instance.contest.resultClasses) {
                        outln"""<td style="white-space:nowrap;">${test_instance.crew.resultclass.name}</td>"""
                    }
                    pos = 0
                    for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
                        if (coordroute_instance.type.IsCpCheckCoord()) {
                            if (attrs.t.showTurnPoints && (show_curved_point || !coordroute_instance.HideSecret(curved_point_ids))) {
                                String s = ""
                                CoordResult coordresult_instance = CoordResult.findByTestAndTypeAndTitleNumber(test_instance,coordroute_instance.type,coordroute_instance.titleNumber)
                                if (!coordresult_instance.resultEntered) {
                                    value_list[j].unevaluated++
                                    s = "-"
                                } else if (coordresult_instance.resultCpNotFound) {
                                    value_list[j].unevaluated++
                                    s = message(code:'fc.flighttest.cpnotfound.short2')
                                } else {
                                    int plancptime_seconds = FcMath.Seconds(coordresult_instance.planCpTime)
                                    int resultcptime_seconds = FcMath.Seconds(coordresult_instance.resultCpTime)
                                    int diff_cptime_seconds =  resultcptime_seconds - plancptime_seconds
                                    value_list[j].value += diff_cptime_seconds
                                    s = "${diff_cptime_seconds}${message(code:'fc.time.s')}"
                                }
                                if (pos >= offset) {
                                    outln"""<td style="white-space:nowrap;">${s}</td>"""
                                }
                                pos++
                                j++
                            }
                            if (attrs.t.showTurnPointSigns && coordroute_instance.IsTurnpointSign()) {
                                TurnpointData turnpointdata_instance = TurnpointData.findByTestAndTpTypeAndTpNumber(test_instance,coordroute_instance.type,coordroute_instance.titleNumber)
                                if (turnpointdata_instance) {
                                    if (turnpointdata_instance.resultValue == EvaluationValue.Unevaluated) {
                                        value_list[j].unevaluated++
                                    } else if (turnpointdata_instance.resultValue.Found()) {
                                        value_list[j].value++
                                    }
                                    if (pos >= offset) {
                                        outln"""<td style="white-space:nowrap;">${message(code:turnpointdata_instance.resultValue.turnpointResultCode+'.short')}</td>"""
                                    }
                                } else {
                                    if (pos >= offset) {
                                        outln"""<td style="white-space:nowrap;">-</td>"""
                                    }
                                }
                                pos++
                                j++
                            }
                            if (attrs.t.showEnroutePhotos && route_instance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                                for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRouteAndTypeAndTitleNumber(route_instance,coordroute_instance.type,coordroute_instance.titleNumber,[sort:"enrouteViewPos"])) {
                                    EnroutePhotoData enroutephotodata_instance = EnroutePhotoData.findByTestAndTpTypeAndTpNumber(test_instance,coordroute_instance.type,coordroute_instance.titleNumber)
                                    if (enroutephotodata_instance) {
                                        if (enroutephotodata_instance.resultValue == EvaluationValue.Unevaluated) {
                                            value_list[j].unevaluated++
                                        } else if (enroutephotodata_instance.resultValue.Found()) {
                                            value_list[j].value++
                                        }
                                        if (pos >= offset) {
                                            outln"""<td style="white-space:nowrap;">${message(code:enroutephotodata_instance.resultValue.enrouteResultCode+'.short')}</td>"""
                                        }
                                    } else {
                                        if (pos >= offset) {
                                            outln"""<td style="white-space:nowrap;">-</td>"""
                                        }
                                    }
                                    pos++
                                    j++
                                }
                            }
                            if (attrs.t.showEnrouteCanavas && route_instance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                                for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(route_instance,coordroute_instance.type,coordroute_instance.titleNumber,[sort:"enrouteViewPos"])) {
                                    EnrouteCanvasData enroutecanvasdata_instance = EnrouteCanvasData.findByTestAndTpTypeAndTpNumber(test_instance,coordroute_instance.type,coordroute_instance.titleNumber)
                                    if (enroutecanvasdata_instance) {
                                        if (enroutecanvasdata_instance.resultValue == EvaluationValue.Unevaluated) {
                                            value_list[j].unevaluated++
                                        } else if (enroutecanvasdata_instance.resultValue.Found()) {
                                            value_list[j].value++
                                        }
                                        if (pos >= offset) {
                                            outln"""<td style="white-space:nowrap;">${message(code:enroutecanvasdata_instance.resultValue.enrouteResultCode+'.short')}</td>"""
                                        }
                                    } else {
                                        if (pos >= offset) {
                                            outln"""<td style="white-space:nowrap;">-</td>"""
                                        }
                                    }
                                    pos++
                                    j++
                                }
                            }
                        }
                    }
                    if (attrs.t.showEnroutePhotos && route_instance.enroutePhotoRoute == EnrouteRoute.InputName) {
                        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
                            EnroutePhotoData enroutephotodata_instance = EnroutePhotoData.findByTestAndPhotoName(test_instance,coordenroutephoto_instance.enroutePhotoName)
                            if (enroutephotodata_instance.resultValue == EvaluationValue.Unevaluated) {
                                value_list[j].unevaluated++
                            } else if (enroutephotodata_instance.resultValue.Found()) {
                                value_list[j].value++
                            }
                            if (pos >= offset) {
                                outln"""<td style="white-space:nowrap;">${message(code:enroutephotodata_instance.resultValue.enrouteResultCode+'.short')}</td>"""
                            }
                            pos++
                            j++
                        }
                    }
                    if (attrs.t.showEnrouteCanavas && route_instance.enrouteCanvasRoute == EnrouteRoute.InputName) {
                        for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
                            EnrouteCanvasData enroutecanvasdata_instance = EnrouteCanvasData.findByTestAndCanvasSign(test_instance,coordenroutecanvas_instance.enrouteCanvasSign)
                            if (enroutecanvasdata_instance.resultValue == EvaluationValue.Unevaluated) {
                                value_list[j].unevaluated++
                            } else if (enroutecanvasdata_instance.resultValue.Found()) {
                                value_list[j].value++
                            }
                            if (pos >= offset) {
                                outln"""<td style="white-space:nowrap;">${message(code:enroutecanvasdata_instance.resultValue.enrouteResultCode+'.short')}</td>"""
                            }
                            pos++
                            j++
                        }
                    }
                    outln"""</tr>"""
                    i++
                }
            }
            if (sort_value == "viewpos") {
                break
            }
            sort_value = "viewpos"
        }
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
        int columns = 4
        if (route_instance.contest.resultClasses) {
            columns++
        }
        outln"""        <td colspan="${columns}"><b>${message(code:'fc.average')}</b></td>"""
        pos = 0
        for (int p = 0; p < value_list.size(); p++) {
            if (crew_num > value_list[p].unevaluated) {
                if (value_list[p].enroute) {
                    if (pos >= offset) {
                        int found_percent = value_list[p].value * 100 / (crew_num - value_list[p].unevaluated)
                        outln"""    <td style="white-space:nowrap;">${found_percent}${message(code:'fc.percent')}</td>"""
                        columns++
                    }
                    pos++
                } else {
                    if (pos >= offset) {
                        int average = (value_list[p].value / (crew_num - value_list[p].unevaluated)).toInteger()
                        outln"""    <td style="white-space:nowrap;">${average}${message(code:'fc.time.s')}</td>"""
                        columns++
                    }
                    pos++
                }
            } else {
                outln"""    <td style="white-space:nowrap;">-</td>"""
                columns++
            }
        }
        outln"""        </tr>"""
        outln"""        <tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
        outln"""            <td colspan="${columns}">&nbsp;</td>"""
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
