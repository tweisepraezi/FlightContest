<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.list')} (${crewInstanceList.size()})</h2>
                <div class="block" id="forms" >
                    <g:form>
                         <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.crew.name')}</th>
                                    <th>${message(code:'fc.crew.team')}</th>
                                    <th>${message(code:'fc.crew.aircraft')}</th>
                                    <th>${message(code:'fc.tas')}</th>
			                    	<g:if test="${resultClasses}">
        	                            <th>${message(code:'fc.crew.resultclass')}</th>
        	                        </g:if>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${crewInstanceList}" status="i" var="crewInstance">
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${crewInstance.name}</td>
                                        <td>${crewInstance.team?.name}</td>
                                        <td><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.registration}</g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
                                        <td>${fieldValue(bean:crewInstance, field:'tas')}${message(code:'fc.knot')}</td>
				                    	<g:if test="${resultClasses}">
    	                                    <td>${crewInstance.resultclass?.name}</td>
    	                                </g:if>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>