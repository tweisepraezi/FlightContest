<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="6" class="table-head">${message(code:'fc.crew.list')}</th>
                    </tr>
                    <tr>
                        <th>${message(code:'fc.crew.name')}</th>
                        <th>${message(code:'fc.crew.country')}</th>
                        <th>${message(code:'fc.crew.ownaircraft')}</th>
                        <th>${message(code:'fc.aircraft.defaulttas')}</th>
                        <th>${message(code:'fc.crew.usedaircraft')}</th>
                        <th>${message(code:'fc.crew.usedtas')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${crewInstanceList}" status="i" var="crewInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'show')}"/></td>
                            <td>${fieldValue(bean:crewInstance, field:'country')}</td>
                            <td><g:aircraft var="${crewInstance.ownAircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                               <td>${fieldValue(bean:crewInstance.ownAircraft, field:'defaultTAS')}${message(code:'fc.knot')}</td>
                            <td><g:aircraft var="${crewInstance.usedAircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                            <g:if test="${crewInstance.usedAircraft}">
                                <td>${fieldValue(bean:crewInstance, field:'usedTAS')}${message(code:'fc.knot')}</td>
                            </g:if> <g:else>
                                <td/>
                            </g:else>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>