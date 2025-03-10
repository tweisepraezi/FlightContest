<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.flighttest.create')}</title>         
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.flighttest.create')}</h2>
                <g:hasErrors bean="${flightTestInstance}">
                    <div class="errors">
                        <g:renderErrors bean="${flightTestInstance}" />
                    </div>
                </g:hasErrors>
                <div class="block" id="forms" >
                    <g:if test="${params.fromlistplanning}">
                        <g:set var="newparams" value="['taskid':params.taskid,'fromlistplanning':true]"/>
                    </g:if> <g:else> 
                        <g:if test="${params.fromtask}">
                            <g:set var="newparams" value="['taskid':params.taskid,'fromtask':true]"/>
                        </g:if> <g:else>
                            <g:set var="newparams" value="['taskid':params.taskid]"/>
                        </g:else>
                    </g:else>
                    <g:form method="post" params="${newparams}" >
                        <g:set var="ti" value="${[]+1}"/>
                        <fieldset>
                            <p>
                                <label>${message(code:'fc.title')}:</label>
                                <br/>
                                <input type="text" id="title" name="title" value="${fieldValue(bean:flightTestInstance,field:'title')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.route')}*:</label>
                                <br/>
                                <g:select id="routeselect_id" from="${RouteTools.GetOkFlightTestRoutes(flightTestInstance.task.contest)}" optionKey="id" optionValue="${{it.GetFlightTestRouteName()}}" name="route.id" value="${flightTestInstance?.route?.id}" ></g:select>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.wind')}</legend>
                            <p>
                                <label>${message(code:'fc.wind.direction')}* [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="direction" name="direction" value="${fieldValue(bean:flightTestInstance,field:'direction')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.wind.velocity')}* [${message(code:'fc.knot')}]:</label>
                                <br/>
                                <input type="text" id="speed" name="speed" value="${fieldValue(bean:flightTestInstance,field:'speed')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend>${message(code:'fc.runway')}</legend>
                            <p>
                                <label>${message(code:'fc.runway.direction.to')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="TODirection" name="TODirection" value="${fieldValue(bean:flightTestInstance,field:'TODirection')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.to2sp')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
								<input type="text" id="TODurationFormula" name="TODurationFormula" value="${fieldValue(bean:flightTestInstance,field:'TODurationFormula')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.direction.ldg')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="LDGDirection" name="LDGDirection" value="${fieldValue(bean:flightTestInstance,field:'LDGDirection')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.fp2ldg')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
								<input type="text" id="LDGDurationFormula" name="LDGDurationFormula" value="${fieldValue(bean:flightTestInstance,field:'LDGDurationFormula')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <fieldset id="intermediateRunway" hidden >
                            <legend>${message(code:'fc.runway.intermediate')}</legend>
                            <p>
                                <label>${message(code:'fc.runway.direction.ildg')} [${message(code:'fc.grad')}]:</label>
                                <br/>
                                <input type="text" id="iTOiLDGDirection" name="iTOiLDGDirection" value="${fieldValue(bean:flightTestInstance,field:'iTOiLDGDirection')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.ifp2ildg')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
                                <input type="text" id="iLDGDurationFormula" name="iLDGDurationFormula" value="${fieldValue(bean:flightTestInstance,field:'iLDGDurationFormula')}" tabIndex="${ti[0]++}"/>
                            </p>
                            <p>
                                <label>${message(code:'fc.runway.duration.2isp')}*:</label>
                                <a href="/fc/docs/help_${session.showLanguage}.html#flight-time-calculation" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a>
                                <br/>
                                <input type="text" id="iTODurationFormula" name="iTODurationFormula" value="${fieldValue(bean:flightTestInstance,field:'iTODurationFormula')}" tabIndex="${ti[0]++}"/>
                            </p>
                        </fieldset>
                        <script>
                            $(document).on('change', '#routeselect_id', function() {
                                set_runway_data();
                            });
                            function set_runway_data() {
                            	var select_route_id = "";
                                $("#routeselect_id :selected").each(function() {
                                   	select_route_id = $(this).val();
                                });
                                if (select_route_id) {
                                    $.post("/fc/flightTest/getroutedata_ajax", {routeId:select_route_id}, route_loaded, "json");
                                    function route_loaded(data, status) {
                                        if (status == "success") {
                                            $("#TODirection").val(data.TODirection);
                                            $("#LDGDirection").val(data.LDGDirection);
                                            $("#iTOiLDGDirection").val(data.iTOiLDGDirection);
                                            if (!data.isIntermediateRunway) {
                                                $("#intermediateRunway").hide()
                                            } else {
                                                $("#intermediateRunway").show()
                                            }
                                        }
                                    }
                                }
                            }
                            set_runway_data();
                        </script>
                        <g:actionSubmit action="save" value="${message(code:'fc.create')}" tabIndex="${ti[0]++}"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="${ti[0]++}"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>