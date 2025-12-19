<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.timetablejudge')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.timetablejudge')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.GetTimeTableVersion()}<g:if test="${taskInstance.timetableModified}">*</g:if>)</h2>
                <div class="block" id="forms">
                    <g:form params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <g:set var="intermediate_tower" value="${false}" />
                            <g:set var="intermediate_landing" value="${false}" />
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="printTimetableJuryPrintTitle" name="printTimetableJuryPrintTitle" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryPrintTitle')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryNumber" value="${taskInstance.printTimetableJuryNumber}" onclick="modify();"/>
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryCrew" value="${taskInstance.printTimetableJuryCrew}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryAircraft" value="${taskInstance.printTimetableJuryAircraft}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryAircraftType" value="${taskInstance.printTimetableJuryAircraftType}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft.type')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryAircraftColour" value="${taskInstance.printTimetableJuryAircraftColour}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft.colour')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryTAS" value="${taskInstance.printTimetableJuryTAS}" onclick="modify();"/>
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryTeam" value="${taskInstance.printTimetableJuryTeam}" onclick="modify();"/>
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <div>
	                                    <g:checkBox name="printTimetableJuryClass" value="${taskInstance.printTimetableJuryClass}" onclick="modify();"/>
	                                    <label>${message(code:'fc.resultclass')}</label>
	                                </div>
                                    <div>
                                        <g:checkBox name="printTimetableJuryShortClass" value="${taskInstance.printTimetableJuryShortClass}" onclick="modify();"/>
                                        <label>${message(code:'fc.resultclass.short')}</label>
                                    </div>
                                </g:if>
                                <g:if test="${taskInstance.flighttest?.route?.corridorWidth}">
                                    <div>
                                        <g:checkBox name="printTimetableJuryCorridorWidth" value="${taskInstance.printTimetableJuryCorridorWidth}" onclick="modify();"/>
                                        <label>${message(code:'fc.route')}</label>
                                    </div>
                                </g:if>
                                <g:if test="${taskInstance.planningTestDuration == 0 || taskInstance.preparationDuration == 0}">
                                    <div>
                                        <g:checkBox name="printTimetableJuryPlanning" value="${taskInstance.printTimetableJuryPlanning}" onclick="modify();"/>
                                        <label>${message(code:'fc.test.planning.publish')}</label>
                                    </div>
                                </g:if>
                                <g:else>
                                    <div>
                                        <g:checkBox name="printTimetableJuryPlanning" value="${taskInstance.printTimetableJuryPlanning}" onclick="modify();"/>
                                        <label>${message(code:'fc.test.planning')}</label>
                                    </div>
                                    <div>
                                        <g:checkBox name="printTimetableJuryPlanningEnd" value="${taskInstance.printTimetableJuryPlanningEnd}" onclick="modify();"/>
                                        <label>${message(code:'fc.test.planning.end')}</label>
                                    </div>
                                </g:else>
                                <div>
                                    <g:checkBox name="printTimetableJuryTakeoff" value="${taskInstance.printTimetableJuryTakeoff}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.takeoff')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryStartPoint" value="${taskInstance.printTimetableJuryStartPoint}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.startpoint')}</label>
                                </div>
                                <g:set var="test_instance" value="${Test.findByTask(taskInstance)}" />
                                <g:if test="${test_instance}">
                                    <g:set var="leg_no" value="${new Integer(0)}" />
                                    <g:set var="leg_num" value="${TestLegFlight.countByTest(test_instance)}" />
                                    <g:set var="print_timetablejudge_checkpoints" value=",${taskInstance.printTimetableJuryCheckPoints},"/>
                                    <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(test_instance,[sort:"id"])}">
                                        <g:set var="leg_no" value="${leg_no+1}" />
                                        <g:set var="leg_name" value="${testlegflight_instance.coordTitle.titleCode()}"/>
                                        <g:if test="${leg_no==leg_num}">
                                        </g:if>
                                        <g:else>
                                            <div>
                                                <g:set var="leg_checkpoint" value=",${leg_no},"/>
                                                <g:if test="${print_timetablejudge_checkpoints.contains(leg_checkpoint)}">
                                                    <g:checkBox name="${leg_name}" value="${true}" onclick="modify();"/>
                                                </g:if>
                                                <g:else>
                                                    <g:checkBox name="${leg_name}" value="${false}" onclick="modify();"/>
                                                </g:else>
                                                <label>${leg_name}</label>
                                            </div>
                                        </g:else>
                                        <g:if test="${testlegflight_instance.coordTitle.type == CoordType.iLDG}">
                                            <g:set var="intermediate_tower" value="${true}" />
                                            <g:set var="intermediate_landing" value="${true}" />
                                        </g:if>
                                        <g:if test="${testlegflight_instance.coordTitle.type == CoordType.iTO}">
                                            <g:set var="intermediate_tower" value="${true}" />
                                        </g:if>
                                    </g:each>
                                </g:if>
                                <div>
                                    <g:checkBox name="printTimetableJuryFinishPoint" value="${taskInstance.printTimetableJuryFinishPoint}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.finishpoint')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryLanding" value="${taskInstance.printTimetableJuryLanding}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.landing.latest')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryArrival" value="${taskInstance.printTimetableJuryArrival}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.arrival')}</label>
                                </div>
			                    <g:if test="${taskInstance.flighttest?.submissionMinutes}">
                                    <div>
                                        <g:checkBox name="printTimetableJurySubmission" value="${taskInstance.printTimetableJurySubmission}" onclick="modify();"/>
                                        <label>${message(code:'fc.test.submission.latest2')}</label>
                                    </div>
                                </g:if>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn1" value="${taskInstance.printTimetableJuryEmptyColumn1}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 1</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle1" name="printTimetableJuryEmptyTitle1" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle1')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn2" value="${taskInstance.printTimetableJuryEmptyColumn2}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 2</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle2" name="printTimetableJuryEmptyTitle2" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle2')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn3" value="${taskInstance.printTimetableJuryEmptyColumn3}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 3</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle3" name="printTimetableJuryEmptyTitle3" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle3')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn4" value="${taskInstance.printTimetableJuryEmptyColumn4}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 4</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle4" name="printTimetableJuryEmptyTitle4" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle4')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryLandingField" value="${taskInstance.printTimetableJuryLandingField}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.landing.field')}</label>
                                </div>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryLandscape" value="${taskInstance.printTimetableJuryLandscape}" onclick="modify();"/>
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryA3" value="${taskInstance.printTimetableJuryA3}" onclick="modify();"/>
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updatetimetablejudgesettingsstandard" value="${message(code:'fc.standard')}" tabIndex="${ti[0]++}"/>
                            <g:if test="${(taskInstance.contest.anrFlying || taskInstance.IsPlanningTestRun()) && taskInstance.planningTestDuration && taskInstance.preparationDuration}">
                                <g:if test="${taskInstance.contest.anrFlying}">
                                    <g:actionSubmit action="updatetimetablejudgesettingsplanning" value="${message(code:'fc.task.listplanning.settings')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="updatetimetablejudgesettingsplanning" value="${message(code:'fc.planningtest.setprintsettings')}" tabIndex="${ti[0]++}"/>
                                </g:else>    
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="updatetimetablejudgesettingsdocumentsoutput" value="${message(code:'fc.flighttest.documentsoutput.setprintsettings')}" tabIndex="${ti[0]++}"/>
                            </g:else>
                            <g:actionSubmit action="updatetimetablejudgesettingstower" value="${message(code:'fc.setprintsettings.tower')}" tabIndex="${ti[0]++}"/>
                            <g:if test="${intermediate_tower}">
                                <g:actionSubmit action="updatetimetablejudgesettingsintermediatetower" value="${message(code:'fc.setprintsettings.tower.intermediate')}" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:if test="${taskInstance.IsLandingTestRun()}">
                                <g:actionSubmit action="updatetimetablejudgesettingslanding" value="${message(code:'fc.landingtest.setprintsettings')}" tabIndex="${ti[0]++}"/>
                                <g:if test="${intermediate_landing}">
                                    <g:actionSubmit action="updatetimetablejudgesettingsintermediatelanding" value="${message(code:'fc.landingtest.setprintsettings.intermediate')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                            </g:if>
                            <g:actionSubmit action="updatetimetablejudgesettingsarrival" value="${message(code:'fc.flighttest.arrival.setprintsettings')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatetimetablejudgesettingsdebriefing" value="${message(code:'fc.flighttest.debriefing.setprintsettings')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatetimetablejudgesettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatetimetablejudgesettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="${ti[0]++}"/>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savetimetablejudgesettings" id="savetimetablejudgesettings_id" value="${message(code:'fc.save')}" disabled tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="printtimetablejudge" id="printtimetablejudge_id" value="${message(code:'fc.print')}"tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                        <script>
                            function modify() {
                                $("#savetimetablejudgesettings_id").prop("disabled", false);
                                $("#printtimetablejudge_id").prop("disabled", true);
                            }
                        </script>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>