<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.selectplanningtesttask')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.selectplanningtesttask')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['testInstanceIDs':testInstanceIDs]}" >
                        <table>
                            <table>
                                <tbody>
                                    <tr>
                                        <td/>
                                        <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                    </tr>
                                    <tr>
                                        <td/>
                                        <td><g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.planningtesttask')}:</label></td>
                                    <td><g:select optionKey="id" optionValue="${{it.name()}}" from="${PlanningTestTask.findAllByPlanningtest(taskInstance.planningtest,[sort:"id"])}" name="planningtesttask.id" value="${taskInstance?.planningtest?.id}" ></g:select></td>
                                </tr> 
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.selectplanningtesttask.tocrews')}:</td>
                                    <td>
                                        <g:each var="testInstanceID" in="${testInstanceIDs}">
                                            <g:if test="${testInstanceID}">
                                                <g:set var="testInstance" value="${Test.get(testInstanceID)}"/>
	                                            <g:if test="${testInstance}">
	                                                ${testInstance.crew.startNum}: ${testInstance.crew.name}<g:if test="${testInstance.planningtesttask}"> (${testInstance.planningtesttask.name()})</g:if>
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
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <g:actionSubmit action="setplanningtesttask" value="${message(code:'fc.assign')}" />
                        <g:actionSubmit action="listplanning" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>