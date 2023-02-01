class ObservationFormsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def observationPrintable = { attrs, body ->
        
        String enroutephoto_style = "float: left;"
        boolean show_inexact_pos = attrs.t.IsObservationTestInexactValue()
        int col_num = 4
        if (show_inexact_pos) {
            col_num++
        }

        // TurnpointData
        if (attrs.t.GetTurnpointRoute().IsTurnpointSign() && attrs.t.IsObservationTestTurnpointRun()) {
            enroutephoto_style = "float: right;"
            outln"""<table class="observationturnpointlist">"""
            outln"""    <thead>"""
            if (attrs.t.GetTurnpointRoute() == TurnpointRoute.AssignPhoto) {
                outln"""    <tr class="title">"""
                outln"""        <th colspan="2">${message(code:'fc.observation.turnpoint.photo')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="tpname">${message(code:'fc.tpname')}</th>"""
                outln"""        <th class="turnpointphoto">${message(code:'fc.observation.turnpoint.photo.sign')}</th>"""
                outln"""    </tr>"""
            } else if (attrs.t.GetTurnpointRoute() == TurnpointRoute.AssignCanvas) {
                outln"""    <tr class="title">"""
                outln"""        <th colspan="2">${message(code:'fc.observation.turnpoint.canvas')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="tpname">${message(code:'fc.tpname')}</th>"""
                outln"""        <th class="turnpointcanvas">${message(code:'fc.observation.turnpoint.canvas.sign')}</th>"""
                outln"""    </tr>"""
            } else if (attrs.t.GetTurnpointRoute() == TurnpointRoute.TrueFalsePhoto) {
                outln"""    <tr class="title">"""
                outln"""        <th colspan="3">${message(code:'fc.observation.turnpoint.photo')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="tpname">${message(code:'fc.tpname')}</th>"""
                outln"""        <th class="turnpointtrue">${message(code:'fc.observation.turnpoint.true')}</th>"""
                outln"""        <th class="turnpointfalse">${message(code:'fc.observation.turnpoint.false')}</th>"""
                outln"""    </tr>"""
            }
            outln"""    </thead>"""
            outln"""    <tbody>"""
            for (TurnpointData turnpointdata_instance in TurnpointData.findAllByTest(attrs.t,[sort:"id"])) {
                if (attrs.t.GetTurnpointRoute() == TurnpointRoute.AssignPhoto) {
                    outln"""<tr class="data">"""
                    outln"""    <td class="tpname">${turnpointdata_instance.tpPrintName()}</td>"""
                    if (attrs.printResults) {
                        outln"""<td class="turnpointphoto">${turnpointdata_instance.tpSign}</td>"""
                    } else {
                        outln"""<td class="turnpointphoto" />"""
                    }
                    outln"""</tr>"""
                } else if (attrs.t.GetTurnpointRoute() == TurnpointRoute.AssignCanvas) {
                    outln"""<tr class="data">"""
                    outln"""    <td class="tpname">${turnpointdata_instance.tpPrintName()}</td>"""
                    if (attrs.printResults) {
                        outln"""<td class="turnpointcanvas">${turnpointdata_instance.tpSign}</td>"""
                    } else {
                        outln"""<td class="turnpointcanvas" />"""
                    }
                    outln"""</tr>"""
                } else if (attrs.t.GetTurnpointRoute() == TurnpointRoute.TrueFalsePhoto) {
                    outln"""<tr class="data">"""
                    outln"""    <td class="tpname">${turnpointdata_instance.tpPrintName()}</td>"""
                    if (attrs.printResults) {
                        if (turnpointdata_instance.tpSignCorrect == TurnpointCorrect.True) {
                            outln"""<td class="turnpointtrue">X</td>"""
                        } else {
                            outln"""<td class="turnpointtrue" />"""
                        }
                        if (turnpointdata_instance.tpSignCorrect == TurnpointCorrect.False) {
                            outln"""<td class="turnpointfalse">X</td>"""
                        } else {
                            outln"""<td class="turnpointfalse" />"""
                        }
                    } else {
                        outln"""<td class="turnpointtrue" />"""
                        outln"""<td class="turnpointfalse" />"""
                    }
                    outln"""</tr>"""
                }
            }
            outln"""    </tbody>"""
            outln"""</table>"""
        }

        // EnroutePhotoData
        EnrouteMeasurement enroutephoto_measurement = attrs.t.GetEnroutePhotoMeasurement(true)
        if (enroutephoto_measurement.IsEnrouteMeasurement() && attrs.t.IsObservationTestEnroutePhotoRun()) {
            if (attrs.printResults && (enroutephoto_measurement == EnrouteMeasurement.Map)) {
                enroutephoto_measurement = attrs.t.GetEnroutePhotoResultMeasurement()
            }
            outln"""<table class="observationsroutephotolist" style="${enroutephoto_style}">"""
            outln"""    <thead>"""
            if (enroutephoto_measurement == EnrouteMeasurement.Map) {
                outln"""    <tr class="title">"""
                outln"""        <th colspan="${col_num}">${message(code:'fc.observation.enroute.photo.short')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="name">${message(code:'fc.observation.enroute.photo.name.short')}</th>"""
                outln"""        <th class="correct">${message(code:'fc.observation.evaluationvalue.enroute.correct',args:[attrs.t.GetObservationTestEnrouteCorrectValueStr(true)])}</th>"""
                if (show_inexact_pos) {
                    outln"""    <th class="inexact">${message(code:'fc.observation.evaluationvalue.enroute.inexact',args:[attrs.t.GetObservationTestEnrouteInexactValueStr(true)])}</th>"""
                }
                outln"""        <th class="false">${message(code:'fc.observation.evaluationvalue.enroute.false')}</th>"""
                outln"""    </tr>"""
            } else if (enroutephoto_measurement == EnrouteMeasurement.NMFromTP) {
                outln"""    <tr class="title">"""
                outln"""        <th colspan="3">${message(code:'fc.observation.enroute.photo.short')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="name">${message(code:'fc.observation.enroute.photo.name.short')}</th>"""
                outln"""        <th class="fromlasttp">${message(code:'fc.distance.fromlasttp.short')}</th>"""
                outln"""        <th class="nmfromtp">${message(code:'fc.observation.enroute.input.nmfromtp')}</th>"""
                outln"""    </tr>"""
            } else if (enroutephoto_measurement == EnrouteMeasurement.mmFromTP) {
                outln"""    <tr class="title">"""
                outln"""        <th colspan="3">${message(code:'fc.observation.enroute.photo.short')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="name">${message(code:'fc.observation.enroute.photo.name.short')}</th>"""
                outln"""        <th class="fromlasttp">${message(code:'fc.distance.fromlasttp.short')}</th>"""
                outln"""        <th class="mmfromtp">${message(code:'fc.observation.enroute.input.mmfromtp')}</th>"""
                outln"""    </tr>"""
            }
            outln"""    </thead>"""
            outln"""    <tbody>"""
            for (EnroutePhotoData enroutephotodata_instance in EnroutePhotoData.findAllByTest(attrs.t,[sort:"id"])) {
                if (enroutephoto_measurement == EnrouteMeasurement.Map) {
                    outln"""<tr class="data">"""
                    outln"""    <td class="name">${enroutephotodata_instance.photoName}</td>"""
                    outln"""    <td class="correct" />"""
                    if (show_inexact_pos) {
                        outln"""<td class="inexact" />"""
                    }
                    outln"""    <td class="false" />"""
                    outln"""</tr>"""
                } else if (enroutephoto_measurement == EnrouteMeasurement.NMFromTP) {
                    outln"""<tr class="data">"""
                    outln"""    <td class="name">${enroutephotodata_instance.photoName}</td>"""
                    if (attrs.printResults) {
                        outln"""<td class="fromlasttp">${enroutephotodata_instance.tpPrintName()}</td>"""
                        outln"""<td class="nmfromtp">${attrs.t.GetEnrouteDistanceResultsNM(enroutephotodata_instance.distanceNM)}</td>"""
                    } else {
                        outln"""<td class="fromlasttp"/>"""
                        outln"""<td class="nmfromtp"/>"""
                    }
                    outln"""</tr>"""
                } else if (enroutephoto_measurement == EnrouteMeasurement.mmFromTP) {
                    outln"""<tr class="data">"""
                    outln"""    <td class="name">${enroutephotodata_instance.photoName}</td>"""
                    if (attrs.printResults) {
                        outln"""<td class="fromlasttp">${enroutephotodata_instance.tpPrintName()}</td>"""
                        outln"""<td class="nmfromtp">${attrs.t.GetEnrouteDistanceResultsmm(enroutephotodata_instance.distancemm)}</td>"""
                    } else {
                        outln"""<td class="fromlasttp"/>"""
                        outln"""<td class="nmfromtp"/>"""
                    }
                    outln"""</tr>"""
                }
            }
            outln"""    </tbody>"""
            outln"""</table>"""
        }
        
        // Judge sign
        if (!attrs.printResults) {
            outln"""<table class="observationsroutecrewjudgesign">"""
            if (attrs.t.IsObservationJudgeSign()) {
                outln"""    <thead>"""
                outln"""        <tr class="hide">"""
                outln"""            <th colspan="2">.</th>"""
                outln"""        </tr>"""
                outln"""        <tr class="hide">"""
                outln"""            <th colspan="2">.</th>"""
                outln"""        </tr>"""
                outln"""    </thead>"""
                outln"""    <tbody>"""
                outln"""        <tr class="title">"""
                outln"""            <td class="name">${message(code:'fc.observation.sign.crew')}:</td>"""
                outln"""            <td class="line"/>"""
                outln"""        </tr>"""
                outln"""        <tr class="hide2">"""
                outln"""            <td colspan="2">.</td>"""
                outln"""        </tr>"""
                outln"""        <tr class="title">"""
                outln"""            <td class="name">${message(code:'fc.observation.sign.judge')}:</td>"""
                outln"""            <td class="line"/>"""
                outln"""        </tr>"""
                outln"""    </tbody>"""
            } else {
                outln"""    <thead>"""
                outln"""        <tr class="hide">"""
                outln"""            <th colspan="2">.</th>"""
                outln"""        </tr>"""
                outln"""    </thead>"""
                outln"""    <tbody>"""
                outln"""        <tr class="title">"""
                outln"""            <td class="name">${message(code:'fc.observation.sign.giventime')}:</td>"""
                outln"""            <td class="line"/>"""
                outln"""        </tr>"""
                outln"""        <tr class="hide2">"""
                outln"""            <td colspan="2">.</td>"""
                outln"""        </tr>"""
                outln"""        <tr class="title">"""
                outln"""            <td class="name">${message(code:'fc.observation.sign.crew')}:</td>"""
                outln"""            <td class="line"/>"""
                outln"""        </tr>"""
                outln"""        <tr class="hide2">"""
                outln"""            <td colspan="2">.</td>"""
                outln"""        </tr>"""
                outln"""        <tr class="title">"""
                outln"""            <td class="name">${message(code:'fc.observation.sign.judge')}:</td>"""
                outln"""            <td class="line"/>"""
                outln"""        </tr>"""
                outln"""    </tbody>"""
            }
            outln"""</table>"""
        }
        
        // EnrouteCanvasData
        EnrouteMeasurement enroutecanvas_measurement = attrs.t.GetEnrouteCanvasMeasurement(true)
        if (enroutecanvas_measurement.IsEnrouteMeasurement() && attrs.t.IsObservationTestEnrouteCanvasRun()) {
            if (attrs.printResults && (enroutecanvas_measurement == EnrouteMeasurement.Map)) {
                enroutecanvas_measurement = attrs.t.GetEnrouteCanvasResultMeasurement()
            }
            outln"""<table class="observationsroutecanvaslist">"""
            outln"""    <thead>"""
            if (enroutecanvas_measurement == EnrouteMeasurement.Map) {
                outln"""    <tr class="hide">"""
                outln"""        <th colspan="${col_num}">.</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="title">"""
                outln"""        <th colspan="${col_num}">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="sign">${message(code:'fc.observation.enroute.canvas.sign.short')}</th>"""
                outln"""        <th class="correct">${message(code:'fc.observation.evaluationvalue.enroute.correct',args:[attrs.t.GetObservationTestEnrouteCorrectValueStr(true)])}</th>"""
                if (show_inexact_pos) {
                    outln"""    <th class="inexact">${message(code:'fc.observation.evaluationvalue.enroute.inexact',args:[attrs.t.GetObservationTestEnrouteInexactValueStr(true)])}</th>"""
                }
                outln"""        <th class="false">${message(code:'fc.observation.evaluationvalue.enroute.false')}</th>"""
                outln"""    </tr>"""
            } else if (enroutecanvas_measurement == EnrouteMeasurement.NMFromTP) {
                outln"""    <tr class="hide">"""
                outln"""        <th colspan="3">.</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="title">"""
                outln"""        <th colspan="3">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="sign">${message(code:'fc.observation.enroute.canvas.sign.short')}</th>"""
                outln"""        <th class="fromlasttp">${message(code:'fc.distance.fromlasttp.short')}</th>"""
                outln"""        <th class="nmfromtp">${message(code:'fc.observation.enroute.input.nmfromtp')}</th>"""
                outln"""    </tr>"""
            } else if (enroutecanvas_measurement == EnrouteMeasurement.mmFromTP) {
                outln"""    <tr class="hide">"""
                outln"""        <th colspan="3">.</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="title">"""
                outln"""        <th colspan="3">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""    </tr>"""
                outln"""    <tr class="subtitle">"""
                outln"""        <th class="sign">${message(code:'fc.observation.enroute.canvas.sign.short')}</th>"""
                outln"""        <th class="fromlasttp">${message(code:'fc.distance.fromlasttp.short')}</th>"""
                outln"""        <th class="mmfromtp">${message(code:'fc.observation.enroute.input.mmfromtp')}</th>"""
                outln"""    </tr>"""
            }
            outln"""    </thead>"""
            outln"""    <tbody>"""
            for (EnrouteCanvasData enroutecanvasdata_instance in EnrouteCanvasData.findAllByTest(attrs.t,[sort:"id"])) {
                if (enroutecanvas_measurement == EnrouteMeasurement.Map) {
                    String image_name = createLinkTo(dir:'',file:enroutecanvasdata_instance.canvasSign.imageName)
                    outln"""<tr class="data">"""
                    outln"""    <td class="sign"><img src="${image_name}"/></td>"""
                    outln"""    <td class="correct" />"""
                    if (show_inexact_pos) {
                        outln"""<td class="inexact" />"""
                    }
                    outln"""    <td class="false" />"""
                    outln"""</tr>"""
                } else if (enroutecanvas_measurement == EnrouteMeasurement.NMFromTP) {
                    String image_name = createLinkTo(dir:'',file:enroutecanvasdata_instance.canvasSign.imageName)
                    outln"""<tr class="data">"""
                    if (attrs.printResults) {
                        outln"""<td class="sign"><img src="${image_name}"/></td>"""
						if (enroutecanvasdata_instance.canvasSign != EnrouteCanvasSign.NoSign) {
							outln"""<td class="fromlasttp">${enroutecanvasdata_instance.tpPrintName()}</td>"""
							outln"""<td class="nmfromtp">${attrs.t.GetEnrouteDistanceResultsNM(enroutecanvasdata_instance.distanceNM)}</td>"""
						} else {
							outln"""<td class="fromlasttp"/>"""
							outln"""<td class="nmfromtp"/>"""
						}
                    } else {
                        outln"""<td class="sign">.</td>"""
                        outln"""<td class="fromlasttp"/>"""
                        outln"""<td class="nmfromtp"/>"""
                    }
                    outln"""</tr>"""
                } else if (enroutecanvas_measurement == EnrouteMeasurement.mmFromTP) {
                    String image_name = createLinkTo(dir:'',file:enroutecanvasdata_instance.canvasSign.imageName)
                    outln"""<tr class="data">"""
                    if (attrs.printResults) {
                        outln"""<td class="sign"><img src="${image_name}"/></td>"""
						if (enroutecanvasdata_instance.canvasSign != EnrouteCanvasSign.NoSign) {
							outln"""<td class="fromlasttp">${enroutecanvasdata_instance.tpPrintName()}</td>"""
							outln"""<td class="mmfromtp">${attrs.t.GetEnrouteDistanceResultsmm(enroutecanvasdata_instance.distancemm)}</td>"""
						} else {
							outln"""<td class="fromlasttp"/>"""
							outln"""<td class="mmfromtp"/>"""
						}
                    } else {
                        outln"""<td class="sign">.</td>"""
                        outln"""<td class="fromlasttp"/>"""
                        outln"""<td class="mmfromtp"/>"""
                    }
                    outln"""</tr>"""
                }
            }
            outln"""    </tbody>"""
            outln"""</table>"""
        }
    }
	
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
