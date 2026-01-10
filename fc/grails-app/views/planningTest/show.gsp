<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtest.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtest.show')}</h2>
                <div class="block" id="forms" >
                    <g:form params="${['planningtestReturnAction':planningtestReturnAction,'planningtestReturnController':planningtestReturnController,'planningtestReturnID':planningtestReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                        <g:task var="${planningTestInstance.task}" link="${createLink(controller:'task',action:'listplanning')}" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                           <p>
                                <g:set var="planningtesttask_instance" value="${PlanningTestTask.findByPlanningtest(planningTestInstance)}" />
                                <label>${message(code:'fc.route')}:</label>
                                <g:parcour var="${planningtesttask_instance.route}" link="${createLink(controller:'route',action:'show')}"/>
                            </p>
                           <p>
                                <label>${message(code:'fc.addtitle')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:planningTestInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <table>
                            <tbody>
                               <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask.list')}:</td>
                                    <td>
                                        <g:each var="n" in="${planningTestInstance.planningtesttasks}">
                                            <g:planningtesttask var="${n}" link="${createLink(controller:'planningTestTask',action:'edit')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${planningTestInstance?.id}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:each var="planningTestTaskInstance" in="${planningTestInstance.planningtesttasks}">
                            <g:if test="${Test.findByPlanningtesttask(planningTestTaskInstance)}">
                                <g:set var="foundTest" value="${true}" />
                            </g:if>
                        </g:each>
                        <g:if test="${!foundTest && !planningTestInstance.task.lockPlanning}">
                           <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>