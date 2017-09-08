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
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    content: "${message(code:'fc.aircraft.list')}"
                }
                @top-right {
                    font-family: Noto Sans;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
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
        <h2>${message(code:'fc.aircraft.list')} (${aircraftInstanceList.size()})</h2>
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
                        <tr class="value" id="${aircraft_instance.registration}">
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
    </body>
</html>