<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aflos.select')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aflos.select')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="['id':testInstance.id]">
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td>${testInstance.crew.startNum}: <g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${testInstance.aflosStartNum}"> (${message(code:'fc.aflos')}: ${testInstance.aflosStartNum})</g:if></td>
                                </tr>
                                <g:if test="${testInstance.crew.team}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.team')}:</td>
                                        <td><g:team var="${testInstance.crew.team}" link="${createLink(controller:'team',action:'edit')}"/></td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.resultclass')}:</td>
                                        <td><g:resultclass var="${testInstance.crew.resultclass}" link="${createLink(controller:'resultClass',action:'edit')}"/></td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <td>
                                        <g:if test="${testInstance.taskAircraft}">
                                            <g:aircraft var="${testInstance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/>
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft.type')}:</td>
                                    <g:if test="${testInstance.taskAircraft}">
                                        <td>${testInstance.taskAircraft.type}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
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
                                    <g:set var="last_startnum" value="${testInstance.GetAFLOSStartNum()}" />
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.crewnames.name')}:</label></td>
									<g:if test="${testInstance.crew.contest.aflosTest}">
                                        <td><g:select from="${AflosCrewNames.aflostest.findAllByPointsNotEqual(0,[sort:"id"])}" name="afloscrewnames.startnum" value="${last_startnum}" optionKey="startnum" optionValue="${{it.viewName()}}" ></g:select></td>
									</g:if>
									<g:elseif test="${testInstance.crew.contest.aflosUpload}">
                                        <td><g:select from="${AflosCrewNames.aflosupload.findAllByPointsNotEqual(0,[sort:"id"])}" name="afloscrewnames.startnum" value="${last_startnum}" optionKey="startnum" optionValue="${{it.viewName()}}" ></g:select></td>
									</g:elseif>
									<g:else>
                                        <td><g:select from="${AflosCrewNames.aflos.findAllByPointsNotEqual(0,[sort:"id"])}" name="afloscrewnames.startnum" value="${last_startnum}" optionKey="startnum" optionValue="${{it.viewName()}}" ></g:select></td>
									</g:else>
                                </tr>
                                <tr>
                                    <g:set var="last_name" value="${testInstance.flighttestwind.flighttest.route.mark}" />
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.routedefs.routename')}:</label></td>
                                    <g:if test="${testInstance.crew.contest.aflosTest}">
                                        <td><g:select from="${AflosRouteNames.aflostest.findAllByNameIsNotNullAndIdNotEqual(0,[sort:"id"])}" name="aflosroutenames.name" value="${last_name}" optionKey="name" optionValue="${{it.viewName()}}" ></g:select></td>
                                    </g:if>
                                    <g:elseif test="${testInstance.crew.contest.aflosUpload}">
                                        <td><g:select from="${AflosRouteNames.aflosupload.findAllByNameIsNotNullAndIdNotEqual(0,[sort:"id"])}" name="aflosroutenames.name" value="${last_name}" optionKey="name" optionValue="${{it.viewName()}}" ></g:select></td>
                                    </g:elseif>
                                    <g:else>
                                        <td><g:select from="${AflosRouteNames.aflos.findAllByNameIsNotNullAndIdNotEqual(0,[sort:"id"])}" name="aflosroutenames.name" value="${last_name}" optionKey="name" optionValue="${{it.viewName()}}" ></g:select></td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.aflos.noremove.existingdata')}:</label></td>
                                    <td><g:checkBox name="no_remove_existing_data" value="${false}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:actionSubmit action="importaflosresults" value="${message(code:'fc.import')}" />
                        <g:actionSubmit action="cancelaflosresults" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>