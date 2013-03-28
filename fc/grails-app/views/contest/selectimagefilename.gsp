<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.image.select')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.image.select')}</h2>
                <div class="block" id="forms" >
                	<g:uploadForm action="loadimage">
                    	<div>
    						<input type="file" size="80" accept="image/jpeg/*.jpg" name="imagefile" tabIndex="1"/>
                    	</div>
                    	<div>
                            <input type="hidden" name="imageField" value="${params.imageField}" />
	    					<input type="submit" value="${message(code:'fc.import')}" tabIndex="2"/>
		                    <g:actionSubmit action="edit" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                    	</div>
					</g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>