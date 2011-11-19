<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.landingtest.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.landingtest.create')}</h2>
                <g:hasErrors bean="${landingTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${landingTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromcontestdaytask}">
                        <g:set var="newparams" value="['contestdaytaskid':params.contestdaytaskid,'fromcontestdaytask':true]"/>
                    </g:if> <g:else>
                        <g:set var="newparams" value="['contestdaytaskid':params.contestdaytaskid]"/>
                    </g:else>
                    <g:form method="post" params="${newparams}">
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:landingTestInstance,field:'title')}"/>
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