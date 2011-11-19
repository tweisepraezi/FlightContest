<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.routecoord.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.routecoord.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routecoord.from')}:</td>
                                    <td><g:route var="${routeCoordInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
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
                                    <td class="detailtitle">${message(code:'fc.latitude')}:</td>
                                    <td>${routeCoordInstance.latName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.longitude')}:</td>
                                    <td>${routeCoordInstance.lonName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.altitude')}:</td>
                                    <td>${routeCoordInstance.altitude}${message(code:'fc.foot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.gatewidth')}:</td>
                                    <td>${routeCoordInstance.gatewidth}${message(code:'fc.mile')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${routeCoordInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>