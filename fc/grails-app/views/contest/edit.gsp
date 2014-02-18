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
                            </p>
                            <p>
                                <label>${message(code:'fc.titleprintprefix')}:</label>
                                <br/>
                                <input type="text" id="printPrefix" name="printPrefix" value="${fieldValue(bean:contestInstance,field:'printPrefix')}" tabIndex="2"/>
                            </p>
                            <p>
                                <div>
	                               	<g:checkBox name="resultClasses" value="${contestInstance.resultClasses}" />
    	                            <label>${message(code:'fc.resultclasses')}</label>
                                </div>
                            </p>
                            <p>
                                <label>${message(code:'fc.contestrule')}*:</label>
                                <br/>
                                <g:select from="${ContestRules.values()}" optionValue="${{message(code:it.titleCode)}}" name="contestRule" value="${contestInstance.contestRule}" tabIndex="3"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.scale')}*:</label>
                                <br/>
                                <input type="text" id="mapScale" name="mapScale" value="${fieldValue(bean:contestInstance,field:'mapScale')}" tabIndex="4"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.timezone')}* [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <input type="text" id="timeZone" name="timeZone" value="${fieldValue(bean:contestInstance,field:'timeZone')}" tabIndex="5"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="6"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.bestofanalysistasknum')}*:</label>
                                <br/>
                                <input type="text" id="bestOfAnalysisTaskNum" name="bestOfAnalysisTaskNum" value="${fieldValue(bean:contestInstance,field:'bestOfAnalysisTaskNum')}" tabIndex="7"/>
                            </p>
                            <g:if test="${!contestInstance.aflosTest}">
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
	                                <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.left')}:</td>
				                    <g:if test="${contestInstance.imageLeft}">
				                        <td><img src="${createLink(controller:'contest',action:'view_image_left',params:[contestid:contestInstance.id])}" height="${contestInstance.imageLeftHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageLeftHeight" name="imageLeftHeight" value="${fieldValue(bean:contestInstance,field:'imageLeftHeight')}" tabIndex="21"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="actualimage_imageleft" value="${message(code:'fc.update')}" tabIndex="22"/>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imageleft" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="23"/>
				                    </g:if>
				                    <g:else>
				                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imageleft" value="${message(code:'fc.contest.image.select')}" tabIndex="24"/>
                                        <td/>
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
                                        <td style="vertical-align:middle"><g:actionSubmit action="actualimage_imagecenter" value="${message(code:'fc.update')}" tabIndex="32"/>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagecenter" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="33"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagecenter" value="${message(code:'fc.contest.image.select')}" tabIndex="34"/>
                                        <td/>
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
                                        <td style="vertical-align:middle"><g:actionSubmit action="actualimage_imageright" value="${message(code:'fc.update')}" tabIndex="42"/>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imageright" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="43"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imageright" value="${message(code:'fc.contest.image.select')}" tabIndex="44"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td/>
                                    <td/>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.title.size')}:</td>
                                    <td style="vertical-align:middle"><input type="text" id="titleSize" name="titleSize" value="${fieldValue(bean:contestInstance,field:'titleSize')}" tabIndex="51"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="actual_titelsize" value="${message(code:'fc.update')}" tabIndex="52"/>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_titlesize" value="${message(code:'fc.contest.title.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="53"/>
                                </tr>
                                <tr>
                                    <td/>
                                    <td colspan="5">
                                        <g:actionSubmit action="printtest_a4_portrait" value="${message(code:'fc.print.test.a4.portrait')}" tabIndex="61"/> 
                                        <g:actionSubmit action="printtest_a3_portrait" value="${message(code:'fc.print.test.a3.portrait')}" tabIndex="62"/> 
                                        <g:actionSubmit action="printtest_a4_landscape" value="${message(code:'fc.print.test.a4.landscape')}" tabIndex="63"/>
                                        <g:actionSubmit action="printtest_a3_landscape" value="${message(code:'fc.print.test.a3.landscape')}" tabIndex="64"/>
                                    </td>
                                </tr>
	                        </tbody>
	                    </table>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="101"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="102"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>