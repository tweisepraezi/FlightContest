<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <g:set var="new_action" value="${message(code:'fc.crew.new')}"/>
        <g:set var="import_action" value="${message(code:'fc.crew.import')}"/>
        <g:set var="import_action2" value=""/>
        <g:if test="${BootStrap.global.IsLiveTrackingPossible() && contestInstance.liveTrackingContestID}">
            <g:set var="import_action2" value="${message(code:'fc.livetracking.teams.import')}"/>
        </g:if>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${new_action}" importaction="${import_action}" importaction2="${import_action2}" printsettings="${message(code:'fc.crew.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:form method="post" >
                <g:set var="ti" value="${[]+1}"/>
                <g:set var="columns" value="${7}"/>
                <g:if test="${resultClasses}">
                    <g:set var="columns" value="${columns+1}"/>
                </g:if>
	            <table>
	                <thead>
	                    <tr>
	                        <th colspan="${columns}" class="table-head">${message(code:'fc.crew.list')} (${activeCrewList.size()})</th>
	                        <th class="table-head"><a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a>&#x00A0;&#x00A0;&#x00A0;<a href="/fc/docs/help_${session.showLanguage}.html#create-crew-list" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></th>
	                    </tr>
	                    <tr>
                            <th>${message(code:'fc.crew.startnum')}</th>
	                        <th>${message(code:'fc.crew.name')}</th>
							<th>${message(code:'fc.test.listpos')}</th>
                            <th>${message(code:'fc.crew.email')}</th>
	                        <th>${message(code:'fc.team')}</th>
                            <g:if test="${resultClasses}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
	                        <th>${message(code:'fc.aircraft')}</th>
	                        <th>${message(code:'fc.tas')}</th>
                            <th>${message(code:'fc.crew.trackerid')}</th>
	                    </tr>
	                </thead>
	                <tbody>
						<g:set var="crew_pos" value="${0}"></g:set>
						<g:set var="last_tas" value="${1000}"></g:set>
						<g:set var="last_team_name" value=""></g:set>
						<g:set var="last_team_name2" value=""></g:set>
	                    <g:each var="crew_instance" in="${crewList}" status="i">
							<g:set var="pagebreak_class" value=""></g:set>
							<g:if test="${crew_instance.pageBreak}">
								<g:set var="pagebreak_class" value="pagebreak"></g:set>
							</g:if>
	                        <tr class="${(i % 2) == 0 ? 'odd' : ''} ${pagebreak_class}">
	                            <g:set var="crew_id" value="selectedCrewID${crew_instance.id.toString()}"></g:set>
                                <g:set var="crew_selected" value="${false}"></g:set>
	                            <g:if test="${flash.selectedCrewIDs && (flash.selectedCrewIDs[crew_id] == 'on')}">
                                    <g:set var="crew_selected" value="${true}"></g:set>
	                            </g:if> 
                                <td>
                                    <g:checkBox name="${crew_id}" value="${crew_selected}"/> ${crew_instance.startNum}
                                    <g:if test="${BootStrap.global.IsLiveTrackingPossible() && contestInstance.liveTrackingContestID && crew_instance.liveTrackingTeamID}" >
                                        <img src="${createLinkTo(dir:'images',file:'livetracking.svg')}" style="margin-left:0.5rem; height:0.7rem;"/>
                                        <g:if test="${crew_instance.liveTrackingDifferences}" >
                                            <a href="${createLink(controller:'crew',action:'livetracking_teamdifferencies')}/${crew_instance.id}"><img src="${createLinkTo(dir:'images',file:'team-different.svg')}" style="margin-left:0.2rem; height:0.7rem;"/></a>
                                        </g:if>
                                    </g:if>
                                </td>
                                <td><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}" /><g:if test="${crew_instance.disabled}"> (${message(code:'fc.disabled')})</g:if><g:if test="${crew_instance.IsIncreaseEnabled()}"> (${message(code:'fc.crew.increaseenabled.short',args:[crew_instance.GetIncreaseFactor()])})</g:if></td>
								<g:if test="${!crew_instance.disabled}">
									<g:set var="crew_pos" value="${crew_pos+1}"></g:set>
									<td>${crew_pos}</td>
								</g:if>
								<g:else>
									<td></td>
								</g:else>
                                <td>${fieldValue(bean:crew_instance, field:'email')}</td>
                                <g:if test="${crew_instance.team}">                          
                                    <g:set var="team_order_problem" value=""></g:set>
                                    <g:if test="${crew_instance.team?.name == last_team_name && !crew_instance.pageBreak}">
                                        <g:set var="team_order_problem" value="!!"></g:set>
                                    </g:if>
                                    <g:elseif test="${crew_instance.team?.name == last_team_name2 && !crew_instance.pageBreak}">
                                        <g:set var="team_order_problem" value="!"></g:set>
                                    </g:elseif>
                                    <td>
                                        <g:team var="${crew_instance.team}" link="${createLink(controller:'team',action:'edit')}"/>
                                        ${team_order_problem}
                                        <g:if test="${crew_instance.team.disabled}"> (${message(code:'fc.disabled')})</g:if>
                                        <g:elseif test="${crew_instance.disabledTeam}"> (${message(code:'fc.crew.disabledteam')})</g:elseif>
                                    </td>
                                </g:if>
                                <g:else>
                                    <td>-</td>
                                </g:else>
                                <g:if test="${resultClasses}">
                                    <g:if test="${crew_instance.resultclass}">
                                        <td><g:resultclass var="${crew_instance.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
	                                </g:if>
	                                <g:else>
	                                    <td class="errors">${message(code:'fc.crew.noclass')}</td>
	                                </g:else>
                                </g:if>
	                            <td><g:aircraft var="${crew_instance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/><g:if test="${crew_instance.aircraft?.user1 && crew_instance.aircraft?.user2}"> *</g:if></td>
                                <g:set var="tas_order_problem" value=""></g:set>
                                <g:if test="${crew_instance.tas > last_tas && !crew_instance.pageBreak}">
                                    <g:set var="tas_order_problem" value="!"></g:set>
                                </g:if>
	                            <td>${fieldValue(bean:crew_instance, field:'tas')}${message(code:'fc.knot')} ${tas_order_problem}<g:if test="${crew_instance.disabledContest}"> (${message(code:'fc.crew.disabledcontest')})</g:if></td>
                                <td>${fieldValue(bean:crew_instance, field:'trackerID')}</td>
	                        </tr>
                            <g:set var="last_tas" value="${crew_instance.tas}"></g:set>
                            <g:set var="last_team_name2" value="${last_team_name}"></g:set>
                            <g:set var="last_team_name" value="${crew_instance.team?.name}"></g:set>
	                    </g:each>
	                </tbody>
	                <tfoot>
                        <input type="hidden" name="id" value="${session.lastContest.id}"/>
	                    <tr class="">
	                        <td><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}" tabIndex="${ti[0]++}"/></td>
	                        <td><g:actionSubmit action="calculatesequence" value="${message(code:'fc.test.sequence.calculate')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/></td>
                            <td colspan="${columns-2}">
                                <g:actionSubmit action="deletecrews" value="${message(code:'fc.crew.deletecrews')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="sortstartnum" value="${message(code:'fc.crew.sortstartnum')}" tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="sortstartnumgaps" value="${message(code:'fc.crew.sortstartnum.gaps')}" tabIndex="${ti[0]++}"/>
                            </td>
		                    <td><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
	                    </tr>
	                    <tr class="join">
	                        <td><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}" tabIndex="${ti[0]++}"/></td>
	                        <td><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}" tabIndex="${ti[0]++}"/> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}" tabIndex="${ti[0]++}"/></td>
                            <td colspan="${columns-2}">
                                <g:if test="${BootStrap.global.IsLiveTrackingPossible() && contestInstance.liveTrackingContestID}">
                                    <g:actionSubmit action="livetracking_connectteams" value="${message(code:'fc.livetracking.teams.connect')}" tabIndex="${ti[0]++}"/>
                                    <g:actionSubmit action="livetracking_disconnectteams" value="${message(code:'fc.livetracking.teams.disconnect')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </g:if>
                            </td>
	                        <td/>
	                    </tr>
                        <tr>
                            <td colspan="${columns+1}">
                                <g:select from="${CrewCommands.GetValues(contestInstance.GetIncreaseValues() != "", contestInstance.crewPilotNavigatorDelimiter.trim() != "")}" optionValue="${{message(code:it.titleCode,args:[contestInstance.crewPilotNavigatorDelimiter])}}" value="${CrewCommands.SELECTCOMMAND}" name="crewcommand" tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="runcommand" value="${message(code:'fc.crew.runcommand')}" tabIndex="${ti[0]++}" />
                            </td>
                        </tr>
	                </tfoot>
	            </table>
	            <a name="end"/>
	        </g:form>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>