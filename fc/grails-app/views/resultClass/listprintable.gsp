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
        <title>${message(code:'fc.resultclass.list')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.resultclass.list')} (${resultclassInstanceList.size()})</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                   <th>${message(code:'fc.resultclass.name')}</th>
                                   <th>${message(code:'fc.resultclass.contesttitle')}</th>
                                   <th>${message(code:'fc.crew')}</th>
			                       <th>${message(code:'fc.crew.aircraft')}</th>
			                       <th>${message(code:'fc.tas')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="resultclass_instance" in="${resultclassInstanceList}" status="i" >
                                	<g:if test="${Crew.findByResultclass(resultclass_instance)}">
										<g:each var="crew_instance" in="${Crew.findAllByResultclass(resultclass_instance,[sort:'name'])}" status="j" >
		                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
		                                    	<g:if test="${j==0}">
		                                        	<td>${resultclass_instance.name}</td>
		                                        	<td>${resultclass_instance.contestTitle}</td>
		                                        </g:if>
		                                        <g:else>
		                                        	<td/>
		                                        	<td/>
		                                        </g:else>
		                                        <td>${crew_instance.name}</td>
		                                        <td><g:if test="${crew_instance.aircraft}">${crew_instance.aircraft.registration}</g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
		                                        <td>${fieldValue(bean:crew_instance, field:'tas')}${message(code:'fc.knot')}</td>
		                                    </tr>
		                                </g:each>
				                	</g:if>
				                	<g:else>
				                		<tr class="${(i % 2) == 0 ? 'odd' : ''}">
				                			<td>${resultclass_instance.name}</td>
                                        	<td>${resultclass_instance.contestTitle}</td>
				                			<td/>
				                			<td/>
				                			<td/>
				                        </tr>
				                	</g:else>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>