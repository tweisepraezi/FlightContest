<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.results')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.results')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>${testInstance?.task.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td>${testInstance.crew.name}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td>${testInstance.crew.aircraft.registration}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                
                            </tbody>
                        </table>
                        <br/>
                        <table border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <th colspan="2">${message(code:'fc.test.results.tests')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtest')}:</td>
                                    <td>${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <td>${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingtest')}:</td>
                                    <td>${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.specialtest')}:</td>
                                    <td>${testInstance.specialTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>
                                    <td>${testInstance.testPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tfoot>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>