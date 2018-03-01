<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtesttask.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtesttask.create')}</h2>
                <g:hasErrors bean="${planningTestTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${planningTestTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromlistplanning}">
                        <g:set var="newparams" value="['planningtestid':params.planningtestid,'fromlistplanning':true]"/>
                    </g:if> <g:else> 
                        <g:set var="newparams" value="['planningtestid':params.planningtestid]"/>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:planningTestTaskInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}*:</label>
                                <br/>
                                <g:if test="${contestInstance}">
                                    <g:select from="${Route.GetOkRoutes(contestInstance)}" optionKey="id" optionValue="${{it.name()}}" name="route.id" value="${planningTestTaskInstance?.route?.id}" ></g:select>
                                </g:if>
                            </p>
                        </fieldset>
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
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>