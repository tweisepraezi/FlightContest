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
                        <g:readLogger t="${taskInstance}" loggertype="${loggertype}" newportimport="${newportimport}" port="${port}" ports="${ports}" checkports="${checkports}" ti="${ti}"/>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit id="readlogger_refresh_id" action="readlogger_refresh" value="${message(code:'fc.refresh')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit id="readlogger_read_id" action="readlogger_read" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit id="cancel_id" action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${!loggertype}">
                            <script>
                                $("#readlogger_read_id").attr("disabled",true);
                            </script>
                        </g:if>
                        <g:if test="${loggerfile}">
                            <script>
                                var logger_file = '${loggerfile}';
                                window.open('/fc/task/loggerformimportextern?loggerfile=${loggerfile}&removefile=true&info=${info}', '_blank');
                            </script>
                        </g:if>
                        <g:elseif test="${loggertype && newportimport == "on"}">
                            <script>
                                if (new_port) {
                                    $("#readlogger_read_id").click();
                                    $("#readlogger_refresh_id").attr("disabled",true);
                                    $("#readlogger_read_id").attr("disabled",true);
                                    $("#cancel_id").attr("disabled",true);
                                }
                            </script>
                        </g:elseif>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>