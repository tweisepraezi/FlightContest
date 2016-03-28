<html>
    <head>
        <title><g:layoutTitle default="${message(code:'fc.program.title')}" /> - ${message(code:'fc.program.title')}</title>

        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'fc.ico')}" type="image/x-icon" />

        <g:if test="${!liveTest}">
	        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'reset.css')}" media="screen" />
	        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'text.css')}" media="screen" />
	        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'grid.css')}" media="screen" />
	        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'layout.css')}" media="screen" />
	        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'nav.css')}" media="screen" />
	        
	        <!--[if IE 6]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie6.css')}" media="screen" /><![endif]-->
	        <!--[if IE 7]><link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'ie.css')}" media="screen" /><![endif]-->
	        
	        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fc.css')}" media="screen" />
            <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fcprint.css')}" media="print" />
	        <g:if test="${grails.util.GrailsUtil.getEnvironment().equals(org.codehaus.groovy.grails.commons.GrailsApplication.ENV_DEVELOPMENT) || grails.util.GrailsUtil.getEnvironment().equals("lastdb")}">
	            <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css',file:'fcdev.css')}" media="screen" />
	        </g:if>
	    </g:if>
        
        <g:layoutHead />

        <g:javascript library="application" />
    </head>
    <body>
        <div class="container_12">
            <div class="grid"  >
                <g:if test="${params.print}">
                    <g:if test="${!params.disabletitle}">
	                    <g:set var="a3_portrait_factor" value="${session.lastContest.a3PortraitFactor}"/>
	                    <g:set var="a4_landscape_factor" value="${session.lastContest.a4LandscapeFactor}"/>
	                    <g:set var="a3_landscape_factor" value="${session.lastContest.a3LandscapeFactor}"/>
	                    
	                    <g:set var="height_value_diff_left" value="${new Integer(0)}"/>
	                    <g:set var="height_value_diff_center" value="${new Integer(0)}"/>
	                    <g:set var="height_value_diff_right" value="${new Integer(0)}"/>
	                    
	                    <g:if test="${session?.lastContest?.imageLeft}">
	                        <g:set var="height_value_left" value="${session.lastContest.imageLeftHeight}"/>
	                        <g:if test="${params.a3=='true'}">
							    <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_left" value="${(a3_landscape_factor*height_value_left).toInteger()}"/>
							    </g:if>
							    <g:else>
	                                <g:set var="height_value_left" value="${(a3_portrait_factor*height_value_left).toInteger()}"/>
	                            </g:else>
	                        </g:if>   
	                        <g:else>
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_left" value="${(a4_landscape_factor*height_value_left).toInteger()}"/>
	                            </g:if>    
	                        </g:else>
	                        <g:if test="${height_value_left > 50}">
	                            <g:set var="height_value_diff_left" value="${height_value_left-50}"/>
	                        </g:if>    
	                    </g:if>
	                    
	                    <g:if test="${session?.lastContest?.imageCenter}">
	                        <g:set var="height_value_center" value="${session.lastContest.imageCenterHeight}"/>
	                        <g:if test="${params.a3=='true'}">
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_center" value="${(a3_landscape_factor*height_value_center).toInteger()}"/>
	                            </g:if>
	                            <g:else>
	                                <g:set var="height_value_center" value="${(a3_portrait_factor*height_value_center).toInteger()}"/>
	                            </g:else>
	                        </g:if>   
		                    <g:else>
		                        <g:if test="${params.landscape=='true'}">
		                            <g:set var="height_value_center" value="${(a4_landscape_factor*height_value_center).toInteger()}"/>
		                        </g:if>    
		                    </g:else>
	                        <g:if test="${height_value_center > 50}">
	                            <g:set var="height_value_diff_center" value="${height_value_center-50}"/>
	                        </g:if>    
	                    </g:if>
	                    
	                    <g:if test="${session?.lastContest?.imageRight}">
	                        <g:set var="height_value_right" value="${session.lastContest.imageRightHeight}"/>
	                        <g:if test="${params.a3=='true'}">
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_right" value="${(a3_landscape_factor*height_value_right).toInteger()}"/>
	                            </g:if>
	                            <g:else>
	                                <g:set var="height_value_right" value="${(a3_portrait_factor*height_value_right).toInteger()}"/>
	                            </g:else>
	                        </g:if>
		                    <g:else>
		                        <g:if test="${params.landscape=='true'}">
		                            <g:set var="height_value_right" value="${(a4_landscape_factor*height_value_right).toInteger()}"/>
		                        </g:if>    
		                    </g:else>
	                        <g:if test="${height_value_right > 50}">
		                        <g:set var="height_value_diff_right" value="${height_value_right-50}"/>
	                        </g:if>    
	                    </g:if>
	
	                    <g:if test="${session?.lastContest?.imageBottomLeft}">
	                        <g:set var="height_value_bottomleft" value="${session.lastContest.imageBottomLeftHeight}"/>
	                        <g:if test="${params.a3=='true'}">
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_bottomleft" value="${(a3_landscape_factor*height_value_bottomleft).toInteger()}"/>
	                            </g:if>
	                            <g:else>
	                                <g:set var="height_value_bottomleft" value="${(a3_portrait_factor*height_value_bottomleft).toInteger()}"/>
	                            </g:else>
	                        </g:if>   
	                        <g:else>
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_bottomleft" value="${(a4_landscape_factor*height_value_bottomleft).toInteger()}"/>
	                            </g:if>    
	                        </g:else>
	                    </g:if>
	                    
	                    <g:if test="${session?.lastContest?.imageBottomRight}">
	                        <g:set var="height_value_bottomright" value="${session.lastContest.imageBottomRightHeight}"/>
	                        <g:if test="${params.a3=='true'}">
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_bottomright" value="${(a3_landscape_factor*height_value_bottomright).toInteger()}"/>
	                            </g:if>
	                            <g:else>
	                                <g:set var="height_value_bottomright" value="${(a3_portrait_factor*height_value_bottomright).toInteger()}"/>
	                            </g:else>
	                        </g:if>   
	                        <g:else>
	                            <g:if test="${params.landscape=='true'}">
	                                <g:set var="height_value_bottomright" value="${(a4_landscape_factor*height_value_bottomright).toInteger()}"/>
	                            </g:if>    
	                        </g:else>
	                    </g:if>
	                    
	                    <g:set var="padding_horizontal" value="${new Integer(10)}"/>
	                    <g:if test="${params.a3=='true'}">
	                        <g:if test="${params.landscape=='true'}">
	                            <g:set var="padding_horizontal" value="${(a3_landscape_factor*padding_horizontal).toInteger()}"/>
	                        </g:if>
	                        <g:else>
	                            <g:set var="padding_horizontal" value="${(a3_portrait_factor*padding_horizontal).toInteger()}"/>
	                        </g:else>
	                    </g:if>   
	                    <g:else>
	                        <g:if test="${params.landscape=='true'}">
	                            <g:set var="padding_horizontal" value="${(a4_landscape_factor*padding_horizontal).toInteger()}"/>
	                        </g:if>    
	                    </g:else>
	
	                    <g:set var="height_value_diff" value="${new Integer(0)}"/>
	                    <g:if test="${height_value_diff_left > height_value_diff}">
	                        <g:set var="height_value_diff" value="${height_value_diff_left}"/>
	                    </g:if>
	                    <g:if test="${height_value_diff_right > height_value_diff}">
	                        <g:set var="height_value_diff" value="${height_value_diff_right}"/>
	                    </g:if>
	                        
	                    <g:set var="height_value_offset" value="${new Integer(20)}"/>
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
	                            <g:set var="height_value" value="${height_value_diff_left + height_value_offset}"/>
	                        </g:if>
	                        <g:else>
	                            <g:set var="height_value" value="${height_value_diff_right + height_value_offset}"/>
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
		                        <img src="${createLink(controller:'contest',action:'view_image_left',params:[contestid:session.lastContest.id])}" align="left" height="${height_value_left}" style="vertical-align:top;padding-right:${padding_horizontal}px;"/>
		                    </g:if>
		                    
		                    <g:if test="${session?.lastContest?.imageCenter}">
		                        <img src="${createLink(controller:'contest',action:'view_image_center',params:[contestid:session.lastContest.id])}" align="center" height="${height_value_center}" style="vertical-align:top;" />
		                    </g:if>
		                    <g:elseif test="${session?.contestTitle}">
		                        ${HTMLFilter.EncodeAsHTML(session.contestTitle)}
		                    </g:elseif>
		                    <g:elseif test="${session?.lastContest}">
		                        ${HTMLFilter.EncodeAsHTML(session.lastContest.name())}
		                    </g:elseif>
		                    <g:else>
		                        ${message(code:'fc.program.title')}
		                    </g:else>
		                    
		                    <g:if test="${session?.lastContest?.imageRight}">
		                        <img src="${createLink(controller:'contest',action:'view_image_right',params:[contestid:session.lastContest.id])}" align="right" height="${height_value_right}" style="vertical-align:top;;padding-left:${padding_horizontal}px;"/>
		                    </g:if>
	                    </h1>
	                </g:if>
				</g:if>
				<g:else>
	                <h1 id="branding">
	                    <g:if test="${session?.contestTitle}">
	                    	${HTMLFilter.EncodeAsHTML(session.contestTitle)}
	                    </g:if>
	                    <g:elseif test="${session?.lastContest}">
	                        ${HTMLFilter.EncodeAsHTML(session.lastContest.name())}
	                    </g:elseif>
	                    <g:else>
	                        ${message(code:'fc.program.title')}
	                    </g:else>
	                </h1>
                </g:else>
            </div>
            <g:layoutBody />
            <g:if test="${params.print}">
                <g:if test="${session?.lastContest?.imageBottomOn}">
		            <g:if test="${session?.lastContest?.imageBottomLeft || session?.lastContest?.imageBottomRight}">
		                <p>
			                <g:if test="${session?.lastContest?.imageBottomLeft}">
			                    <img src="${createLink(controller:'contest',action:'view_image_bottom_left',params:[contestid:session.lastContest.id])}" align="left" height="${height_value_bottomleft}" />
			                </g:if>
			                <g:if test="${session?.lastContest?.imageBottomRight}">
			                    <img src="${createLink(controller:'contest',action:'view_image_bottom_right',params:[contestid:session.lastContest.id])}" align="right" height="${height_value_bottomright}" />
			                </g:if>
		                </p>
		            </g:if>
	                <g:if test="${session?.lastContest?.imageBottomLeftText || session?.lastContest?.imageBottomRightText}">
	                    <g:if test="${session?.lastContest?.imageBottomLeft || session?.lastContest?.imageBottomRight}">
	                        <br/>
	                    </g:if>
		                <h2 id="signature" style="font-size:${session?.lastContest?.imageBottomTextSize}px;font-weight:normal;clear:both;">
		                    <g:if test="${session?.lastContest?.imageBottomLeftText}">
					            <div style="float:left;">${session?.lastContest?.imageBottomLeftText}</div>
					        </g:if>
		                    <g:if test="${session?.lastContest?.imageBottomRightText}">
		                        <div style="float:right;">${session?.lastContest?.imageBottomRightText}</div>
		                    </g:if>
		                </h2>
	                </g:if>
	            </g:if>
	        </g:if>
        </div>
    </body>
</html>