<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:set var="map_upload_job_status" value="${routeInstance.GetMapUploadJobStatus()}"/>
        <g:if test="${!NewOSMMap && (BreakButton || map_upload_job_status == UploadJobStatus.Waiting || map_upload_job_status == UploadJobStatus.Sending)}" >
            <meta http-equiv="refresh" content="5">
        </g:if>
        <title>${message(code:'fc.contestmap')} ${routeInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <g:if test="${NewOSMMap}">
                    <h2>${message(code:'fc.contestmap')} ${routeInstance.name()} - ${message(code:'fc.contestmap.edition')} ${routeInstance.contestMapEdition+1}</h2>
                </g:if>
                <g:else>
                    <h2>${message(code:'fc.contestmap')} ${routeInstance.name()} - ${message(code:'fc.contestmap.edition')} ${routeInstance.contestMapEdition}</h2>
                </g:else>
                <div class="block" id="forms">
                    <g:form params="${['mapexportquestionReturnAction':mapexportquestionReturnAction, 'mapexportquestionReturnController':mapexportquestionReturnController, 'mapexportquestionReturnID':mapexportquestionReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
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
	                            <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInputPosition()}">
	                                <div>
	                                    <g:checkBox name="contestMapEnroutePhotos" value="${routeInstance.contestMapEnroutePhotos}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.observationtest.enroutephotos')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${routeInstance.enrouteCanvasRoute.IsEnrouteRouteInputPosition()}">
	                                <div>
	                                    <g:checkBox name="contestMapEnrouteCanvas" value="${routeInstance.contestMapEnrouteCanvas}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.observationtest.enroutecanvas')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${routeInstance.turnpointRoute == TurnpointRoute.AssignCanvas}">
	                                <div>
	                                    <g:checkBox name="contestMapTurnpointSign" value="${routeInstance.contestMapTurnpointSign}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.observationtest.turnpointsign')}</label>
	                                </div>
	                            </g:if>
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
	                        <fieldset>
								<div>
                                    <g:contestMapAirfieldsSelect airfields="${routeInstance.contestMapAirfields}" airfieldsname="contestMapAirfields" showairfields="${routeInstance.contestMapShowAirfields}" routeid="${routeInstance.id}" airfieldslines="${Tools.Split(routeInstance.contestMapAirfieldsData,'\n').size()}" ti="${ti}"/>
                                    <g:if test="${BootStrap.global.IsOpenAIP()}">
                                        <g:set var="showairfields" value="hidden"/>
                                        <g:if test="${routeInstance.contestMapShowAirfields}">
                                            <g:set var="showairfields" value=""/>
                                        </g:if>
                                        <div style="margin-top:10px;margin-left:20px;" id="showairfields_id" ${showairfields}>
                                            <g:if test="${routeInstance.contestMapAirfieldsData}">
                                                <g:actionSubmit action="getairfields_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairfields.airportarea')}" onclick="return confirm('${message(code:'fc.contestmap.contestmapgetairfields.airportarea.overwrite')}');" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="getairfields_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairfields.airportarea')}" tabIndex="${ti[0]++}" />
                                            </g:else>
                                            <g:actionSubmit action="csvexport_airfields_route" value="${message(code:'fc.contestmap.contestmapairfields.csvexport')}" tabIndex="${ti[0]++}" />
                                            <g:if test="${BootStrap.global.IsContestMapDevOptions()}">
                                                <label style="margin-left:20px;">Dev:</label>
                                                <g:actionSubmit action="csvexportold_airfields_route" value="${message(code:'fc.contestmap.contestmapairfields.csvexport')} (old)" tabIndex="${ti[0]++}" />
                                                <g:actionSubmit action="csvexport_check_airfields_route" value="${message(code:'fc.contestmap.contestmapairfields.csvexport.check')}" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <p style="margin-top:10px;">
                                                <g:textArea id="contestMapAirfieldsData" name="contestMapAirfieldsData" value="${fieldValue(bean:routeInstance,field:'contestMapAirfieldsData')}" rows="5" style="width:100%;" tabIndex="${ti[0]++}"/>
                                            </p>
                                        </div>
                                    </g:if>
								</div>
	                            <g:if test="${BootStrap.global.IsOpenAIP()}">
	                                <div style="margin-top:10px;">
	                                    <g:checkBox name="contestMapAirspaces" value="${routeInstance.contestMapAirspaces}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapairspaces')}</label>
                                        <g:checkBox style="margin-left:10px;" id="contestMapShowAirspaces" name="contestMapShowAirspaces" value="${routeInstance.contestMapShowAirspaces}" onclick="showairspaces_click();"/>
                                        <label>${message(code:'fc.contestmap.contestmapairspaces.edit')}</label>
                                        <label style="margin-left:10px;">${message(code:'fc.contestmap.contestmap.entrynum',args:[Tools.Split(routeInstance.contestMapAirspacesLayer2,'\n').size()])}</label>
                                        <g:set var="showairspaces" value="hidden"/>
                                        <g:if test="${routeInstance.contestMapShowAirspaces}">
                                            <g:set var="showairspaces" value=""/>
                                        </g:if>
                                        <div style="margin-top:10px;margin-left:20px;" id="showairspaces_id" ${showairspaces}>
                                            <g:if test="${routeInstance.contestMapAirspacesLayer2}">
                                                <g:actionSubmit action="getairspaces_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairspaces.airportarea')}" onclick="return confirm('${message(code:'fc.contestmap.contestmapgetairspaces.airportarea.overwrite')}');" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="getairspaces_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairspaces.airportarea')}" tabIndex="${ti[0]++}" />
                                            </g:else>
                                            <input type="text" name="contestMapAirspacesLowerLimit" value="${routeInstance.contestMapAirspacesLowerLimit}" min="0" size="6" tabIndex="${ti[0]++}"/> <label>${message(code:'fc.foot')}</label>
                                            <g:actionSubmit action="kmzexportairspaces_route" value="${message(code:'fc.contestmap.contestmapairspaces.kmzexport')}" tabIndex="${ti[0]++}" />
                                            <g:actionSubmit action="kmzexportairspaceshidden_route" value="${message(code:'fc.contestmap.contestmapairspaces.kmzexport.hidden')}" tabIndex="${ti[0]++}" />
                                            <p style="margin-top:10px;">
                                                <g:textArea id="contestMapAirspacesLayer2" name="contestMapAirspacesLayer2" value="${fieldValue(bean:routeInstance,field:'contestMapAirspacesLayer2')}" rows="5" style="width:100%;" tabIndex="${ti[0]++}"/>
                                            </p>
                                        </div>
                                        <script>
                                            function showairspaces_click() {
                                                var show_airspaces = $("#contestMapShowAirspaces").prop("checked");
                                                $("#showairspaces_id").prop("hidden", !show_airspaces);
                                                $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowAirspaces:show_airspaces}, "json");
                                            }
                                        </script>
	                                </div>
	                            </g:if>
                                <g:showContestMapRouteMapObjects route="${routeInstance}" ti="${ti}"/>
	                        </fieldset>
                            <g:contestMapDevOptions output="${routeInstance.contestMapOutput}" outputname="contestMapOutput" devstyle="${routeInstance.contestMapDevStyle}" devstylename="contestMapDevStyle" ti="${ti}"/>
	                        <fieldset>
                                <g:actionSubmit action="mapsavesettings" value="${message(code:'fc.save')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                <g:if test="${CoordRoute.findByRouteAndType(routeInstance,CoordType.TO,[sort:"id"])}" >
                                    <g:actionSubmit action="mapgenerate_airportarea" value="${message(code:'fc.generate.airportarea.online')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_airportarea_taskcreator" value="${message(code:'fc.generate.airportarea.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="mapgenerate_airportarea" value="${message(code:'fc.generate.airportarea.online')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" disabled="disabled" />
                                    <g:actionSubmit action="mapgenerate_airportarea_taskcreator" value="${message(code:'fc.generate.airportarea.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" disabled="disabled" />
                                </g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <g:checkBox id="firstoptions_checkbox_id" name="contestMapShowFirstOptions" value="${routeInstance.contestMapShowFirstOptions}" onclick="firstoptions_click();"/>
                                    <label>${message(code:'fc.contestmap.printfirstoptions')}</label>
                                </div>
                                <g:set var="firstoptions" value="hidden"/>
                                <g:if test="${routeInstance.contestMapShowFirstOptions}">
                                    <g:set var="firstoptions" value=""/>
                                </g:if>
                                <div id="firstoptions_id" ${firstoptions}>
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
									<g:actionSubmit action="mapgenerate_takeoff" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_noroute" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_taskcreator" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function firstoptions_click() {
                                        var show_options = $("#firstoptions_checkbox_id").prop("checked");
                                        $("#firstoptions_id").prop("hidden", !show_options);
                                        $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowFirstOptions:show_options}, "json");
                                    }
                                </script>
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <g:checkBox id="secondoptions_checkbox_id" name="contestMapShowSecondOptions" value="${routeInstance.contestMapShowSecondOptions}" onclick="secondoptions_click();"/>
                                    <label>${message(code:'fc.contestmap.printsecondoptions')}</label>
                                </div>
                                <g:set var="secondoptions" value="hidden"/>
                                <g:if test="${routeInstance.contestMapShowSecondOptions}">
                                    <g:set var="secondoptions" value=""/>
                                </g:if>
                                <div id="secondoptions_id" ${secondoptions}>
	                                <div>
                                        <br/>
	                                    <p>
	                                        <input type="text" id="contestMapSecondTitle" name="contestMapSecondTitle" value="${fieldValue(bean:routeInstance,field:'contestMapSecondTitle')}" tabIndex="${ti[0]++}"/>
	                                    </p>
	                                </div>
                                    <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos2}" vpname="contestMapCenterVerticalPos2" hp="${routeInstance.contestMapCenterHorizontalPos2}" hpname="contestMapCenterHorizontalPos2" ti="${ti}"/>
                                    <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints2}" tpid="${Defs.TurnpointID_ContestMapCenterPoints2}" ti="${ti}" printpoints="${false}"/>
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
                                    <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints2}" tpid="${Defs.TurnpointID_ContestMapPrintPoints2}" ti="${ti}" printpoints="${true}"/>
                                    <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize2}" printsizename="contestMapPrintSize2" printlandscape="${routeInstance.contestMapPrintLandscape2}" printlandscapename="contestMapPrintLandscape2" ti="${ti}"/>
                                    <g:if test="${routeInstance.corridorWidth}">
                                    </g:if>
                                    <br/>
                                    <g:actionSubmit action="mapgenerate2" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_takeoff2" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_noroute2" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails2" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_taskcreator2" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function secondoptions_click() {
                                        var show_options = $("#secondoptions_checkbox_id").prop("checked");
                                        $("#secondoptions_id").prop("hidden", !show_options);
                                        $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowSecondOptions:show_options}, "json");
                                    }
                                </script>
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <g:checkBox id="thirdoptions_checkbox_id" name="contestMapShowThirdOptions" value="${routeInstance.contestMapShowThirdOptions}" onclick="thirdoptions_click();"/>
                                    <label>${message(code:'fc.contestmap.printthirdoptions')}</label>
                                </div>
                                <g:set var="thirdoptions" value="hidden"/>
                                <g:if test="${routeInstance.contestMapShowThirdOptions}">
                                    <g:set var="thirdoptions" value=""/>
                                </g:if>
                                <div id="thirdoptions_id" ${thirdoptions}>
	                                <div>
                                        <br/>
	                                    <p>
	                                        <input type="text" id="contestMapThirdTitle" name="contestMapThirdTitle" value="${fieldValue(bean:routeInstance,field:'contestMapThirdTitle')}" tabIndex="${ti[0]++}"/>
	                                    </p>
	                                </div>
                                    <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos3}" vpname="contestMapCenterVerticalPos3" hp="${routeInstance.contestMapCenterHorizontalPos3}" hpname="contestMapCenterHorizontalPos3" ti="${ti}"/>
                                    <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints3}" tpid="${Defs.TurnpointID_ContestMapCenterPoints3}" ti="${ti}" printpoints="${false}"/>
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
                                    <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints3}" tpid="${Defs.TurnpointID_ContestMapPrintPoints3}" ti="${ti}" printpoints="${true}"/>
                                    <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize3}" printsizename="contestMapPrintSize3" printlandscape="${routeInstance.contestMapPrintLandscape3}" printlandscapename="contestMapPrintLandscape3" ti="${ti}"/>
                                    <br/>
                                    <g:actionSubmit action="mapgenerate3" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_takeoff3" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_noroute3" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails3" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_taskcreator3" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function thirdoptions_click() {
                                        var show_options = $("#thirdoptions_checkbox_id").prop("checked");
                                        $("#thirdoptions_id").prop("hidden", !show_options);
                                        $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowThirdOptions:show_options}, "json");
                                    }
                                </script>
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <g:checkBox id="forthoptions_checkbox_id" name="contestMapShowForthOptions" value="${routeInstance.contestMapShowForthOptions}" onclick="forthoptions_click();"/>
                                    <label>${message(code:'fc.contestmap.printforthoptions')}</label>
                                </div>
                                <g:set var="forthoptions" value="hidden"/>
                                <g:if test="${routeInstance.contestMapShowForthOptions}">
                                    <g:set var="forthoptions" value=""/>
                                </g:if>
                                <div id="forthoptions_id" ${forthoptions}>
	                                <div>
                                        <br/>
	                                    <p>
	                                        <input type="text" id="contestMapForthTitle" name="contestMapForthTitle" value="${fieldValue(bean:routeInstance,field:'contestMapForthTitle')}" tabIndex="${ti[0]++}"/>
	                                    </p>
	                                </div>
                                    <g:contestMapPos vp="${routeInstance.contestMapCenterVerticalPos4}" vpname="contestMapCenterVerticalPos4" hp="${routeInstance.contestMapCenterHorizontalPos4}" hpname="contestMapCenterHorizontalPos4" ti="${ti}"/>
                                    <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints4}" tpid="${Defs.TurnpointID_ContestMapCenterPoints4}" ti="${ti}" printpoints="${false}"/>
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
                                    <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints4}" tpid="${Defs.TurnpointID_ContestMapPrintPoints4}" ti="${ti}" printpoints="${true}"/>
                                    <g:contestMapPrintOptions printsize="${routeInstance.contestMapPrintSize4}" printsizename="contestMapPrintSize4" printlandscape="${routeInstance.contestMapPrintLandscape4}" printlandscapename="contestMapPrintLandscape4" ti="${ti}"/>
                                    <br/>
                                    <g:actionSubmit action="mapgenerate4" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_takeoff4" value="${message(code:'fc.generate.takeoff')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_noroute4" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails4" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_taskcreator4" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function forthoptions_click() {
                                        var show_options = $("#forthoptions_checkbox_id").prop("checked");
                                        $("#forthoptions_id").prop("hidden", !show_options);
                                        $.post("/fc/route/saveshow_ajax", {id:${routeInstance.id}, contestMapShowForthOptions:show_options}, "json");
                                    }
                                </script>
	                        </fieldset>
                        </g:if>
                        <g:else>
							<g:contestMapRoutePrintOptions r="${routeInstance}" BreakButton="${BreakButton}" FetchButton="${FetchButton}" PrintButton="${PrintButton}" PrintSize="${PrintSize}" map_upload_job_status="${map_upload_job_status}" ti="${ti}"/>
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