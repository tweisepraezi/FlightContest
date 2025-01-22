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
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${routeInstance.printName()} - ${message(code:'fc.scale')} 1:${routeInstance.mapScale}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${routeInstance.printName()}</title>
    </head>
    <body>
        <h2>${routeInstance.printName()}</h2>
        <g:form>
            <g:if test="${routeInstance.corridorWidth}">
                <p>${message(code:'fc.corridorwidth')}: ${FcMath.DistanceStr(routeInstance.corridorWidth)}${message(code:'fc.mile')}</p>
            </g:if>
            <table class="routecoords">
                <thead>
                    <tr>
                        <th>${message(code:'fc.tpname')}</th>
                        <th>${message(code:'fc.coordroute.list')}</th>
                        <th>${message(code:'fc.altitude')}</th>
                        <th>${message(code:'fc.gatewidth.short')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                        <tr>
                            <td class="tpname">${coordroute_instance.titlePrintCode()}</td>
                            <td class="coords">${coordroute_instance.namePrintable(true,false)}</td>
                            <td class="altitude"><g:getAltitudeValues route="${routeInstance}" coordresult="${coordroute_instance}"/></td>
                            <td class="gatewidth">${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:form>
    </body>
</html>