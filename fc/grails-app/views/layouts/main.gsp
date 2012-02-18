<html>
    <head>
        <title><g:layoutTitle default="${message(code:'fc.program.title')}" /> - ${message(code:'fc.program.title')}</title>

        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'fc.ico')}" type="image/x-icon" />

        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'reset.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'text.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'grid.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'layout.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'nav.css')}" media="screen" />
        
        <!--[if IE 6]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie6.css')}" media="screen" /><![endif]-->
        <!--[if IE 7]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie.css')}" media="screen" /><![endif]-->
        
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fc.css')}" media="screen" />
        
        <g:layoutHead />

        <g:javascript library="application" />
    </head>
    <body>
        <div class="container_12">
            <div class="grid">
                <h1 id="branding">
                    <g:if test="${session?.contestTitle}">
                    	${session.contestTitle}
                    </g:if>
                    <g:elseif test="${session?.lastContest}">
                        ${session.lastContest.name()}
                    </g:elseif>
                    <g:else>
                        ${message(code:'fc.program.title')}
                    </g:else>
                </h1>
            </div>
            <g:layoutBody />
        </div>
    </body>
</html>