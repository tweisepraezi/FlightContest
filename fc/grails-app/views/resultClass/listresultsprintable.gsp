<html>
    <head>
		<style type="text/css">
			@page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                @top-center {
                    content: "${resultclassInstance.GetPrintTitle2('fc.contest.listresults')} - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${resultclassInstance.GetPrintTitle('fc.contest.listresults')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${resultclassInstance.GetPrintTitle('fc.contest.listresults')}<g:if test="${resultclassInstance.contestPrintSubtitle}"> - ${resultclassInstance.contestPrintSubtitle}</g:if><g:if test="${resultclassInstance.IsClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance.contestTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <h3>${resultclassInstance.contest.GetResultTitle(resultclassInstance.GetClassResultSettings(),true)}</h3>
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
                                    <g:if test="${resultclassInstance.contestPrintTaskDetails}">
	                                   	<g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
	                                    	<th>${task_instance.bestOfName()}</th>
		                                </g:each>
		                            </g:if>
                                   	<th>${message(code:'fc.test.results.summary.short')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabledAndNoClassPositionAndContestPenaltiesNotEqual(resultclassInstance.contest,false,false,-1,[sort:'classPosition'])}">
                                	<g:if test="${crew_instance.resultclass == resultclassInstance}">
	                                    <tr class="even">
	                                        <td>${crew_instance.classPosition}</td>
	                                        <td>${crew_instance.name}</td>
	                                        <td><g:if test="${crew_instance.aircraft}">${crew_instance.aircraft.registration}</g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
                                            <g:if test="${crew_instance.team}">                          
	                                           <td><g:if test="${crew_instance.team}">${crew_instance.team.name}</g:if></td>
	                                        </g:if>
	                                        <g:else>
	                                            <td>-</td>
	                                        </g:else>
	                                        <g:set var="test_provisional" value="${false}"/>
                                            <g:if test="${resultclassInstance.contestPrintTaskDetails}">
		                                        <g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
		                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
		                                        	<g:if test="${test_instance}">
		                                        	    <td>${test_instance.GetResultPenalties(resultclassInstance.GetClassResultSettings())}<g:if test="${test_instance.IsTestClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance)}"> [${message(code:'fc.provisional')}]<g:set var="test_provisional" value="${true}"/></g:if></td>
		                                        	</g:if>
		                                        	<g:else>
		                                        	    <td>-</td>
		                                        	</g:else>
				                                </g:each>
                                            </g:if>
                                            <g:else>
                                                <g:each var="task_instance" in="${resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults)}">
                                                    <g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
                                                    <g:if test="${test_instance}">
                                                        <g:if test="${test_instance.IsTestClassResultsProvisional(resultclassInstance.GetClassResultSettings(),resultclassInstance)}"><g:set var="test_provisional" value="${true}"/></g:if>
                                                    </g:if>
                                                </g:each>
                                            </g:else>
	                                        <td>${crew_instance.contestPenalties}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
	                                    </tr>
	                            	</g:if>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>