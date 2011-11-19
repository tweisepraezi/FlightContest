<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.edit')}</h2>
                <g:hasErrors bean="${contestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}*:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.scale')}*:</label>
                                <br/>
                                <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:contestInstance,field:'mapScale')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.timezone')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="timeZone" name="timeZone" value="${fieldValue(bean:contestInstance,field:'timeZone')}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.planningtest')}</legend>
                            <p/>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.planningtest.directioncorrectgrad')}* [${message(code:'fc.grad')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestDirectionCorrectGrad" name="planningTestDirectionCorrectGrad" value="${fieldValue(bean:contestInstance,field:'planningTestDirectionCorrectGrad')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.directionpointspergrad')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestDirectionPointsPerGrad" name="planningTestDirectionPointsPerGrad" value="${fieldValue(bean:contestInstance,field:'planningTestDirectionPointsPerGrad')}"/>
	                            </p>
	                        </fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.planningtest.timecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestTimeCorrectSecond" name="planningTestTimeCorrectSecond" value="${fieldValue(bean:contestInstance,field:'planningTestTimeCorrectSecond')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.timepointspersecond')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestTimePointsPerSecond" name="planningTestTimePointsPerSecond" value="${fieldValue(bean:contestInstance,field:'planningTestTimePointsPerSecond')}"/>
	                            </p>
	                        </fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.planningtest.maxpoints')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestMaxPoints" name="planningTestMaxPoints" value="${fieldValue(bean:contestInstance,field:'planningTestMaxPoints')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.giventolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestPlanTooLatePoints" name="planningTestPlanTooLatePoints" value="${fieldValue(bean:contestInstance,field:'planningTestPlanTooLatePoints')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.planningtest.exitroomtolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="planningTestExitRoomTooLatePoints" name="planningTestExitRoomTooLatePoints" value="${fieldValue(bean:contestInstance,field:'planningTestExitRoomTooLatePoints')}"/>
	                            </p>
	                        </fieldset>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.flighttest')}</legend>
                            <p/>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.takeoffmissed')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestTakeoffMissedPoints" name="flightTestTakeoffMissedPoints" value="${fieldValue(bean:contestInstance,field:'flightTestTakeoffMissedPoints')}"/>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cptimecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCptimeCorrectSecond" name="flightTestCptimeCorrectSecond" value="${fieldValue(bean:contestInstance,field:'flightTestCptimeCorrectSecond')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cptimepointspersecond')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCptimePointsPerSecond" name="flightTestCptimePointsPerSecond" value="${fieldValue(bean:contestInstance,field:'flightTestCptimePointsPerSecond')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cptimemaxpoints')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCptimeMaxPoints" name="flightTestCptimeMaxPoints" value="${fieldValue(bean:contestInstance,field:'flightTestCptimeMaxPoints')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.cpnotfound')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestCpNotFoundPoints" name="flightTestCpNotFoundPoints" value="${fieldValue(bean:contestInstance,field:'flightTestCpNotFoundPoints')}"/>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.procedureturnnotflown')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestProcedureTurnNotFlownPoints" name="flightTestProcedureTurnNotFlownPoints" value="${fieldValue(bean:contestInstance,field:'flightTestProcedureTurnNotFlownPoints')}"/>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.minaltitudemissed')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestMinAltitudeMissedPoints" name="flightTestMinAltitudeMissedPoints" value="${fieldValue(bean:contestInstance,field:'flightTestMinAltitudeMissedPoints')}"/>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.badcoursecorrectsecond')}* [${message(code:'fc.time.s')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestBadCourseCorrectSecond" name="flightTestBadCourseCorrectSecond" value="${fieldValue(bean:contestInstance,field:'flightTestBadCourseCorrectSecond')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.badcourse')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestBadCoursePoints" name="flightTestBadCoursePoints" value="${fieldValue(bean:contestInstance,field:'flightTestBadCoursePoints')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.badcoursestartlanding')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestBadCourseStartLandingPoints" name="flightTestBadCourseStartLandingPoints" value="${fieldValue(bean:contestInstance,field:'flightTestBadCourseStartLandingPoints')}"/>
	                            </p>
	                            <p>
	                                <label>${message(code:'fc.flighttest.landingtolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestLandingToLatePoints" name="flightTestLandingToLatePoints" value="${fieldValue(bean:contestInstance,field:'flightTestLandingToLatePoints')}"/>
	                            </p>
                        	</fieldset>
	                        <fieldset>
	                            <p>
	                                <label>${message(code:'fc.flighttest.giventolate')}* [${message(code:'fc.points')}]:</label>
	                                <br/>
	                                <input type="text" id="flightTestGivenToLatePoints" name="flightTestGivenToLatePoints" value="${fieldValue(bean:contestInstance,field:'flightTestGivenToLatePoints')}"/>
	                            </p>
	                        </fieldset>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}" />
                        <input type="hidden" name="version" value="${contestInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>