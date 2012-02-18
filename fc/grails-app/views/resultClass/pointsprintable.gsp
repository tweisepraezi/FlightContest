<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${message(code:resultclassInstance.contestRule.titleCode)} - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${resultclassInstance.GetPrintTitle('fc.contestrule.classpoints')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
            	<h3>${message(code:resultclassInstance.contestRule.titleCode)}</h3>
                <div class="block" id="forms" >
                    <g:form>
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
	                            	<tr>
	                            		<th colspan="2">${message(code:'fc.planningtest')}</th>
	                            		<th style="color:white">.</th>
	                            	</tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.directioncorrectgrad')}</td>
		                                <td>${resultclassInstance.planningTestDirectionCorrectGrad}${message(code:'fc.grad')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestDirectionCorrectGrad != resultclassInstance.contestRule.ruleValues.planningTestDirectionCorrectGrad}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.directionpointspergrad')}</td>
		                                <td>${resultclassInstance.planningTestDirectionPointsPerGrad} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestDirectionPointsPerGrad != resultclassInstance.contestRule.ruleValues.planningTestDirectionPointsPerGrad}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.timecorrectsecond')}</td>
		                                <td>${resultclassInstance.planningTestTimeCorrectSecond}${message(code:'fc.time.s')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestTimeCorrectSecond != resultclassInstance.contestRule.ruleValues.planningTestTimeCorrectSecond}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.timepointspersecond')}</td>
		                                <td>${resultclassInstance.planningTestTimePointsPerSecond} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestTimePointsPerSecond != resultclassInstance.contestRule.ruleValues.planningTestTimePointsPerSecond}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.maxpoints')}</td>
		                                <td>${resultclassInstance.planningTestMaxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestMaxPoints != resultclassInstance.contestRule.ruleValues.planningTestMaxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.giventolate')}</td>
		                                <td>${resultclassInstance.planningTestPlanTooLatePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestPlanTooLatePoints != resultclassInstance.contestRule.ruleValues.planningTestPlanTooLatePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.planningtest.exitroomtolate')}</td>
		                                <td>${resultclassInstance.planningTestExitRoomTooLatePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.planningTestExitRoomTooLatePoints != resultclassInstance.contestRule.ruleValues.planningTestExitRoomTooLatePoints}">!</g:if></td>
		                            </tr>
	                            </tbody>
	                        </table>
	                    </div>
	                    <br/>        
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
		                            <tr>
		                            	<th colspan="2">${message(code:'fc.flighttest')}</th>
	                            		<th style="color:white">.</th>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.takeoffmissed')}</td>
		                                <td>${resultclassInstance.flightTestTakeoffMissedPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestTakeoffMissedPoints != resultclassInstance.contestRule.ruleValues.flightTestTakeoffMissedPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.cptimecorrectsecond')}</td>
		                                <td>${resultclassInstance.flightTestCptimeCorrectSecond}${message(code:'fc.time.s')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestCptimeCorrectSecond != resultclassInstance.contestRule.ruleValues.flightTestCptimeCorrectSecond}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.cptimepointspersecond')}</td>
		                                <td>${resultclassInstance.flightTestCptimePointsPerSecond} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestCptimePointsPerSecond != resultclassInstance.contestRule.ruleValues.flightTestCptimePointsPerSecond}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.cptimemaxpoints')}</td>
		                                <td>${resultclassInstance.flightTestCptimeMaxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestCptimeMaxPoints != resultclassInstance.contestRule.ruleValues.flightTestCptimeMaxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.cpnotfound')}</td>
		                                <td>${resultclassInstance.flightTestCpNotFoundPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestCpNotFoundPoints != resultclassInstance.contestRule.ruleValues.flightTestCpNotFoundPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.procedureturnnotflown')}</td>
		                                <td>${resultclassInstance.flightTestProcedureTurnNotFlownPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestProcedureTurnNotFlownPoints != resultclassInstance.contestRule.ruleValues.flightTestProcedureTurnNotFlownPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.minaltitudemissed')}</td>
		                                <td>${resultclassInstance.flightTestMinAltitudeMissedPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestMinAltitudeMissedPoints != resultclassInstance.contestRule.ruleValues.flightTestMinAltitudeMissedPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.badcoursecorrectsecond')}</td>
		                                <td>${resultclassInstance.flightTestBadCourseCorrectSecond}${message(code:'fc.time.s')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestBadCourseCorrectSecond != resultclassInstance.contestRule.ruleValues.flightTestBadCourseCorrectSecond}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.badcourse')}</td>
		                                <td>${resultclassInstance.flightTestBadCoursePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestBadCoursePoints != resultclassInstance.contestRule.ruleValues.flightTestBadCoursePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.badcoursestartlanding')}</td>
		                                <td>${resultclassInstance.flightTestBadCourseStartLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestBadCourseStartLandingPoints != resultclassInstance.contestRule.ruleValues.flightTestBadCourseStartLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.landingtolate')}</td>
		                                <td>${resultclassInstance.flightTestLandingToLatePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestLandingToLatePoints != resultclassInstance.contestRule.ruleValues.flightTestLandingToLatePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.flighttest.giventolate')}</td>
		                                <td>${resultclassInstance.flightTestGivenToLatePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.flightTestGivenToLatePoints != resultclassInstance.contestRule.ruleValues.flightTestGivenToLatePoints}">!</g:if></td>
		                            </tr>
	                            </tbody>
	                        </table>
	                    </div>
	                    <br/>        
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
		                            <tr>
		                            	<th colspan="2">${message(code:'fc.landingtest.landing1')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing1.precision')})</g:if></th>
	                            		<th style="color:white">.</th>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.maxpoints')}</td>
		                                <td>${resultclassInstance.landingTest1MaxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest1MaxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.nolanding')}</td>
		                                <td>${resultclassInstance.landingTest1NoLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest1NoLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.outsidelanding')}</td>
		                                <td>${resultclassInstance.landingTest1OutsideLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest1OutsideLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.rollingoutside')}</td>
		                                <td>${resultclassInstance.landingTest1RollingOutsidePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest1RollingOutsidePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.powerinbox')}</td>
		                                <td>${resultclassInstance.landingTest1PowerInBoxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest1PowerInBoxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>
		                                <td>${resultclassInstance.landingTest1GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest1GoAroundWithoutTouchingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>
		                                <td>${resultclassInstance.landingTest1GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest1GoAroundInsteadStopPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.abnormallanding')}</td>
		                                <td>${resultclassInstance.landingTest1AbnormalLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest1AbnormalLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td colspan="2">${message(code:'fc.landingtest.penaltycalculator')}:<br/>${resultclassInstance.landingTest1PenaltyCalculator}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest1PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest1PenaltyCalculator}">!</g:if></td>
		                            </tr>
	                            </tbody>
	                        </table>
	                    </div>
	                    <br/>        
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
		                            <tr>
		                            	<th colspan="2">${message(code:'fc.landingtest.landing2')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing2.precision')})</g:if></th>
	                            		<th style="color:white">.</th>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.maxpoints')}</td>
		                                <td>${resultclassInstance.landingTest2MaxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest2MaxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.nolanding')}</td>
		                                <td>${resultclassInstance.landingTest2NoLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest2NoLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.outsidelanding')}</td>
		                                <td>${resultclassInstance.landingTest2OutsideLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest2OutsideLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.rollingoutside')}</td>
		                                <td>${resultclassInstance.landingTest2RollingOutsidePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest2RollingOutsidePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.powerinbox')}</td>
		                                <td>${resultclassInstance.landingTest2PowerInBoxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest2PowerInBoxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>
		                                <td>${resultclassInstance.landingTest2GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest2GoAroundWithoutTouchingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>
		                                <td>${resultclassInstance.landingTest2GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest2GoAroundInsteadStopPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.abnormallanding')}</td>
		                                <td>${resultclassInstance.landingTest2AbnormalLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest2AbnormalLandingPoints}">!</g:if></td>
		                            </tr>
		                            <g:if test="${resultclassInstance?.contestRule.precisionFlying}">
			                            <tr>
		                                    <td width="70%">${message(code:'fc.landingtest.powerinair')}</td>
			                                <td>${resultclassInstance.landingTest2PowerInAirPoints} ${message(code:'fc.points')}</td>
			                                <td width="1%"><g:if test="${resultclassInstance.landingTest2PowerInAirPoints != resultclassInstance.contestRule.ruleValues.landingTest2PowerInAirPoints}">!</g:if></td>
			                            </tr>
		                            </g:if>
		                            <tr>
	                                    <td colspan="2">${message(code:'fc.landingtest.penaltycalculator')}:<br/>${resultclassInstance.landingTest2PenaltyCalculator}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest2PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest2PenaltyCalculator}">!</g:if></td>
		                            </tr>
	                            </tbody>
	                        </table>
	                    </div>
	                    <br/>        
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
		                            <tr>
		                            	<th colspan="2">${message(code:'fc.landingtest.landing3')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing3.precision')})</g:if></th>
	                            		<th style="color:white">.</th>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.maxpoints')}</td>
		                                <td>${resultclassInstance.landingTest3MaxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest3MaxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.nolanding')}</td>
		                                <td>${resultclassInstance.landingTest3NoLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest3NoLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.outsidelanding')}</td>
		                                <td>${resultclassInstance.landingTest3OutsideLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest3OutsideLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.rollingoutside')}</td>
		                                <td>${resultclassInstance.landingTest3RollingOutsidePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest3RollingOutsidePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.powerinbox')}</td>
		                                <td>${resultclassInstance.landingTest3PowerInBoxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest3PowerInBoxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>
		                                <td>${resultclassInstance.landingTest3GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest3GoAroundWithoutTouchingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>
		                                <td>${resultclassInstance.landingTest3GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest3GoAroundInsteadStopPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.abnormallanding')}</td>
		                                <td>${resultclassInstance.landingTest3AbnormalLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest3AbnormalLandingPoints}">!</g:if></td>
		                            </tr>
		                            <g:if test="${resultclassInstance?.contestRule.precisionFlying}">
			                            <tr>
		                                    <td width="70%">${message(code:'fc.landingtest.powerinair')}</td>
			                                <td>${resultclassInstance.landingTest3PowerInAirPoints} ${message(code:'fc.points')}</td>
			                                <td width="1%"><g:if test="${resultclassInstance.landingTest3PowerInAirPoints != resultclassInstance.contestRule.ruleValues.landingTest3PowerInAirPoints}">!</g:if></td>
			                            </tr>
			                            <tr>
		                                    <td width="70%">${message(code:'fc.landingtest.flapsinair')}</td>
			                                <td>${resultclassInstance.landingTest3FlapsInAirPoints} ${message(code:'fc.points')}</td>
			                                <td width="1%"><g:if test="${resultclassInstance.landingTest3FlapsInAirPoints != resultclassInstance.contestRule.ruleValues.landingTest3FlapsInAirPoints}">!</g:if></td>
			                            </tr>
			                        </g:if>
		                            <tr>
	                                    <td colspan="2">${message(code:'fc.landingtest.penaltycalculator')}:<br/>${resultclassInstance.landingTest3PenaltyCalculator}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest3PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest3PenaltyCalculator}">!</g:if></td>
		                            </tr>
	                            </tbody>
	                        </table>
	                    </div>
	                    <br/>        
                       	<div style="page-break-inside:avoid">
	                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                            <tbody>
		                            <tr>
		                            	<th colspan="2">${message(code:'fc.landingtest.landing4')}<g:if test="${resultclassInstance?.contestRule.precisionFlying}"> (${message(code:'fc.landingtest.landing4.precision')})</g:if></th>
	                            		<th style="color:white">.</th>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.maxpoints')}</td>
		                                <td>${resultclassInstance.landingTest4MaxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4MaxPoints != resultclassInstance.contestRule.ruleValues.landingTest4MaxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.nolanding')}</td>
		                                <td>${resultclassInstance.landingTest4NoLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4NoLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest4NoLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.outsidelanding')}</td>
		                                <td>${resultclassInstance.landingTest4OutsideLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4OutsideLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest4OutsideLandingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.rollingoutside')}</td>
		                                <td>${resultclassInstance.landingTest4RollingOutsidePoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4RollingOutsidePoints != resultclassInstance.contestRule.ruleValues.landingTest4RollingOutsidePoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.powerinbox')}</td>
		                                <td>${resultclassInstance.landingTest4PowerInBoxPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4PowerInBoxPoints != resultclassInstance.contestRule.ruleValues.landingTest4PowerInBoxPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>
		                                <td>${resultclassInstance.landingTest4GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4GoAroundWithoutTouchingPoints != resultclassInstance.contestRule.ruleValues.landingTest4GoAroundWithoutTouchingPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>
		                                <td>${resultclassInstance.landingTest4GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4GoAroundInsteadStopPoints != resultclassInstance.contestRule.ruleValues.landingTest4GoAroundInsteadStopPoints}">!</g:if></td>
		                            </tr>
		                            <tr>
	                                    <td width="70%">${message(code:'fc.landingtest.abnormallanding')}</td>
		                                <td>${resultclassInstance.landingTest4AbnormalLandingPoints} ${message(code:'fc.points')}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4AbnormalLandingPoints != resultclassInstance.contestRule.ruleValues.landingTest4AbnormalLandingPoints}">!</g:if></td>
		                            </tr>
		                            <g:if test="${resultclassInstance?.contestRule.precisionFlying}">
			                            <tr>
		                                    <td width="70%">${message(code:'fc.landingtest.touchingobstacle')}</td>
			                                <td>${resultclassInstance.landingTest4TouchingObstaclePoints} ${message(code:'fc.points')}</td>
			                                <td width="1%"><g:if test="${resultclassInstance.landingTest4TouchingObstaclePoints != resultclassInstance.contestRule.ruleValues.landingTest4TouchingObstaclePoints}">!</g:if></td>
			                            </tr>
			                        </g:if>
		                            <tr>
	                                    <td colspan="2">${message(code:'fc.landingtest.penaltycalculator')}:<br/>${resultclassInstance.landingTest4PenaltyCalculator}</td>
		                                <td width="1%"><g:if test="${resultclassInstance.landingTest4PenaltyCalculator != resultclassInstance.contestRule.ruleValues.landingTest4PenaltyCalculator}">!</g:if></td>
		                            </tr>
	                            </tbody>
	                        </table>
	                    </div>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>