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
                @top-left {
                    content: "${message(code:'fc.contest.printresults')}"
                }
                @top-right {
                    content: "${message(code:'fc.program.printpage')} " counter(page)
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
        <title>${message(code:'fc.contest.printresults')}</title>
    </head>
    <body>
        <div>
            <div>
                <h2><g:if test="${contestInstance.contestPrintSubtitle}">${contestInstance.contestPrintSubtitle}</g:if><g:else>${message(code:'fc.contest.printresults')}</g:else><g:if test="${contestInstance.IsContestResultsProvisional(contestInstance.GetResultSettings(),contestInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <h3>${contestInstance.GetResultTitle(contestInstance.GetResultSettings(),true)}</h3>
                <div>
                    <g:form>
                      	<br/>
                        <table class="resultlist">
                            <thead>
                                <tr>
                                	<th>${message(code:'fc.test.results.position.short')}</th>
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
                                   	<th>${message(code:'fc.test.results.summary')}</th>
                                </tr>
		                        <g:if test="${contestInstance.contestPrintTaskTestDetails}">
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
		                                <th/>
		                            </tr>
		                        </g:if>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabledAndNoContestPositionAndContestPenaltiesNotEqual(contestInstance,false,false,-1,[sort:'contestPosition'])}">
                                    <tr id="${crew_instance.contestPosition}">
                                        <td class="pos">${crew_instance.contestPosition}</td>
                                        <td class="crew">${crew_instance.name}</td>
                                        <g:if test="${contestInstance.contestPrintAircraft}">
                                            <td class="aircraft"><g:if test="${crew_instance.aircraft}">${crew_instance.aircraft.registration}</g:if><g:else>-</g:else></td>
                                        </g:if>
                                        <g:if test="${contestInstance.contestPrintTeam}">
	                                        <g:if test="${crew_instance.team}">                          
	                                            <td class="team">${crew_instance.team.name}</td>
	                                        </g:if>
	                                        <g:else>
	                                            <td class="team">-</td>
	                                        </g:else>
	                                    </g:if>
	                                    <g:if test="${contestInstance.contestPrintClass}">
	                                        <td class="resultclass"><g:if test="${crew_instance.resultclass}">${crew_instance.resultclass.name}</g:if></td>
	                                    </g:if>
	                                    <g:if test="${contestInstance.contestPrintShortClass}">
	                                        <td class="shortresultclass"><g:if test="${crew_instance.resultclass}">${crew_instance.resultclass.shortName}</g:if></td>
	                                    </g:if>
                                        <g:if test="${contestInstance.contestPrintTaskDetails || contestInstance.contestPrintTaskTestDetails}">
	                                        <g:set var="test_provisional" value="${false}"/>
	                                        <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
                                                <g:set var="detail_num" value="${new Integer(0)}"/>
	                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
	                                        	<g:if test="${test_instance}">
	                                                <g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">
	                                                    <g:if test="${contestInstance.contestPlanningResults && task_instance.IsPlanningTestRun()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
	                                                        <g:if test="${test_instance.IsPlanningTestRun()}">
	                                                            <td class="planningpenalties">${test_instance.planningTestPenalties}<g:if test="${!test_instance.planningTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td class="planningpenalties">-</td>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:if test="${contestInstance.contestFlightResults && task_instance.IsFlightTestRun()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
	                                                        <g:if test="${test_instance.IsFlightTestRun()}">
	                                                            <td class="flightpenalties">${test_instance.flightTestPenalties}<g:if test="${!test_instance.flightTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td class="flightpenalties">-</td>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:if test="${contestInstance.contestObservationResults && task_instance.IsObservationTestRun()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
	                                                        <g:if test="${test_instance.IsObservationTestRun()}">
	                                                            <td class="observationpenalties">${test_instance.observationTestPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td class="observationpenalties">-</td>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:if test="${contestInstance.contestLandingResults && task_instance.IsLandingTestRun()}">
                                                            <g:if test="${contestInstance.contestPrintLandingDetails}">
	                                                            <g:if test="${task_instance.IsLandingTest1Run()}">
	                                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                                <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest1Run()}">
	                                                                    <td class="landingpenalties">${test_instance.landingTest1Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                                </g:if>
	                                                                <g:else>
	                                                                    <td class="landingpenalties">-</td>
	                                                                </g:else>
	                                                            </g:if>
                                                                <g:if test="${task_instance.IsLandingTest2Run()}">
                                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                                    <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest2Run()}">
                                                                        <td class="landingpenalties">${test_instance.landingTest2Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                                    </g:if>
                                                                    <g:else>
                                                                        <td class="landingpenalties">-</td>
                                                                    </g:else>
                                                                </g:if>
                                                                <g:if test="${task_instance.IsLandingTest3Run()}">
                                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                                    <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest3Run()}">
                                                                        <td class="landingpenalties">${test_instance.landingTest3Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                                    </g:if>
                                                                    <g:else>
                                                                        <td class="landingpenalties">-</td>
                                                                    </g:else>
                                                                </g:if>
                                                                <g:if test="${task_instance.IsLandingTest4Run()}">
                                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                                    <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest4Run()}">
                                                                        <td class="landingpenalties">${test_instance.landingTest4Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                                    </g:if>
                                                                    <g:else>
                                                                        <td class="landingpenalties">-</td>
                                                                    </g:else>
                                                                </g:if>
                                                            </g:if>
                                                            <g:else>
	                                                            <g:set var="detail_num" value="${detail_num+1}"/>
		                                                        <g:if test="${test_instance.IsLandingTestRun()}">
		                                                            <td class="landingpenalties">${test_instance.landingTestPenalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
		                                                        </g:if>
		                                                        <g:else>
		                                                            <td class="landingpenalties">-</td>
		                                                        </g:else>
		                                                    </g:else>
	                                                    </g:if>
	                                                    <g:if test="${contestInstance.contestSpecialResults && task_instance.IsSpecialTestRun()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
	                                                        <g:if test="${test_instance.IsSpecialTestRun()}">
	                                                            <td class="specialpenalties">${test_instance.specialTestPenalties}<g:if test="${!test_instance.specialTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td class="specialpenalties">-</td>
	                                                        </g:else>
	                                                    </g:if>
	                                                </g:if>
	                                                <g:else>
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <td class="taskpenalties">${test_instance.GetResultPenalties(contestInstance.GetResultSettings())}<g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"> [<g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">${message(code:'fc.provisional.short')}</g:if><g:else>${message(code:'fc.provisional')}</g:else>]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                </g:else>
	                                                <g:if test="${contestInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1))}">
                                                           <td class="taskpenalties">${test_instance.GetResultPenalties(contestInstance.GetResultSettings())}<g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"> [<g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">${message(code:'fc.provisional.short')}</g:if><g:else>${message(code:'fc.provisional')}</g:else>]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                </g:if>
	                                        	</g:if>
	                                            <g:else>
	                                               <td class="taskpenalties">-</td>
	                                            </g:else>
			                                </g:each>
                                        </g:if>
                                        <g:else>
                                            <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
                                                <g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
                                                <g:if test="${test_instance}">
                                                    <g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"><g:set var="test_provisional" value="${true}"/></g:if>
                                                </g:if>
                                            </g:each>
                                        </g:else>
                                        <td class="contestpenalties">${crew_instance.contestPenalties}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>