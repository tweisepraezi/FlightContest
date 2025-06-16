class ObservationResultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def observationTestComplete = { attrs, body ->
        boolean head_written = false
        if (attrs.t.IsObservationSignUsed()) {
            Route route_instance = attrs.t.flighttestwind.flighttest.route
            
            if (attrs.crewResults) {
                outln"""<table>"""
                outln"""    <thead>"""
                outln"""        <tr>"""
                if (attrs.t.observationTestComplete) {
                    outln"""        <th colspan="5" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${attrs.t.GetObservationTestVersion()})</th>"""
                } else {
                    outln"""        <th colspan="5" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${attrs.t.GetObservationTestVersion()}) [${message(code:'fc.provisional')}]</th>"""
                }
                outln"""        </tr>"""
                outln"""        <tr>"""
                outln"""            <th>${message(code:'fc.title')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.plan')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.result')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.penalty')}</th>"""
                outln"""        </tr>"""
                outln"""    </thead>"""
                outln"""    <tbody>"""
            }

            // TurnpointData
            if (attrs.t.GetTurnpointRoute().IsTurnpointSign() && attrs.t.IsObservationTestTurnpointRun()) {
                String turnpoint_headcode = ""
                int penalty_sum = 0
                switch (attrs.t.GetTurnpointRoute()) {
                    case TurnpointRoute.AssignPhoto:
                    case TurnpointRoute.TrueFalsePhoto:
                        turnpoint_headcode = 'fc.observation.turnpoint.photos'
                        break
                    case TurnpointRoute.AssignCanvas:
                        turnpoint_headcode = 'fc.observation.turnpoint.canvas'
                        break
                }
                if (!attrs.crewResults) {
                    outln"""<table>"""
                    outln"""    <thead>"""
                }
                outln"""            <tr>"""
                outln"""                <th class="table-head" colspan="5">${message(code:turnpoint_headcode)}</th>"""
                outln"""            </tr>"""
                if (!attrs.crewResults) {
                    outln"""        <tr>"""
                    outln"""            <th>${message(code:'fc.tpname')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.plan')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.result')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.penalty')}</th>"""
                    outln"""        </tr>"""
                }
                if (!attrs.crewResults) {
                    outln"""    </thead>"""
                    outln"""    <tbody>"""
                }
                for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsTurnpointObs,route_instance).contains("${turnpointdata_instance.tpTitle()},")
                    outln"""    <tr>"""
                    outln"""        <td>${turnpointdata_instance.tpName()}</td>"""
                    switch (attrs.t.GetTurnpointRoute()) {
                        case TurnpointRoute.AssignPhoto:
                        case TurnpointRoute.AssignCanvas:
                            outln"""<td>${turnpointdata_instance.evaluationSign.title}</td>"""
                            outln"""<td>${turnpointdata_instance.tpSign}</td>"""
                            break
                        case TurnpointRoute.TrueFalsePhoto:
                            outln"""<td>${message(code:turnpointdata_instance.evaluationValue.turnpointEvaluationCode)}</td>"""
                            outln"""<td>${message(code:turnpointdata_instance.tpSignCorrect.code)}</td>"""
                            break
                    }
                    outln"""         <td class="${GetResultValueClass(turnpointdata_instance.resultValue)}">${message(code:turnpointdata_instance.resultValue.turnpointResultCode)}</td>"""
                    if (is_disabled) {
                        outln"""     <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                    } else if (turnpointdata_instance.penaltyCoord) {
                        outln"""     <td class="points">${turnpointdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""     <td class="zeropoints">${turnpointdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += turnpointdata_instance.penaltyCoord
                    }
                }
                if (!attrs.crewResults) {
                    outln"""    </tbody>"""
                    outln"""    <tfoot>"""
                    outln"""        <tr>"""
                    outln"""            <td colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                    if (penalty_sum) {
                        outln"""        <td class="points">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""        <td class="zeropoints">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""        </tr>"""
                    outln"""    </tfoot>"""
                    outln"""</table>"""
                }
            }
            
            // EnroutePhotoData
            if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurement() && attrs.t.IsObservationTestEnroutePhotoRun()) {
                int penalty_sum = 0
                int col_span = 4
                if (attrs.crewResults || attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                    col_span = 5
                }
                if (!attrs.crewResults) {
                    outln"""<table>"""
                    outln"""    <thead>"""
                }
                outln"""            <tr>"""
                outln"""                <th class="table-head" colspan="${col_span}">${message(code:'fc.observation.enroute.photo.short')}</th>"""
                outln"""            </tr>"""
                if (!attrs.crewResults) {
                    outln"""        <tr>"""
                    outln"""            <th>${message(code:'fc.observation.enroute.photo.name.short')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                    if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
                    } else if (attrs.crewResults) {
                        outln"""        <th/>"""
                    }
                    outln"""            <th>${message(code:'fc.test.results.result')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.penalty')}</th>"""
                    outln"""        </tr>"""
                    outln"""    </thead>"""
                    outln"""    <tbody>"""
                }
                for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = attrs.t.task.disabledEnroutePhotoObs.contains("${enroutephotodata_instance.photoName},")
                    outln"""    <tr>"""
                    outln"""        <td>${enroutephotodata_instance.photoName}</td>"""
                    switch (attrs.t.GetEnroutePhotoMeasurement()) {
                        case EnrouteMeasurement.Map:
                            String s = ""
                            switch (enroutephotodata_instance.evaluationValue) {
                                case EvaluationValue.Correct:
                                    s = attrs.t.GetObservationTestEnrouteCorrectValueStr(false)
                                    break
                                case EvaluationValue.Inexact:
                                    s = attrs.t.GetObservationTestEnrouteInexactValueStr(false)
                                    break
                            }
                            outln"""<td>${message(code:enroutephotodata_instance.evaluationValue.enrouteEvaluationCode,args:[s])}</td>"""
                            if (attrs.crewResults) {
                                outln"""<td/>"""
                            }
                            break
                        case EnrouteMeasurement.NMFromTP:
                            if (enroutephotodata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td>${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutephotodata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td>-</td>"""
                            } else if (enroutephotodata_instance.IsEvaluationFromTPFalse()) {
                                outln"""<td>x</td>"""
                            } else {
                                outln"""<td>${enroutephotodata_instance.evaluationName()} ${FcMath.DistanceStr(enroutephotodata_instance.evaluationDistance)} ${message(code:'fc.mile')}</td>"""
                            }
                            outln"""<td>${enroutephotodata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsNM(enroutephotodata_instance.distanceNM)} ${message(code:'fc.mile')}</td>"""
                            break
                        case EnrouteMeasurement.mmFromTP:
                            if (enroutephotodata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td>${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutephotodata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td>-</td>"""
                            } else if (enroutephotodata_instance.IsEvaluationFromTPFalse()) {
                                outln"""<td>x</td>"""
                            } else {
                                outln"""<td>${enroutephotodata_instance.evaluationName()} ${FcMath.DistanceMeasureStr(enroutephotodata_instance.evaluationDistance)} ${message(code:'fc.mm')}</td>"""
                            }
                            outln"""<td>${enroutephotodata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsmm(enroutephotodata_instance.distancemm)} ${message(code:'fc.mm')}</td>"""
                            break
                    }
                    outln"""         <td class="${GetResultValueClass(enroutephotodata_instance.resultValue)}">${message(code:enroutephotodata_instance.resultValue.enrouteResultCode)}</td>"""
                    if (is_disabled) {
                        outln"""     <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                    } else if (enroutephotodata_instance.penaltyCoord) {
                        outln"""     <td class="points">${enroutephotodata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""     <td class="zeropoints">${enroutephotodata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += enroutephotodata_instance.penaltyCoord
                    }
                }
                if (!attrs.crewResults) {
                    outln"""    </tbody>"""
                    outln"""    <tfoot>"""
                    outln"""        <tr>"""
                    if (!attrs.crewResults) {
                        outln"""        <td colspan="${col_span-1}">${message(code:'fc.test.results.summary')}</td>"""
                    } else {
                        outln"""        <td colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                    }
                    if (penalty_sum) {
                        outln"""        <td class="points">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""        <td class="zeropoints">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""        </tr>"""
                    outln"""    </tfoot>"""
                    outln"""</table>"""
                }
            }

            // EnrouteCanvasData
            if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurement() && attrs.t.IsObservationTestEnrouteCanvasRun()) {
                int penalty_sum = 0
                int col_span = 4
                if (attrs.crewResults || attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                    col_span = 5
                }
                if (!attrs.crewResults) {
                    outln"""<table>"""
                    outln"""    <thead>"""
                }
                outln"""            <tr>"""
                outln"""                <th class="table-head" colspan="${col_span}">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""            </tr>"""
                if (!attrs.crewResults) {
                    outln"""        <tr>"""
                    outln"""            <th>${message(code:'fc.observation.enroute.canvas.sign')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                    if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
                    } else if (attrs.crewResults) {
                        outln"""        <th/>"""
                    }
                    outln"""            <th>${message(code:'fc.test.results.result')}</th>"""
                    outln"""            <th>${message(code:'fc.test.results.penalty')}</th>"""
                    outln"""        </tr>"""
                    outln"""    </thead>"""
                    outln"""    <tbody>"""
                }
                for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = attrs.t.task.disabledEnrouteCanvasObs.contains("${enroutecanvasdata_instance.canvasSign.canvasName},")
                    String image_name = ""
					if (enroutecanvasdata_instance.canvasSign.imageName) {
						image_name = createLinkTo(dir:'',file:enroutecanvasdata_instance.canvasSign.imageName)
					}
                    outln"""    <tr>"""
					if (image_name) {
						outln"""    <td><img class="observationcompletecanvasimage" src="${image_name}"/> ${enroutecanvasdata_instance.canvasSign.canvasName}</td>"""
					} else {
						outln"""    <td><img class="observationcompletecanvasimage" /> ${enroutecanvasdata_instance.canvasSign.canvasName}</td>"""
					}
                    switch (attrs.t.GetEnrouteCanvasMeasurement()) {
                        case EnrouteMeasurement.Map:
                            String s = ""
                            switch (enroutecanvasdata_instance.evaluationValue) {
                                case EvaluationValue.Correct:
                                    s = attrs.t.GetObservationTestEnrouteCorrectValueStr(false)
                                    break
                                case EvaluationValue.Inexact:
                                    s = attrs.t.GetObservationTestEnrouteInexactValueStr(false)
                                    break
                            }
                            outln"""<td>${message(code:enroutecanvasdata_instance.evaluationValue.enrouteEvaluationCode,args:[s])}</td>"""
                            if (attrs.crewResults) {
                                outln"""<td/>"""
                            }
                            break
                        case EnrouteMeasurement.NMFromTP:
                            if (enroutecanvasdata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td>${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutecanvasdata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td>-</td>"""
                            } else if (enroutecanvasdata_instance.IsEvaluationFromTPFalse()) {
                                outln"""<td>x</td>"""
                            } else {
                                outln"""<td>${enroutecanvasdata_instance.evaluationName()} ${FcMath.DistanceStr(enroutecanvasdata_instance.evaluationDistance)} ${message(code:'fc.mile')}</td>"""
                            }
                            outln"""<td>${enroutecanvasdata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsNM(enroutecanvasdata_instance.distanceNM)} ${message(code:'fc.mile')}</td>"""
                            break
                        case EnrouteMeasurement.mmFromTP:
                            if (enroutecanvasdata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td>${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutecanvasdata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td>-</td>"""
                            } else if (enroutecanvasdata_instance.IsEvaluationFromTPFalse()) {
                                outln"""<td>x</td>"""
                            } else {
                                outln"""<td>${enroutecanvasdata_instance.evaluationName()} ${FcMath.DistanceMeasureStr(enroutecanvasdata_instance.evaluationDistance)} ${message(code:'fc.mm')}</td>"""
                            }
                            outln"""<td>${enroutecanvasdata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsmm(enroutecanvasdata_instance.distancemm)} ${message(code:'fc.mm')}</td>"""
                            break
                    }
                    outln"""         <td class="${GetResultValueClass(enroutecanvasdata_instance.resultValue)}">${message(code:enroutecanvasdata_instance.resultValue.enrouteResultCode)}</td>"""
                    if (is_disabled) {
                        outln"""     <td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                    } else if (enroutecanvasdata_instance.penaltyCoord) {
                        outln"""     <td class="points">${enroutecanvasdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""     <td class="zeropoints">${enroutecanvasdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += enroutecanvasdata_instance.penaltyCoord
                    }
                }
                if (!attrs.crewResults) {
                    outln"""    </tbody>"""
                    outln"""    <tfoot>"""
                    outln"""        <tr>"""
                    if (!attrs.crewResults) {
                        outln"""        <td colspan="${col_span-1}">${message(code:'fc.test.results.summary')}</td>"""
                    } else {
                        outln"""        <td colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                    }
                    if (penalty_sum) {
                        outln"""        <td class="points">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""        <td class="zeropoints">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""        </tr>"""
                    outln"""    </tfoot>"""
                    outln"""</table>"""
                }
            }

            if (attrs.crewResults) {
                outln"""</table>"""
            }
        }
        
        outln"""<table>"""
        if (attrs.crewResults && !attrs.t.IsObservationSignUsed()) {
            outln"""<thead>"""
            outln"""    <tr>"""
            if (attrs.t.observationTestComplete) {
                outln"""    <th colspan="4" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${attrs.t.GetObservationTestVersion()})</th>"""
            } else {
                outln"""    <th colspan="4" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${attrs.t.GetObservationTestVersion()}) [${message(code:'fc.provisional')}]</th>"""
            }
            outln"""    </tr>"""
            outln"""</thead>"""
        }
        outln"""    <tbody>"""
        if (attrs.t.IsObservationTestTurnpointRun()) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.observationresults.turnpointphotopenalties')}:</td>"""
            outln"""        <td>${attrs.t.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.IsObservationTestEnroutePhotoRun()) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.observationresults.routephotopenalties')}:</td>"""
            outln"""        <td>${attrs.t.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.IsObservationTestEnrouteCanvasRun()) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.observationresults.groundtargetpenalties')}:</td>"""
            outln"""        <td>${attrs.t.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.observationTestOtherPenalties != 0) {
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.observationresults.otherpenalties')}:</td>"""
            outln"""        <td>${attrs.t.observationTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""    </tbody>"""
        if (attrs.crewResults) {
            outln"""<tfoot>"""
            outln"""    <tr>"""
            outln"""        <td class="detailtitle">${message(code:'fc.penalties')}:</td>"""
            String points_class = "points"
            if (!attrs.t.observationTestPenalties) {
                points_class = "zeropoints"
            }
            outln"""        <td class="${points_class}">${attrs.t.observationTestPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
            outln"""</tfoot>"""
        }
        outln"""</table>"""
    } // def observationTestComplete
    
    // --------------------------------------------------------------------------------------------------------------------
    def observationTestInput = { attrs, body ->
		boolean show_buttons = BootStrap.global.ShowObservationButtons()
		attrs.complete[0] = true
        if (attrs.t.IsObservationSignUsed()) {
            Route route_instance = attrs.t.flighttestwind.flighttest.route
            // TurnpointData
            if (attrs.t.GetTurnpointRoute().IsTurnpointSign() && attrs.t.IsObservationTestTurnpointRun()) {
                String turnpoint_headcode = ""
                int penalty_sum = 0
                switch (attrs.t.GetTurnpointRoute()) {
                    case TurnpointRoute.AssignPhoto:
                    case TurnpointRoute.TrueFalsePhoto:
                        turnpoint_headcode = 'fc.observation.turnpoint.photos'
                        break
                    case TurnpointRoute.AssignCanvas:
                        turnpoint_headcode = 'fc.observation.turnpoint.canvas'
                        break
                }
                boolean complete = true
                for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(attrs.t,[sort:"id"])) {
                    if (turnpointdata_instance.resultValue == EvaluationValue.Unevaluated) {
                        complete = false
						attrs.complete[0] = false
                    }
                }
                outln"""<table>"""
                outln"""    <thead>"""
                outln"""        <tr>"""
                if (complete) {
                    outln"""        <th class="table-head" colspan="5">${message(code:turnpoint_headcode)}</th>"""
                } else {
                    outln"""        <th class="table-head" colspan="2">${message(code:turnpoint_headcode)}</th>"""
                }
                outln"""        </tr>"""
                outln"""        <tr>"""
                outln"""            <th>${message(code:'fc.tpname')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                if (complete) {
                    outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
                    outln"""        <th>${message(code:'fc.test.results.result')}</th>"""
                    outln"""        <th>${message(code:'fc.test.results.penalty')}</th>"""
                }
                outln"""        </tr>"""
                outln"""    </thead>"""
                outln"""    <tbody>"""
                for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsTurnpointObs,route_instance).contains("${turnpointdata_instance.tpTitle()},")
                    outln"""    <tr>"""
                    outln"""        <td>${turnpointdata_instance.tpName()}</td>"""
                    switch (attrs.t.GetTurnpointRoute()) {
                        case TurnpointRoute.AssignPhoto:
                        case TurnpointRoute.AssignCanvas:
                            outln"""<td>"""
                            outln"""    <select name="turnpointdataevaluation_${turnpointdata_instance.id}" tabIndex="${attrs.ti[0]++}">"""
                            for (TurnpointSign turnpoint_sign in TurnpointSign.GetEvaluationSigns(attrs.t.GetTurnpointRoute() == TurnpointRoute.AssignCanvas)) {
                                if (turnpoint_sign == turnpointdata_instance.evaluationSign) {
                                    outln"""<option selected="selected">"""
                                } else {
                                    outln"""<option>"""
                                }
                                outln"""   ${turnpoint_sign.title.encodeAsHTML()}"""
                                outln"""    </option>"""
                            }
                            outln"""    </select>"""
                            outln"""</td>"""
                            if (complete) {
                                outln"""<td>${turnpointdata_instance.tpSign.title}</td>"""
                            }
                            break
                        case TurnpointRoute.TrueFalsePhoto:
                            outln"""<td>"""
                            outln"""    <div>"""
                            for (def v in EvaluationValue.GetEvaluationValues(false)) {
                                if (turnpointdata_instance.evaluationValue == v) {
                                    outln"""<label class="observationinputradio"><input type="radio" name="turnpointdataevaluation_${turnpointdata_instance.id}" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.turnpointEvaluationCode)}</label>"""
                                } else {
                                    outln"""<label class="observationinputradio"><input type="radio" name="turnpointdataevaluation_${turnpointdata_instance.id}" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.turnpointEvaluationCode)}</label>"""
                                }
                            }
                            attrs.ti[0]++
                            outln"""        <br/>"""
                            outln"""    </div>"""
                            outln"""</td>"""
                            if (complete) {
                                outln"""<td>${message(code:turnpointdata_instance.tpSignCorrect.code)}</td>"""
                            }
                            break
                    }
                    if (complete) {
                        outln"""    <td class="${GetResultValueClass(turnpointdata_instance.resultValue)}">${message(code:turnpointdata_instance.resultValue.turnpointResultCode)}</td>"""
                        if (is_disabled) {
                            outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if (turnpointdata_instance.penaltyCoord) {
                            outln"""<td class="points">${turnpointdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        } else {
                            outln"""<td class="zeropoints">${turnpointdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        }
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += turnpointdata_instance.penaltyCoord
                    }
                }
                outln"""    </tbody>"""
                outln"""    <tfoot>"""
                outln"""        <tr>"""
                if (complete) {
                    outln"""        <td colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                } else {
                    outln"""        <td>${message(code:'fc.test.results.summary')}</td>"""
                }
                if (!complete) {
                    outln"""        <td/>"""
                } else if (penalty_sum) {
                    outln"""        <td class="points">${penalty_sum} ${message(code:'fc.points')}</td>"""
                } else {
                    outln"""        <td class="zeropoints">${penalty_sum} ${message(code:'fc.points')}</td>"""
                }
                outln"""        </tr>"""
                outln"""    </tfoot>"""
                outln"""</table>"""
            }

            boolean show_inexact_pos = attrs.t.IsObservationTestInexactValue()
            
            // EnroutePhotoData
            if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurement() && attrs.t.IsObservationTestEnroutePhotoRun()) {
                if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                    outln"""<div>"""
                    for (def v in EnrouteValueUnit.values()) {
                        if (attrs.t.GetObservationTestEnroutePhotoValueUnit() == v) {
                            outln"""<label class="observationinputradio"><input type="radio" name="observationTestEnroutePhotoValueUnit" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                        } else {
                            outln"""<label class="observationinputradio"><input type="radio" name="observationTestEnroutePhotoValueUnit" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                        }
                    }
                    attrs.ti[0]++
                    outln"""<br/>"""
                    outln"""</div>"""
                }
                int penalty_sum = 0
                boolean complete = true
                for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(attrs.t,[sort:"id"])) {
                    if (enroutephotodata_instance.resultValue == EvaluationValue.Unevaluated) {
                        complete = false
						attrs.complete[0] = false
                    }
                }
                int col_span = 2
                if (complete) {
                    if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                        col_span = 5
                    } else {
                        col_span = 4
                    }
                }
                outln"""<table>"""
                outln"""    <thead>"""
                outln"""        <tr>"""
                outln"""            <th class="table-head" colspan="${col_span}">${message(code:'fc.observation.enroute.photo.short')}</th>"""
                outln"""        </tr>"""
                outln"""        <tr>"""
                outln"""            <th>${message(code:'fc.observation.enroute.photo.name.short')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                if (complete) {
                    if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""    <th>${message(code:'fc.test.results.plan')}</th>"""
                    }
                    outln"""        <th>${message(code:'fc.test.results.result')}</th>"""
                    outln"""        <th>${message(code:'fc.test.results.penalty')}</th>"""
                }
                outln"""        </tr>"""
                switch (attrs.t.GetEnroutePhotoMeasurement()) {
                    case EnrouteMeasurement.NMFromTP:
                    case EnrouteMeasurement.mmFromTP:
                        outln"""<tr>"""
                        outln"""    <th/>"""
                        outln"""    <th>${message(code:'fc.distance.fromlasttp')}</th>"""
                        if (complete) {
                            if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                                outln"""<th/>"""
                            }
                            outln"""<th colspan="2"/>"""
                        }
                        outln"""</tr>"""
                        break
                }
                outln"""    </thead>"""
                outln"""    <tbody>"""
                for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = attrs.t.task.disabledEnroutePhotoObs.contains("${enroutephotodata_instance.photoName},")
                    outln"""    <tr>"""
                    outln"""        <td>${enroutephotodata_instance.photoName}</td>"""
                    switch (attrs.t.GetEnroutePhotoMeasurement()) {
                        case EnrouteMeasurement.Map:
                            outln"""<td>"""
                            outln"""    <div>"""
                            for (def v in EvaluationValue.GetEvaluationValues(show_inexact_pos)) {
                                String s = ""
                                switch (v) {
                                    case EvaluationValue.Correct:
                                        s = attrs.t.GetObservationTestEnrouteCorrectValueStr(false)
                                        break
                                    case EvaluationValue.Inexact:
                                        s = attrs.t.GetObservationTestEnrouteInexactValueStr(false)
                                        break
                                }
                                if (enroutephotodata_instance.evaluationValue == v) {
                                    outln"""<label class="observationinputradio"><input type="radio" name="${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.enrouteEvaluationCode,args:[s])}</label>"""
                                } else {
                                    outln"""<label class="observationinputradio"><input type="radio" name="${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.enrouteEvaluationCode,args:[s])}</label>"""
                                }
                            }
                            attrs.ti[0]++
                            outln"""        <br/>"""
                            outln"""    </div>"""
                            outln"""</td>"""
                            break
                        case EnrouteMeasurement.NMFromTP:
                            outln"""<td>"""
							String disabled_attribute = ""
							if (show_buttons && !complete) {
								write_buttons(enroutephotodata_instance, true)
								if (enroutephotodata_instance.IsEvaluationFromTPNotFound() || enroutephotodata_instance.IsEvaluationFromTPFalse()) {
									disabled_attribute = "disabled"
								}
							} else {
								outln"""    <select name="${Defs.EnrouteID_PhotoCoordTitle}${enroutephotodata_instance.id}" tabIndex="${attrs.ti[0]++}">"""
								write_select_options(enroutephotodata_instance)
								outln"""    </select>"""
							}
                            outln"""    <input type="text" class="measurement" id="${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}" name="${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}" value="${fieldValue(bean:enroutephotodata_instance,field:'evaluationDistance')}" size="3" ${disabled_attribute} tabIndex="${attrs.ti[0]++}"/>"""
                            outln"""    <label>${message(code:'fc.mile')}</label>"""
							outln"""	<script>"""
							outln"""		\$(document).on('keypress', '#${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}', function(e) {"""
							outln"""			if (e.charCode == 13) {"""
							outln"""				var next_enroutephotodata_id = get_next_measurement_input_id('${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}');"""
							outln"""				\$('#'+next_enroutephotodata_id).select();"""
							outln"""				e.preventDefault();"""
							outln"""			}"""
							outln"""		});"""
							outln"""	</script>"""
                            outln"""</td>"""
                            if (complete) {
                                outln"""<td>${enroutephotodata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsNM(enroutephotodata_instance.distanceNM)} ${message(code:'fc.mile')}</td>"""
                            }
                            break
                        case EnrouteMeasurement.mmFromTP:
                            outln"""<td>"""
							String disabled_attribute = ""
							if (show_buttons && !complete) {
								write_buttons(enroutephotodata_instance, true)
								if (enroutephotodata_instance.IsEvaluationFromTPNotFound() || enroutephotodata_instance.IsEvaluationFromTPFalse()) {
									disabled_attribute = "disabled"
								}
							} else {
								outln"""    <select name="${Defs.EnrouteID_PhotoCoordTitle}${enroutephotodata_instance.id}" tabIndex="${attrs.ti[0]++}">"""
								write_select_options(enroutephotodata_instance)
								outln"""    </select>"""
							}
                            outln"""    <input type="text" class="measurement" id="${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}" name="${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}" value="${fieldValue(bean:enroutephotodata_instance,field:'evaluationDistance')}" size="3" ${disabled_attribute} tabIndex="${attrs.ti[0]++}"/>"""
                            outln"""    <label>${message(code:'fc.mm')}</label>"""
							outln"""	<script>"""
							outln"""		\$(document).on('keypress', '#${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}', function(e) {"""
							outln"""			if (e.charCode == 13) {"""
							outln"""				var next_enroutephotodata_id = get_next_measurement_input_id('${Defs.EnrouteID_PhotoEvaluationValue}${enroutephotodata_instance.id}');"""
							outln"""				\$('#'+next_enroutephotodata_id).select();"""
							outln"""				e.preventDefault();"""
							outln"""			}"""
							outln"""		});"""
							outln"""	</script>"""
                            outln"""</td>"""
                            if (complete) {
                                outln"""<td>${enroutephotodata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsmm(enroutephotodata_instance.distancemm)} ${message(code:'fc.mm')}</td>"""
                            }
                            break
                    }
                    if (complete) {
                        outln"""    <td class="${GetResultValueClass(enroutephotodata_instance.resultValue)}">${message(code:enroutephotodata_instance.resultValue.enrouteResultCode)}</td>"""
                        if (is_disabled) {
                            outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if (enroutephotodata_instance.penaltyCoord) {
                            outln"""<td class="points">${enroutephotodata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        } else {
                            outln"""<td class="zeropoints">${enroutephotodata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        }
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += enroutephotodata_instance.penaltyCoord
                    }
                }
                outln"""    </tbody>"""
                outln"""    <tfoot>"""
                outln"""        <tr>"""
                if (complete) {
                    if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""    <td colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                        //outln"""    <td colspan="3">${message(code:'fc.observation.enroute.input.notfound')}</td>"""
                    } else {
                        outln"""    <td colspan="3">${message(code:'fc.test.results.summary')}</td>"""
                    }
                } else {
                    outln"""        <td>${message(code:'fc.test.results.summary')}</td>"""
                    if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""    <td colspan="3"/>"""
                        //outln"""    <td colspan="3">${message(code:'fc.observation.enroute.input.notfound')}</td>"""
                    } else {
                        outln"""    <td/>"""
                    }
                }
                if (complete) {
                    if (penalty_sum) {
                        outln"""    <td class="points">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""    <td class="zeropoints">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    }
                }
                outln"""        </tr>"""
                outln"""    </tfoot>"""
                outln"""</table>"""
            }

            // EnrouteCanvasData
            if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurement() && attrs.t.IsObservationTestEnrouteCanvasRun()) {
                if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                    outln"""<div>"""
                    for (def v in EnrouteValueUnit.values()) {
                        if (attrs.t.GetObservationTestEnrouteCanvasValueUnit() == v) {
                            outln"""<label class="observationinputradio"><input type="radio" name="observationTestEnrouteCanvasValueUnit" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                        } else {
                            outln"""<label class="observationinputradio"><input type="radio" name="observationTestEnrouteCanvasValueUnit" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                        }
                    }
                    attrs.ti[0]++
                    outln"""<br/>"""
                    outln"""</div>"""
                }
                int penalty_sum = 0
                boolean complete = true
                for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(attrs.t,[sort:"id"])) {
                    if (enroutecanvasdata_instance.resultValue == EvaluationValue.Unevaluated) {
                        complete = false
						attrs.complete[0] = false
                    }
                }
                int col_span = 2
                if (complete) {
                    if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                        col_span = 5
                    } else {
                        col_span = 4
                    }
                }
                outln"""<table>"""
                outln"""    <thead>"""
                outln"""        <tr>"""
                outln"""            <th class="table-head" colspan="${col_span}">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""        </tr>"""
                outln"""        <tr>"""
                outln"""            <th>${message(code:'fc.observation.enroute.canvas.sign')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                if (complete) {
                    if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""    <th>${message(code:'fc.test.results.plan')}</th>"""
                    }
                    outln"""        <th>${message(code:'fc.test.results.result')}</th>"""
                    outln"""        <th>${message(code:'fc.test.results.penalty')}</th>"""
                }
                outln"""        </tr>"""
                switch (attrs.t.GetEnrouteCanvasMeasurement()) {
                    case EnrouteMeasurement.NMFromTP:
                    case EnrouteMeasurement.mmFromTP:
                        outln"""<tr>"""
                        outln"""    <th/>"""
                        outln"""    <th>${message(code:'fc.distance.fromlasttp')}</th>"""
                        if (complete) {
                            if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                                outln"""<th/>"""
                            }
                            outln"""<th colspan="2"/>"""
                        }
                        outln"""</tr>"""
                        break
                }
                outln"""    </thead>"""
                outln"""    <tbody>"""
                for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = attrs.t.task.disabledEnrouteCanvasObs.contains("${enroutecanvasdata_instance.canvasSign.canvasName},")
                    String image_name = ""
					if (enroutecanvasdata_instance.canvasSign.imageName) {
						image_name = createLinkTo(dir:'',file:enroutecanvasdata_instance.canvasSign.imageName)
					}
                    outln"""    <tr>"""
					if (image_name) {
						outln"""    <td><img class="observationinputcanvasimage" src="${image_name}"/> ${enroutecanvasdata_instance.canvasSign.canvasName}</td>"""
					} else {
						outln"""    <td><img class="observationinputcanvasimage" /> ${enroutecanvasdata_instance.canvasSign.canvasName}</td>"""
					}
                    switch (attrs.t.GetEnrouteCanvasMeasurement()) {
                        case EnrouteMeasurement.Map:
                            outln"""<td>"""
                            outln"""    <div>"""
                            for (def v in EvaluationValue.GetEvaluationValues(show_inexact_pos)) {
                                String s = ""
                                switch (v) {
                                    case EvaluationValue.Correct:
                                        s = attrs.t.GetObservationTestEnrouteCorrectValueStr(false)
                                        break
                                    case EvaluationValue.Inexact:
                                        s = attrs.t.GetObservationTestEnrouteInexactValueStr(false)
                                        break
                                }
                                if (enroutecanvasdata_instance.evaluationValue == v) {
                                    outln"""<label class="observationinputradio"><input type="radio" name="${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.enrouteEvaluationCode,args:[s])}</label>"""
                                } else {
                                    outln"""<label class="observationinputradio"><input type="radio" name="${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.enrouteEvaluationCode,args:[s])}</label>"""
                                }
                            }
                            attrs.ti[0]++
                            outln"""        <br/>"""
                            outln"""    </div>"""
                            outln"""</td>"""
                            break
                        case EnrouteMeasurement.NMFromTP:
                            outln"""<td>"""
							String disabled_attribute = ""
							if (show_buttons && !complete) {
								write_buttons(enroutecanvasdata_instance, false)
								if (enroutecanvasdata_instance.IsEvaluationFromTPNotFound() || enroutecanvasdata_instance.IsEvaluationFromTPFalse()) {
									disabled_attribute = "disabled"
								}
							} else {
								outln"""    <select name="${Defs.EnrouteID_CanvasCoordTitle}${enroutecanvasdata_instance.id}" tabIndex="${attrs.ti[0]++}">"""
								write_select_options(enroutecanvasdata_instance)
								outln"""    </select>"""
							}
                            outln"""    <input type="text" class="measurement" id="${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}" name="${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}" value="${fieldValue(bean:enroutecanvasdata_instance,field:'evaluationDistance')}" size="3" ${disabled_attribute} tabIndex="${attrs.ti[0]++}"/>"""
                            outln"""    <label>${message(code:'fc.mile')}</label>"""
							outln"""	<script>"""
							outln"""		\$(document).on('keypress', '#${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}', function(e) {"""
							outln"""			if (e.charCode == 13) {"""
							outln"""				var next_enroutecanvasdata_id = get_next_measurement_input_id('${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}');"""
							outln"""				\$('#'+next_enroutecanvasdata_id).select();"""
							outln"""				e.preventDefault();"""
							outln"""			}"""
							outln"""		});"""
							outln"""	</script>"""
                            outln"""</td>"""
                            if (complete) {
                                outln"""<td>${enroutecanvasdata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsNM(enroutecanvasdata_instance.distanceNM)} ${message(code:'fc.mile')}</td>"""
                            }
                            break
                        case EnrouteMeasurement.mmFromTP:
                            outln"""<td>"""
							String disabled_attribute = ""
							if (show_buttons && !complete) {
								write_buttons(enroutecanvasdata_instance, false)
								if (enroutecanvasdata_instance.IsEvaluationFromTPNotFound() || enroutecanvasdata_instance.IsEvaluationFromTPFalse()) {
									disabled_attribute = "disabled"
								}
							} else {
								outln"""    <select name="${Defs.EnrouteID_CanvasCoordTitle}${enroutecanvasdata_instance.id}" tabIndex="${attrs.ti[0]++}">"""
								write_select_options(enroutecanvasdata_instance)
								outln"""    </select>"""
							}
                            outln"""    <input type="text" class="measurement" id="${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}" name="${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}" value="${fieldValue(bean:enroutecanvasdata_instance,field:'evaluationDistance')}" size="3" ${disabled_attribute} tabIndex="${attrs.ti[0]++}"/>"""
                            outln"""    <label>${message(code:'fc.mm')}</label>"""
							outln"""	<script>"""
							outln"""		\$(document).on('keypress', '#${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}', function(e) {"""
							outln"""			if (e.charCode == 13) {"""
							outln"""				var next_enroutecanvasdata_id = get_next_measurement_input_id('${Defs.EnrouteID_CanvasEvaluationValue}${enroutecanvasdata_instance.id}');"""
							outln"""				\$('#'+next_enroutecanvasdata_id).select();"""
							outln"""				e.preventDefault();"""
							outln"""			}"""
							outln"""		});"""
							outln"""	</script>"""
                            outln"""</td>"""
                            if (complete) {
                                outln"""<td>${enroutecanvasdata_instance.tpName()} ${attrs.t.GetEnrouteDistanceResultsmm(enroutecanvasdata_instance.distancemm)} ${message(code:'fc.mm')}</td>"""
                            }
                            break
                    }
                    if (complete) {
                        outln"""    <td class="${GetResultValueClass(enroutecanvasdata_instance.resultValue)}">${message(code:enroutecanvasdata_instance.resultValue.enrouteResultCode)}</td>"""
                        if (is_disabled) {
                            outln"""<td class="zeropoints">${message(code:'fc.disabled')}</td>"""
                        } else if (enroutecanvasdata_instance.penaltyCoord) {
                            outln"""<td class="points">${enroutecanvasdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        } else {
                            outln"""<td class="zeropoints">${enroutecanvasdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                        }
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += enroutecanvasdata_instance.penaltyCoord
                    }
                }
                outln"""    </tbody>"""
                outln"""    <tfoot>"""
                outln"""        <tr>"""
                if (complete) {
                    if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""    <td colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                        //outln"""    <td colspan="3">${message(code:'fc.observation.enroute.input.notfound')}</td>"""
                    } else {
                        outln"""    <td colspan="3">${message(code:'fc.test.results.summary')}</td>"""
                    }
                } else {
                    outln"""        <td>${message(code:'fc.test.results.summary')}</td>"""
                    if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                        outln"""    <td colspan="3"/>"""
                        //outln"""    <td colspan="3">${message(code:'fc.observation.enroute.input.notfound')}</td>"""
                    } else {
                        outln"""    <td/>"""
                    }
                }
                if (complete) {
                    if (penalty_sum) {
                        outln"""    <td class="points">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""    <td class="zeropoints">${penalty_sum} ${message(code:'fc.points')}</td>"""
                    }
                }
                outln"""        </tr>"""
                outln"""    </tfoot>"""
                outln"""</table>"""
            }

            // Other penalties
            outln"""<fieldset>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.observationresults.otherpenalties')}* [${message(code:'fc.points')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="observationTestOtherPenalties" name="observationTestOtherPenalties" value="${fieldValue(bean:attrs.t,field:'observationTestOtherPenalties')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
			
			outln"""<script>"""
			outln"""	function get_next_measurement_input_id(startid) {"""
			outln"""		var tr_element = \$('#'+startid).parent().parent().next();"""
			outln"""		while (true) {"""
			outln"""			if (tr_element.val() === undefined) {"""
			outln"""				return 0;"""
			outln"""			}"""
			outln"""			var next_id = tr_element.find('input.measurement:enabled').attr('id');"""
			outln"""			if (next_id) {"""
			outln"""				return next_id;"""
			outln"""			}"""
			outln"""			tr_element = tr_element.next();"""
			outln"""		}"""
			outln"""	}"""
			outln"""</script>"""

            
        } else {
            outln"""<fieldset>"""
            if (attrs.t.IsObservationTestTurnpointRun()) {
                outln"""<p>"""
                outln"""    <label>${message(code:'fc.observationresults.turnpointphotopenalties')}* [${message(code:'fc.points')}]:</label>"""
                outln"""    <br/>"""
                outln"""    <input type="text" id="observationTestTurnPointPhotoPenalties" name="observationTestTurnPointPhotoPenalties" value="${fieldValue(bean:attrs.t,field:'observationTestTurnPointPhotoPenalties')}" tabIndex="${attrs.ti[0]++}"/>"""
                outln"""</p>"""
				outln"""<script>"""
				outln"""	\$('#observationTestTurnPointPhotoPenalties').select();"""
				outln"""</script>"""
            }
            if (attrs.t.IsObservationTestEnroutePhotoRun()) {
                outln"""<p>"""
                outln"""    <label>${message(code:'fc.observationresults.routephotopenalties')}* [${message(code:'fc.points')}]:</label>"""
                outln"""    <br/>"""
                outln"""    <input type="text" id="observationTestRoutePhotoPenalties" name="observationTestRoutePhotoPenalties" value="${fieldValue(bean:attrs.t,field:'observationTestRoutePhotoPenalties')}" tabIndex="${attrs.ti[0]++}"/>"""
                outln"""</p>"""
				if (!attrs.t.IsObservationTestTurnpointRun()) {
					outln"""<script>"""
					outln"""	\$('#observationTestRoutePhotoPenalties').select();"""
					outln"""</script>"""
				}
            }
            if (attrs.t.IsObservationTestEnrouteCanvasRun()) {
                outln"""<p>"""
                outln"""    <label>${message(code:'fc.observationresults.groundtargetpenalties')}* [${message(code:'fc.points')}]:</label>"""
                outln"""    <br/>"""
                outln"""    <input type="text" id="observationTestGroundTargetPenalties" name="observationTestGroundTargetPenalties" value="${fieldValue(bean:attrs.t,field:'observationTestGroundTargetPenalties')}" tabIndex="${attrs.ti[0]++}"/>"""
                outln"""</p>"""
				if (!(attrs.t.IsObservationTestTurnpointRun() || attrs.t.IsObservationTestEnroutePhotoRun())) {
					outln"""<script>"""
					outln"""	\$('#observationTestGroundTargetPenalties').select();"""
					outln"""</script>"""
				}
            }
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.observationresults.otherpenalties')}* [${message(code:'fc.points')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="observationTestOtherPenalties" name="observationTestOtherPenalties" value="${fieldValue(bean:attrs.t,field:'observationTestOtherPenalties')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
    } // def observationTestInput
    
    // --------------------------------------------------------------------------------------------------------------------
    private void write_select_options(EnrouteData enrouteDataInstance)
    {
        for (CoordTitle coord_title in enrouteDataInstance.route.GetEnrouteCoordTitles(true)) {
            if ((coord_title.type == CoordType.UNKNOWN) && (coord_title.number == 0)) {
                if (enrouteDataInstance.IsEvaluationFromTPUnevaluated()) {
                    outln"""<option value="${Defs.EnrouteValue_Unevaluated}" selected="selected"></option>"""
                } else {
                    outln"""<option value="${Defs.EnrouteValue_Unevaluated}"></option>"""
                }
            } else if ((coord_title.type == CoordType.UNKNOWN) && (coord_title.number == 1)) {
                if (enrouteDataInstance.IsEvaluationFromTPNotFound()) {
                    outln"""<option value="${Defs.EnrouteValue_NotFound}" selected="selected">${message(code:'fc.observation.evaluationvalue.enroute.notfound')}</option>"""
                } else {
                    outln"""<option value="${Defs.EnrouteValue_NotFound}">${message(code:'fc.observation.evaluationvalue.enroute.notfound')}</option>"""
                }
            } else if ((coord_title.type == CoordType.UNKNOWN) && (coord_title.number == 2)) {
                if (enrouteDataInstance.IsEvaluationFromTPFalse()) {
                    outln"""<option value="${Defs.EnrouteValue_False}" selected="selected">${message(code:'fc.observation.evaluationvalue.enroute.false')}</option>"""
                } else {
                    outln"""<option value="${Defs.EnrouteValue_False}">${message(code:'fc.observation.evaluationvalue.enroute.false')}</option>"""
                }
            } else {
                if ((coord_title.type == enrouteDataInstance.evaluationType) && (coord_title.number == enrouteDataInstance.evaluationNumber)) {
                    outln"""<option value="${coord_title}" selected="selected">${coord_title.titleCode()}</option>"""
                } else {
                    outln"""<option value="${coord_title}">${coord_title.titleCode()}</option>"""
                }
            }
        }
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    private void write_buttons(EnrouteData enrouteDataInstance, boolean enroutePhoto)
    {
        String coordtitle_id = ""
		String input_id = ""
        if (enroutePhoto) {
            coordtitle_id = Defs.EnrouteID_PhotoCoordTitle
			input_id = "${Defs.EnrouteID_PhotoEvaluationValue}${enrouteDataInstance.id}"
        } else {
            coordtitle_id = Defs.EnrouteID_CanvasCoordTitle
			input_id = "${Defs.EnrouteID_CanvasEvaluationValue}${enrouteDataInstance.id}"
        }
        for (CoordTitle coordtitle_instance in enrouteDataInstance.route.GetEnrouteCoordTitles(true)) {
            if ((coordtitle_instance.type == CoordType.UNKNOWN) && (coordtitle_instance.number == 0)) {
                if (enrouteDataInstance.IsEvaluationFromTPUnevaluated()) {
					outln"""<input type="hidden" id="${coordtitle_id}${enrouteDataInstance.id}" name="${coordtitle_id}${enrouteDataInstance.id}" value="${Defs.EnrouteValue_Unevaluated}"/>"""
                }
            } else if ((coordtitle_instance.type == CoordType.UNKNOWN) && (coordtitle_instance.number == 1)) {
                if (enrouteDataInstance.IsEvaluationFromTPNotFound()) {
                    outln"""<input type="button" class="observationinputbutton" id="button_${coordtitle_id}${enrouteDataInstance.id}_${get_coordtitle_id(coordtitle_instance)}" value="${message(code:'fc.observation.evaluationvalue.enroute.notfound')}" onclick="set_enroute_value('${coordtitle_id}','${enrouteDataInstance.id}','${get_coordtitle_id(coordtitle_instance)}','${input_id}','${Defs.EnrouteValue_NotFound}');"/>"""
					outln"""<input type="hidden" id="${coordtitle_id}${enrouteDataInstance.id}" name="${coordtitle_id}${enrouteDataInstance.id}" value="${Defs.EnrouteValue_NotFound}"/>"""
                } else {
                    outln"""<input type="button" id="button_${coordtitle_id}${enrouteDataInstance.id}_${get_coordtitle_id(coordtitle_instance)}" value="${message(code:'fc.observation.evaluationvalue.enroute.notfound')}" onclick="set_enroute_value('${coordtitle_id}','${enrouteDataInstance.id}','${get_coordtitle_id(coordtitle_instance)}','${input_id}','${Defs.EnrouteValue_NotFound}');"/>"""
                }
            } else if ((coordtitle_instance.type == CoordType.UNKNOWN) && (coordtitle_instance.number == 2)) {
                if (enrouteDataInstance.IsEvaluationFromTPFalse()) {
                    outln"""<input type="button" class="observationinputbutton" id="button_${coordtitle_id}${enrouteDataInstance.id}_${get_coordtitle_id(coordtitle_instance)}" value="${message(code:'fc.observation.evaluationvalue.enroute.false')}" onclick="set_enroute_value('${coordtitle_id}','${enrouteDataInstance.id}','${get_coordtitle_id(coordtitle_instance)}','${input_id}','${Defs.EnrouteValue_False}');"/>"""
					outln"""<input type="hidden" id="${coordtitle_id}${enrouteDataInstance.id}" name="${coordtitle_id}${enrouteDataInstance.id}" value="${Defs.EnrouteValue_False}"/>"""
                } else {
                    outln"""<input type="button" id="button_${coordtitle_id}${enrouteDataInstance.id}_${get_coordtitle_id(coordtitle_instance)}" value="${message(code:'fc.observation.evaluationvalue.enroute.false')}" onclick="set_enroute_value('${coordtitle_id}','${enrouteDataInstance.id}','${get_coordtitle_id(coordtitle_instance)}','${input_id}','${Defs.EnrouteValue_False}');"/>"""
                }
            } else {
                if ((coordtitle_instance.type == enrouteDataInstance.evaluationType) && (coordtitle_instance.number == enrouteDataInstance.evaluationNumber)) {
                    outln"""<input type="button" class="observationinputbutton" id="button_${coordtitle_id}${enrouteDataInstance.id}_${get_coordtitle_id(coordtitle_instance)}" value="${coordtitle_instance.titleCode()}" onclick="set_enroute_value('${coordtitle_id}','${enrouteDataInstance.id}','${get_coordtitle_id(coordtitle_instance)}','${input_id}','${coordtitle_instance}');"/>"""
					outln"""<input type="hidden" id="${coordtitle_id}${enrouteDataInstance.id}" name="${coordtitle_id}${enrouteDataInstance.id}" value="${coordtitle_instance}"/>"""
                } else {
                    outln"""<input type="button" id="button_${coordtitle_id}${enrouteDataInstance.id}_${get_coordtitle_id(coordtitle_instance)}" value="${coordtitle_instance.titleCode()}" onclick="set_enroute_value('${coordtitle_id}','${enrouteDataInstance.id}','${get_coordtitle_id(coordtitle_instance)}','${input_id}','${coordtitle_instance}');"/>"""
                }
            }
        }
        outln"""<input type="button" value="-->" onclick="goto_input('${input_id}');"/>"""
		outln"""<script>"""
		outln"""	function set_enroute_value(coordtitle_id, data_id, button_id, input_id, value) {"""
		outln"""		\$("#"+coordtitle_id+data_id).val(value);"""
        for (CoordTitle coordtitle_instance in enrouteDataInstance.route.GetEnrouteCoordTitles(true)) {
			outln"""	\$("#button_"+coordtitle_id+data_id+"_${get_coordtitle_id(coordtitle_instance)}").attr("class", "");"""
		}
		outln"""		\$("#button_"+coordtitle_id+data_id+"_"+button_id).attr("class", "observationinputbutton");"""
		outln"""		if (value == '${Defs.EnrouteValue_NotFound}' || value == '${Defs.EnrouteValue_False}') {"""
		outln"""			\$("#"+input_id).val(0);"""
		outln"""			\$("#"+input_id).attr("disabled", true);"""
		outln"""		} else {"""
		outln"""			\$("#"+input_id).attr("disabled", false);"""
		outln"""		}"""
		outln"""	}"""
		outln"""	function goto_input(input_id) {"""
		outln"""		\$("#"+input_id).select();"""
		outln"""	}"""
		outln"""</script>"""
    }
    
	// --------------------------------------------------------------------------------------------------------------------
    String get_coordtitle_id(CoordTitle coordtitleInstance)
    {
        if ((coordtitleInstance.type == CoordType.UNKNOWN) && (coordtitleInstance.number == 0)) {
            return "${coordtitleInstance.id}_0"
        } else if ((coordtitleInstance.type == CoordType.UNKNOWN) && (coordtitleInstance.number == 1)) {
            return "${coordtitleInstance.id}_1"
        } else if ((coordtitleInstance.type == CoordType.UNKNOWN) && (coordtitleInstance.number == 2)) {
            return "${coordtitleInstance.id}_2"
        } else {
            return coordtitleInstance.id
        }
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	def observationTestPrintable = { attrs, body ->
        String turnpoint_headcode = "fc.observationresults.turnpointphotopenalties"
        if (attrs.t.IsObservationSignUsed()) {
            Route route_instance = attrs.t.flighttestwind.flighttest.route
            // TurnpointData
            if (attrs.t.GetTurnpointRoute().IsTurnpointSign() && attrs.t.IsObservationTestTurnpointRun()) {
                int penalty_sum = 0
                switch (attrs.t.GetTurnpointRoute()) {
                    case TurnpointRoute.AssignPhoto:
                    case TurnpointRoute.TrueFalsePhoto:
                        turnpoint_headcode = 'fc.observation.turnpoint.photos'
                        break
                    case TurnpointRoute.AssignCanvas:
                        turnpoint_headcode = 'fc.observation.turnpoint.canvas'
                        break
                }
                outln"""<div style="page-break-inside:avoid">"""
                outln"""<table class="observationturnpointresultlist">"""
                outln"""<thead>"""
                outln"""    <tr class="name1">"""
                outln"""        <th class="table-head" colspan="5">${message(code:turnpoint_headcode)}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="name2">"""
                outln"""        <th>${message(code:'fc.tpname')}</th>"""
                outln"""        <th>${message(code:'fc.test.results.given')}</th>"""
                outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
                outln"""        <th>${message(code:'fc.test.results.result')}</th>"""
                outln"""        <th>${message(code:'fc.test.results.penalty')}</th>"""
                outln"""    </tr>"""
                outln"""</thead>"""
                outln"""<tbody>"""
                for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = DisabledCheckPointsTools.Uncompress(attrs.t.task.disabledCheckPointsTurnpointObs,route_instance).contains("${turnpointdata_instance.tpTitle()},")
                    outln"""<tr class="value" id="${turnpointdata_instance.tpTitle()}">"""
                    outln"""        <td class="tpname">${turnpointdata_instance.tpPrintName()}</td>"""
                    switch (attrs.t.GetTurnpointRoute()) {
                        case TurnpointRoute.AssignPhoto:
                        case TurnpointRoute.AssignCanvas:
                            if (!turnpointdata_instance.evaluationSign.title) {
                                outln"""<td class="evaluation">${message(code:'fc.observation.evaluationvalue.turnpoint.noinput')}</td>"""
                            } else {
                                outln"""<td class="evaluation">${turnpointdata_instance.evaluationSign.title}</td>"""
                            }
                            outln"""<td class="plan">${turnpointdata_instance.tpSign.title}</td>"""
                            break
                        case TurnpointRoute.TrueFalsePhoto:
                            outln"""<td class="evaluation">${message(code:turnpointdata_instance.evaluationValue.turnpointEvaluationCode)}</td>"""
                            outln"""<td class="plan">${message(code:turnpointdata_instance.tpSignCorrect.code)}</td>"""
                            break
                    }
                    outln"""        <td class="result">${message(code:turnpointdata_instance.resultValue.turnpointResultCode)}</td>"""
                    if (is_disabled) {
                        outln"""    <td class="penalties">-</td>"""
                    } else if (turnpointdata_instance.penaltyCoord) {
                        outln"""    <td class="penalties">${turnpointdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""    <td class="penalties">${turnpointdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""</tr>"""
                    if (!is_disabled) {
                        penalty_sum += turnpointdata_instance.penaltyCoord
                    }
                }
                outln"""</tbody>"""
                outln"""<tfoot>"""
                outln"""    <tr class="summary">"""
                outln"""        <td class="tpname" colspan="4">${message(code:'fc.test.results.summary')}</td>"""
                if (penalty_sum) {
                    outln"""    <td class="penalties">${penalty_sum} ${message(code:'fc.points')}</td>"""
                } else {
                    outln"""    <td class="penalties">${penalty_sum} ${message(code:'fc.points')}</td>"""
                }
                outln"""    </tr>"""
                outln"""</tfoot>"""
                outln"""</table>"""
                outln"""</div>"""
                outln"""<br/>"""
            }
            
            // EnroutePhotoData
            if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurement() && attrs.t.IsObservationTestEnroutePhotoRun()) {
                int penalty_sum = 0
                int col_span = 4
                if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                    col_span = 5
                }
                outln"""<div style="page-break-inside:avoid">"""
                outln"""<table class="observationsroutephotoresultlist">"""
                outln"""    <thead>"""
                outln"""            <tr class="name1">"""
                outln"""                <th class="table-head" colspan="${col_span}">${message(code:'fc.observation.enroute.photo.short')}</th>"""
                outln"""            </tr>"""
                outln"""        <tr class="name2">"""
                outln"""            <th>${message(code:'fc.observation.enroute.photo.name.short')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                if (attrs.t.GetEnroutePhotoMeasurement().IsEnrouteMeasurementFromTP()) {
                    outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
                }
                outln"""            <th>${message(code:'fc.test.results.result')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.penalty')}</th>"""
                outln"""        </tr>"""
                outln"""    </thead>"""
                outln"""    <tbody>"""
                for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = attrs.t.task.disabledEnroutePhotoObs.contains("${enroutephotodata_instance.photoName},")
                    outln"""    <tr class="value" id="${enroutephotodata_instance.photoName}">"""
                    outln"""        <td class="photoname">${enroutephotodata_instance.photoName}</td>"""
                    switch (attrs.t.GetEnroutePhotoMeasurement()) {
                        case EnrouteMeasurement.Map:
                            String s = ""
                            switch (enroutephotodata_instance.evaluationValue) {
                                case EvaluationValue.Correct:
                                    s = attrs.t.GetObservationTestEnrouteCorrectValueStr(true)
                                    break
                                case EvaluationValue.Inexact:
                                    s = attrs.t.GetObservationTestEnrouteInexactValueStr(true)
                                    break
                            }
                            outln"""<td class="evaluation">${message(code:enroutephotodata_instance.evaluationValue.enrouteEvaluationCode,args:[s])}</td>"""
                            break
                        case EnrouteMeasurement.NMFromTP:
                            if (enroutephotodata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td class="evaluation">${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutephotodata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td class="evaluation">-</td>"""
                            } else {
                                outln"""<td class="evaluation">${enroutephotodata_instance.evaluationPrintName()} ${FcMath.DistanceStr(enroutephotodata_instance.evaluationDistance)} ${message(code:'fc.mile')}</td>"""
                            }
                            outln"""<td class="plan">${enroutephotodata_instance.tpPrintName()} ${attrs.t.GetEnrouteDistanceResultsNM(enroutephotodata_instance.distanceNM)} ${message(code:'fc.mile')}</td>"""
                            break
                        case EnrouteMeasurement.mmFromTP:
                            if (enroutephotodata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td class="evaluation">${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutephotodata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td class="evaluation">-</td>"""
                            } else {
                                outln"""<td class="evaluation">${enroutephotodata_instance.evaluationPrintName()} ${FcMath.DistanceMeasureStr(enroutephotodata_instance.evaluationDistance)} ${message(code:'fc.mm')}</td>"""
                            }
                            outln"""<td class="plan">${enroutephotodata_instance.tpPrintName()} ${attrs.t.GetEnrouteDistanceResultsmm(enroutephotodata_instance.distancemm)} ${message(code:'fc.mm')}</td>"""
                            break
                    }
                    outln"""         <td class="result">${message(code:enroutephotodata_instance.resultValue.enrouteResultCode)}</td>"""
                    if (is_disabled) {
                        outln"""     <td class="penalties">-</td>"""
                    } else if (enroutephotodata_instance.penaltyCoord) {
                        outln"""     <td class="penalties">${enroutephotodata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""     <td class="penalties">${enroutephotodata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += enroutephotodata_instance.penaltyCoord
                    }
                }
                outln"""    </tbody>"""
                outln"""    <tfoot>"""
                outln"""        <tr class="summary">"""
                outln"""            <td class="photoname" colspan="${col_span-1}">${message(code:'fc.test.results.summary')}</td>"""
                if (penalty_sum) {
                    outln"""        <td class="penalties">${penalty_sum} ${message(code:'fc.points')}</td>"""
                } else {
                    outln"""        <td class="penalties">${penalty_sum} ${message(code:'fc.points')}</td>"""
                }
                outln"""        </tr>"""
                outln"""    </tfoot>"""
                outln"""</table>"""
                outln"""</div>"""
                outln"""<br/>"""
            }

            // EnrouteCanvasData
            if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurement() && attrs.t.IsObservationTestEnrouteCanvasRun()) {
                int penalty_sum = 0
                int col_span = 4
                if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                    col_span = 5
                }
                outln"""<div style="page-break-inside:avoid">"""
                outln"""<table class="observationsroutecanvasresultlist">"""
                outln"""    <thead>"""
                outln"""            <tr class="name1">"""
                outln"""                <th class="table-head" colspan="${col_span}">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""            </tr>"""
                outln"""        <tr class="name2">"""
                outln"""            <th>${message(code:'fc.observation.enroute.canvas.sign')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.given')}</th>"""
                if (attrs.t.GetEnrouteCanvasMeasurement().IsEnrouteMeasurementFromTP()) {
                    outln"""        <th>${message(code:'fc.test.results.plan')}</th>"""
                }
                outln"""            <th>${message(code:'fc.test.results.result')}</th>"""
                outln"""            <th>${message(code:'fc.test.results.penalty')}</th>"""
                outln"""        </tr>"""
                outln"""    </thead>"""
                outln"""    <tbody>"""
                for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(attrs.t,[sort:"id"])) {
                    boolean is_disabled = attrs.t.task.disabledEnrouteCanvasObs.contains("${enroutecanvasdata_instance.canvasSign.canvasName},")
					String image_name = ""
					if (enroutecanvasdata_instance.canvasSign.imageName) {
						image_name = createLinkTo(dir:'',file:enroutecanvasdata_instance.canvasSign.imageName)
					}
                    outln"""    <tr class="value" id="${enroutecanvasdata_instance.canvasSign}">"""
					if (image_name) {
						outln"""    <td class="imagename"><img src="${image_name}" /> ${enroutecanvasdata_instance.canvasSign.canvasName}</td>"""
					} else {
						outln"""    <td class="imagename"><img /> ${enroutecanvasdata_instance.canvasSign.canvasName}</td>"""
					}
                    switch (attrs.t.GetEnrouteCanvasMeasurement()) {
                        case EnrouteMeasurement.Map:
                            String s = ""
                            switch (enroutecanvasdata_instance.evaluationValue) {
                                case EvaluationValue.Correct:
                                    s = attrs.t.GetObservationTestEnrouteCorrectValueStr(true)
                                    break
                                case EvaluationValue.Inexact:
                                    s = attrs.t.GetObservationTestEnrouteInexactValueStr(true)
                                    break
                            }
                            outln"""<td class="evaluation">${message(code:enroutecanvasdata_instance.evaluationValue.enrouteEvaluationCode,args:[s])}</td>"""
                            break
                        case EnrouteMeasurement.NMFromTP:
							if (enroutecanvasdata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td class="evaluation">${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutecanvasdata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td class="evaluation">-</td>"""
                            } else {
                                outln"""<td class="evaluation">${enroutecanvasdata_instance.evaluationPrintName()} ${FcMath.DistanceStr(enroutecanvasdata_instance.evaluationDistance)} ${message(code:'fc.mile')}</td>"""
                            }
                            if (enroutecanvasdata_instance.canvasSign != EnrouteCanvasSign.NoSign) {
								outln"""<td class="plan">${enroutecanvasdata_instance.tpPrintName()} ${attrs.t.GetEnrouteDistanceResultsNM(enroutecanvasdata_instance.distanceNM)} ${message(code:'fc.mile')}</td>"""
							} else {
								outln"""<td/>"""
							}
                            break
                        case EnrouteMeasurement.mmFromTP:
                            if (enroutecanvasdata_instance.IsEvaluationFromTPUnevaluated()) {
                                outln"""<td class="evaluation">${message(code:'fc.observation.evaluationvalue.enroute.noinput')}</td>"""
                            } else if (enroutecanvasdata_instance.IsEvaluationFromTPNotFound()) {
                                outln"""<td class="evaluation">-</td>"""
                            } else {
                                outln"""<td class="evaluation">${enroutecanvasdata_instance.evaluationPrintName()} ${FcMath.DistanceMeasureStr(enroutecanvasdata_instance.evaluationDistance)} ${message(code:'fc.mm')}</td>"""
                            }
                            if (enroutecanvasdata_instance.canvasSign != EnrouteCanvasSign.NoSign) {
								outln"""<td class="plan">${enroutecanvasdata_instance.tpPrintName()} ${attrs.t.GetEnrouteDistanceResultsmm(enroutecanvasdata_instance.distancemm)} ${message(code:'fc.mm')}</td>"""
							} else {
								outln"""<td/>"""
							}
                            break
                    }
                    outln"""         <td class="result">${message(code:enroutecanvasdata_instance.resultValue.enrouteResultCode)}</td>"""
                    if (is_disabled) {
                        outln"""     <td class="penalties">-</td>"""
                    } else if (enroutecanvasdata_instance.penaltyCoord) {
                        outln"""     <td class="penalties">${enroutecanvasdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    } else {
                        outln"""     <td class="penalties">${enroutecanvasdata_instance.penaltyCoord} ${message(code:'fc.points')}</td>"""
                    }
                    outln"""    </tr>"""
                    if (!is_disabled) {
                        penalty_sum += enroutecanvasdata_instance.penaltyCoord
                    }
                }
                outln"""    </tbody>"""
                outln"""    <tfoot>"""
                outln"""        <tr class="summary">"""
                outln"""        <td  class="imagename" colspan="${col_span-1}">${message(code:'fc.test.results.summary')}</td>"""
                if (penalty_sum) {
                    outln"""        <td class="penalties">${penalty_sum} ${message(code:'fc.points')}</td>"""
                } else {
                    outln"""        <td class="penalties">${penalty_sum} ${message(code:'fc.points')}</td>"""
                }
                outln"""        </tr>"""
                outln"""    </tfoot>"""
                outln"""</table>"""
                outln"""</div>"""
                outln"""<br/>"""
            }
        }
        
        outln"""<div style="page-break-inside:avoid">"""
        outln"""<table class="summary">"""
        outln"""    <tbody>"""
        if (attrs.t.IsObservationTestTurnpointRun()) {
            outln"""    <tr class="turnpointphotopenalties">"""
            outln"""        <td>${message(code:turnpoint_headcode)}: ${attrs.t.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.IsObservationTestEnroutePhotoRun()) {
            outln"""    <tr class="routephotopenalties">"""
            outln"""        <td>${message(code:'fc.observationresults.routephotopenalties')}: ${attrs.t.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.IsObservationTestEnrouteCanvasRun()) {
            outln"""    <tr class="groundtargetpenalties">"""
            outln"""        <td>${message(code:'fc.observationresults.groundtargetpenalties')}: ${attrs.t.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        if (attrs.t.observationTestOtherPenalties != 0) {
            outln"""    <tr class="otherpenalties">"""
            outln"""        <td>${message(code:'fc.observationresults.otherpenalties')}: ${attrs.t.observationTestOtherPenalties} ${message(code:'fc.points')}</td>"""
            outln"""    </tr>"""
        }
        outln"""        <tr>"""
        outln"""        	<td> </td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr class="penalties">"""
        outln"""            <td>${message(code:'fc.penalties')}: ${attrs.t.observationTestPenalties} ${message(code:'fc.points')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
        outln"""</div>"""
	} // def observationTestPrintable
	
    // --------------------------------------------------------------------------------------------------------------------
    def observationTestScannedPrintable = { attrs, body ->
        if (attrs.t.scannedObservationTest) {
            outln"""<br/>"""
            outln"""<img class="scannedobservationtest" src="${createLink(controller:'test',action:'observationformimage',params:[testid:attrs.t.id])}" />"""
        }
    } // def observationTestScannedPrintable
    
    
    // --------------------------------------------------------------------------------------------------------------------
    private String GetResultValueClass(EvaluationValue evaluationValue)
    {
        String result_value_class = ""
        switch (evaluationValue) {
            case EvaluationValue.False:
            case EvaluationValue.NotFound:
                result_value_class = "errors"
                break
        }
        return result_value_class
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
