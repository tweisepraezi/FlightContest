<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtest.edit')}</title>
    </head>
    <body>
		<g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtest.edit')}</h2>
                <g:hasErrors bean="${planningTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${planningTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${planningTestInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:planningTestInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${planningTestInstance?.id}" />
                        <input type="hidden" name="version" value="${planningTestInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>