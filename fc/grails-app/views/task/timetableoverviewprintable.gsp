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
                    content: "${message(code:'fc.task.timetableoverview')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})"
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.task.timetableoverview')}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${message(code:'fc.task.timetableoverview')}<g:if test="${taskInstance.printTimetablePrintTitle}"> - ${taskInstance.printTimetablePrintTitle}</g:if></h2>
                <h3>${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})</h3>
                <div>
                    <g:form>
                        <br/>
                        <g:set var="first_test" value="${taskInstance.GetFirstTest()}"/>
                        <g:set var="last_test" value="${taskInstance.GetLastTest()}"/>
                        <g:if test="${first_test && last_test}">
                            <table class="timetableoverviewlist">
                                <tbody>
                                    <g:if test="${taskInstance.briefingTime}">
                                        <tr class="briefing">
                                            <td class="col1">${message(code:'fc.test.briefing')}:</td>
                                            <td class="col2">${taskInstance.briefingTime}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${taskInstance.IsPlanningTestRun()}">
                                        <g:if test="${taskInstance.planningTestDuration == 0}">
                                            <tr class="planning">
                                                <td class="col1">${message(code:'fc.test.planning.publish')}:</td>
                                                <td class="col2">${first_test.testingTime.format('HH:mm')}</td>
                                                <td class="col3">- ${last_test.endTestingTime.format('HH:mm')}</td>
                                            </tr>
                                        </g:if>
                                        <g:else>
                                            <tr class="planning">
                                                <td class="col1">${message(code:'fc.planningtest')}:</td>
                                                <td class="col2">${first_test.testingTime.format('HH:mm')}</td>
                                                <td class="col3">- ${last_test.endTestingTime.format('HH:mm')}</td>
                                            </tr>
                                        </g:else>
                                    </g:if>
                                    <g:if test="${taskInstance.IsFlightTestRun()}">
                                        <tr class="takeoff">
                                            <td class="col1">${message(code:'fc.test.takeoff')}:</td>
                                            <td class="col2">${first_test.takeoffTime.format('HH:mm')}</td>
                                            <td class="col3">- ${last_test.takeoffTime.format('HH:mm')}</td>
                                        </tr>
                                        <g:if test="${first_test.GetIntermediateLandingTime(true)}">
                                            <tr class="intermediatelanding">
                                                <td class="col1">${message(code:'fc.landingtest.landings.intermediate')}:</td>
                                                <td class="col2">${first_test.GetIntermediateLandingTime(true)}</td>
                                                <td class="col3">- ${last_test.GetIntermediateLandingTime(true)}</td>
                                            </tr>
                                        </g:if>
                                        <tr class="landing">
                                            <td class="col1">${message(code:'fc.landingtest.landings')}:</td>
                                            <td class="col2">${first_test.maxLandingTime.format('HH:mm')}</td>
                                            <td class="col3">- ${last_test.maxLandingTime.format('HH:mm')}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableOverviewLegTimes}">
                                        <g:set var="last_tasktas" value="${new BigDecimal(0)}"/>
                                        <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskTAS',order:'asc'])}">
							                <g:if test="${!test_instance.crew.disabled}">
							                    <g:if test="${last_tasktas != test_instance.taskTAS}">
							                        <tr class="legtimes">
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
                                </tbody>
                            </table>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>