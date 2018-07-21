class DisableCheckPointsTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def disableCheckpointsInput = { attrs, body ->
        // CoordRoute
        Route route_instance = attrs.t.flighttest.route
        boolean use_procedureturn = route_instance.UseProcedureTurn()
        int col_span = 7
        if (route_instance.showAflosMark) {
            col_span++
        }
        if (route_instance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
            col_span++
        }
        if (route_instance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
            col_span++
        }
        outln"""<table>"""
        outln"""    <thead>"""
        outln"""        <tr>"""
        outln"""            <th colspan="${col_span}" class="table-head">${message(code:'fc.task.disabledcheckpoints.checkpoints')}</th>"""
        outln"""        <tr>"""
        outln"""        </tr>"""
        outln"""            <th>${message(code:'fc.title')}</th>"""
        if (route_instance.showAflosMark) {
            outln"""        <th>${message(code:'fc.aflos.checkpoint')}</th>"""
        }
        outln"""            <th>${message(code:'fc.task.disabledcheckpoints.timecheck')}</th>"""
        outln"""            <th>${message(code:'fc.task.disabledcheckpoints.notfound')}</th>"""
        if (use_procedureturn) {
            outln"""        <th>${message(code:'fc.task.disabledcheckpoints.procedureturn')}</th>"""
        }
        outln"""            <th>${message(code:'fc.task.disabledcheckpoints.badcourse')}</th>"""
        outln"""            <th>${message(code:'fc.task.disabledcheckpoints.minaltitude')}</th>"""
        outln"""            <th>${message(code:'fc.task.disabledcheckpoints.turnpointops')}</th>"""
        if (route_instance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
            outln"""        <th>${message(code:'fc.observation.enroute.photo.short')}</th>"""
        }
        if (route_instance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
            outln"""        <th>${message(code:'fc.observation.enroute.canvas.short')}</th>"""
        }
        outln"""        </tr>"""
        outln"""    </thead>"""
        outln"""    <tbody>"""
        int i = 0
        CoordRoute last_coordroute_instance = null
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
            i = inputCoord(last_coordroute_instance, route_instance, i, attrs.t, use_procedureturn, use_procedureturn && coordroute_instance.planProcedureTurn)
            last_coordroute_instance = coordroute_instance
        }
        i = inputCoord(last_coordroute_instance, route_instance, i, attrs.t, use_procedureturn, false)
        outln"""    </tbody>"""
        outln"""</table>"""
        
        // CoordEnroutePhoto
        if (route_instance.enroutePhotoRoute == EnrouteRoute.InputName) {
            outln"""<table>"""
            outln"""    <thead>"""
            outln"""        <tr>"""
            outln"""            <th class="table-head">${message(code:'fc.observation.enroute.photo.short')}</th>"""
            outln"""        <tr>"""
            outln"""    </thead>"""
            outln"""    <tbody>"""
            outln"""        <tr>"""
            outln"""            <td>"""
            for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
                String check_title = "${coordenroutephoto_instance.enroutePhotoName},"
                checkBox2("${Defs.EnrouteID_PhotoObs}${coordenroutephoto_instance.enroutePhotoName}", attrs.t.disabledEnroutePhotoObs.contains(check_title),coordenroutephoto_instance.enroutePhotoName,"")
            }
            outln"""            </td>"""
            outln"""        </tr>"""
            outln"""    </tbody>"""
            outln"""</table>"""
        }

        // CoordEnrouteCanvas
        if (route_instance.enrouteCanvasRoute == EnrouteRoute.InputName) {
            outln"""<table>"""
            outln"""    <thead>"""
            outln"""        <tr>"""
            outln"""            <th class="table-head">${message(code:'fc.observation.enroute.canvas.short')}</th>"""
            outln"""        <tr>"""
            outln"""    </thead>"""
            outln"""    <tbody>"""
            outln"""        <tr>"""
            outln"""            <td>"""
            for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRoute(route_instance,[sort:"enrouteViewPos"])) {
                String check_title = "${coordenroutecanvas_instance.enrouteCanvasSign.canvasName},"
                checkBox2("${Defs.EnrouteID_CanvasObs}${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}", attrs.t.disabledEnrouteCanvasObs.contains(check_title),coordenroutecanvas_instance.enrouteCanvasSign.canvasName,createLinkTo(dir:'',file:coordenroutecanvas_instance.enrouteCanvasSign.imageName))
            }
            outln"""            </td>"""
            outln"""        </tr>"""
            outln"""    </tbody>"""
            outln"""</table>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private inputCoord(CoordRoute coordrouteInstance, Route routeInstance, int i, Task taskInstance, boolean useProcedureTurn, boolean planProcedureTurn)
    {
        if (coordrouteInstance) {
            if (coordrouteInstance.type.IsCpCheckCoord()) {
                String check_title = coordrouteInstance.title()+','
                i++
                outln"""<tr class="${(i % 2) == 0 ? 'odd' : ''}">"""
                outln"""    <td>${coordrouteInstance.titleCode()}</td>"""
                if (routeInstance.showAflosMark) {
                    outln"""<td>${coordrouteInstance.mark}</td>"""
                }
                if (coordrouteInstance.type.IsCpTimeCheckCoord()) {
                    checkBox("${Defs.TurnpointID_TimeCheck}${coordrouteInstance.title()}", DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPoints).contains(check_title))
                    checkBox("${Defs.TurnpointID_NotFound}${coordrouteInstance.title()}", DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPointsNotFound).contains(check_title))
                } else {
                    outln"""<td/>"""
                    outln"""<td/>"""
                }
                if (useProcedureTurn) {
                    if (planProcedureTurn) {
                        if (coordrouteInstance.type.IsProcedureTurnCoord()) {
                            checkBox("${Defs.TurnpointID_ProcedureTurn}${coordrouteInstance.title()}", DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPointsProcedureTurn).contains(check_title))
                        } else {
                            outln"""<td/>"""
                        }
                    } else {
                        outln"""<td/>"""
                    }
                }
                if (coordrouteInstance.type.IsBadCourseCheckCoord()) {
                    checkBox("${Defs.TurnpointID_BadCourse}${coordrouteInstance.title()}", DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPointsBadCourse).contains(check_title))
                } else {
                    outln"""<td/>"""
                }
                if (coordrouteInstance.type.IsAltitudeCheckCoord()) {
                    checkBox("${Defs.TurnpointID_MinAltitude}${coordrouteInstance.title()}", DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPointsMinAltitude).contains(check_title))
                } else {
                    outln"""<td/>"""
                }
                if (coordrouteInstance.IsTurnpointSign()) {
                    checkBox("${Defs.TurnpointID_TurnpointObs}${coordrouteInstance.title()}", DisabledCheckPointsTools.Uncompress(taskInstance.disabledCheckPointsTurnpointObs).contains(check_title))
                } else {
                    outln"""<td/>"""
                }
                if (routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()) {
                    outln"""<td>"""
                    for (CoordEnroutePhoto coordenroutephoto_instance in CoordEnroutePhoto.findAllByRouteAndTypeAndTitleNumber(routeInstance,coordrouteInstance.type,coordrouteInstance.titleNumber,[sort:"enrouteViewPos"])) {
                        String check_title2 = "${coordenroutephoto_instance.enroutePhotoName},"
                        checkBox2("${Defs.EnrouteID_PhotoObs}${coordenroutephoto_instance.enroutePhotoName}", taskInstance.disabledEnroutePhotoObs.contains(check_title2),coordenroutephoto_instance.enroutePhotoName,"")
                    }
                    outln"""</td>"""
                }
                if (routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()) {
                    outln"""<td>"""
                    for (CoordEnrouteCanvas coordenroutecanvas_instance in CoordEnrouteCanvas.findAllByRouteAndTypeAndTitleNumber(routeInstance,coordrouteInstance.type,coordrouteInstance.titleNumber,[sort:"enrouteViewPos"])) {
                        String check_title2 = "${coordenroutecanvas_instance.enrouteCanvasSign.canvasName},"
                        checkBox2("${Defs.EnrouteID_CanvasObs}${coordenroutecanvas_instance.enrouteCanvasSign.canvasName}", taskInstance.disabledEnrouteCanvasObs.contains(check_title2),coordenroutecanvas_instance.enrouteCanvasSign.canvasName,createLinkTo(dir:'',file:coordenroutecanvas_instance.enrouteCanvasSign.imageName))
                    }
                    outln"""</td>"""
                }
                outln"""</tr>"""
            }
        }
        return i
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox(String name, boolean checked)
    {
        outln"""<td>"""
        outln"""    <input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""<input type="checkbox" id="${name}" name="${name}" checked="checked"/>"""
        } else {
            outln"""<input type="checkbox" id="${name}" name="${name}"/>"""
        }
        outln"""</td>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox2(String name, boolean checked, String label, String imageName)
    {
        outln"""    <input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""<input type="checkbox" id="${name}" name="${name}" checked="checked"/>"""
        } else {
            outln"""<input type="checkbox" id="${name}" name="${name}"/>"""
        }
        if (imageName) {
            outln"""<img src="${imageName}" style="height:16px;"/>"""
        }
        if (label) {
            outln"""<label>${label}</label>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
