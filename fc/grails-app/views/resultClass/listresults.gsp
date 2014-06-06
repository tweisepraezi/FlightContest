<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${resultclassInstance.GetListTitle('fc.contest.printresults')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" contestevaluation="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${resultclassInstance.GetListTitle('fc.contest.printresults')}<g:if test="${resultclassInstance.IsClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <div class="block" id="forms" >
                    <g:form params="${['positionsReturnAction':positionsReturnAction,'positionsReturnController':positionsReturnController,'positionsReturnID':positionsReturnID]}" >
                        <table>
                            <tbody>
                            	<tr>
				                	<td>${resultclassInstance.contest.GetResultTitle(resultclassInstance.GetClassResultSettings(),false)}</td>
				                </tr>
				            </tbody>
				        </table>
                        <table>
                            <thead>
                                <tr>
                                	<th>${message(code:'fc.test.results.position')}</th>
                                   	<th>${message(code:'fc.crew')}</th>
                                   	<th>${message(code:'fc.aircraft')}</th>
                                   	<th>${message(code:'fc.team')}</th>
                                   	<g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
                                    	<th><a href="${createLink(controller:'contest')}/../../task/listresults/${task_instance.id}" >${task_instance.bestOfName()}</a></th>
	                                </g:each>
                                   	<th>${message(code:'fc.test.results.summary')}</th>
                            	</tr>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContest(resultclassInstance.contest,[sort:'classPosition'])}" status="i">
                                	<g:if test="${crew_instance.resultclass == resultclassInstance}">
	                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
	                                    	<g:if test="${crew_instance.disabled}">
	                                    		<td class="position">${message(code:'fc.disabled')}</td>
		                                    </g:if> 
		                                    <g:else>
			                                    <g:if test="${crew_instance.classPosition}">
			                                        <td class="position">${crew_instance.classPosition}</td>
			                                    </g:if> 
			                                    <g:else>
			                                        <td class="position">${message(code:'fc.test.results.position.none')}</td>
			                                    </g:else>
		                                    </g:else>
	                                        <td class="positioncrew"><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                                        <td><g:if test="${crew_instance.aircraft}"><g:aircraft var="${crew_instance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
                                            <g:if test="${crew_instance.team}">                          
	                                           <td><g:team var="${crew_instance.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
	                                        </g:if>
	                                        <g:else>
	                                            <td>-</td>
	                                        </g:else>
	                                        
	                                        <g:set var="test_provisional" value="${false}"/>
	                                        <g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
	                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
	                                        	<g:if test="${test_instance}">
	                                        	    <td>${test_instance.GetResultPenalties(resultclassInstance.GetClassResultSettings())} ${message(code:'fc.points')}<g:if test="${test_instance.IsTestClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance)}"> [${message(code:'fc.provisional')}]<g:set var="test_provisional" value="${true}"/></g:if> <a href="${createLink(controller:'test',action:'crewresults')}/${test_instance.id}">${message(code:'fc.test.results.here')}</a></td>
	                                        	</g:if>
	                                        	<g:else>
	                                        	    <td>-</td>
	                                        	</g:else>
			                                </g:each>
			                                
		                                    <td class="positionpenalties">${crew_instance.contestPenalties} ${message(code:'fc.points')}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
	                                    </tr>
	                            	</g:if>
                                </g:each>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${resultclassInstance.id}" />
                        <g:actionSubmit action="editresultsettings" value="${message(code:'fc.resultclass.resultsettings')}" tabIndex="1"/>
                        <g:actionSubmit action="calculatepositions" value="${message(code:'fc.results.calculatepositions')}" tabIndex="2"/>
                        <g:actionSubmit action="printresults" value="${message(code:'fc.print')}" tabIndex="3"/>
                    </g:form>
                </div>
            </div>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>