<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestmap.importgeodata')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestmap.importgeodata')}</h2>
                <div class="block" id="forms">
                    <g:uploadForm action="loadgeodata">
                        <g:set var="ti" value="${[]+1}"/>
                        <div>
                            <p>${message(code:'fc.contestmap.importgeodata.info',args:[GeoDataService.DATA_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${GeoDataService.DATA_EXTENSIONS}" name="loadgeofile" tabIndex="${ti[0]++}"/>
                        </div>
                        <br/>
                        <div>
                            <input type="submit" name="importgeodata" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        </div>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>