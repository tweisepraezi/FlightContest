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
                                    <th>${message(code:'fc.aflos')}</th>
                                    <th>${message(code:'fc.coordroute.list')}</th>
                                    <th>${message(code:'fc.altitude')}</th>
                                    <th>${message(code:'fc.gatewidth.short')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                                    <tr>
                                        <td width="10%">${coordroute_instance.titleCode()}</td>
                                        <td width="10%">${coordroute_instance.mark}</td>
                                        <td>${coordroute_instance.namePrintable(true)}</td>
                                        <td>${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
                                        <td>${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <br/>
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <g:set var="total_distance" value="${new BigDecimal(0)}" />
	                            <thead>
	                                <tr>
	                                    <th colspan="2">${message(code:'fc.routelegcoord.list')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <g:each var="routelegcoord_instance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}">
                                        <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routelegcoord_instance.testDistance())}" />
                                        <g:set var="course_change" value="${AviationMath.courseChange(routelegcoord_instance.turnTrueTrack,routelegcoord_instance.testTrueTrack())}"/>
	                                    <g:if test="${course_change.abs() >= 90}">
	                                        <tr>
	                                            <td class="center" align="center" colspan="2">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}</td>
	                                        </tr>
	                                    </g:if>
	                                    <tr>
	                                        <td width="20%">${routelegcoord_instance.title}</td>
	                                        <td>${routelegcoord_instance.testName()}</td>
	                                    </tr>
	                                </g:each>
	                            </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="2">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                                    </tr>
                                </tfoot>
	                        </table>
	                    </div>
	                    <br/>
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <g:set var="total_distance" value="${new BigDecimal(0)}" />
                                <thead>
                                    <tr>
                                        <th colspan="2">${message(code:'fc.routelegtest.list')}</th>
                                    </tr>
                                </thead>
	                            <tbody>
	                                <g:each var="routelettest_instance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                        <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routelettest_instance.testDistance())}" />
                                        <g:set var="course_change" value="${AviationMath.courseChange(routelettest_instance.turnTrueTrack,routelettest_instance.testTrueTrack())}"/>
                                        <g:if test="${course_change.abs() >= 90}">
                                            <tr>
                                                <td class="center" align="center" colspan="2">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}</td>
                                            </tr>
                                        </g:if>
	                                    <tr>
	                                        <td width="20%">${routelettest_instance.title}</td>
	                                        <td>${routelettest_instance.testName()}</td>
	                                    </tr>
	                                </g:each>
	                            </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="2">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                                    </tr>
                                </tfoot>
	                        </table>
	                	</div>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>