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
                    content: "${message(code:'fc.landingresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}"
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
        <title>${message(code:'fc.landingresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${message(code:'fc.landingresults')} ${testInstance.GetStartNum()}</h2>
                <g:if test="${!testInstance.landingTestComplete}">
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                </g:if>
                <g:else>
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</h3>
                </g:else>
                <div>
                    <g:form>
                        <g:crewTestPrintable t="${testInstance}"/>
                       	<g:if test="${testInstance.IsLandingTest1Run()}">
	                        <br/>
	                        <g:landingTest1Printable t="${testInstance}"/>
                       	</g:if>
                       	<g:if test="${testInstance.IsLandingTest2Run()}">
	                        <br/>
                            <g:landingTest2Printable t="${testInstance}"/>
                       	</g:if>
                       	<g:if test="${testInstance.IsLandingTest3Run()}">
	                        <br/>
                            <g:landingTest3Printable t="${testInstance}"/>
                       	</g:if>
                       	<g:if test="${testInstance.IsLandingTest4Run()}">
	                        <br/>
                            <g:landingTest4Printable t="${testInstance}"/>
                       	</g:if>
                       	<br/>
                       	<g:landingTestSummaryPrintable t="${testInstance}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>