<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.create')}</h2>
                <g:hasErrors bean="${routeInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${routeInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="newparams" value="['contestid':params.contestid]"/>
                    <g:form method="post" params="${newparams}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:routeInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <g:editRouteUseProcedureTurns route="${routeInstance}" ti="${ti}"/>
                            <g:if test="${BootStrap.global.IsLiveTrackingPossible()}">
                                <g:editRouteLiveTrackingScorecard route="${routeInstance}" ti="${ti}"/>
	                        </g:if>
                            <fieldset>
                                <p>
                                    <label>${message(code:'fc.scale')}*:</label>
                                    <br/>
                                    <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:routeInstance,field:'mapScale')}" tabIndex="${ti[0]++}"/>
                                </p>
                                <p>
                                    <label>${message(code:'fc.altitude.aboveground')}*:</label>
                                    <br/>
                                    <input type="text" id="altitudeAboveGround" name="altitudeAboveGround" value="${fieldValue(bean:routeInstance,field:'altitudeAboveGround')}" tabIndex="${ti[0]++}"/>
                                </p>
                            </fieldset>
                            <a href="/fc/docs/help_${session.showLanguage}.html#route-planning-photos-canvas" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                            <g:editRouteObservations route="${routeInstance}" ti="${ti}"/>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="save_noobservations" value="${message(code:'fc.route.create.noobservations')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>