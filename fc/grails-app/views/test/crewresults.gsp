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
                <h2>${testInstance.GetTitle(ResultType.Crew)}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" params="${['crewresultsReturnAction':crewresultsReturnAction,'crewresultsReturnController':crewresultsReturnController,'crewresultsReturnID':crewresultsReturnID]}">
                        <a name="start"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                    <td style="width:1%;"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></td>
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
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <g:set var="task_penalties" value="${new Integer(0)}" />
	                           	<g:if test="${testInstance.IsPlanningTestRun()}">
	                           	   <g:set var="task_penalties" value="${task_penalties + testInstance.planningTestPenalties}" />
    	                        	<tr>
        	                            <td class="detailtitle">${message(code:'fc.planningresults.planning')}:</td>
        	                            <td>${testInstance.planningTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})<g:if test="${!testInstance.planningTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                     	</tr>
	                            </g:if>
   	                        	<g:if test="${testInstance.IsFlightTestRun()}">
   	                        	   <g:set var="task_penalties" value="${task_penalties + testInstance.flightTestPenalties}" />
			                    	<tr>
			                     		<td class="detailtitle">${message(code:'fc.flightresults.flight')}:</td>
        	                            <td>${testInstance.flightTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})<g:if test="${!testInstance.flightTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsObservationTestRun()}">
                            	   <g:set var="task_penalties" value="${task_penalties + testInstance.observationTestPenalties}" />
			                    	<tr>
			                     		<td class="detailtitle">${message(code:'fc.observationresults.observations')}:</td>
        	                            <td>${testInstance.observationTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})<g:if test="${!testInstance.observationTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
                            	<g:if test="${testInstance.IsLandingTestRun()}">
                            	   <g:set var="task_penalties" value="${task_penalties + testInstance.landingTestPenalties}" />
			                    	<tr>
			                     		<td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
        	                            <td>${testInstance.landingTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
			                    </g:if>
	                           	<g:if test="${testInstance.IsSpecialTestRun()}">
	                           	   <g:set var="task_penalties" value="${task_penalties + testInstance.specialTestPenalties}" />
			                     	<tr>
			                     		<td class="detailtitle">${message(code:'fc.specialresults.other')}:</td>
        	                            <td>${testInstance.specialTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetSpecialTestVersion()})<g:if test="${!testInstance.specialTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
			                    	</tr>
	                            </g:if>
	                            <g:if test="${testInstance.IsIncreaseEnabled()}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.task.increaseenabled.short',args:[testInstance.GetIncreaseValue()])}:</td>
                                        <td>${testInstance.GetIncreasePenalties(task_penalties)} ${message(code:'fc.points')} </td>
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
                            <g:planningtaskTestCrewResults t="${testInstance}"/>
	                    </g:if>
	                    <g:if test="${testInstance.IsFlightTestRun()}">
                            <g:flightTestCrewResults t="${testInstance}"/>
	                    </g:if>
	                    <g:if test="${testInstance.IsObservationTestRun()}">
                            <g:observationTestComplete t="${testInstance}" crewResults="${true}"/>
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
                                        <g:if test="${testInstance.landingTestOtherPenalties != 0}">
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
                            <g:specialTestCrewResults t="${testInstance}"/>
	                    </g:if>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
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
			                        </td>
                                    <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                                </tr>
                            </tfoot>
                        </table>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>