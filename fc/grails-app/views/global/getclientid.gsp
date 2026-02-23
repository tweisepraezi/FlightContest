<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.internal')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.info')}</h2>
                <div class="block" id="forms" >
                    <g:form controller="global">
                        <g:if test="${BootStrap.global.ClientID}">
                            <p>${message(code:'fc.getclientid.value',args:["${BootStrap.global.ClientID}"])}</p>
                        </g:if>
                        <g:if test="${BootStrap.global.OwnerEMail && BootStrap.global.OwnerClub}">
                            <p>${message(code:'fc.config.owner',args:["${BootStrap.global.OwnerEMail}", "${BootStrap.global.OwnerClub}"])}</p>
                        </g:if>
                        <p>
                            <g:if test="${BootStrap.global.FCMapServer}">
                                ${message(code:'fc.config.fcmapserver.found')}<br/>
                            </g:if>
                            <g:if test="${BootStrap.global.OpenAIPServer && BootStrap.global.OpenAIPAPIKey}">
                                ${message(code:'fc.config.openaipserver.found')}<br/>
                            </g:if>
                            <g:if test="${BootStrap.global.ContourSources}">
                                ${message(code:'fc.config.contoursources.found')}<br/>
                            </g:if>
                            <g:if test="${BootStrap.global.SRTMUsername && BootStrap.global.SRTMPassword}">
                                ${message(code:'fc.config.srtmlogin.found')}<br/>
                            </g:if>
                            <g:if test="${BootStrap.global.PostgreSQLHost && BootStrap.global.PostgreSQLPort}">
                                ${message(code:'fc.config.postgresqlservice.found', args:[BootStrap.global.PostgreSQLHost, BootStrap.global.PostgreSQLPort])}<br/>
                            </g:if>
                            <g:if test="${BootStrap.global.PostgreSQLUsername && BootStrap.global.PostgreSQLPassword}">
                                ${message(code:'fc.config.postgresqllogin.found')}<br/>
                            </g:if>
                        </p>
                        <g:if test="${BootStrap.global.FCMapCounter}">
                            <p>${message(code:'fc.config.fcmapserver.runnum',args:["${BootStrap.global.FCMapCounter}"])}</p>
                        </g:if>
                        <g:actionSubmit action="loadconfig" value="${message(code:'fc.config.load')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>