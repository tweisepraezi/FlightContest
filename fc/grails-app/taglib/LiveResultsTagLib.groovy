class LiveResultsTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def liveResultLine = { attrs, body ->
        outln"""<tr class="even">"""
        outln"""    <td>${attrs.pos}</td>"""
        outln"""    <td>${attrs.crew.name}</td>"""
        if (attrs.contest.contestPrintAircraft) {
            if (attrs.crew.aircraft) {
                outln"""<td>${attrs.crew.aircraft.registration}</td>"""
            } else {
                outln"""<td>-</td>"""
            }
        }
        if (attrs.contest.contestPrintTeam) {
            if (attrs.crew.team) {
                outln"""<td>${attrs.crew.team.name}</td>"""
            } else {
                outln"""<td>-</td>"""
            }
        }
        if (attrs.contest.contestPrintClass) {
            if (attrs.crew.resultclass) {
                outln"""<td>${attrs.crew.resultclass.name}</td>"""
            } else {
                outln"""<td/>"""
            }
        }
        if (attrs.contest.contestPrintShortClass) {
            if (attrs.crew.resultclass) {
                outln"""<td>${attrs.crew.resultclass.shortName}</td>"""
            } else {
                outln"""<td/>"""
            }
        }
        if (attrs.contest.contestPrintTaskDetails || attrs.contest.contestPrintTaskTestDetails) {
            for (Task task_instance in attrs.contest.GetResultTasks(attrs.contest.contestTaskResults)) {
                int detail_num = 0
                boolean taskpenalties_written = false
                Test test_instance = Test.findByCrewAndTask(attrs.crew,task_instance)
                if (test_instance) {
                    if (task_instance in attrs.contest.GetTestDetailsTasks(attrs.contest.contestPrintTaskTestDetails)) {
                        if (attrs.contest.contestPlanningResults && task_instance.IsPlanningTestRun()) {
                            detail_num++
                        }
                        if (attrs.contest.contestFlightResults && task_instance.IsFlightTestRun()) {
                            detail_num++
                        }
                        if (attrs.contest.contestObservationResults && task_instance.IsObservationTestRun()) {
                            if (attrs.contest.contestPrintObservationDetails) {
                                if (task_instance.IsObservationTestTurnpointRun()) {
                                    detail_num++
                                }
                                if (task_instance.IsObservationTestEnroutePhotoRun()) {
                                    detail_num++
                                }
                                if (task_instance.IsObservationTestEnrouteCanvasRun()) {
                                    detail_num++
                                }
                            } else {
                                detail_num++
                            }
                        }
                        if (attrs.contest.contestLandingResults && task_instance.IsLandingTestRun()) {
                            if (attrs.contest.contestPrintLandingDetails) {
                                if (task_instance.IsLandingTest1Run()) {
                                    detail_num++
                                }
                                if (task_instance.IsLandingTest2Run()) {
                                    detail_num++
                                }
                                if (task_instance.IsLandingTest3Run()) {
                                    detail_num++
                                }
                                if (task_instance.IsLandingTest4Run()) {
                                    detail_num++
                                }
                            } else {
                                detail_num++
                            }
                        }
                        if (attrs.contest.contestSpecialResults && task_instance.IsSpecialTestRun()) {
                            detail_num++
                        }
                        if ((detail_num == 1) && task_instance.IsIncreaseEnabled()) {
                            detail_num++
                        }
                    } else {
                        detail_num++
                    }
                    if (task_instance in attrs.contest.GetTestDetailsTasks(attrs.contest.contestPrintTaskTestDetails)) {
                        if (attrs.contest.contestPlanningResults && task_instance.IsPlanningTestRun()) {
                            String s = ""
                            if (test_instance.IsPlanningTestRun()) {
                                s += "${test_instance.planningTestPenalties}"
                            } else {
                                s += "-"
                            }
                            if (detail_num == 1) {
                                if (attrs.task != task_instance) {
                                    s += " (${test_instance.taskPosition})"
                                }
                            }
                            outln"""<td>${s}</td>"""
                        }
                        if (attrs.contest.contestFlightResults && task_instance.IsFlightTestRun()) {
                            String s = ""
                            if (test_instance.IsFlightTestRun()) {
                                s += "${test_instance.flightTestPenalties}"
                            } else {
                                s += "-"
                            }
                            if (detail_num == 1) {
                                if (attrs.task != task_instance) {
                                    s += " (${test_instance.taskPosition})"
                                }
                            }
                            outln"""<td>${s}</td>"""
                        }
                        if (attrs.contest.contestObservationResults && task_instance.IsObservationTestRun()) {
                            boolean observation_detail_written = false
                            if (attrs.contest.contestPrintObservationDetails) {
                                if (task_instance.IsObservationTestTurnpointRun()) {
                                    String s = ""
                                    if (test_instance.IsObservationTestRun() && test_instance.IsObservationTestTurnpointRun()) {
                                        s += "${test_instance.observationTestTurnPointPhotoPenalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    observation_detail_written = true
                                }
                                if (task_instance.IsObservationTestEnroutePhotoRun()) {
                                    String s = ""
                                    if (test_instance.IsObservationTestRun() && test_instance.IsObservationTestEnroutePhotoRun()) {
                                        s += "${test_instance.observationTestRoutePhotoPenalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    observation_detail_written = true
                                }
                                if (task_instance.IsObservationTestEnrouteCanvasRun()) {
                                    String s = ""
                                    if (test_instance.IsObservationTestRun() && test_instance.IsObservationTestEnrouteCanvasRun()) {
                                        s += "${test_instance.observationTestGroundTargetPenalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    observation_detail_written = true
                                }
                            }
                            if (!observation_detail_written) {
                                String s = ""
                                if (test_instance.IsObservationTestRun()) {
                                    s += "${test_instance.observationTestPenalties}"
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != task_instance) {
                                        s += " (${test_instance.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                            }
                        }
                        if (attrs.contest.contestLandingResults && task_instance.IsLandingTestRun()) {
                            boolean landing_detail_written = false
                            if (attrs.contest.contestPrintLandingDetails) {
                                if (task_instance.IsLandingTest1Run()) {
                                    String s = ""
                                    if (test_instance.IsLandingTestRun() && test_instance.IsLandingTest1Run()) {
                                        s += "${test_instance.landingTest1Penalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    landing_detail_written = true
                                }
                                if (task_instance.IsLandingTest2Run()) {
                                    String s = ""
                                    if (test_instance.IsLandingTestRun() && test_instance.IsLandingTest2Run()) {
                                        s += "${test_instance.landingTest2Penalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    landing_detail_written = true
                                }
                                if (task_instance.IsLandingTest3Run()) {
                                    String s = ""
                                    if (test_instance.IsLandingTestRun() && test_instance.IsLandingTest3Run()) {
                                        s += "${test_instance.landingTest3Penalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    landing_detail_written = true
                                }
                                if (task_instance.IsLandingTest4Run()) {
                                    String s = ""
                                    if (test_instance.IsLandingTestRun() && test_instance.IsLandingTest4Run()) {
                                        s += "${test_instance.landingTest4Penalties}"
                                    } else {
                                        s += "-"
                                    }
                                    if (detail_num == 1) {
                                        if (attrs.task != task_instance) {
                                            s += " (${test_instance.taskPosition})"
                                        }
                                    }
                                    outln"""<td>${s}</td>"""
                                    landing_detail_written = true
                                }
                            }
                            if (!landing_detail_written) {
                                String s = ""
                                if (test_instance.IsLandingTestRun()) {
                                    s += "${test_instance.landingTestPenalties}"
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != task_instance) {
                                        s += " (${test_instance.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                            }
                        }
                        if (attrs.contest.contestSpecialResults && task_instance.IsSpecialTestRun()) {
                            String s = ""
                            if (test_instance.IsSpecialTestRun()) {
                                s += "${test_instance.specialTestPenalties}"
                            } else {
                                s += "-"
                            }
                            if (detail_num == 1) {
                                if (attrs.task != task_instance) {
                                    s += " (${test_instance.taskPosition})"
                                }
                            }
                            outln"""<td>${s}</td>"""
                        }
                    } else {
                        String s = test_instance.GetResultPenalties(attrs.contest.GetResultSettings())
                        if (test_instance.IsIncreaseEnabled()) {
                            s += """ ${message(code:'fc.crew.increaseenabled.short',args:[test_instance.crew.GetIncreaseFactor()])}"""
                        }
                        if (attrs.task != task_instance) {
                            s += """ (${test_instance.taskPosition})"""
                        }
                        outln"""<td>${s}</td>"""
                        taskpenalties_written = true
                    }
                    if (attrs.contest.contestPrintTaskDetails && !taskpenalties_written&& ((detail_num==0) || (detail_num>1) || task_instance.IsIncreaseEnabled())) {
                        String s = test_instance.GetResultPenalties(attrs.contest.GetResultSettings())
                        if (test_instance.IsIncreaseEnabled()) {
                            s += """ ${message(code:'fc.crew.increaseenabled.short',args:[test_instance.crew.GetIncreaseFactor()])}"""
                        }
                        if (attrs.task != task_instance) {
                            s += """ (${test_instance.taskPosition})"""
                        }
                        outln"""<td>${s}</td>"""
                    }
                }
            }
        }
        if (attrs.contest.liveShowSummary) {
            if (attrs.task) {
                outln"""    <td>${attrs.crew.contestPenalties} (${attrs.crew.contestPosition})</td>"""
            } else {
                outln"""    <td>${attrs.crew.contestPenalties}</td>"""
            }
        }
        outln"""</tr>"""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    private void outln(str)
    {
        out << """$str
"""
    }

}
