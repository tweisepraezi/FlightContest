<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" importaction="${message(code:'fc.crew.import')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.edit')}</h2>
                <g:hasErrors bean="${crewInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${crewInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['crewReturnAction':crewReturnAction,'crewReturnController':crewReturnController,'crewReturnID':crewReturnID]}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.crew.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:crewInstance,field:'name')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.import.name')}:</label>
                                <br/>
                                <input type="text" id="mark" name="mark" value="${fieldValue(bean:crewInstance,field:'mark')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.country')}:</label>
                                <br/>
                                <input type="text" id="country" name="country" value="${fieldValue(bean:crewInstance,field:'country')}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.crew.aircraft')}</legend>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="registration" from="${Aircraft.list(sort:'registration')}" name="aircraft.id" value="${crewInstance?.aircraft?.id}" noSelection="['null':'']"></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="tas" name="tas" value="${fieldValue(bean:crewInstance,field:'tas')}" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <div>
                                    <g:checkBox name="disabled" value="${crewInstance.disabled}" />
                                    <label>${message(code:'fc.disabled')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${crewInstance?.id}" />
                        <input type="hidden" name="version" value="${crewInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>