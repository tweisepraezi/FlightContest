<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.errorpoints.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aflosErrorPoints" />
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
                        <th colspan="11" class="table-head">${message(code:'fc.aflos.errorpoints.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aflos.crewnames.startno')}</th>
                       <th>${message(code:'fc.aflos.crewnames.name')}</th>
                       <th>${message(code:'fc.aflos.routedefs.routename')}</th>
                       <th>${message(code:'fc.aflos.errorpoints.mark')}</th>
                       <th>${message(code:'fc.aflos.utc')}</th>
                       <th>${message(code:'fc.latitude')}</th>
                       <th>${message(code:'fc.longitude')}</th>
                       <th>${message(code:'fc.altitude')}</th>
                       <th>${message(code:'fc.aflos.direction')}</th>
                       <th>${message(code:'fc.truetrack')}</th>
                       <th>${message(code:'fc.aflos.speed')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="aflosErrorPointsInstance" in="${aflosErrorPointsInstanceList}" status="i" >
                        <tr class="${(aflosErrorPointsInstance.startnum % 2) == 0 ? 'odd' : ''}">
                            <td>${aflosErrorPointsInstance.startnum}</td>
							<g:if test="${contestInstance?.aflosTest}">
                                <td>${AflosCrewNames.aflostest.findByStartnumAndNameIsNotNull(aflosErrorPointsInstance.startnum).name}</td>
							</g:if>
							<g:elseif test="${contestInstance?.aflosUpload}">
                                <td>${AflosCrewNames.aflosupload.findByStartnumAndNameIsNotNull(aflosErrorPointsInstance.startnum).name}</td>
							</g:elseif>
							<g:else>
                                <td>${AflosCrewNames.aflos.findByStartnumAndNameIsNotNull(aflosErrorPointsInstance.startnum).name}</td>
							</g:else>
                            <td>${aflosErrorPointsInstance.routename.name}</td>
                            <td>${aflosErrorPointsInstance.mark}</td>
                            <td>${aflosErrorPointsInstance.utc}</td>
                            <td>${aflosErrorPointsInstance.latitude}</td>
                            <td>${aflosErrorPointsInstance.longitude}</td>
                            <td>${aflosErrorPointsInstance.altitude}</td>
                            <td>${aflosErrorPointsInstance.direction}</td>
                            <td>${aflosErrorPointsInstance.truetrack}</td>
                            <td>${aflosErrorPointsInstance.speed}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>