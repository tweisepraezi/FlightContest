<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.internal')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.internal')}</h2>
                <div class="block" id="forms" >
                    <g:form controller="contest">
                        <table>
                            <tbody>
                                <g:if test="${contestInstance}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.contest')}:</td>
                                        <td>
                                            <g:contest var="${contestInstance}" link="${createLink(controller:'contest',action:'show')}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.crew.list')}:</td>
                                        <td>
                                            <g:each var="crewInstance" in="${Crew.findAllByContest(contestInstance)}">
                                                <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'show')}"/>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.aircraft.list')}:</td>
                                        <td>
                                            <g:each var="aircraftInstance" in="${Aircraft.findAllByContest(contestInstance)}">
                                                <g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'show')}"/>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route.list')}:</td>
                                        <td>
                                            <g:each var="routeInstance" in="${Route.findAllByContest(contestInstance)}">
                                                <g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/>
                                                <br/>
                                                <g:each var="routeCoordInstance" in="${RouteCoord.findAllByRoute(routeInstance)}">
                                                    - <g:routecoord var="${routeCoordInstance}" link="${createLink(controller:'routeCoord',action:'show')}"/>
                                                    <br/>
                                                </g:each>
                                                <g:each var="routeLegInstance" in="${RouteLegCoord.findAllByRoute(routeInstance)}">
                                                    + <g:routelegcoord var="${routeLegInstance}" link="${createLink(controller:'routeLegCoord',action:'show')}"/>
                                                    <br/>
                                                </g:each>
                                                <g:each var="routeLegInstance" in="${RouteLegTest.findAllByRoute(routeInstance)}">
                                                    - <g:routelegtest var="${routeLegInstance}" link="${createLink(controller:'routeLegTest',action:'show')}"/>
                                                    <br/>
                                                </g:each>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.contestday.list')}:</td>
                                        <td>
                                            <g:each var="contestDayInstance" in="${ContestDay.findAllByContest(contestInstance)}">
                                                <g:contestday var="${contestDayInstance}" link="${createLink(controller:'contestDay',action:'show')}"/>
                                                <br/>
                                                <g:each var="contestDayTaskInstance" in="${ContestDayTask.findAllByContestday(contestDayInstance)}">
                                                    - <g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'show')}"/>
                                                    <br/>
                                                    <g:if test="${contestDayTaskInstance.planningtest}">
                                                        -- <g:planningtest var="${contestDayTaskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/>
                                                        <br/>
                                                        <g:each var="planningTestTaskInstance" in="${PlanningTestTask.findAllByPlanningtest(contestDayTaskInstance.planningtest)}">
                                                            +++ <g:planningtesttask var="${planningTestTaskInstance}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                                            <br/>
                                                            ++++ <g:wind var="${planningTestTaskInstance.wind}" link="${createLink(controller:'wind',action:'show')}"/>
                                                            <br/>
                                                        </g:each>
                                                    </g:if>
                                                    <g:if test="${contestDayTaskInstance.flighttest}">
                                                        -- <g:flighttest var="${contestDayTaskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                                        <br/>
                                                        <g:each var="flightTestWindInstance" in="${FlightTestWind.findAllByFlighttest(contestDayTaskInstance.flighttest)}">
                                                            +++ <g:flighttestwind var="${flightTestWindInstance}" link="${createLink(controller:'flightTestWind',action:'show')}"/>
                                                            <br/>
                                                            ++++ <g:wind var="${flightTestWindInstance.wind}" link="${createLink(controller:'wind',action:'show')}"/>
                                                            <br/>
                                                        </g:each>
                                                    </g:if>
                                                    <g:if test="${contestDayTaskInstance.landingtest}">
                                                        -- <g:landingtest var="${contestDayTaskInstance.landingtest}" link="${createLink(controller:'landingTest',action:'show')}"/>
                                                        <br/>
                                                        <g:each var="landingTestTaskInstance" in="${LandingTestTask.findAllByLandingtest(contestDayTaskInstance.landingtest)}">
                                                            +++ <g:landingtesttask var="${landingTestTaskInstance}" link="${createLink(controller:'landingTestTask',action:'show')}"/>
                                                            <br/>
                                                        </g:each>
                                                    </g:if>
                                                     <g:if test="${contestDayTaskInstance.specialtest}">
                                                        -- <g:specialtest var="${contestDayTaskInstance.specialtest}" link="${createLink(controller:'specialTest',action:'show')}"/>
                                                        <br/>
                                                        <g:each var="specialTestTaskInstance" in="${SpecialTestTask.findAllBySpecialtest(contestDayTaskInstance.specialtest)}">
                                                            +++ <g:specialtesttask var="${specialTestTaskInstance}" link="${createLink(controller:'specialTestTask',action:'show')}"/>
                                                            <br/>
                                                        </g:each>
                                                    </g:if>
                                                    <g:each var="testInstance" in="${Test.findAllByContestdaytask(contestDayTaskInstance)}">
                                                        -- <g:test var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
                                                        <br/>
                                                        <g:each var="testLegPlanningInstance" in="${TestLegPlanning.findAllByTest(testInstance)}">
                                                            +++ <g:testlegplanning var="${testLegPlanningInstance}" link="${createLink(controller:'testLegPlanning',action:'show')}"/>
                                                            <br/>
                                                        </g:each>
                                                        <g:each var="testLegFlightInstance" in="${TestLegFlight.findAllByTest(testInstance)}">
                                                            --- <g:testlegflight var="${testLegFlightInstance}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
                                                            <br/>
                                                        </g:each>
                                                    </g:each>
                                                </g:each>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                </g:if>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>