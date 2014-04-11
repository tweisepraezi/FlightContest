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
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${taskInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:taskInstance,field:'title')}" tabIndex="1"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p class="warning">${message(code:'fc.task.info.resettimetable')}</p>
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
                            <p class="warning">${message(code:'fc.task.info.recalculatetimes')}</p>
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
                            <p class="warning">${message(code:'fc.task.info.recalculatewarnings')}</p>
                            <p>
                                <label>${message(code:'fc.task.minnextflightduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="minNextFlightDuration" name="minNextFlightDuration" value="${fieldValue(bean:taskInstance,field:'minNextFlightDuration')}" tabIndex="14"/>
                            </p>
                        </fieldset>
                    	<g:if test="${taskInstance.contest.resultClasses}">
                    		<g:each var="taskclass_instance" in="${TaskClass.findAllByTask(taskInstance,[sort:"id"])}" status="i">
		                        <fieldset>
		                            <legend>${taskclass_instance.resultclass.name}</legend>
		                            <p>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_planningTestRun" value="${taskclass_instance.planningTestRun}" />
		    	                            <label>${message(code:'fc.planningtest')}</label>
		                                </div>
	                                    <div style="margin-left:20px">
	                                        <div>
	                                            <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_planningTestDistanceMeasure" value="${taskclass_instance.planningTestDistanceMeasure}" />
	                                            <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
	                                        </div>
	                                        <div>
	                                            <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_planningTestDirectionMeasure" value="${taskclass_instance.planningTestDirectionMeasure}" />
	                                            <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
	                                        </div>
	                                    </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_flightTestRun" value="${taskclass_instance.flightTestRun}" />
		    	                            <label>${message(code:'fc.flighttest')}</label>
		                                </div>
                                        <div style="margin-left:20px">
                                            <div>
                                                <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_flightTestCheckSecretPoints" value="${taskclass_instance.flightTestCheckSecretPoints}" />
                                                <label>${message(code:'fc.task.flighttestchecksecretpoints')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_flightTestCheckTakeOff" value="${taskclass_instance.flightTestCheckTakeOff}" />
                                                <label>${message(code:'fc.task.flighttestchecktakeoff')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_flightTestCheckLanding" value="${taskclass_instance.flightTestCheckLanding}" />
                                                <label>${message(code:'fc.task.flighttestchecklanding')}</label>
                                            </div>
                                        </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_observationTestRun" value="${taskclass_instance.observationTestRun}" />
		    	                            <label>${message(code:'fc.observationtest')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTestRun" value="${taskclass_instance.landingTestRun}" />
		    	                            <label>${message(code:'fc.landingtest')}</label>
		                                </div>
				                        <div style="margin-left:20px">
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest1Run" value="${taskclass_instance.landingTest1Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing1')}<g:if test="${taskclass_instance.resultclass.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest2Run" value="${taskclass_instance.landingTest2Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing2')}<g:if test="${taskclass_instance.resultclass.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest3Run" value="${taskclass_instance.landingTest3Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing3')}<g:if test="${taskclass_instance.resultclass.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest4Run" value="${taskclass_instance.landingTest4Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing4')}<g:if test="${taskclass_instance.resultclass.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></label>
			                                </div>
	                                	</div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_specialTestRun" value="${taskclass_instance.specialTestRun}" />
		    	                            <label>${message(code:'fc.specialtest')}</label>
		    	                            <p>
                                                <input type="text" id="specialTestTitle${taskclass_instance.resultclass.id}" name="specialTestTitle${taskclass_instance.resultclass.id}" value="${fieldValue(bean:taskclass_instance,field:'specialTestTitle')}" tabIndex="${15+i}"/>
                                            </p>
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
	                                <div>
		                               	<g:checkBox name="landingTestRun" value="${taskInstance.landingTestRun}" />
	    	                            <label>${message(code:'fc.landingtest')}</label>
	                                </div>
			                        <div style="margin-left:20px">
		                                <div>
			                               	<g:checkBox name="landingTest1Run" value="${taskInstance.landingTest1Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing1')}<g:if test="${taskInstance.contest.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest2Run" value="${taskInstance.landingTest2Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing2')}<g:if test="${taskInstance.contest.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest3Run" value="${taskInstance.landingTest3Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing3')}<g:if test="${taskInstance.contest.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest4Run" value="${taskInstance.landingTest4Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing4')}<g:if test="${taskInstance.contest.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></label>
		                                </div>
	                                </div>
	                                <div>
		                               	<g:checkBox name="specialTestRun" value="${taskInstance.specialTestRun}" />
	    	                            <label>${message(code:'fc.specialtest')}</label>
	                                    <p>
	                                        <input type="text" id="specialTestTitle" name="specialTestTitle" value="${fieldValue(bean:taskInstance,field:'specialTestTitle')}" tabIndex="15"/>
	                                    </p>
	                                </div>
	                            </p>
	                        </fieldset>
                    	</g:else>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="bestOfAnalysis" value="${taskInstance.bestOfAnalysis}" />
                                    <label>${message(code:'fc.task.bestofanalysis')}</label>
                                </div>
                            </p>
                            <p>
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
                                <input type="text" id="addTimeValue" name="addTimeValue" value="${fieldValue(bean:taskInstance,field:'addTimeValue')}" tabIndex="101"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="102"/>
                        <g:actionSubmit action="copy" value="${message(code:'fc.copy')}" tabIndex="103"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="104"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="105"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>