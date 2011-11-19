<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningresults')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningresults')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post">
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
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:route var="${testInstance.planningtesttask.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:windtext var="${testInstance.planningtesttask.wind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th colspan="7" class="table-head">${message(code:'fc.planningresults.legresultlist')}</th>
                                        </tr>
                                        <tr>
                                            <th>${message(code:'fc.number')}</th>
                                            <th/>
                                            <th>${message(code:'fc.distance')}</th>
                                            <th>${message(code:'fc.truetrack')}</th>
                                            <th>${message(code:'fc.trueheading')}</th>
                                            <th>${message(code:'fc.groundspeed')}</th>
                                            <th>${message(code:'fc.legtime')}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:set var="legNo" value="${new Integer(0)}" />
                                        <g:each var="testLegPlanningInstance" in="${TestLegPlanning.findAllByTest(testInstance)}">
                                            <g:set var="legNo" value="${legNo+1}" />
                                            <g:if test="${!testLegPlanningInstance.test.task.planningTestDistanceMeasure}">
                                                <g:set var="testDistance" value="${FcMath.DistanceStr(testLegPlanningInstance.resultTestDistance)+message(code:'fc.mile')}" />
                                            </g:if>
                                            <g:if test="${!testLegPlanningInstance.test.task.planningTestDirectionMeasure}">
                                                <g:set var="testDirection" value="${FcMath.GradStr(testLegPlanningInstance.resultTrueTrack)+message(code:'fc.grad')}" />
                                            </g:if>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td><g:testlegplanning2 var="${testLegPlanningInstance}" name="${legNo}" link="${createLink(controller:'testLegPlanning',action:'edit')}"/></td>
                                                <td>${message(code:'fc.test.results.plan')}</td>
                                                <td>${FcMath.DistanceStr(testLegPlanningInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(testLegPlanningInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(testLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr(testLegPlanningInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                                <td>${testLegPlanningInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                            </tr>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td>${message(code:'fc.test.results.given')}</td>
                                                <td/>
                                                <td/>
                                                <g:if test="${testLegPlanningInstance.resultEntered}">
                                                    <td>${FcMath.GradStr(testLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
                                                    <td/>
                                                    <td>${testLegPlanningInstance.resultLegTimeStr()}${message(code:'fc.time.h')}</td>
                                                </g:if>
                                                <g:else>
                                                    <g:if test="${testLegPlanningInstance.test.task.planningTestDirectionMeasure}">
                                                        <td>${message(code:'fc.unknown')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td/>
                                                    </g:else>
                                                    <td/>
                                                    <td>${message(code:'fc.unknown')}</td>
                                                </g:else>
                                            </tr>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td>${message(code:'fc.test.results.penalty')}</td>
                                                <g:if test="${testLegPlanningInstance.resultEntered}">
                                                    <td/>
                                                    <td/>
                                                    <td class="points">${testLegPlanningInstance.penaltyTrueHeading} ${message(code:'fc.points')}</td>
                                                    <td/>
                                                    <td class="points">${testLegPlanningInstance.penaltyLegTime} ${message(code:'fc.points')}</td>
                                                </g:if>
                                                <g:else>
                                                    <td/>
                                                    <td/>
                                                    <td>${message(code:'fc.unknown')}</td>
                                                    <td/>
                                                    <td>${message(code:'fc.unknown')}</td>
                                                </g:else>
                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </div>
                        </g:if>
                        <p>
	                        <div>
        	                	<g:checkBox name="planningTestGivenTooLate" value="${testInstance.planningTestGivenTooLate}" disabled="${testInstance.planningTestComplete}" />
								<label>${message(code:'fc.planningtest.giventolate')}</label>
    	                    </div>
	                        <div>
        	                	<g:checkBox name="planningTestExitRoomTooLate" value="${testInstance.planningTestExitRoomTooLate}" disabled="${testInstance.planningTestComplete}" />
								<label>${message(code:'fc.planningtest.exitroomtolate')}</label>
    	                    </div>
                        </p>
                        <table>
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
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <td class="points">${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="2">
                                        <g:if test="${!testInstance.planningTestComplete}">
                                            <g:if test="${testInstance.planningTestLegComplete}">
                                                <g:actionSubmit action="planningtaskresultscomplete" value="${message(code:'fc.planningresults.complete')}" />
					                        	<g:actionSubmit action="planningtaskresultssave" value="${message(code:'fc.save')}" />
                                            </g:if>
                                        </g:if>
                                        <g:else>
                                            <g:actionSubmit action="planningtaskresultsreopen" value="${message(code:'fc.planningresults.reopen')}" />
                                        </g:else>
				                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>