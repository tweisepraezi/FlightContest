<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtesttask.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtesttask.edit')}</h2>
                <g:hasErrors bean="${planningTestTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${planningTestTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['planningtesttaskReturnAction':planningtesttaskReturnAction,'planningtesttaskReturnController':planningtesttaskReturnController,'planningtesttaskReturnID':planningtesttaskReturnID]}" >
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:planningtest var="${planningTestTaskInstance?.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${planningTestTaskInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:planningTestTaskInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <g:if test="${!planningTestTaskInstance.Used() && !planningTestTaskInstance.planningtest.task.lockPlanning}">
                            <fieldset>
                                <p>
                                    <label>${message(code:'fc.route')}*:</label>
                                    <br/>
                                    <g:select from="${RouteTools.GetOkPlanningTestTaskRoutes(contestInstance)}" optionKey="id" optionValue="${{it.GetPlanningTestTaskRouteName()}}" name="route.id" value="${planningTestTaskInstance?.route?.id}" ></g:select>
                                </p>
                            </fieldset>
                         </g:if>
                         <g:else>
                            <fieldset>
                                <legend>${message(code:'fc.route')}</legend>
                                <p/>
                                <br/>
                                <p>${planningTestTaskInstance.route.name()}</p>
                            </fieldset>
                         </g:else>
                        <g:if test="${!planningTestTaskInstance.Used() && !planningTestTaskInstance.planningtest.task.lockPlanning}">
	                        <fieldset>
	                            <legend>${message(code:'fc.wind')}</legend>
	                            <p>
	                                <label>${message(code:'fc.wind.direction')}* [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:planningTestTaskInstance,field:'direction')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.wind.velocity')}* [${message(code:'fc.knot')}]:</label>
	                                <br/>
	                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:planningTestTaskInstance,field:'speed')}"/>
	                            </p>
	                        </fieldset>
                        </g:if>
                        <g:else>
                            <fieldset>
                                <legend>${message(code:'fc.wind')}</legend>
                                <p/>
                                <br/>
                                <p>${planningTestTaskInstance.wind.name()}</p>
                            </fieldset>
                        </g:else>
                        <input type="hidden" name="id" value="${planningTestTaskInstance?.id}" />
                        <input type="hidden" name="version" value="${planningTestTaskInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:if test="${!Test.findByPlanningtesttask(planningTestTaskInstance) && !planningTestTaskInstance.planningtest.task.lockPlanning}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>