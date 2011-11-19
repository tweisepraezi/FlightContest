<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" newaction="${message(code:'fc.aircraft.new')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aircraft.edit')}</h2>
                <g:hasErrors bean="${aircraftInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${aircraftInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['aircraftReturnAction':aircraftReturnAction,'aircraftReturnController':aircraftReturnController,'aircraftReturnID':aircraftReturnID]}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}*:</label>
                                <br/>
                                <input type="text" id="registration" name="registration" value="${fieldValue(bean:aircraftInstance,field:'registration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.type')}:</label>
                                <br/>
                                <input type="text" id="type" name="type" value="${fieldValue(bean:aircraftInstance,field:'type')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.colour')}:</label>
                                <br/>
                                <input type="text" id="colour" name="colour" value="${fieldValue(bean:aircraftInstance,field:'colour')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${aircraftInstance?.id}" />
                        <input type="hidden" name="version" value="${aircraftInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>