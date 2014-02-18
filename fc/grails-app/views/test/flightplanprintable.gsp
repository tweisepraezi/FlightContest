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
                @top-center {
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}</h2>
                <h3>${testInstance.task.name()} (${message(code:'fc.test.timetable.version')} ${testInstance.task.GetTimeTableVersion()}<g:if test="${testInstance.task.GetTimeTableVersion() != testInstance.timetableVersion}">, ${message(code:'fc.test.timetable.unchangedversion')} ${testInstance.timetableVersion}</g:if>)</h3>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if> <g:else>
                                         <td/>
                                    </g:else>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if> <g:else>
                                         <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.taskAircraft}">
                                            ${testInstance.taskAircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.taskAircraft}">
		                                    ${testInstance.taskAircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.wind')}:
	                                    <g:if test="${testInstance.flighttestwind}">
	                                        <g:windtext var="${testInstance.flighttestwind.wind}" />
	                                    </g:if> <g:else>
	                                        ${message(code:'fc.noassigned')}                                    
	                                    </g:else>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
	                                    <g:if test="${testInstance.task.planningTestDuration == 0}">
	                                        ${message(code:'fc.test.planning.publish')}:
	                                    </g:if>
	                                    <g:else>
	                                        ${message(code:'fc.test.planning')}:
	                                    </g:else>
	                                    <g:if test="${testInstance.timeCalculated}">
                                            <g:if test="${testInstance.task.planningTestDuration > 0}">
	                                           ${testInstance.testingTime?.format('HH:mm')} - ${testInstance.endTestingTime?.format('HH:mm')}
	                                        </g:if>
	                                        <g:else>
	                                           ${testInstance.testingTime?.format('HH:mm')}
	                                        </g:else>
	                                    </g:if> <g:else>
	                                        ${message(code:'fc.nocalculated')}
	                                    </g:else>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegFlight.countByTest(testInstance)}" >
                            <br/>
                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <thead>
                                    <tr>
                                        <th>${message(code:'fc.number')}</th>
                                        <th>${message(code:'fc.distance')}</th>
                                        <th>${message(code:'fc.truetrack')}</th>
                                        <th>${message(code:'fc.trueheading')}</th>
                                        <th>${message(code:'fc.groundspeed')}</th>
                                        <th>${message(code:'fc.legtime')}</th>
                                        <th>${message(code:'fc.tpname')}</th>
                                        <th>${message(code:'fc.tptime')}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td>${message(code:CoordType.TO.code)}</td>
                                        <g:if test="${testInstance.timeCalculated}">
                                            <g:if test="${testInstance.takeoffTimeWarning}">
                                                <td class="errors">${FcMath.TimeStr(testInstance.takeoffTime)} !</td>
                                            </g:if> <g:else>
                                                <td>${FcMath.TimeStr(testInstance.takeoffTime)}</td>
                                            </g:else> 
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.nocalculated')}</td>
                                        </g:else>
                                    </tr>
                                    <tr>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td>${message(code:CoordType.SP.code)}</td>
                                        <g:if test="${testInstance.timeCalculated}">
                                            <td>${FcMath.TimeStr(testInstance.startTime)}</td>
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.nocalculated')}</td>
                                        </g:else>
                                    </tr>

                                    <g:set var="legNo" value="${new Integer(0)}" />
                                    <g:set var="legNum" value="${TestLegFlight.countByTest(testInstance)}" />
                                    <g:set var="tptime" value="${testInstance.startTime}" />
                                    <g:set var="totalDistance" value="${new BigDecimal(0)}" />
                                    <g:each var="testLegFlightInstance" in="${TestLegFlight.findAllByTest(testInstance,[sort:"id"])}">
                                        <g:set var="legNo" value="${legNo+1}" />
                                        <g:set var="tptime" value="${testLegFlightInstance.AddPlanLegTime(tptime)}" />
                                        <g:set var="totalDistance" value="${FcMath.AddDistance(totalDistance,testLegFlightInstance.planTestDistance)}" />
                                        <g:if test="${testLegFlightInstance.planProcedureTurn}">
                                            <tr>
                                                <td class="center" align="center" colspan="8">${message(code:'fc.procedureturn')} (${testLegFlightInstance.test.task.procedureTurnDuration}${message(code:'fc.time.min')})</td>
                                            </tr>
                                        </g:if>
                                        <tr>
                                            <td>${legNo}</td>
                                            <td>${FcMath.DistanceStr(testLegFlightInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                            <td>${FcMath.GradStr(testLegFlightInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                            <td>${FcMath.GradStr(testLegFlightInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                            <td>${FcMath.SpeedStr_Flight(testLegFlightInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                            <td>${testLegFlightInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                            <g:if test="${legNo==legNum}">
                                                <td>${message(code:CoordType.FP.code)}</td>
                                            </g:if>
                                            <g:else>
                                                <td>${message(code:CoordType.TP.code)}${legNo}</td>
                                            </g:else>
                                            <td>${FcMath.TimeStr(tptime)}</td>
                                        </tr>
                                    </g:each>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td/>
                                        <td colspan="3">${FcMath.DistanceStr(totalDistance)}${message(code:'fc.mile')} ${message(code:'fc.distance.total')}</td>
                                        <td colspan="3" align="right">${message(code:'fc.test.landing.latest')}:</td>
                                        <td>${FcMath.TimeStr(testInstance.maxLandingTime)}</td>
                                    </tr>
                                </tfoot>
                            </table>
                            <br/>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>