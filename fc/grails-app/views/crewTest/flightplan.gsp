<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewtest.flightplan')} ${crewTestInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewtest.flightplan')} ${crewTestInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.from')}:</td>
                                    <td><g:contestday var="${crewTestInstance?.contestdaytask?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle"/>
                                    <td><g:contestdaytask var="${crewTestInstance?.contestdaytask}" link="${createLink(controller:'contestDayTask',action:'listcrewtests')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${crewTestInstance.crew}" link="${createLink(controller:'crew',action:'show')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <g:if test="${crewTestInstance.aircraft}">
                                        <td><g:aircraft var="${crewTestInstance.aircraft}" link="${createLink(controller:'aircraft',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.tas')}:</td>
                                    <td>${crewTestInstance.TAS}${message(code:'fc.knot')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${crewTestInstance.flighttestwind}">
                                        <td><g:route var="${crewTestInstance.flighttestwind.flighttest.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind')}:</td>
                                    <g:if test="${crewTestInstance.flighttestwind}">
                                        <td><g:windtext var="${crewTestInstance.flighttestwind.wind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <th colspan="2">${message(code:'fc.crewtest.timetable')}</th>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.testing')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.testingTime?.format('HH:mm')} - ${crewTestInstance.endTestingTime?.format('HH:mm')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.takeoff')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <g:if test="${crewTestInstance.takeoffTimeWarning}">
                                            <td class="errors">${crewTestInstance.takeoffTime?.format('HH:mm:ss')} !</td>
                                        </g:if> <g:else>
                                            <td>${crewTestInstance.takeoffTime?.format('HH:mm:ss')}</td>
                                        </g:else> 
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.startpoint')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.startTime?.format('HH:mm:ss')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                                <g:if test="${crewTestInstance.timeCalculated}">
                                    <g:set var="leg" value="${new Integer(0)}" />
                                    <g:set var="lastleg" value="${CrewTestLeg.countByCrewtest(crewTestInstance)}" />
                                    <g:set var="cptime" value="${crewTestInstance.startTime}" />
                                    <g:each var="crewTestLegInstance" in="${CrewTestLeg.findAllByCrewtest(crewTestInstance)}">
                                        <g:set var="leg" value="${leg+1}" />
                                        <tr>
                                            <g:if test="${leg<lastleg}">
                                                <td class="detailtitle">${message(code:'fc.crewtest.checkpoint')}${leg}:</td>
                                                <g:set var="cptime" value="${crewTestLegInstance.addtime(cptime)}" />
                                                <td>${cptime.format('HH:mm:ss')}</td>
                                            </g:if>
                                        </tr>
                                    </g:each>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.finishpoint')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <td>${crewTestInstance.finishTime?.format('HH:mm:ss')}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crewtest.arrival')}:</td>
                                    <g:if test="${crewTestInstance.timeCalculated}">
                                        <g:if test="${crewTestInstance.arrivalTimeWarning}">
                                            <td class="errors">${crewTestInstance.arrivalTime?.format('HH:mm')} !</td>
                                        </g:if> <g:else>
                                            <td>${crewTestInstance.arrivalTime?.format('HH:mm')}</td>
                                        </g:else>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${CrewTestLeg.countByCrewtest(crewTestInstance)}" >
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th colspan="7" class="table-head">${message(code:'fc.crewtestleg.list')}</th>
                                        </tr>
                                        <tr>
                                            <th>${message(code:'fc.crewtestleg.number')}</th>
                                            <th>${message(code:'fc.crewtestleg.distance')}</th>
                                            <th>${message(code:'fc.crewtestleg.procedureturn')}</th>
                                            <th>${message(code:'fc.crewtestleg.truetrack')}</th>
                                            <th>${message(code:'fc.crewtestleg.trueheading')}</th>
                                            <th>${message(code:'fc.crewtestleg.groundspeed')}</th>
                                            <th>${message(code:'fc.crewtestleg.legtime')}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:set var="leg" value="${new Integer(0)}" />
                                        <g:each var="crewTestLegInstance" in="${CrewTestLeg.findAllByCrewtest(crewTestInstance)}">
                                            <g:set var="leg" value="${leg+1}" />
                                            <tr>
                                                <td><g:crewtestleg2 var="${crewTestLegInstance}" name="${leg}" link="${createLink(controller:'crewTestLeg',action:'show')}"/></td>
                                                <td>${crewTestLegInstance.distanceFormat()}${message(code:'fc.mile')}</td>
                                                <g:if test="${crewTestLegInstance.procedureTurn}">
                                                    <td>${message(code:'fc.required')}</td>
                                                </g:if> <g:else>
                                                    <td/>
                                                </g:else>
                                                <td>${crewTestLegInstance.trueTrackFormat()}${message(code:'fc.grad')}</td>
                                                <td>${crewTestLegInstance.trueHeadingFormat()}${message(code:'fc.grad')}</td>
                                                <td>${crewTestLegInstance.groundSpeedFormat()}${message(code:'fc.knot')}</td>
                                                <td>${crewTestLegInstance.legTimeFormat()}${message(code:'fc.time.h')}</td>
                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </div>
                        </g:if>
                        <input type="hidden" name="id" value="${crewTestInstance?.id}" />
                        <g:actionSubmit action="printflightplan" value="${message(code:'fc.print')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>