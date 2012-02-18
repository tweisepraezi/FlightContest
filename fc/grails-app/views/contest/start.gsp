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
                                    	<td class="detailtitle">${message(code:'fc.resultclasses')}:</td>
                                    	<td><g:if test="${contestInstance.resultClasses}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.no')}</g:else></td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.contestrule')}:</td>
                                        <td>${message(code:contestInstance.contestRule.titleCode)}</td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.scale')}:</td>
                                        <td>1:${fieldValue(bean:contestInstance, field:'mapScale')}</td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.timezone')}:</td>
                                        <td>${fieldValue(bean:contestInstance, field:'timeZone')}${message(code:'fc.time.h')}</td>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.teamcrewnum')}:</td>
                                        <td>${fieldValue(bean:contestInstance, field:'teamCrewNum')}</td>
                                    </tr>
                                    <g:if test="${!contestInstance.aflosTest}">
	                                    <tr>
	                                        <td class="detailtitle">${message(code:'fc.useuploadedaflos')}:</td>
	                                        <td><g:if test="${contestInstance.aflosUpload}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.no')}</g:else></td>
	                                    </tr>
                                    </g:if>
                                </tbody>
                            </table>
                        </g:form>
                    </div>
                </div>
            </g:if>
            <p>${message(code:'fc.program.foot')}</p>
        </div>
    </body>
</html>