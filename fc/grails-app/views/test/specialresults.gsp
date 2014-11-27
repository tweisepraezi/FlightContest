<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Special)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Special)}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${testInstance.crew.mark}"></g:if></td>
                                </tr>
                                <g:if test="${testInstance.crew.team}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.team')}:</td>
                                        <td><g:team var="${testInstance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.resultclass')}:</td>
                                        <td><g:resultclass var="${testInstance.crew.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <g:if test="${testInstance.taskAircraft}">
                                        <td><g:aircraft var="${testInstance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>
                                    <g:if test="${testInstance.taskAircraft}">
                                        <td>${testInstance.taskAircraft.type}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${!testInstance.specialTestComplete}">
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.specialresults.results')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="specialTestPenalties" name="specialTestPenalties" value="${fieldValue(bean:testInstance,field:'specialTestPenalties')}" tabIndex="1"/>
	                            </p>
	                        </fieldset>
                        </g:if>
   	                    <g:else>
                               <table>
                                   <tbody>
                                       <tr>
                                           <td class="detailtitle">${message(code:'fc.specialresults.results')}:</td>
                                           <td>${testInstance.specialTestPenalties} ${message(code:'fc.points')}</td>
                                       </tr>
                                   </tbody>
                               </table>
                        </g:else>
                        <g:if test="${!testInstance.specialTestComplete}">
                            <g:if test="${params.next}">
                                <g:actionSubmit action="specialresultsreadynext" value="${message(code:'fc.results.readynext')}"  tabIndex="2"/>
                            </g:if>
                        	<g:actionSubmit action="specialresultsready" value="${message(code:'fc.results.ready')}" tabIndex="3"/>
                        	<g:actionSubmit action="specialresultssave" value="${message(code:'fc.save')}" tabIndex="4"/>
                            <g:actionSubmit action="printspecialresults" value="${message(code:'fc.print')}" tabIndex="5"/>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="6"/>
                        </g:if>
                        <g:else>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="specialresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="2"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                            </g:else>
                        	<g:actionSubmit action="specialresultsreopen" value="${message(code:'fc.results.reopen')}" tabIndex="4"/>
                            <g:actionSubmit action="printspecialresults" value="${message(code:'fc.print')}" tabIndex="5"/>
                            <g:if test="${params.next}">
                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="6"/>
                            </g:if>
                        </g:else>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>