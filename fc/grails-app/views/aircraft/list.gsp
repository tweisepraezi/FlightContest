<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="5" class="table-head">${message(code:'fc.aircraft.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aircraft.registration')}</th>
                       <th>${message(code:'fc.aircraft.type')}</th>
                       <th>${message(code:'fc.aircraft.colour')}</th>
                       <th>${message(code:'fc.aircraft.defaulttas')}</th>
                       <th>${message(code:'fc.aircraft.owner')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="aircraftInstance" in="${aircraftInstanceList}" status="i" >
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                            <td>${fieldValue(bean:aircraftInstance, field:'type')}</td>
                            <td>${fieldValue(bean:aircraftInstance, field:'colour')}</td>
                            <td>${fieldValue(bean:aircraftInstance, field:'defaultTAS')}${message(code:'fc.knot')}</td>
                            <td><g:crew var="${aircraftInstance.owner}" link="${createLink(controller:'crew',action:'show')}"/></td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>