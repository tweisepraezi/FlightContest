<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aircraft.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <td>${fieldValue(bean:aircraftInstance, field:'registration')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>
                                    <td>${fieldValue(bean:aircraftInstance, field:'type')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.colour')}:</td>
                                    <td>${fieldValue(bean:aircraftInstance, field:'colour')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.user1')}:</td>
                                    <td><g:crew var="${aircraftInstance.user1}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.user2')}:</td>
                                    <td><g:crew var="${aircraftInstance.user2}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${aircraftInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>