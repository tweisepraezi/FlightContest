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
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${testInstance.GetTestPos()}"
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}</title>
    </head>
    <body>
        <h2>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}</h2>
        <h3>${testInstance.task.printName()} (${message(code:'fc.test.timetable.version')} ${testInstance.task.GetTimeTableVersion()}<g:if test="${testInstance.task.GetTimeTableVersion() != testInstance.timetableVersion}">, ${message(code:'fc.test.timetable.unchangedversion')} ${testInstance.timetableVersion}</g:if>)</h3>
        <g:form>
            <g:crewTestPrintable t="${testInstance}"/>
            <br/>
            <table class="infoanr">
                <tbody>
                    <tr class="planning">
                        <g:if test="${testInstance.task.planningTestDuration == 0 || testInstance.task.preparationDuration == 0}">
                            <td class="title">${message(code:'fc.flighttest.documentsoutput.localtime')}:</td>
                        </g:if>
                        <g:else>
                            <td class="title">${message(code:'fc.test.planning.localtime')}:</td>
                        </g:else>
                        <td class="value">
                            <g:if test="${testInstance.timeCalculated}">
                                <g:if test="${testInstance.task.planningTestDuration == 0 || testInstance.task.preparationDuration == 0}">
                                    ${testInstance.GetTestingTime().format('HH:mm:ss')}
                                </g:if>
                                <g:else>
                                    ${testInstance.GetTestingTime().format('HH:mm:ss')} - ${testInstance.endTestingTime?.format('HH:mm:ss')}
                                </g:else>
                            </g:if>
                            <g:else>
                                 ${message(code:'fc.nocalculated')}
                            </g:else>
                        </td>
                    </tr>
                    <tr class="takeoff">
                        <td class="title">${message(code:'fc.test.takeoff.localtime')}:</td>
                        <td class="value">
                            <g:if test="${testInstance.timeCalculated}">
                                ${testInstance.takeoffTime.format('HH:mm:ss')}
                            </g:if>
                            <g:else>
                                ${message(code:'fc.nocalculated')}
                            </g:else>
                        </td>
                    </tr>
                    <tr class="sp">
                        <td class="title">${message(code:CoordType.SP.code)} (${message(code:'fc.time.local')}):</td>
                        <td class="value">
                            <g:if test="${testInstance.timeCalculated}">
                                ${testInstance.startTime.format('HH:mm:ss')}
                            </g:if>
                            <g:else>
                                ${message(code:'fc.nocalculated')}
                            </g:else>
                        </td>
                    </tr>
                    <tr class="fp">
                        <td class="title">${message(code:CoordType.FP.code)} (${message(code:'fc.time.local')}):</td>
                        <td class="value">
                            <g:if test="${testInstance.timeCalculated}">
                                ${testInstance.finishTime.format('HH:mm:ss')}
                            </g:if>
                            <g:else>
                                ${message(code:'fc.nocalculated')}
                            </g:else>
                        </td>
                    </tr>
                    <tr class="landing">
                        <td class="title">${message(code:'fc.test.landing.latest.localtime')}:</td>
                        <td class="value">
                            <g:if test="${testInstance.timeCalculated}">
                                ${testInstance.maxLandingTime.format('HH:mm:ss')}
                            </g:if>
                            <g:else>
                                ${message(code:'fc.nocalculated')}
                            </g:else>
                        </td>
                    </tr>
                    <tr class="empty">
                        <td colnum="2"> </td>
                    </tr>
                    <tr class="corridorwidth">
                        <td class="title">${message(code:'fc.corridorwidth')}:</td>
                        <g:if test="${testInstance.flighttestwind.corridorWidthWind}">
                            <td class="value">${FcMath.DistanceStr(testInstance.flighttestwind.corridorWidthWind)}${message(code:'fc.mile')}</td>
                        </g:if>
                        <g:else>
                            <td class="value">${FcMath.DistanceStr(testInstance.flighttestwind.flighttest.route.corridorWidth)}${message(code:'fc.mile')}</td>
                        </g:else>
                    </tr>
                    <g:if test="${testInstance.flighttestwind.wind.speed}">
                        <tr class="wind">
                            <td class="title">${message(code:'fc.wind.directionvelocity')}:</td>
                            <td class="value">
                             <g:if test="${testInstance.flighttestwind}">
                                 <g:windtextprintable var="${testInstance.flighttestwind.wind}" />
                             </g:if> <g:else>
                                 ${message(code:'fc.noassigned')}                                    
                             </g:else>
                            </td>
                        </tr>
                    </g:if>
                </tbody>
            </table>
            <br/>
             
            <g:if test="${params.tplist == 'true' && TestLegFlight.countByTest(testInstance)}" >
                <g:set var="end_curved" value="${false}" />
                <table class="flightplanlistanr2">
                    <thead>
                        <tr>
                            <th></th>
                            <th>${message(code:'fc.distance')}</th>
                            <th>${message(code:'fc.truetrack')}</th>
                            <th>${message(code:'fc.legtime')}<br/>${message(code:'fc.time.values')}</th>
                            <th>${message(code:'fc.time.local')}<br/>${message(code:'fc.time.values')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="value" id="${message(code:CoordType.TO.code)}">
                            <td class="tpname">${message(code:CoordType.TO.code)}</td>
                            <td class="distance"/>
                            <td class="truetrack"/>
                            <td class="legtime"/>
                            <g:if test="${testInstance.timeCalculated}">
                                <td class="tptime">${FcMath.TimeStr(testInstance.takeoffTime)}<g:if test="${testInstance.takeoffTimeWarning}"> !</g:if></td>
                            </g:if> 
                            <g:else>
                                <td class="tptime">${message(code:'fc.nocalculated')}</td>
                            </g:else>
                        </tr>
                        <tr class="value" id="${message(code:CoordType.SP.code)}">
                            <td class="tpname">${message(code:CoordType.SP.code)}</td>
                            <td class="distance"/>
                            <td class="truetrack"/>
                            <g:if test="${testInstance.flighttestwind.flighttest.flightPlanShowLocalTime}">
                                <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.startTime))}</td>
                            </g:if>
                            <g:else>
                                <td class="legtime"></td>
                            </g:else>
                            <g:if test="${testInstance.timeCalculated}">
                                <td class="tptime">${FcMath.TimeStr(testInstance.startTime)}</td>
                            </g:if>
                            <g:else>
                                <td class="tptime">${message(code:'fc.nocalculated')}</td>
                            </g:else>
                        </tr>
                        <g:set var="leg_firstvalue" value="${true}"/>
                        <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                        <g:set var="leg_plantruetrack" value="${new BigDecimal(0)}" />
                        <g:set var="leg_plantrueheading" value="${new BigDecimal(0)}" />
                        <g:set var="leg_plangroundspeed" value="${new BigDecimal(0)}" />
                        <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                        <g:set var="leg_time" value="${testInstance.startTime}" />
                        <g:set var="total_distance" value="${new BigDecimal(0)}" />
                        <g:set var="add_tpnum" value="${testInstance.task.flighttest.AddTPNum()}" />
                        <g:set var="add_tpnum_index" value="${0}" />
                        <g:set var="add_tpnum_addnum" value="${0}" />
                        
                        <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(testInstance,[sort:"id"])}">
                            <g:set var="leg_distance" value="${FcMath.AddDistance(leg_distance,testlegflight_instance.planTestDistance)}" />
                            <g:if test="${leg_firstvalue}">
                                <g:set var="leg_plantruetrack" value="${testlegflight_instance.planTrueTrack}" />
                                <g:set var="leg_plantrueheading" value="${testlegflight_instance.planTrueHeading}" />
                                <g:set var="leg_plangroundspeed" value="${testlegflight_instance.planGroundSpeed}" />
                            </g:if>
                            <g:set var="leg_duration" value="${testlegflight_instance.AddPlanLegTime(leg_duration,leg_time)}" />
                            <g:set var="leg_time" value="${testlegflight_instance.AddPlanLegTime(leg_time)}" />
                            <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,testlegflight_instance.planTestDistance)}" />
                            <g:if test="${add_tpnum && add_tpnum_index < add_tpnum.tp.size() && add_tpnum.tp[add_tpnum_index] == testlegflight_instance.coordTitle.name()}">
                                <g:set var="add_tpnum_addnum" value="${add_tpnum_addnum + add_tpnum.addNumber[add_tpnum_index]}" />
                                <g:set var="add_tpnum_index" value="${add_tpnum_index + 1}" />
                            </g:if>
                            <g:if test="${testlegflight_instance.coordTitle.type != CoordType.SECRET}">
                                <tr class="value" id="${testlegflight_instance.coordTitle.titlePrintCode()}">
                                    <g:if test="${add_tpnum_addnum}" >
                                        <td class="tpname">${testlegflight_instance.coordTitle.titlePrintCode2(add_tpnum_addnum)}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="tpname">${testlegflight_instance.coordTitle.titlePrintCode()}</td>
                                    </g:else>
                                    <g:if test="${testInstance.task.flighttest.flightPlanShowLegDistance}">
                                        <td class="distance">${FcMath.DistanceStr1(leg_distance)}${message(code:'fc.mile')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="distance"/>
                                    </g:else>
                                    <g:if test="${testInstance.task.flighttest.flightPlanShowTrueTrack}">
                                        <td class="truetrack"><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.GradStr(leg_plantruetrack)}${message(code:'fc.grad')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="truetrack"/>
                                    </g:else>
                                    <g:if test="${testInstance.flighttestwind.flighttest.flightPlanShowLocalTime}">
                                        <td class="legtime">${FcMath.TimeStr(leg_duration)}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="legtime"></td>
                                    </g:else>
                                    <g:if test="${testInstance.flighttestwind.flighttest.flightPlanShowLocalTime || testlegflight_instance.coordTitle.type == CoordType.FP}">
                                        <td class="tptime">${FcMath.TimeStr(leg_time)}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="tptime"></td>
                                    </g:else>
                                </tr>
                                <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                                <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                                <g:set var="leg_firstvalue" value="${true}"/>
                            </g:if>
                            <g:else>
                                <g:set var="leg_firstvalue" value="${false}"/>
                            </g:else>
                            <g:if test="${testlegflight_instance.endCurved}">
                                <g:set var="end_curved" value="${true}" />
                            </g:if>
                        </g:each>
                        <tr class="value" id="${message(code:CoordType.LDG.code)}">
                            <td class="tpname">${message(code:CoordType.LDG.code)}</td>
                            <td class="distance"/>
                            <td class="truetrack"/>
                            <g:if test="${testInstance.flighttestwind.flighttest.flightPlanShowLocalTime}">
                                <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(leg_time,testInstance.maxLandingTime))}</td>
                            </g:if>
                            <g:else>
                                <td class="legtime"></td>
                            </g:else>
                            <td class="tptime">${FcMath.TimeStr(testInstance.maxLandingTime)}</td>
                        </tr>
                    </tbody>
                </table>
                <br/>
                <table class="infoanr2">
                    <tbody>
                        <tr class="distance">
                            <td class="title">${message(code:'fc.distance.sp2fp')}</td>
                            <td class="separator"></td>
                            <td class="value">${FcMath.DistanceStr1(total_distance)}${message(code:'fc.mile')}</td>
                        </tr>
                        <tr class="duration">
                            <td class="title">${message(code:'fc.legtime.sp2fp')}</td>
                            <td class="separator"></td>
                            <td class="value">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.startTime,testInstance.finishTime))}</td>
                        </tr>
                        <tr class="empty">
                            <td colnum="3"> </td>
                        </tr>
                        <g:if test="${end_curved && testInstance.task.flighttest.flightPlanShowTrueTrack}">
                            <tr class="endcurved">
                                <td class="title">${message(code:'fc.endcurved')}</td>
                                <td class="separator"></td>
                                <td class="value">${message(code:'fc.endcurved.info')}</td>
                            </tr>
                        </g:if>
                    </tbody>
                </table>
            </g:if>
        </g:form>
    </body>
</html>