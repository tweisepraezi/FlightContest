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
						<g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}*:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:contestInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                                <a href="#end"><img src="${createLinkTo(dir:'images',file:'down.png')}"/></a>
                            </p>
                            <p>
                                <label>${message(code:'fc.titleprintprefix')}:</label>
                                <br/>
                                <input type="text" id="printPrefix" name="printPrefix" value="${fieldValue(bean:contestInstance,field:'printPrefix')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.organizer')}*:</label>
                                <br/>
                                <input type="text" id="printOrganizer" name="printOrganizer" value="${fieldValue(bean:contestInstance,field:'printOrganizer')}" tabIndex="${ti[0]++}"/>
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
                            <p style="margin-right:1%;">
                                <label>${message(code:'fc.contestrule')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#supported-rules" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
                                <g:select from="${ContestRules.GetContestRules()}" optionValue="${{it.ruleValues.ruleTitle}}" name="contestRule" value="${contestInstance.contestRule}" tabIndex="${ti[0]++}"/>
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
                                <label>${message(code:'fc.contest.contestdate')}*:</label>
                                <br/>
                                <input type="date" id="liveTrackingContestDate" name="liveTrackingContestDate" value="${fieldValue(bean:contestInstance,field:'liveTrackingContestDate')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <g:if test="${contestInstance.timeZone2}">
                                <p>
                                    <label>${message(code:'fc.timezone')}*:</label>
                                    <g:select from="${TimeZone.getAvailableIDs()}" optionValue="${{it}}" name="timeZone2" value="${contestInstance.timeZone2.getID()}" tabIndex="${ti[0]++}" />
                                </p>
                            </g:if>
                            <g:else>
                                <%--
                                <p>
                                    <label>${message(code:'fc.timezone')}*:</label>
                                    <g:select from="${TimeZone.getAvailableIDs(1*3600000)}" optionValue="${{it}}" name="timeZone2" tabIndex="${ti[0]++}" />
                                --%>
                                </p>
                            </g:else>
                            <p>
                                <label>${message(code:'fc.timezone.diff')} [${message(code:'fc.time.hmin')}]:</label>
                                <br/>
                                <g:if test="${contestInstance.timeZone2}">
                                    <input type="text" id="timeZone" name="timeZone" value="${fieldValue(bean:contestInstance,field:'timeZone')}" disabled tabIndex="${ti[0]++}"/>
                                </g:if>
                                <g:else>
                                    <input type="text" id="timeZone" name="timeZone" value="${fieldValue(bean:contestInstance,field:'timeZone')}" tabIndex="${ti[0]++}"/>
                                </g:else>
                            </p>
                            <p>
                                <label>${message(code:'fc.teamcrewnum')}*:</label>
                                <br/>
                                <input type="text" id="teamCrewNum" name="teamCrewNum" value="${fieldValue(bean:contestInstance,field:'teamCrewNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.bestofanalysistasknum')}*:</label>
                                <br/>
                                <input type="text" id="bestOfAnalysisTaskNum" name="bestOfAnalysisTaskNum" value="${fieldValue(bean:contestInstance,field:'bestOfAnalysisTaskNum')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
	                    <table>
	                        <tbody>
                                <tr>
                                    <td colspan="5">
                                        <p style="margin-right:2%;">
                                            <label>${message(code:'fc.contest.paper.style')}:</label>
                                            <a href="/fc/docs/help_${session.showLanguage}.html#print-styles-samples" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                            <br/>
                                            <g:textArea name="printStyle" value="${contestInstance.printStyle}" rows="5" style="width:100%;" tabIndex="${ti[0]++}"/>
                                        </p>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td/>
                                    <td/>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.title.size')}:</td>
                                    <td style="vertical-align:middle"><input type="text" id="titleSize" name="titleSize" value="${fieldValue(bean:contestInstance,field:'titleSize')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_titlesize" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </tr>
                                
	                            <tr>
	                                <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.left')}:</td>
				                    <g:if test="${contestInstance.imageLeft}">
				                        <td><img src="${createLink(controller:'contest',action:'view_image_left',params:[contestid:contestInstance.id])}" height="${contestInstance.imageLeftHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageLeftHeight" name="imageLeftHeight" value="${fieldValue(bean:contestInstance,field:'imageLeftHeight')}" tabIndex="${ti[0]++}"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imageleft" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
				                    </g:if>
				                    <g:else>
				                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imageleft" value="${message(code:'fc.contest.image.select')}" tabIndex="${ti[0]++}"/>
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
                                        <td style="vertical-align:middle"><input type="text" id="imageCenterHeight" name="imageCenterHeight" value="${fieldValue(bean:contestInstance,field:'imageCenterHeight')}" tabIndex="${ti[0]++}"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagecenter" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagecenter" value="${message(code:'fc.contest.image.select')}" tabIndex="${ti[0]++}"/>
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
                                        <td style="vertical-align:middle"><input type="text" id="imageRightHeight" name="imageRightHeight" value="${fieldValue(bean:contestInstance,field:'imageRightHeight')}" tabIndex="${ti[0]++}"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imageright" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imageright" value="${message(code:'fc.contest.image.select')}" tabIndex="${ti[0]++}"/>
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
                                    <td style="vertical-align:middle"><input type="text" id="imageBottomTextSize" name="imageBottomTextSize" value="${fieldValue(bean:contestInstance,field:'imageBottomTextSize')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_imagebottomsize" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                </tr>
                                
                                <tr>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.bottomleft')}:</td>
                                    <g:if test="${contestInstance.imageBottomLeft}">
                                        <td><img src="${createLink(controller:'contest',action:'view_image_bottom_left',params:[contestid:contestInstance.id])}" height="${contestInstance.imageBottomLeftHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageBottomLeftHeight" name="imageBottomLeftHeight" value="${fieldValue(bean:contestInstance,field:'imageBottomLeftHeight')}" tabIndex="${ti[0]++}"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagebottomleft" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagebottomleft" value="${message(code:'fc.contest.image.select')}" tabIndex="${ti[0]++}"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>
                                    <td colspan="4" >
                                        <input type="text" id="imageBottomLeftText" name="imageBottomLeftText" value="${fieldValue(bean:contestInstance,field:'imageBottomLeftText')}" size="110" tabIndex="${ti[0]++}"/>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.bottomright')}:</td>
                                    <g:if test="${contestInstance.imageBottomRight}">
                                        <td><img src="${createLink(controller:'contest',action:'view_image_bottom_right',params:[contestid:contestInstance.id])}" height="${contestInstance.imageBottomRightHeight}" style="vertical-align:top"/></td>
                                        <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.image.height')}:</td>
                                        <td style="vertical-align:middle"><input type="text" id="imageBottomRightHeight" name="imageBottomRightHeight" value="${fieldValue(bean:contestInstance,field:'imageBottomRightHeight')}" tabIndex="${ti[0]++}"/></td>
                                        <td style="vertical-align:middle"><g:actionSubmit action="deleteimage_imagebottomright" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                    </g:if>
                                    <g:else>
                                        <td style="vertical-align:middle"><g:actionSubmit action="selectfilename_imagebottomright" value="${message(code:'fc.contest.image.select')}" tabIndex="${ti[0]++}"/>
                                        <td/>
                                        <td/>
                                        <td/>
                                    </g:else>
                                </tr>
                                <tr>
                                    <td>
                                    <td colspan="4">
                                        <input type="text" id="imageBottomRightText" name="imageBottomRightText" value="${fieldValue(bean:contestInstance,field:'imageBottomRightText')}" size="110" tabIndex="${ti[0]++}"/>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a4_portrait" value="${message(code:'fc.print.test.a4.portrait')}" tabIndex="${ti[0]++}"/></td>
                                    <td/>
                                    <td/>
                                    <td/>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a4_landscape" value="${message(code:'fc.print.test.a4.landscape')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.paper.factor')}:</td>
                                    <td><input type="text" id="a4LandscapeFactor" name="a4LandscapeFactor" value="${fieldValue(bean:contestInstance,field:'a4LandscapeFactor')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_a4landscapefactor" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/></td>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a3_portrait" value="${message(code:'fc.print.test.a3.portrait')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.paper.factor')}:</td>
                                    <td><input type="text" id="a3PortraitFactor" name="a3PortraitFactor" value="${fieldValue(bean:contestInstance,field:'a3PortraitFactor')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_a3portraitfactor" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/></td>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><g:actionSubmit action="printtest_a3_landscape" value="${message(code:'fc.print.test.a3.landscape')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle;width:10%">${message(code:'fc.contest.paper.factor')}:</td>
                                    <td><input type="text" id="a3LandscapeFactor" name="a3LandscapeFactor" value="${fieldValue(bean:contestInstance,field:'a3LandscapeFactor')}" tabIndex="${ti[0]++}"/></td>
                                    <td style="vertical-align:middle"><g:actionSubmit action="reset_a3landscapefactor" value="${message(code:'fc.contest.reset')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/></td>
                                </tr>
	                        </tbody>
	                    </table>
	                    <table>
	                        <tbody>
                                <tr>
                                    <td colspan="2" style="vertical-align:middle;width:25%">${message(code:'fc.contest.crewdelimiter.pilotnavigator')}:</td>
                                    <td style="vertical-align:middle"><input type="text" id="crewPilotNavigatorDelimiter" name="crewPilotNavigatorDelimiter" maxlength="1" size="1" value="${fieldValue(bean:contestInstance,field:'crewPilotNavigatorDelimiter')}" tabIndex="${ti[0]++}"/></td>
                                    <td colspan="2"/>
                                </tr>
                                <tr>
                                    <td colspan="2" style="vertical-align:middle;width:25%">${message(code:'fc.contest.crewdelimiter.surnameforename')}:</td>
                                    <td style="vertical-align:middle"><input type="text" id="crewSurnameForenameDelimiter" name="crewSurnameForenameDelimiter" maxlength="1" size="1" value="${fieldValue(bean:contestInstance,field:'crewSurnameForenameDelimiter')}" tabIndex="${ti[0]++}"/></td>
                                    <td colspan="2"/>
                                </tr>
	                        </tbody>
	                    </table>
                        <g:set var="contest_properties" value="${ContestProperty.findAllByContest(contestInstance,[sort:"id"])}"/>
                        <table>
                            <tbody>
                                <tr>
                                    <td style="width:10%;">${message(code:'fc.contest.properties')}: <a href="/fc/docs/help_${session.showLanguage}.html#contest-properties" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></td>
                                    <th>${message(code:'fc.contest.properties.key')}</th>
                                    <th>${message(code:'fc.contest.properties.value')}</th>
                                    <th>${message(code:'fc.contest.properties.actions')}</th>
                                </tr>
                                <tr>
                                    <td/>
                                    <td><input type="text" id="contestproperty_key_id" tabIndex="${ti[0]++}"/></td>
                                    <td><input type="text" id="contestproperty_value_id" tabIndex="${ti[0]++}"/></td>
                                    <td><button type="submit" id="contestproperty_add" tabIndex="${ti[0]++}">${message(code:'fc.contest.properties.add')}</button></td>
                                </tr>
                                <script>
                                    $(document).on('click', '#contestproperty_add', function() {
                                        if (!$("#contestproperty_key_id").val()) {
                                            alert('${message(code:'fc.contest.properties.key.missing')}');
                                            return false;
                                        }
                                        if (!$("#contestproperty_value_id").val()) {
                                            alert('${message(code:'fc.contest.properties.value.missing')}');
                                            return false;
                                        }
                                        if (confirm('${message(code:'fc.areyousure')}')) {
                                            window.location.href = "/fc/contest/add_contestproperty/${contestInstance.id}?key=" + $("#contestproperty_key_id").val() +"&value=" + $("#contestproperty_value_id").val();
                                        }
                                        return false;
                                    });
                                </script>
                                <g:if test="${contest_properties}" >
                                    <g:each var="contest_property_instance" in="${contest_properties}">
                                        <tr>
                                            <td/>
                                            <td>${contest_property_instance.key}</td>
                                            <td>${contest_property_instance.value}</td>
                                            <td><button type="submit" id="contestproperty_delete_${contest_property_instance.id}" tabIndex="${ti[0]++}">${message(code:'fc.delete')}</button></td>
                                        </tr>
                                        <script>
                                            $(document).on('click', '#contestproperty_delete_${contest_property_instance.id}', function() {
                                                if (confirm('${message(code:'fc.contest.properties.delete',args:[contest_property_instance.key, contest_property_instance.value])}')) {
                                                    window.location.href = "/fc/contest/delete_contestproperty?contestPropertyId=${contest_property_instance.id}";
                                                }
                                                return false;
                                            });
                                        </script>
                                    </g:each>
                                </g:if>
                            </tbody>
                        </table>
	                    <g:if test="${BootStrap.global.IsLiveTrackingPossible() && contestInstance.liveTrackingScorecard}" >
	                        <fieldset>
                                <legend>${message(code:'fc.livetracking')}</legend>
	                            <div>
                                    <g:if test="${contestInstance.liveTrackingContestID}" >
                                        <label>${message(code:'fc.livetracking.contestvisibility.contestvisibility')}: ${contestInstance.GetLiveTrackingVisibility()}</label>
                                        <br/>
                                        <g:if test="${BootStrap.global.ShowLiveTrackingIDs()}">
                                            <label>${message(code:'fc.livetracking.contestid')}: ${contestInstance.liveTrackingContestID}</label>
                                            <br/>
                                        </g:if>
                                        <br/>
                                        <g:if test="${BootStrap.global.IsLiveTrackingContestDeletePossible()}" >
                                            <g:actionSubmit action="livetracking_contestdelete" value="${message(code:'fc.livetracking.contestdelete')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:actionSubmit action="livetracking_contestdisconnect" value="${message(code:'fc.livetracking.contestdisconnect')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        
                                        <g:if test="${contestInstance.liveTrackingContestVisibility == Defs.LIVETRACKING_VISIBILITY_PUBLIC}" >
                                            <g:actionSubmit action="livetracking_contestvisibility_setprivate" value="${message(code:'fc.livetracking.contestvisibility.setprivate')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="livetracking_contestvisibility_setunlisted" value="${message(code:'fc.livetracking.contestvisibility.setunlisted')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:if test="${contestInstance.liveTrackingContestVisibility == Defs.LIVETRACKING_VISIBILITY_PRIVATE}" >
                                            <g:actionSubmit action="livetracking_contestvisibility_setpublic" value="${message(code:'fc.livetracking.contestvisibility.setpublic')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="livetracking_contestvisibility_setunlisted" value="${message(code:'fc.livetracking.contestvisibility.setunlisted')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        <g:if test="${contestInstance.liveTrackingContestVisibility == Defs.LIVETRACKING_VISIBILITY_UNLISTED}" >
                                            <g:actionSubmit action="livetracking_contestvisibility_setpublic" value="${message(code:'fc.livetracking.contestvisibility.setpublic')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                            <g:actionSubmit action="livetracking_contestvisibility_setprivate" value="${message(code:'fc.livetracking.contestvisibility.setprivate')}" onclick="return confirm('${message(code:'fc.areyousure')}');" tabIndex="${ti[0]++}"/>
                                        </g:if>
                                        
                                        
                                    </g:if>
                                    <g:else>
                                        <g:actionSubmit action="livetracking_contestcreate" value="${message(code:'fc.livetracking.contestcreate')}" tabIndex="${ti[0]++}"/>
                                        <g:actionSubmit action="livetracking_contestconnect" value="${message(code:'fc.livetracking.contestconnect')}" tabIndex="${ti[0]++}"/>
                                    </g:else>
                                    <a href="/fc/docs/help_${session.showLanguage}.html#live-tracking" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
	                            </div>
                            </fieldset>
	                    </g:if>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <input type="hidden" name="version" value="${contestInstance?.version}"/>
                        <g:actionSubmit action="update" value="${message(code:'fc.update')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="savecontest" value="${message(code:'fc.save')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                        <a href="#start"><img src="${createLinkTo(dir:'images',file:'up.png')}"/></a>
                        <a name="end"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>