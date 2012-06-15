<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningresults')} ${testInstance.GetStartNum()}</h2>
                <g:if test="${!testInstance.planningTestComplete}">
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                </g:if>
                <g:else>
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})</h3>
                </g:else>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if>
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
                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
                            <br/>
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
                        </g:if>
                        <br/>
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
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>