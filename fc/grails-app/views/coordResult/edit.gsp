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
                                    <td><g:test var="${coordResultInstance?.test}" link="${createLink(controller:'test',action:'flightresults')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${coordResultInstance.titleCode()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <legend>${message(code:'fc.test.results.plan')}</legend>
                            <table>
                                <tbody>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.cptime')}:</td>
                                        <td>${FcMath.TimeStr(coordResultInstance.planCpTime)}</td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.altitude')}:</td>
                                        <td>${coordResultInstance.altitude}${message(code:'fc.foot')}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.test.results.measured')}</legend>
                            <g:set var="edit_checkpoint" value="${false}"/>
                            <g:if test="${!coordResultInstance.test.flightTestComplete}">
                                <g:if test="${coordResultInstance.type == CoordType.TO}">
                                    <g:if test="${coordResultInstance.test.IsFlightTestCheckTakeOff() || coordResultInstance.test.GetFlightTestTakeoffCheckSeconds()}">
                                        <g:set var="edit_checkpoint" value="${true}"/>
                                    </g:if>
                                </g:if>
                                <g:elseif test="${coordResultInstance.type == CoordType.LDG}">
                                    <g:if test="${coordResultInstance.test.IsFlightTestCheckLanding()}">
                                        <g:set var="edit_checkpoint" value="${true}"/>
                                    </g:if>
                                </g:elseif>
                                <g:else>
                                    <g:set var="edit_checkpoint" value="${true}"/>
                                </g:else>
                            </g:if>
                            <g:if test="${edit_checkpoint}">
                                 <p>
                                   	<div>
                                   		<g:checkBox name="resultCpNotFound" value="${coordResultInstance.resultCpNotFound}" />
                                       	<label>${coordResultInstance.GetCpNotFoundName()}</label>
                                   	</div>
                                 </p>
                                 <p>
                                    <label>${message(code:'fc.cptime')}* [${message(code:'fc.time.hminsec')}]:</label>
                                    <br/>
                                    <input type="text" id="resultCpTimeInput" name="resultCpTimeInput" value="${fieldValue(bean:coordResultInstance,field:'resultCpTimeInput')}" tabIndex="2"/>
                                 </p>
                                <p>
                                    <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
                                    <br/>
                                    <input type="text" id="resultAltitude" name="resultAltitude" value="${coordResultInstance.resultAltitude}" tabIndex="3"/>
                                </p>
                                <g:if test="${coordResultInstance.type.IsBadCourseCheckCoord()}">
                                    <p>
                                        <label>${message(code:'fc.badcoursenum')}*:</label>
                                        <br/>
                                        <input type="text" id="resultBadCourseNum" name="resultBadCourseNum" value="${fieldValue(bean:coordResultInstance,field:'resultBadCourseNum')}" tabIndex="4"/>
                                    </p>
                                </g:if>
                            </g:if>
                            <g:else>
                                <table>
                                    <tbody>
                                        <g:if test="${coordResultInstance.resultCpNotFound}">
                                            <tr>
                                                <td colspan="2">${coordResultInstance.GetCpNotFoundName()}</td>
                                            </tr>
                                        </g:if>
                                        <g:else>
                                            <tr>
                                                <td class="detailtitle">${message(code:'fc.cptime')}:</td>
                                                <td>${coordResultInstance.resultCpTimeInput}</td>
                                            </tr>
                                        </g:else>
                                        <g:if test="${coordResultInstance.resultAltitude}">
	                                        <tr>
	                                            <td class="detailtitle">${message(code:'fc.altitude')}:</td>
	                                            <td>${coordResultInstance.resultAltitude}${message(code:'fc.foot')}</td>
	                                        </tr>
	                                    </g:if>
                                        <g:if test="${coordResultInstance.type.IsBadCourseCheckCoord()}">
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
                        <input type="hidden" name="next" value="${params.next}" />
                        <g:if test="${edit_checkpoint}">
                        	<g:if test="${params.next}">
                            	<g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" tabIndex="5"/>
                            </g:if>
                            <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}" tabIndex="6"/>
                            <g:actionSubmit action="reset" value="${message(code:'fc.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="7"/>
                        </g:if>
                        <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}" tabIndex="8"/>
                            </g:if>
                        </g:else>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="9"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>