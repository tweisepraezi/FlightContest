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
                                <g:if test="${coordRouteInstance.route.IsObservationSignUsed() && coordRouteInstance.type.IsTurnpointSignCoord()}">
                                    <g:if test="${coordRouteInstance.route.turnpointRoute.IsTurnpointSign()}">
                                        <tr>
                                            <g:if test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignPhoto}">
                                                <td class="detailtitle">${message(code:'fc.observation.turnpoint.photo.short')}:</td>
                                            </g:if>
                                            <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignCanvas}">
                                                <td class="detailtitle">${message(code:'fc.observation.turnpoint.canvas.short')}:</td>
                                            </g:elseif>
                                            <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto}">
                                                <td class="detailtitle">${message(code:'fc.observation.turnpoint.photo.short')}:</td>
                                            </g:elseif>
                                            <td>${coordRouteInstance.GetTurnpointSign()}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${coordRouteInstance.route.turnpointRoute.IsTurnpointPhoto()}">
                                        <g:if test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto || coordRouteInstance.assignedSign != TurnpointSign.NoSign}">
                                            <g:if test="${coordRouteInstance.imagecoord}">
                                                <tr>
                                                    <td>
                                                        <div class="photo">
                                                            <img class="photo" id="photo_img_id" src="/fc/route/get_turnpoint_photo/${coordRouteInstance.id}" style="height:${coordRouteInstance.route.turnpointPrintStyle.height}px; width:${coordRouteInstance.route.turnpointPrintStyle.width}px;"/>
                                                            <g:if test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto}">
                                                                <div class="phototext">${coordRouteInstance.titlePrintCode()}</div>
                                                            </g:if>
                                                            <g:else>
                                                                <div class="phototext">${coordRouteInstance.assignedSign.title}</div>
                                                            </g:else>
                                                            <div class="photoposition" id="photo_position_id" style="top:${coordRouteInstance.GetObservationPositionTop()}px; left:${coordRouteInstance.GetObservationPositionLeft()}px;"></div>
                                                        </div>
                                                    </td>
                                                    <td/>
                                                </tr>
                                            </g:if>
                                            <g:if test="${coordRouteInstance.observationNextPrintPage}">
                                                <tr>
                                                    <td class="detailtitle">${message(code:'fc.observation.printnextpage.turnpoint')}:</td>
                                                    <td>${message(code:'fc.yes')}</td>
                                                </tr>
                                            </g:if>
                                        </g:if>
                                    </g:if>
                                    <g:if test="${coordRouteInstance.route.enroutePhotoRoute.IsEnrouteRouteInput()}">
                                        <g:if test="${coordRouteInstance.observationNextPrintPageEnroute}">
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.observation.printnextpage.enroute')}:</td>
                                                <td>${message(code:'fc.yes')}</td>
                                            </tr>
                                        </g:if>
                                    </g:if>
                                </g:if>
                            </tbody>
                        </table>
	                    <g:if test="${!coordRouteInstance.route.IsObservationSignUsed() && coordRouteInstance.type.IsTurnpointSignCoord()}">
                            <g:if test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignPhoto}">
                                <fieldset>
                                    <p>
                                        <label>${message(code:'fc.observation.turnpoint.assignedphoto')}*:</label>
                                        <br/>
                                        <g:select from="${TurnpointSign.GetTurnpointSigns(false)}" optionValue="${{it.title}}" value="${coordRouteInstance.assignedSign}" name="assignedSign" tabIndex="${ti[0]++}"/>
                                    </p>
                                </fieldset>
                            </g:if>
                            <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignCanvas}">
                                <fieldset>
                                    <p>
                                        <label>${message(code:'fc.observation.turnpoint.assignedcanvas')}*:</label>
                                        <br/>
                                        <g:select from="${TurnpointSign.GetTurnpointSigns(true)}" optionValue="${{it.title}}" value="${coordRouteInstance.assignedSign}" name="assignedSign" tabIndex="${ti[0]++}"/>
                                    </p>
                                </fieldset>
                            </g:elseif>
                            <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto}">
                                <fieldset>
                                    <div>
                                        <label>${message(code:'fc.observation.turnpoint.photo')}*:</label>
                                        <br/>
                                        <g:each var="v" in="${TurnpointCorrect.values()}">
                                            <g:if test="${v != TurnpointCorrect.Unassigned}">
                                                <g:if test="${coordRouteInstance.correctSign == v}">
                                                    <label><input type="radio" name="correctSign" value="${v}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:v.code)}</label>
                                                </g:if>
                                                <g:else>
                                                    <label><input type="radio" name="correctSign" value="${v}" tabIndex="${ti[0]++}"/>${message(code:v.code)}</label>
                                                </g:else>
                                            </g:if>
                                        </g:each>
                                        <br/>
                                    </div>
                                </fieldset>
                            </g:elseif>
                            <g:if test="${coordRouteInstance.route.turnpointRoute.IsTurnpointPhoto() || coordRouteInstance.route.enroutePhotoRoute.IsEnrouteRouteInput()}">
                                <g:editCoordTurnpointPhoto coordRoute="${coordRouteInstance}"ti="${ti}" next="${params.next}" />
                            </g:if>
                        </g:if>
                        <input type="hidden" name="id" value="${coordRouteInstance?.id}" />
                        <input type="hidden" name="version" value="${coordRouteInstance?.version}" />
                        <g:if test="${!coordRouteInstance.route.IsObservationSignUsed()}">
	                       	<g:if test="${params.next}">
                                <g:actionSubmit action="gotonext_object" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
	                            <g:actionSubmit action="updatenext_object" value="${message(code:'fc.savenext')}"  tabIndex="${ti[0]++}"/>
	                        </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:else>
	                        <g:actionSubmit action="updatereturn_object" value="${message(code:'fc.saveend')}"  tabIndex="${ti[0]++}"/>
                            <g:if test="${coordRouteInstance.route.turnpointRoute.IsTurnpointPhoto() && (coordRouteInstance.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto || coordRouteInstance.assignedSign != TurnpointSign.NoSign) && coordRouteInstance.type.IsTurnpointSignCoord()}">
                                <g:actionSubmit action="selectimagefilename" value="${message(code:'fc.observation.turnpoint.photo.import')}" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
	                    </g:if>
	                    <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext_object" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
	                    </g:else>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>