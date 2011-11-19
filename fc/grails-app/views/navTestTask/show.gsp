<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtesttask')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtesttask')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttask.from')}:</td>
                                    <td><g:contestday var="${navTestTaskInstance?.navtest?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${navTestTaskInstance?.navtest?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:navtest var="${navTestTaskInstance?.navtest}" link="${createLink(controller:'navTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${navTestTaskInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <td><g:route var="${navTestTaskInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
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
                            <table>
                                <thead>
                                    <tr>
                                        <th colspan="7" class="table-head">${message(code:'fc.crewtestleg.list')}</th>
                                    </tr>
                                    <tr>
                                        <th>${message(code:'fc.crewtestleg.number')}</th>
                                        <th>${message(code:'fc.crewtestleg.distance')}</th>
                                        <th>${message(code:'fc.crewtestleg.procedureturn')}</th>
                                        <th>${message(code:'fc.crewtestleg.truetrack')}</th>
                                        <th>${message(code:'fc.crewtestleg.trueheading')}</th>
                                        <th>${message(code:'fc.crewtestleg.groundspeed')}</th>
                                        <th>${message(code:'fc.crewtestleg.legtime')}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <g:set var="leg" value="${new Integer(0)}" />
                                    <g:each var="navTestTaskLegInstance" in="${NavTestTaskLeg.findAllByNavtesttask(navTestTaskInstance)}">
                                        <g:set var="leg" value="${leg+1}" />
                                        <tr>
                                            <td><g:navtesttaskleg2 var="${navTestTaskLegInstance}" name="${leg}" link="${createLink(controller:'navTestTaskLeg',action:'show')}"/></td>
                                            <td>${navTestTaskLegInstance.distanceFormat()}${message(code:'fc.mile')}</td>
                                            <g:if test="${navTestTaskLegInstance.procedureTurn}">
                                                <td>${message(code:'fc.required')}</td>
                                            </g:if> <g:else>
                                                <td/>
                                            </g:else>
                                            <td>${navTestTaskLegInstance.trueTrackFormat()}${message(code:'fc.grad')}</td>
                                            <td>${navTestTaskLegInstance.trueHeadingFormat()}${message(code:'fc.grad')}</td>
                                            <td>${navTestTaskLegInstance.groundSpeedFormat()}${message(code:'fc.knot')}</td>
                                            <td>${navTestTaskLegInstance.legTimeFormat()}${message(code:'fc.time.h')}</td>
                                        </tr>
                                    </g:each>
                                </tbody>
                            </table>
                        </g:if>
                        <input type="hidden" name="id" value="${navTestTaskInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:if test="${!CrewTest.findByNavtesttask(navTestTaskInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="print" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>