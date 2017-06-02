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
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    content: "${testInstance.GetStartNum()}: ${testInstance.crew.name}"
                }
                @top-right {
                    content: "${testInstance.GetViewPos()}-" counter(page)
                }
                @bottom-left {
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.crewresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.printName()}</title>
    </head>
    <body>
        <g:set var="detail_num" value="${testInstance.GetDetailNum()}" />
        <g:if test="${(detail_num > 0) || (testInstance.printSummaryResults)}">
            <h2>${message(code:'fc.crewresults')} ${testInstance.GetStartNum()}</h2>
            <h3>${testInstance?.task.printName()} (${message(code:'fc.version')} ${testInstance.GetCrewResultsVersion()})<g:if test="${testInstance.IsTestResultsProvisional(testInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></h3>
        </g:if>
        <g:form>
            <g:if test="${(detail_num > 0) || (testInstance.printSummaryResults) || (testInstance.IsIncreaseEnabled())}">
                <g:crewTestPrintable t="${testInstance}"/>
            </g:if>
            
            <!-- summary -->
            <g:if test="${(testInstance.printSummaryResults) || (testInstance.IsIncreaseEnabled())}">
	            <br/>
	            <table class="crewresultsummary">
	                <g:set var="task_penalties" value="${new Integer(0)}" />
	                <tbody>
                        <g:if test="${testInstance.IsPlanningTestRun()}">
                            <g:set var="task_penalties" value="${task_penalties + testInstance.planningTestPenalties}" />
                            <tr>
                                <td class="planningpenalties">${message(code:'fc.planningresults.planning')}: ${testInstance.planningTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})<g:if test="${!testInstance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
                            </tr>
                        </g:if>
	                	<g:if test="${testInstance.IsFlightTestRun()}">
	              	        <g:set var="task_penalties" value="${task_penalties + testInstance.flightTestPenalties}" />
	           	            <tr>
	            		         <td class="flightpenalties">${message(code:'fc.flightresults.flight')}: ${testInstance.flightTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})<g:if test="${!testInstance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
	           	            </tr>
	                    </g:if>
	                	<g:if test="${testInstance.IsObservationTestRun()}">
	              	        <g:set var="task_penalties" value="${task_penalties + testInstance.observationTestPenalties}" />
	           	            <tr>
	            		        <td class="observationpenalties">${message(code:'fc.observationresults.observations')}: ${testInstance.observationTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})<g:if test="${!testInstance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
	           	            </tr>
	                    </g:if>
                        <g:if test="${testInstance.IsLandingTestRun()}">
                            <g:set var="task_penalties" value="${task_penalties + testInstance.landingTestPenalties}" />
                            <tr>
                                <td class="landingpenalties">${message(code:'fc.landingresults.landing')}: ${testInstance.landingTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
                            </tr>
                        </g:if>
                        <g:if test="${testInstance.IsSpecialTestRun()}">
                            <g:set var="task_penalties" value="${task_penalties + testInstance.specialTestPenalties}" />
                            <tr>
                                <td class="specialpenalties">${message(code:'fc.specialresults.other')}: ${testInstance.specialTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})<g:if test="${!testInstance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
                            </tr>
                        </g:if>
                        <g:if test="${testInstance.IsIncreaseEnabled()}">
                            <tr>
                                <td class="increasepenalties">${message(code:'fc.task.increaseenabled.short',args:[testInstance.GetIncreaseValue()])}: ${testInstance.GetIncreasePenalties(task_penalties)} ${message(code:'fc.points')} </td>
                            </tr>
                            <g:set var="task_penalties" value="${task_penalties + testInstance.GetIncreasePenalties(task_penalties)}" />
                        </g:if>
	                    <tr>
	                    	<td> </td>
	                    </tr>
	                </tbody>
	                <tfoot>
	                    <tr class="penalties">
	                        <td>${message(code:'fc.penalties.total')}: ${task_penalties} ${message(code:'fc.points')}</td>
	                    </tr>
	                </tfoot>
	            </table>
	            <br/>
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
                <br/>
            </g:if>
         
            <!-- special test --> 
            <g:if test="${testInstance.IsSpecialTestRun() && testInstance.printSpecialResults}">
                <div style="page-break-inside:avoid">
                    <g:if test="${!testInstance.specialTestComplete}">
                        <h3>${testInstance.GetSpecialTestTitle(true)} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                    </g:if>
                    <g:else>
                        <h3>${testInstance.GetSpecialTestTitle(true)} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})</h3>
                    </g:else>
                    <g:specialTestPrintable t="${testInstance}"/>
                </div>
            </g:if>
            
            <!-- flight map --> 
            <g:if test="${testInstance.IsFlightTestRun() && testInstance.printFlightMap}" >
                <g:flightTestMapPrintable t="${testInstance}" flightMapFileName="${flightMapFileName}"/>
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
         
            <!-- scanned observation test -->
            <g:if test="${testInstance.IsObservationTestRun() && testInstance.printObservationResultsScan}">
                <g:observationTestScannedPrintable t="${testInstance}"/>
            </g:if>
            
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
         
            <!-- scannned planning test -->
            <g:if test="${testInstance.IsPlanningTestRun() && testInstance.printPlanningResultsScan}">
                <g:planningtaskTestScannedPrintable t="${testInstance}"/>
            </g:if>
            
        </g:form>
    </body>
</html>