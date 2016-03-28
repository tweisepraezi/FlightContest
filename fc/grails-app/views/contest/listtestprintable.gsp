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
                    content: "${message(code:'fc.program.printtest')}"
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
        <title>${message(code:'fc.program.printtest')}</title>
    </head>
    <body>
        <g:set var="num" value="${new Integer(100)}"/>
        <g:if test="${crewList}">
            <g:set var="num" value="${crewList.size}"/>
        </g:if>
        <h2>${message(code:'fc.program.printtest')}</h2>
        <h3>${message(code:'fc.program.printtest.subtitle')} (${num})</h3>
        <g:form>
          	<br/>
            <table class="testlist">
                <thead>
                    <tr>
                    	<th>${message(code:'fc.program.printtest.col1')}</th>
                       	<th>${message(code:'fc.program.printtest.col2')}</th>
                       	<th>${message(code:'fc.program.printtest.col3')}</th>
                       	<th>${message(code:'fc.program.printtest.col4')}</th>
                       	<th>${message(code:'fc.program.printtest.col5')}</th>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${crewList}">
                        <g:each in="${crewList}" status="i" var="crew_instance">
                            <tr>
                                <td class="testcol1">${crew_instance.startNum}</td>
                                <td class="testcol2">${crew_instance.name}</td>
                                <td class="testcol3">-</td>
                                <td class="testcol4">-</td>
                                <td class="testcol5">-</td>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                     <g:set var="i" value="${new Integer(0)}" />
                     <g:while test="${i < num}">
                      <tr>
                          <td class="testcol1">${i+1}</td>
                          <td class="testcol2">-</td>
                          <td class="testcol3">-</td>
                          <td class="testcol4">-</td>
                          <td class="testcol5">-</td>
                      </tr>
                         <g:set var="i" value="${i+1}" />
                     </g:while>
                    </g:else>
                </tbody>
            </table>
        </g:form>
    </body>
</html>