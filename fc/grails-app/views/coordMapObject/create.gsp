<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.mapobjects.add')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.mapobjects.add')}</h2>
                <g:hasErrors bean="${coordMapObjectInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordMapObjectInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="fromrouteparam" value="['routeid':params.routeid]"/>
                    <g:form method="post" params="${fromrouteparam}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <g:editCoordMapObject coordEnroute="${coordMapObjectInstance}" create="${true}" ti="${ti}"/>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="saveadd" value="${message(code:'fc.coordroute.mapobjects.saveadd')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>