<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.edit')}</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${taskInstance.GetNextTaskID()}"/>
						<g:set var="prev_id" value="${taskInstance.GetPrevTaskID()}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${taskInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:taskInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                                <a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.date')}:</label>
                                <br/>
                                <input type="date" id="liveTrackingNavigationTaskDate" name="liveTrackingNavigationTaskDate" value="${fieldValue(bean:taskInstance,field:'liveTrackingNavigationTaskDate')}" min="${taskInstance.contest.liveTrackingContestDate}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p class="warning">${message(code:'fc.task.info.resettimetable')}</p>
                            <p>
                                <label>${message(code:'fc.task.firsttime')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="firstTime" name="firstTime" value="${fieldValue(bean:taskInstance,field:'firstTime')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="firstTime" name="firstTime" value="${fieldValue(bean:taskInstance,field:'firstTime')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.normal')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="takeoffIntervalNormal" name="takeoffIntervalNormal" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalNormal')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="takeoffIntervalNormal" name="takeoffIntervalNormal" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalNormal')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.sloweraircraft')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="takeoffIntervalSlowerAircraft" name="takeoffIntervalSlowerAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalSlowerAircraft')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="takeoffIntervalSlowerAircraft" name="takeoffIntervalSlowerAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalSlowerAircraft')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.fasteraircraft')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="takeoffIntervalFasterAircraft" name="takeoffIntervalFasterAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalFasterAircraft')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="takeoffIntervalFasterAircraft" name="takeoffIntervalFasterAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalFasterAircraft')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p class="warning">${message(code:'fc.task.info.recalculatetimes')}</p>
                            <p>
                                <label>${message(code:'fc.task.planningtestduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="planningTestDuration" name="planningTestDuration" value="${fieldValue(bean:taskInstance,field:'planningTestDuration')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="planningTestDuration" name="planningTestDuration" value="${fieldValue(bean:taskInstance,field:'planningTestDuration')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.preparationduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="preparationDuration" name="preparationDuration" value="${fieldValue(bean:taskInstance,field:'preparationDuration')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="preparationDuration" name="preparationDuration" value="${fieldValue(bean:taskInstance,field:'preparationDuration')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.parkingduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="parkingDuration" name="parkingDuration" value="${fieldValue(bean:taskInstance,field:'parkingDuration')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="parkingDuration" name="parkingDuration" value="${fieldValue(bean:taskInstance,field:'parkingDuration')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.procedureturnduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="procedureTurnDuration" name="procedureTurnDuration" value="${fieldValue(bean:taskInstance,field:'procedureTurnDuration')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="procedureTurnDuration" name="procedureTurnDuration" value="${fieldValue(bean:taskInstance,field:'procedureTurnDuration')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p class="warning">${message(code:'fc.task.info.recalculatewarnings')}</p>
                            <p>
                                <label>${message(code:'fc.task.minnextflightduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
								<g:if test="${taskInstance.lockPlanning}">
									<input type="text" id="minNextFlightDuration" name="minNextFlightDuration" value="${fieldValue(bean:taskInstance,field:'minNextFlightDuration')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="minNextFlightDuration" name="minNextFlightDuration" value="${fieldValue(bean:taskInstance,field:'minNextFlightDuration')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                        </fieldset>
                    	<g:if test="${taskInstance.contest.resultClasses}">
                    	    <g:set var="no_class" value="${true}"/>
                    		<g:each var="taskclass_instance" in="${TaskClass.findAllByTask(taskInstance,[sort:"id"])}" status="i">
                                <g:set var="no_class" value="${false}"/>
		                        <fieldset>
		                            <legend>${taskclass_instance.resultclass.name}</legend>
		                            <p>
                                        <g:if test="${taskInstance.contest.showPlanningTest}">
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_PlanningTestRun}" value="${taskclass_instance.planningTestRun}" />
                                                <label>${message(code:'fc.planningtest')}</label>
                                            </div>
                                            <div style="margin-left:20px">
                                                <div>
                                                    <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_PlanningTestDistanceMeasure}" value="${taskclass_instance.planningTestDistanceMeasure}" />
                                                    <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
                                                </div>
                                                <div>
                                                    <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_PlanningTestDirectionMeasure}" value="${taskclass_instance.planningTestDirectionMeasure}" />
                                                    <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
                                                </div>
                                            </div>
                                        </g:if>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestRun}" value="${taskclass_instance.flightTestRun}" />
		    	                            <label>${message(code:'fc.flighttest')}</label>
		                                </div>
                                        <div style="margin-left:20px">
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestCheckSecretPoints}" value="${taskclass_instance.flightTestCheckSecretPoints}" />
                                                <label>${message(code:'fc.task.flighttestchecksecretpoints')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestCheckTakeOff}" value="${taskclass_instance.flightTestCheckTakeOff}" />
                                                <label>${message(code:'fc.task.flighttestchecktakeoff')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_FlightTestCheckLanding}" value="${taskclass_instance.flightTestCheckLanding}" />
                                                <label>${message(code:'fc.task.flighttestchecklanding')}</label>
                                            </div>
                                        </div>
                                        <g:if test="${taskInstance.contest.showObservationTest}">
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestRun}" value="${taskclass_instance.observationTestRun}" />
                                                <label>${message(code:'fc.observationtest')}</label>
                                            </div>
                                            <div style="margin-left:20px">
                                                <div>
                                                    <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestTurnpointRun}" value="${taskclass_instance.observationTestTurnpointRun}" />
                                                    <label>${message(code:'fc.observationtest.turnpoints')}</label>
                                                </div>
                                                <div>
                                                    <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestEnroutePhotoRun}" value="${taskclass_instance.observationTestEnroutePhotoRun}" />
                                                    <label>${message(code:'fc.observationtest.enroutephotos')}</label>
                                                </div>
                                                <div>
                                                    <g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_ObservationTestEnrouteCanvasRun}" value="${taskclass_instance.observationTestEnrouteCanvasRun}" />
                                                    <label>${message(code:'fc.observationtest.enroutecanvas')}</label>
                                                </div>
                                            </div>
                                        </g:if>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTestRun}" value="${taskclass_instance.landingTestRun}" />
		    	                            <label>${message(code:'fc.landingtest')}</label>
		                                </div>
				                        <div style="margin-left:20px">
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest1Run}" value="${taskclass_instance.landingTest1Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing1')}</label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest2Run}" value="${taskclass_instance.landingTest2Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing2')}</label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest3Run}" value="${taskclass_instance.landingTest3Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing3')}</label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_LandingTest4Run}" value="${taskclass_instance.landingTest4Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing4')}</label>
			                                </div>
	                                	</div>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_SpecialTestRun}" value="${taskclass_instance.specialTestRun}" />
		    	                            <label>${message(code:'fc.specialtest')}</label>
		    	                            <p>
                                                <input type="text" id="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_SpecialTestTitle}" name="${Defs.TaskClassID}${taskclass_instance.resultclass.id}${Defs.TaskClassSubID_SpecialTestTitle}" value="${fieldValue(bean:taskclass_instance,field:'specialTestTitle')}" tabIndex="${ti[0]++}"/>
                                            </p>
		                                </div>
		                            </p>
		                    	</fieldset>
                    		</g:each>
                    		<g:if test="${no_class}">
                                <p class="error">${message(code:'fc.task.error.noclass')}</p>
                    		</g:if>
                    	</g:if>
                    	<g:else>
	                        <fieldset>
	                            <p>
                                    <g:if test="${taskInstance.contest.showPlanningTest}">
                                        <div>
                                            <g:checkBox name="planningTestRun" value="${taskInstance.planningTestRun}" />
                                            <label>${message(code:'fc.planningtest')}</label>
                                        </div>
                                        <div style="margin-left:20px">
                                            <div>
                                                <g:checkBox name="planningTestDistanceMeasure" value="${taskInstance.planningTestDistanceMeasure}" />
                                                <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="planningTestDirectionMeasure" value="${taskInstance.planningTestDirectionMeasure}" />
                                                <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
                                            </div>
                                        </div>
                                    </g:if>
	                                <div>
		                               	<g:checkBox name="flightTestRun" value="${taskInstance.flightTestRun}" />
	    	                            <label>${message(code:'fc.flighttest')}</label>
	                                </div>
                                    <div style="margin-left:20px">
                                        <div>
                                            <g:checkBox name="flightTestCheckSecretPoints" value="${taskInstance.flightTestCheckSecretPoints}" />
                                            <label>${message(code:'fc.task.flighttestchecksecretpoints')}</label>
                                        </div>
                                        <div>
                                            <g:checkBox name="flightTestCheckTakeOff" value="${taskInstance.flightTestCheckTakeOff}" />
                                            <label>${message(code:'fc.task.flighttestchecktakeoff')}</label>
                                        </div>
                                        <div>
                                            <g:checkBox name="flightTestCheckLanding" value="${taskInstance.flightTestCheckLanding}" />
                                            <label>${message(code:'fc.task.flighttestchecklanding')}</label>
                                        </div>
                                    </div>
                                    <g:if test="${taskInstance.contest.showObservationTest}">
                                        <div>
                                            <g:checkBox name="observationTestRun" value="${taskInstance.observationTestRun}" />
                                            <label>${message(code:'fc.observationtest')}</label>
                                        </div>
                                        <div style="margin-left:20px">
                                            <div>
                                                <g:checkBox name="observationTestTurnpointRun" value="${taskInstance.observationTestTurnpointRun}" />
                                                <label>${message(code:'fc.observationtest.turnpoints')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="observationTestEnroutePhotoRun" value="${taskInstance.observationTestEnroutePhotoRun}" />
                                                <label>${message(code:'fc.observationtest.enroutephotos')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="observationTestEnrouteCanvasRun" value="${taskInstance.observationTestEnrouteCanvasRun}" />
                                                <label>${message(code:'fc.observationtest.enroutecanvas')}</label>
                                            </div>
                                        </div>
                                    </g:if>
	                                <div>
		                               	<g:checkBox name="landingTestRun" value="${taskInstance.landingTestRun}" />
	    	                            <label>${message(code:'fc.landingtest')}</label>
	                                </div>
			                        <div style="margin-left:20px">
		                                <div>
			                               	<g:checkBox name="landingTest1Run" value="${taskInstance.landingTest1Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing1')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest2Run" value="${taskInstance.landingTest2Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing2')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest3Run" value="${taskInstance.landingTest3Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing3')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest4Run" value="${taskInstance.landingTest4Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing4')}</label>
		                                </div>
	                                </div>
	                                <div>
		                               	<g:checkBox name="specialTestRun" value="${taskInstance.specialTestRun}" />
	    	                            <label>${message(code:'fc.specialtest')}</label>
	                                    <p>
	                                        <input type="text" id="specialTestTitle" name="specialTestTitle" value="${fieldValue(bean:taskInstance,field:'specialTestTitle')}" tabIndex="${ti[0]++}"/>
	                                    </p>
	                                </div>
	                            </p>
	                        </fieldset>
                    	</g:else>
                        <fieldset>
                            <label>${message(code:'fc.landingtest.landing1')}:</label>
							<g:if test="${taskInstance.lockPlanning}">
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest1Points}" name="landingTest1Points" disabled tabIndex="${ti[0]++}"/>
							</g:if>
							<g:else>
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest1Points}" name="landingTest1Points" tabIndex="${ti[0]++}"/>
							</g:else>
                            <br/>
                            <label>${message(code:'fc.landingtest.landing2')}:</label>
							<g:if test="${taskInstance.lockPlanning}">
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest2Points}" name="landingTest2Points" disabled tabIndex="${ti[0]++}"/>
							</g:if>
							<g:else>
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest2Points}" name="landingTest2Points" tabIndex="${ti[0]++}"/>
							</g:else>
                            <br/>
                            <label>${message(code:'fc.landingtest.landing3')}:</label>
							<g:if test="${taskInstance.lockPlanning}">
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest3Points}" name="landingTest3Points" disabled tabIndex="${ti[0]++}"/>
							</g:if>
							<g:else>
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest3Points}" name="landingTest3Points" tabIndex="${ti[0]++}"/>
							</g:else>
                            <br/>
                            <label>${message(code:'fc.landingtest.landing4')}:</label>
							<g:if test="${taskInstance.lockPlanning}">
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest4Points}" name="landingTest4Points" disabled tabIndex="${ti[0]++}"/>
							</g:if>
							<g:else>
								<g:select from="${[1,2,3,4]}" optionValue="${{taskInstance.GetLandingPointsText(it)}}" value="${taskInstance.landingTest4Points}" name="landingTest4Points" tabIndex="${ti[0]++}"/>
							</g:else>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
									<g:if test="${taskInstance.lockPlanning}">
										<g:checkBox name="bestOfAnalysis" value="${taskInstance.bestOfAnalysis}" disabled="disabled" />
									</g:if>
									<g:else>
										<g:checkBox name="bestOfAnalysis" value="${taskInstance.bestOfAnalysis}" />
									</g:else>
                                    <label>${message(code:'fc.task.bestofanalysis')}</label>
                                </div>
	                            <g:if test="${taskInstance.GetIncreaseValues() != ""}">
	                                <div>
										<g:if test="${taskInstance.lockPlanning}">
											<g:checkBox name="increaseEnabled" value="${taskInstance.increaseEnabled}" disabled="disabled" />
										</g:if>
										<g:else>
											<g:checkBox name="increaseEnabled" value="${taskInstance.increaseEnabled}" />
										</g:else>
	                                    <label>${message(code:'fc.task.increaseenabled',args:[taskInstance.GetIncreaseValues()])}</label>
	                                </div>
	                            </g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="lockPlanning" value="${taskInstance.lockPlanning}" />
                                    <label>${message(code:'fc.task.lockplanning')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="hidePlanning" value="${taskInstance.hidePlanning}" />
                                    <label>${message(code:'fc.task.hideplanning')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="hideResults" value="${taskInstance.hideResults}" />
                                    <label>${message(code:'fc.task.hideresults')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.addtimevalue')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="addTimeValue" name="addTimeValue" value="${fieldValue(bean:taskInstance,field:'addTimeValue')}" tabIndex="${ti[0]++}"/>
                                <a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:if test="${next_id}">
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.task.gotonext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.task.gotonext')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:if test="${prev_id}">
                            <g:actionSubmit action="gotoprev" value="${message(code:'fc.task.gotoprev')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="gotoprev" value="${message(code:'fc.task.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="savesettings" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${next_id}">
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.task.updatenext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.task.updatenext')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="copy" value="${message(code:'fc.copy')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${taskInstance.lockPlanning}">
							<g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" disabled tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
							<g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>