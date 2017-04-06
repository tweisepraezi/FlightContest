<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.observation.importform')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" taskresults="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.observation.importform')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <g:set var="test_instances" value="${taskInstance.GetObservationTests()}" />
                        <div>
                            <p>${message(code:'fc.observation.importform.extern',args:[imagefile])}</p>
                        </div>
                        <g:if test="${test_instances}">
                            <p>
                                <div>
                                    <label>${message(code:'fc.crew')}: </label>
                                    <g:select from="${test_instances}" optionKey="id" optionValue="${{it.name()}}" name="testid" value="" noSelection="${[id:'']}" ></g:select>
                                </div>
                            </p>
	                        <div>
	                            <p>${message(code:'fc.observation.importform.task',args:[taskInstance.name()])}</p>
	                        </div>
                        </g:if>
                        <g:else>
                            <div>
                                <p>${message(code:'fc.observation.importform.task.complete',args:[taskInstance.name()])}</p>
                            </div>
                        </g:else>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />                        
                        <input type="hidden" name="imagefile" value="${imagefile}" />
                        <g:if test="${test_instances}">
                            <g:actionSubmit action="observationformimportsave" value="${message(code:'fc.import')}" tabIndex="11"/>
                        </g:if>
                        <g:actionSubmit action="formimportcancel" value="${message(code:'fc.cancel')}" tabIndex="12"/>
                        <br/>
                        <br/>
                        <img src="${createLink(controller:'task',action:'formimage',params:['taskid':taskInstance.id,'imagefile':imagefile])}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>