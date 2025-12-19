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
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="printTimetablePrintTitle" name="printTimetablePrintTitle" value="${fieldValue(bean:taskInstance,field:'printTimetablePrintTitle')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableNumber" value="${taskInstance.printTimetableNumber}" onclick="modify();"/>
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableCrew" value="${taskInstance.printTimetableCrew}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableAircraft" value="${taskInstance.printTimetableAircraft}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableTAS" value="${taskInstance.printTimetableTAS}" onclick="modify();"/>
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableTeam" value="${taskInstance.printTimetableTeam}" onclick="modify();"/>
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <div>
	                                    <g:checkBox name="printTimetableClass" value="${taskInstance.printTimetableClass}" onclick="modify();"/>
	                                    <label>${message(code:'fc.resultclass')}</label>
	                                </div>
	                                <div>
	                                    <g:checkBox name="printTimetableShortClass" value="${taskInstance.printTimetableShortClass}" onclick="modify();"/>
	                                    <label>${message(code:'fc.resultclass.short')}</label>
	                                </div>
	                            </g:if>
                                <g:if test="${taskInstance.flighttest?.route?.corridorWidth}">
                                    <div>
                                        <g:checkBox name="printTimetableCorridorWidth" value="${taskInstance.printTimetableCorridorWidth}" onclick="modify();"/>
                                        <label>${message(code:'fc.route')}</label>
                                    </div>
                                </g:if>
                                <g:if test="${taskInstance.planningTestDuration == 0 || taskInstance.preparationDuration == 0}">
                                    <div>
                                        <g:checkBox name="printTimetablePlanning" value="${taskInstance.printTimetablePlanning}" onclick="modify();"/>
                                        <label>${message(code:'fc.test.planning.publish')}</label>
                                    </div>
                                </g:if>
                                <g:else>
                                    <div>
                                        <g:checkBox name="printTimetablePlanning" value="${taskInstance.printTimetablePlanning}" onclick="modify();"/>
                                        <label>${message(code:'fc.test.planning')}</label>
                                    </div>
                                </g:else>
                                <div>
                                    <g:checkBox name="printTimetableTakeoff" value="${taskInstance.printTimetableTakeoff}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.takeoff')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableVersion" value="${taskInstance.printTimetableVersion}" onclick="modify();"/>
                                    <label>${message(code:'fc.version')}</label>
                                </div>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableLegTimes" value="${taskInstance.printTimetableLegTimes}" onclick="modify();"/>
                                    <label>${message(code:'fc.legtime.total')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updatetimetablesettingsstandard" value="${message(code:'fc.standard')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatetimetablesettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatetimetablesettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="${ti[0]++}"/>
                            <p>
                                <br/>
                                <label>${message(code:'fc.test.timetable.change')}:</label>
                                <br/>
                                <g:textArea name="printTimetableChange" value="${taskInstance.printTimetableChange}" rows="5" cols="100" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                            </p>
                            <g:actionSubmit action="removechangetimetablesettings" id="removechangetimetablesettings_id" value="${message(code:'fc.removechangeprintsettings')}" onclick="return confirm('${message(code:'fc.removechangeprintsettings.areyousure')}');" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="addchangetimetablesettings" id="addchangetimetablesettings_id" value="${message(code:'fc.addchangeprintsettings')}" tabIndex="${ti[0]++}"/>
                            <p>
                                <br/>
                                <div>
                                    <g:checkBox name="printTimetableLandscape" value="${taskInstance.printTimetableLandscape}" onclick="modify();"/>
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableA3" value="${taskInstance.printTimetableA3}" onclick="modify();"/>
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savetimetablesettings" id="savetimetablesettings_id" value="${message(code:'fc.save')}" disabled tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="printtimetable" id="printtimetable_id" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                        <script>
                            function modify() {
                                $("#savetimetablesettings_id").prop("disabled", false);
                                $("#printtimetable_id").prop("disabled", true);
                                $("#removechangetimetablesettings_id").prop("disabled", true);
                                $("#addchangetimetablesettings_id").prop("disabled", true);
                            }
                        </script>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>