<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title><g:layoutTitle default="${message(code:'fc.gpx.showmap')}" /> - ${message(code:'fc.program.title')}</title>
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'fc.ico')}" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'reset.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'text.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'grid.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'layout.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'nav.css')}" media="screen" />
         <!--[if IE 6]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie6.css')}" media="screen" /><![endif]-->
         <!--[if IE 7]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie.css')}" media="screen" /><![endif]-->
         <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fc.css')}" media="screen" />
         <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fcprint.css')}" media="print" />
         <g:if test="${grails.util.GrailsUtil.getEnvironment().equals(org.codehaus.groovy.grails.commons.GrailsApplication.ENV_DEVELOPMENT) || grails.util.GrailsUtil.getEnvironment().equals("lastdb")}">
             <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fcdev.css')}" media="screen" />
         </g:if>
        <g:layoutHead />
    </head>
    <body onunload="removeGpxFile()">
        <div class="container_12">
            <div class="grid"  >
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
            <g:layoutBody/>
        </div>
    </body>
</html>