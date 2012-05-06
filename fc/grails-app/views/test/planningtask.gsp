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
                    <g:form method="post" params="${['planningtaskReturnAction':planningtaskReturnAction,'planningtaskReturnController':planningtaskReturnController,'planningtaskReturnID':planningtaskReturnID]}">
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
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
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
                                            <th colspan="6" class="table-head">${message(code:'fc.testlegplanning.list')}</th>
                                        </tr>
                                        <tr>
                                            <th>${message(code:'fc.title')}</th>
                                            <th>${message(code:'fc.distance')}</th>
                                            <th>${message(code:'fc.truetrack')}</th>
                                            <th>${message(code:'fc.trueheading')}</th>
                                            <th>${message(code:'fc.groundspeed')}</th>
                                            <th>${message(code:'fc.legtime')}</th>
                                        </tr>
                                        <tr>
                                            <th/>
                                            <th>[${message(code:'fc.mile')}]</th>
                                            <th>[${message(code:'fc.grad')}]</th>
                                            <th>[${message(code:'fc.grad')}]</th>
                                            <th>[${message(code:'fc.knot')}]</th>
                                            <th>[${message(code:'fc.time.minsec')}]</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:set var="legNo" value="${new Integer(0)}" />
                                        <g:set var="legNum" value="${TestLegPlanning.countByTest(testInstance)}" />
                                        <g:each var="testlegplanning_instance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                                            <g:set var="legNo" value="${legNo+1}" />
                                            <g:if test="${!testlegplanning_instance.test.IsPlanningTestDistanceMeasure()}">
                                                <g:set var="test_distance" value="${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}" />
                                            </g:if>
                                            <g:if test="${!testlegplanning_instance.test.IsPlanningTestDirectionMeasure()}">
                                                <g:set var="test_direction" value="${FcMath.GradStr(testlegplanning_instance.planTrueTrack)+message(code:'fc.grad')}" />
                                            </g:if>
                                            <tr>
                                                <g:if test="${legNo==legNum}">
                                                    <td>${message(code:CoordType.FP.code)}</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${message(code:CoordType.TP.code)}${legNo}</td>
                                                </g:else>
                                                <td>${test_distance}</td>
                                                <td>${test_direction}</td>
                                                <td/>
                                                <td/>
                                                <td/>
                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </div>
                        </g:if>
                        <input type="hidden" name="id" value="${testInstance?.id}"/>
                        <g:actionSubmit action="printplanningtask" value="${message(code:'fc.print')}" tabIndex="1"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="2"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>