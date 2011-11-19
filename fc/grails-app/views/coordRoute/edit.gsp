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
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aflos.checkpoint')}:</td>
                                    <td>${coordRouteInstance.mark}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.from')}:</td>
                                    <td><g:route var="${coordRouteInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.latitude')}*</legend>
                            <p>
                                <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}" />
                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="latMinute" name="latMinute" value="${fieldValue(bean:coordRouteInstance,field:'latMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.longitude')}*</legend>
                            <p>
                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}" />
                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="lonMinute" name="lonMinute" value="${fieldValue(bean:coordRouteInstance,field:'lonMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
                                <br/>
                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:coordRouteInstance,field:'altitude')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="gatewidth" name="gatewidth" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth')}"/>
                            </p>
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
                                <input type="text" id="measureTrueTrack" name="measureTrueTrack" value="${fieldValue(bean:coordRouteInstance,field:'measureTrueTrack')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.distance.map.measure')} [${message(code:'fc.mm')}]:</label>
                                <br/>
                                <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:coordRouteInstance,field:'measureDistance')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${coordRouteInstance?.id}" />
                        <input type="hidden" name="version" value="${coordRouteInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="reset" value="${message(code:'fc.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>