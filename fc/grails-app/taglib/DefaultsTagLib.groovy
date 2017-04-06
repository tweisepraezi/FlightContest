class DefaultsTagLib 
{
	//static defaultEncodeAs = 'html'
	//static encodeAsForTags = [tagName: 'raw']
	
    // --------------------------------------------------------------------------------------------------------------------
    def editDefaults = { attrs, body ->
        outln"""<table>"""
        outln"""    <tbody>"""
        outln"""        <tr>"""
        outln"""            <td>${message(code:attrs.i.contestRule.titleCode)}</td>"""
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
        outln"""<fieldset>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.minroutelegs')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="minRouteLegs" name="minRouteLegs" value="${fieldValue(bean:attrs.i,field:'minRouteLegs')}" tabIndex="1"/>"""
        if (attrs.i.minRouteLegs != attrs.i.contestRule.ruleValues.minRouteLegs) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.maxroutelegs')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="maxRouteLegs" name="maxRouteLegs" value="${fieldValue(bean:attrs.i,field:'maxRouteLegs')}" tabIndex="2"/>"""
        if (attrs.i.maxRouteLegs != attrs.i.contestRule.ruleValues.maxRouteLegs) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.gatewidth.sc')}* [${message(code:'fc.mile')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="scGateWidth" name="scGateWidth" value="${fieldValue(bean:attrs.i,field:'scGateWidth')}" tabIndex="3"/>"""
        if (attrs.i.scGateWidth != attrs.i.contestRule.ruleValues.scGateWidth) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.turnpoint')}*:</label>"""
        outln"""        <br/>"""
        for (def v in TurnpointRule.values()) {
            radioEntry("turnpointRule", v, attrs.i.turnpointRule == v, message(code:v.code),"4")
        }
        if (attrs.i.turnpointRule != attrs.i.contestRule.ruleValues.turnpointRule) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <br/>"""
        outln"""    <div>"""
        checkBox("turnpointMapMeasurement", attrs.i.turnpointMapMeasurement, 'fc.contestrule.turnpointmapmeasurement', "5")
        if (attrs.i.turnpointMapMeasurement != attrs.i.contestRule.ruleValues.turnpointMapMeasurement) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <br/>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.enroute.photo')}*:</label>"""
        outln"""        <br/>"""
        for (def v in EnrouteRule.values()) {
            radioEntry("enroutePhotoRule", v, attrs.i.enroutePhotoRule == v, message(code:v.code),"6")
        }
        if (attrs.i.enroutePhotoRule != attrs.i.contestRule.ruleValues.enroutePhotoRule) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <br/>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.enroute.canvas')}*:</label>"""
        outln"""        <br/>"""
        for (def v in EnrouteRule.values()) {
            radioEntry("enrouteCanvasRule", v, attrs.i.enrouteCanvasRule == v, message(code:v.code),"7")
        }
        if (attrs.i.enrouteCanvasRule != attrs.i.contestRule.ruleValues.enrouteCanvasRule) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <br/>"""
        outln"""    <div>"""
        checkBox("enrouteCanvasMultiple", attrs.i.enrouteCanvasMultiple, 'fc.contestrule.enroutecanvasmultiple', "8")
        if (attrs.i.enrouteCanvasMultiple != attrs.i.contestRule.ruleValues.enrouteCanvasMultiple) {
            outln"""    !"""
            attrs.ret.modifynum++
        }
        outln"""    </div>"""
        outln"""    <br/>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.minenroutephotos')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="minEnroutePhotos" name="minEnroutePhotos" value="${fieldValue(bean:attrs.i,field:'minEnroutePhotos')}" tabIndex="9"/>"""
        if (attrs.i.minEnroutePhotos != attrs.i.contestRule.ruleValues.minEnroutePhotos) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.maxenroutephotos')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="maxEnroutePhotos" name="maxEnroutePhotos" value="${fieldValue(bean:attrs.i,field:'maxEnroutePhotos')}" tabIndex="10"/>"""
        if (attrs.i.maxEnroutePhotos != attrs.i.contestRule.ruleValues.maxEnroutePhotos) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.minenroutecanvas')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="minEnrouteCanvas" name="minEnrouteCanvas" value="${fieldValue(bean:attrs.i,field:'minEnrouteCanvas')}" tabIndex="11"/>"""
        if (attrs.i.minEnrouteCanvas != attrs.i.contestRule.ruleValues.minEnrouteCanvas) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.maxenroutecanvas')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="maxEnrouteCanvas" name="maxEnrouteCanvas" value="${fieldValue(bean:attrs.i,field:'maxEnrouteCanvas')}" tabIndex="12"/>"""
        if (attrs.i.maxEnrouteCanvas != attrs.i.contestRule.ruleValues.maxEnrouteCanvas) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.minenroutetargets')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="minEnrouteTargets" name="minEnrouteTargets" value="${fieldValue(bean:attrs.i,field:'minEnrouteTargets')}" tabIndex="13"/>"""
        if (attrs.i.minEnrouteTargets != attrs.i.contestRule.ruleValues.minEnrouteTargets) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.maxenroutetargets')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="maxEnrouteTargets" name="maxEnrouteTargets" value="${fieldValue(bean:attrs.i,field:'maxEnrouteTargets')}" tabIndex="14"/>"""
        if (attrs.i.maxEnrouteTargets != attrs.i.contestRule.ruleValues.maxEnrouteTargets) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.contestrule.unsuitablestartnum')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="unsuitableStartNum" name="unsuitableStartNum" value="${fieldValue(bean:attrs.i,field:'unsuitableStartNum')}" tabIndex="15"/>"""
        if (attrs.i.unsuitableStartNum != attrs.i.contestRule.ruleValues.unsuitableStartNum) {
            outln"""        !"""
            attrs.ret.modifynum++
        }
        outln"""    </p>"""
        outln"""</fieldset>"""
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
        outln"""            <td class="value">${attrs.i.planningTestDirectionCorrectGrad}${message(code:'fc.grad')}</td>"""
        if (attrs.i.planningTestDirectionCorrectGrad != attrs.i.contestRule.ruleValues.planningTestDirectionCorrectGrad) {
            outln"""        <td class="modify">!</td>"""
        } else {
            outln"""        <td class="modify"/>"""
        }
        outln"""        </tr>"""
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox(String name, boolean checked, String label, String tabIndex)
    {
        outln"""<input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""            <input type="checkbox" id="${name}" name="${name}" checked="checked" tabIndex="${tabIndex}"/>"""
        } else {
            outln"""            <input type="checkbox" id="${name}" name="${name}" tabIndex="${tabIndex}"/>"""
        }
        outln"""<label>${message(code:label)}</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private radioEntry(String name, def value, boolean checked, String label, String tabIndex)
    {
        if (checked) {
            outln"""    <label><input type="radio" name="${name}" value="${value}" checked="checked" tabIndex="${tabIndex}"/>${label}</label>"""
        } else {
            outln"""    <label><input type="radio" name="${name}" value="${value}" tabIndex="${tabIndex}"/>${label}</label>"""
        }
    }
    
	// --------------------------------------------------------------------------------------------------------------------
	private void outln(str)
	{
		out << """$str
"""
	}

}
