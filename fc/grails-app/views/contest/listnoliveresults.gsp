<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:if test="${contestInstance.liveRefreshSeconds}">
            <meta http-equiv="refresh" content="${contestInstance.liveRefreshSeconds}">
        </g:if>
        
        <title>${message(code:'fc.contest.liveresults')} [${message(code:'fc.provisional')}]</title>
        
        <link rel="shortcut icon" href="fc.ico" type="image/x-icon" />

        <g:if test="${contestInstance.liveStylesheet}">
            <link rel="stylesheet" type="text/css" href="${contestInstance.liveStylesheet}" media="screen" />
        </g:if>
    </head>
    <body>
        <div class="box boxborder" >
            <h2>${message(code:'fc.contest.noliveresults')}</h2>
            <p class="programinfo">${contestInstance.printOrganizer}</p>
        </div>
    </body>
</html>