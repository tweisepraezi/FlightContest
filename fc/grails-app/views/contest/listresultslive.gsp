<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <meta http-equiv="refresh" content="10">
        
        <title>${message(code:'fc.contest.liveresults')} [${message(code:'fc.provisional')}]</title>
        
        <link rel="shortcut icon" href="fc.ico" type="image/x-icon" />

        <link rel="stylesheet" type="text/css" href="fclive.css" media="screen" />
    </head>
    <body>
        <div class="box boxborder" >
            <h2><g:if test="${contestInstance.contestPrintSubtitle}">${contestInstance.contestPrintSubtitle}</g:if><g:else>${message(code:'fc.contest.liveresults')}</g:else> [${message(code:'fc.provisional')}]</h2>
            <g:set var="contest_instance" value="${Contest.findById(contestInstance.id)}"/>
            <div class="block" id="forms" >
                <g:if test="${!contest_instance.contestPrintTaskDetails}">
	                <table class="details">
	                    <tbody>
	                        <tr>
	                            <td>${contest_instance.GetResultTitle(contest_instance.GetResultSettings(),false)}</td>
	                        </tr>
	                    </tbody>
	                </table>
	            </g:if>
                <table class="data">
                    <thead>
                        <tr>
                            <th>${message(code:'fc.test.results.position')}</th>
                            <th>${message(code:'fc.crew')}</th>
                            <th>${message(code:'fc.aircraft')}</th>
                            <g:if test="${contest_instance.contestPrintTaskDetails}">
	                            <g:if test="${contest_instance.contestPlanningResults}">
	                                <th>${message(code:'fc.planningresults.planning.short')}</th>
	                            </g:if>
	                            <g:if test="${contest_instance.contestFlightResults}">
	                                <th>${message(code:'fc.flightresults.flight.short')}</th>
	                            </g:if>
	                            <g:if test="${contest_instance.contestObservationResults}">
	                                <th>${message(code:'fc.observationresults.observations.short')}</th>
	                            </g:if>
	                            <g:if test="${contest_instance.contestLandingResults}">
                                    <th>${message(code:'fc.landingresults.landing.short')}</th>
	                            </g:if>
	                            <g:if test="${contest_instance.contestSpecialResults}">
	                                <th>${message(code:'fc.specialresults.other.short')}</th>
	                            </g:if>
                            </g:if>
                            <th>${message(code:'fc.points')}</th>
            	        </tr>
                    </thead>
                    <tbody>
                        <g:set var="provisional_position" value="${0}"/>
                        <g:each var="crew_instance" in="${Crew.findAllByContestAndDisabledAndNoContestPositionAndContestPositionNotEqual(contest_instance,false,false,0,[sort:'contestPosition'])}">
                            <g:set var="test_provisional" value="${false}"/>
                            <g:each var="task_instance" in="${contest_instance.GetResultTasks(contest_instance.contestTaskResults)}">
                                <g:set var="test_instance" value="${Test.findByCrewAndTask(crew_instance,task_instance)}"/>
                                <g:if test="${test_instance}">
                                    <g:if test="${test_instance.IsTestResultsProvisional(contest_instance.GetResultSettings())}"><g:set var="test_provisional" value="${true}"/></g:if>
                                </g:if>
                            </g:each>
                            <g:if test="${!test_provisional}">
                                <g:set var="provisional_position" value="${provisional_position+1}"/>
	                            <tr class="even">
	                                <td>${provisional_position}</td>
	                                <td>${crew_instance.name}</td>
	                                <td>${crew_instance.aircraft.registration}</td>
		                            <g:if test="${contest_instance.contestPrintTaskDetails}">
		                                <g:if test="${contest_instance.contestPlanningResults}">
                                            <td>${crew_instance.planningPenalties}</td>
		                                </g:if>
		                                <g:if test="${contest_instance.contestFlightResults}">
                                            <td>${crew_instance.flightPenalties}</td>
		                                </g:if>
		                                <g:if test="${contest_instance.contestObservationResults}">
                                            <td>${crew_instance.observationPenalties}</td>
		                                </g:if>
		                                <g:if test="${contest_instance.contestLandingResults}">
                                            <td>${crew_instance.landingPenalties}</td>
		                                </g:if>
		                                <g:if test="${contest_instance.contestSpecialResults}">
                                            <td>${crew_instance.specialPenalties}</td>
		                                </g:if>
		                            </g:if>
	                                <td>${crew_instance.contestPenalties}</td>
	                            </tr>
	                        </g:if>
                        </g:each>
                    </tbody>
                </table>
            </div>
            <p class="programinfo">${message(code:'fc.program.foot.live')}
        </div>
    </body>
</html>