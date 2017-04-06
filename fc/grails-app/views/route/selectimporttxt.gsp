<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:params.titlecode)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:params.titlecode)}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post">
                        <g:set var="ti" value="${[]+1}"/>
                        <div>
                            <p>${message(code:'fc.route.signfileimport.info1',args:[RouteFileTools.TXT_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${RouteFileTools.TXT_EXTENSIONS}" name="txtfile" tabIndex="${ti[0]++}"/>
                        </div>
                        <div>
                            <br/>
                            <p>${message(code:'fc.route.signfileimport.info2')}<br/>${params.lineContent}</p>
                        </div>
                        <br/>
                        <input type="hidden" name="routeid" value="${params.routeid}"/>
                        <input type="hidden" name="importSign" value="${params.importSign}"/>
                        <g:actionSubmit action="importenroutesign" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>