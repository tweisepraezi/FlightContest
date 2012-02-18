<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.resultclass.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="resultClass" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.resultclass.create')}</h2>
                <g:hasErrors bean="${resultclassInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${resultclassInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.resultclass.name')}*:</label>
                                <br/>
                                <input type="text" id="name" name="name" value="${fieldValue(bean:resultclassInstance,field:'name')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.resultclass.contesttitle')}:</label>
                                <br/>
                                <input type="text" id="contestTitle" name="contestTitle" value="${fieldValue(bean:resultclassInstance,field:'contestTitle')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestrule')}*:</label>
                                <br/>
                                <g:select from="${ContestRules.values()}" optionValue="${{message(code:it.titleCode)}}" name="contestRule" value="${contestInstance.contestRule}" tabIndex="3"/>
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