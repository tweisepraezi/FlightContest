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
                                    <td class="detailtitle">${message(code:'fc.routecoord.from')}:</td>
                                    <td><g:route var="${routeCoordInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.routecoord.latitude')}</legend>
                            <p>
                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:routeCoordInstance,field:'latGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="latMinute" name="latMinute" value="${fieldValue(bean:routeCoordInstance,field:'latMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                                <g:select class="direction" id="latDirection" name="latDirection" from="${routeCoordInstance.constraints.latDirection.inList}" value="${routeCoordInstance.latDirection}" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.routecoord.longitude')}</legend>
                            <p>
                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:routeCoordInstance,field:'lonGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="lonMinute" name="lonMinute" value="${fieldValue(bean:routeCoordInstance,field:'lonMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${routeCoordInstance.constraints.lonDirection.inList}" value="${routeCoordInstance.lonDirection}" />
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${routeCoordInstance?.id}" />
                        <input type="hidden" name="version" value="${routeCoordInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>