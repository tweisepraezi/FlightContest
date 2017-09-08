<html>
    <g:set var="print_subtitle" value="${message(code:resultclassInstance.contestRule.titleCode)}"/>
    <g:if test="${resultclassInstance.printPointsPrintTitle}">
        <g:set var="print_subtitle" value="${resultclassInstance.printPointsPrintTitle}"/>
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
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    content: "${print_subtitle}"
                }
                @top-right {
                    font-family: Noto Sans;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${resultclassInstance.GetPrintTitle('fc.contestrule.classpoints')}</title>
    </head>
    <body>
        <h3>${print_subtitle}</h3>
        <g:form>
            <g:pointsPrintable i="${resultclassInstance}" lang="${session.printLanguage}"/>
        </g:form>
    </body>
</html>