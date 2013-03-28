<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.editprintsettings')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.editprintsettings')} - ${taskInstance.name()}</h2>
                <div class="block" id="forms">
                    <g:form params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}">
                        <fieldset>
                            <legend>${message(code:'fc.test.timetable')}</legend>
                            <p>
	                            <label>${message(code:'fc.printsubtitle')}:</label>
	                            <br/>
	                            <input type="text" id="printTimetablePrintTitle" name="printTimetablePrintTitle" value="${fieldValue(bean:taskInstance,field:'printTimetablePrintTitle')}" tabIndex="1"/>
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
                                    <g:checkBox name="printTimetableLandscape" value="${taskInstance.printTimetableLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableA3" value="${taskInstance.printTimetableA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updateprintsettingstimetablestandard" value="${message(code:'fc.setprintsettings.standard')}" tabIndex="2"/>
                            <g:actionSubmit action="updateprintsettingstimetablenone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="3"/>
                            <g:actionSubmit action="updateprintsettingstimetableall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="4"/>
                            <p>
                                <label>${message(code:'fc.test.timetable.change')}:</label>
                                <br/>
                                <g:textArea name="printTimetableChange" value="${taskInstance.printTimetableChange}" rows="5" cols="100" tabIndex="5"/>
                            </p>
                            <g:actionSubmit action="savechangeprintsettingstimetable" value="${message(code:'fc.savechangeprintsettings')}" tabIndex="6"/>
                            <g:actionSubmit action="removechangeprintsettingstimetable" value="${message(code:'fc.removechangeprintsettings')}" onclick="return confirm('${message(code:'fc.removechangeprintsettings.areyousure')}');" tabIndex="7"/>
                            <g:actionSubmit action="addchangeprintsettingstimetable" value="${message(code:'fc.addchangeprintsettings')}" tabIndex="8"/>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.test.timetablejury')}</legend>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="printTimetableJuryPrintTitle" name="printTimetableJuryPrintTitle" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryPrintTitle')}" tabIndex="9"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryNumber" value="${taskInstance.printTimetableJuryNumber}" />
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryCrew" value="${taskInstance.printTimetableJuryCrew}" />
                                    <label>${message(code:'fc.crew')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryAircraft" value="${taskInstance.printTimetableJuryAircraft}" />
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryAircraftType" value="${taskInstance.printTimetableJuryAircraftType}" />
                                    <label>${message(code:'fc.aircraft.type')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryAircraftColour" value="${taskInstance.printTimetableJuryAircraftColour}" />
                                    <label>${message(code:'fc.aircraft.colour')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryTAS" value="${taskInstance.printTimetableJuryTAS}" />
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryTeam" value="${taskInstance.printTimetableJuryTeam}" />
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${taskInstance.planningTestDuration == 0}">
                                    <div>
                                        <g:checkBox name="printTimetableJuryPlanning" value="${taskInstance.printTimetableJuryPlanning}" />
                                        <label>${message(code:'fc.test.planning.publish')}</label>
                                    </div>
                                </g:if>
                                <g:else>
                                    <div>
                                        <g:checkBox name="printTimetableJuryPlanning" value="${taskInstance.printTimetableJuryPlanning}" />
                                        <label>${message(code:'fc.test.planning')}</label>
                                    </div>
                                    <div>
                                        <g:checkBox name="printTimetableJuryPlanningEnd" value="${taskInstance.printTimetableJuryPlanningEnd}" />
                                        <label>${message(code:'fc.test.planning.end')}</label>
                                    </div>
                                </g:else>
                                <div>
                                    <g:checkBox name="printTimetableJuryTakeoff" value="${taskInstance.printTimetableJuryTakeoff}" />
                                    <label>${message(code:'fc.test.takeoff')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryStartPoint" value="${taskInstance.printTimetableJuryStartPoint}" />
                                    <label>${message(code:'fc.test.startpoint')}</label>
                                </div>
                                <g:set var="test_instance" value="${Test.findByTask(taskInstance)}" />
                                <g:if test="${test_instance}">
                                    <g:set var="leg_no" value="${new Integer(0)}" />
                                    <g:set var="leg_num" value="${TestLegFlight.countByTest(test_instance)}" />
                                    <g:set var="print_timetablejury_checkpoints" value="${taskInstance.printTimetableJuryCheckPoints},"/>
                                    <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(test_instance,[sort:"id"])}">
                                        <g:set var="leg_no" value="${leg_no+1}" />
                                        <g:if test="${leg_no==leg_num}">
                                            <g:set var="leg_name" value="${message(code:CoordType.FP.code)}"/>
                                        </g:if>
                                        <g:else>
                                            <g:set var="leg_name" value="${message(code:CoordType.TP.code)}${leg_no}"/>
                                            <div>
                                                <g:set var="leg_checkpoint" value="${leg_no},"/>
                                                <g:if test="${print_timetablejury_checkpoints.contains(leg_checkpoint)}">
                                                    <g:checkBox name="${leg_name}" value="${true}" />
                                                </g:if>
                                                <g:else>
                                                    <g:checkBox name="${leg_name}" value="${false}" />
                                                </g:else>
                                                <label>${leg_name}</label>
                                            </div>
                                        </g:else>
                                    </g:each>
                                </g:if>
                                <div>
                                    <g:checkBox name="printTimetableJuryFinishPoint" value="${taskInstance.printTimetableJuryFinishPoint}" />
                                    <label>${message(code:'fc.test.finishpoint')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryLanding" value="${taskInstance.printTimetableJuryLanding}" />
                                    <label>${message(code:'fc.test.landing.latest')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryArrival" value="${taskInstance.printTimetableJuryArrival}" />
                                    <label>${message(code:'fc.test.arrival')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn1" value="${taskInstance.printTimetableJuryEmptyColumn1}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 1</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle1" name="printTimetableJuryEmptyTitle1" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle1')}" tabIndex="10"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn2" value="${taskInstance.printTimetableJuryEmptyColumn2}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 2</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle2" name="printTimetableJuryEmptyTitle2" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle2')}" tabIndex="11"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryEmptyColumn3" value="${taskInstance.printTimetableJuryEmptyColumn3}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 3</label>
                                </div>
                                <p>
                                    <input type="text" id="printTimetableJuryEmptyTitle3" name="printTimetableJuryEmptyTitle3" value="${fieldValue(bean:taskInstance,field:'printTimetableJuryEmptyTitle3')}" tabIndex="12"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printTimetableJuryLandscape" value="${taskInstance.printTimetableJuryLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printTimetableJuryA3" value="${taskInstance.printTimetableJuryA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updateprintsettingsplanning" value="${message(code:'fc.planningtest.setprintsettings')}" tabIndex="13"/>
                            <g:actionSubmit action="updateprintsettingstakeoff" value="${message(code:'fc.flighttest.takeoff.setprintsettings')}" tabIndex="14"/>
                            <g:actionSubmit action="updateprintsettingslanding" value="${message(code:'fc.landingtest.setprintsettings')}" tabIndex="15"/>
                            <g:actionSubmit action="updateprintsettingsparking" value="${message(code:'fc.flighttest.parking.setprintsettings')}" tabIndex="16"/>
                            <g:actionSubmit action="updateprintsettingsstandard" value="${message(code:'fc.setprintsettings.standard')}" tabIndex="17"/>
                            <g:actionSubmit action="updateprintsettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="18"/>
                            <g:actionSubmit action="updateprintsettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="19"/>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="updateprintsettings" value="${message(code:'fc.update')}" tabIndex="101"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="102"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>