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
                boolean checked = attrs.tp.contains(check_title)
                checkBox("${attrs.tpid}${coordrouteInstance.title()}", coordrouteInstance.titleCode(), checked, attrs)
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
