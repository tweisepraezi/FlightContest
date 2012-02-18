<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.results.print')} ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.results.print')} ${taskInstance.name()}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['listresultsprintquestionReturnAction':listresultsprintquestionReturnAction,'listresultsprintquestionReturnController':listresultsprintquestionReturnController,'listresultsprintquestionReturnID':listresultsprintquestionReturnID]}">
                        <fieldset>
                        	<p>
	                        	<g:each var="resultclass_instance" in="${taskInstance.contest.resultclasses}">
	                                <div>
	                                    <g:checkBox name="resultclass_${resultclass_instance.id}" value="${true}" />
	                                    <label>${resultclass_instance.name} (${taskInstance.contest.GetResultTitle(taskInstance.GetClassResultSettings(resultclass_instance),false)})</label>
	                                </div>
    	                    	</g:each>
    	                    </p>
                        	</fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <g:actionSubmit action="printresultclassresults" value="${message(code:'fc.print')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>