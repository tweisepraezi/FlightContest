<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="5" class="table-head">${message(code:'fc.route.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.title')}</th>
                       <th>${message(code:'fc.routecoord.list')}</th>
                       <th>${message(code:'fc.routeleg.list')}</th>
                       <th>${message(code:'fc.navtesttask.list')}</th>
                       <th>${message(code:'fc.flighttest.list')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${routeInstanceList}" status="i" var="routeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/></td>
                            <td>
                                <g:each var="c" in="${RouteCoord.findAllByRoute(routeInstance)}">
                                    <g:routecoord var="${c}" link="${createLink(controller:'routeCoord',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>
                            <td>
                                <g:each var="c" in="${RouteLeg.findAllByRoute(routeInstance)}">
                                    <g:routeleg var="${c}" link="${createLink(controller:'routeLeg',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>
                            <td>
                                <g:each var="c" in="${NavTestTask.findAllByRoute(routeInstance)}">
                                    <g:navtesttask var="${c}" link="${createLink(controller:'navTestTask',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>
                            <td>
                                <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance)}">
                                    <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                    <br/>                                     
                                </g:each>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>