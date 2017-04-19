<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.tasks')}</title>
    </head>
    <body>
        <g:if test="${contestInstance}">
            <g:mainnav link="${createLink(controller:'contest')}" controller="contest" id="${contestInstance.id}" newaction="${message(code:'fc.task.new')}" contesttasks="true" />
        </g:if> <g:else>
            <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        </g:else>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:if test="${contestInstance}">
                <table>
                    <thead>
                        <tr>
                            <th colspan="5" class="table-head">${message(code:'fc.contest.tasks')}</th>
                        </tr>
                        <tr>
                            <th>${message(code:'fc.task')}</th>
                            <th>${message(code:'fc.planningtest')}</th>
                            <th>${message(code:'fc.flighttest')}</th>
                            <th>${message(code:'fc.task.listplanning')}</th>
                            <th>${message(code:'fc.task.listresults')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${contestTasks}" status="i" var="taskInstance">
                            <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                <td colspan=1><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/></td>

                                <g:if test="${taskInstance.planningtest}">
                                    <td colspan=1><g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/></td>
                                </g:if> <g:else>
                                    <td class="add" colspan=1><g:link controller="planningTest" params="['task.id':taskInstance?.id,'taskid':taskInstance?.id]" action="create">${message(code:'fc.planningtest.add')}</g:link></td>
                                </g:else>

                                <g:if test="${taskInstance.flighttest}">
                                    <td colspan=1><g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                </g:if> <g:else>
                                    <td class="add" colspan=1><g:link controller="flightTest" params="['task.id':taskInstance?.id,'taskid':taskInstance?.id]" action="create">${message(code:'fc.flighttest.add')}</g:link></td>
                                </g:else>
                                
                                <g:if test="${taskInstance.hidePlanning}">
                                    <td>${message(code:'fc.hided')}</td>
                                </g:if>
                                <g:else>
                                    <td/>
                                </g:else>
                                
                                <g:if test="${taskInstance.hideResults}">
                                    <td>${message(code:'fc.hided')}</td>
                                </g:if>
                                <g:else>
                                    <td/>
                                </g:else>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </g:if>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>