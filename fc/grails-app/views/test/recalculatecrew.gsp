<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flightresults.recalculate')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flightresults.recalculate')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="['id':testInstance.id]">
                        <g:set var="ti" value="${[]+1}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td>${testInstance.crew.startNum}: <g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
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
                                <g:if test="${testInstance.flighttestwind.IsCorridor()}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route')}:</td>
                                        <g:if test="${testInstance.flighttestwind}">
                                            <td><g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'edit')}"/></td>
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.noassigned')}</td>
                                        </g:else>
                                    </tr>
                                </g:if>
                                <g:else>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route')}:</td>
                                        <g:if test="${testInstance.flighttestwind?.flighttest}">
                                            <td><g:route var="${testInstance.flighttestwind.GetRoute()}" link="${createLink(controller:'route',action:'show')}"/></td>
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.noassigned')}</td>
                                        </g:else>
                                    </tr>
                                </g:else>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                   <g:set var="track_points" value="${testInstance.GetTrackPoints("", "")}" />
                                   <g:set var="last_startutc" value="${testInstance.loggerDataStartUtc}" />
                                   <g:if test="${!last_startutc}">
                                       <g:set var="last_startutc" value="${track_points.startUtc}" />
                                   </g:if>
                                   <g:set var="last_endutc" value="${testInstance.loggerDataEndUtc}" />
                                   <g:if test="${!last_endutc}">
                                       <g:set var="last_endutc" value="${track_points.endUtc}" />
                                   </g:if>
                                   <tr>
                                       <td class="detailtitle"><label>${message(code:'fc.flightresults.recalculate.startlocaltime')}:</label></td>
                                       <td><g:select from="${track_points.trackPoints}" name="loggerdata_startutc" value="${last_startutc}" optionKey="utc" optionValue="${{FcTime.UTCGetLocalTime(it.utc,testInstance.task.contest.timeZone)}}"></g:select></td>
                                   </tr>
                                   <tr>
                                       <td>${message(code:CoordType.TO.code)}:</td>
                                       <td>${FcMath.TimeStr(testInstance.takeoffTime)}</td>
                                   </tr>
                                   <tr>
                                       <td>${message(code:CoordType.LDG.code)}:</td>
                                       <td>${FcMath.TimeStr(testInstance.maxLandingTime)}</td>
                                   </tr>
                                   <tr>
                                       <td class="detailtitle"><label>${message(code:'fc.flightresults.recalculate.endlocaltime')}:</label></td>
                                       <td><g:select from="${track_points.trackPoints}" name="loggerdata_endutc" value="${last_endutc}" optionKey="utc" optionValue="${{FcTime.UTCGetLocalTime(it.utc,testInstance.task.contest.timeZone)}}"></g:select></td>
                                   </tr>
                                <tr>
                                    <td class="detailtitle"><label>${message(code:'fc.flightresults.noremove.existingdata')}:</label></td>
                                    <td><g:checkBox name="no_remove_existing_data" value="${false}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:actionSubmit action="recalculateresults" value="${message(code:'fc.flightresults.recalculate.loggerdata')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${testInstance.IsTrackerImportPossible()}">
                            <g:actionSubmit action="importtracker" value="${message(code:'fc.flightresults.trackerimport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="importlogger" value="${message(code:'fc.flightresults.loggerimport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancelimportcrew" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>