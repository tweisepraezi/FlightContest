<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.timetable')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.timetable')}</h2>
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
                                <g:each var="testInstance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                    <tr>
                                        <td>${testInstance.viewpos+1}</td>
                                        <td>${testInstance.crew.name}</td>
                                        <td>${testInstance.crew.aircraft.registration}</td>
                                        <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
                                        <td>${testInstance.testingTime?.format('HH:mm')}</td>
                                        <td>${testInstance.takeoffTime?.format('HH:mm')}</td>
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