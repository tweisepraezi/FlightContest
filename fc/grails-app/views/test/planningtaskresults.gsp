<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Planningtask)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Planningtask)}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post">
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                    <g:if test="${testInstance.scannedPlanningTest}"> 
                                        <td style="width:1%;">
                                            <a href="${createLink(controller:'test',action:'planningtaskformimage',params:[testid:testInstance.id])}" target="_blank"><img src="${createLinkTo(dir:'images',file:'scanned.png')}"/></a>
                                        </td>
                                    </g:if>
                                    <td style="width:1%;">
                                        <a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
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
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:route var="${testInstance.planningtesttask.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:windtext var="${testInstance.planningtesttask.wind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:planningtaskTestResults t="${testInstance}"/>
                        <g:if test="${!testInstance.planningTestComplete}">
	                        <fieldset>
		                        <p>
		                        	<g:if test="${testInstance.GetPlanningTestPlanTooLatePoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="planningTestGivenTooLate" value="${testInstance.planningTestGivenTooLate}"/>
											<label>${message(code:'fc.planningtest.giventolate')}</label>
			    	                    </div>
			    	                </g:if>
		                        	<g:if test="${testInstance.GetPlanningTestExitRoomTooLatePoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="planningTestExitRoomTooLate" value="${testInstance.planningTestExitRoomTooLate}"/>
											<label>${message(code:'fc.planningtest.exitroomtolate')}</label>
			    	                    </div>
			    	                </g:if>
                                    <g:if test="${testInstance.GetPlanningTestForbiddenCalculatorsPoints() > 0}">
                                        <div>
                                            <g:checkBox name="planningTestForbiddenCalculators" value="${testInstance.planningTestForbiddenCalculators}"/>
                                            <label>${message(code:'fc.planningtest.forbiddencalculators')}</label>
                                        </div>
                                    </g:if>
		                        </p>
		                        <p>
	                                <label>${message(code:'fc.planningtest.otherpenalties')}* [${message(code:'fc.points')}]:</label>
		                            <br/>
			                        <input type="text" id="planningTestOtherPenalties" name="planningTestOtherPenalties" value="${fieldValue(bean:testInstance,field:'planningTestOtherPenalties')}" tabIndex="1"/>
			                    </p>
                            </fieldset>
                        </g:if>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningresults.legpenalties')}:</td>
                                    <td>${testInstance.planningTestLegPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <g:if test="${testInstance.planningTestGivenTooLate}">
                                	<tr>
                                    	<td class="detailtitle">${message(code:'fc.planningtest.giventolate')}:</td>
                                        <td>${testInstance.GetPlanningTestPlanTooLatePoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.planningTestExitRoomTooLate}">
                                	<tr>
                                    	<td class="detailtitle">${message(code:'fc.planningtest.exitroomtolate')}:</td>
                                        <td>${testInstance.GetPlanningTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>
		                            </tr>
                                </g:if>
                                <g:if test="${testInstance.planningTestForbiddenCalculators}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.planningtest.forbiddencalculators')}:</td>
                                        <td>${testInstance.GetPlanningTestForbiddenCalculatorsPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.planningTestOtherPenalties != 0}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.planningtest.otherpenalties')}:</td>
                                        <td>${testInstance.planningTestOtherPenalties} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <g:set var="points_class" value="points"/>
                                    <g:if test="${!testInstance.planningTestPenalties}">
                                        <g:set var="points_class" value="zeropoints"/>
                                    </g:if>
                                    <td class="${points_class}">${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
                                        <g:if test="${!testInstance.planningTestComplete}">
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="planningtaskresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="11"/>
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="12"/>
                                            </g:else>
                                            <g:if test="${testInstance.planningTestLegComplete}">
	                                            <g:if test="${params.next}">
	                                                <g:actionSubmit action="planningtaskresultsreadynext" value="${message(code:'fc.results.readynext')}"  tabIndex="13"/>
	                                            </g:if>
                                                <g:actionSubmit action="planningtaskresultsready" value="${message(code:'fc.results.ready')}" tabIndex="14"/>
					                        	<g:actionSubmit action="planningtaskresultssave" value="${message(code:'fc.save')}" tabIndex="15"/>
                                            </g:if>
				                            <g:if test="${!testInstance.scannedPlanningTest}">
				                                <g:actionSubmit action="planningtaskformimportcrew" value="${message(code:'fc.planningtesttask.importform')}" onclick="this.form.target='_self';return true;" tabIndex="106"/>
				                            </g:if>
				                            <g:else>
				                                <g:actionSubmit action="planningtaskformdeleteimagefile" value="${message(code:'fc.planningtesttask.deleteform')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="107"/>
				                            </g:else>
                                            <g:actionSubmit action="printplanningtaskresults" value="${message(code:'fc.print')}" tabIndex="16"/>
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="17"/>
                                            </g:if>
                                        </g:if>
                                        <g:else>
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="planningtaskresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="41"/>
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="42"/>
                                            </g:else>
                                            <g:actionSubmit action="planningtaskresultsreopen" value="${message(code:'fc.results.reopen')}" tabIndex="43"/>
                                            <g:actionSubmit action="printplanningtaskresults" value="${message(code:'fc.print')}" tabIndex="44"/>
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="45"/>
                                            </g:if>
                                        </g:else>
                                    </td>
                                    <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                                </tr>
                            </tfoot>
                        </table>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>