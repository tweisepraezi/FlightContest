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
                                    <th class="table-head" colspan="7">${message(code:'fc.routecoord.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.mark')}</th>
                                    <th>${message(code:'fc.latitude')}</th>
                                    <th>${message(code:'fc.longitude')}</th>
                                    <th>${message(code:'fc.altitude')}</th>
                                    <th>${message(code:'fc.gatewidth')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="routeCoordInstance" in="${routeInstance.routecoords}" status="i" >
                                    <tr>
                                        <td><g:routecoordnum var="${routeCoordInstance}" num="${i+1}" link="${createLink(controller:'routeCoord',action:'edit')}"/></td>
                                        <td>${routeCoordInstance.title()}</td>
                                        <td>${routeCoordInstance.mark}</td>
                                        <td>${routeCoordInstance.latName()}</td>
                                        <td>${routeCoordInstance.lonName()}</td>
                                        <td>${routeCoordInstance.altitude}${message(code:'fc.foot')}</td>
                                        <td>${routeCoordInstance.gatewidth}${message(code:'fc.mile')}</td>
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
                                    <th>${message(code:'fc.truetrack')}</th>
                                    <th>${message(code:'fc.distance.coord')}</th>
                                    <th>${message(code:'fc.distance.map.measure')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="routeLegInstance" in="${routeInstance.routelegs}" status="i" >
                                    <tr>
                                        <td><g:routelegcoordnum var="${routeLegInstance}" num="${i+1}" link="${createLink(controller:'routeLegCoord',action:'edit')}"/></td>
                                        <td>${routeLegInstance.title}</td>
                                        <td>${routeLegInstance.trueTrackName()}</td>
                                        <td>${routeLegInstance.coordDistanceName()}</td>
                                        <td>${routeLegInstance.mapMeasureDistanceName()}</td>
                                        <td>${routeLegInstance.mapDistanceName()}</td>
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
                                    <th>${message(code:'fc.truetrack')}</th>
                                    <th>${message(code:'fc.distance.coord')}</th>
                                    <th>${message(code:'fc.distance.map.measure')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="routeLegInstance" in="${routeInstance.testlegs}" status="i" >
                                    <tr>
                                        <td>${i+1}</td>
                                        <td>${routeLegInstance.title}</td>
                                        <td>${routeLegInstance.trueTrackName()}</td>
                                        <td>${routeLegInstance.coordDistanceName()}</td>
                                        <td>${routeLegInstance.mapMeasureDistanceName()}</td>
                                        <td>${routeLegInstance.mapDistanceName()}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${routeInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:if test="${!PlanningTestTask.findByRoute(routeInstance) && !FlightTest.findByRoute(routeInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="createroutecoords" value="${message(code:'fc.routecoord.add1')}" />
                        <g:actionSubmit action="createsecretroutecoords" value="${message(code:'fc.routecoord.addsecret')}" />
                        <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}" />
                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>