<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtestleg.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtestleg.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.from')}:</td>
                                    <td><g:crewtest var="${crewTestLegInstance?.crewtest}" link="${createLink(controller:'crewTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.distance')}:</td>
                                    <td>${crewTestLegInstance.distanceFormat()}${message(code:'fc.mile')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.procedureturn')}:</td>
                                    <g:if test="${crewTestLegInstance.procedureTurn}">
                                        <td>${message(code:'fc.required')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.notrequired')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.truetrack')}:</td>
                                    <td>${crewTestLegInstance.trueTrackFormat()}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.trueheading')}:</td>
                                    <td>${crewTestLegInstance.trueHeadingFormat()}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.groundspeed')}:</td>
                                    <td>${crewTestLegInstance.groundSpeedFormat()}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.legtime')}:</td>
                                    <td>${crewTestLegInstance.legTimeFormat()}${message(code:'fc.time.h')}</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>