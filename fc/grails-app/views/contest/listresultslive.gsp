<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:if test="${contestInstance.liveRefreshSeconds}">
            <meta http-equiv="refresh" content="${contestInstance.liveRefreshSeconds}">
        </g:if>
        
        <title>${message(code:'fc.contest.liveresults')} [${message(code:'fc.provisional')}]</title>
        
        <link rel="shortcut icon" href="fc.ico" type="image/x-icon" />

        <g:if test="${contestInstance.liveStylesheet}">
            <link rel="stylesheet" type="text/css" href="${contestInstance.liveStylesheet}" media="screen" />
        </g:if>
    </head>
    <body>
        <div class="box boxborder" >
            <h2><g:if test="${contestInstance.contestPrintSubtitle}">${contestInstance.contestPrintSubtitle}</g:if><g:else>${message(code:'fc.contest.liveresults')}</g:else> [${message(code:'fc.provisional')}]</h2>
            <div class="block" id="forms" >
                <g:if test="${!contestInstance.contestPrintTaskTestDetails}">
	                <table class="details">
	                    <tbody>
	                        <tr>
	                            <td>${contestInstance.GetResultTitle(contestInstance.GetResultSettings(),false)}</td>
	                        </tr>
	                    </tbody>
	                </table>
	            </g:if>
                <table class="data">
                    <thead>
                        <tr>
                            <th>${message(code:'fc.test.results.position')}</th>
                            <th>${message(code:'fc.crew')}</th>
                            <g:if test="${contestInstance.contestPrintAircraft}">
                                <th>${message(code:'fc.aircraft')}</th>
                            </g:if>
                            <g:if test="${contestInstance.contestPrintTeam}">
                                <th>${message(code:'fc.team')}</th>
                            </g:if>
                            <g:if test="${contestInstance.contestPrintClass}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
                            <g:if test="${contestInstance.contestPrintShortClass}">
                                <th>${message(code:'fc.resultclass.short.short')}</th>
                            </g:if>
                            <g:if test="${contestInstance.contestPrintTaskDetails || contestInstance.contestPrintTaskTestDetails}">
	                            <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
				                    <g:set var="detail_num" value="${new Integer(0)}"/>
				                    <g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">
				                        <g:if test="${contestInstance.contestPlanningResults && task_instance.IsPlanningTestRun()}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                        <g:if test="${contestInstance.contestFlightResults && task_instance.IsFlightTestRun()}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                        <g:if test="${contestInstance.contestObservationResults && task_instance.IsObservationTestRun()}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                        <g:if test="${contestInstance.contestLandingResults && task_instance.IsLandingTestRun()}">
				                            <g:if test="${contestInstance.contestPrintLandingDetails}">
				                                <g:if test="${task_instance.IsLandingTest1Run()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
				                                </g:if>
                                                <g:if test="${task_instance.IsLandingTest2Run()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                </g:if>
                                                <g:if test="${task_instance.IsLandingTest3Run()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                </g:if>
                                                <g:if test="${task_instance.IsLandingTest4Run()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                </g:if>
				                            </g:if>
				                            <g:else>
				                                <g:set var="detail_num" value="${detail_num+1}"/>
				                            </g:else>
				                        </g:if>
				                        <g:if test="${contestInstance.contestSpecialResults && task_instance.IsSpecialTestRun()}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                    </g:if>
				                    <g:if test="${contestInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1))}">
				                        <g:set var="detail_num" value="${detail_num+1}"/>
				                    </g:if>
	                                <th colspan="${detail_num}">${task_instance.bestOfName()}</th>
	                            </g:each>
	                        </g:if>
	                        <g:if test="${contestInstance.liveShowSummary}">
	                            <th>${message(code:'fc.test.results.summary')}</th>
	                        </g:if>
            	        </tr>
                        <g:if test="${contestInstance.contestPrintTaskTestDetails != ""}">
                            <tr>
                                <th/>
                                <th/>
	                            <g:if test="${contestInstance.contestPrintAircraft}">
                                    <th/>
	                            </g:if>
	                            <g:if test="${contestInstance.contestPrintTeam}">
                                    <th/>
	                            </g:if>
	                            <g:if test="${contestInstance.contestPrintClass}">
                                    <th/>
	                            </g:if>
	                            <g:if test="${contestInstance.contestPrintShortClass}">
                                    <th/>
	                            </g:if>
                                <g:if test="${contestInstance.contestPrintTaskDetails || contestInstance.contestPrintTaskTestDetails}">
	                                <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
                                        <g:set var="detail_num" value="${new Integer(0)}"/>
	                                    <g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">
			                                <g:if test="${contestInstance.contestPlanningResults && task_instance.IsPlanningTestRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
			                                    <th>${message(code:'fc.planningresults.planning.short')}</th>
			                                </g:if>
			                                <g:if test="${contestInstance.contestFlightResults && task_instance.IsFlightTestRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
			                                    <th>${message(code:'fc.flightresults.flight.short')}</th>
			                                </g:if>
			                                <g:if test="${contestInstance.contestObservationResults && task_instance.IsObservationTestRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
			                                    <th>${message(code:'fc.observationresults.observations.short')}</th>
			                                </g:if>
			                                <g:if test="${contestInstance.contestLandingResults && task_instance.IsLandingTestRun()}">
                                                <g:if test="${contestInstance.contestPrintLandingDetails}">
	                                                <g:if test="${task_instance.IsLandingTest1Run()}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing1')}</th>
	                                                </g:if>
	                                                <g:if test="${task_instance.IsLandingTest2Run()}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing2')}</th>
	                                                </g:if>
	                                                <g:if test="${task_instance.IsLandingTest3Run()}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing3')}</th>
	                                                </g:if>
	                                                <g:if test="${task_instance.IsLandingTest4Run()}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing4')}</th>
	                                                </g:if>
                                                </g:if>
                                                <g:else>
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
				                                    <th>${message(code:'fc.landingresults.landing.short')}</th>
				                                </g:else>
			                                </g:if>
			                                <g:if test="${contestInstance.contestSpecialResults && task_instance.IsSpecialTestRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
			                                    <th>${message(code:'fc.specialresults.other.short')}</th>
			                                </g:if>
			                            </g:if>
			                            <g:else>
                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                            <th>${message(code:'fc.test.results.summary.short')}</th>
			                            </g:else>
                                        <g:if test="${contestInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1))}">
		                                    <th>${message(code:'fc.test.results.summary.short')}</th>
		                                </g:if>
	                                </g:each>
	                            </g:if>
	                            <g:if test="${contestInstance.liveShowSummary}">
                                    <th/>
                                </g:if>
                            </tr>
                        </g:if>
                    </thead>
                    <tbody>
                        <g:if test="${contestInstance.livePositionCalculation == 0}">
	                        <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabledAndNoContestPositionAndContestPositionNotEqual(contestInstance,false,false,0,[sort:'contestPosition'])}">
	                            <g:if test="${!crew_instance.IsProvisionalCrew(contestInstance.GetResultSettings())}">
	                                <g:liveResultLine pos="${crew_instance.contestPosition}" crew="${crew_instance}" contest="${contestInstance}" />
		                        </g:if>
	                        </g:each>
	                    </g:if>
	                    <g:else>
	                        <g:set var="task_instance" value="${Task.get(contestInstance.livePositionCalculation)}"/>
	                        <g:if test="${task_instance}">
	                            <g:each var="test_instance" in="${Test.findAllByTask(task_instance,[sort:'taskPosition'])}">
	                                <g:if test="${!test_instance.crew.IsProvisionalCrew(contestInstance.GetResultSettings())}">
	                                    <g:liveResultLine pos="${test_instance.taskPosition}" crew="${test_instance.crew}" contest="${contestInstance}" task="${task_instance}" />
	                                </g:if>
	                            </g:each>
	                        </g:if>
	                    </g:else>
                    </tbody>
                </table>
            </div>
            <p class="programinfo">${contestInstance.printOrganizer}</p>
        </div>
    </body>
</html>