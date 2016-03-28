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
                        <th colspan="9" class="table-head">${message(code:'fc.route.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.title')}</th>
                       <th>${message(code:'fc.distance.to2ldg')}</th>
                       <th>${message(code:'fc.distance.sp2fp')}</th>
                       <th>${message(code:'fc.planningtesttask.list')}</th>
                       <th>${message(code:'fc.flighttest.list')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${routeInstanceList}" status="i" var="routeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/></td>

                            <g:set var="distance_to2ldg" value="${new BigDecimal(0)}" />
                            <g:each var="routeleg_instance" in="${routeInstance.routelegs}">
                                <g:set var="distance_to2ldg" value="${FcMath.AddDistance(distance_to2ldg,routeleg_instance.testDistance())}" />
                            </g:each>
                            <td>${FcMath.DistanceStr(distance_to2ldg)}${message(code:'fc.mile')}</td>
                            
                            <g:set var="distance_sp2fp" value="${new BigDecimal(0)}" />
                            <g:each var="routeleg_instance" in="${routeInstance.testlegs}">
                                <g:set var="distance_sp2fp" value="${FcMath.AddDistance(distance_sp2fp,routeleg_instance.testDistance())}" />
                            </g:each>
                            <td>${FcMath.DistanceStr(distance_sp2fp)}${message(code:'fc.mile')}</td>
                            
                            <td>
                                <g:each var="c" in="${PlanningTestTask.findAllByRoute(routeInstance,[sort:"id"])}">
                                    <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>

                            <td>
                                <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                    <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>