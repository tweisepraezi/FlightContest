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
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.resultclass.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:resultclassInstance,field:'name')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.name.short')}*:</label>
                                <br/>
                                <input type="text" id="shortName" name="shortName" value="${fieldValue(bean:resultclassInstance,field:'shortName')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.contesttitle')}:</label>
                                <br/>
                                <input type="text" id="contestTitle" name="contestTitle" value="${fieldValue(bean:resultclassInstance,field:'contestTitle')}" tabIndex="3"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestrule')}*:</label>
                                <br/>
                                <g:select from="${ContestRules.values()}" optionValue="${{message(code:it.titleCode)}}" name="contestRule" value="${resultclassInstance.contestRule}" tabIndex="4"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="11"/>
                        <g:actionSubmit action="savesettings" value="${message(code:'fc.save')}" tabIndex="12"/>
                        <g:if test="${resultclassInstance.contest.contestRuleForEachClass}">
                            <g:actionSubmit action="editpoints" value="${message(code:'fc.points')}" tabIndex="13"/>
                        </g:if>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="14"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="15"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>