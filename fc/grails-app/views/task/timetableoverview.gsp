<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.timetableoverview')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.timetableoverview')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.GetTimeTableVersion()}<g:if test="${taskInstance.timetableModified}">*</g:if>)</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms">
                    <g:form params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.briefingtime')} [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="briefingTime" name="briefingTime" value="${fieldValue(bean:taskInstance,field:'briefingTime')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                            </p>
                            
                            <g:if test="${taskInstance.IsFlightTestRun()}">
                                <table>
                                    <tbody>
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
                                                    <tr>
                                                        <td class="detailtitle">${message(code:'fc.test.planning.publish')}${test_num}:</td>
                                                        <td>${first_test.GetTestingTime().format('HH:mm')} - ${last_test.endTestingTime.format('HH:mm')}</td>
                                                        <td/>
                                                        <g:if test="${first_landing_time}">
                                                            <td/>
                                                            <td/>
                                                        </g:if>
                                                        <td/>
                                                    </tr>
                                                </g:if>
                                                <g:else>
                                                    <tr>
                                                        <td class="detailtitle">${message(code:'fc.planningtest')}${test_num}:</td>
                                                        <td>${first_test.GetTestingTime().format('HH:mm')} - ${last_test.endTestingTime.format('HH:mm')}</td>
                                                        <td/>
                                                        <g:if test="${first_landing_time}">
                                                            <td/>
                                                            <td/>
                                                        </g:if>
                                                        <td/>
                                                    </tr>
                                                </g:else>
                                                <tr>
                                                    <td class="detailtitle">${message(code:'fc.test.takeoff')}${test_num}:</td>
                                                    <td>${first_test.takeoffTime.format('HH:mm')} - ${last_test.takeoffTime.format('HH:mm')}</td>
                                                    <td/>
                                                    <g:if test="${first_landing_time}">
                                                        <td/>
                                                        <td/>
                                                    </g:if>
                                                    <td/>
                                                </tr>
                                                <g:if test="${first_landing_time}">
                                                    <tr>
                                                        <td class="detailtitle">${message(code:'fc.landingtest.landings.intermediate')}${test_num}:</td>
                                                        <td>${first_landing_time} - ${last_test.GetIntermediateLandingTime(true)}</td>
                                                        <td/>
                                                        <td/>
                                                        <td/>
                                                        <td/>
                                                    </tr>
                                                </g:if>
                                                <tr>
                                                    <td class="detailtitle">${message(code:'fc.landingtest.landings')}${test_num}:</td>
                                                    <td>${first_test.maxLandingTime.format('HH:mm')} - ${last_test.maxLandingTime.format('HH:mm')}</td>
                                                    <td/>
                                                    <g:if test="${first_landing_time}">
                                                        <td/>
                                                        <td/>
                                                    </g:if>
                                                    <td/>
                                                </tr>
                                                <tr>
                                                    <g:if test="${first_landing_time}">
                                                        <td colspan="7">.</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td colspan="5">.</td>
                                                    </g:else>
                                                </tr>
                                            </g:if>
                                        </g:each>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.task.takeoffinterval')}:</td>
                                            <td colspan="2">${taskInstance.takeoffIntervalNormal} ${message(code:'fc.time.min')}</td>
                                            <g:if test="${first_landing_time}">
                                                <td/>
                                                <td/>
                                            </g:if>
                                            <td/>
                                        </tr>
                                        <tr>
                                            <g:if test="${first_landing_time}">
                                                <td colspan="7">.</td>
                                            </g:if>
                                            <g:else>
                                                <td colspan="5">.</td>
                                            </g:else>
                                        </tr>
                                        <g:set var="last_tasktas" value="${new BigDecimal(0)}"/>
                                        <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskTAS',order:'desc'])}">
                                            <g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.timeCalculated}">
                                                <g:if test="${last_tasktas != test_instance.taskTAS}">
                                                    <tr>
                                                        <g:if test="${!last_tasktas}">
                                                            <td>${message(code:'fc.legtime.total')}:</td>
                                                        </g:if>
                                                        <g:else>
                                                            <td/>
                                                        </g:else>
                                                        <td>${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')} - ${FcMath.TimeStrShort(FcMath.TimeDiff(test_instance.takeoffTime,test_instance.maxLandingTime))}${message(code:'fc.time.h')}</td>
                                                        <td>${message(code:CoordType.TO.code)} -> ${message(code:CoordType.SP.code)}: ${FcMath.TimeStrMin(test_instance.GetTO2SPTime())}${message(code:'fc.time.min')}</td>
                                                        <g:if test="${first_landing_time}">
                                                            <td>${message(code:CoordType.iFP.code)} -> ${message(code:CoordType.iLDG.code)}: ${FcMath.TimeStrMin(test_instance.GetiFP2iLDGTime())}${message(code:'fc.time.min')}</td>
                                                            <td>${message(code:CoordType.iLDG.code)} -> ${message(code:CoordType.iSP.code)}: ${FcMath.TimeStrMin(test_instance.GetiLDG2iSPTime())}${message(code:'fc.time.min')}</td>
                                                        </g:if>
                                                        <td>${message(code:CoordType.FP.code)} -> ${message(code:CoordType.LDG.code)}: ${FcMath.TimeStrMin(test_instance.GetFP2LDGTime())}${message(code:'fc.time.min')}</td>
                                                    </tr>
                                                </g:if>
                                                <g:set var="last_tasktas" value="${test_instance.taskTAS}"/>
                                            </g:if>
                                        </g:each>
                                    </tbody>
                                </table>
                            </g:if>
                            <table>
                                <tbody>
		                        </tbody>
		                    </table>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableOverviewLegTimes" value="${taskInstance.printTimetableOverviewLegTimes}" onclick="modify();"/>
                                    <label>${message(code:'fc.legtime.total')}</label>
                                </div>
                                <br/>
                                <div>
                                    <g:checkBox name="printTimetableOverviewLandscape" value="${taskInstance.printTimetableOverviewLandscape}" onclick="modify();"/>
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableOverviewA3" value="${taskInstance.printTimetableOverviewA3}" onclick="modify();"/>
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savetimetableoverviewsettings" id="savetimetableoverviewsettings_id" value="${message(code:'fc.save')}" disabled tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="printtimetableoverview" id="printtimetableoverview_id" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                        <script>
                            function modify() {
                                $("#savetimetableoverviewsettings_id").prop("disabled", false);
                                $("#printtimetableoverview_id").prop("disabled", true);
                            }
                        </script>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>