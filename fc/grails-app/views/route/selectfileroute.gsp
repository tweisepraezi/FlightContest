<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.fileimport')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.fileimport')}</h2>
                <div class="block" id="forms" >
                    <g:uploadForm method="post">
                        <g:set var="ti" value="${[]+1}"/>
                        <div>
                            <p>${message(code:'fc.route.fileimport.info',args:[RouteFileTools.ROUTE_EXTENSIONS])}</p>
                            <input type="file" size="80" accept="${RouteFileTools.ROUTE_EXTENSIONS}" name="routefile" tabIndex="${ti[0]++}"/>
                        </div>
                        <div>
                            <br/>
                            <p>${message(code:'fc.route.fileimport.coords.info1')}<br/>${message(code:'fc.route.fileimport.coords.info2')}</p>
                        </div>
                        <div>
                            <p>${message(code:'fc.route.fileimport.info.txt')}<br/>${HTMLFilter.FilterParam(params.lineContent)}</p>
                        </div>
                        <div>
                            <label>${message(code:'fc.route.fileimport.foldername')}:</label>
                            <input type="text" id="foldername" name="foldername" value="" tabIndex="${ti[0]++}"/>
                        </div>
                        <div>
                            <g:checkBox name="readplacemarks" value="${readplacemarks}" checked= "${true}" tabIndex="${ti[0]++}"/>
                            <label>${message(code:'fc.route.fileimport.readplacemarks')}</label>
                            <br/>
                        </div>
                        <p>
                            <div>
                                <g:checkBox name="firstcoordto" value="${firstcoordto}" checked= "${true}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.firstcoordto')}</label>
                            </div>
                            <div>
                                <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
                                <input type="text" id="todirection" name="todirection" value="270" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <br/>
                            <div>
                                <g:checkBox name="curved1" value="${curved1}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.curved1')}*:</label>
                                <input type="text" id="curvedstartpos1" name="curvedstartpos1" value="5" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                                <input type="text" id="curvedendpos1" name="curvedendpos1" value="8" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <div>
                                <g:checkBox name="curved2" value="${curved2}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.curved2')}*:</label>
                                <input type="text" id="curvedstartpos2" name="curvedstartpos2" value="8" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                                <input type="text" id="curvedendpos2" name="curvedendpos2" value="12" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <div>
                                <g:checkBox name="curved3" value="${curved3}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.curved3')}*:</label>
                                <input type="text" id="curvedstartpos3" name="curvedstartpos3" value="15" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                                <input type="text" id="curvedendpos3" name="curvedendpos3" value="18" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <br/>
                            <div>
                                <g:checkBox name="semicircle1" value="${semicircle1}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.semicircle1')}*:</label>
                                <input type="text" id="semicirclepos1" name="semicirclepos1" value="6" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <div>
                                <g:checkBox name="semicircle2" value="${semicircle2}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.semicircle2')}*:</label>
                                <input type="text" id="semicirclepos2" name="semicirclepos2" value="9" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <div>
                                <g:checkBox name="semicircle3" value="${semicircle3}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.semicircle3')}*:</label>
                                <input type="text" id="semicirclepos3" name="semicirclepos3" value="12" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <br/>
                            <div>
                                <g:checkBox name="ildg" value="${ildg}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.ildg')}*:</label>
                                <input type="text" id="ildgpos" name="ildgpos" value="12" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <div>
                                <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
                                <input type="text" id="ildgdirection" name="ildgdirection" value="270" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <br/>
                            <div>
                                <label>${message(code:'fc.route.fileimport.ldg')}:</label>
                                <br/>
                                <g:radioGroup name="ldg" labels="${[message(code:'fc.route.fileimport.lastcoord'),message(code:'fc.route.fileimport.addto'),message(code:'fc.route.fileimport.nothing')]}" values="${[0,1,2]}" value="${0}" tabIndex="${ti[0]++}">
                                    <div>
                                        <label>${it.radio} ${it.label}</label>
                                    </div>
                                </g:radioGroup>
                            </div>
                            <div>
                                <label>${message(code:'fc.gatedirection')}* [${message(code:'fc.grad')}]:</label>
                                <input type="text" id="ldgdirection" name="ldgdirection" value="270" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                            </div>
                            <br/>
                            <div>
                                <g:checkBox name="autosecret" value="${autosecret}" checked= "${contestInstance.precisionFlying && !contestInstance.anrFlying}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.autosecret')}</label>
                                <input type="text" id="secretcoursechange" name="secretcoursechange" value="1.5" maxlength="5" size="2" tabIndex="${ti[0]++}"/>
								<label>${message(code:'fc.grad')}</label>
                            </div>
                        </p>
                        <g:if test="${contestInstance.anrFlying}" >
                            <div>
                                <label>${message(code:'fc.corridorwidth')}:</label>
                                <input type="text" id="corridorWidth" name="corridorWidth" value="0.5" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.mile')}</label>
                            </div>
                            <br/>
                        </g:if>
                        <g:else>
                            <input type="hidden" id="corridorWidth" name="corridorWidth" value="0.0" tabIndex="${ti[0]++}"/>
                        </g:else>
                        <g:actionSubmit action="importfileroute2" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>