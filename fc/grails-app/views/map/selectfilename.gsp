<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.map.import')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.map.import')}</h2>
                <div class="block" id="forms" >
                	<g:uploadForm action="importfilename">
                        <g:set var="ti" value="${[]+1}"/>
                    	<div>
    						<input type="file" size="80" accept=".zip" name="zipfile" tabIndex="${ti[0]++}"/>
                    	</div>
                        <br/>
                    	<div>
                            <input type="hidden" name="id" value="${params.id}" />
	    					<input type="submit" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
		                    <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    	</div>
					</g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>
