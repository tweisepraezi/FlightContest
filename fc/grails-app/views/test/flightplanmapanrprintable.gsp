<html>
    <g:set var="show_crew" value="${params.crews == "true"}"/>
    <g:set var="show_results" value="${params.results == "true"}"/>
    <head>
        <style type="text/css">
            @page {
                size: A4 landscape;
                margin-left: 3%;
                margin-right: 3%;
                margin-top: 5%;
                margin-bottom: 5%;
                @top-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    <g:if test="${show_crew}">
                        content: "${testInstance.task.printName()} (${message(code:'fc.test.timetable.version')} ${testInstance.task.GetTimeTableVersion()}) - ${message(code:'fc.test.flightplan')} ${testInstance.GetStartNum()}"
                    </g:if>
                    <g:elseif test="${show_results}">
                        content: "${testInstance.task.printName()} - ${message(code:'fc.test.planning')} ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}"
                    </g:elseif>
                    <g:else>
                        content: "${testInstance.task.printName()}"
                    </g:else>
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    <g:if test="${show_crew}">
                        content: "${testInstance.GetTestPos()}"
                    </g:if>
                    <g:elseif test="${show_results}">
                        content: "${params.page}"
                    </g:elseif>
                    <g:else>
                    </g:else>
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="printanr" />
        <style type="text/css">${contestInstance.printStyle}</style>
    </head>
    <body>
        <g:form>
            <g:anrCrew t="${testInstance}" showCrew="${show_crew}"/>
            <table class="anr">
                <tbody>
                    <tr>
                        <td>
                            <g:anrMap t="${testInstance}" />
                        </td>
                        <td>
                            <g:anrFlightPlan t="${testInstance}" showCrew="${show_crew}" showResults="${show_results}" showDuration="${false}"/>
                        </td>
                    </tr>
                </tbody>
            </table>
       </g:form>
    </body>
</html>