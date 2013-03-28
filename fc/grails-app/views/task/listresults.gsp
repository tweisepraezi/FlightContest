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
                            </tr>
                            <tr>
                                <g:if test="${!taskInstance.hidePlanning}">
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </g:if>
                                <g:else>
                                    <td/>
                                </g:else>
                                <td/>
                            </tr>
                        </tbody>
                    </table>
                    <table>
                        <g:set var="results_columns" value="${new Integer(3)}"></g:set>
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
	                                <th class="table-head" colspan="5">${message(code:'fc.crew.list')}</th>
	                            </g:if>
	                            <g:else>
	                            	<th class="table-head" colspan="4">${message(code:'fc.crew.list')}</th>
	                            </g:else>
                                <th class="table-head" colspan="${results_columns}">${message(code:'fc.test.results')}</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.team')}</th>
		                        <g:if test="${taskInstance.contest.resultClasses}">
                                	<th>${message(code:'fc.crew.resultclass')}</th>
		                        </g:if>
                                <g:if test="${taskInstance.IsPlanningTestRun()}">
	                                <th>${message(code:'fc.planningresults.planning')}</th>
                                </g:if>
                                <g:if test="${taskInstance.IsFlightTestRun()}">
	                                <th>${message(code:'fc.flightresults.flight')}</th>
                                </g:if>
                                <g:if test="${taskInstance.IsObservationTestRun()}">
	                                <th>${message(code:'fc.observationresults.observations')}</th>
                                </g:if>
                                <g:if test="${taskInstance.IsLandingTestRun()}">
	                                <th>${message(code:'fc.landingresults.landing')}</th>
                                </g:if>
                                <g:if test="${taskInstance.IsSpecialTestRun()}">
	                                <th>${message(code:'fc.specialresults.other')}</th>
                                </g:if>
                                <th>${message(code:'fc.crewresults.all')}</th>
                                <th>${message(code:'fc.test.results.summary')}</th>
                                <th>${message(code:'fc.test.results.position')}</th>
                            </tr>
                        </thead>
                        <tbody>
                       		<g:set var="showline" value="${false}"></g:set>
                            <g:each var="testInstance" status="i" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
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
	                                <tr class="${(i % 2) == 0 ? 'odd' : ''}">
	    
                                        <!-- search next id -->
                                        <g:set var="next" value="" />
                                        <g:set var="setnext" value="${false}" />
                                        <g:each var="test_instance2" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                            <g:if test="${setnext}">
                                                <g:set var="next" value="?next=${test_instance2.id}" />
                                                <g:set var="setnext" value="${false}" />
                                            </g:if>
                                            <g:if test="${test_instance2 == testInstance}">
                                                <g:set var="setnext" value="${true}" />
                                            </g:if>
                                        </g:each>
                                                
	                                    <g:set var="testInstanceID" value="selectedTestID${testInstance.id.toString()}"></g:set>
	                                    <g:if test="${flash.selectedTestIDs && (flash.selectedTestIDs[testInstanceID] == 'on')}">
	                                        <td><g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:if> <g:else>
	                                        <td><g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:else>
	                            
	                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                                    
                                    	<td><g:if test="${testInstance.taskAircraft}"><g:aircraft var="${testInstance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></g:if><g:else>${message(code:'fc.noassigned')}</g:else> (${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')})</td>
                                        <g:if test="${testInstance.crew.team}">
                                        	<td><g:team var="${testInstance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
                                        </g:if>
                                        <g:else>
                                            <td>-</td>
                                        </g:else>
                                    	
	                                    <g:if test="${taskInstance.contest.resultClasses}">
	                                    	<g:if test="${testInstance.crew.resultclass}">
	                                    		<td><g:resultclass var="${testInstance.crew.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
	                                    	</g:if>
	                                    	<g:else>
	                                    		<td>${message(code:'fc.noassigned')}</td>
	                                    	</g:else>
	                                    </g:if>
	                                    
										<g:if test="${testInstance.crew.disabled}">
											<td colspan="8">${message(code:'fc.disabled')}</td>
										</g:if>
										<g:else>
			                                <g:if test="${taskInstance.IsPlanningTestRun()}">
			                                	<g:if test="${testInstance.IsPlanningTestRun()}">
				                                    <g:if test="${testInstance.planningtesttask}">
				                                    	<td>${testInstance.planningTestPenalties} <g:if test="${!testInstance.planningTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'planningtaskresults')}/${testInstance.id}${next}">${message(code:'fc.test.results.here')}</a></td>
													</g:if> <g:else>
														<td>${message(code:'fc.noassigned')}</td>
													</g:else>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
												
											<g:if test="${taskInstance.IsFlightTestRun()}">
												<g:if test="${testInstance.IsFlightTestRun()}">
													<g:if test="${testInstance.timeCalculated}">
				                                    	<td>${testInstance.flightTestPenalties} <g:if test="${!testInstance.flightTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'flightresults')}/${testInstance.id}${next}">${message(code:'fc.test.results.here')}</a></td>
													</g:if> <g:else>
														<td>${message(code:'fc.nocalculated')}</td>
													</g:else>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
											
			                                <g:if test="${taskInstance.IsObservationTestRun()}">
				                                <g:if test="${testInstance.IsObservationTestRun()}">
		        	                            	<td>${testInstance.observationTestPenalties} <g:if test="${!testInstance.observationTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'observationresults')}/${testInstance.id}${next}">${message(code:'fc.test.results.here')}</a></td>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
											
			                                <g:if test="${taskInstance.IsLandingTestRun()}">
				                                <g:if test="${testInstance.IsLandingTestRun()}">
		                                    		<td>${testInstance.landingTestPenalties} <g:if test="${!testInstance.landingTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'landingresults')}/${testInstance.id}${next}">${message(code:'fc.test.results.here')}</a></td>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
	                                    	
			                                <g:if test="${taskInstance.IsSpecialTestRun()}">
				                                <g:if test="${testInstance.IsSpecialTestRun()}">
			                                    	<td>${testInstance.specialTestPenalties} <g:if test="${!testInstance.specialTestComplete}">[${message(code:'fc.provisional')}] </g:if><a href="${createLink(controller:'test',action:'specialresults')}/${testInstance.id}${next}">${message(code:'fc.test.results.here')}</a></td>
												</g:if>
												<g:else>
													<td>${message(code:'fc.none')}</td>
												</g:else>
											</g:if>
			                                
		                                    <td><a href="${createLink(controller:'test',action:'crewresults')}/${testInstance.id}${next}">${message(code:'fc.test.results.here')}</a></td>
		                                    
		                                    <td>${testInstance.taskPenalties} ${message(code:'fc.points')}<g:if test="${testInstance.IsTestResultsProvisional(testInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></td>
		                                    
		                                    <g:if test="${testInstance.taskPosition}">
		                                        <td>${testInstance.taskPosition}</td>
		                                    </g:if> <g:else>
		                                        <td>${message(code:'fc.test.results.position.none')}</td>
		                                    </g:else>
	                                    </g:else>
	                                </tr>
                                </g:if>
                            </g:each>
                        </tbody>
                        <tfoot>
                            <tr class="">
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <td colspan="5"></td>
	                            </g:if>
	                            <g:else>
	                                <td colspan="4"></td>
	                            </g:else>
                                <td colspan="${results_columns}"><g:actionSubmit action="crewresultsprintquestion" value="${message(code:'fc.crewresults.all.print')}" tabIndex="1"/> <g:actionSubmit action="calculatepositions" value="${message(code:'fc.results.calculatepositions')}" tabIndex="2"/> <g:actionSubmit action="printresults" value="${message(code:'fc.test.results.print')}" tabIndex="3"/></td>
                            </tr>
                        </tfoot>
                    </table>
                </g:form>
            </div>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>