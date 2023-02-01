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
						<g:set var="ti" value="${[]+1}" />
						<g:set var="next_id" value="${testInstance.GetNextTestID(ResultType.Special,session)}"/>
						<g:set var="prev_id" value="${testInstance.GetPrevTestID(ResultType.Special,session)}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                    <td style="width:1%;"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a></td>
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
                            </tbody>
                        </table>
                        <g:if test="${!testInstance.specialTestComplete}">
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.specialresults.results')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="specialTestPenalties" name="specialTestPenalties" value="${fieldValue(bean:testInstance,field:'specialTestPenalties')}" tabIndex="${ti[0]++}"/>
	                            </p>
                                <script>
									$('#specialTestPenalties').select();
								</script>
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
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
				                        <g:if test="${!testInstance.specialTestComplete}">
				                        	<g:actionSubmit action="specialresultssave" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
				                            <g:if test="${next_id}">
				                                <g:actionSubmit action="specialresultsreadynext" value="${message(code:'fc.results.readynext')}" tabIndex="${ti[0]++}"/>
				                            </g:if>
											<g:else>
												<g:actionSubmit action="specialresultsreadynext" value="${message(code:'fc.results.readynext')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
				                        	<g:actionSubmit action="specialresultsready" value="${message(code:'fc.results.ready')}" tabIndex="${ti[0]++}"/>
				                            <g:if test="${next_id}">
				                                <g:actionSubmit action="specialresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="${ti[0]++}"/>
				                            </g:if>
											<g:else>
												<g:actionSubmit action="specialresultsgotonext" value="${message(code:'fc.results.gotonext')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
											<g:if test="${prev_id}">
												<g:actionSubmit action="specialresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" tabIndex="${ti[0]++}"/>
											</g:if>
											<g:else>
												<g:actionSubmit action="specialresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
				                            <g:actionSubmit action="printspecialresults" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
			                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
				                        </g:if>
				                        <g:else>
				                            <g:if test="${next_id}">
				                                <g:actionSubmit action="specialresultsgotonext" value="${message(code:'fc.results.gotonext')}" tabIndex="${ti[0]++}"/>
				                            </g:if>
				                            <g:else>
				                                <g:actionSubmit action="specialresultsgotonext" value="${message(code:'fc.results.gotonext')}" disabled tabIndex="${ti[0]++}"/>
				                            </g:else>
											<g:if test="${prev_id}">
												<g:actionSubmit action="specialresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" tabIndex="${ti[0]++}"/>
											</g:if>
											<g:else>
												<g:actionSubmit action="specialresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
											</g:else>
				                        	<g:actionSubmit action="specialresultsreopen" value="${message(code:'fc.results.reopen')}" tabIndex="${ti[0]++}"/>
				                            <g:actionSubmit action="printspecialresults" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
			                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
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