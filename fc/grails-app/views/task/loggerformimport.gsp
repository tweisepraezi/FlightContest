<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.loggerdata.importform')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskresults="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.loggerdata.importform')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <g:set var="ti" value="${[]+1}"/>
                        <g:set var="test_instances" value="${taskInstance.GetFlightTests()}" />
                        <div>
                            <p>${message(code:'fc.loggerdata.importform.extern',args:[loggerfile])}</p>
                        </div>
                        <g:if test="${test_instances}">
                            <p>
                                <div>
                                    <label>${message(code:'fc.crew')}: </label>
                                    <g:select from="${test_instances}" optionKey="id" optionValue="${{it.name()}}" name="testid" value="" noSelection="${[id:'']}" tabIndex="${ti[0]++}"/>
                                </div>
                            </p>
	                        <div>
	                            <p>${message(code:'fc.loggerdata.importform.task',args:[taskInstance.name()])}</p>
	                        </div>
                        </g:if>
                        <g:else>
                            <div>
                                <p>${message(code:'fc.loggerdata.importform.task.complete',args:[taskInstance.name()])}</p>
                            </div>
                        </g:else>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />                        
                        <input type="hidden" name="loggerfile" value="${loggerfile}" />
                        <g:if test="${test_instances}">
                            <g:actionSubmit action="loggerformimportsave" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="loggerimportcancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>