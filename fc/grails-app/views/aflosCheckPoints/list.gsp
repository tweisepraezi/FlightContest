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
                            <td>${AflosCrewNames.findByStartnumAndNameIsNotNull(aflosCheckPointsInstance.startnum).name}</td>
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
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>