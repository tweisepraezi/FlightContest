<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()} - ${testInstance?.task.name()} (${message(code:'fc.test.timetable.version')} ${testInstance.task.GetTimeTableVersion()}<g:if test="${testInstance.task.timetableModified}">*</g:if><g:if test="${testInstance.task.GetTimeTableVersion() != testInstance.timetableVersion}">, ${message(code:'fc.test.timetable.unchangedversion')} ${testInstance.timetableVersion}</g:if>)</h2>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['flightplanReturnAction':flightplanReturnAction,'flightplanReturnController':flightplanReturnController,'flightplanReturnID':flightplanReturnID]}">
						<g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${testInstance.GetNextTestID(ResultType.Flight,session)}"/>
						<g:set var="prev_id" value="${testInstance.GetPrevTestID(ResultType.Flight,session)}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listplanning')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/></td>
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
                                    <td class="detailtitle">${message(code:'fc.aircraft.registration')}:</td>
                                    <g:if test="${testInstance.taskAircraft}">
                                        <td><g:aircraft var="${testInstance.taskAircraft}" link="${createLink(controller:'aircraft',action:'edit')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>                    
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
                                    <td class="detailtitle">${message(code:'fc.tas')}:</td>
                                    <td>${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                                <g:if test="${testInstance.flighttestwind.IsCorridor()}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route')}:</td>
                                        <g:if test="${testInstance.flighttestwind}">
                                            <td><g:flighttestwindtext var="${testInstance.flighttestwind}" /></td>
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.noassigned')}</td>
                                        </g:else>
                                    </tr>
                                </g:if>
                                <g:else>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.route')}:</td>
                                        <g:if test="${testInstance.flighttestwind}">
                                            <td><g:route var="${testInstance.flighttestwind.GetRoute()}" link="${createLink(controller:'route',action:'show')}"/></td>
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.noassigned')}</td>
                                        </g:else>
                                    </tr>
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.wind.directionvelocity')}:</td>
                                        <g:if test="${testInstance.flighttestwind}">
                                            <td><g:flighttestwindtext var="${testInstance.flighttestwind}" /></td>
                                        </g:if> <g:else>
                                            <td>${message(code:'fc.noassigned')}</td>
                                        </g:else>
                                    </tr>
                                </g:else>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <g:if test="${testInstance.task.planningTestDuration == 0 || testInstance.task.preparationDuration == 0}">
                                        <td class="detailtitle">${message(code:'fc.flighttest.documentsoutput.localtime')}:</th>
                                    </g:if>
                                    <g:else>
                                        <td class="detailtitle">${message(code:'fc.test.planning.localtime')}:</td>
                                    </g:else>
                                    <g:if test="${testInstance.timeCalculated}">
                                        <g:if test="${testInstance.task.planningTestDuration == 0 || testInstance.task.preparationDuration == 0}">
                                            <td>${testInstance.GetTestingTime().format('HH:mm:ss')}</td>
                                        </g:if>
                                        <g:else>
                                            <td>${testInstance.GetTestingTime().format('HH:mm:ss')} - ${testInstance.endTestingTime?.format('HH:mm:ss')}</td>
                                        </g:else>
                                    </g:if>
                                    <g:else>
                                        <td>${message(code:'fc.nocalculated')}</td>
                                    </g:else>
                                </tr>
			                    <g:if test="${testInstance.task.flighttest.submissionMinutes}">
			                        <tr>
			                            <td class="detailtitle">${message(code:'fc.test.submission.latest')}:</td>
	                                    <g:if test="${testInstance.timeCalculated}">
                                            <td>${testInstance.GetMaxSubmissionTime().format('HH:mm:ss')}</td>
	                                    </g:if>
	                                    <g:else>
	                                        <td>${message(code:'fc.nocalculated')}</td>
	                                    </g:else>
			                        </tr>
			                    </g:if>
                            </tbody>
                        </table>
                        <g:if test="${TestLegFlight.countByTest(testInstance)}" >
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th colspan="8" class="table-head">${message(code:'fc.testlegflight.list')}</th>
                                        </tr>
                                        <tr>
                                            <th>${message(code:'fc.number')}</th>
                                            <th>${message(code:'fc.distance')}</th>
                                            <th>${message(code:'fc.truetrack')}</th>
                                            <th>${message(code:'fc.trueheading')}</th>
                                            <th>${message(code:'fc.groundspeed')}</th>
                                            <th>${message(code:'fc.legtime')}</th>
                                            <th>${message(code:'fc.tpname')}</th>
                                            <th>${message(code:'fc.time.local')}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td>${message(code:CoordType.TO.code)}</td>
                                            <g:if test="${testInstance.timeCalculated}">
                                                <g:if test="${testInstance.takeoffTimeWarning}">
                                                    <td class="errors">${FcMath.TimeStr(testInstance.takeoffTime)} !</td>
                                                </g:if> <g:else>
                                                    <td>${FcMath.TimeStr(testInstance.takeoffTime)}</td>
                                                </g:else> 
                                            </g:if>
                                            <g:else>
                                                <td>${message(code:'fc.nocalculated')}</td>
                                            </g:else>
                                        </tr>
                                        <tr>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td/>
                                            <td>${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.startTime))}${message(code:'fc.time.h')}</td>
                                            <td>${message(code:CoordType.SP.code)}</td>
                                            <g:if test="${testInstance.timeCalculated}">
                                                <td>${FcMath.TimeStr(testInstance.startTime)}</td>
                                            </g:if>
                                            <g:else>
                                                <td>${message(code:'fc.nocalculated')}</td>
                                            </g:else>
                                        </tr>
                                    
                                        <g:set var="leg_no" value="${new Integer(0)}" />
                                        <g:set var="leg_firstvalue" value="${true}"/>
                                        <g:set var="leg_procedureturn" value="${false}"/>
                                        <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                                        <g:set var="leg_plantruetrack" value="${new BigDecimal(0)}" />
                                        <g:set var="leg_plantrueheading" value="${new BigDecimal(0)}" />
                                        <g:set var="leg_plangroundspeed" value="${new BigDecimal(0)}" />
                                        <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                                        <g:set var="leg_time" value="${testInstance.startTime}" />
                                        <g:set var="total_distance" value="${new BigDecimal(0)}" />
				                        <g:set var="add_tpnum" value="${testInstance.task.flighttest.AddTPNum()}" />
				                        <g:set var="add_tpnum_index" value="${0}" />
				                        <g:set var="add_tpnum_addnum" value="${0}" />
                                        
                                        <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(testInstance,[sort:"id"])}">
                                            <g:set var="leg_distance" value="${FcMath.AddDistance(leg_distance,testlegflight_instance.planTestDistance)}" />
                                            <g:if test="${leg_firstvalue}">
                                                <g:set var="leg_procedureturn" value="${testlegflight_instance.planProcedureTurn}"/>
	                                            <g:set var="leg_plantruetrack" value="${testlegflight_instance.planTrueTrack}" />
	                                            <g:set var="leg_plantrueheading" value="${testlegflight_instance.planTrueHeading}" />
	                                            <g:set var="leg_plangroundspeed" value="${testlegflight_instance.planGroundSpeed}" />
	                                        </g:if>
                                            <g:set var="leg_duration" value="${testlegflight_instance.AddPlanLegTime(leg_duration,leg_time)}" />
                                            <g:set var="leg_time" value="${testlegflight_instance.AddPlanLegTime(leg_time)}" />
                                            <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,testlegflight_instance.planTestDistance)}" />
				                            <g:if test="${add_tpnum && add_tpnum_index < add_tpnum.tp.size() && add_tpnum.tp[add_tpnum_index] == testlegflight_instance.coordTitle.name()}">
				                                <g:set var="add_tpnum_addnum" value="${add_tpnum_addnum + add_tpnum.addNumber[add_tpnum_index]}" />
				                                <g:set var="add_tpnum_index" value="${add_tpnum_index + 1}" />
				                            </g:if>
                                            <g:if test="${testlegflight_instance.coordTitle.type != CoordType.SECRET}">
	                                            <g:set var="leg_no" value="${leg_no+1}" />
	                                            <g:if test="${leg_procedureturn}">
	                                                <tr>
	                                                    <td class="center" colspan="8">${message(code:'fc.procedureturn')} (${testlegflight_instance.test.task.procedureTurnDuration}${message(code:'fc.time.min')})</td>
	                                                </tr>
	                                            </g:if>
	                                            <tr>
	                                                <td>${leg_no}</td>
	                                                <td>${FcMath.DistanceStr(leg_distance)}${message(code:'fc.mile')}</td>
	                                                <td><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.GradStr(leg_plantruetrack)}${message(code:'fc.grad')}</td>
	                                                <td><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.GradStr(leg_plantrueheading)}${message(code:'fc.grad')}</td>
	                                                <td><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.SpeedStr_Flight(leg_plangroundspeed)}${message(code:'fc.knot')}</td>
	                                                <td>${FcMath.TimeStr(leg_duration)}${message(code:'fc.time.h')}</td>
	                                                <td>${testlegflight_instance.coordTitle.titleCode()}<g:if test="${add_tpnum_addnum}"> (${testlegflight_instance.coordTitle.titleCode2(add_tpnum_addnum)})</g:if></td>
	                                                <td>${FcMath.TimeStr(leg_time)}</td>
	                                            </tr>
	                                            <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                                                <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                                                <g:set var="leg_firstvalue" value="${true}"/>
                                            </g:if>
                                            <g:else>
                                                <g:set var="leg_firstvalue" value="${false}"/>
                                            </g:else>
                                        </g:each>
                                        <tr>
                                            <td colspan="5"/> 
                                            <td>${FcMath.TimeStr(FcMath.TimeDiff(leg_time,testInstance.maxLandingTime))}${message(code:'fc.time.h')}</td>
                                            <td>${message(code:CoordType.LDG.code)}</td>
                                            <td>${FcMath.TimeStr(testInstance.maxLandingTime)}</td>
                                        </tr>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td/>
                                            <td colspan="4">${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')} ${message(code:'fc.distance.sp2fp')}</td>
                                            <td colspan="3">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.maxLandingTime))}${message(code:'fc.time.h')} ${message(code:'fc.legtime.total')}</td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </g:if>
                        <input type="hidden" name="id" value="${testInstance?.id}"/>
						<g:if test="${next_id}">
							<g:actionSubmit action="flightplangotonext" value="${message(code:'fc.test.flightplan.gotonext')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="flightplangotonext" value="${message(code:'fc.test.flightplan.gotonext')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
						<g:if test="${prev_id}">
							<g:actionSubmit action="flightplangotoprev" value="${message(code:'fc.test.flightplan.gotoprev')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="flightplangotoprev" value="${message(code:'fc.test.flightplan.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
                        <g:actionSubmit action="printflightplan" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>