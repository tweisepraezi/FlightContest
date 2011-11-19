<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestdaytask.listresults')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contestDayTask" contestdaytaskresults="true" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestdaytask.listresults')}</h2>
                <g:form id="${contestDayTaskInstance.id}" method="post" >
                    <br/>
                    <table>
                        <tbody>
                            <tr>
                                <td class="detailtitle">${message(code:'fc.test.from')}:</td>
                                <td><g:contestday var="${contestDayTaskInstance?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                            </tr>
                            <tr>
                                <td class="detailtitle" />
                                <td><g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                            </tr>
                            <tr>
                                <td class="detailtitle" />
                                <td><g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'listplanning')}"/></td>
                            </tr>
                        </tbody>
                    </table>
                    <table>
                        <thead>
                            <tr>
                                <th class="table-head" colspan="4">${message(code:'fc.crew.list')}</th>
                                <th class="table-head" colspan="4">${message(code:'fc.test.results')}</th>
                            </tr>
                            <tr>
                                <th/>
                                <th>${message(code:'fc.crew')}</th>
                                <th>${message(code:'fc.aircraft')}</th>
                                <th>${message(code:'fc.tas')}</th>
                               
                                <th>${message(code:'fc.test.results.planning')}</th>
                                <th>${message(code:'fc.points')}</th>
                                <th>${message(code:'fc.test.results.summary')}</th>
                                <th>${message(code:'fc.test.results.position')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each var="testInstance" status="i" in="${Test.findAllByContestdaytask(contestDayTaskInstance,[sort:'viewpos'])}">
                                <tr class="${(i % 2) == 0 ? 'odd' : ''}">
    
                                    <g:set var="testInstanceID" value="selectedTestID${testInstance.id.toString()}"/>
                                    <g:if test="${flash.selectedTestIDs && (flash.selectedTestIDs[testInstanceID] == 'on')}">
                                        <td><g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td><g:testnum var="${testInstance}" link="${createLink(controller:'test',action:'show')}"/></td>
                                    </g:else>
                            
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                    
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                    
                                    <td>${testInstance.crew.tas}${message(code:'fc.knot')}</td>
                                    
                                    <td><g:if test="${testInstance.planningTestComplete}">${testInstance.planningTestPenalties} ${message(code:'fc.points')} </g:if><a href="${createLink(controller:'test',action:'planningtaskresults')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
                                    
                                    <td><a href="${createLink(controller:'test',action:'results')}/${testInstance.id}">${message(code:'fc.test.results.here')}</a></td>
                                    
                                    <td>${testInstance.testPenalties}</td>
                                    
                                    <g:if test="${testInstance.positionContestDay}">
                                        <td>${testInstance.positionContestDay}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.test.results.position.none')}</td>
                                    </g:else>
                                    
                                </tr>
                            </g:each>
                        </tbody>
                        <tfoot>
                            <tr class="">
                                <td colspan="4"></td>
                                <td colspan="4"><g:actionSubmit action="calculatepositions" value="${message(code:'fc.test.results.positions.calculate')}" /> <g:actionSubmit action="printresults" value="${message(code:'fc.test.results.print')}" /></td>
                            </tr>
                        </tfoot>
                    </table>
                </g:form>
            </div>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>