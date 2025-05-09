<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${testInstance.GetTitle(ResultType.Flight)}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${testInstance.GetTitle(ResultType.Flight)}</h2>
                <div class="block" id="forms" >
                    <g:form id="${testInstance.id}" method="post">
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${testInstance.GetNextTestID(ResultType.Flight,session)}"/>
						<g:set var="prev_id" value="${testInstance.GetPrevTestID(ResultType.Flight,session)}"/>
                        <g:set var="is_loggerdata" value="${testInstance.IsLoggerData()}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
									<g:set var="upload_job_status" value="${testInstance.GetFlightTestUploadJobStatus()}"/>
                                    <g:if test="${upload_job_status == UploadJobStatus.Waiting}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email.png')}"/>
                                        </td>
                                    </g:if>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Sending}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email-sending.png')}"/>
                                        </td>
                                    </g:elseif>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Error}"> 
                                        <td style="width:1%;"> 
                                            <img src="${createLinkTo(dir:'images',file:'email-error.png')}"/>
                                        </td>
                                    </g:elseif>
                                    <g:elseif test="${upload_job_status == UploadJobStatus.Done}">
                                        <g:set var="email_links" value="${NetTools.EMailLinks(testInstance.GetFlightTestUploadLink())}" />
                                        <td style="width:1%;"> 
                                            <a href="${email_links.map}" target="_blank"><img src="${createLinkTo(dir:'images',file:'map.png')}"/></a>
                                        </td>
                                        <td style="width:1%;"> 
                                            <a href="${email_links.kmz}" target="_blank"><img src="${createLinkTo(dir:'images',file:'kmz.png')}"/></a>
                                        </td>
                                    </g:elseif>
                                    <td style="width:1%;">
                                        <a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a>
                                    </td>
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
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route')}:</td>
                                    <g:if test="${testInstance.flighttestwind?.flighttest}">
                                        <td><g:route var="${testInstance.flighttestwind.flighttest.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.wind')} (${message(code:'fc.runway.setoffset')}):</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:flighttestwind var="${testInstance.flighttestwind}" link="${createLink(controller:'flightTestWind',action:'edit')}"/></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
                        <g:flightTestLoggerResults t="${testInstance}" showAll="false" allowJudgeActions="${(!testInstance.flightTestComplete).toString()}" />
                        <g:flightTestResults t="${testInstance}"/>
                        <g:if test="${!testInstance.flightTestComplete}">
                            <g:set var="show_takeoffmissed" value="${true}"/>
                            <g:set var="show_landingtolate" value="${true}"/>
                            <g:if test="${CoordResult.countByTest(testInstance)}" >
	                            <g:each var="coordResultInstance" in="${CoordResult.findAllByTest(testInstance,[sort:"id"])}">
	                                <g:if test="${coordResultInstance.type == CoordType.TO}">
	                                    <g:if test="${testInstance.IsFlightTestCheckTakeOff() || testInstance.GetFlightTestTakeoffCheckSeconds()}">
	                                        <g:set var="show_takeoffmissed" value="${false}"/>
	                                    </g:if>
	                                </g:if>
	                                <g:if test="${coordResultInstance.type == CoordType.LDG}">
	                                    <g:if test="${testInstance.IsFlightTestCheckLanding()}">
	                                        <g:set var="show_landingtolate" value="${false}"/>
	                                    </g:if>
	                                </g:if>
	                            </g:each>
                            </g:if>
                            <fieldset>
		                        <p>
		                        	<g:if test="${testInstance.GetFlightTestTakeoffMissedPoints() > 0}">
                                        <g:if test="${show_takeoffmissed}">
					                        <div>
				        	                	<g:checkBox name="flightTestTakeoffMissed" value="${testInstance.flightTestTakeoffMissed}" tabIndex="${ti[0]++}"/>
												<label>${message(code:'fc.flighttest.takeoffmissed')}</label>
				    	                    </div>
                                        </g:if>
			    	                </g:if>
                                    <g:if test="${testInstance.GetFlightTestLandingToLatePoints() > 0}">
                                        <g:if test="${show_landingtolate}">
                                            <div>
                                                <g:checkBox name="flightTestLandingTooLate" value="${testInstance.flightTestLandingTooLate}" tabIndex="${ti[0]++}"/>
                                                <label>${message(code:'fc.flighttest.landingtolate')}</label>
                                            </div>
                                        </g:if>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestExitRoomTooLatePoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestExitRoomTooLate" value="${testInstance.flightTestExitRoomTooLate}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.planningtest.exitroomtolate')}</label>
                                        </div>
                                    </g:if>
		                        	<g:if test="${testInstance.GetFlightTestGivenToLatePoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="flightTestGivenTooLate" value="${testInstance.flightTestGivenTooLate}" tabIndex="${ti[0]++}"/>
											<label>${message(code:'fc.flighttest.giventolate')}</label>
			    	                    </div>
			    	                </g:if>
		                        	<g:if test="${testInstance.GetFlightTestBadCourseStartLandingPoints() > 0}">
                                        <g:if test="${!testInstance.GetFlightTestBadCourseStartLandingSeparatePoints()}">
                                            <div>
                                                <g:checkBox name="flightTestBadCourseStartLanding" value="${testInstance.flightTestBadCourseStartLanding}" tabIndex="${ti[0]++}"/>
                                                <label>${message(code:'fc.flighttest.badcoursestartlanding')}</label>
                                            </div>
                                        </g:if>
                                        <g:else>
                                            <div>
                                                <g:checkBox name="flightTestBadCourseStart" value="${testInstance.flightTestBadCourseStart}" tabIndex="${ti[0]++}"/>
                                                <label>${message(code:'fc.flighttest.badcoursestart')}</label>
                                            </div>
                                            <div>
                                                <g:checkBox name="flightTestBadCourseLanding" value="${testInstance.flightTestBadCourseLanding}" tabIndex="${ti[0]++}"/>
                                                <label>${message(code:'fc.flighttest.badcourselanding')}</label>
                                            </div>
                                        </g:else>
			    	                </g:if>
                                    <g:if test="${testInstance.GetFlightTestSafetyAndRulesInfringementPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestSafetyAndRulesInfringement" value="${testInstance.flightTestSafetyAndRulesInfringement}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.flighttest.safetyandrulesinfringement')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestInstructionsNotFollowedPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestInstructionsNotFollowed" value="${testInstance.flightTestInstructionsNotFollowed}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.flighttest.instructionsnotfollowed')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestFalseEnvelopeOpenedPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestFalseEnvelopeOpened" value="${testInstance.flightTestFalseEnvelopeOpened}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.flighttest.falseenvelopeopened')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestSafetyEnvelopeOpenedPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestSafetyEnvelopeOpened" value="${testInstance.flightTestSafetyEnvelopeOpened}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.flighttest.safetyenvelopeopened')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestFrequencyNotMonitoredPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestFrequencyNotMonitored" value="${testInstance.flightTestFrequencyNotMonitored}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.flighttest.frequencynotmonitored')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestForbiddenEquipmentPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestForbiddenEquipment" value="${testInstance.flightTestForbiddenEquipment}" tabIndex="${ti[0]++}"/>
                                            <label>${message(code:'fc.flighttest.forbiddenequipment')}</label>
                                        </div>
                                    </g:if>
		                        </p>
                                <p>
                                    <label>${message(code:'fc.flighttest.otherpenalties')}* [${message(code:'fc.points')}]:</label>
                                    <br/>
                                    <input type="text" id="flightTestOtherPenalties" name="flightTestOtherPenalties" value="${fieldValue(bean:testInstance,field:'flightTestOtherPenalties')}" tabIndex="${ti[0]++}"/>
                                </p>
		                    </fieldset>
		                </g:if>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flightresults.checkpointpenalties')}:</td>
                                    <td>${testInstance.flightTestCheckPointPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                                <g:if test="${testInstance.flightTestTakeoffMissed}">
                                	<tr>
                                    	<td class="detailtitle">${message(code:'fc.flighttest.takeoffmissed')}:</td>
                                        <td>${testInstance.GetFlightTestTakeoffMissedPoints()} ${message(code:'fc.points')}</td>
                                	</tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestLandingTooLate}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.landingtolate')}:</td>
                                        <td>${testInstance.GetFlightTestLandingToLatePoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestExitRoomTooLate}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.planningtest.exitroomtolate')}:</td>
                                        <td>${testInstance.GetFlightTestExitRoomTooLatePoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestGivenTooLate}">
                                	<tr>
                                    	<td class="detailtitle">${message(code:'fc.flighttest.giventolate')}:</td>
                                        <td>${testInstance.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>
                                	</tr>
                                </g:if>
                                <g:if test="${!testInstance.GetFlightTestBadCourseStartLandingSeparatePoints()}">
                                    <g:if test="${testInstance.flightTestBadCourseStartLanding}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.badcoursestartlanding')}:</td>
                                            <td>${testInstance.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                </g:if>
                                <g:else>
                                    <g:if test="${testInstance.flightTestBadCourseStart}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.badcoursestart')}:</td>
                                            <td>${testInstance.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                    <g:if test="${testInstance.flightTestBadCourseLanding}">
                                        <tr>
                                            <td class="detailtitle">${message(code:'fc.flighttest.badcourselanding')}:</td>
                                            <td>${testInstance.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>
                                        </tr>
                                    </g:if>
                                </g:else>
                                <g:if test="${testInstance.flightTestSafetyAndRulesInfringement}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.safetyandrulesinfringement')}:</td>
                                        <td>${testInstance.GetFlightTestSafetyAndRulesInfringementPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestInstructionsNotFollowed}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.instructionsnotfollowed')}:</td>
                                        <td>${testInstance.GetFlightTestInstructionsNotFollowedPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestFalseEnvelopeOpened}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.falseenvelopeopened')}:</td>
                                        <td>${testInstance.GetFlightTestFalseEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestSafetyEnvelopeOpened}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.safetyenvelopeopened')}:</td>
                                        <td>${testInstance.GetFlightTestSafetyEnvelopeOpenedPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestFrequencyNotMonitored}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.frequencynotmonitored')}:</td>
                                        <td>${testInstance.GetFlightTestFrequencyNotMonitoredPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestForbiddenEquipment}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.forbiddenequipment')}:</td>
                                        <td>${testInstance.GetFlightTestForbiddenEquipmentPoints()} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestOtherPenalties != 0}">
                                    <tr>
                                        <td class="detailtitle">${message(code:'fc.flighttest.otherpenalties')}:</td>
                                        <td>${testInstance.flightTestOtherPenalties} ${message(code:'fc.points')}</td>
                                    </tr>
                                </g:if>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.penalties.total')}:</td>
                                    <g:set var="points_class" value="points"/>
                                    <g:if test="${!testInstance.flightTestPenalties}">
                                        <g:set var="points_class" value="zeropoints"/>
                                    </g:if>
                                    <td class="${points_class}">${testInstance.flightTestPenalties} ${message(code:'fc.points')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tfoot>
                                <tr>
                                    <td>
                                        <g:if test="${!testInstance.flightTestComplete}">
				                        	<g:actionSubmit action="flightresultssave" value="${message(code:'fc.save')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:if test="${testInstance.flightTestCheckPointsComplete}">
                                                <g:if test="${next_id}">
                                                    <g:actionSubmit action="flightresultsreadynext" value="${message(code:'fc.results.readynext')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                </g:if>
												<g:else>
													<g:actionSubmit action="flightresultsreadynext" value="${message(code:'fc.results.readynext')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
												</g:else>
                                                <g:actionSubmit action="flightresultsready" value="${message(code:'fc.results.ready')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
											<g:else>
												<g:actionSubmit action="flightresultsreadynext" value="${message(code:'fc.results.readynext')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
												<g:actionSubmit action="flightresultsready" value="${message(code:'fc.results.ready')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
											</g:else>
                                            <g:if test="${next_id}">
                                                <g:actionSubmit action="flightresultsgotonext" value="${message(code:'fc.results.gotonext')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
											<g:else>
												<g:actionSubmit action="flightresultsgotonext" value="${message(code:'fc.results.gotonext')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
											</g:else>
											<g:if test="${prev_id}">
												<g:actionSubmit action="flightresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
											</g:if>
											<g:else>
												<g:actionSubmit action="flightresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
											</g:else>
                                            <g:if test="${is_loggerdata}">
                                                <g:actionSubmit action="recalculatecrew" value="${message(code:'fc.flightresults.recalculate')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:else>
	                                            <g:if test="${testInstance.IsTrackerImportPossible()}">
	                                                <g:actionSubmit action="importtracker" value="${message(code:'fc.flightresults.trackerimport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
	                                            </g:if>
                                                <g:actionSubmit action="importlogger" value="${message(code:'fc.flightresults.loggerimport')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:else>
                                            <g:actionSubmit action="setnoflightresults" value="${message(code:'fc.flightresults.setnoresults')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:if test="${testInstance.IsShowMapPossible()}">
                                                <g:actionSubmit action="showofflinemap_test" value="${message(code:'fc.offlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="showmap_test" value="${message(code:'fc.onlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="gpxexport_test" value="${message(code:'fc.gpx.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="kmzexport_test" value="${message(code:'fc.kmz.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:actionSubmit action="printflightresults" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="printmeasureflightresults" value="${message(code:'fc.flightresults.printmeasurement')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:if test="${is_loggerdata}">
                                                <g:actionSubmit action="printloggerdata" value="${message(code:'fc.flightresults.printloggerdata')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:else>
                                            <g:if test="${next_id}">
                                                <g:actionSubmit action="flightresultsgotonext" value="${message(code:'fc.results.gotonext')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="flightresultsgotonext" value="${message(code:'fc.results.gotonext')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
                                            </g:else>
											<g:if test="${prev_id}">
												<g:actionSubmit action="flightresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
											</g:if>
											<g:else>
												<g:actionSubmit action="flightresultsgotoprev" value="${message(code:'fc.results.gotoprev')}" onclick="this.form.target='_self';return true;" disabled tabIndex="${ti[0]++}"/>
											</g:else>
                                            <g:actionSubmit action="flightresultsreopen" value="${message(code:'fc.results.reopen')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:if test="${testInstance.IsShowMapPossible()}">
                                                <g:actionSubmit action="showofflinemap_test" value="${message(code:'fc.offlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="showmap_test" value="${message(code:'fc.onlinemap')}" onclick="this.form.target='_blank';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="gpxexport_test" value="${message(code:'fc.gpx.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                                <g:actionSubmit action="kmzexport_test" value="${message(code:'fc.kmz.export')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:actionSubmit action="printflightresults" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="printmeasureflightresults" value="${message(code:'fc.flightresults.printmeasurement')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            <g:if test="${is_loggerdata}">
                                                <g:actionSubmit action="printloggerdata" value="${message(code:'fc.flightresults.printloggerdata')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                            </g:if>
                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="${ti[0]++}"/>
                                        </g:else>
                                    </td>
                                    <td style="width:1%;"><a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a></td>
                                </tr>
                            </tfoot>
                        </table>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>