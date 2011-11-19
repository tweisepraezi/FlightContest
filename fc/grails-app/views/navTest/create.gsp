<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtest.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtest.create')}</h2>
                <g:hasErrors bean="${navTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${navTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromlistcrewtests}">
                        <g:set var="newparams" value="['contestdaytaskid':params.contestdaytaskid,'fromlistcrewtests':true]"/>
                    </g:if> <g:else> 
                        <g:if test="${params.fromcontestdaytask}">
                          <g:set var="newparams" value="['contestdaytaskid':params.contestdaytaskid,'fromcontestdaytask':true]"/>
                        </g:if> <g:else>
                            <g:set var="newparams" value="['contestdaytaskid':params.contestdaytaskid]"/>
                        </g:else>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:navTestInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.navtesttask')}</legend>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="taskTitle" name="taskTitle" value="${fieldValue(bean:navTestInstance,field:'taskTitle')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="${{it.name()}}" from="${Route.list()}" name="route.id" value="${navTestInstance?.route?.id}" ></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="TAS" name="TAS" value="${fieldValue(bean:navTestInstance,field:'TAS')}" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:navTestInstance,field:'direction')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.speed')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:navTestInstance,field:'speed')}"/>
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