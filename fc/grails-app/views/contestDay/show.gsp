<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestday.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestday.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${contestDayInstance.name()}</td>
                                </tr>                                
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.list')}:</td>
                                    <td>
                                        <g:each var="contestDayTaskInstance" in="${ContestDayTask.findAllByContestday(contestDayInstance)}">
                                           <g:contestdaytask var="${contestDayTaskInstance}" link="${createLink(controller:'contestDayTask',action:'show')}"/>
                                           <br/>
                                        </g:each>
                                     </td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${contestDayInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="createdaytask" value="${message(code:'fc.contestdaytask.add1')}" />
                </g:form>
            </div>
        </div>
    </body>
</html>
