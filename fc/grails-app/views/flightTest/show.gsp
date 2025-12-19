<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttest.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttest.show')}</h2>
                <div class="block" id="forms" >
                    <g:form params="${['flighttestReturnAction':flighttestReturnAction,'flighttestReturnController':flighttestReturnController,'flighttestReturnID':flighttestReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <g:each var="flighttestwind_instance" in="${FlightTestWind.findAllByFlighttest(flightTestInstance,[sort:"id"])}">
                                    <g:if test="${Test.findByFlighttestwind(flighttestwind_instance)}">
                                        <g:set var="foundTest" value="${true}" />
                                    </g:if>
                                </g:each>
                                <g:if test="${flightTestInstance.route.corridorWidth}">
                                    <g:set var="route_label" value="${message(code:'fc.parcour')}" />
                                </g:if>
                                <g:else>
                                    <g:set var="route_label" value="${message(code:'fc.route')}" />
                                </g:else>
                                <g:if test="${!foundTest && !flightTestInstance.task.lockPlanning}">
                                    <label>${route_label}*:</label>
                                    <br/>
                                    <g:select from="${RouteTools.GetOkFlightTestRoutes(flightTestInstance.task.contest)}" optionKey="id" optionValue="${{it.GetParcourName(true)}}" name="route.id" value="${flightTestInstance?.route?.id}" tabIndex="${ti[0]++}"></g:select>
                                </g:if>
                                <g:else>
                                    <label>${route_label}:</label>
                                    <g:parcour var="${flightTestInstance?.route}" link="${createLink(controller:'route',action:'show')}"/>
                                </g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.addtitle')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:flightTestInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <g:editFlightTest flighttest="${flightTestInstance}" ti="${ti}"/>
                        <fieldset>
                            <p>
                                <g:set var="intermediate_landing" value="${flightTestInstance.route.IsIntermediateLanding()}"/>
                                <g:if test="${flightTestInstance.route.corridorWidth}">
                                    <label>${message(code:'fc.route.list')}:</label>
                                </g:if>
                                <g:else>
                                    <label>${message(code:'fc.flighttestwind.list')}:</label>
                                </g:else>
                                <br/>
                                <table>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>${message(code:'fc.runway.direction.to')}</th>
                                            <th>${message(code:'fc.runway.duration.to2sp')}</th>
                                            <th>${message(code:'fc.runway.direction.ldg')}</th>
                                            <th>${message(code:'fc.runway.duration.fp2ldg')}</th>
                                            <g:if test="${intermediate_landing.isRunway}">
                                                <th>${message(code:'fc.runway.direction.ildg')}</th>
                                                <th>${message(code:'fc.runway.duration.ifp2ildg')}</th>
                                            </g:if>
                                            <g:if test="${intermediate_landing.isLanding}">
                                                <th>${message(code:'fc.runway.duration.2isp')}</th>
                                            </g:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:each var="flighttestwind_instance" in="${FlightTestWind.findAllByFlighttest(flightTestInstance,[sort:"id"])}">
                                            <tr>
                                                <td><g:flighttestwind var="${flighttestwind_instance}" link="${createLink(controller:'flightTestWind',action:'edit')}"/></td>
                                                <td>${fieldValue(bean:flighttestwind_instance,field:'TODirection')}${message(code:'fc.grad')}</td>
                                                <td>${fieldValue(bean:flighttestwind_instance,field:'TODurationFormula')}</td>
                                                <td>${fieldValue(bean:flighttestwind_instance,field:'LDGDirection')}${message(code:'fc.grad')}</td>
                                                <td>${fieldValue(bean:flighttestwind_instance,field:'LDGDurationFormula')}</td>
                                                <g:if test="${intermediate_landing.isRunway}">
                                                    <td>${fieldValue(bean:flighttestwind_instance,field:'iTOiLDGDirection')}${message(code:'fc.grad')}</td>
                                                    <td>${fieldValue(bean:flighttestwind_instance,field:'iLDGDurationFormula')}</td>
                                                </g:if>
                                                <g:if test="${intermediate_landing.isLanding}">
                                                    <td>${fieldValue(bean:flighttestwind_instance,field:'iTODurationFormula')}</td>
                                                </g:if>
                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${flightTestInstance?.id}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="update_end" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${flightTestInstance.CanObservationsAdd()}">
                            <g:actionSubmit action="addobservations" value="${message(code:'fc.flighttestwind.addobservation')}" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:if test="${flightTestInstance.CanObservationsRemove()}">
                            <g:actionSubmit action="removeobservations" value="${message(code:'fc.flighttestwind.removeobservation')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:if test="${flightTestInstance.IsObservationSignUsed()}">
                            <g:actionSubmit action="printneutralobservation" value="${message(code:'fc.flighttestwind.printneutralobservation')}" tabIndex="${ti[0]++}" />
                            <g:actionSubmit action="printobservationresults" value="${message(code:'fc.flighttestwind.printobservationresults')}" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:if test="${flightTestInstance.CanANRPlanPrinted()}">
                            <g:if test="${flightTestInstance.route.defaultPrintMap}">
                                <g:actionSubmit action="printneutralanrplan" value="${message(code:'fc.flighttestwind.printanrmap')}" tabIndex="${ti[0]++}" />
                            </g:if>
                            <g:actionSubmit action="printresultanrplans" value="${message(code:'fc.flighttestwind.printanrmapwithtimes')}" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:each var="flighttestwind_instance" in="${FlightTestWind.findAllByFlighttest(flightTestInstance,[sort:"id"])}">
                            <g:if test="${Test.findByFlighttestwind(flighttestwind_instance)}">
                                <g:set var="foundTest" value="${true}" />
                            </g:if>
                        </g:each>
                        <g:if test="${!foundTest && !flightTestInstance.task.lockPlanning}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:if test="${!flightTestInstance.task.lockPlanning}">
                            <g:if test="${flightTestInstance.route.corridorWidth}">
                                <g:actionSubmit action="createflighttestwind" value="${message(code:'fc.route.add')}" tabIndex="${ti[0]++}" />
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="createflighttestwind" value="${message(code:'fc.flighttestwind.add1')}" tabIndex="${ti[0]++}" />
                            </g:else>
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>