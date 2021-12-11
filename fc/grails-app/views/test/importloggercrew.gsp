<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flightresults.loggerimport')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flightresults.loggerimport')}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post" params="['id':testInstance.id]">
                        <g:set var="ti" value="${[]+1}"/>
                        <g:crewDetails t="${testInstance}" />
                        <div>
                            <p>${message(code:'fc.loggerdata.selectloggerfilename.info',args:[LoggerFileTools.LOGGER_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${LoggerFileTools.LOGGER_EXTENSIONS}" name="loggerfile" tabIndex="${ti[0]++}"/>
                        </div>
                        <br/>
                        <p>
                            <g:checkBox name="interpolate_missing_data" value="${true}" tabIndex="${ti[0]++}"/>
                            ${message(code:'fc.loggerdata.interpolate')}
                        </p>
                        <div>
                            ${message(code:'fc.loggerdata.correctseconds')} [${message(code:'fc.time.s')}]:<br/>
                            <input type="text" id="correct_seconds" name="correct_seconds" value="${0}" tabIndex="${ti[0]++}"/>
                        </div>
                        <br/>
                        <g:actionSubmit action="importloggerfile" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancelimportcrew" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>