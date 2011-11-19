<html>
    <head>
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
                            <tbody>
                                <g:each var="coordRouteInstance" in="${CoordRoute.findAllByRoute(routeInstance)}" status="j" >
                                    <tr>
                                        <g:if test="${j==0}">
                                            <td>${message(code:'fc.coordroute.list')}</td>
                                        </g:if>
                                        <g:else>
                                            <td/>
                                        </g:else>
                                        <td>${coordRouteInstance.titleWithRatio()}<br/></td>
                                        <td>${coordRouteInstance.name()}<br/></td>
                                    </tr>
                                </g:each>
                                <tr>
                                    <td colspan="3"><br/></td>
                                </tr>
                                <g:each var="routeLegCoordInstance" in="${RouteLegCoord.findAllByRoute(routeInstance)}" status="j" >
                                    <tr>
                                        <g:if test="${j==0}">
                                            <td>${message(code:'fc.routelegcoord.list')}</td>
                                        </g:if>
                                        <g:else>
                                            <td/>
                                        </g:else>
                                        <td>${routeLegCoordInstance.title}<br/></td>
                                        <td>${routeLegCoordInstance.coordName()}<br/></td>
                                    </tr>
                                </g:each>
                                <tr>
                                    <td colspan="3"><br/></td>
                                </tr>
                                <g:each var="routeLetTestInstance" in="${RouteLegTest.findAllByRoute(routeInstance)}" status="j" >
                                    <tr>
                                        <g:if test="${j==0}">
                                            <td>${message(code:'fc.routelegtest.list')}</td>
                                        </g:if>
                                        <g:else>
                                            <td/>
                                        </g:else>
                                        <td>${routeLetTestInstance.title}<br/></td>
                                        <td>${routeLetTestInstance.testName()}<br/></td>
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