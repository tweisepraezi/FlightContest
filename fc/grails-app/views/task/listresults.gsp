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
                <h2>${message(code:'fc.task.listresults')} - ${taskInstance.name()}</h2>
                <g:form id="${taskInstance.id}" method="post" >
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/></td>
	                            <g:if test="${taskInstance.flighttest && taskInstance.flightTestRun}">
	                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'disabledcheckpoints')}"/></td>
	                            </g:if>
	                            <g:else>
	                            	<td/>
	                            </g:else>
                            </tr>
                            <tr>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                <td/>
                            </tr>
                        </tbody>
                    </table>
                    <table>
                        <g:set var="results_columns" value="${new Integer(3)}"></g:set>
                        <g:if test="${taskInstance.planningTestRun}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.flightTestRun}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.observationTestRun}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.landingTestRun}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <g:if test="${taskInstance.specialTestRun}">
                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                        </g:if>
                        <thead>
                            <tr>
                                <th class="table-head" colspan="4">${message(code:'fc.crew.list')}</th>
                                <th class="table-head" colspan="${results_columns}">${message(code:'fc.test.results')}</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.tas')}</th>
                               
                                <g:if test="${taskInstance.planningTestRun}">
	                                <th>${message(code:'fc.planningresults.planning')}</th>
                                </g:if>
                                <g:if test="${taskInstance.flightTestRun}">
	                                <th>${message(code:'fc.flightresults.flight')}</th>
                                </g:if>
                                <g:if test="${taskInstance.observationTestRun}">
	                                <th>${message(code:'fc.observationresults.observations')}</th>
                                </g:if>
                                <g:if test="${taskInstance.landingTestRun}">
	                                <th>${message(code:'fc.landingresults.landing')}</th>
                                </g:if>
                                <g:if test="${taskInstance.specialTestRun}">
	                                <th>${message(code:'fc.specialresults.other')}</th>
                                </g:if>
                                <th>${message(code:'fc.test.debriefing')}</th>
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
	    
	                                    <g:set var="testInstanceID" value="selectedTestID${testInstance.id.toString()}"></g:set>
	                                    <g:if test="${flash.selectedTestIDs && (flash.selectedTestIDs[testInstanceID] == 'on')}">
	                                        <td><g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:if> <g:else>
	                                        <td><g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:else>
	                            
	                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                                    
	                                    <g:if test="${testInstance.crew.aircraft}">
	                                        <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
	                                    </g:if> <g:else>
	                                        <td>${message(code:'fc.noassigned')}</td>
	                                    </g:else>
	                                    
	                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
	                                    
										<g:if test="${testInstance.crew.disabled}">
											<td colspan="8">${message(code:'fc.disabled')}</td>
										</g:if>
										<g:else>
			                                <g:if test="${taskInstance.planningTestRun}">
			                                    <g:if test="${testInstance.planningtesttask}">
			                                    	<td><g:if test="${testInstance.planningTestComplete}">${testInstance.planningTestPenalties} ${message(code:'fc.points')} </g:if><a href="${createLink(controller:'test',action:'planningtaskresults')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
												</g:if> <g:else>
													<td>${message(code:'fc.noassigned')}</td>
												</g:else>
											</g:if>
												
											<g:if test="${taskInstance.flightTestRun}">
												<g:if test="${testInstance.timeCalculated}">
			                                    	<td><g:if test="${testInstance.flightTestComplete}">${testInstance.flightTestPenalties} ${message(code:'fc.points')} </g:if><a href="${createLink(controller:'test',action:'flightresults')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
												</g:if> <g:else>
													<td>${message(code:'fc.nocalculated')}</td>
												</g:else>
											</g:if>
											
			                                <g:if test="${taskInstance.observationTestRun}">
	        	                            	<td><g:if test="${testInstance.observationTestComplete}">${testInstance.observationTestPenalties} ${message(code:'fc.points')} </g:if><a href="${createLink(controller:'test',action:'observationresults')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
											</g:if>
											
			                                <g:if test="${taskInstance.landingTestRun}">
		                                    	<td><g:if test="${testInstance.landingTestComplete}">${testInstance.landingTestPenalties} ${message(code:'fc.points')} </g:if><a href="${createLink(controller:'test',action:'landingresults')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
											</g:if>
	                                    	
			                                <g:if test="${taskInstance.specialTestRun}">
		                                    	<td><g:if test="${testInstance.specialTestComplete}">${testInstance.specialTestPenalties} ${message(code:'fc.points')} </g:if><a href="${createLink(controller:'test',action:'specialresults')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
											</g:if>
			                                
		                                    <td><a href="${createLink(controller:'test',action:'debriefing')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
		                                    
		                                    <td>${testInstance.taskPenalties}</td>
		                                    
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
                                <td colspan="4"></td>
                                <td colspan="${results_columns}"><g:actionSubmit action="printdebriefings" value="${message(code:'fc.test.debriefing.print')}" /> <g:actionSubmit action="calculatepositions" value="${message(code:'fc.results.calculatepositions')}" /> <g:actionSubmit action="printresults" value="${message(code:'fc.test.results.print')}" /></td>
                            </tr>
                        </tfoot>
                    </table>
                </g:form>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>