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
									<th>${message(code:'fc.title')}</th>
									<th>${message(code:'fc.aflos.checkpoint')}</th>
									<th>${message(code:'fc.task.disabledcheckpoints.timecheck')}</th>
                                    <th>${message(code:'fc.task.disabledcheckpoints.notfound')}</th>
                                    <th>${message(code:'fc.task.disabledcheckpoints.procedureturn')}</th>
                                    <th>${message(code:'fc.task.disabledcheckpoints.badcourse')}</th>
                                    <th>${message(code:'fc.task.disabledcheckpoints.minaltitude')}</th>
								</tr>
                            </thead>
                        	<tbody>
                        	    <g:set var="last_coordroute_instance" value="${null}"/>
	                            <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(taskInstance.flighttest.route,[sort:"id"])}" status="i" >
                                    <g:if test="${last_coordroute_instance}">
		                            	<g:if test="${last_coordroute_instance.type.IsCpCheckCoord()}">
		                            		<tr class="${(i % 2) == 0 ? 'odd' : ''}">
		                            			<td>${last_coordroute_instance.titleCode()}</td>
		                            			<td>${last_coordroute_instance.mark}</td>
                                                <g:if test="${last_coordroute_instance.type.IsCpTimeCheckCoord()}">
    	                                            <td><g:checkBox name="timecheck_${last_coordroute_instance.title()}" value="${taskInstance.disabledCheckPoints.contains(last_coordroute_instance.title()+',')}" /></td>
	                                                <td><g:checkBox name="notfound_${last_coordroute_instance.title()}" value="${taskInstance.disabledCheckPointsNotFound.contains(last_coordroute_instance.title()+',')}" /></td>
	                                            </g:if>
	                                            <g:else>
                                                    <td/>
                                                    <td/>
	                                            </g:else>
	                                            <g:if test="${last_coordroute_instance.type.IsProcedureTurnCoord() && coordroute_instance.planProcedureTurn}">
	                                                <td><g:checkBox name="procedureturn_${last_coordroute_instance.title()}" value="${taskInstance.disabledCheckPointsProcedureTurn.contains(last_coordroute_instance.title()+',')}" /></td>
	                                            </g:if>
	                                            <g:else>
	                                                <td/>
	                                            </g:else>
                                                <g:if test="${last_coordroute_instance.type.IsBadCourseCheckCoord()}">
                                                    <td><g:checkBox name="badcourse_${last_coordroute_instance.title()}" value="${taskInstance.disabledCheckPointsBadCourse.contains(last_coordroute_instance.title()+',')}" /></td>
                                                </g:if>
                                                <g:else>
                                                    <td/>
                                                </g:else>
                                                <g:if test="${last_coordroute_instance.type.IsAltitudeCheckCoord()}">
                                                    <td><g:checkBox name="minaltitude_${last_coordroute_instance.title()}" value="${taskInstance.disabledCheckPointsMinAltitude.contains(last_coordroute_instance.title()+',')}" /></td>
                                                </g:if>
                                                <g:else>
                                                    <td/>
                                                </g:else>
		                            		</tr>
		                                </g:if>
		                            </g:if>
                                    <g:set var="last_coordroute_instance" value="${coordroute_instance}"/>
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