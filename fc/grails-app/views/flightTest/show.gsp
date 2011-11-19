<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttest.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttest.show')}</h2>
                <div class="block" id="forms" >
                    <g:form params="${['flighttestReturnAction':flighttestReturnAction,'flighttestReturnController':flighttestReturnController,'flighttestReturnID':flighttestReturnID]}" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${flightTestInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <td><g:route var="${flightTestInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind.list')}:</td>
                                    <td>
                                        <g:each var="flightTestWindInstance" in="${flightTestInstance.flighttestwinds}">
                                            <g:flighttestwind var="${flightTestWindInstance}" link="${createLink(controller:'flightTestWind',action:'edit')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${flightTestInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:each var="flightTestWindInstance" in="${flightTestInstance.flighttestwinds}">
                            <g:if test="${Test.findByFlighttestwind(flightTestWindInstance)}">
                                <g:set var="foundTest" value="${true}" />
                            </g:if>
                        </g:each>
                        <g:if test="${!foundTest}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="createflighttestwind" value="${message(code:'fc.flighttestwind.add1')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>