<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Observation)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Observation)}</h2>
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
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${testInstance.crew.mark}"></g:if></td>
                                </tr>
                                <g:if test="${testInstance.crew.team}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.team')}:</td>
                                        <td><g:team var="${testInstance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.resultclass')}:</td>
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
                        <g:if test="${!testInstance.observationTestComplete}">
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.observationresults.turnpointphotopenalties')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="observationTestTurnPointPhotoPenalties" name="observationTestTurnPointPhotoPenalties" value="${fieldValue(bean:testInstance,field:'observationTestTurnPointPhotoPenalties')}" tabIndex="1"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.observationresults.routephotopenalties')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="observationTestRoutePhotoPenalties" name="observationTestRoutePhotoPenalties" value="${fieldValue(bean:testInstance,field:'observationTestRoutePhotoPenalties')}" tabIndex="2"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.observationresults.groundtargetpenalties')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="observationTestGroundTargetPenalties" name="observationTestGroundTargetPenalties" value="${fieldValue(bean:testInstance,field:'observationTestGroundTargetPenalties')}" tabIndex="3"/>
	                            </p>
	                        </fieldset>
                               <table>
                                   <tbody>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                        <g:set var="points_class" value="points"/>
                                        <g:if test="${!testInstance.observationTestPenalties}">
                                            <g:set var="points_class" value="zeropoints"/>
                                        </g:if>
	                                    <td class="${points_class}">${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
                                   </tbody>
                               </table>
                        </g:if>
   	                    <g:else>
                               <table>
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
	                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                        <g:set var="points_class" value="points"/>
                                        <g:if test="${!testInstance.observationTestPenalties}">
                                            <g:set var="points_class" value="zeropoints"/>
                                        </g:if>
	                                    <td class="${points_class}">${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
	                                </tr>
                                   </tbody>
                               </table>
                        </g:else>
                        <g:if test="${!testInstance.observationTestComplete}">
                            <g:if test="${params.next}">
                                <g:actionSubmit action="observationresultsreadynext" value="${message(code:'fc.results.readynext')}"  tabIndex="4"/>
                            </g:if>
                        	<g:actionSubmit action="observationresultsready" value="${message(code:'fc.results.ready')}" tabIndex="5"/>
                        	<g:actionSubmit action="observationresultssave" value="${message(code:'fc.save')}" tabIndex="6"/>
                            <g:actionSubmit action="printobservationresults" value="${message(code:'fc.print')}" tabIndex="7"/>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="8"/>
                        </g:if>
                        <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="observationresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="4"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="5"/>
                            </g:else>
                        	<g:actionSubmit action="observationresultsreopen" value="${message(code:'fc.results.reopen')}" tabIndex="6"/>
                            <g:actionSubmit action="printobservationresults" value="${message(code:'fc.print')}" tabIndex="7"/>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="8"/>
                            </g:if>
                        </g:else>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>