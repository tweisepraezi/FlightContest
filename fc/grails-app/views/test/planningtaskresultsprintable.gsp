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
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.planningresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.printName()}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${testInstance.GetTestPos()}-" counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.planningresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.printName()}</title>
    </head>
    <body>
        <h2>${message(code:'fc.planningresults')} ${testInstance.GetStartNum()}</h2>
        <g:if test="${!testInstance.planningTestComplete}">
            <h3>${testInstance?.task.printName()} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()}) [${message(code:'fc.provisional')}]</h3>
        </g:if>
        <g:else>
            <h3>${testInstance?.task.printName()} (${message(code:'fc.version')} ${testInstance.GetPlanningTestVersion()})</h3>
        </g:else>
        <div>
            <g:form>
                <div style="page-break-inside:avoid">
	                <g:crewTestPrintable t="${testInstance}"/>
	                <br/>
	                <g:planningtaskTestPrintable t="${testInstance}"/>
	            </div>
	            <g:planningtaskTestScannedPrintable t="${testInstance}"/>
            </g:form>
        </div>
    </body>
</html>