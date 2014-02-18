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
                    content: "${message(code:'fc.program.printtest')} - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.listresults')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.program.printtest')}</h2>
                <h3>${message(code:'fc.program.printtest.subtitle')}</h3>
                <div class="block" id="forms" >
                    <g:form>
                      	<br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
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
	                            <tr class="even">
	                                <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
	                            </tr>
                                <tr class="even">
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr class="even">
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr class="even">
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr class="even">
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>