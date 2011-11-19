<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.show')} ${testInstance.viewpos+1}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.show')} ${testInstance.viewpos+1}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post" params="${[id:testInstance.id,'positionsReturnAction':positionsReturnAction,'positionsReturnController':positionsReturnController,'positionsReturnID':positionsReturnID]}" >
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'edit')}"/></td>
                                </tr>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.aircraft')}:</td>
                                    <td>
                                    	<g:if test="${testInstance.crew.aircraft}">
                                    		<g:aircraft var="${testInstance.crew.aircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/>
	                                    </g:if> <g:else>
    	                                    ${message(code:'fc.noassigned')}
        	                            </g:else>
        	                        </td>                    
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtest')}:</td>
                                    <g:if test="${testInstance.planningtesttask?.planningtest}">
                                        <td><g:planningtest var="${testInstance.planningtesttask.planningtest}" link="${createLink(controller:'planningTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask')}:</td>
                                    <g:if test="${testInstance.planningtesttask}">
                                        <td><g:planningtesttask var="${testInstance.planningtesttask}" link="${createLink(controller:'planningTestTask',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest')}:</td>
                                    <g:if test="${testInstance.flighttestwind?.flighttest}">
                                        <td><g:flighttest var="${testInstance.flighttestwind.flighttest}" link="${createLink(controller:'flightTest',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttestwind')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else>
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.planning')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${testInstance.testingTime.format('HH:mm')} - ${testInstance.endTestingTime.format('HH:mm')}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.takeoff')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <g:if test="${testInstance.takeoffTimeWarning}">
                                            <td class="errors">${FcMath.TimeStr(testInstance.takeoffTime)}${message(code:'fc.time.h')} !</td>
                                        </g:if> <g:else>
                                            <td>${FcMath.TimeStr(testInstance.takeoffTime)}</td>
                                        </g:else> 
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.startpoint')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${FcMath.TimeStr(testInstance.startTime)}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.finishpoint')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <td>${FcMath.TimeStr(testInstance.finishTime)}</td>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.arrival')}:</td>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <g:if test="${testInstance.arrivalTimeWarning}">
                                            <td class="errors">${testInstance.arrivalTime.format('HH:mm')}${message(code:'fc.time.h')} !</td>
                                        </g:if> <g:else>
                                            <td>${testInstance.arrivalTime.format('HH:mm')}</td>
                                        </g:else>
                                    </g:if> <g:else>
                                        <td/>
                                    </g:else> 
                                </tr>

                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningresults')}:</td>
                                    <td>${testInstance.planningTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flightresults')}:</td>
                                    <td>${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.observationresults')}:</td>
                                    <td>${testInstance.observationTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.landingresults')}:</td>
                                    <td>${testInstance.landingTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.results.summary')}:</td>
                                    <td>${testInstance.taskPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.test.results.position')}:</td>
                                    <g:if test="${testInstance.taskPosition}">
                                        <td>${testInstance.taskPosition}</td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.test.results.position.none')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>