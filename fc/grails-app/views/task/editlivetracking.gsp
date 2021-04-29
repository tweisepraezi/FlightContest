<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.livetracking.settings')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.livetracking.settings')} - ${taskInstance.name()} <img src="${createLinkTo(dir:'images',file:'livetracking-white.svg')}" style="margin-left:0.2rem; height:0.6rem;"/></h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.task.date')}:</label>
                                <br/>
                                <input type="date" id="liveTrackingNavigationTaskDate" name="liveTrackingNavigationTaskDate" value="${fieldValue(bean:taskInstance,field:'liveTrackingNavigationTaskDate')}" min="${taskInstance.contest.liveTrackingContestDate}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <div>
                                <g:if test="${taskInstance.liveTrackingNavigationTaskID}" >
                                    <g:set var="livetracking_map" value="${BootStrap.global.GetLiveTrackingMap(taskInstance.liveTrackingNavigationTaskID)}"/>
                                    <label>
                                        <g:if test="${livetracking_map}">
                                            <a href="${livetracking_map}" target="_blank">${message(code:'fc.livetracking.navigationtaskmap')}</a>
                                        </g:if>
                                        ${message(code:'fc.livetracking.navigationtaskid')}: ${taskInstance.liveTrackingNavigationTaskID}
                                    </label>
                                    <br/>
                                    <g:if test="${taskInstance.liveTrackingResultsTaskID}" >
                                        <g:set var="livetracking_results" value="${BootStrap.global.GetLiveTrackingResults(taskInstance.contest.liveTrackingContestID, taskInstance.liveTrackingResultsTaskID)}"/>
                                        <g:if test="${livetracking_results}">
                                            <label><a href="${livetracking_results}" target="_blank">${message(code:'fc.livetracking.results.service')}</a></label>
                                        </g:if>
                                        <label>${message(code:'fc.livetracking.results.task.id')}: ${taskInstance.liveTrackingResultsTaskID}<g:if test="${taskInstance.liveTrackingResultsPlanningID}" >,
                                            ${message(code:'fc.livetracking.results.task.planning')}: ${taskInstance.liveTrackingResultsPlanningID}</g:if><g:if test="${taskInstance.liveTrackingResultsFlightID}" >,
                                            ${message(code:'fc.livetracking.results.task.flight')}: ${taskInstance.liveTrackingResultsFlightID}</g:if><g:if test="${taskInstance.liveTrackingResultsObservationID}" >,
                                            ${message(code:'fc.livetracking.results.task.observation')}: ${taskInstance.liveTrackingResultsObservationID}</g:if><g:if test="${taskInstance.liveTrackingResultsLandingID}" >,
                                            ${message(code:'fc.livetracking.results.task.landing')}: ${taskInstance.liveTrackingResultsLandingID}</g:if><g:if test="${taskInstance.liveTrackingResultsSpecialID}" >,
                                            ${message(code:'fc.livetracking.results.task.special')}: ${taskInstance.liveTrackingResultsSpecialID}</g:if>
                                        </label>
                                        <br/>
                                    </g:if>
                                    <p>
                                        <div>
                                            <g:checkBox name="liveTrackingResultsFlightOn" value="${taskInstance.liveTrackingResultsFlightOn}" />
                                            <label>${message(code:'fc.livetracking.results.publishnavigation')}</label>
                                        </div>
                                        <div>
                                            <g:checkBox name="liveTrackingResultsPublishImmediately" value="${taskInstance.liveTrackingResultsPublishImmediately}" />
                                            <label>${message(code:'fc.livetracking.results.publishimmediately')}</label>
                                        </div>
                                    </p>
                                </g:if>
                            </div>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="savelivetracking" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${taskInstance.liveTrackingNavigationTaskID}" >
                            <g:if test="${BootStrap.global.IsLiveTrackingNavigationTaskDeletePossible()}" >
                                <g:actionSubmit action="livetracking_navigationtaskdelete" value="${message(code:'fc.livetracking.navigationtaskdelete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:actionSubmit action="livetracking_navigationtaskdisconnect" value="${message(code:'fc.livetracking.navigationtaskdisconnect')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                            <g:if test="${taskInstance.liveTrackingTracksAvailable}">
                                <g:actionSubmit action="livetracking_navigationtaskaddtracks" value="${message(code:'fc.livetracking.navigationtaskaddtracks')}" tabIndex="${ti[0]++}"/>
                                <g:actionSubmit action="livetracking_navigationtaskaddtracks_incomplete" value="${message(code:'fc.livetracking.navigationtaskaddtracks.incomplete')}" tabIndex="${ti[0]++}"/>
                            </g:if>
                            <g:if test="${taskInstance.IsPlanningTestRun()}">
                                <g:if test="${!taskInstance.liveTrackingResultsPlanningID}" >
                                    <g:actionSubmit action="livetracking_planningcreate" value="${message(code:'fc.livetracking.results.task.planningcreate')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="livetracking_planningdelete" value="${message(code:'fc.livetracking.results.task.planningdelete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </g:else>
                            </g:if>
                            <g:if test="${taskInstance.IsObservationTestRun()}">
                                <g:if test="${!taskInstance.liveTrackingResultsObservationID}" >
                                    <g:actionSubmit action="livetracking_observationcreate" value="${message(code:'fc.livetracking.results.task.observationcreate')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="livetracking_observationdelete" value="${message(code:'fc.livetracking.results.task.observationdelete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </g:else>
                            </g:if>
                            <g:if test="${taskInstance.IsLandingTestRun()}">
                                <g:if test="${!taskInstance.liveTrackingResultsLandingID}" >
                                    <g:actionSubmit action="livetracking_landingcreate" value="${message(code:'fc.livetracking.results.task.landingcreate')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="livetracking_landingdelete" value="${message(code:'fc.livetracking.results.task.landingdelete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </g:else>
                            </g:if>
                            <g:if test="${taskInstance.IsSpecialTestRun()}">
                                <g:if test="${!taskInstance.liveTrackingResultsSpecialID}" >
                                    <g:actionSubmit action="livetracking_othercreate" value="${message(code:'fc.livetracking.results.task.specialcreate')}" tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <g:actionSubmit action="livetracking_otherdelete" value="${message(code:'fc.livetracking.results.task.specialdelete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </g:else>
                            </g:if>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="livetracking_navigationtaskcreate" value="${message(code:'fc.livetracking.navigationtaskcreate')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="livetracking_navigationtaskconnect" value="${message(code:'fc.livetracking.navigationtaskconnect')}" tabIndex="${ti[0]++}"/>
                        </g:else>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>