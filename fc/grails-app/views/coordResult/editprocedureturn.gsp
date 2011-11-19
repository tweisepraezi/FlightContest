<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordresult.edit.procedureturn',args:[params.name])}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}"/>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordresult.edit.procedureturn',args:[params.name])}</h2>
                <g:hasErrors bean="${coordResultInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordResultInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordresult.from.procedureturn')}:</td>
                                    <td><g:test var="${coordResultInstance?.test}" link="${createLink(controller:'test',action:'planningtaskresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <g:if test="${!coordResultInstance.test.flightTestComplete}">
                                 <p>
                                   <div>
                                       <g:checkBox name="resultProcedureTurnNotFlown" value="${coordResultInstance.resultProcedureTurnNotFlown}" />
                                       <label>${message(code:'fc.flighttest.procedureturnnotflown')}</label>
                                   </div>
                                 </p>
                             </g:if>
                             <g:else>
                                <table>
                                    <tbody>
                                         <g:if test="${coordResultInstance.resultProcedureTurnNotFlown}">
                                             <tr>
                                                <td>${message(code:'fc.flighttest.procedureturnnotflown')}</td>
                                             </tr>
                                         </g:if>
                                         <g:else>
                                                <td>${message(code:'fc.flighttest.procedureturnflown')}</td>
                                         </g:else>
                                    </tbody>
                                </table>
                             </g:else>
                        </fieldset>
                        <input type="hidden" name="id" value="${coordResultInstance.id}" />
                        <input type="hidden" name="testid" value="${coordResultInstance.test.id}" />
                        <input type="hidden" name="name" value="${params.name}" />
                        <input type="hidden" name="resultCpTimeInput" value="${coordResultInstance.resultCpTimeInput}" />
                        <g:if test="${!coordResultInstance.test.flightTestComplete}">
                            <g:actionSubmit action="updateprocedureturn" value="${message(code:'fc.update')}" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>