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
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.typename')}:</label>
                                <br/>
                                <g:select from="${coordRouteInstance.type.ListNextValues(existiFP)}" name="type" value="${coordRouteInstance.type}" optionValue="${{message(code:it.code)}}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <g:editCoordRoute coordRoute="${coordRouteInstance}" ti="${ti}"/>
                        <g:if test="${!params.secret && coordRouteInstance.type.IsTurnpointSignNextCoord(existiFP)}">
	                        <g:if test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignPhoto}">
	                            <fieldset>
		                            <p>
		                                <label>${message(code:'fc.observation.turnpoint.assignedphoto')}*:</label>
		                                <br/>
		                                <g:select from="${TurnpointSign.GetTurnpointSigns(false)}" optionValue="${it}" name="assignedSign" tabIndex="${ti[0]++}"/>
		                            </p>
	                            </fieldset>
	                        </g:if>
	                        <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.AssignCanvas}">
	                            <fieldset>
	                                <p>
	                                    <label>${message(code:'fc.observation.turnpoint.assignedcanvas')}*:</label>
	                                    <br/>
	                                    <g:select from="${TurnpointSign.GetTurnpointSigns(true)}" optionValue="${it}" name="assignedSign" tabIndex="${ti[0]++}"/>
	                                </p>
	                            </fieldset>
	                        </g:elseif>
	                        <g:elseif test="${coordRouteInstance.route.turnpointRoute == TurnpointRoute.TrueFalsePhoto}">
	                            <fieldset>
	                             <div>
	                                 <label>${message(code:'fc.observation.turnpoint')}*:</label>
	                                 <br/>
	                                 <g:each var="v" in="${TurnpointCorrect.values()}">
	                                     <g:if test="${v != TurnpointCorrect.Unassigned}">
	                                         <g:if test="${coordRouteInstance.correctSign == v}">
	                                             <label><input type="radio" name="correctSign" value="${v}" checked="checked" tabIndex="${ti[0]++}"/>${message(code:v.code)}</label>
	                                         </g:if>
	                                         <g:else>
	                                             <label><input type="radio" name="correctSign" value="${v}" tabIndex="${ti[0]++}"/>${message(code:v.code)}</label>
	                                         </g:else>
	                                     </g:if>
	                                 </g:each>
	                                 <br/>
	                             </div>
	                            </fieldset>
	                        </g:elseif>
	                    </g:if>
                        <input type="hidden" name="titleNumber" value="${coordRouteInstance.titleNumber}" />
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>