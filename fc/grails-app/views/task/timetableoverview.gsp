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
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.briefingtime')} [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="briefingTime" name="briefingTime" value="${fieldValue(bean:taskInstance,field:'briefingTime')}" tabIndex="1"/>
                            </p>
                            <g:set var="first_test" value="${taskInstance.GetFirstTest()}"/>
                            <g:set var="last_test" value="${taskInstance.GetLastTest()}"/>
                            <g:if test="${first_test && last_test}">
	                            <table>
	                                <tbody>
	                                    <g:if test="${taskInstance.IsPlanningTestRun()}">
		                                    <g:if test="${taskInstance.planningTestDuration == 0}">
		                                        <tr>
		                                            <td class="detailtitle">${message(code:'fc.test.planning.publish')}:</td>
		                                            <td>${first_test.testingTime.format('HH:mm')} - ${last_test.endTestingTime.format('HH:mm')}</td>
		                                            <td/>
		                                        </tr>
		                                    </g:if>
		                                    <g:else>
			                                    <tr>
			                                        <td class="detailtitle">${message(code:'fc.planningtest')}:</td>
			                                        <td>${first_test.testingTime.format('HH:mm')} - ${last_test.endTestingTime.format('HH:mm')}</td>
                                                    <td/>
		                                        </tr>
		                                    </g:else>
		                                </g:if>
	                                    <g:if test="${taskInstance.IsFlightTestRun()}">
		                                    <tr>
		                                        <td class="detailtitle">${message(code:'fc.test.takeoff')}:</td>
		                                        <td>${first_test.takeoffTime.format('HH:mm')} - ${last_test.takeoffTime.format('HH:mm')}</td>
                                                <td/>
		                                    </tr>
		                                    <g:if test="${first_test.GetIntermediateLandingTime(true)}">
                                                <tr>
                                                    <td class="detailtitle">${message(code:'fc.landingtest.landings.intermediate')}:</td>
                                                    <td>${first_test.GetIntermediateLandingTime(true)} - ${last_test.GetIntermediateLandingTime(true)}</td>
                                                    <td/>
                                                </tr>
		                                    </g:if>
		                                    <tr>
		                                        <td class="detailtitle">${message(code:'fc.landingtest.landings')}:</td>
  		                                        <td>${first_test.maxLandingTime.format('HH:mm')} - ${last_test.maxLandingTime.format('HH:mm')}</td>
                                                <td/>
		                                    </tr>
		                                    <tr><td span="2">.</td></tr>
		                                    <g:set var="last_tasktas" value="${new BigDecimal(0)}"/>
		                                    <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskTAS',order:'asc'])}">
		                                        <g:if test="${!test_instance.crew.disabled}">
		                                            <g:if test="${last_tasktas != test_instance.taskTAS}">
		                                                <tr>
		                                                    <g:if test="${!last_tasktas}">
		                                                        <td>${message(code:'fc.legtime.total')}:</td>
		                                                    </g:if>
		                                                    <g:else>
		                                                        <td/>
		                                                    </g:else>
		                                                    <td>${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')} - ${FcMath.TimeStrShort(FcMath.TimeDiff(test_instance.takeoffTime,test_instance.maxLandingTime))}${message(code:'fc.time.h')}</td>
		                                                    <td>${message(code:CoordType.TO.code)} -> ${message(code:CoordType.SP.code)}: ${FcMath.TimeStrMin(FcMath.TimeDiff(test_instance.takeoffTime,test_instance.startTime))}${message(code:'fc.time.min')}</td>
		                                                </tr>
		                                            </g:if>
		                                            <g:set var="last_tasktas" value="${test_instance.taskTAS}"/>
		                                        </g:if>
                                            </g:each>
		                                </g:if>
	                                </tbody>
	                            </table>
	                        </g:if>
                            <table>
                                <tbody>
		                        </tbody>
		                    </table>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableOverviewLegTimes" value="${taskInstance.printTimetableOverviewLegTimes}" />
                                    <label>${message(code:'fc.legtime.total')}</label>
                                </div>
                                <br/>
                                <div>
                                    <g:checkBox name="printTimetableOverviewLandscape" value="${taskInstance.printTimetableOverviewLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableOverviewA3" value="${taskInstance.printTimetableOverviewA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savetimetableoverviewsettings" value="${message(code:'fc.save')}" tabIndex="101"/>
                        <g:actionSubmit action="printtimetableoverview" value="${message(code:'fc.print')}" tabIndex="102"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="103"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>