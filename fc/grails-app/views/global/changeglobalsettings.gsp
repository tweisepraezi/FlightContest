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
                    	<g:set var="lastShowLimitCrewNum" value="${session.showLimitCrewNum}"></g:set>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.language')}*:</label>
                                <br/>
                                <g:select from="${Languages.values()}" name="showLanguage" value="${session.showLanguage}" optionValue="title" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printlanguage')}*:</label>
                                <br/>
                                <g:select from="${Languages.values()}" name="printLanguage" value="${session.printLanguage}" optionValue="title" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.showlimitcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="showLimitCrewNum" name="showLimitCrewNum" value="${fieldValue(bean:session,field:'showLimitCrewNum')}" tabIndex="3"/>
                            </p>
                        </fieldset>
                        <g:if test="${!Global.IsCloudFoundryEnvironment()}">
	                        <fieldset>
		                        <p>
		                            <label>${message(code:'fc.config')}:</label>
		                            <br/>
		                            <g:textArea name="configText" value="${session.configText}" rows="20" cols="110" tabIndex="4"/>
		                        </p>
	                        </fieldset>
	                    </g:if>
                        <input type="hidden" name="lastShowLimitCrewNum" value="${lastShowLimitCrewNum}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="101"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="102"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>