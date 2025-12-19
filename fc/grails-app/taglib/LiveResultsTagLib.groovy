import java.util.Map;

class LiveResultsTagLib
{
    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    
    // --------------------------------------------------------------------------------------------------------------------
    def liveResultLine = { attrs, body ->
        outln"""<tr class="even">"""
        outln"""    <td>${attrs.pos}</td>"""
        outln"""    <td>${attrs.crew.name}</td>"""
        if (attrs.livecontest.contestPrintAircraft) {
            if (attrs.crew.registration) {
                outln"""<td>${attrs.crew.registration}</td>"""
            } else {
                outln"""<td>-</td>"""
            }
        }
        if (attrs.livecontest.contestPrintTeam) {
            if (attrs.crew.teamName) {
                outln"""<td>${attrs.crew.teamName}</td>"""
            } else {
                outln"""<td>-</td>"""
            }
        }
        if (attrs.livecontest.contestPrintClass) {
            if (attrs.crew.className) {
                outln"""<td>${attrs.crew.className}</td>"""
            } else {
                outln"""<td/>"""
            }
        }
        if (attrs.livecontest.contestPrintShortClass) {
            if (attrs.crew.classShortName) {
                outln"""<td>${attrs.crew.classShortName}</td>"""
            } else {
                outln"""<td/>"""
            }
        }
        boolean summary_provisional = false
        if (attrs.livecontest.contestPrintTaskDetails || attrs.livecontest.contestPrintTaskTestDetails) {
            for (Map live_task in attrs.crew.tasks) {
                int detail_num = 0
                boolean taskpenalties_written = false
                if (live_task.id in attrs.livecontest.getTestDetailsTasksIDs) {
                    if (attrs.livecontest.contestPlanningResults && live_task.isTaskPlanningTest) {
                        detail_num++
                    }
                    if (attrs.livecontest.contestFlightResults && live_task.isTaskFlightTest) {
                        detail_num++
                    }
                    if (attrs.livecontest.contestObservationResults && live_task.isTaskObservationTest) {
                        if (attrs.livecontest.contestPrintObservationDetails) {
                            if (live_task.isTaskObservationTurnpointTest) {
                                detail_num++
                            }
                            if (live_task.isTaskObservationEnroutePhotoTest) {
                                detail_num++
                            }
                            if (live_task.isTaskObservationEnrouteCanvasTest) {
                                detail_num++
                            }
                        } else {
                            detail_num++
                        }
                    }
                    if (attrs.livecontest.contestLandingResults && live_task.isTaskLandingTest) {
                        if (attrs.livecontest.contestPrintLandingDetails) {
                            if (live_task.isTaskLanding1Test) {
                                detail_num++
                            }
                            if (live_task.isTaskLanding2Test) {
                                detail_num++
                            }
                            if (live_task.isTaskLanding3Test) {
                                detail_num++
                            }
                            if (live_task.isTaskLanding4Test) {
                                detail_num++
                            }
                            if (attrs.livecontest.getLandingResultsFactor) {
                                detail_num++
                            }
                        } else {
                            detail_num++
                        }
                    }
                    if (attrs.livecontest.contestSpecialResults && live_task.isTaskSpecialTest) {
                        detail_num++
                    }
                    if ((detail_num == 1) && live_task.isTaskIncreaseEnabled) {
                        detail_num++
                    }
                } else {
                    detail_num++
                }
                if (live_task.id in attrs.livecontest.getTestDetailsTasksIDs) {
                    if (attrs.livecontest.contestPlanningResults && live_task.isTaskPlanningTest) {
                        String s = ""
                        if (live_task.isPlanningTest) {
                            s += "${live_task.planningTestPenalties}"
                        } else {
                            s += "-"
                        }
                        if (detail_num == 1) {
                            if (attrs.task != live_task.id) {
                                s += " (${live_task.taskPosition})"
                            }
                        }
                        outln"""<td>${s}</td>"""
                    }
                    if (attrs.livecontest.contestFlightResults && live_task.isTaskFlightTest) {
                        String s = ""
                        if (live_task.isFlightTest) {
                            s += "${live_task.flightTestPenalties}"
                        } else {
                            s += "-"
                        }
                        if (detail_num == 1) {
                            if (attrs.task != live_task.id) {
                                s += " (${live_task.taskPosition})"
                            }
                        }
                        outln"""<td>${s}</td>"""
                    }
                    if (attrs.livecontest.contestObservationResults && live_task.isTaskObservationTest) {
                        boolean observation_detail_written = false
                        if (attrs.livecontest.contestPrintObservationDetails) {
                            if (live_task.isTaskObservationTurnpointTest) {
                                String s = ""
                                if (live_task.isObservationTest && live_task.isObservationTurnpointTest) {
                                    s += "${live_task.observationTestTurnPointPhotoPenalties}"
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                observation_detail_written = true
                            }
                            if (live_task.isTaskObservationEnroutePhotoTest) {
                                String s = ""
                                if (live_task.isObservationTest && live_task.isObservationEnroutePhotoTest) {
                                    s += "${live_task.observationTestRoutePhotoPenalties}"
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                observation_detail_written = true
                            }
                            if (live_task.isTaskObservationEnrouteCanvasTest) {
                                String s = ""
                                if (live_task.isObservationTest && live_task.isObservationEnrouteCanvasTest) {
                                    s += "${live_task.observationTestGroundTargetPenalties}"
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                observation_detail_written = true
                            }
                        }
                        if (!observation_detail_written) {
                            String s = ""
                            if (live_task.isObservationTest) {
                                s += "${live_task.observationTestPenalties}"
                            } else {
                                s += "-"
                            }
                            if (detail_num == 1) {
                                if (attrs.task != live_task.id) {
                                    s += " (${live_task.taskPosition})"
                                }
                            }
                            outln"""<td>${s}</td>"""
                        }
                    }
                    if (attrs.livecontest.contestLandingResults && live_task.isTaskLandingTest) {
                        boolean landing_detail_written = false
                        if (attrs.livecontest.contestPrintLandingDetails) {
                            if (live_task.isTaskLanding1Test) {
                                String s = ""
                                if (live_task.isLandingTest && live_task.isLanding1Test) {
                                    s += "${live_task.landingTest1Penalties}"
                                    if (live_task.landingTest1VideoCheck) {
                                        summary_provisional = true
                                        s += " [${message(code:'fc.landingresults.videocheck.short')}]"
                                    }
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                landing_detail_written = true
                            }
                            if (live_task.isTaskLanding2Test) {
                                String s = ""
                                if (live_task.isLandingTest && live_task.isLanding2Test) {
                                    s += "${live_task.landingTest2Penalties}"
                                    if (live_task.landingTest2VideoCheck) {
                                        summary_provisional = true
                                        s += " [${message(code:'fc.landingresults.videocheck.short')}]"
                                    }
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                landing_detail_written = true
                            }
                            if (live_task.isTaskLanding3Test) {
                                String s = ""
                                if (live_task.isLandingTest && live_task.isLanding3Test) {
                                    s += "${live_task.landingTest3Penalties}"
                                    if (live_task.landingTest3VideoCheck) {
                                        summary_provisional = true
                                        s += " [${message(code:'fc.landingresults.videocheck.short')}]"
                                    }
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                landing_detail_written = true
                            }
                            if (live_task.isTaskLanding4Test) {
                                String s = ""
                                if (live_task.isLandingTest && live_task.isLanding4Test) {
                                    s += "${live_task.landingTest4Penalties}"
                                    if (live_task.landingTest4VideoCheck) {
                                        summary_provisional = true
                                        s += " [${message(code:'fc.landingresults.videocheck.short')}]"
                                    }
                                } else {
                                    s += "-"
                                }
                                if (detail_num == 1) {
                                    if (attrs.task != live_task.id) {
                                        s += " (${live_task.taskPosition})"
                                    }
                                }
                                outln"""<td>${s}</td>"""
                                landing_detail_written = true
                            }
                        }
                        if (!landing_detail_written || attrs.livecontest.getLandingResultsFactor) {
                            String s = ""
                            if (live_task.isLandingTest) {
                                s += "${FcMath.GetLandingPenalties(attrs.livecontest.getLandingResultsFactor, live_task.landingTestPenalties)}"
                            } else {
                                s += "-"
                            }
                            if (detail_num == 1) {
                                if (attrs.task != live_task.id) {
                                    s += " (${live_task.taskPosition})"
                                }
                            }
                            outln"""<td>${s}</td>"""
                        }
                    }
                    if (attrs.livecontest.contestSpecialResults && live_task.isTaskSpecialTest) {
                        String s = ""
                        if (live_task.isSpecialTest) {
                            s += "${live_task.specialTestPenalties}"
                        } else {
                            s += "-"
                        }
                        if (detail_num == 1) {
                            if (attrs.task != live_task.id) {
                                s += " (${live_task.taskPosition})"
                            }
                        }
                        outln"""<td>${s}</td>"""
                    }
                } else {
                    String s = EvaluationService.GetResultPenalties(attrs.livecontest.getResultSettings, attrs.crew, live_task, attrs.livecontest.getLandingResultsFactor)
                    if (live_task.isIncreaseEnabled) {
                        s += """ ${message(code:'fc.crew.increaseenabled.short',args:[attrs.crew.increaseFactor])}"""
                    }
                    if (attrs.task != live_task.id) {
                        s += """ (${live_task.taskPosition})"""
                    }
                    outln"""<td>${s}</td>"""
                    taskpenalties_written = true
                }
                if (attrs.livecontest.contestPrintTaskDetails && !taskpenalties_written&& ((detail_num==0) || (detail_num>1) || live_task.isTaskIncreaseEnabled)) {
                    String s = EvaluationService.GetResultPenalties(attrs.livecontest.getResultSettings, attrs.crew, live_task, attrs.livecontest.getLandingResultsFactor)
                    if (live_task.isIncreaseEnabled) {
                        s += """ ${message(code:'fc.crew.increaseenabled.short',args:[attrs.crew.increaseFactor])}"""
                    }
                    if (attrs.task != live_task.id) {
                        s += """ (${live_task.taskPosition})"""
                    }
                    outln"""<td>${s}</td>"""
                }
            }
        }
        if (attrs.livecontest.liveShowSummary) {
            if (attrs.task) {
                outln"""    <td>${attrs.crew.contestPenalties} (${attrs.crew.contestPosition})</td>"""
            } else {
                String s = "<td>${attrs.crew.contestPenalties}"
                if (summary_provisional) {
                    s += " [${message(code:'fc.provisional')}]"
                }
                s += "</td>"
                outln s
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
