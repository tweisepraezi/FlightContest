<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.printfreetext')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.printfreetext')}</h2>
                <g:hasErrors bean="${contestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['contestReturnAction':contestReturnAction,'contestReturnController':contestReturnController,'contestReturnID':contestReturnID]}" >
                        <p>
                            <label>${message(code:'fc.title')}:</label>
                            <br/>
                            <g:textField name="printFreeTextTitle" value="${contestInstance.printFreeTextTitle}" size="110" tabIndex="1"/>
                        </p>
                        <p>
                            <label>${message(code:'fc.contest.freetext')}:</label>
                            <br/>
                            <g:textArea name="printFreeText" value="${contestInstance.printFreeText}" rows="10" cols="110" tabIndex="2"/>
                        </p>
                        <p>
                            <label>${message(code:'fc.contest.paper.style')}:</label>
                            <br/>
                            <g:textArea name="printFreeTextStyle" value="${contestInstance.printFreeTextStyle}" rows="5" cols="110" tabIndex="3"/>
                        </p>
                        <p>
                            <div>
                                <g:checkBox name="printFreeTextLandscape" value="${contestInstance.printFreeTextLandscape}" />
                                <label>${message(code:'fc.printlandscape')}</label>
                            </div>
                            <div>
                                <g:checkBox name="printFreeTextA3" value="${contestInstance.printFreeTextA3}" />
                                <label>${message(code:'fc.printa3')}</label>
                            </div>
                        </p>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="savefreetext" value="${message(code:'fc.save')}" tabIndex="201"/>
                        <g:actionSubmit action="printfreetext" value="${message(code:'fc.print')}" tabIndex="202"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="203"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>