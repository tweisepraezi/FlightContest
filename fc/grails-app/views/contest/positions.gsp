<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.positions')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.positions')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <thead>
                                <tr>
                                	<th>${message(code:'fc.test.results.position')}</th>
                                   	<th>${message(code:'fc.crew')}</th>
                                   	<g:each var="task_instance" in="${Task.findAllByContest(contestInstance)}">
                                    	<th><a href="${createLink(controller:'contest')}/../../task/listresults/${task_instance.id}" >${task_instance.name()}</a></th>
	                                </g:each>
                                   	<th>${message(code:'fc.test.results.summary')}</th>
                            	</tr>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContest(contestInstance,[sort:'contestPosition'])}" status="i">
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                    	<g:if test="${crew_instance.disabled}">
                                    		<td>${message(code:'fc.disabled')}</td>
	                                    </g:if> <g:else>
		                                    <g:if test="${crew_instance.contestPosition}">
		                                        <td>${crew_instance.contestPosition}</td>
		                                    </g:if> <g:else>
		                                        <td>${message(code:'fc.test.results.position.none')}</td>
		                                    </g:else>
	                                    </g:else>
                                        <td><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}"/></td>
                                        <g:each var="task_instance" in="${Task.findAllByContest(contestInstance)}">
                                        	<td>${Test.findByCrewAndTask(crew_instance,task_instance).taskPenalties} ${message(code:'fc.points')} <a href="${createLink(controller:'test',action:'debriefing')}/${Test.findByCrewAndTask(crew_instance,task_instance).id}">${message(code:'fc.test.results.here')}</a></td>
		                                </g:each>
	                                    <td>${crew_instance.contestPenalties} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <g:actionSubmit action="calculatepositions" value="${message(code:'fc.results.calculatepositions')}" />
                        <g:actionSubmit action="printpositions" value="${message(code:'fc.print')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>