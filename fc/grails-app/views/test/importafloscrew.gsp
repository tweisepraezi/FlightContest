<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.import')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aflos.import')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="['id':testInstance.id]">
                        <g:crewDetails t="${testInstance}" />
                        <table>
                            <tbody>
                                <tr>
                                    <g:set var="last_startnum" value="${testInstance.aflosStartNum}" />
                                    <g:if test="${!last_startnum}">
                                        <g:set var="last_startnum" value="${testInstance.crew.startNum}" />
                                    </g:if>
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.crewnames.crewnumber')}:</label></td>
                                    <td><g:select from="${AflosTools.GetAflosCrewNames(testInstance.crew.contest)}" name="afloscrewnames.startnum" value="${last_startnum}" optionKey="startnum" optionValue="${{it.viewName()}}" noSelection="${['null':'']}"></g:select></td>
                                </tr>
                                <tr>
                                    <g:set var="last_name" value="${testInstance.flighttestwind.flighttest.route.mark}" />
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.routedefs.routename')}:</label></td>
                                    <td><g:select from="${AflosTools.GetAflosRouteNames2(testInstance.crew.contest)}" name="aflosroutenames.name" value="${last_name}" optionKey="name" optionValue="${{it.viewName()}}" ></g:select></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:actionSubmit action="calculateaflosresults" value="${message(code:'fc.aflos.calculate')}" tabIndex="1"/>
                        <g:if test="${testInstance.IsAFLOSResultsPossible()}">
                            <g:actionSubmit action="importaflosresults" value="${message(code:'fc.aflos.importcalculation')}" tabIndex="2"/>
                        </g:if>
                        <g:actionSubmit action="cancelimportcrew" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>