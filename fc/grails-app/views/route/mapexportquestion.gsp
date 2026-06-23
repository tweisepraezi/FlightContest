<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:set var="map_upload_job_status" value="${routeInstance.GetMapUploadJobStatus()}"/>
        <g:if test="${!NewOSMMap && (BreakButton || map_upload_job_status == UploadJobStatus.Waiting || map_upload_job_status == UploadJobStatus.Sending)}" >
            <meta http-equiv="refresh" content="5">
        </g:if>
        <title>${message(code:'fc.contestmap')} ${routeInstance.name()} ${Defs.ROUTE_NUM}${routeInstance.idTitle}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <g:if test="${NewOSMMap}">
                    <h2>${message(code:'fc.contestmap')} ${routeInstance.name()} ${Defs.ROUTE_NUM}${routeInstance.idTitle} - ${message(code:'fc.contestmap.edition')} ${routeInstance.contestMapEdition+1}</h2>
                </g:if>
                <g:else>
                    <h2>${message(code:'fc.contestmap')} ${routeInstance.name()} ${Defs.ROUTE_NUM}${routeInstance.idTitle} - ${message(code:'fc.contestmap.edition')} ${routeInstance.contestMapEdition}</h2>
                </g:else>
                <div class="block" id="forms">
                    <g:form params="${['mapexportquestionReturnAction':mapexportquestionReturnAction, 'mapexportquestionReturnController':mapexportquestionReturnController, 'mapexportquestionReturnID':mapexportquestionReturnID, 'routeid':routeInstance.id]}">
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${routeInstance.GetNextRouteID()}"/>
						<g:set var="prev_id" value="${routeInstance.GetPrevRouteID()}"/>
                        <g:if test="${NewOSMMap}">
	                        <table>
	                            <tbody>
	                                <tr>
	                                    <td><p class="warning">${message(code:'fc.contestmap.warning')}</p></td>
	                                    <td><a href="/fc/docs/help_${session.showLanguage}.html#osm-contest-map" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></td>
	                                </tr>
	                            </tbody>
	                        </table>
	                        <fieldset>
                                <g:actionSubmit action="mapsavesettings" value="${message(code:'fc.save')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;" tabIndex="${ti[0]++}" />
                                <g:if test="${CoordRoute.findByRouteAndType(routeInstance,CoordType.TO,[sort:"id"])}" >
                                    <g:actionSubmit action="mapgenerate_airportarea" value="${message(code:'fc.generate.airportarea.online')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_airportarea_taskcreator" value="${message(code:'fc.generate.airportarea.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;" tabIndex="${ti[0]++}" />
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="mapgenerate_airportarea" value="${message(code:'fc.generate.airportarea.online')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;" tabIndex="${ti[0]++}" disabled="disabled" />
                                    <g:actionSubmit action="mapgenerate_airportarea_taskcreator" value="${message(code:'fc.generate.airportarea.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;" tabIndex="${ti[0]++}" disabled="disabled" />
                                </g:else>
                                <g:if test="${next_id}">
                                    <g:actionSubmit action="gotonext_mapexportquestion" value="${message(code:'fc.route.gotonext')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="gotonext_mapexportquestion" value="${message(code:'fc.route.gotonext')}" disabled tabIndex="${ti[0]++}"/>
                                </g:else>
                                <g:if test="${prev_id}">
                                    <g:actionSubmit action="gotoprev_mapexportquestion" value="${message(code:'fc.route.gotoprev')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="gotoprev_mapexportquestion" value="${message(code:'fc.route.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
                                </g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
	                        </fieldset>
	                        <fieldset>
                                <g:showContestMapSelectOtherRoute route="${routeInstance}" ti="${ti}"/>
								<div>
                                    <g:if test="${routeInstance.contestMapShowMapObjectsFromRouteID}">
                                        <g:set var="route_instance2" value="${Route.get(routeInstance.contestMapShowMapObjectsFromRouteID)}"/>
                                        <g:contestMapAirfieldsSelect airfields="${routeInstance.contestMapAirfields}" airfieldsname="contestMapAirfields" showairfields="${routeInstance.contestMapShowAirfields}" routeid="${routeInstance.id}" airfieldslines="${Tools.Split(routeInstance.contestMapAirfieldsData,'\n').size()}" airfieldsfromname="${route_instance2.name()}" airfieldslines2="${Tools.Split(route_instance2.contestMapAirfieldsData,'\n').size()}" ti="${ti}"/>
                                    </g:if>
                                    <g:else>
                                        <g:contestMapAirfieldsSelect airfields="${routeInstance.contestMapAirfields}" airfieldsname="contestMapAirfields" showairfields="${routeInstance.contestMapShowAirfields}" routeid="${routeInstance.id}" airfieldslines="${Tools.Split(routeInstance.contestMapAirfieldsData,'\n').size()}" ti="${ti}"/>
                                    </g:else>
                                    <g:if test="${BootStrap.global.IsOpenAIP()}">
                                        <g:set var="showairfields" value="hidden"/>
                                        <g:if test="${routeInstance.contestMapShowAirfields}">
                                            <g:set var="showairfields" value=""/>
                                        </g:if>
                                        <div style="margin-top:10px;margin-left:20px;" id="showairfields_id" title="${message(code:'fc.contestmap.contestmapairfields.edit',args:[])}" ${showairfields}>
                                            <g:if test="${!routeInstance.contestMapShowMapObjectsFromRouteID}">
                                                <g:if test="${routeInstance.contestMapAirfieldsData}">
                                                    <g:actionSubmit action="getairfields_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairfields.airportarea')}" onclick="return confirm('${message(code:'fc.contestmap.contestmapgetairfields.airportarea.overwrite')}');" tabIndex="${ti[0]++}" />
                                                </g:if>
                                                <g:else>
                                                    <g:actionSubmit action="getairfields_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairfields.airportarea')}" tabIndex="${ti[0]++}" />
                                                </g:else>
                                            </g:if>
                                            <g:actionSubmit action="csvexport_airfields_route" value="${message(code:'fc.contestmap.contestmapairfields.csvexport')}" tabIndex="${ti[0]++}" />
                                            <g:if test="${BootStrap.global.IsContestMapDevOptions()}">
                                                <label style="margin-left:20px;">Dev:</label>
                                                <g:actionSubmit action="csvexportold_airfields_route" value="${message(code:'fc.contestmap.contestmapairfields.csvexport')} (old)" tabIndex="${ti[0]++}" />
                                                <g:actionSubmit action="csvexport_check_airfields_route" value="${message(code:'fc.contestmap.contestmapairfields.csvexport.check')}" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <p style="margin-top:10px;">
                                                <g:textArea id="contestMapAirfieldsData" name="contestMapAirfieldsData" value="${fieldValue(bean:routeInstance,field:'contestMapAirfieldsData')}" rows="5" style="width:100%;white-space:pre;" tabIndex="${ti[0]++}"/>
                                            </p>
                                        </div>
                                    </g:if>
								</div>
	                            <g:if test="${BootStrap.global.IsOpenAIP()}">
	                                <div>
	                                    <g:checkBox name="contestMapAirspaces" value="${routeInstance.contestMapAirspaces}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapairspaces')}</label>
                                        <g:if test="${routeInstance.contestMapShowMapObjectsFromRouteID}">
                                            <g:set var="route_instance2" value="${Route.get(routeInstance.contestMapShowMapObjectsFromRouteID)}"/>
                                            <label style="background-color:yellow;">${message(code:'fc.contestmap.contestmap.entrynum2',args:[Tools.Split(routeInstance.contestMapAirspacesLayer2,'\n').size(),Tools.Split(route_instance2.contestMapAirspacesLayer2,'\n').size(),route_instance2.name()])}</label>
                                        </g:if>
                                        <g:else>
                                            <label style="background-color:yellow;">${message(code:'fc.contestmap.contestmap.entrynum',args:[Tools.Split(routeInstance.contestMapAirspacesLayer2,'\n').size()])}</label>
                                        </g:else>
                                        <g:if test="${routeInstance.contestMapShowAirspaces}">
                                            <a id="showairspaces_off_id" href="#x" class="arrowhead" onclick="showairspaces(false);">${Defs.ARROWHEAD_UP}</a>
                                            <a id="showairspaces_on_id" href="#x" class="arrowhead" onclick="showairspaces(true);" hidden>${Defs.ARROWHEAD_DOWN}</a>
                                        </g:if>
                                        <g:else>
                                            <a id="showairspaces_off_id" href="#x" class="arrowhead" onclick="showairspaces(false);" hidden>${Defs.ARROWHEAD_UP}</a>
                                            <a id="showairspaces_on_id" href="#x" class="arrowhead" onclick="showairspaces(true);">${Defs.ARROWHEAD_DOWN}</a>
                                        </g:else>
                                        <g:set var="show_airspaces" value="hidden"/>
                                        <g:if test="${routeInstance.contestMapShowAirspaces}">
                                            <g:set var="show_airspaces" value=""/>
                                        </g:if>
                                        <div style="margin-top:10px;margin-left:20px;" id="showairspaces_id" title="${message(code:'fc.contestmap.contestmapairspaces.edit',args:[])}" ${show_airspaces}>
                                            <g:if test="${!routeInstance.contestMapShowMapObjectsFromRouteID}">
                                                <g:if test="${routeInstance.contestMapAirspacesLayer2}">
                                                    <g:actionSubmit action="getairspaces_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairspaces.airportarea')}" onclick="return confirm('${message(code:'fc.contestmap.contestmapgetairspaces.airportarea.overwrite')}');" tabIndex="${ti[0]++}" />
                                                </g:if>
                                                <g:else>
                                                    <g:actionSubmit action="getairspaces_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairspaces.airportarea')}" tabIndex="${ti[0]++}" />
                                                </g:else>
                                                <input type="text" name="contestMapAirspacesLowerLimit" value="${routeInstance.contestMapAirspacesLowerLimit}" min="0" size="6" tabIndex="${ti[0]++}"/> <label>${message(code:'fc.foot')}</label>
                                            </g:if>
                                            <g:actionSubmit action="importairspace" value="${message(code:'fc.contestmap.contestmapairspaces.importairspace')}" tabIndex="${ti[0]++}" />
                                            <g:actionSubmit action="kmzexportairspaces_route" value="${message(code:'fc.contestmap.contestmapairspaces.kmzexport')}" tabIndex="${ti[0]++}" />
                                            <g:actionSubmit action="kmzexportairspaceshidden_route" value="${message(code:'fc.contestmap.contestmapairspaces.kmzexport.hidden')}" tabIndex="${ti[0]++}" />
                                            <p style="margin-top:10px;">
                                                <g:textArea id="contestMapAirspacesLayer2" name="contestMapAirspacesLayer2" value="${fieldValue(bean:routeInstance,field:'contestMapAirspacesLayer2')}" rows="5" style="width:100%;white-space:pre;" tabIndex="${ti[0]++}"/>
                                            </p>
                                        </div>
                                        <script>
                                            function showairspaces(showAirspaces) {
                                                $("#showairspaces_id").prop("hidden", !showAirspaces);
                                                $("#showairspaces_off_id").prop("hidden", !showAirspaces);
                                                $("#showairspaces_on_id").prop("hidden", showAirspaces);
                                                $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowAirspaces:showAirspaces}, "json");
                                            }
                                        </script>
	                                </div>
	                            </g:if>
                                <g:showContestMapRouteMapObjects route="${routeInstance}" ti="${ti}"/>
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <g:checkBox name="contestMapGraticule" value="${routeInstance.contestMapGraticule}" tabIndex="${ti[0]++}" />
                                    <label style="margin-right:10px;">${message(code:'fc.contestmap.contestmapgraticule')}</label>
	                                <g:checkBox name="contestMapChurches" value="${routeInstance.contestMapChurches}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapchurches')}</label>
	                                <img style="margin-right:10px;" src="${createLinkTo(dir:'images/map',file:'church.png')}"/>
	                                <g:checkBox name="contestMapCastles" value="${routeInstance.contestMapCastles}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapcastles')}</label>
	                                <img src="${createLinkTo(dir:'images/map',file:'castle.png')}"/><img style="margin-right:10px;" src="${createLinkTo(dir:'images/map',file:'castle_ruin.png')}"/>
                                    <g:checkBox name="contestMapWindpowerstations" value="${routeInstance.contestMapWindpowerstations}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmapwindpowerstations')}</label>
                                    <img style="margin-right:10px;" src="${createLinkTo(dir:'images/map',file:'windpowerstation.png')}"/>
                                    <g:checkBox name="contestMapPowerlines" value="${routeInstance.contestMapPowerlines}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmappowerlines')}</label>
                                </div>
                                <div>
                                    <g:contestMapSmallRoadsGradeSelect route="${routeInstance}" ti="${ti}" />
                                </div>
                                <div>
                                    <g:contestMapContourLinesSelect contourlines="${routeInstance.contestMapContourLines}" contourlinesname="contestMapContourLines" ti="${ti}"/>
                                </div>
	                            <g:if test="${false}">
	                                <div>
	                                    <g:checkBox name="contestMapDropShadow" value="${routeInstance.contestMapDropShadow}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapdropshadow')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${false}">
	                                <div>
	                                    <g:checkBox name="contestMapMunicipalityNames" value="${routeInstance.contestMapMunicipalityNames}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapmunicipalitynames')}</label>
	                                </div>
	                            </g:if>
	                        </fieldset>
                            <g:contestMapDevOptions output="${routeInstance.contestMapOutput}" outputname="contestMapOutput" devstyle="${routeInstance.contestMapDevStyle}" devstylename="contestMapDevStyle" ti="${ti}"/>
	                        <fieldset>
	                            <g:set var="route_data" value="${routeInstance.GetRouteData()}" />
                                <g:if test="${!routeInstance.corridorWidth}">
                                    <div>
                                        <g:checkBox name="contestMapCircle" value="${routeInstance.contestMapCircle}" tabIndex="${ti[0]++}" />
                                        <label>${message(code:'fc.contestmap.contestmapcircle')}</label>
                                    </div>
		                        </g:if>
	                            <g:if test="${route_data.procedureturn_num && routeInstance.useProcedureTurns}">
		                            <div>
		                                <g:checkBox name="contestMapProcedureTurn" value="${routeInstance.contestMapProcedureTurn}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapprocedureturn')}</label>
		                            </div>
		                        </g:if>
	                            <div>
	                                <g:checkBox name="contestMapLeg" value="${routeInstance.contestMapLeg}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapleg')}</label>
	                            </div>
                                <g:if test="${route_data.curved_num}">
		                            <div>
		                                <g:checkBox name="contestMapCurvedLeg" value="${routeInstance.contestMapCurvedLeg}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapcurvedleg')}: </label>
                                        <g:contestMapCurvedPointsInput r="${routeInstance}" tp="${routeInstance.contestMapCurvedLegPoints}" tpid="${Defs.TurnpointID_ContestMapCurvedLegPoints}" ti="${ti}" />
		                            </div>
                                </g:if>
	                            <div>
	                                <g:checkBox name="contestMapTpName" value="${routeInstance.contestMapTpName}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmaptpname')}</label>
	                            </div>
	                            <g:if test="${route_data.secret_num > 0}">
	                                <div>
	                                    <g:checkBox name="contestMapSecretGates" value="${routeInstance.contestMapSecretGates}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapsecretgates')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${!routeInstance.corridorWidth && routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()}">
	                                <div>
	                                    <g:checkBox name="contestMapEnroutePhotos" value="${routeInstance.contestMapEnroutePhotos}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.observationtest.enroutephotos')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${!routeInstance.corridorWidth && routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()}">
	                                <div>
	                                    <g:checkBox name="contestMapEnrouteCanvas" value="${routeInstance.contestMapEnrouteCanvas}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.observationtest.enroutecanvas')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${!routeInstance.corridorWidth && routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas}">
	                                <div>
	                                    <g:checkBox name="contestMapTurnpointSign" value="${routeInstance.contestMapTurnpointSign}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.observationtest.turnpointsign')}</label>
	                                </div>
	                            </g:if>
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <label>${message(code:'fc.contestmap.printfirstoptions')}${routeInstance.GetFirstRouteName()}</label>
                                    <g:set var="show_firstoptions" value=""/>
                                    <g:if test="${routeInstance.contestMapShowFirstOptions}">
                                        <a id="showfirstoptions_off_id" href="#x" class="arrowhead" onclick="showfirstoptions(false);">${Defs.ARROWHEAD_UP}</a>
                                        <a id="showfirstoptions_on_id" href="#x" class="arrowhead" onclick="showfirstoptions(true);" hidden>${Defs.ARROWHEAD_DOWN}</a>
                                    </g:if>
                                    <g:else>
                                        <g:set var="show_firstoptions" value="hidden"/>
                                        <a id="showfirstoptions_off_id" href="#x" class="arrowhead" onclick="showfirstoptions(false);" hidden>${Defs.ARROWHEAD_UP}</a>
                                        <a id="showfirstoptions_on_id" href="#x" class="arrowhead" onclick="showfirstoptions(true);">${Defs.ARROWHEAD_DOWN}</a>
                                    </g:else>
                                    <div style="margin-left:20px;" id="showfirstoptions_id" ${show_firstoptions}>
                                        <div>
                                            <br/>
                                            <p>
                                                <input type="text" id="contestMapFirstTitle" name="contestMapFirstTitle" value="${fieldValue(bean:routeInstance,field:'contestMapFirstTitle')}" tabIndex="${ti[0]++}"/>
                                            </p>
                                        </div>
                                        <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos}" vpname="contestMapCenterVerticalPos" hp="${routeInstance.contestMapCenterHorizontalPos}" hpname="contestMapCenterHorizontalPos" ti="${ti}"/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints}" tpid="${Defs.TurnpointID_ContestMapCenterPoints}" ti="${ti}" printpoints="${false}"/>
                                        <table class="mapexportquestiontable">
                                            <tr class="mapexportquestiontable">
                                                <td class="mapexportquestiontable">
                                                    <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveX" fieldvalue="${routeInstance.contestMapCenterMoveX}" ti="${ti}"/>
                                                </td>
                                                <td class="mapexportquestiontable">
                                                    <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveY" fieldvalue="${routeInstance.contestMapCenterMoveY}" ti="${ti}"/>
                                                </td>
                                                <g:if test="${routeInstance.corridorWidth}">
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapShowCorridorWidth r="${routeInstance}"/>
                                                    </td>
                                                </g:if>
                                            </tr>
                                        </table>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints}" tpid="${Defs.TurnpointID_ContestMapPrintPoints}" ti="${ti}" printpoints="${true}"/>
                                        <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize}" printsizename="contestMapPrintSize" printlandscape="${routeInstance.contestMapPrintLandscape}" printlandscapename="contestMapPrintLandscape" ti="${ti}"/>
                                        <br/>
                                        <input type="hidden" name="id" value="${routeInstance?.id}" />
                                        <g:actionSubmit action="mapgenerate" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                        <g:if test="${!routeInstance.IsOtherRoute()}" >
                                            <g:actionSubmit action="mapgenerate_takeoff" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            <g:actionSubmit action="mapgenerate_noroute" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                        </g:if>
                                        <g:if test="${!routeInstance.corridorWidth}">
                                            <g:actionSubmit action="mapgenerate_allroutedetails" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                        </g:if>
                                        <g:if test="${!routeInstance.IsOtherRoute()}" >
                                            <g:actionSubmit action="mapgenerate_taskcreator" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                        </g:if>    
                                        <g:if test="${routeInstance.IsOtherRoute()}" >
                                            <g:actionSubmit action="mapgenerate_anrmap_allroutes" value="${message(code:'fc.generate.anrmap.allroutes')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;" tabIndex="${ti[0]++}" />
                                        </g:if>    
                                    </div>
                                    <script>
                                        function showfirstoptions(showFirstOptions) {
                                            $("#showfirstoptions_id").prop("hidden", !showFirstOptions);
                                            $("#showfirstoptions_off_id").prop("hidden", !showFirstOptions);
                                            $("#showfirstoptions_on_id").prop("hidden", showFirstOptions);
                                            $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowFirstOptions:showFirstOptions}, "json");
                                        }
                                    </script>
                                </div>
                                <g:if test="${routeInstance.ShowSecondMapOptions()}">
                                    <div style="margin-top:10px;">
                                        <label>${message(code:'fc.contestmap.printsecondoptions')}${routeInstance.GetSecondRouteName()}</label>
                                        <g:set var="show_secondoptions" value=""/>
                                        <g:if test="${routeInstance.contestMapShowSecondOptions}">
                                            <a id="showsecondoptions_off_id" href="#x" class="arrowhead" onclick="showsecondoptions(false);">${Defs.ARROWHEAD_UP}</a>
                                            <a id="showsecondoptions_on_id" href="#x" class="arrowhead" onclick="showsecondoptions(true);" hidden>${Defs.ARROWHEAD_DOWN}</a>
                                        </g:if>
                                        <g:else>
                                            <g:set var="show_secondoptions" value="hidden"/>
                                            <a id="showsecondoptions_off_id" href="#x" class="arrowhead" onclick="showsecondoptions(false);" hidden>${Defs.ARROWHEAD_UP}</a>
                                            <a id="showsecondoptions_on_id" href="#x" class="arrowhead" onclick="showsecondoptions(true);">${Defs.ARROWHEAD_DOWN}</a>
                                        </g:else>
                                        <div style="margin-left:20px;" id="showsecondoptions_id" ${show_secondoptions}>
                                            <div>
                                                <br/>
                                                <p>
                                                    <input type="text" id="contestMapSecondTitle" name="contestMapSecondTitle" value="${fieldValue(bean:routeInstance,field:'contestMapSecondTitle')}" tabIndex="${ti[0]++}"/>
                                                </p>
                                            </div>
                                            <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos2}" vpname="contestMapCenterVerticalPos2" hp="${routeInstance.contestMapCenterHorizontalPos2}" hpname="contestMapCenterHorizontalPos2" ti="${ti}"/>
                                            <g:contestMapRoutePointsInput r="${routeInstance.GetSecondRoute()}" tp="${routeInstance.contestMapCenterPoints2}" tpid="${Defs.TurnpointID_ContestMapCenterPoints2}" ti="${ti}" printpoints="${false}"/>
                                            <table class="mapexportquestiontable">
                                                <tr class="mapexportquestiontable">
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveX2" fieldvalue="${routeInstance.contestMapCenterMoveX2}" ti="${ti}"/>
                                                    </td>
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveY2" fieldvalue="${routeInstance.contestMapCenterMoveY2}" ti="${ti}"/>
                                                    </td>
                                                    <g:if test="${routeInstance.corridorWidth}">
                                                        <td class="mapexportquestiontable">
                                                            <g:contestMapShowCorridorWidth r="${routeInstance}"/>
                                                        </td>
                                                        <td class="mapexportquestiontable">
                                                            <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.corridorwidth.deviating')} [${message(code:'fc.mile')}]:" fieldid="corridorWidth2" fieldvalue="${routeInstance.corridorWidth2}" ti="${ti}"/>
                                                        </td>
                                                    </g:if>
                                                </tr>
                                            </table>
                                            <g:contestMapRoutePointsInput r="${routeInstance.GetSecondRoute()}" tp="${routeInstance.contestMapPrintPoints2}" tpid="${Defs.TurnpointID_ContestMapPrintPoints2}" ti="${ti}" printpoints="${true}"/>
                                            <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize2}" printsizename="contestMapPrintSize2" printlandscape="${routeInstance.contestMapPrintLandscape2}" printlandscapename="contestMapPrintLandscape2" ti="${ti}"/>
                                            <g:if test="${routeInstance.corridorWidth}">
                                            </g:if>
                                            <br/>
                                            <g:actionSubmit action="mapgenerate2" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            <g:if test="${!routeInstance.IsOtherRoute()}" >
                                                <g:actionSubmit action="mapgenerate_takeoff2" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                                <g:actionSubmit action="mapgenerate_noroute2" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:if test="${!routeInstance.corridorWidth}">
                                                <g:actionSubmit action="mapgenerate_allroutedetails2" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:if test="${!routeInstance.IsOtherRoute()}" >
                                                <g:actionSubmit action="mapgenerate_taskcreator2" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>    
                                        </div>
                                        <script>
                                            function showsecondoptions(showThirdOptions) {
                                                $("#showsecondoptions_id").prop("hidden", !showThirdOptions);
                                                $("#showsecondoptions_off_id").prop("hidden", !showThirdOptions);
                                                $("#showsecondoptions_on_id").prop("hidden", showThirdOptions);
                                                $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowSecondOptions:showThirdOptions}, "json");
                                            }
                                        </script>
                                    </div>
                                </g:if>
                                <g:if test="${routeInstance.ShowThirdMapOptions()}">
                                    <div style="margin-top:10px;">
                                        <label>${message(code:'fc.contestmap.printthirdoptions')}${routeInstance.GetThirdRouteName()}</label>
                                        <g:set var="show_thirdoptions" value=""/>
                                        <g:if test="${routeInstance.contestMapShowThirdOptions}">
                                            <a id="showthirdoptions_off_id" href="#x" class="arrowhead" onclick="showthirdoptions(false);">${Defs.ARROWHEAD_UP}</a>
                                            <a id="showthirdoptions_on_id" href="#x" class="arrowhead" onclick="showthirdoptions(true);" hidden>${Defs.ARROWHEAD_DOWN}</a>
                                        </g:if>
                                        <g:else>
                                            <g:set var="show_thirdoptions" value="hidden"/>
                                            <a id="showthirdoptions_off_id" href="#x" class="arrowhead" onclick="showthirdoptions(false);" hidden>${Defs.ARROWHEAD_UP}</a>
                                            <a id="showthirdoptions_on_id" href="#x" class="arrowhead" onclick="showthirdoptions(true);">${Defs.ARROWHEAD_DOWN}</a>
                                        </g:else>
                                        <div style="margin-left:20px;" id="showthirdoptions_id" ${show_thirdoptions}>
                                            <div>
                                                <br/>
                                                <p>
                                                    <input type="text" id="contestMapThirdTitle" name="contestMapThirdTitle" value="${fieldValue(bean:routeInstance,field:'contestMapThirdTitle')}" tabIndex="${ti[0]++}"/>
                                                </p>
                                            </div>
                                            <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos3}" vpname="contestMapCenterVerticalPos3" hp="${routeInstance.contestMapCenterHorizontalPos3}" hpname="contestMapCenterHorizontalPos3" ti="${ti}"/>
                                            <g:contestMapRoutePointsInput r="${routeInstance.GetThirdRoute()}" tp="${routeInstance.contestMapCenterPoints3}" tpid="${Defs.TurnpointID_ContestMapCenterPoints3}" ti="${ti}" printpoints="${false}"/>
                                            <table class="mapexportquestiontable">
                                                <tr class="mapexportquestiontable">
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveX3" fieldvalue="${routeInstance.contestMapCenterMoveX3}" ti="${ti}"/>
                                                    </td>
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveY3" fieldvalue="${routeInstance.contestMapCenterMoveY3}" ti="${ti}"/>
                                                    </td>
                                                    <g:if test="${routeInstance.corridorWidth}">
                                                        <td class="mapexportquestiontable">
                                                            <g:contestMapShowCorridorWidth r="${routeInstance}"/>
                                                        </td>
                                                        <td class="mapexportquestiontable">
                                                            <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.corridorwidth.deviating')} [${message(code:'fc.mile')}]:" fieldid="corridorWidth3" fieldvalue="${routeInstance.corridorWidth3}" ti="${ti}"/>
                                                        </td>
                                                    </g:if>
                                                </tr>
                                            </table>
                                            <g:contestMapRoutePointsInput r="${routeInstance.GetThirdRoute()}" tp="${routeInstance.contestMapPrintPoints3}" tpid="${Defs.TurnpointID_ContestMapPrintPoints3}" ti="${ti}" printpoints="${true}"/>
                                            <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize3}" printsizename="contestMapPrintSize3" printlandscape="${routeInstance.contestMapPrintLandscape3}" printlandscapename="contestMapPrintLandscape3" ti="${ti}"/>
                                            <br/>
                                            <g:actionSubmit action="mapgenerate3" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            <g:if test="${!routeInstance.IsOtherRoute()}" >
                                                <g:actionSubmit action="mapgenerate_takeoff3" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                                <g:actionSubmit action="mapgenerate_noroute3" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:if test="${!routeInstance.corridorWidth}">
                                                <g:actionSubmit action="mapgenerate_allroutedetails3" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:if test="${!routeInstance.IsOtherRoute()}" >
                                                <g:actionSubmit action="mapgenerate_taskcreator3" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                        </div>
                                        <script>
                                            function showthirdoptions(showThirdOptions) {
                                                $("#showthirdoptions_id").prop("hidden", !showThirdOptions);
                                                $("#showthirdoptions_off_id").prop("hidden", !showThirdOptions);
                                                $("#showthirdoptions_on_id").prop("hidden", showThirdOptions);
                                                $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowThirdOptions:showThirdOptions}, "json");
                                            }
                                        </script>
                                    </div>
                                </g:if>
                                <g:if test="${routeInstance.ShowForthMapOptions()}">
                                    <div style="margin-top:10px;">
                                        <label>${message(code:'fc.contestmap.printforthoptions')}${routeInstance.GetForthRouteName()}</label>
                                        <g:set var="show_forthoptions" value=""/>
                                        <g:if test="${routeInstance.contestMapShowForthOptions}">
                                            <a id="showforthoptions_off_id" href="#x" class="arrowhead" onclick="showforthoptions(false);">${Defs.ARROWHEAD_UP}</a>
                                            <a id="showforthoptions_on_id" href="#x" class="arrowhead" onclick="showforthoptions(true);" hidden>${Defs.ARROWHEAD_DOWN}</a>
                                        </g:if>
                                        <g:else>
                                            <g:set var="show_forthoptions" value="hidden"/>
                                            <a id="showforthoptions_off_id" href="#x" class="arrowhead" onclick="showforthoptions(false);" hidden>${Defs.ARROWHEAD_UP}</a>
                                            <a id="showforthoptions_on_id" href="#x" class="arrowhead" onclick="showforthoptions(true);">${Defs.ARROWHEAD_DOWN}</a>
                                        </g:else>
                                        <div style="margin-left:20px;" id="showforthoptions_id" ${show_forthoptions}>
                                            <div>
                                                <br/>
                                                <p>
                                                    <input type="text" id="contestMapForthTitle" name="contestMapForthTitle" value="${fieldValue(bean:routeInstance,field:'contestMapForthTitle')}" tabIndex="${ti[0]++}"/>
                                                </p>
                                            </div>
                                            <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos4}" vpname="contestMapCenterVerticalPos4" hp="${routeInstance.contestMapCenterHorizontalPos4}" hpname="contestMapCenterHorizontalPos4" ti="${ti}"/>
                                            <g:contestMapRoutePointsInput r="${routeInstance.GetForthRoute()}" tp="${routeInstance.contestMapCenterPoints4}" tpid="${Defs.TurnpointID_ContestMapCenterPoints4}" ti="${ti}" printpoints="${false}"/>
                                            <table class="mapexportquestiontable">
                                                <tr class="mapexportquestiontable">
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveX4" fieldvalue="${routeInstance.contestMapCenterMoveX4}" ti="${ti}"/>
                                                    </td>
                                                    <td class="mapexportquestiontable">
                                                        <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:" fieldid="contestMapCenterMoveY4" fieldvalue="${routeInstance.contestMapCenterMoveY4}" ti="${ti}"/>
                                                    </td>
                                                    <g:if test="${routeInstance.corridorWidth}">
                                                        <td class="mapexportquestiontable">
                                                            <g:contestMapShowCorridorWidth r="${routeInstance}"/>
                                                        </td>
                                                        <td class="mapexportquestiontable">
                                                            <g:contestMapRouteInputField r="${routeInstance}" fieldlabel="${message(code:'fc.corridorwidth.deviating')} [${message(code:'fc.mile')}]:" fieldid="corridorWidth4" fieldvalue="${routeInstance.corridorWidth4}" ti="${ti}"/>
                                                        </td>
                                                    </g:if>
                                                </tr>
                                            </table>
                                            <g:contestMapRoutePointsInput r="${routeInstance.GetForthRoute()}" tp="${routeInstance.contestMapPrintPoints4}" tpid="${Defs.TurnpointID_ContestMapPrintPoints4}" ti="${ti}" printpoints="${true}"/>
                                            <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize4}" printsizename="contestMapPrintSize4" printlandscape="${routeInstance.contestMapPrintLandscape4}" printlandscapename="contestMapPrintLandscape4" ti="${ti}"/>
                                            <br/>
                                            <g:actionSubmit action="mapgenerate4" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            <g:if test="${!routeInstance.IsOtherRoute()}" >
                                                <g:actionSubmit action="mapgenerate_takeoff4" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                                <g:actionSubmit action="mapgenerate_noroute4" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:if test="${!routeInstance.corridorWidth}">
                                                <g:actionSubmit action="mapgenerate_allroutedetails4" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:if test="${!routeInstance.IsOtherRoute()}" >
                                                <g:actionSubmit action="mapgenerate_taskcreator4" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                            </g:if>
                                        </div>
                                        <script>
                                            function showforthoptions(showForthOptions) {
                                                $("#showforthoptions_id").prop("hidden", !showForthOptions);
                                                $("#showforthoptions_off_id").prop("hidden", !showForthOptions);
                                                $("#showforthoptions_on_id").prop("hidden", showForthOptions);
                                                $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowForthOptions:showForthOptions}, "json");
                                            }
                                        </script>
                                    </div>
                                </g:if>
	                        </fieldset>
                        </g:if>
                        <g:else>
							<g:contestMapRoutePrintOptions r="${routeInstance}" BreakButton="${BreakButton}" FetchButton="${FetchButton}" PrintButton="${PrintButton}" OtherRouteId="${OtherRouteId}" PrintSize="${PrintSize}" MapProjection="${MapProjection}" map_upload_job_status="${map_upload_job_status}" ti="${ti}"/>
                            <input type="hidden" name="id" value="${routeInstance?.id}" />
                            <g:if test="${BreakButton}">
                                <g:actionSubmit action="mapbreak" value="${message(code:'fc.contestmap.job.break')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                                <g:actionSubmit action="mapdiscard" value="${message(code:'fc.contestmap.job.discard')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
                                <g:actionSubmit action="maprefresh" value="${message(code:'fc.refresh')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                            </g:if>
                            <g:elseif test="${FetchButton}">
                                <g:actionSubmit action="mapfetch" value="${message(code:'fc.contestmap.job.fetch')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                                <g:actionSubmit action="mapdiscard" value="${message(code:'fc.contestmap.job.discard')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
                                <g:actionSubmit action="maprefresh" value="${message(code:'fc.refresh')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                            </g:elseif>
                            <g:elseif test="${PrintButton}">
                                <g:actionSubmit id="mapsave_gotomap_id" action="mapsave_gotomap" value="${message(code:'fc.contestmap.job.save.gotomap')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                                <g:actionSubmit id="mapsave_id" action="mapsave" value="${message(code:'fc.contestmap.job.save')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                                <%--
                                <g:if test="${routeInstance.IsMapSendEMailPossible()}">
                                    <g:actionSubmit id="mapsendmail_id" action="mapsendmail" value="${message(code:'fc.route.sendmail')}" onclick="this.form.target='_self';return true;" title="${routeInstance.EMailAddress()}" tabIndex="${ti[0]++}"/>
                                    <script>
                                        if (!$("#${Defs.CONTESTMAPPRINT_PDFMAP}").prop("checked")) {
                                            $("#mapsendmail_id").prop("disabled", true);
                                        }
                                    </script>
                                </g:if>
                                <g:actionSubmit action="mapdiscard" value="${message(code:'fc.contestmap.job.finish')}" tabIndex="${ti[0]++}" />
                                --%>
                                <g:actionSubmit action="mapdiscard" value="${message(code:'fc.contestmap.job.discard')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
                            </g:elseif>
                            <g:else>
                                <g:actionSubmit action="maprefresh" value="${message(code:'fc.refresh')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                            </g:else>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                        </g:else>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>