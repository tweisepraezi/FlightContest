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
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contest')}:</td>
                                    <td>
                                        <g:each var="contestInstance" in="${Contest.list()}">
                                            <g:contest var="${contestInstance}" link="${createLink(controller:'contest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew.list')}:</td>
                                    <td>
                                        <g:each var="crewInstance" in="${Crew.list()}">
                                            <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.list')}:</td>
                                    <td>
                                        <g:each var="aircraftInstance" in="${Aircraft.list()}">
                                            <g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route.list')}:</td>
                                    <td>
                                        <g:each var="routeInstance" in="${Route.list()}">
                                            <g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.list')}:</td>
                                    <td>
                                        <g:each var="coordRouteInstance" in="${CoordRoute.list()}">
                                            <g:coordroute var="${coordRouteInstance}" link="${createLink(controller:'coordRoute',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routelegcoord.list')}:</td>
                                    <td>
                                        <g:each var="routeLegInstance" in="${RouteLegCoord.list()}">
                                            <g:routelegcoord var="${routeLegInstance}" link="${createLink(controller:'routeLegCoord',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routelegtest.list')}:</td>
                                    <td>
                                        <g:each var="routeLegInstance" in="${RouteLegTest.list()}">
                                            <g:routelegtest var="${routeLegInstance}" link="${createLink(controller:'routeLegTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.task.list')}:</td>
                                    <td>
                                        <g:each var="taskInstance" in="${Task.list()}">
                                            <g:task var="${taskInstance}" link="${createLink(controller:'task',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtest.list')}:</td>
                                    <td>
                                        <g:each var="planningTestInstance" in="${PlanningTest.list()}">
                                            <g:planningtest var="${planningTestInstance}" link="${createLink(controller:'planningTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask.list')}:</td>
                                    <td>
                                        <g:each var="planningTestTaskInstance" in="${PlanningTestTask.list()}">
                                            <g:planningtesttask var="${planningTestTaskInstance}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td>
                                        <g:each var="flightTestInstance" in="${FlightTest.list()}">
                                            <g:flighttest var="${flightTestInstance}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind.list')}:</td>
                                    <td>
                                        <g:each var="flightTestWindInstance" in="${FlightTestWind.list()}">
                                            <g:flighttestwind var="${flightTestWindInstance}" link="${createLink(controller:'flightTestWind',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest.list')}:</td>
                                    <td>
                                        <g:each var="landingTestInstance" in="${LandingTest.list()}">
                                            <g:landingtest var="${landingTestInstance}" link="${createLink(controller:'landingTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtesttask.list')}:</td>
                                    <td>
                                        <g:each var="landingTestTaskInstance" in="${LandingTestTask.list()}">
                                            <g:landingtesttask var="${landingTestTaskInstance}" link="${createLink(controller:'landingTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest.list')}:</td>
                                    <td>
                                        <g:each var="specialTestInstance" in="${SpecialTest.list()}">
                                            <g:specialtest var="${specialTestInstance}" link="${createLink(controller:'specialTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtesttask.list')}:</td>
                                    <td>
                                        <g:each var="specialTestTaskInstance" in="${SpecialTestTask.list()}">
                                            <g:specialtesttask var="${specialTestTaskInstance}" link="${createLink(controller:'specialTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.list')}:</td>
                                    <td>
                                        <g:each var="testInstance" in="${Test.list()}">
                                            <g:test var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegplanning.list')}:</td>
                                    <td>
                                        <g:each var="testLegPlanningInstance" in="${TestLegPlanning.list()}">
                                            <g:testlegplanning var="${testLegPlanningInstance}" link="${createLink(controller:'testLegPlanning',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegflight.list')}:</td>
                                    <td>
                                        <g:each var="testLegFlightInstance" in="${TestLegFlight.list()}">
                                            <g:testlegflight var="${testLegFlightInstance}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind.list')}:</td>
                                    <td>
                                        <g:each var="windInstance" in="${Wind.list()}">
                                            <g:wind var="${windInstance}" link="${createLink(controller:'wind',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>