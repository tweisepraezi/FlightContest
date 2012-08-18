<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${message(code:'fc.test.timetable')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion}) - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.timetable')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.timetable')}</h2>
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
                                    </g:else>
                                    <th>${message(code:'fc.test.takeoff')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                   	<g:if test="${!test_instance.crew.disabled}">
	                                    <tr>
	                                        <td>${test_instance.GetStartNum()}</td>
	                                        <td>${test_instance.crew.name}</td>
	                                        <td>${test_instance.crew.aircraft.registration}<g:if test="${test_instance.crew.aircraft?.user1 && test_instance.crew.aircraft?.user2}"> *</g:if></td>
	                                        <td>${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
	                                        <td><g:if test="${test_instance.crew.team}">${test_instance.crew.team.name}</g:if></td>
	                                        <td>${test_instance.testingTime?.format('HH:mm')}</td>
	                                        <td>${test_instance.takeoffTime?.format('HH:mm')}</td>
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