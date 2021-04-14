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
	                        <g:set var="geodata_airfields" value="${new File(Defs.FCSAVE_FILE_GEODATA_AIRFIELDS).exists()}"/>
	                        <g:set var="geodata_chateaus" value="${false && new File(Defs.FCSAVE_FILE_GEODATA_CHATEAUS).exists()}"/>
	                        <g:set var="geodata_peaks" value="${false && new File(Defs.FCSAVE_FILE_GEODATA_PEAKS).exists()}"/>
	                        <g:set var="geodata_additionals" value="${new File(Defs.FCSAVE_FILE_GEODATA_ADDITIONALS).exists()}"/>
	                        <g:set var="geodata_special" value="${new File(Defs.FCSAVE_FILE_GEODATA_SPECIALS).exists()}"/>
	                        <g:set var="geodata_airspace" value="${new File(Defs.FCSAVE_FILE_GEODATA_AIRSPACES).exists()}"/>
	                        <fieldset>
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
	                            <g:if test="${geodata_airfields}">
	                                <div>
                                        <g:if test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}">
                                            <g:set var="airfields_checked_icao" value="checked"/>
                                        </g:if>
                                        <g:elseif test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_OSM_NAME}">
                                            <g:set var="airfields_checked_name" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapAirfields == Defs.CONTESTMAPAIRFIELDS_GEODATA}">
                                            <g:set var="airfields_checked_geodata" value="checked"/>
                                        </g:elseif>
	                                    <label>${message(code:'fc.contestmap.contestmapairfields')}</label> <img src="${createLinkTo(dir:'images/map',file:'airfield.png')}"/>:
                                        <label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}" value="${Defs.CONTESTMAPAIRFIELDS_OSM_ICAO}" ${airfields_checked_icao} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.osmdata.icao')}</label>
                                        <label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_OSM_NAME}" value="${Defs.CONTESTMAPAIRFIELDS_OSM_NAME}" ${airfields_checked_name} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.osmdata.name')}</label>
                                        <label><input type="radio" name="contestMapAirfields" id="${Defs.CONTESTMAPAIRFIELDS_GEODATA}" value="${Defs.CONTESTMAPAIRFIELDS_GEODATA}" ${airfields_checked_geodata} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.contestmapairfields.geodata')}</label>
	                                </div>
	                            </g:if>
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
	                            <g:if test="${geodata_chateaus}">
		                            <div>
		                                <g:checkBox name="contestMapChateaus" value="${routeInstance.contestMapChateaus}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapchateaus')}</label>
		                                <img src="${createLinkTo(dir:'images/map',file:'chateau.png')}"/>
		                            </div>
	                            </g:if>
                                <div>
                                    <g:checkBox name="contestMapWindpowerstations" value="${routeInstance.contestMapWindpowerstations}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmapwindpowerstations')}</label>
                                    <img src="${createLinkTo(dir:'images/map',file:'windpowerstation.png')}"/>
                                </div>
                                <div>
                                    <g:checkBox name="contestMapSmallRoads" value="${routeInstance.contestMapSmallRoads}" tabIndex="${ti[0]++}" />
                                    <label>${message(code:'fc.contestmap.contestmapsmallroads')}</label>
                                </div>
	                            <g:if test="${geodata_peaks}">
	                                <div>
	                                    <g:checkBox name="contestMapPeaks" value="${routeInstance.contestMapPeaks}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmappeaks')}</label>
	                                    <img src="${createLinkTo(dir:'images/map',file:'peak.png')}"/>
	                                </div>
	                            </g:if>
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
	                            <g:if test="${geodata_airspace}">
	                                <div>
	                                    <g:checkBox name="contestMapAirspaces" value="${routeInstance.contestMapAirspaces}" tabIndex="${ti[0]++}" />
	                                    <label>${message(code:'fc.contestmap.contestmapairspaces')}</label>
	                                    <p>
	                                        <input type="text" id="contestMapAirspacesLayer" name="contestMapAirspacesLayer" value="${fieldValue(bean:routeInstance,field:'contestMapAirspacesLayer')}" tabIndex="${ti[0]++}"/>
	                                    </p>
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
	                        </fieldset>
	                        <fieldset>
                                <div>
                                    <g:checkBox id="secondoptions__checkbox_id" name="contestMapShowSecondOptions" value="${routeInstance.contestMapShowSecondOptions}" onclick="secondoptions_click();"/>
                                    <label>${message(code:'fc.contestmap.printsecondoptions')}</label>
                                </div>
                                <g:set var="secondoptions" value="hidden"/>
                                <g:if test="${routeInstance.contestMapShowSecondOptions}">
                                    <g:set var="secondoptions" value=""/>
                                </g:if>
                                <div id="secondoptions_id" ${secondoptions}>
                                    <br/>
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
                                </div>
                                <script>
                                    function secondoptions_click() {
                                        $("#secondoptions_id").prop("hidden", !$("#secondoptions__checkbox_id").prop("checked"));
                                    }
                                </script>
	                        </fieldset>
                            <g:actionSubmit action="mapsavesettings" value="${message(code:'fc.save')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:else>
                            <p>
                                <g:if test="${BreakButton}">
                                    ${message(code:'fc.contestmap.job.running')}
                                </g:if>
                                <g:elseif test="${FetchButton}">
                                    ${message(code:'fc.contestmap.job.continue')}
                                </g:elseif>
                                <g:elseif test="${PrintButton}">
                                    ${message(code:'fc.contestmap.job.done')}
                                </g:elseif>
                                <g:else>
                                    ${message(code:'fc.contestmap.job.otherrunning')}
                                </g:else>
                                <g:if test="${map_upload_job_status == UploadJobStatus.Waiting}"> 
                                    <img src="${createLinkTo(dir:'images',file:'email.png')}"/>
                                </g:if>
                                <g:elseif test="${map_upload_job_status == UploadJobStatus.Sending}"> 
                                    <img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>
                                </g:elseif>
                                <g:elseif test="${map_upload_job_status == UploadJobStatus.Error}"> 
                                    <img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>
                                </g:elseif>
                                <g:elseif test="${map_upload_job_status == UploadJobStatus.Done}">
                                    <a href="${routeInstance.GetMapUploadLink()}" target="_blank"><img src="${createLinkTo(dir:'images',file:'map.png')}"/></a>
                                </g:elseif>
                            </p>
                            
                            <g:if test="${PrintButton}">
                                <fieldset>
                                    <div>
                                        <g:if test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_PDFMAP}">
                                            <g:set var="contestmapprint_checked_pdfmap" value="checked"/>
                                        </g:if>
                                        <g:elseif test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_PNGMAP}">
                                            <g:set var="contestmapprint_checked_pngmap" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_PNGZIP}">
                                            <g:set var="contestmapprint_checked_pngzip" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_TIFMAP}">
                                            <g:set var="contestmapprint_checked_tifmap" value="checked"/>
                                        </g:elseif>
                                        <g:elseif test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_TILES}">
                                            <g:set var="contestmapprint_checked_tiles" value="checked"/>
                                        </g:elseif>
                                        <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PDFMAP}" value="${Defs.CONTESTMAPPRINT_PDFMAP}" ${contestmapprint_checked_pdfmap} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.pdf')}</label>
                                        <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PNGMAP}" value="${Defs.CONTESTMAPPRINT_PNGMAP}" ${contestmapprint_checked_pngmap} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.png')}</label>
                                        <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PNGZIP}" value="${Defs.CONTESTMAPPRINT_PNGZIP}" ${contestmapprint_checked_pngzip} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.pngzip')}</label>
                                        <g:if test="${BootStrap.global.IsGDALAvailable()}">
                                            <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_TIFMAP}" value="${Defs.CONTESTMAPPRINT_TIFMAP}" ${contestmapprint_checked_tifmap} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.tif')}</label>
                                            <g:if test="${BootStrap.global.IsTitlesUploadAvailable()}">
                                                <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_TILES}" value="${Defs.CONTESTMAPPRINT_TILES}" ${contestmapprint_checked_tiles} tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.tiles')}</label>
                                            </g:if>
                                        </g:if>
                                    </div>
                                </fieldset>
                                <script>
                                    $(document).on('change', '#${Defs.CONTESTMAPPRINT_PDFMAP}', function() {
                                        $("#mapsendmail_id").prop("disabled", false);
                                    });
                                    $(document).on('change', '#${Defs.CONTESTMAPPRINT_PNGMAP}', function() {
                                        $("#mapsendmail_id").prop("disabled", true);
                                    });
                                    $(document).on('change', '#${Defs.CONTESTMAPPRINT_PNGZIP}', function() {
                                        $("#mapsendmail_id").prop("disabled", true);
                                    });
                                    <g:if test="${BootStrap.global.IsGDALAvailable()}">
                                        $(document).on('change', '#${Defs.CONTESTMAPPRINT_TIFMAP}', function() {
                                            $("#mapsendmail_id").prop("disabled", true);
                                        });
                                        <g:if test="${BootStrap.global.IsTitlesUploadAvailable()}">
                                            $(document).on('change', '#${Defs.CONTESTMAPPRINT_TILES}', function() {
                                                $("#mapsendmail_id").prop("disabled", true);
                                            });
                                        </g:if>
                                    </g:if>
                                </script>
                            </g:if>
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
                                <g:actionSubmit action="mapprint" value="${message(code:'fc.contestmap.job.print')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                                <g:if test="${routeInstance.IsMapSendEMailPossible()}">
                                    <g:actionSubmit id="mapsendmail_id" action="mapsendmail" value="${message(code:'fc.route.sendmail')}" onclick="this.form.target='_self';return true;" title="${routeInstance.EMailAddress()}" tabIndex="${ti[0]++}"/>
                                    <script>
                                        if (!$("#${Defs.CONTESTMAPPRINT_PDFMAP}").prop("checked")) {
                                            $("#mapsendmail_id").prop("disabled", true);
                                        }
                                    </script>
                                </g:if>
                                <g:actionSubmit action="mapdiscard" value="${message(code:'fc.contestmap.job.finish')}" tabIndex="${ti[0]++}" />
                            </g:elseif>
                            <g:else>
                                <g:actionSubmit action="maprefresh" value="${message(code:'fc.refresh')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                            </g:else>
                        </g:else>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='';return true;" tabIndex="${ti[0]++}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>