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
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.team.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:teamInstance,field:'name')}" tabIndex="1"/>
                            </p>
                        </fieldset>
                        <fieldset>
                        	<legend>${message(code:'fc.crew.list')}</legend>
                        	<p><g:each var="${crew_instance}" in="${Crew.findAllByTeam(teamInstance)}"><br/>${crew_instance.name}<g:if test="${crew_instance.disabled}"> (${message(code:'fc.disabled')})</g:if></g:each></p>
                        </fieldset>
                        <input type="hidden" name="id" value="${teamInstance?.id}"/>
                        <input type="hidden" name="version" value="${teamInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="2"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="3"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="4"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>