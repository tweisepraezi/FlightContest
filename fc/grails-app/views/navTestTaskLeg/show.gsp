<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtesttaskleg.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtesttaskleg.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.from')}:</td>
                                    <td valign="top" class="value"><g:navtesttask var="${navTestTaskLegInstance?.navtesttask}" link="${createLink(controller:'navTestTask',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.distance')}:</td>
                                    <td>${navTestTaskLegInstance.distanceFormat()}${message(code:'fc.mile')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.procedureturn')}:</td>
                                    <g:if test="${navTestTaskLegInstance.procedureTurn}">
                                        <td>${message(code:'fc.required')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.notrequired')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.truetrack')}:</td>
                                    <td>${navTestTaskLegInstance.trueTrackFormat()}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.trueheading')}:</td>
                                    <td>${navTestTaskLegInstance.trueHeadingFormat()}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.groundspeed')}:</td>
                                    <td>${navTestTaskLegInstance.groundSpeedFormat()}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.legtime')}:</td>
                                    <td>${navTestTaskLegInstance.legTimeFormat()}${message(code:'fc.time.h')}</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>