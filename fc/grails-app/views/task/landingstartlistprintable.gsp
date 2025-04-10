<html>
    <head>
        <g:set var="landingtest_num" value="${taskInstance.GetLandingTests().size()}"/>
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
                    content: "${message(code:'fc.task.landingstartlist')} - ${taskInstance.printName()} (${message(code:'fc.crew.num', args:[landingtest_num])})"
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
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
			}
		</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title>${message(code:'fc.task.landingstartlist')}</title>
    </head>
    <body>
	    <h2>${message(code:'fc.task.landingstartlist')}<g:if test="${taskInstance.printLandingStartlistPrintTitle}"> - ${taskInstance.printLandingStartlistPrintTitle}</g:if></h2>
	    <h3>${taskInstance.printName()} (${message(code:'fc.crew.num', args:[landingtest_num])})</h3>
        <g:if test="${taskInstance.printLandingStartlistLandingField}">
            <h4>${message(code:'fc.test.landing.printinfo', args:[BootStrap.global.GetLandingInfo()])}</h4>
        </g:if>
        <g:form>
            <table class="landingstartlistlist">
                <thead>
                    <tr>
						<g:if test="${taskInstance.printLandingStartlistGroupCrewNum > 0}">
							<th>${message(code:'fc.group')}</th>
						</g:if>
                        <g:if test="${taskInstance.printLandingStartlistNumber}">
                            <th>${message(code:'fc.number')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistCrew}">
                            <th>${message(code:'fc.crew')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistAircraft}">
                            <th>${message(code:'fc.aircraft')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistAircraftType}">
                            <th>${message(code:'fc.aircraft.type')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistAircraftColour}">
                            <th>${message(code:'fc.aircraft.colour')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistTAS}">
                            <th>${message(code:'fc.tas')}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistTeam}">
                            <th>${message(code:'fc.team')}</th>
                        </g:if>
                        <g:if test="${taskInstance.contest.resultClasses}">
                            <g:if test="${taskInstance.printLandingStartlistClass}">
                                <th>${message(code:'fc.resultclass')}</th>
                            </g:if>
                            <g:if test="${taskInstance.printLandingStartlistShortClass}">
                                <th>${message(code:'fc.resultclass.short.short')}</th>
                            </g:if>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistEmptyColumn1}">
                            <th>${taskInstance.printLandingStartlistEmptyTitle1}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistEmptyColumn2}">
                            <th>${taskInstance.printLandingStartlistEmptyTitle2}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistEmptyColumn3}">
                            <th>${taskInstance.printLandingStartlistEmptyTitle3}</th>
                        </g:if>
                        <g:if test="${taskInstance.printLandingStartlistEmptyColumn4}">
                            <th>${taskInstance.printLandingStartlistEmptyTitle4}</th>
                        </g:if>
                    </tr>
                </thead>
                <tbody>
					<g:set var="crew_pos" value="${0}"/>
					<g:set var="group_num" value="${0}"/>
                    <g:each var="test_instance" in="${Test.findAllByTask(taskInstance,[sort:'viewpos'])}">
                       	<g:if test="${!test_instance.disabledCrew && !test_instance.crew.disabled && test_instance.IsLandingTestRun()}">
							<g:if test="${test_instance.pageBreak}">
								<div style="page-break-before:always"/>
 								<g:set var="crew_pos" value="${0}"/>
							</g:if>
                            <tr class="value" id="${test_instance.crew.startNum}">
								<g:if test="${taskInstance.printLandingStartlistGroupCrewNum > 0}">
									<g:set var="crew_pos" value="${crew_pos+1}"/>
									<g:if test="${taskInstance.IsGroupStart(test_instance.GetStartNum())}">
										<g:set var="crew_pos" value="${1}"/>
									</g:if>
									<g:if test="${crew_pos == 1}">
										<g:set var="group_num" value="${group_num+1}"/>
										<td class="group">${group_num}</td>
									</g:if>
									<g:else>
										<td class="group" style="border-top: 1px solid white;"></td>
									</g:else>
									<g:if test="${taskInstance.printLandingStartlistGroupCrewNum == crew_pos}">
										<g:set var="crew_pos" value="${0}"/>
									</g:if>
								</g:if>
                                <g:if test="${taskInstance.printLandingStartlistNumber}">
                                    <td class="num">${test_instance.GetStartNum()}</td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistCrew}">
                                    <td class="crew">${HTMLFilter.NoWrapStr(test_instance.crew.name)}</td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistAircraft}">
                                    <td class="aircraft">${test_instance.taskAircraft.registration}<g:if test="${test_instance.taskAircraft?.user1 && test_instance.taskAircraft?.user2}">${HTMLFilter.NoWrapStr(' *')}</g:if></td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistAircraftType}">
                                    <td class="aircrafttype">${test_instance.taskAircraft.type}</td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistAircraftColour}">
                                    <td class="aircraftcolor">${test_instance.taskAircraft.colour}</td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistTAS}">
                                    <td class="tas">${fieldValue(bean:test_instance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistTeam}">
                                    <td class="team"><g:if test="${test_instance.crew.team}">${HTMLFilter.NoWrapStr(test_instance.crew.team.name)}</g:if></td>
                                </g:if>
                                <g:if test="${taskInstance.contest.resultClasses}">
                                    <g:if test="${taskInstance.printLandingStartlistClass}">
                                        <td class="resultclass"><g:if test="${test_instance.crew.resultclass}">${HTMLFilter.NoWrapStr(test_instance.crew.resultclass.name)}</g:if></td>
                                    </g:if>
                                    <g:if test="${taskInstance.printLandingStartlistShortClass}">
                                        <td class="shortresultclass"><g:if test="${test_instance.crew.resultclass}">${HTMLFilter.NoWrapStr(test_instance.crew.resultclass.shortName)}</g:if></td>
                                    </g:if>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistEmptyColumn1}">
                                    <td class="empty1"></td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistEmptyColumn2}">
                                    <td class="empty2"></td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistEmptyColumn3}">
                                    <td class="empty3"></td>
                                </g:if>
                                <g:if test="${taskInstance.printLandingStartlistEmptyColumn4}">
                                    <td class="empty4"></td>
                                </g:if>
                            </tr>
                        </g:if>
                    </g:each>
                </tbody>
            </table>
            <g:if test="${taskInstance.printLandingStartlistLandingField}">
                <div style="page-break-before: always; color: white; height: 2px;">.</div>
                <g:each var="landingfield_imagename" in="${FcService.GetLandingAirfieldImageNames(taskInstance.contest)}">
                    <g:set var="landingfield_imagename2" value="${landingfield_imagename}"/>
                    <g:if test="${landingfield_imagename.contains('*')}">
                        <g:set var="landingfield_imagename2" value="${landingfield_imagename.replace('*', "_${session.printLanguage}")}"/>
                    </g:if>
                    <img class="landingfield" src="${createLinkTo(dir:'',file:landingfield_imagename2)}" />
                </g:each>
            </g:if>
        </g:form>
    </body>
</html>