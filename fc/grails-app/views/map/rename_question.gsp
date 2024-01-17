<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.map.rename')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="task" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.map.rename')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.map.rename.oldname')}:</label></td>
                                    <td>${params.title}</td>
                                </tr> 
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.map.rename.newname')}:</label></td>
                                    <td><input type="text" name="MapRenameNewName" value="${params.title}" size="60"></td>
                                </tr> 
                            </tbody>
                        </table>
                        <input type="hidden" name="name" value="${params.title}" />
                        <g:actionSubmit action="rename" value="${message(code:'fc.map.rename')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>