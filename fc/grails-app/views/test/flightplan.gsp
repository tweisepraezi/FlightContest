<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()} (${message(code:'fc.test.timetable')} ${message(code:'fc.version')} ${testInstance.timetableVersion})</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['flightplanReturnAction':flightplanReturnAction,'flightplanReturnController':flightplanReturnController,'flightplanReturnID':flightplanReturnID]}">
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
                                </tr>
		                    	<g:if test="${testInstance.crew.team}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.crew.team')}:</td>
	                                    <td><g:team var="${testInstance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
	                                </tr>
    		                    </g:if>
		                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
	                                <tr>
	                                	<td class="detailtitle">${message(code:'fc.crew.resultclass')}:</td>
	                                	<td><g:resultclass var="${testInstance.crew.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
    		                        </tr>
    		                    </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
	                                    <td>${testInstance.crew.aircraft.type}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:route var="${testInstance.flighttestwind.flighttest.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:windtext var="${testInstance.flighttestwind.wind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <g:if test="${testInstance.task.planningTestDuration == 0}">
                                        <td class="detailtitle">${message(code:'fc.test.planning.publish')}:</th>
                                    </g:if>
                                    <g:else>
                                        <td class="detailtitle">${message(code:'fc.test.planning')}:</td>
                                    </g:else>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <g:if test="${testInstance.task.planningTestDuration > 0}">
                                            <td>${testInstance.testingTime?.format('HH:mm')} - ${testInstance.endTestingTime?.format('HH:mm')}</td>
                                        </g:if>
                                        <g:else>
                                            <td>${testInstance.testingTime?.format('HH:mm')}</td>
                                        </g:else>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegFlight.countByTest(testInstance)}" >
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th colspan="8" class="table-head">${message(code:'fc.testlegflight.list')}</th>
                                        </tr>
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
                                                    <td class="center" colspan="8">${message(code:'fc.procedureturn')} (${testLegFlightInstance.test.task.procedureTurnDuration}${message(code:'fc.time.min')})</td>
                                                </tr>
                                            </g:if>
                                            <tr>
                                                <td>${legNo}</td>
                                                <td>${FcMath.DistanceStr(testLegFlightInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(testLegFlightInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(testLegFlightInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr(testLegFlightInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
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
                                            <td colspan="4">${FcMath.DistanceStr(totalDistance)}${message(code:'fc.mile')} ${message(code:'fc.distance.total')}</td>
                                            <td colspan="2" align="right">${message(code:'fc.maxlandingtime')}:</td>
                                            <td>${FcMath.TimeStr(testInstance.maxLandingTime)}</td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </g:if>
                        <input type="hidden" name="id" value="${testInstance?.id}"/>
                        <g:actionSubmit action="printflightplan" value="${message(code:'fc.print')}" tabIndex="1"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="2"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>