<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                @top-center {
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.list')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.aircraft.list')} (${aircraftInstanceList.size()})</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                   <th>${message(code:'fc.aircraft.registration')}</th>
                                   <th>${message(code:'fc.aircraft.type')}</th>
                                   <th>${message(code:'fc.aircraft.colour')}</th>
                                   <th>${message(code:'fc.aircraft.crew1')}</th>
                                   <th>${message(code:'fc.aircraft.crew2')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each var="aircraftInstance" in="${aircraftInstanceList}" status="i" >
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${aircraftInstance.registration}</td>
                                        <td>${aircraftInstance.type}</td>
                                        <td>${aircraftInstance.colour}</td>
                                        <td>${aircraftInstance.user1?.name}</td>
                                        <td>${aircraftInstance.user2?.name}</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>