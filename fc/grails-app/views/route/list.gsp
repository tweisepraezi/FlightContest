<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" importaction="${message(code:'fc.route.import')}" printaction="${message(code:'fc.route.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="9" class="table-head">${message(code:'fc.route.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.title')}</th>
                       <th colspan="2">${message(code:'fc.coordroute.list')}</th>
                       <th colspan="2">${message(code:'fc.routelegcoord.list')}</th>
                       <th colspan="2">${message(code:'fc.routelegtest.list')}</th>
                       <th>${message(code:'fc.planningtesttask.list')}</th>
                       <th>${message(code:'fc.flighttest.list')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${routeInstanceList}" status="i" var="routeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/></td>

                            <td>
                                <g:each var="coordRouteInstance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                                    ${coordRouteInstance.titleCode()}
                                    <br/>                                     
                                </g:each>
                            </td>
                            <td>
                                <g:each var="coordRouteInstance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                                    ${coordRouteInstance.name()}
                                    <br/>                                     
                                </g:each>
                            </td>

                            <td>
                                <g:each var="routeLegCoordInstance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}">
                                    ${routeLegCoordInstance.title}
                                    <br/>                                     
                                </g:each>
                            </td>
                            <td>
                                <g:each var="routeLegCoordInstance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}">
                                    ${routeLegCoordInstance.testName()}
                                    <br/>                                     
                                </g:each>
                            </td>

                            <td>
                                <g:each var="routeLetTestInstance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                    ${routeLetTestInstance.title}
                                    <br/>                                     
                                </g:each>
                            </td>
                            <td>
                                <g:each var="routeLetTestInstance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                    ${routeLetTestInstance.testName()}
                                    <br/>                                     
                                </g:each>
                            </td>

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