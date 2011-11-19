<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.debriefing')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.debriefing')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td colspan="4">${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.crew.country')}: ${testInstance.crew.country}</td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.crew.aircraft}">
		                                    ${testInstance.crew.aircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.crew.aircraft}">
                                            ${testInstance.crew.aircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${testInstance.task.planningTestRun && testInstance.planningTestComplete}">
	                        <br/>
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th class="table-head">${message(code:'fc.planningresults.summary')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td>${message(code:'fc.planningresults.legpenalties')}: ${testInstance.planningTestLegPenalties}</td>
	                                </tr>
	                                <tr>
	                                    <td>${message(code:'fc.planningtest.giventolate')}: 
		                                   	<g:if test="${testInstance.planningTestGivenTooLate}">
		                                       	${testInstance.task.contest.planningTestPlanTooLatePoints},
		                                   	</g:if>
		                                   	<g:else>
		                                       	0,
		                                   	</g:else>
	                                    	${message(code:'fc.planningtest.exitroomtolate')}:
	                                    	<g:if test="${testInstance.planningTestExitRoomTooLate}">
	                                        	${testInstance.task.contest.planningTestExitRoomTooLatePoints}
	                                    	</g:if>
	                                    	<g:else>
	                                        	0
	                                    	</g:else>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td>${message(code:'fc.penalties')}: ${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.task.flightTestRun && testInstance.flightTestComplete}">
	                        <g:if test="${CoordResult.countByTest(testInstance)}" >
	                            <br/>
	                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
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
	                        </g:if>
	                        <br/>
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th class="table-head">${message(code:'fc.flightresults.summary')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td>${message(code:'fc.flightresults.checkpointpenalties')}: ${testInstance.flightTestCheckPointPenalties}</td>
	                                </tr>
	                                <tr>
	                                    <td>${message(code:'fc.flighttest.takeoffmissed')}: 
	                                    	<g:if test="${testInstance.flightTestTakeoffMissed}">
	                                        	${testInstance.task.contest.flightTestTakeoffMissedPoints},
	                                    	</g:if>
	                                    	<g:else>
	                                        	0,
	                                    	</g:else>
	                                    	${message(code:'fc.flighttest.badcoursestartlanding')}:
	                                    	<g:if test="${testInstance.flightTestBadCourseStartLanding}">
	                                        	${testInstance.task.contest.flightTestBadCourseStartLandingPoints}
	                                    	</g:if>
	                                    	<g:else>
	                                        	0
	                                    	</g:else>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td>${message(code:'fc.flighttest.landingtolate')}:
	                                    	<g:if test="${testInstance.flightTestLandingTooLate}">
	                                        	${testInstance.task.contest.flightTestLandingToLatePoints},
	                                    	</g:if>
	                                    	<g:else>
	                                        	0,
	                                    	</g:else>
		                                    ${message(code:'fc.flighttest.giventolate')}:
	                                    	<g:if test="${testInstance.flightTestGivenTooLate}">
	                                        	${testInstance.task.contest.flightTestGivenToLatePoints}
	                                    	</g:if>
	                                    	<g:else>
	                                        	0
	                                    	</g:else>
	                                    </td>
	                                </tr>
	                                <tr>
	                                	<td>${message(code:'fc.penalties')}: ${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.task.observationTestRun && testInstance.observationTestComplete}">
	                        <br/>
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th class="table-head">${message(code:'fc.observationresults.summary')}</th>
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td>${message(code:'fc.observationresults.turnpointphotopenalties')}: ${testInstance.observationTestTurnPointPhotoPenalties},
	                                        ${message(code:'fc.observationresults.routephotopenalties')}: ${testInstance.observationTestRoutePhotoPenalties},
	                                        ${message(code:'fc.observationresults.groundtargetpenalties')}: ${testInstance.observationTestGroundTargetPenalties}
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td>${message(code:'fc.penalties')}: ${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </g:if>
                        <br/>
                        <table width="100%">
                            <thead>
                                <tr>
                                    <th class="table-head">${message(code:'fc.summary')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
	                            	<g:if test="${testInstance.task.planningTestRun}">
	                                    <g:if test="${testInstance.planningTestComplete}">
		                                    ${message(code:'fc.planningresults.planning')}: ${testInstance.planningTestPenalties},
	                                    </g:if> <g:else>
	                                    	${message(code:'fc.planningresults.planning')}: ${message(code:'fc.test.results.position.none')},
	                                    </g:else>
		                            </g:if>
    	                        	<g:if test="${testInstance.task.flightTestRun}">
	                                    <g:if test="${testInstance.flightTestComplete}">
	                                        ${message(code:'fc.flightresults.flight')}: ${testInstance.flightTestPenalties},
	                                    </g:if> <g:else>
	                                    	${message(code:'fc.flightresults.flight')}: ${message(code:'fc.test.results.position.none')},
	                                    </g:else>
		                            </g:if>
	                            	<g:if test="${testInstance.task.observationTestRun}">
	                                    <g:if test="${testInstance.observationTestComplete}">
	                                        ${message(code:'fc.observationresults.observations')}: ${testInstance.observationTestPenalties},
	                                    </g:if> <g:else>
	                                    	${message(code:'fc.observationresults.observations')}: ${message(code:'fc.test.results.position.none')},
	                                    </g:else>
		                            </g:if>
	                            	<g:if test="${testInstance.task.landingTestRun}">
	                                    <g:if test="${testInstance.landingTestComplete}">
	                                        ${message(code:'fc.landingresults.landing')}: ${testInstance.landingTestPenalties},
	                                    </g:if> <g:else>
	                                    	${message(code:'fc.landingresults.landing')}: ${message(code:'fc.test.results.position.none')},
	                                    </g:else>
		                            </g:if>
	                            	<g:if test="${testInstance.task.specialTestRun}">
	                                    <g:if test="${testInstance.specialTestComplete}">
	                                        ${message(code:'fc.specialresults.other')}: ${testInstance.specialTestPenalties},
	                                    </g:if> <g:else>
	                                    	${message(code:'fc.specialresults.other')}: ${message(code:'fc.test.results.position.none')},
	                                    </g:else>
		                            </g:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.penalties.total')}: ${testInstance.taskPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>