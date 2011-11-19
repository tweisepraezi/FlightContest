<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.timetable')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.timetable')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${taskInstance.name()} (${message(code:'fc.test.timetable.version')} ${taskInstance.timetableVersion})</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.crew')}</th>
                                    <th>${message(code:'fc.aircraft')}</th>
                                    <th>${message(code:'fc.tas')}</th>
                                    <th>${message(code:'fc.test.planning')}</th>
                                    <th>${message(code:'fc.test.takeoff')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                   	<g:if test="${!test_instance.crew.disabled}">
	                                    <tr>
	                                        <td>${test_instance.viewpos+1}</td>
	                                        <td>${test_instance.crew.name}</td>
	                                        <td>${test_instance.crew.aircraft.registration}</td>
	                                        <td>${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
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