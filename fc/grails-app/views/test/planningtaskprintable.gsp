<html>
    <head>
        <style type="text/css">
            @page {
                @top-center {
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-center {
                    content: "${message(code:'fc.program.printfoot.left')} - ${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.test.planningtask')} ${testInstance.GetStartNum()}</title>
    </head>
    <body>
        <div class="box">
            <div class="box boxborder" >
                <h2>${message(code:'fc.test.planningtask')} ${testInstance.GetStartNum()}</h2>
                <h3>${testInstance?.task.name()}</h3>
                <div class="block" id="forms" >
                    <g:form>
                        <table width="100%">
                            <tbody>
                                <tr>
                                    <td>${message(code:'fc.crew')}: ${testInstance.crew.name}</td>
			                    	<g:if test="${testInstance.crew.team}">
		                            	<td>${message(code:'fc.crew.team')}: ${testInstance.crew.team.name}</td>
	    		                    </g:if>
			                    	<g:if test="${testInstance.task.contest.resultClasses && testInstance.crew.resultclass}">
		                                <td>${message(code:'fc.crew.resultclass')}: ${testInstance.crew.resultclass.name}</td>
	    		                    </g:if>
                                </tr>
                                <tr>
                                    <td>${message(code:'fc.aircraft.registration')}:
                                        <g:if test="${testInstance.crew.aircraft}">
                                            ${testInstance.crew.aircraft.registration}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.aircraft.type')}: 
                                        <g:if test="${testInstance.crew.aircraft}">
		                                    ${testInstance.crew.aircraft.type}
                                        </g:if> <g:else>
                                            ${message(code:'fc.noassigned')}
                                        </g:else>
                                    </td>
                                    <td>${message(code:'fc.tas')}: ${fieldValue(bean:testInstance, field:'taskTAS')}${message(code:'fc.knot')}</td>
                                </tr>
                            </tbody>
                        </table>
                        <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
                            <br/>
                            <table width="100%" border="1" cellspacing="0" cellpadding="2">
                                <thead>
                                    <tr>
                                        <th>${message(code:'fc.title')}</th>
                                        <th>${message(code:'fc.distance')}</th>
                                        <th>${message(code:'fc.truetrack')}</th>
                                        <th>${message(code:'fc.trueheading')}</th>
                                        <th>${message(code:'fc.groundspeed')}</th>
                                        <th>${message(code:'fc.legtime')}</th>
                                    </tr>
                                    <tr>
                                        <th/>
                                        <th>[${message(code:'fc.mile')}]</th>
                                        <th>[${message(code:'fc.grad')}]</th>
                                        <th>[${message(code:'fc.grad')}]</th>
                                        <th>[${message(code:'fc.knot')}]</th>
                                        <th>[${message(code:'fc.time.minsec')}]</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <g:set var="legNo" value="${new Integer(0)}" />
                                    <g:set var="legNum" value="${TestLegPlanning.countByTest(testInstance)}" />
                                    <g:each var="testlegplanning_instance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                                        <g:set var="legNo" value="${legNo+1}" />
                                        <g:if test="${!testlegplanning_instance.test.IsPlanningTestDistanceMeasure()}">
                                            <g:set var="test_distance" value="${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}" />
                                        </g:if>
                                        <g:if test="${!testlegplanning_instance.test.IsPlanningTestDirectionMeasure()}">
                                            <g:set var="test_direction" value="${FcMath.GradStr(testlegplanning_instance.planTrueTrack)+message(code:'fc.grad')}" />
                                        </g:if>
                                        <tr>
                                            <g:if test="${params.results=='yes'}">
                                                <td>${legNo}</td>
                                                <td>${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}${message(code:'fc.mile')}</td>
                                                <td>${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.GradStr(testlegplanning_instance.planTrueHeading)}${message(code:'fc.grad')}</td>
                                                <td>${FcMath.SpeedStr(testlegplanning_instance.planGroundSpeed)}${message(code:'fc.knot')}</td>
                                                <td>${testlegplanning_instance.planLegTimeStr()}${message(code:'fc.time.h')}</td>
                                                <g:if test="${legNo==legNum}">
                                                    <td>${message(code:CoordType.FP.code)}</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${message(code:CoordType.TP.code)}${legNo}</td>
                                                </g:else>
                                            </g:if>
                                            <g:else>
                                                <g:if test="${legNo==legNum}">
                                                    <td>${message(code:CoordType.FP.code)}</td>
                                                </g:if>
                                                <g:else>
                                                    <td>${message(code:CoordType.TP.code)}${legNo}</td>
                                                </g:else>
                                                <td>${test_distance}</td>
                                                <td>${test_direction}</td>
                                                <td/>
                                                <td/>
                                                <td/>
                                            </g:else>
                                        </tr>
                                    </g:each>
                                </tbody>
                            </table>
                        </g:if>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>