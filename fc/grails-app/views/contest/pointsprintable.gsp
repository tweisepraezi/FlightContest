<html>
    <g:set var="print_subtitle" value="${message(code:contestInstance.contestRule.titleCode)}"/>
    <g:if test="${contestInstance.printPointsPrintTitle}">
        <g:set var="print_subtitle" value="${contestInstance.printPointsPrintTitle}"/>
    </g:if>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                @top-left {
                    content: "${print_subtitle}"
                }
                @top-right {
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${print_subtitle}</title>
    </head>
    <body>
        <div>
            <div>
                <h3>${print_subtitle}</h3>
                <div>
                    <g:form>
                        <g:pointsPrintable i="${contestInstance}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>