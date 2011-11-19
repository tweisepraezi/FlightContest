<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.routeleg.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.routeleg.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routeleg.from')}:</td>
                                    <td><g:route var="${routeLegInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routeleg.truetrack')}:</td>
                                    <td>${fieldValue(bean:routeLegInstance, field:'trueTrack')}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routeleg.distance')}:</td>
                                    <td>${fieldValue(bean:routeLegInstance, field:'distance')}${message(code:'fc.mile')}</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>