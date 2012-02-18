<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" importaction="${message(code:'fc.crew.import')}" printaction="${message(code:'fc.crew.print')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:form method="post" >
	            <table>
	                <thead>
	                    <tr>
	                    	<g:if test="${resultClasses}">
	                        	<th colspan="8" class="table-head">${message(code:'fc.crew.list')} (${activeCrewInstanceList.size()})</th>
	                        </g:if>
	                        <g:else>
	                        	<th colspan="7" class="table-head">${message(code:'fc.crew.list')} (${activeCrewInstanceList.size()})</th>
	                        </g:else>
	                    </tr>
	                    <tr>
	                        <th>${message(code:'fc.crew.name')}</th>
	                        <th>${message(code:'fc.crew.team')}</th>
	                        <th>${message(code:'fc.crew.aircraft')}</th>
	                        <th>${message(code:'fc.tas')}</th>
	                    	<g:if test="${resultClasses}">
		                        <th>${message(code:'fc.crew.resultclass')}</th>
		                    </g:if>
	                        <th/>
	                    </tr>
	                </thead>
	                <tbody>
	                    <g:each in="${crewInstanceList}" status="i" var="crewInstance">
	                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
	                            <g:set var="crew_id" value="selectedCrewID${crewInstance.id.toString()}"></g:set>
	                            <g:if test="${flash.selectedCrewIDs && (flash.selectedCrewIDs[crew_id] == 'on')}">
	                                <td><g:checkBox name="${crew_id}" value="${true}" /> ${crewInstance.viewpos+1} <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                            </g:if> <g:else>
	                                <td><g:checkBox name="${crew_id}" value="${false}" /> ${crewInstance.viewpos+1} <g:crew var="${crewInstance}" link="${createLink(controller:'crew',action:'edit')}"/></td>
	                            </g:else>
	                            <td><g:team var="${crewInstance.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
	                            <td><g:aircraft var="${crewInstance.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
	                            <td>${fieldValue(bean:crewInstance, field:'tas')}${message(code:'fc.knot')}</td>
		                    	<g:if test="${resultClasses}">
		                            <td><g:resultclass var="${crewInstance.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
		                        </g:if>
	                            <g:if test="${crewInstance.disabled}">
	                            	<td>${message(code:'fc.disabled')}</td>
	                            </g:if> <g:else>
                                    <td/>
	                            </g:else>
	                        </tr>
	                    </g:each>
	                </tbody>
	                <tfoot>
	                    <tr class="">
	                        <td><g:actionSubmit action="selectall" value="${message(code:'fc.selectall')}" /></td>
	                        <td><g:actionSubmit action="calculatesequence" value="${message(code:'fc.test.sequence.calculate')}" /></td>
	                    	<g:if test="${resultClasses}">
		                        <td colspan="4"><g:actionSubmit action="deletecrews" value="${message(code:'fc.crew.deletecrews')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></td>
		                    </g:if>
		                    <g:else>
		                        <td colspan="3"><g:actionSubmit action="deletecrews" value="${message(code:'fc.crew.deletecrews')}" onclick="return confirm('${message(code:'fc.areyousure')}');" /></td>
		                    </g:else>
	                    </tr>
	                    <tr class="join">
	                        <td><g:actionSubmit action="deselectall" value="${message(code:'fc.deselectall')}" /></td>
	                    	<g:if test="${resultClasses}">
		                        <td colspan="5"><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}" /> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}" /></td>
		                    </g:if>
		                    <g:else>
		                        <td colspan="4"><g:actionSubmit action="moveup" value="${message(code:'fc.test.moveup')}" /> <g:actionSubmit action="movedown" value="${message(code:'fc.test.movedown')}" /></td>
		                    </g:else>
	                    </tr>
	                </tfoot>
	            </table>
	        </g:form>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>