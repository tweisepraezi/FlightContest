<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.edit')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.edit')}</h2>
                <g:hasErrors bean="${contestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${contestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:form method="post" params="${['contestReturnAction':contestReturnAction,'contestReturnController':contestReturnController,'contestReturnID':contestReturnID]}" >
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}*:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}" tabIndex="1"/>
                                <a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a>
                            </p>
                            <p>
                                <label>${message(code:'fc.titleprintprefix')}:</label>
                                <br/>
                                <input type="text" id="printPrefix" name="printPrefix" value="${fieldValue(bean:contestInstance,field:'printPrefix')}" tabIndex="2"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.organizer')}*:</label>
                                <br/>
                                <input type="text" id="printOrganizer" name="printOrganizer" value="${fieldValue(bean:contestInstance,field:'printOrganizer')}" tabIndex="3"/>
                            </p>
                            <p>
                                <div>
	                               	<g:checkBox name="resultClasses" value="${contestInstance.resultClasses}" />
    	                            <label>${message(code:'fc.contest.withclasses')}</label>
                                </div>
                                <div>
                                    <g:checkBox name="contestRuleForEachClass" value="${contestInstance.contestRuleForEachClass}" />
                                    <label>${message(code:'fc.contestrule.foreachclass')}</label>
                                </div>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestrule')}*:</label>
                                <a href="../../docs/help.html#supported-rules" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
                                <g:select from="${ContestRules.GetContestRules()}" optionValue="${{message(code:it.titleCode)}}" name="contestRule" value="${contestInstance.contestRule}" tabIndex="4"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.scale')}*:</label>
                                <br/>
                                <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:contestInstance,field:'mapScale')}" tabIndex="5"/>
                            </p>
                            <label>${message(code:'fc.coordpresentation')}:</label>
                            <g:set var="format_labels" value="${[]}"/>
                            <g:each var="format_code" in="${CoordPresentation.GetCodes()}">
                                <g:set var="format_labels" value="${format_labels+message(code:format_code)}"/>
                            </g:each>
                            <g:set var="format_values" value="${CoordPresentation.GetValues()}"/>
                            <g:radioGroup name="coordPresentation" labels="${format_labels}" values="${format_values}" value="${contestInstance.coordPresentation}">
                                <div>
                                    <label>${it.radio} ${it.label}</label>
                                </div>
                            </g:radioGroup>
                            <br/>
                            <p>
                                <label>${message(code:'fc.timezone')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="timeZone" name="timeZone" value="${fieldValue(bean:contestInstance,field:'timeZone')}" tabIndex="6"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="7"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.bestofanalysistasknum')}*:</label>
                                <br/>
                                <input type="text" id="bestOfAnalysisTaskNum" name="bestOfAnalysisTaskNum" value="${fieldValue(bean:contestInstance,field:'bestOfAnalysisTaskNum')}" tabIndex="8"/>
                            </p>
                            <g:if test="${!contestInstance.aflosTest && contestInstance.IsAFLOSPossible()}">
	                            <p>
	                                <div>
	                                    <g:checkBox name="aflosUpload" value="${contestInstance.aflosUpload}" />
	                                    <label>${message(code:'fc.useuploadedaflos')}</label>
	                                </div>
	                            </p>
                            </g:if>
                        </fieldset>
	                    <table>
	                        <tbody>
                                <tr>
                                    <td colspan="5">
                                        <p>
                                            <label>${message(code:'fc.contest.paper.style')}:</label>
                                            <a href="../../docs/help.html#print-styles-samples" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                            <br/>
                                            <g:textArea name="printStyle" value="${contestInstance.printStyle}" rows="5" cols="110" tabIndex="10"/>
                                        </p>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td/>
                                    <td/>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.title.size')}:</td>
                                    <td style="vertical-align:middle"><input type="text" id="titleSize" name="titleSize" value="${fieldValue(bean:contestInstance,field:'titleSize')}" tabIndex="11"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_titlesize" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="12"/>
                                </tr>
                                
	                            <tr>
	                                <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.left')}:</td>
				                    <g:if test="${contestInstance.imageLeft}">
				                        <td><img src="${createLink(controller:'contest',action:'view_image_left',params:[contestid:contestInstance.id])}" height="${contestInstance.imageLeftHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageLeftHeight" name="imageLeftHeight" value="${fieldValue(bean:contestInstance,field:'imageLeftHeight')}" tabIndex="21"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imageleft" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="22"/>
				                    </g:if>
				                    <g:else>
				                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imageleft" value="${message(code:'fc.contest.image.select')}" tabIndex="23"/>
                                        <td/>
				                        <td/>
                                        <td/>
				                    </g:else>
	                            </tr>
                                <tr>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.center')}:</td>
                                    <g:if test="${contestInstance.imageCenter}">
                                        <td><img src="${createLink(controller:'contest',action:'view_image_center',params:[contestid:contestInstance.id])}" height="${contestInstance.imageCenterHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageCenterHeight" name="imageCenterHeight" value="${fieldValue(bean:contestInstance,field:'imageCenterHeight')}" tabIndex="31"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagecenter" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="32"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagecenter" value="${message(code:'fc.contest.image.select')}" tabIndex="33"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.right')}:</td>
                                    <g:if test="${contestInstance.imageRight}">
                                        <td><img src="${createLink(controller:'contest',action:'view_image_right',params:[contestid:contestInstance.id])}" height="${contestInstance.imageRightHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageRightHeight" name="imageRightHeight" value="${fieldValue(bean:contestInstance,field:'imageRightHeight')}" tabIndex="41"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imageright" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="42"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imageright" value="${message(code:'fc.contest.image.select')}" tabIndex="43"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                
                                <tr>
                                    <td>
                                    <td>
                                    <td>
                                    <td colspan="2">
			                            <p>
			                                <div>
			                                    <g:checkBox name="imageBottomOn" value="${contestInstance.imageBottomOn}" />
			                                    <label>${message(code:'fc.contest.image.bottom.on')}</label>
			                                </div>
			                            </p>
			                        </td>
                                </tr>
                                <tr>
                                    <td/>
                                    <td/>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.bottom.size')}:</td>
                                    <td style="vertical-align:middle"><input type="text" id="imageBottomTextSize" name="imageBottomTextSize" value="${fieldValue(bean:contestInstance,field:'imageBottomTextSize')}" tabIndex="51"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_imagebottomsize" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="52"/>
                                </tr>
                                
                                <tr>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.bottomleft')}:</td>
                                    <g:if test="${contestInstance.imageBottomLeft}">
                                        <td><img src="${createLink(controller:'contest',action:'view_image_bottom_left',params:[contestid:contestInstance.id])}" height="${contestInstance.imageBottomLeftHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageBottomLeftHeight" name="imageBottomLeftHeight" value="${fieldValue(bean:contestInstance,field:'imageBottomLeftHeight')}" tabIndex="61"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagebottomleft" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="62"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagebottomleft" value="${message(code:'fc.contest.image.select')}" tabIndex="63"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>
                                    <td colspan="4">
                                        <input type="text" id="imageBottomLeftText" name="imageBottomLeftText" value="${fieldValue(bean:contestInstance,field:'imageBottomLeftText')}" size="110" tabIndex="64"/>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.bottomright')}:</td>
                                    <g:if test="${contestInstance.imageBottomRight}">
                                        <td><img src="${createLink(controller:'contest',action:'view_image_bottom_right',params:[contestid:contestInstance.id])}" height="${contestInstance.imageBottomRightHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageBottomRightHeight" name="imageBottomRightHeight" value="${fieldValue(bean:contestInstance,field:'imageBottomRightHeight')}" tabIndex="71"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagebottomright" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="72"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagebottomright" value="${message(code:'fc.contest.image.select')}" tabIndex="73"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>
                                    <td colspan="4">
                                        <input type="text" id="imageBottomRightText" name="imageBottomRightText" value="${fieldValue(bean:contestInstance,field:'imageBottomRightText')}" size="110" tabIndex="74"/>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a4_portrait" value="${message(code:'fc.print.test.a4.portrait')}" tabIndex="101"/></td>
                                    <td/>
                                    <td/>
                                    <td/>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a4_landscape" value="${message(code:'fc.print.test.a4.landscape')}" tabIndex="111"/></td>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.paper.factor')}:</td>
                                    <td><input type="text" id="a4LandscapeFactor" name="a4LandscapeFactor" value="${fieldValue(bean:contestInstance,field:'a4LandscapeFactor')}" tabIndex="112"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_a4landscapefactor" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="113"/></td>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a3_portrait" value="${message(code:'fc.print.test.a3.portrait')}" tabIndex="121"/></td>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.paper.factor')}:</td>
                                    <td><input type="text" id="a3PortraitFactor" name="a3PortraitFactor" value="${fieldValue(bean:contestInstance,field:'a3PortraitFactor')}" tabIndex="122"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_a3portraitfactor" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="123"/></td>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a3_landscape" value="${message(code:'fc.print.test.a3.landscape')}" tabIndex="131"/></td>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.paper.factor')}:</td>
                                    <td><input type="text" id="a3LandscapeFactor" name="a3LandscapeFactor" value="${fieldValue(bean:contestInstance,field:'a3LandscapeFactor')}" tabIndex="132"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_a3landscapefactor" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="133"/></td>
                                </tr>
                                <tr>
                                    <td/>
                                    <td/>
                                    <td/>
                                    <td><g:actionSubmit action="savecontest" value="${message(code:'fc.save')}" tabIndex="141"/></td>
                                    <td/>
                                </tr>
	                        </tbody>
	                    </table>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="201"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="202"/>
                        <a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>