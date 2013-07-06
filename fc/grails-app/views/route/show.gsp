<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.route.show')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="route" newaction="${message(code:'fc.route.new')}" importaction="${message(code:'fc.route.import')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.route.show')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.title')}:</td>
                                    <td colspan="2">${routeInstance.name()}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.route.import.name')}:</td>
                                    <td colspan="2">${routeInstance.mark}</td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.planningtesttask.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${PlanningTestTask.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance,[sort:"id"])}">
                                            <g:flighttest var="${c}" link="${createLink(controller:'flightTest',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <thead>
                                <tr>
                                    <th class="table-head" colspan=11">${message(code:'fc.coordroute.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.aflos.checkpoint')}</th>
                                    <th>${message(code:'fc.latitude')}</th>
                                    <th>${message(code:'fc.longitude')}</th>
                                    <th>${message(code:'fc.altitude')}</th>
                                    <th>${message(code:'fc.gatewidth')}</th>
                                    <th>${message(code:'fc.legduration')}</th>
                                    <th>${message(code:'fc.notimecheck')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:set var="last_measuretruetrack" value="${new BigDecimal(0)}"/>
                                <g:set var="last_measuredistance" value="${new BigDecimal(0)}"/>
                                <g:each var="coordroute_instance" in="${routeInstance.coords}" status="i">
                                    <tr class="${coordroute_instance.type in [CoordType.SP,CoordType.TP,CoordType.FP] ? '' : 'odd'}">
                                    	<!-- search next id -->
                                        <g:set var="next" value="${new Integer(0)}" />
                                        <g:set var="setnext" value="${false}" />
     		                            <g:each var="coordroute_instance2" in="${routeInstance.coords}">
                                        	<g:if test="${setnext}">
     				                        	<g:set var="next" value="${coordroute_instance2.id}" />
			                                	<g:set var="setnext" value="${false}" />
                                            </g:if>
                                            <g:if test="${coordroute_instance2 == coordroute_instance}">
	                                        	<g:set var="setnext" value="${true}" />
                                            </g:if>
     		                            </g:each>
        		                                
                                        <td><g:coordroutenum var="${coordroute_instance}" num="${i+1}" next="${next}" link="${createLink(controller:'coordRoute',action:'edit')}"/></td>
                                        <td>${coordroute_instance.titleWithRatio()}</td>
                                        <td>${coordroute_instance.mark}<g:if test="${coordroute_instance.planProcedureTurn}"> (${message(code:'fc.procedureturn.symbol')})</g:if></td>
                                        <td>${coordroute_instance.latName()}</td>
                                        <td>${coordroute_instance.lonName()}</td>
                                        <td>${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
                                        <td>${coordroute_instance.gatewidth2}${message(code:'fc.mile')}</td>
                                        <td>${coordroute_instance.legDurationName()}</td>
                                        <g:if test="${coordroute_instance.noTimeCheck}">
                                            <td>${message(code:'fc.yes')}</td>
                                        </g:if>
                                        <g:else>
                                            <td>-</td>
                                        </g:else>
                                        <g:if test="${last_measuretruetrack && last_measuretruetrack != coordroute_instance.measureTrueTrack}">
	                                        <td class="errors">${coordroute_instance.measureTrueTrackName()} !</td>
	                                    </g:if><g:else>
	                                        <td>${coordroute_instance.measureTrueTrackName()}</td>
	                                    </g:else>
                                        <g:if test="${last_measuredistance && last_measuredistance >= coordroute_instance.measureDistance}">
	                                        <td class="errors">${coordroute_instance.measureDistanceName()} !</td>
	                                    </g:if><g:else>
	                                        <td>${coordroute_instance.measureDistanceName()}</td>
	                                    </g:else>
                                    </tr>
                                    <g:if test="${coordroute_instance.type == CoordType.SECRET}">
	                                	<g:set var="last_measuretruetrack" value="${coordroute_instance.measureTrueTrack}" />
	                               	</g:if>
	                               	<g:else>
		                                <g:set var="last_measuretruetrack" value="${new BigDecimal(0)}"/>
	                               	</g:else>
                                    <g:if test="${coordroute_instance.type == CoordType.SECRET}">
	    	                            <g:set var="last_measuredistance" value="${coordroute_instance.measureDistance}" />
	                               	</g:if>
	                               	<g:else>
	                               		<g:set var="last_measuredistance" value="${new BigDecimal(0)}"/>
	                               	</g:else>
                                </g:each>
                            </tbody>
                        </table>
                        <table>
                            <g:set var="total_distance" value="${new BigDecimal(0)}" />
                            <thead>
                                <tr>
                                    <th class="table-head" colspan="6">${message(code:'fc.routelegcoord.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.truetrack.coord')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.coord')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:set var="i" value="${new Integer(0)}"/>
                                <g:each var="routeleg_instance" in="${routeInstance.routelegs}" status="num">
                                    <g:set var="i" value="${i+1}"/>
                                    <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routeleg_instance.testDistance())}" />
                                    <g:set var="course_change" value="${AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())}"/>
                                    <g:if test="${course_change.abs() >= 90}">
                                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                            <td class="center" colspan="6">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}</td>
                                        </tr>
                                        <g:set var="i" value="${i+1}"/>
                                    </g:if>
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${num+1}</td>
                                        <td>${routeleg_instance.title}</td>
                                        <td>${routeleg_instance.coordTrueTrackName()}</td>
                                        <td>${routeleg_instance.mapMeasureTrueTrackName()}</td>
                                        <td>${routeleg_instance.coordDistanceName()}</td>
                                        <td>${routeleg_instance.mapDistanceName()} (${routeleg_instance.mapMeasureDistanceName()})</td>
                                    </tr>
                                </g:each>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="6">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                                </tr>
                            </tfoot>
                        </table>
                        <table>
                            <g:set var="total_distance" value="${new BigDecimal(0)}" />
                            <thead>
                                <tr>
                                    <th class="table-head" colspan="6">${message(code:'fc.routelegtest.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.truetrack.coord')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.coord')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:set var="i" value="${new Integer(0)}"/>
                                <g:each var="routeleg_instance" in="${routeInstance.testlegs}" status="num">
                                    <g:set var="i" value="${i+1}"/>
                                    <g:set var="total_distance" value="${FcMath.AddDistance(total_distance,routeleg_instance.testDistance())}" />
                                    <g:set var="course_change" value="${AviationMath.courseChange(routeleg_instance.turnTrueTrack,routeleg_instance.testTrueTrack())}"/>
                                    <g:if test="${course_change.abs() >= 90}">
                                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                            <td class="center" colspan="6">${message(code:'fc.coursechange')} ${FcMath.GradStrMinus(course_change)}${message(code:'fc.grad')}</td>
                                        </tr>
                                        <g:set var="i" value="${i+1}"/>
                                    </g:if>
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${num+1}</td>
                                        <td>${routeleg_instance.title}</td>
                                        <td>${routeleg_instance.coordTrueTrackName()}</td>
                                        <td>${routeleg_instance.mapMeasureTrueTrackName()}</td>
                                        <td>${routeleg_instance.coordDistanceName()}</td>
                                        <td>${routeleg_instance.mapDistanceName()} (${routeleg_instance.mapMeasureDistanceName()})</td>
                                    </tr>
                                </g:each>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="6">${message(code:'fc.distance.total')} ${FcMath.DistanceStr(total_distance)}${message(code:'fc.mile')}</td>
                                </tr>
                            </tfoot>
                        </table>
                        <input type="hidden" name="id" value="${routeInstance?.id}"/>
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" tabIndex="1"/>
                        <g:if test="${!routeInstance.Used()}">
                            <g:actionSubmit action="createcoordroutes" value="${message(code:'fc.coordroute.add1')}"  tabIndex="2"/>
                            <g:actionSubmit action="createsecretcoordroutes" value="${message(code:'fc.coordroute.addsecret')}"  tabIndex="3"/>
                            <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}"  tabIndex="4"/>
                        </g:if>
                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}"  tabIndex="5"/>
                        <g:actionSubmit action="printcoordall" value="${message(code:'fc.printcoord.all')}"  tabIndex="6"/>
                        <g:actionSubmit action="printcoordtp" value="${message(code:'fc.printcoord.tp')}"  tabIndex="7"/>
                        <g:if test="${!routeInstance.Used()}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="8"/>
                        </g:if>
                        <g:actionSubmit action="copyroute" value="${message(code:'fc.copy')}"  tabIndex="9"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>