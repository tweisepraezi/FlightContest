<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.planningtask')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.planningtask')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${testInstance?.task.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td>${testInstance.crew.name}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:windtext var="${testInstance.planningtesttask.wind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>                                    
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
                            <br/>
                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <thead>
                                    <tr>
                                        <th colspan="7">${message(code:'fc.testlegplanning.list')}</th>
                                    </tr>
                                    <tr>
                                        <th>${message(code:'fc.number')}</th>
                                        <th>${message(code:'fc.distance')}</th>
                                        <th>${message(code:'fc.truetrack')}</th>
                                        <th>${message(code:'fc.trueheading')}</th>
                                        <th>${message(code:'fc.groundspeed')}</th>
                                        <th>${message(code:'fc.legtime')}</th>
                                        <th>${message(code:'fc.tpname')}</th>
                                    </tr>
                                    <tr>
                                        <th/>
                                        <th>[${message(code:'fc.mile')}]</th>
                                        <th>[${message(code:'fc.grad')}]</th>
                                        <th>[${message(code:'fc.grad')}]</th>
                                        <th>[${message(code:'fc.knot')}]</th>
                                        <th>[${message(code:'fc.time.minsec')}]</th>
                                        <th/>
                                    </tr>
                                </thead>
                                <tbody>
                                    <g:set var="legNo" value="${new Integer(0)}" />
                                    <g:set var="legNum" value="${TestLegPlanning.countByTest(testInstance)}" />
                                    <g:each var="testLegPlanningInstance" in="${TestLegPlanning.findAllByTest(testInstance)}">
                                        <g:set var="legNo" value="${legNo+1}" />
                                        <g:if test="${!testLegPlanningInstance.test.task.planningTestDistanceMeasure}">
                                            <g:set var="testDistance" value="${FcMath.DistanceStr(testLegPlanningInstance.planTestDistance)}" />
                                        </g:if>
                                        <g:if test="${!testLegPlanningInstance.test.task.planningTestDirectionMeasure}">
                                            <g:set var="testDirection" value="${FcMath.GradStr(testLegPlanningInstance.planTrueTrack)+message(code:'fc.grad')}" />
                                        </g:if>
                                        <tr>
                                            <g:if test="${params.results=='yes'}">
                                                <td>${legNo}</td>
                                                <td>${FcMath.DistanceStr(testLegPlanningInstance.planTestDistance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(testLegPlanningInstance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(testLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr(testLegPlanningInstance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                                <td>${testLegPlanningInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                                <g:if test="${legNo==legNum}">
                                                    <td>${CoordType.FP.title}</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${CoordType.TP.title}${legNo}</td>
                                                </g:else>
                                            </g:if>
                                            <g:else>
                                                <td>${legNo}</td>
                                                <td>${testDistance}</td>
                                                <td>${testDirection}</td>
                                                <td/>
                                                <td/>
                                                <td/>
                                                <g:if test="${legNo==legNum}">
                                                    <td>${CoordType.FP.title}</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${CoordType.TP.title}${legNo}</td>
                                                </g:else>
                                            </g:else>
                                        </tr>
                                    </g:each>
                                </tbody>
                            </table>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>