<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.routelegcoord.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.routelegcoord.edit')}</h2>
                <g:hasErrors bean="${routeLegInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${routeLegInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
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
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeLegInstance.title}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.truetrack.coord')}:</td>
                                    <td>${routeLegInstance.coordTrueTrackName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
                                    <td>${routeLegInstance.coordDistanceName()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.distance.map.measure')} [${message(code:'fc.mm')}]:</label>
                                <br/>
                                <input type="text" id="measureDistance" name="measureDistance" value="${fieldValue(bean:routeLegInstance,field:'measureDistance')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.truetrack.map.measure')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="measureTrueTrack" name="measureTrueTrack" value="${fieldValue(bean:routeLegInstance,field:'measureTrueTrack')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${routeLegInstance?.id}" />
                        <input type="hidden" name="version" value="${routeLegInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>