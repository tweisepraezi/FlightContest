<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${routeInstance.printName()} - ${message(code:'fc.scale')} 1:${routeInstance.contest.mapScale}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${routeInstance.printName()}</title>
    </head>
    <body>
        <h2>${routeInstance.printName()}</h2>
        <g:form>
            <table class="routecoords">
                <thead>
                    <tr class="title">
                        <th>${message(code:'fc.tpname')}</th>
                        <g:if test="${routeInstance.mark}">
                            <th>${message(code:'fc.aflos')}</th>
                        </g:if>
                        <th>${message(code:'fc.coordroute.list')}</th>
                        <th>${message(code:'fc.altitude')}</th>
                        <th>${message(code:'fc.gatewidth.short')}</th>
                        <g:if test="${routeInstance.turnpointRoute.IsTurnpointSign()}" >
                            <g:if test="${routeInstance.turnpointRoute == TurnpointRoute.AssignPhoto}">
                                <th>${message(code:'fc.observation.turnpoint.photo.short')}</th>
                            </g:if>
                            <g:elseif test="${routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas}">
                                <th>${message(code:'fc.observation.turnpoint.canvas.short')}</th>
                            </g:elseif>
                            <g:elseif test="${routeInstance.turnpointRoute == TurnpointRoute.TrueFalsePhoto}">
                                <th>${message(code:'fc.observation.turnpoint.photo.short')}</th>
                            </g:elseif>
                        </g:if>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                        <tr class="value">
                            <td class="tpname">${coordroute_instance.titlePrintCode()}</td>
                            <g:if test="${routeInstance.mark}">
                                <td class="aflosname">${coordroute_instance.mark}</td>
                            </g:if>
                            <td class="coords">${coordroute_instance.namePrintable(true,true)}</td>
                            <td class="altitude">${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
                            <td class="gatewidth">${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>
                            <g:if test="${routeInstance.turnpointRoute.IsTurnpointSign()}" >
                                <td class="sign">${coordroute_instance.GetPrintTurnpointSign()}</td>
                            </g:if>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <br/>
           	<div style="page-break-inside:avoid">
                <table class="routelegs">
                    <g:set var="total_distance" value="${new BigDecimal(0)}" />
                    <thead>
                        <tr class="name">
                            <th colspan="2">${message(code:'fc.routelegcoord.list')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each var="routelegcoord_instance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}">
                            <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routelegcoord_instance.testDistance())}" />
                            <g:set var="course_change" value="${AviationMath.courseChange(routelegcoord_instance.turnTrueTrack,routelegcoord_instance.testTrueTrack())}"/>
                            <g:if test="${course_change.abs() >= 90}">
                                <tr class="coursechange">
                                    <td class="center" align="center" colspan="2">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}<g:if test="${routelegcoord_instance.IsProcedureTurn()}"> (<g:if test="${routeInstance.UseProcedureTurn()}">${message(code:'fc.procedureturn')}</g:if><g:else>${message(code:'fc.procedureturn.disabled')}</g:else>)</g:if></td>
                                </tr>
                            </g:if>
                            <tr class="value">
                                <td class="from2tp">${routelegcoord_instance.GetPrintTitle()}</td>
                                <td class="trackdistance">${routelegcoord_instance.testPrintName()}</td>
                            </tr>
                        </g:each>
                    </tbody>
                    <tfoot>
                        <tr class="summary">
                            <td colspan="2">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            <br/>
           	<div style="page-break-inside:avoid">
                <table class="routelegs">
                    <g:set var="total_distance" value="${new BigDecimal(0)}" />
                    <thead>
                        <tr class="name">
                            <th colspan="2">${message(code:'fc.routelegtest.list')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each var="routelegtest_instance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}">
                            <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routelegtest_instance.testDistance())}" />
                            <g:set var="course_change" value="${AviationMath.courseChange(routelegtest_instance.turnTrueTrack,routelegtest_instance.testTrueTrack())}"/>
                            <g:if test="${course_change.abs() >= 90}">
                                <tr class="coursechange">
                                    <td class="center" align="center" colspan="2">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}<g:if test="${routelegtest_instance.IsProcedureTurn()}"> (<g:if test="${routeInstance.UseProcedureTurn()}">${message(code:'fc.procedureturn')}</g:if><g:else>${message(code:'fc.procedureturn.disabled')}</g:else>)</g:if></td>
                                </tr>
                            </g:if>
                            <tr class="value">
                                <td class="from2tp">${routelegtest_instance.GetPrintTitle()}</td>
                                <td class="trackdistance">${routelegtest_instance.testPrintName()}</td>
                            </tr>
                        </g:each>
                    </tbody>
                    <tfoot>
                        <tr class="summary">
                            <td colspan="2">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                        </tr>
                    </tfoot>
                </table>
     	    </div>
     	    <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()}">
                <br/>
	            <div style="page-break-inside:avoid">
                    <table class="enroutephotos">
		                <thead>
	                        <tr class="name">
	                            <th colspan="6">${message(code:'fc.coordroute.photos')}</th>
	                        </tr>
		                    <tr class="title">
		                        <th>${message(code:'fc.observation.enroute.photo.name.short')}</th>
                                <th>${message(code:'fc.coordroute.list')}</th>
                                <th colspan="2">${message(code:'fc.distance.from.tp.orthogonal')}</th>
		                    </tr>
		                </thead>
		                <tbody>
		                    <g:each var="coordenroutephoto_instance" in="${CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])}">
		                        <tr class="value">
		                            <td class="photoname">${coordenroutephoto_instance.enroutePhotoName}</td>
                                    <td class="coords">${coordenroutephoto_instance.namePrintable(false,true)}</td>
                                    <td class="distfromtp">${FcMath.DistanceStr(coordenroutephoto_instance.enrouteDistance)}${message(code:'fc.mile')}<br/>${FcMath.DistanceMeasureStr(coordenroutephoto_instance.GetMeasureDistance())}${message(code:'fc.mm')}<br/>${coordenroutephoto_instance.GetPrintEnrouteOrthogonalDistance()}</td>
                                    <td class="tpname">${coordenroutephoto_instance.titlePrintCode()}</td>
		                        </tr>
		                    </g:each>
		                </tbody>
                    </table>
	            </div>
	        </g:if>
            <g:if test="${routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()}">
                <br/>
                <div style="page-break-inside:avoid">
                    <table class="enroutecanvas">
                        <thead>
                            <tr class="name">
                                <th colspan="6">${message(code:'fc.coordroute.canvas')}</th>
                            </tr>
                            <tr class="title">
                                <th>${message(code:'fc.observation.enroute.canvas.sign.short')}</th>
                                <th>${message(code:'fc.coordroute.list')}</th>
                                <th colspan="2">${message(code:'fc.distance.from.tp.orthogonal')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each var="coordenroutecanvas_instance" in="${CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])}">
                                <tr class="value">
                                    <td class="canvassign"><img src="${createLinkTo(dir:'',file:coordenroutecanvas_instance.enrouteCanvasSign.imageName)}" /><br/>${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}</td>
                                    <td class="coords">${coordenroutecanvas_instance.namePrintable(false,true)}</td>
                                    <td class="distfromtp">${FcMath.DistanceStr(coordenroutecanvas_instance.enrouteDistance)}${message(code:'fc.mile')}<br/>${FcMath.DistanceMeasureStr(coordenroutecanvas_instance.GetMeasureDistance())}${message(code:'fc.mm')}<br/>${coordenroutecanvas_instance.GetPrintEnrouteOrthogonalDistance()}</td>
                                    <td class="tpname">${coordenroutecanvas_instance.titlePrintCode()}</td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </g:if>
            <br/>
            <div style="page-break-inside:avoid">
	            <table class="routecoordexport">
                    <thead>
                        <tr class="title">
                            <th>${message(code:'fc.coordroute.export.coords')}</th>
                        </tr>
                    </thead>
	                <tbody>
                        <tr class="value">
                            <td>
	                            <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
	                                ${coordroute_instance.GetExportRouteCoord()}<br/>
	                            </g:each>
	                        </td>
                        </tr>
	                </tbody>
	            </table>
	        </div>
	        <g:if test="${routeInstance.turnpointRoute.IsTurnpointSign()}">
	            <br/>
	            <div style="page-break-inside:avoid">
	                <table class="turnpointsignexport">
	                    <thead>
	                        <tr class="title">
	                            <th>${message(code:'fc.coordroute.export.turnpointsign')}</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <tr class="value">
	                            <td>
	                                <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
	                                    <g:if test="${coordroute_instance.IsTurnpointSign()}">
	                                        ${coordroute_instance.GetExportTurnpointSign()}<br/>
	                                    </g:if>
	                                </g:each>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	        </g:if>
            <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()}">
                <br/>
	            <div style="page-break-inside:avoid">
	                <table class="enroutephotoexport">
	                    <thead>
	                        <tr class="title">
	                            <th>${message(code:'fc.coordroute.export.photo')}</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <tr class="value">
	                            <td>
	                                <g:each var="coordenroutephoto_instance" in="${CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])}">
	                                    ${coordenroutephoto_instance.GetExportEnroute(true)} <br/>
	                                </g:each>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
            </g:if>
            <g:if test="${routeInstance.enrouteCanvasRoute.IsEnrouteRouteInput()}">
                <br/>
                <div style="page-break-inside:avoid">
                    <table class="enroutecanvasexport">
                        <thead>
                            <tr class="title">
                                <th>${message(code:'fc.coordroute.export.canvas')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="value">
                                <td>
                                    <g:each var="coordenroutecanvas_instance" in="${CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])}">
                                        ${coordenroutecanvas_instance.GetExportEnroute(false)} <br/>
                                    </g:each>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:if>
            <br/>
            <div style="page-break-inside:avoid">
                <table class="routecoordgeodata">
                    <thead>
                        <tr class="title">
                            <th>${message(code:'fc.coordroute.geodata.coords')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="value">
                            <td>
                                id|point|name|wkt<br/>
                                <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}" status="i">
                                    ${i+1}|${coordroute_instance.GetGeoDataRouteCoord()}<br/>
                                </g:each>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()}">
                <br/>
                <div style="page-break-inside:avoid">
                    <table class="enroutephotogeodata">
                        <thead>
                            <tr class="title">
                                <th>${message(code:'fc.coordroute.geodata.photo')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="value">
                                <td>
                                    id|point|name|wkt<br/>
                                    <g:each var="coordenroutephoto_instance" in="${CoordEnroutePhoto.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])}" status="i">
                                        ${i+1}|${coordenroutephoto_instance.GetGeoDataEnroute(true)} <br/>
                                    </g:each>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:if>
            <g:if test="${routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()}">
                <br/>
                <div style="page-break-inside:avoid">
                    <table class="enroutecanvasgeodata">
                        <thead>
                            <tr class="title">
                                <th>${message(code:'fc.coordroute.geodata.canvas')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="value">
                                <td>
                                    id|point|name|wkt<br/>
                                    <g:each var="coordenroutecanvas_instance" in="${CoordEnrouteCanvas.findAllByRoute(routeInstance,[sort:"enrouteViewPos"])}" status="i">
                                        ${i+1}|${coordenroutecanvas_instance.GetGeoDataEnroute(false)} <br/>
                                    </g:each>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </g:if>
        </g:form>
    </body>
</html>