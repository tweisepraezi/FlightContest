<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:if test="${flightTestWindInstance.IsCorridor()}">
            <title>${message(code:'fc.route.edit')}</title>
        </g:if>
        <g:else>
            <title>${message(code:'fc.flighttestwind.edit')}</title>
        </g:else>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <g:if test="${flightTestWindInstance.IsCorridor()}">
                    <h2>${message(code:'fc.route.edit')}</h2>
                </g:if>
                <g:else>
                    <h2>${message(code:'fc.flighttestwind.edit')}</h2>
                </g:else>
                <g:hasErrors bean="${flightTestWindInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${flightTestWindInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['flighttestwindReturnAction':flighttestwindReturnAction,'flighttestwindReturnController':flighttestwindReturnController,'flighttestwindReturnID':flighttestwindReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <g:set var="flighttestwind_used" value="${Test.findByFlighttestwind(flightTestWindInstance)}"/>
                        <g:set var="test_instance" value="${Test.get(flighttestwindReturnID)}"/>
                        <g:set var="is_loggerdata" value="${test_instance && !test_instance.flightTestComplete && test_instance.IsLoggerData()}"/>
                        <g:set var="is_showmappossible" value="${test_instance && test_instance.IsShowMapPossible()}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.flighttest')}: <g:flighttest var="${flightTestWindInstance?.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!flightTestWindInstance.Used() && !flightTestWindInstance.flighttest.task.lockPlanning}">
                            <g:if test="${flightTestWindInstance.IsCorridor()}">
                                <fieldset>
                                    <legend>${message(code:'fc.route')}</legend>
                                    <g:showDeviatingRoute flighttestwind="${flightTestWindInstance}" routeid="${flightTestWindInstance.corridorRouteID}" ti="${ti}"/>
                                    <div>
                                        <label>${message(code:'fc.corridorwidth.deviating')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="corridorWidthWind" name="corridorWidthWind" value="${fieldValue(bean:flightTestWindInstance,field:'corridorWidthWind')}" tabIndex="${ti[0]++}"/>
                                    </div>
                                </fieldset>
                            </g:if>
                            <g:if test="${!flightTestWindInstance.GetParcour().contest.anrFlying}">
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
                            </g:if>
                            <g:else>
                                <input type="hidden" name="direction" value="${0.0}" />
                                <input type="hidden" name="speed" value="${0.0}" />
                            </g:else>
	                    </g:if>
                        <g:else>
                            <g:if test="${flightTestWindInstance.IsCorridor()}">
                                <fieldset>
                                    <legend>${message(code:'fc.route')}</legend>
                                    <p><g:route var="${flightTestWindInstance.GetRoute()}" link="${createLink(controller:'route',action:'show')}" /></p>
                                </fieldset>
                            </g:if>
                            <g:if test="${!flightTestWindInstance.IsCorridor() || flightTestWindInstance.IsWind()}">
                                <fieldset>
                                    <legend>${message(code:'fc.wind')}</legend>
                                    <p>${flightTestWindInstance.wind.name()}</p>
                                </fieldset>
                            </g:if>
                        </g:else>
                        <fieldset>
                            <legend>${message(code:'fc.runway')}</legend>
                            <p>
                                <label>${message(code:'fc.runway.direction.to')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="TODirection" name="TODirection" value="${fieldValue(bean:flightTestWindInstance,field:'TODirection')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.offset.to')} [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="TOOffset" name="TOOffset" value="${fieldValue(bean:flightTestWindInstance,field:'TOOffset')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.orthogonaloffset.to')} [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="TOOrthogonalOffset" name="TOOrthogonalOffset" value="${fieldValue(bean:flightTestWindInstance,field:'TOOrthogonalOffset')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.to2sp')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
								<g:if test="${flighttestwind_used || flightTestWindInstance.flighttest.task.lockPlanning}">
									<input type="text" id="TODurationFormula" name="TODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'TODurationFormula')}" disabled tabIndex="${ti[0]++}"/>
								</g:if>
								<g:else>
									<input type="text" id="TODurationFormula" name="TODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'TODurationFormula')}" tabIndex="${ti[0]++}"/>
								</g:else>
                            </p>
                            <br/>
                            <p>
                                <label>${message(code:'fc.runway.direction.ldg')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="LDGDirection" name="LDGDirection" value="${fieldValue(bean:flightTestWindInstance,field:'LDGDirection')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.offset.ldg')} [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="LDGOffset" name="LDGOffset" value="${fieldValue(bean:flightTestWindInstance,field:'LDGOffset')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.orthogonaloffset.ldg')} [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="LDGOrthogonalOffset" name="LDGOrthogonalOffset" value="${fieldValue(bean:flightTestWindInstance,field:'LDGOrthogonalOffset')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.fp2ldg')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
								<g:if test="${flighttestwind_used || flightTestWindInstance.flighttest.task.lockPlanning}">
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
                                        <input type="text" id="iTOiLDGDirection" name="iTOiLDGDirection" value="${fieldValue(bean:flightTestWindInstance,field:'iTOiLDGDirection')}" tabIndex="${ti[0]++}"/>
                                    </p>
                                    <p>
                                        <label>${message(code:'fc.runway.offset.itoildg')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="iTOiLDGOffset" name="iTOiLDGOffset" value="${fieldValue(bean:flightTestWindInstance,field:'iTOiLDGOffset')}" tabIndex="${ti[0]++}"/>
                                    </p>
                                    <p>
                                        <label>${message(code:'fc.runway.orthogonaloffset.itoildg')} [${message(code:'fc.mile')}]:</label>
                                        <br/>
                                        <input type="text" id="iTOiLDGOrthogonalOffset" name="iTOiLDGOrthogonalOffset" value="${fieldValue(bean:flightTestWindInstance,field:'iTOiLDGOrthogonalOffset')}" tabIndex="${ti[0]++}"/>
                                    </p>
                                    <p>
                                        <label>${message(code:'fc.runway.duration.ifp2ildg')}*:</label>
                                        <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                        <br/>
                                        <g:if test="${flighttestwind_used || flightTestWindInstance.flighttest.task.lockPlanning}">
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
                                    <g:if test="${flighttestwind_used || flightTestWindInstance.flighttest.task.lockPlanning}">
                                        <input type="text" id="iTODurationFormula" name="iTODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'iTODurationFormula')}" disabled tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:else>
                                        <input type="text" id="iTODurationFormula" name="iTODurationFormula" value="${fieldValue(bean:flightTestWindInstance,field:'iTODurationFormula')}" tabIndex="${ti[0]++}"/>
                                    </g:else>
                                </p>
                            </fieldset>
                        </g:if>
                        <input type="hidden" name="id" value="${flightTestWindInstance?.id}" />
                        <input type="hidden" name="version" value="${flightTestWindInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${!flighttestwind_used && !flightTestWindInstance.flighttest.task.lockPlanning}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:if test="${flighttestwindReturnAction == "flightresults"}">
                            <g:if test="${is_showmappossible}">
                                <g:actionSubmit action="showofflinemap_to" value="${message(code:'fc.offlinemap.to')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="showofflinemap_ldg" value="${message(code:'fc.offlinemap.ldg')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:if test="${is_loggerdata}">
                                <g:actionSubmit action="recalculateresults" value="${message(code:'fc.flightresults.recalculate.loggerdata')}" tabIndex="${ti[0]++}"/>
                            </g:if>
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>