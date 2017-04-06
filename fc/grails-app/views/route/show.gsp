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
                        <g:set var="ti" value="${[]+1}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeInstance.name()}</td>
                                    <td class="errors">${routeInstance.GetRouteStatusInfo()}</td>
                                </tr>
                                <g:if test="${routeInstance.showAflosMark || routeInstance.mark}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route.aflosimport.name')}:</td>
                                        <td colspan="2">${routeInstance.mark}</td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.observation.turnpoint')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.turnpointRoute.code)}<br/>${message(code:'fc.observation.measurement')}: <g:if test="${routeInstance.turnpointMapMeasurement}">${message(code:'fc.observation.turnpoint.map')}</g:if><g:else>${message(code:'fc.observation.turnpoint.log')}</g:else></td>
                                    <td class="errors">${routeInstance.GetTurnpointSignStatusInfo()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.observation.enroute.photo')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.enroutePhotoRoute.code)}<br/>${message(code:'fc.observation.measurement')}: ${message(code:routeInstance.enroutePhotoMeasurement.code)}</td>
                                    <td class="errors">${routeInstance.GetEnrouteSignStatusInfo(true)}<br/>${routeInstance.GetEnrouteMeasurementStatusInfo(true)}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.observation.enroute.canvas')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.enrouteCanvasRoute.code)}<br/>${message(code:'fc.observation.measurement')}: ${message(code:routeInstance.enrouteCanvasMeasurement.code)}</td>
                                    <td class="errors">${routeInstance.GetEnrouteSignStatusInfo(false)}<br/>${routeInstance.GetEnrouteMeasurementStatusInfo(false)}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${PlanningTestTask.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <g:showRouteDetails route="${routeInstance}" />
                        <input type="hidden" name="id" value="${routeInstance?.id}"/>
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:if test="${!routeInstance.Used()}">
                            <g:actionSubmit action="createcoordroutes" value="${message(code:'fc.coordroute.add1')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="createsecretcoordroutes" value="${message(code:'fc.coordroute.addsecret')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${routeInstance.IsRouteEmpty()}">
                            <g:actionSubmit action="importcoord" value="${message(code:'fc.coordroute.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${routeInstance.CanTurnpointSignModify()}">
                            <g:actionSubmit action="importturnpointsign" value="${message(code:'fc.coordroute.turnpointsign.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${routeInstance.CanEnrouteSignModify(true)}">
                            <g:actionSubmit action="createenroutephoto" value="${message(code:'fc.coordroute.photo.add')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="importenroutephoto" value="${message(code:'fc.coordroute.photo.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="removeallenroutephoto" value="${message(code:'fc.coordroute.photo.removeall')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${routeInstance.CanEnrouteSignModify(false)}">
                            <g:actionSubmit action="createenroutecanvas" value="${message(code:'fc.coordroute.canvas.add')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="importenroutecanvas" value="${message(code:'fc.coordroute.canvas.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="removeallenroutecanvas" value="${message(code:'fc.coordroute.canvas.removeall')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${!routeInstance.Used()}">
                            <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="printcoordall" value="${message(code:'fc.printcoord.all')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="printcoordtp" value="${message(code:'fc.printcoord.tp')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:if test="${!routeInstance.Used()}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="showofflinemap" value="${message(code:'fc.offlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="showmap" value="${message(code:'fc.onlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="gpxexport" value="${message(code:'fc.gpxexport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:if test="${routeInstance.IsAflosReferenceExportPossible()}">
                            <g:actionSubmit action="aflosrefexport" value="${message(code:'fc.aflosrefexport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${routeInstance.IsEMailPossible()}">
                            <g:actionSubmit action="sendmail" value="${message(code:'fc.route.sendmail')}" onclick="this.form.target='_self';return true;" title="${routeInstance.EMailAddress()}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="copyroute" value="${message(code:'fc.copy')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>