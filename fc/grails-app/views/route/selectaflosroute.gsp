<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.selectaflosroute')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.selectaflosroute')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.routedefs.routename')}:</label></td>
                                    <td><g:select optionKey="id" optionValue="${{it.name}}" from="${AflosRouteNames.findAllByIdNotEqual(0)}" name="aflosroutenames.id" value="${id}" ></g:select></td>
                                </tr> 
                            </tbody>
                        </table>
                        <g:actionSubmit action="importaflosroute" value="${message(code:'fc.import')}" />
                        <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>