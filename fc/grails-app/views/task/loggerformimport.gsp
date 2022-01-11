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
                        <g:set var="test_instances" value="${taskInstance.GetIncompleteFlightTests()}" />
                        <div>
                            <g:if test="${info}">
                                <p>${message(code:'fc.loggerdata.importform.externdata',args:[info])}</p>
                            </g:if>
                            <g:else>
                                <p>${message(code:'fc.loggerdata.importform.externfile',args:[loggerfile])}</p>
                            </g:else>
                        </div>
                        <g:if test="${test_instances}">
                            <p>
                                <div>
                                    <label>${message(code:'fc.crew')}: </label>
                                    <g:select from="${test_instances}" id="select_id" optionKey="id" optionValue="${{it.name()}}" name="testid" value="" noSelection="${[id:'']}" onchange="select();" tabIndex="${ti[0]++}"/>
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
                        <p>
                            <g:checkBox name="interpolate_missing_data" value="${true}" tabIndex="${ti[0]++}"/>
                            ${message(code:'fc.loggerdata.interpolate')}
                        </p>
                        <div>
                            ${message(code:'fc.loggerdata.correctseconds')} [${message(code:'fc.time.s')}]:<br/>
                            <input type="text" id="correct_seconds" name="correct_seconds" value="${0}" tabIndex="${ti[0]++}"/>
                        </div>
                        <br/>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />                        
                        <input type="hidden" name="loggerfile" value="${loggerfile}" />
                        <input type="hidden" name="removefile" value="${removefile}" />
                        <input type="hidden" name="info" value="${info}" />
                        <g:if test="${test_instances}">
                            <g:actionSubmit id="import_button" action="loggerformimportsave" value="${message(code:'fc.import')}" disabled tabIndex="${ti[0]++}"/>
                            <script>
                                function select() {
                                    var select_value = $("#select_id").val();
                                    $("#import_button").prop("disabled", select_value == "id");
                                }
                            </script>
                        </g:if>
                        <g:actionSubmit action="loggerimportcancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        <script>
                             var body = document.getElementsByTagName("body");
                             <g:if test="${info}">
                                if (body) {
                                    body[0].onunload = removeGpxFile;
                                }
                            </g:if>
                            function removeGpxFile() {
                                var xmlhttp=new XMLHttpRequest();
                                xmlhttp.open("GET","${createLinkTo(dir:'gpx/deletegpxfile',file:'')}?filename=${loggerfile}",false);
                                xmlhttp.send();
                            }
                        </script>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>