<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.anonymize')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.anonymize')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${fieldValue(bean:contestInstance, field:'title')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <g:actionSubmit action="anonymize" value="${message(code:'fc.anonymize')}" onclick="return confirm('${message(code:'fc.contest.anonymize.areyousure')}');" tabIndex="1"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="2"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>