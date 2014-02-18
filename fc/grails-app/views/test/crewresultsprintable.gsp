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
                    content: "${testInstance.GetStartNum()}: ${testInstance.crew.name} (${testInstance.GetViewPos()}-" counter(page) ")"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewresults')} ${testInstance.GetStartNum()}</h2>
                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetCrewResultsVersion()})<g:if test="${testInstance.IsTestResultsProvisional(testInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></h3>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if> <g:else>
                                         <td/>
                                    </g:else>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if> <g:else>
                                         <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.taskAircraft}">
                                            ${testInstance.taskAircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.taskAircraft}">
		                                    ${testInstance.taskAircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        
	                    <!-- summary -->
	                    <br/>
                        <table>
                        	<g:set var="taskPenalties" value="${new Integer(0)}" />
                            <tbody>
	                           	<g:if test="${testInstance.IsPlanningTestRun() && testInstance.printPlanningResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.planningTestPenalties}" />
    	                        	<tr>
        	                            <td>${message(code:'fc.planningresults.planning')}: ${testInstance.planningTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})<g:if test="${!testInstance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                     	</tr>
	                            </g:if>
   	                        	<g:if test="${testInstance.IsFlightTestRun() && testInstance.printFlightResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.flightTestPenalties}" />
			                    	<tr>
			                     		<td>${message(code:'fc.flightresults.flight')}: ${testInstance.flightTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})<g:if test="${!testInstance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsObservationTestRun() && testInstance.printObservationResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.observationTestPenalties}" />
			                    	<tr>
			                     		<td>${message(code:'fc.observationresults.observations')}: ${testInstance.observationTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})<g:if test="${!testInstance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsLandingTestRun() && testInstance.printLandingResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.landingTestPenalties}" />
			                    	<tr>
			                     		<td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
			                    </g:if>
	                           	<g:if test="${testInstance.IsSpecialTestRun() && testInstance.printSpecialResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.specialTestPenalties}" />
			                     	<tr>
			                     		<td>${message(code:'fc.specialresults.other')}: ${testInstance.specialTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})<g:if test="${!testInstance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                                <tr>
                                	<td> </td>
                                </tr>
	                        </tbody>
                            <tfoot>
                                <tr>
                                    <td>${message(code:'fc.penalties.total')}: ${taskPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tfoot>
                        </table>
	                    <br/>
						                        
	                    <!-- planning test --> 
                        <g:if test="${testInstance.IsPlanningTestRun() && testInstance.printPlanningResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.planningTestComplete}">
					                <h3>${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})</h3>
				                </g:else>
	                            <g:planningtaskTestPrintable t="${testInstance}"/>
	                        </div>
	                    </g:if>
	                    
	                    <!-- flight test --> 
	                    <g:if test="${testInstance.IsFlightTestRun() && testInstance.printFlightResults}" >
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.flightTestComplete}">
				                	<h3>${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})</h3>
				                </g:else>
	                            <g:flightTestPrintable t="${testInstance}"/>
	                        </div>
			            </g:if>

	                    <!-- observation test --> 
	                    <g:if test="${testInstance.IsObservationTestRun() && testInstance.printObservationResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.observationTestComplete}">
					                <h3>${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})</h3>
				                </g:else>
				                <g:observationTestPrintable t="${testInstance}"/>
		                	</div>
	                    </g:if>
	                    
	                    <!-- landing test --> 
	                    <g:if test="${testInstance.IsLandingTestRun() && testInstance.printLandingResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.landingTestComplete}">
					                <h3>${message(code:'fc.landingresults')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.landingresults')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</h3>
				                </g:else>
		                       	<g:if test="${testInstance.IsLandingTest1Run()}">
                                    <g:landingTest1Printable t="${testInstance}"/>
			                        <br/>
		                       	</g:if>
		                       	<g:if test="${testInstance.IsLandingTest2Run()}">
                                    <g:landingTest2Printable t="${testInstance}"/>
			                        <br/>
		                       	</g:if>
		                       	<g:if test="${testInstance.IsLandingTest3Run()}">
                                    <g:landingTest3Printable t="${testInstance}"/>
			                        <br/>
		                       	</g:if>
		                       	<g:if test="${testInstance.IsLandingTest4Run()}">
                                    <g:landingTest4Printable t="${testInstance}"/>
			                        <br/>
		                       	</g:if>
                                <g:landingTestSummaryPrintable t="${testInstance}"/>
		                	</div>
	                    </g:if>
	                    
	                    <!-- special test --> 
	                    <g:if test="${testInstance.IsSpecialTestRun() && testInstance.printSpecialResults}">
                        	<div style="page-break-inside:avoid">
                                <g:if test="${!testInstance.specialTestComplete}">
                                    <h3>${testInstance.GetSpecialTestTitle()} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                                </g:if>
                                <g:else>
                                    <h3>${testInstance.GetSpecialTestTitle()} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})</h3>
                                </g:else>
                                <g:specialTestPrintable t="${testInstance}"/>
							</div>
	                    </g:if>
	                    
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>