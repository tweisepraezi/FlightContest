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
                    content: "<g:if test="${params.results=='yes'}">${message(code:'fc.test.planningtask.withresults')}</g:if><g:else>${message(code:'fc.test.planningtask')}</g:else> ${testInstance.GetStartNum()}"
                }
                @top-right {
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-left {
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title><g:if test="${params.results=='yes'}">${message(code:'fc.test.planningtask.withresults')}</g:if><g:else>${message(code:'fc.test.planningtask')}</g:else> ${testInstance.GetStartNum()}</title>
    </head>
    <body>
        <div>
            <div>
                <h2><g:if test="${params.results=='yes'}">${message(code:'fc.test.planningtask.withresults')}</g:if><g:else>${message(code:'fc.test.planningtask')}</g:else> ${testInstance.GetStartNum()}</h2>
                <h3>${testInstance?.task.name()}</h3>
                <div>
                    <g:form>
                        <g:crewTestPrintable t="${testInstance}"/>
                        <br/>
                        <table class="info">
                            <tbody>
                                <tr class="wind">
                                    <td class="title">${message(code:'fc.wind')}:</td>
                                    <td class="value">
	                                    <g:if test="${testInstance.planningtesttask}">
	                                        <g:windtext var="${testInstance.planningtesttask.wind}" />
	                                    </g:if> <g:else>
	                                        ${message(code:'fc.noassigned')}                                    
	                                    </g:else>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
                            <br/>
                            <table class="planningtasklist">
                                <thead>
                                    <tr class="valuename">
                                        <th>${message(code:'fc.distance')}</th>
                                        <th>${message(code:'fc.truetrack')}</th>
                                        <th>${message(code:'fc.trueheading')}</th>
                                        <th>${message(code:'fc.groundspeed')}</th>
                                        <th>${message(code:'fc.legtime')}</th>
                                        <th>${message(code:'fc.tpname')}</th>
                                    </tr>
                                    <tr class="unit">
                                        <th>[${message(code:'fc.mile')}]</th>
                                        <th>[${message(code:'fc.grad')}]</th>
                                        <th>[${message(code:'fc.grad')}]</th>
                                        <th>[${message(code:'fc.knot')}]</th>
                                        <g:if test="${params.results=='yes'}">
                                            <th>[${message(code:'fc.time.hminsec2')}]</th>
                                        </g:if>
                                        <g:else>
                                            <th>[${message(code:'fc.time.minsec')}]</th>
                                        </g:else>
                                        <th/>
                                    </tr>
                                </thead>
                                <tbody>
                                     <tr class="value" id="${message(code:CoordType.SP.code)}">
                                         <td class="distance">-</td>
                                         <td class="truetrack">-</td>
                                         <td class="trueheading">-</td>
                                         <td class="groundspeed">-</td>
                                         <td class="legtime">-</td>
                                         <td class="tpname">${message(code:CoordType.SP.code)}</td>
                                     </tr>
                                    <g:each var="testlegplanning_instance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                                        <g:if test="${!testlegplanning_instance.test.IsPlanningTestDistanceMeasure()}">
                                            <g:set var="test_distance" value="${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}" />
                                        </g:if>
                                        <g:if test="${!testlegplanning_instance.test.IsPlanningTestDirectionMeasure()}">
                                            <g:set var="test_direction" value="${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}" />
                                        </g:if>
                                        <tr class="value" id="${testlegplanning_instance.coordTitle.titleCode()}">
                                            <g:if test="${!testlegplanning_instance.noPlanningTest}">
	                                            <g:if test="${params.results=='yes'}">
	                                                <td class="distance">${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}</td>
	                                                <td class="truetrack">${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}</td>
	                                                <td class="trueheading">${FcMath.GradStr(testlegplanning_instance.planTrueHeading)}</td>
	                                                <td class="groundspeed">${FcMath.SpeedStr_Planning(testlegplanning_instance.planGroundSpeed)}</td>
	                                                <td class="legtime">${testlegplanning_instance.planLegTimeStr()}</td>
                                                    <td class="tpname">${testlegplanning_instance.coordTitle.titleCode()}</td>
	                                            </g:if>
	                                            <g:else>
	                                                <td class="distance">${test_distance}</td>
	                                                <td class="truetrack">${test_direction}</td>
	                                                <td class="trueheading"/>
	                                                <td class="groundspeed"/>
	                                                <td class="legtime"/>
                                                    <td class="tpname">${testlegplanning_instance.coordTitle.titlePrintCode()}</td>
	                                            </g:else>
	                                         </g:if>
	                                         <g:else>
                                                <td class="distance">-</td>
                                                <td class="truetrack">-</td>
                                                <td class="trueheading">-</td>
                                                <td class="groundspeed">-</td>
                                                <td class="legtime">-</td>
                                                <td class="tpname">${testlegplanning_instance.coordTitle.titlePrintCode()}</td>
	                                         </g:else>
                                        </tr>
                                    </g:each>
                                </tbody>
                            </table>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>