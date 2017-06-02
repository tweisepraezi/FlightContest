<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.listplanning')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskplanning="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.listplanning')} - ${taskInstance.name()}</h2>
                <g:form id="${taskInstance.id}" method="post" >
                    <a name="start"/>
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/></td>
	                            <g:if test="${taskInstance.planningtest}">
	                                <td><g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/> (${taskInstance.planningtest.planningtesttasks?.size()} ${message(code:'fc.planningtesttask.list')})</td>
	                            </g:if> <g:else>
	                                <td><g:link controller="planningTest" params="${['task.id':taskInstance?.id,'taskid':taskInstance?.id,'fromlistplanning':true]}" action="create">${message(code:'fc.planningtest.add')}</g:link></td>
	                            </g:else>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'timetableoverview')}"/></td>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'timetablejury')}"/></td>
                                <td style="width:1%;"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></td>
                            </tr>
                            <tr>
                                <g:if test="${!taskInstance.hideResults}">
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </g:if>
                                <g:else>
                                    <td/>
                                </g:else>
	                            <g:if test="${taskInstance.flighttest}">
	                                <td><g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/> (${taskInstance.flighttest.flighttestwinds?.size()} ${message(code:'fc.flighttestwind.list')}<g:if test="${taskInstance.flighttest.IsObservationSignUsed()}">, ${message(code:'fc.observation')}</g:if>)</td>
	                            </g:if> <g:else>
	                                <td><g:link controller="flightTest" params="${['task.id':taskInstance?.id,'taskid':taskInstance?.id,'fromlistplanning':true]}" action="create">${message(code:'fc.flighttest.add')}</g:link></td>
	                            </g:else>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'timetable')}"/></td>
                                <td/>
                                <td/>
                            </tr>
                        </tbody>
                    </table>
                    <table>
                    	<g:set var="timetable_version" value="${taskInstance.GetTimeTableVersion()}"></g:set>
                        <thead>
                            <tr>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <th class="table-head" colspan="5">${message(code:'fc.crew.list')}</th>
	                            </g:if>
	                            <g:else>
	                                <th class="table-head" colspan="4">${message(code:'fc.crew.list')}</th>
	                            </g:else>
                                <th class="table-head" colspan="3">${message(code:'fc.test.taskdata')}</th>
                                <th class="table-head" colspan="5">${message(code:'fc.test.timetable')} (${message(code:'fc.version')} ${timetable_version}<g:if test="${taskInstance.timetableModified}">*</g:if>)</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.team')}</th>
                                <g:if test="${taskInstance.contest.resultClasses}">
                                	<th>${message(code:'fc.resultclass')}</th>
                                </g:if>
                                <th colspan="2">${message(code:'fc.planningtesttask')}</th>
                                <th>${message(code:'fc.flighttestwind')}</th>
                               
                                <g:if test="${taskInstance.planningTestDuration == 0}">
                                    <th>${message(code:'fc.test.planning.publish')}</th>
                                </g:if>
                                <g:else>
                                    <th>${message(code:'fc.test.planning')}</th>
                                </g:else>
                                <th>${message(code:'fc.test.takeoff')}</th>
                                <th>${message(code:'fc.test.landing')}</th>
                                <th>${message(code:'fc.test.arrival')}</th>
                                <th>${message(code:'fc.test.flightplan')}</th>
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
	                                        <td><g:checkBox name="${testInstanceID}" value="${true}" /> <g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:if> <g:else>
	                                        <td><g:checkBox name="${testInstanceID}" value="${false}" /> <g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:else>
	                            
	                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                                    
                                    	<td><g:if test="${testInstance.taskAircraft}"><g:aircraft var="${testInstance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/><g:if test="${testInstance.taskAircraft?.user1 && testInstance.taskAircraft?.user2}"> *</g:if><g:if test="${testInstance.taskAircraft != testInstance.crew.aircraft}"> !</g:if></g:if><g:else>${message(code:'fc.noassigned')}</g:else> (${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}<g:if test="${testInstance.taskTAS != testInstance.crew.tas}"> !</g:if>)</td>
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
                                        <g:elseif test="${testInstance.disabledCrew}">
                                            <td colspan="8">${message(code:'fc.test.crewdisabled')}</td>
                                        </g:elseif>
										<g:else>
		                                    <g:if test="${testInstance.planningtesttask}">
		                                        <td colspan="2"><g:planningtesttask var="${testInstance.planningtesttask}" link="${createLink(controller:'planningTestTask',action:'edit')}"/> <a href="${createLink(controller:'test',action:'planningtask')}/${testInstance.id}">${message(code:'fc.test.planningtask.here')}</a></td>
		                                    </g:if> <g:else>
		                                        <td colspan="2">${message(code:'fc.noassigned')}</td>
		                                    </g:else>
		                                    
		                                    <g:if test="${testInstance.flighttestwind}">
		                                        <td><g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'edit')}"/></td>
		                                    </g:if> <g:else>
		                                        <td>${message(code:'fc.noassigned')}</td>                                    
		                                    </g:else>
		                                    
											<g:if test="${testInstance.timeCalculated}">
		                                        <td>${testInstance.testingTime?.format('HH:mm')}</td>
		                                        <g:if test="${testInstance.takeoffTimeWarning}">
		                                            <td class="errors">${testInstance.takeoffTime?.format('HH:mm')} !</td>
		                                        </g:if> <g:else>
		                                            <td>${testInstance.takeoffTime?.format('HH:mm')}</td>
		                                        </g:else>
												<td>${testInstance.maxLandingTime?.format('HH:mm:ss')}</td>
		                                        <g:if test="${testInstance.arrivalTimeWarning}">
		                                            <td class="errors">${testInstance.arrivalTime?.format('HH:mm:ss')} !</td>
		                                        </g:if> <g:else>
		                                            <td>${testInstance.arrivalTime?.format('HH:mm:ss')}</td>
		                                        </g:else>
		                                        <td><a href="${createLink(controller:'test',action:'flightplan')}/${testInstance.id}">${message(code:'fc.test.flightplan.here')}</a></td>
		                                    </g:if> <g:else>
		                                        <td colspan="5">${message(code:'fc.nocalculated')}</td>
		                                    </g:else>
	                                    </g:else>
	                                </tr>
                                </g:if>
                            </g:each>
                        </tbody>
                    </table>
                    <table>
                        <tfoot>
                            <tr>
                                <td colspan="2"><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}"/></td>
                                <g:if test="${taskInstance.contest.resultClasses}">
                                	<td colspan="3"><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}"/> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}"/></td>
                                </g:if>
                                <g:else>
                                	<td colspan="2"><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}"/> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}"/></td>
                                </g:else>
                                <td colspan="2"><g:actionSubmit action="assignplanningtesttask" value="${message(code:'fc.planningtesttask.assign')}"/></td>
                                <td><g:actionSubmit action="assignflighttestwind" value="${message(code:'fc.flighttestwind.assign')}"/></td>
                                <td colspan="3"><g:actionSubmit action="calculatetimetable" value="${message(code:'fc.test.timetable.calculate')}"/></td>
                                <td colspan="2"><g:actionSubmit action="disablecrew" value="${message(code:'fc.test.disablecrew')}"/></td>
                                <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                            </tr>
                            <tr class="join">
                            	<td colspan="2"><g:actionSubmit action="selectend" value="${message(code:'fc.selectend')}"/></td>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                            	<td colspan="3"><g:actionSubmit action="moveend" value="${message(code:'fc.test.moveend')}"/>
                                </g:if>
                                <g:else>
	                            	<td colspan="2"><g:actionSubmit action="moveend" value="${message(code:'fc.test.moveend')}"/>
                                </g:else>
                            	<td colspan="2"><g:actionSubmit action="printplanningtesttask" value="${message(code:'fc.planningtesttask.print')}"/></td>
                            	<td></td>
                            	<td colspan="3"><g:actionSubmit action="printflightplans" value="${message(code:'fc.test.flightplan.print')}" /></td>
                                <td colspan="2"><g:actionSubmit action="enablecrew" value="${message(code:'fc.test.enablecrew')}"/></td>
                                <td/>
                            </tr>
                            <tr class="join">
                                <td colspan="2"><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}"/></td>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <td colspan="3"><g:actionSubmit action="resetsequence" value="${message(code:'fc.test.sequence.toreset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></td>
                                </g:if>
                                <g:else>
                                	<td colspan="2"><g:actionSubmit action="resetsequence" value="${message(code:'fc.test.sequence.toreset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></td>
								</g:else>
								<g:if test="${taskInstance.flighttest?.IsObservationSignUsed()}">
								    <td colspan="3"><g:actionSubmit action="printobservation" value="${message(code:'fc.observation.print')}"/></td>
								</g:if>
								<g:else>
								    <td colspan="3"></td>
								</g:else>
                                <td colspan="3"><g:actionSubmit action="timeadd" value="${message(code:'fc.test.time.add')}" /> <g:actionSubmit action="timesubtract" value="${message(code:'fc.test.time.subtract')}" /></td>
                                <td colspan="2"><g:actionSubmit action="exporttimetable" value="${message(code:'fc.test.timetable.export')}" /></td>
                                <td/>
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