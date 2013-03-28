<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${taskInstance.printTimetableA3}">
                    <g:if test="${taskInstance.printTimetableLandscape}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${taskInstance.printTimetableLandscape}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else> 
                @top-center {
                    content: "${message(code:'fc.test.timetable')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion}) - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.timetable')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.timetable')}<g:if test="${taskInstance.printTimetablePrintTitle}"> - ${taskInstance.printTimetablePrintTitle}</g:if></h2>
                <h3>${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})</h3>
                <div class="block" id="forms" >
                    <g:form>
                        <br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <g:if test="${taskInstance.printTimetableNumber}">
                                        <th valign="top">${message(code:'fc.number')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableCrew}">
                                        <th valign="top">${message(code:'fc.crew')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableAircraft}">
                                        <th valign="top">${message(code:'fc.aircraft')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableTAS}">
                                        <th valign="top">${message(code:'fc.tas')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableTeam}">
                                        <th valign="top">${message(code:'fc.team')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetablePlanning}">
	                                    <g:if test="${taskInstance.planningTestDuration == 0}">
	                                        <th valign="top">${message(code:'fc.test.planning.publish')}</th>
	                                    </g:if>
	                                    <g:else>
	                                        <th valign="top">${message(code:'fc.test.planning')}</th>
	                                    </g:else>
	                                </g:if>
                                    <g:if test="${taskInstance.printTimetableTakeoff}">
                                        <th valign="top">${message(code:'fc.test.takeoff')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableVersion}">
                                        <th valign="top">${message(code:'fc.test.timetable.unchangedversion.short')}</th>
                                    </g:if>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                   	<g:if test="${!test_instance.crew.disabled}">
	                                    <tr>
                                            <g:if test="${taskInstance.printTimetableNumber}">
	                                           <td>${test_instance.GetStartNum()}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableCrew}">
	                                           <td>${test_instance.crew.name}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableAircraft}">
	                                           <td>${test_instance.taskAircraft.registration}<g:if test="${test_instance.taskAircraft?.user1 && test_instance.taskAircraft?.user2}">${HTMLFilter.NoWrapStr(' *')}</g:if></td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableTAS}">
	                                           <td>${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableTeam}">
	                                           <td><g:if test="${test_instance.crew.team}">${test_instance.crew.team.name}</g:if></td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetablePlanning}">
	                                           <td>${test_instance.testingTime?.format('HH:mm')}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableTakeoff}">
	                                           <td>${test_instance.takeoffTime?.format('HH:mm')}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableVersion}">
	                                           <td>${test_instance.timetableVersion}</td>
	                                        </g:if>
	                                    </tr>
	                                </g:if>
                                </g:each>
                            </tbody>
                        </table>
                        <g:if test="${taskInstance.printTimetableVersion}">
                            <p>${message(code:'fc.test.timetable.unchangedversion.short.help')}</p>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableChange}">
                            <p><g:each var="line" in="${taskInstance.printTimetableChange.readLines()}">${HTMLFilter.NoWrapStr(line)}<br/></g:each></p>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>