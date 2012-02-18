<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="aircraft" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.aircraft.create')}</h2>
                <g:hasErrors bean="${aircraftInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${aircraftInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.aircraft.registration')}*:</label>
                                <br/>
                                <input type="text" id="registration" name="registration" value="${fieldValue(bean:aircraftInstance,field:'registration')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.type')}:</label>
                                <br/>
                                <input type="text" id="type" name="type" value="${fieldValue(bean:aircraftInstance,field:'type')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.aircraft.colour')}:</label>
                                <br/>
                                <input type="text" id="colour" name="colour" value="${fieldValue(bean:aircraftInstance,field:'colour')}" tabIndex="3"/>
                            </p>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="4"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="5"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>