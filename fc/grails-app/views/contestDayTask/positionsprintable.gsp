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
                                    <td>${contestDayTaskInstance?.contestday.name()} - ${contestDayTaskInstance.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                   <th>${message(code:'fc.test.results.position')}</th>
                                   <th>${message(code:'fc.crew')}</th>
                                   <th>${message(code:'fc.planningtest')}</th>
                                   <th>${message(code:'fc.flighttest')}</th>
                                   <th>${message(code:'fc.landingtest')}</th>
                                   <th>${message(code:'fc.specialtest')}</th>
                                   <th>${message(code:'fc.test.results.summary')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="testInstance" in="${Test.findAllByContestdaytask(contestDayTaskInstance,[sort:'positionContestDay'])}">
                                    <tr class="even">
                                        <td>${testInstance.positionContestDay}</td>
                                        <td>${testInstance.crew.name}</td>
                                        <td>${testInstance.planningTestPenalties}</td>
                                        <td>${testInstance.flightTestPenalties}</td>
                                        <td>${testInstance.landingTestPenalties}</td>
                                        <td>${testInstance.specialTestPenalties}</td>
                                        <td>${testInstance.testPenalties}</td>
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