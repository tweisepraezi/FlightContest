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
                @top-left {
                    content: "${message(code:'fc.aircraft.list')}"
                }
                @top-right {
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.aircraft.list')}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${message(code:'fc.aircraft.list')} (${aircraftInstanceList.size()})</h2>
                <div>
                    <g:form>
                        <table class="aircraftlist">
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
                                <g:each var="aircraft_instance" in="${aircraftInstanceList}" status="i" >
                                    <tr>
                                        <td class="aircraft">${aircraft_instance.registration}</td>
                                        <td class="aircrafttype">${aircraft_instance.type}</td>
                                        <td class="aircraftcolor">${aircraft_instance.colour}</td>
                                        <td class="crew1">${aircraft_instance.user1?.name}</td>
                                        <td class="crew2">${aircraft_instance.user2?.name}</td>
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