<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.settings')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.settings')}</h2>
                <g:hasErrors bean="${routeInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${routeInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['routeReturnAction':routeReturnAction,'routeReturnController':routeReturnController,'routeReturnID':routeReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${routeInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:routeInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <g:if test="${routeInstance.mark || routeInstance.ExistAnyAflosRoute()}">
	                            <p>
	                                <label>${message(code:'fc.route.aflosimport.name')}:</label>
	                                <br/>
	                                <input type="text" id="mark" name="mark" value="${fieldValue(bean:routeInstance,field:'mark')}" tabIndex="${ti[0]++}"/>
	                            </p>
	                        </g:if>
	                        <g:if test="${!routeInstance.IsObservationSignUsed()}">
                                <g:editRouteObservations route="${routeInstance}" ti="${ti}"/>
	                        </g:if>
                        </fieldset>
                        <input type="hidden" name="id" value="${routeInstance?.id}" />
                        <input type="hidden" name="version" value="${routeInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}"  tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>