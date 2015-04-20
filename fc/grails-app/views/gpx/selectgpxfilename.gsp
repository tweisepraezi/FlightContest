<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.gpx.selectgpxfilename')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="gac" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.gpx.selectgpxfilename')}</h2>
                <div class="block" id="forms">
                	<g:uploadForm action="showmapgpx">
                    	<div>
    						<input type="file" size="80" accept="text/plain/*.gpx" name="loadgpxfile" tabIndex="1"/>
                    	</div>
                        <g:if test="${contestInstance && Route.findByContest(contestInstance)}">
                            <p>
                                <div>
                                    <label>${message(code:'fc.route')}: </label>
                                    <g:select optionKey="id" optionValue="${{it.name()}}" from="${Route.findAllByContest(contestInstance,[sort:"id"])}" name="routeid" value="${Route.findByContest(contestInstance,[sort:"id"]).id}" noSelection="${[id:'']}" ></g:select>
                                </div>
                            </p>
                        </g:if>
                    	<div>
	    					<input type="submit" value="${message(code:'fc.gpx.showmap')}" tabIndex="11"/>
		                    <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="12"/>
                    	</div>
					</g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>