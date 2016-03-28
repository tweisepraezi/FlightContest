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
                                    <td><g:select from="${AflosTools.GetAflosRouteNames(contestInstance)}" name="aflosroutenames.id" value="${id}" optionKey="id" optionValue="${{it.name}}" tabIndex="1"></g:select></td>
                                </tr> 
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.routedefs.secretpoint.identification')}:</label></td>
                                    <td><g:select from="${SecretCoordRouteIdentification.values()}" optionValue="${{message(code:it.titleCode)}}" value="${SecretCoordRouteIdentification.GATEWIDTH2ORSECRETMARK}" name="aflosroutenames.secretcoordrouteidentification" tabIndex="2"/></td> <!-- name="type" value="${xx}" -->
                                </tr>
                            </tbody>
                        </table>
                        <g:actionSubmit action="importaflosroute2" value="${message(code:'fc.import')}" tabIndex="3"/>
                        <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="4"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>