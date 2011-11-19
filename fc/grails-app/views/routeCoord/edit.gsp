<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.routecoord.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.routecoord.edit')}</h2>
                <g:hasErrors bean="${routeCoordInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${routeCoordInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeCoordInstance.title()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.mark')}:</td>
                                    <td>${routeCoordInstance.mark}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routecoord.from')}:</td>
                                    <td><g:route var="${routeCoordInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.latitude')}</legend>
                            <p>
                                <g:select class="direction" id="latDirection" name="latDirection" from="${routeCoordInstance.constraints.latDirection.inList}" value="${routeCoordInstance.latDirection}" />
                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:routeCoordInstance,field:'latGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="latMinute" name="latMinute" value="${fieldValue(bean:routeCoordInstance,field:'latMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.longitude')}</legend>
                            <p>
                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${routeCoordInstance.constraints.lonDirection.inList}" value="${routeCoordInstance.lonDirection}" />
                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:routeCoordInstance,field:'lonGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="lonMinute" name="lonMinute" value="${fieldValue(bean:routeCoordInstance,field:'lonMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.altitude')} [${message(code:'fc.foot')}]:</label>
                                <br/>
                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:routeCoordInstance,field:'altitude')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.gatewidth')} [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="gatewidth" name="gatewidth" value="${fieldValue(bean:routeCoordInstance,field:'gatewidth')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${routeCoordInstance?.id}" />
                        <input type="hidden" name="version" value="${routeCoordInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>