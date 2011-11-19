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
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.testlegplanningresult.from')}:</td>
                                    <td><g:test var="${testLegPlanningInstance?.test}" link="${createLink(controller:'test',action:'planningtaskresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <g:if test="${!testLegPlanningInstance.test.planningTestComplete}">
                                <p>
                                    <label>${message(code:'fc.trueheading')} [${message(code:'fc.grad')}]:</label>
                                    <br/>
                                    <input type="text" id="resultTrueHeading" name="resultTrueHeading" value="${fieldValue(bean:testLegPlanningInstance,field:'resultTrueHeading')}"/>
                                </p>
                                <p>
                                    <label>${message(code:'fc.legtime')} [${message(code:'fc.time.hminsec')}]:</label>
                                    <br/>
                                    <input type="text" id="resultLegTimeInput" name="resultLegTimeInput" value="${fieldValue(bean:testLegPlanningInstance,field:'resultLegTimeInput')}"/>
                                </p>
                             </g:if>
                             <g:else>
                                <table>
                                    <tbody>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.trueheading')}:</td>
                                            <td>${testLegPlanningInstance.resultTrueHeading}${message(code:'fc.grad')}</td>
                                        </tr>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.legtime')}:</td>
                                            <td>${testLegPlanningInstance.resultLegTimeInput}${message(code:'fc.time.h')}</td>
                                        </tr>
                                    </tbody>
                                </table>
                             </g:else>
                        </fieldset>
                        <input type="hidden" name="id" value="${testLegPlanningInstance.id}" />
                        <input type="hidden" name="testid" value="${testLegPlanningInstance.test.id}" />
                        <g:if test="${!testLegPlanningInstance.test.planningTestComplete}">
                            <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>