<html>
    <head>
		<style type="text/css">
			@page {
                <g:if test="${contestInstance.contestPrintA3}">
                    <g:if test="${contestInstance.contestPrintLandscape}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
				    <g:if test="${contestInstance.contestPrintLandscape}">
				        size: A4 landscape;
				    </g:if>
				    <g:else>
				        size: A4;
				    </g:else> 
                </g:else> 
                @top-center {
                    content: "${message(code:'fc.contest.listresults')} - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.listresults')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.listresults')}<g:if test="${contestInstance.contestPrintSubtitle}"> - ${contestInstance.contestPrintSubtitle}</g:if><g:if test="${contestInstance.IsContestResultsProvisional(contestInstance.GetResultSettings(),contestInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <h3>${contestInstance.GetResultTitle(contestInstance.GetResultSettings(),true)}</h3>
                <div class="block" id="forms" >
                    <g:form>
                      	<br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                	<th>${message(code:'fc.test.results.position.short')}</th>
                                   	<th>${message(code:'fc.crew')}</th>
                                   	<th>${message(code:'fc.aircraft')}</th>
                                   	<th>${message(code:'fc.team')}</th>
                                   	<g:if test="${contestInstance.contestPrintTaskDetails}">
	                                   	<g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
	                                    	<th>${task_instance.bestOfName()}</th>
		                                </g:each>
	                                </g:if>
                                   	<th>${message(code:'fc.test.results.summary.short')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabledAndNoContestPositionAndContestPenaltiesNotEqual(contestInstance,false,false,-1,[sort:'contestPosition'])}">
                                    <tr class="even">
                                        <td>${crew_instance.contestPosition}</td>
                                        <td>${crew_instance.name}</td>
                                        <td>${crew_instance.aircraft.registration}</td>
                                        <g:if test="${crew_instance.team}">                          
                                            <td>${crew_instance.team.name}</td>
                                        </g:if>
                                        <g:else>
                                            <td>-</td>
                                        </g:else>
                                        <g:set var="test_provisional" value="${false}"/>
	                                    <g:if test="${contestInstance.contestPrintTaskDetails}">
	                                        <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
	                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
	                                        	<g:if test="${test_instance}">
	                                        	    <td>${test_instance.GetResultPenalties(contestInstance.GetResultSettings())}<g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"> [${message(code:'fc.provisional')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
	                                        	</g:if>
	                                            <g:else>
	                                               <td>-</td>
	                                            </g:else>
			                                </g:each>
                                        </g:if>
                                        <g:else>
                                            <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.contestTaskResults)}">
                                                <g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
                                                <g:if test="${test_instance}">
                                                    <g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetResultSettings())}"><g:set var="test_provisional" value="${true}"/></g:if>
                                                </g:if>
                                            </g:each>
                                        </g:else>
                                        <td>${crew_instance.contestPenalties}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>