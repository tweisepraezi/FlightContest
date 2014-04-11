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

                                    <g:set var="leg_no" value="${new Integer(0)}" />
                                    <g:set var="leg_firstvalue" value="${true}"/>
                                    <g:set var="leg_procedureturn" value="${false}"/>
                                    <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                                    <g:set var="leg_plantruetrack" value="${new BigDecimal(0)}" />
                                    <g:set var="leg_plantrueheading" value="${new BigDecimal(0)}" />
                                    <g:set var="leg_plangroundspeed" value="${new BigDecimal(0)}" />
                                    <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                                    <g:set var="leg_time" value="${testInstance.startTime}" />
                                    <g:set var="total_distance" value="${new BigDecimal(0)}" />
                                    
                                    <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(testInstance,[sort:"id"])}">
                                        <g:set var="leg_distance" value="${FcMath.AddDistance(leg_distance,testlegflight_instance.planTestDistance)}" />
                                        <g:if test="${leg_firstvalue}">
                                            <g:set var="leg_procedureturn" value="${testlegflight_instance.planProcedureTurn}"/>
                                            <g:set var="leg_plantruetrack" value="${testlegflight_instance.planTrueTrack}" />
                                            <g:set var="leg_plantrueheading" value="${testlegflight_instance.planTrueHeading}" />
                                            <g:set var="leg_plangroundspeed" value="${testlegflight_instance.planGroundSpeed}" />
                                        </g:if>
                                        <g:set var="leg_duration" value="${testlegflight_instance.AddPlanLegTime(leg_duration,leg_time)}" />
                                        <g:set var="leg_time" value="${testlegflight_instance.AddPlanLegTime(leg_time)}" />
                                        <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,testlegflight_instance.planTestDistance)}" />
                                        <g:if test="${testlegflight_instance.coordTitle.type != CoordType.SECRET}">
                                            <g:set var="leg_no" value="${leg_no+1}" />
                                            <g:if test="${leg_procedureturn}">
                                                <tr>
                                                    <td class="center" align="center" colspan="8">${message(code:'fc.procedureturn')} (${testlegflight_instance.test.task.procedureTurnDuration}${message(code:'fc.time.min')})</td>
                                                </tr>
                                            </g:if>
                                            <tr>
                                                <td>${leg_no}</td>
                                                <td>${FcMath.DistanceStr(leg_distance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(leg_plantruetrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(leg_plantrueheading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr_Flight(leg_plangroundspeed)}${message(code:'fc.knot')}</td>
                                                <td>${FcMath.TimeStr(leg_duration)}${message(code:'fc.time.h')}</td>
                                                <td>${testlegflight_instance.coordTitle.titlePrintCode()}</td>
                                                <td>${FcMath.TimeStr(leg_time)}</td>
                                            </tr>
                                            <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                                            <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                                            <g:set var="leg_firstvalue" value="${true}"/>
                                        </g:if>
                                        <g:else>
                                            <g:set var="leg_firstvalue" value="${false}"/>
                                        </g:else>
                                        </g:each>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td/>
                                        <td colspan="3">${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')} ${message(code:'fc.distance.total')}</td>
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