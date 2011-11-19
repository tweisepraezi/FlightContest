<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.crewnames.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aflosCrewNames" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="2" class="table-head">${message(code:'fc.aflos.crewnames.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aflos.crewnames.startno')}</th>
                       <th>${message(code:'fc.aflos.crewnames.name')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="aflosCrewNamesInstance" in="${aflosCrewNamesInstanceList}" status="i" >
                        <g:if test="${aflosCrewNamesInstance.points}">
                            <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                <td>${aflosCrewNamesInstance.startnum}</td>
                                <td>${aflosCrewNamesInstance.name}</td>
                            </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>