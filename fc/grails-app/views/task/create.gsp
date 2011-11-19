<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.create')}</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="newparams" value="['contestid':params.contestid]"/>
                    <g:form method="post" params="${newparams}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:taskInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.firsttime')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="firstTime" name="firstTime" value="${fieldValue(bean:taskInstance,field:'firstTime')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.takeoffinterval.normal')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalNormal" name="takeoffIntervalNormal" value="${fieldValue(bean:taskInstance,field:'takeoffIntervalNormal')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.task.risingduration')}* [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="risingDuration" name="risingDuration" value="${fieldValue(bean:taskInstance,field:'risingDuration')}"/>
                            </p>
                            <p>
                                <div>
	                               	<g:checkBox name="planningTestRun" value="${taskInstance.planningTestRun}" />
    	                            <label>${message(code:'fc.planningresults')}</label>
                                </div>
                                <div>
	                               	<g:checkBox name="flightTestRun" value="${taskInstance.flightTestRun}" />
    	                            <label>${message(code:'fc.flightresults')}</label>
                                </div>
                                <div>
	                               	<g:checkBox name="observationTestRun" value="${taskInstance.observationTestRun}" />
    	                            <label>${message(code:'fc.observationresults')}</label>
                                </div>
                                <div>
	                               	<g:checkBox name="landingTestRun" value="${taskInstance.landingTestRun}" />
    	                            <label>${message(code:'fc.landingresults')}</label>
                                </div>
                                <div>
	                               	<g:checkBox name="specialTestRun" value="${taskInstance.specialTestRun}" />
    	                            <label>${message(code:'fc.specialresults')}</label>
                                </div>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="planningTestDistanceMeasure" value="${taskInstance.planningTestDistanceMeasure}" />
                                    <label>${message(code:'fc.task.planningtestdistancemeasure')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="planningTestDirectionMeasure" value="${taskInstance.planningTestDirectionMeasure}" />
                                    <label>${message(code:'fc.task.planningtestdirectionmeasure')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>