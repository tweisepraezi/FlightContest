<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttestwind.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttestwind.edit')}</h2>
                <g:hasErrors bean="${flightTestWindInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${flightTestWindInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['flighttestwindReturnAction':flighttestwindReturnAction,'flighttestwindReturnController':flighttestwindReturnController,'flighttestwindReturnID':flighttestwindReturnID]}" >
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:flighttest var="${flightTestWindInstance?.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')}* [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:flightTestWindInstance,field:'direction')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.speed')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:flightTestWindInstance,field:'speed')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${flightTestWindInstance?.id}" />
                        <input type="hidden" name="version" value="${flightTestWindInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:if test="${!Test.findByFlighttestwind(flightTestWindInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>