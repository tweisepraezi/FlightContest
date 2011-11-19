<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.testlegflight.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.testlegflight.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegflight.from')}:</td>
                                    <td><g:test var="${testLegFlightInstance?.test}" link="${createLink(controller:'test',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
                                    <td>${FcMath.DistanceStr(testLegFlightInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.procedureturn')}:</td>
                                    <g:if test="${testLegFlightInstance.planProcedureTurn}">
                                        <td>${message(code:'fc.required')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.notrequired')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.truetrack')}:</td>
                                    <td>${FcMath.GradStr(testLegFlightInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.trueheading')}:</td>
                                    <td>${FcMath.GradStr(testLegFlightInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.groundspeed')}:</td>
                                    <td>${FcMath.SpeedStr(testLegFlightInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.legtime')}:</td>
                                    <td>${testLegFlightInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>