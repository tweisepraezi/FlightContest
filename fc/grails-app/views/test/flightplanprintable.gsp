<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.flightplan')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.flightplan')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${testInstance.task.name()} (${message(code:'fc.test.timetable')} ${message(code:'fc.test.timetable.version')} ${testInstance.timetableVersion})</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td>${testInstance.crew.name}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.country')}:</td>
                                    <td>${testInstance.crew.country}</td>
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
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td>${testInstance.crew.aircraft.registration}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:windtext var="${testInstance.flighttestwind.wind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>                                    
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.planning')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.testingTime?.format('HH:mm')} - ${testInstance.endTestingTime?.format('HH:mm')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegFlight.countByTest(testInstance)}" >
                            <br/>
                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <thead>
                                    <tr>
                                        <th colspan="8">${message(code:'fc.testlegflight.list')}</th>
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
                                    <g:each var="testLegFlightInstance" in="${TestLegFlight.findAllByTest(testInstance)}">
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
                                        <td colspan="3">${FcMath.DistanceStr(totalDistance)}${message(code:'fc.mile')} ${message(code:'fc.distance.total')}</td>
                                        <td colspan="3" align="right">${message(code:'fc.maxlandingtime')}:</td>
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