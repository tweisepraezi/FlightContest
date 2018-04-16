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
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.crew.startnum')}*:</label>
                                <br/>
                                <input type="text" id="startNum" name="startNum" value="${fieldValue(bean:crewInstance,field:'startNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:crewInstance,field:'name')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.email')} (${message(code:'fc.email.more')}):</label>
                                <br/>
                                <input type="text" id="email" name="email" value="${fieldValue(bean:crewInstance,field:'email')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.team')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="name" from="${Team.findAllByContest(crewInstance.contest,[sort:'name'])}" name="team.id" value="${crewInstance?.team?.id}" noSelection="['null':'']" tabIndex="${ti[0]++}"></g:select>
                            </p>
                            <g:if test="${crewInstance.contest.resultClasses}">
	                            <p>
	                                <label>${message(code:'fc.resultclass')}:</label>
	                                <br/>
	                                <g:select optionKey="id" optionValue="name" from="${ResultClass.findAllByContest(crewInstance.contest,[sort:'name'])}" name="resultclass.id" value="${crewInstance?.resultclass?.id}" noSelection="['null':'']" tabIndex="${ti[0]++}"></g:select>
	                            </p>
	                        </g:if>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.aircraft')}</legend>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="registration" from="${Aircraft.findAllByContest(crewInstance.contest,[sort:'registration'])}" name="aircraft.id" value="${crewInstance?.aircraft?.id}" noSelection="['null':'']" tabIndex="${ti[0]++}"></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="tas" name="tas" value="${fieldValue(bean:crewInstance,field:'tas')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="disabled" value="${crewInstance.disabled}" />
                                    <label>${message(code:'fc.disabled')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="disabledTeam" value="${crewInstance.disabledTeam}" />
                                    <label>${message(code:'fc.crew.disabledteam')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="disabledContest" value="${crewInstance.disabledContest}" />
                                    <label>${message(code:'fc.crew.disabledcontest')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <g:if test="${crewInstance.GetIncreaseFactor() > 0}">
                            <fieldset>
                                <p>
                                    <div>
                                        <g:checkBox name="increaseEnabled" value="${crewInstance.increaseEnabled}" />
                                        <label>${message(code:'fc.crew.increaseenabled',args:[crewInstance.GetIncreaseFactor()])}</label>
                                    </div>
                                </p>
                            </fieldset>
                        </g:if>
                        <fieldset>
                            ${message(code:'fc.crew.uuid')}: ${fieldValue(bean:crewInstance, field:'uuid')}
                        </fieldset>
                        <input type="hidden" name="id" value="${crewInstance?.id}"/>
                        <input type="hidden" name="version" value="${crewInstance?.version}"/>
                        <g:if test="${params.next}">
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.crew.gotonext')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.crew.updatenext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        <g:if test="${params.next}">
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>