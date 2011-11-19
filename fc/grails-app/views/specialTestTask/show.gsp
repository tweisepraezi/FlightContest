<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.specialtesttask.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.specialtesttask.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:task var="${specialTestTaskInstance?.specialtest?.task}" link="${createLink(controller:'task',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:specialtest var="${specialTestTaskInstance?.specialtest}" link="${createLink(controller:'specialTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${specialTestTaskInstance.name()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${specialTestTaskInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>