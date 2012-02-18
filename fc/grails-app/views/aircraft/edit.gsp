<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aircraft.edit')}</h2>
                <g:hasErrors bean="${aircraftInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${aircraftInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['aircraftReturnAction':aircraftReturnAction,'aircraftReturnController':aircraftReturnController,'aircraftReturnID':aircraftReturnID]}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}*:</label>
                                <br/>
                                <input type="text" id="registration" name="registration" value="${fieldValue(bean:aircraftInstance,field:'registration')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.type')}:</label>
                                <br/>
                                <input type="text" id="type" name="type" value="${fieldValue(bean:aircraftInstance,field:'type')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.colour')}:</label>
                                <br/>
                                <input type="text" id="colour" name="colour" value="${fieldValue(bean:aircraftInstance,field:'colour')}" tabIndex="3"/>
                            </p>
                        </fieldset>
                        <g:if test="${aircraftInstance.user1 || aircraftInstance.user2}">
	                        <fieldset>
	                        	<legend>${message(code:'fc.crew.list')}</legend>
	                        	<p>
	                        		<g:if test="${aircraftInstance.user1}">
	                        			<br/>${aircraftInstance.user1.name}
	                        			<g:if test="${aircraftInstance.user1.disabled}"> (${message(code:'fc.disabled')})</g:if>
	                        		</g:if>
	                        		<g:if test="${aircraftInstance.user2}">
	                        			<br/>${aircraftInstance.user2.name}
	                        			<g:if test="${aircraftInstance.user2.disabled}"> (${message(code:'fc.disabled')})</g:if>
	                        		</g:if>
	                        	</p>
	                        </fieldset>
                        </g:if>
                        <input type="hidden" name="id" value="${aircraftInstance?.id}"/>
                        <input type="hidden" name="version" value="${aircraftInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="4"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="5"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="6"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>