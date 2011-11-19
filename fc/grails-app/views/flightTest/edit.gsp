<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttest.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttest.edit')}</h2>
                <g:hasErrors bean="${flightTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${flightTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.from')}:</td>
                                    <td><g:contestday var="${flightTestInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${flightTestInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${flightTestInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:flightTestInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="${{it.name()}}" from="${Route.findAllByContest(flightTestInstance.contestdaytask.contestday.contest)}" name="route.id" value="${flightTestInstance?.route?.id}" ></g:select>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${flightTestInstance?.id}" />
                        <input type="hidden" name="version" value="${flightTestInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>