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
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${aircraftInstance.GetNextAircraftID()}"/>
						<g:set var="prev_id" value="${aircraftInstance.GetPrevAircraftID()}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}*:</label>
                                <br/>
                                <input type="text" id="registration" name="registration" value="${fieldValue(bean:aircraftInstance,field:'registration')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.type')}:</label>
                                <br/>
                                <input type="text" id="type" name="type" value="${fieldValue(bean:aircraftInstance,field:'type')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.colour')}:</label>
                                <br/>
                                <input type="text" id="colour" name="colour" value="${fieldValue(bean:aircraftInstance,field:'colour')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <g:if test="${aircraftInstance.user1 || aircraftInstance.user2}">
	                        <fieldset>
	                        	<legend>${message(code:'fc.aircraft.usedby.crew')}</legend>
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
                        <g:if test="${Test.findByTaskAircraftAndCrewNotEqualAndCrewNotEqual(aircraftInstance,aircraftInstance.user1,aircraftInstance.user2)}">
                            <fieldset>
                                <legend>${message(code:'fc.aircraft.usedby.task')}</legend>
                                <p>
                                    <g:each var="test_instance" in="${Test.findAllByTaskAircraftAndCrewNotEqualAndCrewNotEqual(aircraftInstance,aircraftInstance.user1,aircraftInstance.user2)}">
                                        <br/>${test_instance.crew.name} (${test_instance.task.name()})
                                    </g:each>
                                </p>
                            </fieldset>
                        </g:if>
                        <input type="hidden" name="id" value="${aircraftInstance?.id}"/>
                        <input type="hidden" name="version" value="${aircraftInstance?.version}"/>
						<g:actionSubmit action="savesettings" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${next_id}">
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.aircraft.updatenext')}" tabIndex="${ti[0]++}"/>
							<g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.aircraft.gotonext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.aircraft.updatenext')}" disabled tabIndex="${ti[0]++}"/>
							<g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.aircraft.gotonext')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
						<g:if test="${prev_id}">
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.aircraft.gotoprev')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.aircraft.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:if test="${!Test.findByTaskAircraft(aircraftInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>