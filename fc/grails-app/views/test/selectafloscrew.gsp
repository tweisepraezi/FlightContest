<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.crewnames.select')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aflos.crewnames.select')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="['id':testInstance.id]">
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${testInstance.crew.mark}"> (${message(code:'fc.aflos')}: ${testInstance.crew.mark})</g:if></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.flighttestwind?.flighttest}">
                                        <td><g:route var="${testInstance.flighttestwind.flighttest.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <g:set var="lastName" value="${testInstance.crew.mark}" />
                                    <g:if test="${lastName}">
                                        <g:set var="lastStartnum" value="${lastName.substring(lastName.lastIndexOf('(')+1,lastName.lastIndexOf(')'))}" />
                                    </g:if>
                                    <g:else>
                                        <g:set var="lastStartnum" value="" />
                                    </g:else>
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.crewnames.name')}:</label></td>
									<g:if test="${testInstance.crew.contest.aflosTest}">
                                        <td><g:select from="${AflosCrewNames.aflostest.findAllByNameIsNotNullAndPointsNotEqual(0)}" name="afloscrewnames.startnum" value="${lastStartnum}" optionKey="startnum" optionValue="${{it.viewName()}}" ></g:select></td>
									</g:if>
									<g:elseif test="${testInstance.crew.contest.aflosUpload}">
                                        <td><g:select from="${AflosCrewNames.aflosupload.findAllByNameIsNotNullAndPointsNotEqual(0)}" name="afloscrewnames.startnum" value="${lastStartnum}" optionKey="startnum" optionValue="${{it.viewName()}}" ></g:select></td>
									</g:elseif>
									<g:else>
                                        <td><g:select from="${AflosCrewNames.aflos.findAllByNameIsNotNullAndPointsNotEqual(0)}" name="afloscrewnames.startnum" value="${lastStartnum}" optionKey="startnum" optionValue="${{it.viewName()}}" ></g:select></td>
									</g:else>
                                </tr> 
                            </tbody>
                        </table>
                        <g:actionSubmit action="importaflosresults" value="${message(code:'fc.import')}" />
                        <g:actionSubmit action="flightresults" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>