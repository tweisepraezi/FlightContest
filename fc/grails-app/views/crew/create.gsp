<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.create')}</h2>
                <g:hasErrors bean="${crewInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${crewInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.crew.name')}:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:crewInstance,field:'name')}"/>
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
                                <input type="text" id="registration" name="registration" value="${fieldValue(bean:crewInstance,field:'registration')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.type')}:</label>
                                <br/>
                                <input type="text" id="type" name="type" value="${fieldValue(bean:crewInstance,field:'type')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.colour')}:</label>
                                <br/>
                                <input type="text" id="colour" name="colour" value="${fieldValue(bean:crewInstance,field:'colour')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.tas')} [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="tas" name="tas" value="${fieldValue(bean:crewInstance,field:'tas')}" />
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