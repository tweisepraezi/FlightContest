<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.name1')}:</td>
                                    <td>${fieldValue(bean:crewInstance, field:'name1')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.name2')}:</td>
                                    <td>${fieldValue(bean:crewInstance, field:'name2')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.country')}:</td>
                                    <td>${fieldValue(bean:crewInstance, field:'country')}</td>
                                </tr>
                                <g:if test="${crewInstance.ownAircraft}">
                                    <tr>
                                        <td class="detailtitle"">${message(code:'fc.crew.ownaircraft')}:</td>
                                        <td><g:aircraft var="${crewInstance.ownAircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.aircraft.defaulttas')}:</td>
                                        <td>${fieldValue(bean:crewInstance.ownAircraft, field:'defaultTAS')}${message(code:'fc.knot')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${crewInstance.usedAircraft}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.crew.usedaircraft')}:</td>
                                        <td><g:aircraft var="${crewInstance.usedAircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.crew.usedtas')}:</td>
                                        <td>${fieldValue(bean:crewInstance, field:'usedTAS')}${message(code:'fc.knot')}</td>
                                    </tr>
                                </g:if>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${crewInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>