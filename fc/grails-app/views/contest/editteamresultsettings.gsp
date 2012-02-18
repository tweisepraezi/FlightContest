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
                    <g:form params="${['editteamresultsettingsReturnAction':editteamresultsettingsReturnAction,'editteamresultsettingsReturnController':editteamresultsettingsReturnController,'editteamresultsettingsReturnID':editteamresultsettingsReturnID]}">
                       	<g:set var="resultclass_contesttitles" value="${false}"/>
                        <g:if test="${contestInstance.resultClasses}">
	                        <fieldset>
	                        	<p>
		                        	<g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance)}">
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
                        <g:if test="${contestInstance.resultClasses}">
                        	<g:if test="${resultclass_contesttitles}">
		                        <fieldset>
		                        	<legend>${message(code:'fc.resultclass.contesttitle')}</legend>
		                        	<br/>
		                        	<g:set var="print_title_labels" value="${[contestInstance.title]}"/>
		                        	<g:set var="print_title_values" value="${[1]}"/>
		                        	<g:set var="i" value="${new Integer(1)}"/>
		                        	<g:each var="resultclass_instance" in="${ResultClass.findAllByContest(contestInstance)}">
		                        		<g:if test="${resultclass_instance.contestTitle}">
		                        			<g:set var="i" value="${i+1}"/>
				                        	<g:set var="print_title_labels" value="${print_title_labels += resultclass_instance.name + ': ' + resultclass_instance.contestTitle}"/>
				                        	<g:set var="print_title_values" value="${print_title_values += i}"/>
		                        		</g:if>
		                        	</g:each>
		                        	<g:set var="print_title_value" value="${contestInstance.teamContestTitle}"/>
		                        	<g:if test="${!contestInstance.teamContestTitle || (contestInstance.teamContestTitle > i)}">
		                        		<g:set var="print_title_value" value="${new Integer(1)}"/>
		                        	</g:if>
	                        		<g:radioGroup name="teamContestTitle" labels="${print_title_labels}" values="${print_title_values}" value="${print_title_value}">
	                        			<div>
											<label>${it.radio} ${it.label}</label>
										</div>
									</g:radioGroup>
	                        	</fieldset>
                        	</g:if>
                        </g:if>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="1"/>
                            </p>
                       	</fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="2"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>