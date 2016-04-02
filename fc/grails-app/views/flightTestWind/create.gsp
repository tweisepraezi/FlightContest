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
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:flightTestWindInstance,field:'direction')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.speed')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:flightTestWindInstance,field:'speed')}" tabIndex="2"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.runway')}</legend>
                            <p>
                                <label>${message(code:'fc.runway.direction.to')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="TODirection" name="TODirection" value="${flightTestWindInstance.TODirection}" tabIndex="3"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.direction.ldg')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="LDGDirection" name="LDGDirection" value="${flightTestWindInstance.LDGDirection}" tabIndex="4"/>
                            </p>
                            <g:if test="${flightTestWindInstance.flighttest.route.IsIntermediateRunway()}">
	                            <p>
	                                <label>${message(code:'fc.runway.direction.itoildg')} [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="iTOiLDGDirection" name="iTOiLDGDirection" value="${flightTestWindInstance.iTOiLDGDirection}" tabIndex="5"/>
	                            </p>
	                        </g:if>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="101"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="102"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>