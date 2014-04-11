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
        <title>${message(code:'fc.crew.list')}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.list')} (${crewList.size()})<g:if test="${contestInstance.printCrewPrintTitle}"> - ${contestInstance.printCrewPrintTitle}</g:if></h2>
                <div class="block" id="forms" >
                    <g:form>
                         <table width="100%" border="1" cellspacing="0" cellpadding="2">
                            <thead>
                                <tr>
                                    <g:if test="${contestInstance.printCrewNumber}">
                                        <th>${message(code:'fc.crew.startnum.short')}</th>
                                    </g:if>
                                    <g:if test="${contestInstance.printCrewName}">
                                        <th>${message(code:'fc.crew.name')}</th>
                                    </g:if>
                                    <g:if test="${contestInstance.printCrewTeam}">
                                        <th>${message(code:'fc.crew.team')}</th>
                                    </g:if>
                                    <g:if test="${contestInstance.printCrewClass}">
                                        <g:if test="${resultClasses}">
                                            <th>${message(code:'fc.crew.resultclass')}</th>
                                        </g:if>
                                    </g:if>
                                    <g:if test="${contestInstance.printCrewAircraft}">
                                        <th>${message(code:'fc.crew.aircraft')}</th>
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
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${crewList}" status="i" var="crewInstance">
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <g:if test="${contestInstance.printCrewNumber}">
                                            <td>${crewInstance.startNum}</td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewName}">
                                            <td>${crewInstance.name}</td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewTeam}">
	                                        <g:if test="${crewInstance.team}">                          
	                                            <td>${crewInstance.team.name}</td>
			                                </g:if>
			                                <g:else>
			                                    <td>-</td>
			                                </g:else>
			                            </g:if>
			                            <g:if test="${contestInstance.printCrewClass}">
	                                        <g:if test="${resultClasses}">
	                                            <g:if test="${crewInstance.resultclass}">                          
	                                                <td>${crewInstance.resultclass?.name}</td>
			                                    </g:if>
			                                    <g:else>
			                                        <td>-</td>
			                                    </g:else>
	                                        </g:if>
	                                    </g:if>
                                        <g:if test="${contestInstance.printCrewAircraft}">
                                            <td><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.registration}<g:if test="${crewInstance.aircraft?.user1 && crewInstance.aircraft?.user2}">${HTMLFilter.NoWrapStr(' *')}</g:if></g:if><g:else>${message(code:'fc.noassigned')}</g:else></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewAircraftType}">
                                            <td><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.type}</g:if></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewAircraftColour}">
                                            <td><g:if test="${crewInstance.aircraft}">${crewInstance.aircraft.colour}</g:if></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewTAS}">
                                            <td>${fieldValue(bean:crewInstance, field:'tas')}${message(code:'fc.knot')}</td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn1}">
                                            <td width="10%"></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn2}">
                                            <td width="10%"></td>
                                        </g:if>
                                        <g:if test="${contestInstance.printCrewEmptyColumn3}">
                                            <td width="10%"></td>
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