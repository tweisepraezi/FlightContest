<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.changeglobalsettings')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'global')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.changeglobalsettings')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <g:set var="ti" value="${[]+1}"/>
                    	<g:set var="lastShowLimitCrewNum" value="${session.showLimitCrewNum}"></g:set>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.language')}*:</label>
                                <br/>
                                <g:select from="${Languages.values()}" name="showLanguage" value="${session.showLanguage}" optionValue="title" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.taskcreatorlanguage')}*:</label>
                                <br/>
                                <g:select from="${TaskCreatorLanguages.values()}" name="taskCreatorLanguage" value="${session.taskCreatorLanguage}" optionValue="title" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printlanguage')}*:</label>
                                <br/>
                                <g:select from="${Languages.values()}" name="printLanguage" value="${session.printLanguage}" optionValue="title" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.showlimitcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="showLimitCrewNum" name="showLimitCrewNum" value="${fieldValue(bean:session,field:'showLimitCrewNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <g:if test="${!Global.IsCloudFoundryEnvironment()}">
	                        <fieldset>
		                        <p>
		                            <label>${message(code:'fc.config')}:</label>
		                            <a href="../docs/help_${session.showLanguage}.html#configuration" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
		                            <br/>
		                            <g:textArea name="configText" value="${session.configText}" rows="20" cols="110" tabIndex="${ti[0]++}"/>
		                        </p>
	                        </fieldset>
	                    </g:if>
                        <input type="hidden" name="lastShowLimitCrewNum" value="${lastShowLimitCrewNum}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>