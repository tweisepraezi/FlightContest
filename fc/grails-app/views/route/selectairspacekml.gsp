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
                            <p>${message(code:'fc.contestmap.contestmapairspaces.info1',args:[RouteFileTools.AIRSPACE_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${RouteFileTools.AIRSPACE_EXTENSIONS}" name="kmzfile" tabIndex="${ti[0]++}"/>
                        </div>
                        <g:if test="${params.showFolderName=="true"}" >
                            <div>
                                <br/>
                                <label>${message(code:'fc.route.fileimport.foldername')}:</label>
                                <br/>
                                <input type="text" id="foldername" name="foldername" value="${params.folderName}" tabIndex="${ti[0]++}"/>
                            </div>
                        </g:if>
                        <br/>
                        <input type="hidden" name="routeid" value="${params.routeid}"/>
                        <g:actionSubmit action="importairspacekmz" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>