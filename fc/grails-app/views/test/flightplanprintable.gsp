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
                                    <td>${testInstance?.contestdaytask?.contestday.name()} - ${testInstance?.contestdaytask.name()}</td>
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
                                    <td>${testInstance.crew.aircraft.type}</td>
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
                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
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
                            <thead>
                                <tr>
                                    <th colspan="2">${message(code:'fc.test.timetable')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.planning')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.testingTime?.format('HH:mm')} - ${testInstance.endTestingTime?.format('HH:mm')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.arrival')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.arrivalTime?.format('HH:mm')}</td>
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
                                        <td>${RouteCoordType.TO.title}</td>
                                        <g:if test="${testInstance.timeCalculated}">
                                            <g:if test="${testInstance.takeoffTimeWarning}">
                                                <td class="errors">${testInstance.takeoffTime?.format('HH:mm:ss')} !</td>
                                            </g:if> <g:else>
                                                <td>${testInstance.takeoffTime?.format('HH:mm:ss')}</td>
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
                                        <td>${RouteCoordType.SP.title}</td>
                                        <g:if test="${testInstance.timeCalculated}">
                                            <td>${testInstance.startTime?.format('HH:mm:ss')}</td>
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
                                                <td class="center" align="center" colspan="8">${message(code:'fc.procedureturn')} (${testLegFlightInstance.test.contestdaytask.procedureTurnDuration}${message(code:'fc.time.min')})</td>
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
                                                <td>${RouteCoordType.FP.title}</td>
                                            </g:if>
                                            <g:else>
                                                <td>${RouteCoordType.TP.title}${legNo}</td>
                                            </g:else>
                                            <td>${tptime.format('HH:mm:ss')}</td>
                                        </tr>
                                    </g:each>
                                </tbody>
                            </table>
                            <br/>
                            <table>
                                <tbody>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.distance.total')}:</td>
                                        <td>${FcMath.DistanceStr(totalDistance)}${message(code:'fc.mile')}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>