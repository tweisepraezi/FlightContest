<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.landingtesttask.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.landingtesttask.edit')}</h2>
                <g:hasErrors bean="${landingTestTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${landingTestTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${landingTestTaskInstance?.landingtest?.task}" link="${createLink(controller:'task',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:landingtest var="${landingTestTaskInstance?.landingtest}" link="${createLink(controller:'landingTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${landingTestTaskInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:landingTestTaskInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${landingTestTaskInstance?.id}" />
                        <input type="hidden" name="version" value="${landingTestTaskInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>