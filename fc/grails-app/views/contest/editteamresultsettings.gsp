<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.teamresultsettings')}}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.teamresultsettings')}</h2>
                <div class="block" id="forms">
                    <g:form params="${['resultfilter':resultfilter,'editteamresultsettingsReturnAction':editteamresultsettingsReturnAction,'editteamresultsettingsReturnController':editteamresultsettingsReturnController,'editteamresultsettingsReturnID':editteamresultsettingsReturnID]}">
						<g:set var="ti" value="${[]+1}"/>
                       	<g:set var="resultclass_contesttitles" value="${false}"/>
                        <g:if test="${contestInstance.resultClasses}">
	                        <fieldset>
	                        	<p>
		                        	<g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance,[sort:"id"])}">
		                        		<g:set var="resultclass_selected" value="${false}"/>
		                        		<g:each var="team_class_result" in="${contestInstance.teamClassResults.split(',')}">
			                        		<g:if test="${team_class_result == 'resultclass_' + resultclass_instance.id.toString()}">
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
                                <g:each var="task_instance" in="${Task.findAllByContest(contestInstance,[sort:"idTitle"])}">
                                    <g:set var="task_selected" value="${false}"/>
                                    <g:each var="contest_task_result" in="${contestInstance.teamTaskResults.split(',')}">
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
                        <fieldset>
                            <p>
                            	<g:if test="${contestInstance.IsPlanningTestRun()}">
	                                <div>
	                                    <g:checkBox name="teamPlanningResults" value="${contestInstance.teamPlanningResults}" />
	                                    <label>${message(code:'fc.planningresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsFlightTestRun()}">
	                                <div>
	                                    <g:checkBox name="teamFlightResults" value="${contestInstance.teamFlightResults}" />
	                                    <label>${message(code:'fc.flightresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsObservationTestRun()}">
	                                <div>
	                                    <g:checkBox name="teamObservationResults" value="${contestInstance.teamObservationResults}" />
	                                    <label>${message(code:'fc.observationresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsLandingTestRun()}">
	                                <div>
	                                    <g:checkBox name="teamLandingResults" value="${contestInstance.teamLandingResults}" />
	                                    <label>${message(code:'fc.landingresults')}</label>
	                                </div>
                                </g:if>
                            	<g:if test="${contestInstance.IsSpecialTestRun()}">
	                                <div>
	                                    <g:checkBox name="teamSpecialResults" value="${contestInstance.teamSpecialResults}" />
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
                        	<g:set var="print_title_value" value="${contestInstance.teamContestTitle}"/>
                        	<g:if test="${!contestInstance.teamContestTitle || (contestInstance.teamContestTitle > i)}">
                        		<g:set var="print_title_value" value="${new Integer(1)}"/>
                        	</g:if>
                       		<g:radioGroup name="teamContestTitle" labels="${print_title_labels}" values="${print_title_values}" value="${print_title_value}">
                       			<div>
									<label>${it.radio} ${it.label}</label>
								</div>
							</g:radioGroup>
                            <p>
                                <input type="text" id="teamPrintTitle" name="teamPrintTitle" value="${fieldValue(bean:contestInstance,field:'teamPrintTitle')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="teamPrintSubtitle" name="teamPrintSubtitle" value="${fieldValue(bean:contestInstance,field:'teamPrintSubtitle')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.printfooter')}:</label>
                                <br/>
                                <input type="text" id="teamPrintFooter" name="teamPrintFooter" value="${fieldValue(bean:contestInstance,field:'teamPrintFooter')}" tabIndex="${ti[0]++}"/>
                            </p>
                       	</fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <div>
                                <g:checkBox name="teamPrintStartNum" value="${contestInstance.teamPrintStartNum}" />
                                <label>${message(code:'fc.printstartnum')}</label>
                            </div>
                            <br/>
                            <div>
                                <g:checkBox name="teamPrintLandscape" value="${contestInstance.teamPrintLandscape}" />
                                <label>${message(code:'fc.printlandscape')}</label>
                            </div>
                            <div>
                                <g:checkBox name="teamPrintA3" value="${contestInstance.teamPrintA3}" />
                                <label>${message(code:'fc.printa3')}</label>
                            </div>
                            <br/>
                            <div>
                                <g:checkBox name="teamPrintProvisional" value="${contestInstance.teamPrintProvisional}" />
                                <label>${message(code:'fc.printprovisional')}</label>
                            </div>
                            <div>
                                <g:checkBox name="teamPrintEqualPositions" value="${contestInstance.teamPrintEqualPositions}" />
                                <label>${message(code:'fc.printequalpositions')}</label>
                            </div>
                       	</fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>