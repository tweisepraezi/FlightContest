<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${routeInstance.name()} - ${message(code:'fc.scale')} 1:${routeInstance.contest.mapScale} - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${routeInstance.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${routeInstance.name()}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.tpname')}</th>
                                    <th>${message(code:'fc.coordroute.list')}</th>
                                    <th align="center">${message(code:'fc.altitude.long')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                                    <g:if test="${coordroute_instance.type != CoordType.SECRET}">
	                                    <tr>
	                                        <td width="10%">${coordroute_instance.titleCode()}</td>
	                                        <td>${coordroute_instance.namePrintable(false)}</td>
	                                        <td width="10%" align="center">${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
	                                    </tr>
                                    </g:if>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>