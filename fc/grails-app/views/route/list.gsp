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
                        <th colspan="11" class="table-head">${message(code:'fc.route.list')}</th>
                        <th class="table-head"><a href="../docs/help_${session.showLanguage}.html#route-planning" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.title')}</th>
                       <th>${message(code:'fc.observation.turnpoint')}</th>
                       <th>${message(code:'fc.observation.enroute.photo')}</th>
                       <th>${message(code:'fc.observation.enroute.canvas')}</th>
                       <th>${message(code:'fc.procedureturns')}</th>
                       <th>${message(code:'fc.curvedlegs')}</th>
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
                            
                            <g:if test="${route_instance.IsRouteOk()}">
                                <td><g:route var="${route_instance}" link="${createLink(controller:'route',action:'show')}" next="${next_route}"/></td>
                            </g:if>
                            <g:else>
                                <td class="errors"><g:route var="${route_instance}" link="${createLink(controller:'route',action:'show')}" next="${next_route}" error="${true}"/> !</td>
                            </g:else>
                            <g:set var="is_turnpoint_sign_ok" value="${route_instance.IsTurnpointSignOk()}"/>
                            <g:set var="turnpoint_sign_class" value=""/>
                            <g:if test="${!is_turnpoint_sign_ok}">
                                <g:set var="turnpoint_sign_class" value="errors"/>
                            </g:if>
                            <g:set var="is_enroute_photo_ok" value="${route_instance.IsEnrouteSignOk(true)}"/>
                            <g:set var="is_enroute_photo_measurement_ok" value="${route_instance.IsEnrouteMeasurementOk(true)}"/>
                            <g:set var="enroute_photo_class" value=""/>
                            <g:if test="${!is_enroute_photo_ok || !is_enroute_photo_measurement_ok}">
                                <g:set var="enroute_photo_class" value="errors"/>
                            </g:if>
                            <g:set var="is_enroute_canvas_ok" value="${route_instance.IsEnrouteSignOk(false)}"/>
                            <g:set var="is_enroute_canvas_measurement_ok" value="${route_instance.IsEnrouteMeasurementOk(false)}"/>
                            <g:set var="enroute_canvas_class" value=""/>
                            <g:if test="${!is_enroute_canvas_ok || !is_enroute_canvas_measurement_ok}">
                                <g:set var="enroute_canvas_class" value="errors"/>
                            </g:if>
                            <td class="${turnpoint_sign_class}">${message(code:route_instance.turnpointRoute.code)}<g:if test="${!is_turnpoint_sign_ok}"> !</g:if><br/><g:if test="${route_instance.turnpointMapMeasurement}">${message(code:'fc.observation.turnpoint.map')}</g:if><g:else>${message(code:'fc.observation.turnpoint.log')}</g:else></td>
                            <td class="${enroute_photo_class}">${message(code:route_instance.enroutePhotoRoute.code)}<g:if test="${!is_enroute_photo_ok}"> !</g:if><br/>${message(code:route_instance.enroutePhotoMeasurement.code)}<g:if test="${!is_enroute_photo_measurement_ok}"> !</g:if></td>
                            <td class="${enroute_canvas_class}">${message(code:route_instance.enrouteCanvasRoute.code)}<g:if test="${!is_enroute_canvas_ok}"> !</g:if><br/>${message(code:route_instance.enrouteCanvasMeasurement.code)}<g:if test="${!is_enroute_canvas_measurement_ok}"> !</g:if></td>

                            <g:set var="route_data" value="${route_instance.GetRouteData()}" />
                            <g:if test="${route_data.procedureturn_num}">
                                <td style="white-space: nowrap;"><g:if test="${route_instance.useProcedureTurns}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.disabled')}</g:else> (${route_data.procedureturn_num})</td>
                            </g:if>
                            <g:else>
                                <td style="white-space: nowrap;">${message(code:'fc.no')}</td>
                            </g:else>
                            <g:if test="${route_data.curved_num}">
                                <td style="white-space: nowrap;">${message(code:'fc.yes')} (${route_data.curved_num})</td>
                            </g:if>
                            <g:else>
                                <td style="white-space: nowrap;">${message(code:'fc.no')}</td>
                            </g:else>
                            <g:if test="${route_data.secret_num}">
                                <td style="white-space: nowrap;">${message(code:'fc.yes')} (${route_data.secret_num})</td>
                            </g:if>
                            <g:else>
                                <td style="white-space: nowrap;">${message(code:'fc.no')}</td>
                            </g:else>
                            <td>${FcMath.DistanceStr(route_data.distance_to2ldg)}${message(code:'fc.mile')}</td>
                            <td>${FcMath.DistanceStr(route_data.distance_sp2fp)}${message(code:'fc.mile')}</td>
                            
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