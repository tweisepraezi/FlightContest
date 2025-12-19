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
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.contest.printteamresults')}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.contest.printteamresults')}</title>
    </head>
    <body>
        <h2><g:if test="${contestInstance.teamPrintSubtitle}">${contestInstance.teamPrintSubtitle}</g:if><g:else>${message(code:'fc.contest.printteamresults')}</g:else><g:if test="${contestInstance.IsTeamResultsProvisional(contestInstance.GetTeamResultSettings(),contestInstance.teamTaskResults)}"> [${message(code:'fc.provisional')}]</g:if></h2>
        <h3>${contestInstance.GetResultTitle(contestInstance.GetTeamResultSettings(),true)}</h3>
        <g:form>
            <table class="teamresultlist">
                <thead>
                    <tr>
                    	<th>${message(code:'fc.test.results.position.short')}</th>
                       	<th>${message(code:'fc.team')}</th>
                       	<th>${message(code:'fc.crew.list')}</th>
                       	<th>${message(code:'fc.test.results.summary')}</th>
                	</tr>
                </thead>
                <tbody>
                    <g:each var="team_instance" in="${Team.findAllByContest(contestInstance,[sort:'contestPosition'])}" status="i">
                   		<g:if test="${team_instance.IsActiveTeam()}">
                         <tr id="${team_instance.contestPosition}">
                             <td class="pos">${team_instance.contestPosition}</td>
                             <td class="team">${team_instance.name}</td>
                             <g:set var="team_provisional" value="${false}"/>
                             <td class="crews">
                             	<g:set var="crew_num" value="${new Integer(0)}"/>
                             	<g:each var="crew_instance" in="${Crew.findAllByTeamAndDisabledAndDisabledTeam(team_instance,false,false,[sort:'teamPenalties'])}">
                             		<g:if test="${crew_instance.IsActiveCrew(ResultFilter.Team) && (crew_instance.teamPenalties != -1) && (crew_num < contestInstance.teamCrewNum)}">
                              		<g:set var="crew_num" value="${crew_num+1}"/>
                                    <g:set var="test_provisional" value="${false}"/>
                                    <g:each var="task_instance" in="${contestInstance.GetResultTasks(contestInstance.teamTaskResults)}">
                                        <g:set var="test_instance" value="${Test.findByCrewAndTaskAndFlightTestAdditionalResult(crew_instance,task_instance,false)}"/>
                                        <g:if test="${test_instance}">
                                            <g:if test="${test_instance.IsTestResultsProvisional(contestInstance.GetTeamResultSettings())}">
                                                <g:set var="test_provisional" value="${true}"/>
                                                <g:set var="team_provisional" value="${true}"/>
                                            </g:if>
                                        </g:if>
                                    </g:each>
                                    <g:if test="${crew_num > 1}">, </g:if><g:if test="${contestInstance.teamPrintStartNum}">${crew_instance.startNum} - </g:if>${crew_instance.name} (${crew_instance.teamPenalties}<g:if test="${test_provisional}"> [${message(code:'fc.provisional')}]</g:if>)</g:if>
                             	</g:each>
                             </td>
                          <td class="teampenalties">${team_instance.contestPenalties} ${message(code:'fc.points')}<g:if test="${team_provisional}"> [${message(code:'fc.provisional')}]</g:if></td>
                         </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
			<g:if test="${contestInstance.teamPrintFooter}">
				<br/>
				<table class="resultfooter">
					<tbody>
						<g:each var="foot_line" in="${Tools.Split(contestInstance.teamPrintFooter,Defs.RESULTS_FOOTER_LINE_SEPARATOR)}" status="i">
							<tr id="line${i+1}">
								<g:if test="${!foot_line}">
									<td><br/></td>
								</g:if>
								<g:else>
									<g:each var="foot_col" in="${Tools.Split(foot_line,Defs.RESULTS_FOOTER_COLUMN_SEPARATOR)}" status="j">
										<td id="col${j+1}">${foot_col}</td>
									</g:each>
								</g:else>
							</tr>
						</g:each>
					</tbody>
				</table>
			</g:if>
        </g:form>
    </body>
</html>