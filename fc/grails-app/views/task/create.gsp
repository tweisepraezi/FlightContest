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
                    <g:set var="newparams" value="['contestid':params.contestid]"/>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
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
                        <g:if test="${contestInstance.resultClasses}">
                    		<g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance)}" status="i">
		                        <fieldset>
		                            <legend>${resultclass_instance.name}</legend>
		                            <p>
		                                <div>
			                               	<g:checkBox name="taskclass_${resultclass_instance.id}_planningTestRun" value="${true}" />
		    	                            <label>${message(code:'fc.planningresults')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${resultclass_instance.id}_flightTestRun" value="${true}" />
		    	                            <label>${message(code:'fc.flightresults')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${resultclass_instance.id}_observationTestRun" value="${true}" />
		    	                            <label>${message(code:'fc.observationresults')}</label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="taskclass_${resultclass_instance.id}_landingTestRun" value="${true}" />
		    	                            <label>${message(code:'fc.landingresults')}</label>
		                                </div>
				                        <div style="margin-left:20px">
			                                <div>
				                               	<g:checkBox name="taskclass_${resultclass_instance.id}_landingTest1Run" value="${false}" />
			    	                            <label>${message(code:'fc.landingtest.landing1')}<g:if test="${resultclass_instance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${resultclass_instance.id}_landingTest2Run" value="${false}" />
			    	                            <label>${message(code:'fc.landingtest.landing2')}<g:if test="${resultclass_instance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${resultclass_instance.id}_landingTest3Run" value="${false}" />
			    	                            <label>${message(code:'fc.landingtest.landing3')}<g:if test="${resultclass_instance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></label>
			                                </div>
			                                <div>
				                               	<g:checkBox name="taskclass_${resultclass_instance.id}_landingTest4Run" value="${false}" />
			    	                            <label>${message(code:'fc.landingtest.landing4')}<g:if test="${resultclass_instance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></label>
			                                </div>
	                                	</div>
		                                <div>
			                               	<g:checkBox name="taskclass_${resultclass_instance.id}_specialTestRun" value="${false}" />
		    	                            <label>${message(code:'fc.specialresults')}</label>
		                                </div>
		                            </p>
		                            <p>
		                                <div>
		                                    <g:checkBox name="taskclass_${resultclass_instance.id}_planningTestDistanceMeasure" value="${false}" />
		                                    <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
		                                </div>
		                                <div>
		                                    <g:checkBox name="taskclass_${resultclass_instance.id}_planningTestDirectionMeasure" value="${true}" />
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
		    	                            <label>${message(code:'fc.landingtest.landing1')}<g:if test="${contestInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest2Run" value="${taskInstance.landingTest2Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing2')}<g:if test="${contestInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest3Run" value="${taskInstance.landingTest3Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing3')}<g:if test="${contestInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></label>
		                                </div>
		                                <div>
			                               	<g:checkBox name="landingTest4Run" value="${taskInstance.landingTest4Run}" />
		    	                            <label>${message(code:'fc.landingtest.landing4')}<g:if test="${contestInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></label>
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
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="5"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="6"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>