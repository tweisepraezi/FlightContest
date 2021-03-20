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
                    font-size: 90%;
				    content: "${message(code:'fc.crew.list')}"
				}
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printpage')} " counter(page)
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}";
                }
            }
        </style>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <h2>${message(code:'fc.crew.list')} (${crewNum})<g:if test="${contestInstance.printCrewPrintTitle}"> - ${contestInstance.printCrewPrintTitle}</g:if></h2>
        <g:form>
             <table class="crewlist">
                <thead>
                    <tr>
                        <g:if test="${contestInstance.printCrewNumber}">
                            <th>${message(code:'fc.crew.startnum.short')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewName}">
                            <th>${message(code:'fc.crew.name')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewEmail}">
                            <th>${message(code:'fc.crew.email')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewTeam}">
                            <th>${message(code:'fc.team')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewClass}">
                            <g:if test="${contestInstance.resultClasses}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
                        </g:if>
                        <g:if test="${contestInstance.printCrewShortClass}">
                            <g:if test="${contestInstance.resultClasses}">
                                <th>${message(code:'fc.resultclass.short.short')}</th>
                            </g:if>
                        </g:if>
                        <g:if test="${contestInstance.printCrewAircraft}">
                            <th>${message(code:'fc.aircraft')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewAircraftType}">
                            <th>${message(code:'fc.aircraft.type')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewAircraftColour}">
                            <th>${message(code:'fc.aircraft.colour')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewTAS}">
                            <th>${message(code:'fc.tas')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewTrackerID}">
                            <th>${message(code:'fc.crew.trackerid')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewUUID}">
                            <th>${message(code:'fc.crew.uuid')}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewEmptyColumn1}">
                            <th>${contestInstance.printCrewEmptyTitle1}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewEmptyColumn2}">
                            <th>${contestInstance.printCrewEmptyTitle2}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewEmptyColumn3}">
                            <th>${contestInstance.printCrewEmptyTitle3}</th>
                        </g:if>
                        <g:if test="${contestInstance.printCrewEmptyColumn4}">
                            <th>${contestInstance.printCrewEmptyTitle4}</th>
                        </g:if>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${crewList}">
                     <g:each var="crew_instance" in="${crewList}" >
                         <g:crewPrintable crew="${crew_instance}" contest="${contestInstance}" />
                     </g:each>
                 </g:if>
                 <g:else>
                        <g:each var="test_instance" in="${testList}">
                            <g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled}">
                                <g:crewPrintable crew="${test_instance.crew}" contest="${contestInstance}" />
                            </g:if>
                        </g:each>
                 </g:else>
                </tbody>
            </table>
        </g:form>
    </body>
</html>