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
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}*:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.titleprintprefix')}:</label>
                                <br/>
                                <input type="text" id="printPrefix" name="printPrefix" value="${fieldValue(bean:contestInstance,field:'printPrefix')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.organizer')}*:</label>
                                <br/>
                                <input type="text" id="printOrganizer" name="printOrganizer" value="${fieldValue(bean:contestInstance,field:'printOrganizer')}" tabIndex="${ti[0]++}"/>
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
                                <a href="/fc/docs/help_${session.showLanguage}.html#supported-rules" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
                                <g:select from="${ContestRules.GetContestRules(true)}" optionValue="${{it.ruleValues.ruleTitle}}" name="contestRule" tabIndex="${ti[0]++}"/>
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
                                <label>${message(code:'fc.contest.contestdate')}:*:</label>
                                <br/>
                                <input type="date" id="liveTrackingContestDate" name="liveTrackingContestDate" value="${fieldValue(bean:contestInstance,field:'liveTrackingContestDate')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.timezone')}*:</label>
                                <g:select from="${TimeZone.getAvailableIDs()}" optionValue="${{it}}" name="timeZone2" value="${TimeZone.getDefault().getID()}" tabIndex="${ti[0]++}" />
                            </p>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.bestofanalysistasknum')}*:</label>
                                <br/>
                                <input type="text" id="bestOfAnalysisTaskNum" name="bestOfAnalysisTaskNum" value="${fieldValue(bean:contestInstance,field:'bestOfAnalysisTaskNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>