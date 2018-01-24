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
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.contest.printresults')}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
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
        <h2><g:if test="${contestInstance.contestPrintSubtitle}">${contestInstance.contestPrintSubtitle}</g:if><g:else>${message(code:'fc.contest.printresults')}</g:else><g:if test="${contestInstance.IsContestResultsProvisional(contestInstance.GetResultSettings(),contestInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
        <h3>${contestInstance.GetResultTitle(contestInstance.GetResultSettings(),true)}</h3>
        <g:form>
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
                                        <g:set var="observation_detail_written" value="${false}"/>
                                        <g:if test="${contestInstance.contestPrintObservationDetails}">
                                            <g:if test="${task_instance.IsObservationTestTurnpointRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="observation_detail_written" value="${true}"/>
                                            </g:if>
                                            <g:if test="${task_instance.IsObservationTestEnroutePhotoRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="observation_detail_written" value="${true}"/>
                                            </g:if>
                                            <g:if test="${task_instance.IsObservationTestEnrouteCanvasRun()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="observation_detail_written" value="${true}"/>
                                            </g:if>
                                        </g:if>
                                        <g:if test="${!observation_detail_written}">
                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                        </g:if>
                                    </g:if>
                                    <g:if test="${contestInstance.contestLandingResults && task_instance.IsLandingTestRun()}">
                                        <g:set var="landing_detail_written" value="${false}"/>
                                        <g:if test="${contestInstance.contestPrintLandingDetails}">
                                            <g:if test="${task_instance.IsLandingTest1Run()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="landing_detail_written" value="${true}"/>
                                            </g:if>
                                            <g:if test="${task_instance.IsLandingTest2Run()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="landing_detail_written" value="${true}"/>
                                            </g:if>
                                            <g:if test="${task_instance.IsLandingTest3Run()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="landing_detail_written" value="${true}"/>
                                            </g:if>
                                            <g:if test="${task_instance.IsLandingTest4Run()}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="landing_detail_written" value="${true}"/>
                                            </g:if>
                                        </g:if>
                                        <g:if test="${!landing_detail_written}">
                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                        </g:if>
                                    </g:if>
                                    <g:if test="${contestInstance.contestSpecialResults && task_instance.IsSpecialTestRun()}">
                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                    </g:if>
                                </g:if>
                                <g:if test="${contestInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1) || (task_instance.IsIncreaseEnabled()))}">
                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                </g:if>
                         	    <th colspan="${detail_num}">${task_instance.bestOfNamePrintable()}</th>
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
                                            <g:set var="observation_detail_written" value="${false}"/>
                                            <g:if test="${contestInstance.contestPrintObservationDetails}">
                                                <g:if test="${task_instance.IsObservationTestTurnpointRun()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <th>${message(code:'fc.observationresults.turnpoint.short')}</th>
                                                    <g:set var="observation_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${task_instance.IsObservationTestEnroutePhotoRun()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <th>${message(code:'fc.observationresults.enroutephoto.short')}</th>
                                                    <g:set var="observation_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${task_instance.IsObservationTestEnrouteCanvasRun()}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <th>${message(code:'fc.observationresults.enroutecanvas.short')}</th>
                                                    <g:set var="observation_detail_written" value="${true}"/>
                                                </g:if>
                                            </g:if>
                                            <g:if test="${!observation_detail_written}">
	                                            <g:set var="detail_num" value="${detail_num+1}"/>
	                                            <th>${message(code:'fc.observationresults.observations.short')}</th>
	                                        </g:if>
	                                    </g:if>
	                                    <g:if test="${contestInstance.contestLandingResults && task_instance.IsLandingTestRun()}">
                                            <g:set var="landing_detail_written" value="${false}"/>
	                                        <g:if test="${contestInstance.contestPrintLandingDetails}">
	                                            <g:if test="${task_instance.IsLandingTest1Run()}">
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                <th>${message(code:'fc.landingresults.landing1')}</th>
                                                    <g:set var="landing_detail_written" value="${true}"/>
	                                            </g:if>
	                                            <g:if test="${task_instance.IsLandingTest2Run()}">
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                <th>${message(code:'fc.landingresults.landing2')}</th>
                                                    <g:set var="landing_detail_written" value="${true}"/>
	                                            </g:if>
	                                            <g:if test="${task_instance.IsLandingTest3Run()}">
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                <th>${message(code:'fc.landingresults.landing3')}</th>
                                                    <g:set var="landing_detail_written" value="${true}"/>
	                                            </g:if>
	                                            <g:if test="${task_instance.IsLandingTest4Run()}">
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                <th>${message(code:'fc.landingresults.landing4')}</th>
                                                    <g:set var="landing_detail_written" value="${true}"/>
	                                            </g:if>
	                                        </g:if>
                                            <g:if test="${!landing_detail_written}">
	                                            <g:set var="detail_num" value="${detail_num+1}"/>
	                                            <th>${message(code:'fc.landingresults.landing.short')}</th>
	                                        </g:if>
	                                    </g:if>
	                                    <g:if test="${contestInstance.contestSpecialResults && task_instance.IsSpecialTestRun()}">
	                                        <g:set var="detail_num" value="${detail_num+1}"/>
	                                        <th>${message(code:'fc.specialresults.other.short')}</th>
	                                    </g:if>
	                                </g:if>
	                                <g:if test="${contestInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1) || (task_instance.IsIncreaseEnabled()))}">
	                                    <th>${message(code:'fc.test.results.summary.short')}</th>
	                                </g:if>
	                                <g:elseif test="${contestInstance.contestPrintTaskTestDetails && (detail_num==0)}">
	                                    <th>${message(code:'fc.test.results.summary.short')}</th>
	                                </g:elseif>
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
                                        <g:set var="taskpenalties_written" value="${false}"/>
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
                                                <g:set var="observation_detail_written" value="${false}"/>
                                                <g:if test="${contestInstance.contestPrintObservationDetails}">
													<g:if test="${task_instance.IsObservationTestTurnpointRun()}">
													    <g:set var="detail_num" value="${detail_num+1}"/>
													    <g:if test="${test_instance.IsObservationTestRun() && test_instance.IsObservationTestTurnpointRun()}">
													        <td class="observationpenalties">${test_instance.observationTestTurnPointPhotoPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
													    </g:if>
													    <g:else>
													        <td class="observationpenalties">-</td>
													    </g:else>
													    <g:set var="observation_detail_written" value="${true}"/>
													</g:if>
													<g:if test="${task_instance.IsObservationTestEnroutePhotoRun()}">
													    <g:set var="detail_num" value="${detail_num+1}"/>
													    <g:if test="${test_instance.IsObservationTestRun() && test_instance.IsObservationTestEnroutePhotoRun()}">
													        <td class="observationpenalties">${test_instance.observationTestRoutePhotoPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
													    </g:if>
													    <g:else>
													        <td class="observationpenalties">-</td>
													    </g:else>
													    <g:set var="observation_detail_written" value="${true}"/>
													</g:if>
													<g:if test="${task_instance.IsObservationTestEnrouteCanvasRun()}">
													    <g:set var="detail_num" value="${detail_num+1}"/>
													    <g:if test="${test_instance.IsObservationTestRun() && test_instance.IsObservationTestEnrouteCanvasRun()}">
													        <td class="observationpenalties">${test_instance.observationTestGroundTargetPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
													    </g:if>
													    <g:else>
													        <td class="observationpenalties">-</td>
													    </g:else>
													    <g:set var="observation_detail_written" value="${true}"/>
													</g:if>
                                                </g:if>
                                                <g:if test="${!observation_detail_written}">
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                <g:if test="${test_instance.IsObservationTestRun()}">
	                                                    <td class="observationpenalties">${test_instance.observationTestPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                </g:if>
	                                                <g:else>
	                                                    <td class="observationpenalties">-</td>
	                                                </g:else>
	                                            </g:if>
                                            </g:if>
                                            <g:if test="${contestInstance.contestLandingResults && task_instance.IsLandingTestRun()}">
                                                <g:set var="landing_detail_written" value="${false}"/>
                                                <g:if test="${contestInstance.contestPrintLandingDetails}">
                                                    <g:if test="${task_instance.IsLandingTest1Run()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest1Run()}">
                                                            <td class="landingpenalties">${test_instance.landingTest1Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                        </g:if>
                                                        <g:else>
                                                            <td class="landingpenalties">-</td>
                                                        </g:else>
                                                        <g:set var="landing_detail_written" value="${true}"/>
                                                    </g:if>
                                                    <g:if test="${task_instance.IsLandingTest2Run()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest2Run()}">
                                                            <td class="landingpenalties">${test_instance.landingTest2Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                        </g:if>
                                                        <g:else>
                                                            <td class="landingpenalties">-</td>
                                                        </g:else>
                                                        <g:set var="landing_detail_written" value="${true}"/>
                                                    </g:if>
                                                    <g:if test="${task_instance.IsLandingTest3Run()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest3Run()}">
                                                            <td class="landingpenalties">${test_instance.landingTest3Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                        </g:if>
                                                        <g:else>
                                                            <td class="landingpenalties">-</td>
                                                        </g:else>
                                                        <g:set var="landing_detail_written" value="${true}"/>
                                                    </g:if>
                                                    <g:if test="${task_instance.IsLandingTest4Run()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest4Run()}">
                                                            <td class="landingpenalties">${test_instance.landingTest4Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                        </g:if>
                                                        <g:else>
                                                            <td class="landingpenalties">-</td>
                                                        </g:else>
                                                        <g:set var="landing_detail_written" value="${true}"/>
                                                    </g:if>
                                                </g:if>
                                                <g:if test="${!landing_detail_written}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:if test="${test_instance.IsLandingTestRun()}">
                                                        <td class="landingpenalties">${test_instance.landingTestPenalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                    </g:if>
                                                    <g:else>
                                                        <td class="landingpenalties">-</td>
                                                    </g:else>
                                                </g:if>
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
                                            <td class="taskpenalties">${test_instance.GetResultPenalties(contestInstance.GetResultSettings())}<g:if test="${test_instance.IsIncreaseEnabled()}"> ${message(code:'fc.crew.increaseenabled.short',args:[test_instance.crew.GetIncreaseFactor()])}</g:if><g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"> [<g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">${message(code:'fc.provisional.short')}</g:if><g:else>${message(code:'fc.provisional')}</g:else>]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                            <g:set var="taskpenalties_written" value="${true}"/>
                                        </g:else>
                                        <g:if test="${contestInstance.contestPrintTaskDetails  && !taskpenalties_written && ((detail_num==0) || (detail_num>1) || (task_instance.IsIncreaseEnabled()))}">
                                            <td class="taskpenalties">${test_instance.GetResultPenalties(contestInstance.GetResultSettings())}<g:if test="${test_instance.IsIncreaseEnabled()}"> ${message(code:'fc.crew.increaseenabled.short',args:[test_instance.crew.GetIncreaseFactor()])}</g:if><g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"> [<g:if test="${task_instance in contestInstance.GetTestDetailsTasks(contestInstance.contestPrintTaskTestDetails)}">${message(code:'fc.provisional.short')}</g:if><g:else>${message(code:'fc.provisional')}</g:else>]<g:set var="test_provisional" value="${true}"/></g:if></td>
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
    </body>
</html>