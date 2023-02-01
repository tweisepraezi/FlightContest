<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.task.landingstartlist')} - ${taskInstance.name()}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.task.landingstartlist')} - ${taskInstance.name()}</h2>
                <div class="block" id="forms">
                    <g:form params="${['taskReturnAction':taskReturnAction,'taskReturnController':taskReturnController,'taskReturnID':taskReturnID]}">
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.printsubtitle')}:</label>
                                <br/>
                                <input type="text" id="printLandingStartlistPrintTitle" name="printLandingStartlistPrintTitle" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistPrintTitle')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printLandingStartlistNumber" value="${taskInstance.printLandingStartlistNumber}" onclick="modify();"/>
                                    <label>${message(code:'fc.number')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistCrew" value="${taskInstance.printLandingStartlistCrew}" onclick="modify();"/>
                                    <label>${message(code:'fc.crew')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistAircraft" value="${taskInstance.printLandingStartlistAircraft}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistAircraftType" value="${taskInstance.printLandingStartlistAircraftType}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft.type')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistAircraftColour" value="${taskInstance.printLandingStartlistAircraftColour}" onclick="modify();"/>
                                    <label>${message(code:'fc.aircraft.colour')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistTAS" value="${taskInstance.printLandingStartlistTAS}" onclick="modify();"/>
                                    <label>${message(code:'fc.tas')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistTeam" value="${taskInstance.printLandingStartlistTeam}" onclick="modify();"/>
                                    <label>${message(code:'fc.team')}</label>
                                </div>
                                <g:if test="${taskInstance.contest.resultClasses}">
	                                <div>
	                                    <g:checkBox name="printLandingStartlistClass" value="${taskInstance.printLandingStartlistClass}" onclick="modify();"/>
	                                    <label>${message(code:'fc.resultclass')}</label>
	                                </div>
	                                <div>
	                                    <g:checkBox name="printLandingStartlistShortClass" value="${taskInstance.printLandingStartlistShortClass}" onclick="modify();"/>
	                                    <label>${message(code:'fc.resultclass.short')}</label>
	                                </div>
	                            </g:if>
                                <div>
                                    <g:checkBox name="printLandingStartlistEmptyColumn1" value="${taskInstance.printLandingStartlistEmptyColumn1}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 1</label>
                                </div>
                                <p>
                                    <input type="text" id="printLandingStartlistEmptyTitle1" name="printLandingStartlistEmptyTitle1" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistEmptyTitle1')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printLandingStartlistEmptyColumn2" value="${taskInstance.printLandingStartlistEmptyColumn2}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 2</label>
                                </div>
                                <p>
                                    <input type="text" id="printLandingStartlistEmptyTitle2" name="printLandingStartlistEmptyTitle2" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistEmptyTitle2')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printLandingStartlistEmptyColumn3" value="${taskInstance.printLandingStartlistEmptyColumn3}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 3</label>
                                </div>
                                <p>
                                    <input type="text" id="printLandingStartlistEmptyTitle3" name="printLandingStartlistEmptyTitle3" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistEmptyTitle3')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
                            <p>
                                <div>
                                    <g:checkBox name="printLandingStartlistEmptyColumn4" value="${taskInstance.printLandingStartlistEmptyColumn4}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.emptycolumn')} 4</label>
                                </div>
                                <p>
                                    <input type="text" id="printLandingStartlistEmptyTitle4" name="printLandingStartlistEmptyTitle4" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistEmptyTitle4')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
                                </p>
                            </p>
							<p>
								<label>${message(code:'fc.task.landingstartlist.groupcrewnum')}:</label>
								<br/>
								<input type="number" id="printLandingStartlistGroupCrewNum" name="printLandingStartlistGroupCrewNum" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistGroupCrewNum')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
							</p>
							<p>
								<label>${message(code:'fc.task.landingstartlist.startgroupcrews')}:</label>
								<br/>
								<input type="text" id="printLandingStartlistStartGroupCrews" name="printLandingStartlistStartGroupCrews" value="${fieldValue(bean:taskInstance,field:'printLandingStartlistStartGroupCrews')}" tabIndex="${ti[0]++}" onkeydown="modify();"/>
							</p>
                            <p>
                                <div>
                                    <g:checkBox name="printLandingStartlistLandingField" value="${taskInstance.printLandingStartlistLandingField}" onclick="modify();"/>
                                    <label>${message(code:'fc.test.landing.field')}</label>
                                </div>
                            </p>
                            <g:actionSubmit action="updatelandingstartlistsettingsstandard" value="${message(code:'fc.standard')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatelandingstartlistsettingsnone" value="${message(code:'fc.setprintsettings.none')}" tabIndex="${ti[0]++}"/>
                            <g:actionSubmit action="updatelandingstartlistsettingsall" value="${message(code:'fc.setprintsettings.all')}" tabIndex="${ti[0]++}"/>
                            <p>
                                <br/>
                                <div>
                                    <g:checkBox name="printLandingStartlistLandscape" value="${taskInstance.printLandingStartlistLandscape}" onclick="modify();"/>
                                    <label>${message(code:'fc.printlandscape')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="printLandingStartlistA3" value="${taskInstance.printLandingStartlistA3}" onclick="modify();"/>
                                    <label>${message(code:'fc.printa3')}</label>
                                </div>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${taskInstance?.id}" />
                        <input type="hidden" name="version" value="${taskInstance?.version}"/>
                        <g:actionSubmit action="savelandingstartlistsettings" id="savelandingstartlistsettings_id" value="${message(code:'fc.save')}" disabled tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="printlandingstartlist" id="printlandingstartlist_id" value="${message(code:'fc.print')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}"  tabIndex="${ti[0]++}"/>
                        <script>
                            function modify() {
                                $("#savelandingstartlistsettings_id").prop("disabled", false);
                                $("#printlandingstartlist_id").prop("disabled", true);
                                $("#removechangelandingstartlistsettings_id").prop("disabled", true);
                                $("#addchangelandingstartlistsettings_id").prop("disabled", true);
                            }
                        </script>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>