<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.coordroute.mapobjects.edit',args:[coordMapObjectInstance.enrouteViewPos])}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.coordroute.mapobjects.edit',args:[coordMapObjectInstance.enrouteViewPos])}</h2>
                <g:hasErrors bean="${coordMapObjectInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${coordMapObjectInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:set var="fromrouteparam" value="['routeid':params.routeid,'nextid':next_id]"/>
                    <g:form method="post" params="${fromrouteparam}" >
                        <g:set var="ti" value="${[]+1}"/>
						<g:set var="next_id" value="${coordMapObjectInstance.GetNextCoordMapObjectID()}"/>
						<g:set var="prev_id" value="${coordMapObjectInstance.GetPrevCoordMapObjectID()}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.coordroute.mapobjects.from')}:</td>
                                    <td><g:route var="${coordMapObjectInstance?.route}" link="${createLink(controller:'route',action:'show')}"/></td>
                                </tr>
                            </tbody>
                        </table>
                        <g:editCoordMapObject coordEnroute="${coordMapObjectInstance}" create="${false}" ti="${ti}"/>
                        <input type="hidden" name="id" value="${coordMapObjectInstance?.id}" />
                        <input type="hidden" name="version" value="${coordMapObjectInstance?.version}" />
						<g:if test="${next_id}">
							<g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="gotonext" value="${message(code:'fc.gotonext')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
						<g:if test="${prev_id}">
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.gotoprev')}" tabIndex="${ti[0]++}"/>
						</g:if>
						<g:else>
							<g:actionSubmit action="gotoprev" value="${message(code:'fc.gotoprev')}" disabled tabIndex="${ti[0]++}"/>
						</g:else>
                        <g:if test="${next_id}">
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:else>
                            <g:actionSubmit action="updatenext" value="${message(code:'fc.savenext')}" disabled tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="updatereturn" value="${message(code:'fc.saveend')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>