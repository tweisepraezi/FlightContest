<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestdaytask.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestdaytask.edit')}</h2>
                <g:hasErrors bean="${contestDayTaskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestDayTaskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.contestdaytask.from')}:</td>
                                    <td><g:contestday var="${contestDayTaskInstance?.contestday}" link="${createLink(controller:'contestDay',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')} (${contestDayTaskInstance.idName()}):</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestDayTaskInstance,field:'title')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.firsttime')}:</label>
                                <br/>
                                <input type="text" id="firstTime" name="firstTime" value="${fieldValue(bean:contestDayTaskInstance,field:'firstTime')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.takeoffinterval.normal')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalNormal" name="takeoffIntervalNormal" value="${fieldValue(bean:contestDayTaskInstance,field:'takeoffIntervalNormal')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.takeoffinterval.fasteraircraft')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="takeoffIntervalFasterAircraft" name="takeoffIntervalFasterAircraft" value="${fieldValue(bean:contestDayTaskInstance,field:'takeoffIntervalFasterAircraft')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.planningtestduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="planningTestDuration" name="planningTestDuration" value="${fieldValue(bean:contestDayTaskInstance,field:'planningTestDuration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.preparationduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="preparationDuration" name="preparationDuration" value="${fieldValue(bean:contestDayTaskInstance,field:'preparationDuration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.risingduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="risingDuration" name="risingDuration" value="${fieldValue(bean:contestDayTaskInstance,field:'risingDuration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.landingduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="landingDuration" name="landingDuration" value="${fieldValue(bean:contestDayTaskInstance,field:'landingDuration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.minnextflightduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="minNextFlightDuration" name="minNextFlightDuration" value="${fieldValue(bean:contestDayTaskInstance,field:'minNextFlightDuration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.procedureturnduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="procedureTurnDuration" name="procedureTurnDuration" value="${fieldValue(bean:contestDayTaskInstance,field:'procedureTurnDuration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestdaytask.addtimevalue')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="addTimeValue" name="addTimeValue" value="${fieldValue(bean:contestDayTaskInstance,field:'addTimeValue')}"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="planningTestDistanceMeasure" value="${contestDayTaskInstance.planningTestDistanceMeasure}" />
                                    <label>${message(code:'fc.contestdaytask.planningtestdistancemeasure')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="planningTestDirectionMeasure" value="${contestDayTaskInstance.planningTestDirectionMeasure}" />
                                    <label>${message(code:'fc.contestdaytask.planningtestdirectionmeasure')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestDayTaskInstance?.id}" />
                        <input type="hidden" name="version" value="${contestDayTaskInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>