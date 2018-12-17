<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.create')}</h2>
                <g:hasErrors bean="${contestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}*:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}" tabIndex="1"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.titleprintprefix')}:</label>
                                <br/>
                                <input type="text" id="printPrefix" name="printPrefix" value="${fieldValue(bean:contestInstance,field:'printPrefix')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.organizer')}*:</label>
                                <br/>
                                <input type="text" id="printOrganizer" name="printOrganizer" value="${fieldValue(bean:contestInstance,field:'printOrganizer')}" tabIndex="3"/>
                            </p>
                            <p>
                                <div>
	                               	<g:checkBox name="resultClasses" value="${contestInstance.resultClasses}" />
    	                            <label>${message(code:'fc.contest.withclasses')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestRuleForEachClass" value="${contestInstance.contestRuleForEachClass}" />
                                    <label>${message(code:'fc.contestrule.foreachclass')}</label>
                                </div>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestrule')}*:</label>
                                <a href="../docs/help.html#supported-rules" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
                                <g:select from="${ContestRules.GetContestRules()}" optionValue="${{it.ruleValues.ruleTitle}}" name="contestRule" tabIndex="4"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.scale')}*:</label>
                                <br/>
                                <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:contestInstance,field:'mapScale')}" tabIndex="5"/>
                            </p>
                            <label>${message(code:'fc.coordpresentation')}:</label>
                            <g:set var="format_labels" value="${[]}"/>
                            <g:each var="format_code" in="${CoordPresentation.GetCodes()}">
                                <g:set var="format_labels" value="${format_labels+message(code:format_code)}"/>
                            </g:each>
                            <g:set var="format_values" value="${CoordPresentation.GetValues()}"/>
                            <g:radioGroup name="coordPresentation" labels="${format_labels}" values="${format_values}" value="${contestInstance.coordPresentation}">
                                <div>
                                    <label>${it.radio} ${it.label}</label>
                                </div>
                            </g:radioGroup>
                            <br/>
                            <p>
                                <label>${message(code:'fc.timezone')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="timeZone" name="timeZone" value="${fieldValue(bean:contestInstance,field:'timeZone')}" tabIndex="6"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="7"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.bestofanalysistasknum')}*:</label>
                                <br/>
                                <input type="text" id="bestOfAnalysisTaskNum" name="bestOfAnalysisTaskNum" value="${fieldValue(bean:contestInstance,field:'bestOfAnalysisTaskNum')}" tabIndex="8"/>
                            </p>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="101"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="102"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>