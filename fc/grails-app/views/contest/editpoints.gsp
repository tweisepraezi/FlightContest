<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contestrule.points.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contestrule.points.edit')}</h2>
                <g:hasErrors bean="${contestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['contestReturnAction':contestReturnAction,'contestReturnController':contestReturnController,'contestReturnID':contestReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <g:set var="ret" value="${[modifynum:0]}"/>
                        <g:editPoints i="${contestInstance}" contest="${contestInstance}" recalculatepoints="${message(code:'fc.contest.recalculatepoints')}" ret="${ret}" ti="${ti}"/>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="savepoints" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:if test="${ret.modifynum > 0}">
                            <g:actionSubmit action="standardpoints" value="${message(code:'fc.standard')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        </g:if>
                        <g:actionSubmit action="printpoints" value="${message(code:'fc.print')}"  tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="recalculatepenalties" value="${message(code:'fc.recalculatepenalties')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>