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
	                        <fieldset>
	                            <legend>${message(code:'fc.latitude')}*</legend>
                                <div>
                                    <g:if test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREE}">
                                        <input type="text" id="latGradDecimal" name="latGradDecimal" value="${coordRouteInstance.latGradDecimal.toFloat()}"  tabIndex="1"/>
                                        <label>${message(code:'fc.grad')}</label>
                                    </g:if>
                                    <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTE}">
		                                <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}"  tabIndex="2"/>
		                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}"  tabIndex="3"/>
		                                <label>${message(code:'fc.grad')}</label>
		                                <input type="text" id="latMinute" name="latMinute" value="${coordRouteInstance.latMinute.toFloat()}" tabIndex="4"/>
		                                <label>${message(code:'fc.min')}</label>
    		                        </g:elseif>
                                    <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTESECOND}">
                                        <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}"  tabIndex="5"/>
                                        <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}"  tabIndex="6"/>
                                        <label>${message(code:'fc.grad')}</label>
                                        <input class="minute" type="text" id="latMin" name="latMin" value="${fieldValue(bean:coordRouteInstance,field:'latMin')}" tabIndex="7"/>
                                        <label>${message(code:'fc.min')}</label>
                                        <input type="text" id="latSecondDecimal" name="latSecondDecimal" value="${coordRouteInstance.latSecondDecimal.toFloat()}" tabIndex="8"/>
                                        <label>${message(code:'fc.sec')}</label>
                                    </g:elseif>
                                </div>
	                        </fieldset>
	                        <fieldset>
	                            <legend>${message(code:'fc.longitude')}*</legend>
	                            <div>
                                    <g:if test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREE}">
                                        <input type="text" id="lonGradDecimal" name="lonGradDecimal" value="${coordRouteInstance.lonGradDecimal.toFloat()}"  tabIndex="11"/>
                                        <label>${message(code:'fc.grad')}</label>
                                    </g:if>
                                    <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTE}">
		                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}"  tabIndex="12"/>
		                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}"  tabIndex="13"/>
		                                <label>${message(code:'fc.grad')}</label>
		                                <input type="text" id="lonMinute" name="lonMinute" value="${coordRouteInstance.lonMinute.toFloat()}" tabIndex="14"/>
		                                <label>${message(code:'fc.min')}</label>
		                            </g:elseif>
                                    <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTESECOND}">
                                        <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}"  tabIndex="15"/>
                                        <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}"  tabIndex="16"/>
                                        <label>${message(code:'fc.grad')}</label>
                                        <input class="minute" type="text" id="lonMin" name="lonMin" value="${fieldValue(bean:coordRouteInstance,field:'lonMin')}" tabIndex="17"/>
                                        <label>${message(code:'fc.min')}</label>
                                        <input type="text" id="lonSecondDecimal" name="lonSecondDecimal" value="${coordRouteInstance.lonSecondDecimal.toFloat()}" tabIndex="18"/>
                                        <label>${message(code:'fc.sec')}</label>
                                    </g:elseif>
	                            </div>
	                        </fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
	                                <br/>
	                                <input type="text" id="altitude" name="altitude" value="${coordRouteInstance.altitude}" tabIndex="21"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="gatewidth2" name="gatewidth2" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth2')}" tabIndex="22"/>
	                            </p>
                                <g:if test="${coordRouteInstance.type.IsRunwayCoord()}">
                                    <p>
                                        <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
                                        <br/>
                                        <input type="text" id="gateDirection" name="gateDirection" value="${fieldValue(bean:coordRouteInstance,field:'gateDirection')}" tabIndex="23"/>
                                    </p>
                                </g:if>
	                            <p>
	                                <label>${message(code:'fc.legduration')} [${message(code:'fc.time.min')}]:</label>
	                                <br/>
	                                <input type="text" id="legDuration" name="legDuration" value="${fieldValue(bean:coordRouteInstance,field:'legDuration')}" tabIndex="24"/>
	                            </p>
	                            <div>
	                                <g:checkBox name="noTimeCheck" value="${coordRouteInstance.noTimeCheck}" />
	                                <label>${message(code:'fc.notimecheck')}</label>
	                            </div>
	                            <div>
	                                <g:checkBox name="noGateCheck" value="${coordRouteInstance.noGateCheck}" />
	                                <label>${message(code:'fc.nogatecheck')}</label>
	                            </div>
                                <div>
                                    <g:checkBox name="noPlanningTest" value="${coordRouteInstance.noPlanningTest}" />
                                    <label>${message(code:'fc.noplanningtest')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="endCurved" value="${coordRouteInstance.endCurved}" />
                                    <label>${message(code:'fc.endcurved.long')}</label>
                                </div>
	                        </fieldset>
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
	                                <input type="text" id="measureTrueTrack" name="measureTrueTrack" value="${fieldValue(bean:coordRouteInstance,field:'measureTrueTrack')}" tabIndex="25"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.distance.map.measure')} [${message(code:'fc.mm')}]:</label>
	                                <br/>
	                                <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:coordRouteInstance,field:'measureDistance')}" tabIndex="26"/>
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
                                </tbody>
                            </table>
                            <fieldset>
	                            <p>
	                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
	                                <br/>
	                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:coordRouteInstance,field:'altitude')}" tabIndex="27"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="gatewidth2" name="gatewidth2" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth2')}" tabIndex="28"/>
	                            </p>
	                            <g:if test="${coordRouteInstance.type.IsRunwayCoord()}">
	                                <p>
	                                    <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
	                                    <br/>
	                                    <input type="text" id="gateDirection" name="gateDirection" value="${fieldValue(bean:coordRouteInstance,field:'gateDirection')}" tabIndex="29"/>
	                                </p>
	                            </g:if>
                            </fieldset>
	                    </g:else>
                        <input type="hidden" name="id" value="${coordRouteInstance?.id}" />
                        <input type="hidden" name="version" value="${coordRouteInstance?.version}" />
                        <g:if test="${!coordRouteInstance.route.Used()}">
	                       	<g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="101"/>
	                            <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}"  tabIndex="102"/>
	                        </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="103"/>
                            </g:else>
	                        <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}"  tabIndex="104"/>
	                        <g:actionSubmit action="reset" value="${message(code:'fc.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="105"/>
	                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="106"/>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="107"/>
                            </g:if>
	                    </g:if>
	                    <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="121"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="122"/>
                            </g:else>
                            <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}"  tabIndex="123"/>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="124"/>
                            </g:if>
	                    </g:else>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>