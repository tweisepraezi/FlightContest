<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.routedefs.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aflosRouteDefs" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <tbody>
                    <tr>
                        <g:if test="${contestInstance?.aflosTest}">
                            <td>${message(code:'fc.aflos.show.test')}</td>
                        </g:if>
                        <g:elseif test="${contestInstance?.aflosUpload}">
                            <td>${message(code:'fc.aflos.show.upload')}</td>
                        </g:elseif>
                        <g:else>
                            <td>${message(code:'fc.aflos.show.local')}</td>
                        </g:else>
                    </tr>
                </tbody>
            </table>
            <table>
                <thead>
                    <tr>
                        <th colspan="8" class="table-head">${message(code:'fc.aflos.routedefs.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aflos.routedefs.routename')}</th>
                       <th>${message(code:'fc.aflos.checkpoint')}</th>
                       <th>${message(code:'fc.latitude')}</th>
                       <th>${message(code:'fc.longitude')}</th>
                       <th>${message(code:'fc.altitude')}</th>
                       <th>${message(code:'fc.truetrack')}</th>
                       <th>${message(code:'fc.distance')}</th>
                       <th>${message(code:'fc.gatewidth')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="aflosRouteDefsInstance" in="${aflosRouteDefsInstanceList}" status="i" >
                        <tr class="${(aflosRouteDefsInstance.routename.id % 2) == 0 ? 'odd' : ''}">
                            <td>${aflosRouteDefsInstance.routename.name}</td>
                            <td>${aflosRouteDefsInstance.mark}</td>
                            <td>${aflosRouteDefsInstance.latitude}</td>
                            <td>${aflosRouteDefsInstance.longitude}</td>
                            <td>${aflosRouteDefsInstance.altitude}${message(code:'fc.foot')}</td>
                            <td>${aflosRouteDefsInstance.truetrack}${message(code:'fc.grad')}</td>
                            <td>${aflosRouteDefsInstance.coordDistance}${message(code:'fc.mile')}</td>
                            <td>${aflosRouteDefsInstance.gatewidth}${message(code:'fc.mile')}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>