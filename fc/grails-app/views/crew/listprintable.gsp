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
				    content: "${message(code:'fc.crew.list')}"
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
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <div>
            <div>
                <h2>${message(code:'fc.crew.list')} (${crewList.size()})<g:if test="${contestInstance.printCrewPrintTitle}"> - ${contestInstance.printCrewPrintTitle}</g:if></h2>
                <div>
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
                                    <g:if test="${contestInstance.printCrewTeam}">
                                        <th>${message(code:'fc.team')}</th>
                                    </g:if>
                                    <g:if test="${contestInstance.printCrewClass}">
                                        <g:if test="${resultClasses}">
                                            <th>${message(code:'fc.resultclass')}</th>
                                        </g:if>
                                    </g:if>
                                    <g:if test="${contestInstance.printCrewShortClass}">
                                        <g:if test="${resultClasses}">
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
                                <g:each in="${crewList}" status="i" var="crewInstance">
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <g:if test="${contestInstance.printCrewNumber}">
                                            <td class="num">${crewInstance.startNum}</td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewName}">
                                            <td class="crew">${crewInstance.name}</td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewTeam}">
	                                        <g:if test="${crewInstance.team}">                          
	                                            <td class="team">${crewInstance.team.name}</td>
			                                </g:if>
			                                <g:else>
			                                    <td class="team">-</td>
			                                </g:else>
			                            </g:if>
			                            <g:if test="${contestInstance.printCrewClass}">
	                                        <g:if test="${resultClasses}">
	                                            <g:if test="${crewInstance.resultclass}">                          
	                                                <td class="resultclass">${crewInstance.resultclass?.name}</td>
			                                    </g:if>
			                                    <g:else>
			                                        <td class="resultclass">-</td>
			                                    </g:else>
	                                        </g:if>
	                                    </g:if>
                                        <g:if test="${contestInstance.printCrewShortClass}">
                                            <g:if test="${resultClasses}">
                                                <g:if test="${crewInstance.resultclass}">                          
                                                    <td class="shortresultclass">${crewInstance.resultclass?.shortName}</td>
                                                </g:if>
                                                <g:else>
                                                    <td class="shortresultclass">-</td>
                                                </g:else>
                                            </g:if>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewAircraft}">
                                            <td class="aircraft"><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.registration}<g:if test="${crewInstance.aircraft?.user1 && crewInstance.aircraft?.user2}">${HTMLFilter.NoWrapStr(' *')}</g:if></g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewAircraftType}">
                                            <td class="aircrafttype"><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.type}</g:if></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewAircraftColour}">
                                            <td class="aircraftcolor"><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.colour}</g:if></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewTAS}">
                                            <td class="tas">${fieldValue(bean:crewInstance, field:'tas')}${message(code:'fc.knot')}</td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn1}">
                                            <td class="empty1"></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn2}">
                                            <td class="empty2"></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn3}">
                                            <td class="empty3"></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn4}">
                                            <td class="empty4"></td>
                                        </g:if>
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