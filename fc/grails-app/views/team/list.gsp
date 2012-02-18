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
            <table>
                <thead>
                    <tr>
                        <th colspan="4" class="table-head">${message(code:'fc.team.list')} (${teamInstanceList.size()})</th>
                    </tr>
                    <tr>
                       <th>${message(code:'fc.team.name')}</th>
                       <th>${message(code:'fc.crew')}</th>
                       <th>${message(code:'fc.crew.aircraft')}</th>
                       <th>${message(code:'fc.tas')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="team_instance" in="${teamInstanceList}" status="i" >
                    	<g:if test="${Crew.findByTeam(team_instance)}">
							<g:each var="crew_instance" in="${Crew.findAllByTeam(team_instance,[sort:'name'])}" status="j" >
		                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
	                               	<g:if test="${j==0}">
			                            <td><g:team var="${team_instance}" link="${createLink(controller:'team',action:'edit')}"/></td>
	                                </g:if>
	                                <g:else>
	                                	<td/>
	                                </g:else>
	                                <td><g:crew var="${crew_instance}" link="${createLink(controller:'crew',action:'edit')}"/></td></td>
	                                <td><g:aircraft var="${crew_instance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td></td>
	                                <td>${fieldValue(bean:crew_instance, field:'tas')}${message(code:'fc.knot')}</td>
		                        </tr>
	                        </g:each>
	                	</g:if>
	                	<g:else>
	                		<tr class="${(i % 2) == 0 ? 'odd' : ''}">
	                			<td><g:team var="${team_instance}" link="${createLink(controller:'team',action:'edit')}"/></td>
	                			<td/>
	                			<td/>
	                			<td/>
	                        </tr>
	                	</g:else>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>