class DefaultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def editDefaults = { attrs, body ->
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td>${attrs.contest.ruleTitle}</td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
        outln"""<fieldset>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.minroutelegs')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="minRouteLegs" name="minRouteLegs" value="${fieldValue(bean:attrs.contest,field:'minRouteLegs')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.contest.minRouteLegs != attrs.contest.contestRule.ruleValues.minRouteLegs) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.maxroutelegs')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="maxRouteLegs" name="maxRouteLegs" value="${fieldValue(bean:attrs.contest,field:'maxRouteLegs')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.contest.maxRouteLegs != attrs.contest.contestRule.ruleValues.maxRouteLegs) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        if (!attrs.contest.anrFlying) {        
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.gatewidth.sc')}* [${message(code:'fc.mile')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="scGateWidth" name="scGateWidth" value="${fieldValue(bean:attrs.contest,field:'scGateWidth')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.scGateWidth != attrs.contest.contestRule.ruleValues.scGateWidth) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
            outln"""    <div>"""
            checkBox("useProcedureTurns", attrs.contest.useProcedureTurns, 'fc.contestrule.useprocedureturns', attrs)
            if (attrs.contest.useProcedureTurns != attrs.contest.contestRule.ruleValues.useProcedureTurns) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <div>"""
            outln"""        <label>${message(code:'fc.observation.turnpoint')}*:</label>"""
            outln"""        <br/>"""
            for (def v in TurnpointRule.values()) {
                radioEntry("turnpointRule", v, attrs.contest.turnpointRule == v, message(code:v.code), attrs)
            }
            if (attrs.contest.turnpointRule != attrs.contest.contestRule.ruleValues.turnpointRule) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <div>"""
            checkBox("turnpointMapMeasurement", attrs.contest.turnpointMapMeasurement, 'fc.contestrule.turnpointmapmeasurement', attrs)
            if (attrs.contest.turnpointMapMeasurement != attrs.contest.contestRule.ruleValues.turnpointMapMeasurement) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <div>"""
            outln"""        <label>${message(code:'fc.observation.enroute.photo')}*:</label>"""
            outln"""        <br/>"""
            for (def v in EnrouteRule.values()) {
                radioEntry("enroutePhotoRule", v, attrs.contest.enroutePhotoRule == v, message(code:v.code), attrs)
            }
            if (attrs.contest.enroutePhotoRule != attrs.contest.contestRule.ruleValues.enroutePhotoRule) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <div>"""
            outln"""        <label>${message(code:'fc.observation.enroute.canvas')}*:</label>"""
            outln"""        <br/>"""
            for (def v in EnrouteRule.values()) {
                radioEntry("enrouteCanvasRule", v, attrs.contest.enrouteCanvasRule == v, message(code:v.code), attrs)
            }
            if (attrs.contest.enrouteCanvasRule != attrs.contest.contestRule.ruleValues.enrouteCanvasRule) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <div>"""
            checkBox("enrouteCanvasMultiple", attrs.contest.enrouteCanvasMultiple, 'fc.contestrule.enroutecanvasmultiple', attrs)
            if (attrs.contest.enrouteCanvasMultiple != attrs.contest.contestRule.ruleValues.enrouteCanvasMultiple) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.minenroutephotos')}*:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="minEnroutePhotos" name="minEnroutePhotos" value="${fieldValue(bean:attrs.contest,field:'minEnroutePhotos')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.minEnroutePhotos != attrs.contest.contestRule.ruleValues.minEnroutePhotos) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.maxenroutephotos')}*:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="maxEnroutePhotos" name="maxEnroutePhotos" value="${fieldValue(bean:attrs.contest,field:'maxEnroutePhotos')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.maxEnroutePhotos != attrs.contest.contestRule.ruleValues.maxEnroutePhotos) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.minenroutecanvas')}*:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="minEnrouteCanvas" name="minEnrouteCanvas" value="${fieldValue(bean:attrs.contest,field:'minEnrouteCanvas')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.minEnrouteCanvas != attrs.contest.contestRule.ruleValues.minEnrouteCanvas) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.maxenroutecanvas')}*:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="maxEnrouteCanvas" name="maxEnrouteCanvas" value="${fieldValue(bean:attrs.contest,field:'maxEnrouteCanvas')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.maxEnrouteCanvas != attrs.contest.contestRule.ruleValues.maxEnrouteCanvas) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.minenroutetargets')}*:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="minEnrouteTargets" name="minEnrouteTargets" value="${fieldValue(bean:attrs.contest,field:'minEnrouteTargets')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.minEnrouteTargets != attrs.contest.contestRule.ruleValues.minEnrouteTargets) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.contestrule.maxenroutetargets')}*:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="maxEnrouteTargets" name="maxEnrouteTargets" value="${fieldValue(bean:attrs.contest,field:'maxEnrouteTargets')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.maxEnrouteTargets != attrs.contest.contestRule.ruleValues.maxEnrouteTargets) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
        }
        outln"""    <div>"""
        checkBox("flightPlanShowLegDistance", attrs.contest.flightPlanShowLegDistance, 'fc.flighttest.flightplan.showlegdistance', attrs)
        if (attrs.contest.flightPlanShowLegDistance != attrs.contest.contestRule.ruleValues.flightPlanShowLegDistance) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("flightPlanShowTrueTrack", attrs.contest.flightPlanShowTrueTrack, 'fc.flighttest.flightplan.showtruetrack', attrs)
        if (attrs.contest.flightPlanShowTrueTrack != attrs.contest.contestRule.ruleValues.flightPlanShowTrueTrack) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        if (attrs.contest.anrFlying) {
            outln"""    <div>"""
            checkBox("flightPlanShowLocalTime", attrs.contest.flightPlanShowLocalTime, 'fc.flighttest.flightplan.showlocaltime', attrs)
            if (attrs.contest.flightPlanShowLocalTime != attrs.contest.contestRule.ruleValues.flightPlanShowLocalTime) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
        } else {
            outln"""    <div>"""
            checkBox("flightPlanShowTrueHeading", attrs.contest.flightPlanShowTrueHeading, 'fc.flighttest.flightplan.showtrueheading', attrs)
            if (attrs.contest.flightPlanShowTrueHeading != attrs.contest.contestRule.ruleValues.flightPlanShowTrueHeading) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <div>"""
            checkBox("flightPlanShowGroundSpeed", attrs.contest.flightPlanShowGroundSpeed, 'fc.flighttest.flightplan.showgroundspeed', attrs)
            if (attrs.contest.flightPlanShowGroundSpeed != attrs.contest.contestRule.ruleValues.flightPlanShowGroundSpeed) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <div>"""
            checkBox("flightPlanShowLocalTime", attrs.contest.flightPlanShowLocalTime, 'fc.flighttest.flightplan.showlocaltime', attrs)
            if (attrs.contest.flightPlanShowLocalTime != attrs.contest.contestRule.ruleValues.flightPlanShowLocalTime) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <div>"""
            checkBox("flightPlanShowElapsedTime", attrs.contest.flightPlanShowElapsedTime, 'fc.flighttest.flightplan.showelapsedtime', attrs)
            if (attrs.contest.flightPlanShowElapsedTime != attrs.contest.contestRule.ruleValues.flightPlanShowElapsedTime) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""    </div>"""
            outln"""    <br/>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.flighttest.flightplan.submissionminutes')} [${message(code:'fc.time.min')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="flightTestSubmissionMinutes" name="flightTestSubmissionMinutes" value="${fieldValue(bean:attrs.contest,field:'flightTestSubmissionMinutes')}" tabIndex="${attrs.ti[0]++}"/>"""
            if (attrs.contest.flightTestSubmissionMinutes != attrs.contest.contestRule.ruleValues.flightTestSubmissionMinutes) {
                outln"""        !"""
                attrs.ret.modifynum++
            }
            outln"""    </p>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.unsuitablestartnum')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="unsuitableStartNum" name="unsuitableStartNum" value="${fieldValue(bean:attrs.contest,field:'unsuitableStartNum')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.contest.unsuitableStartNum != attrs.contest.contestRule.ruleValues.unsuitableStartNum) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        if (BootStrap.global.IsLiveTrackingPossible()) {
            outln"""<p>"""
            outln"""    <label>${message(code:'fc.general.livetrackingscorecard')}:</label>"""
            outln"""    <br/>"""
            //outln"""    <input type="text" id="liveTrackingScorecard" name="liveTrackingScorecard" value="${fieldValue(bean:attrs.contest,field:'liveTrackingScorecard')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""        <select name="liveTrackingScorecard" tabIndex="${attrs.ti[0]++}">"""
            //outln"""            <option></option>"""
            for (String scorecard in Defs.LIVETRACKING_SCORECARDS) {
                if (scorecard == attrs.contest.liveTrackingScorecard) {
                    outln"""<option value="${scorecard}" selected>${scorecard}</option>"""
                } else {
                    outln"""<option value="${scorecard}">${scorecard}</option>"""
                }
            }
            outln"""        </select>"""
            if (attrs.contest.liveTrackingScorecard != attrs.contest.contestRule.ruleValues.liveTrackingScorecard) {
                outln"""    !"""
                attrs.ret.modifynum++
            }
            outln"""</p>"""
        }
        outln"""</fieldset>"""
        
        // other defaults
        outln"""<fieldset>"""
        outln"""    <div>"""
        outln"""        <input type="checkbox" id="otherdefaults_checkbox_id" tabIndex="${attrs.ti[0]++}" onclick="otherdefaults_click();"/>"""
        outln"""        <label>${message(code:'fc.contestrule.defaults.showother')}</label>"""
        outln"""    </div>"""
        outln"""    <div id="otherdefaults_id" hidden>"""
        outln"""        <br/>"""
        outln"""        <div>"""
        checkBox("precisionFlying", attrs.contest.precisionFlying, 'fc.general.precisionflying', attrs)
        if (attrs.contest.precisionFlying != attrs.contest.contestRule.ruleValues.precisionFlying) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("anrFlying", attrs.contest.anrFlying, 'fc.contestrule.anr', attrs)
        if (attrs.contest.anrFlying != attrs.contest.contestRule.ruleValues.anrFlying) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("showPlanningTest", attrs.contest.showPlanningTest, 'fc.contestrule.showplanningtest', attrs)
        if (attrs.contest.showPlanningTest != attrs.contest.contestRule.ruleValues.showPlanningTest) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("activateFlightTestCheckLanding", attrs.contest.activateFlightTestCheckLanding, 'fc.contestrule.activateflighttestchecklanding', attrs)
        if (attrs.contest.activateFlightTestCheckLanding != attrs.contest.contestRule.ruleValues.activateFlightTestCheckLanding) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </div>"""
        outln"""        <div>"""
        checkBox("showObservationTest", attrs.contest.showObservationTest, 'fc.contestrule.showobservationtest', attrs)
        if (attrs.contest.showObservationTest != attrs.contest.contestRule.ruleValues.showObservationTest) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""        </div>"""
        outln"""        <p>"""
        outln"""            <br/>"""
        outln"""            <label>${message(code:'fc.contestrule.flighttestlastgatenobadcourseseconds')}* [${message(code:'fc.time.s')}]:</label>"""
        outln"""            <br/>"""
        outln"""            <input type="text" id="flightTestLastGateNoBadCourseSeconds" name="flightTestLastGateNoBadCourseSeconds" value="${fieldValue(bean:attrs.contest,field:'flightTestLastGateNoBadCourseSeconds')}" tabIndex="${attrs.ti[0]++}"/>"""
        if (attrs.contest.flightTestLastGateNoBadCourseSeconds != attrs.contest.contestRule.ruleValues.flightTestLastGateNoBadCourseSeconds) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""        </p>"""
        outln"""    </div>"""
        outln"""</fieldset>"""
        outln"""<script>"""
        outln"""    function otherdefaults_click() {"""
        outln"""        \$("#otherdefaults_id").prop("hidden", !\$("#otherdefaults_checkbox_id").prop("checked"));"""
        outln"""    }"""
        outln"""</script>"""
        
        // show difference count
        if (attrs.ret.modifynum > 0) {
            outln"""<fieldset>"""
            outln"""    <p class="warning">${message(code:'fc.contestrule.differences',args:[attrs.ret.modifynum])}</p>"""
            outln"""</fieldset>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def defaultsPrintable = { attrs, body ->
        outln"""<table class="planningpoints">"""
        outln"""    <tbody>"""
        outln"""        <tr class="title">"""
        outln"""            <th colspan="3">${message(code:'fc.planningtest')}</th>"""
        outln"""        </tr>"""
        outln"""        <tr class="value">"""
        outln"""            <td class="name">${message(code:'fc.planningtest.directioncorrectgrad')}</td>"""
        outln"""            <td class="value">${attrs.contest.planningTestDirectionCorrectGrad}${message(code:'fc.grad')}</td>"""
        if (attrs.contest.planningTestDirectionCorrectGrad != attrs.contest.contestRule.ruleValues.planningTestDirectionCorrectGrad) {
            outln"""        <td class="modify">!</td>"""
        } else {
            outln"""        <td class="modify"/>"""
        }
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox(String name, boolean checked, String label, attrs)
    {
        outln"""<input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""            <input type="checkbox" id="${name}" name="${name}" checked="checked" tabIndex="${attrs.ti[0]++}"/>"""
        } else {
            outln"""            <input type="checkbox" id="${name}" name="${name}" tabIndex="${attrs.ti[0]++}"/>"""
        }
        outln"""<label>${message(code:label)}</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private radioEntry(String name, def value, boolean checked, String label, attrs)
    {
        if (checked) {
            outln"""    <label><input type="radio" name="${name}" value="${value}" checked="checked" tabIndex="${attrs.ti[0]++}"/>${label}</label>"""
        } else {
            outln"""    <label><input type="radio" name="${name}" value="${value}" tabIndex="${attrs.ti[0]++}"/>${label}</label>"""
        }
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
