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
                                <label>${message(code:'fc.task.risingduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="risingDuration" name="risingDuration" value="${fieldValue(bean:taskInstance,field:'risingDuration')}" tabIndex="4"/>
                            </p>
                        </fieldset>
                    	<g:if test="${taskInstance.contest.resultClasses}">
                    		<g:each var="taskclass_instance" in="${TaskClass.findAllByTask(taskInstance)}" status="i">
		                        <fieldset>
		                            <legend>${taskclass_instance.resultclass.name}</legend>
		                            <p>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_planningTestRun" value="${taskclass_instance.planningTestRun}" />
		    	                            <label>${message(code:'fc.planningresults')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_flightTestRun" value="${taskclass_instance.flightTestRun}" />
		    	                            <label>${message(code:'fc.flightresults')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_observationTestRun" value="${taskclass_instance.observationTestRun}" />
		    	                            <label>${message(code:'fc.observationresults')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTestRun" value="${taskclass_instance.landingTestRun}" />
		    	                            <label>${message(code:'fc.landingresults')}</label>
		                                </div>
				                        <div style="margin-left:20px">
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest1Run" value="${taskclass_instance.landingTest1Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing1')}<g:if test="${taskclass_instance.resultclass?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest2Run" value="${taskclass_instance.landingTest2Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing2')}<g:if test="${taskclass_instance.resultclass?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest3Run" value="${taskclass_instance.landingTest3Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing3')}<g:if test="${taskclass_instance.resultclass?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_landingTest4Run" value="${taskclass_instance.landingTest4Run}" />
			    	                            <label>${message(code:'fc.landingtest.landing4')}<g:if test="${taskclass_instance.resultclass?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></label>
			                                </div>
	                                	</div>
		                                <div>
			                               	<g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_specialTestRun" value="${taskclass_instance.specialTestRun}" />
		    	                            <label>${message(code:'fc.specialresults')}</label>
		                                </div>
		                            </p>
		                            <p>
		                                <div>
		                                    <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_planningTestDistanceMeasure" value="${taskclass_instance.planningTestDistanceMeasure}" />
		                                    <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
		                                </div>
		                                <div>
		                                    <g:checkBox name="taskclass_${taskclass_instance.resultclass.id}_planningTestDirectionMeasure" value="${taskclass_instance.planningTestDirectionMeasure}" />
		                                    <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
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
	    	                            <label>${message(code:'fc.planningresults')}</label>
	                                </div>
	                                <div>
		                               	<g:checkBox name="flightTestRun" value="${taskInstance.flightTestRun}" />
	    	                            <label>${message(code:'fc.flightresults')}</label>
	                                </div>
	                                <div>
		                               	<g:checkBox name="observationTestRun" value="${taskInstance.observationTestRun}" />
	    	                            <label>${message(code:'fc.observationresults')}</label>
	                                </div>
	                                <div>
		                               	<g:checkBox name="landingTestRun" value="${taskInstance.landingTestRun}" />
	    	                            <label>${message(code:'fc.landingresults')}</label>
	                                </div>
			                        <div style="margin-left:20px">
		                                <div>
			                               	<g:checkBox name="landingTest1Run" value="${taskInstance.landingTest1Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing1')}<g:if test="${taskInstance.contest?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest2Run" value="${taskInstance.landingTest2Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing2')}<g:if test="${taskInstance.contest?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest3Run" value="${taskInstance.landingTest3Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing3')}<g:if test="${taskInstance.contest?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest4Run" value="${taskInstance.landingTest4Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing4')}<g:if test="${taskInstance.contest?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></label>
		                                </div>
	                                </div>
	                                <div>
		                               	<g:checkBox name="specialTestRun" value="${taskInstance.specialTestRun}" />
	    	                            <label>${message(code:'fc.specialresults')}</label>
	                                </div>
	                            </p>
	                            <p>
	                                <div>
	                                    <g:checkBox name="planningTestDistanceMeasure" value="${taskInstance.planningTestDistanceMeasure}" />
	                                    <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
	                                </div>
	                                <div>
	                                    <g:checkBox name="planningTestDirectionMeasure" value="${taskInstance.planningTestDirectionMeasure}" />
	                                    <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
	                                </div>
	                            </p>
	                        </fieldset>
                    	</g:else>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.addtimevalue')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="addTimeValue" name="addTimeValue" value="${fieldValue(bean:taskInstance,field:'addTimeValue')}" tabIndex="5"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.fasteraircraft')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalFasterAircraft" name="takeoffIntervalFasterAircraft" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalFasterAircraft')}" tabIndex="6"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.planningtestduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="planningTestDuration" name="planningTestDuration" value="${fieldValue(bean:taskInstance,field:'planningTestDuration')}" tabIndex="7"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.preparationduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="preparationDuration" name="preparationDuration" value="${fieldValue(bean:taskInstance,field:'preparationDuration')}" tabIndex="8"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.maxlandingduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="maxLandingDuration" name="maxLandingDuration" value="${fieldValue(bean:taskInstance,field:'maxLandingDuration')}" tabIndex="9"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.parkingduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="parkingDuration" name="parkingDuration" value="${fieldValue(bean:taskInstance,field:'parkingDuration')}" tabIndex="10"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.minnextflightduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="minNextFlightDuration" name="minNextFlightDuration" value="${fieldValue(bean:taskInstance,field:'minNextFlightDuration')}" tabIndex="11"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.procedureturnduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="procedureTurnDuration" name="procedureTurnDuration" value="${fieldValue(bean:taskInstance,field:'procedureTurnDuration')}" tabIndex="12"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="13"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="14"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="15"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>