<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" importaction="${message(code:'fc.route.import')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td colspan="2">${routeInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route.import.name')}:</td>
                                    <td colspan="2">${routeInstance.mark}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${PlanningTestTask.findAllByRoute(routeInstance)}">
                                            <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance)}">
                                            <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <tr>
                                    <th class="table-head" colspan="9">${message(code:'fc.coordroute.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.aflos.checkpoint')}</th>
                                    <th>${message(code:'fc.latitude')}</th>
                                    <th>${message(code:'fc.longitude')}</th>
                                    <th>${message(code:'fc.altitude')}</th>
                                    <th>${message(code:'fc.gatewidth')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="coordRouteInstance" in="${routeInstance.coords}" status="i" >
                                    <tr>
                                        <td><g:coordroutenum var="${coordRouteInstance}" num="${i+1}" link="${createLink(controller:'coordRoute',action:'edit')}"/></td>
                                        <td>${coordRouteInstance.title()}</td>
                                        <td>${coordRouteInstance.mark}</td>
                                        <td>${coordRouteInstance.latName()}</td>
                                        <td>${coordRouteInstance.lonName()}</td>
                                        <td>${coordRouteInstance.altitude}${message(code:'fc.foot')}</td>
                                        <td>${coordRouteInstance.gatewidth}${message(code:'fc.mile')}</td>
                                        <td>${coordRouteInstance.measureTrueTrackName()}</td>
                                        <td>${coordRouteInstance.measureDistanceName()}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <tr>
                                    <th class="table-head" colspan="6">${message(code:'fc.routelegcoord.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.truetrack.coord')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.coord')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="routeLegInstance" in="${routeInstance.routelegs}" status="i" >
                                    <tr>
                                        <td>${i+1}</td>
                                        <td>${routeLegInstance.title}</td>
                                        <td>${routeLegInstance.coordTrueTrackName()}</td>
                                        <td>${routeLegInstance.mapMeasureTrueTrackName()}</td>
                                        <td>${routeLegInstance.coordDistanceName()}</td>
                                        <td>${routeLegInstance.mapDistanceName()} (${routeLegInstance.mapMeasureDistanceName()})</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <tr>
                                    <th class="table-head" colspan="6">${message(code:'fc.routelegtest.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.truetrack.coord')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.coord')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="routeLegInstance" in="${routeInstance.testlegs}" status="i" >
                                    <tr>
                                        <td>${i+1}</td>
                                        <td>${routeLegInstance.title}</td>
                                        <td>${routeLegInstance.coordTrueTrackName()}</td>
                                        <td>${routeLegInstance.mapMeasureTrueTrackName()}</td>
                                        <td>${routeLegInstance.coordDistanceName()}</td>
                                        <td>${routeLegInstance.mapDistanceName()} (${routeLegInstance.mapMeasureDistanceName()})</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${routeInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:if test="${!PlanningTestTask.findByRoute(routeInstance) && !FlightTest.findByRoute(routeInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="createcoordroutes" value="${message(code:'fc.coordroute.add1')}" />
                        <g:actionSubmit action="createsecretcoordroutes" value="${message(code:'fc.coordroute.addsecret')}" />
                        <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}" />
                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>