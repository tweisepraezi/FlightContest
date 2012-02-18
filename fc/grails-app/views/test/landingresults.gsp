<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.landingresults')} ${testInstance.viewpos+1} - ${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.landingresults')} ${testInstance.viewpos+1} - ${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</h2>
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
                       	<g:if test="${testInstance.IsLandingTestAnyRun()}">
                       		<g:if test="${testInstance.IsLandingTest1Run()}">
		                        <fieldset>
		                            <legend>${message(code:'fc.landingtest.landing1')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></legend>
			                        <g:if test="${!testInstance.landingTestComplete}">
			                            <p>
			                                <label>${message(code:'fc.landingresults.measure')}*:</label>
			                                <br/>
			                                <input type="text" id="landingTest1Measure" name="landingTest1Measure" value="${fieldValue(bean:testInstance,field:'landingTest1Measure')}" tabIndex="1"/>
			                            </p>
	                                    <p>
	                                        <div>
                                                <g:radioGroup name="landingTest1Landing" labels="${[message(code:'fc.landingtest.landing'),message(code:'fc.landingtest.nolanding'),message(code:'fc.landingtest.outsidelanding')]}" values="[1,2,3]" value="${testInstance.landingTest1Landing}">
                                                    <label>${it.radio} ${it.label}</label>
                                                </g:radioGroup>
	                                            <g:if test="${testInstance.landingTest1Landing == 2}">
	                                                (${testInstance.GetLandingTest1NoLandingPoints()} ${message(code:'fc.points')})
	                                            </g:if>
	                                            <g:elseif test="${testInstance.landingTest1Landing == 3}">
	                                                (${testInstance.GetLandingTest1OutsideLandingPoints()} ${message(code:'fc.points')})
	                                            </g:elseif>
	                                            <g:else>
	                                                (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})
	                                            </g:else>
	                                        </div>
	                                        <div>
	                                            <g:if test="${testInstance.GetLandingTest1RollingOutsidePoints() > 0}">
	                                                <g:checkBox name="landingTest1RollingOutside" value="${testInstance.landingTest1RollingOutside}"/>
	                                                <label>${message(code:'fc.landingtest.rollingoutside')}</label>
	                                            </g:if>
	                                            <g:if test="${testInstance.GetLandingTest1PowerInBoxPoints() > 0}">
	                                                <g:checkBox name="landingTest1PowerInBox" value="${testInstance.landingTest1PowerInBox}"/>
	                                                <label>${message(code:'fc.landingtest.powerinbox')}</label>
	                                            </g:if>
	                                        </div>
	                                        <div>
	                                            <g:if test="${testInstance.GetLandingTest1GoAroundWithoutTouchingPoints() > 0}">
	                                                <g:checkBox name="landingTest1GoAroundWithoutTouching" value="${testInstance.landingTest1GoAroundWithoutTouching}"/>
	                                                <label>${message(code:'fc.landingtest.goaroundwithouttouching')}</label>
	                                            </g:if>
	                                            <g:if test="${testInstance.GetLandingTest1GoAroundInsteadStopPoints() > 0}">
	                                                <g:checkBox name="landingTest1GoAroundInsteadStop" value="${testInstance.landingTest1GoAroundInsteadStop}"/>
	                                                <label>${message(code:'fc.landingtest.goaroundinsteadstop')}</label>
	                                            </g:if>
	                                        </div>
	                                        <div>
	                                            <g:if test="${testInstance.GetLandingTest1AbnormalLandingPoints() > 0}">
	                                                <g:checkBox name="landingTest1AbnormalLanding" value="${testInstance.landingTest1AbnormalLanding}"/>
	                                                <label>${message(code:'fc.landingtest.abnormallanding')}</label>
	                                            </g:if>
	                                        </div>
	                                    </p>
                                        <p>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest1Penalties} ${message(code:'fc.points')}</p>
			                        </g:if>
			                        <g:else>
			                            <table>
			                                <tbody>
		                                        <tr>
		                                            <g:if test="${testInstance.landingTest1Landing == 2}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>
		                                                <td>${testInstance.GetLandingTest1NoLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:if>
		                                            <g:elseif test="${testInstance.landingTest1Landing == 3}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>
		                                                <td>${testInstance.GetLandingTest1OutsideLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:elseif>
		                                            <g:elseif test="${!testInstance.landingTest1Measure}">
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>- (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:elseif>
		                                            <g:else>
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>${testInstance.landingTest1Measure} (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:else>
		                                         </tr>
		                                         <g:if test="${testInstance.landingTest1RollingOutside}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>
		                                                <td>${testInstance.GetLandingTest1RollingOutsidePoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest1PowerInBox}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>
		                                                <td>${testInstance.GetLandingTest1PowerInBoxPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest1GoAroundWithoutTouching}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>
		                                                <td>${testInstance.GetLandingTest1GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest1GoAroundInsteadStop}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>
		                                                <td>${testInstance.GetLandingTest1GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest1AbnormalLanding}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>
		                                                <td>${testInstance.GetLandingTest1AbnormalLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                        <tr>
		                                            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
		                                            <td class="subpoints">${testInstance.landingTest1Penalties} ${message(code:'fc.points')}</td>
		                                        </tr>
		                                    </tbody>
		                                </table>
			                        </g:else>
		                        </fieldset>
                            </g:if>
                       		<g:if test="${testInstance.IsLandingTest2Run()}">
		                        <fieldset>
		                            <legend>${message(code:'fc.landingtest.landing2')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></legend>
			                        <g:if test="${!testInstance.landingTestComplete}">
			                            <p>
			                                <label>${message(code:'fc.landingresults.measure')}*:</label>
			                                <br/>
			                                <input type="text" id="landingTest2Measure" name="landingTest2Measure" value="${fieldValue(bean:testInstance,field:'landingTest2Measure')}" tabIndex="2"/>
			                            </p>
				                        <p>
					                        <div>
                                                <g:radioGroup name="landingTest2Landing" labels="${[message(code:'fc.landingtest.landing'),message(code:'fc.landingtest.nolanding'),message(code:'fc.landingtest.outsidelanding')]}" values="[1,2,3]" value="${testInstance.landingTest2Landing}">
                                                    <label>${it.radio} ${it.label}</label>
                                                </g:radioGroup>
												<g:if test="${testInstance.landingTest2Landing == 2}">
													(${testInstance.GetLandingTest2NoLandingPoints()} ${message(code:'fc.points')})
												</g:if>
												<g:elseif test="${testInstance.landingTest2Landing == 3}">
													(${testInstance.GetLandingTest2OutsideLandingPoints()} ${message(code:'fc.points')})
												</g:elseif>
												<g:else>
													(${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})
												</g:else>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest2RollingOutsidePoints() > 0}">
					        	                	<g:checkBox name="landingTest2RollingOutside" value="${testInstance.landingTest2RollingOutside}"/>
													<label>${message(code:'fc.landingtest.rollingoutside')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest2PowerInBoxPoints() > 0}">
					        	                	<g:checkBox name="landingTest2PowerInBox" value="${testInstance.landingTest2PowerInBox}"/>
													<label>${message(code:'fc.landingtest.powerinbox')}</label>
												</g:if>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest2GoAroundWithoutTouchingPoints() > 0}">
					        	                	<g:checkBox name="landingTest2GoAroundWithoutTouching" value="${testInstance.landingTest2GoAroundWithoutTouching}"/>
													<label>${message(code:'fc.landingtest.goaroundwithouttouching')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest2GoAroundInsteadStopPoints() > 0}">
				        	                		<g:checkBox name="landingTest2GoAroundInsteadStop" value="${testInstance.landingTest2GoAroundInsteadStop}"/>
													<label>${message(code:'fc.landingtest.goaroundinsteadstop')}</label>
												</g:if>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest2AbnormalLandingPoints() > 0}">
					        	                	<g:checkBox name="landingTest2AbnormalLanding" value="${testInstance.landingTest2AbnormalLanding}"/>
													<label>${message(code:'fc.landingtest.abnormallanding')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest2PowerInAirPoints() > 0}">
					        	                	<g:checkBox name="landingTest2PowerInAir" value="${testInstance.landingTest2PowerInAir}"/>
													<label>${message(code:'fc.landingtest.powerinair')}</label>
												</g:if>
				    	                    </div>
				                        </p>
				                        <p>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest2Penalties} ${message(code:'fc.points')}</p>
                                    </g:if>
                                    <g:else>
                                        <table>
                                            <tbody>
		                                        <tr>
		                                            <g:if test="${testInstance.landingTest2Landing == 2}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>
		                                                <td>${testInstance.GetLandingTest2NoLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:if>
		                                            <g:elseif test="${testInstance.landingTest2Landing == 3}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>
		                                                <td>${testInstance.GetLandingTest2OutsideLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:elseif>
		                                            <g:elseif test="${!testInstance.landingTest2Measure}">
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>- (${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:elseif>
		                                            <g:else>
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>${testInstance.landingTest2Measure} (${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:else>
		                                         </tr>
		                                         <g:if test="${testInstance.landingTest2RollingOutside}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>
		                                                <td>${testInstance.GetLandingTest2RollingOutsidePoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest2PowerInBox}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>
		                                                <td>${testInstance.GetLandingTest2PowerInBoxPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest2GoAroundWithoutTouching}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>
		                                                <td>${testInstance.GetLandingTest2GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest2GoAroundInsteadStop}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>
		                                                <td>${testInstance.GetLandingTest2GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest2AbnormalLanding}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>
		                                                <td>${testInstance.GetLandingTest2AbnormalLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest2PowerInAir}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>
		                                                <td>${testInstance.GetLandingTest2PowerInAirPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                        <tr>
		                                            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
		                                            <td class="subpoints">${testInstance.landingTest2Penalties} ${message(code:'fc.points')}</td>
		                                        </tr>
                                            </tbody>
                                        </table>
                                    </g:else>
		                        </fieldset>
                            </g:if>
                       		<g:if test="${testInstance.IsLandingTest3Run()}">
		                        <fieldset>
		                            <legend>${message(code:'fc.landingtest.landing3')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></legend>
			                        <g:if test="${!testInstance.landingTestComplete}">
			                            <p>
			                                <label>${message(code:'fc.landingresults.measure')}*:</label>
			                                <br/>
			                                <input type="text" id="landingTest3Measure" name="landingTest3Measure" value="${fieldValue(bean:testInstance,field:'landingTest3Measure')}" tabIndex="3"/>
			                            </p>
				                        <p>
					                        <div>
                                                <g:radioGroup name="landingTest3Landing" labels="${[message(code:'fc.landingtest.landing'),message(code:'fc.landingtest.nolanding'),message(code:'fc.landingtest.outsidelanding')]}" values="[1,2,3]" value="${testInstance.landingTest3Landing}">
                                                    <label>${it.radio} ${it.label}</label>
                                                </g:radioGroup>
												<g:if test="${testInstance.landingTest3Landing == 2}">
													(${testInstance.GetLandingTest3NoLandingPoints()} ${message(code:'fc.points')})
												</g:if>
												<g:elseif test="${testInstance.landingTest3Landing == 3}">
													(${testInstance.GetLandingTest3OutsideLandingPoints()} ${message(code:'fc.points')})
												</g:elseif>
												<g:else>
													(${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})
												</g:else>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest3RollingOutsidePoints() > 0}">
					        	                	<g:checkBox name="landingTest3RollingOutside" value="${testInstance.landingTest3RollingOutside}"/>
													<label>${message(code:'fc.landingtest.rollingoutside')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest3PowerInBoxPoints() > 0}">
					        	                	<g:checkBox name="landingTest3PowerInBox" value="${testInstance.landingTest3PowerInBox}"/>
													<label>${message(code:'fc.landingtest.powerinbox')}</label>
												</g:if>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest3GoAroundWithoutTouchingPoints() > 0}">
					        	                	<g:checkBox name="landingTest3GoAroundWithoutTouching" value="${testInstance.landingTest3GoAroundWithoutTouching}"/>
													<label>${message(code:'fc.landingtest.goaroundwithouttouching')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest3GoAroundInsteadStopPoints() > 0}">
				        	                		<g:checkBox name="landingTest3GoAroundInsteadStop" value="${testInstance.landingTest3GoAroundInsteadStop}"/>
													<label>${message(code:'fc.landingtest.goaroundinsteadstop')}</label>
												</g:if>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest3AbnormalLandingPoints() > 0}">
					        	                	<g:checkBox name="landingTest3AbnormalLanding" value="${testInstance.landingTest3AbnormalLanding}"/>
													<label>${message(code:'fc.landingtest.abnormallanding')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest3PowerInAirPoints() > 0}">
					        	                	<g:checkBox name="landingTest3PowerInAir" value="${testInstance.landingTest3PowerInAir}"/>
													<label>${message(code:'fc.landingtest.powerinair')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest3FlapsInAirPoints() > 0}">
				        	                		<g:checkBox name="landingTest3FlapsInAir" value="${testInstance.landingTest3FlapsInAir}"/>
													<label>${message(code:'fc.landingtest.flapsinair')}</label>
												</g:if>
				    	                    </div>
				                        </p>
				                        <p>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest3Penalties} ${message(code:'fc.points')}</p>
                                    </g:if>
                                    <g:else>
                                        <table>
                                            <tbody>
		                                        <tr>
		                                            <g:if test="${testInstance.landingTest3Landing == 2}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>
		                                                <td>${testInstance.GetLandingTest3NoLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:if>
		                                            <g:elseif test="${testInstance.landingTest3Landing == 3}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>
		                                                <td>${testInstance.GetLandingTest3OutsideLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:elseif>
		                                            <g:elseif test="${!testInstance.landingTest3Measure}">
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>- (${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:elseif>
		                                            <g:else>
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>${testInstance.landingTest3Measure} (${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:else>
		                                         </tr>
		                                         <g:if test="${testInstance.landingTest3RollingOutside}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>
		                                                <td>${testInstance.GetLandingTest3RollingOutsidePoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest3PowerInBox}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>
		                                                <td>${testInstance.GetLandingTest3PowerInBoxPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest3GoAroundWithoutTouching}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>
		                                                <td>${testInstance.GetLandingTest3GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest3GoAroundInsteadStop}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>
		                                                <td>${testInstance.GetLandingTest3GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest3AbnormalLanding}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>
		                                                <td>${testInstance.GetLandingTest3AbnormalLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest3PowerInAir}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.powerinair')}:</td>
		                                                <td>${testInstance.GetLandingTest3PowerInAirPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest3FlapsInAir}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.flapsinair')}:</td>
		                                                <td>${testInstance.GetLandingTest3FlapsInAirPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                        <tr>
		                                            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
		                                            <td class="subpoints">${testInstance.landingTest3Penalties} ${message(code:'fc.points')}</td>
		                                        </tr>
                                            </tbody>
                                        </table>
                                    </g:else>
		                        </fieldset>
                            </g:if>
                       		<g:if test="${testInstance.IsLandingTest4Run()}">
		                        <fieldset>
		                            <legend>${message(code:'fc.landingtest.landing4')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></legend>
			                        <g:if test="${!testInstance.landingTestComplete}">
			                            <p>
			                                <label>${message(code:'fc.landingresults.measure')}*:</label>
			                                <br/>
			                                <input type="text" id="landingTest4Measure" name="landingTest4Measure" value="${fieldValue(bean:testInstance,field:'landingTest4Measure')}" tabIndex="4"/>
			                            </p>
				                        <p>
					                        <div>
                                                <g:radioGroup name="landingTest4Landing" labels="${[message(code:'fc.landingtest.landing'),message(code:'fc.landingtest.nolanding'),message(code:'fc.landingtest.outsidelanding')]}" values="[1,2,3]" value="${testInstance.landingTest4Landing}">
                                                    <label>${it.radio} ${it.label}</label>
                                                </g:radioGroup>
												<g:if test="${testInstance.landingTest4Landing == 2}">
													(${testInstance.GetLandingTest4NoLandingPoints()} ${message(code:'fc.points')})
												</g:if>
												<g:elseif test="${testInstance.landingTest4Landing == 3}">
													(${testInstance.GetLandingTest4OutsideLandingPoints()} ${message(code:'fc.points')})
												</g:elseif>
												<g:else>
													(${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})
												</g:else>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest4RollingOutsidePoints() > 0}">
					        	                	<g:checkBox name="landingTest4RollingOutside" value="${testInstance.landingTest4RollingOutside}"/>
													<label>${message(code:'fc.landingtest.rollingoutside')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest4PowerInBoxPoints() > 0}">
					        	                	<g:checkBox name="landingTest4PowerInBox" value="${testInstance.landingTest4PowerInBox}"/>
													<label>${message(code:'fc.landingtest.powerinbox')}</label>
												</g:if>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest4GoAroundWithoutTouchingPoints() > 0}">
					        	                	<g:checkBox name="landingTest4GoAroundWithoutTouching" value="${testInstance.landingTest4GoAroundWithoutTouching}"/>
													<label>${message(code:'fc.landingtest.goaroundwithouttouching')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest4GoAroundInsteadStopPoints() > 0}">
				        	                		<g:checkBox name="landingTest4GoAroundInsteadStop" value="${testInstance.landingTest4GoAroundInsteadStop}"/>
													<label>${message(code:'fc.landingtest.goaroundinsteadstop')}</label>
												</g:if>
				    	                    </div>
					                        <div>
					                        	<g:if test="${testInstance.GetLandingTest4AbnormalLandingPoints() > 0}">
				        	                		<g:checkBox name="landingTest4AbnormalLanding" value="${testInstance.landingTest4AbnormalLanding}"/>
													<label>${message(code:'fc.landingtest.abnormallanding')}</label>
												</g:if>
					                        	<g:if test="${testInstance.GetLandingTest4TouchingObstaclePoints() > 0}">
				        	                		<g:checkBox name="landingTest4TouchingObstacle" value="${testInstance.landingTest4TouchingObstacle}"/>
													<label>${message(code:'fc.landingtest.touchingobstacle')}</label>
												</g:if>
				    	                    </div>
				                        </p>
				                        <p>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest4Penalties} ${message(code:'fc.points')}</p>
                                    </g:if>
                                    <g:else>
                                        <table>
                                            <tbody>
		                                        <tr>
		                                            <g:if test="${testInstance.landingTest4Landing == 2}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.nolanding')}:</td>
		                                                <td>${testInstance.GetLandingTest4NoLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:if>
		                                            <g:elseif test="${testInstance.landingTest4Landing == 3}">
		                                                <td class="detailtitle">${message(code:'fc.landingtest.outsidelanding')}:</td>
		                                                <td>${testInstance.GetLandingTest4OutsideLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </g:elseif>
		                                            <g:elseif test="${!testInstance.landingTest4Measure}">
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>- (${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:elseif>
		                                            <g:else>
		                                                <td class="detailtitle">${message(code:'fc.landingresults.measure')}:</td>
		                                                <td>${testInstance.landingTest4Measure} (${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})</td>
		                                            </g:else>
		                                         </tr>
		                                         <g:if test="${testInstance.landingTest4RollingOutside}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.rollingoutside')}:</td>
		                                                <td>${testInstance.GetLandingTest4RollingOutsidePoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest4PowerInBox}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.powerinbox')}:</td>
		                                                <td>${testInstance.GetLandingTest4PowerInBoxPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest4GoAroundWithoutTouching}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundwithouttouching')}:</td>
		                                                <td>${testInstance.GetLandingTest4GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest4GoAroundInsteadStop}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.goaroundinsteadstop')}:</td>
		                                                <td>${testInstance.GetLandingTest4GoAroundInsteadStopPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest4AbnormalLanding}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.abnormallanding')}:</td>
		                                                <td>${testInstance.GetLandingTest4AbnormalLandingPoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                         <g:if test="${testInstance.landingTest4TouchingObstacle}">
		                                            <tr>
		                                                <td class="detailtitle">${message(code:'fc.landingtest.touchingobstacle')}:</td>
		                                                <td>${testInstance.GetLandingTest4TouchingObstaclePoints()} ${message(code:'fc.points')}</td>
		                                            </tr>
		                                         </g:if>
		                                        <tr>
		                                            <td class="detailtitle">${message(code:'fc.landingresults.landing')}:</td>
		                                            <td class="subpoints">${testInstance.landingTest4Penalties} ${message(code:'fc.points')}</td>
		                                        </tr>
                                            </tbody>
                                        </table>
                                    </g:else>
		                        </fieldset>
                            </g:if>
                            <g:if test="${!testInstance.landingTestComplete}">
                                <fieldset>
	                                <p>
	                                    <label>${message(code:'fc.landingtest.otherpenalties')}* [${message(code:'fc.points')}]:</label>
	                                    <br/>
	                                    <input type="text" id="landingTestOtherPenalties" name="landingTestOtherPenalties" value="${fieldValue(bean:testInstance,field:'landingTestOtherPenalties')}" tabIndex="5"/>
	                                </p>
                                </fieldset>
                            </g:if>
                        </g:if>
                        <g:else>
	                        <g:if test="${!testInstance.landingTestComplete}">
		                        <fieldset>
		                            <p>
		                                <label>${message(code:'fc.penalties.total')}* [${message(code:'fc.points')}]:</label>
		                                <br/>
		                                <input type="text" id="landingTestPenalties" name="landingTestPenalties" value="${fieldValue(bean:testInstance,field:'landingTestPenalties')}" tabIndex="6"/>
		                            </p>
		                        </fieldset>
		                	</g:if>
                        </g:else>
                        <table>
                            <tbody>
                                <g:if test="${testInstance.IsLandingTestAnyRun()}">
	                                <g:if test="${testInstance.landingTestOtherPenalties > 0}">
	                                    <tr>
	                                        <td class="detailtitle">${message(code:'fc.landingtest.otherpenalties')}:</td>
	                                        <td>${testInstance.landingTestOtherPenalties} ${message(code:'fc.points')}</td>
	                                    </tr>
	                                </g:if>
	                            </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <td class="points">${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!testInstance.landingTestComplete}">
                        	<g:actionSubmit action="landingresultscomplete" value="${message(code:'fc.landingresults.complete')}" tabIndex="7"/>
                        	<g:actionSubmit action="landingresultssave" value="${message(code:'fc.save')}" tabIndex="8"/>
                        </g:if>
                        <g:else>
                        	<g:actionSubmit action="landingresultsreopen" value="${message(code:'fc.landingresults.reopen')}" tabIndex="9"/>
                        </g:else>
				        <g:actionSubmit action="printlandingresults" value="${message(code:'fc.print')}" tabIndex="10"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="11"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>