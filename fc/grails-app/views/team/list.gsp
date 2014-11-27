<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.team.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="team" newaction="${message(code:'fc.team.new')}" printaction="${message(code:'fc.team.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:form method="post" >
	            <table>
	                <thead>
	                    <tr>
                            <g:if test="${resultClasses}">
                                <th colspan="6" class="table-head">${message(code:'fc.team.list')} (${teamInstanceList.size()})</th>
                            </g:if>
                            <g:else>
                                <th colspan="5" class="table-head">${message(code:'fc.team.list')} (${teamInstanceList.size()})</th>
                            </g:else>
	                    </tr>
	                    <tr>
	                       <th>${message(code:'fc.team.name')}</th>
	                       <th>${message(code:'fc.crew')}</th>
                            <g:if test="${resultClasses}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
	                       <th>${message(code:'fc.aircraft')}</th>
	                       <th>${message(code:'fc.tas')}</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <g:each var="team_instance" in="${teamInstanceList}" status="i" >
	                    	<g:if test="${Crew.findByTeam(team_instance)}">
								<g:each var="crew_instance" in="${Crew.findAllByTeam(team_instance,[sort:'name'])}" status="j" >
			                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
		                               	<g:if test="${j==0}">
		                                    <g:set var="team_id" value="selectedTeamID${team_instance.id.toString()}"></g:set>
		                                    <g:if test="${flash.selectedTeamIDs && (flash.selectedTeamIDs[team_id] == 'on')}">
		                                        <td><g:checkBox name="${team_id}" value="${true}"/> <g:team var="${team_instance}" link="${createLink(controller:'team',action:'edit')}"/><g:if test="${team_instance.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
		                                    </g:if> <g:else>
		                                        <td><g:checkBox name="${team_id}" value="${false}"/> <g:team var="${team_instance}" link="${createLink(controller:'team',action:'edit')}"/><g:if test="${team_instance.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
		                                    </g:else>
		                                </g:if>
		                                <g:else>
		                                	<td/>
		                                </g:else>
		                                <td><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${crew_instance.disabled}"> (${message(code:'fc.disabled')})</g:if><g:elseif test="${crew_instance.disabledTeam}"> (${message(code:'fc.crew.disabledteam')})</g:elseif></td>
		                                <g:if test="${resultClasses}">
		                                    <g:if test="${crew_instance.resultclass}">                          
		                                        <td><g:resultclass var="${crew_instance.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
		                                    </g:if>
		                                    <g:else>
		                                        <td>-</td>
		                                    </g:else>
		                                </g:if>
		                                <td><g:aircraft var="${crew_instance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
		                                <td>${fieldValue(bean:crew_instance, field:'tas')}${message(code:'fc.knot')}</td>
			                        </tr>
		                        </g:each>
		                	</g:if>
		                	<g:else>
		                		<tr class="${(i % 2) == 0 ? 'odd' : ''}">
	                                <g:set var="team_id" value="selectedTeamID${team_instance.id.toString()}"></g:set>
	                                <g:if test="${flash.selectedTeamIDs && (flash.selectedTeamIDs[team_id] == 'on')}">
	                                    <td><g:checkBox name="${team_id}" value="${true}"/> <g:team var="${team_instance}" link="${createLink(controller:'team',action:'edit')}"/><g:if test="${team_instance.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
	                                </g:if> <g:else>
	                                    <td><g:checkBox name="${team_id}" value="${false}"/> <g:team var="${team_instance}" link="${createLink(controller:'team',action:'edit')}"/><g:if test="${team_instance.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
	                                </g:else>
		                			<td/>
                                    <g:if test="${resultClasses}">
                                        <td/>
                                    </g:if>
		                			<td/>
	                                <td/>
		                        </tr>
		                	</g:else>
	                    </g:each>
	                </tbody>
	                <tfoot>
	                    <tr class="">
	                        <td><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}" /></td>
                            <g:if test="${resultClasses}">
    	                        <td colspan="4"><g:actionSubmit action="deleteteams" value="${message(code:'fc.team.deleteteams')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></td>
    	                    </g:if>
    	                    <g:else>
                                <td colspan="3"><g:actionSubmit action="deleteteams" value="${message(code:'fc.team.deleteteams')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></td>
    	                    </g:else>
	                    </tr>
	                    <tr class="join">
	                        <td><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}" /></td>
                            <g:if test="${resultClasses}">
    	                        <td colspan="4"><g:actionSubmit action="disableteams" value="${message(code:'fc.team.disableteams')}"/><g:actionSubmit action="enableteams" value="${message(code:'fc.team.enableteams')}"/></td>
                            </g:if>
                            <g:else>
                                <td colspan="3"><g:actionSubmit action="disableteams" value="${message(code:'fc.team.disableteams')}"/><g:actionSubmit action="enableteams" value="${message(code:'fc.team.enableteams')}"/></td>
                            </g:else>
	                    </tr>
	                </tfoot>
	            </table>
            </g:form>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>