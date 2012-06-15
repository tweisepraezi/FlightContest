<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${resultclassInstance.GetListTitle('fc.contestrule.classpoints.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="resultClass"/>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${resultclassInstance.GetListTitle('fc.contestrule.classpoints.edit')}</h2>
                <g:hasErrors bean="${resultclassInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${resultclassInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['editpointsReturnAction':editpointsReturnAction,'editpointsReturnController':editpointsReturnController,'editpointsReturnID':editpointsReturnID]}" >
                        <table>
                            <tbody>
	                        	<tr>
	                        		<td>${message(code:resultclassInstance.contestRule.titleCode)}</td>
	                        	</tr>
	                        </tbody>
                        </table>
                        <p class="warning">${message(code:'fc.resultclass.recalculatepoints')}</p>
                        <fieldset>
                            <legend>${message(code:'fc.planningtest')}</legend>
                            <p/>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.planningtest.directioncorrectgrad')}* [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestDirectionCorrectGrad" name="planningTestDirectionCorrectGrad" value="${fieldValue(bean:resultclassInstance,field:'planningTestDirectionCorrectGrad')}" tabIndex="1"/>
	                                <g:if test="${resultclassInstance.planningTestDirectionCorrectGrad != resultclassInstance.contestRule.ruleValues.planningTestDirectionCorrectGrad}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.directionpointspergrad')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestDirectionPointsPerGrad" name="planningTestDirectionPointsPerGrad" value="${fieldValue(bean:resultclassInstance,field:'planningTestDirectionPointsPerGrad')}" tabIndex="2"/>
	                                <g:if test="${resultclassInstance.planningTestDirectionPointsPerGrad != resultclassInstance.contestRule.ruleValues.planningTestDirectionPointsPerGrad}">!</g:if>
	                            </p>
	                        </fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.planningtest.timecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestTimeCorrectSecond" name="planningTestTimeCorrectSecond" value="${fieldValue(bean:resultclassInstance,field:'planningTestTimeCorrectSecond')}" tabIndex="3"/>
	                                <g:if test="${resultclassInstance.planningTestTimeCorrectSecond != resultclassInstance.contestRule.ruleValues.planningTestTimeCorrectSecond}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.timepointspersecond')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestTimePointsPerSecond" name="planningTestTimePointsPerSecond" value="${fieldValue(bean:resultclassInstance,field:'planningTestTimePointsPerSecond')}" tabIndex="4"/>
	                                <g:if test="${resultclassInstance.planningTestTimePointsPerSecond != resultclassInstance.contestRule.ruleValues.planningTestTimePointsPerSecond}">!</g:if>
	                            </p>
	                        </fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.planningtest.maxpoints')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestMaxPoints" name="planningTestMaxPoints" value="${fieldValue(bean:resultclassInstance,field:'planningTestMaxPoints')}" tabIndex="5"/>
	                                <g:if test="${resultclassInstance.planningTestMaxPoints != resultclassInstance.contestRule.ruleValues.planningTestMaxPoints}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.giventolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestPlanTooLatePoints" name="planningTestPlanTooLatePoints" value="${fieldValue(bean:resultclassInstance,field:'planningTestPlanTooLatePoints')}" tabIndex="6"/>
	                                <g:if test="${resultclassInstance.planningTestPlanTooLatePoints != resultclassInstance.contestRule.ruleValues.planningTestPlanTooLatePoints}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.exitroomtolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestExitRoomTooLatePoints" name="planningTestExitRoomTooLatePoints" value="${fieldValue(bean:resultclassInstance,field:'planningTestExitRoomTooLatePoints')}" tabIndex="7"/>
	                                <g:if test="${resultclassInstance.planningTestExitRoomTooLatePoints != resultclassInstance.contestRule.ruleValues.planningTestExitRoomTooLatePoints}">!</g:if>
	                            </p>
	                        </fieldset>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.flighttest')}</legend>
                            <p/>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.takeoffmissed')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestTakeoffMissedPoints" name="flightTestTakeoffMissedPoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestTakeoffMissedPoints')}" tabIndex="8"/>
	                                <g:if test="${resultclassInstance.flightTestTakeoffMissedPoints != resultclassInstance.contestRule.ruleValues.flightTestTakeoffMissedPoints}">!</g:if>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cptimecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCptimeCorrectSecond" name="flightTestCptimeCorrectSecond" value="${fieldValue(bean:resultclassInstance,field:'flightTestCptimeCorrectSecond')}" tabIndex="9"/>
	                                <g:if test="${resultclassInstance.flightTestCptimeCorrectSecond != resultclassInstance.contestRule.ruleValues.flightTestCptimeCorrectSecond}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cptimepointspersecond')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCptimePointsPerSecond" name="flightTestCptimePointsPerSecond" value="${fieldValue(bean:resultclassInstance,field:'flightTestCptimePointsPerSecond')}" tabIndex="10"/>
	                                <g:if test="${resultclassInstance.flightTestCptimePointsPerSecond != resultclassInstance.contestRule.ruleValues.flightTestCptimePointsPerSecond}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cptimemaxpoints')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCptimeMaxPoints" name="flightTestCptimeMaxPoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestCptimeMaxPoints')}" tabIndex="11"/>
	                                <g:if test="${resultclassInstance.flightTestCptimeMaxPoints != resultclassInstance.contestRule.ruleValues.flightTestCptimeMaxPoints}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cpnotfound')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCpNotFoundPoints" name="flightTestCpNotFoundPoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestCpNotFoundPoints')}" tabIndex="12"/>
	                                <g:if test="${resultclassInstance.flightTestCpNotFoundPoints != resultclassInstance.contestRule.ruleValues.flightTestCpNotFoundPoints}">!</g:if>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.procedureturnnotflown')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestProcedureTurnNotFlownPoints" name="flightTestProcedureTurnNotFlownPoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestProcedureTurnNotFlownPoints')}" tabIndex="13"/>
	                                <g:if test="${resultclassInstance.flightTestProcedureTurnNotFlownPoints != resultclassInstance.contestRule.ruleValues.flightTestProcedureTurnNotFlownPoints}">!</g:if>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.minaltitudemissed')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestMinAltitudeMissedPoints" name="flightTestMinAltitudeMissedPoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestMinAltitudeMissedPoints')}" tabIndex="14"/>
	                                <g:if test="${resultclassInstance.flightTestMinAltitudeMissedPoints != resultclassInstance.contestRule.ruleValues.flightTestMinAltitudeMissedPoints}">!</g:if>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.badcoursecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestBadCourseCorrectSecond" name="flightTestBadCourseCorrectSecond" value="${fieldValue(bean:resultclassInstance,field:'flightTestBadCourseCorrectSecond')}" tabIndex="15"/>
	                                <g:if test="${resultclassInstance.flightTestBadCourseCorrectSecond != resultclassInstance.contestRule.ruleValues.flightTestBadCourseCorrectSecond}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.badcourse')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestBadCoursePoints" name="flightTestBadCoursePoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestBadCoursePoints')}" tabIndex="16"/>
	                                <g:if test="${resultclassInstance.flightTestBadCoursePoints != resultclassInstance.contestRule.ruleValues.flightTestBadCoursePoints}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.badcoursestartlanding')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestBadCourseStartLandingPoints" name="flightTestBadCourseStartLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestBadCourseStartLandingPoints')}" tabIndex="17"/>
	                                <g:if test="${resultclassInstance.flightTestBadCourseStartLandingPoints != resultclassInstance.contestRule.ruleValues.flightTestBadCourseStartLandingPoints}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.landingtolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestLandingToLatePoints" name="flightTestLandingToLatePoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestLandingToLatePoints')}" tabIndex="18"/>
	                                <g:if test="${resultclassInstance.flightTestLandingToLatePoints != resultclassInstance.contestRule.ruleValues.flightTestLandingToLatePoints}">!</g:if>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.giventolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestGivenToLatePoints" name="flightTestGivenToLatePoints" value="${fieldValue(bean:resultclassInstance,field:'flightTestGivenToLatePoints')}" tabIndex="19"/>
	                                <g:if test="${resultclassInstance.flightTestGivenToLatePoints != resultclassInstance.contestRule.ruleValues.flightTestGivenToLatePoints}">!</g:if>
	                            </p>
	                        </fieldset>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.landingtest.landing1')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></legend>
                            <p>
                                <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1MaxPoints" name="landingTest1MaxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1MaxPoints')}" tabIndex="20"/>
                                <g:if test="${resultclassInstance.landingTest1MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest1MaxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1NoLandingPoints" name="landingTest1NoLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1NoLandingPoints')}" tabIndex="21"/>
                                <g:if test="${resultclassInstance.landingTest1NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest1NoLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1OutsideLandingPoints" name="landingTest1OutsideLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1OutsideLandingPoints')}" tabIndex="22"/>
                                <g:if test="${resultclassInstance.landingTest1OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest1OutsideLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1RollingOutsidePoints" name="landingTest1RollingOutsidePoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1RollingOutsidePoints')}" tabIndex="23"/>
                                <g:if test="${resultclassInstance.landingTest1RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest1RollingOutsidePoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1PowerInBoxPoints" name="landingTest1PowerInBoxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1PowerInBoxPoints')}" tabIndex="24"/>
                                <g:if test="${resultclassInstance.landingTest1PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest1PowerInBoxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1GoAroundWithoutTouchingPoints" name="landingTest1GoAroundWithoutTouchingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1GoAroundWithoutTouchingPoints')}" tabIndex="25"/>
                                <g:if test="${resultclassInstance.landingTest1GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest1GoAroundWithoutTouchingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1GoAroundInsteadStopPoints" name="landingTest1GoAroundInsteadStopPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1GoAroundInsteadStopPoints')}" tabIndex="26"/>
                                <g:if test="${resultclassInstance.landingTest1GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest1GoAroundInsteadStopPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest1AbnormalLandingPoints" name="landingTest1AbnormalLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest1AbnormalLandingPoints')}" tabIndex="27"/>
                                <g:if test="${resultclassInstance.landingTest1AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest1AbnormalLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>
                                <br/>
                                <input type="text" id="landingTest1PenaltyCalculator" name="landingTest1PenaltyCalculator" value="${fieldValue(bean:resultclassInstance,field:'landingTest1PenaltyCalculator')}" tabIndex="28"/>
                                <g:if test="${resultclassInstance.landingTest1PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest1PenaltyCalculator}">!</g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.landingtest.landing2')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></legend>
                            <p>
                                <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2MaxPoints" name="landingTest2MaxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2MaxPoints')}" tabIndex="29"/>
                                <g:if test="${resultclassInstance.landingTest2MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest2MaxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2NoLandingPoints" name="landingTest2NoLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2NoLandingPoints')}" tabIndex="30"/>
                                <g:if test="${resultclassInstance.landingTest2NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest2NoLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2OutsideLandingPoints" name="landingTest2OutsideLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2OutsideLandingPoints')}" tabIndex="31"/>
                                <g:if test="${resultclassInstance.landingTest2OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest2OutsideLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2RollingOutsidePoints" name="landingTest2RollingOutsidePoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2RollingOutsidePoints')}" tabIndex="32"/>
                                <g:if test="${resultclassInstance.landingTest2RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest2RollingOutsidePoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2PowerInBoxPoints" name="landingTest2PowerInBoxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2PowerInBoxPoints')}" tabIndex="33"/>
                                <g:if test="${resultclassInstance.landingTest2PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest2PowerInBoxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2GoAroundWithoutTouchingPoints" name="landingTest2GoAroundWithoutTouchingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2GoAroundWithoutTouchingPoints')}" tabIndex="34"/>
                                <g:if test="${resultclassInstance.landingTest2GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest2GoAroundWithoutTouchingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2GoAroundInsteadStopPoints" name="landingTest2GoAroundInsteadStopPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2GoAroundInsteadStopPoints')}" tabIndex="35"/>
                                <g:if test="${resultclassInstance.landingTest2GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest2GoAroundInsteadStopPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest2AbnormalLandingPoints" name="landingTest2AbnormalLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2AbnormalLandingPoints')}" tabIndex="36"/>
                                <g:if test="${resultclassInstance.landingTest2AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest2AbnormalLandingPoints}">!</g:if>
                            </p>
                            <g:if test="${resultclassInstance?.contestRule.precisionFlying}">
	                            <p>
	                                <label>${message(code:'fc.landingtest.powerinair')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="landingTest2PowerInAirPoints" name="landingTest2PowerInAirPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest2PowerInAirPoints')}" tabIndex="37"/>
	                                <g:if test="${resultclassInstance.landingTest2PowerInAirPoints != resultclassInstance.contestRule.ruleValues.landingTest2PowerInAirPoints}">!</g:if>
	                            </p>
	                        </g:if>
                            <p>
                                <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>
                                <br/>
                                <input type="text" id="landingTest2PenaltyCalculator" name="landingTest2PenaltyCalculator" value="${fieldValue(bean:resultclassInstance,field:'landingTest2PenaltyCalculator')}" tabIndex="38"/>
                                <g:if test="${resultclassInstance.landingTest2PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest2PenaltyCalculator}">!</g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.landingtest.landing3')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></legend>
                            <p>
                                <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3MaxPoints" name="landingTest3MaxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3MaxPoints')}" tabIndex="39"/>
                                <g:if test="${resultclassInstance.landingTest3MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest3MaxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3NoLandingPoints" name="landingTest3NoLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3NoLandingPoints')}" tabIndex="40"/>
                                <g:if test="${resultclassInstance.landingTest3NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest3NoLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3OutsideLandingPoints" name="landingTest3OutsideLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3OutsideLandingPoints')}" tabIndex="41"/>
                                <g:if test="${resultclassInstance.landingTest3OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest3OutsideLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3RollingOutsidePoints" name="landingTest3RollingOutsidePoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3RollingOutsidePoints')}" tabIndex="42"/>
                                <g:if test="${resultclassInstance.landingTest3RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest3RollingOutsidePoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3PowerInBoxPoints" name="landingTest3PowerInBoxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3PowerInBoxPoints')}" tabIndex="43"/>
                                <g:if test="${resultclassInstance.landingTest3PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest3PowerInBoxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3GoAroundWithoutTouchingPoints" name="landingTest3GoAroundWithoutTouchingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3GoAroundWithoutTouchingPoints')}" tabIndex="44"/>
                                <g:if test="${resultclassInstance.landingTest3GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest3GoAroundWithoutTouchingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3GoAroundInsteadStopPoints" name="landingTest3GoAroundInsteadStopPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3GoAroundInsteadStopPoints')}" tabIndex="45"/>
                                <g:if test="${resultclassInstance.landingTest3GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest3GoAroundInsteadStopPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest3AbnormalLandingPoints" name="landingTest3AbnormalLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3AbnormalLandingPoints')}" tabIndex="46"/>
                                <g:if test="${resultclassInstance.landingTest3AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest3AbnormalLandingPoints}">!</g:if>
                            </p>
                            <g:if test="${resultclassInstance?.contestRule.precisionFlying}">
	                            <p>
	                                <label>${message(code:'fc.landingtest.powerinair')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="landingTest3PowerInAirPoints" name="landingTest3PowerInAirPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3PowerInAirPoints')}" tabIndex="47"/>
	                                <g:if test="${resultclassInstance.landingTest3PowerInAirPoints != resultclassInstance.contestRule.ruleValues.landingTest3PowerInAirPoints}">!</g:if>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.landingtest.flapsinair')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="landingTest3FlapsInAirPoints" name="landingTest3FlapsInAirPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest3FlapsInAirPoints')}" tabIndex="48"/>
	                                <g:if test="${resultclassInstance.landingTest3FlapsInAirPoints != resultclassInstance.contestRule.ruleValues.landingTest3FlapsInAirPoints}">!</g:if>
	                            </p>
	                        </g:if>
                            <p>
                                <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>
                                <br/>
                                <input type="text" id="landingTest3PenaltyCalculator" name="landingTest3PenaltyCalculator" value="${fieldValue(bean:resultclassInstance,field:'landingTest3PenaltyCalculator')}" tabIndex="49"/>
                                <g:if test="${resultclassInstance.landingTest3PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest3PenaltyCalculator}">!</g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.landingtest.landing4')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></legend>
                            <p>
                                <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4MaxPoints" name="landingTest4MaxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4MaxPoints')}" tabIndex="50"/>
                                <g:if test="${resultclassInstance.landingTest4MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest4MaxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4NoLandingPoints" name="landingTest4NoLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4NoLandingPoints')}" tabIndex="51"/>
                                <g:if test="${resultclassInstance.landingTest4NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest4NoLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4OutsideLandingPoints" name="landingTest4OutsideLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4OutsideLandingPoints')}" tabIndex="52"/>
                                <g:if test="${resultclassInstance.landingTest4OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest4OutsideLandingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4RollingOutsidePoints" name="landingTest4RollingOutsidePoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4RollingOutsidePoints')}" tabIndex="53"/>
                                <g:if test="${resultclassInstance.landingTest4RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest4RollingOutsidePoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4PowerInBoxPoints" name="landingTest4PowerInBoxPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4PowerInBoxPoints')}" tabIndex="54"/>
                                <g:if test="${resultclassInstance.landingTest4PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest4PowerInBoxPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4GoAroundWithoutTouchingPoints" name="landingTest4GoAroundWithoutTouchingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4GoAroundWithoutTouchingPoints')}" tabIndex="55"/>
                                <g:if test="${resultclassInstance.landingTest4GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest4GoAroundWithoutTouchingPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4GoAroundInsteadStopPoints" name="landingTest4GoAroundInsteadStopPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4GoAroundInsteadStopPoints')}" tabIndex="56"/>
                                <g:if test="${resultclassInstance.landingTest4GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest4GoAroundInsteadStopPoints}">!</g:if>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTest4AbnormalLandingPoints" name="landingTest4AbnormalLandingPoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4AbnormalLandingPoints')}" tabIndex="57"/>
                                <g:if test="${resultclassInstance.landingTest4AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest4AbnormalLandingPoints}">!</g:if>
                            </p>
							<g:if test="${resultclassInstance?.contestRule.precisionFlying}">                            
								<p>
	                                <label>${message(code:'fc.landingtest.touchingobstacle')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="landingTest4TouchingObstaclePoints" name="landingTest4TouchingObstaclePoints" value="${fieldValue(bean:resultclassInstance,field:'landingTest4TouchingObstaclePoints')}" tabIndex="58"/>
	                                <g:if test="${resultclassInstance.landingTest4TouchingObstaclePoints != resultclassInstance.contestRule.ruleValues.landingTest4TouchingObstaclePoints}">!</g:if>
	                            </p>
	                        </g:if>
                            <p>
                                <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>
                                <br/>
                                <input type="text" id="landingTest4PenaltyCalculator" name="landingTest4PenaltyCalculator" value="${fieldValue(bean:resultclassInstance,field:'landingTest4PenaltyCalculator')}" tabIndex="59"/>
                                <g:if test="${resultclassInstance.landingTest4PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest4PenaltyCalculator}">!</g:if>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
                        <input type="hidden" name="pointssaved" value="${true}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="60"/>
                        <g:actionSubmit action="savepoints" value="${message(code:'fc.save')}" tabIndex="61"/>
                        <g:actionSubmit action="printpoints" value="${message(code:'fc.print')}"  tabIndex="63"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="64"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>