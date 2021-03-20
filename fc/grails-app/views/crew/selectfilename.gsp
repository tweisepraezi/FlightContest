<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.crew.import.selectfilename')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="crew" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.crew.import.selectfilename')}</h2>
                <div class="block" id="forms" >
                	<g:uploadForm action="importcrews">
                        <g:set var="ti" value="${[]+1}"/>
                    	<div>
    						<input type="file" size="80" accept=".xls,.xlsx" name="loadfile" tabIndex="${ti[0]++}"/>
                    	</div>
                    	<g:if test="${contestInstance.unsuitableStartNum}">
	                        <p>
	                            <div>
	                                <g:checkBox name="noUnsuitableStartNum" value="${noUnsuitableStartNum}" />
	                                <label>${message(code:'fc.crew.import.nostartnum',args:[contestInstance.unsuitableStartNum])}</label>
	                            </div>
	                        </p>
	                    </g:if>
                    	<div>
	    					<input type="submit" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
		                    <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    	</div>
					</g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>