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
                        <g:if test="${!flightTestWindInstance.Used()}">
	                        <fieldset>
	                            <legend>${message(code:'fc.wind')}</legend>
	                            <p>
	                                <label>${message(code:'fc.wind.direction')}* [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:flightTestWindInstance,field:'direction')}" tabIndex="1"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.wind.velocity')}* [${message(code:'fc.knot')}]:</label>
	                                <br/>
	                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:flightTestWindInstance,field:'speed')}" tabIndex="2"/>
	                            </p>
	                        </fieldset>
	                    </g:if>
                        <g:else>
                            <fieldset>
                                <legend>${message(code:'fc.wind')}</legend>
                                <p/>
                                <br/>
                                <p>${flightTestWindInstance.wind.name()}</p>
                            </fieldset>
                        </g:else>
                        <fieldset>
                            <legend>${message(code:'fc.runway')}</legend>
                            <fieldset>
	                            <p>
	                                <label>${message(code:'fc.runway.direction.to')} [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="TODirection" name="TODirection" value="${fieldValue(bean:flightTestWindInstance,field:'TODirection')}" tabIndex="3"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.runway.offset.to')} [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="TOOffset" name="TOOffset" value="${fieldValue(bean:flightTestWindInstance,field:'TOOffset')}" tabIndex="4"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.runway.orthogonaloffset.to')} [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="TOOrthogonalOffset" name="TOOrthogonalOffset" value="${fieldValue(bean:flightTestWindInstance,field:'TOOrthogonalOffset')}" tabIndex="5"/>
	                            </p>
                            </fieldset>
                            <fieldset>
	                            <p>
	                                <label>${message(code:'fc.runway.direction.ldg')} [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="LDGDirection" name="LDGDirection" value="${fieldValue(bean:flightTestWindInstance,field:'LDGDirection')}" tabIndex="6"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.runway.offset.ldg')} [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="LDGOffset" name="LDGOffset" value="${fieldValue(bean:flightTestWindInstance,field:'LDGOffset')}" tabIndex="7"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.runway.orthogonaloffset.ldg')} [${message(code:'fc.mile')}]:</label>
	                                <br/>
	                                <input type="text" id="LDGOrthogonalOffset" name="LDGOrthogonalOffset" value="${fieldValue(bean:flightTestWindInstance,field:'LDGOrthogonalOffset')}" tabIndex="8"/>
	                            </p>
                            </fieldset>
                            <g:if test="${flightTestWindInstance.flighttest.route.IsIntermediateRunway()}">
	                            <fieldset>
		                            <p>
		                                <label>${message(code:'fc.runway.direction.itoildg')} [${message(code:'fc.grad')}]:</label>
		                                <br/>
		                                <input type="text" id="iTOiLDGDirection" name="iTOiLDGDirection" value="${fieldValue(bean:flightTestWindInstance,field:'iTOiLDGDirection')}" tabIndex="9"/>
		                            </p>
	                                <p>
	                                    <label>${message(code:'fc.runway.offset.itoildg')} [${message(code:'fc.mile')}]:</label>
	                                    <br/>
	                                    <input type="text" id="iTOiLDGOffset" name="iTOiLDGOffset" value="${fieldValue(bean:flightTestWindInstance,field:'iTOiLDGOffset')}" tabIndex="10"/>
	                                </p>
	                                <p>
	                                    <label>${message(code:'fc.runway.orthogonaloffset.itoildg')} [${message(code:'fc.mile')}]:</label>
	                                    <br/>
	                                    <input type="text" id="iTOiLDGOrthogonalOffset" name="iTOiLDGOrthogonalOffset" value="${fieldValue(bean:flightTestWindInstance,field:'iTOiLDGOrthogonalOffset')}" tabIndex="11"/>
	                                </p>
	                            </fieldset>
                            </g:if>
                        </fieldset>
                        <input type="hidden" name="id" value="${flightTestWindInstance?.id}" />
                        <input type="hidden" name="version" value="${flightTestWindInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="101"/>
                        <g:if test="${!Test.findByFlighttestwind(flightTestWindInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="102"/>
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="103"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>