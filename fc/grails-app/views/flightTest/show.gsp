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
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${flightTestInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <td><g:route var="${flightTestInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.observation')}:</td>
                                    <g:if test="${flightTestInstance.IsObservationSignUsed()}">
                                        <td>${message(code:'fc.yes')}</td>
                                    </g:if>
                                    <g:else>
                                        <td>${message(code:'fc.no')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind.list')}:</td>
                                    <td>
                                        <g:each var="flighttestwind_instance" in="${FlightTestWind.findAllByFlighttest(flightTestInstance,[sort:"id"])}">
                                            <g:flighttestwind var="${flighttestwind_instance}" link="${createLink(controller:'flightTestWind',action:'edit')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${flightTestInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" tabIndex="${ti[0]++}" />
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
                        <g:each var="flighttestwind_instance" in="${FlightTestWind.findAllByFlighttest(flightTestInstance,[sort:"id"])}">
                            <g:if test="${Test.findByFlighttestwind(flighttestwind_instance)}">
                                <g:set var="foundTest" value="${true}" />
                            </g:if>
                        </g:each>
                        <g:if test="${!foundTest}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}" />
                        </g:if>
                        <g:actionSubmit action="createflighttestwind" value="${message(code:'fc.flighttestwind.add1')}" tabIndex="${ti[0]++}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>