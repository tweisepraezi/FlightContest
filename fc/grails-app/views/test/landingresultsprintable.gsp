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
        <title>${message(code:'fc.landingresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.landingresults')} ${testInstance.GetStartNum()}</h2>
                <g:if test="${!testInstance.landingTestComplete}">
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                </g:if>
                <g:else>
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</h3>
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
                       	<g:if test="${testInstance.IsLandingTest1Run()}">
	                        <br/>
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                        	<thead>
	                                <tr>
			                       		<td>${message(code:'fc.landingtest.landing1')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></td>
	                                </tr>
	                        	</thead>
	                            <tbody>
	                                <tr>
	                                	<td>
		                                	<g:if test="${testInstance.landingTest1Landing == 2}">
		                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest1NoLandingPoints()} ${message(code:'fc.points')}<br/>
		                                	</g:if>
		                                 	<g:elseif test="${testInstance.landingTest1Landing == 3}">
		                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest1OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
		                                 	</g:elseif>
		                                 	<g:elseif test="${!testInstance.landingTest1Measure}">
		                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:elseif>
		                                	<g:else>
		                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest1Measure} (${testInstance.landingTest1MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:else>
		                                 	<g:if test="${testInstance.landingTest1RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest1RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest1PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest1PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest1GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest1GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest1GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest1GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest1AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest1AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
	                                    </td>
	                                </tr>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest1Penalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
                       	</g:if>
                       	<g:if test="${testInstance.IsLandingTest2Run()}">
	                        <br/>
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                        	<thead>
	                                <tr>
			                       		<td>${message(code:'fc.landingtest.landing2')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></td>
	                                </tr>
	                        	</thead>
	                            <tbody>
	                                <tr>
	                                	<td>
		                                	<g:if test="${testInstance.landingTest2Landing == 2}">
		                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest2NoLandingPoints()} ${message(code:'fc.points')}<br/>
		                                	</g:if>
		                                 	<g:elseif test="${testInstance.landingTest2Landing == 3}">
		                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest2OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
		                                 	</g:elseif>
		                                 	<g:elseif test="${!testInstance.landingTest2Measure}">
		                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:elseif>
		                                	<g:else>
		                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest2Measure} (${testInstance.landingTest2MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:else>
		                                 	<g:if test="${testInstance.landingTest2RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest2RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest2PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest2PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest2GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest2GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest2GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest2GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest2AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest2AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest2PowerInAir}">${message(code:'fc.landingtest.powerinair')}: ${testInstance.GetLandingTest2PowerInAirPoints()} ${message(code:'fc.points')}<br/></g:if>
	                                    </td>
	                                </tr>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest2Penalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
                       	</g:if>
                       	<g:if test="${testInstance.IsLandingTest3Run()}">
	                        <br/>
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                        	<thead>
	                                <tr>
			                       		<td>${message(code:'fc.landingtest.landing3')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></td>
	                                </tr>
	                        	</thead>
	                            <tbody>
	                                <tr>
	                                	<td>
		                                	<g:if test="${testInstance.landingTest3Landing == 2}">
		                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest3NoLandingPoints()} ${message(code:'fc.points')}<br/>
		                                	</g:if>
		                                 	<g:elseif test="${testInstance.landingTest3Landing == 3}">
		                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest3OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
		                                 	</g:elseif>
		                                 	<g:elseif test="${!testInstance.landingTest3Measure}">
		                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:elseif>
		                                	<g:else>
		                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest3Measure} (${testInstance.landingTest3MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:else>
		                                 	<g:if test="${testInstance.landingTest3RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest3RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest3PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest3PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest3GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest3GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest3GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest3GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest3AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest3AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest3PowerInAir}">${message(code:'fc.landingtest.powerinair')}: ${testInstance.GetLandingTest3PowerInAirPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest3FlapsInAir}">${message(code:'fc.landingtest.flapsinair')}: ${testInstance.GetLandingTest3FlapsInAirPoints()} ${message(code:'fc.points')}<br/></g:if>
	                                    </td>
	                                </tr>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest3Penalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
                       	</g:if>
                       	<g:if test="${testInstance.IsLandingTest4Run()}">
	                        <br/>
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                        	<thead>
	                                <tr>
			                       		<td>${message(code:'fc.landingtest.landing4')}<g:if test="${testInstance.IsPrecisionFlying()}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></td>
	                                </tr>
	                        	</thead>
	                            <tbody>
	                                <tr>
	                                	<td>
		                                	<g:if test="${testInstance.landingTest4Landing == 2}">
		                                		${message(code:'fc.landingtest.nolanding')}: ${testInstance.GetLandingTest4NoLandingPoints()} ${message(code:'fc.points')}<br/>
		                                	</g:if>
		                                 	<g:elseif test="${testInstance.landingTest4Landing == 3}">
		                                 		${message(code:'fc.landingtest.outsidelanding')}: ${testInstance.GetLandingTest4OutsideLandingPoints()} ${message(code:'fc.points')}<br/>
		                                 	</g:elseif>
		                                 	<g:elseif test="${!testInstance.landingTest4Measure}">
		                                 		${message(code:'fc.landingresults.measure')}: - (${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:elseif>
		                                	<g:else>
		                                 		${message(code:'fc.landingresults.measure')}: ${testInstance.landingTest4Measure} (${testInstance.landingTest4MeasurePenalties} ${message(code:'fc.points')})<br/>
		                                 	</g:else>
		                                 	<g:if test="${testInstance.landingTest4RollingOutside}">${message(code:'fc.landingtest.rollingoutside')}: ${testInstance.GetLandingTest4RollingOutsidePoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest4PowerInBox}">${message(code:'fc.landingtest.powerinbox')}: ${testInstance.GetLandingTest4PowerInBoxPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest4GoAroundWithoutTouching}">${message(code:'fc.landingtest.goaroundwithouttouching')}: ${testInstance.GetLandingTest4GoAroundWithoutTouchingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest4GoAroundInsteadStop}">${message(code:'fc.landingtest.goaroundinsteadstop')}: ${testInstance.GetLandingTest4GoAroundInsteadStopPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest4AbnormalLanding}">${message(code:'fc.landingtest.abnormallanding')}: ${testInstance.GetLandingTest4AbnormalLandingPoints()} ${message(code:'fc.points')}<br/></g:if>
		                                 	<g:if test="${testInstance.landingTest4TouchingObstacle}">${message(code:'fc.landingtest.touchingobstacle')}: ${testInstance.GetLandingTest4TouchingObstaclePoints()} ${message(code:'fc.points')}<br/></g:if>
	                                    </td>
	                                </tr>
	                            </tbody>
	                            <tfoot>
	                                <tr>
	                                    <td>${message(code:'fc.landingresults.landing')}: ${testInstance.landingTest4Penalties} ${message(code:'fc.points')}</td>
	                                </tr>
	                            </tfoot>
	                        </table>
                       	</g:if>
                       	<br/>
                        <table>
                            <tfoot>
                                <g:if test="${testInstance.IsLandingTestAnyRun()}">
                                    <g:if test="${testInstance.landingTestOtherPenalties > 0}">
                                        <tr>
                                            <td>${message(code:'fc.landingtest.otherpenalties')}: ${testInstance.landingTestOtherPenalties} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                </g:if>
                                <tr>
                                    <td>${message(code:'fc.penalties')}: ${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tfoot>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>