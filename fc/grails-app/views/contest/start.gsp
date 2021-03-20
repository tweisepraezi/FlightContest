<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest')}</title>
    </head>
    <body>
        <g:if test="${contestInstance}">
            <g:mainnav link="${createLink(controller:'contest')}" controller="contest" edit="${message(code:'fc.contest.settings')}" id="${contestInstance.id}" conteststart="true" />
        </g:if> <g:else>
            <g:mainnav link="${createLink(controller:'contest')}" controller="global" />
        </g:else>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:if test="${contestInstance}">
                <table>
                    <thead>
                        <tr>
                            <th class="table-head">${message(code:'fc.contest.show')}</th>
                            <th class="table-head"><a href="../docs/help.html#default-competition" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.title')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'title')}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.titleprintprefix')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'printPrefix')}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.organizer')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'printOrganizer')}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.contest.withclasses')}:</td>
                            <td><g:if test="${contestInstance.resultClasses}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.no')}</g:else></td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.contestrule.foreachclass')}:</td>
                            <td><g:if test="${contestInstance.contestRuleForEachClass}">${message(code:'fc.yes')}</g:if><g:else>${message(code:'fc.no')}</g:else></td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.contestrule')}:</td>
                            <td>${contestInstance.ruleTitle}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.coordpresentation')}:</td>
                            <td>${message(code:contestInstance.coordPresentation.code)}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.contest.contestdate')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'liveTrackingContestDate')}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.timezone')}:</td>
                            <td>${contestInstance.timeZone2?.getID()}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.timezone.diff')}:</td>
                            <g:if test="${contestInstance.liveTrackingContestDate && contestInstance.timeZone2?.inDaylightTime(Date.parse("yyyy-MM-dd",contestInstance.liveTrackingContestDate))}">
                                <td>${fieldValue(bean:contestInstance, field:'timeZone')}${message(code:'fc.time.h')} (${message(code:'fc.timezone.daylighttime')})</td>
                            </g:if>
                            <g:else>
                                <td>${fieldValue(bean:contestInstance, field:'timeZone')}${message(code:'fc.time.h')}</td>
                            </g:else>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.teamcrewnum')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'teamCrewNum')}</td>
                        </tr>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.bestofanalysistasknum')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'bestOfAnalysisTaskNum')}</td>
                        </tr>
                        <g:if test="${contestInstance.liveTrackingContestID}">
	                        <tr>
	                            <td class="detailtitle">${message(code:'fc.livetracking')} ${message(code:'fc.livetracking.contestdate')} (${message(code:'fc.livetracking.contestid')}):</td>
	                            <td>${fieldValue(bean:contestInstance, field:'liveTrackingContestDate')} (${fieldValue(bean:contestInstance, field:'liveTrackingContestID')})</td>
	                        </tr>
                        </g:if>
                        <tr>
                            <td class="detailtitle">${message(code:'fc.contest.uuid')}:</td>
                            <td>${fieldValue(bean:contestInstance, field:'contestUUID')}</td>
                        </tr>
                    </tbody>
                </table>
            </g:if>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>