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
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td class="detailtitle" />
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'show')}"/></td>
                            </tr>
                            <tr>
                                <td class="detailtitle" />
                                <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                            </tr>
                            <g:if test="${taskInstance.planningtest}">
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/> (${taskInstance.planningtest.planningtesttasks?.size()} ${message(code:'fc.planningtesttask.list')})</td>
                                </tr>
                            </g:if> <g:else>
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:link controller="planningTest" params="['task.id':taskInstance?.id,'taskid':taskInstance?.id,'fromlistplanning':true]" action="create">${message(code:'fc.planningtest.add')}</g:link></td>
                                </tr>
                            </g:else>
                            <g:if test="${taskInstance.flighttest}">
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/> (${taskInstance.flighttest.flighttestwinds?.size()} ${message(code:'fc.flighttestwind.list')})</td>
                                </tr>
                            </g:if> <g:else>
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:link controller="flightTest" params="['task.id':taskInstance?.id,'taskid':taskInstance?.id,'fromlistplanning':true]" action="create">${message(code:'fc.flighttest.add')}</g:link></td>
                                </tr>
                            </g:else>
                        </tbody>
                    </table>
                    <table>
                        <thead>
                            <tr>
                                <th class="table-head" colspan="4">${message(code:'fc.crew.list')}</th>
                                <th class="table-head" colspan="3">${message(code:'fc.test.taskdata')}</th>
                                <th class="table-head" colspan="4">${message(code:'fc.test.timetable')}</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.tas')}</th>
                                <th colspan="2">${message(code:'fc.planningtesttask')}</th>
                                <th>${message(code:'fc.flighttestwind')}</th>
                               
                                <th>${message(code:'fc.test.planning')}</th>
                                <th>${message(code:'fc.test.takeoff')}</th>
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
	                            		<g:set var="j" value="${i}"></g:set>
                            		</g:if>
                            		<g:else>
	                            		<g:set var="showline" value="${false}"></g:set>
									</g:else>
                            	</g:if>
                            	<g:else>
                            		<g:set var="showline" value="${true}"></g:set>
                            		<g:set var="j" value="${i}"></g:set>
                            	</g:else>
                            	<g:if test="${showline}">
	                                <tr class="${(j % 2) == 0 ? 'odd' : ''}">
	    
	                                    <g:set var="testInstanceID" value="selectedTestID${testInstance.id.toString()}"/>
	                                    <g:if test="${flash.selectedTestIDs && (flash.selectedTestIDs[testInstanceID] == 'on')}">
	                                        <td><g:checkBox name="${testInstanceID}" value="${true}" /> <g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:if> <g:else>
	                                        <td><g:checkBox name="${testInstanceID}" value="${false}" /> <g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
	                                    </g:else>
	                            
	                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
	                                    
	                                    <g:if test="${testInstance.crew.aircraft}">
	                                        <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
	                                    </g:if> <g:else>
	                                        <td>${message(code:'fc.noassigned')}</td>
	                                    </g:else>
	                                    
	                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
	                                    
	                                    <g:if test="${testInstance.planningtesttask}">
	                                        <td colspan="2"><g:planningtesttask var="${testInstance.planningtesttask}" link="${createLink(controller:'planningTestTask',action:'show')}"/> <a href="${createLink(controller:'test',action:'planningtask')}/${testInstance.id}">${message(code:'fc.test.planningtask.here')}</a></td>
	                                    </g:if> <g:else>
	                                        <td colspan="2">${message(code:'fc.noassigned')}</td>
	                                    </g:else>
	                                    
	                                    <g:if test="${testInstance.flighttestwind}">
	                                        <td><g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'show')}"/></td>
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
	                                        <g:if test="${testInstance.arrivalTimeWarning}">
	                                            <td class="errors">${testInstance.arrivalTime?.format('HH:mm')} !</td>
	                                        </g:if> <g:else>
	                                            <td>${testInstance.arrivalTime?.format('HH:mm')}</td>
	                                        </g:else>
	                                        <td><a href="${createLink(controller:'test',action:'flightplan')}/${testInstance.id}">${message(code:'fc.test.flightplan.here')}</a></td>
	                                    </g:if> <g:else>
	                                        <td colspan="4">${message(code:'fc.nocalculated')}</td>
	                                    </g:else>
	                                    
	                                </tr>
                                </g:if>
                            </g:each>
                        </tbody>
                        <tfoot>
                            <tr class="">
                                <td colspan="2"><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}" /></td>
                                <td colspan="2"><g:actionSubmit action="calculatesequence" value="${message(code:'fc.test.sequence.calculate')}" /></td>
                                <td colspan="2"><g:actionSubmit action="assignplanningtesttask" value="${message(code:'fc.planningtesttask.assign')}" /></td>
                                <td><g:actionSubmit action="assignflighttestwind" value="${message(code:'fc.flighttestwind.assign')}" /></td>
                                <td colspan="4"><g:actionSubmit action="calculatetimetable" value="${message(code:'fc.test.timetable.calculate')}" /> <g:actionSubmit action="printtimetable" value="${message(code:'fc.test.timetable.print')}" /></td>
                            </tr>
                            <tr class="join">
                                <td colspan="2"><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}" /></td>
                                <td colspan="2"><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}" /> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}" /></td>
                                <td><g:actionSubmit action="printplanningtesttask" value="${message(code:'fc.planningtesttask.print')}" /></td>
                                <td colspan="2"/>
                                <td colspan="4"><g:actionSubmit action="timeadd" value="${message(code:'fc.test.time.add')}" /> <g:actionSubmit action="timesubtract" value="${message(code:'fc.test.time.subtract')}" /> <g:actionSubmit action="printflightplans" value="${message(code:'fc.test.flightplan.print')}" /></td>
                            </tr>
                        </tfoot>
                    </table>
                </g:form>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>