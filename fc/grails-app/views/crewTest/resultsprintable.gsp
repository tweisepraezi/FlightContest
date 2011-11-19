<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtest.results')} ${crewTestInstance.viewpos+1}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtest.results')} ${crewTestInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${crewTestInstance?.contestdaytask?.contestday.name()} - ${crewTestInstance?.contestdaytask.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td>${crewTestInstance.crew.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${crewTestInstance.aircraft}">
                                        <td>${crewTestInstance.aircraft.registration}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <thead>
                                <th colspan="2">${message(code:'fc.crewtest.results.tests')}</th>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtest')}:</td>
                                    <td>${crewTestInstance.penaltyNavTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <td>${crewTestInstance.penaltyFlightTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest')}:</td>
                                    <td>${crewTestInstance.penaltyLandingTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest')}:</td>
                                    <td>${crewTestInstance.penaltySpecialTest} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.results.summary')}:</td>
                                    <td>${crewTestInstance.penaltySummary} ${message(code:'fc.crewtest.results.points')}</td>
                                </tr>
                            </tfoot>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>