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
                                        <g:each var="c" in="${PlanningTestTask.findAllByRoute(routeInstance)}">
                                            <g:planningtesttask var="${c}" link="${createLink(controller:'planningTestTask',action:'show')}"/>
                                            <br/>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="detailtitle">${message(code:'fc.flighttest.list')}:</td>
                                    <td colspan="2">
                                        <g:each var="c" in="${FlightTest.findAllByRoute(routeInstance)}">
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
                                    <th class="table-head" colspan="9">${message(code:'fc.coordroute.list')}</th>
                                </tr>
                                <tr>
                                    <th>${message(code:'fc.number')}</th>
                                    <th>${message(code:'fc.title')}</th>
                                    <th>${message(code:'fc.aflos.checkpoint')}</th>
                                    <th>${message(code:'fc.latitude')}</th>
                                    <th>${message(code:'fc.longitude')}</th>
                                    <th>${message(code:'fc.altitude')}</th>
                                    <th>${message(code:'fc.gatewidth')}</th>
                                    <th>${message(code:'fc.truetrack.map.measure')}</th>
                                    <th>${message(code:'fc.distance.map')}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:set var="last_measuretruetrack" value="${new Integer(0)}"/>
                                <g:set var="last_measuredistance" value="${new Integer(0)}"/>
                                <g:each var="coordroute_instance" in="${routeInstance.coords}" status="i" >
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
                                        <td>${coordroute_instance.mark}</td>
                                        <td>${coordroute_instance.latName()}</td>
                                        <td>${coordroute_instance.lonName()}</td>
                                        <td>${coordroute_instance.altitude}${message(code:'fc.foot')}</td>
                                        <td>${coordroute_instance.gatewidth}${message(code:'fc.mile')}</td>
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
		                                <g:set var="last_measuretruetrack" value="${new Integer(0)}"/>
	                               	</g:else>
                                    <g:if test="${coordroute_instance.type == CoordType.SECRET}">
	    	                            <g:set var="last_measuredistance" value="${coordroute_instance.measureDistance}" />
	                               	</g:if>
	                               	<g:else>
	                               		<g:set var="last_measuredistance" value="${new Integer(0)}"/>
	                               	</g:else>
                                </g:each>
                            </tbody>
                        </table>
                        <table>
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
                                <g:each var="routeLegInstance" in="${routeInstance.routelegs}" status="i" >
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${i+1}</td>
                                        <td>${routeLegInstance.title}</td>
                                        <td>${routeLegInstance.coordTrueTrackName()}</td>
                                        <td>${routeLegInstance.mapMeasureTrueTrackName()}</td>
                                        <td>${routeLegInstance.coordDistanceName()}</td>
                                        <td>${routeLegInstance.mapDistanceName()} (${routeLegInstance.mapMeasureDistanceName()})</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <table>
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
                                <g:each var="routeLegInstance" in="${routeInstance.testlegs}" status="i" >
                                    <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                                        <td>${i+1}</td>
                                        <td>${routeLegInstance.title}</td>
                                        <td>${routeLegInstance.coordTrueTrackName()}</td>
                                        <td>${routeLegInstance.mapMeasureTrueTrackName()}</td>
                                        <td>${routeLegInstance.coordDistanceName()}</td>
                                        <td>${routeLegInstance.mapDistanceName()} (${routeLegInstance.mapMeasureDistanceName()})</td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <input type="hidden" name="id" value="${routeInstance?.id}"/>
                        <g:actionSubmit action="edit" value="${message(code:'fc.edit')}" tabIndex="1"/>
                        <g:actionSubmit action="createcoordroutes" value="${message(code:'fc.coordroute.add1')}"  tabIndex="2"/>
                        <g:actionSubmit action="createsecretcoordroutes" value="${message(code:'fc.coordroute.addsecret')}"  tabIndex="3"/>
                        <g:actionSubmit action="calculateroutelegs" value="${message(code:'fc.routeleg.calculate')}"  tabIndex="4"/>
                        <g:actionSubmit action="printroute" value="${message(code:'fc.print')}"  tabIndex="5"/>
                        <g:if test="${!PlanningTestTask.findByRoute(routeInstance) && !FlightTest.findByRoute(routeInstance)}">
                            <g:actionSubmit action="delete" value="${message(code:'fc.delete')}" onclick="return confirm('${message(code:'fc.areyousure')}');"  tabIndex="6"/>
                        </g:if>
                        <g:actionSubmit action="copyroute" value="${message(code:'fc.copy')}"  tabIndex="7"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>