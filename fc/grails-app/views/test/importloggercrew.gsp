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
                        <g:crewDetails t="${testInstance}" />
                        <div>
                            <p>${message(code:'fc.loggerdata.selectloggerfilename.info',args:[LoggerFileTools.LOGGER_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${LoggerFileTools.LOGGER_EXTENSIONS}" name="loggerfile" tabIndex="1"/>
                        </div>
                        <br/>
                        <g:actionSubmit action="importloggerfile" value="${message(code:'fc.import')}" tabIndex="11"/>
                        <g:actionSubmit action="cancelimportcrew" value="${message(code:'fc.cancel')}" tabIndex="12"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>