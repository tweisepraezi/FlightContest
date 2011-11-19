<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.selectflighttestwind')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.selectflighttestwind')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="['testInstanceIDs':testInstanceIDs]" >
                        <table>
                            <table>
                                <tbody>
                                    <tr>
                                        <td/>
                                        <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                    </tr>
                                    <tr>
                                        <td/>
                                        <td><g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.flighttestwind')}:</label></td>
                                    <td><g:select optionKey="id" optionValue="${{it.wind.name()}}" from="${FlightTestWind.findAllByFlighttest(taskInstance.flighttest)}" name="flighttestwind.id" value="${taskInstance?.flighttest?.id}" ></g:select></td>
                                </tr> 
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.selectflighttestwind.tocrews')}:</td>
                                    <td  valign="top" style="text-align:left;" class="value">
                                        <g:each var="testInstanceID" in="${flash.testInstanceIDs}">
                                            <g:if test="${testInstanceID}">
                                                <g:set var="testInstance" value="${Test.get(testInstanceID)}"/>
                                                <g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/>
                                                <g:if test="${testInstance.flighttestwind}">
                                                    (<g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'show')}"/>)
                                                </g:if>                                                
                                                <br/>                                                
                                            </g:if>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <g:actionSubmit action="setflighttestwind" value="${message(code:'fc.assign')}" />
                        <g:actionSubmit action="listplanning" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>