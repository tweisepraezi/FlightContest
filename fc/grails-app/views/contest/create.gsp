<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.create')}</h2>
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
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>