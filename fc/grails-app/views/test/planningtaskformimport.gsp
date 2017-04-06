<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtesttask.importform')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtesttask.importform')}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post" params="['id':testInstance.id]">
                        <g:crewDetails t="${testInstance}" />
                        <div>
                            <p>${message(code:'fc.planningtesttask.importform.info',args:[ImageService.IMAGE_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${ImageService.IMAGE_EXTENSIONS}" name="imagefile" tabIndex="1"/>
                        </div>
                        <br/>
                        <g:actionSubmit action="planningtaskformimportimagefile" value="${message(code:'fc.import')}" tabIndex="11"/>
                        <g:actionSubmit action="planningtaskformimportcancel" value="${message(code:'fc.cancel')}" tabIndex="12"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>