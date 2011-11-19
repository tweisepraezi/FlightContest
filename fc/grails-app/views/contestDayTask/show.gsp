<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestdaytask.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestdaytask.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.from')}:</td>
                                    <td><g:contestday var="${contestDayTaskInstance?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${contestDayTaskInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.firsttime')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'firstTime')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.takeoffinterval.normal')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'takeoffIntervalNormal')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.takeoffinterval.fasteraircraft')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'takeoffIntervalFasterAircraft')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.navtestduration')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'navTestDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.contestdaytask.preparationduration')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'preparationDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.risingduration')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'risingDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.landingduration')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'landingDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.minnextflightduration')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'minNextFlightDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.procedureturnduration')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'procedureTurnDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.addtimevalue')}:</td>
                                    <td>${fieldValue(bean:contestDayTaskInstance, field:'addTimeValue')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtest')}:</td>
                                    <g:if test="${contestDayTaskInstance.navtest}">
                                        <td><g:navtest var="${contestDayTaskInstance.navtest}" link="${createLink(controller:'navTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <g:if test="${contestDayTaskInstance.flighttest}">
                                        <td><g:flighttest var="${contestDayTaskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest')}:</td>
                                    <g:if test="${contestDayTaskInstance.landingtest}">
                                        <td><g:landingtest var="${contestDayTaskInstance.landingtest}" link="${createLink(controller:'landingTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest')}:</td>
                                    <g:if test="${contestDayTaskInstance.specialtest}">
                                       <td><g:specialtest var="${contestDayTaskInstance.specialtest}" link="${createLink(controller:'specialTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.listcrewtests')}:</td>
                                    <td><g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'listcrewtests')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${contestDayTaskInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:if test="${!contestDayTaskInstance.navtest}">
                            <g:actionSubmit action="createnavtest" value="${message(code:'fc.navtest.add1')}" />
                        </g:if>
                        <g:if test="${!contestDayTaskInstance.flighttest}">
                            <g:actionSubmit action="createflighttest" value="${message(code:'fc.flighttest.add1')}" />
                        </g:if>
                        <g:if test="${!contestDayTaskInstance.landingtest}">
                            <g:actionSubmit action="createlandingtest" value="${message(code:'fc.landingtest.add1')}" />
                        </g:if>
                        <g:if test="${!contestDayTaskInstance.specialtest}">
                            <g:actionSubmit action="createspecialtest" value="${message(code:'fc.specialtest.add1')}" />
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>