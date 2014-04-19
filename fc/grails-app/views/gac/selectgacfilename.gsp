<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.gac.selectgacfilename')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="gac" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.gac.selectgacfilename')}</h2>
                <div class="block" id="forms" >
                	<g:uploadForm action="loadgac">
                    	<div>
    						<input type="file" size="80" accept="text/plain/*.gac" name="loadgacfile" tabIndex="1"/>
                    	</div>
                    	<div>
	    					<input type="submit" value="${message(code:'fc.gac.repair.track')}" tabIndex="2"/>
		                    <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                    	</div>
					</g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>