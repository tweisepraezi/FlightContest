<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${contestInstance.GetListTitle(ResultFilter.Contest,'fc.contest.printresults')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" contestevaluation="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${contestInstance.GetListTitle(ResultFilter.Contest,'fc.contest.printresults')}<g:if test="${contestInstance.IsContestResultsProvisional(contestInstance.GetResultSettings(),contestInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <div class="block" id="forms" >
                    <g:form params="${['positionsReturnAction':positionsReturnAction,'positionsReturnController':positionsReturnController,'positionsReturnID':positionsReturnID]}" >
                        <table>
                            <tbody>
                            	<tr>
				                	<td>${contestInstance.GetResultTitle(contestInstance.GetResultSettings(),false)}</td>
				                </tr>
				            </tbody>
				        </table>
                        <table>
                            <thead>
                                <tr>
                                	<th>${message(code:'fc.test.results.position')}</th>
                                   	<th>${message(code:'fc.crew')}</th>
		                            <g:if test="${resultClasses}">
		                                <th>${message(code:'fc.resultclass')}</th>
		                            </g:if>
                                   	<th>${message(code:'fc.aircraft')}</th>
                                   	<th>${message(code:'fc.team')}</th>
                                   	<g:each var="task_instance1" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
                                    	<th><a href="${createLink(controller:'contest')}/../../task/listresults/${task_instance1.id}" >${task_instance1.bestOfName()}</a></th>
	                                </g:each>
                                   	<th>${message(code:'fc.test.results.summary')}</th>
                            	</tr>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContest(contestInstance,[sort:'contestPosition'])}" status="i">
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                    	<g:if test="${crew_instance.disabled || crew_instance.noContestPosition}">
                                    		<td class="position">${message(code:'fc.disabled')}</td>
	                                    </g:if> <g:else>
		                                    <g:if test="${crew_instance.contestPosition}">
		                                        <td class="position">${crew_instance.contestPosition}<g:if test="${crew_instance.contestEqualPosition}"> <a href="${createLink(controller:'contest',action:'addposition',params:[crewid:crew_instance.id])}">+</a></g:if><g:if test="${crew_instance.contestAddPosition > 0}"> <a href="${createLink(controller:'contest',action:'subposition',params:[crewid:crew_instance.id])}">-</a></g:if></td>
		                                    </g:if> <g:else>
		                                        <td class="position">${message(code:'fc.test.results.position.none')}</td>
		                                    </g:else>
	                                    </g:else>
                                        <td class="positioncrew"><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}"/></td>
                                        <g:if test="${resultClasses}">
                                            <g:if test="${crew_instance.resultclass}">                          
                                                <td><g:resultclass var="${crew_instance.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
                                            </g:if>
                                            <g:else>
                                                <td>-</td>
                                            </g:else>
                                        </g:if>
                                        <td><g:aircraft var="${crew_instance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                                        <g:if test="${crew_instance.team}">                          
                                            <td><g:team var="${crew_instance.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
		                                </g:if>
		                                <g:else>
		                                    <td>-</td>
		                                </g:else>
                                        <g:set var="test_provisional" value="${false}"/>
                                        <g:set var="test_disabled" value="${false}"/>
                                        <g:each var="task_instance2" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance2)}"/>
                                        	<g:if test="${test_instance}">
                                                <g:if test="${!test_instance.disabledCrew}">
                                        	        <td>${test_instance.GetResultPenalties(contestInstance.GetResultSettings())} ${message(code:'fc.points')}<g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]<g:set var="test_provisional" value="${true}"/></g:if> <a href="${createLink(controller:'test',action:'crewresults')}/${test_instance.id}">${message(code:'fc.test.results.here')}</a></td>
                                        	    </g:if>
                                        	    <g:else>
                                                    <g:set var="test_disabled" value="${true}"/>
                                        	        <td>-</td>
                                        	    </g:else>
                                        	</g:if>
                                        	<g:else>
                                        	   <td>-</td>
                                        	</g:else>
		                                </g:each>
		                                <g:if test="${!test_disabled}">
	                                        <td class="positionpenalties">${crew_instance.contestPenalties} ${message(code:'fc.points')}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>-</td>
	                                    </g:else>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <g:actionSubmit action="editresultsettings" value="${message(code:'fc.contest.resultsettings')}" onclick="this.form.target='_self';return true;" tabIndex="1"/>
                        <g:actionSubmit action="calculatepositions" value="${message(code:'fc.results.calculatepositions')}" onclick="this.form.target='_self';return true;" tabIndex="2"/>
                        <g:actionSubmit action="printresults" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="3"/>
                        <g:actionSubmit action="liveview" value="${message(code:'fc.liveview')}" onclick="this.form.target='_blank';return true;" tabIndex="4"/>
                    </g:form>
                </div>
            </div>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>