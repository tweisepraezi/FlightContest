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
                @top-center {
                    content: "${message(code:'fc.test.timetablejury')} - ${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion}) - ${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.timetablejury')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.timetablejury')}<g:if test="${taskInstance.printTimetableJuryPrintTitle}"> - ${taskInstance.printTimetableJuryPrintTitle}</g:if></h2>
                <h3>${taskInstance.name()} (${message(code:'fc.version')} ${taskInstance.timetableVersion})</h3>
                <div class="block" id="forms" >
                    <g:form>
                        <br/>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <g:if test="${taskInstance.printTimetableJuryNumber}">
                                        <th valign="top">${message(code:'fc.number')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryCrew}">
                                        <th valign="top">${message(code:'fc.crew')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryAircraft}">
                                        <th valign="top">${message(code:'fc.aircraft')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryAircraftType}">
                                        <th valign="top">${message(code:'fc.aircraft.type')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryAircraftColour}">
                                        <th valign="top">${message(code:'fc.aircraft.colour')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryTAS}">
                                        <th valign="top">${message(code:'fc.tas')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryTeam}">
                                        <th valign="top">${message(code:'fc.team')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.planningTestDuration == 0}">
                                        <g:if test="${taskInstance.printTimetableJuryPlanning}">
                                            <th valign="top">${message(code:'fc.test.planning.publish')}</th>
                                        </g:if>
                                    </g:if>
                                    <g:else>
                                        <g:if test="${taskInstance.printTimetableJuryPlanning}">
    	                                    <th valign="top">${message(code:'fc.test.planning')}</th>
    	                                </g:if>
                                        <g:if test="${taskInstance.printTimetableJuryPlanningEnd}">
                                            <th valign="top">${message(code:'fc.test.planning.end.short')}</th>
                                        </g:if>
                                    </g:else>
                                    <g:if test="${taskInstance.printTimetableJuryTakeoff}">
                                        <th valign="top">${message(code:'fc.test.takeoff')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryStartPoint}">
                                        <th valign="top">${message(code:'fc.test.startpoint')}</th>
                                    </g:if>
	                                <g:set var="test_instance" value="${Test.findByTask(taskInstance)}" />
	                                <g:if test="${test_instance}">
	                                    <g:set var="leg_no" value="${new Integer(0)}" />
	                                    <g:set var="leg_num" value="${TestLegFlight.countByTest(test_instance)}" />
	                                    <g:set var="print_timetablejury_checkpoints" value=",${taskInstance.printTimetableJuryCheckPoints},"/>
	                                    <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(test_instance,[sort:"id"])}">
	                                        <g:set var="leg_no" value="${leg_no+1}" />
                                            <g:set var="leg_name" value="${testlegflight_instance.coordTitle.titlePrintCode()}"/>
	                                        <g:if test="${leg_no==leg_num}">
	                                        </g:if>
	                                        <g:else>
                                                <g:set var="leg_checkpoint" value=",${leg_no},"/>
	                                            <g:if test="${print_timetablejury_checkpoints.contains(leg_checkpoint)}">
	                                                <th valign="top">${leg_name}</th>
	                                            </g:if>
	                                        </g:else>
	                                    </g:each>
	                                </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryFinishPoint}">
                                        <th valign="top">${message(code:'fc.test.finishpoint')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryLanding}">
                                        <th valign="top">${message(code:'fc.test.landing.latest')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryArrival}">
                                        <th valign="top">${message(code:'fc.test.arrival')}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryEmptyColumn1}">
                                        <th valign="top">${taskInstance.printTimetableJuryEmptyTitle1}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryEmptyColumn2}">
                                        <th valign="top">${taskInstance.printTimetableJuryEmptyTitle2}</th>
                                    </g:if>
                                    <g:if test="${taskInstance.printTimetableJuryEmptyColumn3}">
                                        <th valign="top">${taskInstance.printTimetableJuryEmptyTitle3}</th>
                                    </g:if>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                                   	<g:if test="${!test_instance.crew.disabled}">
	                                    <tr>
                                            <g:if test="${taskInstance.printTimetableJuryNumber}">
	                                           <td width="1%">${test_instance.GetStartNum()}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryCrew}">
	                                           <td width="10%">${HTMLFilter.NoWrapStr(test_instance.crew.name)}</td>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryAircraft}">
    	                                        <td width="1%">${test_instance.taskAircraft.registration}<g:if test="${test_instance.taskAircraft?.user1 && test_instance.taskAircraft?.user2}">${HTMLFilter.NoWrapStr(' *')}</g:if></td>
    	                                    </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryAircraftType}">
                                                <td width="1%">${test_instance.taskAircraft.type}</td>
                                            </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryAircraftColour}">
                                                <td width="1%">${test_instance.taskAircraft.colour}</td>
                                            </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryTAS}">
    	                                        <td width="1%">${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
    	                                     </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryTeam}">
    	                                        <td width="1%"><g:if test="${test_instance.crew.team}">${HTMLFilter.NoWrapStr(test_instance.crew.team.name)}</g:if></td>
    	                                    </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryPlanning}">
    	                                        <td width="1%">${test_instance.testingTime?.format('HH:mm')}</td>
    	                                    </g:if>
                                            <g:if test="${taskInstance.planningTestDuration > 0}">
                                                <g:if test="${taskInstance.printTimetableJuryPlanningEnd}">
	                                                <td width="1%">${test_instance.endTestingTime?.format('HH:mm')}</td>
	                                            </g:if>
	                                        </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryTakeoff}">
    	                                        <td width="1%">${test_instance.takeoffTime?.format('HH:mm')}</td>
    	                                    </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryStartPoint}">
                                                <g:if test="${taskInstance.IsFullMinute(taskInstance.risingDurationFormula)}">
    	                                           <td width="1%">${test_instance.startTime?.format('HH:mm')}</td>
    	                                        </g:if>
    	                                        <g:else>
    	                                           <td width="1%">${test_instance.startTime?.format('HH:mm:ss')}</td>
    	                                        </g:else>
    	                                    </g:if>
		                                    <g:set var="leg_no" value="${new Integer(0)}" />
		                                    <g:set var="leg_num" value="${TestLegFlight.countByTest(test_instance)}" />
                                            <g:set var="print_timetablejury_checkpoints" value=",${taskInstance.printTimetableJuryCheckPoints},"/>
                                            <g:set var="tptime" value="${test_instance.startTime}" />
		                                    <g:each var="testlegflight_instance" in="${TestLegFlight.findAllByTest(test_instance,[sort:"id"])}">
		                                        <g:set var="leg_no" value="${leg_no+1}" />
                                                <g:set var="leg_name" value="${testlegflight_instance.coordTitle.titlePrintCode()}"/>
                                                <g:set var="tptime" value="${testlegflight_instance.AddPlanLegTime(tptime)}" />
		                                        <g:if test="${leg_no==leg_num}">
		                                        </g:if>
		                                        <g:else>
                                                    <g:set var="leg_checkpoint" value=",${leg_no},"/>
	                                                <g:if test="${print_timetablejury_checkpoints.contains(leg_checkpoint)}">
                                                        <g:if test="${(testlegflight_instance.coordTitle.type == CoordType.iLDG) && taskInstance.IsFullMinute(taskInstance.iLandingDurationFormula)}">
                                                            <td width="1%">${FcMath.TimeStrShort(tptime)}</td>
                                                        </g:if>
                                                        <g:elseif test="${(testlegflight_instance.coordTitle.type == CoordType.iSP) && taskInstance.IsFullMinute(taskInstance.iRisingDurationFormula)}">
                                                            <td width="1%">${FcMath.TimeStrShort(tptime)}</td>
                                                        </g:elseif>
                                                        <g:else>
	                                                       <td width="1%">${FcMath.TimeStr(tptime)}</td>
	                                                    </g:else>
	                                                </g:if>
		                                        </g:else>
		                                    </g:each>
                                            <g:if test="${taskInstance.printTimetableJuryFinishPoint}">
    	                                        <td width="1%">${test_instance.finishTime?.format('HH:mm:ss')}</td>
    	                                    </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryLanding}">
                                                <g:if test="${taskInstance.IsFullMinute(taskInstance.maxLandingDurationFormula)}">
                                                   <td width="1%">${test_instance.maxLandingTime?.format('HH:mm')}</td>
                                                </g:if>
                                                <g:else>
    	                                           <td width="1%">${test_instance.maxLandingTime?.format('HH:mm:ss')}</td>
    	                                        </g:else>
    	                                    </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryArrival}">
                                                <g:if test="${taskInstance.IsFullMinute(taskInstance.maxLandingDurationFormula)}">
        	                                        <td width="1%">${test_instance.arrivalTime?.format('HH:mm')}</td>
                                                </g:if>
                                                <g:else>
                                                    <td width="1%">${test_instance.arrivalTime?.format('HH:mm:ss')}</td>
                                                </g:else>
    	                                    </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryEmptyColumn1}">
                                                <td width="25%"></td>
                                            </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryEmptyColumn2}">
                                                <td width="25%"></td>
                                            </g:if>
                                            <g:if test="${taskInstance.printTimetableJuryEmptyColumn3}">
                                                <td width="25%"></td>
                                            </g:if>
	                                    </tr>
	                                </g:if>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>