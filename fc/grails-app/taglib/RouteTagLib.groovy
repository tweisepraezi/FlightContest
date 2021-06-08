class RouteTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def editCoordRoute = { attrs, body ->
        edit_coord_latitude(attrs.coordRoute, attrs)
        edit_coord_longitude(attrs.coordRoute, attrs)
        outln"""<fieldset>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:attrs.coordRoute,field:'altitude')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="gatewidth2" name="gatewidth2" value="${fieldValue(bean:attrs.coordRoute,field:'gatewidth2')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        if (attrs.coordRoute.type.IsRunwayCoord()) {
            outln"""<p>"""
            outln"""    <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>"""
            outln"""    <br/>"""
            outln"""    <input type="text" id="gateDirection" name="gateDirection" value="${fieldValue(bean:attrs.coordRoute,field:'gateDirection')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""</p>"""
        }
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.legduration')} [${message(code:'fc.time.min')}]:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="legDuration" name="legDuration" value="${fieldValue(bean:attrs.coordRoute,field:'legDuration')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        outln"""    <div>"""
        checkBox("noTimeCheck", attrs.coordRoute.noTimeCheck, 'fc.notimecheck', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("noGateCheck", attrs.coordRoute.noGateCheck, 'fc.nogatecheck', attrs)
        outln"""    </div>"""
        if (!attrs.secret) {
            outln"""<div>"""
            checkBox("noPlanningTest", attrs.coordRoute.noPlanningTest, 'fc.noplanningtest', attrs)
            outln"""</div>"""
            outln"""<div>"""
            checkBox("endCurved", attrs.coordRoute.endCurved, 'fc.endcurved.long', attrs)
            outln"""</div>"""
        }
        if (attrs.secret) {
            outln"""<div>"""
            checkBox("circleCenter", attrs.coordRoute.circleCenter, 'fc.circlecenter', attrs)
            outln"""</div>"""
            outln"""<div>"""
            checkBox("semiCircleInvert", attrs.coordRoute.semiCircleInvert, 'fc.semicircleinvert', attrs)
            outln"""</div>"""
        }
        // neue Felder funktionieren erst nach grails clean
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editCoordTurnpointPhoto = { attrs, body ->
        if (attrs.coordRoute.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto || attrs.coordRoute.assignedSign != TurnpointSign.NoSign) {
            outln"""<fieldset>"""
            outln"""    <div>"""
            checkBox("observationNextPrintPage", attrs.coordRoute.observationNextPrintPage, 'fc.observation.printnextpage', attrs)
            outln"""    </div>"""
            outln"""</fieldset>"""
            if (attrs.coordRoute.imagecoord) {
                outln"""<fieldset>"""
                outln"""    <div class="photo">"""
                outln"""        <img class="photo" id="photo_img_id" src="/fc/route/get_turnpoint_photo/${attrs.coordRoute.id}" style="width:${attrs.coordRoute.route.turnpointPrintStyle.width}px; height:${attrs.coordRoute.route.turnpointPrintStyle.height}px;"/>"""
                if (attrs.coordRoute.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto) {
                    outln"""    <div class="phototext">${attrs.coordRoute.titlePrintCode()}</div>"""
                } else {
                    outln"""    <div class="phototext">${attrs.coordRoute.assignedSign.title}</div>"""
                }
                outln"""        <div class="photoposition" id="photo_position_id" style="top:${attrs.coordRoute.observationPositionTop}px; left:${attrs.coordRoute.observationPositionLeft}px;"></div>"""
                outln"""    </div>"""
                outln"""</fieldset>"""
                outln"""<script>"""
                outln"""    \$(document).on('click', '#photo_img_id', function(e) {"""
                outln"""        var left = e.offsetX - \$("#photo_position_id").width()/2;"""
                outln"""        var top = e.offsetY - \$("#photo_position_id").height()/2;"""
                outln"""        window.location.href = "/fc/coordRoute/setposition/${attrs.coordRoute.id}?left=" + left + "&top=" + top + "&next=${attrs.next}";"""
                outln"""    });"""
                outln"""    \$(document).on('click', '#photo_position_id', function(e) {"""
                outln"""        var left = e.offsetX + \$("#photo_position_id").position().left - \$("#photo_position_id").width()/2;"""
                outln"""        var top = e.offsetY + \$("#photo_position_id").position().top - \$("#photo_position_id").height()/2;"""
                outln"""        window.location.href = "/fc/coordRoute/setposition/${attrs.coordRoute.id}?left=" + left + "&top=" + top + "&next=${attrs.next}";"""
                outln"""    });"""
                outln"""</script>"""
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editCoordEnroutePhoto = { attrs, body ->
        outln"""<fieldset>"""
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.observation.enroute.photo.name')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <input type="text" id="enroutePhotoName" name="enroutePhotoName" value="${fieldValue(bean:attrs.coordEnroute,field:'enroutePhotoName')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""    </p>"""
        if (attrs.create && (attrs.coordEnroute.route.enroutePhotoRoute == EnrouteRoute.InputName)) {
            outln"""<p>"""
            outln"""    <label>${message(code:'fc.observation.enroute.photo.lastname')}:</label>"""
            outln"""    <br/>"""
            outln"""    <input type="text" id="enrouteLastPhotoName" name="enrouteLastPhotoName" value="${fieldValue(bean:attrs.coordEnroute,field:'enrouteLastPhotoName')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""</p>"""
            outln"""<p>${message(code:'fc.observation.enroute.photo.rangeinfo')}</p>"""
        }
        outln"""</fieldset>"""
        if (attrs.coordEnroute.route.enroutePhotoRoute.IsEnrouteRouteInputCoord()) {
            edit_coord_latitude(attrs.coordEnroute, attrs)
            edit_coord_longitude(attrs.coordEnroute, attrs)
        }
        if (attrs.coordEnroute.route.enroutePhotoRoute == EnrouteRoute.InputNMFromTP) {
            outln"""<fieldset>"""
            edit_coord_lasttp(attrs.coordEnroute, attrs)
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.distance.fromlasttp')}* [${message(code:'fc.mile')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="enrouteDistance" name="enrouteDistance" value="${fieldValue(bean:attrs.coordEnroute,field:'enrouteDistance')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
        if (attrs.coordEnroute.route.enroutePhotoRoute == EnrouteRoute.InputmmFromTP) {
            outln"""<fieldset>"""
            edit_coord_lasttp(attrs.coordEnroute, attrs)
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.distance.fromlasttp')}* [${message(code:'fc.mm')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:attrs.coordEnroute,field:'measureDistance')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
        if (!attrs.create && (attrs.coordEnroute.route.enroutePhotoRoute == EnrouteRoute.InputCoordmm)) {
            outln"""<fieldset>"""
            outln"""    <table>"""
            outln"""        <tbody>"""
            outln"""            <tr>"""
            outln"""                <td class="detailtitle">${message(code:'fc.distance.fromlasttp.coord')}:</td>"""
            outln"""                <td>${FcMath.DistanceMeasureStr(attrs.coordEnroute.coordMeasureDistance)}${message(code:'fc.mm')}</td>"""
            outln"""            </tr>"""
            outln"""            <tr>"""
            outln"""                <td class="detailtitle">${message(code:'fc.distance.lasttp')}:</td>"""
            outln"""                <td>${attrs.coordEnroute.titleCode()}</td>"""
            outln"""            </tr>"""
            outln"""        </tbody>"""
            outln"""    </table>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.distance.fromlasttp')} [${message(code:'fc.mm')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:attrs.coordEnroute,field:'measureDistance')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
        if (!attrs.create) {
            outln"""<fieldset>"""
            outln"""    <div>"""
            checkBox("observationNextPrintPage", attrs.coordEnroute.observationNextPrintPage, 'fc.observation.printnextpage', attrs)
            outln"""    </div>"""
            outln"""</fieldset>"""
            if (attrs.coordEnroute.imagecoord) {
                outln"""<fieldset>"""
                outln"""    <div class="photo">"""
                outln"""        <img class="photo" id="photo_img_id" src="/fc/route/get_enroute_photo/${attrs.coordEnroute.id}" style="width:${attrs.coordEnroute.route.enroutePhotoPrintStyle.width}px; height:${attrs.coordEnroute.route.enroutePhotoPrintStyle.height}px;"/>"""
                outln"""        <div class="phototext">${attrs.coordEnroute.enroutePhotoName}</div>"""
                outln"""        <div class="photoposition" id="photo_position_id" style="top:${attrs.coordEnroute.observationPositionTop}px; left:${attrs.coordEnroute.observationPositionLeft}px;"></div>"""
                outln"""    </div>"""
                outln"""</fieldset>"""
                outln"""<script>"""
                outln"""    \$(document).on('click', '#photo_img_id', function(e) {"""
                outln"""        var left = e.offsetX - \$("#photo_position_id").width()/2;"""
                outln"""        var top = e.offsetY - \$("#photo_position_id").height()/2;"""
                outln"""        window.location.href = "/fc/coordEnroutePhoto/setposition/${attrs.coordEnroute.id}?left=" + left + "&top=" + top + "&next=${attrs.next}";"""
                outln"""    });"""
                outln"""    \$(document).on('click', '#photo_position_id', function(e) {"""
                outln"""        var left = e.offsetX + \$("#photo_position_id").position().left - \$("#photo_position_id").width()/2;"""
                outln"""        var top = e.offsetY + \$("#photo_position_id").position().top - \$("#photo_position_id").height()/2;"""
                outln"""        window.location.href = "/fc/coordEnroutePhoto/setposition/${attrs.coordEnroute.id}?left=" + left + "&top=" + top + "&next=${attrs.next}";"""
                outln"""    });"""
                outln"""</script>"""
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editCoordEnrouteCanvas = { attrs, body ->
        outln"""<fieldset>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.enroute.canvas.sign')}*:</label>"""
        outln"""        <br/>"""
        boolean add_tabindex = false
        for (EnrouteCanvasSign v in EnrouteCanvasSign.values()) {
            if (attrs.create && (attrs.coordEnroute.route.enrouteCanvasRoute == EnrouteRoute.InputName)) {
                if (v != EnrouteCanvasSign.None && !(v in attrs.coordEnroute.route.contest.contestRule.ruleValues.printIgnoreEnrouteCanvas) && (attrs.coordEnroute.route.contest.enrouteCanvasMultiple ||!attrs.coordEnroute.route.IsEnrouteCanvasSignUsed(v))) {
                    checkBoxImg("enrouteCanvasSign_${v.canvasName}", v.canvasName, createLinkTo(dir:'',file:v.imageName), attrs)
                }
            } else {
                if (v == EnrouteCanvasSign.None || (v in attrs.coordEnroute.route.contest.contestRule.ruleValues.printIgnoreEnrouteCanvas)) {
                    if (attrs.create) {
                        if (attrs.coordEnroute.enrouteCanvasSign == v) {
                            outln"""<label><input type="radio" name="enrouteCanvasSign" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${v}</label>"""
                        } else {
                            outln"""<label><input type="radio" name="enrouteCanvasSign" value="${v}" tabIndex="${attrs.ti[0]}"/>${v}</label>"""
                        }
                        add_tabindex = true
                    }
                } else {
                    if (attrs.coordEnroute.enrouteCanvasSign == v) {
                        outln"""<label><input type="radio" name="enrouteCanvasSign" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/><img src="${createLinkTo(dir:'',file:v.imageName)}" style="height:16px;"/> ${v.canvasName}</label>"""
                        add_tabindex = true
                    } else if (attrs.coordEnroute.route.contest.enrouteCanvasMultiple || !attrs.coordEnroute.route.IsEnrouteCanvasSignUsed(v)) {
                        outln"""<label><input type="radio" name="enrouteCanvasSign" value="${v}" tabIndex="${attrs.ti[0]}"/><img src="${createLinkTo(dir:'',file:v.imageName)}" style="height:16px;"/> ${v.canvasName}</label>"""
                        add_tabindex = true
                    }
                }
            }
        }
        if (add_tabindex) {
            attrs.ti[0]++
        }
        outln"""    </div>"""
        outln"""</fieldset>"""
        if (attrs.coordEnroute.route.enrouteCanvasRoute.IsEnrouteRouteInputCoord()) {
            edit_coord_latitude(attrs.coordEnroute, attrs)
            edit_coord_longitude(attrs.coordEnroute, attrs)
        }
        if (attrs.coordEnroute.route.enrouteCanvasRoute == EnrouteRoute.InputNMFromTP) {
            outln"""<fieldset>"""
            edit_coord_lasttp(attrs.coordEnroute, attrs)
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.distance.fromlasttp')}* [${message(code:'fc.mile')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="enrouteDistance" name="enrouteDistance" value="${fieldValue(bean:attrs.coordEnroute,field:'enrouteDistance')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
        if (attrs.coordEnroute.route.enrouteCanvasRoute == EnrouteRoute.InputmmFromTP) {
            outln"""<fieldset>"""
            edit_coord_lasttp(attrs.coordEnroute, attrs)
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.distance.fromlasttp')}* [${message(code:'fc.mm')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:attrs.coordEnroute,field:'measureDistance')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
        if (!attrs.create && (attrs.coordEnroute.route.enrouteCanvasRoute == EnrouteRoute.InputCoordmm)) {
            outln"""<fieldset>"""
            outln"""    <table>"""
            outln"""        <tbody>"""
            outln"""            <tr>"""
            outln"""                <td class="detailtitle">${message(code:'fc.distance.fromlasttp.coord')}:</td>"""
            outln"""                <td>${FcMath.DistanceMeasureStr(attrs.coordEnroute.coordMeasureDistance)}${message(code:'fc.mm')}</td>"""
            outln"""            </tr>"""
            outln"""            <tr>"""
            outln"""                <td class="detailtitle">${message(code:'fc.distance.lasttp')}:</td>"""
            outln"""                <td>${attrs.coordEnroute.titleCode()}</td>"""
            outln"""            </tr>"""
            outln"""        </tbody>"""
            outln"""    </table>"""
            outln"""    <p>"""
            outln"""        <label>${message(code:'fc.distance.fromlasttp')} [${message(code:'fc.mm')}]:</label>"""
            outln"""        <br/>"""
            outln"""        <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:attrs.coordEnroute,field:'measureDistance')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    </p>"""
            outln"""</fieldset>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void edit_coord_latitude(Coord coordValue, attrs)
    {
        outln"""<fieldset>"""
        outln"""    <legend>${message(code:'fc.latitude')}*</legend>"""
        outln"""    <div>"""
        if (coordValue.route.contest.coordPresentation == CoordPresentation.DEGREE) {
            outln"""    <input type="text" id="latGradDecimal" name="latGradDecimal" value="${coordValue.latGradDecimal.toFloat()}"  tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.grad')}</label>"""
        } else if (coordValue.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTE) {
            outln"""    <select class="direction" id="latDirection" name="latDirection" tabIndex="${attrs.ti[0]++}">"""
            for (String direction in coordValue.constraints.latDirection.inList) {
                if (direction == coordValue.latDirection) {
                    outln"""<option selected="selected">"""
                } else {
                    outln"""<option>"""
                }
                outln"""        ${direction}"""
                outln"""    </option>"""
            }
            outln"""    </select>"""
            outln"""    <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordValue,field:'latGrad')}"  tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.grad')}</label>"""
            outln"""    <input type="text" id="latMinute" name="latMinute" value="${coordValue.latMinute.toFloat()}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.min')}</label>"""
        } else if (coordValue.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTESECOND) {
            outln"""    <select class="direction" id="latDirection" name="latDirection" tabIndex="${attrs.ti[0]++}">"""
            for (String direction in coordValue.constraints.latDirection.inList) {
                if (direction == coordValue.latDirection) {
                    outln"""<option selected="selected">"""
                } else {
                    outln"""<option>"""
                }
                outln"""        ${direction}"""
                outln"""    </option>"""
            }
            outln"""    </select>"""
            outln"""    <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordValue,field:'latGrad')}"  tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.grad')}</label>"""
            outln"""    <input class="minute" type="text" id="latMin" name="latMin" value="${fieldValue(bean:coordValue,field:'latMin')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.min')}</label>"""
            outln"""    <input type="text" id="latSecondDecimal" name="latSecondDecimal" value="${coordValue.latSecondDecimal.toFloat()}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.sec')}</label>"""
        }
        outln"""    </div>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void edit_coord_longitude(Coord coordValue, attrs)
    {
        outln"""<fieldset>"""
        outln"""    <legend>${message(code:'fc.longitude')}*</legend>"""
        outln"""    <div>"""
        if (coordValue.route.contest.coordPresentation == CoordPresentation.DEGREE) {
            outln"""    <input type="text" id="lonGradDecimal" name="lonGradDecimal" value="${coordValue.lonGradDecimal.toFloat()}"  tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.grad')}</label>"""
        } else if (coordValue.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTE) {
            outln"""    <select class="direction" id="lonDirection" name="lonDirection" tabIndex="${attrs.ti[0]++}">"""
            for (String direction in coordValue.constraints.lonDirection.inList) {
                if (direction == coordValue.lonDirection) {
                    outln"""<option selected="selected">"""
                } else {
                    outln"""<option>"""
                }
                outln"""        ${direction}"""
                outln"""    </option>"""
            }
            outln"""    </select>"""
            outln"""    <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordValue,field:'lonGrad')}"  tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.grad')}</label>"""
            outln"""    <input type="text" id="lonMinute" name="lonMinute" value="${coordValue.lonMinute.toFloat()}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.min')}</label>"""
        } else if (coordValue.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTESECOND) {
            outln"""    <select class="direction" id="lonDirection" name="lonDirection" tabIndex="${attrs.ti[0]++}">"""
            for (String direction in coordValue.constraints.lonDirection.inList) {
                if (direction == coordValue.lonDirection) {
                    outln"""<option selected="selected">"""
                } else {
                    outln"""<option>"""
                }
                outln"""        ${direction}"""
                outln"""    </option>"""
            }
            outln"""    </select>"""
            outln"""    <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordValue,field:'lonGrad')}"  tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.grad')}</label>"""
            outln"""    <input class="minute" type="text" id="lonMin" name="lonMin" value="${fieldValue(bean:coordValue,field:'lonMin')}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.min')}</label>"""
            outln"""    <input type="text" id="lonSecondDecimal" name="lonSecondDecimal" value="${coordValue.lonSecondDecimal.toFloat()}" tabIndex="${attrs.ti[0]++}"/>"""
            outln"""    <label>${message(code:'fc.sec')}</label>"""
        }
        outln"""    </div>"""
        outln"""</fieldset>"""
    }

    // --------------------------------------------------------------------------------------------------------------------
    private void edit_coord_lasttp(Coord coordValue, attrs)
    {
        outln"""    <p>"""
        outln"""        <label>${message(code:'fc.distance.lasttp')}*:</label>"""
        outln"""        <br/>"""
        outln"""        <select name="enrouteCoordTitle" tabIndex="${attrs.ti[0]++}">"""
        for (CoordTitle coord_title in coordValue.route.GetEnrouteCoordTitles(false)) {
            if ((coord_title.type == coordValue.type) && (coord_title.number == coordValue.titleNumber)) {
                outln"""    <option value="${coord_title}" selected="selected">"""
            } else {
                outln"""    <option value="${coord_title}">"""
            }
            outln"""            ${coord_title.titleCode()}"""
            outln"""        </option>"""
        }
        outln"""        </select>"""
        outln"""    </p>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editRouteUseProcedureTurns = { attrs, body ->
        outln"""<fieldset>"""
        outln"""    <div>"""
        checkBox("useProcedureTurns", attrs.route.useProcedureTurns, 'fc.route.useprocedureturns', attrs)
        outln"""    </div>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editRouteLiveTrackingScorecard = { attrs, body ->
        outln"""<fieldset>"""
        outln"""<p>"""
        //outln"""    <p class="group">${message(code:'fc.livetracking')}</p>"""
        outln"""    <label>${message(code:'fc.general.livetrackingscorecard')}:</label>"""
        outln"""    <br/>"""
        outln"""    <input type="text" id="liveTrackingScorecard" name="liveTrackingScorecard" value="${fieldValue(bean:attrs.route,field:'liveTrackingScorecard')}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""</p>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editRouteSpecialSettings = { attrs, body ->
        outln"""<fieldset>"""
        outln"""    <div>"""
        checkBox("showCurvedPoints", attrs.route.showCurvedPoints, 'fc.route.showcurvedpoints', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        checkBox("exportSemicircleGates", attrs.route.exportSemicircleGates, 'fc.route.exportsemicirclegates', attrs)
        outln"""    </div>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def editRouteObservations = { attrs, body ->
        outln"""<fieldset>"""
        outln"""    <p class="group">${message(code:'fc.observation.turnpoint')}</p>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.input')}*:</label>"""
        outln"""        <br/>"""
        for (TurnpointRoute v in TurnpointRoute.values()) {
            if (v != TurnpointRoute.Unassigned) {
                if (attrs.route.turnpointRoute == v) {
                    outln"""<label><input type="radio" name="turnpointRoute" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                } else {
                    outln"""<label><input type="radio" name="turnpointRoute" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                }
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""    <div>"""
        outln"""        <br/>"""
        outln"""        <label>${message(code:'fc.observation.measurement')}*:</label>"""
        outln"""        <br/>"""
        checkBox("turnpointMapMeasurement", attrs.route.turnpointMapMeasurement, 'fc.observation.turnpoint.mapmeasurement', attrs)
        outln"""    </div>"""
        outln"""    <div>"""
        outln"""        <br/>"""
        outln"""        <label>${message(code:'fc.observation.printstyle')}*:</label>"""
        outln"""        <br/>"""
        for (ObservationPrintStyle v in ObservationPrintStyle.values()) {
            if (attrs.route.turnpointPrintStyle == v) {
                outln"""<label><input type="radio" name="turnpointPrintStyle" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
            } else {
                outln"""<label><input type="radio" name="turnpointPrintStyle" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        outln"""    <p class="group">${message(code:'fc.observation.enroute.photo')}</p>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.input')}*:</label>"""
        outln"""        <br/>"""
        for (EnrouteRoute v in EnrouteRoute.values()) {
            if (v != EnrouteRoute.Unassigned) {
                if (attrs.route.enroutePhotoRoute == v) {
                    outln"""<label><input type="radio" name="enroutePhotoRoute" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                } else {
                    outln"""<label><input type="radio" name="enroutePhotoRoute" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                }
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""    <div>"""
        outln"""        <br/>"""
        outln"""        <label>${message(code:'fc.observation.measurement')}*:</label>"""
        outln"""        <br/>"""
        for (EnrouteMeasurement v in EnrouteMeasurement.values()) {
            if (v != EnrouteMeasurement.Unassigned) {
                if (attrs.route.enroutePhotoMeasurement == v) {
                    outln"""<label><input type="radio" name="enroutePhotoMeasurement" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                } else {
                    outln"""<label><input type="radio" name="enroutePhotoMeasurement" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                }
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""    <div>"""
        outln"""        <br/>"""
        outln"""        <label>${message(code:'fc.observation.printstyle')}*:</label>"""
        outln"""        <br/>"""
        for (ObservationPrintStyle v in ObservationPrintStyle.values()) {
            if (attrs.route.enroutePhotoPrintStyle == v) {
                outln"""<label><input type="radio" name="enroutePhotoPrintStyle" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
            } else {
                outln"""<label><input type="radio" name="enroutePhotoPrintStyle" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""</fieldset>"""
        outln"""<fieldset>"""
        outln"""    <p class="group">${message(code:'fc.observation.enroute.canvas')}</p>"""
        outln"""    <div>"""
        outln"""        <label>${message(code:'fc.observation.input')}*:</label>"""
        outln"""        <br/>"""
        for (EnrouteRoute v in EnrouteRoute.values()) {
            if (v != EnrouteRoute.Unassigned) {
                if (attrs.route.enrouteCanvasRoute == v) {
                    outln"""<label><input type="radio" name="enrouteCanvasRoute" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                } else {
                    outln"""<label><input type="radio" name="enrouteCanvasRoute" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                }
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""    <div>"""
        outln"""        <br/>"""
        outln"""        <label>${message(code:'fc.observation.measurement')}*:</label>"""
        outln"""        <br/>"""
        for (EnrouteMeasurement v in EnrouteMeasurement.values()) {
            if (v != EnrouteMeasurement.Unassigned) {
                if (attrs.route.enrouteCanvasMeasurement == v) {
                    outln"""<label><input type="radio" name="enrouteCanvasMeasurement" value="${v}" checked="checked" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                } else {
                    outln"""<label><input type="radio" name="enrouteCanvasMeasurement" value="${v}" tabIndex="${attrs.ti[0]}"/>${message(code:v.code)}</label>"""
                }
            }
        }
        attrs.ti[0]++
        outln"""    </div>"""
        outln"""</fieldset>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def showRouteDetails = { attrs, body ->
        // Koordinaten
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        if (attrs.route.turnpointRoute.IsTurnpointSign()) {
            if (attrs.route.turnpointRoute.IsTurnpointPhoto()) {
                outln"""    <th class="table-head" colspan=16">${message(code:'fc.coordroute.list')}</th>"""
            } else {
                outln"""    <th class="table-head" colspan=15">${message(code:'fc.coordroute.list')}</th>"""
            }
        } else {
            outln"""        <th class="table-head" colspan=14">${message(code:'fc.coordroute.list')}</th>"""
        }
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <th>${message(code:'fc.number')}</th>"""
        outln"""            <th>${message(code:'fc.title')}</th>"""
        outln"""            <th>${message(code:'fc.latitude')}</th>"""
        outln"""            <th>${message(code:'fc.longitude')}</th>"""
        outln"""            <th>${message(code:'fc.altitude')}</th>"""
        outln"""            <th>${message(code:'fc.gatewidth')}</th>"""
        outln"""            <th>${message(code:'fc.gatedirection')}</th>"""
        outln"""            <th>${message(code:'fc.legduration')}</th>"""
        outln"""            <th>${message(code:'fc.notimecheck')}</th>"""
        outln"""            <th>${message(code:'fc.nogatecheck')}</th>"""
        outln"""            <th>${message(code:'fc.noplanningtest')}</th>"""
        outln"""            <th>${message(code:'fc.truetrack.map.measure')}</th>"""
        outln"""            <th>${message(code:'fc.distance.map')}</th>"""
        if (attrs.route.turnpointRoute.IsTurnpointSign()) {
            if (attrs.route.turnpointRoute == TurnpointRoute.AssignPhoto) {
                outln"""    <th>${message(code:'fc.observation.turnpoint.photo.short')}</th>"""
            } else if (attrs.route.turnpointRoute == TurnpointRoute.AssignCanvas) {
                outln"""    <th>${message(code:'fc.observation.turnpoint.canvas.short')}</th>"""
            } else if (attrs.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto) {
                outln"""    <th>${message(code:'fc.observation.turnpoint.photo.short')}</th>"""
            }
            if (attrs.route.turnpointRoute.IsTurnpointPhoto()) {
                outln"""    <th>${message(code:'fc.observation.printnextpage.short')}</th>"""
            }
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        BigDecimal last_measuretruetrack = 0.0
        BigDecimal last_measuredistance = 0.0
        int i = 0
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(attrs.route,[sort:'id'])) {
            long next_id = 0
            i++
            boolean set_next_id = false
            for (CoordRoute coordroute_instance2 in CoordRoute.findAllByRoute(attrs.route,[sort:'id'])) {
                if (set_next_id) {
                    next_id = coordroute_instance2.id
                    set_next_id = false
                }
                if (coordroute_instance2 == coordroute_instance) {
                    set_next_id = true
                }
            }
            outln"""    <tr class="${coordroute_instance.type in [CoordType.SP,CoordType.TP,CoordType.FP] ? '' : 'odd'}">"""
            String s = """  <td style="white-space: nowrap;">"""
            s += coordroutenum(coordroute_instance, i, next_id, "${createLink(controller:'coordRoute',action:'edit')}", true)
            s += """        </td>"""
            outln s
            s = """         <td style="white-space: nowrap;">${coordroute_instance.titleWithRatio()}"""
            if (coordroute_instance.planProcedureTurn) {
                s += """ (${message(code:'fc.procedureturn.symbol')})"""
            }
            if (coordroute_instance.endCurved) {
               s += """${message(code:'fc.endcurved')}"""
            }
            if (coordroute_instance.circleCenter) {
                if (coordroute_instance.semiCircleInvert) {
                   s += """${message(code:'fc.semicircleinvert.symbol')}"""
                }
                s += """${message(code:'fc.circlecenter.symbol')}"""
            }
            s += """        </td>"""
            outln s
            outln"""        <td style="white-space: nowrap;">${coordroute_instance.latName()}</td>"""
            outln"""        <td style="white-space: nowrap;">${coordroute_instance.lonName()}</td>"""
            outln"""        <td>${coordroute_instance.altitude}${message(code:'fc.foot')}</td>"""
            outln"""        <td>${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>"""
            if (coordroute_instance.type.IsRunwayCoord()) {
                outln"""    <td>${coordroute_instance.gateDirection}${message(code:'fc.grad')}</td>"""
            } else {
                outln"""    <td>-</td>"""
            }
            outln"""        <td>${coordroute_instance.legDurationName()}</td>"""
            if (coordroute_instance.noTimeCheck) {
                outln"""    <td>${message(code:'fc.yes')}</td>"""
            } else {
                outln"""    <td>-</td>"""
            }
            if (coordroute_instance.noGateCheck) {
                outln"""    <td>${message(code:'fc.yes')}</td>"""
            } else {
                outln"""    <td>-</td>"""
            }
            if (coordroute_instance.noPlanningTest) {
                outln"""    <td>${message(code:'fc.yes')}</td>"""
            } else {
                outln"""    <td>-</td>"""
            }
            if (last_measuretruetrack && last_measuretruetrack != coordroute_instance.measureTrueTrack) {
                outln"""    <td class="errors">${coordroute_instance.measureTrueTrackName()} !</td>"""
            } else {
                outln"""    <td>${coordroute_instance.measureTrueTrackName()}</td>"""
            }
            if (last_measuredistance && last_measuredistance >= coordroute_instance.measureDistance) {
                outln"""    <td class="errors">${coordroute_instance.measureDistanceName()} !</td>"""
            } else {
                outln"""    <td>${coordroute_instance.measureDistanceName()}</td>"""
            }
            if (attrs.route.turnpointRoute.IsTurnpointSign()) {
                outln"""    <td>${coordroute_instance.GetTurnpointSign()}</td>"""
                if (attrs.route.turnpointRoute.IsTurnpointPhoto()) {
                    if (coordroute_instance.observationNextPrintPage) {
                        outln"""<td>${message(code:'fc.yes')}</td>"""
                    } else {
                        outln"""<td>-</td>"""
                    }
                }
            }
            outln"""    </tr>"""
            if (coordroute_instance.type == CoordType.SECRET) {
                last_measuretruetrack = coordroute_instance.measureTrueTrack
            } else {
                last_measuretruetrack = 0.0
            }
            if (coordroute_instance.type == CoordType.SECRET) {
                last_measuredistance = coordroute_instance.measureDistance
            } else {
                last_measuredistance = 0.0
            }
        }
        outln"""    </tbody>"""
        outln"""</table>"""
        
        // Auswerte-Etappen
        outln"""<table>"""
        BigDecimal total_distance = 0.0
        outln"""    <thead>"""
        outln"""        <tr>"""
        outln"""            <th class="table-head" colspan="6">${message(code:'fc.routelegcoord.list')}</th>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <th>${message(code:'fc.number')}</th>"""
        outln"""            <th>${message(code:'fc.title')}</th>"""
        outln"""            <th>${message(code:'fc.truetrack.coord')}</th>"""
        outln"""            <th>${message(code:'fc.truetrack.map.measure')}</th>"""
        outln"""            <th>${message(code:'fc.distance.coord')}</th>"""
        outln"""            <th>${message(code:'fc.distance.map')}</th>"""
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        i = 0
        int num = 0
        for (RouteLegCoord routeleg_instance in RouteLegCoord.findAllByRoute(attrs.route,[sort:'id'])) {
            num++
            i++
            total_distance = FcMath.AddDistance(total_distance,routeleg_instance.testDistance())
            BigDecimal course_change = AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())
            if (course_change.abs() >= 90) {
                outln"""<tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
                String s = """<td class="center" colspan="6">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}"""
                if (routeleg_instance.IsProcedureTurn()) {
                    if (attrs.route.useProcedureTurns) {
                        s += """ (${message(code:'fc.procedureturn')})"""
                    } else {
                        s += """ (${message(code:'fc.procedureturn.disabled')})"""
                    }
                }
                s += """      </td>"""
                outln s
                outln"""</tr>"""
                i++
            }
            outln"""    <tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
            outln"""        <td>${num}</td>"""
            String s = """  <td>${routeleg_instance.GetTitle()}"""
            if (routeleg_instance.endCurved) {
                s += """${message(code:'fc.endcurved')}"""
            }
            s += """        </td>"""
            outln s
            outln"""        <td>${routeleg_instance.coordTrueTrackName()}</td>"""
            outln"""        <td>${routeleg_instance.mapMeasureTrueTrackName()}</td>"""
            outln"""        <td>${routeleg_instance.coordDistanceName()}</td>"""
            outln"""        <td>${routeleg_instance.mapDistanceName()} (${routeleg_instance.mapMeasureDistanceName()})</td>"""
            outln"""    </tr>"""
        }
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td colspan="6">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
        
        // Test-Etappen
        outln"""<table>"""
        total_distance = 0.0
        outln"""    <thead>"""
        outln"""        <tr>"""
        outln"""            <th class="table-head" colspan="6">${message(code:'fc.routelegtest.list')}</th>"""
        outln"""        </tr>"""
        outln"""        <tr>"""
        outln"""            <th>${message(code:'fc.number')}</th>"""
        outln"""            <th>${message(code:'fc.title')}</th>"""
        outln"""            <th>${message(code:'fc.truetrack.coord')}</th>"""
        outln"""            <th>${message(code:'fc.truetrack.map.measure')}</th>"""
        outln"""            <th>${message(code:'fc.distance.coord')}</th>"""
        outln"""            <th>${message(code:'fc.distance.map')}</th>"""
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        i = 0
        num = 0
        for (RouteLegTest routeleg_instance in RouteLegTest.findAllByRoute(attrs.route,[sort:'id'])) {
            num++
            i++
            total_distance = FcMath.AddDistance(total_distance,routeleg_instance.testDistance())
            BigDecimal course_change = AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())
            if (course_change.abs() >= 90) {
                outln"""<tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
                String s = """<td class="center" colspan="6">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}"""
                if (routeleg_instance.IsProcedureTurn()) {
                    if (attrs.route.useProcedureTurns) {
                        s += """ (${message(code:'fc.procedureturn')})"""
                    } else {
                        s += """ (${message(code:'fc.procedureturn.disabled')})"""
                    }
                }
                s += """      </td>"""
                outln s
                outln"""</tr>"""
                i++
            }
            outln"""    <tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
            outln"""        <td>${num}</td>"""
            String s = """  <td>${routeleg_instance.GetTitle()}"""
            if (routeleg_instance.endCurved) {
                s += """${message(code:'fc.endcurved')}"""
            }
            s += """        </td>"""
            outln s
            outln"""        <td>${routeleg_instance.coordTrueTrackName()}</td>"""
            outln"""        <td>${routeleg_instance.mapMeasureTrueTrackName()}</td>"""
            outln"""        <td>${routeleg_instance.coordDistanceName()}</td>"""
            outln"""        <td>${routeleg_instance.mapDistanceName()} (${routeleg_instance.mapMeasureDistanceName()})</td>"""
            outln"""    </tr>"""
        }
        outln"""    </tbody>"""
        outln"""    <tfoot>"""
        outln"""        <tr>"""
        outln"""            <td colspan="6">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>"""
        outln"""        </tr>"""
        outln"""    </tfoot>"""
        outln"""</table>"""
        
        // Strecken-Fotos
        if (attrs.route.enroutePhotoRoute.IsEnrouteRouteInput()) {
            outln"""<table>"""
            outln"""    <thead>"""
            outln"""        <tr>"""
            outln"""           <th class="table-head" colspan="9">${message(code:'fc.coordroute.photos')}</th>"""
            outln"""        </tr>"""
            outln"""        <tr>"""
            outln"""            <th>${message(code:'fc.number')}</th>"""
            outln"""            <th>${message(code:'fc.observation.enroute.photo.name')}</th>"""
            if (attrs.route.enroutePhotoRoute != EnrouteRoute.InputName) {
                outln"""        <th>${message(code:'fc.latitude')}</th>"""
                outln"""        <th>${message(code:'fc.longitude')}</th>"""
                outln"""        <th>${message(code:'fc.distance.lasttp')}</th>"""
                outln"""        <th>${message(code:'fc.observation.printnextpage.short')}</th>"""
                outln"""        <th colspan="2">${message(code:'fc.distance.fromlasttp')}</th>"""
                outln"""        <th>${message(code:'fc.distance.orthogonal')}</th>"""
            } else {
                outln"""        <th>${message(code:'fc.observation.printnextpage.short')}</th>"""
            }
            outln"""        </tr>"""
            outln"""    </thead>"""
            outln"""    <tbody>"""
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])) {
                long next_id = 0
                boolean set_next_id = false
                for (CoordEnroutePhoto coordenroutephoto_instance2 in CoordEnroutePhoto.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])) {
                    if (set_next_id) {
                        next_id = coordenroutephoto_instance2.id
                        set_next_id = false
                    }
                    if (coordenroutephoto_instance2 == coordenroutephoto_instance) {
                        set_next_id = true
                    }
                }
                outln"""    <tr class="${coordenroutephoto_instance.enrouteViewPos ? '' : 'odd'}">"""
                String s = """  <td style="white-space: nowrap;">"""
                s += coordroutenum(
                    coordenroutephoto_instance, coordenroutephoto_instance.enrouteViewPos, next_id, 
                    "${createLink(controller:'coordEnroutePhoto',action:'edit')}", attrs.route.enroutePhotoRoute == EnrouteRoute.InputCoordmm
                )
                if (coordenroutephoto_instance.route.enroutePhotoRoute == EnrouteRoute.InputName && !coordenroutephoto_instance.route.IsEnrouteSignUsed(true)) {
                    if (next_id) {
                        s += """ <a href="${createLink(controller:'coordEnroutePhoto',action:'addposition',params:[id:coordenroutephoto_instance.id])}">+</a>"""
                    }
                    if (coordenroutephoto_instance.enrouteViewPos > 1) {
                        s += """ <a href="${createLink(controller:'coordEnroutePhoto',action:'subposition',params:[id:coordenroutephoto_instance.id])}">-</a>"""
                    }
                }
                s += """        </td>"""
                outln s
                outln"""        <td style="white-space: nowrap;">${coordenroutephoto_instance.enroutePhotoName}</td>"""
                if (attrs.route.enroutePhotoRoute != EnrouteRoute.InputName) {
                    outln"""        <td style="white-space: nowrap;">${coordenroutephoto_instance.latName()}</td>"""
                    outln"""        <td style="white-space: nowrap;">${coordenroutephoto_instance.lonName()}</td>"""
                    if (coordenroutephoto_instance.type == CoordType.UNKNOWN) {
                        s = """ <td style="white-space: nowrap;" class="errors">"""
                    } else {
                        s = """ <td style="white-space: nowrap;">"""
                    }
                    s += """        ${coordenroutephoto_instance.titleCode()}"""
                    if (coordenroutephoto_instance.type == CoordType.UNKNOWN) {
                        s += """ !"""
                    }
                    s += """    </td>"""
                    outln s
                    if (coordenroutephoto_instance.observationNextPrintPage) {
                        outln"""    <td>${message(code:'fc.yes')}</td>"""
                    } else {
                        outln"""    <td>-</td>"""
                    }
                    if (coordenroutephoto_instance.enrouteDistance != null) {
                        if (coordenroutephoto_instance.enrouteDistanceOk) {
                            s = """ <td style="white-space: nowrap;">"""
                        } else {
                            s = """ <td style="white-space: nowrap;" class="errors">"""
                        }
                        s += """      ${FcMath.DistanceStr(coordenroutephoto_instance.enrouteDistance)}${message(code:'fc.mile')}"""
                        if (!coordenroutephoto_instance.enrouteDistanceOk) {
                            s += """ !"""
                        }
                        s += """</td>"""
                        outln s
                    } else {
                        outln"""<td style="white-space: nowrap;">-</td>"""
                    }
                    if (coordenroutephoto_instance.GetMeasureDistance() != null) {
                        outln"""<td style="white-space: nowrap;">${FcMath.DistanceMeasureStr(coordenroutephoto_instance.GetMeasureDistance())}${message(code:'fc.mm')}</td>"""
                    } else {
                        outln"""<td style="white-space: nowrap;">-</td>"""
                    }
                    outln"""    <td style="white-space: nowrap;">${coordenroutephoto_instance.GetEnrouteOrthogonalDistance()}</td>"""
                } else {
                    if (coordenroutephoto_instance.observationNextPrintPage) {
                        outln"""    <td>${message(code:'fc.yes')}</td>"""
                    } else {
                        outln"""    <td>-</td>"""
                    }
                }
                outln"""    </tr>"""
            }
            outln"""    </tbody>"""
            outln"""</table>"""
        }
        
        // Strecken-Bodenzeichen
        if (attrs.route.enrouteCanvasRoute.IsEnrouteRouteInput()) {
            outln"""<table>"""
            outln"""    <thead>"""
            outln"""        <tr>"""
            outln"""           <th class="table-head" colspan="8">${message(code:'fc.coordroute.canvas')}</th>"""
            outln"""        </tr>"""
            outln"""        <tr>"""
            outln"""            <th>${message(code:'fc.number')}</th>"""
            outln"""            <th>${message(code:'fc.observation.enroute.canvas.sign')}</th>"""
            if (attrs.route.enrouteCanvasRoute != EnrouteRoute.InputName) {
                outln"""            <th>${message(code:'fc.latitude')}</th>"""
                outln"""            <th>${message(code:'fc.longitude')}</th>"""
                outln"""            <th>${message(code:'fc.distance.lasttp')}</th>"""
                outln"""            <th colspan="2">${message(code:'fc.distance.fromlasttp')}</th>"""
                outln"""            <th>${message(code:'fc.distance.orthogonal')}</th>"""
            }
            outln"""        </tr>"""
            outln"""    </thead>"""
            outln"""    <tbody>"""
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])) {
                long next_id = 0
                boolean set_next_id = false
                for (CoordEnrouteCanvas coordenroutecanvas_instance2 in CoordEnrouteCanvas.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])) {
                    if (set_next_id) {
                        next_id = coordenroutecanvas_instance2.id
                        set_next_id = false
                    }
                    if (coordenroutecanvas_instance2 == coordenroutecanvas_instance) {
                        set_next_id = true
                    }
                }
                outln"""    <tr class="${coordenroutecanvas_instance.enrouteViewPos ? '' : 'odd'}">"""
                String s = """  <td style="white-space: nowrap;">"""
                s += coordroutenum(
                    coordenroutecanvas_instance, coordenroutecanvas_instance.enrouteViewPos, next_id, 
                    "${createLink(controller:'coordEnrouteCanvas',action:'edit')}", attrs.route.enrouteCanvasRoute == EnrouteRoute.InputCoordmm
                )
                if (coordenroutecanvas_instance.route.enrouteCanvasRoute == EnrouteRoute.InputName && !coordenroutecanvas_instance.route.IsEnrouteSignUsed(false)) {
                    if (next_id) {
                        s += """ <a href="${createLink(controller:'coordEnrouteCanvas',action:'addposition',params:[id:coordenroutecanvas_instance.id])}">+</a>"""
                    }
                    if (coordenroutecanvas_instance.enrouteViewPos > 1) {
                        s += """ <a href="${createLink(controller:'coordEnrouteCanvas',action:'subposition',params:[id:coordenroutecanvas_instance.id])}">-</a>"""
                    }
                }
                s += """        </td>"""
                outln s
                outln"""        <td style="white-space: nowrap;"><img src="${createLinkTo(dir:'',file:coordenroutecanvas_instance.enrouteCanvasSign.imageName)}" style="height:16px;"/> ${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}</td>"""
                
                if (attrs.route.enrouteCanvasRoute != EnrouteRoute.InputName) {
                    outln"""        <td style="white-space: nowrap;">${coordenroutecanvas_instance.latName()}</td>"""
                    outln"""        <td style="white-space: nowrap;">${coordenroutecanvas_instance.lonName()}</td>"""
                    if (coordenroutecanvas_instance.type == CoordType.UNKNOWN) {
                        s = """     <td style="white-space: nowrap;" class="errors">"""
                    } else {
                        s = """     <td style="white-space: nowrap;">"""
                    }
                    s += """          ${coordenroutecanvas_instance.titleCode()}"""
                    if (coordenroutecanvas_instance.type == CoordType.UNKNOWN) {
                        s += """ !"""
                    }
                    s += """        </td>"""
                    outln s
                    if (coordenroutecanvas_instance.enrouteDistance != null) {
                        if (coordenroutecanvas_instance.enrouteDistanceOk) {
                            s = """ <td style="white-space: nowrap;">"""
                        } else {
                            s = """ <td style="white-space: nowrap;" class="errors">"""
                        }
                        s += """      ${FcMath.DistanceStr(coordenroutecanvas_instance.enrouteDistance)}${message(code:'fc.mile')}"""
                        if (!coordenroutecanvas_instance.enrouteDistanceOk) {
                            s += """ !"""
                        }
                        s += """</td>"""
                        outln s
                    } else {
                        outln"""    <td style="white-space: nowrap;">-</td>"""
                    }
                    if (coordenroutecanvas_instance.GetMeasureDistance() != null) {
                        outln"""    <td style="white-space: nowrap;">${FcMath.DistanceMeasureStr(coordenroutecanvas_instance.GetMeasureDistance())}${message(code:'fc.mm')}</td>"""
                    } else {
                        outln"""    <td style="white-space: nowrap;">-</td>"""
                    }
                    s = """        <td style="white-space: nowrap;">${coordenroutecanvas_instance.enrouteOrthogonalDistance.abs()}${message(code:'fc.m')}"""
                    if (coordenroutecanvas_instance.enrouteOrthogonalDistance > 0) {
                         s += """ ${message(code:'fc.right')}"""
                    } else if (coordenroutecanvas_instance.enrouteOrthogonalDistance < 0) {
                         s += """ ${message(code:'fc.left')}"""   
                    }
                    s += """       </td>"""
                    outln s
                }
                outln"""    </tr>"""
            }
            outln"""    </tbody>"""
            outln"""</table>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private String coordroutenum(Coord coordInstance, int num, long nextId, String link, boolean showMeasure) {
        String t = num.toString()
        if (showMeasure && coordInstance.IsRouteMeasure()) {
            t += """ <img src="${createLinkTo(dir:'',file:'images/skin/ok.png')}"/>"""
        } else {
            t += " ..."
        }
        String ret = ""
        if (nextId) {
            ret = """<a href="${link}/${coordInstance.id}?next=${nextId}">${t}</a>""" // .encodeAsHTML()
        } else {
            ret = """<a href="${link}/${coordInstance.id}">${t}</a>""" // .encodeAsHTML()
        }
        return ret
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    def printCoordEnroutePhoto = { attrs, body ->

        int col_pos = 0
        int col_num = attrs.route.enroutePhotoPrintStyle.columns
        int row_pos = 0
        int row_num = attrs.route.enroutePhotoPrintStyle.rows
        
        List check_list = []
        int sort_area = 1
        boolean name_type_number = false
        boolean check_name_type = true
        for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])) {
            if (check_name_type) {
                if (coordenroutephoto_instance.enroutePhotoName.isInteger()) {
                    name_type_number = true
                }
                check_name_type = false
            }
            if (coordenroutephoto_instance.observationNextPrintPage) {
                sort_area++
            }
            if (name_type_number && coordenroutephoto_instance.enroutePhotoName.isInteger()) {
                check_list += [instance:coordenroutephoto_instance, name:coordenroutephoto_instance.enroutePhotoName.toInteger(), sortarea:sort_area]
            } else {
                check_list += [instance:coordenroutephoto_instance, name:coordenroutephoto_instance.enroutePhotoName, sortarea:sort_area]
            }
        }
        if (attrs.params.alphabetical == 'true') {
            check_list = check_list.asImmutable().toSorted { a, b -> a.name <=> b.name }
            check_list = check_list.asImmutable().toSorted { a, b -> a.sortarea <=> b.sortarea }
        }
        
        List write_list = []
        List write_row = []
        int check_area = 1
        for (Map check_value in check_list) {
            if (check_value.sortarea != check_area) { // new page
                if (write_row) {
                    write_list += [write_row]
                }
                write_row = []
                col_pos = 0
                write_list += [[]]
                row_pos = 0
                check_area = check_value.sortarea
            }
            if (col_pos < col_num) {
                write_row += check_value.instance
            }
            col_pos++
            if (col_pos == col_num) { // new line
                write_list += [write_row]
                write_row = []
                col_pos = 0
                row_pos++
            }
            if (row_pos == row_num) {  // new page
                write_list += [[]]
                row_pos = 0
            }
        }
        if (write_row) {
            write_list += [write_row]
        }
        
        outln"""<table class="photo">"""
        outln"""    <tbody>"""
        for (List row in write_list) {
            if (row) {
                outln"""<tr>"""
                for (CoordEnroutePhoto coordenroutephoto_instance in row) {
                    outln"""<td>"""
                    outln"""    <div class="photo">"""
                    outln"""        <img class="photo" src="/fc/route/get_enroute_photo/${coordenroutephoto_instance.id}" style="width:${attrs.route.enroutePhotoPrintStyle.width}px; height:${attrs.route.enroutePhotoPrintStyle.height}px;"/>"""
                    outln"""        <div class="phototext">${coordenroutephoto_instance.enroutePhotoName}</div>"""
                    outln"""        <div class="photoposition" style="top:${coordenroutephoto_instance.observationPositionTop}px; left:${coordenroutephoto_instance.observationPositionLeft}px;"></div>"""
                    outln"""    </div>"""
                    outln"""</td>"""
                }
                outln"""</tr>"""
            } else {
                outln"""<div style="page-break-after: always;"></div>"""
            }
        }
        outln"""    </tbody>"""
        outln"""</table>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def printCoordTurnpointPhoto = { attrs, body ->

        int col_pos = 0
        int col_num = attrs.route.turnpointPrintStyle.columns
        int row_pos = 0
        int row_num = attrs.route.turnpointPrintStyle.rows
        
        List check_list = []
        int sort_area = 1
        boolean name_type_number = false
        boolean check_name_type = attrs.route.turnpointRoute != TurnpointRoute.TrueFalsePhoto
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(attrs.route,[sort:"id"])) {
            if (check_name_type) {
                if (coordroute_instance.assignedSign != TurnpointSign.NoSign && coordroute_instance.assignedSign != TurnpointSign.None) {
                    if (coordroute_instance.assignedSign.title.isInteger()) {
                        name_type_number = true
                    }
                    check_name_type = false
                }
            }
            if (coordroute_instance.observationNextPrintPage) {
                sort_area++
            }
            if (coordroute_instance.imagecoord) {
                if (attrs.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto) {
                    check_list += [instance:coordroute_instance, name:coordroute_instance.titlePrintCode(), sortarea:sort_area]
                } else {
                    if (coordroute_instance.assignedSign != TurnpointSign.NoSign && coordroute_instance.assignedSign != TurnpointSign.None) {
                        if (name_type_number && coordroute_instance.assignedSign.title.isInteger()) {
                            check_list += [instance:coordroute_instance, name:coordroute_instance.assignedSign.title.toInteger(), sortarea:sort_area]
                        } else {
                            check_list += [instance:coordroute_instance, name:coordroute_instance.assignedSign.title, sortarea:sort_area]
                        }
                    }
                }
            }
        }
        if (attrs.params.alphabetical == 'true') {
            check_list = check_list.asImmutable().toSorted { a, b -> a.name <=> b.name }
            check_list = check_list.asImmutable().toSorted { a, b -> a.sortarea <=> b.sortarea }
        }
        
        List write_list = []
        List write_row = []
        int check_area = 1
        for (Map check_value in check_list) {
            if (check_value.sortarea != check_area) { // new page
                if (write_row) {
                    write_list += [write_row]
                }
                write_row = []
                col_pos = 0
                write_list += [[]]
                row_pos = 0
                check_area = check_value.sortarea
            }
            if (col_pos < col_num) {
                write_row += check_value.instance
            }
            col_pos++
            if (col_pos == col_num) { // new line
                write_list += [write_row]
                write_row = []
                col_pos = 0
                row_pos++
            }
            if (row_pos == row_num) {  // new page
                write_list += [[]]
                row_pos = 0
            }
        }
        if (write_row) {
            write_list += [write_row]
        }
        
        outln"""<table class="photo">"""
        outln"""    <tbody>"""
        for (List row in write_list) {
            if (row) {
                outln"""<tr>"""
                for (CoordRoute coordroute_instance in row) {
                    outln"""<td>"""
                    outln"""    <div class="photo">"""
                    outln"""        <img class="photo" src="/fc/route/get_turnpoint_photo/${coordroute_instance.id}" style="width:${attrs.route.turnpointPrintStyle.width}px; height:${attrs.route.turnpointPrintStyle.height}px;"/>"""
                    if (attrs.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto) {
                        outln"""    <div class="phototext">${coordroute_instance.titlePrintCode()}</div>"""
                    } else {
                        outln"""    <div class="phototext">${coordroute_instance.assignedSign.title}</div>"""
                    }
                    outln"""        <div class="photoposition" style="top:${coordroute_instance.observationPositionTop}px; left:${coordroute_instance.observationPositionLeft}px;"></div>"""
                    outln"""    </div>"""
                    outln"""</td>"""
                }
                outln"""</tr>"""
            } else {
                outln"""<div style="page-break-after: always;"></div>"""
            }
        }
        outln"""    </tbody>"""
        outln"""</table>"""
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
    private checkBoxImg(String name, String label, String imageName, attrs)
    {
        //outln"""<input type="hidden" name="_${name}"/>"""
        outln"""<input type="checkbox" id="${name}" name="${name}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""<img src="${imageName}" style="height:16px;"/>"""
        outln"""<label>$label</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
