<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.livesettings')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'global')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.livesettings')}</h2>
                <g:hasErrors bean="${globalInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${globalInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post">
                        <fieldset>
                            <label>${message(code:'fc.livesettings.view.contest')}:</label>
                            <br/>
                            <g:set var="view_labels" value="${[message(code:'fc.livesettings.view.off')]}"/>
                            <g:set var="view_values" value="${[0]}"/>
                            <g:set var="view_value" value="${0}"/>
                            
                            <g:each var="contest_instance" in="${Contest.list([sort:"id"])}">
                                <g:set var="view_labels" value="${view_labels += contest_instance.title}"/>
                                <g:set var="view_values" value="${view_values += contest_instance.id}"/>
                                <g:if test="${globalInstance.liveContestID == contest_instance.id}">
                                    <g:set var="view_value" value="${globalInstance.liveContestID}"/>
                                </g:if>
                            </g:each>
                            
                            <g:radioGroup name="liveContestID" labels="${view_labels}" values="${view_values}" value="${view_value}">
                                <div>
                                    <label>${it.radio} ${it.label}</label>
                                </div>
                            </g:radioGroup>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.livesettings.view.uploadtime')}* [${message(code:'fc.time.s')}]:</label>
                                <br/>
                                <g:if test="${globalInstance.liveContestID && (globalInstance.liveUploadSeconds > 0)}">
                                    <input type="text" id="liveUploadSeconds" name="liveUploadSeconds" value="${fieldValue(bean:globalInstance,field:'liveUploadSeconds')}" tabIndex="1" disabled/>
                                </g:if>
                                <g:else>
                                    <input type="text" id="liveUploadSeconds" name="liveUploadSeconds" value="${fieldValue(bean:globalInstance,field:'liveUploadSeconds')}" tabIndex="1"/>
                                </g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.language')}*:</label>
                                <br/>
                                <g:select from="${Languages.values()}" name="liveLanguage" value="${globalInstance.liveLanguage}" optionValue="title" tabIndex="2"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.livesettings.view.publicurls')}:</label>
                                <g:each var="url" in="${urlList}">
                                    <br/><a href="${url}" target="_blank">${url}</a>
                                </g:each>
                            </p>
                        </fieldset>
                        <input type="hidden" name="newwindow" value="${params.newwindow}" />
                        <g:actionSubmit action="savelivesettings" value="${message(code:'fc.save')}" tabIndex="101"/>
                        <g:if test="${contestInstance && !globalInstance.liveContestID}">
                            <g:actionSubmit action="liverunonce" value="${message(code:'fc.livesettings.view.liverunonce')}" tabIndex="102"/>
                            <g:actionSubmit action="enablelive" value="${message(code:'fc.livesettings.view.enablelive')}" tabIndex="103"/>
                            <g:actionSubmit action="uploadlivestylesheet" value="${message(code:'fc.livesettings.view.uploadstylesheet')}" tabIndex="105"/>
                            <g:actionSubmit action="uploadnoliveresults" value="${message(code:'fc.livesettings.view.removeresults')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="106"/>
                        </g:if>
                        <g:if test="${globalInstance.liveContestID}">
                            <g:actionSubmit action="disablelive" value="${message(code:'fc.livesettings.view.disablelive')}" tabIndex="104"/>
                        </g:if>
                        <g:if test="${!(params.newwindow=='true')}">
                            <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="107"/>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>