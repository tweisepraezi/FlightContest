<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.testlegplanning.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.testlegplanning.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegplanning.from')}:</td>
                                    <td><g:test var="${testLegPlanningInstance?.test}" link="${createLink(controller:'test',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <tr>
                                    <td colspan="2">${message(code:'fc.testlegplanning')}</td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
                                    <td>${FcMath.DistanceStr(testLegPlanningInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.truetrack')}:</td>
                                    <td>${FcMath.GradStr(testLegPlanningInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.trueheading')}:</td>
                                    <td>${FcMath.GradStr(testLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.groundspeed')}:</td>
                                    <td>${FcMath.SpeedStr_Planning(testLegPlanningInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.legtime')}:</td>
                                    <td>${testLegPlanningInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <tr>
                                    <td colspan="2">${message(code:'fc.testlegplanningresult')}</td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegplanningresult.entered')}:</td>
                                    <g:viewbool value="${testLegPlanningInstance.resultEntered}" tag="td" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.distance.coord')}:</td>
                                    <td>${FcMath.DistanceStr(testLegPlanningInstance.resultTestDistance)}${message(code:'fc.mile')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.truetrack')}:</td>
                                    <td>${FcMath.GradStr(testLegPlanningInstance.resultTrueTrack)}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.trueheading')}:</td>
                                    <td>${FcMath.GradStr(testLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.groundspeed')}:</td>
                                    <td>${FcMath.SpeedStr_Planning(testLegPlanningInstance.resultGroundSpeed)}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.legtime')}:</td>
                                    <td>${testLegPlanningInstance.resultLegTimeStr()}${message(code:'fc.time.h')}</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>