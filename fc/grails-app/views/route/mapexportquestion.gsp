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
	                                    <td><a href="../../docs/help_${session.showLanguage}.html#osm-contest-map" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></td>
	                                </tr>
	                            </tbody>
	                        </table>
	                        <fieldset>
	                            <g:set var="route_data" value="${routeInstance.GetRouteData()}" />
	                            <div>
	                                <g:checkBox name="contestMapCircle" value="${routeInstance.contestMapCircle}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapcircle')}</label>
	                            </div>
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
	                        </fieldset>
	                        <g:set var="geodata_additionals" value="${new File(Defs.FCSAVE_FILE_GEODATA_ADDITIONALS).exists()}"/>
	                        <g:set var="geodata_special" value="${new File(Defs.FCSAVE_FILE_GEODATA_SPECIALS).exists()}"/>
	                        <g:set var="geodata_airspace" value="${new File(Defs.FCSAVE_FILE_GEODATA_AIRSPACES).exists()}"/>
	                        <fieldset>
								<div>
									<g:if test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_AUTO}">
                                        <g:set var="airfields_checked_auto" value="checked"/>
									</g:if>
                                    <g:elseif test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OPENAIP}">
                                        <g:if test="${BootStrap.global.IsOpenAIP()}">
                                            <g:set var="airfields_checked_openaip" value="checked"/>
                                        </g:if>
                                        <g:else>
                                            <g:set var="airfields_checked_auto" value="checked"/>
                                        </g:else>
                                    </g:elseif>
									<g:elseif test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}">
										<g:set var="airfields_checked_icao" value="checked"/>
									</g:elseif>
									<g:elseif test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OSM_NAME}">
										<g:set var="airfields_checked_name" value="checked"/>
									</g:elseif>
									<label>${message(code:'fc.contestmap.contestmapairfields')}</label>:
                                    <label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_AUTO}" value="${Defs.CONTESTMAPAIRFIELDS_AUTO}" ${airfields_checked_auto} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.auto')}</label>
                                    <g:if test="${BootStrap.global.IsOpenAIP()}">
                                        <label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_OPENAIP}" value="${Defs.CONTESTMAPAIRFIELDS_OPENAIP}" ${airfields_checked_openaip} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.openaip')}</label>
                                    </g:if>
									<label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}" value="${Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}" ${airfields_checked_icao} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.osmdata.icao')}</label>
									<label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_OSM_NAME}" value="${Defs.CONTESTMAPAIRFIELDS_OSM_NAME}" ${airfields_checked_name} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.osmdata.name')}</label>
								</div>
                                <div>
                                    <g:if test="${routeInstance.contestMapContourLines == Defs.CONTESTMAPCONTOURLINES_100M}">
                                        <g:set var="contourlines_checked_100m" value="checked"/>
                                    </g:if>
                                    <g:elseif test="${routeInstance.contestMapContourLines == Defs.CONTESTMAPCONTOURLINES_50M}">
                                        <g:set var="contourlines_checked_50m" value="checked"/>
                                    </g:elseif>
                                    <g:elseif test="${routeInstance.contestMapContourLines == Defs.CONTESTMAPCONTOURLINES_20M}">
                                        <g:set var="contourlines_checked_20m" value="checked"/>
                                    </g:elseif>
                                    <g:else>
                                        <g:set var="contourlines_checked_none" value="checked"/>
                                    </g:else>
                                    <label>${message(code:'fc.contestmap.contestmapcontourlines')}</label>:
                                    <label><input type="radio" name="contestMapContourLines" id="${Defs.CONTESTMAPCONTOURLINES_100M}" value="${Defs.CONTESTMAPCONTOURLINES_100M}" ${contourlines_checked_100m} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.100m')}</label>
                                    <label><input type="radio" name="contestMapContourLines" id="${Defs.CONTESTMAPCONTOURLINES_50M}" value="${Defs.CONTESTMAPCONTOURLINES_50M}" ${contourlines_checked_50m} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.50m')}</label>
                                    <label><input type="radio" name="contestMapContourLines" id="${Defs.CONTESTMAPCONTOURLINES_20M}" value="${Defs.CONTESTMAPCONTOURLINES_20M}" ${contourlines_checked_20m} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.20m')}</label>
                                    <label><input type="radio" name="contestMapContourLines" id="${Defs.CONTESTMAPCONTOURLINES_NONE}" value="${Defs.CONTESTMAPCONTOURLINES_NONE}" ${contourlines_checked_none} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapcontourlines.none')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestMapGraticule" value="${routeInstance.contestMapGraticule}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmapgraticule')}</label>
                                </div>
	                            <div>
	                                <g:checkBox name="contestMapChurches" value="${routeInstance.contestMapChurches}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapchurches')}</label>
	                                <img src="${createLinkTo(dir:'images/map',file:'church.png')}"/>
	                            </div>
	                            <div>
	                                <g:checkBox name="contestMapCastles" value="${routeInstance.contestMapCastles}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapcastles')}</label>
	                                <img src="${createLinkTo(dir:'images/map',file:'castle.png')}"/><img src="${createLinkTo(dir:'images/map',file:'castle_ruin.png')}"/>
	                            </div>
                                <div>
                                    <g:checkBox name="contestMapPowerlines" value="${routeInstance.contestMapPowerlines}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmappowerlines')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestMapWindpowerstations" value="${routeInstance.contestMapWindpowerstations}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmapwindpowerstations')}</label>
                                    <img src="${createLinkTo(dir:'images/map',file:'windpowerstation.png')}"/>
                                </div>
                                <div>
                                    <g:checkBox name="contestMapSmallRoads" value="${routeInstance.contestMapSmallRoads}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmapsmallroads')}</label>
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
	                            <g:if test="${geodata_additionals}">
	                                <div>
	                                    <g:checkBox name="contestMapAdditionals" value="${routeInstance.contestMapAdditionals}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapadditionals')}</label>
	                                </div>
	                            </g:if>
	                            <g:if test="${geodata_special}">
	                                <div>
	                                    <g:checkBox name="contestMapSpecials" value="${routeInstance.contestMapSpecials}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapspecial')}</label>
	                                    <img src="${createLinkTo(dir:'images/map',file:'special.png')}"/>
	                                </div>
	                            </g:if>
	                            <g:if test="${BootStrap.global.IsOpenAIP() || geodata_airspace}">
	                                <div>
	                                    <g:checkBox name="contestMapAirspaces" value="${routeInstance.contestMapAirspaces}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapairspaces')}</label>
	                                    <p>
                                            <g:textArea id="contestMapAirspacesLayer2" name="contestMapAirspacesLayer2" value="${fieldValue(bean:routeInstance,field:'contestMapAirspacesLayer2')}" rows="5" style="width:100%;" tabIndex="${ti[0]++}"/>
	                                    </p>
										<g:if test="${BootStrap.global.IsOpenAIP()}">
                                            <g:if test="${routeInstance.contestMapAirspacesLayer2}">
                                                <g:actionSubmit action="getairspaces_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairspaces.airportarea')}" onclick="return confirm('${message(code:'fc.contestmap.contestmapgetairspaces.airportarea.overwrite')}');" tabIndex="${ti[0]++}" />
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="getairspaces_airportarea_route" value="${message(code:'fc.contestmap.contestmapgetairspaces.airportarea')}" tabIndex="${ti[0]++}" />
                                            </g:else>
                                            <input type="text" name="contestMapAirspacesLowerLimit" value="${routeInstance.contestMapAirspacesLowerLimit}" min="0" size="6" tabIndex="${ti[0]++}"/> <label>${message(code:'fc.foot')}</label>
											<g:actionSubmit action="kmzexportairspaces_route" value="${message(code:'fc.contestmap.contestmapairspaces.kmzexport')}" tabIndex="${ti[0]++}" />
											<g:actionSubmit action="csvexportairports_route" value="${message(code:'fc.contestmap.contestmapairports.csvexport')}" tabIndex="${ti[0]++}" />
										</g:if>
	                                </div>
	                            </g:if>
	                        </fieldset>
	                        <g:if test="${BootStrap.global.IsContestMapDevOptions()}">
		                        <fieldset>
		                            <div>
	                                    <g:if test="${routeInstance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}">
                                            <g:set var="contestmap_output_exportprintmap" value="checked"/>
	                                    </g:if>
	                                    <g:elseif test="${routeInstance.contestMapOutput == Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}">
                                            <g:set var="contestmap_output_showonlinemap" value="checked"/>
	                                    </g:elseif>
	                                    <g:elseif test="${routeInstance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTGPX}">
                                            <g:set var="contestmap_output_exportgpx" value="checked"/>
	                                    </g:elseif>
                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" value="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" ${contestmap_output_exportprintmap} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap')}</label>
                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" value="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" ${contestmap_output_showonlinemap} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.showonlinemap')}</label>
                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" value="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" ${contestmap_output_exportgpx} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportgpxp')}</label>
		                            </div>
                                    <div>
                                        <g:checkBox name="contestMapDevStyle" value="${routeInstance.contestMapDevStyle}" tabIndex="${ti[0]++}" />
                                        <label>${message(code:'fc.contestmap.devstyle')}</label>
                                    </div>
                                </fieldset>
		                    </g:if>
		                    <g:else>
	                            <input type="hidden" name="contestMapOutput" value="${routeInstance.contestMapOutput}" />
		                    </g:else>
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
									<div>
										<label>${message(code:'fc.contestmap.contestmapcenterpos')}:</label>
										<br/>
										<g:each var="verticalpos_instance" in="${VerticalPos.GetValues()}">
											<g:if test="${routeInstance.contestMapCenterVerticalPos == verticalpos_instance}">
												<label><input type="radio" name="contestMapCenterVerticalPos" value="${verticalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
											</g:if>
											<g:else>
												<label><input type="radio" name="contestMapCenterVerticalPos" value="${verticalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
											</g:else>
										</g:each>
									</div>
									<div>
										<g:each var="horizontalpos_instance" in="${HorizontalPos.GetValues()}">
											<g:if test="${routeInstance.contestMapCenterHorizontalPos == horizontalpos_instance}">
												<label><input type="radio" name="contestMapCenterHorizontalPos" value="${horizontalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
											</g:if>
											<g:else>
												<label><input type="radio" name="contestMapCenterHorizontalPos" value="${horizontalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
											</g:else>
										</g:each>
									</div>
									<div>
										<br/>
										<label>${message(code:'fc.contestmap.contestmapcenterpoints')}:</label>
										<br/>
										<g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints}" tpid="${Defs.TurnpointID_ContestMapCenterPoints}" ti="${ti}" />
									</div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveX" name="contestMapCenterMoveX" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveX')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveY" name="contestMapCenterMoveY" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveY')}" tabIndex="${ti[0]++}"/>
                                    </div>
									<div>
										<br/>
										<label>${message(code:'fc.contestmap.contestmapprintpoints')}:</label>
										<br/>
										<g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints}" tpid="${Defs.TurnpointID_ContestMapPrintPoints}" ti="${ti}" />
									</div>
									<br/>
									<div>
										<g:if test="${routeInstance.contestMapPrintSize == Defs.CONTESTMAPPRINTSIZE_A4}">
											<g:set var="printsize_checked_a4" value="checked"/>
										</g:if>
										<g:elseif test="${routeInstance.contestMapPrintSize == Defs.CONTESTMAPPRINTSIZE_A3}">
											<g:set var="printsize_checked_a3" value="checked"/>
										</g:elseif>
										<g:elseif test="${routeInstance.contestMapPrintSize == Defs.CONTESTMAPPRINTSIZE_A2}">
											<g:set var="printsize_checked_a2" value="checked"/>
										</g:elseif>
										<g:elseif test="${routeInstance.contestMapPrintSize == Defs.CONTESTMAPPRINTSIZE_A1}">
											<g:set var="printsize_checked_a1" value="checked"/>
										</g:elseif>
										<g:elseif test="${routeInstance.contestMapPrintSize == Defs.CONTESTMAPPRINTSIZE_ANR}">
											<g:set var="printsize_checked_anr" value="checked"/>
										</g:elseif>
										<label>${message(code:'fc.contestmap.printsize')}</label>:
										<label><input type="radio" name="contestMapPrintSize" id="${Defs.CONTESTMAPPRINTSIZE_A4}" value="${Defs.CONTESTMAPPRINTSIZE_A4}" ${printsize_checked_a4} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a4')}</label>
										<label><input type="radio" name="contestMapPrintSize" id="${Defs.CONTESTMAPPRINTSIZE_A3}" value="${Defs.CONTESTMAPPRINTSIZE_A3}" ${printsize_checked_a3} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a3')}</label>
										<label><input type="radio" name="contestMapPrintSize" id="${Defs.CONTESTMAPPRINTSIZE_A2}" value="${Defs.CONTESTMAPPRINTSIZE_A2}" ${printsize_checked_a2} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a2')}</label>
										<label><input type="radio" name="contestMapPrintSize" id="${Defs.CONTESTMAPPRINTSIZE_A1}" value="${Defs.CONTESTMAPPRINTSIZE_A1}" ${printsize_checked_a1} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a1')}</label>
										<label><input type="radio" name="contestMapPrintSize" id="${Defs.CONTESTMAPPRINTSIZE_ANR}" value="${Defs.CONTESTMAPPRINTSIZE_ANR}" ${printsize_checked_anr} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.anr')}</label>
									</div>
									<div>
										<g:checkBox name="contestMapPrintLandscape" value="${routeInstance.contestMapPrintLandscape}" />
										<label>${message(code:'fc.printlandscape')}</label>
									</div>
									<br/>
									<input type="hidden" name="id" value="${routeInstance?.id}" />
									<g:actionSubmit action="mapgenerate" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_noroute" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_taskcreator" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function firstoptions_click() {
                                        $("#firstoptions_id").prop("hidden", !$("#firstoptions_checkbox_id").prop("checked"));
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
                                    <div>
                                        <label>${message(code:'fc.contestmap.contestmapcenterpos')}:</label>
                                        <br/>
                                        <g:each var="verticalpos_instance" in="${VerticalPos.GetValues()}">
                                            <g:if test="${routeInstance.contestMapCenterVerticalPos2 == verticalpos_instance}">
                                                <label><input type="radio" name="contestMapCenterVerticalPos2" value="${verticalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
                                            </g:if>
                                            <g:else>
                                                <label><input type="radio" name="contestMapCenterVerticalPos2" value="${verticalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
                                            </g:else>
                                        </g:each>
                                    </div>
                                    <div>
                                        <g:each var="horizontalpos_instance" in="${HorizontalPos.GetValues()}">
                                            <g:if test="${routeInstance.contestMapCenterHorizontalPos2 == horizontalpos_instance}">
                                                <label><input type="radio" name="contestMapCenterHorizontalPos2" value="${horizontalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
                                            </g:if>
                                            <g:else>
                                                <label><input type="radio" name="contestMapCenterHorizontalPos2" value="${horizontalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
                                            </g:else>
                                        </g:each>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcenterpoints')}:</label>
                                        <br/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints2}" tpid="${Defs.TurnpointID_ContestMapCenterPoints2}" ti="${ti}" />
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveX2" name="contestMapCenterMoveX2" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveX2')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveY2" name="contestMapCenterMoveY2" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveY2')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapprintpoints')}:</label>
                                        <br/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints2}" tpid="${Defs.TurnpointID_ContestMapPrintPoints2}" ti="${ti}" />
                                    </div>
                                    <br/>
                                    <div>
                                        <g:if test="${routeInstance.contestMapPrintSize2 == Defs.CONTESTMAPPRINTSIZE_A4}">
                                            <g:set var="printsize_checked_a4_2" value="checked"/>
                                        </g:if>
                                        <g:elseif test="${routeInstance.contestMapPrintSize2 == Defs.CONTESTMAPPRINTSIZE_A3}">
                                            <g:set var="printsize_checked_a3_2" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize2 == Defs.CONTESTMAPPRINTSIZE_A2}">
                                            <g:set var="printsize_checked_a2_2" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize2 == Defs.CONTESTMAPPRINTSIZE_A1}">
                                            <g:set var="printsize_checked_a1_2" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize2 == Defs.CONTESTMAPPRINTSIZE_ANR}">
                                            <g:set var="printsize_checked_anr_2" value="checked"/>
                                        </g:elseif>
                                        <label>${message(code:'fc.contestmap.printsize')}</label>:
                                        <label><input type="radio" name="contestMapPrintSize2" id="${Defs.CONTESTMAPPRINTSIZE_A4}" value="${Defs.CONTESTMAPPRINTSIZE_A4}" ${printsize_checked_a4_2} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a4')}</label>
                                        <label><input type="radio" name="contestMapPrintSize2" id="${Defs.CONTESTMAPPRINTSIZE_A3}" value="${Defs.CONTESTMAPPRINTSIZE_A3}" ${printsize_checked_a3_2} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a3')}</label>
                                        <label><input type="radio" name="contestMapPrintSize2" id="${Defs.CONTESTMAPPRINTSIZE_A2}" value="${Defs.CONTESTMAPPRINTSIZE_A2}" ${printsize_checked_a2_2} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a2')}</label>
                                        <label><input type="radio" name="contestMapPrintSize2" id="${Defs.CONTESTMAPPRINTSIZE_A1}" value="${Defs.CONTESTMAPPRINTSIZE_A1}" ${printsize_checked_a1_2} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a1')}</label>
                                        <label><input type="radio" name="contestMapPrintSize2" id="${Defs.CONTESTMAPPRINTSIZE_ANR}" value="${Defs.CONTESTMAPPRINTSIZE_ANR}" ${printsize_checked_anr_2} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.anr')}</label>
                                    </div>
                                    <div>
                                        <g:checkBox name="contestMapPrintLandscape2" value="${routeInstance.contestMapPrintLandscape2}" />
                                        <label>${message(code:'fc.printlandscape')}</label>
                                    </div>
                                    <br/>
                                    <g:actionSubmit action="mapgenerate2" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_noroute2" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails2" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_taskcreator2" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function secondoptions_click() {
                                        $("#secondoptions_id").prop("hidden", !$("#secondoptions_checkbox_id").prop("checked"));
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
                                    <div>
                                        <label>${message(code:'fc.contestmap.contestmapcenterpos')}:</label>
                                        <br/>
                                        <g:each var="verticalpos_instance" in="${VerticalPos.GetValues()}">
                                            <g:if test="${routeInstance.contestMapCenterVerticalPos3 == verticalpos_instance}">
                                                <label><input type="radio" name="contestMapCenterVerticalPos3" value="${verticalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
                                            </g:if>
                                            <g:else>
                                                <label><input type="radio" name="contestMapCenterVerticalPos3" value="${verticalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
                                            </g:else>
                                        </g:each>
                                    </div>
                                    <div>
                                        <g:each var="horizontalpos_instance" in="${HorizontalPos.GetValues()}">
                                            <g:if test="${routeInstance.contestMapCenterHorizontalPos3 == horizontalpos_instance}">
                                                <label><input type="radio" name="contestMapCenterHorizontalPos3" value="${horizontalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
                                            </g:if>
                                            <g:else>
                                                <label><input type="radio" name="contestMapCenterHorizontalPos3" value="${horizontalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
                                            </g:else>
                                        </g:each>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcenterpoints')}:</label>
                                        <br/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints3}" tpid="${Defs.TurnpointID_ContestMapCenterPoints3}" ti="${ti}" />
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveX3" name="contestMapCenterMoveX3" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveX3')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveY3" name="contestMapCenterMoveY3" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveY3')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapprintpoints')}:</label>
                                        <br/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints3}" tpid="${Defs.TurnpointID_ContestMapPrintPoints3}" ti="${ti}" />
                                    </div>
                                    <br/>
                                    <div>
                                        <g:if test="${routeInstance.contestMapPrintSize3 == Defs.CONTESTMAPPRINTSIZE_A4}">
                                            <g:set var="printsize_checked_a4_3" value="checked"/>
                                        </g:if>
                                        <g:elseif test="${routeInstance.contestMapPrintSize3 == Defs.CONTESTMAPPRINTSIZE_A3}">
                                            <g:set var="printsize_checked_a3_3" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize3 == Defs.CONTESTMAPPRINTSIZE_A2}">
                                            <g:set var="printsize_checked_a2_3" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize3 == Defs.CONTESTMAPPRINTSIZE_A1}">
                                            <g:set var="printsize_checked_a1_3" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize3 == Defs.CONTESTMAPPRINTSIZE_ANR}">
                                            <g:set var="printsize_checked_anr_3" value="checked"/>
                                        </g:elseif>
                                        <label>${message(code:'fc.contestmap.printsize')}</label>:
                                        <label><input type="radio" name="contestMapPrintSize3" id="${Defs.CONTESTMAPPRINTSIZE_A4}" value="${Defs.CONTESTMAPPRINTSIZE_A4}" ${printsize_checked_a4_3} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a4')}</label>
                                        <label><input type="radio" name="contestMapPrintSize3" id="${Defs.CONTESTMAPPRINTSIZE_A3}" value="${Defs.CONTESTMAPPRINTSIZE_A3}" ${printsize_checked_a3_3} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a3')}</label>
                                        <label><input type="radio" name="contestMapPrintSize3" id="${Defs.CONTESTMAPPRINTSIZE_A2}" value="${Defs.CONTESTMAPPRINTSIZE_A2}" ${printsize_checked_a2_3} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a2')}</label>
                                        <label><input type="radio" name="contestMapPrintSize3" id="${Defs.CONTESTMAPPRINTSIZE_A1}" value="${Defs.CONTESTMAPPRINTSIZE_A1}" ${printsize_checked_a1_3} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a1')}</label>
                                        <label><input type="radio" name="contestMapPrintSize3" id="${Defs.CONTESTMAPPRINTSIZE_ANR}" value="${Defs.CONTESTMAPPRINTSIZE_ANR}" ${printsize_checked_anr_3} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.anr')}</label>
                                    </div>
                                    <div>
                                        <g:checkBox name="contestMapPrintLandscape3" value="${routeInstance.contestMapPrintLandscape3}" />
                                        <label>${message(code:'fc.printlandscape')}</label>
                                    </div>
                                    <br/>
                                    <g:actionSubmit action="mapgenerate3" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_noroute3" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails3" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_taskcreator3" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function thirdoptions_click() {
                                        $("#thirdoptions_id").prop("hidden", !$("#thirdoptions_checkbox_id").prop("checked"));
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
                                    <div>
                                        <label>${message(code:'fc.contestmap.contestmapcenterpos')}:</label>
                                        <br/>
                                        <g:each var="verticalpos_instance" in="${VerticalPos.GetValues()}">
                                            <g:if test="${routeInstance.contestMapCenterVerticalPos4 == verticalpos_instance}">
                                                <label><input type="radio" name="contestMapCenterVerticalPos4" value="${verticalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
                                            </g:if>
                                            <g:else>
                                                <label><input type="radio" name="contestMapCenterVerticalPos4" value="${verticalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:verticalpos_instance.code)}</label>
                                            </g:else>
                                        </g:each>
                                    </div>
                                    <div>
                                        <g:each var="horizontalpos_instance" in="${HorizontalPos.GetValues()}">
                                            <g:if test="${routeInstance.contestMapCenterHorizontalPos4 == horizontalpos_instance}">
                                                <label><input type="radio" name="contestMapCenterHorizontalPos4" value="${horizontalpos_instance}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
                                            </g:if>
                                            <g:else>
                                                <label><input type="radio" name="contestMapCenterHorizontalPos4" value="${horizontalpos_instance}" tabIndex="${ti[0]++}"/>${message(code:horizontalpos_instance.code)}</label>
                                            </g:else>
                                        </g:each>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcenterpoints')}:</label>
                                        <br/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapCenterPoints4}" tpid="${Defs.TurnpointID_ContestMapCenterPoints4}" ti="${ti}" />
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovex')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveX4" name="contestMapCenterMoveX4" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveX4')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapcentermovey')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="contestMapCenterMoveY4" name="contestMapCenterMoveY4" value="${fieldValue(bean:routeInstance,field:'contestMapCenterMoveY4')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                    <div>
                                        <br/>
                                        <label>${message(code:'fc.contestmap.contestmapprintpoints')}:</label>
                                        <br/>
                                        <g:contestMapRoutePointsInput r="${routeInstance}" tp="${routeInstance.contestMapPrintPoints4}" tpid="${Defs.TurnpointID_ContestMapPrintPoints4}" ti="${ti}" />
                                    </div>
                                    <br/>
                                    <div>
                                        <g:if test="${routeInstance.contestMapPrintSize4 == Defs.CONTESTMAPPRINTSIZE_A4}">
                                            <g:set var="printsize_checked_a4_4" value="checked"/>
                                        </g:if>
                                        <g:elseif test="${routeInstance.contestMapPrintSize4 == Defs.CONTESTMAPPRINTSIZE_A3}">
                                            <g:set var="printsize_checked_a3_4" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize4 == Defs.CONTESTMAPPRINTSIZE_A2}">
                                            <g:set var="printsize_checked_a2_4" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize4 == Defs.CONTESTMAPPRINTSIZE_A1}">
                                            <g:set var="printsize_checked_a1_4" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrintSize4 == Defs.CONTESTMAPPRINTSIZE_ANR}">
                                            <g:set var="printsize_checked_anr_4" value="checked"/>
                                        </g:elseif>
                                        <label>${message(code:'fc.contestmap.printsize')}</label>:
                                        <label><input type="radio" name="contestMapPrintSize4" id="${Defs.CONTESTMAPPRINTSIZE_A4}" value="${Defs.CONTESTMAPPRINTSIZE_A4}" ${printsize_checked_a4_4} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a4')}</label>
                                        <label><input type="radio" name="contestMapPrintSize4" id="${Defs.CONTESTMAPPRINTSIZE_A3}" value="${Defs.CONTESTMAPPRINTSIZE_A3}" ${printsize_checked_a3_4} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a3')}</label>
                                        <label><input type="radio" name="contestMapPrintSize4" id="${Defs.CONTESTMAPPRINTSIZE_A2}" value="${Defs.CONTESTMAPPRINTSIZE_A2}" ${printsize_checked_a2_4} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a2')}</label>
                                        <label><input type="radio" name="contestMapPrintSize4" id="${Defs.CONTESTMAPPRINTSIZE_A1}" value="${Defs.CONTESTMAPPRINTSIZE_A1}" ${printsize_checked_a1_4} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.a1')}</label>
                                        <label><input type="radio" name="contestMapPrintSize4" id="${Defs.CONTESTMAPPRINTSIZE_ANR}" value="${Defs.CONTESTMAPPRINTSIZE_ANR}" ${printsize_checked_anr_4} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.printsize.anr')}</label>
                                    </div>
                                    <div>
                                        <g:checkBox name="contestMapPrintLandscape4" value="${routeInstance.contestMapPrintLandscape4}" />
                                        <label>${message(code:'fc.printlandscape')}</label>
                                    </div>
                                    <br/>
                                    <g:actionSubmit action="mapgenerate4" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_noroute4" value="${message(code:'fc.generate.noroute')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
									<g:actionSubmit action="mapgenerate_allroutedetails4" value="${message(code:'fc.generate.allroutedetails')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                    <g:actionSubmit action="mapgenerate_taskcreator4" value="${message(code:'fc.generate.taskcreator')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                                </div>
                                <script>
                                    function forthoptions_click() {
                                        $("#forthoptions_id").prop("hidden", !$("#forthoptions_checkbox_id").prop("checked"));
                                    }
                                </script>
	                        </fieldset>
                        </g:if>
                        <g:else>
							<g:contestMapRoutePrintOptions r="${routeInstance}" BreakButton="${BreakButton}" FetchButton="${FetchButton}" PrintButton="${PrintButton}" map_upload_job_status="${map_upload_job_status}" ti="${ti}"/>
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