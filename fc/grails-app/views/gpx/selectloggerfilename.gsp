<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.loggerdata.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="gac" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.loggerdata.show')}</h2>
                <div class="block" id="forms">
                	<g:uploadForm action="showmaploggerdata">
                        <g:set var="ti" value="${[]+1}"/>
                    	<div>
                            <p>${message(code:'fc.loggerdata.selectloggerfilename.info',args:[LoggerFileTools.LOGGER_EXTENSIONS])}</p>
    						<input type="file" size="80" accept="${LoggerFileTools.LOGGER_EXTENSIONS}" name="loadloggerfile" tabIndex="${ti[0]++}"/>
                    	</div>
                    	<br/>
                        <g:if test="${contestInstance && Route.findByContest(contestInstance)}">
                            <p>
                                <div>
                                    <label>${message(code:'fc.route')}: </label>
                                    <g:select optionKey="id" optionValue="${{it.name()}}" from="${Route.findAllByContest(contestInstance,[sort:"idTitle"])}" name="routeid" value="" noSelection="${[id:'']}" ></g:select>
                                </div>
                            </p>
                            <br/>
                        </g:if>
                    	<div>
                            <input type="submit" name="offlinemap" value="${message(code:'fc.offlinemap')}" tabIndex="${ti[0]++}"/>
	    					<input type="submit" name="onlinemap" value="${message(code:'fc.onlinemap')}" tabIndex="${ti[0]++}"/>
	    					<input type="submit" name="gpxdownload" value="${message(code:'fc.gpxdownload')}" tabIndex="${ti[0]++}"/>
		                    <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    	</div>
					</g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>