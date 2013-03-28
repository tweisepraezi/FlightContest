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
                        <fieldset>
                            <p>
                                <g:each var="task_instance" in="${Task.findAllByContest(resultclassInstance.contest,[sort:"id"])}">
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
                                    <g:if test="${Crew.findByContestAndTeamIsNull(resultclassInstance.contest)}">
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
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.resultclass.contesttitle')}:</label>
                                <br/>
                                <input type="text" id="contestTitle" name="contestTitle" value="${fieldValue(bean:resultclassInstance,field:'contestTitle')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="contestPrintSubtitle" name="contestPrintSubtitle" value="${fieldValue(bean:resultclassInstance,field:'contestPrintSubtitle')}" tabIndex="2"/>
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
                                <div>
                                    <g:checkBox name="contestPrintTaskDetails" value="${resultclassInstance.contestPrintTaskDetails}" />
                                    <label>${message(code:'fc.printtaskdetails')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintTaskNamesInTitle" value="${resultclassInstance.contestPrintTaskNamesInTitle}" />
                                    <label>${message(code:'fc.printtasknamesinttitle')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintProvisional" value="${resultclassInstance.contestPrintProvisional}" />
                                    <label>${message(code:'fc.printprovisional')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="3"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="4"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>