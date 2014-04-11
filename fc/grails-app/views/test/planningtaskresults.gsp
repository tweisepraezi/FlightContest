<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Planningtask)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Planningtask)}</h2>
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
                                            <th colspan="8" class="table-head">${message(code:'fc.planningresults.legresultlist')}</th>
                                        </tr>
                                        <tr>
                                            <th>${message(code:'fc.number')}</th>
                                            <th>${message(code:'fc.title')}</th>
                                            <th/>
                                            <th>${message(code:'fc.distance')}</th>
                                            <th>${message(code:'fc.truetrack')}</th>
                                            <th>${message(code:'fc.trueheading')}</th>
                                            <th>${message(code:'fc.groundspeed')}</th>
                                            <th>${message(code:'fc.legtime')}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:set var="leg_no" value="${new Integer(0)}" />
                                        <g:each var="testlegplanning_instance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                                            <g:set var="leg_no" value="${leg_no+1}" />
                                            <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">
                                            
                                            	<!-- search next id -->
                                            	<g:set var="next" value="${new Integer(0)}" />
		                                        <g:set var="setnext" value="${false}" />
        		                                <g:each var="testlegplanning_instance2" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                                                    <g:if test="${setnext}">
	       				                                <g:set var="next" value="${testlegplanning_instance2.id}" />
						                                <g:set var="setnext" value="${false}" />
                                                    </g:if>
                                                    <g:if test="${testlegplanning_instance2 == testlegplanning_instance}">
				                                        <g:set var="setnext" value="${true}" />
                                                    </g:if>
        		                                </g:each>
        		                                
                                                <td><g:testlegplanning2 var="${testlegplanning_instance}" name="${leg_no}" next="${next}" link="${createLink(controller:'testLegPlanning',action:'edit')}"/></td>
                                                <td>${testlegplanning_instance.coordTitle.titleCode()}</td>
                                                <td>${message(code:'fc.test.results.plan')}</td>
                                                <td>${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr_Planning(testlegplanning_instance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                                <td>${testlegplanning_instance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                            </tr>
                                            <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td/>
                                                <td>${message(code:'fc.test.results.given')}</td>
                                                <td/>
                                                <td/>
                                                <g:if test="${testlegplanning_instance.resultEntered}">
                                                    <td>${FcMath.GradStrComma(testlegplanning_instance.resultTrueHeading)}${message(code:'fc.grad')}</td>
                                                    <td/>
                                                    <td>${testlegplanning_instance.resultLegTimeStr()}${message(code:'fc.time.h')}</td>
                                                </g:if>
                                                <g:else>
                                                    <g:if test="${testlegplanning_instance.test.task.planningTestDirectionMeasure}">
                                                        <td>${message(code:'fc.unknown')}</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td/>
                                                    </g:else>
                                                    <td/>
                                                    <td>${message(code:'fc.unknown')}</td>
                                                </g:else>
                                            </tr>
                                            <tr class="${(leg_no % 2) == 0 ? '' : 'odd'}">
                                                <td/>
                                                <td/>
                                                <td>${message(code:'fc.test.results.penalty')}</td>
                                                <g:if test="${testlegplanning_instance.resultEntered}">
                                                    <td/>
                                                    <td/>
                                                    <g:set var="points_class" value="points"/>
                                                    <g:if test="${!testlegplanning_instance.penaltyTrueHeading}">
                                                        <g:set var="points_class" value="zeropoints"/>
                                                    </g:if>
                                                    <td class="${points_class}">${testlegplanning_instance.penaltyTrueHeading} ${message(code:'fc.points')}</td>
                                                    <td/>
                                                    <g:set var="points_class" value="points"/>
                                                    <g:if test="${!testlegplanning_instance.penaltyLegTime}">
                                                        <g:set var="points_class" value="zeropoints"/>
                                                    </g:if>
                                                    <td class="${points_class}">${testlegplanning_instance.penaltyLegTime} ${message(code:'fc.points')}</td>
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
                        <g:if test="${!testInstance.planningTestComplete}">
	                        <fieldset>
		                        <p>
		                        	<g:if test="${testInstance.GetPlanningTestPlanTooLatePoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="planningTestGivenTooLate" value="${testInstance.planningTestGivenTooLate}"/>
											<label>${message(code:'fc.planningtest.giventolate')}</label>
			    	                    </div>
			    	                </g:if>
		                        	<g:if test="${testInstance.GetPlanningTestExitRoomTooLatePoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="planningTestExitRoomTooLate" value="${testInstance.planningTestExitRoomTooLate}"/>
											<label>${message(code:'fc.planningtest.exitroomtolate')}</label>
			    	                    </div>
			    	                </g:if>
		                        </p>
		                        <p>
	                                <label>${message(code:'fc.planningtest.otherpenalties')}* [${message(code:'fc.points')}]:</label>
		                            <br/>
			                        <input type="text" id="planningTestOtherPenalties" name="planningTestOtherPenalties" value="${fieldValue(bean:testInstance,field:'planningTestOtherPenalties')}" tabIndex="1"/>
			                    </p>
                            </fieldset>
                        </g:if>
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
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <g:set var="points_class" value="points"/>
                                    <g:if test="${!testInstance.planningTestPenalties}">
                                        <g:set var="points_class" value="zeropoints"/>
                                    </g:if>
                                    <td class="${points_class}">${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="2">
                                        <g:if test="${!testInstance.planningTestComplete}">
                                            <g:if test="${testInstance.planningTestLegComplete}">
	                                            <g:if test="${params.next}">
	                                                <g:actionSubmit action="planningtaskresultsreadynext" value="${message(code:'fc.results.readynext')}"  tabIndex="2"/>
	                                            </g:if>
                                                <g:actionSubmit action="planningtaskresultsready" value="${message(code:'fc.results.ready')}" tabIndex="3"/>
					                        	<g:actionSubmit action="planningtaskresultssave" value="${message(code:'fc.save')}" tabIndex="4"/>
                                            </g:if>
                                            <g:elseif test="${params.next}">
                                                <g:actionSubmit action="planningtaskresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="2"/>
                                            </g:elseif>
                                            <g:actionSubmit action="printplanningtaskresults" value="${message(code:'fc.print')}" tabIndex="5"/>
                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="6"/>
                                        </g:if>
                                        <g:else>
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="planningtaskresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="2"/>
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                                            </g:else>
                                            <g:actionSubmit action="planningtaskresultsreopen" value="${message(code:'fc.results.reopen')}" tabIndex="4"/>
                                            <g:actionSubmit action="printplanningtaskresults" value="${message(code:'fc.print')}" tabIndex="5"/>
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="6"/>
                                            </g:if>
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