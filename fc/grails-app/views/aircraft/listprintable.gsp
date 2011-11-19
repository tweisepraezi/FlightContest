<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.aircraft.list')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.aircraft.list')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                   <th>${message(code:'fc.aircraft.registration')}</th>
                                   <th>${message(code:'fc.aircraft.type')}</th>
                                   <th>${message(code:'fc.aircraft.colour')}</th>
                                   <th>${message(code:'fc.aircraft.user1')}</th>
                                   <th>${message(code:'fc.aircraft.user2')}</th>
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