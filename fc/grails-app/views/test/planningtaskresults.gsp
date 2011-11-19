<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.planningtaskresults')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.planningtaskresults')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post">
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.from')}:</td>
                                    <td><g:contestday var="${testInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${testInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
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
                                            <th colspan="7" class="table-head">${message(code:'fc.test.planningtaskresults.legresultlist')}</th>
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
                                            <g:if test="${!testLegPlanningInstance.test.contestdaytask.planningTestDistanceMeasure}">
                                                <g:set var="testDistance" value="${FcMath.DistanceStr(testLegPlanningInstance.resultTestDistance)+message(code:'fc.mile')}" />
                                            </g:if>
                                            <g:if test="${!testLegPlanningInstance.test.contestdaytask.planningTestDirectionMeasure}">
                                                <g:set var="testDirection" value="${FcMath.GradStr(testLegPlanningInstance.resultTrueTrack)+message(code:'fc.grad')}" />
                                            </g:if>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td><g:testlegplanning2 var="${testLegPlanningInstance}" name="${legNo}" link="${createLink(controller:'testLegPlanning',action:'edit')}"/></td>
                                                <td>${message(code:'fc.test.planningtaskresults.plan')}</td>
                                                <td>${FcMath.DistanceStr(testLegPlanningInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(testLegPlanningInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(testLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr(testLegPlanningInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                                <td>${testLegPlanningInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                            </tr>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td>${message(code:'fc.test.planningtaskresults.given')}</td>
                                                <td/>
                                                <td/>
                                                <g:if test="${testLegPlanningInstance.resultEntered}">
                                                    <td>${FcMath.GradStr(testLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
                                                    <td/>
                                                    <td>${testLegPlanningInstance.resultLegTimeStr()}${message(code:'fc.time.h')}</td>
                                                </g:if>
                                                <g:else>
                                                    <g:if test="${testLegPlanningInstance.test.contestdaytask.planningTestDirectionMeasure}">
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
                                                <td>${message(code:'fc.test.planningtaskresults.penalty')}</td>
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
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtaskresults.legpenalties')}:</td>
                                    <td>${testInstance.planningTestLegPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtaskresults.giventolate')}:</td>
                                    <g:viewbool value="${testInstance.planningTestTooLate}" tag="td" trueclass="points" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtaskresults.exitroomtolate')}:</td>
                                    <g:viewbool value="${testInstance.planningTestExitRoomTooLate}" tag="td" trueclass="points" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtaskresults.penalties')}:</td>
                                    <td class="points">${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="2">
                                        <g:if test="${!testInstance.planningTestComplete}">
                                            <g:if test="${testInstance.planningTestLegComplete}">
                                                <g:actionSubmit action="planningtaskresultscomplete" value="${message(code:'fc.planningtaskresults.complete')}" />
                                            </g:if>
                                            <g:if test="${!testInstance.planningTestTooLate}">
                                                <g:actionSubmit action="planningtaskresultsgiventolateon" value="${message(code:'fc.planningtaskresults.giventolate.on')}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="planningtaskresultsgiventolateoff" value="${message(code:'fc.planningtaskresults.giventolate.off')}" />
                                            </g:else>
                                            <g:if test="${!testInstance.planningTestExitRoomTooLate}">
                                                <g:actionSubmit action="planningtaskresultsexitroomtolateon" value="${message(code:'fc.planningtaskresults.exitroomtolate.on')}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="planningtaskresultsexitroomtolateoff" value="${message(code:'fc.planningtaskresults.exitroomtolate.off')}" />
                                            </g:else>
                                        </g:if>
                                        <g:else>
                                            <g:actionSubmit action="planningtaskresultsreopen" value="${message(code:'fc.planningtaskresults.reopen')}" />
                                        </g:else>
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