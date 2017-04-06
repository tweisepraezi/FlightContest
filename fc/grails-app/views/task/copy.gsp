<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.create')}</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:taskInstance,field:'title')}" tabIndex="1"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.firsttime')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="firstTime" name="firstTime" value="${fieldValue(bean:taskInstance,field:'firstTime')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.normal')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalNormal" name="takeoffIntervalNormal" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalNormal')}" tabIndex="3"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.sloweraircraft')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalSlowerAircraft" name="takeoffIntervalSlowerAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalSlowerAircraft')}" tabIndex="4"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.fasteraircraft')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalFasterAircraft" name="takeoffIntervalFasterAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalFasterAircraft')}" tabIndex="5"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.planningtestduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="planningTestDuration" name="planningTestDuration" value="${fieldValue(bean:taskInstance,field:'planningTestDuration')}" tabIndex="6"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.preparationduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="preparationDuration" name="preparationDuration" value="${fieldValue(bean:taskInstance,field:'preparationDuration')}" tabIndex="7"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.risingduration')}*:</label>
                                <br/>
                                <input type="text" id="risingDurationFormula" name="risingDurationFormula" value="${fieldValue(bean:taskInstance,field:'risingDurationFormula')}" tabIndex="8"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.maxlandingduration')}*:</label>
                                <br/>
                                <input type="text" id="maxLandingDurationFormula" name="maxLandingDurationFormula" value="${fieldValue(bean:taskInstance,field:'maxLandingDurationFormula')}" tabIndex="9"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.parkingduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="parkingDuration" name="parkingDuration" value="${fieldValue(bean:taskInstance,field:'parkingDuration')}" tabIndex="10"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.procedureturnduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="procedureTurnDuration" name="procedureTurnDuration" value="${fieldValue(bean:taskInstance,field:'procedureTurnDuration')}" tabIndex="11"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.ilandingduration')}*:</label>
                                <br/>
                                <input type="text" id="iLandingDurationFormula" name="iLandingDurationFormula" value="${fieldValue(bean:taskInstance,field:'iLandingDurationFormula')}" tabIndex="12"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.irisingduration')}*:</label>
                                <br/>
                                <input type="text" id="iRisingDurationFormula" name="iRisingDurationFormula" value="${fieldValue(bean:taskInstance,field:'iRisingDurationFormula')}" tabIndex="13"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.minnextflightduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="minNextFlightDuration" name="minNextFlightDuration" value="${fieldValue(bean:taskInstance,field:'minNextFlightDuration')}" tabIndex="14"/>
                            </p>
                        </fieldset>
                        <g:if test="${contestInstance.resultClasses}">
                    		<g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance,[sort:"id"])}" status="i">
		                        <fieldset>
		                            <legend>${resultclass_instance.name}</legend>
		                            <p>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_PlanningTestRun)}" />
		    	                            <label>${message(code:'fc.planningtest')}</label>
		                                </div>
                                        <div style="margin-left:20px">
	                                        <div>
	                                            <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestDistanceMeasure}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_PlanningTestDistanceMeasure)}" />
	                                            <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
	                                        </div>
	                                        <div>
	                                            <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_PlanningTestDirectionMeasure}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_PlanningTestDirectionMeasure)}" />
	                                            <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
	                                        </div>
                                        </div>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_FlightTestRun)}" />
		    	                            <label>${message(code:'fc.flighttest')}</label>
		                                </div>
                                        <div style="margin-left:20px">
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckSecretPoints}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_FlightTestCheckSecretPoints)}" />
                                                <label>${message(code:'fc.task.flighttestchecksecretpoints')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckTakeOff}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_FlightTestCheckTakeOff)}" />
                                                <label>${message(code:'fc.task.flighttestchecktakeoff')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_FlightTestCheckLanding}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_FlightTestCheckLanding)}" />
                                                <label>${message(code:'fc.task.flighttestchecklanding')}</label>
                                            </div>
                                        </div>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_ObservationTestRun)}" />
		    	                            <label>${message(code:'fc.observationtest')}</label>
		                                </div>
                                        <div style="margin-left:20px">
	                                        <div>
	                                            <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestTurnpointRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_ObservationTestTurnpointRun)}" />
	                                            <label>${message(code:'fc.observationtest.turnpoints')}</label>
	                                        </div>
	                                        <div>
	                                            <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestEnroutePhotoRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_ObservationTestEnroutePhotoRun)}" />
	                                            <label>${message(code:'fc.observationtest.enroutephotos')}</label>
	                                        </div>
	                                        <div>
	                                            <g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_ObservationTestEnrouteCanvasRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_ObservationTestEnrouteCanvasRun)}" />
	                                            <label>${message(code:'fc.observationtest.enroutecanvas')}</label>
	                                        </div>
                                        </div>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTestRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_LandingTestRun)}" />
		    	                            <label>${message(code:'fc.landingtest')}</label>
		                                </div>
				                        <div style="margin-left:20px">
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest1Run}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_LandingTest1Run)}" />
			    	                            <label>${message(code:'fc.landingtest.landing1')}</label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest2Run}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_LandingTest2Run)}" />
			    	                            <label>${message(code:'fc.landingtest.landing2')}</label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest3Run}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_LandingTest3Run)}" />
			    	                            <label>${message(code:'fc.landingtest.landing3')}</label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_LandingTest4Run}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_LandingTest4Run)}" />
			    	                            <label>${message(code:'fc.landingtest.landing4')}</label>
			                                </div>
	                                	</div>
		                                <div>
			                               	<g:checkBox name="${Defs.TaskClassID}${resultclass_instance.id}${Defs.TaskClassSubID_SpecialTestRun}" value="${taskClassSettings.(Defs.TaskClassID+resultclass_instance.id+Defs.TaskClassSubID_SpecialTestRun)}" />
		    	                            <label>${message(code:'fc.specialtest')}</label>
		                                </div>
		                            </p>
		                    	</fieldset>
                    		</g:each>
                    	</g:if>
                    	<g:else>
	                        <fieldset>
	                            <p>
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
	                                </div>
	                            </p>
	                    	</fieldset>
	                   	</g:else>
                        <fieldset>
                            <label>${message(code:'fc.landingtest.landing1')}:</label>
                            <g:select from="${[1,2,3,4]}" optionValue="${{message(code:taskInstance.GetLandingPointsText(it))}}" value="${taskInstance.landingTest1Points}" name="landingTest1Points" tabIndex="95"/>
                            <br/>
                            <label>${message(code:'fc.landingtest.landing2')}:</label>
                            <g:select from="${[1,2,3,4]}" optionValue="${{message(code:taskInstance.GetLandingPointsText(it))}}" value="${taskInstance.landingTest2Points}" name="landingTest2Points" tabIndex="96"/>
                            <br/>
                            <label>${message(code:'fc.landingtest.landing3')}:</label>
                            <g:select from="${[1,2,3,4]}" optionValue="${{message(code:taskInstance.GetLandingPointsText(it))}}" value="${taskInstance.landingTest3Points}" name="landingTest3Points" tabIndex="97"/>
                            <br/>
                            <label>${message(code:'fc.landingtest.landing4')}:</label>
                            <g:select from="${[1,2,3,4]}" optionValue="${{message(code:taskInstance.GetLandingPointsText(it))}}" value="${taskInstance.landingTest4Points}" name="landingTest4Points" tabIndex="98"/>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="bestOfAnalysis" value="${taskInstance.bestOfAnalysis}" />
                                    <label>${message(code:'fc.task.bestofanalysis')}</label>
                                </div>
                                <g:if test="${taskInstance.GetIncreaseValues() != ""}">
                                    <div>
                                        <g:checkBox name="increaseEnabled" value="${taskInstance.increaseEnabled}" />
                                        <label>${message(code:'fc.task.increaseenabled',args:[taskInstance.GetIncreaseValues()])}</label>
                                    </div>
                                </g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.addtimevalue')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="addTimeValue" name="addTimeValue" value="${fieldValue(bean:taskInstance,field:'addTimeValue')}" tabIndex="101"/>
                            </p>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="102"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="103"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>