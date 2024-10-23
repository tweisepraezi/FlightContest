<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" importaction="." />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.show')}</h2>
                <div class="block" id="forms" >
                    <g:form params="${['routeReturnAction':routeReturnAction,'routeReturnController':routeReturnController,'routeReturnID':routeReturnID]}">
                        <input type="hidden" name="id" value="${routeInstance?.id}"/>
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${routeInstance.GetNextRouteID()}"/>
						<g:set var="prev_id" value="${routeInstance.GetPrevRouteID()}"/>
                        <g:set var="route_data" value="${routeInstance.GetRouteData()}" />
                        <g:set var="route_used" value="${routeInstance.Used()}"/>
                        <g:set var="route_empty" value="${routeInstance.IsRouteEmpty()}"/>
                        <g:set var="route_canturnpointsignmodify" value="${routeInstance.CanTurnpointSignModify()}"/>
                        <g:set var="route_useprocedureturns" value="${routeInstance.useProcedureTurns}" />
                        <g:set var="route_status" value="${RouteTools.GetRouteStatus(routeInstance)}" />
                        <table>
                            <tbody>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeInstance.name()}</td>
                                    <td class="errors">${info = route_status.routeInfo}</td>
                                    <td style="width:1%;"><g:if test="${info}"><a href="/fc/docs/help_${session.showLanguage}.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td style="width:1%;"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.scale')}:</td>
                                    <td>1:${fieldValue(bean:routeInstance, field:'mapScale')}</td>
                                    <td colspan="3"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route.onlinemap.default')}:</td>
                                    <td>${fieldValue(bean:routeInstance, field:'defaultOnlineMap')}</td>
                                    <td colspan="3"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.altitude.aboveground')}:</td>
									<td>${fieldValue(bean:routeInstance, field:'altitudeAboveGround')}${message(code:'fc.foot')}</td>
									<g:set var="upload_job_status" value="${routeInstance.GetUploadJobStatus()}"/>
                                    <g:if test="${upload_job_status == UploadJobStatus.Waiting}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email.png')}"/>
                                        </td>
										<td style="width:1%;"/>
										<td style="width:1%;"/>
                                    </g:if>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Sending}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>
                                        </td>
										<td style="width:1%;"/>
										<td style="width:1%;"/>
                                    </g:elseif>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Error}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>
                                        </td>
										<td style="width:1%;"/>
										<td style="width:1%;"/>
                                    </g:elseif>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Done}">
                                        <g:set var="email_links" value="${NetTools.EMailLinks(routeInstance.GetUploadLink())}" />
										<td style="width:1%;"> 
											<a href="${email_links.map}" target="_blank"><img src="${createLinkTo(dir:'images',file:'map.png')}"/></a>
										</td>
										<td style="width:1%;"> 
											<a href="${email_links.kmz}" target="_blank"><img src="${createLinkTo(dir:'images',file:'kmz.png')}"/></a>
										</td>
                                        <td style="width:1%;"> 
                                            <a href="${email_links.pdf}" target="_blank"><img src="${createLinkTo(dir:'images',file:'pdf.png')}"/></a>
                                        </td>
                                    </g:elseif>
                                    <g:else>
										<td style="width:1%;"/>
										<td style="width:1%;"/>
										<td style="width:1%;"/>
                                    </g:else>
                                </tr>
                                <g:set var="map_links" value="${routeInstance.GetMapUploadLinks()}" />
                                <g:if test="${map_links}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.contestmap.contestmaps')}:</td>
                                        <td colspan="4" style="text-align: right;">
                                            <g:each var="map_link" in="${map_links}">
                                                <a style="margin-left: 0.8rem; font-size: 0.8rem;" href="${map_link.link}" target="_blank">${map_link.title}</a><a style="margin-left: 0.5rem;" id="deletelink_${map_link.edition}_id" hidden href="/fc/route/mapdeletelink/${routeInstance.id}?edition=${map_link.edition}" onclick="return confirm('${message(code:'fc.areyousure')}');" ><img src="${createLinkTo(dir:'images',file:'delete.png')}" style="width: 10px; heigth: 10px;" /></a></a>
                                            </g:each>
                                            <script>
                                                $(document).on('change', '#show_delete_id', function() {
                                                    if ($("#show_delete_id").prop("checked")) {
                                                        <g:each var="map_link" in="${map_links}">
                                                            $("#deletelink_${map_link.edition}_id").show();
                                                        </g:each>
                                                    } else {
                                                        <g:each var="map_link" in="${map_links}">
                                                            $("#deletelink_${map_link.edition}_id").hide();
                                                        </g:each>
                                                    }
                                                });
                                            </script>
                                            <input type="checkbox" id="show_delete_id" style="margin-left: 0.8rem;"/>
                                        </td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <td class="detailtitle">${message(code:'fc.observation.turnpoint')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.turnpointRoute.code)}<br/>${message(code:'fc.observation.measurement')}: <g:if test="${routeInstance.turnpointMapMeasurement}">${message(code:'fc.observation.turnpoint.map')}</g:if><g:else>${message(code:'fc.observation.turnpoint.log')}</g:else></td>
                                    <td class="errors">${info = route_status.turnpointSignStatusInfo}</td>
                                    <td style="width:1%;"><g:if test="${info}"><a href="/fc/docs/help_${session.showLanguage}.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td>
                                        <g:if test="${upload_job_status == UploadJobStatus.Done || map_links}">
                                            <a href="/fc/route/routelinks/${routeInstance.id}">...</a>
                                        </g:if>
                                    </td>
                                </tr>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <g:set var="info2" value=""/>
                                    <td class="detailtitle">${message(code:'fc.observation.enroute.photo')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.enroutePhotoRoute.code)}<br/>${message(code:'fc.observation.measurement')}: ${message(code:routeInstance.enroutePhotoMeasurement.code)}</td>
                                    <td class="errors">${info = route_status.enrouteSignPhotoStatusInfo}<br/>${info2 = route_status.enrouteMeasurementPhotoInfo}</td>
                                    <td style="width:1%;"><g:if test="${info || info2}"><a href="/fc/docs/help_${session.showLanguage}.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td/>
                                </tr>
                                <tr>
                                    <g:set var="info" value=""/>
                                    <g:set var="info2" value=""/>
                                    <td class="detailtitle">${message(code:'fc.observation.enroute.canvas')}:</td>
                                    <td>${message(code:'fc.observation.input')}: ${message(code:routeInstance.enrouteCanvasRoute.code)}<br/>${message(code:'fc.observation.measurement')}: ${message(code:routeInstance.enrouteCanvasMeasurement.code)}</td>
                                    <td class="errors">${info = route_status.enrouteSignCanvasStatusInfo}<br/>${info2 = route_status.enrouteMeasurementCanvasInfo}</td>
                                    <td style="width:1%;"><g:if test="${info || info2}"><a href="/fc/docs/help_${session.showLanguage}.html#route-planning-errors" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></g:if></td>
                                    <td/>
                                </tr>
                                <g:if test="${BootStrap.global.IsLiveTrackingPossible()}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.general.livetrackingscorecard')}:</td>
                                        <td>${routeInstance.liveTrackingScorecard}</td>
                                        <td colspan="3"/>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.procedureturns')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        <g:if test="${route_data.procedureturn_num}"><g:if test="${route_useprocedureturns}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.disabled')}</g:else> (${route_data.procedureturn_num})</g:if><g:else>${message(code:'fc.no')}</g:else>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.curvedlegs')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        <g:if test="${route_data.curved_num}">${message(code:'fc.yes')} (${route_data.curved_num})</g:if><g:else>${message(code:'fc.no')}</g:else>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.secretpoints')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        <g:if test="${route_data.secret_num}">${message(code:'fc.yes')} (${route_data.secret_num})</g:if><g:else>${message(code:'fc.no')}</g:else>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.to2ldg')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        ${FcMath.DistanceStr(route_data.distance_to2ldg)}${message(code:'fc.mile')} 
                                    </td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.sp2fp')}:</td>
                                    <td colspan="2" style="white-space: nowrap;">
                                        ${FcMath.DistanceStr(route_data.distance_sp2fp)}${message(code:'fc.mile')}
                                    </td>
                                    <td colspan="2"/>
                                </tr>
								
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.tasks')}:</td>
                                    <td colspan="2">
										<g:each var="task" in="${SearchTools.GetRouteTasks(routeInstance)}">
											<g:if test="${!task.hidePlanning}">
												<g:task var="${task}" link="${createLink(controller:'task',action:'listplanning')}"/>
											</g:if>
											<g:elseif test="${!task.hideResults}">
												<g:task var="${task}" link="${createLink(controller:'task',action:'listresults')}"/>
											</g:elseif>
											<g:else>
												${task.name().encodeAsHTML()}
											</g:else>
											<g:each var="flighttest_instance" in="${FlightTest.findAllByRoute(routeInstance,[sort:"id"])}">
												<g:if test="${flighttest_instance && flighttest_instance.task == task}">
													<g:if test="${flighttest_instance.IsObservationSignUsed()}">
														(${message(code:'fc.observation')}: ${message(code:'fc.yes')})
													</g:if>
													<g:else>
														(${message(code:'fc.observation')}: ${message(code:'fc.no')})
													</g:else>
												</g:if>
											</g:each>
											<br/>                                  
										</g:each>
									</td>
                                    <td colspan="2"/>
                                </tr>
								
								<%--
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="flighttest_instance" in="${FlightTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:flighttest var="${flighttest_instance}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <g:if test="${flighttest_instance.IsObservationSignUsed()}">
                                                (${message(code:'fc.observation')}: ${message(code:'fc.yes')})
                                            </g:if>
                                            <g:else>
                                                (${message(code:'fc.observation')}: ${message(code:'fc.no')})
                                            </g:else>
                                            <br/>
                                        </g:each>
                                    </td>
                                    <td colspan="2"/>
                                </tr>
								--%>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                            </tbody>
                        </table>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
                                        <g:if test="${next_id}">
                                            <g:actionSubmit action="gotonext" value="${message(code:'fc.route.gotonext')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:else>
                                            <g:actionSubmit action="gotonext" value="${message(code:'fc.route.gotonext')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
                                        </g:else>
                                        <g:if test="${prev_id}">
                                            <g:actionSubmit action="gotoprev" value="${message(code:'fc.route.gotoprev')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:else>
                                            <g:actionSubmit action="gotoprev" value="${message(code:'fc.route.gotoprev')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
                                        </g:else>
                                        <g:actionSubmit action="edit" value="${message(code:'fc.route.settings')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        <g:if test="${!route_used}">
                                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:actionSubmit action="copyroute" value="${message(code:'fc.copy')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                        <g:set var="turnpoint_photos_uploaded" value="${routeInstance.IsTurnpointPhoto() && routeInstance.AllTurnpointPhotoUploaded()}"/>
                        <g:if test="${!route_used || route_empty || route_canturnpointsignmodify || turnpoint_photos_uploaded}">
	                        <table>
	                            <tfoot>
	                                <tr>
	                                    <td>
	                                        <g:if test="${!route_used}">
	                                            <g:actionSubmit action="createcoordroutes" value="${message(code:'fc.coordroute.add1')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                            <g:actionSubmit action="createsecretcoordroutes" value="${message(code:'fc.coordroute.addsecret')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:if test="${route_empty}">
	                                            <g:actionSubmit action="importcoord" value="${message(code:'fc.coordroute.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:if test="${route_canturnpointsignmodify}">
	                                            <g:actionSubmit action="importturnpointsign" value="${message(code:'fc.coordroute.turnpointsign.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                <g:if test="${routeInstance.IsTurnpointPhoto()}">
                                                    <g:actionSubmit action="selectturnpointphotos" value="${message(code:'fc.coordroute.turnpointphoto.images.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                </g:if>
	                                        </g:if>
	                                        <g:if test="${!route_used}">
	                                            <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
                                            <g:if test="${turnpoint_photos_uploaded}">
                                                <g:if test="${routeInstance.turnpointRoute == TurnpointRoute.TrueFalsePhoto}">
                                                    <g:actionSubmit action="print_turnpointphoto_route" value="${message(code:'fc.coordroute.turnpointphoto.print')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                </g:if>
                                                <g:else>
                                                    <g:actionSubmit action="print_turnpointphoto_alphabetical" value="${message(code:'fc.coordroute.turnpointphoto.print.alphabetical')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                    <g:actionSubmit action="print_turnpointphoto_route" value="${message(code:'fc.coordroute.turnpointphoto.print.route')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                </g:else>
                                                <g:actionSubmit action="delete_turnpointphoto_route" value="${message(code:'fc.coordroute.turnpointphoto.delete')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                                <a href="/fc/docs/help_${session.showLanguage}.html#route-planning-photos-canvas" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                            </g:if>
	                                    </td>
	                                </tr>
	                            </tfoot>
	                        </table>
	                    </g:if>
                        <g:if test="${routeInstance.CanEnrouteSignModify(true) || (routeInstance.enroutePhotoRoute.IsEnrouteRouteInput() && routeInstance.AllEnroutePhotoUploaded())}">
                            <g:set var="is_any_enroue_photo" value="${routeInstance.IsAnyEnroutePhoto()}"/>
                            <table>
                                <tfoot>
                                    <tr>
                                        <td>
                                            <g:if test="${routeInstance.CanEnrouteSignModify(true)}">
                                                <g:actionSubmit action="importenroutephoto" value="${message(code:'fc.coordroute.photo.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="createenroutephoto" value="${message(code:'fc.coordroute.photo.add')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                <g:if test="${is_any_enroue_photo}">
                                                    <g:actionSubmit action="removeallenroutephoto" value="${message(code:'fc.coordroute.photo.removeall')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                                    <g:actionSubmit action="selectenroutephotos" value="${message(code:'fc.coordroute.photo.images.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                </g:if>
                                            </g:if>
                                            <g:if test="${is_any_enroue_photo}">
                                                <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInput()}">
                                                    <g:if test="${routeInstance.CanEnrouteSignModify(true)}">
                                                        <g:actionSubmit action="assignnamealphabetical_enroutephoto" value="${message(code:'fc.coordroute.photo.assignnamealphabetical')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                                        <g:actionSubmit action="assignnamerandomly_enroutephoto" value="${message(code:'fc.coordroute.photo.assignnamerandomly')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                                    </g:if>
                                                </g:if>
                                                <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInput() && routeInstance.AllEnroutePhotoUploaded()}">
                                                    <g:actionSubmit action="print_enroutephoto_alphabetical" value="${message(code:'fc.coordroute.photo.print.alphabetical')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                    <g:actionSubmit action="print_enroutephoto_route" value="${message(code:'fc.coordroute.photo.print.route')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                </g:if>
                                                <g:if test="${routeInstance.enroutePhotoRoute.IsEnrouteRouteInput() && routeInstance.AnyEnroutePhotoUploaded()}">
                                                    <g:actionSubmit action="delete_enroutephoto_route" value="${message(code:'fc.coordroute.photo.delete')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                                </g:if>
                                            </g:if>
                                            <a href="/fc/docs/help_${session.showLanguage}.html#route-planning-photos-canvas" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </g:if>
                        <g:if test="${routeInstance.CanEnrouteSignModify(false)}">
                            <table>
                                <tfoot>
                                    <tr>
                                        <td>
                                            <g:actionSubmit action="importenroutecanvas" value="${message(code:'fc.coordroute.canvas.import')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="createenroutecanvas" value="${message(code:'fc.coordroute.canvas.add')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="removeallenroutecanvas" value="${message(code:'fc.coordroute.canvas.removeall')}" onclick="this.form.target='_self';return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
                                            <a href="/fc/docs/help_${session.showLanguage}.html#route-planning-photos-canvas" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </g:if>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
                                        <g:if test="${!route_empty}">
                                            <g:actionSubmit action="showofflinemap_route" value="${message(code:'fc.offlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="showonlinemap_route" value="${message(code:'fc.onlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:if test="${BootStrap.global.GetPrintServerAPI()}">
	                                            <g:actionSubmit action="mapexportquestion2" value="${message(code:'fc.contestmap')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        </g:if>
                                            <g:actionSubmit action="kmzexport_route" value="${message(code:'fc.kmz.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:if test="${routeInstance.exportSemicircleGates}">
                                                <g:actionSubmit action="gpxexport_route" value="${message(code:'fc.gpx.export.semicirclegates')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="gpxexport_route" value="${message(code:'fc.gpx.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:else>
	                                        <g:if test="${routeInstance.IsSendEMailPossible()}">
	                                            <g:actionSubmit action="sendmail" value="${message(code:'fc.route.sendmail')}" onclick="this.form.target='_self';return true;" title="${routeInstance.EMailAddress()}" tabIndex="${ti[0]++}"/>
	                                        </g:if>
	                                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:actionSubmit action="printcoordall" value="${message(code:'fc.printcoord.all')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                        <g:actionSubmit action="printcoordtp" value="${message(code:'fc.printcoord.tp')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                    </g:if>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                        <g:showRouteDetails route="${routeInstance}" ti="${ti}"/>
                        <table>
                            <tfoot>
                                <tr>
                                    <td/>
                                    <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                                </tr>
                            </tfoot>
                        </table>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>