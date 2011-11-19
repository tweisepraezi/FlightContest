<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.list')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                         <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <th>${message(code:'fc.crew.name')}</th>
                                    <th>${message(code:'fc.crew.country')}</th>
                                    <th>${message(code:'fc.crew.aircraft')}</th>
                                    <th>${message(code:'fc.tas')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${crewInstanceList}" status="i" var="crewInstance">
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${crewInstance.name}</td>
                                        <td>${crewInstance.country}</td>
                                        <td>${crewInstance.aircraft.registration}</td>
                                        <td>${crewInstance.tas}${message(code:'fc.knot')}</td>
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