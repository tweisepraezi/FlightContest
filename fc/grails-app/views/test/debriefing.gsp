<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.protestprotocol')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.protestprotocol')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.country')}:</td>
                                    <td>${testInstance.crew.country}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
	                                    <td>${testInstance.crew.aircraft.type}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${testInstance.task.planningTestRun && testInstance.planningTestComplete}">
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th colspan="4" class="table-head">${message(code:'fc.planningresults.summary')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.planningresults.legpenalties')}:</td>
	                                    <td>${testInstance.planningTestLegPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.planningtest.giventolate')}:</td>
	                                    <g:if test="${testInstance.planningTestGivenTooLate}">
	                                        <td>${testInstance.task.contest.planningTestPlanTooLatePoints} ${message(code:'fc.points')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>0 ${message(code:'fc.points')}</td>
	                                    </g:else>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.planningtest.exitroomtolate')}:</td>
	                                    <g:if test="${testInstance.planningTestExitRoomTooLate}">
	                                        <td>${testInstance.task.contest.planningTestExitRoomTooLatePoints} ${message(code:'fc.points')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>0 ${message(code:'fc.points')}</td>
	                                    </g:else>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
	                                    <td class="points">${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.task.flightTestRun && testInstance.flightTestComplete}">
	                        <g:if test="${CoordResult.countByTest(testInstance)}" >
	                            <div>
	                                <table>
	                                    <thead>
	                                        <tr>
	                                            <th colspan="7" class="table-head">${message(code:'fc.flightresults.details')}</th>
	                                        </tr>
	                                        <tr>
	                                            <th>${message(code:'fc.title')}</th>
	                                            <th>${message(code:'fc.test.results.plan')}</th>
	                                            <th>${message(code:'fc.test.results.measured')}</th>
	                                            <th>${message(code:'fc.points')}</th>
	                                            <th>${message(code:'fc.procedureturn')}</th>
	                                            <th>${message(code:'fc.badcoursenum')}</th>
	                                            <th>${message(code:'fc.altitude')}</th>
	                                        </tr>
	                                    </thead>
	                                    <tbody>
	                                        <g:set var="penaltyCoordSummary" value="${new Integer(0)}" />
	                                        <g:set var="penaltyProcedureTurnSummary" value="${new Integer(0)}" />
	                                        <g:set var="penaltyBadCourseSummary" value="${new Integer(0)}" />
	                                        <g:set var="penaltyAltitudeSummary" value="${new Integer(0)}" />
	                                        <g:each var="coordResultInstance" in="${CoordResult.findAllByTest(testInstance)}">
	                                            <g:if test="${lastCoordResultInstance}">
	                                                <g:set var="penaltyCoordSummary" value="${penaltyCoordSummary + lastCoordResultInstance.penaltyCoord}" />
	                                                <tr>
	                                                    <td>${lastCoordResultInstance.titleCode()}</td>
	                                                    <td>${FcMath.TimeStr(lastCoordResultInstance.planCpTime)}</td>
	                                                    <td>${FcMath.TimeStr(lastCoordResultInstance.resultCpTime)}</td>
	                                                    <td>${lastCoordResultInstance.penaltyCoord}</td>
	                                                    <g:if test="${coordResultInstance.planProcedureTurn}">
	                                                        <g:if test="${coordResultInstance.resultProcedureTurnEntered}">
	                                                            <g:if test="${coordResultInstance.resultProcedureTurnEntered}">
	                                                                <g:if test="${coordResultInstance.resultProcedureTurnNotFlown}">
	                                                                    <g:set var="penaltyProcedureTurnSummary" value="${penaltyProcedureTurnSummary + coordResultInstance.test.task.contest.flightTestProcedureTurnNotFlownPoints}" />
	                                                                    <td>${coordResultInstance.test.task.contest.flightTestProcedureTurnNotFlownPoints}</td>
	                                                                </g:if>
	                                                                <g:else>
	                                                                    <td>0</td>
	                                                                </g:else>
	                                                            </g:if>
	                                                            <g:else>
	                                                                <td/>
	                                                            </g:else>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td/>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td/>
	                                                    </g:else>
	                                                    <g:if test="${lastCoordResultInstance.resultEntered}">
	                                                        <g:set var="penaltyBadCourseSummary" value="${penaltyBadCourseSummary + lastCoordResultInstance.resultBadCourseNum*lastCoordResultInstance.test.task.contest.flightTestBadCoursePoints}" />
	                                                        <td>${lastCoordResultInstance.resultBadCourseNum*lastCoordResultInstance.test.task.contest.flightTestBadCoursePoints}</td>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td/>
	                                                    </g:else>
	                                                    <g:if test="${lastCoordResultInstance.resultAltitude}">
	                                                        <g:if test="${lastCoordResultInstance.resultMinAltitudeMissed}">
	                                                            <g:set var="penaltyAltitudeSummary" value="${penaltyAltitudeSummary + lastCoordResultInstance.test.task.contest.flightTestMinAltitudeMissedPoints}" />
	                                                            <td>${lastCoordResultInstance.test.task.contest.flightTestMinAltitudeMissedPoints}</td>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td>0</td>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td>0</td>
	                                                    </g:else>
	                                                </tr>
	                                            </g:if>
	                                            <g:set var="lastCoordResultInstance" value="${coordResultInstance}" />
	                                        </g:each>
	                                        <g:if test="${lastCoordResultInstance}">
	                                            <g:set var="penaltyCoordSummary" value="${penaltyCoordSummary + lastCoordResultInstance.penaltyCoord}" />
	                                            <tr>
	                                                <td>${lastCoordResultInstance.titleCode()}</td>
	                                                <td>${FcMath.TimeStr(lastCoordResultInstance.planCpTime)}</td>
	                                                <td>${FcMath.TimeStr(lastCoordResultInstance.resultCpTime)}</td>
	                                                <td>${lastCoordResultInstance.penaltyCoord}</td>
	                                                <td/>
	                                                <g:if test="${lastCoordResultInstance.resultEntered}">
	                                                    <g:set var="penaltyBadCourseSummary" value="${penaltyBadCourseSummary + lastCoordResultInstance.resultBadCourseNum*lastCoordResultInstance.test.task.contest.flightTestBadCoursePoints}" />
	                                                    <td>${lastCoordResultInstance.resultBadCourseNum*lastCoordResultInstance.test.task.contest.flightTestBadCoursePoints}</td>
	                                                </g:if>
	                                                <g:else>
	                                                    <td/>
	                                                </g:else>
	                                                <g:if test="${lastCoordResultInstance.resultAltitude}">
	                                                    <g:if test="${lastCoordResultInstance.resultMinAltitudeMissed}">
	                                                        <g:set var="penaltyAltitudeSummary" value="${penaltyAltitudeSummary + lastCoordResultInstance.test.task.contest.flightTestMinAltitudeMissedPoints}" />
	                                                        <td>${lastCoordResultInstance.test.task.contest.flightTestMinAltitudeMissedPoints}</td>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td>0</td>
	                                                    </g:else>
	                                                </g:if>
	                                                <g:else>
	                                                    <td>0</td>
	                                                </g:else>
	                                            </tr>
	                                        </g:if>
	                                    </tbody>
	                                    <tfoot>
	                                        <tr>
	                                            <td/>
	                                            <td/>
	                                            <td/>
	                                            <td>${penaltyCoordSummary}</td>
	                                            <td>${penaltyProcedureTurnSummary}</td>
	                                            <td>${penaltyBadCourseSummary}</td>
	                                            <td>${penaltyAltitudeSummary}</td>
	                                        </tr>
	                                    </tfoot>
	                                </table>
	                            </div>
	                        </g:if>
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th colspan="4" class="table-head">${message(code:'fc.flightresults.summary')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.flightresults.checkpointpenalties')}:</td>
	                                    <td>${testInstance.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.flighttest.takeoffmissed')}:</td>
	                                    <g:if test="${testInstance.flightTestTakeoffMissed}">
	                                        <td>${testInstance.task.contest.flightTestTakeoffMissedPoints} ${message(code:'fc.points')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>0 ${message(code:'fc.points')}</td>
	                                    </g:else>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.flighttest.badcoursestartlanding')}:</td>
	                                    <g:if test="${testInstance.flightTestBadCourseStartLanding}">
	                                        <td>${testInstance.task.contest.flightTestBadCourseStartLandingPoints} ${message(code:'fc.points')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>0 ${message(code:'fc.points')}</td>
	                                    </g:else>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.flighttest.landingtolate')}:</td>
	                                    <g:if test="${testInstance.flightTestLandingTooLate}">
	                                        <td>${testInstance.task.contest.flightTestLandingToLatePoints} ${message(code:'fc.points')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>0 ${message(code:'fc.points')}</td>
	                                    </g:else>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.flighttest.giventolate')}:</td>
	                                    <g:if test="${testInstance.flightTestGivenTooLate}">
	                                        <td>${testInstance.task.contest.flightTestGivenToLatePoints} ${message(code:'fc.points')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>0 ${message(code:'fc.points')}</td>
	                                    </g:else>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
	                                    <td class="points">${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.task.observationTestRun && testInstance.observationTestComplete}">
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th colspan="4" class="table-head">${message(code:'fc.observationresults.summary')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.observationresults.turnpointphotopenalties')}:</td>
	                                    <td>${testInstance.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.observationresults.routephotopenalties')}:</td>
	                                    <td>${testInstance.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.observationresults.groundtargetpenalties')}:</td>
	                                    <td>${testInstance.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
	                                    <td class="points">${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </g:if>
                        <table>
                            <thead>
                                <tr>
                                    <th colspan="4" class="table-head">${message(code:'fc.summary')}</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<g:if test="${testInstance.task.planningTestRun}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')} ${message(code:'fc.planningresults.planning')}:</td>
	                                    <g:if test="${testInstance.planningTestComplete}">
	                                    	<td>${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
	                                    </g:if> <g:else>
	                                    	<td>${message(code:'fc.test.results.position.none')}</td>
	                                    </g:else>
	                                </tr>
	                            </g:if>
                            	<g:if test="${testInstance.task.flightTestRun}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')} ${message(code:'fc.flightresults.flight')}:</td>
	                                    <g:if test="${testInstance.flightTestComplete}">
	                                    	<td>${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
	                                    </g:if> <g:else>
	                                    	<td>${message(code:'fc.test.results.position.none')}</td>
	                                    </g:else>
	                                </tr>
	                            </g:if>
                            	<g:if test="${testInstance.task.observationTestRun}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')} ${message(code:'fc.observationresults.observations')}:</td>
	                                    <g:if test="${testInstance.observationTestComplete}">
	                                    	<td>${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
	                                    </g:if> <g:else>
	                                    	<td>${message(code:'fc.test.results.position.none')}</td>
	                                    </g:else>
	                                </tr>
	                            </g:if>
                            	<g:if test="${testInstance.task.landingTestRun}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')} ${message(code:'fc.landingresults.landing')}:</td>
	                                    <g:if test="${testInstance.landingTestComplete}">
		                                    <td>${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
	                                    </g:if> <g:else>
	                                    	<td>${message(code:'fc.test.results.position.none')}</td>
	                                    </g:else>
	                                </tr>
	                            </g:if>
                            	<g:if test="${testInstance.task.specialTestRun}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')} ${message(code:'fc.specialresults.other')}:</td>
	                                    <g:if test="${testInstance.specialTestComplete}">
		                                    <td>${testInstance.specialTestPenalties} ${message(code:'fc.points')}</td>
	                                    </g:if> <g:else>
	                                    	<td>${message(code:'fc.test.results.position.none')}</td>
	                                    </g:else>
	                                </tr>
	                            </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <td class="points">${testInstance.taskPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:actionSubmit action="printdebriefing" value="${message(code:'fc.print')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>