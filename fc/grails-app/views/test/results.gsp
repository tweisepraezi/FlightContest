<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.results')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.results')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <th colspan="2" class="table-head">${message(code:'fc.test.results.tests')}</th>
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
                        <input type="hidden" name="id" value="${testInstance?.id}" />
                        <g:actionSubmit action="editresults" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="printresults" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>