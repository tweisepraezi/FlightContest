<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.navtesttask.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.navtesttask.edit')}</h2>
                <g:hasErrors bean="${navTestTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${navTestTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.navtesttask.from')}:</td>
                                    <td><g:contestday var="${navTestTaskInstance?.navtest?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${navTestTaskInstance?.navtest?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:navtest var="${navTestTaskInstance?.navtest}" link="${createLink(controller:'navTest',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${navTestTaskInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:navTestTaskInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="${{it.name()}}" from="${Route.list()}" name="route.id" value="${navTestTaskInstance?.route?.id}" ></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="TAS" name="TAS" value="${fieldValue(bean:navTestTaskInstance,field:'TAS')}" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:navTestTaskInstance,field:'direction')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.speed')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:navTestTaskInstance,field:'speed')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${navTestTaskInstance?.id}" />
                        <input type="hidden" name="version" value="${navTestTaskInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>