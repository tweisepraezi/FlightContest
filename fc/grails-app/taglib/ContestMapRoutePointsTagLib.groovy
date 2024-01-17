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
    def contestMapRoutePrintOptions = { attrs, body ->
		outln"""<p>"""
		if (attrs.BreakButton) {
			outln message(code:'fc.contestmap.job.running')
		} else if (attrs.FetchButton) {
			outln message(code:'fc.contestmap.job.continue')
		} else if (attrs.PrintButton) {
			outln message(code:'fc.contestmap.job.done')
		} else {
			outln message(code:'fc.contestmap.job.otherrunning')
		}
        /*
		if (attrs.map_upload_job_status == UploadJobStatus.Waiting) {
			outln"""<img src="${createLinkTo(dir:'images',file:'email.png')}"/>"""
		} else if (attrs.map_upload_job_status == UploadJobStatus.Sending) {
			outln"""<img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>"""
		} else if (attrs.map_upload_job_status == UploadJobStatus.Error) {
			outln"""<img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>"""
		} else if (attrs.map_upload_job_status == UploadJobStatus.Done) {
			outln"""<a href="${attrs.r.GetMapUploadLink()}" target="_blank"><img src="${createLinkTo(dir:'images',file:'map.png')}"/></a>"""
		}
        */
		outln"""</p>"""
		
		if (attrs.PrintButton) {
            outln"""<p>"""
			outln"""	    <br/>"""
            outln"""        <label>${message(code:'fc.contestmap.savemap.name')}: <input type="text" id="maponlinename_id" name="contestMapPrintName" value="${attrs.r.contestMapPrintName}" size="${attrs.r.contestMapPrintName.size()}" ></input></label>"""
            checkBox("contestMapAllowOverwrite", message(code:'fc.contestmap.savemap.allowoverwrite'), false, attrs)
            outln"""</p>"""
            outln"""<br/>"""
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
