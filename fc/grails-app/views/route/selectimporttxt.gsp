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
                            <g:if test="${params.importEnrouteData=="true"}" >
                                <p>${message(code:'fc.route.signfileimport.info1',args:[RouteFileTools.ENROUTE_SIGN_EXTENSIONS])}</p>
                                <input type="file" size="80" accept="${RouteFileTools.ENROUTE_SIGN_EXTENSIONS}" name="txtfile" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:else>
                                <p>${message(code:'fc.route.signfileimport.info1',args:[RouteFileTools.TURNPOINT_EXTENSIONS])}</p>
                                <input type="file" size="80" accept="${RouteFileTools.TURNPOINT_EXTENSIONS}" name="txtfile" tabIndex="${ti[0]++}"/>
                            </g:else>
                        </div>
                        <g:if test="${params.importEnrouteData=="true"}" >
                            <div>
                                <br/>
                                <label>${message(code:'fc.route.signfileimport.foldername')}:</label>
                                <br/>
                                <input type="text" id="foldername" name="foldername" value="" tabIndex="${ti[0]++}"/>
                            </div>
                            <div>
                                <br/>
                                <label>${message(code:'fc.route.signfileimport.namepraefix')}:</label>
                                <br/>
                                <input type="text" id="namepraefix" name="namepraefix" value="" tabIndex="${ti[0]++}"/>
                            </div>
                        </g:if>
                        <div>
                            <br/>
                            <p>${message(code:'fc.route.fileimport.info.txt')}<br/>${HTMLFilter.FilterParam(params.lineContent)}</p>
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