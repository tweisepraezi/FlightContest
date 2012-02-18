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
                            <tr class="${(aflosCrewNamesInstance.startnum % 2) == 0 ? 'odd' : ''}">
                                <td>${aflosCrewNamesInstance.startnum}</td>
                                <td>${aflosCrewNamesInstance.name}</td>
                            </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>