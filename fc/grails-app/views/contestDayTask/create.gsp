<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestdaytask.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestdaytask.create')}</h2>
                <g:hasErrors bean="${contestDayTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestDayTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromcontestday}">
                        <g:set var="newparams" value="['contestdayid':params.contestdayid,'fromcontestday':true]"/>
                    </g:if>
                    <g:else>
                        <g:set var="newparams" value="['contestdayid':params.contestdayid]"/>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestDayTaskInstance,field:'title')}"/>
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