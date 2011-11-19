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
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.scale')}:</label>
                                <br/>
                                <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:contestInstance,field:'mapScale')}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.planningtest')}</legend>
                            <p>
                                <label>${message(code:'fc.planningtest.directioncorrectgrad')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="planningTestDirectionCorrectGrad" name="planningTestDirectionCorrectGrad" value="${fieldValue(bean:contestInstance,field:'planningTestDirectionCorrectGrad')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.planningtest.directionpointspergrad')} [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="planningTestDirectionPointsPerGrad" name="planningTestDirectionPointsPerGrad" value="${fieldValue(bean:contestInstance,field:'planningTestDirectionPointsPerGrad')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.planningtest.timecorrectsecond')} [${message(code:'fc.time.s')}]:</label>
                                <br/>
                                <input type="text" id="planningTestTimeCorrectSecond" name="planningTestTimeCorrectSecond" value="${fieldValue(bean:contestInstance,field:'planningTestTimeCorrectSecond')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.planningtest.timepointspersecond')} [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="planningTestTimePointsPerSecond" name="planningTestTimePointsPerSecond" value="${fieldValue(bean:contestInstance,field:'planningTestTimePointsPerSecond')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.planningtest.maxpoints')} [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="planningTestMaxPoints" name="planningTestMaxPoints" value="${fieldValue(bean:contestInstance,field:'planningTestMaxPoints')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.planningtaskresults.giventolate')} [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="planningTestPlanTooLatePoints" name="planningTestPlanTooLatePoints" value="${fieldValue(bean:contestInstance,field:'planningTestPlanTooLatePoints')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.planningtaskresults.exitroomtolate')} [${message(code:'fc.points')}]:</label>
                                <br/>
                                <input type="text" id="planningTestExitRoomTooLatePoints" name="planningTestExitRoomTooLatePoints" value="${fieldValue(bean:contestInstance,field:'planningTestExitRoomTooLatePoints')}"/>
                            </p>
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