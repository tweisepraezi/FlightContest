<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.print')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.print')}</h2>
                <div class="block" id="forms">
                    <g:form params="${['crewReturnAction':crewReturnAction,'crewReturnController':crewReturnController,'crewReturnID':crewReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
	                            <label>${message(code:'fc.printsubtitle')}:</label>
	                            <br/>
	                            <input type="text" id="printCrewPrintTitle" name="printCrewPrintTitle" value="${fieldValue(bean:contestInstance,field:'printCrewPrintTitle')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
	                        </p>
	                        <p>
                                <div>
                                    <g:checkBox name="printCrewNumber" value="${contestInstance.printCrewNumber}" onclick="modify();"/>
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewName" value="${contestInstance.printCrewName}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew.name')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewEmail" value="${contestInstance.printCrewEmail}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew.email')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewTeam" value="${contestInstance.printCrewTeam}" onclick="modify();"/>
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${contestInstance.resultClasses}">
	                                <div>
	                                    <g:checkBox name="printCrewClass" value="${contestInstance.printCrewClass}" onclick="modify();"/>
	                                    <label>${message(code:'fc.resultclass')}</label>
	                                </div>
                                    <div>
                                        <g:checkBox name="printCrewShortClass" value="${contestInstance.printCrewShortClass}" onclick="modify();"/>
                                        <label>${message(code:'fc.resultclass.short')}</label>
                                    </div>
	                            </g:if>
                                <div>
                                    <g:checkBox name="printCrewAircraft" value="${contestInstance.printCrewAircraft}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewAircraftType" value="${contestInstance.printCrewAircraftType}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft.type')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewAircraftColour" value="${contestInstance.printCrewAircraftColour}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft.colour')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewTAS" value="${contestInstance.printCrewTAS}" onclick="modify();"/>
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewTrackerID" value="${contestInstance.printCrewTrackerID}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew.trackerid')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewUUID" value="${contestInstance.printCrewUUID}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew.uuid')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewSortHelp" value="${contestInstance.printCrewSortHelp}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew.sorthelp')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn1" value="${contestInstance.printCrewEmptyColumn1}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 1</label>
                                </div>
                                <p>
                                    <input type="text" id="printCrewEmptyTitle1" name="printCrewEmptyTitle1" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle1')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn2" value="${contestInstance.printCrewEmptyColumn2}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 2</label>
                                </div>
	                            <p>
	                                <input type="text" id="printCrewEmptyTitle2" name="printCrewEmptyTitle2" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle2')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
	                            </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn3" value="${contestInstance.printCrewEmptyColumn3}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 3</label>
                                </div>
	                            <p>
	                                <input type="text" id="printCrewEmptyTitle3" name="printCrewEmptyTitle3" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle3')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
	                            </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn4" value="${contestInstance.printCrewEmptyColumn4}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 4</label>
                                </div>
                                <p>
                                    <input type="text" id="printCrewEmptyTitle4" name="printCrewEmptyTitle4" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle4')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewLandscape" value="${contestInstance.printCrewLandscape}" onclick="modify();"/>
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewA3" value="${contestInstance.printCrewA3}" onclick="modify();"/>
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updateprintsettingsstandard" value="${message(code:'fc.standard')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updateprintsettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updateprintsettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updateprintsettingslanding" value="${message(code:'fc.landingtest')}" tabIndex="${ti[0]++}"/>
                        </fieldset>
                        <fieldset>
                            <label>${message(code:'fc.crew.printsettings.order')}:</label>
                            <br/>
                            <g:set var="order_labels" value="${[message(code:'fc.crew.printsettings.order.crewlist')]}"/>
                            <g:set var="order_values" value="${[0]}"/>
                            <g:set var="order_value" value="${0}"/>
                            <g:each var="task_instance" in="${Task.findAllByContest(contestInstance,[sort:"id"])}">
                                <g:set var="order_labels" value="${order_labels += message(code:'fc.crew.printsettings.order.task',args:[task_instance.bestOfName()])}"/>
                                <g:set var="order_values" value="${order_values += task_instance.id}"/>
                                <g:if test="${contestInstance.printCrewOrder == task_instance.id}">
                                    <g:set var="order_value" value="${contestInstance.printCrewOrder}"/>
                                </g:if>
                            </g:each>
                            <g:radioGroup name="printCrewOrder" labels="${order_labels}" values="${order_values}" value="${order_value}" onclick="modify();">
                                <div>
                                    <label>${it.radio} ${it.label}</label>
                                </div>
                            </g:radioGroup>
                        </fieldset>
                        <fieldset>
							<div>
								<g:checkBox name="printTeams" value="${contestInstance.printTeams}" onclick="modify();"/>
								<label>${message(code:'fc.team.print')}</label>
							</div>
							<div>
								<g:checkBox name="printTeamLandscape" value="${contestInstance.printTeamLandscape}" onclick="modify();"/>
								<label>${message(code:'fc.printlandscape')}</label>
							</div>
                        </fieldset>
                        <fieldset>
							<div>
								<g:checkBox name="printAircraft" value="${contestInstance.printAircraft}" onclick="modify();"/>
								<label>${message(code:'fc.aircraft.print')}</label>
							</div>
							<div>
								<g:checkBox name="printAircraftLandscape" value="${contestInstance.printAircraftLandscape}" onclick="modify();"/>
								<label>${message(code:'fc.printlandscape')}</label>
							</div>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}" />
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="saveprintsettings" id="saveprintsettings_id" value="${message(code:'fc.save')}" disabled tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="print" id="print_id" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                        <script>
                            function modify() {
                                $("#saveprintsettings_id").prop("disabled", false);
                                $("#print_id").prop("disabled", true);
                            }
                        </script>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>