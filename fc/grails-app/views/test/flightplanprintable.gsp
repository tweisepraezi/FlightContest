<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}</title>
    </head>
    <body>
        <h2>${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}</h2>
        <h3>${testInstance.task.printName()} (${message(code:'fc.test.timetable.version')} ${testInstance.task.GetTimeTableVersion()}<g:if test="${testInstance.task.GetTimeTableVersion() != testInstance.timetableVersion}">, ${message(code:'fc.test.timetable.unchangedversion')} ${testInstance.timetableVersion}</g:if>)</h3>
        <g:form>
            <g:set var="show_distance" value="${true}" />
            <g:set var="show_truetrack" value="${true}" />
            <g:set var="show_trueheading" value="${true}" />
            <g:set var="show_groundspeed" value="${true}" />
            <g:set var="show_local_time" value="${true}" />
            <g:set var="show_elapsed_time" value="${false}" />
            <g:if test="${contestInstance.printStyle.contains('--flightplan')}">
	            <g:if test="${contestInstance.printStyle.contains('hide-distance')}">
	                <g:set var="show_distance" value="${false}" />
	            </g:if>
	            <g:if test="${contestInstance.printStyle.contains('hide-truetrack')}">
	                <g:set var="show_truetrack" value="${false}" />
	            </g:if>
	            <g:if test="${contestInstance.printStyle.contains('hide-trueheading')}">
	                <g:set var="show_trueheading" value="${false}" />
	            </g:if>
	            <g:if test="${contestInstance.printStyle.contains('hide-groundspeed')}">
	                <g:set var="show_groundspeed" value="${false}" />
	            </g:if>
	            <g:if test="${contestInstance.printStyle.contains('disable-local-time')}">
	                <g:set var="show_local_time" value="${false}" />
	            </g:if>
	            <g:if test="${contestInstance.printStyle.contains('show-elapsed-time')}">
	                <g:set var="show_elapsed_time" value="${true}" />
	            </g:if>
	        </g:if>
            <g:set var="show_submission_time" value="${false}" />
            <g:if test="${contestInstance.printStyle.contains('--submission')}">
                <g:set var="show_submission_time" value="${true}" />
            </g:if>
            <g:crewTestPrintable t="${testInstance}"/>
            <br/>
            <table class="info">
                <tbody>
                    <tr class="wind">
                        <td class="title">${message(code:'fc.wind')}:</td>
                        <td class="value">
                         <g:if test="${testInstance.flighttestwind}">
                             <g:windtextprintable var="${testInstance.flighttestwind.wind}" />
                         </g:if> <g:else>
                             ${message(code:'fc.noassigned')}                                    
                         </g:else>
                        </td>
                    </tr>
                    <tr class="planning">
                        <td class="title">
                            <g:if test="${testInstance.task.planningTestDuration == 0}">
                                ${message(code:'fc.test.planning.publish')}:
                            </g:if>
                            <g:else>
                                ${message(code:'fc.test.planning')}:
                            </g:else>
                        </td>
                        <td class="value">
                            <g:if test="${testInstance.timeCalculated}">
                                <g:if test="${testInstance.task.planningTestDuration > 0}">
                                    ${testInstance.testingTime?.format('HH:mm')} - ${testInstance.endTestingTime?.format('HH:mm')}
                                </g:if>
                                <g:else>
                                    ${testInstance.testingTime?.format('HH:mm')}
                                </g:else>
                            </g:if>
                            <g:else>
                                 ${message(code:'fc.nocalculated')}
                            </g:else>
                        </td>
                    </tr>
                    <g:if test="${show_elapsed_time && !show_local_time}">
	                    <tr class="takeoff">
	                        <td class="title">${message(code:'fc.test.takeoff')}:</td>
	                        <td class="value">
		                        <g:if test="${testInstance.timeCalculated}">
		                            ${testInstance.takeoffTime.format('HH:mm')}<g:if test="${testInstance.takeoffTimeWarning}"> !</g:if>
		                        </g:if>
		                        <g:else>
	                                ${message(code:'fc.nocalculated')}
	                            </g:else>
                            </td>
	                    </tr>
                    </g:if>
                    <g:if test="${show_submission_time}">
                        <tr class="submission">
                            <td class="title">${message(code:'fc.test.submission.latest')}:</td>
                            <td class="value">
                                <g:if test="${testInstance.timeCalculated}">
                                    ${testInstance.GetMaxSubmissionTime().format('HH:mm:ss')}
                                </g:if>
                                <g:else>
                                    ${message(code:'fc.nocalculated')}
                                </g:else>
                            </td>
                        </tr>
                    </g:if>
                </tbody>
            </table>
            <g:if test="${TestLegFlight.countByTest(testInstance)}" >
                <g:set var="end_curved" value="${false}" />
                <br/>
                <table class="flightplanlist">
                    <thead>
                        <tr>
                            <th>${message(code:'fc.number')}</th>
                            <th>${message(code:'fc.distance')}</th>
                            <th>${message(code:'fc.truetrack')}</th>
                            <th>${message(code:'fc.trueheading')}</th>
                            <th>${message(code:'fc.groundspeed')}</th>
                            <th>${message(code:'fc.legtime')}</th>
                            <th>${message(code:'fc.tpname')}</th>
                            <g:if test="${show_local_time}">
                                <th>${message(code:'fc.time.local')}</th>
                            </g:if>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="value" id="${message(code:CoordType.TO.code)}">
                            <td class="num"/>
                            <td class="distance"/>
                            <td class="truetrack"/>
                            <td class="trueheading"/>
                            <td class="groundspeed"/>
                            <g:if test="${show_elapsed_time}">
                                <td class="legtime">${FcMath.TimeStr(Date.parse("HH:mm","00:00"))}${message(code:'fc.time.h')}</td>
                            </g:if>
                            <g:else>
                                <td class="legtime"/>
                            </g:else>
                            <td class="tpname">${message(code:CoordType.TO.code)}</td>
                            <g:if test="${show_local_time}">
	                            <g:if test="${testInstance.timeCalculated}">
	                                <td class="tptime">${FcMath.TimeStr(testInstance.takeoffTime)}<g:if test="${testInstance.takeoffTimeWarning}"> !</g:if></td>
	                            </g:if> 
	                            <g:else>
	                                <td class="tptime">${message(code:'fc.nocalculated')}</td>
	                            </g:else>
	                        </g:if>
                        </tr>
                        <tr class="value" id="${message(code:CoordType.SP.code)}">
                            <td class="num"/>
                            <td class="distance"/>
                            <td class="truetrack"/>
                            <td class="trueheading"/>
                            <td class="groundspeed"/>
                            <g:if test="${show_elapsed_time}">
                                <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.startTime))}${message(code:'fc.time.h')}</td>
                            </g:if>
                            <g:else>
                                <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.startTime))}${message(code:'fc.time.h')}</td>
                            </g:else>
                            <td class="tpname">${message(code:CoordType.SP.code)}</td>
                            <g:if test="${show_local_time}">
	                            <g:if test="${testInstance.timeCalculated}">
	                                <td class="tptime">${FcMath.TimeStr(testInstance.startTime)}</td>
	                            </g:if>
	                            <g:else>
	                                <td class="tptime">${message(code:'fc.nocalculated')}</td>
	                            </g:else>
	                        </g:if>
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
                            <g:if test="${testlegflight_instance.coordTitle.type != CoordType.SECRET}">
                                <g:set var="leg_no" value="${leg_no+1}" />
                                <g:if test="${leg_procedureturn}">
                                    <tr class="procedureturn">
                                        <td colspan="8">${message(code:'fc.procedureturn')} (${testlegflight_instance.test.task.procedureTurnDuration}${message(code:'fc.time.min')})</td>
                                    </tr>
                                </g:if>
                                <tr class="value" id="${testlegflight_instance.coordTitle.titlePrintCode()}">
                                    <td class="num">${leg_no}</td>
                                    <g:if test="${show_distance}">
                                        <td class="distance">${FcMath.DistanceStr(leg_distance)}${message(code:'fc.mile')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="distance"/>
                                    </g:else>
                                    <g:if test="${show_truetrack}">
                                        <td class="truetrack"><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.GradStr(leg_plantruetrack)}${message(code:'fc.grad')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="truetrack"/>
                                    </g:else>
                                    <g:if test="${show_trueheading}">
                                        <td class="trueheading"><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.GradStr(leg_plantrueheading)}${message(code:'fc.grad')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="trueheading"/>
                                    </g:else>
                                    <g:if test="${show_groundspeed}">
                                        <td class="groundspeed"><g:if test="${testlegflight_instance.endCurved}">${message(code:'fc.endcurved')}</g:if>${FcMath.SpeedStr_Flight(leg_plangroundspeed)}${message(code:'fc.knot')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="groundspeed"/>
                                    </g:else>
                                    <g:if test="${show_elapsed_time}">
                                        <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,leg_time))}${message(code:'fc.time.h')}</td>
                                    </g:if>
                                    <g:else>
                                        <td class="legtime">${FcMath.TimeStr(leg_duration)}${message(code:'fc.time.h')}</td>
                                    </g:else>
                                    <td class="tpname">${testlegflight_instance.coordTitle.titlePrintCode()}</td>
                                    <g:if test="${show_local_time}">
                                        <td class="tptime">${FcMath.TimeStr(leg_time)}</td>
	                                </g:if>
                                </tr>
                                <g:set var="leg_distance" value="${new BigDecimal(0)}" />
                                <g:set var="leg_duration" value="${new BigDecimal(0)}" />
                                <g:set var="leg_firstvalue" value="${true}"/>
                            </g:if>
                            <g:else>
                                <g:set var="leg_firstvalue" value="${false}"/>
                            </g:else>
                            <g:if test="${testlegflight_instance.endCurved}">
                                <g:set var="end_curved" value="${true}" />
                            </g:if>
                        </g:each>
                        <tr class="value" id="${message(code:CoordType.LDG.code)}">
                            <td class="num"/> 
                            <td class="distance"/>
                            <td class="truetrack"/>
                            <td class="trueheading"/> 
                            <td class="groundspeed"/>
                            <g:if test="${show_elapsed_time}">
                                <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.maxLandingTime))}${message(code:'fc.time.h')}</td>
                            </g:if>
                            <g:else>
                                <td class="legtime">${FcMath.TimeStr(FcMath.TimeDiff(leg_time,testInstance.maxLandingTime))}${message(code:'fc.time.h')}</td>
                            </g:else>
                            <td class="tpname">${message(code:CoordType.LDG.code)}</td>
                            <g:if test="${show_local_time}"> 
                                <td class="tptime">${FcMath.TimeStr(testInstance.maxLandingTime)}</td>
                            </g:if>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr class="summary">
                            <td/>
                            <td class="distance" colspan="4">${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')} ${message(code:'fc.distance.sp2fp')}</td>
                            <td class="legtime" colspan="3"><g:if test="${!show_elapsed_time}">${FcMath.TimeStr(FcMath.TimeDiff(testInstance.takeoffTime,testInstance.maxLandingTime))}${message(code:'fc.time.h')} ${message(code:'fc.legtime.total')}</g:if></td>
                        </tr>
                    </tfoot>
                </table>
                <br/>
                <table class="info">
                    <tbody>
                        <g:if test="${end_curved}">
                            <tr class="endcurved">
                                <td class="title">${message(code:'fc.endcurved')}</td>
                                <td class="separator"></td>
                                <td class="value">${message(code:'fc.endcurved.info')}</td>
                            </tr>
                        </g:if>
                        <tr class="landinglatest">
                            <td class="title">${message(code:CoordType.LDG.code)}</td>
                            <td class="separator"></td>
                            <td class="value">${message(code:'fc.test.landing.latest')}</td>
                        </tr>
                    </tbody>
                </table>
            </g:if>
        </g:form>
    </body>
</html>