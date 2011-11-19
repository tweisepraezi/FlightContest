<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestdaytask.timetable')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestdaytask.timetable')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${contestDayTaskInstance?.contestday.name()} - ${contestDayTaskInstance.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.crewtest.number')}</th>
                                    <th>${message(code:'fc.crew')}</th>
                                    <th>${message(code:'fc.aircraft')}</th>
                                    <th>${message(code:'fc.crewtest.tas')}</th>
                                    <th>${message(code:'fc.flighttestwind')}</th>
                                    <th>${message(code:'fc.crewtest.testing')}</th>
                                    <th>${message(code:'fc.crewtest.takeoff')}</th>
                                    <th>${message(code:'fc.crewtest.arrival')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="crewTestInstance" in="${CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:'viewpos'])}">
                                    <tr>
                                        <td>${crewTestInstance.viewpos+1}</td>
                                        <td>${crewTestInstance.crew.name()}</td>
                                        <td>${crewTestInstance.aircraft.registration}</td>
                                        <td>${crewTestInstance.TAS}${message(code:'fc.knot')}</td>
                                        <td><g:windtext var="${crewTestInstance.flighttestwind.wind}"/></td>
                                        <td>${crewTestInstance.testingTime?.format('HH:mm')}</td>
                                        <td>${crewTestInstance.takeoffTime?.format('HH:mm')}</td>
                                        <td>${crewTestInstance.arrivalTime?.format('HH:mm')}</td>
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