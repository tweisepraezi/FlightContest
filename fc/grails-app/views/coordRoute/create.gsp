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
                                <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}" tabIndex="1"/>
                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}" tabIndex="2"/>
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="latMinute" name="latMinute" value="${fieldValue(bean:coordRouteInstance,field:'latMinute')}" tabIndex="3"/>
                                <label>${message(code:'fc.min')}</label>
                            </div>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.longitude')}*</legend>
                            <div>
                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}" tabIndex="4"/>
                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}" tabIndex="5"/>
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="lonMinute" name="lonMinute" value="${fieldValue(bean:coordRouteInstance,field:'lonMinute')}" tabIndex="6"/>
                                <label>${message(code:'fc.min')}</label>
                            </div>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
                                <br/>
                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:coordRouteInstance,field:'altitude')}" tabIndex="7"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="gatewidth2" name="gatewidth2" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth2')}" tabIndex="8"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.legduration')} [${message(code:'fc.time.min')}]:</label>
                                <br/>
                                <input type="text" id="legDuration" name="legDuration" value="${fieldValue(bean:coordRouteInstance,field:'legDuration')}" tabIndex="9"/>
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
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="10"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="11"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>