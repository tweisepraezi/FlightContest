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
                        <g:set var="route_used" value="${routeInstance.Used()}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${routeInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:routeInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <g:if test="${!route_used && (routeInstance.contest.anrFlying || routeInstance.corridorWidth)}">
                                <fieldset>
                                    <div>
                                        <label>${message(code:'fc.corridorwidth')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="corridorWidth" name="corridorWidth" value="${fieldValue(bean:routeInstance,field:'corridorWidth')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                </fieldset>
                            </g:if>
                            <fieldset>
                                <p>
                                    <label>${message(code:'fc.scale')}*:</label>
                                    <br/>
                                    <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:routeInstance,field:'mapScale')}" tabIndex="${ti[0]++}"/>
                                </p>
                                <g:editRouteDefaultOnlineMap route="${routeInstance}" ti="${ti}"/>
                                <p>
                                    <label>${message(code:'fc.altitude.aboveground')}*:</label>
                                    <br/>
                                    <input type="text" id="altitudeAboveGround" name="altitudeAboveGround" value="${fieldValue(bean:routeInstance,field:'altitudeAboveGround')}" tabIndex="${ti[0]++}"/>
                                </p>
                            </fieldset>
	                        <g:if test="${!route_used && (!routeInstance.corridorWidth || routeInstance.useProcedureTurns)}">
                                <g:editRouteUseProcedureTurns route="${routeInstance}" ti="${ti}"/>
	                        </g:if>
	                        <g:if test="${!routeInstance.IsObservationSignUsed() && !routeInstance.corridorWidth}">
                                <a href="/fc/docs/help_${session.showLanguage}.html#route-planning-photos-canvas" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <g:editRouteObservations route="${routeInstance}" ti="${ti}"/>
	                        </g:if>
                            <g:editRouteSpecialSettings route="${routeInstance}" ti="${ti}"/>
                            <g:if test="${BootStrap.global.IsLiveTrackingPossible()}">
                                <g:editRouteLiveTrackingScorecard route="${routeInstance}" ti="${ti}"/>
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