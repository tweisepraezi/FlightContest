<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${resultclassInstance.GetListTitle('fc.contestrule.classpoints.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="resultClass"/>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${resultclassInstance.GetListTitle('fc.contestrule.classpoints.edit')}</h2>
                <g:hasErrors bean="${resultclassInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${resultclassInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['editpointsReturnAction':editpointsReturnAction,'editpointsReturnController':editpointsReturnController,'editpointsReturnID':editpointsReturnID]}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <g:set var="ret" value="${[modifynum:0]}"/>
                        <g:editPoints i="${resultclassInstance}" contest="${resultclassInstance.contest}" recalculatepoints="${message(code:'fc.resultclass.recalculatepoints')}" ret="${ret}" ti="${ti}"/>
                        <input type="hidden" name="id" value="${resultclassInstance?.id}"/>
                        <input type="hidden" name="version" value="${resultclassInstance?.version}"/>
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