class ContestMapRoutePointsTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapRoutePointsInput = { attrs, body ->
        Route route_instance = attrs.r
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
            inputCoord(coordroute_instance, route_instance, attrs)
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private inputCoord(CoordRoute coordrouteInstance, Route routeInstance, attrs)
    {
        if (coordrouteInstance) {
            if (coordrouteInstance.type.IsContestMapQuestionCoord()) {
                String check_title = coordrouteInstance.title()+','
                boolean checked = DisabledCheckPointsTools.Uncompress(attrs.tp).contains(check_title)
                checkBox("${attrs.tpid}${coordrouteInstance.title()}", coordrouteInstance.titleCode(), checked, attrs)
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapCurvedPointsInput = { attrs, body ->
        Route route_instance = attrs.r
        CoordRoute last_tp_coordroute_instance
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
            if (coordroute_instance.endCurved) {
                inputCurvedCoord(last_tp_coordroute_instance, coordroute_instance, route_instance, attrs)
            }
            if (coordroute_instance.type != CoordType.SECRET) {
                last_tp_coordroute_instance = coordroute_instance
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private inputCurvedCoord(CoordRoute lastCoordrouteInstance, CoordRoute coordrouteInstance, Route routeInstance, attrs)
    {
        if (coordrouteInstance) {
            if (coordrouteInstance.type.IsContestMapQuestionCoord()) {
                String check_title = coordrouteInstance.title()+','
                boolean checked = DisabledCheckPointsTools.Uncompress(attrs.tp).contains(check_title)
                checkBox("${attrs.tpid}${coordrouteInstance.title()}", "${lastCoordrouteInstance.titleCode()}...${coordrouteInstance.titleCode()}", checked, attrs)
            }
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private checkBox(String name, String title, boolean checked, attrs)
    {
        outln"""    <input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""<input type="checkbox" id="${name}" name="${name}" checked="checked" tabIndex="${attrs.ti[0]++}">${title}</input>"""
        } else {
            outln"""<input type="checkbox" id="${name}" name="${name}" tabIndex="${attrs.ti[0]++}">${title}</input>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
