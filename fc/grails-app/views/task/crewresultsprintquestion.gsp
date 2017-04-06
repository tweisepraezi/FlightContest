<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crewresults.print')} ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crewresults.print')} ${taskInstance.name()}</h2>
                <div class="block" id="forms">
                    <g:form params="${['crewresultsprintquestionReturnAction':crewresultsprintquestionReturnAction,'crewresultsprintquestionReturnController':crewresultsprintquestionReturnController,'crewresultsprintquestionReturnID':crewresultsprintquestionReturnID]}">
                        <g:if test="${taskInstance.contest.resultClasses}">
	                        <fieldset>
	                        	<p>
		                        	<g:each var="resultclass_instance" in="${taskInstance.contest.resultclasses}">
		                                <div>
		                                    <g:checkBox name="resultclass_${resultclass_instance.id}" value="${true}" />
		                                    <label>${resultclass_instance.name} (${taskInstance.contest.GetResultTitle(taskInstance.GetClassResultSettings(resultclass_instance),false)})</label>
		                                </div>
	    	                    	</g:each>
	    	                    </p>
                        	</fieldset>
                        </g:if>
                        <fieldset>
                            <p>
	                           	<g:if test="${taskInstance.IsPlanningTestRun()}">
	                                <div>
	                                    <g:checkBox name="printPlanningResults" value="${taskInstance.printPlanningResults}" />
	                                    <label>${message(code:'fc.planningresults')}</label>
	                                </div>
                                    <div>
                                        <g:checkBox name="printPlanningResultsScan" value="${taskInstance.printPlanningResultsScan}" />
                                        <label>${message(code:'fc.planningtesttask.scannedforms')}</label>
                                    </div>
        	                    </g:if>
    	                       	<g:if test="${taskInstance.IsFlightTestRun()}">
	                                <div>
	                                    <g:checkBox name="printFlightResults" value="${taskInstance.printFlightResults}" />
	                                    <label>${message(code:'fc.flightresults')}</label>
	                                </div>
                                    <div>
                                        <g:checkBox name="printFlightMap" value="${taskInstance.printFlightMap}" />
                                        <label>${message(code:'fc.flightmaps')}</label>
                                    </div>
	                            </g:if>
    	                       	<g:if test="${taskInstance.IsObservationTestRun()}">
	                                <div>
	                                    <g:checkBox name="printObservationResults" value="${taskInstance.printObservationResults}" />
	                                    <label>${message(code:'fc.observationresults')}</label>
	                                </div>
                                    <div>
                                        <g:checkBox name="printObservationResultsScan" value="${taskInstance.printObservationResultsScan}" />
                                        <label>${message(code:'fc.observation.scannedforms')}</label>
                                    </div>
	                            </g:if>
    	                       	<g:if test="${taskInstance.IsLandingTestRun()}">
	                                <div>
	                                    <g:checkBox name="printLandingResults" value="${taskInstance.printLandingResults}" />
	                                    <label>${message(code:'fc.landingresults')}</label>
	                                </div>
	                            </g:if>
    	                       	<g:if test="${taskInstance.IsSpecialTestRun()}">
	                                <div>
	                                    <g:checkBox name="printSpecialResults" value="${taskInstance.printSpecialResults}" />
	                                    <label>${message(code:'fc.specialresults')}</label>
	                                </div>
	                            </g:if>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="printProvisionalResults" value="${taskInstance.printProvisionalResults}" />
                                    <label>${message(code:'fc.printprovisional')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <g:actionSubmit action="printcrewresults" value="${message(code:'fc.print')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>