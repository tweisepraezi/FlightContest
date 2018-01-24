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
                    content: "${message(code:'fc.observation')}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${taskInstance.printName()}"
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
        <title>${message(code:'fc.observation')}</title>
    </head>
    <body>
        <g:set var="resultclass_instance" value="${null}" />
        <g:if test="${params.resultclassid}">
            <g:set var="resultclass_instance" value="${ResultClass.get(params.resultclassid.toLong())}" />
        </g:if>
        <g:set var="test_instance" value="${Test.GetFirstTest(taskInstance,resultclass_instance)}" />
        <h2>${message(code:'fc.observation')}</h2>
        <h3>${taskInstance.printName()}<g:if test="${resultclass_instance}"> - ${resultclass_instance.name}</g:if></h3>
        <g:form>
            <br/>
            <g:if test="${params.results != 'true'}">
	            <g:crewNeutralPrintable />
	            <br/>
                <br/>
	        </g:if>
            <g:observationPrintable t="${test_instance}" printResults="${params.results == 'true'}" />
        </g:form>
    </body>
</html>