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
                            <g:if test="${resultclassInstance.contest.contestRuleForEachClass}">
	                            <p>
	                                <label>${message(code:'fc.contestrule')}*:</label>
	                                <a href="../../docs/help.html#supported-rules" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
	                                <br/>
	                                <g:select from="${ContestRules.GetContestRules()}" optionValue="${{it.ruleValues.ruleTitle}}" name="contestRule" value="${resultclassInstance.contestRule}" tabIndex="${ti[0]++}"/>
	                            </p>
	                        </g:if>
                        </fieldset>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
                        <g:if test="${params.next}">
                            <g:actionSubmit action="gotonext" value="${message(code:'fc.resultclass.gotonext')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.resultclass.updatenext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="update" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="savesettings" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${resultclassInstance.contest.contestRuleForEachClass}">
                            <g:actionSubmit action="editpoints" value="${message(code:'fc.points')}" tabIndex="${ti[0]++}"/>
                        </g:if>
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