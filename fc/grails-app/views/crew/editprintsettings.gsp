<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.editprintsettings')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.editprintsettings')}</h2>
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
                                    <g:checkBox name="printCrewLandscape" value="${contestInstance.printCrewLandscape}" />
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printCrewA3" value="${contestInstance.printCrewA3}" />
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updateprintsettingsstandard" value="${message(code:'fc.setprintsettings.standard')}" tabIndex="5"/>
                            <g:actionSubmit action="updateprintsettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="6"/>
                            <g:actionSubmit action="updateprintsettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="7"/>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}" />
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="updateprintsettings" value="${message(code:'fc.update')}" tabIndex="8"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="9"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>