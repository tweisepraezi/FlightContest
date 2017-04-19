<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.checkpoints.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aflosCheckPoints" />
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
                        <th colspan="11" class="table-head">${message(code:'fc.aflos.checkpoints.list')}</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aflos.crewnames.startno')}</th>
                       <th>${message(code:'fc.aflos.crewnames.name')}</th>
                       <th>${message(code:'fc.aflos.routedefs.routename')}</th>
                       <th>${message(code:'fc.aflos.checkpoint')}</th>
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
                    <g:each var="aflosCheckPointsInstance" in="${aflosCheckPointsInstanceList}" status="i" >
                        <tr class="${(aflosCheckPointsInstance.startnum % 2) == 0 ? 'odd' : ''}">
                            <td>${aflosCheckPointsInstance.startnum}</td>
							<g:if test="${contestInstance?.aflosTest}">
                                <td>${AflosCrewNames.aflostest.findByStartnumAndPointsNotEqual(aflosCheckPointsInstance.startnum,0).name}</td>
							</g:if>
							<g:elseif test="${contestInstance?.aflosUpload}">
                                <td>${AflosCrewNames.aflosupload.findByStartnumAndPointsNotEqual(aflosCheckPointsInstance.startnum,0).name}</td>
							</g:elseif>
							<g:else>
                                <td>${AflosCrewNames.aflos.findByStartnumAndPointsNotEqual(aflosCheckPointsInstance.startnum,0).name}</td>
							</g:else>
                            <td>${aflosCheckPointsInstance.routename.name}</td>
                            <td>${aflosCheckPointsInstance.mark}</td>
                            <td>${aflosCheckPointsInstance.utc}</td>
                            <td>${aflosCheckPointsInstance.latitude}</td>
                            <td>${aflosCheckPointsInstance.longitude}</td>
                            <td>${aflosCheckPointsInstance.altitude}</td>
                            <td>${aflosCheckPointsInstance.direction}</td>
                            <td>${aflosCheckPointsInstance.truetrack}</td>
                            <td>${aflosCheckPointsInstance.speed}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>