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
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.from')}:</td>
                                    <td><g:contestday var="${flightTestInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${flightTestInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
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
                                            <g:flighttestwind var="${flightTestWindInstance}" link="${createLink(controller:'flightTestWind',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${flightTestInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:each var="flightTestWindInstance" in="${flightTestInstance.flighttestwinds}">
                            <g:if test="${CrewTest.findByFlighttestwind(flightTestWindInstance)}">
                                <g:set var="foundCrewTest" value="${true}" />
                            </g:if>
                        </g:each>
                        <g:if test="${!foundCrewTest}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="createflighttestwind" value="${message(code:'fc.flighttestwind.add1')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>