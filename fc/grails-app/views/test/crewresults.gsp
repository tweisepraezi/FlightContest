<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Crew)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Crew)}<g:if test="${testInstance.IsTestResultsProvisional(testInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></h2>
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
		                    	<g:if test="${testInstance.crew.team}">
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.crew.team')}:</td>
	                                    <td><g:team var="${testInstance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
	                                </tr>
    		                    </g:if>
		                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
	                                <tr>
	                                	<td class="detailtitle">${message(code:'fc.crew.resultclass')}:</td>
	                                	<td><g:resultclass var="${testInstance.crew.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
    		                        </tr>
    		                    </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <g:if test="${testInstance.taskAircraft}">
                                        <td><g:aircraft var="${testInstance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>
                                    <g:if test="${testInstance.taskAircraft}">
	                                    <td>${testInstance.taskAircraft.type}</td>
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
                        <table>
                            <tbody>
	                           	<g:if test="${testInstance.IsPlanningTestRun()}">
    	                        	<tr>
        	                            <td class="detailtitle">${message(code:'fc.planningresults.planning')}:</td>
        	                            <td>${testInstance.planningTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})<g:if test="${!testInstance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                     	</tr>
	                            </g:if>
   	                        	<g:if test="${testInstance.IsFlightTestRun()}">
			                    	<tr>
			                     		<td class="detailtitle">${message(code:'fc.flightresults.flight')}:</td>
        	                            <td>${testInstance.flightTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})<g:if test="${!testInstance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsObservationTestRun()}">
			                    	<tr>
			                     		<td class="detailtitle">${message(code:'fc.observationresults.observations')}:</td>
        	                            <td>${testInstance.observationTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})<g:if test="${!testInstance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsLandingTestRun()}">
			                    	<tr>
			                     		<td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
        	                            <td>${testInstance.landingTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
			                    </g:if>
	                           	<g:if test="${testInstance.IsSpecialTestRun()}">
			                     	<tr>
			                     		<td class="detailtitle">${message(code:'fc.specialresults.other')}:</td>
        	                            <td>${testInstance.specialTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})<g:if test="${!testInstance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
	                        </tbody>
                            <tfoot>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <g:set var="points_class" value="points"/>
                                    <g:if test="${!testInstance.taskPenalties}">
                                        <g:set var="points_class" value="zeropoints"/>
                                    </g:if>
        	                        <td class="${points_class}">${testInstance.taskPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tfoot>
                        </table>
                        <g:if test="${testInstance.IsPlanningTestRun()}">
                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <thead>
	                                <tr>
    	                                <th colspan="7" class="table-head">${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})<g:if test="${!testInstance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></th>
        	                        </tr>
                                </thead>
		                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
		                            <thead>
	                                    <tr>
	                                        <th>${message(code:'fc.title')}</th>
	                                        <th colspan="3">${message(code:'fc.trueheading')}</th>
	                                        <th colspan="3">${message(code:'fc.legtime')}</th>
	                                    </tr>
	                                    <tr>
	                                    	<th/>
	                                        <th>${message(code:'fc.test.results.plan')}</th>
	                                        <th>${message(code:'fc.test.results.given')}</th>
	                                        <th>${message(code:'fc.points')}</th>
	                                        <th>${message(code:'fc.test.results.plan')}</th>
	                                        <th>${message(code:'fc.test.results.given')}</th>
	                                        <th>${message(code:'fc.points')}</th>
	                                    </tr>
	                                </thead>
	                                <tbody>
	                                    <g:set var="penaltyTrueHeadingSummary" value="${new Integer(0)}" />
	                                    <g:set var="penaltyLegTimeSummary" value="${new Integer(0)}" />
	                                    <g:set var="legNo" value="${new Integer(0)}" />
	                                    <g:each var="testLegPlanningInstance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
	                                        <g:if test="${lastTestLegPlanningInstance}">
	                                            <g:set var="penaltyTrueHeadingSummary" value="${penaltyTrueHeadingSummary + lastTestLegPlanningInstance.penaltyTrueHeading}" />
	                                            <g:set var="penaltyLegTimeSummary" value="${penaltyLegTimeSummary + lastTestLegPlanningInstance.penaltyLegTime}" />
	                                            <tr>
	                                                <td>${message(code:CoordType.TP.code)}${legNo}</td>
	                                                <td>${FcMath.GradStr(lastTestLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
	                                                <td>${FcMath.GradStrComma(lastTestLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
	                                                <td>${lastTestLegPlanningInstance.penaltyTrueHeading}</td>
	                                                <td>${FcMath.TimeStr(lastTestLegPlanningInstance.planLegTime)}</td>
	                                                <td>${FcMath.TimeStr(lastTestLegPlanningInstance.resultLegTime)}</td>
	                                                <td>${lastTestLegPlanningInstance.penaltyLegTime}</td>
	                                            </tr>
	                                        </g:if>
	                                        <g:set var="lastTestLegPlanningInstance" value="${testLegPlanningInstance}" />
	                                        <g:set var="legNo" value="${legNo+1}" />
	                                    </g:each>
	                                    <g:if test="${lastTestLegPlanningInstance}">
	                                        <g:set var="penaltyTrueHeadingSummary" value="${penaltyTrueHeadingSummary + lastTestLegPlanningInstance.penaltyTrueHeading}" />
	                                        <g:set var="penaltyLegTimeSummary" value="${penaltyLegTimeSummary + lastTestLegPlanningInstance.penaltyLegTime}" />
	                                        <tr>
	                                            <td>${message(code:CoordType.FP.code)}</td>
	                                            <td>${FcMath.GradStr(lastTestLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
	                                            <td>${FcMath.GradStrComma(lastTestLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
	                                            <td>${lastTestLegPlanningInstance.penaltyTrueHeading}</td>
	                                            <td>${FcMath.TimeStr(lastTestLegPlanningInstance.planLegTime)}</td>
	                                            <td>${FcMath.TimeStr(lastTestLegPlanningInstance.resultLegTime)}</td>
	                                            <td>${lastTestLegPlanningInstance.penaltyLegTime}</td>
	                                        </tr>
	                                    </g:if>
	                                </tbody>
	                                <tfoot>
	                                    <tr>
	                                        <td/>
	                                        <td/>
	                                        <td/>
	                                        <td>${penaltyTrueHeadingSummary}</td>
	                                        <td/>
	                                        <td/>
	                                        <td>${penaltyLegTimeSummary}</td>
	                                    </tr>
	                                </tfoot>
			                    </g:if>
	                        </table>
	                        <table>
	                            <tbody>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.planningresults.legpenalties')}:</td>
	                                    <td>${testInstance.planningTestLegPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <g:if test="${testInstance.planningTestGivenTooLate}">
	                                	<tr>
	                                    	<td class="detailtitle">${message(code:'fc.planningtest.giventolate')}:</td>
	                                        <td>${testInstance.GetPlanningTestPlanTooLatePoints()} ${message(code:'fc.points')}</td>
		                                </tr>
                                    </g:if>
                                    <g:if test="${testInstance.planningTestExitRoomTooLate}">
		                                <tr>
		                                    <td class="detailtitle">${message(code:'fc.planningtest.exitroomtolate')}:</td>
	                                        <td>${testInstance.GetPlanningTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>
		                                </tr>
	                                </g:if>
	                                <g:if test="${testInstance.planningTestOtherPenalties > 0}">
	                                    <tr>
	                                        <td class="detailtitle">${message(code:'fc.planningtest.otherpenalties')}:</td>
	                                        <td>${testInstance.planningTestOtherPenalties} ${message(code:'fc.points')}</td>
	                                    </tr>
	                                </g:if>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
	                                    <g:set var="points_class" value="points"/>
	                                    <g:if test="${!testInstance.planningTestPenalties}">
	                                        <g:set var="points_class" value="zeropoints"/>
	                                    </g:if>
	                                    <td class="${points_class}">${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.IsFlightTestRun()}">
                            <table>
                            	<thead>
                                   	<tr>
                                    	<th colspan="7" class="table-head">${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})<g:if test="${!testInstance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></th>
                                   	</tr>
                                </thead>
		                        <g:if test="${CoordResult.countByTest(testInstance)}" >
	                                <thead>
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
                                        <g:set var="disabled_checkpoints" value="${testInstance.task.disabledCheckPoints},"/>
	                                    <g:set var="penaltyCoordSummary" value="${new Integer(0)}" />
	                                    <g:set var="penaltyProcedureTurnSummary" value="${new Integer(0)}" />
	                                    <g:set var="penaltyBadCourseSummary" value="${new Integer(0)}" />
	                                    <g:set var="penaltyAltitudeSummary" value="${new Integer(0)}" />
                                        <g:set var="check_secretpoints" value="${testInstance.IsFlightTestCheckSecretPoints()}"/>
	                                    <g:each var="coordResultInstance" in="${CoordResult.findAllByTest(testInstance,[sort:"id"])}">
	                                        <g:if test="${lastCoordResultInstance}">
                                                <g:if test="${(lastCoordResultInstance.type != CoordType.SECRET) || check_secretpoints}">
	                                               <g:set var="penaltyCoordSummary" value="${penaltyCoordSummary + lastCoordResultInstance.penaltyCoord}" />
	                                            </g:if>
	                                            <tr>
	                                                <td>${lastCoordResultInstance.titleCode()}</td>
	                                                <td>${FcMath.TimeStr(lastCoordResultInstance.planCpTime)}</td>
	                                                <g:if test="${lastCoordResultInstance.resultCpNotFound}">
                                                        <td>-</td>
	                                                </g:if>
	                                                <g:else>
	                                                    <td>${FcMath.TimeStr(lastCoordResultInstance.resultCpTime)}</td>
	                                                </g:else>
		                                            <g:if test="${disabled_checkpoints.contains(lastCoordResultInstance.title()+',')}">
		                                                <td>-</td>
		                                            </g:if>
                                                    <g:elseif test="${(lastCoordResultInstance.type != CoordType.SECRET) || check_secretpoints}">
                                                        <td>${lastCoordResultInstance.penaltyCoord}</td>
                                                    </g:elseif>
		                                            <g:else>
                                                        <td>-</td>
    	                                            </g:else>
	                                                <g:if test="${coordResultInstance.planProcedureTurn}">
                                                        <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
	                                                        <g:if test="${coordResultInstance.resultProcedureTurnEntered}">
			                                                    <g:if test="${disabled_checkpoints.contains(lastCoordResultInstance.title()+',')}">
			                                                        <td>-</td>
			                                                    </g:if>
	                                                            <g:elseif test="${coordResultInstance.resultProcedureTurnNotFlown}">
	                                                                <g:set var="penaltyProcedureTurnSummary" value="${penaltyProcedureTurnSummary + testInstance.GetFlightTestProcedureTurnNotFlownPoints()}" />
	                                                                <td>${testInstance.GetFlightTestProcedureTurnNotFlownPoints()}</td>
	                                                            </g:elseif>
	                                                            <g:else>
	                                                                <td>0</td>
	                                                            </g:else>
	                                                        </g:if>
	                                                        <g:else>
	                                                            <td/>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td>-</td>
	                                                    </g:else>
	                                                </g:if>
	                                                <g:else>
	                                                    <td/>
	                                                </g:else>
	                                                <g:if test="${testInstance.GetFlightTestBadCoursePoints() > 0}">
		                                                <g:if test="${lastCoordResultInstance.resultEntered}">
		                                                    <g:set var="penaltyBadCourseSummary" value="${penaltyBadCourseSummary + lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()}" />
		                                                    <g:if test="${lastCoordResultInstance.resultBadCourseNum > 0}">
		                                                    	<td>${lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()} (${lastCoordResultInstance.resultBadCourseNum})</td>
		                                                    </g:if>
		                                                    <g:else>
		                                                    	<td>${lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()}</td>
		                                                    </g:else>
		                                                </g:if>
		                                                <g:else>
		                                                    <td/>
		                                                </g:else>
                                                    </g:if>
                                                    <g:else>
                                                        <td>-</td>
                                                    </g:else>
	                                                <g:if test="${testInstance.GetFlightTestMinAltitudeMissedPoints() > 0}">
		                                                <g:if test="${lastCoordResultInstance.resultAltitude}">
		                                                    <g:if test="${lastCoordResultInstance.resultMinAltitudeMissed}">
		                                                        <g:set var="penaltyAltitudeSummary" value="${penaltyAltitudeSummary + testInstance.GetFlightTestMinAltitudeMissedPoints()}" />
		                                                        <td>${testInstance.GetFlightTestMinAltitudeMissedPoints()} (${lastCoordResultInstance.resultAltitude}${message(code:'fc.foot')})</td>
		                                                    </g:if>
		                                                    <g:else>
		                                                        <td>0</td>
		                                                    </g:else>
		                                                </g:if>
		                                                <g:else>
		                                                    <td>0</td>
		                                                </g:else>
		                                            </g:if>
		                                            <g:else>
		                                                <td>-</td>
		                                            </g:else>
	                                            </tr>
	                                        </g:if>
	                                        <g:set var="lastCoordResultInstance" value="${coordResultInstance}" />
	                                    </g:each>
	                                    <g:if test="${lastCoordResultInstance}">
                                            <g:if test="${(lastCoordResultInstance.type != CoordType.SECRET) || check_secretpoints}">
	                                            <g:set var="penaltyCoordSummary" value="${penaltyCoordSummary + lastCoordResultInstance.penaltyCoord}" />
	                                        </g:if>
	                                        <tr>
	                                            <td>${lastCoordResultInstance.titleCode()}</td>
	                                            <td>${FcMath.TimeStr(lastCoordResultInstance.planCpTime)}</td>
                                                <g:if test="${lastCoordResultInstance.resultCpNotFound}">
                                                    <td>-</td>
                                                </g:if>
                                                <g:else>
	                                                <td>${FcMath.TimeStr(lastCoordResultInstance.resultCpTime)}</td>
	                                            </g:else>
                                                <g:if test="${disabled_checkpoints.contains(lastCoordResultInstance.title()+',')}">
                                                    <td>-</td>
                                                </g:if>
                                                <g:elseif test="${(lastCoordResultInstance.type != CoordType.SECRET) || check_secretpoints}">
    	                                            <td>${lastCoordResultInstance.penaltyCoord}</td>
    	                                        </g:elseif>
                                                <g:else>
                                                    <td>-</td>
                                                </g:else>
	                                            <td/>
                                                <g:if test="${testInstance.GetFlightTestBadCoursePoints() > 0}">
		                                            <g:if test="${lastCoordResultInstance.resultEntered}">
		                                                <g:set var="penaltyBadCourseSummary" value="${penaltyBadCourseSummary + lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()}" />
		                                                <td>${lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()}</td>
		                                            </g:if>
		                                            <g:else>
		                                                <td/>
		                                            </g:else>
                                                </g:if>
                                                <g:else>
                                                    <td>-</td>
                                                </g:else>
	                                            <g:if test="${testInstance.GetFlightTestMinAltitudeMissedPoints() > 0}">
		                                            <g:if test="${lastCoordResultInstance.resultAltitude}">
		                                                <g:if test="${lastCoordResultInstance.resultMinAltitudeMissed}">
		                                                    <g:set var="penaltyAltitudeSummary" value="${penaltyAltitudeSummary + testInstance.GetFlightTestMinAltitudeMissedPoints()}" />
		                                                    <td>${testInstance.GetFlightTestMinAltitudeMissedPoints()}</td>
		                                                </g:if>
		                                                <g:else>
		                                                    <td>0</td>
		                                                </g:else>
		                                            </g:if>
		                                            <g:else>
		                                                <td>0</td>
		                                            </g:else>
                                                </g:if>
                                                <g:else>
                                                    <td>-</td>
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
		                        </g:if>
                            </table>
	                        <table>
	                            <tbody>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.flightresults.checkpointpenalties')}:</td>
	                                    <td>${testInstance.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                                <g:if test="${testInstance.flightTestTakeoffMissed}">
	                                	<tr>
	                                    	<td class="detailtitle">${message(code:'fc.flighttest.takeoffmissed')}:</td>
	                                        <td>${testInstance.GetFlightTestTakeoffMissedPoints()} ${message(code:'fc.points')}</td>
	                                	</tr>
	                                </g:if>
                                    <g:if test="${testInstance.flightTestLandingTooLate}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.landingtolate')}:</td>
                                            <td>${testInstance.GetFlightTestLandingToLatePoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
	                                <g:if test="${testInstance.flightTestBadCourseStartLanding}">
	                                	<tr>
	                                    	<td class="detailtitle">${message(code:'fc.flighttest.badcoursestartlanding')}:</td>
	                                        <td>${testInstance.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>
	                                	</tr>
	                                </g:if>
                                    <g:if test="${testInstance.flightTestGivenTooLate}">
		                                <tr>
		                                    <td class="detailtitle">${message(code:'fc.flighttest.giventolate')}:</td>
	                                        <td>${testInstance.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>
	                                	</tr>
	                                </g:if>
                                    <g:if test="${testInstance.flightTestSafetyAndRulesInfringement}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.safetyandrulesinfringement')}:</td>
                                            <td>${testInstance.GetFlightTestSafetyAndRulesInfringementPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${testInstance.flightTestInstructionsNotFollowed}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.instructionsnotfollowed')}:</td>
                                            <td>${testInstance.GetFlightTestInstructionsNotFollowedPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${testInstance.flightTestFalseEnvelopeOpened}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.falseenvelopeopened')}:</td>
                                            <td>${testInstance.GetFlightTestFalseEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${testInstance.flightTestSafetyEnvelopeOpened}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.safetyenvelopeopened')}:</td>
                                            <td>${testInstance.GetFlightTestSafetyEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${testInstance.flightTestFrequencyNotMonitored}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.frequencynotmonitored')}:</td>
                                            <td>${testInstance.GetFlightTestFrequencyNotMonitoredPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
	                                <g:if test="${testInstance.flightTestOtherPenalties > 0}">
	                                    <tr>
	                                        <td class="detailtitle">${message(code:'fc.flighttest.otherpenalties')}:</td>
	                                        <td>${testInstance.flightTestOtherPenalties} ${message(code:'fc.points')}</td>
	                                    </tr>
	                                </g:if>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
                                        <g:set var="points_class" value="points"/>
                                        <g:if test="${!testInstance.flightTestPenalties}">
                                            <g:set var="points_class" value="zeropoints"/>
                                        </g:if>
	                                    <td class="${points_class}">${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.IsObservationTestRun()}">
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th colspan="4" class="table-head">${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})<g:if test="${!testInstance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></th>
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
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
                                        <g:set var="points_class" value="points"/>
                                        <g:if test="${!testInstance.observationTestPenalties}">
                                            <g:set var="points_class" value="zeropoints"/>
                                        </g:if>
	                                    <td class="${points_class}">${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.IsLandingTestRun()}">
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th colspan="4" class="table-head">${message(code:'fc.landingresults')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></th>
	                                </tr>
	                            </thead>
	                            <tbody>
			                       	<g:if test="${testInstance.IsLandingTest1Run()}">
			                       	     <g:landingTest1Complete t="${testInstance}" crewResults="${true}"/>
			                       	</g:if>
			                       	<g:if test="${testInstance.IsLandingTest2Run()}">
                                         <g:landingTest2Complete t="${testInstance}" crewResults="${true}"/>
			                       	</g:if>
			                       	<g:if test="${testInstance.IsLandingTest3Run()}">
                                         <g:landingTest3Complete t="${testInstance}" crewResults="${true}"/>
			                       	</g:if>
			                       	<g:if test="${testInstance.IsLandingTest4Run()}">
                                         <g:landingTest4Complete t="${testInstance}" crewResults="${true}"/>
			                       	</g:if>
                                    <g:if test="${testInstance.IsLandingTestAnyRun()}">
                                        <g:if test="${testInstance.landingTestOtherPenalties > 0}">
                                            <tr>
                                                <th class="detailtitle">${message(code:'fc.landingtest.otherpenalties')}:</th>
                                                <td>${testInstance.landingTestOtherPenalties} ${message(code:'fc.points')}</td>
                                            </tr>
                                        </g:if>
                                    </g:if>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
                                        <g:set var="points_class" value="points"/>
                                        <g:if test="${!testInstance.landingTestPenalties}">
                                            <g:set var="points_class" value="zeropoints"/>
                                        </g:if>
	                                    <td class="${points_class}">${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
	                    <g:if test="${testInstance.IsSpecialTestRun()}">
	                        <table>
	                            <thead>
	                                <tr>
	                                    <th colspan="4" class="table-head">${testInstance.GetSpecialTestTitle()} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})<g:if test="${!testInstance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></th>
	                                </tr>
	                            </thead>
	                            <tfoot>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties')}:</td>
                                        <g:set var="points_class" value="points"/>
                                        <g:if test="${!testInstance.specialTestPenalties}">
                                            <g:set var="points_class" value="zeropoints"/>
                                        </g:if>
	                                    <td class="${points_class}">${testInstance.specialTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
                        <g:if test="${params.next}">
                            <g:actionSubmit action="crewresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="1"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="2"/>
                        </g:else>
                        <g:actionSubmit action="crewresultsprintquestion" value="${message(code:'fc.print')}" tabIndex="3"/>
                        <g:if test="${params.next}">
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="4"/>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>