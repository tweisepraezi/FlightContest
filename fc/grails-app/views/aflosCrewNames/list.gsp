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
                        <th colspan="3" class="table-head">${message(code:'fc.aflos.crewnames.list')} (${aflosCrewNamesInstanceTotal})</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aflos.crewnames.startno')}</th>
                       <th>${message(code:'fc.aflos.crewnames.name')}</th>
                       <th>${message(code:'fc.aflos.crewnames.points')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="afloscrewaames_instance" in="${aflosCrewNamesInstanceList}" status="i" >
                        <tr class="${(afloscrewaames_instance.startnum % 2) == 0 ? 'odd' : ''}">
                            <td>${afloscrewaames_instance.startnum}</td>
                            <td>${afloscrewaames_instance.name}</td>
                            <td>${afloscrewaames_instance.points}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>