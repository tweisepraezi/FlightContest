<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtesttask.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtesttask.create')}</h2>
                <g:hasErrors bean="${navTestTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${navTestTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromlistcrewtests}">
                        <g:set var="newparams" value="['navtestid':params.navtestid,'fromlistcrewtests':true]"/>
                    </g:if> <g:else> 
                        <g:set var="newparams" value="['navtestid':params.navtestid]"/>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:navTestTaskInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="${{it.name()}}" from="${Route.list()}" name="route.id" value="${navTestTaskInstance?.route?.id}" ></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="TAS" name="TAS" value="${fieldValue(bean:navTestTaskInstance,field:'TAS')}" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:navTestTaskInstance,field:'direction')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.speed')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:navTestTaskInstance,field:'speed')}"/>
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