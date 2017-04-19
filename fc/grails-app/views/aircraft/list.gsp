<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" printaction="${message(code:'fc.aircraft.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="5" class="table-head">${message(code:'fc.aircraft.list')} (${aircraftInstanceList.size()})</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.aircraft.registration')}</th>
                       <th>${message(code:'fc.aircraft.type')}</th>
                       <th>${message(code:'fc.aircraft.colour')}</th>
                       <th>${message(code:'fc.aircraft.crew1')}</th>
                       <th>${message(code:'fc.aircraft.crew2')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="aircraftInstance" in="${aircraftInstanceList}" status="i" >
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                            <td>${fieldValue(bean:aircraftInstance, field:'type')}</td>
                            <td>${fieldValue(bean:aircraftInstance, field:'colour')}</td>
                            <td><g:crew var="${aircraftInstance.user1}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${aircraftInstance.user1?.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
                            <td><g:crew var="${aircraftInstance.user2}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${aircraftInstance.user2?.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>