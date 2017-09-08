<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.a3=='true'}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else> 
                </g:if>
                <g:else>
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else> 
                </g:else>
                <g:if test="${params.landscape=='true'}">
                    margin-top: 8%;
                    margin-bottom: 8%;
                </g:if>
                <g:else>
                    margin-top: 10%;
                    margin-bottom: 10%;
                </g:else>
                @top-left {
                    font-family: Noto Sans;
                    content: "<g:if test="${params.results=='yes'}">${message(code:'fc.test.planningtask.withresults')}</g:if><g:else>${message(code:'fc.test.planningtask')}</g:else> ${testInstance.GetStartNum()}"
                }
                @top-right {
                    font-family: Noto Sans;
                    content: "${testInstance.GetViewPos()}"
                }
                @bottom-left {
                    font-family: Noto Sans;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <style type="text/css">${contestInstance.printStyle}</style>
        <title><g:if test="${params.results=='yes'}">${message(code:'fc.test.planningtask.withresults')}</g:if><g:else>${message(code:'fc.test.planningtask')}</g:else> ${testInstance.GetStartNum()}</title>
    </head>
    <body>
        <h2><g:if test="${params.results=='yes'}">${message(code:'fc.test.planningtask.withresults')}</g:if><g:else>${message(code:'fc.test.planningtask')}</g:else> ${testInstance.GetStartNum()}</h2>
        <h3>${testInstance?.task.printName()}</h3>
        <g:form>
            <g:crewTestPrintable t="${testInstance}"/>
            <br/>
            <table class="info">
                <tbody>
                    <tr class="wind">
                        <td class="title">${message(code:'fc.wind')}:</td>
                        <td class="value">
                         <g:if test="${testInstance.planningtesttask}">
                             <g:windtextprintable var="${testInstance.planningtesttask.wind}" />
                         </g:if> <g:else>
                             ${message(code:'fc.noassigned')}                                    
                         </g:else>
                        </td>
                    </tr>
                </tbody>
            </table>
            <g:if test="${TestLegPlanning.countByTest(testInstance)}" >
                <br/>
                <table class="planningtasklist">
                    <thead>
                        <tr class="valuename">
                            <th class="tpname">${message(code:'fc.tpname')}</th>
                            <th class="distance">${message(code:'fc.distance')}</th>
                            <th class="truetrack">${message(code:'fc.truetrack')}</th>
                            <th class="trueheading">${message(code:'fc.trueheading')}*</th>
                            <th class="groundspeed">${message(code:'fc.groundspeed')}</th>
                            <th class="legtime">${message(code:'fc.legtime')}*</th>
                        </tr>
                        <tr class="unit">
                            <th class="tpname"/>
                            <th class="distance">[${message(code:'fc.mile')}]</th>
                            <th class="truetrack">[${message(code:'fc.grad')}]</th>
                            <th class="trueheading">[${message(code:'fc.grad')}]</th>
                            <th class="groundspeed">[${message(code:'fc.knot')}]</th>
                            <g:if test="${params.results=='yes'}">
                                <th class="legtime">[${message(code:'fc.time.hminsec2')}]</th>
                            </g:if>
                            <g:else>
                                <th class="legtime">[${message(code:'fc.time.minsec')}]</th>
                            </g:else>
                        </tr>
                    </thead>
                    <tbody>
                         <tr class="value" id="${message(code:CoordType.SP.code)}">
                             <td class="tpname">${message(code:CoordType.SP.code)}</td>
                             <td class="distance">-</td>
                             <td class="truetrack">-</td>
                             <td class="trueheading">-</td>
                             <td class="groundspeed">-</td>
                             <td class="legtime">-</td>
                         </tr>
                        <g:each var="testlegplanning_instance" in="${TestLegPlanning.findAllByTest(testInstance,[sort:"id"])}">
                            <g:if test="${!testlegplanning_instance.test.IsPlanningTestDistanceMeasure()}">
                                <g:set var="test_distance" value="${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}" />
                            </g:if>
                            <g:if test="${!testlegplanning_instance.test.IsPlanningTestDirectionMeasure()}">
                                <g:set var="test_direction" value="${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}" />
                            </g:if>
                            <tr class="value" id="${testlegplanning_instance.coordTitle.titlePrintCode()}">
                                <g:if test="${!testlegplanning_instance.noPlanningTest}">
                                 <g:if test="${params.results=='yes'}">
                                        <td class="tpname">${testlegplanning_instance.coordTitle.titlePrintCode()}</td>
                                     <td class="distance">${FcMath.DistanceStr(testlegplanning_instance.planTestDistance)}</td>
                                     <td class="truetrack">${FcMath.GradStr(testlegplanning_instance.planTrueTrack)}</td>
                                     <td class="trueheading">${FcMath.GradStr(testlegplanning_instance.planTrueHeading)}</td>
                                     <td class="groundspeed">${FcMath.SpeedStr_Planning(testlegplanning_instance.planGroundSpeed)}</td>
                                     <td class="legtime">${testlegplanning_instance.planLegTimeStr()}</td>
                                 </g:if>
                                 <g:else>
                                        <td class="tpname">${testlegplanning_instance.coordTitle.titlePrintCode()}</td>
                                     <td class="distance">${test_distance}</td>
                                     <td class="truetrack">${test_direction}</td>
                                     <td class="trueheading"/>
                                     <td class="groundspeed"/>
                                     <td class="legtime"/>
                                 </g:else>
                              </g:if>
                              <g:else>
                                    <td class="tpname">${testlegplanning_instance.coordTitle.titlePrintCode()}</td>
                                    <td class="distance">-</td>
                                    <td class="truetrack">-</td>
                                    <td class="trueheading">-</td>
                                    <td class="groundspeed">-</td>
                                    <td class="legtime">-</td>
                              </g:else>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
                <br/>
                <table class="info">
                    <tbody>
                        <g:if test="${params.results!='yes'}">
                            <tr class="planninginfo">
                                <td class="title" colspan="3">${message(code:'fc.test.planning.taskinfo')}</td>
                            </tr>
                         <tr class="planningignore">
                             <td class="title">-</td>
                                <td class="separator"></td>
                             <td class="value">${message(code:'fc.test.planning.ignore')}</td>
                         </tr>
                     </g:if>
                        <tr class="planningevaluation">
                            <td class="title">*</td>
                            <td class="separator"></td>
                            <td class="value">${message(code:'fc.test.planning.evaluation')}</td>
                        </tr>
                    </tbody>
                </table>
            </g:if>
        </g:form>
    </body>
</html>