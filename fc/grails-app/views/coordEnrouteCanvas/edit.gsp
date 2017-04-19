<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.canvas.edit',args:[coordEnrouteCanvasInstance.enrouteViewPos])}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.canvas.edit',args:[coordEnrouteCanvasInstance.enrouteViewPos])}</h2>
                <g:hasErrors bean="${coordEnrouteCanvasInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordEnrouteCanvasInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="fromrouteparam" value="['routeid':params.routeid,'nextid':params.next]"/>
                    <g:form method="post" params="${fromrouteparam}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.canvas.from')}:</td>
                                    <td><g:route var="${coordEnrouteCanvasInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!coordEnrouteCanvasInstance.route.IsEnrouteSignUsed(false)}">
                            <g:editCoordEnrouteCanvas coordEnroute="${coordEnrouteCanvasInstance}" create="${false}" ti="${ti}"/>
                        </g:if>
                        <g:else>
                             <table>
                                <tbody>
                                   <tr>
                                       <td class="detailtitle">${message(code:'fc.observation.enroute.canvas.sign')}:</td>
                                       <td><img src="${createLinkTo(dir:'',file:coordEnrouteCanvasInstance.enrouteCanvasSign.imageName)}" style="height:16px;"/> ${coordEnrouteCanvasInstance.enrouteCanvasSign.canvasName}</td>
                                   </tr>
                                   <g:if test="${coordEnrouteCanvasInstance.route.enrouteCanvasRoute != EnrouteRoute.InputName}" >
	                                   <tr>
	                                       <td class="detailtitle">${message(code:'fc.latitude')}:</td>
	                                       <td>${coordEnrouteCanvasInstance.latName()}</td>
	                                   </tr>
	                                   <tr>
	                                       <td class="detailtitle">${message(code:'fc.longitude')}:</td>
	                                       <td>${coordEnrouteCanvasInstance.lonName()}</td>
	                                   </tr>
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.distance.lasttp')}:</td>
                                           <td>${coordEnrouteCanvasInstance.titleCode()}</td>
                                       </tr>
	                                   <g:if test="${coordEnrouteCanvasInstance.enrouteDistance}">
	                                       <tr>
	                                           <td class="detailtitle">${message(code:'fc.distance.fromlasttp')}:</td>
	                                           <td>${FcMath.DistanceStr(coordEnrouteCanvasInstance.enrouteDistance)}${message(code:'fc.mile')}</td>
	                                       </tr>
	                                   </g:if>
	                                   <g:if test="${coordEnrouteCanvasInstance.measureDistance}">
	                                       <tr>
	                                           <td class="detailtitle">${message(code:'fc.distance.fromlasttp')}:</td>
	                                           <td>${FcMath.DistanceMeasureStr(coordEnrouteCanvasInstance.measureDistance)}${message(code:'fc.mm')}</td>
	                                       </tr>
	                                   </g:if>
                                       <g:if test="${coordEnrouteCanvasInstance.coordMeasureDistance}">
                                           <tr>
                                               <td class="detailtitle">${message(code:'fc.distance.fromlasttp.coord')}:</td>
                                               <td>${FcMath.DistanceMeasureStr(coordEnrouteCanvasInstance.coordMeasureDistance)}${message(code:'fc.mm')}</td>
                                           </tr>
                                       </g:if>
	                                   <tr>
	                                       <td class="detailtitle">${message(code:'fc.distance.orthogonal')}:</td>
	                                       <td>${coordEnrouteCanvasInstance.enrouteOrthogonalDistance}${message(code:'fc.m')}</td>
	                                   </tr>
	                               </g:if>
                                </tbody>
                            </table>
                        </g:else>
                        <input type="hidden" name="id" value="${coordEnrouteCanvasInstance?.id}" />
                        <input type="hidden" name="version" value="${coordEnrouteCanvasInstance?.version}" />
                        <g:if test="${!coordEnrouteCanvasInstance.route.IsEnrouteSignUsed(false)}">
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:else>
                            <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}"  tabIndex="${ti[0]++}"/>
                            <g:if test="${coordEnrouteCanvasInstance.IsEnrouteCanvasMeasure()}">
                                <g:actionSubmit action="reset" value="${message(code:'fc.distance.fromlasttp.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
                        </g:if>
                        <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
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