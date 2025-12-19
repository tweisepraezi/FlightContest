<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:if test="${taskInstance.flighttest.route.corridorWidth}">
            <title>${message(code:'fc.task.assignflighttestwind.route')}</title>
        </g:if>
        <g:else>
            <title>${message(code:'fc.task.assignflighttestwind.wind')}</title>
        </g:else>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <g:if test="${taskInstance.flighttest.route.corridorWidth}">
                    <h2>${message(code:'fc.task.assignflighttestwind.route')}</h2>
                </g:if>
                <g:else>
                    <h2>${message(code:'fc.task.assignflighttestwind.wind')}</h2>
                </g:else>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['testInstanceIDs':testInstanceIDs]}" >
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                                <tr>
                                    <td><g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <g:if test="${taskInstance.flighttest.route.corridorWidth}">
                                        <td class="detailtitle"><label>${message(code:'fc.route')}:</label></td>
                                    </g:if>
                                    <g:else>
                                        <td class="detailtitle"><label>${message(code:'fc.flighttestwind')}:</label></td>
                                    </g:else>
                                    <td><g:select optionKey="id" optionValue="${{it.name()}}" from="${FlightTestWind.findAllByFlighttest(taskInstance.flighttest,[sort:"id"])}" name="flighttestwind.id" value="${taskInstance?.flighttest?.id}" ></g:select></td>
                                </tr> 
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.assignflighttestwind.tocrews')}:</td>
                                    <td  valign="top" style="text-align:left;" class="value">
                                        <g:each var="testInstanceID" in="${testInstanceIDs}">
                                            <g:if test="${testInstanceID}">
                                                <g:set var="testInstance" value="${Test.get(testInstanceID)}"/>
	                                            <g:if test="${testInstance}">
	                                                ${testInstance.crew.startNum}: ${testInstance.crew.name}<g:if test="${testInstance.flighttestwind}"> (${testInstance.flighttestwind.name()})</g:if>
	                                            </g:if>
	                                            <g:else>
	                                            	[${testInstanceID}]
	                                            </g:else>
                                                <br/>                                                
                                            </g:if>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="warning">${message(code:'fc.task.assignflighttestwind.recalculatetimes')}</td>
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