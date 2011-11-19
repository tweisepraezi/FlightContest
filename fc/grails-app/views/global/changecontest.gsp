<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.changecontest')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'global')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.changecontest')}</h2>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.contest')}:</label>
                                <br/>
                                <g:select from="${Contest.list()}" optionValue="${{it.name()}}" name="contestid" optionKey="id" />
                            </p>
                        </fieldset>
                        <g:actionSubmit action="updatecontest" value="${message(code:'fc.update')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>