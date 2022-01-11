<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <!-- <meta http-equiv="refresh" content="5"> -->
        <title>${message(code:'fc.task.readlogger')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.readlogger')} - ${taskInstance.name()}</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <g:readLogger t="${taskInstance}" loggertype="${loggertype}" port="${port}" ti="${ti}"/>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="readlogger_refresh" value="${message(code:'fc.refresh')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="readlogger_read" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${loggerfile}">
                            <script>
                                var logger_file = '${loggerfile}';
                                window.open('/fc/task/loggerformimportextern?loggerfile=${loggerfile}&removefile=true&info=${info}', '_blank');
                            </script>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>