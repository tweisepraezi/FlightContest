<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" importaction="." printaction="${message(code:'fc.route.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="10" class="table-head">${message(code:'fc.route.list')}</th>
                        <th class="table-head"><a href="../docs/help.html#route-planning" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.title')}</th>
                       <th>${message(code:'fc.observation.turnpoint')}</th>
                       <th>${message(code:'fc.observation.enroute.photo')}</th>
                       <th>${message(code:'fc.observation.enroute.canvas')}</th>
                       <th>${message(code:'fc.procedureturns')}</th>
                       <th>${message(code:'fc.secretpoints')}</th>
                       <th>${message(code:'fc.distance.to2ldg')}</th>
                       <th>${message(code:'fc.distance.sp2fp')}</th>
                       <th>${message(code:'fc.planningtesttask.list2')}</th>
                       <th>${message(code:'fc.flighttest.list')}</th>
                       <th>${message(code:'fc.observation')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="route_instance" in="${routeInstanceList}" status="i">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <g:set var="next_route" value=""/>
                            <g:set var="next_route_id" value="${route_instance.GetNextID()}" />
                            <g:if test="${next_route_id}">
                                <g:set var="next_route" value="?next=${next_route_id}"/>
                            </g:if>
                            
                            <td><g:route var="${route_instance}" link="${createLink(controller:'route',action:'show')}" next="${next_route}"/><g:if test="${!route_instance.IsRouteOk()}"> !</g:if></td>
                            <td>${message(code:route_instance.turnpointRoute.code)}<g:if test="${!route_instance.IsTurnpointSignOk()}"> !</g:if><br/><g:if test="${route_instance.turnpointMapMeasurement}">${message(code:'fc.observation.turnpoint.map')}</g:if><g:else>${message(code:'fc.observation.turnpoint.log')}</g:else></td>
                            <td>${message(code:route_instance.enroutePhotoRoute.code)}<g:if test="${!route_instance.IsEnrouteSignOk(true)}"> !</g:if><br/>${message(code:route_instance.enroutePhotoMeasurement.code)}<g:if test="${!route_instance.IsEnrouteMeasurementOk(true)}"> !</g:if></td>
                            <td>${message(code:route_instance.enrouteCanvasRoute.code)}<g:if test="${!route_instance.IsEnrouteSignOk(false)}"> !</g:if><br/>${message(code:route_instance.enrouteCanvasMeasurement.code)}<g:if test="${!route_instance.IsEnrouteMeasurementOk(false)}"> !</g:if></td>

                            <g:set var="distance_to2ldg" value="${new BigDecimal(0)}" />
                            <g:set var="procedureturn_num" value="${0}"/>
                            <g:set var="secret_num" value="${0}"/>
                            <g:each var="routeleg_instance" in="${RouteLegCoord.findAllByRoute(route_instance,[sort:'id'])}">
                                <g:set var="distance_to2ldg" value="${FcMath.AddDistance(distance_to2ldg,routeleg_instance.testDistance())}" />
                                <g:if test="${routeleg_instance.IsProcedureTurn()}">
                                    <g:set var="course_change" value="${AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())}"/>
                                    <g:if test="${course_change.abs() >= 90}">
                                        <g:set var="procedureturn_num" value="${procedureturn_num+1}"/>
                                    </g:if>
                                </g:if>
                                <g:if test="${routeleg_instance.startTitle.type == CoordType.SECRET}">
                                    <g:set var="secret_num" value="${secret_num+1}"/>
                                </g:if>
                            </g:each>
                            <g:if test="${procedureturn_num}">
                                <td style="white-space: nowrap;"><g:if test="${route_instance.UseProcedureTurn()}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.disabled')}</g:else> (${procedureturn_num})</td>
                            </g:if>
                            <g:else>
                                <td style="white-space: nowrap;">${message(code:'fc.no')}</td>
                            </g:else>
                            <g:if test="${secret_num}">
                                <td style="white-space: nowrap;">${message(code:'fc.yes')} (${secret_num})</td>
                            </g:if>
                            <g:else>
                                <td style="white-space: nowrap;">${message(code:'fc.no')}</td>
                            </g:else>
                            <td>${FcMath.DistanceStr(distance_to2ldg)}${message(code:'fc.mile')}</td>
                            
                            <g:set var="distance_sp2fp" value="${new BigDecimal(0)}" />
                            <g:each var="routeleg_instance" in="${RouteLegTest.findAllByRoute(route_instance,[sort:'id'])}">
                                <g:set var="distance_sp2fp" value="${FcMath.AddDistance(distance_sp2fp,routeleg_instance.testDistance())}" />
                            </g:each>
                            <td>${FcMath.DistanceStr(distance_sp2fp)}${message(code:'fc.mile')}</td>
                            
                            <td>
                                <g:each var="c" in="${PlanningTestTask.findAllByRoute(route_instance,[sort:"id"])}">
                                    <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>

                            <td>
                                <g:each var="c" in="${FlightTest.findAllByRoute(route_instance,[sort:"id"])}">
                                    <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>
                            
                            <td>
                                <g:each var="flighttest_instance" in="${FlightTest.findAllByRoute(route_instance,[sort:"id"])}">
                                    <g:if test="${flighttest_instance.IsObservationSignUsed()}">
                                        ${message(code:'fc.yes')}
                                    </g:if>
                                    <g:else>
                                        ${message(code:'fc.no')}
                                    </g:else>
                                    <br/>
                                </g:each>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>