<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" importaction="${message(code:'fc.crew.import')}" printsettings="${message(code:'fc.crew.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:form method="post" >
	            <table>
	                <thead>
	                    <tr>
	                    	<g:if test="${resultClasses}">
	                        	<th colspan="8" class="table-head">${message(code:'fc.crew.list')} (${activeCrewList.size()})</th>
	                        </g:if>
	                        <g:else>
	                        	<th colspan="7" class="table-head">${message(code:'fc.crew.list')} (${activeCrewList.size()})</th>
	                        </g:else>
	                    </tr>
	                    <tr>
                            <th>${message(code:'fc.crew.startnum')}</th>
	                        <th>${message(code:'fc.crew.name')}</th>
	                        <th>${message(code:'fc.team')}</th>
                            <g:if test="${resultClasses}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
	                        <th>${message(code:'fc.aircraft')}</th>
	                        <th>${message(code:'fc.tas')}</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <g:each in="${crewList}" var="crew_instance" status="i">
	                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
	                            <g:set var="crew_id" value="selectedCrewID${crew_instance.id.toString()}"></g:set>
	                            <g:if test="${flash.selectedCrewIDs && (flash.selectedCrewIDs[crew_id] == 'on')}">
	                                <td><g:checkBox name="${crew_id}" value="${true}" /> ${crew_instance.startNum}</td>
	                            </g:if> 
	                            <g:else>
	                                <td><g:checkBox name="${crew_id}" value="${false}" /> ${crew_instance.startNum}</td>
	                            </g:else>
                                <td><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${crew_instance.disabled}"> (${message(code:'fc.disabled')})</g:if></td>
                                <g:if test="${crew_instance.team}">                          
                                    <td>
                                        <g:team var="${crew_instance.team}" link="${createLink(controller:'team',action:'edit')}"/>
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
	                                    <td>-</td>
	                                </g:else>
                                </g:if>
	                            <td><g:aircraft var="${crew_instance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/><g:if test="${crew_instance.aircraft?.user1 && crew_instance.aircraft?.user2}"> *</g:if></td>
	                            <td>${fieldValue(bean:crew_instance, field:'tas')}${message(code:'fc.knot')}<g:if test="${crew_instance.disabledContest}"> (${message(code:'fc.crew.disabledcontest')})</g:if></td>
	                        </tr>
	                    </g:each>
	                </tbody>
	                <tfoot>
	                    <tr class="">
	                        <td><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}" /></td>
	                        <td><g:actionSubmit action="calculatesequence" value="${message(code:'fc.test.sequence.calculate')}" /></td>
	                    	<g:if test="${resultClasses}">
		                        <td colspan="5">
		                            <g:actionSubmit action="deletecrews" value="${message(code:'fc.crew.deletecrews')}" onclick="return confirm('${message(code:'fc.areyousure')}');"/>
		                        </td>
		                    </g:if>
		                    <g:else>
		                        <td colspan="4">
		                            <g:actionSubmit action="deletecrews" value="${message(code:'fc.crew.deletecrews')}" onclick="return confirm('${message(code:'fc.areyousure')}');"/>
		                        </td>
		                    </g:else>
	                    </tr>
	                    <tr class="join">
	                        <td><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}" /></td>
	                        <td><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}" /> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}" /></td>
	                    	<g:if test="${resultClasses}">
		                        <td colspan="5"/>
		                    </g:if>
		                    <g:else>
		                        <td colspan="4"/>
		                    </g:else>
	                    </tr>
                        <tr>
                            <g:if test="${resultClasses}">
                                <td colspan="7">
                                    <g:select from="${CrewCommands.values()}" optionValue="${{message(code:it.titleCode)}}" value="${CrewCommands.SELECTCOMMAND}" name="crewcommand" tabIndex="1"/>
                                    <g:actionSubmit action="runcommand" value="${message(code:'fc.crew.runcommand')}" tabIndex="2" />
                                </td>
                            </g:if>
                            <g:else>
                                <td colspan="6">
                                    <g:select from="${CrewCommands.values()}" optionValue="${{message(code:it.titleCode)}}" value="${CrewCommands.SELECTCOMMAND}" name="crewcommand" tabIndex="1"/>
                                    <g:actionSubmit action="runcommand" value="${message(code:'fc.crew.runcommand')}" tabIndex="2" />
                                </td>
                            </g:else>
                        </tr>
	                </tfoot>
	            </table>
	        </g:form>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>