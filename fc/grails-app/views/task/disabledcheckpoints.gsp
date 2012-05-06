<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.disabledcheckpoints')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.disabledcheckpoints')} - ${taskInstance.name()}</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}" >
                        <table>
                            <thead>
                            	<tr>
                                	<th>${message(code:'fc.number')}</th>
									<th>${message(code:'fc.title')}</th>
									<th>${message(code:'fc.aflos.checkpoint')}</th>
								</tr>
                            </thead>
                        	<tbody>
                        		<g:set var="cp" value="${taskInstance.disabledCheckPoints},"/> 
	                            <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(taskInstance.flighttest.route,[sort:"id"])}" status="i" >
	                            	<g:if test="${(coordroute_instance.type != CoordType.TO) && (coordroute_instance.type != CoordType.LDG)}">
	                            		<tr class="${(i % 2) == 0 ? '' : 'odd'}">
	                            			<td><g:checkBox name="${coordroute_instance.title()}" value="${cp.contains(coordroute_instance.title()+',')}" /> <label>${i}</label></td>
	                            			<td>${coordroute_instance.titleCode()}</td>
	                            			<td>${coordroute_instance.mark}</td>
	                            		</tr>
	                                </g:if>
	                            </g:each>
                        	</tbody>
                        </table>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savedisabledcheckpoints" value="${message(code:'fc.update')}" tabIndex="1"/>
                        <g:actionSubmit action="resetdisabledcheckpoints" value="${message(code:'fc.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="2"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>