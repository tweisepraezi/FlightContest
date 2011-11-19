<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.internal')}</title>
    </head>
    <body>
        <g:if test="${Contest.list()}">
            <g:mainnav link="${createLink(controller:'contest')}" controller="root" />
        </g:if> <g:else>
            <div class="grid">
                <ul class="nav main">
                    <li> <g:link controller="contest" action="create">${message(code:'fc.contest.new')}</g:link> </li>
                    <li> <g:link controller="contest" action="createtest">${message(code:'fc.contest.new.test')}</g:link> </li>
                    <li class="secondary"> <a class="list" href="${createLinkTo(dir:'')}">${message(code:'fc.internal')}</a> </li>
                </ul>
            </div>
        </g:else>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.internal')}</h2>
                <div class="block" id="forms" >
                    <g:form controller="contest">
                        <table>
                            <tbody>
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
                                    <td class="detailtitle">${message(code:'fc.routecoord.list')}:</td>
                                    <td>
                                        <g:each var="routeCoordInstance" in="${RouteCoord.list()}">
                                            <g:routecoord var="${routeCoordInstance}" link="${createLink(controller:'routeCoord',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routeleg.list')}:</td>
                                    <td>
                                        <g:each var="routeLegInstance" in="${RouteLeg.list()}">
                                            <g:routeleg var="${routeLegInstance}" link="${createLink(controller:'routeLeg',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestday.list')}:</td>
                                    <td>
                                        <g:each var="contestDayInstance" in="${ContestDay.list()}">
                                            <g:contestday var="${contestDayInstance}" link="${createLink(controller:'contestDay',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.list')}:</td>
                                    <td>
                                        <g:each var="contestDayTaskInstance" in="${ContestDayTask.list()}">
                                            <g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtest.list')}:</td>
                                    <td>
                                        <g:each var="navTestInstance" in="${NavTest.list()}">
                                            <g:navtest var="${navTestInstance}" link="${createLink(controller:'navTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttask.list')}:</td>
                                    <td>
                                        <g:each var="navTestTaskInstance" in="${NavTestTask.list()}">
                                            <g:navtesttask var="${navTestTaskInstance}" link="${createLink(controller:'navTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttaskleg.list')}:</td>
                                    <td>
                                        <g:each var="navTestTaskLegInstance" in="${NavTestTaskLeg.list()}">
                                            <g:navtesttaskleg var="${navTestTaskLegInstance}" link="${createLink(controller:'navTestTaskLeg',action:'show')}"/>
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
                                    <td class="detailtitle">${message(code:'fc.crewtest.list')}:</td>
                                    <td>
                                        <g:each var="crewTestInstance" in="${CrewTest.list()}">
                                            <g:crewtest var="${crewTestInstance}" link="${createLink(controller:'crewTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtestleg.list')}:</td>
                                    <td>
                                        <g:each var="crewTestLegInstance" in="${CrewTestLeg.list()}">
                                            <g:crewtestleg var="${crewTestLegInstance}" link="${createLink(controller:'crewTestLeg',action:'show')}"/>
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
                        <g:each var="c" in="${Contest.list()}">
                            <input type="hidden" name="id" value="${c?.id}" />
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.contest.delete.areyousure')}');" />
                        </g:each>
                    </g:form>
                </div>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>