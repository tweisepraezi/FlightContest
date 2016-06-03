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
                    content: "${message(code:'fc.test.timetable')} - ${taskInstance.printName()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})"
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
        <title>${message(code:'fc.test.timetable')}</title>
    </head>
    <body>
        <h2>${message(code:'fc.test.timetable')}<g:if test="${taskInstance.printTimetablePrintTitle}"> - ${taskInstance.printTimetablePrintTitle}</g:if></h2>
        <h3>${taskInstance.printName()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})</h3>
        <g:form>
            <table class="timetablelist">
                <thead>
                    <tr>
                        <g:if test="${taskInstance.printTimetableNumber}">
                            <th>${message(code:'fc.number')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableCrew}">
                            <th>${message(code:'fc.crew')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableAircraft}">
                            <th>${message(code:'fc.aircraft')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableTAS}">
                            <th>${message(code:'fc.tas')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableTeam}">
                            <th>${message(code:'fc.team')}</th>
                        </g:if>
                        <g:if test="${taskInstance.contest.resultClasses}">
                            <g:if test="${taskInstance.printTimetableClass}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
                            <g:if test="${taskInstance.printTimetableShortClass}">
                                <th>${message(code:'fc.resultclass.short.short')}</th>
                            </g:if>
                        </g:if>
                        <g:if test="${taskInstance.printTimetablePlanning}">
                            <g:if test="${taskInstance.planningTestDuration == 0}">
                                <th>${message(code:'fc.test.planning.publish')}</th>
                            </g:if>
                            <g:else>
                                <th>${message(code:'fc.test.planning')}</th>
                            </g:else>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableTakeoff}">
                            <th>${message(code:'fc.test.takeoff')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printTimetableVersion}">
                            <th>${message(code:'fc.test.timetable.unchangedversion.short')}</th>
                        </g:if>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                       	<g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled}">
                            <tr class="value" id="${test_instance.crew.startNum}">
                                <g:if test="${taskInstance.printTimetableNumber}">
                                    <td class="num">${test_instance.GetStartNum()}</td>
                                </g:if>
                                <g:if test="${taskInstance.printTimetableCrew}">
                                    <td class="crew">${test_instance.crew.name}</td>
                                </g:if>
                                <g:if test="${taskInstance.printTimetableAircraft}">
                                    <td class="aircraft">${test_instance.taskAircraft.registration}<g:if test="${test_instance.taskAircraft?.user1 && test_instance.taskAircraft?.user2}">${HTMLFilter.NoWrapStr(' *')}</g:if></td>
                                </g:if>
                                <g:if test="${taskInstance.printTimetableTAS}">
                                    <td class="tas">${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </g:if>
                                <g:if test="${taskInstance.printTimetableTeam}">
                                    <td class="team"><g:if test="${test_instance.crew.team}">${test_instance.crew.team.name}</g:if></td>
                                </g:if>
                                <g:if test="${taskInstance.contest.resultClasses}">
                                    <g:if test="${taskInstance.printTimetableClass}">
                                        <td class="resultclass"><g:if test="${test_instance.crew.resultclass}">${test_instance.crew.resultclass.name}</g:if></td>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableShortClass}">
                                       <td class="shortresultclass"><g:if test="${test_instance.crew.resultclass}">${test_instance.crew.resultclass.shortName}</g:if></td>
                                    </g:if>
                                </g:if>
                                <g:if test="${taskInstance.printTimetablePlanning}">
                                    <td class="planningtime">${test_instance.testingTime?.format('HH:mm')}</td>
                                </g:if>
                                <g:if test="${taskInstance.printTimetableTakeoff}">
                                    <td class="takeofftime">${test_instance.takeoffTime?.format('HH:mm')}</td>
                                </g:if>
                                <g:if test="${taskInstance.printTimetableVersion}">
                                    <td class="version">${test_instance.timetableVersion}</td>
                                </g:if>
                            </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
            <g:if test="${taskInstance.printTimetableVersion}">
                <p>${message(code:'fc.test.timetable.unchangedversion.short.help')}</p>
            </g:if>
            <g:if test="${taskInstance.printTimetableLegTimes}">
                <div style="page-break-inside:avoid">
                    <g:if test="${!taskInstance.printTimetableVersion}">
                        <br/>
                    </g:if>
                    <table class="legtimelist">
                        <thead>
                            <tr>
                                <th class="tas">${message(code:'fc.tas')}</th>
                                <th class="legtime">${message(code:'fc.legtime.total')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:set var="lastTaskTAS" value="${new BigDecimal(0)}"/>
                            <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskTAS',order:'asc'])}">
                                <g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled}">
                                    <g:if test="${lastTaskTAS != test_instance.taskTAS}">
                                        <tr class="value" id="${test_instance.taskTAS.toInteger()}">
                                            <td class="tas">${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                            <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(test_instance.takeoffTime,test_instance.maxLandingTime))}${message(code:'fc.time.h')}</td>
                                        </tr>
                                    </g:if>
                                    <g:set var="lastTaskTAS" value="${test_instance.taskTAS}"/>
                                </g:if>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </g:if>
            <g:if test="${taskInstance.printTimetableChange}">
                <p><g:each var="line" in="${taskInstance.printTimetableChange.readLines()}">${HTMLFilter.NoWrapStr(line)}<br/></g:each></p>
            </g:if>
        </g:form>
    </body>
</html>