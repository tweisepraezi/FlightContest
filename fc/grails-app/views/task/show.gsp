<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${taskInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.listplanning')}:</td>
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.listresults')}:</td>
                                    <td><g:task var="${taskInstance}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.firsttime')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'firstTime')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.takeoffinterval.normal')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'takeoffIntervalNormal')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.takeoffinterval.fasteraircraft')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'takeoffIntervalFasterAircraft')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.planningtestduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'planningTestDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.task.preparationduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'preparationDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.risingduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'risingDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.maxlandingduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'maxLandingDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.parkingduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'parkingDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.minnextflightduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'minNextFlightDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.procedureturnduration')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'procedureTurnDuration')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.addtimevalue')}:</td>
                                    <td>${fieldValue(bean:taskInstance, field:'addTimeValue')}${message(code:'fc.time.min')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.planningtestdistancemeasure')}:</td>
                                    <g:viewbool value="${taskInstance.planningTestDistanceMeasure}" tag="td" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.planningtestdirectionmeasure')}:</td>
                                    <g:viewbool value="${taskInstance.planningTestDirectionMeasure}" tag="td" />
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtest')}:</td>
                                    <g:if test="${taskInstance.planningtest}">
                                        <td><g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <g:if test="${taskInstance.flighttest}">
                                        <td><g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest')}:</td>
                                    <g:if test="${taskInstance.landingtest}">
                                        <td><g:landingtest var="${taskInstance.landingtest}" link="${createLink(controller:'landingTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest')}:</td>
                                    <g:if test="${taskInstance.specialtest}">
                                       <td><g:specialtest var="${taskInstance.specialtest}" link="${createLink(controller:'specialTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:if test="${!taskInstance.planningtest}">
                            <g:actionSubmit action="createplanningtest" value="${message(code:'fc.planningtest.add1')}" />
                        </g:if>
                        <g:if test="${!taskInstance.flighttest}">
                            <g:actionSubmit action="createflighttest" value="${message(code:'fc.flighttest.add1')}" />
                        </g:if>
                        <g:if test="${!taskInstance.landingtest}">
                            <g:actionSubmit action="createlandingtest" value="${message(code:'fc.landingtest.add1')}" />
                        </g:if>
                        <g:if test="${!taskInstance.specialtest}">
                            <g:actionSubmit action="createspecialtest" value="${message(code:'fc.specialtest.add1')}" />
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>