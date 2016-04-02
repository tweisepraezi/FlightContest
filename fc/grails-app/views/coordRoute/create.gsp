<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.create')}</h2>
                <g:hasErrors bean="${coordRouteInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordRouteInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="fromrouteparam" value="['routeid':params.routeid]"/>
                    <g:form method="post" params="${fromrouteparam}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.typename')}:</label>
                                <br/>
                                <g:select from="${coordRouteInstance.type.ListNextValues(existiFP)}" name="type" value="${coordRouteInstance.type}" optionValue="title" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.latitude')}*</legend>
                            <div>
	                            <g:if test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREE}">
	                                <input type="text" id="latGradDecimal" name="latGradDecimal" value="${coordRouteInstance.latGradDecimal.toFloat()}"  tabIndex="1"/>
	                                <label>${message(code:'fc.grad')}</label>
	                            </g:if>
	                            <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTE}">
	                                <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}"  tabIndex="2"/>
	                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}"  tabIndex="3"/>
	                                <label>${message(code:'fc.grad')}</label>
	                                <input type="text" id="latMinute" name="latMinute" value="${coordRouteInstance.latMinute.toFloat()}" tabIndex="4"/>
	                                <label>${message(code:'fc.min')}</label>
	                            </g:elseif>
	                            <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTESECOND}">
	                                <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}"  tabIndex="5"/>
	                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}"  tabIndex="6"/>
	                                <label>${message(code:'fc.grad')}</label>
	                                <input class="minute" type="text" id="latMin" name="latMin" value="${fieldValue(bean:coordRouteInstance,field:'latMin')}" tabIndex="7"/>
	                                <label>${message(code:'fc.min')}</label>
	                                <input type="text" id="latSecondDecimal" name="latSecondDecimal" value="${coordRouteInstance.latSecondDecimal.toFloat()}" tabIndex="8"/>
	                                <label>${message(code:'fc.sec')}</label>
	                            </g:elseif>
                            </div>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.longitude')}*</legend>
                            <div>
	                            <g:if test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREE}">
	                                <input type="text" id="lonGradDecimal" name="lonGradDecimal" value="${coordRouteInstance.lonGradDecimal.toFloat()}"  tabIndex="11"/>
	                                <label>${message(code:'fc.grad')}</label>
	                            </g:if>
	                            <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTE}">
	                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}"  tabIndex="12"/>
	                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}"  tabIndex="13"/>
	                                <label>${message(code:'fc.grad')}</label>
	                                <input type="text" id="lonMinute" name="lonMinute" value="${coordRouteInstance.lonMinute.toFloat()}" tabIndex="14"/>
	                                <label>${message(code:'fc.min')}</label>
	                            </g:elseif>
	                            <g:elseif test="${coordRouteInstance.route.contest.coordPresentation == CoordPresentation.DEGREEMINUTESECOND}">
	                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}"  tabIndex="15"/>
	                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}"  tabIndex="16"/>
	                                <label>${message(code:'fc.grad')}</label>
	                                <input class="minute" type="text" id="lonMin" name="lonMin" value="${fieldValue(bean:coordRouteInstance,field:'lonMin')}" tabIndex="17"/>
	                                <label>${message(code:'fc.min')}</label>
	                                <input type="text" id="lonSecondDecimal" name="lonSecondDecimal" value="${coordRouteInstance.lonSecondDecimal.toFloat()}" tabIndex="18"/>
	                                <label>${message(code:'fc.sec')}</label>
	                            </g:elseif>
                            </div>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
                                <br/>
                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:coordRouteInstance,field:'altitude')}" tabIndex="21"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="gatewidth2" name="gatewidth2" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth2')}" tabIndex="22"/>
                            </p>
                            <g:if test="${coordRouteInstance.type.IsRunwayCoord()}">
                                <p>
                                    <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
                                    <br/>
                                    <input type="text" id="gateDirection" name="gateDirection" value="${fieldValue(bean:coordRouteInstance,field:'gateDirection')}" tabIndex="23"/>
                                </p>
                            </g:if>
                            <p>
                                <label>${message(code:'fc.legduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="legDuration" name="legDuration" value="${fieldValue(bean:coordRouteInstance,field:'legDuration')}" tabIndex="24"/>
                            </p>
                            <div>
                                <g:checkBox name="noTimeCheck" value="${coordRouteInstance.noTimeCheck}" />
                                <label>${message(code:'fc.notimecheck')}</label>
                            </div>
                            <div>
                                <g:checkBox name="noGateCheck" value="${coordRouteInstance.noGateCheck}" />
                                <label>${message(code:'fc.nogatecheck')}</label>
                            </div>
                            <div>
                                <g:checkBox name="noPlanningTest" value="${coordRouteInstance.noPlanningTest}" />
                                <label>${message(code:'fc.noplanningtest')}</label>
                            </div>
                        </fieldset>
                        <input type="hidden" name="titleNumber" value="${coordRouteInstance.titleNumber}" />
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="101"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="102"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>