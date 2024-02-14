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
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${planningTestInstance.name()}</td>
                                </tr>
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
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:each var="planningTestTaskInstance" in="${planningTestInstance.planningtesttasks}">
                            <g:if test="${Test.findByPlanningtesttask(planningTestTaskInstance)}">
                                <g:set var="foundTest" value="${true}" />
                            </g:if>
                        </g:each>
                        <g:if test="${!foundTest && !planningTestInstance.task.lockPlanning}">
                           <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:if test="${!planningTestInstance.task.lockPlanning}">
                            <g:actionSubmit action="createplanningtesttask" value="${message(code:'fc.planningtesttask.add1')}" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>