<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Landing4)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Landing4)}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${testInstance.GetNextTestID(ResultType.Landing4,session)}"/>
						<g:set var="prev_id" value="${testInstance.GetPrevTestID(ResultType.Landing4,session)}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
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
                                        <td><g:flighttestwindtext var="${testInstance.flighttestwind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
						<fieldset>
							<legend>${testInstance.GetLandingShortTitle(testInstance.task.landingTest4Points,false)}</legend>
							<g:if test="${!testInstance.landingTest4Complete}">
								<p>
									<label>${message(code:'fc.landingresults.measure')}*:</label>
									<br/>
									<input type="text" id="landingTest4Measure" name="landingTest4Measure" value="${fieldValue(bean:testInstance,field:'landingTest4Measure')}" tabIndex="${ti[0]++}"/>
								</p>
								<p>
									<div>
										<g:radioGroup name="landingTest4Landing" labels="${[message(code:'fc.landingtest.landing'),message(code:'fc.landingtest.nolanding')+" ("+Contest.LANDING_NO+")",message(code:'fc.landingtest.outsidelanding')+" ("+Contest.LANDING_OUT+")"]}" values="[1,2,3]" value="${testInstance.landingTest4Landing}">
											<label>${it.radio} ${it.label}</label>
										</g:radioGroup>
										<g:if test="${testInstance.landingTest4Landing == 2}">
											(${testInstance.GetLandingTestNoLandingPoints(testInstance.task.landingTest4Points)} ${message(code:'fc.points')})
										</g:if>
										<g:elseif test="${testInstance.landingTest4Landing == 3}">
											(${testInstance.GetLandingTestOutsideLandingPoints(testInstance.task.landingTest4Points)} ${message(code:'fc.points')})
										</g:elseif>
										<g:else>
											(${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})
										</g:else>
									</div>
									<div>
										<g:if test="${testInstance.GetLandingTestRollingOutsidePoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4RollingOutside" value="${testInstance.landingTest4RollingOutside}"/>
											<label>${message(code:'fc.landingtest.rollingoutside')}</label>
										</g:if>
										<g:if test="${testInstance.GetLandingTestPowerInBoxPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4PowerInBox" value="${testInstance.landingTest4PowerInBox}"/>
											<label>${message(code:'fc.landingtest.powerinbox')}</label>
										</g:if>
									</div>
									<div>
										<g:if test="${testInstance.GetLandingTestGoAroundWithoutTouchingPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4GoAroundWithoutTouching" value="${testInstance.landingTest4GoAroundWithoutTouching}"/>
											<label>${message(code:'fc.landingtest.goaroundwithouttouching')}</label>
										</g:if>
										<g:if test="${testInstance.GetLandingTestGoAroundInsteadStopPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4GoAroundInsteadStop" value="${testInstance.landingTest4GoAroundInsteadStop}"/>
											<label>${message(code:'fc.landingtest.goaroundinsteadstop')}</label>
										</g:if>
									</div>
									<div>
										<g:if test="${testInstance.GetLandingTestAbnormalLandingPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4AbnormalLanding" value="${testInstance.landingTest4AbnormalLanding}"/>
											<label>${message(code:'fc.landingtest.abnormallanding')}</label>
										</g:if>
										<g:if test="${testInstance.GetLandingTestNotAllowedAerodynamicAuxiliariesPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4NotAllowedAerodynamicAuxiliaries" value="${testInstance.landingTest4NotAllowedAerodynamicAuxiliaries}"/>
											<label>${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}</label>
										</g:if>
										<g:if test="${testInstance.GetLandingTestPowerInAirPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4PowerInAir" value="${testInstance.landingTest4PowerInAir}"/>
											<label>${message(code:'fc.landingtest.powerinair')}</label>
										</g:if>
										<g:if test="${testInstance.GetLandingTestFlapsInAirPoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4FlapsInAir" value="${testInstance.landingTest4FlapsInAir}"/>
											<label>${message(code:'fc.landingtest.flapsinair')}</label>
										</g:if>
										<g:if test="${testInstance.GetLandingTestTouchingObstaclePoints(testInstance.task.landingTest4Points) > 0}">
											<g:checkBox name="landingTest4TouchingObstacle" value="${testInstance.landingTest4TouchingObstacle}"/>
											<label>${message(code:'fc.landingtest.touchingobstacle')}</label>
										</g:if>
									</div>
								</p>
                                <script>
									$('#landingTest4Measure').select();
								</script>
							</g:if>
							<g:else>
								<g:landingTest4Complete t="${testInstance}" crewResults="${false}"/>
							</g:else>
						</fieldset>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>
                                    <g:set var="points_class" value="points"/>
                                    <g:if test="${!testInstance.landingTest4Penalties}">
                                        <g:set var="points_class" value="zeropoints"/>
                                    </g:if>
                                    <td class="${points_class}">${testInstance.landingTest4Penalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
				                        <g:if test="${!testInstance.landingTest4Complete}">
				                        	<g:actionSubmit action="landingresultssave4" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
				                            <g:if test="${next_id}">
				                                <g:actionSubmit action="landingresultsreadynext4" value="${message(code:'fc.results.readynext')}" tabIndex="${ti[0]++}"/>
				                            </g:if>
											<g:else>
												<g:actionSubmit action="landingresultsreadynext4" value="${message(code:'fc.results.readynext')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
				                        	<g:actionSubmit action="landingresultsready4" value="${message(code:'fc.results.ready')}" tabIndex="${ti[0]++}"/>
				                            <g:if test="${next_id}">
				                                <g:actionSubmit action="landingresultssavenext4" value="${message(code:'fc.results.savenext')}" tabIndex="${ti[0]++}"/>
				                                <g:actionSubmit action="landingresultsgotonext4" value="${message(code:'fc.results.gotonext')}" tabIndex="${ti[0]++}"/>
				                            </g:if>
											<g:else>
				                                <g:actionSubmit action="landingresultssavenext4" value="${message(code:'fc.results.savenext')}" disabled tabIndex="${ti[0]++}"/>
				                                <g:actionSubmit action="landingresultsgotonext4" value="${message(code:'fc.results.gotonext')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
											<g:if test="${prev_id}">
												<g:actionSubmit action="landingresultsgotoprev4" value="${message(code:'fc.results.gotoprev')}" tabIndex="${ti[0]++}"/>
											</g:if>
											<g:else>
												<g:actionSubmit action="landingresultsgotoprev4" value="${message(code:'fc.results.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
				                            <g:actionSubmit action="printlandingresults4" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
			                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
				                        </g:if>
				                        <g:else>
				                            <g:if test="${next_id}">
				                                <g:actionSubmit action="landingresultsgotonext4" value="${message(code:'fc.results.gotonext')}" tabIndex="${ti[0]++}"/>
				                            </g:if>
				                            <g:else>
				                                <g:actionSubmit action="landingresultsgotonext4" value="${message(code:'fc.results.gotonext')}" disabled tabIndex="${ti[0]++}"/>
				                            </g:else>
											<g:if test="${prev_id}">
												<g:actionSubmit action="landingresultsgotoprev4" value="${message(code:'fc.results.gotoprev')}" tabIndex="${ti[0]++}"/>
											</g:if>
											<g:else>
												<g:actionSubmit action="landingresultsgotoprev4" value="${message(code:'fc.results.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
				                        	<g:actionSubmit action="landingresultsreopen4" value="${message(code:'fc.results.reopen')}" tabIndex="${ti[0]++}"/>
				                            <g:actionSubmit action="printlandingresults4" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
			                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
				                        </g:else>
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