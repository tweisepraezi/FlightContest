<html>
    <head>
        <title><g:layoutTitle default="${message(code:'fc.program.title')}" /> - ${message(code:'fc.program.title')}</title>

        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'fc.ico')}" type="image/x-icon" />

        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'reset.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'text.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'grid.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'layout.css')}" media="screen" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'nav.css')}" media="screen" />
        
        <!--[if IE 6]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie6.css')}" media="screen" /><![endif]-->
        <!--[if IE 7]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie.css')}" media="screen" /><![endif]-->
        
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fc.css')}" media="screen" />
        
        <g:layoutHead />

        <g:javascript library="application" />
    </head>
    <body>
        <div class="container_12">
            <div class="grid"  >
                <g:if test="${params.print}">
                    <g:set var="height_value_diff_left" value="${new Integer(0)}"/>
                    <g:set var="height_value_diff_center" value="${new Integer(0)}"/>
                    <g:set var="height_value_diff_right" value="${new Integer(0)}"/>
                    <g:if test="${session?.lastContest?.imageLeft}">
                        <g:if test="${session.lastContest.imageLeftHeight > 50}">
                            <g:set var="height_value_diff_left" value="${session.lastContest.imageLeftHeight-50}"/>
                        </g:if>    
                    </g:if>
                    <g:if test="${session?.lastContest?.imageCenter}">
                        <g:if test="${session.lastContest.imageCenterHeight > 50}">
                            <g:set var="height_value_diff_center" value="${session.lastContest.imageCenterHeight-50}"/>
                        </g:if>    
                    </g:if>
                    <g:if test="${session?.lastContest?.imageRight}">
                        <g:if test="${session.lastContest.imageRightHeight > 50}">
                            <g:set var="height_value_diff_right" value="${session.lastContest.imageRightHeight-50}"/>
                        </g:if>    
                    </g:if>

                    <g:set var="height_value_diff" value="${new Integer(0)}"/>
                    <g:if test="${height_value_diff_left > height_value_diff}">
                        <g:set var="height_value_diff" value="${height_value_diff_left}"/>
                    </g:if>
                    <g:if test="${height_value_diff_right > height_value_diff}">
                        <g:set var="height_value_diff" value="${height_value_diff_right}"/>
                    </g:if>
                        
                    <g:if test="${session?.lastContest?.imageCenter}">
                        <g:if test="${height_value_diff_center > height_value_diff}">
                            <g:set var="set_style" value=""/>
                        </g:if>
                        <g:else>
                            <g:set var="set_style" value="padding-bottom:${height_value_diff-height_value_diff_center}px;"/>
                        </g:else>
                    </g:if>
                    <g:elseif test="${session?.lastContest?.imageLeft || session?.lastContest?.imageRight}">
                        <g:if test="${height_value_diff_left > height_value_diff_right}">
                            <g:set var="height_value" value="${height_value_diff_left + 20}"/>
                        </g:if>
                        <g:else>
                            <g:set var="height_value" value="${height_value_diff_right + 20}"/>
                        </g:else>
                        <g:set var="set_style" value="padding-bottom:${height_value}px;"/>
                    </g:elseif>
                    <g:else>
	                   <g:set var="set_style" value=""/>
	                </g:else>
	                
	                <g:if test="${session?.lastContest?.imageLeft && session?.lastContest?.imageRight}">
	                    <g:set var="set_style" value="${set_style}text-align:center;"/>
	                </g:if>
	                
                    <g:if test="${session?.lastContest?.titleSize}">
                        <g:set var="set_style" value="${set_style}font-size:${session.lastContest.titleSize};"/>
                    </g:if>
                    
	                <h1 id="branding" style="${set_style}" >
	                    <g:if test="${session?.lastContest?.imageLeft}">
	                        <img src="${createLink(controller:'contest',action:'view_image_left',params:[contestid:session.lastContest.id])}" align="left" height="${session.lastContest.imageLeftHeight}" style="vertical-align:top;padding-right:10px;"/>
	                    </g:if>
	                    <g:if test="${session?.lastContest?.imageCenter}">
	                        <img src="${createLink(controller:'contest',action:'view_image_center',params:[contestid:session.lastContest.id])}" align="center" height="${session.lastContest.imageCenterHeight}" style="vertical-align:top;" />
	                    </g:if>
	                    <g:elseif test="${session?.contestTitle}">
	                        ${session.contestTitle}
	                    </g:elseif>
	                    <g:elseif test="${session?.lastContest}">
	                        ${session.lastContest.name()}
	                    </g:elseif>
	                    <g:else>
	                        ${message(code:'fc.program.title')}
	                    </g:else>
	                    <g:if test="${session?.lastContest?.imageRight}">
	                        <img src="${createLink(controller:'contest',action:'view_image_right',params:[contestid:session.lastContest.id])}" align="right" height="${session.lastContest.imageRightHeight}" style="vertical-align:top;;padding-left:10px;"/>
	                    </g:if>
                    </h1>
				</g:if>
				<g:else>
	                <h1 id="branding">
	                    <g:if test="${session?.contestTitle}">
	                    	${session.contestTitle}
	                    </g:if>
	                    <g:elseif test="${session?.lastContest}">
	                        ${session.lastContest.name()}
	                    </g:elseif>
	                    <g:else>
	                        ${message(code:'fc.program.title')}
	                    </g:else>
	                </h1>
                </g:else>
            </div>
            <g:layoutBody />
        </div>
    </body>
</html>