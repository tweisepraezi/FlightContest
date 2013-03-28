<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.resultsettings')}}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.resultsettings')}</h2>
                <div class="block" id="forms">
                    <g:form params="${['resultfilter':resultfilter,'editresultsettingsReturnAction':editresultsettingsReturnAction,'editresultsettingsReturnController':editresultsettingsReturnController,'editresultsettingsReturnID':editresultsettingsReturnID]}">
                        <g:if test="${contestInstance.resultClasses}">
                            <fieldset>
                                <p>
                                    <g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance,[sort:"id"])}">
                                        <g:set var="resultclass_selected" value="${false}"/>
                                        <g:each var="contest_class_result" in="${contestInstance.contestClassResults.split(',')}">
                                            <g:if test="${contest_class_result == 'resultclass_' + resultclass_instance.id.toString()}">
                                                <g:set var="resultclass_selected" value="${true}"/>
                                            </g:if>
                                        </g:each>
                                        <g:if test="${resultclass_instance.contestTitle}">
                                            <g:set var="resultclass_contesttitles" value="${true}"/>
                                        </g:if>
                                        <div>
                                            <g:checkBox name="resultclass_${resultclass_instance.id}" value="${resultclass_selected}" />
                                            <label>${resultclass_instance.name} (${contestInstance.GetResultTitle(resultclass_instance.GetTeamResultSettings(),false)})</label>
                                        </div>
                                    </g:each>
                                </p>
                            </fieldset>
                        </g:if>
                        <fieldset>
                            <p>
                                <g:each var="task_instance" in="${Task.findAllByContest(contestInstance,[sort:"id"])}">
                                    <g:set var="task_selected" value="${false}"/>
                                    <g:each var="contest_task_result" in="${contestInstance.contestTaskResults.split(',')}">
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
                        <g:if test="${Team.findByContest(contestInstance)}">
	                        <fieldset>
	                            <p>
	                                <g:each var="team_instance" in="${Team.findAllByContest(contestInstance,[sort:"id"])}">
	                                    <g:set var="team_selected" value="${false}"/>
	                                    <g:each var="contest_team_result" in="${contestInstance.contestTeamResults.split(',')}">
	                                        <g:if test="${contest_team_result == 'team_' + team_instance.id.toString()}">
	                                            <g:set var="team_selected" value="${true}"/>
	                                        </g:if>
	                                    </g:each>
	                                    <div>
	                                        <g:checkBox name="team_${team_instance.id}" value="${team_selected}" />
	                                        <label>${team_instance.name}</label>
	                                    </div>
	                                </g:each>
	                                <g:if test="${Crew.findByContestAndTeamIsNull(contestInstance)}">
	                                    <g:set var="team_selected" value="${false}"/>
	                                    <g:each var="contest_team_result" in="${contestInstance.contestTeamResults.split(',')}">
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
                            	<g:if test="${contestInstance.IsPlanningTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestPlanningResults" value="${contestInstance.contestPlanningResults}" />
	                                    <label>${message(code:'fc.planningresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsFlightTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestFlightResults" value="${contestInstance.contestFlightResults}" />
	                                    <label>${message(code:'fc.flightresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsObservationTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestObservationResults" value="${contestInstance.contestObservationResults}" />
	                                    <label>${message(code:'fc.observationresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsLandingTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestLandingResults" value="${contestInstance.contestLandingResults}" />
	                                    <label>${message(code:'fc.landingresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsSpecialTestRun()}">
	                                <div>
	                                    <g:checkBox name="contestSpecialResults" value="${contestInstance.contestSpecialResults}" />
	                                    <label>${message(code:'fc.specialresults')}</label>
	                                </div>
                                </g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <label>${message(code:'fc.resultclass.contesttitle')}:</label>
                            <br/>
                            <g:set var="print_title_labels" value="${[contestInstance.title]}"/>
                            <g:set var="print_title_values" value="${[1]}"/>
                            <g:set var="i" value="${new Integer(1)}"/>
                            <g:if test="${resultclass_contesttitles}">
	                            <g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance,[sort:"id"])}">
	                                <g:if test="${resultclass_instance.contestTitle}">
	                                    <g:set var="i" value="${i+1}"/>
	                                    <g:set var="print_title_labels" value="${print_title_labels += resultclass_instance.name + ': ' + resultclass_instance.contestTitle}"/>
	                                    <g:set var="print_title_values" value="${print_title_values += i}"/>
	                                </g:if>
	                            </g:each>
	                        </g:if>
                            <g:set var="i" value="${i+1}"/>
                            <g:set var="print_title_labels" value="${print_title_labels += message(code:'fc.resultclass.contesttitle.othervalue') + ':'}"/>
                            <g:set var="print_title_values" value="${print_title_values += i}"/>
                            <g:set var="print_title_value" value="${contestInstance.contestContestTitle}"/>
                            <g:if test="${!contestInstance.contestContestTitle || (contestInstance.contestContestTitle > i + 1)}">
                                <g:set var="print_title_value" value="${new Integer(1)}"/>
                            </g:if>
                            <g:radioGroup name="contestContestTitle" labels="${print_title_labels}" values="${print_title_values}" value="${print_title_value}">
                                <div>
                                    <label>${it.radio} ${it.label}</label>
                                </div>
                            </g:radioGroup>
                            <p>
                                <input type="text" id="contestPrintTitle" name="contestPrintTitle" value="${fieldValue(bean:contestInstance,field:'contestPrintTitle')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="contestPrintSubtitle" name="contestPrintSubtitle" value="${fieldValue(bean:contestInstance,field:'contestPrintSubtitle')}" tabIndex="2"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
	                            <div>
	                                <g:checkBox name="contestPrintLandscape" value="${contestInstance.contestPrintLandscape}" />
	                                <label>${message(code:'fc.printlandscape')}</label>
	                            </div>
                                <div>
                                    <g:checkBox name="contestPrintA3" value="${contestInstance.contestPrintA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintTaskDetails" value="${contestInstance.contestPrintTaskDetails}" />
                                    <label>${message(code:'fc.printtaskdetails')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintTaskNamesInTitle" value="${contestInstance.contestPrintTaskNamesInTitle}" />
                                    <label>${message(code:'fc.printtasknamesinttitle')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestPrintProvisional" value="${contestInstance.contestPrintProvisional}" />
                                    <label>${message(code:'fc.printprovisional')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="3"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="4"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>