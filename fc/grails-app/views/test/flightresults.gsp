<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.flightresults')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.flightresults')} ${testInstance.viewpos+1} - ${testInstance?.task.name()}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post">
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/><g:if test="${testInstance.crew.mark}"> (${message(code:'fc.aflos')}: ${testInstance.crew.mark})</g:if></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.flighttestwind?.flighttest}">
                                        <td><g:route var="${testInstance.flighttestwind.flighttest.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:windtext var="${testInstance.flighttestwind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${CoordResult.countByTest(testInstance)}" >
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th colspan="9" class="table-head">${message(code:'fc.test.flightresults.coordresultlist')}</th>
                                        </tr>
                                        <tr>
                                            <th>${message(code:'fc.number')}</th>
                                            <th>${message(code:'fc.title')}</th>
                                            <th>${message(code:'fc.aflos.checkpoint')}</th>
                                            <th/>
                                            <th>${message(code:'fc.latitude')}</th>
                                            <th>${message(code:'fc.longitude')}</th>
                                            <th>${message(code:'fc.altitude')}</th>
                                            <th>${message(code:'fc.cptime')}</th>
                                            <th>${message(code:'fc.badcoursenum')}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:set var="legNo" value="${new Integer(0)}" />
                                        <g:each var="coordResultInstance" in="${CoordResult.findAllByTest(testInstance)}">
                                            <g:if test="${coordResultInstance.planProcedureTurn}">
                                                <g:set var="legNo" value="${legNo+1}" />
                                                <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                    <td><g:coordresult var="${coordResultInstance}" name="${legNo}" link="${createLink(controller:'coordResult',action:'editprocedureturn')}"/></td>
                                                    <td>${message(code:'fc.procedureturn')}</td>
                                                    <td/>
                                                    <td>${message(code:'fc.test.results.measured')}</td>
                                                    <td/>
                                                    <td/>
                                                    <td/>
                                                    <g:if test="${coordResultInstance.resultProcedureTurnEntered}">
                                                        <g:if test="${coordResultInstance.resultProcedureTurnNotFlown}">
                                                            <td class="errors">${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>
                                                        </g:if>
                                                        <g:else>
                                                            <td>${message(code:'fc.flighttest.procedureturnflown.short')}</td>
                                                        </g:else>
                                                    </g:if>
                                                    <g:else>
                                                        <td>${message(code:'fc.unknown')}</td>
                                                    </g:else>
                                                    <td/>
                                                </tr>
                                                <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                    <td/>
                                                    <td/>
                                                    <td/>
                                                    <td>${message(code:'fc.test.results.penalty')}</td>
                                                    <td/>
                                                    <td/>
                                                    <td/>
                                                    <g:if test="${coordResultInstance.resultProcedureTurnEntered}">
                                                        <g:if test="${coordResultInstance.resultProcedureTurnNotFlown}">
                                                            <td class="points">${coordResultInstance.test.task.contest.flightTestProcedureTurnNotFlownPoints} ${message(code:'fc.points')}</td>
                                                        </g:if>
                                                        <g:else>
                                                            <td class="points">0 ${message(code:'fc.points')}</td>
                                                        </g:else>
                                                    </g:if>
                                                    <g:else>
                                                        <td>${message(code:'fc.unknown')}</td>
                                                    </g:else>
                                                    <td/>
                                                </tr>
                                            </g:if>
                                            <g:set var="legNo" value="${legNo+1}" />
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td><g:coordresult var="${coordResultInstance}" name="${legNo}" link="${createLink(controller:'coordResult',action:'edit')}"/></td>
                                                <td>${coordResultInstance.title()}</td>
                                                <td>${coordResultInstance.mark}</td>
                                                <td>${message(code:'fc.test.results.plan')}</td>
                                                <td>${coordResultInstance.latName()}</td>
                                                <td>${coordResultInstance.lonName()}</td>
                                                <td>${coordResultInstance.altitude}${message(code:'fc.foot')}</td>
                                                <td>${coordResultInstance.planCpTime.format('HH:mm:ss')}</td>
                                                <g:if test="${coordResultInstance.type != CoordType.SP}">
                                                    <td>0</td>
                                                </g:if>
                                                <g:else>
                                                    <td/>
                                                </g:else>
                                            </tr>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td/>
                                                <td/>
                                                <td>${message(code:'fc.test.results.measured')}</td>
                                                <g:if test="${coordResultInstance.resultEntered}">
                                                    <td>${coordResultInstance.resultLatitude}</td>
                                                    <td>${coordResultInstance.resultLongitude}</td>
                                                    <g:if test="${!coordResultInstance.resultAltitude}">
                                                        <td/>
                                                    </g:if>
                                                    <g:else>
                                                        <g:if test="${!coordResultInstance.resultMinAltitudeMissed}">
                                                            <td>${coordResultInstance.resultAltitude}${message(code:'fc.foot')}</td>
                                                        </g:if>
                                                        <g:else>
                                                            <td class="errors">${coordResultInstance.resultAltitude}${message(code:'fc.foot')}</td>
                                                        </g:else>
                                                    </g:else>
                                                    <g:if test="${coordResultInstance.resultCpNotFound}">
                                                        <td class="errors">${message(code:'fc.flighttest.cpnotfound.short')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td>${coordResultInstance.resultCpTime.format('HH:mm:ss')}</td>
                                                    </g:else>
                                                    <g:if test="${coordResultInstance.type != CoordType.SP}">
                                                        <g:if test="${coordResultInstance.resultBadCourseNum}">
                                                            <td class="errors">${coordResultInstance.resultBadCourseNum}</td>
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
                                                    <td/>
                                                    <td/>
                                                    <td>${message(code:'fc.unknown')}</td>
                                                    <g:if test="${coordResultInstance.type != CoordType.SP}">
                                                        <td>${message(code:'fc.unknown')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td/>
                                                    </g:else>
                                                </g:else>
                                            </tr>
                                            <tr class="${(legNo % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td/>
                                                <td/>
                                                <td>${message(code:'fc.test.results.penalty')}</td>
                                                <td/>
                                                <td/>
                                                <g:if test="${!coordResultInstance.resultAltitude}">
                                                    <td/>
                                                </g:if>
                                                <g:else>
                                                    <g:if test="${coordResultInstance.resultMinAltitudeMissed}">
                                                        <td class="points">${coordResultInstance.test.task.contest.flightTestMinAltitudeMissedPoints} ${message(code:'fc.points')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td class="points">0 ${message(code:'fc.points')}</td>
                                                    </g:else>
                                                </g:else>
                                                <g:if test="${coordResultInstance.resultEntered}">
                                                    <td class="points">${coordResultInstance.penaltyCoord} ${message(code:'fc.points')}</td>
                                                    <g:if test="${coordResultInstance.type != CoordType.SP}">
                                                        <td class="points">${coordResultInstance.resultBadCourseNum*coordResultInstance.test.task.contest.flightTestBadCoursePoints} ${message(code:'fc.points')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td/>
                                                    </g:else>
                                                </g:if>
                                                <g:else>
                                                    <td>${message(code:'fc.unknown')}</td>
                                                    <g:if test="${coordResultInstance.type != CoordType.SP}">
                                                        <td>${message(code:'fc.unknown')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td/>
                                                    </g:else>
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
                                    <td class="detailtitle">${message(code:'fc.flightresults.checkpointpenalties')}:</td>
                                    <td>${testInstance.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.takeoffmissed')}:</td>
                                    <g:viewbool value="${testInstance.flightTestTakeoffMissed}" tag="td" trueclass="points" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.badcoursestartlanding')}:</td>
                                    <g:viewbool value="${testInstance.flightTestBadCourseStartLanding}" tag="td" trueclass="points" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.landingtolate')}:</td>
                                    <g:viewbool value="${testInstance.flightTestLandingTooLate}" tag="td" trueclass="points" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.giventolate')}:</td>
                                    <g:viewbool value="${testInstance.flightTestGivenTooLate}" tag="td" trueclass="points" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flightresults.penalties')}:</td>
                                    <td class="points">${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="2">
                                        <g:if test="${!testInstance.flightTestComplete}">
                                            <g:if test="${testInstance.flightTestCheckPointsComplete}">
                                                <g:actionSubmit action="flightresultscomplete" value="${message(code:'fc.flightresults.complete')}" />
                                            </g:if>
                                            <g:actionSubmit action="importresults" value="${message(code:'fc.flightresults.aflosimport')}" />
                                            <g:if test="${!testInstance.flightTestTakeoffMissed}">
                                                <g:actionSubmit action="flightresultstakeoffmissedon" value="${message(code:'fc.flightresults.takeoffmissed.on')}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="flightresultstakeoffmissedoff" value="${message(code:'fc.flightresults.takeoffmissed.off')}" />
                                            </g:else>
                                            <g:if test="${!testInstance.flightTestBadCourseStartLanding}">
                                                <g:actionSubmit action="flightresultsbadcoursestartlandingon" value="${message(code:'fc.flightresults.badcoursestartlanding.on')}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="flightresultsbadcoursestartlandingoff" value="${message(code:'fc.flightresults.badcoursestartlanding.off')}" />
                                            </g:else>
                                            <g:if test="${!testInstance.flightTestLandingTooLate}">
                                                <g:actionSubmit action="flightresultslandingtolateon" value="${message(code:'fc.flightresults.landingtolate.on')}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="flightresultslandingtolateoff" value="${message(code:'fc.flightresults.landingtolate.off')}" />
                                            </g:else>
                                            <g:if test="${!testInstance.flightTestGivenTooLate}">
                                                <g:actionSubmit action="flightresultsgiventolateon" value="${message(code:'fc.flightresults.giventolate.on')}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="flightresultsgiventolateoff" value="${message(code:'fc.flightresults.giventolate.off')}" />
                                            </g:else>
                                        </g:if>
                                        <g:else>
                                            <g:actionSubmit action="flightresultsreopen" value="${message(code:'fc.flightresults.reopen')}" />
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