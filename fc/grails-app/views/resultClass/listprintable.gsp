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
                    content: "${message(code:'fc.resultclass.list')}"
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
        <title>${message(code:'fc.resultclass.list')}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${message(code:'fc.resultclass.list')} (${resultclassInstanceList.size()})</h2>
                <div>
                    <g:form>
                        <table class="resultclasslist">
                            <thead>
                                <tr>
                                   <th>${message(code:'fc.resultclass.name')}</th>
                                   <th>${message(code:'fc.resultclass.name.short.short')}</th>
                                   <th>${message(code:'fc.crew')}</th>
			                       <th>${message(code:'fc.aircraft')}</th>
			                       <th>${message(code:'fc.tas')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="resultclass_instance" in="${resultclassInstanceList}" status="i" >
                                	<g:if test="${Crew.findByResultclass(resultclass_instance)}">
                                        <g:set var="j" value="${new Integer(0)}"/>
										<g:each var="crew_instance" in="${Crew.findAllByResultclass(resultclass_instance,[sort:'name'])}">
                                            <g:if test="${!crew_instance.disabled}">
			                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
			                                    	<g:if test="${j==0}">
			                                        	<td class="resultclass">${resultclass_instance.name}</td>
			                                        	<td class="shortresultclass">${resultclass_instance.shortName}</td>
			                                        </g:if>
			                                        <g:else>
			                                        	<td class="resultclass"/>
			                                        	<td class="shortresultclass"/>
			                                        </g:else>
			                                        <td class="crew">${crew_instance.name}</td>
			                                        <td class="aircraft"><g:if test="${crew_instance.aircraft}">${crew_instance.aircraft.registration}</g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
			                                        <td class="tas">${fieldValue(bean:crew_instance, field:'tas')}${message(code:'fc.knot')}</td>
			                                    </tr>
	                                            <g:set var="j" value="${j+1}"/>
	                                        </g:if>
		                                </g:each>
				                	</g:if>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>