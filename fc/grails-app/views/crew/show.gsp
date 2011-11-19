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
                                    <td class="detailtitle">${message(code:'fc.crew.name')}:</td>
                                    <td>${fieldValue(bean:crewInstance, field:'name')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.import.name')}:</td>
                                    <td>${fieldValue(bean:crewInstance, field:'mark')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.country')}:</td>
                                    <td>${fieldValue(bean:crewInstance, field:'country')}</td>
                                </tr>
                                <g:if test="${crewInstance.aircraft}">
                                    <tr>
                                        <td class="detailtitle"">${message(code:'fc.crew.aircraft')}:</td>
                                        <td><g:aircraft var="${crewInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                        <td>${fieldValue(bean:crewInstance, field:'tas')}${message(code:'fc.knot')}</td>
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