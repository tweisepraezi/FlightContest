<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                @top-left {
                    content: "${routeInstance.name()} - ${message(code:'fc.scale')} 1:${routeInstance.contest.mapScale}"
                }
                @top-right {
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${routeInstance.name()}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${routeInstance.name()}</h2>
                <div>
                    <g:form>
                        <table class="routecoords">
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.tpname')}</th>
                                    <th>${message(code:'fc.aflos')}</th>
                                    <th>${message(code:'fc.coordroute.list')}</th>
                                    <th>${message(code:'fc.altitude')}</th>
                                    <th>${message(code:'fc.gatewidth.short')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                                    <tr>
                                        <td class="tpname">${coordroute_instance.titlePrintCode()}</td>
                                        <td class="aflosname">${coordroute_instance.mark}</td>
                                        <td class="coords">${coordroute_instance.namePrintable(true)}</td>
                                        <td class="altitude">${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
                                        <td class="gatewidth">${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>