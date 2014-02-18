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
                @top-center {
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.landingresults')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.landingresults')} ${testInstance.GetStartNum()}</h2>
                <g:if test="${!testInstance.landingTestComplete}">
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                </g:if>
                <g:else>
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetLandingTestVersion()})</h3>
                </g:else>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.taskAircraft}">
                                            ${testInstance.taskAircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.taskAircraft}">
		                                    ${testInstance.taskAircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
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