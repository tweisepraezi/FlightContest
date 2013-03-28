<html>
    <head>
		<style type="text/css">
			@page {
                <g:if test="${contestInstance.teamPrintA3}">
                    <g:if test="${contestInstance.teamPrintLandscape}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
	                <g:if test="${contestInstance.teamPrintLandscape}">
	                    size: A4 landscape;
	                </g:if>
	                <g:else>
	                    size: A4;
	                </g:else> 
                </g:else> 
                @top-center {
                    content: "${message(code:'fc.contest.listteamresults')} - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.listteamresults')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.listteamresults')}<g:if test="${contestInstance.teamPrintSubtitle}"> - ${contestInstance.teamPrintSubtitle}</g:if><g:if test="${contestInstance.IsTeamResultsProvisional(contestInstance.GetTeamResultSettings(),contestInstance.teamTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
                <h3>${contestInstance.GetResultTitle(contestInstance.GetTeamResultSettings(),true)}</h3>
                <div class="block" id="forms" >
                    <g:form>
                      	<br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                	<th>${message(code:'fc.test.results.position.short')}</th>
                                   	<th>${message(code:'fc.team')}</th>
                                   	<th>${message(code:'fc.crew.list')}</th>
                                   	<th>${message(code:'fc.test.results.summary.short')}</th>
                            	</tr>
                            </thead>
                            <tbody>
                                <g:each var="team_instance" in="${Team.findAllByContest(contestInstance,[sort:'contestPosition'])}" status="i">
                               		<g:if test="${team_instance.IsActiveTeam()}">
	                                    <tr class="even">
	                                        <td>${team_instance.contestPosition}</td>
	                                        <td>${team_instance.name}</td>
	                                        <g:set var="team_provisional" value="${false}"/>
	                                        <td>
	                                        	<g:set var="crew_num" value="${new Integer(0)}"/>
	                                        	<g:each var="crew_instance" in="${Crew.findAllByTeamAndDisabled(team_instance,false,[sort:'teamPenalties'])}">
	                                        		<g:if test="${crew_instance.IsActiveCrew(ResultFilter.Team) && (crew_instance.teamPenalties != -1) && (crew_num < contestInstance.teamCrewNum)}">
		                                        		<g:set var="crew_num" value="${crew_num+1}"/>
				                                        <g:set var="test_provisional" value="${false}"/>
				                                        <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.teamTaskResults)}">
				                                        	<g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
				                                        	<g:if test="${test_instance}">
					                                        	<g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetTeamResultSettings())}">
					                                        		<g:set var="test_provisional" value="${true}"/>
							                                        <g:set var="team_provisional" value="${true}"/>
					                                        	</g:if>
					                                        </g:if>
						                                </g:each>
	                                        			<g:if test="${crew_num > 1}">, </g:if>${crew_instance.name} (${crew_instance.teamPenalties}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if>)
	                                        		</g:if>
	                                        	</g:each>
	                                        </td>
		                                    <td>${team_instance.contestPenalties} ${message(code:'fc.points')}<g:if test="${team_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
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