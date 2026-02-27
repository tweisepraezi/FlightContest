<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.fcfileimport')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.fcfileimport')}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post">
                        <g:set var="ti" value="${[]+1}"/>
                        <p class="warning">${message(code:'fc.route.fcfileimport.info.extern',args:[RouteFileTools.FC_ROUTE_EXTENSIONS])}</p>
                        <div>
                            <p>${message(code:'fc.route.fcfileimport.info',args:[RouteFileTools.FC_ROUTE_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${RouteFileTools.FC_ROUTE_EXTENSIONS}" name="routefile" tabIndex="${ti[0]++}"/>
                        </div>
                        <br/>
                        <input type="hidden" id="corridorWidth" name="corridorWidth" value="0.0" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="importfcroute2" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>