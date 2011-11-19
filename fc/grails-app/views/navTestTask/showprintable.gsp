<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtesttask')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtesttask')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${navTestTaskInstance?.navtest.contestdaytask.contestday.name()} - ${navTestTaskInstance?.navtest.contestdaytask.name()}</td>
                                </tr>
                                <tr>
                                    <td><g:navtesttext var="${navTestTaskInstance?.navtest}"/> - <g:navtesttasktext var="${navTestTaskInstance}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <tbody>
                                <g:if test="${params.crewTest}">
                                    <g:set var="crewTestInstance" value="${CrewTest.get(params.crewTest)}"/>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                        <td><g:crew var="${crewTestInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                    </tr>
                                </g:if>
                                <g:if test="${!params.crewTest}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route')}:</td>
                                        <td>${navTestTaskInstance?.route.name()}</td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:navTestTaskInstance, field:'TAS')}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <td><g:windtext var="${navTestTaskInstance?.wind}" /></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${NavTestTaskLeg.countByNavtesttask(navTestTaskInstance)}" >
                            <br/>
                            <table>
                                <thead>
                                    <tr>
                                        <th colspan="7">${message(code:'fc.crewtestleg.list')}</th>
                                    </tr>
                                    <tr>
                                        <th class="sub">${message(code:'fc.crewtestleg.number')}</th>
                                        <th class="sub">${message(code:'fc.crewtestleg.distance')}</th>
                                        <th class="sub">${message(code:'fc.crewtestleg.procedureturn')}</th>
                                        <th class="sub">${message(code:'fc.crewtestleg.truetrack')}</th>
                                        <th class="sub">${message(code:'fc.crewtestleg.trueheading')}</th>
                                        <th class="sub">${message(code:'fc.crewtestleg.groundspeed')}</th>
                                        <th class="sub">${message(code:'fc.crewtestleg.legtime')}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                       <g:set var="leg" value="${new Integer(0)}" />
                                    <g:each var="navTestTaskLegInstance" in="${NavTestTaskLeg.findAllByNavtesttask(navTestTaskInstance)}">
                                        <g:set var="leg" value="${leg+1}" />
                                        <tr>
                                            <td>${leg}</td>
                                            <td>${navTestTaskLegInstance.distanceFormat()}${message(code:'fc.mile')}</td>
                                            <g:if test="${params.crewTest}">
                                                <td/>
                                                <td/>
                                                <td/>
                                                <td/>
                                                <td/>
                                            </g:if> <g:else>
                                                <g:if test="${navTestTaskLegInstance.procedureTurn}">
                                                    <td>${message(code:'fc.required')}</td>
                                                </g:if> <g:else>
                                                    <td/>
                                                </g:else>
                                                <td>${navTestTaskLegInstance.trueTrackFormat()}${message(code:'fc.grad')}</td>
                                                <td>${navTestTaskLegInstance.trueHeadingFormat()}${message(code:'fc.grad')}</td>
                                                <td>${navTestTaskLegInstance.groundSpeedFormat()}${message(code:'fc.knot')}</td>
                                                <td>${navTestTaskLegInstance.legTimeFormat()}${message(code:'fc.time.h')}</td>
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