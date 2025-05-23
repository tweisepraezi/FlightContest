<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.testlegplanningresult.edit',args:[params.name])}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}"/>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.testlegplanningresult.edit',args:[params.name])}</h2>
                <g:hasErrors bean="${testLegPlanningInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${testLegPlanningInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form>
						<g:set var="ti" value="${[]+1}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegplanningresult.from')}:</td>
                                    <td><g:test var="${testLegPlanningInstance?.test}" link="${createLink(controller:'test',action:'planningtaskresults')}"/></td>
                                </tr>
                                <tr>
                                	<td class="detailtitle">${message(code:'fc.title')}:</td>
	                                <td>${testLegPlanningInstance.coordTitle.titleCode()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.test.results.plan')}</legend>
                            <table>
                                <tbody>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.trueheading')}:</td>
                                        <td>${FcMath.GradStr(testLegPlanningInstance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.legtime')}:</td>
                                        <td>${testLegPlanningInstance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.test.results.given')}</legend>
                            <g:if test="${!testLegPlanningInstance.test.planningTestComplete}">
                                <p>
                                    <label>${message(code:'fc.trueheading')}* [${message(code:'fc.grad')}]:</label>
                                    <br/>
                                    <input type="text" id="resultTrueHeading" name="resultTrueHeading" value="${fieldValue(bean:testLegPlanningInstance,field:'resultTrueHeading')}" tabIndex="${ti[0]++}"/>
                                </p>
                                <p>
                                    <label>${message(code:'fc.legtime')}* [${message(code:'fc.time.hminsec')}]:</label>
                                    <br/>
                                    <input type="text" id="resultLegTimeInput" name="resultLegTimeInput" value="${fieldValue(bean:testLegPlanningInstance,field:'resultLegTimeInput')}" tabIndex="${ti[0]++}"/>
                                </p>
                                <script>
									$('#resultTrueHeading').select();
                                    $(document).on('keypress', '#resultTrueHeading', function(e) {
										if (e.charCode == 13) {
											$('#resultLegTimeInput').select() 
											e.preventDefault();
										}
                                    });
                                    $(document).on('keypress', '#resultLegTimeInput', function(e) {
										if (e.charCode == 13) {
											<g:if test="${params.next}">
												$('#updatenext').focus()
											</g:if>
											<g:else>
												$('#updatereturn').focus()
											</g:else>
											e.preventDefault();
										}
                                    });
                                </script>
                             </g:if>
                             <g:else>
                                <table>
                                    <tbody>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.trueheading')}:</td>
                                            <td>${FcMath.GradStrComma(testLegPlanningInstance.resultTrueHeading)}${message(code:'fc.grad')}</td>
                                        </tr>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.legtime')}:</td>
                                            <td>${testLegPlanningInstance.resultLegTimeInput}${message(code:'fc.time.h')}</td>
                                        </tr>
                                    </tbody>
                                </table>
                             </g:else>
                        </fieldset>
                        <input type="hidden" name="id" value="${testLegPlanningInstance.id}"/>
                        <input type="hidden" name="testid" value="${testLegPlanningInstance.test.id}"/>
                        <g:if test="${!testLegPlanningInstance.test.planningTestComplete}">
                        	<g:if test="${params.next}">
                            	<g:actionSubmit action="updatenext" id="updatenext" value="${message(code:'fc.savenext')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:actionSubmit action="updatereturn" id="updatereturn" value="${message(code:'fc.saveend')}"  tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="reset" value="${message(code:'fc.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}"  tabIndex="${ti[0]++}"/>
                            </g:if>
                        </g:else>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>