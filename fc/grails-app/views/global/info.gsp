<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.internal')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.info')}</h2>
                <div class="block" id="forms" >
                    <g:form controller="contest">
                        <g:if test="${url}">
                            <p>${message(code:'fc.uploaded.link',args:["${url}"])}</p>
                        </g:if>
                        <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>