<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${testInstance.GetStartNum()}: ${testInstance.crew.name} (${testInstance.GetViewPos()}/" counter(page) ")"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewresults')} ${testInstance.GetStartNum()}</h2>
                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetCrewResultsVersion()})<g:if test="${testInstance.AreResultsProvisional(testInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]</g:if></h3>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if> <g:else>
                                         <td/>
                                    </g:else>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if> <g:else>
                                         <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.crew.aircraft}">
                                            ${testInstance.crew.aircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.crew.aircraft}">
		                                    ${testInstance.crew.aircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        
	                    <!-- summary -->
	                    <br/>
                        <table>
                        	<g:set var="taskPenalties" value="${new Integer(0)}" />
                            <tbody>
	                           	<g:if test="${testInstance.IsPlanningTestRun() && testInstance.printPlanningResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.planningTestPenalties}" />
    	                        	<tr>
        	                            <td>${message(code:'fc.planningresults.planning')}: ${testInstance.planningTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})<g:if test="${!testInstance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                     	</tr>
	                            </g:if>
   	                        	<g:if test="${testInstance.IsFlightTestRun() && testInstance.printFlightResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.flightTestPenalties}" />
			                    	<tr>
			                     		<td>${message(code:'fc.flightresults.flight')}: ${testInstance.flightTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})<g:if test="${!testInstance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsObservationTestRun() && testInstance.printObservationResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.observationTestPenalties}" />
			                    	<tr>
			                     		<td>${message(code:'fc.observationresults.observations')}: ${testInstance.observationTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})<g:if test="${!testInstance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsLandingTestRun() && testInstance.printLandingResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.landingTestPenalties}" />
			                    	<tr>
			                     		<td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
			                    </g:if>
	                           	<g:if test="${testInstance.IsSpecialTestRun() && testInstance.printSpecialResults}">
		                        	<g:set var="taskPenalties" value="${taskPenalties + testInstance.specialTestPenalties}" />
			                     	<tr>
			                     		<td>${message(code:'fc.specialresults.other')}: ${testInstance.specialTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})<g:if test="${!testInstance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                                <tr>
                                	<td> </td>
                                </tr>
	                        </tbody>
                            <tfoot>
                                <tr>
                                    <td>${message(code:'fc.penalties.total')}: ${taskPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tfoot>
                        </table>
	                    <br/>
						                        
	                    <!-- planning test --> 
                        <g:if test="${testInstance.IsPlanningTestRun() && testInstance.printPlanningResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.planningTestComplete}">
					                <h3>${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.planningresults')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})</h3>
				                </g:else>
		                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
		                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
		                                <thead>
		                                    <tr>
		                                        <th class="table-head">${message(code:'fc.title')}</th>
		                                        <th colspan="3" class="table-head">${message(code:'fc.trueheading')}</th>
		                                        <th colspan="3" class="table-head">${message(code:'fc.legtime')}</th>
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
		                                                <td>${FcMath.GradStr(lastTestLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
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
		                                            <td>${FcMath.GradStr(lastTestLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
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
		                            </table>
			                        <br/>
			                    </g:if>
		                        <table>
		                            <tbody>
		                                <tr>
		                                    <td>${message(code:'fc.planningresults.legpenalties')}: ${testInstance.planningTestLegPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                                <g:if test="${testInstance.planningTestGivenTooLate}">
		                                	<tr>
		                                    	<td>${message(code:'fc.planningtest.giventolate')}: ${testInstance.GetPlanningTestPlanTooLatePoints()} ${message(code:'fc.points')}</td>
		                                	</tr>
		                                </g:if>
		                                <g:if test="${testInstance.planningTestExitRoomTooLate}">
		                                	<tr>
		                                 		<td>${message(code:'fc.planningtest.exitroomtolate')}: ${testInstance.GetPlanningTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>
		                                	</tr>
		                                </g:if>
		                                <g:if test="${testInstance.planningTestOtherPenalties > 0}">
		                                    <tr>
		                                        <td>${message(code:'fc.planningtest.otherpenalties')}: ${testInstance.planningTestOtherPenalties} ${message(code:'fc.points')}</td>
		                                    </tr>
		                                </g:if>
		                                <tr>
		                                	<td> </td>
		                                </tr>
		                            </tbody>
		                            <tfoot>
		                                <tr>
		                                    <td>${message(code:'fc.penalties')}: ${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                            </tfoot>
		                        </table>
	                        </div>
	                    </g:if>
	                    
	                    <!-- flight test --> 
	                    <g:if test="${testInstance.IsFlightTestRun() && testInstance.printFlightResults}" >
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.flightTestComplete}">
				                	<h3>${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.flightresults')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})</h3>
				                </g:else>
		                        <g:if test="${CoordResult.countByTest(testInstance)}" >
		                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
		                                <thead>
		                                    <tr>
		                                        <th class="table-head">${message(code:'fc.title')}</th>
		                                        <th colspan="3" class="table-head">${message(code:'fc.cptime')}</th>
                                                <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
		                                            <th class="table-head">${message(code:'fc.procedureturn')}</th>
		                                        </g:if>
                                                <g:if test="${testInstance.GetFlightTestBadCoursePoints() > 0}">
    		                                        <th class="table-head">${message(code:'fc.badcoursenum')}</th>
    		                                    </g:if>
                                                <g:if test="${testInstance.GetFlightTestMinAltitudeMissedPoints() > 0}">
		                                            <th class="table-head">${message(code:'fc.altitude')}</th>
		                                        </g:if>
		                                    </tr>
		                                    <tr>
		                                        <th/>
		                                        <th>${message(code:'fc.test.results.plan')}</th>
		                                        <th>${message(code:'fc.test.results.measured')}</th>
		                                        <th>${message(code:'fc.points')}</th>
                                                <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
    		                                        <th/>
    		                                    </g:if>
                                                <g:if test="${testInstance.GetFlightTestBadCoursePoints() > 0}">
    		                                        <th/>
    		                                    </g:if>
                                                <g:if test="${testInstance.GetFlightTestMinAltitudeMissedPoints() > 0}">
		                                            <th/>
		                                        </g:if>
		                                    </tr>
		                                </thead>
		                                <tbody>
                                            <g:set var="disabled_checkpoints" value="${testInstance.task.disabledCheckPoints},"/>
		                                    <g:set var="penaltyCoordSummary" value="${new Integer(0)}" />
		                                    <g:set var="penaltyProcedureTurnSummary" value="${new Integer(0)}" />
		                                    <g:set var="penaltyBadCourseSummary" value="${new Integer(0)}" />
		                                    <g:set var="penaltyAltitudeSummary" value="${new Integer(0)}" />
		                                    <g:each var="coordResultInstance" in="${CoordResult.findAllByTest(testInstance,[sort:"id"])}">
		                                        <g:if test="${lastCoordResultInstance}">
		                                            <g:set var="penaltyCoordSummary" value="${penaltyCoordSummary + lastCoordResultInstance.penaltyCoord}" />
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
	                                                    <g:else>
    		                                                <td>${lastCoordResultInstance.penaltyCoord}</td>
                                                        </g:else>
                                                        <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
			                                                <g:if test="${coordResultInstance.planProcedureTurn}">
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
			                                                    <td/>
			                                                </g:else>
			                                            </g:if>
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
		                                            </tr>
		                                        </g:if>
		                                        <g:set var="lastCoordResultInstance" value="${coordResultInstance}" />
		                                    </g:each>
		                                    <g:if test="${lastCoordResultInstance}">
		                                        <g:set var="penaltyCoordSummary" value="${penaltyCoordSummary + lastCoordResultInstance.penaltyCoord}" />
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
                                                    <g:else>
    		                                            <td>${lastCoordResultInstance.penaltyCoord}</td>
                                                    </g:else>
                                                    <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
		                                                <td/>
		                                            </g:if>
                                                    <g:if test="${testInstance.GetFlightTestBadCoursePoints() > 0}">
			                                            <g:if test="${lastCoordResultInstance.resultEntered}">
			                                                <g:set var="penaltyBadCourseSummary" value="${penaltyBadCourseSummary + lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()}" />
			                                                <td>${lastCoordResultInstance.resultBadCourseNum*testInstance.GetFlightTestBadCoursePoints()}</td>
			                                            </g:if>
			                                            <g:else>
			                                                <td/>
			                                            </g:else>
			                                        </g:if>
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
		                                        </tr>
		                                    </g:if>
		                                </tbody>
		                                <tfoot>
		                                    <tr>
		                                        <td/>
		                                        <td/>
		                                        <td/>
		                                        <td>${penaltyCoordSummary}</td>
                                                <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
		                                            <td>${penaltyProcedureTurnSummary}</td>
		                                        </g:if>
                                                <g:if test="${testInstance.GetFlightTestBadCoursePoints() > 0}">
    		                                        <td>${penaltyBadCourseSummary}</td>
    		                                    </g:if>
                                                <g:if test="${testInstance.GetFlightTestMinAltitudeMissedPoints() > 0}">
    		                                        <td>${penaltyAltitudeSummary}</td>
    		                                    </g:if>
		                                    </tr>
		                                </tfoot>
		                            </table>
			                        <br/>
		                        </g:if>
		                        <table>
		                            <tbody>
		                                <tr>
		                                    <td>${message(code:'fc.flightresults.checkpointpenalties')}: ${testInstance.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                                <g:if test="${testInstance.flightTestTakeoffMissed}">
		                                	<tr>
		                                    	<td>${message(code:'fc.flighttest.takeoffmissed')}: ${testInstance.GetFlightTestTakeoffMissedPoints()} ${message(code:'fc.points')}</td>
		                                	</tr>
		                                </g:if>
		                                <g:if test="${testInstance.flightTestBadCourseStartLanding}">
		                                	<tr>
		                                 		<td>${message(code:'fc.flighttest.badcoursestartlanding')}: ${testInstance.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>
		                                	</tr>
		                                </g:if>
		                                <g:if test="${testInstance.flightTestLandingTooLate}">
		                                	<tr>
		                                    	<td>${message(code:'fc.flighttest.landingtolate')}: ${testInstance.GetFlightTestLandingToLatePoints()} ${message(code:'fc.points')}</td>
		                                	</tr>
		                                </g:if>
		                                <g:if test="${testInstance.flightTestGivenTooLate}">
		                                	<tr>
		                                    	<td>${message(code:'fc.flighttest.giventolate')}: ${testInstance.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>
		                                	</tr>
		                                </g:if>
		                                <g:if test="${testInstance.flightTestOtherPenalties > 0}">
		                                    <tr>
		                                        <td>${message(code:'fc.flighttest.otherpenalties')}: ${testInstance.flightTestOtherPenalties} ${message(code:'fc.points')}</td>
		                                    </tr>
		                                </g:if>
		                                <tr>
		                                	<td> </td>
		                                </tr>
		                            </tbody>
		                            <tfoot>
		                                <tr>
		                                	<td>${message(code:'fc.penalties')}: ${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                            </tfoot>
	                        	</table>
	                        </div>
			            </g:if>

	                    <!-- observation test --> 
	                    <g:if test="${testInstance.IsObservationTestRun() && testInstance.printObservationResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.observationTestComplete}">
					                <h3>${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.observationresults')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})</h3>
				                </g:else>
		                        <table>
		                            <tbody>
		                                <tr>
		                                    <td>${message(code:'fc.observationresults.turnpointphotopenalties')}: ${testInstance.observationTestTurnPointPhotoPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                                <tr>
		                                    <td>${message(code:'fc.observationresults.routephotopenalties')}: ${testInstance.observationTestRoutePhotoPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                                <tr>
		                                    <td>${message(code:'fc.observationresults.groundtargetpenalties')}: ${testInstance.observationTestGroundTargetPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                                <tr>
		                                	<td> </td>
		                                </tr>
		                            </tbody>
		                            <tfoot>
		                                <tr>
		                                    <td>${message(code:'fc.penalties')}: ${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                            </tfoot>
		                        </table>
		                	</div>
	                    </g:if>
	                    
	                    <!-- landing test --> 
	                    <g:if test="${testInstance.IsLandingTestRun() && testInstance.printLandingResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.landingTestComplete}">
					                <h3>${message(code:'fc.landingresults')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.landingresults')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</h3>
				                </g:else>
		                       	<g:if test="${testInstance.IsLandingTest1Run()}">
			                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
			                        	<thead>
			                                <tr>
					                       		<td>${message(code:'fc.landingtest.landing1')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></td>
			                                </tr>
			                        	</thead>
			                            <tbody>
			                                <tr>
			                                	<td>
				                                	<g:if test="${testInstance.landingTest1Landing == 2}">
				                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest1NoLandingPoints()} ${message(code:'fc.points')}<br/>
				                                	</g:if>
				                                 	<g:elseif test="${testInstance.landingTest1Landing == 3}">
				                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest1OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
				                                 	</g:elseif>
				                                 	<g:elseif test="${!testInstance.landingTest1Measure}">
				                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:elseif>
				                                	<g:else>
				                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest1Measure} (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:else>
				                                 	<g:if test="${testInstance.landingTest1RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest1RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest1PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest1PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest1GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest1GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest1GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest1GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest1AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest1AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
			                                    </td>
			                                </tr>
			                            </tbody>
			                            <tfoot>
			                                <tr>
			                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest1Penalties} ${message(code:'fc.points')}</td>
			                                </tr>
			                            </tfoot>
			                        </table>
			                        <br/>
		                       	</g:if>
		                       	<g:if test="${testInstance.IsLandingTest2Run()}">
			                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
			                        	<thead>
			                                <tr>
					                       		<td>${message(code:'fc.landingtest.landing2')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></td>
			                                </tr>
			                        	</thead>
			                            <tbody>
			                                <tr>
			                                	<td>
				                                	<g:if test="${testInstance.landingTest2Landing == 2}">
				                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest2NoLandingPoints()} ${message(code:'fc.points')}<br/>
				                                	</g:if>
				                                 	<g:elseif test="${testInstance.landingTest2Landing == 3}">
				                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest2OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
				                                 	</g:elseif>
				                                 	<g:elseif test="${!testInstance.landingTest2Measure}">
				                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:elseif>
				                                	<g:else>
				                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest2Measure} (${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:else>
				                                 	<g:if test="${testInstance.landingTest2RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest2RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest2PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest2PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest2GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest2GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest2GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest2GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest2AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest2AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest2PowerInAir}">${message(code:'fc.landingtest.powerinair')}: ${testInstance.GetLandingTest2PowerInAirPoints()} ${message(code:'fc.points')}<br/></g:if>
			                                    </td>
			                                </tr>
			                            </tbody>
			                            <tfoot>
			                                <tr>
			                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest2Penalties} ${message(code:'fc.points')}</td>
			                                </tr>
			                            </tfoot>
			                        </table>
			                        <br/>
		                       	</g:if>
		                       	<g:if test="${testInstance.IsLandingTest3Run()}">
			                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
			                        	<thead>
			                                <tr>
					                       		<td>${message(code:'fc.landingtest.landing3')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></td>
			                                </tr>
			                        	</thead>
			                            <tbody>
			                                <tr>
			                                	<td>
				                                	<g:if test="${testInstance.landingTest3Landing == 2}">
				                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest3NoLandingPoints()} ${message(code:'fc.points')}<br/>
				                                	</g:if>
				                                 	<g:elseif test="${testInstance.landingTest3Landing == 3}">
				                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest3OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
				                                 	</g:elseif>
				                                 	<g:elseif test="${!testInstance.landingTest3Measure}">
				                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:elseif>
				                                	<g:else>
				                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest3Measure} (${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:else>
				                                 	<g:if test="${testInstance.landingTest3RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest3RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest3PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest3PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest3GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest3GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest3GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest3GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest3AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest3AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest3PowerInAir}">${message(code:'fc.landingtest.powerinair')}: ${testInstance.GetLandingTest3PowerInAirPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest3FlapsInAir}">${message(code:'fc.landingtest.flapsinair')}: ${testInstance.GetLandingTest3FlapsInAirPoints()} ${message(code:'fc.points')}<br/></g:if>
			                                    </td>
			                                </tr>
			                            </tbody>
			                            <tfoot>
			                                <tr>
			                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest3Penalties} ${message(code:'fc.points')}</td>
			                                </tr>
			                            </tfoot>
			                        </table>
			                        <br/>
		                       	</g:if>
		                       	<g:if test="${testInstance.IsLandingTest4Run()}">
			                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
			                        	<thead>
			                                <tr>
					                       		<td>${message(code:'fc.landingtest.landing4')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></td>
			                                </tr>
			                        	</thead>
			                            <tbody>
			                                <tr>
			                                	<td>
				                                	<g:if test="${testInstance.landingTest4Landing == 2}">
				                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest4NoLandingPoints()} ${message(code:'fc.points')}<br/>
				                                	</g:if>
				                                 	<g:elseif test="${testInstance.landingTest4Landing == 3}">
				                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest4OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
				                                 	</g:elseif>
				                                 	<g:elseif test="${!testInstance.landingTest4Measure}">
				                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:elseif>
				                                	<g:else>
				                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest4Measure} (${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>
				                                 	</g:else>
				                                 	<g:if test="${testInstance.landingTest4RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest4RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest4PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest4PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest4GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest4GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest4GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest4GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest4AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest4AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
				                                 	<g:if test="${testInstance.landingTest4TouchingObstacle}">${message(code:'fc.landingtest.touchingobstacle')}: ${testInstance.GetLandingTest4TouchingObstaclePoints()} ${message(code:'fc.points')}<br/></g:if>
			                                    </td>
			                                </tr>
			                            </tbody>
			                            <tfoot>
			                                <tr>
			                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest4Penalties} ${message(code:'fc.points')}</td>
			                                </tr>
			                            </tfoot>
			                        </table>
			                        <br/>
		                       	</g:if>
		                        <table>
		                            <tfoot>
		                                <g:if test="${testInstance.IsLandingTestAnyRun()}">
		                                    <g:if test="${testInstance.landingTestOtherPenalties > 0}">
		                                        <tr>
		                                            <td>${message(code:'fc.landingtest.otherpenalties')}: ${testInstance.landingTestOtherPenalties} ${message(code:'fc.points')}</td>
		                                        </tr>
		                                    </g:if>
		                                </g:if>
		                                <tr>
		                                    <td>${message(code:'fc.penalties')}: ${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                            </tfoot>
		                        </table>
		                	</div>
	                    </g:if>
	                    
	                    <!-- special test --> 
	                    <g:if test="${testInstance.IsSpecialTestRun() && testInstance.printSpecialResults}">
                        	<div style="page-break-inside:avoid">
				                <g:if test="${!testInstance.specialTestComplete}">
					                <h3>${message(code:'fc.specialresults')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()}) [${message(code:'fc.provisional')}]</h3>
				                </g:if>
				                <g:else>
					                <h3>${message(code:'fc.specialresults')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})</h3>
				                </g:else>
		                        <table>
		                            <tbody>
		                                <tr>
		                                	<td> </td>
		                                </tr>
		                            </tbody>
		                            <tfoot>
		                                <tr>
		                                    <td>${message(code:'fc.penalties')}: ${testInstance.specialTestPenalties} ${message(code:'fc.points')}</td>
		                                </tr>
		                            </tfoot>
		                        </table>
							</div>
	                    </g:if>
	                    
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>