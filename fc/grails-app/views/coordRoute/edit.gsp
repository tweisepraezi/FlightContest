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
                                <g:if test="${coordRouteInstance.route.showAflosMark}" >
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.aflos.checkpoint')}:</td>
	                                    <td>${coordRouteInstance.mark}</td>
	                                </tr>
	                            </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.from')}:</td>
                                    <td><g:route var="${coordRouteInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!coordRouteInstance.route.Used()}">
                            <g:editCoordRoute coordRoute="${coordRouteInstance}" ti="${ti}"/>
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
	                               <g:if test="${coordRouteInstance.route.IsObservationSignUsed() && coordRouteInstance.route.turnpointRoute.IsTurnpointSign() && coordRouteInstance.type.IsTurnpointSignCoord()}">
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
                                </tbody>
                            </table>
                            <fieldset>
	                            <p>
	                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
	                                <br/>
	                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:coordRouteInstance,field:'altitude')}" tabIndex="${ti[0]++}"/>
	                            </p>
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
	                    <g:if test="${!coordRouteInstance.route.IsObservationSignUsed()}">
                            <g:if test="${coordRouteInstance.type.IsTurnpointSignCoord()}">
                                <g:if test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignPhoto}">
                                    <fieldset>
                                        <p>
                                            <label>${message(code:'fc.observation.turnpoint.assignedphoto')}*:</label>
                                            <br/>
                                            <g:select from="${TurnpointSign.GetTurnpointSigns(false)}" optionValue="${it}" value="${coordRouteInstance.assignedSign}" name="assignedSign" tabIndex="${ti[0]++}"/>
                                        </p>
                                    </fieldset>
                                </g:if>
                                <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignCanvas}">
                                    <fieldset>
                                        <p>
                                            <label>${message(code:'fc.observation.turnpoint.assignedcanvas')}*:</label>
                                            <br/>
                                            <g:select from="${TurnpointSign.GetTurnpointSigns(true)}" optionValue="${it}" value="${coordRouteInstance.assignedSign}" name="assignedSign" tabIndex="${ti[0]++}"/>
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
                            </g:if>
                        </g:if>
                        <input type="hidden" name="id" value="${coordRouteInstance?.id}" />
                        <input type="hidden" name="version" value="${coordRouteInstance?.version}" />
                        <g:if test="${!coordRouteInstance.route.Used()}">
	                       	<g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
	                            <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}"  tabIndex="${ti[0]++}"/>
	                        </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:else>
	                        <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}"  tabIndex="${ti[0]++}"/>
                            <g:if test="${coordRouteInstance.IsRouteMeasure()}">
	                            <g:actionSubmit action="reset" value="${message(code:'fc.measure.fromlasttp.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
	                        </g:if>
	                        <g:if test="${!params.next || coordRouteInstance.type.IsDeleteAllowedCoord()}">
	                           <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
	                        </g:if>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
	                    </g:if>
	                    <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                            </g:else>
                            <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}"  tabIndex="${ti[0]++}"/>
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