<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.from')}:</td>
                                    <td><g:route var="${coordRouteInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${coordRouteInstance.title()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aflos.checkpoint')}:</td>
                                    <td>${coordRouteInstance.mark}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.latitude')}:</td>
                                    <td>${coordRouteInstance.latName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.longitude')}:</td>
                                    <td>${coordRouteInstance.lonName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.altitude')}:</td>
                                    <td>${coordRouteInstance.altitude}${message(code:'fc.foot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.gatewidth')}:</td>
                                    <td>${coordRouteInstance.gatewidth}${message(code:'fc.mile')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${coordRouteInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>