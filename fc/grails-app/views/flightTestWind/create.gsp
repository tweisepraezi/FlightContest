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
                        <g:set var="ti" value="${[]+1}"/>
                        <g:if test="${flightTestWindInstance.flighttest.route.corridorWidth}">
                            <fieldset>
                                <legend>${message(code:'fc.corridorwidth.deviating')}</legend>
                                <div>
                                    <label>${message(code:'fc.corridorwidth')} [${message(code:'fc.mile')}]:</label>
                                    <br/>
                                    <input type="text" id="corridorWidthWind" name="corridorWidthWind" value="${fieldValue(bean:flightTestWindInstance,field:'corridorWidthWind')}" tabIndex="${ti[0]++}"/>
                                </div>
                            </fieldset>
                        </g:if>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')}* [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:flightTestWindInstance,field:'direction')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.velocity')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:flightTestWindInstance,field:'speed')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.runway')}</legend>
                            <p>
                                <label>${message(code:'fc.runway.direction.to')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="TODirection" name="TODirection" value="${flightTestWindInstance.TODirection}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.to2sp')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
								<g:if test="${flightTestWindInstance.flighttest.task.lockPlanning}">
									<input type="text" id="TODurationFormula" name="TODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'TODurationFormula')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="TODurationFormula" name="TODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'TODurationFormula')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.direction.ldg')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="LDGDirection" name="LDGDirection" value="${flightTestWindInstance.LDGDirection}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.fp2ldg')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
								<g:if test="${flightTestWindInstance.flighttest.task.lockPlanning}">
									<input type="text" id="LDGDurationFormula" name="LDGDurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'LDGDurationFormula')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="LDGDurationFormula" name="LDGDurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'LDGDurationFormula')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                        </fieldset>
                        <g:set var="intermediate_landing" value="${flightTestWindInstance.flighttest.route.IsIntermediateLanding()}"/>
                        <g:if test="${intermediate_landing.isLanding}">
                            <fieldset>
                                <legend>${message(code:'fc.runway.intermediate')}</legend>
                                <g:if test="${intermediate_landing.isRunway}">
                                    <p>
                                        <label>${message(code:'fc.runway.direction.ildg')} [${message(code:'fc.grad')}]:</label>
                                        <br/>
                                        <input type="text" id="iTOiLDGDirection" name="iTOiLDGDirection" value="${flightTestWindInstance.iTOiLDGDirection}" tabIndex="${ti[0]++}"/>
                                    </p>
                                    <p>
                                        <label>${message(code:'fc.runway.duration.ifp2ildg')}*:</label>
                                        <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                        <br/>
                                        <g:if test="${flightTestWindInstance.flighttest.task.lockPlanning}">
                                            <input type="text" id="iLDGDurationFormula" name="iLDGDurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'iLDGDurationFormula')}" disabled tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:else>
                                            <input type="text" id="iLDGDurationFormula" name="iLDGDurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'iLDGDurationFormula')}" tabIndex="${ti[0]++}"/>
                                        </g:else>
                                    </p>
                                </g:if>
                                <p>
                                    <label>${message(code:'fc.runway.duration.2isp')}*:</label>
                                    <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                    <br/>
                                    <g:if test="${flightTestWindInstance.flighttest.task.lockPlanning}">
                                        <input type="text" id="iTODurationFormula" name="iTODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'iTODurationFormula')}" disabled tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:else>
                                        <input type="text" id="iTODurationFormula" name="iTODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'iTODurationFormula')}" tabIndex="${ti[0]++}"/>
                                    </g:else>
                                </p>
                            </fieldset>
                        </g:if>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>