<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flightresults.trackerimport')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flightresults.trackerimport')}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post" params="['id':testInstance.id]">
                        <g:set var="ti" value="${[]+1}"/>
                        <g:crewDetails t="${testInstance}" />
                        <p>
                            <g:checkBox name="interpolate_missing_data" value="${true}" tabIndex="${ti[0]++}"/>
                            ${message(code:'fc.loggerdata.interpolate')}
                        </p>
                        <g:actionSubmit action="importtrackerdata" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancelimportcrew" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>