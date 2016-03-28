<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.fileimport')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.fileimport')}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post">
                        <div>
                            <p>${message(code:'fc.route.fileimport.info',args:[RouteFileTools.ROUTE_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${RouteFileTools.ROUTE_EXTENSIONS}" name="routefile" tabIndex="1"/>
                        </div>
                        <br/>
                        <g:actionSubmit action="importfileroute2" value="${message(code:'fc.import')}" tabIndex="11"/>
                        <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="12"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>