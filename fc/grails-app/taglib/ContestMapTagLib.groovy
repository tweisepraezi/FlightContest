class ContestMapTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapPos = { attrs, body ->
        outln"""<div>"""
        outln"""    <label>${message(code:'fc.contestmap.contestmapcenterpos')}:</label>"""
        outln"""    <br/>"""
        for (VerticalPos verticalpos_instance in VerticalPos.GetValues()) {
            if (attrs.vp == verticalpos_instance) {
                outln"""<label><input type="radio" name="${attrs.vpname}" value="${verticalpos_instance}" checked="checked" tabIndex="${attrs.ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>"""
            } else {
                outln"""<label><input type="radio" name="${attrs.vpname}" value="${verticalpos_instance}" tabIndex="${attrs.ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>"""
            }
        }
        outln"""<label style="margin-left:20px;"/>"""
        for (HorizontalPos horizontalpos_instance in HorizontalPos.GetValues()) {
            if (attrs.hp == horizontalpos_instance) {
                outln"""<label><input type="radio" name="${attrs.hpname}" value="${horizontalpos_instance}" checked="checked" tabIndex="${attrs.ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>"""
            } else {
                outln"""<label><input type="radio" name="${attrs.hpname}" value="${horizontalpos_instance}" tabIndex="${attrs.ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>"""
            }
        }
        outln"""</div>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapRouteInputField = { attrs, body ->
        outln"""<div>"""
        outln"""    <br/>"""
        outln"""    <label>${attrs.fieldlabel}</label>"""
        outln"""    <br/>"""
        outln"""    <input type="text" id="${attrs.fieldid}" name="${attrs.fieldid}" value="${attrs.fieldvalue}" tabIndex="${attrs.ti[0]++}"/>"""
        outln"""</div>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapShowCorridorWidth = { attrs, body ->
        if (attrs.r.corridorWidth) {
            outln"""<div>"""
            outln"""    <br>"""
            outln"""    <label>${message(code:'fc.corridorwidth')}:</label>"""
            outln"""    <br>"""
            outln"""    <input type="text" value="${attrs.r.corridorWidth}${message(code:'fc.mile')}" disabled/>"""
            outln"""</div>"""
        }
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapRoutePointsInput = { attrs, body ->
        outln"""<div>"""
        outln"""    <br/>"""
        if (attrs.printpoints) {
            outln"""<label>${message(code:'fc.contestmap.contestmapprintpoints')}:</label>"""
        } else {
            outln"""<label>${message(code:'fc.contestmap.contestmapcenterpoints')}:</label>"""
        }
        outln"""    <br/>"""
        Route route_instance = attrs.r
        for (CoordRoute coordroute_instance in CoordRoute.findAllByRoute(route_instance,[sort:"id"])) {
            inputCoord(coordroute_instance, route_instance, attrs)
        }
        outln"""</div>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private inputCoord(CoordRoute coordrouteInstance, Route routeInstance, attrs)
    {
        if (coordrouteInstance) {
            if (coordrouteInstance.type.IsContestMapQuestionCoord()) {
                String check_title = coordrouteInstance.title()+','
                boolean checked = DisabledCheckPointsTools.Uncompress(attrs.tp,routeInstance).contains(check_title)
                checkBox("${attrs.tpid}${coordrouteInstance.title()}", coordrouteInstance.titleCode(), checked, attrs)
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------------------
    def contestMapPrintOptions = { attrs, body ->
        outln"""<br/>"""
        outln"""<div>"""
        String printsize_checked_a4 = ""
        String printsize_checked_a3 = ""
        String printsize_checked_a2 = ""
        String printsize_checked_a1 = ""
        String printsize_checked_anr = ""
        if (attrs.printsize == Defs.CONTESTMAPPRINTSIZE_A4) {
            printsize_checked_a4 = "checked"
        } else if (attrs.printsize == Defs.CONTESTMAPPRINTSIZE_A3) {
            printsize_checked_a3 = "checked"
        } else if (attrs.printsize == Defs.CONTESTMAPPRINTSIZE_A2) {
            printsize_checked_a2 = "checked"
        } else if (attrs.printsize == Defs.CONTESTMAPPRINTSIZE_A1) {
            printsize_checked_a1 = "checked"
        } else if (attrs.printsize == Defs.CONTESTMAPPRINTSIZE_ANR) {
            printsize_checked_anr = "checked"
        }
        outln"""    <label>${message(code:'fc.contestmap.printsize')}</label>:"""
        outln"""    <label><input type="radio" name="${attrs.printsizename}" id="${Defs.CONTESTMAPPRINTSIZE_A4}" value="${Defs.CONTESTMAPPRINTSIZE_A4}" ${printsize_checked_a4} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.printsize.a4')}</label>"""
        outln"""    <label><input type="radio" name="${attrs.printsizename}" id="${Defs.CONTESTMAPPRINTSIZE_A3}" value="${Defs.CONTESTMAPPRINTSIZE_A3}" ${printsize_checked_a3} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.printsize.a3')}</label>"""
        outln"""    <label><input type="radio" name="${attrs.printsizename}" id="${Defs.CONTESTMAPPRINTSIZE_A2}" value="${Defs.CONTESTMAPPRINTSIZE_A2}" ${printsize_checked_a2} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.printsize.a2')}</label>"""
        outln"""    <label><input type="radio" name="${attrs.printsizename}" id="${Defs.CONTESTMAPPRINTSIZE_A1}" value="${Defs.CONTESTMAPPRINTSIZE_A1}" ${printsize_checked_a1} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.printsize.a1')}</label>"""
        outln"""    <label style="margin-right:20px;"><input type="radio" name="${attrs.printsizename}" id="${Defs.CONTESTMAPPRINTSIZE_ANR}" value="${Defs.CONTESTMAPPRINTSIZE_ANR}" ${printsize_checked_anr} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.printsize.anr')}</label>"""
        checkBox(attrs.printlandscapename, message(code:'fc.printlandscape'), attrs.printlandscape, attrs)
        outln"""</div>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapAirfieldsSelect = { attrs, body ->
        String airfields_checked_auto = ""
        String airfields_checked_openaip = ""
        String airfields_checked_icao = ""
        String airfields_checked_name = ""
        if (attrs.airfields == Defs.CONTESTMAPAIRFIELDS_AUTO) {
            airfields_checked_auto = "checked"
        } else if (attrs.airfields == Defs.CONTESTMAPAIRFIELDS_OPENAIP) {
            if (BootStrap.global.IsOpenAIP()) {
                airfields_checked_openaip = "checked"
            } else {
                airfields_checked_auto = "checked"
            }
        } else if (attrs.airfields == Defs.CONTESTMAPAIRFIELDS_OSM_ICAO) {
            airfields_checked_icao = "checked"
        } else if (attrs.airfields == Defs.CONTESTMAPAIRFIELDS_OSM_NAME) {
            airfields_checked_name = "checked"
        }
        outln"""<label>${message(code:'fc.contestmap.contestmapairfields')}</label>:"""
        outln"""<label><input type="radio" name="${attrs.airfieldsname}" id="${Defs.CONTESTMAPAIRFIELDS_AUTO}" value="${Defs.CONTESTMAPAIRFIELDS_AUTO}" ${airfields_checked_auto} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.auto')}</label>"""
        if (BootStrap.global.IsOpenAIP()) {
            outln"""<label><input type="radio" name="${attrs.airfieldsname}" id="${Defs.CONTESTMAPAIRFIELDS_OPENAIP}" value="${Defs.CONTESTMAPAIRFIELDS_OPENAIP}" ${airfields_checked_openaip} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.openaip')}</label>"""
        }
        outln"""<label><input type="radio" name="${attrs.airfieldsname}" id="${Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}" value="${Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}" ${airfields_checked_icao} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.osmdata.icao')}</label>"""
        outln"""<label style="margin-right:10px;"><input type="radio" name="${attrs.airfieldsname}" id="${Defs.CONTESTMAPAIRFIELDS_OSM_NAME}" value="${Defs.CONTESTMAPAIRFIELDS_OSM_NAME}" ${airfields_checked_name} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.osmdata.name')}</label>"""
        checkBoxClick("contestMapShowAirfields", attrs.showairfields, "fc.contestmap.contestmapairfields.edit", [], "showairfields_click();", attrs)
        outln"""<label style="margin-left:10px;">${message(code:'fc.contestmap.contestmap.entrynum',args:[attrs.airfieldslines])}</label>"""
        outln"""<script>"""
        outln"""    function showairfields_click() {"""
        outln"""        var show_airfields = \$("#contestMapShowAirfields").prop("checked");"""
        outln"""        \$("#showairfields_id").prop("hidden", !show_airfields);"""
        outln"""        \$.post("/fc/route/saveshow_ajax", {id:${attrs.routeid}, contestMapShowAirfields:show_airfields}, "json");"""
        outln"""    }"""
        outln"""</script>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapContourLinesSelect = { attrs, body ->
        String contourlines_checked_100m = ""
        String contourlines_checked_50m = ""
        String contourlines_checked_20m = ""
        String contourlines_checked_none = ""
        if (attrs.contourlines == Defs.CONTESTMAPCONTOURLINES_100M) {
            contourlines_checked_100m = "checked"
        } else if (attrs.contourlines == Defs.CONTESTMAPCONTOURLINES_50M) {
            contourlines_checked_50m = "checked"
        } else if (attrs.contourlines == Defs.CONTESTMAPCONTOURLINES_20M) {
            contourlines_checked_20m = "checked"
        } else {
            contourlines_checked_none = "checked"
        }
        outln"""    <label>${message(code:'fc.contestmap.contestmapcontourlines')}</label>:"""
        outln"""    <label><input type="radio" name="${attrs.contourlinesname}" id="${Defs.CONTESTMAPCONTOURLINES_100M}" value="${Defs.CONTESTMAPCONTOURLINES_100M}" ${contourlines_checked_100m} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.100m')}</label>"""
        outln"""    <label><input type="radio" name="${attrs.contourlinesname}" id="${Defs.CONTESTMAPCONTOURLINES_50M}" value="${Defs.CONTESTMAPCONTOURLINES_50M}" ${contourlines_checked_50m} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.50m')}</label>"""
        outln"""    <label><input type="radio" name="${attrs.contourlinesname}" id="${Defs.CONTESTMAPCONTOURLINES_20M}" value="${Defs.CONTESTMAPCONTOURLINES_20M}" ${contourlines_checked_20m} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.20m')}</label>"""
        outln"""    <label style="margin-right:10px;"><input type="radio" name="${attrs.contourlinesname}" id="${Defs.CONTESTMAPCONTOURLINES_NONE}" value="${Defs.CONTESTMAPCONTOURLINES_NONE}" ${contourlines_checked_none} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.none')}</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapDevOptions = { attrs, body ->
        if (BootStrap.global.IsContestMapDevOptions()) {
            outln"""<fieldset>"""
            outln"""    <div>"""
            String contestmap_output_exportprintmap = ""
            String contestmap_output_showonlinemap = ""
            String contestmap_output_exportgpx = ""
            if (attrs.output == Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP) {
                contestmap_output_exportprintmap = "checked"
            } else if (attrs.output == Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP) {
                contestmap_output_showonlinemap = "checked"
            } else if (attrs.output == Defs.CONTESTMAPOUTPUT_EXPORTGPX) {
                contestmap_output_exportgpx = "checked"
            }
            outln"""        <label><input type="radio" name="${attrs.outputname}" id="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" value="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" ${contestmap_output_exportprintmap} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportmap')}</label>"""
            outln"""        <label><input type="radio" name="${attrs.outputname}" id="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" value="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" ${contestmap_output_showonlinemap} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.showonlinemap')}</label>"""
            outln"""        <label><input type="radio" name="${attrs.outputname}" id="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" value="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" ${contestmap_output_exportgpx} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.exportgpxp')}</label>"""
            outln"""    </div>"""
            outln"""    <div>"""
            checkBox(attrs.devstylename, message(code:'fc.contestmap.devstyle'), attrs.devstyle, attrs)
            outln"""    </div>"""
            outln"""</fieldset>"""
        } else {
            outln"""<input type="hidden" name="${attrs.outputname}" value="${attrs.output}" />"""
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
                boolean checked = DisabledCheckPointsTools.Uncompress(attrs.tp,routeInstance).contains(check_title)
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
            Route route_instance = null
            if (attrs.OtherRouteId) {
                route_instance = Route.get(attrs.OtherRouteId)
            }
            if (route_instance) {
                outln message(code:'fc.contestmap.job.otherrunning.route', args:[route_instance.name()])
            } else {
                outln message(code:'fc.contestmap.job.otherrunning')
            }
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
            outln"""        <label>${message(code:'fc.contestmap.savemap.name')}: <input type="text" id="maponlinename_id" name="contestMapPrintName" value="${attrs.r.contestMapPrintName}" size="${attrs.r.contestMapPrintName.size()+10}" ></input></label>"""
            checkBox("contestMapAllowOverwrite", message(code:'fc.contestmap.savemap.allowoverwrite'), false, attrs)
            outln"""</p>"""
            if (attrs.MapProjection == "3857") {
                outln"""<p>"""
                checkBox("contestMapSetDefaultOnlineMap", message(code:'fc.contestmap.savemap.defaultonlinemap'), true, attrs)
                if (attrs.r.defaultOnlineMap) {
                    outln """(${message(code:'fc.contestmap.savemap.saveddefault', args:[attrs.r.defaultOnlineMap])})"""
                }
                outln"""</p>"""
                if (attrs.PrintSize == Defs.CONTESTMAPPRINTSIZE_ANR) {
                    outln"""<p>"""
                    checkBox("contestMapSetDefaultPrintMap", message(code:'fc.contestmap.savemap.defaultprintmap'), true, attrs)
                    if (attrs.r.defaultPrintMap) {
                        outln """(${message(code:'fc.contestmap.savemap.saveddefault', args:[attrs.r.defaultPrintMap])})"""
                    }
                    outln"""</p>"""
                }
            }
		}
	}
	
    // --------------------------------------------------------------------------------------------------------------------
    def showContestMapRouteMapObjects = { attrs, body ->
    
        // Map objects
        String show_map_objets = "hidden"
        if (attrs.route.contestMapShowMapObjects) {
            show_map_objets = ""
        }
        List maps_objects = CoordMapObject.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])
        outln"""<div style="margin-top:10px;"/>"""
        checkBoxClick("contestMapAdditionals", attrs.route.contestMapAdditionals, "fc.contestmap.contestmapadditionals", [], "additionals_click();", attrs)
        outln"""<label style="margin-left:10px;"></label>"""
        checkBoxClick("contestMapShowMapObjects", attrs.route.contestMapShowMapObjects, "fc.coordroute.mapobjects.edit.all", [], "showmapobjects_click();", attrs)
        outln"""<label style="margin-left:10px;">${message(code:'fc.contestmap.contestmap.entrynum',args:[maps_objects.size()])}</label>"""
        outln"""<div class="mapexportquestionsection" id="showmapobjects_id" ${show_map_objets}>"""
        outln"""    <div class="mapexportquestionbuttonline" >"""
        outln"""        <input type="button" class="mapexportquestionbutton" id="" value="${message(code:'fc.coordroute.mapobjects.import')}" onclick="importmapobjects_click();"/>"""
        outln"""        <input type="button" class="mapexportquestionbutton" id="" value="${message(code:'fc.coordroute.mapobjects.add')}" onclick="addmapobject_click();"/>"""
        outln"""        <input type="button" class="mapexportquestionbutton" id="" value="${message(code:'fc.coordroute.mapobjects.removeall')}" onclick="this.form.target='_self';if (confirm('${message(code:'fc.areyousure')}')){removeallmapobjects_click()};"/>"""
        outln"""        <label style="margin-left:10px;">${message(code:'fc.coordroute.mapobjects.fromroute')}:</label>"""
        outln"""        <select name="contestMapShowMapObjectsFromRouteID" tabIndex="${attrs.ti[0]++}">"""
        outln"""            <option></option>"""
        for (Route route_instance in Route.findAllByContest(attrs.route.contest,[sort:"idTitle"])) {
            if (route_instance.id != attrs.route.id) {
                if (route_instance.id == attrs.route.contestMapShowMapObjectsFromRouteID) {
                    outln"""<option value="${route_instance.id}" selected>${route_instance.name()}</option>"""
                } else {
                    outln"""<option value="${route_instance.id}">${route_instance.name()}</option>"""
                }
            }
        }
        outln"""        </select>"""
        outln"""    </div>"""
        outln"""    <table>"""
        outln"""        <thead>"""
        outln"""            <tr>"""
        outln"""                <th>${message(code:'fc.number')}</th>"""
        outln"""                <th>${message(code:'fc.coordroute.mapobjects.type')}</th>"""
        outln"""                <th>${message(code:'fc.coordroute.mapobjects.subtitle')}</th>"""
        outln"""                <th>${message(code:'fc.coordroute.mapobjects.details')}</th>"""
        outln"""                <th>${message(code:'fc.latitude')}</th>"""
        outln"""                <th>${message(code:'fc.longitude')}</th>"""
        outln"""            </tr>"""
        outln"""        </thead>"""
        outln"""        <tbody>"""
        for (CoordMapObject coordmapobject_instance in maps_objects) {
            long next_id = 0
            boolean set_next_id = false
            for (CoordMapObject coordmapobject_instance2 in CoordMapObject.findAllByRoute(attrs.route,[sort:"enrouteViewPos"])) {
                if (set_next_id) {
                    next_id = coordmapobject_instance2.id
                    set_next_id = false
                }
                if (coordmapobject_instance2 == coordmapobject_instance) {
                    set_next_id = true
                }
            }
            outln"""        <tr class="${coordmapobject_instance.enrouteViewPos ? '' : 'odd'}">"""
            String s = """      <td style="white-space: nowrap;">"""
            s += coordroutenum(
                coordmapobject_instance, coordmapobject_instance.enrouteViewPos, next_id, "${createLink(controller:'coordMapObject',action:'edit')}"
            )
            if (next_id) {
                s += """ <a href="${createLink(controller:'coordMapObject',action:'addposition',params:[id:coordmapobject_instance.id])}">+</a>"""
            }
            if (coordmapobject_instance.enrouteViewPos > 1) {
                s += """ <a href="${createLink(controller:'coordMapObject',action:'subposition',params:[id:coordmapobject_instance.id])}">-</a>"""
            }
            s += """            </td>"""
            outln s
            String image_str = ""
            if (coordmapobject_instance.mapObjectType.imageName) {
                image_str = """<img style="max-height:12px;" src="${createLinkTo(dir:'',file:coordmapobject_instance.mapObjectType.imageName)}"/>"""
            } else if (coordmapobject_instance.imagecoord) {
                image_str = """<img style="max-height:12px;" src="/fc/route/get_mapobject_symbol/${coordmapobject_instance.id}"/>"""
            }
            outln"""            <td style="white-space: nowrap;">${message(code:coordmapobject_instance.mapObjectType.code)} ${image_str}</td>"""
            outln"""            <td style="white-space: nowrap;">${coordmapobject_instance.mapObjectText}</td>"""
            if (coordmapobject_instance.mapObjectType == MapObjectType.Airfield) {
                String details_str = "${coordmapobject_instance.gateDirection}${message(code:'fc.grad')}"
                if (coordmapobject_instance.mapObjectGliderAirfield) {
                    details_str += ", ${message(code:'fc.coordroute.mapobjects.details.gliderairfield')}"
                }
                if (coordmapobject_instance.mapObjectPavedAirfield) {
                    details_str += ", ${message(code:'fc.coordroute.mapobjects.details.pavedairfield')}"
                }
                outln"""        <td style="white-space: nowrap;">${details_str}</td>"""
            } else {
                outln"""        <td style="white-space: nowrap;"/>"""
            }
            outln"""            <td style="white-space: nowrap;">${coordmapobject_instance.latName()}</td>"""
            outln"""            <td style="white-space: nowrap;">${coordmapobject_instance.lonName()}</td>"""
            outln"""        </tr>"""
        }
        outln"""        </tbody>"""
        outln"""    </table>"""
        outln"""</div>"""
        outln"""<script>"""
        outln"""    function showmapobjects_click() {"""
        outln"""        var show_mapobjects = \$("#contestMapShowMapObjects").prop("checked");"""
        outln"""        \$("#showmapobjects_id").prop("hidden", !show_mapobjects);"""
        outln"""        \$.post("/fc/route/saveshow_ajax", {id:${attrs.route.id}, contestMapShowMapObjects:show_mapobjects}, "json");"""
        outln"""    }"""
        outln"""    function additionals_click() {"""
        outln"""        var additionals = \$("#contestMapAdditionals").prop("checked");"""
        outln"""        \$.post("/fc/route/saveshow_ajax", {id:${attrs.route.id}, contestMapAdditionals:additionals}, "json");"""
        outln"""    }"""
        outln"""    function importmapobjects_click() {"""
        outln"""        window.location.href = "/fc/route/importmapobject?routeid=${attrs.route.id}";"""
        outln"""    }"""
        outln"""    function addmapobject_click() {"""
        outln"""        window.location.href = "/fc/coordMapObject/create?routeid=${attrs.route.id}";"""
        outln"""    }"""
        outln"""    function removeallmapobjects_click() {"""
        outln"""        window.location.href = "/fc/coordMapObject/removeall?routeid=${attrs.route.id}";"""
        outln"""    }"""
        outln"""</script>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    def contestMapSmallRoadsGradeSelect = { attrs, body ->
        String checked_none = ""
        String checked_1 = ""
        String checked_2 = ""
        String checked_3 = ""
        String checked_4 = ""
        String checked_5 = ""
        if (attrs.route.contestMapSmallRoadsGrade == Defs.CONTESTMAPSMALLROADSGRADE_1) {
            checked_1 = "checked"
        } else if (attrs.route.contestMapSmallRoadsGrade == Defs.CONTESTMAPSMALLROADSGRADE_2) {
            checked_2 = "checked"
        } else if (attrs.route.contestMapSmallRoadsGrade == Defs.CONTESTMAPSMALLROADSGRADE_3) {
            checked_3 = "checked"
        } else if (attrs.route.contestMapSmallRoadsGrade == Defs.CONTESTMAPSMALLROADSGRADE_4) {
            checked_4 = "checked"
        } else if (attrs.route.contestMapSmallRoadsGrade == Defs.CONTESTMAPSMALLROADSGRADE_5) {
            checked_5 = "checked"
        } else {
            checked_none = "checked"
        }
        outln"""<label>${message(code:'fc.contestmap.contestmapsmallroads')}</label>:"""
        outln"""<label><input type="radio" name="contestMapSmallRoadsGrade" value="${Defs.CONTESTMAPSMALLROADSGRADE_NONE}" ${checked_none} tabIndex="${attrs.ti[0]++}"/>${message(code:'fc.contestmap.contestmapsmallroads.none')}</label>"""
        outln"""<label><input type="radio" name="contestMapSmallRoadsGrade" value="${Defs.CONTESTMAPSMALLROADSGRADE_1}" ${checked_1} tabIndex="${attrs.ti[0]++}"/>${Defs.CONTESTMAPSMALLROADSGRADE_1}</label>"""
        outln"""<label><input type="radio" name="contestMapSmallRoadsGrade" value="${Defs.CONTESTMAPSMALLROADSGRADE_2}" ${checked_2} tabIndex="${attrs.ti[0]++}"/>${Defs.CONTESTMAPSMALLROADSGRADE_2}</label>"""
        outln"""<label><input type="radio" name="contestMapSmallRoadsGrade" value="${Defs.CONTESTMAPSMALLROADSGRADE_3}" ${checked_3} tabIndex="${attrs.ti[0]++}"/>${Defs.CONTESTMAPSMALLROADSGRADE_3}</label>"""
        outln"""<label><input type="radio" name="contestMapSmallRoadsGrade" value="${Defs.CONTESTMAPSMALLROADSGRADE_4}" ${checked_4} tabIndex="${attrs.ti[0]++}"/>${Defs.CONTESTMAPSMALLROADSGRADE_4}</label>"""
        outln"""<label><input type="radio" name="contestMapSmallRoadsGrade" value="${Defs.CONTESTMAPSMALLROADSGRADE_5}" ${checked_5} tabIndex="${attrs.ti[0]++}"/>${Defs.CONTESTMAPSMALLROADSGRADE_5}</label>"""
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
    private checkBoxClick(String name, boolean checked, String label, List labelArgs, String onClick, attrs)
    {
        outln"""    <input type="hidden" name="_${name}"/>"""
        if (checked) {
            outln"""<input type="checkbox" id="${name}" name="${name}" checked="checked" tabIndex="${attrs.ti[0]++}" onclick="${onClick}"/>"""
        } else {
            outln"""<input type="checkbox" id="${name}" name="${name}" tabIndex="${attrs.ti[0]++}" onclick="${onClick}"/>"""
        }
        outln"""    <label>${message(code:label, args:labelArgs)}</label>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private String coordroutenum(Coord coordInstance, int num, long nextId, String link) {
        String t = num.toString()
        t += " ..."
        String ret = ""
        if (nextId) {
            ret = """<a href="${link}/${coordInstance.id}?next=${nextId}">${t}</a>""" // .encodeAsHTML()
        } else {
            ret = """<a href="${link}/${coordInstance.id}">${t}</a>""" // .encodeAsHTML()
        }
        return ret
    }
   
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
