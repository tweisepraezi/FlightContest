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
                        <fieldset>
                            <p>
	                            <label>${message(code:'fc.printsubtitle')}:</label>
	                            <br/>
	                            <input type="text" id="printCrewPrintTitle" name="printCrewPrintTitle" value="${fieldValue(bean:contestInstance,field:'printCrewPrintTitle')}" tabIndex="1"/>
	                        </p>
	                        <p>
                                <div>
                                    <g:checkBox name="printCrewNumber" value="${contestInstance.printCrewNumber}" />
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewName" value="${contestInstance.printCrewName}" />
                                    <label>${message(code:'fc.crew.name')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewTeam" value="${contestInstance.printCrewTeam}" />
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${contestInstance.resultClasses}">
	                                <div>
	                                    <g:checkBox name="printCrewClass" value="${contestInstance.printCrewClass}" />
	                                    <label>${message(code:'fc.resultclass')}</label>
	                                </div>
                                    <div>
                                        <g:checkBox name="printCrewShortClass" value="${contestInstance.printCrewShortClass}" />
                                        <label>${message(code:'fc.resultclass.short')}</label>
                                    </div>
	                            </g:if>
                                <div>
                                    <g:checkBox name="printCrewAircraft" value="${contestInstance.printCrewAircraft}" />
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewAircraftType" value="${contestInstance.printCrewAircraftType}" />
                                    <label>${message(code:'fc.aircraft.type')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewAircraftColour" value="${contestInstance.printCrewAircraftColour}" />
                                    <label>${message(code:'fc.aircraft.colour')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewTAS" value="${contestInstance.printCrewTAS}" />
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn1" value="${contestInstance.printCrewEmptyColumn1}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 1</label>
                                </div>
                                <p>
                                    <input type="text" id="printCrewEmptyTitle1" name="printCrewEmptyTitle1" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle1')}" tabIndex="2"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn2" value="${contestInstance.printCrewEmptyColumn2}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 2</label>
                                </div>
	                            <p>
	                                <input type="text" id="printCrewEmptyTitle2" name="printCrewEmptyTitle2" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle2')}" tabIndex="3"/>
	                            </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn3" value="${contestInstance.printCrewEmptyColumn3}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 3</label>
                                </div>
	                            <p>
	                                <input type="text" id="printCrewEmptyTitle3" name="printCrewEmptyTitle3" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle3')}" tabIndex="4"/>
	                            </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewEmptyColumn4" value="${contestInstance.printCrewEmptyColumn4}" />
                                    <label>${message(code:'fc.test.emptycolumn')} 4</label>
                                </div>
                                <p>
                                    <input type="text" id="printCrewEmptyTitle4" name="printCrewEmptyTitle4" value="${fieldValue(bean:contestInstance,field:'printCrewEmptyTitle4')}" tabIndex="5"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printCrewLandscape" value="${contestInstance.printCrewLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewA3" value="${contestInstance.printCrewA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updateprintsettingsstandard" value="${message(code:'fc.standard')}" tabIndex="6"/>
                            <g:actionSubmit action="updateprintsettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="7"/>
                            <g:actionSubmit action="updateprintsettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="8"/>
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
                            <g:radioGroup name="printCrewOrder" labels="${order_labels}" values="${order_values}" value="${order_value}">
                                <div>
                                    <label>${it.radio} ${it.label}</label>
                                </div>
                            </g:radioGroup>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}" />
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="saveprintsettings" value="${message(code:'fc.save')}" tabIndex="101"/>
                        <g:actionSubmit action="print" value="${message(code:'fc.print')}" tabIndex="102"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="103"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>