<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.differences')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.differences')} - ${taskInstance.name()}</h2>
                <g:hasErrors bean="${taskInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${taskInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <g:listDifferences t="${taskInstance}" />
                        <fieldset>
                            <div>
                                ${message(code:'fc.task.differences.showoffset')}: <input type="text" id="showOffset" name="showOffset" value="${fieldValue(bean:taskInstance,field:'showOffset')}" tabIndex="${ti[0]++}"/>
                                <g:checkBox name="showTurnPoints" value="${taskInstance.showTurnPoints}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.task.differences.showturnpoints')}</label>
                                <g:if test="${!taskInstance.flighttest.route.corridorWidth}">
                                    <g:checkBox name="showTurnPointSigns" value="${taskInstance.showTurnPointSigns}" tabIndex="${ti[0]++}"/>
                                    <label>${message(code:'fc.task.differences.showturnpointsigns')}</label>
                                    <g:checkBox name="showEnroutePhotos" value="${taskInstance.showEnroutePhotos}" tabIndex="${ti[0]++}"/>
                                    <label>${message(code:'fc.task.differences.showenroutephotos')}</label>
                                    <g:checkBox name="showEnrouteCanavas" value="${taskInstance.showEnrouteCanavas}" tabIndex="${ti[0]++}"/>
                                    <label>${message(code:'fc.task.differences.showenroutecanavas')}</label>
                                </g:if>
                            </div>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}"/>
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="refresh" value="${message(code:'fc.refresh')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>