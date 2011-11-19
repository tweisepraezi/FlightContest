<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.errors.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'aflos')}" controller="aflosErrors" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="8" class="table-head">${message(code:'fc.aflos.errors.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aflos.crewnames.startno')}</th>
                       <th>${message(code:'fc.aflos.crewnames.name')}</th>
                       <th>${message(code:'fc.aflos.routedefs.routename')}</th>
                       <th>${message(code:'fc.aflos.errors.checkpoints')}</th>
                       <th>${message(code:'fc.aflos.errors.course')}</th>
                       <th>${message(code:'fc.aflos.errors.height')}</th>
                       <th>${message(code:'fc.aflos.errors.dropout')}</th>
                       <th>${message(code:'fc.aflos.errors.result')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="aflosErrorsInstance" in="${aflosErrorsInstanceList}" status="i" >
                        <tr class="${(aflosErrorsInstance.startnum % 2) == 0 ? 'odd' : ''}">
                            <td>${aflosErrorsInstance.startnum}</td>
                            <td>${AflosCrewNames.findByStartnumAndNameIsNotNull(aflosErrorsInstance.startnum).name}</td>
                            <td>${aflosErrorsInstance.routename.name}</td>
                            <td>${aflosErrorsInstance.checkPointErrors}</td>
                            <td>${aflosErrorsInstance.courseErrors}</td>
                            <td>${aflosErrorsInstance.heightErrors}</td>
                            <td>${aflosErrorsInstance.dropOutErrors}</td>
                            <td>${aflosErrorsInstance.mark}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>