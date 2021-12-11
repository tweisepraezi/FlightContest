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
                        <p>Session timeout: ${session.maxInactiveInterval/60}min</p>
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
                                        <td class="detailtitle">${message(code:'fc.contest.properties')}:</td>
                                        <td>
                                            <g:each var="contest_property_instance" in="${ContestProperty.findAllByContest(contestInstance,[sort:"id"])}">
                                                ${contest_property_instance.key}:${contest_property_instance.value}
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.crew.list')}:</td>
                                        <td>
                                            <g:each var="crewInstance" in="${Crew.findAllByContest(contestInstance,[sort:"id"])}">
                                                <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'edit')}"/>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
	                                <tr>
	                                    <td class="detailtitle">${message(code:'fc.team.list')}:</td>
	                                    <td>
	                                        <g:each var="teamInstance" in="${Team.list()}">
	                                            <g:team var="${teamInstance}" link="${createLink(controller:'team',action:'edit')}"/>
	                                            <br/>
	                                        </g:each>
	                                    </td>
	                                </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.aircraft.list')}:</td>
                                        <td>
                                            <g:each var="aircraftInstance" in="${Aircraft.findAllByContest(contestInstance,[sort:"id"])}">
                                                <g:aircraft var="${aircraftInstance}" link="${createLink(controller:'aircraft',action:'edit')}"/>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route.list')}:</td>
                                        <td>
                                            <g:each var="routeInstance" in="${Route.findAllByContest(contestInstance,[sort:"idTitle"])}">
                                                <g:route var="${routeInstance}" link="${createLink(controller:'route',action:'show')}"/>
                                                <br/>
                                                <g:each var="coordRouteInstance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:"id"])}">
                                                    - <g:coordroute var="${coordRouteInstance}" link="${createLink(controller:'coordRoute',action:'show')}"/>
                                                    <br/>
                                                </g:each>
                                                <g:each var="routeLegInstance" in="${RouteLegCoord.findAllByRoute(routeInstance,[sort:"id"])}">
                                                    + <g:routelegcoord var="${routeLegInstance}" link="${createLink(controller:'routeLegCoord',action:'show')}"/>
                                                    <br/>
                                                </g:each>
                                                <g:each var="routeLegInstance" in="${RouteLegTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                                    - <g:routelegtest var="${routeLegInstance}" link="${createLink(controller:'routeLegTest',action:'show')}"/>
                                                    <br/>
                                                </g:each>
                                                <br/>
                                            </g:each>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.task.list')}:</td>
                                        <td>
                                            <g:each var="taskInstance" in="${Task.findAllByContest(contestInstance,[sort:"id"])}">
                                                - <g:task var="${taskInstance}" link="${createLink(controller:'task',action:'edit')}"/>
                                                <br/>
                                                <g:if test="${taskInstance.planningtest}">
                                                    -- <g:planningtest var="${taskInstance.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/>
                                                    <br/>
                                                    <g:each var="planningTestTaskInstance" in="${PlanningTestTask.findAllByPlanningtest(taskInstance.planningtest,[sort:"id"])}">
                                                        +++ <g:planningtesttask var="${planningTestTaskInstance}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                                        <br/>
                                                        ++++ <g:wind var="${planningTestTaskInstance.wind}" link="${createLink(controller:'wind',action:'show')}"/>
                                                        <br/>
                                                    </g:each>
                                                </g:if>
                                                <g:if test="${taskInstance.flighttest}">
                                                    -- <g:flighttest var="${taskInstance.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                                    <br/>
                                                    <g:each var="flightTestWindInstance" in="${FlightTestWind.findAllByFlighttest(taskInstance.flighttest,[sort:"id"])}">
                                                        +++ <g:flighttestwind var="${flightTestWindInstance}" link="${createLink(controller:'flightTestWind',action:'show')}"/>
                                                        <br/>
                                                        ++++ <g:wind var="${flightTestWindInstance.wind}" link="${createLink(controller:'wind',action:'show')}"/>
                                                        <br/>
                                                    </g:each>
                                                </g:if>
                                                <g:each var="testInstance" in="${Test.findAllByTask(taskInstance,[sort:"id"])}">
                                                    -- <g:test var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/>
                                                    <br/>
                                                    <g:each var="testLegPlanningInstance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                                                        +++ <g:testlegplanning var="${testLegPlanningInstance}" link="${createLink(controller:'testLegPlanning',action:'show')}"/>
                                                        <br/>
                                                    </g:each>
                                                    <g:each var="testLegFlightInstance" in="${TestLegFlight.findAllByTest(testInstance,[sort:"id"])}">
                                                        --- <g:testlegflight var="${testLegFlightInstance}" link="${createLink(controller:'testLegFlight',action:'show')}"/>
                                                        <br/>
                                                    </g:each>
                                                </g:each>
                                            </g:each>
                                        </td>
                                    </tr>
                                </g:if>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>