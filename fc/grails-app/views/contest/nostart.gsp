<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest')}</title>
    </head>
    <body>
        <div class="box">
       		<g:if test="${reason == 'older'}">
            	<g:viewmsg msg="${message(code:'fc.program.dbolder')}" error="${true}"/>
       		</g:if>
       		<g:else test="${reason == 'newer'}">
            	<g:viewmsg msg="${message(code:'fc.program.dbnewer')}" error="${true}"/>
       		</g:else>
            <div class="box boxborder" >
            </div>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>