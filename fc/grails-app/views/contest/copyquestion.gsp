<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.copy')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.copy')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}*:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="copyContestSettings" value="${contestInstance.copyContestSettings}" />
                                    <label>${message(code:'fc.contest.copycontestsettings')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="copyRoutes" value="${contestInstance.copyRoutes}" />
                                    <label>${message(code:'fc.contest.copyroutes')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="copyCrews" value="${contestInstance.copyCrews}" />
                                    <label>${message(code:'fc.contest.copycrews')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="copyTaskSettings" value="${contestInstance.copyTaskSettings}" />
                                    <label>${message(code:'fc.contest.copytasksettings')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}" />
                        <g:actionSubmit action="copy" value="${message(code:'fc.copy')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>