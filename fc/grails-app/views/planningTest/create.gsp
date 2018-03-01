<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtest.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtest.create')}</h2>
                <g:hasErrors bean="${planningTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${planningTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromlistplanning}">
                        <g:set var="newparams" value="['taskid':params.taskid,'fromlistplanning':true]"/>
                    </g:if> <g:else> 
                        <g:if test="${params.fromtask}">
                          <g:set var="newparams" value="['taskid':params.taskid,'fromtask':true]"/>
                        </g:if> <g:else>
                            <g:set var="newparams" value="['taskid':params.taskid]"/>
                        </g:else>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:planningTestInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.planningtesttask')}</legend>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="taskTitle" name="taskTitle" value="${fieldValue(bean:planningTestInstance,field:'taskTitle')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}*:</label>
                                <br/>
                                <g:select from="${Route.GetOkRoutes(planningTestInstance.task.contest)}" optionKey="id" optionValue="${{it.name()}}" name="route.id" value="${planningTestInstance?.route?.id}" ></g:select>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')}* [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:planningTestInstance,field:'direction')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.velocity')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:planningTestInstance,field:'speed')}"/>
                            </p>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>