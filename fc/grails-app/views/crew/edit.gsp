<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" newaction="${message(code:'fc.crew.new')}" />
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
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.crew.name1')}:</label>
                                <br/>
                                <input type="text" id="name1" name="name1" value="${fieldValue(bean:crewInstance,field:'name1')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.name2')}:</label>
                                <br/>
                                <input type="text" id="name2" name="name2" value="${fieldValue(bean:crewInstance,field:'name2')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.country')}:</label>
                                <br/>
                                <input type="text" id="country" name="country" value="${fieldValue(bean:crewInstance,field:'country')}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.crew.ownaircraft')}</legend>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="registration" from="${Aircraft.list(sort:'registration')}" name="ownAircraft.id" value="${crewInstance?.ownAircraft?.id}" noSelection="['null':'']"></g:select>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.crew.usedaircraft')}</legend>
                            <p>
                                <label>${message(code:'fc.crew.usedaircraft')}:</label>
                                <br/>
                                <g:select optionKey="id" optionValue="registration" from="${Aircraft.list(sort:'registration')}" name="usedAircraft.id" value="${crewInstance?.usedAircraft?.id}" noSelection="['null':'']"></g:select>
                            </p>
                            <p>
                                <label>${message(code:'fc.crew.usedtas')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="usedTAS" name="usedTAS" value="${fieldValue(bean:crewInstance,field:'usedTAS')}" />
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${crewInstance?.id}" />
                        <input type="hidden" name="version" value="${crewInstance?.version}" />
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>