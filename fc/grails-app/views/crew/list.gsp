<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" printaction="${message(code:'fc.crew.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="4" class="table-head">${message(code:'fc.crew.list')}</th>
                    </tr>
                    <tr>
                        <th>${message(code:'fc.crew.name')}</th>
                        <th>${message(code:'fc.crew.country')}</th>
                        <th>${message(code:'fc.crew.aircraft')}</th>
                        <th>${message(code:'fc.tas')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${crewInstanceList}" status="i" var="crewInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'show')}"/></td>
                            <td>${fieldValue(bean:crewInstance, field:'country')}</td>
                            <td><g:aircraft var="${crewInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                            <td>${fieldValue(bean:crewInstance, field:'tas')}${message(code:'fc.knot')}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>