<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.sortstartnum')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.sortstartnum')}</h2>
                <div class="block" id="forms" >
                	<g:form action="sortstartnumrun">
                        <p>
                            <div>
                                <g:checkBox name="noStartnum13" value="${noStartnum13}" />
                                <label>${message(code:'fc.crew.import.nostartnum13')}</label>
                            </div>
                        </p>
                    	<div>
	    					<input type="submit" value="${message(code:'fc.crew.runcommand')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="1"/>
		                    <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="2"/>
		                </div>
					</g:form>
                </div>
            </div>
        </div>
    </body>
</html>