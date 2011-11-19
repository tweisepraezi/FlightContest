<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${routeInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routecoord.list')}:</td>
                                    <td>
                                        <g:each var="routeCoordInstance" in="${routeInstance.routecoords}">
                                            <g:routecoord var="${routeCoordInstance}" link="${createLink(controller:'routeCoord',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.routeleg.list')}:</td>
                                    <td>
                                        <g:each var="routeLegInstance" in="${routeInstance.routelegs}">
                                            <g:routeleg var="${routeLegInstance}" link="${createLink(controller:'routeLeg',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttask.list')}:</td>
                                    <td>
                                        <g:each var="c" in="${NavTestTask.findAllByRoute(routeInstance)}">
                                            <g:navtesttask var="${c}" link="${createLink(controller:'navTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td>
                                        <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance)}">
                                            <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${routeInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:if test="${!NavTestTask.findByRoute(routeInstance) && !FlightTest.findByRoute(routeInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="createroutecoords" value="${message(code:'fc.routecoord.add1')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>