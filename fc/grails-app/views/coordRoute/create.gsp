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
                                <g:select from="${CoordType.listNextValues(coordRouteInstance.type)}" name="type" value="${coordRouteInstance.type}" optionValue="title" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.latitude')}*</legend>
                            <p>
                                <g:select class="direction" id="latDirection" name="latDirection" from="${coordRouteInstance.constraints.latDirection.inList}" value="${coordRouteInstance.latDirection}" />
                                <input class="grad" type="text" id="latGrad" name="latGrad" value="${fieldValue(bean:coordRouteInstance,field:'latGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="latMinute" name="latMinute" value="${fieldValue(bean:coordRouteInstance,field:'latMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.longitude')}*</legend>
                            <p>
                                <g:select class="direction" id="lonDirection" name="lonDirection" from="${coordRouteInstance.constraints.lonDirection.inList}" value="${coordRouteInstance.lonDirection}" />
                                <input class="grad" type="text" id="lonGrad" name="lonGrad" value="${fieldValue(bean:coordRouteInstance,field:'lonGrad')}" />
                                <label>${message(code:'fc.grad')}</label>
                                <input class="minute" type="text" id="lonMinute" name="lonMinute" value="${fieldValue(bean:coordRouteInstance,field:'lonMinute')}" />
                                <label>${message(code:'fc.min')}</label>
                            </p>
                        </fieldset>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.altitude')}* [${message(code:'fc.foot')}]:</label>
                                <br/>
                                <input type="text" id="altitude" name="altitude" value="${fieldValue(bean:coordRouteInstance,field:'altitude')}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.gatewidth')}* [${message(code:'fc.mile')}]:</label>
                                <br/>
                                <input type="text" id="gatewidth" name="gatewidth" value="${fieldValue(bean:coordRouteInstance,field:'gatewidth')}"/>
                            </p>
                        </fieldset>
                        <input type="hidden" name="titleNumber" value="${coordRouteInstance.titleNumber}" />
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" />
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>