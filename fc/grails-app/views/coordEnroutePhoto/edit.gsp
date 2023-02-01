<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.photo.edit',args:[coordEnroutePhotoInstance.enrouteViewPos])}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.photo.edit',args:[coordEnroutePhotoInstance.enrouteViewPos])}</h2>
                <g:hasErrors bean="${coordEnroutePhotoInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordEnroutePhotoInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="fromrouteparam" value="['routeid':params.routeid,'nextid':next_id]"/>
                    <g:form method="post" params="${fromrouteparam}" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${coordEnroutePhotoInstance.GetNextCoordEnroutePhotoID()}"/>
						<g:set var="prev_id" value="${coordEnroutePhotoInstance.GetPrevCoordEnroutePhotoID()}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.photo.from')}:</td>
                                    <td><g:route var="${coordEnroutePhotoInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!coordEnroutePhotoInstance.route.IsEnrouteSignUsed(true)}">
                            <g:editCoordEnroutePhoto coordEnroute="${coordEnroutePhotoInstance}" create="${false}" ti="${ti}" next="${next_id}" />
                        </g:if>
                        <g:else>
                            <table>
                                <tbody>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.observation.enroute.photo.name')}:</td>
                                        <td>${coordEnroutePhotoInstance.enroutePhotoName}</td>
                                    </tr>
                                    <g:if test="${coordEnroutePhotoInstance.route.enroutePhotoRoute != EnrouteRoute.InputName}" >
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.latitude')}:</td>
                                            <td>${coordEnroutePhotoInstance.latName()}</td>
                                        </tr>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.longitude')}:</td>
                                            <td>${coordEnroutePhotoInstance.lonName()}</td>
                                        </tr>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.distance.lasttp')}:</td>
                                            <td>${coordEnroutePhotoInstance.titleCode()}</td>
                                        </tr>
                                        <g:if test="${coordEnroutePhotoInstance.enrouteDistance}">
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.distance.fromlasttp')}:</td>
                                                <td>${FcMath.DistanceStr(coordEnroutePhotoInstance.enrouteDistance)}${message(code:'fc.mile')}</td>
                                            </tr>
                                        </g:if>
                                        <g:if test="${coordEnroutePhotoInstance.measureDistance}">
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.distance.fromlasttp')}:</td>
                                                <td>${FcMath.DistanceMeasureStr(coordEnroutePhotoInstance.measureDistance)}${message(code:'fc.mm')}</td>
                                            </tr>
                                        </g:if>
                                        <g:if test="${coordEnroutePhotoInstance.coordMeasureDistance}">
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.distance.fromlasttp.coord')}:</td>
                                                <td>${FcMath.DistanceMeasureStr(coordEnroutePhotoInstance.coordMeasureDistance)}${message(code:'fc.mm')}</td>
                                            </tr>
                                        </g:if>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.distance.orthogonal')}:</td>
                                            <td>${coordEnroutePhotoInstance.enrouteOrthogonalDistance}${message(code:'fc.m')}</td>
                                        </tr>
                                        <g:if test="${coordEnroutePhotoInstance.imagecoord}">
                                            <tr>
                                                <td>
                                                    <div class="photo">
                                                        <img class="photo" id="photo_img_id" src="/fc/route/get_enroute_photo/${coordEnroutePhotoInstance.id}" style="height:${coordEnroutePhotoInstance.route.enroutePhotoPrintStyle.height}px; width:${coordEnroutePhotoInstance.route.enroutePhotoPrintStyle.width}px;"/>
                                                        <div class="phototext">${coordEnroutePhotoInstance.enroutePhotoName}</div>
                                                        <div class="photoposition" id="photo_position_id" style="top:${coordEnroutePhotoInstance.GetObservationPositionTop()}px; left:${coordEnroutePhotoInstance.GetObservationPositionLeft()}px;"></div>
                                                    </div>
                                                </td>
                                                <td/>
                                            </tr>
                                        </g:if>
                                    </g:if>
                                </tbody>
                            </table>
                        </g:else>
                        <input type="hidden" name="id" value="${coordEnroutePhotoInstance?.id}" />
                        <input type="hidden" name="version" value="${coordEnroutePhotoInstance?.version}" />
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
                        <g:if test="${!coordEnroutePhotoInstance.route.IsEnrouteSignUsed(true)}">
                            <g:if test="${next_id}">
                                <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" disabled tabIndex="${ti[0]++}"/>
                            </g:else>
                            <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:if test="${coordEnroutePhotoInstance.IsEnroutePhotoMeasure()}">
                                <g:actionSubmit action="reset" value="${message(code:'fc.distance.fromlasttp.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
				            <g:actionSubmit action="selectimagefilename" value="${message(code:'fc.observation.enroute.photo.import')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>