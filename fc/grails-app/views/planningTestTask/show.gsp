<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.planningtesttask')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.planningtesttask')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:planningtest var="${planningTestTaskInstance?.planningtest}" link="${createLink(controller:'planningTest',action:'edit')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td>${planningTestTaskInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <td><g:route var="${planningTestTaskInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <td><g:windtext var="${planningTestTaskInstance?.wind}" /></td>
                                </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${planningTestTaskInstance?.id}" />
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" />
                        <g:if test="${!Test.findByPlanningtesttask(planningTestTaskInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>