<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" importaction="." />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.show')}</h2>
                <div class="block" id="forms" >
                    <g:form params="${['routeReturnAction':routeReturnAction,'routeReturnController':routeReturnController,'routeReturnID':routeReturnID]}">
                        <input type="hidden" name="id" value="${routeInstance?.id}"/>
                        <g:set var="ti" value="${[]+1}"/>
                        <g:set var="route_data" value="${routeInstance.GetRouteData()}" />
                        <g:set var="route_used" value="${routeInstance.Used()}"/>
                        <g:set var="route_empty" value="${routeInstance.IsRouteEmpty()}"/>
                        <g:set var="route_canturnpointsignmodify" value="${routeInstance.CanTurnpointSignModify()}"/>
                        <g:set var="route_useproecdureturn" value="${routeInstance.UseProcedureTurn()}" />
                        <table>
                            <tbody>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeInstance.name()}</td>
                                    <td class="errors">${info = routeInstance.GetRouteStatusInfo()}</td>
                                    <td style="width:1%;"><g:if test="${info}"><a href="../../docs/help.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td style="width:1%;"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></td>
                                </tr>
                                <g:if test="${routeInstance.showAflosMark || routeInstance.mark}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route.aflosimport.name')}:</td>
                                        <td colspan="2">${routeInstance.mark}</td>
                                        <td colspan="2"/>
                                    </tr>
                                </g:if>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <td class="detailtitle">${message(code:'fc.observation.turnpoint')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.turnpointRoute.code)}<br/>${message(code:'fc.observation.measurement')}: <g:if test="${routeInstance.turnpointMapMeasurement}">${message(code:'fc.observation.turnpoint.map')}</g:if><g:else>${message(code:'fc.observation.turnpoint.log')}</g:else></td>
                                    <td class="errors">${info = routeInstance.GetTurnpointSignStatusInfo()}</td>
                                    <td style="width:1%;"><g:if test="${info}"><a href="../../docs/help.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td/>
                                </tr>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <td class="detailtitle">${message(code:'fc.observation.enroute.photo')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.enroutePhotoRoute.code)}<br/>${message(code:'fc.observation.measurement')}: ${message(code:routeInstance.enroutePhotoMeasurement.code)}</td>
                                    <td class="errors">${info = routeInstance.GetEnrouteSignStatusInfo(true)}<br/>${info += routeInstance.GetEnrouteMeasurementStatusInfo(true)}</td>
                                    <td style="width:1%;"><g:if test="${info}"><a href="../../docs/help.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td/>
                                </tr>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <td class="detailtitle">${message(code:'fc.observation.enroute.canvas')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.enrouteCanvasRoute.code)}<br/>${message(code:'fc.observation.measurement')}: ${message(code:routeInstance.enrouteCanvasMeasurement.code)}</td>
                                    <td class="errors">${info = routeInstance.GetEnrouteSignStatusInfo(false)}<br/>${info += routeInstance.GetEnrouteMeasurementStatusInfo(false)}</td>
                                    <td style="width:1%;"><g:if test="${info}"><a href="../../docs/help.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.procedureturns')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        <g:if test="${route_data.procedureturn_num}"><g:if test="${route_useproecdureturn}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.disabled')}</g:else> (${route_data.procedureturn_num})</g:if><g:else>${message(code:'fc.no')}</g:else>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.curvedlegs')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        <g:if test="${route_data.curved_num}">${message(code:'fc.yes')} (${route_data.curved_num})</g:if><g:else>${message(code:'fc.no')}</g:else>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.secretpoints')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        <g:if test="${route_data.secret_num}">${message(code:'fc.yes')} (${route_data.secret_num})</g:if><g:else>${message(code:'fc.no')}</g:else>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.to2ldg')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        ${FcMath.DistanceStr(route_data.distance_to2ldg)}${message(code:'fc.mile')} 
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.sp2fp')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        ${FcMath.DistanceStr(route_data.distance_sp2fp)}${message(code:'fc.mile')}
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${PlanningTestTask.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="flighttest_instance" in="${FlightTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:flighttest var="${flighttest_instance}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <g:if test="${flighttest_instance.IsObservationSignUsed()}">
                                                (${message(code:'fc.observation')}: ${message(code:'fc.yes')})
                                            </g:if>
                                            <g:else>
                                                (${message(code:'fc.observation')}: ${message(code:'fc.no')})
                                            </g:else>
                                            <br/>
                                        </g:each>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                            </tbody>
                        </table>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
                                        <g:if test="${params.next}">
                                            <g:actionSubmit action="gotonext" value="${message(code:'fc.route.gotonext')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:else>
                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:else>
                                        <g:actionSubmit action="edit" value="${message(code:'fc.route.settings')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        <g:if test="${!route_used}">
                                            <g:if test="${route_data.procedureturn_num && route_useproecdureturn}">
                                                <g:actionSubmit action="disableprocedureturn" value="${message(code:'fc.route.disableprocedureturn')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:actionSubmit action="copyroute" value="${message(code:'fc.copy')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        <g:if test="${params.next}">
                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                        <g:if test="${!route_used || route_empty || route_canturnpointsignmodify}">
	                        <table>
	                            <tfoot>
	                                <tr>
	                                    <td>
	                                        <g:if test="${!route_used}">
	                                            <g:actionSubmit action="createcoordroutes" value="${message(code:'fc.coordroute.add1')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                            <g:actionSubmit action="createsecretcoordroutes" value="${message(code:'fc.coordroute.addsecret')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:if test="${route_empty}">
	                                            <g:actionSubmit action="importcoord" value="${message(code:'fc.coordroute.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:if test="${route_canturnpointsignmodify}">
	                                            <g:actionSubmit action="importturnpointsign" value="${message(code:'fc.coordroute.turnpointsign.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:if test="${!route_used}">
	                                            <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                    </td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
                        <g:if test="${routeInstance.CanEnrouteSignModify(true)}">
                            <table>
                                <tfoot>
                                    <tr>
                                        <td>
                                            <g:actionSubmit action="importenroutephoto" value="${message(code:'fc.coordroute.photo.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="createenroutephoto" value="${message(code:'fc.coordroute.photo.add')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="removeallenroutephoto" value="${message(code:'fc.coordroute.photo.removeall')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </g:if>
                        <g:if test="${routeInstance.CanEnrouteSignModify(false)}">
                            <table>
                                <tfoot>
                                    <tr>
                                        <td>
                                            <g:actionSubmit action="importenroutecanvas" value="${message(code:'fc.coordroute.canvas.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="createenroutecanvas" value="${message(code:'fc.coordroute.canvas.add')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="removeallenroutecanvas" value="${message(code:'fc.coordroute.canvas.removeall')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </g:if>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
                                        <g:if test="${!route_empty}">
                                            <g:actionSubmit action="showofflinemap" value="${message(code:'fc.offlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="showmap" value="${message(code:'fc.onlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:if test="${BootStrap.global.GetPrintServerAPI()}">
	                                            <g:actionSubmit action="mapexportquestion2" value="${message(code:'fc.contestmap')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
                                            <g:actionSubmit action="kmzexport" value="${message(code:'fc.kmz.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="gpxexport" value="${message(code:'fc.gpx.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:if test="${routeInstance.IsAflosReferenceExportPossible()}">
	                                            <g:actionSubmit action="aflosrefexport" value="${message(code:'fc.aflosrefexport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:if test="${routeInstance.IsEMailPossible()}">
	                                            <g:actionSubmit action="sendmail" value="${message(code:'fc.route.sendmail')}" onclick="this.form.target='_self';return true;" title="${routeInstance.EMailAddress()}" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:actionSubmit action="printcoordall" value="${message(code:'fc.printcoord.all')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:actionSubmit action="printcoordtp" value="${message(code:'fc.printcoord.tp')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                    </g:if>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                        <g:showRouteDetails route="${routeInstance}" />
                        <table>
                            <tfoot>
                                <tr>
                                    <td/>
                                    <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                                </tr>
                            </tfoot>
                        </table>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>