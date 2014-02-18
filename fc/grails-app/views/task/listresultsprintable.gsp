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
                @top-center {
                    <g:if test="${resultclassInstance}">
                        content: "${resultclassInstance.GetPrintTitle2('fc.test.results')} - ${message(code:'fc.program.printpage')} " counter(page)
                    </g:if>
                    <g:else>
                        content: "${message(code:'fc.test.results')} - ${taskInstance.name()} - ${message(code:'fc.program.printpage')} " counter(page)
                    </g:else>
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
       	<g:if test="${resultclassInstance}">
       		<title>${resultclassInstance.GetPrintTitle('fc.test.results')}</title>
       	</g:if>
       	<g:else>
        	<title>${message(code:'fc.test.results')}</title>
        </g:else>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
		       	<g:if test="${resultclassInstance}">
		       		<h2>${resultclassInstance.GetPrintTitle('fc.test.results')}<g:if test="${taskInstance.IsTaskClassResultsProvisional(taskInstance.GetClassResultSettings(resultclassInstance),resultclassInstance)}"> [${message(code:'fc.provisional')}]</g:if></h2>
		       	</g:if>
       			<g:else>
                	<h2>${message(code:'fc.test.results')}<g:if test="${taskInstance.IsTaskResultsProvisional(taskInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></h2>
                </g:else>
               	<h3>${taskInstance.name()}</h3>
                <div class="block" id="forms" >
                    <g:form>
                    	<g:if test="${resultclassInstance}">
	                   		<br/>
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                        	<g:set var="taskclass_instance" value="${taskInstance.GetTaskClass(resultclassInstance)}"></g:set>
		                        <g:set var="results_columns" value="${taskclass_instance.GetResultColumns()}"></g:set>
		                        <g:set var="landing_result_any_columns" value="${taskclass_instance.GetLandingResultAnyColumns()}"></g:set>
	                            <thead>
	                                <tr>
	    	                        	<th>${message(code:'fc.test.results.position.short')}</th>
	                                   	<th>${message(code:'fc.crew')}</th>
	                                   	<th>${message(code:'fc.aircraft')}</th>
	                                   	<th>${message(code:'fc.team')}</th>
		                               	<g:if test="${taskclass_instance.planningTestRun}">
	    	                            	<th>${message(code:'fc.planningresults.planning.short')}</th>
		                                </g:if>
	                                	<g:if test="${taskclass_instance.flightTestRun}">
	                                   		<th>${message(code:'fc.flightresults.flight.short')}</th>
		                                </g:if>
		                                <g:if test="${taskclass_instance.observationTestRun}">
		                                    <th>${message(code:'fc.observationresults.observations.short')}</th>
		                                </g:if>
	                                	<g:if test="${taskclass_instance.landingTestRun}">
			                                <g:if test="${landing_result_any_columns > 0}">
		                                		<th colspan="${landing_result_any_columns}">${message(code:'fc.landingresults.landing')}</th>
			                                </g:if>
			                                <g:else>
		                                		<th>${message(code:'fc.landingresults.landing.short')}</th>
			                                </g:else>
	                                	</g:if>
		                                <g:if test="${taskclass_instance.specialTestRun}">
		                                    <th>${message(code:'fc.specialresults.other.short')}</th>
		                                </g:if>
		                                <g:if test="${results_columns > 1}">
	                                    	<th>${message(code:'fc.test.results.summary.short')}</th>
		                                </g:if>
	                                </tr>
	                                <g:if test="${landing_result_any_columns > 0}">
		                                <tr>
		                                	<th/>
		                                	<th/>
		                                	<th/>
		                                	<th/>
			                               	<g:if test="${taskclass_instance.planningTestRun}">
			                                	<th/>
			                               	</g:if>
		                                	<g:if test="${taskclass_instance.flightTestRun}">
			                                	<th/>
		                                	</g:if>
			                                <g:if test="${taskclass_instance.observationTestRun}">
			                                	<th/>
			                                </g:if>
			                                <g:if test="${taskclass_instance.landingTest1Run}">
			                                    <th>${message(code:'fc.landingresults.landing1')}</th>
			                                </g:if>
			                                <g:if test="${taskclass_instance.landingTest2Run}">
			                                    <th>${message(code:'fc.landingresults.landing2')}</th>
			                                </g:if>
			                                <g:if test="${taskclass_instance.landingTest3Run}">
			                                    <th>${message(code:'fc.landingresults.landing3')}</th>
			                                </g:if>
			                                <g:if test="${taskclass_instance.landingTest4Run}">
			                                    <th>${message(code:'fc.landingresults.landing4')}</th>
			                                </g:if>
			                                <g:if test="${taskclass_instance.specialTestRun}">
			                                	<th/>
			                                </g:if>
			                                <g:if test="${results_columns > 1}">
			                                	<th/>
			                                </g:if>
			                        	</tr>
			                        </g:if>
	                            </thead>
	                            <tbody>
	                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskPosition'])}">
	                                	<g:if test="${test_instance.crew.resultclass == resultclassInstance}">
		                                   	<g:if test="${!test_instance.crew.disabled}">
			                                    <tr class="even">
			                                        <td>${test_instance.taskPosition}</td>
			                                        <td>${test_instance.crew.name}</td>
			                                    	<td><g:if test="${test_instance.taskAircraft}">${test_instance.taskAircraft.registration}</g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
			                                    	<g:if test="${test_instance.crew.team}">
			                                    	    <td>${test_instance.crew.team.name}</td>
			                                    	</g:if>
		                                            <g:else>
		                                                <td>-</td>
		                                            </g:else>
					                               	<g:if test="${test_instance.IsPlanningTestRun()}">
			                                        	<td>${test_instance.planningTestPenalties}<g:if test="${!test_instance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
				                                	<g:if test="${test_instance.IsFlightTestRun()}">
				                                		<td>${test_instance.flightTestPenalties}<g:if test="${!test_instance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsObservationTestRun()}">
					                                	<td>${test_instance.observationTestPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsLandingTestRun() && !test_instance.IsLandingTestAnyRun()}">
					                                	<td>${test_instance.landingTestPenalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest1Run()}">
					                                	<td>${test_instance.landingTest1Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest2Run()}">
					                                	<td>${test_instance.landingTest2Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest3Run()}">
					                                	<td>${test_instance.landingTest3Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest4Run()}">
					                                	<td>${test_instance.landingTest4Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${test_instance.IsSpecialTestRun()}">
					                                	<td>${test_instance.specialTestPenalties}<g:if test="${!test_instance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
					                                </g:if>
					                                <g:if test="${results_columns > 1}">
			    	                                    <td>${test_instance.taskPenalties}<g:if test="${test_instance.IsTestResultsProvisional(test_instance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></td>
			    	                                </g:if>
			                                    </tr>
			                                </g:if>
			                        	</g:if> 
	                                </g:each>
	                            </tbody>
	                        </table>
                    	</g:if>
                    	<g:else>
	                   		<br/>
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
		                        <g:set var="results_columns" value="${taskInstance.GetResultColumns()}"></g:set>
		                        <g:set var="landing_result_any_columns" value="${taskInstance.GetLandingResultAnyColumns()}"></g:set>
	                            <thead>
	                                <tr>
	    	                        	<th>${message(code:'fc.test.results.position.short')}</th>
	                                   	<th>${message(code:'fc.crew')}</th>
	                                   	<th>${message(code:'fc.aircraft')}</th>
	                                   	<th>${message(code:'fc.team')}</th>
		                               	<g:if test="${taskInstance.IsPlanningTestRun()}">
	    	                            	<th>${message(code:'fc.planningresults.planning.short')}</th>
		                                </g:if>
	                                	<g:if test="${taskInstance.IsFlightTestRun()}">
	                                   		<th>${message(code:'fc.flightresults.flight.short')}</th>
		                                </g:if>
		                                <g:if test="${taskInstance.IsObservationTestRun()}">
		                                    <th>${message(code:'fc.observationresults.observations.short')}</th>
		                                </g:if>
		                                <g:if test="${taskInstance.IsLandingTestRun()}">
			                                <g:if test="${landing_result_any_columns > 0}">
		                                		<th colspan="${landing_result_any_columns}">${message(code:'fc.landingresults.landing')}</th>
			                                </g:if>
			                                <g:else>
		                                		<th>${message(code:'fc.landingresults.landing.short')}</th>
			                                </g:else>
		                                </g:if>
		                                <g:if test="${taskInstance.IsSpecialTestRun()}">
		                                    <th>${message(code:'fc.specialresults.other.short')}</th>
		                                </g:if>
		                                <g:if test="${results_columns > 1}">
	                                    	<th>${message(code:'fc.test.results.summary.short')}</th>
		                                </g:if>
	                                </tr>
	                                <g:if test="${landing_result_any_columns > 0}">
		                                <tr>
		                                	<th/>
		                                	<th/>
		                                	<th/>
		                                	<th/>
			                               	<g:if test="${taskInstance.IsPlanningTestRun()}">
			                                	<th/>
			                                </g:if>
		                                	<g:if test="${taskInstance.IsFlightTestRun()}">
			                                	<th/>
			                                </g:if>
			                                <g:if test="${taskInstance.IsObservationTestRun()}">
			                                	<th/>
			                                </g:if>
			                                <g:if test="${taskInstance.IsLandingTest1Run()}">
			                                    <th>${message(code:'fc.landingresults.landing1')}</th>
			                                </g:if>
			                                <g:if test="${taskInstance.IsLandingTest2Run()}">
			                                    <th>${message(code:'fc.landingresults.landing2')}</th>
			                                </g:if>
			                                <g:if test="${taskInstance.IsLandingTest3Run()}">
			                                    <th>${message(code:'fc.landingresults.landing3')}</th>
			                                </g:if>
			                                <g:if test="${taskInstance.IsLandingTest4Run()}">
			                                    <th>${message(code:'fc.landingresults.landing4')}</th>
			                                </g:if>
			                                <g:if test="${taskInstance.IsSpecialTestRun()}">
			                                    <th/>
			                                </g:if>
			                                <g:if test="${results_columns > 1}">
		                                    	<th/>
			                                </g:if>
		                                </tr>
	                                </g:if>
	                            </thead>
	                            <tbody>
	                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'taskPosition'])}">
	                                   	<g:if test="${!test_instance.crew.disabled}">
		                                    <tr class="even">
		                                        <td>${test_instance.taskPosition}</td>
		                                        <td>${test_instance.crew.name}</td>
			                                    <td><g:if test="${test_instance.taskAircraft}">${test_instance.taskAircraft.registration}</g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
			                                    <g:if test="${test_instance.crew.team}">
			                                        <td>${test_instance.crew.team.name}</td>
			                                    </g:if>
	                                            <g:else>
	                                                <td>-</td>
	                                            </g:else>
				                               	<g:if test="${test_instance.IsPlanningTestRun()}">
		                                        	<td>${test_instance.planningTestPenalties}<g:if test="${!test_instance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
			                                	<g:if test="${test_instance.IsFlightTestRun()}">
			                                		<td>${test_instance.flightTestPenalties}<g:if test="${!test_instance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsObservationTestRun()}">
				                                	<td>${test_instance.observationTestPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsLandingTestRun() && !test_instance.IsLandingTestAnyRun()}">
				                                	<td>${test_instance.landingTestPenalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest1Run()}">
				                                	<td>${test_instance.landingTest1Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest2Run()}">
				                                	<td>${test_instance.landingTest2Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest3Run()}">
				                                	<td>${test_instance.landingTest3Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest4Run()}">
				                                	<td>${test_instance.landingTest4Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${test_instance.IsSpecialTestRun()}">
				                                	<td>${test_instance.specialTestPenalties}<g:if test="${!test_instance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
				                                </g:if>
				                                <g:if test="${results_columns > 1}">
		    	                                    <td>${test_instance.taskPenalties}<g:if test="${test_instance.IsTestResultsProvisional(test_instance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></td>
		    	                                </g:if>
		                                    </tr>
		                                </g:if> 
	                                </g:each>
	                            </tbody>
	                        </table>
	                	</g:else>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>