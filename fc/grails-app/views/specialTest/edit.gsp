<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.specialtest.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.specialtest.edit')}</h2>
                <g:hasErrors bean="${specialTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${specialTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${specialTestInstance?.task}" link="${createLink(controller:'task',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${specialTestInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:specialTestInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${specialTestInstance?.id}" />
                        <input type="hidden" name="version" value="${specialTestInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>