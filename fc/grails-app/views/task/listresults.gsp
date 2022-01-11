<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.listresults')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskresults="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.listresults')} - ${taskInstance.name()}<g:if test="${taskInstance.IsTaskResultsProvisional(taskInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <g:form id="${taskInstance.id}" method="post" >
                    <g:set var="ti" value="${[]+1}"/>
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/></td>
	                            <g:if test="${taskInstance.flighttest && taskInstance.IsFlightTestRun()}">
	                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'disabledcheckpoints')}"/></td>
	                            </g:if>
	                            <g:else>
	                            	<td/>
	                            </g:else>
                                <g:if test="${taskInstance.flighttest && taskInstance.IsFlightTestRun()}">
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listdifferences')}"/></td>
                                </g:if>
                                <g:else>
                                    <td/>
                                </g:else>
	                            <g:if test="${new File(Defs.EXE_GPSBABEL).exists() && taskInstance.flighttest && taskInstance.IsFlightTestRun()}">
	                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'readlogger')}"/></td>
	                            </g:if>
	                            <g:else>
	                            	<td/>
	                            </g:else>
                                <td style="width:1%;"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></td>
                            </tr>
                            <tr>
                                <g:if test="${!taskInstance.hidePlanning}">
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </g:if>
                                <g:else>
                                    <td/>
                                </g:else>
                                <td/>
                                <td/>
                                <td/>
                                <td/>
                            </tr>
                            <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.contest.liveTrackingContestID}" >
                                <tr>
                                    <td><g:livetrackingtask var="${taskInstance}" link="${createLink(controller:'task',action:'editlivetracking')}"/><img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="margin-left:0.2rem; height:0.6rem;"/></td>
                                    <td>
                                        <g:set var="livetracking_map" value="${BootStrap.global.GetLiveTrackingMap(taskInstance.liveTrackingNavigationTaskID)}"/>
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingNavigationTaskID && livetracking_map}">
                                            <a href="${livetracking_map}" target="_blank">${message(code:'fc.livetracking.navigationtaskmap')}<img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="margin-left:0.2rem; height:0.6rem;"/></a> (${taskInstance.GetLiveTrackingVisibility()})
                                        </g:if>
                                    </td>
                                    <td>
                                        <g:set var="livetracking_results" value="${BootStrap.global.GetLiveTrackingResults(taskInstance.contest.liveTrackingContestID, taskInstance.liveTrackingResultsTaskID)}"/>
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsTaskID && livetracking_results}">
                                                <a href="${livetracking_results}" target="_blank">${message(code:'fc.livetracking.results.service')}<img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="margin-left:0.2rem; height:0.6rem;"/></a> (${taskInstance.GetLiveTrackingVisibility()})
                                        </g:if>
                                    </td>
                                    <td/>
                                    <td/>
                                </tr>
                            </g:if>
                        </tbody>
                    </table>
                    <table>
                        <g:set var="results_columns" value="${new Integer(5)}"></g:set>
                        <g:if test="${taskInstance.IsPlanningTestRun()}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.IsFlightTestRun()}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.IsObservationTestRun()}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.IsLandingTestRun()}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.IsSpecialTestRun()}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <thead>
                            <tr>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <th class="table-head" colspan="6">${message(code:'fc.crew.list')}</th>
	                            </g:if>
	                            <g:else>
	                            	<th class="table-head" colspan="5">${message(code:'fc.crew.list')}</th>
	                            </g:else>
                                <th class="table-head" colspan="${results_columns}">${message(code:'fc.test.results')}</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.team')}</th>
		                        <g:if test="${taskInstance.contest.resultClasses}">
                                	<th>${message(code:'fc.resultclass')}</th>
		                        </g:if>
                                <th>${message(code:'fc.test.listpos')}</th>
                                <g:if test="${taskInstance.IsPlanningTestRun()}">
	                                <th>
                                        ${message(code:'fc.planningresults.planning')}
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsPlanningID}" >
                                            <g:if test="${taskInstance.liveTrackingResultsPublishImmediately}">
                                                <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="height:0.6rem;"/>
                                            </g:if>
                                            <g:else>
                                                <img src="${createLinkTo(dir:'images',file:'livetracking-off.svg')}" style="height:0.6rem;"/>
                                            </g:else>
                                        </g:if>
                                    </th>
                                </g:if>
                                <g:if test="${taskInstance.IsFlightTestRun()}">
	                                <th>
                                        ${message(code:'fc.flightresults.flight')}
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsFlightID && taskInstance.liveTrackingResultsFlightOn}" >
                                            <g:if test="${taskInstance.liveTrackingResultsPublishImmediately}">
                                                <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="height:0.6rem;"/>
                                            </g:if>
                                            <g:else>
                                                <img src="${createLinkTo(dir:'images',file:'livetracking-off.svg')}" style="height:0.6rem;"/>
                                            </g:else>
                                        </g:if>
                                    </th>
                                </g:if>
                                <g:if test="${taskInstance.IsObservationTestRun()}">
	                                <th>
                                        ${message(code:'fc.observationresults.observations')}
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsObservationID}" >
                                            <g:if test="${taskInstance.liveTrackingResultsPublishImmediately}">
                                                <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="height:0.6rem;"/>
                                            </g:if>
                                            <g:else>
                                                <img src="${createLinkTo(dir:'images',file:'livetracking-off.svg')}" style="height:0.6rem;"/>
                                            </g:else>
                                        </g:if>
                                    </th>
                                </g:if>
                                <g:if test="${taskInstance.IsLandingTestRun()}">
	                                <th>
                                        ${message(code:'fc.landingresults.landing')}
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsLandingID}" >
                                            <g:if test="${taskInstance.liveTrackingResultsPublishImmediately}">
                                                <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="height:0.6rem;"/>
                                            </g:if>
                                            <g:else>
                                                <img src="${createLinkTo(dir:'images',file:'livetracking-off.svg')}" style="height:0.6rem;"/>
                                            </g:else>
                                        </g:if>
                                    </th>
                                </g:if>
                                <g:if test="${taskInstance.IsSpecialTestRun()}">
	                                <th>
                                        ${message(code:'fc.specialresults.other')}
                                        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsSpecialID}" >
                                            <g:if test="${taskInstance.liveTrackingResultsPublishImmediately}">
                                                <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="height:0.6rem;"/>
                                            </g:if>
                                            <g:else>
                                                <img src="${createLinkTo(dir:'images',file:'livetracking-off.svg')}" style="height:0.6rem;"/>
                                            </g:else>
                                        </g:if>
                                    </th>
                                </g:if>
                                <th>${message(code:'fc.crewresults.all')}</th>
                                <th>${message(code:'fc.test.results.summary')}</th>
                                <th>${message(code:'fc.test.results.position')}</th>
                                <th/>
                                <th/>
                            </tr>
                        </thead>
                        <tbody>
                       		<g:set var="showline" value="${false}"></g:set>
                            <g:each var="test_instance" status="i" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                            	<g:if test="${session.showLimit}">
                            		<g:if test="${(i + 1 > session.showLimitStartPos) && (i < session.showLimitStartPos + session.showLimitCrewNum)}">
	                            		<g:set var="showline" value="${true}"></g:set>
                            		</g:if>
                            		<g:else>
	                            		<g:set var="showline" value="${false}"></g:set>
									</g:else>
                            	</g:if>
                            	<g:else>
                            		<g:set var="showline" value="${true}"></g:set>
                            	</g:else>
                            	<g:if test="${showline}">
                            	    <g:set var="test_provisional" value="${test_instance.IsTestResultsProvisional(test_instance.GetResultSettings())}"/>
	                                <tr class="${(i % 2) == 0 ? 'odd' : ''}">
	    
                                        <!-- search next ids -->
                                        <g:set var="next_planningtask" value=""/>
                                        <g:set var="next_planningtask_id" value="${test_instance.GetNext2TestID(ResultType.Planningtask)}" />
                                        <g:if test="${next_planningtask_id}">
                                            <g:set var="next_planningtask" value="?next=${next_planningtask_id}"/>
                                        </g:if>
                                        <g:set var="next_flight" value=""/>
                                        <g:set var="next_flight_id" value="${test_instance.GetNext2TestID(ResultType.Flight)}" />
                                        <g:if test="${next_flight_id}">
                                            <g:set var="next_flight" value="?next=${next_flight_id}"/>
                                        </g:if>
                                        <g:set var="next_observation" value=""/>
                                        <g:set var="next_observation_id" value="${test_instance.GetNext2TestID(ResultType.Observation)}" />
                                        <g:if test="${next_observation_id}">
                                            <g:set var="next_observation" value="?next=${next_observation_id}"/>
                                        </g:if>
                                        <g:set var="next_landing" value=""/>
                                        <g:set var="next_landing_id" value="${test_instance.GetNext2TestID(ResultType.Landing)}" />
                                        <g:if test="${next_landing_id}">
                                            <g:set var="next_landing" value="?next=${next_landing_id}"/>
                                        </g:if>
                                        <g:set var="next_special" value=""/>
                                        <g:set var="next_special_id" value="${test_instance.GetNext2TestID(ResultType.Special)}" />
                                        <g:if test="${next_special_id}">
                                            <g:set var="next_special" value="?next=${next_special_id}"/>
                                        </g:if>
                                        <g:set var="next_crew" value=""/>
                                        <g:set var="next_crew_id" value="${test_instance.GetNext2TestID(ResultType.Crew)}" />
                                        <g:if test="${next_crew_id}">
                                            <g:set var="next_crew" value="?next=${next_crew_id}"/>
                                        </g:if>
                                        
	                                    <g:set var="testInstanceID" value="selectedTestID${test_instance.id.toString()}"></g:set>
                                        <td>
                                            <g:testnum var="${test_instance}" link="${createLink(controller:'test',action:'show')}"/>
                                            <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsTaskID && test_instance.taskLiveTrackingTeamID}" >
                                                <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="margin-left:0.5rem; height:0.7rem;"/>
                                            </g:if>
                                        </td>
	                            
	                                    <td><g:crew var="${test_instance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                                    
                                    	<td><g:if test="${test_instance.taskAircraft}"><g:aircraft var="${test_instance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></g:if><g:else>${message(code:'fc.noassigned')}</g:else> (${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')})</td>
                                        <g:if test="${test_instance.crew.team}">
                                        	<td><g:team var="${test_instance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
                                        </g:if>
                                        <g:else>
                                            <td>-</td>
                                        </g:else>
                                    	
	                                    <g:if test="${taskInstance.contest.resultClasses}">
	                                    	<g:if test="${test_instance.crew.resultclass}">
	                                    		<td><g:resultclass var="${test_instance.crew.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
	                                    	</g:if>
	                                    	<g:else>
	                                    		<td>${message(code:'fc.noassigned')}</td>
	                                    	</g:else>
	                                    </g:if>
	                                    
                                        <g:set var="flighttest_pos" value="${test_instance.GetFlightTestPos()}"></g:set>
                                        <g:if test="${flighttest_pos}">
                                            <td>${flighttest_pos}</td>
                                        </g:if> <g:else>
                                            <td/>
                                        </g:else>
                                        
										<g:if test="${test_instance.crew.disabled}">
											<td colspan="10">${message(code:'fc.disabled')}</td>
										</g:if>
                                        <g:elseif test="${test_instance.disabledCrew}">
                                            <td colspan="10">${message(code:'fc.test.crewdisabled')}</td>
                                        </g:elseif>
										<g:else>
			                                <g:if test="${taskInstance.IsPlanningTestRun()}">
			                                	<g:if test="${test_instance.IsPlanningTestRun()}">
				                                    <g:if test="${test_instance.planningtesttask}">
				                                    	<td>${test_instance.planningTestPenalties} <g:if test="${test_instance.scannedPlanningTest}"> <a href="${createLink(controller:'test',action:'planningtaskformimage',params:[testid:test_instance.id])}" target="_blank"><img src="${createLinkTo(dir:'images',file:'scanned.png')}"/></a></g:if> <g:if test="${!test_instance.planningTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'planningtaskresults')}/${test_instance.id}${next_planningtask}">${message(code:'fc.test.results.here')}</a>
                                                            <g:if test="${test_instance.planningTestComplete && BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsPlanningID}">
                                                                <g:if test="${test_instance.planningTestLiveTrackingResultOk}">
                                                                    <img src="${createLinkTo(dir:'images',file:'out-ok.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                                </g:if>
                                                                <g:elseif test="${test_instance.planningTestLiveTrackingResultError}">
                                                                    <img src="${createLinkTo(dir:'images',file:'out-error.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                                </g:elseif>
                                                            </g:if>
                                                        </td>
													</g:if> <g:else>
														<td>${message(code:'fc.noassigned')}</td>
													</g:else>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
												
											<g:if test="${taskInstance.IsFlightTestRun()}">
												<g:if test="${test_instance.IsFlightTestRun()}">
													<g:if test="${test_instance.timeCalculated}">
				                                    	<td>${test_instance.flightTestPenalties} <g:if test="${!test_instance.flightTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'flightresults')}/${test_instance.id}${next_flight}">${message(code:'fc.test.results.here')}</a>
                                                            <g:if test="${test_instance.flightTestComplete && BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsFlightID}">
                                                                <g:if test="${test_instance.flightTestLiveTrackingResultOk}">
                                                                    <img src="${createLinkTo(dir:'images',file:'out-ok.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                                </g:if>
                                                                <g:elseif test="${test_instance.flightTestLiveTrackingResultError}">
                                                                    <img src="${createLinkTo(dir:'images',file:'out-error.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                                </g:elseif>
                                                            </g:if>
                                                        </td>
													</g:if> <g:else>
														<td>${message(code:'fc.nocalculated')}</td>
													</g:else>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
											
			                                <g:if test="${taskInstance.IsObservationTestRun()}">
				                                <g:if test="${test_instance.IsObservationTestRun()}">
		        	                            	<td>${test_instance.observationTestPenalties} <g:if test="${test_instance.scannedObservationTest}"> <a href="${createLink(controller:'test',action:'observationformimage',params:[testid:test_instance.id])}" target="_blank"><img src="${createLinkTo(dir:'images',file:'scanned.png')}"/></a></g:if> <g:if test="${!test_instance.observationTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'observationresults')}/${test_instance.id}${next_observation}">${message(code:'fc.test.results.here')}</a>
                                                        <g:if test="${test_instance.observationTestComplete && BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsObservationID}">
                                                            <g:if test="${test_instance.observationTestLiveTrackingResultOk}">
                                                                <img src="${createLinkTo(dir:'images',file:'out-ok.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                            </g:if>
                                                            <g:elseif test="${test_instance.observationTestLiveTrackingResultError}">
                                                                <img src="${createLinkTo(dir:'images',file:'out-error.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                            </g:elseif>
                                                        </g:if>
                                                    </td>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
											
			                                <g:if test="${taskInstance.IsLandingTestRun()}">
				                                <g:if test="${test_instance.IsLandingTestRun()}">
		                                    		<td>${test_instance.landingTestPenalties} <g:if test="${!test_instance.landingTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'landingresults')}/${test_instance.id}${next_landing}">${message(code:'fc.test.results.here')}</a>
                                                        <g:if test="${test_instance.landingTestComplete && BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsLandingID}">
                                                            <g:if test="${test_instance.landingTestLiveTrackingResultOk}">
                                                                <img src="${createLinkTo(dir:'images',file:'out-ok.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                            </g:if>
                                                            <g:elseif test="${test_instance.landingTestLiveTrackingResultError}">
                                                                <img src="${createLinkTo(dir:'images',file:'out-error.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                            </g:elseif>
                                                        </g:if>
                                                    </td>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
	                                    	
			                                <g:if test="${taskInstance.IsSpecialTestRun()}">
				                                <g:if test="${test_instance.IsSpecialTestRun()}">
			                                    	<td>${test_instance.specialTestPenalties} <g:if test="${!test_instance.specialTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'specialresults')}/${test_instance.id}${next_special}">${message(code:'fc.test.results.here')}</a>
                                                        <g:if test="${test_instance.specialTestComplete && BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsSpecialID}">
                                                            <g:if test="${test_instance.specialTestLiveTrackingResultOk}">
                                                                <img src="${createLinkTo(dir:'images',file:'out-ok.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                            </g:if>
                                                            <g:elseif test="${test_instance.specialTestLiveTrackingResultError}">
                                                                <img src="${createLinkTo(dir:'images',file:'out-error.svg')}" style="margin-left:0.2rem; height:0.6rem;"/>
                                                            </g:elseif>
                                                        </g:if>
                                                    </td>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
			                                
		                                    <td><a href="${createLink(controller:'test',action:'crewresults')}/${test_instance.id}${next_crew}">${message(code:'fc.test.results.here')}</a></td>
		                                    
		                                    <td>${test_instance.taskPenalties} ${message(code:'fc.points')}<g:if test="${test_instance.IsIncreaseEnabled()}"> (${message(code:'fc.crew.increaseenabled.short',args:[test_instance.crew.GetIncreaseFactor()])})</g:if><g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
		                                    
		                                    <g:if test="${test_instance.taskPosition}">
		                                        <td>${test_instance.taskPosition}</td>
		                                    </g:if> <g:else>
		                                        <td>${message(code:'fc.test.results.position.none')}</td>
		                                    </g:else>
		                                    <g:if test="${!test_provisional && test_instance.crewResultsModified}">
		                                        <td><a href="${createLink(controller:'test',action:'printcrewresults',id:test_instance.id,params:test_instance.GetPrintCrewResultsDefaultParams())}"><img src="${createLinkTo(dir:'images',file:'print.png')}"/></a></td>
		                                    </g:if>
		                                    <g:else>
		                                        <td/>
		                                    </g:else>
		                                    <td>
												<g:set var="upload_job_status" value="${test_instance.GetFlightTestUploadJobStatus()}"/>
												<g:if test="${upload_job_status == UploadJobStatus.Waiting}"> 
													<img src="${createLinkTo(dir:'images',file:'email.png')}"/>
												</g:if>
												<g:elseif test="${upload_job_status == UploadJobStatus.Sending}"> 
													<img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>
												</g:elseif>
												<g:elseif test="${upload_job_status == UploadJobStatus.Error}"> 
													<img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>
												</g:elseif>
												<g:elseif test="${upload_job_status == UploadJobStatus.Done}"> 
													<g:set var="email_links" value="${NetTools.EMailLinks(test_instance.GetFlightTestUploadLink())}" />
													<a href="${email_links.pdf}" target="_blank"><img src="${createLinkTo(dir:'images',file:'pdf.png')}"/></a>
												</g:elseif>
		                                    </td>
	                                    </g:else>
	                                </tr>
                                </g:if>
                            </g:each>
                        </tbody>
                    </table>
                    <table>
                        <tfoot>
                            <tr>
                                <td>
                                    <g:actionSubmit action="crewresultsprintquestion" value="${message(code:'fc.crewresults.all.print')}" tabIndex="${ti[0]++}"/>
                                    <g:if test="${taskInstance.IsEMailPossible()}">
                                        <g:actionSubmit action="emailnewcrewresults" value="${message(code:'fc.crewresults.new.email')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        <g:actionSubmit action="emailallcrewresults" value="${message(code:'fc.crewresults.all.email')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:if test="${taskInstance.IsFlightTestRun()}">
                                        <g:actionSubmit action="kmzexport_task" value="${message(code:'fc.kmz.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:actionSubmit action="calculatepositions" value="${message(code:'fc.results.calculatepositions')}" tabIndex="${ti[0]++}"/> 
                                    <g:actionSubmit action="printresults" value="${message(code:'fc.test.results.print')}" tabIndex="${ti[0]++}"/>
                                    <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.contest.liveTrackingContestID && taskInstance.contest.liveTrackingScorecard && taskInstance.liveTrackingNavigationTaskID}" >
                                        <g:actionSubmit action="livetracking_updatetestresults" value="${message(code:'fc.livetracking.results.updatetestresults')}"/>
                                    </g:if>
                                </td>
                                <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                            </tr>
                        </tfoot>
                    </table>
                    <a name="end"/>
                </g:form>
            </div>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>