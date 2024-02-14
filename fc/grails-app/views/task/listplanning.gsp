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
					<g:set var="add_col" value="${0}"/>
                    <g:if test="${taskInstance.contest.resultClasses}">
						<g:set var="add_col" value="${add_col+1}"/>
					</g:if>
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/></td>
	                            <g:if test="${taskInstance.planningtest}">
	                                <td><g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/> (${taskInstance.planningtest.planningtesttasks?.size()} ${message(code:'fc.planningtesttask.list')})</td>
	                            </g:if> <g:else>
	                                <td><g:if test="${!taskInstance.lockPlanning}"><g:link controller="planningTest" params="${['task.id':taskInstance?.id,'taskid':taskInstance?.id,'fromlistplanning':true]}" action="create">${message(code:'fc.planningtest.add')}</g:link></g:if></td>
	                            </g:else>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'timetableoverview')}"/></td>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'timetable')}"/></td>
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
	                                <td><g:if test="${!taskInstance.lockPlanning}"><g:link controller="flightTest" params="${['task.id':taskInstance?.id,'taskid':taskInstance?.id,'fromlistplanning':true]}" action="create">${message(code:'fc.flighttest.add')}</g:link></g:if></td>
	                            </g:else>
								<td><g:if test="${taskInstance.IsLandingTestRun()}"><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'landingstartlist')}"/></g:if></td>
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'timetablejudge')}"/></td>
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
                                    <td/>
                                    <td/>
                                    <td/>
                                </tr>
                            </g:if>
                        </tbody>
                    </table>
                    <table>
                    	<g:set var="timetable_version" value="${taskInstance.GetTimeTableVersion()}"></g:set>
                        <thead>
                            <tr>
                                <th class="table-head" colspan="${add_col+4}">${message(code:'fc.crew.list')}</th>
                                <th class="table-head" />
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
                                <th>${message(code:'fc.test.listpos')}</th>
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
                       		<g:set var="show_test" value="${false}"></g:set>
							<g:set var="page_pos" value="${1}"/>
                            <g:each var="test_instance" status="i" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                            	<g:if test="${session.showLimit}">
                            		<g:if test="${(i + 1 > session.showLimitStartPos) && (i < session.showLimitStartPos + session.showLimitCrewNum)}">
	                            		<g:set var="show_test" value="${true}"></g:set>
                            		</g:if>
                            		<g:else>
	                            		<g:set var="show_test" value="${false}"></g:set>
									</g:else>
                            	</g:if>
								<g:elseif test="${session.showPage}">
									<g:if test="${test_instance.pageBreak}">
										<g:set var="page_pos" value="${page_pos+1}"/>
									</g:if>
									<g:if test="${page_pos == session.showPagePos}">
										<g:set var="show_test" value="${true}"></g:set>
									</g:if>
                            		<g:else>
	                            		<g:set var="show_test" value="${false}"></g:set>
									</g:else>
								</g:elseif>
                            	<g:else>
                            		<g:set var="show_test" value="${true}"></g:set>
                            	</g:else>
                            	<g:if test="${show_test}">
									<g:set var="pagebreak_class" value=""></g:set>
									<g:if test="${test_instance.pageBreak}">
										<g:set var="pagebreak_class" value="pagebreak"></g:set>
									</g:if>
	                                <tr class="${(i % 2) == 0 ? 'odd' : ''} ${pagebreak_class}">
	    
                                        <td>
                                            <g:set var="testInstanceID" value="selectedTestID${test_instance.id.toString()}"></g:set>
                                            <g:if test="${flash.selectedTestIDs && (flash.selectedTestIDs[testInstanceID] == 'on')}">
                                                <g:checkBox name="${testInstanceID}" value="${true}" /> <g:testnum var="${test_instance}" link="${createLink(controller:'test',action:'show')}"/>
                                            </g:if> <g:else>
                                                <g:checkBox name="${testInstanceID}" value="${false}" /> <g:testnum var="${test_instance}" link="${createLink(controller:'test',action:'show')}"/>
                                            </g:else>
                                            <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.liveTrackingResultsTaskID && test_instance.crew.liveTrackingTeamID}" >
                                                <g:if test="${test_instance.taskLiveTrackingTeamID}">
                                                    <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="margin-left:0.5rem; height:0.7rem;"/>
                                                </g:if>
                                                <g:else>
                                                    <img src="${createLinkTo(dir:'images',file:'livetracking-off.svg')}" style="margin-left:0.5rem; height:0.7rem;"/>
                                                </g:else>
                                            </g:if>
                                        </td>
	                            
	                                    <td><g:crew var="${test_instance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                                    
                                    	<td><g:if test="${test_instance.taskAircraft}"><g:aircraft var="${test_instance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/><g:if test="${test_instance.taskAircraft?.user1 && test_instance.taskAircraft?.user2}"> *</g:if><g:if test="${test_instance.taskAircraft != test_instance.crew.aircraft}"> !</g:if></g:if><g:else>${message(code:'fc.noassigned')}</g:else> (${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}<g:if test="${test_instance.taskTAS != test_instance.crew.tas}"> !</g:if>)</td>
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
	                                    
                                        <g:set var="test_pos" value="${test_instance.GetTestPos()}"></g:set>
                                        <g:if test="${test_pos}">
                                            <td>${test_pos}</td>
                                        </g:if> <g:else>
                                            <td/>
                                        </g:else>
                                        
										<g:if test="${test_instance.crew.disabled}">
											<td colspan="9">${message(code:'fc.disabled')}</td>
										</g:if>
                                        <g:elseif test="${test_instance.disabledCrew}">
                                            <td colspan="9">${message(code:'fc.test.crewdisabled')}</td>
                                        </g:elseif>
										<g:else>
		                                    <g:if test="${test_instance.planningtesttask}">
		                                        <td colspan="2"><g:planningtesttask var="${test_instance.planningtesttask}" link="${createLink(controller:'planningTestTask',action:'edit')}"/> <a href="${createLink(controller:'test',action:'planningtask')}/${test_instance.id}">${message(code:'fc.test.planningtask.here')}</a></td>
		                                    </g:if> <g:else>
		                                        <td colspan="2">${message(code:'fc.noassigned')}</td>
		                                    </g:else>
		                                    
		                                    <g:if test="${test_instance.flighttestwind}">
		                                        <td><g:flighttestwind var="${test_instance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'edit')}"/></td>
		                                    </g:if> <g:else>
		                                        <td>${message(code:'fc.noassigned')}</td>                                    
		                                    </g:else>
		                                    
											<g:if test="${test_instance.timeCalculated}">
											    <g:set var="minutes_before" value="${test_instance.GetMinutesBeforeStartTime()}"/>
											    <g:if test="${minutes_before}">
                                                    <td>${test_instance.testingTime.format('HH:mm')} (${test_instance.GetTestingTime().format('HH:mm')})</td>
											    </g:if>
											    <g:else>
											        <td>${test_instance.testingTime.format('HH:mm')}</td>
											    </g:else>
		                                        <g:if test="${test_instance.takeoffTimeWarning}">
		                                            <td class="errors">${test_instance.takeoffTime?.format('HH:mm')} !</td>
		                                        </g:if> <g:else>
		                                            <td>${test_instance.takeoffTime?.format('HH:mm')}</td>
		                                        </g:else>
												<td>${test_instance.maxLandingTime?.format('HH:mm:ss')}</td>
		                                        <g:if test="${test_instance.arrivalTimeWarning}">
		                                            <td class="errors">${test_instance.arrivalTime?.format('HH:mm:ss')} !</td>
		                                        </g:if> <g:else>
		                                            <td>${test_instance.arrivalTime?.format('HH:mm:ss')}</td>
		                                        </g:else>
		                                        <td><a href="${createLink(controller:'test',action:'flightplan')}/${test_instance.id}">${message(code:'fc.test.flightplan.here')}</a></td>
		                                    </g:if> <g:else>
		                                        <td colspan="6">${message(code:'fc.nocalculated')}</td>
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
                               	<td colspan="${add_col+2}"><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}"/> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}"/></g:if></td>
                                <td colspan="2"><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="disablecrew" value="${message(code:'fc.test.disablecrew')}"/></g:if></td>
								<td><g:actionSubmit action="setpagebreak" value="${message(code:'fc.test.setpagebreak')}"/></td>
                                <td><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="assignplanningtesttask" value="${message(code:'fc.planningtesttask.assign')}"/></g:if></td>
                                <td><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="assignflighttestwind" value="${message(code:'fc.flighttestwind.assign')}"/></g:if></td>
                                <td colspan="3"><g:actionSubmit action="printflightplans" value="${message(code:'fc.test.flightplan.print')}" /></td>
                                <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                            </tr>
                            <tr class="join">
                            	<td colspan="2"><g:actionSubmit action="selectend" value="${message(code:'fc.selectend')}"/></td>
                            	<td colspan="${add_col+2}"><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="moveend" value="${message(code:'fc.test.moveend')}"/></g:if></td>
                                <td colspan="2"><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="enablecrew" value="${message(code:'fc.test.enablecrew')}"/></g:if></td>
								<td><g:actionSubmit action="resetallpagebreak" value="${message(code:'fc.test.resetpagebreak.all')}"/></td>
                            	<td><g:actionSubmit action="printplanningtesttask" value="${message(code:'fc.planningtesttask.print')}"/></td>
                            	<td colspan="3"><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="calculatetimetable" value="${message(code:'fc.test.timetable.calculate')}"/></g:if></td>
                            	<td><g:actionSubmit action="exporttimetable_label" value="${message(code:'fc.test.timetable.export.labelprint')}" /></td>
                                <td/>
                            </tr>
                            <tr class="join">
                                <td colspan="2"><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}"/></td>
                                <td colspan="${add_col+2}"><g:if test="${!taskInstance.lockPlanning}"><g:actionSubmit action="resetsequence" value="${message(code:'fc.test.sequence.toreset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></g:if></td>
                                <td colspan="2"></td>
								<td></td>
								<g:if test="${taskInstance.flighttest?.IsObservationSignUsed()}">
								    <td><g:actionSubmit action="printobservation" value="${message(code:'fc.observation.print')}"/></td>
								</g:if>
								<g:else>
								    <td></td>
								</g:else>
                                <td colspan="3"><g:if test="${!taskInstance.lockPlanning}">
                                    <g:actionSubmit action="timeadd" value="${message(code:'fc.test.time.add')}" />
                                    <g:actionSubmit action="timesubtract" value="${message(code:'fc.test.time.subtract')}" />
                                    <input type="text" id="addTimeValue" name="addTimeValue" value="${fieldValue(bean:taskInstance,field:'addTimeValue')}" size="3" /> ${message(code:'fc.time.min')}
                                </g:if></td>
                                <td><g:actionSubmit action="exporttimetable_startlist" value="${message(code:'fc.test.timetable.export.startlist')}" /></td>
                                <td/>
                            </tr>
                            <tr class="join">
                                <g:if test="${BootStrap.global.IsLiveTrackingPossible() && taskInstance.contest.liveTrackingContestID && taskInstance.contest.liveTrackingScorecard && taskInstance.liveTrackingNavigationTaskID}" >
                                    <td colspan="2"><g:actionSubmit action="livetracking_navigationtaskupdatecrews" value="${message(code:'fc.livetracking.navigationtaskupdatecrews')}"/></td>
									<g:if test="${taskInstance.liveTrackingTracksAvailable}">
										<td colspan="${add_col+2}"><g:actionSubmit action="livetracking_navigationtaskaddtrackcrews" value="${message(code:'fc.livetracking.navigationtaskaddtrackcrews')}"/></td>
									</g:if>
									<g:else>
										<td colspan="${add_col+2}"></td>
									</g:else>
                                </g:if>
                                <g:else>
                                    <td colspan="${add_col+4}"></td>
                                </g:else>
                                <td colspan="4"></td>
                                <td colspan="3"></td>
                                <td><g:actionSubmit action="exporttimetable_data" value="${message(code:'fc.test.timetable.export.data')}" /></td>
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