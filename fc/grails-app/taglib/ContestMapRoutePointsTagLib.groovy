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
		if (attrs.map_upload_job_status == UploadJobStatus.Waiting) {
			outln"""<img src="${createLinkTo(dir:'images',file:'email.png')}"/>"""
		} else if (attrs.map_upload_job_status == UploadJobStatus.Sending) {
			outln"""<img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>"""
		} else if (attrs.map_upload_job_status == UploadJobStatus.Error) {
			outln"""<img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>"""
		} else if (attrs.map_upload_job_status == UploadJobStatus.Done) {
			outln"""<a href="${attrs.r.GetMapUploadLink()}" target="_blank"><img src="${createLinkTo(dir:'images',file:'map.png')}"/></a>"""
		}
		outln"""</p>"""
		
		if (attrs.PrintButton) {
			String contestmapprint_checked_pdfmap = ""
			String contestmapprint_checked_pngmap = ""
			String contestmapprint_checked_pngzip = ""
			String contestmapprint_checked_tifmap = ""
			String contestmapprint_checked_tiles = ""
			outln"""<fieldset>"""
			outln"""	<div>"""
			if (attrs.r.contestMapPrint == Defs.CONTESTMAPPRINT_PDFMAP) {
				contestmapprint_checked_pdfmap = "checked"
			} else if (attrs.r.contestMapPrint == Defs.CONTESTMAPPRINT_PNGMAP) {
				contestmapprint_checked_pngmap = "checked"
			} else if (attrs.r.contestMapPrint == Defs.CONTESTMAPPRINT_PNGZIP) {
				contestmapprint_checked_pngzip = "checked"
			} else if (attrs.r.contestMapPrint == Defs.CONTESTMAPPRINT_TIFMAP) {
				contestmapprint_checked_tifmap = "checked"
			} else if (attrs.r.contestMapPrint == Defs.CONTESTMAPPRINT_TILES) {
				contestmapprint_checked_tiles = "checked"
			}
			outln"""		<label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PDFMAP}" value="${Defs.CONTESTMAPPRINT_PDFMAP}" ${contestmapprint_checked_pdfmap} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportmap.pdf')}</label>"""
			outln"""		<label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PNGMAP}" value="${Defs.CONTESTMAPPRINT_PNGMAP}" ${contestmapprint_checked_pngmap} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportmap.png')}</label>"""
			outln"""		<label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PNGZIP}" value="${Defs.CONTESTMAPPRINT_PNGZIP}" ${contestmapprint_checked_pngzip} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportmap.pngzip')}</label>"""
			if (BootStrap.global.IsGDALAvailable()) {
				outln"""	<label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_TIFMAP}" value="${Defs.CONTESTMAPPRINT_TIFMAP}" ${contestmapprint_checked_tifmap} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportmap.tif')}</label>"""
				if (BootStrap.global.IsTitlesUploadAvailable()) {
					outln"""<label title="${BootStrap.global.GetTitlesUploadHost()}"><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_TILES}" value="${Defs.CONTESTMAPPRINT_TILES}" ${contestmapprint_checked_tiles} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportmap.tiles')}</label>"""
				}
			}
			outln"""	</div>"""
			if (BootStrap.global.IsContestMapDevOptions()) {
				outln"""<div>"""
				outln"""	<br/>"""
				checkBox("contestMapNoTilesUpload", message(code:'fc.contestmap.notilesupload'), true, attrs)
						//<g:checkBox name="contestMapNoTilesUpload" value="${true}" tabIndex="${attrs.ti[0]++}" />
						//<label>${message(code:'fc.contestmap.notilesupload')}</label>
				outln"""</div>"""
			}
			outln"""</fieldset>"""
			outln"""<script>"""
			outln"""	\$(document).on('change', '#${Defs.CONTESTMAPPRINT_PDFMAP}', function() {"""
			outln"""		\$("#mapsendmail_id").prop("disabled", false);"""
			outln"""	});"""
			outln"""	\$(document).on('change', '#${Defs.CONTESTMAPPRINT_PNGMAP}', function() {"""
			outln"""		\$("#mapsendmail_id").prop("disabled", true);"""
			outln"""	});"""
			outln"""	\$(document).on('change', '#${Defs.CONTESTMAPPRINT_PNGZIP}', function() {"""
			outln"""		\$("#mapsendmail_id").prop("disabled", true);"""
			outln"""	});"""
			if (BootStrap.global.IsGDALAvailable()) {
				outln"""\$(document).on('change', '#${Defs.CONTESTMAPPRINT_TIFMAP}', function() {"""
				outln"""	\$("#mapsendmail_id").prop("disabled", true);"""
				outln"""});"""
				if (BootStrap.global.IsTitlesUploadAvailable()) {
					outln"""\$(document).on('change', '#${Defs.CONTESTMAPPRINT_TILES}', function() {"""
					outln"""	\$("#mapsendmail_id").prop("disabled", true);"""
					outln"""});"""
				}
			}
			outln"""</script>"""
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
