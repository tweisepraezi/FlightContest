<html>
    <head>
		<style type="text/css">
		    @page {
                size: A4 landscape;
                @top-center {
                    content: "${message(code:'fc.test.timetablejury')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion}) - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.timetablejury')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.timetablejury')}</h2>
                <h3>${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})</h3>
                <div class="block" id="forms" >
                    <g:form>
                        <br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.crew')}</th>
                                    <th>${message(code:'fc.aircraft')}</th>
                                    <th>${message(code:'fc.tas')}</th>
                                    <th>${message(code:'fc.team')}</th>
                                    <g:if test="${taskInstance.planningTestDuration == 0}">
                                        <th>${message(code:'fc.test.planning.publish')}</th>
                                    </g:if>
                                    <g:else>
	                                    <th>${message(code:'fc.test.planning')}</th>
                                        <th>${message(code:'fc.test.planning.end')}</th>
                                    </g:else>
                                    <th>${message(code:'fc.test.takeoff')}</th>
                                    <th>${message(code:'fc.test.startpoint')}</th>
                                    <th>${message(code:'fc.test.finishpoint')}</th>
                                    <th>${message(code:'fc.test.landing')}</th>
                                    <th>${message(code:'fc.test.arrival')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                   	<g:if test="${!test_instance.crew.disabled}">
	                                    <tr>
	                                        <td>${test_instance.GetStartNum()}</td>
	                                        <td>${test_instance.crew.name}</td>
	                                        <td>${test_instance.crew.aircraft.registration}</td>
	                                        <td>${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
	                                        <td><g:if test="${test_instance.crew.team}">${test_instance.crew.team.name}</g:if></td>
	                                        <td>${test_instance.testingTime?.format('HH:mm')}</td>
                                            <g:if test="${taskInstance.planningTestDuration > 0}">
	                                           <td>${test_instance.endTestingTime?.format('HH:mm')}</td>
	                                        </g:if>
	                                        <td>${test_instance.takeoffTime?.format('HH:mm')}</td>
	                                        <td>${test_instance.startTime?.format('HH:mm')}</td>
	                                        <td>${test_instance.finishTime?.format('HH:mm:ss')}</td>
	                                        <td>${test_instance.maxLandingTime?.format('HH:mm:ss')}</td>
	                                        <td>${test_instance.arrivalTime?.format('HH:mm:ss')}</td>
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