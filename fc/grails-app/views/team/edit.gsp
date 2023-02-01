<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.team.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="team" newaction="${message(code:'fc.team.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.team.edit')}</h2>
                <g:hasErrors bean="${teamInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${teamInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['teamReturnAction':teamReturnAction,'teamReturnController':teamReturnController,'teamReturnID':teamReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${teamInstance.GetNextTeamID()}"/>
						<g:set var="prev_id" value="${teamInstance.GetPrevTeamID()}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.team.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:teamInstance,field:'name')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                        	<legend>${message(code:'fc.crew.list')}</legend>
                        	<p>
                        	   <g:each var="${crew_instance}" in="${Crew.findAllByTeam(teamInstance,[sort:"id"])}"><br/>${crew_instance.name}
                        	       <g:if test="${crew_instance.disabled}"> (${message(code:'fc.disabled')})</g:if>
                        	       <g:else>
                        	           <g:if test="${crew_instance.disabledTeam}"> (${message(code:'fc.crew.disabledteam')})</g:if>
                        	           <g:if test="${crew_instance.disabledContest}"> (${message(code:'fc.crew.disabledcontest')})</g:if>
                        	       </g:else>
                        	   </g:each>
                        	</p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="disabled" value="${teamInstance.disabled}" />
                                    <label>${message(code:'fc.disabled')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${teamInstance?.id}"/>
                        <input type="hidden" name="version" value="${teamInstance?.version}"/>
						<g:actionSubmit action="savesettings" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${next_id}">
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.team.updatenext')}" tabIndex="${ti[0]++}"/>
							<g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.team.gotonext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.team.updatenext')}" disabled tabIndex="${ti[0]++}"/>
							<g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.team.gotonext')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
						<g:if test="${prev_id}">
                            <g:actionSubmit action="gotoprev" value="${message(code:'fc.team.gotoprev')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="gotoprev" value="${message(code:'fc.team.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>