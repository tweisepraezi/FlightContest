<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:if test="${!showIntern}">
	        <g:if test="${liveContest.liveRefreshSeconds}">
	            <meta http-equiv="refresh" content="${liveContest.liveRefreshSeconds}">
	        </g:if>
	    </g:if>
        
        <title>${message(code:'fc.contest.liveresults')} [${message(code:'fc.provisional')}]</title>
        
        <link rel="shortcut icon" href="fc.ico" type="image/x-icon" />

        <g:if test="${liveContest.liveStylesheet}">
            <g:if test="${showIntern}">
                <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'',file:'css')}/${liveContest.liveStylesheet}" media="screen" />
            </g:if>
            <g:else>        
                <link rel="stylesheet" type="text/css" href="${liveContest.liveStylesheet}" media="screen" />
            </g:else>
        </g:if>
    </head>
    <body>
        <div class="box boxborder" >
            <h2><g:if test="${liveContest.contestPrintSubtitle}">${liveContest.contestPrintSubtitle}</g:if><g:else>${message(code:'fc.contest.liveresults')}</g:else> [${message(code:'fc.provisional')}]</h2>
            <div class="block" id="forms" >
                <g:if test="${!liveContest.contestPrintTaskTestDetails}">
	                <table class="details">
	                    <tbody>
	                        <tr>
	                            <td>${liveContest.getResultTitle}</td>
	                        </tr>
	                    </tbody>
	                </table>
	            </g:if>
                <table class="data">
                    <g:each var="live_crew" in="${liveCrews}">
                        <g:if test="${!live_tasks}">
                            <g:set var="live_tasks" value="${live_crew.tasks}"/>
                        </g:if>
                    </g:each>
                    <g:set var="col_num" value="${new Integer(2)}"/>
                    <thead>
                        <tr>
                            <th>${message(code:'fc.test.results.position')}</th>
                            <th>${message(code:'fc.crew')}</th>
                            <g:if test="${liveContest.contestPrintAircraft}">
                                <g:set var="col_num" value="${col_num+1}"/>
                                <th>${message(code:'fc.aircraft')}</th>
                            </g:if>
                            <g:if test="${liveContest.contestPrintTeam}">
                                <g:set var="col_num" value="${col_num+1}"/>
                                <th>${message(code:'fc.team')}</th>
                            </g:if>
                            <g:if test="${liveContest.contestPrintClass}">
                                <g:set var="col_num" value="${col_num+1}"/>
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
                            <g:if test="${liveContest.contestPrintShortClass}">
                                <g:set var="col_num" value="${col_num+1}"/>
                                <th>${message(code:'fc.resultclass.short.short')}</th>
                            </g:if>
                            <g:if test="${liveContest.contestPrintTaskDetails || liveContest.contestPrintTaskTestDetails}">
                                <g:set var="col_num" value="${col_num+1}"/>
	                            <g:each var="live_task" in="${live_tasks}">
				                    <g:set var="detail_num" value="${new Integer(0)}"/>
				                    <g:if test="${live_task.id in liveContest.getTestDetailsTasksIDs}">
				                        <g:if test="${liveContest.contestPlanningResults && live_task.isTaskPlanningTest}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                        <g:if test="${liveContest.contestFlightResults && live_task.isTaskFlightTest}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                        <g:if test="${liveContest.contestObservationResults && live_task.isTaskObservationTest}">
                                            <g:set var="observation_detail_written" value="${false}"/>
                                            <g:if test="${liveContest.contestPrintObservationDetails}">
                                                <g:if test="${live_task.isTaskObservationTurnpointTest}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="observation_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${live_task.isTaskObservationEnroutePhotoTest}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="observation_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${live_task.isTaskObservationEnrouteCanvasTest}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="observation_detail_written" value="${true}"/>
                                                </g:if>
                                            </g:if>
                                            <g:if test="${!observation_detail_written}">
				                                <g:set var="detail_num" value="${detail_num+1}"/>
				                            </g:if>
				                        </g:if>
				                        <g:if test="${liveContest.contestLandingResults && live_task.isTaskLandingTest}">
                                            <g:set var="landing_detail_written" value="${false}"/>
				                            <g:if test="${liveContest.contestPrintLandingDetails}">
				                                <g:if test="${live_task.isTaskLanding1Test}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="landing_detail_written" value="${true}"/>
				                                </g:if>
                                                <g:if test="${live_task.isTaskLanding2Test}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="landing_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${live_task.isTaskLanding3Test}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="landing_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${live_task.isTaskLanding4Test}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:set var="landing_detail_written" value="${true}"/>
                                                </g:if>
                                                <g:if test="${liveContest.getLandingResultsFactor}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                </g:if>
				                            </g:if>
				                            <g:if test="${!landing_detail_written}">
				                                <g:set var="detail_num" value="${detail_num+1}"/>
				                            </g:if>
				                        </g:if>
				                        <g:if test="${liveContest.contestSpecialResults && live_task.isTaskSpecialTest}">
				                            <g:set var="detail_num" value="${detail_num+1}"/>
				                        </g:if>
				                    </g:if>
				                    <g:if test="${liveContest.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1) || (live_task.isTaskIncreaseEnabled))}">
				                        <g:set var="detail_num" value="${detail_num+1}"/>
				                    </g:if>
	                                <th colspan="${detail_num}">${live_task.bestOfName}</th>
                                    <g:set var="col_num" value="${col_num+detail_num}"/>
	                            </g:each>
	                        </g:if>
	                        <g:if test="${liveContest.liveShowSummary}">
                                <g:set var="col_num" value="${col_num+1}"/>
	                            <th>${message(code:'fc.test.results.summary')}</th>
	                        </g:if>
            	        </tr>
                        <g:if test="${liveContest.contestPrintTaskTestDetails != ""}">
                            <tr>
                                <th/>
                                <th/>
	                            <g:if test="${liveContest.contestPrintAircraft}">
                                    <th/>
	                            </g:if>
	                            <g:if test="${liveContest.contestPrintTeam}">
                                    <th/>
	                            </g:if>
	                            <g:if test="${liveContest.contestPrintClass}">
                                    <th/>
	                            </g:if>
	                            <g:if test="${liveContest.contestPrintShortClass}">
                                    <th/>
	                            </g:if>
                                <g:if test="${liveContest.contestPrintTaskDetails || liveContest.contestPrintTaskTestDetails}">
	                                <g:each var="live_task" in="${live_tasks}">
                                        <g:set var="detail_num" value="${new Integer(0)}"/>
	                                    <g:if test="${live_task.id in liveContest.getTestDetailsTasksIDs}">
			                                <g:if test="${liveContest.contestPlanningResults && live_task.isTaskPlanningTest}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
			                                    <th>${message(code:'fc.planningresults.planning.short')}</th>
			                                </g:if>
			                                <g:if test="${liveContest.contestFlightResults && live_task.isTaskFlightTest}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
			                                    <th>${message(code:'fc.flightresults.flight.short')}</th>
			                                </g:if>
			                                <g:if test="${liveContest.contestObservationResults && live_task.isTaskObservationTest}">
                                                <g:set var="observation_detail_written" value="${false}"/>
                                                <g:if test="${liveContest.contestPrintObservationDetails}">
                                                    <g:if test="${live_task.isTaskObservationTurnpointTest}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.observationresults.turnpoint.short')}</th>
                                                        <g:set var="observation_detail_written" value="${true}"/>
                                                    </g:if>
                                                    <g:if test="${live_task.isTaskObservationEnroutePhotoTest}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.observationresults.enroutephoto.short')}</th>
                                                        <g:set var="observation_detail_written" value="${true}"/>
                                                    </g:if>
                                                    <g:if test="${live_task.isTaskObservationEnrouteCanvasTest}">
                                                        <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.observationresults.enroutecanvas.short')}</th>
                                                        <g:set var="observation_detail_written" value="${true}"/>
                                                    </g:if>
                                                </g:if>
                                                <g:if test="${!observation_detail_written}">
                                                    <g:set var="detail_num" value="${detail_num+1}"/>
		                                            <th>${message(code:'fc.observationresults.observations.short')}</th>
		                                        </g:if>
			                                </g:if>
			                                <g:if test="${liveContest.contestLandingResults && live_task.isTaskLandingTest}">
			                                    <g:set var="landing_detail_written" value="${false}"/>
                                                <g:if test="${liveContest.contestPrintLandingDetails}">
	                                                <g:if test="${live_task.isTaskLanding1Test}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing1')}</th>
                                                        <g:set var="landing_detail_written" value="${true}"/>
	                                                </g:if>
	                                                <g:if test="${live_task.isTaskLanding2Test}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing2')}</th>
                                                        <g:set var="landing_detail_written" value="${true}"/>
	                                                </g:if>
	                                                <g:if test="${live_task.isTaskLanding3Test}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing3')}</th>
                                                        <g:set var="landing_detail_written" value="${true}"/>
	                                                </g:if>
	                                                <g:if test="${live_task.isTaskLanding4Test}">
	                                                    <g:set var="detail_num" value="${detail_num+1}"/>
                                                        <th>${message(code:'fc.landingresults.landing4')}</th>
                                                        <g:set var="landing_detail_written" value="${true}"/>
	                                                </g:if>
                                                </g:if>
                                                <g:if test="${!landing_detail_written || liveContest.getLandingResultsFactor}">
	                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                    <g:if test="${liveContest.getLandingResultsFactor}">
                                                        <th>${message(code:'fc.landingresults.landing.short')} (${liveContest.getLandingResultsFactor})</th>
                                                    </g:if>
                                                    <g:else>
				                                        <th>${message(code:'fc.landingresults.landing.short')}</th>
				                                    </g:else>
				                                </g:if>
			                                </g:if>
			                                <g:if test="${liveContest.contestSpecialResults && live_task.isTaskSpecialTest}">
                                                <g:set var="detail_num" value="${detail_num+1}"/>
                                                <g:set var="specialtest_title" value="${live_task.isTaskSpecialTestTitle}"/>
                                                <g:if test="${specialtest_title}">
                                                    <th>${specialtest_title}</th>
                                                </g:if>
                                                <g:else>
                                                    <th>${message(code:'fc.specialresults.other.short')}</th>
                                                </g:else>
			                                </g:if>
			                            </g:if>
                                        <g:if test="${liveContest.contestPrintTaskDetails && ((detail_num==0) || (detail_num>1) || (live_task.isTaskIncreaseEnabled))}">
		                                    <th>${message(code:'fc.test.results.summary.short')}</th>
		                                </g:if>
	                                    <g:elseif test="${liveContest.contestPrintTaskTestDetails && (detail_num==0)}">
	                                        <th>${message(code:'fc.test.results.summary.short')}</th>
	                                    </g:elseif>
	                                </g:each>
	                            </g:if>
	                            <g:if test="${liveContest.liveShowSummary}">
                                    <th/>
                                </g:if>
                            </tr>
                        </g:if>
                    </thead>
                    <tbody>
                        <g:if test="${LiveNewestShowSize}">
                            <g:set var="newest_live_crew_pos" value="${0}"/>
                            <g:each var="newest_live_crew" in="${newestLiveCrews}">
                                <g:set var="newest_live_crew_pos" value="${newest_live_crew_pos+1}"/>
                                <g:if test="${newest_live_crew_pos <= LiveNewestShowSize}">
                                    <g:liveResultLine pos="${newest_live_crew.contestPosition}" crew="${newest_live_crew}" livecontest="${liveContest}" />
                                </g:if>
                            </g:each>
                            <tr><td colspan="${col_num}" style="height:10px;"/></tr>
                        </g:if>
                        <g:set var="live_crew_pos" value="${0}"/>
                        <g:if test="${liveContest.livePositionCalculation == 0}">
                            <g:each var="live_crew" in="${liveCrews}">
                                <g:if test="${!live_crew.noContestPosition && (live_crew.contestPosition != 0)}">
                                    <g:set var="live_crew_pos" value="${live_crew_pos+1}"/>
                                    <g:if test="${live_crew_pos >= LiveStartPos && live_crew_pos < LiveStartPos+LiveShowSize}">
                                        <g:liveResultLine pos="${live_crew.contestPosition}" crew="${live_crew}" livecontest="${liveContest}" />
                                    </g:if>
                                </g:if>
                            </g:each>
	                    </g:if>
	                    <g:else>
                            <g:each var="live_crew" in="${EvaluationService.GetLiveCrewSort(liveCrews,liveContest.livePositionCalculation)}">
                                <g:each var="live_task" in="${live_crew.tasks}">
                                    <g:if test="${live_task.id == liveContest.livePositionCalculation}">
                                        <g:set var="live_crew_pos" value="${live_crew_pos+1}"/>
                                        <g:if test="${live_crew_pos >= LiveStartPos && live_crew_pos < LiveStartPos+LiveShowSize}">
                                            <g:liveResultLine pos="${live_task.taskPosition}" crew="${live_crew}" livecontest="${liveContest}" task="${liveContest.livePositionCalculation}" />
                                        </g:if>
                                    </g:if>
                                </g:each>
		                    </g:each>
	                    </g:else>
                    </tbody>
                </table>
            </div>
            <table class="foot">
                <tbody>
                    <tr>
                        <td class="programinfo">${liveContest.printOrganizer}</td>
                        <g:if test="${BootStrap.global.IsLiveFTPUploadPossible()}">
                            <td class="liveurl">${BootStrap.global.GetLiveFTPURL()}</td>
                        </g:if>
                    </tr>
                </tbody>
            </table>
        </div>
    </body>
</html>