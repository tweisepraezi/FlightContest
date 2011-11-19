<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.results')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.results')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${taskInstance.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
	                        <g:set var="results_columns" value="${new Integer(0)}"></g:set>
                            <thead>
                                <tr>
    	                        	<th>${message(code:'fc.test.results.position')}</th>
                                   	<th>${message(code:'fc.crew')}</th>
	                               	<g:if test="${taskInstance.planningTestRun}">
			                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
    	                            	<th>${message(code:'fc.planningresults.planning')}</th>
	                                </g:if>
                                	<g:if test="${taskInstance.flightTestRun}">
			                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
                                   		<th>${message(code:'fc.flightresults.flight')}</th>
	                                </g:if>
	                                <g:if test="${taskInstance.observationTestRun}">
			                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
	                                    <th>${message(code:'fc.observationresults.observations')}</th>
	                                </g:if>
	                                <g:if test="${taskInstance.landingTestRun}">
			                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
	                                    <th>${message(code:'fc.landingresults.landing')}</th>
	                                </g:if>
	                                <g:if test="${taskInstance.specialTestRun}">
			                        	<g:set var="results_columns" value="${results_columns+1}"></g:set>
	                                    <th>${message(code:'fc.specialresults.other')}</th>
	                                </g:if>
	                                <g:if test="${results_columns > 1}">
                                    	<th>${message(code:'fc.test.results.summary')}</th>
	                                </g:if>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="testInstance" in="${Test.findAllByTask(taskInstance,[sort:'taskPosition'])}">
                                   	<g:if test="${!testInstance.crew.disabled}">
	                                    <tr class="even">
	                                        <td>${testInstance.taskPosition}</td>
	                                        <td>${testInstance.crew.name}</td>
			                               	<g:if test="${taskInstance.planningTestRun}">
		                                        <g:if test="${testInstance.planningTestComplete}">
		                                        	<td>${testInstance.planningTestPenalties}</td>
		                                        </g:if> <g:else>
		                                        	<td>${message(code:'fc.test.results.position.none')}</td>
		                                        </g:else>
			                                </g:if>
		                                	<g:if test="${taskInstance.flightTestRun}">
		                                        <g:if test="${testInstance.flightTestComplete}">
			                                        <td>${testInstance.flightTestPenalties}</td>
		                                        </g:if> <g:else>
		                                        	<td>${message(code:'fc.test.results.position.none')}</td>
		                                        </g:else>
			                                </g:if>
			                                <g:if test="${taskInstance.observationTestRun}">
		                                        <g:if test="${testInstance.observationTestComplete}">
			                                        <td>${testInstance.observationTestPenalties}</td>
		                                        </g:if> <g:else>
		                                        	<td>${message(code:'fc.test.results.position.none')}</td>
		                                        </g:else>
			                                </g:if>
			                                <g:if test="${taskInstance.landingTestRun}">
		                                        <g:if test="${testInstance.landingTestComplete}">
			                                        <td>${testInstance.landingTestPenalties}</td>
		                                        </g:if> <g:else>
		                                        	<td>${message(code:'fc.test.results.position.none')}</td>
		                                        </g:else>
			                                </g:if>
			                                <g:if test="${taskInstance.specialTestRun}">
		                                        <g:if test="${testInstance.specialTestComplete}">
			                                        <td>${testInstance.specialTestPenalties}</td>
		                                        </g:if> <g:else>
		                                        	<td>${message(code:'fc.test.results.position.none')}</td>
		                                        </g:else>
			                                </g:if>
			                                <g:if test="${results_columns > 1}">
	    	                                    <td>${testInstance.taskPenalties}</td>
	    	                                </g:if>
	                                    </tr>
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