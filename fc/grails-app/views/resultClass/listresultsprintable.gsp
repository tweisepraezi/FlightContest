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
                    content: "${resultclassInstance.GetPrintTitle2('fc.contest.printresults')}"
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
        <title>${resultclassInstance.GetPrintTitle('fc.contest.printresults')}</title>
    </head>
    <body>
        <div>
            <div>
                <h2><g:if test="${resultclassInstance.contestPrintSubtitle}">${resultclassInstance.contestPrintSubtitle}</g:if><g:else>${resultclassInstance.GetPrintTitle('fc.contest.printresults')}</g:else><g:if test="${resultclassInstance.IsClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <h3>${resultclassInstance.contest.GetResultTitle(resultclassInstance.GetClassResultSettings(),true)}</h3>
                <div>
                    <g:form>
                   		<br/>
                        <table class="resultlist">
                            <thead>
                                <tr>
                                   	<th>${message(code:'fc.test.results.position.short')}</th>
                                   	<th>${message(code:'fc.crew')}</th>
                                    <g:if test="${resultclassInstance.contestPrintAircraft}">
                                       	<th>${message(code:'fc.aircraft')}</th>
                                    </g:if>
                                    <g:if test="${resultclassInstance.contestPrintTeam}">
                                       	<th>${message(code:'fc.team')}</th>
                                    </g:if>
		                            <g:if test="${resultclassInstance.contestPrintClass}">
		                                <th>${message(code:'fc.resultclass')}</th>
		                            </g:if>
		                            <g:if test="${resultclassInstance.contestPrintShortClass}">
		                                <th>${message(code:'fc.resultclass.short.short')}</th>
		                            </g:if>
                                    <g:if test="${resultclassInstance.contestPrintTaskDetails || resultclassInstance.contestPrintTaskTestDetails}">
	                                   	<g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
				                            <g:set var="detail_num" value="${new Integer(0)}"/>
				                            <g:if test="${task_instance in resultclassInstance.contest.GetTestDetailsTasks(resultclassInstance.contestPrintTaskTestDetails)}">
				                                <g:if test="${resultclassInstance.contestPlanningResults && resultclassInstance.IsPlanningTestRun()}">
				                                    <g:set var="detail_num" value="${detail_num+1}"/>
				                                </g:if>
				                                <g:if test="${resultclassInstance.contestFlightResults && resultclassInstance.IsFlightTestRun()}">
				                                    <g:set var="detail_num" value="${detail_num+1}"/>
				                                </g:if>
				                                <g:if test="${resultclassInstance.contestObservationResults && resultclassInstance.IsObservationTestRun()}">
				                                    <g:set var="detail_num" value="${detail_num+1}"/>
				                                </g:if>
				                                <g:if test="${resultclassInstance.contestLandingResults && resultclassInstance.IsLandingTestRun()}">
                                                    <g:if test="${resultclassInstance.contestPrintLandingDetails}">
                                                        <g:if test="${resultclassInstance.IsLandingTest1Run()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                                        </g:if>
                                                        <g:if test="${resultclassInstance.IsLandingTest2Run()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                                        </g:if>
                                                        <g:if test="${resultclassInstance.IsLandingTest3Run()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                                        </g:if>
                                                        <g:if test="${resultclassInstance.IsLandingTest4Run()}">
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                                        </g:if>
                                                    </g:if>
                                                    <g:else>
				                                        <g:set var="detail_num" value="${detail_num+1}"/>
				                                    </g:else>
				                                </g:if>
				                                <g:if test="${resultclassInstance.contestSpecialResults && resultclassInstance.IsSpecialTestRun()}">
				                                    <g:set var="detail_num" value="${detail_num+1}"/>
				                                </g:if>
				                            </g:if>
				                            <g:if test="${resultclassInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1))}">
				                                <g:set var="detail_num" value="${detail_num+1}"/>
				                            </g:if>
	                                    	<th colspan="${detail_num}">${task_instance.bestOfNamePrintable()}</th>
		                                </g:each>
		                            </g:if>
                                   	<th>${message(code:'fc.test.results.summary')}</th>
                                </tr>
                                <g:if test="${resultclassInstance.contestPrintTaskTestDetails}">
                                    <tr>
                                        <th/>
                                        <th/>
                                        <g:if test="${resultclassInstance.contestPrintAircraft}">
                                            <th/>
                                        </g:if>
                                        <g:if test="${resultclassInstance.contestPrintTeam}">
                                            <th/>
                                        </g:if>
                                        <g:if test="${resultclassInstance.contestPrintClass}">
                                            <th/>
                                        </g:if>
                                        <g:if test="${resultclassInstance.contestPrintShortClass}">
                                            <th/>
                                        </g:if>
                                        <g:if test="${resultclassInstance.contestPrintTaskDetails || resultclassInstance.contestPrintTaskTestDetails}">
                                            <g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
                                                <g:set var="detail_num" value="${new Integer(0)}"/>
                                                <g:if test="${task_instance in resultclassInstance.contest.GetTestDetailsTasks(resultclassInstance.contestPrintTaskTestDetails)}">
                                                    <g:if test="${resultclassInstance.contestPlanningResults && resultclassInstance.IsPlanningTestRun()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.planningresults.planning.short')}</th>
                                                    </g:if>
                                                    <g:if test="${resultclassInstance.contestFlightResults && resultclassInstance.IsFlightTestRun()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.flightresults.flight.short')}</th>
                                                    </g:if>
                                                    <g:if test="${resultclassInstance.contestObservationResults && resultclassInstance.IsObservationTestRun()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.observationresults.observations.short')}</th>
                                                    </g:if>
                                                    <g:if test="${resultclassInstance.contestLandingResults && resultclassInstance.IsLandingTestRun()}">
                                                        <g:if test="${resultclassInstance.contestPrintLandingDetails}">
                                                            <g:if test="${resultclassInstance.IsLandingTest1Run()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                                <th>${message(code:'fc.landingresults.landing1')}</th>
                                                            </g:if>
                                                            <g:if test="${resultclassInstance.IsLandingTest2Run()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                                <th>${message(code:'fc.landingresults.landing2')}</th>
                                                            </g:if>
                                                            <g:if test="${resultclassInstance.IsLandingTest3Run()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                                <th>${message(code:'fc.landingresults.landing3')}</th>
                                                            </g:if>
                                                            <g:if test="${resultclassInstance.IsLandingTest4Run()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                                <th>${message(code:'fc.landingresults.landing4')}</th>
                                                            </g:if>
                                                        </g:if>
                                                        <g:else>
                                                            <g:set var="detail_num" value="${detail_num+1}"/>
                                                            <th>${message(code:'fc.landingresults.landing.short')}</th>
                                                        </g:else>
                                                    </g:if>
                                                    <g:if test="${resultclassInstance.contestSpecialResults && resultclassInstance.IsSpecialTestRun()}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.specialresults.other.short')}</th>
                                                    </g:if>
                                                </g:if>
                                                <g:else>
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <th>${message(code:'fc.test.results.summary.short')}</th>
                                                </g:else>
                                                <g:if test="${resultclassInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1))}">
                                                    <th>${message(code:'fc.test.results.summary.short')}</th>
                                                </g:if>
                                            </g:each>
                                        </g:if>
                                        <th/>
                                    </tr>
                                </g:if>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabledAndNoClassPositionAndContestPenaltiesNotEqual(resultclassInstance.contest,false,false,-1,[sort:'classPosition'])}">
                                	<g:if test="${crew_instance.resultclass == resultclassInstance}">
	                                    <tr id="${crew_instance.classPosition}">
	                                        <td class="pos">${crew_instance.classPosition}</td>
	                                        <td class="crew">${crew_instance.name}</td>
                                            <g:if test="${resultclassInstance.contestPrintAircraft}">
	                                           <td class="aircraft"><g:if test="${crew_instance.aircraft}">${crew_instance.aircraft.registration}</g:if><g:else>-</g:else></td>
	                                        </g:if>
                                            <g:if test="${resultclassInstance.contestPrintTeam}">
                                                <g:if test="${crew_instance.team}">                          
	                                              <td class="team"><g:if test="${crew_instance.team}">${crew_instance.team.name}</g:if></td>
	                                            </g:if>
	                                            <g:else>
	                                                <td class="team">-</td>
	                                            </g:else>
	                                        </g:if>
		                                    <g:if test="${resultclassInstance.contestPrintClass}">
		                                        <td class="resultclass"><g:if test="${crew_instance.resultclass}">${crew_instance.resultclass.name}</g:if></td>
		                                    </g:if>
		                                    <g:if test="${resultclassInstance.contestPrintShortClass}">
		                                        <td class="shortresultclass"><g:if test="${crew_instance.resultclass}">${crew_instance.resultclass.shortName}</g:if></td>
		                                    </g:if>
                                            <g:if test="${resultclassInstance.contestPrintTaskDetails || resultclassInstance.contestPrintTaskTestDetails}">
		                                        <g:set var="test_provisional" value="${false}"/>
		                                        <g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
                                                    <g:set var="detail_num" value="${new Integer(0)}"/>
		                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
		                                        	<g:if test="${test_instance}">
	                                                    <g:if test="${task_instance in resultclassInstance.contest.GetTestDetailsTasks(resultclassInstance.contestPrintTaskTestDetails)}">
	                                                        <g:if test="${resultclassInstance.contestPlanningResults && resultclassInstance.IsPlanningTestRun()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                            <g:if test="${test_instance.IsPlanningTestRun()}">
	                                                                <td class="planningpenalties">${test_instance.planningTestPenalties}<g:if test="${!test_instance.planningTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                            </g:if>
	                                                            <g:else>
	                                                                <td class="planningpenalties">-</td>
	                                                            </g:else>
	                                                        </g:if>
	                                                        <g:if test="${resultclassInstance.contestFlightResults && resultclassInstance.IsFlightTestRun()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                            <g:if test="${test_instance.IsFlightTestRun()}">
	                                                                <td class="flightpenalties">${test_instance.flightTestPenalties}<g:if test="${!test_instance.flightTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                            </g:if>
	                                                            <g:else>
	                                                                <td class="flightpenalties">-</td>
	                                                            </g:else>
	                                                        </g:if>
	                                                        <g:if test="${resultclassInstance.contestObservationResults && resultclassInstance.IsObservationTestRun()}">
                                                                <g:set var="detail_num" value="${detail_num+1}"/>
	                                                            <g:if test="${test_instance.IsObservationTestRun()}">
	                                                                <td class="observationpenalties">${test_instance.observationTestPenalties}<g:if test="${!test_instance.observationTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                            </g:if>
	                                                            <g:else>
	                                                                <td class="observationpenalties">-</td>
	                                                            </g:else>
	                                                        </g:if>
	                                                        <g:if test="${resultclassInstance.contestLandingResults && resultclassInstance.IsLandingTestRun()}">
	                                                            <g:if test="${resultclassInstance.contestPrintLandingDetails}">
	                                                                <g:if test="${resultclassInstance.IsLandingTest1Run()}">
	                                                                    <g:set var="detail_num" value="${detail_num+1}"/>
	                                                                    <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest1Run()}">
	                                                                        <td class="landingpenalties">${test_instance.landingTest1Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                                    </g:if>
	                                                                    <g:else>
	                                                                        <td class="landingpenalties">-</td>
	                                                                    </g:else>
	                                                                </g:if>
	                                                                <g:if test="${resultclassInstance.IsLandingTest2Run()}">
	                                                                    <g:set var="detail_num" value="${detail_num+1}"/>
	                                                                    <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest2Run()}">
	                                                                        <td class="landingpenalties">${test_instance.landingTest2Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                                    </g:if>
	                                                                    <g:else>
	                                                                        <td class="landingpenalties">-</td>
	                                                                    </g:else>
	                                                                </g:if>
	                                                                <g:if test="${resultclassInstance.IsLandingTest3Run()}">
	                                                                    <g:set var="detail_num" value="${detail_num+1}"/>
	                                                                    <g:if test="${test_instance.IsLandingTestRun() && test_instance.IsLandingTest3Run()}">
	                                                                        <td class="landingpenalties">${test_instance.landingTest3Penalties}<g:if test="${!test_instance.landingTestComplete}"> [${message(code:'fc.provisional.short')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                                                    </g:if>
	                                                                    <g:else>
	                                                                        <td class="landingpenalties">-</td>
	                                                                    </g:else>
	                                                                </g:if>
	                                                                <g:if test="${resultclassInstance.IsLandingTest4Run()}">
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
	                                                        <g:if test="${resultclassInstance.contestSpecialResults && resultclassInstance.IsSpecialTestRun()}">
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
                                                            <td class="taskpenalties">${test_instance.GetResultPenalties(resultclassInstance.GetClassResultSettings())}<g:if test="${test_instance.IsTestClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance)}"> [<g:if test="${task_instance in resultclassInstance.contest.GetTestDetailsTasks(resultclassInstance.contestPrintTaskTestDetails)}">${message(code:'fc.provisional.short')}</g:if><g:else>${message(code:'fc.provisional')}</g:else>]<g:set var="test_provisional" value="${true}"/></g:if></td>
                                                        </g:else>
                                                        <g:if test="${resultclassInstance.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1))}">
	      	                                        	    <td class="taskpenalties">${test_instance.GetResultPenalties(resultclassInstance.GetClassResultSettings())}<g:if test="${test_instance.IsTestClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance)}"> [<g:if test="${task_instance in resultclassInstance.contest.GetTestDetailsTasks(resultclassInstance.contestPrintTaskTestDetails)}">${message(code:'fc.provisional.short')}</g:if><g:else>${message(code:'fc.provisional')}</g:else>]<g:set var="test_provisional" value="${true}"/></g:if></td>
	      	                                        	</g:if>
		                                        	</g:if>
		                                        	<g:else>
		                                        	    <td class="taskpenalties">-</td>
		                                        	</g:else>
				                                </g:each>
	                                        </g:if>
                                            <g:else>
                                                <g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
                                                    <g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
                                                    <g:if test="${test_instance}">
                                                        <g:if test="${test_instance.IsTestClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance)}"><g:set var="test_provisional" value="${true}"/></g:if>
                                                    </g:if>
                                                </g:each>
                                            </g:else>
	                                        <td class="contestpenalties">${crew_instance.contestPenalties}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
	                                    </tr>
	                            	</g:if>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>