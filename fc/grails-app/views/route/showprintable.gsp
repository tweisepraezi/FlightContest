<html>
    <head>
        <style type="text/css">
            @page {
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
                            <tbody>
                                <g:each var="coordRouteInstance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}" status="j" >
                                    <tr>
                                        <g:if test="${j==0}">
                                            <td width="25%">${message(code:'fc.coordroute.list')}</td>
                                        </g:if>
                                        <g:else>
                                            <td width="25%"></td>
                                        </g:else>
                                        <td width="25%">${coordRouteInstance.titleCode()}<br/></td>
                                        <td>${coordRouteInstance.namePrintable()}<br/></td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <br/>
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
	                                <g:each var="routeLegCoordInstance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}" status="j" >
	                                    <tr>
	                                        <g:if test="${j==0}">
	                                            <td width="25%">${message(code:'fc.routelegcoord.list')}</td>
	                                        </g:if>
	                                        <g:else>
	                                            <td width="25%"></td>
	                                        </g:else>
	                                        <td width="25%">${routeLegCoordInstance.title}<br/></td>
	                                        <td>${routeLegCoordInstance.testName()}<br/></td>
	                                    </tr>
	                                </g:each>
	                            </tbody>
	                        </table>
	                    </div>
	                    <br/>
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
	                                <g:each var="routeLetTestInstance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}" status="j" >
	                                    <tr>
	                                        <g:if test="${j==0}">
	                                            <td width="25%">${message(code:'fc.routelegtest.list')}</td>
	                                        </g:if>
	                                        <g:else>
	                                            <td width="25%"></td>
	                                        </g:else>
	                                        <td width="25%">${routeLetTestInstance.title}<br/></td>
	                                        <td>${routeLetTestInstance.testName()}<br/></td>
	                                    </tr>
	                                </g:each>
	                            </tbody>
	                        </table>
	                	</div>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>