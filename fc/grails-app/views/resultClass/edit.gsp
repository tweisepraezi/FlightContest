<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.resultclass.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="resultClass" newaction="${message(code:'fc.resultclass.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.resultclass.edit')}</h2>
                <g:hasErrors bean="${resultclassInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${resultclassInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['resultclassReturnAction':resultclassReturnAction,'resultclassReturnController':resultclassReturnController,'resultclassReturnID':resultclassReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${resultclassInstance.GetNextResultClassID()}"/>
						<g:set var="prev_id" value="${resultclassInstance.GetPrevResultClassID()}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.resultclass.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:resultclassInstance,field:'name')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.name.short')}*:</label>
                                <br/>
                                <input type="text" id="shortName" name="shortName" value="${fieldValue(bean:resultclassInstance,field:'shortName')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.contesttitle')}:</label>
                                <br/>
                                <input type="text" id="contestTitle" name="contestTitle" value="${fieldValue(bean:resultclassInstance,field:'contestTitle')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <g:if test="${resultclassInstance.contest.contestRuleForEachClass}">
	                            <p>
	                                <label>${message(code:'fc.contestrule')}*:</label>
	                                <a href="/fc/docs/help_${session.showLanguage}.html#supported-rules" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
	                                <br/>
	                                <g:select from="${ContestRules.GetContestRules(false)}" optionValue="${{it.ruleValues.ruleTitle}}" name="contestRule" value="${resultclassInstance.contestRule}" tabIndex="${ti[0]++}"/>
	                            </p>
	                        </g:if>
                            <p>
                                <label>${message(code:'fc.resultclass.gatewidth')} [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="secretGateWidth" name="secretGateWidth" value="${fieldValue(bean:resultclassInstance,field:'secretGateWidth')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.minutesbeforestarttime')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="minutesBeforeStartTime" name="minutesBeforeStartTime" value="${fieldValue(bean:resultclassInstance,field:'minutesBeforeStartTime')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.minutesaddsubmission')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="minutesAddSubmission" name="minutesAddSubmission" value="${fieldValue(bean:resultclassInstance,field:'minutesAddSubmission')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
                        <g:actionSubmit action="savesettings" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${next_id}">
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.resultclass.updatenext')}" tabIndex="${ti[0]++}"/>
							<g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.resultclass.gotonext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.resultclass.updatenext')}" disabled tabIndex="${ti[0]++}"/>
							<g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.resultclass.gotonext')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
						<g:if test="${prev_id}">
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.resultclass.gotoprev')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.resultclass.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:if test="${resultclassInstance.contest.contestRuleForEachClass}">
                            <g:actionSubmit action="editpoints" value="${message(code:'fc.points')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>