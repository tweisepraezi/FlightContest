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
                        <table>
                            <tbody>
                                <tr>
                                    <td><g:task var="${testInstance?.task}" link="${createLink(controller:'task',action:'listresults')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.crew')}:</td>
                                    <td><g:crew var="${testInstance.crew}" link="${createLink(controller:'crew',action:'edit')}"/><g:if test="${testInstance.aflosStartNum}"> (${message(code:'fc.aflos')}: ${testInstance.aflosStartNum})</g:if></td>
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
                                    <td class="detailtitle">${message(code:'fc.wind')}:</td>
                                    <g:if test="${testInstance.flighttestwind}">
                                        <td><g:windtext var="${testInstance.flighttestwind}" /></td>
                                    </g:if> <g:else>
                                        <td>${message(code:'fc.noassigned')}</td>
                                    </g:else>
                                </tr>
                            </tbody>
                        </table>
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
				        	                	<g:checkBox name="flightTestTakeoffMissed" value="${testInstance.flightTestTakeoffMissed}"/>
												<label>${message(code:'fc.flighttest.takeoffmissed')}</label>
				    	                    </div>
                                        </g:if>
			    	                </g:if>
                                    <g:if test="${testInstance.GetFlightTestLandingToLatePoints() > 0}">
                                        <g:if test="${show_landingtolate}">
                                            <div>
                                                <g:checkBox name="flightTestLandingTooLate" value="${testInstance.flightTestLandingTooLate}"/>
                                                <label>${message(code:'fc.flighttest.landingtolate')}</label>
                                            </div>
                                        </g:if>
                                    </g:if>
		                        	<g:if test="${testInstance.GetFlightTestBadCourseStartLandingPoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="flightTestBadCourseStartLanding" value="${testInstance.flightTestBadCourseStartLanding}"/>
											<label>${message(code:'fc.flighttest.badcoursestartlanding')}</label>
			    	                    </div>
			    	                </g:if>
		                        	<g:if test="${testInstance.GetFlightTestGivenToLatePoints() > 0}">
				                        <div>
			        	                	<g:checkBox name="flightTestGivenTooLate" value="${testInstance.flightTestGivenTooLate}"/>
											<label>${message(code:'fc.flighttest.giventolate')}</label>
			    	                    </div>
			    	                </g:if>
                                    <g:if test="${testInstance.GetFlightTestSafetyAndRulesInfringementPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestSafetyAndRulesInfringement" value="${testInstance.flightTestSafetyAndRulesInfringement}"/>
                                            <label>${message(code:'fc.flighttest.safetyandrulesinfringement')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestInstructionsNotFollowedPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestInstructionsNotFollowed" value="${testInstance.flightTestInstructionsNotFollowed}"/>
                                            <label>${message(code:'fc.flighttest.instructionsnotfollowed')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestFalseEnvelopeOpenedPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestFalseEnvelopeOpened" value="${testInstance.flightTestFalseEnvelopeOpened}"/>
                                            <label>${message(code:'fc.flighttest.falseenvelopeopened')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestSafetyEnvelopeOpenedPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestSafetyEnvelopeOpened" value="${testInstance.flightTestSafetyEnvelopeOpened}"/>
                                            <label>${message(code:'fc.flighttest.safetyenvelopeopened')}</label>
                                        </div>
                                    </g:if>
                                    <g:if test="${testInstance.GetFlightTestFrequencyNotMonitoredPoints() > 0}">
                                        <div>
                                            <g:checkBox name="flightTestFrequencyNotMonitored" value="${testInstance.flightTestFrequencyNotMonitored}"/>
                                            <label>${message(code:'fc.flighttest.frequencynotmonitored')}</label>
                                        </div>
                                    </g:if>
		                        </p>
                                <p>
                                    <label>${message(code:'fc.flighttest.otherpenalties')}* [${message(code:'fc.points')}]:</label>
                                    <br/>
                                    <input type="text" id="flightTestOtherPenalties" name="flightTestOtherPenalties" value="${fieldValue(bean:testInstance,field:'flightTestOtherPenalties')}" tabIndex="1"/>
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
                                <g:if test="${testInstance.flightTestBadCourseStartLanding}">
                                	<tr>
                                    	<td class="detailtitle">${message(code:'fc.flighttest.badcoursestartlanding')}:</td>
                                        <td>${testInstance.GetFlightTestBadCourseStartLandingPoints()} ${message(code:'fc.points')}</td>
                                	</tr>
                                </g:if>
                                <g:if test="${testInstance.flightTestGivenTooLate}">
                                	<tr>
                                    	<td class="detailtitle">${message(code:'fc.flighttest.giventolate')}:</td>
                                        <td>${testInstance.GetFlightTestGivenToLatePoints()} ${message(code:'fc.points')}</td>
                                	</tr>
                                </g:if>
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
                                <g:if test="${testInstance.flightTestOtherPenalties > 0}">
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
                            <tfoot>
                                <tr>
                                    <td colspan="2">
                                        <g:if test="${!testInstance.flightTestComplete}">
                                            <g:if test="${testInstance.flightTestCheckPointsComplete}">
                                                <g:if test="${params.next}">
                                                    <g:actionSubmit action="flightresultsreadynext" value="${message(code:'fc.results.readynext')}" onclick="this.form.target='_self';return true;" tabIndex="2"/>
                                                </g:if>
                                                <g:actionSubmit action="flightresultsready" value="${message(code:'fc.results.ready')}" onclick="this.form.target='_self';return true;" tabIndex="3"/>
					                        	<g:actionSubmit action="flightresultssave" value="${message(code:'fc.save')}" onclick="this.form.target='_self';return true;" tabIndex="4"/>
                                            </g:if>
                                            <g:elseif test="${params.next}">
                                                <g:actionSubmit action="flightresultsgotonext" value="${message(code:'fc.results.gotonext')}" onclick="this.form.target='_self';return true;" tabIndex="2"/>
                                            </g:elseif>
                                            <g:actionSubmit action="importresults" value="${message(code:'fc.flightresults.aflosimport')}" onclick="this.form.target='_self';return true;" tabIndex="5"/>
                                            <g:if test="${testInstance.aflosStartNum}">
                                                <g:actionSubmit action="viewimporterrors" value="${message(code:'fc.flightresults.viewimporterrors')}" onclick="this.form.target='_self';return true;" tabIndex="6"/>
                                            </g:if>
                                            <g:actionSubmit action="setnoflightresults" value="${message(code:'fc.flightresults.setnoresults')}" onclick="this.form.target='_self';return true;" tabIndex="7"/>
                                            <g:actionSubmit action="printflightresults" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="8"/>
                                            <g:actionSubmit action="printaflosflightresults" value="${message(code:'fc.printaflos')}" onclick="this.form.target='_self';return true;" tabIndex="9"/>
                                            <g:if test="${testInstance.aflosStartNum}">
                                                <g:actionSubmit action="showmap" value="${message(code:'fc.flightresults.map')}" onclick="this.form.target='_blank';return true;" tabIndex="10"/>
                                            </g:if>
                                            <g:if test="${testInstance.IsEMailPossible()}">
                                                <g:actionSubmit action="sendmail" value="${message(code:'fc.flightresults.sendmail')}" onclick="this.form.target='_self';return true;" title="${testInstance.EMailAddress()}" tabIndex="11"/>
                                            </g:if>
                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="12"/>
                                        </g:if>
                                        <g:else>
                                            <g:if test="${params.next}">
                                                <g:actionSubmit action="flightresultsgotonext" value="${message(code:'fc.results.gotonext')}" onclick="this.form.target='_self';return true;" tabIndex="2"/>
                                            </g:if>
                                            <g:else>
                                                <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="3"/>
                                            </g:else>
                                            <g:actionSubmit action="flightresultsreopen" value="${message(code:'fc.results.reopen')}" onclick="this.form.target='_self';return true;" tabIndex="4"/>
                                            <g:actionSubmit action="printflightresults" value="${message(code:'fc.print')}" onclick="this.form.target='_self';return true;" tabIndex="5"/>
                                            <g:actionSubmit action="printaflosflightresults" value="${message(code:'fc.printaflos')}" onclick="this.form.target='_self';return true;" tabIndex="6"/>
                                            <g:if test="${testInstance.aflosStartNum}">
                                                <g:actionSubmit action="showmap" value="${message(code:'fc.flightresults.map')}" onclick="this.form.target='_blank';return true;" tabIndex="7"/>
                                            </g:if>
                                            <g:if test="${testInstance.IsEMailPossible()}">
                                                <g:actionSubmit action="sendmail" value="${message(code:'fc.flightresults.sendmail')}" onclick="this.form.target='_self';return true;" title="${testInstance.EMailAddress()}" tabIndex="8"/>
                                            </g:if>
	                                        <g:if test="${params.next}">
	                                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" onclick="this.form.target='_self';return true;" tabIndex="9"/>
	                                        </g:if>
                                        </g:else>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>