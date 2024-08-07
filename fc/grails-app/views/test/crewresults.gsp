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
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${testInstance.GetNextTestID(ResultType.Crew,session)}"/>
						<g:set var="prev_id" value="${testInstance.GetPrevTestID(ResultType.Crew,session)}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
									<g:set var="upload_job_status" value="${testInstance.GetFlightTestUploadJobStatus()}"/>
                                    <g:if test="${upload_job_status == UploadJobStatus.Waiting}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email.png')}"/>
                                        </td>
                                    </g:if>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Sending}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>
                                        </td>
                                    </g:elseif>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Error}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>
                                        </td>
                                    </g:elseif>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Done}">
                                        <g:set var="email_links" value="${NetTools.EMailLinks(testInstance.GetFlightTestUploadLink())}" />
                                        <g:if test="${testInstance.IsFlightTestRun()}">
	                                        <td style="width:1%;"> 
	                                            <a href="${email_links.map}" target="_blank"><img src="${createLinkTo(dir:'images',file:'map.png')}"/></a>
	                                        </td>
	                                        <td style="width:1%;"> 
	                                            <a href="${email_links.kmz}" target="_blank"><img src="${createLinkTo(dir:'images',file:'kmz.png')}"/></a>
	                                        </td>
	                                    </g:if>
                                        <td style="width:1%;"> 
                                            <a href="${email_links.pdf}" target="_blank"><img src="${createLinkTo(dir:'images',file:'pdf.png')}"/></a>
                                        </td>
                                    </g:elseif>
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
									<g:if test="${testInstance.IsLandingTestAnyRun()}">
										<g:if test="${testInstance.IsLandingTest1Run()}">
										   <g:set var="task_penalties" value="${task_penalties + testInstance.landingTest1Penalties}" />
											<tr>
												<td class="detailtitle">${message(code:'fc.landingtest.landing1')}:</td>
												<td>${testInstance.landingTest1Penalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTest1Version()})<g:if test="${!testInstance.landingTest1Complete}"> [${message(code:'fc.provisional')}]</g:if></td>
											</tr>
										</g:if>
										<g:if test="${testInstance.IsLandingTest2Run()}">
										   <g:set var="task_penalties" value="${task_penalties + testInstance.landingTest2Penalties}" />
											<tr>
												<td class="detailtitle">${message(code:'fc.landingtest.landing2')}:</td>
												<td>${testInstance.landingTest2Penalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTest2Version()})<g:if test="${!testInstance.landingTest2Complete}"> [${message(code:'fc.provisional')}]</g:if></td>
											</tr>
										</g:if>
										<g:if test="${testInstance.IsLandingTest3Run()}">
										   <g:set var="task_penalties" value="${task_penalties + testInstance.landingTest3Penalties}" />
											<tr>
												<td class="detailtitle">${message(code:'fc.landingtest.landing3')}:</td>
												<td>${testInstance.landingTest3Penalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTest3Version()})<g:if test="${!testInstance.landingTest3Complete}"> [${message(code:'fc.provisional')}]</g:if></td>
											</tr>
										</g:if>
										<g:if test="${testInstance.IsLandingTest4Run()}">
										   <g:set var="task_penalties" value="${task_penalties + testInstance.landingTest4Penalties}" />
											<tr>
												<td class="detailtitle">${message(code:'fc.landingtest.landing4')}:</td>
												<td>${testInstance.landingTest4Penalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTest4Version()})<g:if test="${!testInstance.landingTest4Complete}"> [${message(code:'fc.provisional')}]</g:if></td>
											</tr>
										</g:if>
									</g:if>
									<g:else>
									   <g:set var="task_penalties" value="${task_penalties + testInstance.landingTestPenalties}" />
										<tr>
											<td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
											<td>${testInstance.landingTestPenalties} ${message(code:'fc.points')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></td>
										</tr>
									</g:else>
								</g:if>
	                           	<g:if test="${testInstance.IsSpecialTestRun()}">
	                           	   <g:set var="task_penalties" value="${task_penalties + testInstance.specialTestPenalties}" />
			                     	<tr>
			                     		<td class="detailtitle">${testInstance.GetSpecialTestTitle(false)}:</td>
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
										<g:if test="${testInstance.IsLandingTestAnyRun()}">
											<th colspan="4" class="table-head">${message(code:'fc.landingresults')}</th>
										</g:if>
										<g:else>
											<th colspan="4" class="table-head">${message(code:'fc.landingresults')} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})<g:if test="${!testInstance.landingTestComplete}"> [${message(code:'fc.provisional')}]</g:if></th>
										</g:else>
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
				                        <g:if test="${next_id}">
				                            <g:actionSubmit action="crewresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="${ti[0]++}"/>
				                        </g:if>
				                        <g:else>
				                            <g:actionSubmit action="crewresultsgotonext" value="${message(code:'fc.results.gotonext')}" disabled tabIndex="${ti[0]++}"/>
				                        </g:else>
				                        <g:if test="${prev_id}">
				                            <g:actionSubmit action="crewresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" tabIndex="${ti[0]++}"/>
				                        </g:if>
										<g:else>
											<g:actionSubmit action="crewresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
										</g:else>
				                        <g:actionSubmit action="crewresultsprintquestion" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
                                        <g:if test="${testInstance.IsSendEMailPossible()}">
                                            <g:actionSubmit action="sendmail" value="${message(code:'fc.crewresults.sendmail')}" onclick="this.form.target='_self';return true;" title="${testInstance.EMailAddress()}" tabIndex="${ti[0]++}"/>
                                        </g:if>
			                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
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