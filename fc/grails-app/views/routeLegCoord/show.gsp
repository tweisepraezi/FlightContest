<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.routelegcoord.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.routelegcoord.show')}</h2>
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
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeLegInstance.title}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.truetrack')}:</td>
                                    <td>${routeLegInstance.coordTrueTrackName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
                                    <td>${routeLegInstance.coordDistanceName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.map.measure')}:</td>
                                    <td>${routeLegInstance.mapMeasureDistanceName()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.map')}:</td>
                                    <td>${routeLegInstance.mapDistanceName()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${routeLegInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>