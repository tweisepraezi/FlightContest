<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtest.results')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtest.results')}</h2>
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
                                   <th>${message(code:'fc.crewtest.results.position')}</th>
                                   <th>${message(code:'fc.crew')}</th>
                                   <th>${message(code:'fc.navtest')}</th>
                                   <th>${message(code:'fc.flighttest')}</th>
                                   <th>${message(code:'fc.landingtest')}</th>
                                   <th>${message(code:'fc.specialtest')}</th>
                                   <th>${message(code:'fc.crewtest.results.summary')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="crewTestInstance" in="${CrewTest.findAllByContestdaytask(contestDayTaskInstance,[sort:'positionContestDay'])}">
                                    <tr class="even">
                                        <td>${crewTestInstance.positionContestDay}</td>
                                        <td>${crewTestInstance.crew.name()}</td>
                                        <td>${crewTestInstance.penaltyNavTest}</td>
                                        <td>${crewTestInstance.penaltyFlightTest}</td>
                                        <td>${crewTestInstance.penaltyLandingTest}</td>
                                        <td>${crewTestInstance.penaltySpecialTest}</td>
                                        <td>${crewTestInstance.penaltySummary}</td>
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