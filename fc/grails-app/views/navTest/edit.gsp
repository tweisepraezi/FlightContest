<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtest.edit')}</title>
    </head>
    <body>
		<g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtest.edit')}</h2>
                <g:hasErrors bean="${navTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${navTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtest.from')}:</td>
                                    <td><g:contestday var="${navTestInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${navTestInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${navTestInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:navTestInstance,field:'title')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${navTestInstance?.id}" />
                        <input type="hidden" name="version" value="${navTestInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>