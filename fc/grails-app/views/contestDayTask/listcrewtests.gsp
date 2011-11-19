<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestdaytask.listcrewtests')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contestDayTask" contestdaytasknav="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestdaytask.listcrewtests')}</h2>
                <g:form id="${contestDayTaskInstance.id}" method="post" >
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td class="detailtitle">${message(code:'fc.crewtest.from')}:</td>
                                <td><g:contestday var="${contestDayTaskInstance?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                            </tr>
                            <tr>
                                <td class="detailtitle" />
                                <td><g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                            </tr>
                            <g:if test="${contestDayTaskInstance.navtest}">
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:navtest var="${contestDayTaskInstance.navtest}" link="${createLink(controller:'navTest',action:'show')}"/> (${contestDayTaskInstance.navtest.navtesttasks?.size()} ${message(code:'fc.navtesttask.list')})</td>
                                </tr>
                            </g:if> <g:else>
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:link controller="navTest" params="['contestdaytask.id':contestDayTaskInstance?.id,'contestdaytaskid':contestDayTaskInstance?.id,'fromlistcrewtests':true]" action="create">${message(code:'fc.navtest.add')}</g:link></td>
                                </tr>
                            </g:else>
                            <g:if test="${contestDayTaskInstance.flighttest}">
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:flighttest var="${contestDayTaskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/> (${contestDayTaskInstance.flighttest.flighttestwinds?.size()} ${message(code:'fc.flighttestwind.list')})</td>
                                </tr>
                            </g:if> <g:else>
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:link controller="flightTest" params="['contestdaytask.id':contestDayTaskInstance?.id,'contestdaytaskid':contestDayTaskInstance?.id,'fromlistcrewtests':true]" action="create">${message(code:'fc.flighttest.add')}</g:link></td>
                                </tr>
                            </g:else>
                        </tbody>
                    </table>
                    <table>
                        <thead>
                            <tr>
                                <th class="table-head"/>
                                <th class="table-head" colspan="5">${message(code:'fc.crewtest.taskdata')}</th>
                                <th class="table-head" colspan="4">${message(code:'fc.crewtest.timetable')}</th>
                                <th class="table-head" colspan="3">${message(code:'fc.crewtest.results')}</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.navtesttask')}</th>
                                <th>${message(code:'fc.flighttestwind')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.crewtest.tas')}</th>
                               
                                <th>${message(code:'fc.crewtest.testing')}</th>
                                <th>${message(code:'fc.crewtest.takeoff')}</th>
                                <th>${message(code:'fc.crewtest.arrival')}</th>
                                <th>${message(code:'fc.crewtest.flightplan')}</th>
                                
                                <th>${message(code:'fc.crewtest.results.points')}</th>
                                <th>${message(code:'fc.crewtest.results.summary')}</th>
                                <th>${message(code:'fc.crewtest.results.position')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each var="crewTestInstance" status="i" in="${CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:'viewpos'])}">
                                <tr class="${(i % 2) == 0 ? 'odd' : ''}">
    
                                    <g:set var="crewTestInstanceId" value="selectedCrewTestID${crewTestInstance.id.toString()}"/>
                                    <g:if test="${flash.selectedCrewTestIDs && (flash.selectedCrewTestIDs[crewTestInstanceId] == 'on')}">
                                        <td><g:checkBox name="${crewTestInstanceId}" value="${true}" /> <g:crewtestnum var="${crewTestInstance}" link="${createLink(controller:'crewTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td><g:checkBox name="${crewTestInstanceId}" value="${false}" /> <g:crewtestnum var="${crewTestInstance}" link="${createLink(controller:'crewTest',action:'show')}"/></td>
                                    </g:else>
                            
                                    <td><g:crew var="${crewTestInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                    
                                    <g:if test="${crewTestInstance.navtesttask}">
                                        <td><g:navtesttask var="${crewTestInstance.navtesttask}" link="${createLink(controller:'navTestTask',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>                                    
                                    </g:else>
                                    
                                    <g:if test="${crewTestInstance.flighttestwind}">
                                        <td><g:flighttestwind var="${crewTestInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>                                    
                                    </g:else>
                                    
                                    <g:if test="${crewTestInstance.aircraft}">
                                        <td><g:aircraft var="${crewTestInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                    
                                    <td>${crewTestInstance.TAS}${message(code:'fc.knot')}</td>
                                    
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.testingTime?.format('HH:mm')}</td>
                                        <g:if test="${crewTestInstance.takeoffTimeWarning}">
                                            <td class="errors">${crewTestInstance.takeoffTime?.format('HH:mm')} !</td>
                                        </g:if> <g:else>
                                            <td>${crewTestInstance.takeoffTime?.format('HH:mm')}</td>
                                        </g:else>
                                        <g:if test="${crewTestInstance.arrivalTimeWarning}">
                                            <td class="errors">${crewTestInstance.arrivalTime?.format('HH:mm')} !</td>
                                        </g:if> <g:else>
                                            <td>${crewTestInstance.arrivalTime?.format('HH:mm')}</td>
                                        </g:else>
                                        <td><a href="${createLink(controller:'crewTest',action:'flightplan')}/${crewTestInstance.id}">${message(code:'fc.crewtest.flightplan.here')}</a></td>
                                    </g:if> <g:else>
                                        <td colspan="4">${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                    
                                    <td><a href="${createLink(controller:'crewTest',action:'results')}/${crewTestInstance.id}">${message(code:'fc.crewtest.results.here')}</a></td>
                                    
                                    <td>${crewTestInstance.penaltySummary}</td>
                                    
                                    <g:if test="${crewTestInstance.positionContestDay}">
                                        <td>${crewTestInstance.positionContestDay}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.crewtest.results.position.none')}</td>
                                    </g:else>
                                    
                                </tr>
                            </g:each>
                        </tbody>
                        <tfoot>
                            <tr class="">
                                <td colspan="2"><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}" /></td>
                                <td><g:actionSubmit action="assignnavtesttask" value="${message(code:'fc.navtesttask.assign')}" /></td>
                                <td><g:actionSubmit action="assignflighttestwind" value="${message(code:'fc.flighttestwind.assign')}" /></td>
                                <td colspan="2"><g:actionSubmit action="calculatesequence" value="${message(code:'fc.crewtest.sequence.calculate')}" /></td>
                                <td colspan="4"><g:actionSubmit action="calculatetimetable" value="${message(code:'fc.crewtest.timetable.calculate')}" /> <g:actionSubmit action="printtimetable" value="${message(code:'fc.crewtest.timetable.print')}" /></td>
                                <td colspan="4"><g:actionSubmit action="calculatepositions" value="${message(code:'fc.crewtest.results.positions.calculate')}" /></td>
                            </tr>
                            <tr class="join">
                                <td colspan="2"><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}" /></td>
                                <td><g:actionSubmit action="printnavtesttask" value="${message(code:'fc.navtesttask.print')}" /></td>
                                <td/>
                                <td colspan="2"><g:actionSubmit action="moveup" value="${message(code:'fc.crewtest.moveup')}" /> <g:actionSubmit action="movedown" value="${message(code:'fc.crewtest.movedown')}" /></td>
                                <td colspan="4"><g:actionSubmit action="timeadd" value="${message(code:'fc.crewtest.time.add')}" /> <g:actionSubmit action="timesubtract" value="${message(code:'fc.crewtest.time.subtract')}" />  <g:actionSubmit action="printflightplans" value="${message(code:'fc.crewtest.flightplan.print')}" /></td>
                                <td colspan="4"><g:actionSubmit action="printresults" value="${message(code:'fc.crewtest.results.print')}" /></td>
                            </tr>
                        </tfoot>
                    </table>
                </g:form>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>