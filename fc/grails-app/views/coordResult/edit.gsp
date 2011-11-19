<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordresult.edit',args:[params.name])}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}"/>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordresult.edit',args:[params.name])}</h2>
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
                                    <td class="detailtitle">${message(code:'fc.coordresult.from')}:</td>
                                    <td><g:test var="${coordResultInstance?.test}" link="${createLink(controller:'test',action:'planningtaskresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.test.results.plan')}</legend>
                            <table>
                                <tbody>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.cptime')}:</td>
                                        <td>${coordResultInstance.planCpTime.format('HH:mm:ss')}</td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.altitude')}:</td>
                                        <td>${coordResultInstance.altitude}${message(code:'fc.foot')}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.test.results.given')}</legend>
                            <g:if test="${!coordResultInstance.test.flightTestComplete}">
                                 <p>
                                   <div>
                                       <g:checkBox name="resultCpNotFound" value="${coordResultInstance.resultCpNotFound}" />
                                       <label>${message(code:'fc.flighttest.cpnotfound')}</label>
                                   </div>
                                 </p>
                                 <p>
                                    <label>${message(code:'fc.cptime')}* [${message(code:'fc.time.hminsec')}]:</label>
                                    <br/>
                                    <input type="text" id="resultCpTimeInput" name="resultCpTimeInput" value="${fieldValue(bean:coordResultInstance,field:'resultCpTimeInput')}"/>
                                 </p>
                                <p>
                                    <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
                                    <br/>
                                    <input type="text" id="resultAltitude" name="resultAltitude" value="${fieldValue(bean:coordResultInstance,field:'resultAltitude')}"/>
                                </p>
                                <g:if test="${coordResultInstance.type != CoordType.SP}">
                                    <p>
                                        <label>${message(code:'fc.badcoursenum')}*:</label>
                                        <br/>
                                        <input type="text" id="resultBadCourseNum" name="resultBadCourseNum" value="${fieldValue(bean:coordResultInstance,field:'resultBadCourseNum')}"/>
                                    </p>
                                </g:if>
                            </g:if>
                            <g:else>
                                <table>
                                    <tbody>
                                        <g:if test="${coordResultInstance.resultCpNotFound}">
                                            <tr>
                                                <td>${message(code:'fc.flighttest.cpnotfound')}</td>
                                            </tr>
                                        </g:if>
                                        <g:else>
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.cptime')}:</td>
                                                <td>${coordResultInstance.resultCpTimeInput}</td>
                                            </tr>
                                        </g:else>
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.altitude')}:</td>
                                            <td>${coordResultInstance.resultAltitude}${message(code:'fc.foot')}</td>
                                        </tr>
                                        <g:if test="${coordResultInstance.type != CoordType.SP}">
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.badcoursenum')}:</td>
                                                <td>${coordResultInstance.resultBadCourseNum}</td>
                                            </tr>
                                        </g:if>
                                    </tbody>
                                </table>
                            </g:else>
                        </fieldset>
                        <input type="hidden" name="id" value="${coordResultInstance.id}" />
                        <input type="hidden" name="testid" value="${coordResultInstance.test.id}" />
                        <input type="hidden" name="name" value="${params.name}" />
                        <g:if test="${!coordResultInstance.test.flightTestComplete}">
                            <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                            <g:actionSubmit action="reset" value="${message(code:'fc.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>