<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.planningtask')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.planningtask')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:route var="${testInstance.planningtesttask.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
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
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th colspan="7" class="table-head">${message(code:'fc.testlegplanning.list')}</th>
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
                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </div>
                        </g:if>
                        <input type="hidden" name="id" value="${testInstance?.id}" />
                        <g:actionSubmit action="printplanningtask" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>