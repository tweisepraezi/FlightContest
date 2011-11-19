<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest')}</title>
    </head>
    <body>
        <g:if test="${contestInstance}">
            <g:mainnav link="${createLink(controller:'contest')}" controller="contest" edit="${message(code:'fc.contest.settings')}" id="${contestInstance.id}" conteststart="true" />
        </g:if> <g:else>
            <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        </g:else>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:if test="${contestInstance}">
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
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.timezone')}:</td>
                                        <td>${fieldValue(bean:contestInstance, field:'timeZone')}${message(code:'fc.time.h')}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </g:form>
                    </div>
                </div>
            </g:if>
            <p>${message(code:'fc.programfoot')}</p>
        </div>
    </body>
</html>