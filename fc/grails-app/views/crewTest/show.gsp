<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtest.show')} ${crewTestInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtest.show')} ${crewTestInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.from')}:</td>
                                    <td><g:contestday var="${crewTestInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${crewTestInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'listcrewtests')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${crewTestInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttask')}:</td>
                                    <g:if test="${crewTestInstance.navtesttask}">
                                        <td><g:navtesttask var="${crewTestInstance.navtesttask}" link="${createLink(controller:'navTestTask',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind')}:</td>
                                    <g:if test="${crewTestInstance.flighttestwind}">
                                        <td><g:flighttestwind var="${crewTestInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <td><g:aircraft var="${crewTestInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.tas')}:</td>
                                    <td>${crewTestInstance.TAS}${message(code:'fc.knot')}</td>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.testing')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.testingTime.format('HH:mm')} - ${crewTestInstance.endTestingTime.format('HH:mm')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.takeoff')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <g:if test="${crewTestInstance.takeoffTimeWarning}">
                                            <td class="errors">${crewTestInstance.takeoffTime.format('HH:mm:ss')}${message(code:'fc.time.h')} !</td>
                                        </g:if> <g:else>
                                            <td>${crewTestInstance.takeoffTime.format('HH:mm:ss')}</td>
                                        </g:else> 
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.startpoint')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.startTime.format('HH:mm:ss')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.finishpoint')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.finishTime.format('HH:mm:ss')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.arrival')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <g:if test="${crewTestInstance.arrivalTimeWarning}">
                                            <td class="errors">${crewTestInstance.arrivalTime.format('HH:mm')}${message(code:'fc.time.h')} !</td>
                                        </g:if> <g:else>
                                            <td>${crewTestInstance.arrivalTime.format('HH:mm')}</td>
                                        </g:else>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtest')}:</td>
                                    <td>${crewTestInstance.penaltyNavTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <td>${crewTestInstance.penaltyFlightTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest')}:</td>
                                    <td>${crewTestInstance.penaltyLandingTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest')}:</td>
                                    <td>${crewTestInstance.penaltySpecialTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.results.summary')}:</td>
                                    <td>${crewTestInstance.penaltySummary} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.results.position')}:</td>
                                    <g:if test="${crewTestInstance.positionContestDay}">
                                        <td>${crewTestInstance.positionContestDay}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.crewtest.results.position.none')}</td>
                                    </g:else>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.list')}:</td>
                                    <td>
                                        <g:each var="crewTestLegInstance" in="${crewTestInstance.crewtestlegs}">
                                            <g:crewtestleg var="${crewTestLegInstance}" link="${createLink(controller:'crewTestLeg',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>