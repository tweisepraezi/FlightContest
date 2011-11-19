<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.results.edit')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.results.edit')} ${testInstance.viewpos+1}</h2>
                <g:hasErrors bean="${testInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${testInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
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
                                    <td valign="top" class="value"><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${testInstance.crew.aircraft}">
                                        <td valign="top" class="value"><g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td valign="top" class="value">${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.landingtest')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="landingTestPenalties" name="landingTestPenalties" value="${fieldValue(bean:testInstance,field:'landingTestPenalties')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.specialtest')}* [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="specialTestPenalties" name="specialTestPenalties" value="${fieldValue(bean:testInstance,field:'specialTestPenalties')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${testInstance?.id}" />
                        <input type="hidden" name="version" value="${testInstance?.version}" />
                        <g:actionSubmit action="updateresults" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>