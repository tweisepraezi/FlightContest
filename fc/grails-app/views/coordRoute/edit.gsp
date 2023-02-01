<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.edit')}</h2>
                <g:hasErrors bean="${coordRouteInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordRouteInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${coordRouteInstance.GetNextCoordRouteID(false)}"/>
						<g:set var="prev_id" value="${coordRouteInstance.GetPrevCoordRouteID(false)}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${coordRouteInstance.titleCode()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.from')}:</td>
                                    <td><g:route var="${coordRouteInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!coordRouteInstance.route.Used()}">
                            <g:editCoordRoute coordRoute="${coordRouteInstance}" ti="${ti}" secret="${coordRouteInstance.type == CoordType.SECRET}"/>
	                        <fieldset>
	                        	<legend>${message(code:'fc.measure.fromlasttp')}</legend>
		                        <table>
		                            <tbody>
		                                <tr>
		                                    <td class="detailtitle">${message(code:'fc.truetrack.coord')}:</td>
		                                    <td>${coordRouteInstance.coordTrueTrackName()}</td>
		                                </tr>
		                                <tr>
		                                    <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
		                                    <td>${coordRouteInstance.coordMeasureDistanceName()}</td>
		                                </tr>
	                                </tbody>
	                            </table>
	                            <p>
	                                <label>${message(code:'fc.truetrack.map.measure')} [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="measureTrueTrack" name="measureTrueTrack" value="${fieldValue(bean:coordRouteInstance,field:'measureTrueTrack')}" tabIndex="${ti[0]++}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.distance.map.measure')} [${message(code:'fc.mm')}]:</label>
	                                <br/>
	                                <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:coordRouteInstance,field:'measureDistance')}" tabIndex="${ti[0]++}"/>
	                            </p>
	                        </fieldset>
	                    </g:if>
	                    <g:else>
                            <table>
                                <tbody>
                                   <tr>
                                       <td class="detailtitle">${message(code:'fc.latitude')}:</td>
                                       <td>${coordRouteInstance.latName()}</td>
                                   </tr>
                                   <tr>
                                       <td class="detailtitle">${message(code:'fc.longitude')}:</td>
                                       <td>${coordRouteInstance.lonName()}</td>
                                   </tr>
                                   <g:if test="${coordRouteInstance.legDuration}">
	                                   <tr>
	                                       <td class="detailtitle">${message(code:'fc.legduration')}:</td>
	                                       <td>${coordRouteInstance.legDuration}${message(code:'fc.time.min')}</td>
	                                   </tr>
	                               </g:if>
                                   <g:if test="${coordRouteInstance.noTimeCheck}">
	                                   <tr>
	                                       <td class="detailtitle">${message(code:'fc.notimecheck')}:</td>
	                                       <td>${message(code:'fc.yes')}</td>
	                                   </tr>
	                               </g:if>
                                   <g:if test="${coordRouteInstance.noGateCheck}">
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.nogatecheck')}:</td>
                                           <td>${message(code:'fc.yes')}</td>
                                       </tr>
                                   </g:if>
                                   <g:if test="${coordRouteInstance.noPlanningTest}">
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.noplanningtest')}:</td>
                                           <td>${message(code:'fc.yes')}</td>
                                       </tr>
                                   </g:if>
                                   <g:if test="${coordRouteInstance.endCurved}">
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.endcurved.long')}:</td>
                                           <td>${message(code:'fc.yes')}</td>
                                       </tr>
                                   </g:if>
                                   <g:if test="${coordRouteInstance.circleCenter}">
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.circlecenter')}:</td>
                                           <td>${message(code:'fc.yes')}</td>
                                       </tr>
                                   </g:if>
                                   <g:if test="${coordRouteInstance.semiCircleInvert}">
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.semicircleinvert')}:</td>
                                           <td>${message(code:'fc.yes')}</td>
                                       </tr>
                                   </g:if>
                                   <tr>
                                       <td class="detailtitle">${message(code:'fc.truetrack.coord')}:</td>
                                       <td>${coordRouteInstance.coordTrueTrackName()}</td>
                                   </tr>
                                   <g:if test="${coordRouteInstance.measureTrueTrack}">
	                                   <tr>
	                                       <td class="detailtitle">${message(code:'fc.truetrack.map.measure')}:</td>
	                                       <td>${coordRouteInstance.measureTrueTrackName()}</td>
	                                   </tr>
	                               </g:if>
                                   <tr>
                                       <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
                                       <td>${coordRouteInstance.coordMeasureDistanceName()}</td>
                                   </tr>
                                   <g:if test="${coordRouteInstance.measureDistance}">
	                                    <tr>
	                                        <td class="detailtitle">${message(code:'fc.distance.map.measure')}:</td>
	                                        <td>${coordRouteInstance.measureDistanceName()}</td>
	                                    </tr>
	                               </g:if>
                                </tbody>
                            </table>
                            <fieldset>
	                            <p>
	                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
	                                <br/>
	                                <input type="text" id="altitude" name="altitude" value="${coordRouteInstance.altitude}" tabIndex="${ti[0]++}"/>
	                            </p>
                                <g:if test="${!coordRouteInstance.type.IsRunwayCoord()}">
                                    <p>
                                        <label>${message(code:'fc.minaltitude.abovegroud')} [${message(code:'fc.foot')}]:</label>
                                        <br/>
                                        <input type="text" id="minAltitudeAboveGround" name="minAltitudeAboveGround" value="${coordRouteInstance.minAltitudeAboveGround}" tabIndex="${ti[0]++}"/>
                                    </p>
                                    <p>
                                        <label>${message(code:'fc.maxaltitude.abovegroud')} [${message(code:'fc.foot')}]:</label>
                                        <br/>
                                        <input type="text" id="maxAltitudeAboveGround" name="maxAltitudeAboveGround" value="${coordRouteInstance.maxAltitudeAboveGround}" tabIndex="${ti[0]++}"/>
                                    </p>
                                </g:if>
	                            <p>
	                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="gatewidth2" name="gatewidth2" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth2')}" tabIndex="${ti[0]++}"/>
	                            </p>
	                            <g:if test="${coordRouteInstance.type.IsRunwayCoord()}">
	                                <p>
	                                    <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
	                                    <br/>
	                                    <input type="text" id="gateDirection" name="gateDirection" value="${fieldValue(bean:coordRouteInstance,field:'gateDirection')}" tabIndex="${ti[0]++}"/>
	                                </p>
	                            </g:if>
                            </fieldset>
	                    </g:else>
                        <input type="hidden" name="id" value="${coordRouteInstance?.id}" />
                        <input type="hidden" name="version" value="${coordRouteInstance?.version}" />
						<g:if test="${next_id}">
							<g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
						<g:if test="${prev_id}">
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.gotoprev')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
						<g:if test="${next_id}">
							<g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
                        <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${!coordRouteInstance.route.Used()}">
                            <g:if test="${coordRouteInstance.IsRouteMeasure()}">
	                            <g:actionSubmit action="reset" value="${message(code:'fc.measure.fromlasttp.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
	                        </g:if>
	                        <g:if test="${!next_id || coordRouteInstance.type.IsDeleteAllowedCoord()}">
	                           <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
	                        </g:if>
	                    </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>