<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.resultclass.resultsettings')}}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.resultclass.resultsettings')}</h2>
                <div class="block" id="forms">
                    <g:form params="${['resultfilter':resultfilter,'editresultsettingsReturnAction':editresultsettingsReturnAction,'editresultsettingsReturnController':editresultsettingsReturnController,'editresultsettingsReturnID':editresultsettingsReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
						<fieldset>
                            <p>
                                <g:each var="task_instance" in="${Task.findAllByContest(resultclassInstance.contest,[sort:"idTitle"])}">
                                    <g:set var="task_selected" value="${false}"/>
                                    <g:each var="contest_task_result" in="${resultclassInstance.contestTaskResults.split(',')}">
                                        <g:if test="${contest_task_result == 'task_' + task_instance.id.toString()}">
                                            <g:set var="task_selected" value="${true}"/>
                                        </g:if>
                                    </g:each>
                                    <div>
                                        <g:checkBox name="task_${task_instance.id}" value="${task_selected}" />
                                        <label>${task_instance.bestOfName()}</label>
                                    </div>
                                </g:each>
                            </p>
                        </fieldset>
                        <g:if test="${Team.findByContest(resultclassInstance.contest)}">
                            <fieldset>
                                <p>
                                    <g:set var="team_selected" value="${false}"/>
                                    <g:each var="contest_team_result" in="${resultclassInstance.contestTeamResults.split(',')}">
                                        <g:if test="${contest_team_result == 'team_all_teams'}">
                                            <g:set var="team_selected" value="${true}"/>
                                        </g:if>
                                    </g:each>
                                    <div>
                                        <g:checkBox name="team_all_teams" value="${team_selected}" />
                                        <label>${message(code:'fc.team.all.teams')}</label>
                                    </div>
                                    <g:if test="${Crew.findByContestAndResultclassAndTeamIsNull(resultclassInstance.contest,resultclassInstance)}">
                                        <g:set var="team_selected" value="${false}"/>
                                        <g:each var="contest_team_result" in="${resultclassInstance.contestTeamResults.split(',')}">
                                            <g:if test="${contest_team_result == 'team_no_team_crew'}">
                                                <g:set var="team_selected" value="${true}"/>
                                            </g:if>
                                        </g:each>
                                        <div>
                                            <g:checkBox name="team_no_team_crew" value="${team_selected}" />
                                            <label>${message(code:'fc.team.no.team.crew')}</label>
                                        </div>
                                    </g:if>
                                    <br/>
                                    <g:each var="team_instance" in="${Team.findAllByContest(resultclassInstance.contest,[sort:"id"])}">
                                        <g:set var="team_selected" value="${false}"/>
                                        <g:each var="contest_team_result" in="${resultclassInstance.contestTeamResults.split(',')}">
                                            <g:if test="${contest_team_result == 'team_' + team_instance.id.toString()}">
                                                <g:set var="team_selected" value="${true}"/>
                                            </g:if>
                                        </g:each>
                                        <div>
                                            <g:checkBox name="team_${team_instance.id}" value="${team_selected}" />
                                            <label>${team_instance.name}</label>
                                        </div>
                                    </g:each>
                                </p>
                            </fieldset>
                        </g:if>
                        <fieldset>
                            <p>
                            	<g:if test="${resultclassInstance.IsPlanningTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestPlanningResults" value="${resultclassInstance.contestPlanningResults}" />
	                                    <label>${message(code:'fc.planningresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${resultclassInstance.IsFlightTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestFlightResults" value="${resultclassInstance.contestFlightResults}" />
	                                    <label>${message(code:'fc.flightresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${resultclassInstance.IsObservationTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestObservationResults" value="${resultclassInstance.contestObservationResults}" />
	                                    <label>${message(code:'fc.observationresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${resultclassInstance.IsLandingTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestLandingResults" value="${resultclassInstance.contestLandingResults}" />
	                                    <label>${message(code:'fc.landingresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${resultclassInstance.IsSpecialTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestSpecialResults" value="${resultclassInstance.contestSpecialResults}" />
	                                    <label>${message(code:'fc.specialresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${resultclassInstance.IsLandingTestRun()}">
                                        <br/>
                                        <p>
                                            <label>${message(code:'fc.contest.resultsettings')} - ${message(code:'fc.landingresults.factor')}:</label>
                                            <br/>
                                            <input type="text" id="contestLandingResultsFactor" name="contestLandingResultsFactor" value="${fieldValue(bean:resultclassInstance.contest,field:'contestLandingResultsFactor')}" tabIndex="${ti[0]++}" disabled />
                                        </p>
                                </g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.resultclass.contesttitle')}:</label>
                                <br/>
                                <input type="text" id="contestTitle" name="contestTitle" value="${fieldValue(bean:resultclassInstance,field:'contestTitle')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="contestPrintSubtitle" name="contestPrintSubtitle" value="${fieldValue(bean:resultclassInstance,field:'contestPrintSubtitle')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="contestPrintTaskNamesInTitle" value="${resultclassInstance.contestPrintTaskNamesInTitle}" />
                                    <label>${message(code:'fc.printtasknamesinttitle')}</label>
                                </div>
                                <br/>
                                <div>
                                    <g:checkBox name="contestPrintAircraft" value="${resultclassInstance.contestPrintAircraft}" />
                                    <label>${message(code:'fc.printaircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintTeam" value="${resultclassInstance.contestPrintTeam}" />
                                    <label>${message(code:'fc.printteam')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintClass" value="${resultclassInstance.contestPrintClass}" />
                                    <label>${message(code:'fc.printresultclass')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintShortClass" value="${resultclassInstance.contestPrintShortClass}" />
                                    <label>${message(code:'fc.printresultclass.short')}</label>
                                </div>
                                <g:each var="task_instance" in="${Task.findAllByContest(resultclassInstance.contest,[sort:"idTitle"])}">
                                    <g:set var="task_selected" value="${false}"/>
                                    <g:each var="contest_task_test_details" in="${resultclassInstance.contestPrintTaskTestDetails.split(',')}">
                                        <g:if test="${contest_task_test_details == 'tasktestdetails_' + task_instance.id.toString()}">
                                            <g:set var="task_selected" value="${true}"/>
                                        </g:if>
                                    </g:each>
                                    <div>
                                        <g:checkBox name="tasktestdetails_${task_instance.id}" value="${task_selected}" />
                                        <label>${message(code:'fc.printtasktestdetails2',args:[task_instance.bestOfName()])}</label>
                                    </div>
                                </g:each>
                                <div>
                                    <g:checkBox name="contestPrintObservationDetails" value="${resultclassInstance.contestPrintObservationDetails}" />
                                    <label>${message(code:'fc.printobservationdetails')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintLandingDetails" value="${resultclassInstance.contestPrintLandingDetails}" />
                                    <label>${message(code:'fc.printlandingdetails')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintTaskDetails" value="${resultclassInstance.contestPrintTaskDetails}" />
                                    <label>${message(code:'fc.printtaskdetails')}</label>
                                </div>
                            </p>
                            <p>
                                <label>${message(code:'fc.printfooter')}:</label>
                                <br/>
                                <input type="text" id="contestPrintFooter" name="contestPrintFooter" value="${fieldValue(bean:resultclassInstance,field:'contestPrintFooter')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="contestPrintLandscape" value="${resultclassInstance.contestPrintLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintA3" value="${resultclassInstance.contestPrintA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                                <br/>
                                <div>
                                    <g:checkBox name="contestPrintProvisional" value="${resultclassInstance.contestPrintProvisional}" />
                                    <label>${message(code:'fc.printprovisional')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintEqualPositions" value="${resultclassInstance.contestPrintEqualPositions}" />
                                    <label>${message(code:'fc.printequalpositions')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>