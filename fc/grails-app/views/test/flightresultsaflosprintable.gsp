<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flightresults.aflos')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.flightresults.aflos')} ${testInstance.GetStartNum()}</h2>
                <g:if test="${!testInstance.flightTestComplete}">
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()}) [${message(code:'fc.provisional')}]</h3>
                </g:if>
                <g:else>
	                <h3>${testInstance?.task.name()} (${message(code:'fc.version')} ${testInstance.GetFlightTestVersion()})</h3>
                </g:else>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.taskAircraft}">
                                            ${testInstance.taskAircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.taskAircraft}">
		                                    ${testInstance.taskAircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${CoordResult.countByTest(testInstance)}" >
                            <br/>
                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <thead>
                                    <tr>
                                        <th class="table-head">${message(code:'fc.title')}</th>
                                        <th class="table-head">${message(code:'fc.aflos.checkpoint')}</th>
                                        <th colspan="2" class="table-head">${message(code:'fc.cptime')}</th>
                                        <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
                                            <th class="table-head">${message(code:'fc.procedureturn')}</th>
                                        </g:if>
                                        <g:if test="${true || (testInstance.GetFlightTestBadCoursePoints() > 0)}">
                                            <th class="table-head">${message(code:'fc.badcoursenum')}</th>
                                        </g:if>
                                        <g:if test="${true || (testInstance.GetFlightTestMinAltitudeMissedPoints() > 0)}">
                                            <th class="table-head">${message(code:'fc.altitude')}</th>
                                        </g:if>
                                    </tr>
                                    <tr>
                                        <th/>
                                        <th/>
                                        <th>${message(code:'fc.test.results.plan')}</th>
                                        <th>${message(code:'fc.test.results.measured')}</th>
                                        <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
                                            <th/>
                                        </g:if>
                                        <g:if test="${true || (testInstance.GetFlightTestBadCoursePoints() > 0)}">
                                            <th/>
                                        </g:if>
                                        <g:if test="${true || (testInstance.GetFlightTestMinAltitudeMissedPoints() > 0)}">
                                            <th/>
                                        </g:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <g:each var="coordResultInstance" in="${CoordResult.findAllByTest(testInstance,[sort:"id"])}">
                                        <g:if test="${lastCoordResultInstance}">
                                            <tr>
                                                <td>${lastCoordResultInstance.titleCode()}</td>
                                                <td>${lastCoordResultInstance.mark}</td>
                                                <td>${FcMath.TimeStr(lastCoordResultInstance.planCpTime)}</td>
                                                <g:if test="${lastCoordResultInstance.resultCpNotFound}">
                                                    <td>-</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${FcMath.TimeStr(lastCoordResultInstance.resultCpTime)}</td>
                                                </g:else>
                                                <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
	                                                <g:if test="${coordResultInstance.planProcedureTurn}">
	                                                    <g:if test="${coordResultInstance.resultProcedureTurnEntered}">
	                                                        <g:if test="${coordResultInstance.resultProcedureTurnNotFlown}">
	                                                            <td>${message(code:'fc.flighttest.procedureturnnotflown.short')}</td>
	                                                        </g:if>
	                                                        <g:else>
                                                                <td>${message(code:'fc.flighttest.procedureturnflown.short')}</td>
	                                                        </g:else>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td/>
	                                                    </g:else>
	                                                </g:if>
	                                                <g:else>
	                                                    <td/>
	                                                </g:else>
	                                            </g:if>
                                                <g:if test="${true || (testInstance.GetFlightTestBadCoursePoints() > 0)}">
	                                                <g:if test="${lastCoordResultInstance.resultEntered}">
                                                        <g:if test="${lastCoordResultInstance.type.IsBadCourseCheckCoord()}">
	                                                        <td>${lastCoordResultInstance.resultBadCourseNum}</td>
	                                                    </g:if>
	                                                    <g:else>
	                                                        <td/>
	                                                    </g:else>
	                                                </g:if>
	                                                <g:else>
	                                                    <td/>
	                                                </g:else>
	                                            </g:if>
                                                <g:if test="${true || (testInstance.GetFlightTestMinAltitudeMissedPoints() > 0)}">
                                                    <g:if test="${lastCoordResultInstance.resultCpNotFound}">
                                                        <td>-</td>
                                                    </g:if>
                                                    <g:else>
                                                        <td>${lastCoordResultInstance.resultAltitude}${message(code:'fc.foot')}</td>
                                                    </g:else>
	                                            </g:if>
                                            </tr>
                                        </g:if>
                                        <g:set var="lastCoordResultInstance" value="${coordResultInstance}" />
                                    </g:each>
                                    <g:if test="${lastCoordResultInstance}">
                                        <tr>
                                            <td>${lastCoordResultInstance.titleCode()}</td>
                                            <td>${lastCoordResultInstance.mark}</td>
                                            <td>${FcMath.TimeStr(lastCoordResultInstance.planCpTime)}</td>
                                            <g:if test="${lastCoordResultInstance.resultCpNotFound}">
                                                <td>-</td>
                                            </g:if>
                                            <g:else>
                                                <td>${FcMath.TimeStr(lastCoordResultInstance.resultCpTime)}</td>
                                            </g:else>
                                            <g:if test="${testInstance.GetFlightTestProcedureTurnNotFlownPoints() > 0}">
                                                <td/>
                                            </g:if>
                                            <g:if test="${true || (testInstance.GetFlightTestBadCoursePoints() > 0)}">
	                                            <g:if test="${lastCoordResultInstance.resultEntered}">
                                                    <g:if test="${lastCoordResultInstance.type.IsBadCourseCheckCoord()}">
	                                                    <td>${lastCoordResultInstance.resultBadCourseNum}</td>
	                                                </g:if>
	                                                <g:else>
	                                                   <td/>
	                                                </g:else>
	                                            </g:if>
	                                            <g:else>
	                                               <td/>
	                                            </g:else>
	                                        </g:if>
                                            <g:if test="${true || (testInstance.GetFlightTestMinAltitudeMissedPoints() > 0)}">
                                                <g:if test="${lastCoordResultInstance.resultCpNotFound}">
                                                    <td>-</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${lastCoordResultInstance.resultAltitude}${message(code:'fc.foot')}</td>
                                                </g:else>
	                                        </g:if>
                                        </tr>
                                    </g:if>
                                </tbody>
                            </table>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>