class PointsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def editPoints = { attrs, body ->
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:attrs.i.contestRule.titleCode)}</td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
        outln"""<p class="warning">${attrs.recalculatepoints}</p>"""
        outln"""<fieldset>"""
        outln"""    <legend>${message(code:'fc.general')}</legend>"""
        outln"""    <p/>"""
        outln"""    <div>"""
        checkBox("precisionFlying", attrs.i.precisionFlying, 'fc.general.precisionflyinglanding', attrs)
        if (attrs.i.precisionFlying != attrs.i.contestRule.ruleValues.precisionFlying) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.general.increasefactor')}* [${message(code:'fc.percent')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="increaseFactor" name="increaseFactor" value="${fieldValue(bean:attrs.i,field:'increaseFactor')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.increaseFactor != attrs.i.contestRule.ruleValues.increaseFactor) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        outln"""    <legend>${message(code:'fc.planningtest')}</legend>"""
        outln"""    <p/>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.directioncorrectgrad')}* [${message(code:'fc.grad')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestDirectionCorrectGrad" name="planningTestDirectionCorrectGrad" value="${fieldValue(bean:attrs.i,field:'planningTestDirectionCorrectGrad')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestDirectionCorrectGrad != attrs.i.contestRule.ruleValues.planningTestDirectionCorrectGrad) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.directionpointspergrad')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestDirectionPointsPerGrad" name="planningTestDirectionPointsPerGrad" value="${fieldValue(bean:attrs.i,field:'planningTestDirectionPointsPerGrad')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestDirectionPointsPerGrad != attrs.i.contestRule.ruleValues.planningTestDirectionPointsPerGrad) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.timecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestTimeCorrectSecond" name="planningTestTimeCorrectSecond" value="${fieldValue(bean:attrs.i,field:'planningTestTimeCorrectSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestTimeCorrectSecond != attrs.i.contestRule.ruleValues.planningTestTimeCorrectSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.timepointspersecond')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestTimePointsPerSecond" name="planningTestTimePointsPerSecond" value="${fieldValue(bean:attrs.i,field:'planningTestTimePointsPerSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestTimePointsPerSecond != attrs.i.contestRule.ruleValues.planningTestTimePointsPerSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.maxpoints')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestMaxPoints" name="planningTestMaxPoints" value="${fieldValue(bean:attrs.i,field:'planningTestMaxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestMaxPoints != attrs.i.contestRule.ruleValues.planningTestMaxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.giventolate')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestPlanTooLatePoints" name="planningTestPlanTooLatePoints" value="${fieldValue(bean:attrs.i,field:'planningTestPlanTooLatePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestPlanTooLatePoints != attrs.i.contestRule.ruleValues.planningTestPlanTooLatePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.exitroomtolate')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestExitRoomTooLatePoints" name="planningTestExitRoomTooLatePoints" value="${fieldValue(bean:attrs.i,field:'planningTestExitRoomTooLatePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestExitRoomTooLatePoints != attrs.i.contestRule.ruleValues.planningTestExitRoomTooLatePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.planningtest.forbiddencalculators')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="planningTestForbiddenCalculatorsPoints" name="planningTestForbiddenCalculatorsPoints" value="${fieldValue(bean:attrs.i,field:'planningTestForbiddenCalculatorsPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.planningTestForbiddenCalculatorsPoints != attrs.i.contestRule.ruleValues.planningTestForbiddenCalculatorsPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        outln"""    <legend>${message(code:'fc.flighttest')}</legend>"""
        outln"""    <p/>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.takeoffcorrectsecond')}* [${message(code:'fc.time.s')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestTakeoffCorrectSecond" name="flightTestTakeoffCorrectSecond" value="${fieldValue(bean:attrs.i,field:'flightTestTakeoffCorrectSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestTakeoffCorrectSecond != attrs.i.contestRule.ruleValues.flightTestTakeoffCorrectSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <div>"""
        checkBox("flightTestTakeoffCheckSeconds", attrs.i.flightTestTakeoffCheckSeconds, 'fc.flighttest.takeoffcheckpoints', attrs)
        if (attrs.i.flightTestTakeoffCheckSeconds != attrs.i.contestRule.ruleValues.flightTestTakeoffCheckSeconds) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </div>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.takeoffpointspersecond')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestTakeoffPointsPerSecond" name="flightTestTakeoffPointsPerSecond" value="${fieldValue(bean:attrs.i,field:'flightTestTakeoffPointsPerSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestTakeoffPointsPerSecond != attrs.i.contestRule.ruleValues.flightTestTakeoffPointsPerSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.takeoffmissed')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestTakeoffMissedPoints" name="flightTestTakeoffMissedPoints" value="${fieldValue(bean:attrs.i,field:'flightTestTakeoffMissedPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestTakeoffMissedPoints != attrs.i.contestRule.ruleValues.flightTestTakeoffMissedPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.cptimecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestCptimeCorrectSecond" name="flightTestCptimeCorrectSecond" value="${fieldValue(bean:attrs.i,field:'flightTestCptimeCorrectSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestCptimeCorrectSecond != attrs.i.contestRule.ruleValues.flightTestCptimeCorrectSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.cptimepointspersecond')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestCptimePointsPerSecond" name="flightTestCptimePointsPerSecond" value="${fieldValue(bean:attrs.i,field:'flightTestCptimePointsPerSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestCptimePointsPerSecond != attrs.i.contestRule.ruleValues.flightTestCptimePointsPerSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.cptimemaxpoints')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestCptimeMaxPoints" name="flightTestCptimeMaxPoints" value="${fieldValue(bean:attrs.i,field:'flightTestCptimeMaxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestCptimeMaxPoints != attrs.i.contestRule.ruleValues.flightTestCptimeMaxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.cpnotfound')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestCpNotFoundPoints" name="flightTestCpNotFoundPoints" value="${fieldValue(bean:attrs.i,field:'flightTestCpNotFoundPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestCpNotFoundPoints != attrs.i.contestRule.ruleValues.flightTestCpNotFoundPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.procedureturnnotflown')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestProcedureTurnNotFlownPoints" name="flightTestProcedureTurnNotFlownPoints" value="${fieldValue(bean:attrs.i,field:'flightTestProcedureTurnNotFlownPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestProcedureTurnNotFlownPoints != attrs.i.contestRule.ruleValues.flightTestProcedureTurnNotFlownPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.minaltitudemissed')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestMinAltitudeMissedPoints" name="flightTestMinAltitudeMissedPoints" value="${fieldValue(bean:attrs.i,field:'flightTestMinAltitudeMissedPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestMinAltitudeMissedPoints != attrs.i.contestRule.ruleValues.flightTestMinAltitudeMissedPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.badcoursecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestBadCourseCorrectSecond" name="flightTestBadCourseCorrectSecond" value="${fieldValue(bean:attrs.i,field:'flightTestBadCourseCorrectSecond')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestBadCourseCorrectSecond != attrs.i.contestRule.ruleValues.flightTestBadCourseCorrectSecond) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.badcourse')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestBadCoursePoints" name="flightTestBadCoursePoints" value="${fieldValue(bean:attrs.i,field:'flightTestBadCoursePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestBadCoursePoints != attrs.i.contestRule.ruleValues.flightTestBadCoursePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.badcoursestartlanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestBadCourseStartLandingPoints" name="flightTestBadCourseStartLandingPoints" value="${fieldValue(bean:attrs.i,field:'flightTestBadCourseStartLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestBadCourseStartLandingPoints != attrs.i.contestRule.ruleValues.flightTestBadCourseStartLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.landingtolate')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestLandingToLatePoints" name="flightTestLandingToLatePoints" value="${fieldValue(bean:attrs.i,field:'flightTestLandingToLatePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestLandingToLatePoints != attrs.i.contestRule.ruleValues.flightTestLandingToLatePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""    <fieldset>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.giventolate')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestGivenToLatePoints" name="flightTestGivenToLatePoints" value="${fieldValue(bean:attrs.i,field:'flightTestGivenToLatePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestGivenToLatePoints != attrs.i.contestRule.ruleValues.flightTestGivenToLatePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.safetyandrulesinfringement')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestSafetyAndRulesInfringementPoints" name="flightTestSafetyAndRulesInfringementPoints" value="${fieldValue(bean:attrs.i,field:'flightTestSafetyAndRulesInfringementPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestSafetyAndRulesInfringementPoints != attrs.i.contestRule.ruleValues.flightTestSafetyAndRulesInfringementPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.instructionsnotfollowed')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestInstructionsNotFollowedPoints" name="flightTestInstructionsNotFollowedPoints" value="${fieldValue(bean:attrs.i,field:'flightTestInstructionsNotFollowedPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestInstructionsNotFollowedPoints != attrs.i.contestRule.ruleValues.flightTestInstructionsNotFollowedPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.falseenvelopeopened')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestFalseEnvelopeOpenedPoints" name="flightTestFalseEnvelopeOpenedPoints" value="${fieldValue(bean:attrs.i,field:'flightTestFalseEnvelopeOpenedPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestFalseEnvelopeOpenedPoints != attrs.i.contestRule.ruleValues.flightTestFalseEnvelopeOpenedPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.safetyenvelopeopened')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestSafetyEnvelopeOpenedPoints" name="flightTestSafetyEnvelopeOpenedPoints" value="${fieldValue(bean:attrs.i,field:'flightTestSafetyEnvelopeOpenedPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestSafetyEnvelopeOpenedPoints != attrs.i.contestRule.ruleValues.flightTestSafetyEnvelopeOpenedPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.frequencynotmonitored')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestFrequencyNotMonitoredPoints" name="flightTestFrequencyNotMonitoredPoints" value="${fieldValue(bean:attrs.i,field:'flightTestFrequencyNotMonitoredPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestFrequencyNotMonitoredPoints != attrs.i.contestRule.ruleValues.flightTestFrequencyNotMonitoredPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""        <p>"""
        outln"""            <label>${message(code:'fc.flighttest.forbiddenequipment')}* [${message(code:'fc.points')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestForbiddenEquipmentPoints" name="flightTestForbiddenEquipmentPoints" value="${fieldValue(bean:attrs.i,field:'flightTestForbiddenEquipmentPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.flightTestForbiddenEquipmentPoints != attrs.i.contestRule.ruleValues.flightTestForbiddenEquipmentPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </fieldset>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        outln"""    <legend>${message(code:'fc.observationtest')}</legend>"""
        outln"""    <p/>"""
        outln"""    <p class="group">${message(code:'fc.observationtest.turnpoint')}</p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.turnpointnotfound')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestTurnpointNotFoundPoints" name="observationTestTurnpointNotFoundPoints" value="${fieldValue(bean:attrs.i,field:'observationTestTurnpointNotFoundPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestTurnpointNotFoundPoints != attrs.i.contestRule.ruleValues.observationTestTurnpointNotFoundPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.turnpointfalse')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestTurnpointFalsePoints" name="observationTestTurnpointFalsePoints" value="${fieldValue(bean:attrs.i,field:'observationTestTurnpointFalsePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestTurnpointFalsePoints != attrs.i.contestRule.ruleValues.observationTestTurnpointFalsePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p class="group">${message(code:'fc.observationtest.enroute')}</p>"""

        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observationtest.enroutevalueunit')}:</label>"""
        outln"""        <br/>"""
        for (def v in EnrouteValueUnit.values()) {
            radioEntry("observationTestEnrouteValueUnit", v, attrs.i.observationTestEnrouteValueUnit == v, message(code:v.code), attrs)
        }
        attrs.ti[0]++
        if (attrs.i.observationTestEnrouteValueUnit != attrs.i.contestRule.ruleValues.observationTestEnrouteValueUnit) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <p/>"""

        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.enroutecorrectvalue')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestEnrouteCorrectValue" name="observationTestEnrouteCorrectValue" value="${fieldValue(bean:attrs.i,field:'observationTestEnrouteCorrectValue')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestEnrouteCorrectValue != attrs.i.contestRule.ruleValues.observationTestEnrouteCorrectValue) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.enrouteinexactvalue')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestEnrouteInexactValue" name="observationTestEnrouteInexactValue" value="${fieldValue(bean:attrs.i,field:'observationTestEnrouteInexactValue')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestEnrouteInexactValue != attrs.i.contestRule.ruleValues.observationTestEnrouteInexactValue) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.enrouteinexact')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestEnrouteInexactPoints" name="observationTestEnrouteInexactPoints" value="${fieldValue(bean:attrs.i,field:'observationTestEnrouteInexactPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestEnrouteInexactPoints != attrs.i.contestRule.ruleValues.observationTestEnrouteInexactPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.enroutenotfound')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestEnrouteNotFoundPoints" name="observationTestEnrouteNotFoundPoints" value="${fieldValue(bean:attrs.i,field:'observationTestEnrouteNotFoundPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestEnrouteNotFoundPoints != attrs.i.contestRule.ruleValues.observationTestEnrouteNotFoundPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observationtest.enroutefalse')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="observationTestEnrouteFalsePoints" name="observationTestEnrouteFalsePoints" value="${fieldValue(bean:attrs.i,field:'observationTestEnrouteFalsePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.observationTestEnrouteFalsePoints != attrs.i.contestRule.ruleValues.observationTestEnrouteFalsePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        if (attrs.i.precisionFlying) {
            outln"""<legend>${message(code:'fc.landingtest.landing1')} (${message(code:'fc.landingtest.landing1.precision')})</legend>"""
        } else {
            outln"""<legend>${message(code:'fc.landingtest.landing1')}</legend>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1MaxPoints" name="landingTest1MaxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1MaxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1MaxPoints != attrs.i.contestRule.ruleValues.landingTest1MaxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1NoLandingPoints" name="landingTest1NoLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1NoLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest1NoLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1OutsideLandingPoints" name="landingTest1OutsideLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1OutsideLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest1OutsideLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1RollingOutsidePoints" name="landingTest1RollingOutsidePoints" value="${fieldValue(bean:attrs.i,field:'landingTest1RollingOutsidePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest1RollingOutsidePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1PowerInBoxPoints" name="landingTest1PowerInBoxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1PowerInBoxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest1PowerInBoxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1GoAroundWithoutTouchingPoints" name="landingTest1GoAroundWithoutTouchingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1GoAroundWithoutTouchingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest1GoAroundWithoutTouchingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1GoAroundInsteadStopPoints" name="landingTest1GoAroundInsteadStopPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1GoAroundInsteadStopPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest1GoAroundInsteadStopPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1AbnormalLandingPoints" name="landingTest1AbnormalLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1AbnormalLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest1AbnormalLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1NotAllowedAerodynamicAuxiliariesPoints" name="landingTest1NotAllowedAerodynamicAuxiliariesPoints" value="${fieldValue(bean:attrs.i,field:'landingTest1NotAllowedAerodynamicAuxiliariesPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest1NotAllowedAerodynamicAuxiliariesPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest1PenaltyCalculator" name="landingTest1PenaltyCalculator" value="${fieldValue(bean:attrs.i,field:'landingTest1PenaltyCalculator')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest1PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest1PenaltyCalculator) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        if (attrs.i.precisionFlying) {
            outln"""<legend>${message(code:'fc.landingtest.landing2')} (${message(code:'fc.landingtest.landing2.precision')})</legend>"""
        } else {
            outln"""<legend>${message(code:'fc.landingtest.landing2')}</legend>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2MaxPoints" name="landingTest2MaxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2MaxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2MaxPoints != attrs.i.contestRule.ruleValues.landingTest2MaxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2NoLandingPoints" name="landingTest2NoLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2NoLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest2NoLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2OutsideLandingPoints" name="landingTest2OutsideLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2OutsideLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest2OutsideLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2RollingOutsidePoints" name="landingTest2RollingOutsidePoints" value="${fieldValue(bean:attrs.i,field:'landingTest2RollingOutsidePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest2RollingOutsidePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2PowerInBoxPoints" name="landingTest2PowerInBoxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2PowerInBoxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest2PowerInBoxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2GoAroundWithoutTouchingPoints" name="landingTest2GoAroundWithoutTouchingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2GoAroundWithoutTouchingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest2GoAroundWithoutTouchingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2GoAroundInsteadStopPoints" name="landingTest2GoAroundInsteadStopPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2GoAroundInsteadStopPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest2GoAroundInsteadStopPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2AbnormalLandingPoints" name="landingTest2AbnormalLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2AbnormalLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest2AbnormalLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2NotAllowedAerodynamicAuxiliariesPoints" name="landingTest2NotAllowedAerodynamicAuxiliariesPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2NotAllowedAerodynamicAuxiliariesPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest2NotAllowedAerodynamicAuxiliariesPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.powerinair')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2PowerInAirPoints" name="landingTest2PowerInAirPoints" value="${fieldValue(bean:attrs.i,field:'landingTest2PowerInAirPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2PowerInAirPoints != attrs.i.contestRule.ruleValues.landingTest2PowerInAirPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest2PenaltyCalculator" name="landingTest2PenaltyCalculator" value="${fieldValue(bean:attrs.i,field:'landingTest2PenaltyCalculator')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest2PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest2PenaltyCalculator) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        if (attrs.i.precisionFlying) {
            outln"""<legend>${message(code:'fc.landingtest.landing3')} (${message(code:'fc.landingtest.landing3.precision')})</legend>"""
        } else {
            outln"""<legend>${message(code:'fc.landingtest.landing3')}</legend>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3MaxPoints" name="landingTest3MaxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3MaxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3MaxPoints != attrs.i.contestRule.ruleValues.landingTest3MaxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3NoLandingPoints" name="landingTest3NoLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3NoLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest3NoLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3OutsideLandingPoints" name="landingTest3OutsideLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3OutsideLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest3OutsideLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3RollingOutsidePoints" name="landingTest3RollingOutsidePoints" value="${fieldValue(bean:attrs.i,field:'landingTest3RollingOutsidePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest3RollingOutsidePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3PowerInBoxPoints" name="landingTest3PowerInBoxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3PowerInBoxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest3PowerInBoxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3GoAroundWithoutTouchingPoints" name="landingTest3GoAroundWithoutTouchingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3GoAroundWithoutTouchingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest3GoAroundWithoutTouchingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3GoAroundInsteadStopPoints" name="landingTest3GoAroundInsteadStopPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3GoAroundInsteadStopPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest3GoAroundInsteadStopPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3AbnormalLandingPoints" name="landingTest3AbnormalLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3AbnormalLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest3AbnormalLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3NotAllowedAerodynamicAuxiliariesPoints" name="landingTest3NotAllowedAerodynamicAuxiliariesPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3NotAllowedAerodynamicAuxiliariesPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest3NotAllowedAerodynamicAuxiliariesPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.powerinair')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3PowerInAirPoints" name="landingTest3PowerInAirPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3PowerInAirPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3PowerInAirPoints != attrs.i.contestRule.ruleValues.landingTest3PowerInAirPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.flapsinair')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3FlapsInAirPoints" name="landingTest3FlapsInAirPoints" value="${fieldValue(bean:attrs.i,field:'landingTest3FlapsInAirPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3FlapsInAirPoints != attrs.i.contestRule.ruleValues.landingTest3FlapsInAirPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest3PenaltyCalculator" name="landingTest3PenaltyCalculator" value="${fieldValue(bean:attrs.i,field:'landingTest3PenaltyCalculator')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest3PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest3PenaltyCalculator) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        if (attrs.i.precisionFlying) {
            outln"""<legend>${message(code:'fc.landingtest.landing4')} (${message(code:'fc.landingtest.landing4.precision')})</legend>"""
        } else {
            outln"""<legend>${message(code:'fc.landingtest.landing4')}</legend>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.maxpoints')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4MaxPoints" name="landingTest4MaxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4MaxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4MaxPoints != attrs.i.contestRule.ruleValues.landingTest4MaxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.nolanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4NoLandingPoints" name="landingTest4NoLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4NoLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest4NoLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.outsidelanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4OutsideLandingPoints" name="landingTest4OutsideLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4OutsideLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest4OutsideLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.rollingoutside')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4RollingOutsidePoints" name="landingTest4RollingOutsidePoints" value="${fieldValue(bean:attrs.i,field:'landingTest4RollingOutsidePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest4RollingOutsidePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.powerinbox')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4PowerInBoxPoints" name="landingTest4PowerInBoxPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4PowerInBoxPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest4PowerInBoxPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundwithouttouching')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4GoAroundWithoutTouchingPoints" name="landingTest4GoAroundWithoutTouchingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4GoAroundWithoutTouchingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest4GoAroundWithoutTouchingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.goaroundinsteadstop')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4GoAroundInsteadStopPoints" name="landingTest4GoAroundInsteadStopPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4GoAroundInsteadStopPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest4GoAroundInsteadStopPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.abnormallanding')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4AbnormalLandingPoints" name="landingTest4AbnormalLandingPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4AbnormalLandingPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest4AbnormalLandingPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4NotAllowedAerodynamicAuxiliariesPoints" name="landingTest4NotAllowedAerodynamicAuxiliariesPoints" value="${fieldValue(bean:attrs.i,field:'landingTest4NotAllowedAerodynamicAuxiliariesPoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest4NotAllowedAerodynamicAuxiliariesPoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.touchingobstacle')}* [${message(code:'fc.points')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4TouchingObstaclePoints" name="landingTest4TouchingObstaclePoints" value="${fieldValue(bean:attrs.i,field:'landingTest4TouchingObstaclePoints')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4TouchingObstaclePoints != attrs.i.contestRule.ruleValues.landingTest4TouchingObstaclePoints) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.landingtest.penaltycalculator')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="landingTest4PenaltyCalculator" name="landingTest4PenaltyCalculator" value="${fieldValue(bean:attrs.i,field:'landingTest4PenaltyCalculator')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.landingTest4PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest4PenaltyCalculator) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        if (attrs.ret.modifynum > 0) {
            outln"""<p class="warning">${message(code:'fc.contestrule.differences',args:[attrs.ret.modifynum])}</p>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.printsubtitle')}:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="printPointsPrintTitle" name="printPointsPrintTitle" value="${fieldValue(bean:attrs.i,field:'printPointsPrintTitle')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <div>"""
        checkBox("printPointsGeneral", attrs.i.printPointsGeneral, 'fc.general', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsPlanningTest", attrs.i.printPointsPlanningTest, 'fc.planningtest', attrs) 
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsFlightTest", attrs.i.printPointsFlightTest, 'fc.flighttest', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsObservationTest", attrs.i.printPointsObservationTest, 'fc.observationtest', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsLandingTest1", attrs.i.printPointsLandingTest1, 'fc.landingtest.landing1', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsLandingTest2", attrs.i.printPointsLandingTest2, 'fc.landingtest.landing2', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsLandingTest3", attrs.i.printPointsLandingTest3, 'fc.landingtest.landing3', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsLandingTest4", attrs.i.printPointsLandingTest4, 'fc.landingtest.landing4', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsLandingField", attrs.i.printPointsLandingField, 'fc.landingtest.landingfield', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsTurnpointSign", attrs.i.printPointsTurnpointSign, 'fc.observationtest.turnpointsign', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsEnrouteCanvas", attrs.i.printPointsEnrouteCanvas, 'fc.observationtest.enroutecanvas', attrs)
        outln"""        </div>"""
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.general.printlandingcalculatorvalues')}:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="printLandingCalculatorValues" name="printLandingCalculatorValues" value="${fieldValue(bean:attrs.i,field:'printLandingCalculatorValues')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.i.printLandingCalculatorValues != attrs.i.contestRule.ruleValues.printLandingCalculatorValues) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <div>"""
        checkBox("printPointsZero", attrs.i.printPointsZero, 'fc.general.printpointszero', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsLandscape", attrs.i.printPointsLandscape, 'fc.printlandscape', attrs)
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("printPointsA3", attrs.i.printPointsA3, 'fc.printa3', attrs)
        outln"""        </div>"""
        outln"""    </p>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def pointsPrintable = { attrs, body ->
        boolean points_printed = false
        if (attrs.i.printPointsGeneral && (attrs.i.printPointsZero || attrs.i.increaseFactor > 0)) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="generalpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            outln"""                                <th colspan="3">${message(code:'fc.general')}</th>"""
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.increaseFactor > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.general.increasefactor.print')}</td>"""
                outln"""                            <td class="value">${attrs.i.increaseFactor}${message(code:'fc.percent')}</td>"""
                if (attrs.i.increaseFactor != attrs.i.contestRule.ruleValues.increaseFactor) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsPlanningTest) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="planningpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            outln"""                                <th colspan="3">${message(code:'fc.planningtest')}</th>"""
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.planningtest.directioncorrectgrad')}</td>"""
            outln"""                                <td class="value">${attrs.i.planningTestDirectionCorrectGrad}${message(code:'fc.grad')}</td>"""
            if (attrs.i.planningTestDirectionCorrectGrad != attrs.i.contestRule.ruleValues.planningTestDirectionCorrectGrad) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.planningtest.directionpointspergrad')}</td>"""
            outln"""                                <td class="value">${attrs.i.planningTestDirectionPointsPerGrad} ${message(code:'fc.points')}</td>"""
            if (attrs.i.planningTestDirectionPointsPerGrad != attrs.i.contestRule.ruleValues.planningTestDirectionPointsPerGrad) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.planningtest.timecorrectsecond')}</td>"""
            outln"""                                <td class="value">${attrs.i.planningTestTimeCorrectSecond}${message(code:'fc.time.s')}</td>"""
            if (attrs.i.planningTestTimeCorrectSecond != attrs.i.contestRule.ruleValues.planningTestTimeCorrectSecond) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.planningtest.timepointspersecond')}</td>"""
            outln"""                                <td class="value">${attrs.i.planningTestTimePointsPerSecond} ${message(code:'fc.points')}</td>"""
            if (attrs.i.planningTestTimePointsPerSecond != attrs.i.contestRule.ruleValues.planningTestTimePointsPerSecond) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.planningtest.maxpoints')}</td>"""
            outln"""                                <td class="value">${attrs.i.planningTestMaxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.planningTestMaxPoints != attrs.i.contestRule.ruleValues.planningTestMaxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.planningTestPlanTooLatePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.planningtest.giventolate')}</td>"""
                outln"""                            <td class="value">${attrs.i.planningTestPlanTooLatePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.planningTestPlanTooLatePoints != attrs.i.contestRule.ruleValues.planningTestPlanTooLatePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.planningTestExitRoomTooLatePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.planningtest.exitroomtolate')}</td>"""
                outln"""                            <td class="value">${attrs.i.planningTestExitRoomTooLatePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.planningTestExitRoomTooLatePoints != attrs.i.contestRule.ruleValues.planningTestExitRoomTooLatePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.planningTestForbiddenCalculatorsPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.planningtest.forbiddencalculators')}</td>"""
                outln"""                            <td class="value">${attrs.i.planningTestForbiddenCalculatorsPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.planningTestForbiddenCalculatorsPoints != attrs.i.contestRule.ruleValues.planningTestForbiddenCalculatorsPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsFlightTest) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="flightpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            outln"""                                <th colspan="3">${message(code:'fc.flighttest')}</th>"""
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.flighttest.takeoffcorrectsecond')}</td>"""
            outln"""                                <td class="value">${attrs.i.flightTestTakeoffCorrectSecond} ${message(code:'fc.time.s')}</td>"""
            if (attrs.i.flightTestTakeoffCorrectSecond != attrs.i.contestRule.ruleValues.flightTestTakeoffCorrectSecond) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.flightTestTakeoffCheckSeconds && attrs.i.flightTestTakeoffPointsPerSecond > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.takeoffpointspersecond')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestTakeoffPointsPerSecond} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestTakeoffPointsPerSecond != attrs.i.contestRule.ruleValues.flightTestTakeoffPointsPerSecond) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.flighttest.takeoffmissed')}</td>"""
            outln"""                                <td class="value">${attrs.i.flightTestTakeoffMissedPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.flightTestTakeoffMissedPoints != attrs.i.contestRule.ruleValues.flightTestTakeoffMissedPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.flighttest.cptimecorrectsecond')}</td>"""
            outln"""                                <td class="value">${attrs.i.flightTestCptimeCorrectSecond}${message(code:'fc.time.s')}</td>"""
            if (attrs.i.flightTestCptimeCorrectSecond != attrs.i.contestRule.ruleValues.flightTestCptimeCorrectSecond) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.flighttest.cptimepointspersecond')}</td>"""
            outln"""                                <td class="value">${attrs.i.flightTestCptimePointsPerSecond} ${message(code:'fc.points')}</td>"""
            if (attrs.i.flightTestCptimePointsPerSecond != attrs.i.contestRule.ruleValues.flightTestCptimePointsPerSecond) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.flighttest.cptimemaxpoints')}</td>"""
            outln"""                                <td class="value">${attrs.i.flightTestCptimeMaxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.flightTestCptimeMaxPoints != attrs.i.contestRule.ruleValues.flightTestCptimeMaxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.flighttest.cpnotfound')}</td>"""
            outln"""                                <td class="value">${attrs.i.flightTestCpNotFoundPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.flightTestCpNotFoundPoints != attrs.i.contestRule.ruleValues.flightTestCpNotFoundPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.flightTestProcedureTurnNotFlownPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.procedureturnnotflown')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestProcedureTurnNotFlownPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestProcedureTurnNotFlownPoints != attrs.i.contestRule.ruleValues.flightTestProcedureTurnNotFlownPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestMinAltitudeMissedPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.minaltitudemissed')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestMinAltitudeMissedPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestMinAltitudeMissedPoints != attrs.i.contestRule.ruleValues.flightTestMinAltitudeMissedPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestBadCoursePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.badcoursecorrectsecond')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestBadCourseCorrectSecond}${message(code:'fc.time.s')}</td>"""
                if (attrs.i.flightTestBadCourseCorrectSecond != attrs.i.contestRule.ruleValues.flightTestBadCourseCorrectSecond) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.badcourse')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestBadCoursePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestBadCoursePoints != attrs.i.contestRule.ruleValues.flightTestBadCoursePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestBadCourseStartLandingPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.badcoursestartlanding')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestBadCourseStartLandingPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestBadCourseStartLandingPoints != attrs.i.contestRule.ruleValues.flightTestBadCourseStartLandingPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestLandingToLatePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.landingtolate')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestLandingToLatePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestLandingToLatePoints != attrs.i.contestRule.ruleValues.flightTestLandingToLatePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestGivenToLatePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.giventolate')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestGivenToLatePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestGivenToLatePoints != attrs.i.contestRule.ruleValues.flightTestGivenToLatePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestSafetyAndRulesInfringementPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.safetyandrulesinfringement')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestSafetyAndRulesInfringementPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestSafetyAndRulesInfringementPoints != attrs.i.contestRule.ruleValues.flightTestSafetyAndRulesInfringementPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestInstructionsNotFollowedPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.instructionsnotfollowed')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestInstructionsNotFollowedPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestInstructionsNotFollowedPoints != attrs.i.contestRule.ruleValues.flightTestInstructionsNotFollowedPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestFalseEnvelopeOpenedPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.falseenvelopeopened')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestFalseEnvelopeOpenedPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestFalseEnvelopeOpenedPoints != attrs.i.contestRule.ruleValues.flightTestFalseEnvelopeOpenedPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestSafetyEnvelopeOpenedPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.safetyenvelopeopened')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestSafetyEnvelopeOpenedPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestSafetyEnvelopeOpenedPoints != attrs.i.contestRule.ruleValues.flightTestSafetyEnvelopeOpenedPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestFrequencyNotMonitoredPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.frequencynotmonitored')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestFrequencyNotMonitoredPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestFrequencyNotMonitoredPoints != attrs.i.contestRule.ruleValues.flightTestFrequencyNotMonitoredPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.flightTestForbiddenEquipmentPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.flighttest.forbiddenequipment')}</td>"""
                outln"""                            <td class="value">${attrs.i.flightTestForbiddenEquipmentPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.flightTestForbiddenEquipmentPoints != attrs.i.contestRule.ruleValues.flightTestForbiddenEquipmentPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsObservationTest) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="observationpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            outln"""                                <th colspan="3">${message(code:'fc.observationtest')}</th>"""
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || (attrs.i.observationTestTurnpointNotFoundPoints > 0 && attrs.i.observationTestTurnpointFalsePoints > 0)) {
                outln"""                        <tr class="group">"""
                outln"""                            <td colspan="3">${message(code:'fc.observationtest.turnpoint')}</td>"""
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.observationTestTurnpointNotFoundPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.observationtest.turnpointnotfound')}</td>"""
                outln"""                            <td class="value">${attrs.i.observationTestTurnpointNotFoundPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.observationTestTurnpointNotFoundPoints != attrs.i.contestRule.ruleValues.observationTestTurnpointNotFoundPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.observationTestTurnpointFalsePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.observationtest.turnpointfalse')}</td>"""
                outln"""                            <td class="value">${attrs.i.observationTestTurnpointFalsePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.observationTestTurnpointFalsePoints != attrs.i.contestRule.ruleValues.observationTestTurnpointFalsePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="group">"""
            outln"""                                <td colspan="3">${message(code:'fc.observationtest.enroute')}</td>"""
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.observationtest.enroutecorrectvalue')}</td>"""
            outln"""                                <td class="value">${FcMath.EnrouteValueStr(attrs.i.observationTestEnrouteCorrectValue)} ${message(code:attrs.i.observationTestEnrouteValueUnit.code)}</td>"""
            if (attrs.i.observationTestEnrouteCorrectValue != attrs.i.contestRule.ruleValues.observationTestEnrouteCorrectValue || attrs.i.observationTestEnrouteValueUnit != attrs.i.contestRule.ruleValues.observationTestEnrouteValueUnit) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.observationTestEnrouteInexactValue > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.observationtest.enrouteinexactvalue')}</td>"""
                outln"""                            <td class="value">${FcMath.EnrouteValueStr(attrs.i.observationTestEnrouteInexactValue)} ${message(code:attrs.i.observationTestEnrouteValueUnit.code)}</td>"""
                if (attrs.i.observationTestEnrouteInexactValue != attrs.i.contestRule.ruleValues.observationTestEnrouteInexactValue || attrs.i.observationTestEnrouteValueUnit != attrs.i.contestRule.ruleValues.observationTestEnrouteValueUnit) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.observationTestEnrouteInexactPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.observationtest.enrouteinexact')}</td>"""
                outln"""                            <td class="value">${attrs.i.observationTestEnrouteInexactPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.observationTestEnrouteInexactPoints != attrs.i.contestRule.ruleValues.observationTestEnrouteInexactPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.observationtest.enroutenotfound')}</td>"""
            outln"""                                <td class="value">${attrs.i.observationTestEnrouteNotFoundPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.observationTestEnrouteNotFoundPoints != attrs.i.contestRule.ruleValues.observationTestEnrouteNotFoundPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.observationtest.enroutefalse')}</td>"""
            outln"""                                <td class="value">${attrs.i.observationTestEnrouteFalsePoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.observationTestEnrouteFalsePoints != attrs.i.contestRule.ruleValues.observationTestEnrouteFalsePoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsLandingTest1) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="landingpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            if (attrs.i.printPointsLandingTest2 || attrs.i.printPointsLandingTest3 || attrs.i.printPointsLandingTest4) {
                if (attrs.i.precisionFlying) {
                    outln"""                        <th colspan="3">${message(code:'fc.landingtest.landing1')} (${message(code:'fc.landingtest.landing1.precision')})</th>"""
                } else {
                    outln"""                        <th colspan="3">${message(code:'fc.landingtest.landing1')}</th>"""
                }
            } else {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing')}</th>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.maxpoints')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest1MaxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest1MaxPoints != attrs.i.contestRule.ruleValues.landingTest1MaxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.nolanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest1NoLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest1NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest1NoLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.outsidelanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest1OutsideLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest1OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest1OutsideLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest1RollingOutsidePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.rollingoutside')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest1RollingOutsidePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest1RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest1RollingOutsidePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.powerinbox')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest1PowerInBoxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest1PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest1PowerInBoxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest1GoAroundWithoutTouchingPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest1GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest1GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest1GoAroundWithoutTouchingPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest1GoAroundInsteadStopPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest1GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest1GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest1GoAroundInsteadStopPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.abnormallanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest1AbnormalLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest1AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest1AbnormalLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest1NotAllowedAerodynamicAuxiliariesPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest1NotAllowedAerodynamicAuxiliariesPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest1NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest1NotAllowedAerodynamicAuxiliariesPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="penaltycalculator">"""
            outln"""                                <td class="value" colspan="2">${message(code:'fc.landingtest.penalties')}: ${FcMath.GetPrintLandingCalculatorValues(attrs.i.printLandingCalculatorValues,attrs.i.landingTest1PenaltyCalculator)}</td>"""
            if (attrs.i.landingTest1PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest1PenaltyCalculator) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsLandingTest2) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="landingpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            if (attrs.i.precisionFlying) {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing2')} (${message(code:'fc.landingtest.landing2.precision')})</th>"""
            } else {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing2')}</th>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.maxpoints')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest2MaxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest2MaxPoints != attrs.i.contestRule.ruleValues.landingTest2MaxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.nolanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest2NoLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest2NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest2NoLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.outsidelanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest2OutsideLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest2OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest2OutsideLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest2RollingOutsidePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.rollingoutside')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest2RollingOutsidePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest2RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest2RollingOutsidePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.powerinbox')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest2PowerInBoxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest2PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest2PowerInBoxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest2GoAroundWithoutTouchingPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest2GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest2GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest2GoAroundWithoutTouchingPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest2GoAroundInsteadStopPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest2GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest2GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest2GoAroundInsteadStopPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.abnormallanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest2AbnormalLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest2AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest2AbnormalLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest2NotAllowedAerodynamicAuxiliariesPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest2NotAllowedAerodynamicAuxiliariesPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest2NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest2NotAllowedAerodynamicAuxiliariesPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest2PowerInAirPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.powerinair')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest2PowerInAirPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest2PowerInAirPoints != attrs.i.contestRule.ruleValues.landingTest2PowerInAirPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="penaltycalculator">"""
            outln"""                                <td class="value" colspan="2">${message(code:'fc.landingtest.penalties')}: ${FcMath.GetPrintLandingCalculatorValues(attrs.i.printLandingCalculatorValues,attrs.i.landingTest2PenaltyCalculator)}</td>"""
            if (attrs.i.landingTest2PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest2PenaltyCalculator) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsLandingTest3) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="landingpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            if (attrs.i.precisionFlying) {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing3')} (${message(code:'fc.landingtest.landing3.precision')})</th>"""
            } else {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing3')}</th>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.maxpoints')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest3MaxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest3MaxPoints != attrs.i.contestRule.ruleValues.landingTest3MaxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.nolanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest3NoLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest3NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest3NoLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.outsidelanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest3OutsideLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest3OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest3OutsideLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest3RollingOutsidePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.rollingoutside')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest3RollingOutsidePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest3RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest3RollingOutsidePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.powerinbox')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest3PowerInBoxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest3PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest3PowerInBoxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest3GoAroundWithoutTouchingPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest3GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest3GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest3GoAroundWithoutTouchingPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest3GoAroundInsteadStopPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest3GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest3GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest3GoAroundInsteadStopPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.abnormallanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest3AbnormalLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest3AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest3AbnormalLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest3NotAllowedAerodynamicAuxiliariesPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest3NotAllowedAerodynamicAuxiliariesPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest3NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest3NotAllowedAerodynamicAuxiliariesPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest3PowerInAirPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.powerinair')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest3PowerInAirPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest3PowerInAirPoints != attrs.i.contestRule.ruleValues.landingTest3PowerInAirPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest3FlapsInAirPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.flapsinair')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest3FlapsInAirPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest3FlapsInAirPoints != attrs.i.contestRule.ruleValues.landingTest3FlapsInAirPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="penaltycalculator">"""
            outln"""                                <td class="value" colspan="2">${message(code:'fc.landingtest.penalties')}: ${FcMath.GetPrintLandingCalculatorValues(attrs.i.printLandingCalculatorValues,attrs.i.landingTest3PenaltyCalculator)}</td>"""
            if (attrs.i.landingTest3PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest3PenaltyCalculator) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
            outln"""                <br/>"""
        }
        if (attrs.i.printPointsLandingTest4) {
            points_printed = true
            outln"""                <div style="page-break-inside:avoid">"""
            outln"""                    <table class="landingpoints">"""
            outln"""                        <tbody>"""
            outln"""                            <tr class="title">"""
            if (attrs.i.precisionFlying) {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing4')} (${message(code:'fc.landingtest.landing4.precision')})</th>"""
            } else {
                outln"""                            <th colspan="3">${message(code:'fc.landingtest.landing4')}</th>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.maxpoints')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest4MaxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest4MaxPoints != attrs.i.contestRule.ruleValues.landingTest4MaxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.nolanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest4NoLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest4NoLandingPoints != attrs.i.contestRule.ruleValues.landingTest4NoLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.outsidelanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest4OutsideLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest4OutsideLandingPoints != attrs.i.contestRule.ruleValues.landingTest4OutsideLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest4RollingOutsidePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.rollingoutside')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest4RollingOutsidePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest4RollingOutsidePoints != attrs.i.contestRule.ruleValues.landingTest4RollingOutsidePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.powerinbox')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest4PowerInBoxPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest4PowerInBoxPoints != attrs.i.contestRule.ruleValues.landingTest4PowerInBoxPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest4GoAroundWithoutTouchingPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundwithouttouching')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest4GoAroundWithoutTouchingPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest4GoAroundWithoutTouchingPoints != attrs.i.contestRule.ruleValues.landingTest4GoAroundWithoutTouchingPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest4GoAroundInsteadStopPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.goaroundinsteadstop')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest4GoAroundInsteadStopPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest4GoAroundInsteadStopPoints != attrs.i.contestRule.ruleValues.landingTest4GoAroundInsteadStopPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="value">"""
            outln"""                                <td class="name">${message(code:'fc.landingtest.abnormallanding')}</td>"""
            outln"""                                <td class="value">${attrs.i.landingTest4AbnormalLandingPoints} ${message(code:'fc.points')}</td>"""
            if (attrs.i.landingTest4AbnormalLandingPoints != attrs.i.contestRule.ruleValues.landingTest4AbnormalLandingPoints) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            if (attrs.i.printPointsZero || attrs.i.landingTest4NotAllowedAerodynamicAuxiliariesPoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.notallowedaerodynamicauxiliaries')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest4NotAllowedAerodynamicAuxiliariesPoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest4NotAllowedAerodynamicAuxiliariesPoints != attrs.i.contestRule.ruleValues.landingTest4NotAllowedAerodynamicAuxiliariesPoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            if (attrs.i.printPointsZero || attrs.i.landingTest4TouchingObstaclePoints > 0) {
                outln"""                        <tr class="value">"""
                outln"""                            <td class="name">${message(code:'fc.landingtest.touchingobstacle')}</td>"""
                outln"""                            <td class="value">${attrs.i.landingTest4TouchingObstaclePoints} ${message(code:'fc.points')}</td>"""
                if (attrs.i.landingTest4TouchingObstaclePoints != attrs.i.contestRule.ruleValues.landingTest4TouchingObstaclePoints) {
                    outln"""                        <td class="modify">!</td>"""
                } else {
                    outln"""                        <td class="modify"/>"""
                }
                outln"""                        </tr>"""
            }
            outln"""                            <tr class="penaltycalculator">"""
            outln"""                                <td class="value" colspan="2">${message(code:'fc.landingtest.penalties')}: ${FcMath.GetPrintLandingCalculatorValues(attrs.i.printLandingCalculatorValues,attrs.i.landingTest4PenaltyCalculator)}</td>"""
            if (attrs.i.landingTest4PenaltyCalculator != attrs.i.contestRule.ruleValues.landingTest4PenaltyCalculator) {
                outln"""                            <td class="modify">!</td>"""
            } else {
                outln"""                            <td class="modify"/>"""
            }
            outln"""                            </tr>"""
            outln"""                        </tbody>"""
            outln"""                    </table>"""
            outln"""                </div>"""
        }
        if (attrs.i.printPointsLandingField) {
            if (points_printed) {
                outln"""<div style="page-break-before: always; color: white; height: 2px;">.</div>"""
            }
            points_printed = true
            for (String landingfield_imagename in attrs.i.landingFieldImageName.split(',')) {
                if (landingfield_imagename.contains('*')) {
                    landingfield_imagename = landingfield_imagename.replace('*', "_${attrs.lang}")
                }
                outln"""<img class="landingfield" src="${createLinkTo(dir:'',file:landingfield_imagename)}" />"""
            }
        }
        if (attrs.i.printPointsTurnpointSign || attrs.i.printPointsEnrouteCanvas) {
            if (points_printed) {
                outln"""<div style="page-break-before: always; color: white; height: 2px;">.</div>"""
            }
            String enroutephoto_style = "float: left;"
            if (attrs.i.printPointsTurnpointSign) {
                enroutephoto_style = "float: right;"
                outln"""                <table class="turnpointsignpoints" style="float: left;">"""
                outln"""                    <tbody>"""
                outln"""                        <tr class="title">"""
                outln"""                            <th colspan="2">${message(code:'fc.observation.turnpoint.canvas')}</th>"""
                outln"""                        </tr>"""
                for (TurnpointSign sign in TurnpointSign.GetTurnpointSigns(true)) { // true - isCanvas
                    if (sign != TurnpointSign.None) {
                        outln"""                <tr class="value">"""
                        outln"""                    <td class="name">${sign.title}</td>"""
                        outln"""                    <td class="value"><img src="${createLinkTo(dir:'',file:sign.imageName)}"/></td>"""
                        outln"""                </tr>"""
                    }
                }
                outln"""                    </tbody>"""
                outln"""                </table>"""
            }
            if (attrs.i.printPointsEnrouteCanvas) {
                outln"""                <table class="enroutecanvaspoints" style="${enroutephoto_style}">"""
                outln"""                    <tbody>"""
                outln"""                        <tr class="title">"""
                outln"""                            <th colspan="2">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
                outln"""                        </tr>"""
                for (EnrouteCanvasSign sign in EnrouteCanvasSign.values()) {
                    if (sign != EnrouteCanvasSign.None) {
                        outln"""                <tr class="value">"""
                        outln"""                    <td class="name">${sign.canvasName}</td>"""
                        outln"""                    <td class="value"><img src="${createLinkTo(dir:'',file:sign.imageName)}"/></td>"""
                        outln"""                </tr>"""
                    }
                }
                outln"""                    </tbody>"""
                outln"""                </table>"""
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox(String name, boolean checked, String label, attrs)
    {
        outln"""    <input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""<input type="checkbox" id="${name}" name="${name}" checked="checked" tabIndex="${attrs.ti[0]++}"/>"""
        } else {
            outln"""<input type="checkbox" id="${name}" name="${name}" tabIndex="${attrs.ti[0]++}"/>"""
        }
        outln"""    <label>${message(code:label)}</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private radioEntry(String name, def value, boolean checked, String label, attrs)
    {
        //outln"""<div>"""
        if (checked) {
            outln"""<label><input type="radio" name="${name}" value="${value}" checked="checked" tabIndex="${attrs.ti[0]}"/>${label}</label>"""
        } else {
            outln"""<label><input type="radio" name="${name}" value="${value}" tabIndex="${attrs.ti[0]}"/>${label}</label>"""
        }
        //outln"""</div>"""
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
