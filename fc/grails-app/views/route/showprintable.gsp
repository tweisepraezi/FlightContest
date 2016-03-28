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
                    content: "${routeInstance.printName()} - ${message(code:'fc.scale')} 1:${routeInstance.contest.mapScale}"
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
        <title>${routeInstance.printName()}</title>
    </head>
    <body>
        <h2>${routeInstance.printName()}</h2>
        <g:form>
            <table class="routecoords">
                <thead>
                    <tr>
                        <th>${message(code:'fc.tpname')}</th>
                        <g:if test="${routeInstance.mark}">
                            <th>${message(code:'fc.aflos')}</th>
                        </g:if>
                        <th>${message(code:'fc.coordroute.list')}</th>
                        <th>${message(code:'fc.altitude')}</th>
                        <th>${message(code:'fc.gatewidth.short')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                        <tr>
                            <td class="tpname">${coordroute_instance.titlePrintCode()}</td>
                            <g:if test="${routeInstance.mark}">
                                <td class="aflosname">${coordroute_instance.mark}</td>
                            </g:if>
                            <td class="coords">${coordroute_instance.namePrintable(true,true)}</td>
                            <td class="altitude">${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
                            <td class="gatewidth">${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <br/>
           	<div style="page-break-inside:avoid">
             <table class="routelegs">
                 <g:set var="total_distance" value="${new BigDecimal(0)}" />
                 <thead>
                     <tr class="name">
                         <th colspan="2">${message(code:'fc.routelegcoord.list')}</th>
                     </tr>
                 </thead>
                 <tbody>
                     <g:each var="routelegcoord_instance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}">
                         <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routelegcoord_instance.testDistance())}" />
                         <g:set var="course_change" value="${AviationMath.courseChange(routelegcoord_instance.turnTrueTrack,routelegcoord_instance.testTrueTrack())}"/>
                         <g:if test="${course_change.abs() >= 90}">
                             <tr class="coursechange">
                                 <td class="center" align="center" colspan="2">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}<g:if test="${routelegcoord_instance.IsProcedureTurn()}"> (${message(code:'fc.procedureturn')})</g:if></td>
                             </tr>
                         </g:if>
                         <tr class="value">
                             <td class="from2tp">${routelegcoord_instance.GetPrintTitle()}</td>
                             <td class="trackdistance">${routelegcoord_instance.testPrintName()}</td>
                         </tr>
                     </g:each>
                 </tbody>
                 <tfoot>
                     <tr class="summary">
                         <td colspan="2">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                     </tr>
                 </tfoot>
             </table>
         </div>
         <br/>
           	<div style="page-break-inside:avoid">
              <table class="routelegs">
                 <g:set var="total_distance" value="${new BigDecimal(0)}" />
                 <thead>
                     <tr class="name">
                         <th colspan="2">${message(code:'fc.routelegtest.list')}</th>
                     </tr>
                 </thead>
                 <tbody>
                     <g:each var="routelegtest_instance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}">
                         <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routelegtest_instance.testDistance())}" />
                         <g:set var="course_change" value="${AviationMath.courseChange(routelegtest_instance.turnTrueTrack,routelegtest_instance.testTrueTrack())}"/>
                         <g:if test="${course_change.abs() >= 90}">
                             <tr class="coursechange">
                                 <td class="center" align="center" colspan="2">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}<g:if test="${routelegtest_instance.IsProcedureTurn()}"> (${message(code:'fc.procedureturn')})</g:if></td>
                             </tr>
                         </g:if>
                         <tr class="value">
                             <td class="from2tp">${routelegtest_instance.GetPrintTitle()}</td>
                             <td class="trackdistance">${routelegtest_instance.testPrintName()}</td>
                         </tr>
                     </g:each>
                 </tbody>
                 <tfoot>
                     <tr class="summary">
                         <td colspan="2">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                     </tr>
                 </tfoot>
              </table>
     	    </div>
        </g:form>
    </body>
</html>