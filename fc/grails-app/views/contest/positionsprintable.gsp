<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.positions')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.positions')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                   <th>${message(code:'fc.test.results.position')}</th>
                                   <th>${message(code:'fc.crew')}</th>
                                   	<g:each var="task_instance" in="${Task.findAllByContest(contestInstance)}">
                                    	<th>${task_instance.name()}</th>
	                                </g:each>
                                   <th>${message(code:'fc.test.results.summary')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabled(contestInstance,false,[sort:'contestPosition'])}">
                                    <tr class="even">
                                        <td>${crew_instance.contestPosition}</td>
                                        <td>${crew_instance.name}</td>
                                        <g:each var="task_instance" in="${Task.findAllByContest(contestInstance)}">
                                        	<td>${Test.findByCrewAndTask(crew_instance,task_instance).taskPenalties}</td>
		                                </g:each>
                                        <td>${crew_instance.contestPenalties}</td>
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