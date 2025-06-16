<html>
    <head>
        <g:set var="flighttest_num" value="${taskInstance.GetFlightTests().size()}"/>
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
                    content: "${message(code:'fc.task.timetableoverview')} - ${taskInstance.printName()} (${message(code:'fc.crew.num', args:[flighttest_num])}, ${message(code:'fc.version')} ${taskInstance.timetableVersion})"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
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
        <title>${message(code:'fc.task.timetableoverview')}</title>
    </head>
    <body>
        <h2>${message(code:'fc.task.timetableoverview')}<g:if test="${taskInstance.printTimetablePrintTitle}"> - ${taskInstance.printTimetablePrintTitle}</g:if></h2>
        <h3>${taskInstance.printName()} (${message(code:'fc.crew.num', args:[flighttest_num])}, ${message(code:'fc.version')} ${taskInstance.timetableVersion})</h3>
        <g:form>
            <g:if test="${params.spfptimes == 'false'}">
                <table class="timetableoverviewlist">
                    <tbody>
                        <g:if test="${taskInstance.briefingTime}">
                            <tr class="briefing">
                                <td class="col1">${message(code:'fc.test.briefing')}:</td>
                                <td colspan="2" class="col2">${taskInstance.briefingTime}</td>
                            </tr>
                        </g:if>
                        <g:if test="${taskInstance.IsFlightTestRun()}">
                            <g:set var="first_tests" value="${taskInstance.GetFirstTests()}"/>
                            <g:set var="last_tests" value="${taskInstance.GetLastTests()}"/>
                            <g:each var="first_test" in="${first_tests}" status="i">
                                <g:set var="last_test" value="${last_tests[i]}"/>
                                <g:if test="${first_tests.size() > 1}">
                                    <g:set var="test_num" value=" ${i+1}"/>
                                </g:if>
                                <g:if test="${first_test && last_test}">
                                    <g:set var="first_landing_time" value="${first_test.GetIntermediateLandingTime(true)}"/>
                        
                                    <g:if test="${taskInstance.planningTestDuration == 0 || taskInstance.preparationDuration == 0}">
                                        <tr class="planning">
                                            <td class="col1">${message(code:'fc.test.planning.publish')}${test_num}:</td>
                                            <td class="col2">${first_test.GetTestingTime().format('HH:mm')}</td>
                                            <td class="col3">- ${last_test.endTestingTime.format('HH:mm')}</td>
                                        </tr>
                                    </g:if>
                                    <g:else>
                                        <tr class="planning">
                                            <td class="col1">${message(code:'fc.planningtest')}${test_num}:</td>
                                            <td class="col2">${first_test.GetTestingTime().format('HH:mm')}</td>
                                            <td class="col3">- ${last_test.endTestingTime.format('HH:mm')}</td>
                                        </tr>
                                    </g:else>
                                    <tr class="takeoff">
                                        <td class="col1">${message(code:'fc.test.takeoff')}${test_num}:</td>
                                        <td class="col2">${first_test.takeoffTime.format('HH:mm')}</td>
                                        <td class="col3">- ${last_test.takeoffTime.format('HH:mm')}</td>
                                    </tr>
                                    <g:if test="${first_landing_time}">
                                        <tr class="intermediatelanding">
                                            <td class="col1">${message(code:'fc.landingtest.landings.intermediate')}${test_num}:</td>
                                            <td class="col2">${first_landing_time}</td>
                                            <td class="col3">- ${last_test.GetIntermediateLandingTime(true)}</td>
                                        </tr>
                                    </g:if>
                                    <tr class="landing">
                                        <td class="col1">${message(code:'fc.landingtest.landings')}${test_num}:</td>
                                        <td class="col2">${first_test.maxLandingTime.format('HH:mm')}</td>
                                        <td class="col3">- ${last_test.maxLandingTime.format('HH:mm')}</td>
                                    </tr>
                                </g:if>
                            </g:each>
                            <tr class="takeoffinterval">
                                <td class="col1">${message(code:'fc.task.takeoffinterval')}:</td>
                                <td class="col2" colspan="2">${taskInstance.takeoffIntervalNormal} ${message(code:'fc.time.min')}</td>
                            </tr>
                            <g:if test="${taskInstance.printTimetableOverviewLegTimes}">
                                <g:set var="last_tasktas" value="${new BigDecimal(0)}"/>
                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskTAS',order:'desc'])}">
                                    <g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled}">
                                        <g:if test="${last_tasktas != test_instance.taskTAS}">
                                            <tr class="legtimes" id="${test_instance.taskTAS.toInteger()}">
                                                <g:if test="${!last_tasktas}">
                                                    <td class="col1">${message(code:'fc.legtime.total')}:</td>
                                                </g:if>
                                                <g:else>
                                                    <td class="col1"/>
                                                </g:else>
                                                <td class="col2">${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                                <td class="col3">${FcMath.TimeStrShort(FcMath.TimeDiff(test_instance.takeoffTime,test_instance.maxLandingTime))}${message(code:'fc.time.h')}</td>
                                            </tr>
                                        </g:if>
                                        <g:set var="last_tasktas" value="${test_instance.taskTAS}"/>
                                    </g:if>
                                </g:each>
                            </g:if>
                        </g:if>
                    </tbody>
                </table>
            </g:if>
            <g:else>
                <table class="timetableoverviewlist2">
                    <tbody>
                        <tr class="head">
                            <td/>
                            <td>${message(code:CoordType.TO.code)} -> ${message(code:CoordType.SP.code)}</td>
                            <g:if test="${first_landing_time}">
                                <td>${message(code:CoordType.iFP.code)} -> ${message(code:CoordType.iLDG.code)}</td>
                                <td>${message(code:CoordType.iLDG.code)} -> ${message(code:CoordType.iSP.code)}</td>
                            </g:if>
                            <td>${message(code:CoordType.FP.code)} -> ${message(code:CoordType.LDG.code)}</td>
                        </tr>
                        <g:set var="last_tasktas" value="${new BigDecimal(0)}"/>
                        <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskTAS',order:'desc'])}">
                            <g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.timeCalculated}">
                                <g:if test="${last_tasktas != test_instance.taskTAS}">
                                    <tr class="times" id="${test_instance.taskTAS.toInteger()}">
                                        <td class="tas">${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                        <td class="to2sp">${FcMath.TimeStrMin(test_instance.GetTO2SPTime())}${message(code:'fc.time.min')}</td>
                                        <g:if test="${first_landing_time}">
                                            <td class="ifp2ildg">${FcMath.TimeStrMin(test_instance.GetiFP2iLDGTime())}${message(code:'fc.time.min')}</td>
                                            <td class="ildg2isp">${FcMath.TimeStrMin(test_instance.GetiLDG2iSPTime())}${message(code:'fc.time.min')}</td>
                                        </g:if>
                                        <td class="fp2ldg">${FcMath.TimeStrMin(test_instance.GetFP2LDGTime())}${message(code:'fc.time.min')}</td>
                                    </tr>
                                </g:if>
                                <g:set var="last_tasktas" value="${test_instance.taskTAS}"/>
                            </g:if>
                        </g:each>
                    </tbody>
                </table>
            </g:else>
        </g:form>
    </body>
</html>