<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtest.results')} ${crewTestInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtest.results')} ${crewTestInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.from')}:</td>
                                    <td><g:contestday var="${crewTestInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${crewTestInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'listcrewtests')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${crewTestInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${crewTestInstance.aircraft}">
                                        <td><g:aircraft var="${crewTestInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <th colspan="2" class="table-head">${message(code:'fc.crewtest.results.tests')}</th>
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
                        <input type="hidden" name="id" value="${crewTestInstance?.id}" />
                        <g:actionSubmit action="editresults" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="printresults" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>