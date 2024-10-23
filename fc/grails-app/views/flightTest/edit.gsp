<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttest.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttest.edit')}</h2>
                <g:hasErrors bean="${flightTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${flightTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${flightTestInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:flightTestInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <g:each var="flighttestwind_instance" in="${FlightTestWind.findAllByFlighttest(flightTestInstance,[sort:"id"])}">
                                    <g:if test="${Test.findByFlighttestwind(flighttestwind_instance)}">
                                        <g:set var="foundTest" value="${true}" />
                                    </g:if>
                                </g:each>
                                <g:if test="${!foundTest && !flightTestInstance.task.lockPlanning}">
                                    <label>${message(code:'fc.route')}*:</label>
                                    <br/>
                                    <g:select from="${RouteTools.GetOkFlightTestRoutes(flightTestInstance.task.contest)}" optionKey="id" optionValue="${{it.GetFlightTestRouteName()}}" name="route.id" value="${flightTestInstance?.route?.id}" tabIndex="${ti[0]++}"></g:select>
                                </g:if>
                                <g:else>
                                    <label>${message(code:'fc.route')}:</label>
                                    <g:route var="${flightTestInstance?.route}" link="${createLink(controller:'route',action:'show')}"/>
                                </g:else>
                            </p>
                        </fieldset>
                        <g:editFlightTest flighttest="${flightTestInstance}" ti="${ti}"/>
                        <input type="hidden" name="id" value="${flightTestInstance?.id}" />
                        <input type="hidden" name="version" value="${flightTestInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>