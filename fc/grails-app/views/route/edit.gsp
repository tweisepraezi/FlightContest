<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.edit')}</h2>
                <g:hasErrors bean="${routeInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${routeInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${routeInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:routeInstance,field:'title')}"/>
                            </p>
                            <g:if test="${routeInstance.mark || routeInstance.ExistAnyAflosRoute()}">
	                            <p>
	                                <label>${message(code:'fc.route.aflosimport.name')}:</label>
	                                <br/>
	                                <input type="text" id="mark" name="mark" value="${fieldValue(bean:routeInstance,field:'mark')}"/>
	                            </p>
	                        </g:if>
                        </fieldset>
                        <input type="hidden" name="id" value="${routeInstance?.id}" />
                        <input type="hidden" name="version" value="${routeInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>