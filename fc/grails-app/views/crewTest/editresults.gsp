<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtest.results.edit')} ${crewTestInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtest.results.edit')} ${crewTestInstance.viewpos+1}</h2>
                <g:hasErrors bean="${crewTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${crewTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
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
                                    <td valign="top" class="value"><g:crew var="${crewTestInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${crewTestInstance.aircraft}">
                                        <td valign="top" class="value"><g:aircraft var="${crewTestInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td valign="top" class="value">${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.navtest')} [${message(code:'fc.crewtest.results.points')}]:</label>
                                <br/>
                                <input type="text" id="penaltyNavTest" name="penaltyNavTest" value="${fieldValue(bean:crewTestInstance,field:'penaltyNavTest')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.flighttest')} [${message(code:'fc.crewtest.results.points')}]:</label>
                                <br/>
                                <input type="text" id="penaltyFlightTest" name="penaltyFlightTest" value="${fieldValue(bean:crewTestInstance,field:'penaltyFlightTest')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.landingtest')} [${message(code:'fc.crewtest.results.points')}]:</label>
                                <br/>
                                <input type="text" id="penaltyLandingTest" name="penaltyLandingTest" value="${fieldValue(bean:crewTestInstance,field:'penaltyLandingTest')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.specialtest')} [${message(code:'fc.crewtest.results.points')}]:</label>
                                <br/>
                                <input type="text" id="penaltySpecialTest" name="penaltySpecialTest" value="${fieldValue(bean:crewTestInstance,field:'penaltySpecialTest')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${crewTestInstance?.id}" />
                        <input type="hidden" name="version" value="${crewTestInstance?.version}" />
                        <g:actionSubmit action="updateresults" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>