<html>
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
                    content: "${contestInstance.GetPrintFreeTextTitle()}"
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <style type="text/css">${contestInstance.printFreeTextStyle}</style>
        <title>${contestInstance.GetPrintFreeTextTitle()}</title>
    </head>
    <body>
	    <g:form>
	        <table class="freetext">
	            <tbody>
	                <g:each var="line" in="${contestInstance.printFreeText.readLines()}" status="i">
	                    <tr>
                            <td id="${i+1}">${line}</td>
	                    </tr>
                    </g:each>
	            </tbody>
	        </table>
	    </g:form>
    </body>
</html>