<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.show')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.show')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle" />
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
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
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtest')}:</td>
                                    <g:if test="${testInstance.planningtesttask?.planningtest}">
                                        <td><g:planningtest var="${testInstance.planningtesttask.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:planningtesttask var="${testInstance.planningtesttask}" link="${createLink(controller:'planningTestTask',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegplanning.list')}:</td>
                                    <td>
                                        <g:each var="testLegPlanningInstance" in="${testInstance.testlegplannings}">
                                            <g:testlegflight var="${testLegPlanningInstance}" link="${createLink(controller:'testLegPlanning',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <g:if test="${testInstance.flighttestwind?.flighttest}">
                                        <td><g:flighttest var="${testInstance.flighttestwind.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegflight.list')}:</td>
                                    <td>
                                        <g:each var="testLegFlightInstance" in="${testInstance.testlegflights}">
                                            <g:testlegflight var="${testLegFlightInstance}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.planning')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.testingTime.format('HH:mm')} - ${testInstance.endTestingTime.format('HH:mm')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.takeoff')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <g:if test="${testInstance.takeoffTimeWarning}">
                                            <td class="errors">${testInstance.takeoffTime.format('HH:mm:ss')}${message(code:'fc.time.h')} !</td>
                                        </g:if> <g:else>
                                            <td>${testInstance.takeoffTime.format('HH:mm:ss')}</td>
                                        </g:else> 
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.startpoint')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.startTime.format('HH:mm:ss')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.finishpoint')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.finishTime.format('HH:mm:ss')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.arrival')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <g:if test="${testInstance.arrivalTimeWarning}">
                                            <td class="errors">${testInstance.arrivalTime.format('HH:mm')}${message(code:'fc.time.h')} !</td>
                                        </g:if> <g:else>
                                            <td>${testInstance.arrivalTime.format('HH:mm')}</td>
                                        </g:else>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtest')}:</td>
                                    <td>${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <td>${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest')}:</td>
                                    <td>${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest')}:</td>
                                    <td>${testInstance.specialTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>
                                    <td>${testInstance.testPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.results.position')}:</td>
                                    <g:if test="${testInstance.positionContestDay}">
                                        <td>${testInstance.positionContestDay}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.test.results.position.none')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>