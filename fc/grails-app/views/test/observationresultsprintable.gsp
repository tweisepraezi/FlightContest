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
                @top-left {
                    content: "${message(code:'fc.observationresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.printName()}"
                }
                @top-right {
                    content: "${testInstance.GetViewPos()}"
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
        <title>${message(code:'fc.observationresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.printName()}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${message(code:'fc.observationresults')} ${testInstance.GetStartNum()}</h2>
                <g:if test="${!testInstance.observationTestComplete}">
	                <h3>${testInstance?.task.printName()} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                </g:if>
                <g:else>
	                <h3>${testInstance?.task.printName()} (${message(code:'fc.version')} ${testInstance.GetObservationTestVersion()})</h3>
                </g:else>
                <div>
                    <g:form>
                        <g:crewTestPrintable t="${testInstance}"/>
                        <br/>
                        <g:observationTestPrintable t="${testInstance}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>