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
                        <th colspan="4" class="table-head">${message(code:'fc.aircraft.list')} (${aircraftInstanceList.size()})</th>
                        <th class="table-head"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></th>
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
                    <g:each var="aircraft_instance" in="${aircraftInstanceList}" status="i" >
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td><g:aircraft var="${aircraft_instance}" link="${createLink(controller:'aircraft',action:'edit')}" /></td>
                            <td>${fieldValue(bean:aircraft_instance, field:'type')}</td>
                            <td>${fieldValue(bean:aircraft_instance, field:'colour')}</td>
                            <td><g:crew var="${aircraft_instance.user1}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${aircraft_instance.user1?.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
                            <td><g:crew var="${aircraft_instance.user2}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${aircraft_instance.user2?.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
                        </tr>
                    </g:each>
                </tbody>
                <tfoot>
                    <tr class="">
                        <td colspan="4"/>
                        <td><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                    </tr>
                </tfoot>
            </table>
            <a name="end"/>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>