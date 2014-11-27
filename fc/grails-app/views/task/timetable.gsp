<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.timetable')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.timetable')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.GetTimeTableVersion()}<g:if test="${taskInstance.timetableModified}">*</g:if>)</h2>
                <div class="block" id="forms">
                    <g:form params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}">
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="printTimetablePrintTitle" name="printTimetablePrintTitle" value="${fieldValue(bean:taskInstance,field:'printTimetablePrintTitle')}" tabIndex="21"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableNumber" value="${taskInstance.printTimetableNumber}" />
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableCrew" value="${taskInstance.printTimetableCrew}" />
                                    <label>${message(code:'fc.crew')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableAircraft" value="${taskInstance.printTimetableAircraft}" />
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableTAS" value="${taskInstance.printTimetableTAS}" />
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableTeam" value="${taskInstance.printTimetableTeam}" />
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <div>
	                                    <g:checkBox name="printTimetableClass" value="${taskInstance.printTimetableClass}" />
	                                    <label>${message(code:'fc.resultclass')}</label>
	                                </div>
	                                <div>
	                                    <g:checkBox name="printTimetableShortClass" value="${taskInstance.printTimetableShortClass}" />
	                                    <label>${message(code:'fc.resultclass.short')}</label>
	                                </div>
	                            </g:if>
                                <g:if test="${taskInstance.planningTestDuration == 0}">
                                    <div>
                                        <g:checkBox name="printTimetablePlanning" value="${taskInstance.printTimetablePlanning}" />
                                        <label>${message(code:'fc.test.planning.publish')}</label>
                                    </div>
                                </g:if>
                                <g:else>
                                    <div>
                                        <g:checkBox name="printTimetablePlanning" value="${taskInstance.printTimetablePlanning}" />
                                        <label>${message(code:'fc.test.planning')}</label>
                                    </div>
                                </g:else>
                                <div>
                                    <g:checkBox name="printTimetableTakeoff" value="${taskInstance.printTimetableTakeoff}" />
                                    <label>${message(code:'fc.test.takeoff')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableVersion" value="${taskInstance.printTimetableVersion}" />
                                    <label>${message(code:'fc.version')}</label>
                                </div>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableLegTimes" value="${taskInstance.printTimetableLegTimes}" />
                                    <label>${message(code:'fc.legtime.total')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updatetimetablesettingsstandard" value="${message(code:'fc.standard')}" tabIndex="31"/>
                            <g:actionSubmit action="updatetimetablesettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="32"/>
                            <g:actionSubmit action="updatetimetablesettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="33"/>
                            <p>
                                <br/>
                                <label>${message(code:'fc.test.timetable.change')}:</label>
                                <br/>
                                <g:textArea name="printTimetableChange" value="${taskInstance.printTimetableChange}" rows="5" cols="100" tabIndex="34"/>
                            </p>
                            <g:actionSubmit action="removechangetimetablesettings" value="${message(code:'fc.removechangeprintsettings')}" onclick="return confirm('${message(code:'fc.removechangeprintsettings.areyousure')}');" tabIndex="36"/>
                            <g:actionSubmit action="addchangetimetablesettings" value="${message(code:'fc.addchangeprintsettings')}" tabIndex="37"/>
                            <p>
                                <br/>
                                <div>
                                    <g:checkBox name="printTimetableLandscape" value="${taskInstance.printTimetableLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableA3" value="${taskInstance.printTimetableA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savetimetablesettings" value="${message(code:'fc.save')}" tabIndex="101"/>
                        <g:actionSubmit action="printtimetable" value="${message(code:'fc.print')}" tabIndex="102"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="103"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>