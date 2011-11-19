<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttestwind.create')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttestwind.create')}</h2>
                <g:hasErrors bean="${flightTestWindInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${flightTestWindInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromlistplanning}">
                        <g:set var="newparams" value="['flighttestid':params.flighttestid,'fromlistplanning':true]"/>
                    </g:if> <g:else> 
                        <g:set var="newparams" value="['flighttestid':params.flighttestid]"/>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
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
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>