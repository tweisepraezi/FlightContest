<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.errors.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aflosErrors" />
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
							<g:if test="${contestInstance?.aflosTest}">
                                <td>${AflosCrewNames.aflostest.findByStartnumAndPointsNotEqual(aflosErrorsInstance.startnum,0).name}</td>
							</g:if>
							<g:elseif test="${contestInstance?.aflosUpload}">
                                <td>${AflosCrewNames.aflosupload.findByStartnumAndPointsNotEqual(aflosErrorsInstance.startnum,0).name}</td>
							</g:elseif>
							<g:else>
                                <td>${AflosCrewNames.aflos.findByStartnumAndPointsNotEqual(aflosErrorsInstance.startnum,0).name}</td>
							</g:else>
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
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>