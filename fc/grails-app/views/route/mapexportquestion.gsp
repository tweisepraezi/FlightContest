<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestmap')} ${routeInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestmap')} ${routeInstance.name()}</h2>
                <div class="block" id="forms">
                    <g:form params="${['mapexportquestionReturnAction':mapexportquestionReturnAction, 'mapexportquestionReturnController':mapexportquestionReturnController, 'mapexportquestionReturnID':mapexportquestionReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
                        <g:if test="${NewOSMMap}">
	                        <table>
	                            <tbody>
	                                <tr>
	                                    <td><p class="warning">${message(code:'fc.contestmap.warning')}</p></td>
	                                    <td><a href="../docs/help.html#osm-contest-map" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></td>
	                                </tr>
	                            </tbody>
	                        </table>
	                        <fieldset>
	                            <div>
	                                <g:checkBox name="contestMapCircle" value="${routeInstance.contestMapCircle}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapcircle')}</label>
	                            </div>
	                            <g:if test="${routeInstance.UseProcedureTurn()}">
		                            <div>
		                                <g:checkBox name="contestMapProcedureTurn" value="${routeInstance.contestMapProcedureTurn}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapprocedureturn')}</label>
		                            </div>
		                        </g:if>
	                            <div>
	                                <g:checkBox name="contestMapLeg" value="${routeInstance.contestMapLeg}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapleg')}</label>
	                            </div>
	                            <div>
	                                <g:checkBox name="contestMapCurvedLeg" value="${routeInstance.contestMapCurvedLeg}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapcurvedleg')}</label>
	                            </div>
	                            <div>
	                                <g:checkBox name="contestMapTpName" value="${routeInstance.contestMapTpName}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmaptpname')}</label>
	                            </div>
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
	                        <g:set var="geodata_airfields" value="${PrintMapsOSM && new File(Defs.FCSAVE_FILE_GEODATA_AIRFIELDS).exists()}"/>
	                        <g:set var="geodata_churches" value="${PrintMapsOSM && new File(Defs.FCSAVE_FILE_GEODATA_CHURCHES).exists()}"/>
	                        <g:set var="geodata_castles" value="${PrintMapsOSM && new File(Defs.FCSAVE_FILE_GEODATA_CASTLES).exists()}"/>
	                        <g:set var="geodata_chateaus" value="${PrintMapsOSM && new File(Defs.FCSAVE_FILE_GEODATA_CHATEAUS).exists()}"/>
	                        <g:set var="geodata_windpowerstations" value="${PrintMapsOSM && new File(Defs.FCSAVE_FILE_GEODATA_WINDPOWERSTATIONS).exists()}"/>
	                        <g:set var="geodata_peaks" value="${PrintMapsOSM && new File(Defs.FCSAVE_FILE_GEODATA_PEAKS).exists()}"/>
	                        <g:set var="geodata_additionals" value="${new File(Defs.FCSAVE_FILE_GEODATA_ADDITIONALS).exists()}"/>
	                        <g:set var="geodata_special" value="${new File(Defs.FCSAVE_FILE_GEODATA_SPECIALS).exists()}"/>
	                        <g:set var="geodata_airspace" value="${new File(Defs.FCSAVE_FILE_GEODATA_AIRSPACES).exists()}"/>
	                        <fieldset>
	                            <div>
	                                <g:checkBox name="contestMapGraticule" value="${routeInstance.contestMapGraticule}" tabIndex="${ti[0]++}" />
	                                <label>${message(code:'fc.contestmap.contestmapgraticule')}</label>
	                            </div>
	                               <g:if test="${geodata_airfields}">
	                                   <div>
	                                       <g:checkBox name="contestMapAirfields" value="${routeInstance.contestMapAirfields}" tabIndex="${ti[0]++}" />
	                                       <label>${message(code:'fc.contestmap.contestmapairfields')}</label>
	                                       <img src="${createLinkTo(dir:'images/map',file:'airfield.png')}"/>
	                                   </div>
	                               </g:if>
	                            <g:if test="${geodata_churches}">
		                            <div>
		                                <g:checkBox name="contestMapChurches" value="${routeInstance.contestMapChurches}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapchurches')}</label>
		                                <img src="${createLinkTo(dir:'images/map',file:'church.png')}"/>
		                            </div>
		                        </g:if>
	                               <g:if test="${geodata_castles}">
		                            <div>
		                                <g:checkBox name="contestMapCastles" value="${routeInstance.contestMapCastles}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapcastles')}</label>
		                                <img src="${createLinkTo(dir:'images/map',file:'castle.png')}"/>
		                            </div>
		                        </g:if>
	                               <g:if test="${geodata_chateaus}">
		                            <div>
		                                <g:checkBox name="contestMapChateaus" value="${routeInstance.contestMapChateaus}" tabIndex="${ti[0]++}" />
		                                <label>${message(code:'fc.contestmap.contestmapchateaus')}</label>
		                                <img src="${createLinkTo(dir:'images/map',file:'chateau.png')}"/>
		                            </div>
	                               </g:if>
	                               <g:if test="${geodata_windpowerstations}">
	                                   <div>
	                                       <g:checkBox name="contestMapWindpowerstations" value="${routeInstance.contestMapWindpowerstations}" tabIndex="${ti[0]++}" />
	                                       <label>${message(code:'fc.contestmap.contestmapwindpowerstations')}</label>
	                                       <img src="${createLinkTo(dir:'images/map',file:'windpowerstation.png')}"/>
	                                   </div>
	                               </g:if>
	                               <g:if test="${geodata_peaks}">
	                                   <div>
	                                       <g:checkBox name="contestMapPeaks" value="${routeInstance.contestMapPeaks}" tabIndex="${ti[0]++}" />
	                                       <label>${message(code:'fc.contestmap.contestmappeaks')}</label>
	                                       <img src="${createLinkTo(dir:'images/map',file:'peak.png')}"/>
	                                   </div>
	                               </g:if>
	                               <g:if test="${PrintMapsOSM}">
	                                   <div>
	                                       <g:checkBox name="contestMapMunicipalityNames" value="${routeInstance.contestMapMunicipalityNames}" tabIndex="${ti[0]++}" />
	                                       <label>${message(code:'fc.contestmap.contestmapmunicipalitynames')}</label>
	                                   </div>
	                               </g:if>
	                               <div>
	                                   <g:checkBox name="contestMapContourLines" value="${routeInstance.contestMapContourLines}" tabIndex="${ti[0]++}" />
	                                   <label>${message(code:'fc.contestmap.contestmapcontourlines')}</label>
	                               </div>
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
	                        <fieldset>
	                            <div>
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
	                                <g:checkBox name="contestMapPrintLandscape" value="${routeInstance.contestMapPrintLandscape}" />
	                                <label>${message(code:'fc.printlandscape')}</label>
	                            </div>
	                            <div>
	                                <g:checkBox name="contestMapPrintA3" value="${routeInstance.contestMapPrintA3}" />
	                                <label>${message(code:'fc.printa3')}</label>
	                            </div>
	                            <g:if test="${PrintMapsOSM}">
	                                <div>
	                                    <g:checkBox name="contestMapColorChanges" value="${routeInstance.contestMapColorChanges}" />
	                                    <label>${message(code:'fc.contestmap.contestmapcolorchanges')}</label>
	                                </div>
	                            </g:if>
	                        </fieldset>
	                        <g:if test="${BootStrap.global.IsContestMapWriteOptions()}">
		                        <fieldset>
		                            <div>
	                                    <g:if test="${routeInstance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}">
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" value="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap')}</label>
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" value="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.showonlinemap')}</label>
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" value="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportgpxp')}</label>
	                                    </g:if>
	                                    <g:elseif test="${routeInstance.contestMapOutput == Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}">
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" value="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap')}</label>
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" value="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.showonlinemap')}</label>
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" value="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportgpxp')}</label>
	                                    </g:elseif>
	                                    <g:elseif test="${routeInstance.contestMapOutput == Defs.CONTESTMAPOUTPUT_EXPORTGPX}">
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" value="${Defs.CONTESTMAPOUTPUT_EXPORTPRINTMAP}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap')}</label>
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" value="${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.showonlinemap')}</label>
	                                        <label><input type="radio" name="contestMapOutput" id="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" value="${Defs.CONTESTMAPOUTPUT_EXPORTGPX}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportgpxp')}</label>
	                                    </g:elseif>
		                            </div>
		                        </fieldset>
		                    </g:if>
		                    <g:else>
	                            <input type="hidden" name="contestMapOutput" value="${routeInstance.contestMapOutput}" />
		                    </g:else>
                            <input type="hidden" name="id" value="${routeInstance?.id}" />
                            <g:actionSubmit action="mapgenerate" value="${message(code:'fc.generate')}" onclick="if (document.getElementById('${Defs.CONTESTMAPOUTPUT_SHOWONLINEMAP}').checked){this.form.target='_blank';}else{this.form.target='';}return true;""" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:else>
                            <g:if test="${BreakButton}">
                                <p>${message(code:'fc.contestmap.job.running')}</p>
                            </g:if>
                            <g:elseif test="${FetchButton}">
                                <p>${message(code:'fc.contestmap.job.continue')}</p>
                            </g:elseif>
                            <g:elseif test="${PrintButton}">
                                <p>${message(code:'fc.contestmap.job.done')}</p>
                            </g:elseif>
                            <g:else>
                                <p>${message(code:'fc.contestmap.job.otherrunning')}</p>
                            </g:else>
                            <g:if test="${PrintButton}">
	                            <g:if test="${BootStrap.global.IsContestMapWriteOptions()}">
	                                <fieldset>
	                                    <div>
	                                        <g:if test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_PDFMAP}">
	                                            <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PDFMAP}" value="${Defs.CONTESTMAPPRINT_PDFMAP}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.pdf')}</label>
	                                            <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PNGMAP}" value="${Defs.CONTESTMAPPRINT_PNGMAP}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.png')}</label>
	                                        </g:if>
	                                        <g:elseif test="${routeInstance.contestMapPrint == Defs.CONTESTMAPPRINT_PNGMAP}">
	                                            <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PDFMAP}" value="${Defs.CONTESTMAPPRINT_PDFMAP}" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.pdf')}</label>
	                                            <label><input type="radio" name="contestMapPrint" id="${Defs.CONTESTMAPPRINT_PNGMAP}" value="${Defs.CONTESTMAPPRINT_PNGMAP}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:'fc.contestmap.exportmap.png')}</label>
	                                        </g:elseif>
	                                    </div>
	                                </fieldset>
	                            </g:if>
	                            <g:else>
	                                <input type="hidden" name="contestMapPrint" value="${routeInstance.contestMapPrint}" />
	                            </g:else>
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
                                <g:actionSubmit action="mapdiscard" value="${message(code:'fc.contestmap.job.discard')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
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