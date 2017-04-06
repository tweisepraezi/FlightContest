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
                            <p>${message(code:'fc.route.fileimport.info.txt')}<br/>${params.lineContent}</p>
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
                                <g:checkBox name="curved" value="${curved}" checked= "${false}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.curved')}*:</label>
                                <input type="text" id="curvedstartpos" name="curvedstartpos" value="5" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
                                <input type="text" id="curvedendpos" name="curvedendpos" value="8" maxlength="3" size="1" tabIndex="${ti[0]++}"/>
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
                                <g:checkBox name="autosecret" value="${autosecret}" checked= "${true}" tabIndex="${ti[0]++}"/>
                                <label>${message(code:'fc.route.fileimport.autosecret')}</label>
                            </div>
                            <br/>
                        </p>
                        <g:actionSubmit action="importfileroute2" value="${message(code:'fc.import')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="list" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:uploadForm>
                </div>
            </div>
        </div>
    </body>
</html>