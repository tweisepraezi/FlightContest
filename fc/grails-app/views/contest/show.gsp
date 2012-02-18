<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${fieldValue(bean:contestInstance, field:'title')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.scale')}:</td>
                                    <td>1:${fieldValue(bean:contestInstance, field:'mapScale')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${contestInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>