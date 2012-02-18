<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" importaction="${message(code:'fc.crew.import')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.edit')}</h2>
                <g:hasErrors bean="${crewInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${crewInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['crewReturnAction':crewReturnAction,'crewReturnController':crewReturnController,'crewReturnID':crewReturnID]}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.crew.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:crewInstance,field:'name')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.import.name')}:</label>
                                <br/>
                                <input type="text" id="mark" name="mark" value="${fieldValue(bean:crewInstance,field:'mark')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.team')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="name" from="${Team.findAllByContest(crewInstance.contest,[sort:'name'])}" name="team.id" value="${crewInstance?.team?.id}" noSelection="['null':'']" tabIndex="3"></g:select>
                            </p>
                            <g:if test="${crewInstance.contest.resultClasses}">
	                            <p>
	                                <label>${message(code:'fc.crew.resultclass')}:</label>
	                                <br/>
	                                <g:select optionKey="id" optionValue="name" from="${ResultClass.findAllByContest(crewInstance.contest,[sort:'name'])}" name="resultclass.id" value="${crewInstance?.resultclass?.id}" noSelection="['null':'']" tabIndex="4"></g:select>
	                            </p>
	                        </g:if>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.crew.aircraft')}</legend>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="registration" from="${Aircraft.findAllByContest(crewInstance.contest,[sort:'registration'])}" name="aircraft.id" value="${crewInstance?.aircraft?.id}" noSelection="['null':'']" tabIndex="5"></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="tas" name="tas" value="${fieldValue(bean:crewInstance,field:'tas')}" tabIndex="6"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="disabled" value="${crewInstance.disabled}" />
                                    <label>${message(code:'fc.disabled')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${crewInstance?.id}"/>
                        <input type="hidden" name="version" value="${crewInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="7"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="8"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="9"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>