import java.util.List;
import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder

class EvaluationService
{
    def logService
    def messageSource
    
    //--------------------------------------------------------------------------
    Map calculatepositionsTask(Map params)
    {
        printstart "calculatepositionsTask"
        
        Task task_instance = Task.get(params.id) 
        if (!task_instance) {
            printerror ""
            return task
        }

        // calculate positions
        if (task_instance.contest.resultClasses) {
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(task_instance.contest,[sort:"id"])) {
                calculatepositions_task(task_instance, [resultclass_instance], null, false) // TODO: GetResultSettings(), ignoreProvisional
            }
        } else {
            calculatepositions_task(task_instance, null, null, false) // TODO: GetResultSettings(), ignoreProvisional
        }
        
        Map task = [:]
        task.instance = task_instance
        task.message = getMsg('fc.results.positionscalculated')  
        printdone task.message      
        return task
    }
    
    //--------------------------------------------------------------------------
    Map calculatecontestpositionsContest(Contest contestInstance, List contestClasses, List contestTasks, List contestTeams)
    {
        printstart "calculatecontestpositionsContest"
        
        Map contest = [:]
        
        if (contestClasses && contestInstance.resultClasses) {
            println "Set contest result classes with $contestClasses"
            contestInstance.contestClassResults = ""
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(contestInstance,[sort:"id"])) {
                for (Map contest_class in contestClasses) {
                    if (contest_class.instance == resultclass_instance) {
                        if (contestInstance.contestClassResults) {
                            contestInstance.contestClassResults += ","
                        }
                        contestInstance.contestClassResults += "resultclass_${resultclass_instance.id}"
                    }
                }
            }
            contestInstance.save()
        }
        
        if (contestTasks) {
            println "Set contest result tasks with $contestTasks"
            contestInstance.contestTaskResults = ""
            for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"idTitle"])) {
                for (Map contest_task in contestTasks) {
                    if (contest_task.instance == task_instance) {
                        if (contestInstance.contestTaskResults) {
                            contestInstance.contestTaskResults += ","
                        }
                        contestInstance.contestTaskResults += "task_${task_instance.id}"
                    }
                }
            }
            contestInstance.save()
        }
        
        if (contestTeams) {
            println "Set contest result teams with $contestTeams"
            contestInstance.contestTeamResults = ""
            for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
                for (Map contest_team in contestTeams) {
                    if (contest_team.instance == team_instance) {
                        if (contestInstance.contestTeamResults) {
                            contestInstance.contestTeamResults += ","
                        }
                        contestInstance.contestTeamResults += "team_${team_instance.id}"
                    }
                }
            }
            contestInstance.save()
        }
        
        if (contestInstance.resultClasses && !contestInstance.contestClassResults) {
            contest.message = getMsg('fc.contest.resultsettings.noclassselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        if (!contestInstance.contestTaskResults) {
            contest.message = getMsg('fc.contest.resultsettings.notaskselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        
        if (!contestInstance.contestTeamResults && Team.findByContest(contestInstance)) {
            contest.message = getMsg('fc.contest.resultsettings.noteamselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        
        println "Calculate contest results with classes: '${contestInstance.GetResultClassNames(contestInstance.contestClassResults)}'"
        println "Calculate contest results with tasks: '${contestInstance.GetResultTaskNames(contestInstance.contestTaskResults)}'"
        println "Calculate contest results with teams: '${contestInstance.GetResultTeamNames(contestInstance.contestTeamResults)}'"
        
        calculate_crew_penalties(contestInstance,null,contestInstance.GetResultTasks(contestInstance.contestTaskResults),contestInstance.GetResultTeams(contestInstance.contestTeamResults),contestInstance.GetResultSettings(),ResultFilter.Contest)
        
        // calculate positions
        calculatepositions_contest(contestInstance, null, contestInstance.GetResultTeams(contestInstance.contestTeamResults), null, false)
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map calculatelivepositionsContest(Contest contestInstance)
    {
        printstart "calculatelivepositionsContest"
        
        Map contest = [:]
        
        if (contestInstance.resultClasses && !contestInstance.contestClassResults) {
            contest.message = getMsg('fc.contest.resultsettings.noclassselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        if (!contestInstance.contestTaskResults) {
            contest.message = getMsg('fc.contest.resultsettings.notaskselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        
        if (!contestInstance.contestTeamResults && Team.findByContest(contestInstance)) {
            contest.message = getMsg('fc.contest.resultsettings.noteamselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        
        println "Calculate contest results with classes: '${contestInstance.GetResultClassNames(contestInstance.contestClassResults)}'"
        println "Calculate contest results with tasks: '${contestInstance.GetResultTaskNames(contestInstance.contestTaskResults)}'"
        println "Calculate contest results with teams: '${contestInstance.GetResultTeamNames(contestInstance.contestTeamResults)}'"
        
        List result_classes = contestInstance.GetResultClasses(contestInstance.contestClassResults)
        List result_tasks = contestInstance.GetResultTasks(contestInstance.contestTaskResults)
        List result_task_ids = contestInstance.GetResultTaskIDs(contestInstance.contestTaskResults)
        List team_settings = contestInstance.GetResultTeamIDs(contestInstance.contestTeamResults)
        
        Map result_settings = contestInstance.GetResultSettings()

        Map live_contest = [contestPrintAircraft:contestInstance.contestPrintAircraft,
                            contestPrintTeam:contestInstance.contestPrintTeam,
                            contestPrintClass:contestInstance.contestPrintClass,
                            contestPrintShortClass:contestInstance.contestPrintShortClass,
                            contestPrintTaskDetails:contestInstance.contestPrintTaskDetails,
                            contestPrintTaskTestDetails:contestInstance.contestPrintTaskTestDetails,
                            contestPrintObservationDetails:contestInstance.contestPrintObservationDetails,
                            contestPrintLandingDetails:contestInstance.contestPrintLandingDetails,
                            contestPrintSubtitle:contestInstance.contestPrintSubtitle,
                            
                            contestPlanningResults:contestInstance.contestPlanningResults,
                            contestFlightResults:contestInstance.contestFlightResults,
                            contestObservationResults:contestInstance.contestObservationResults,
                            contestLandingResults:contestInstance.contestLandingResults,
                            contestSpecialResults:contestInstance.contestSpecialResults,
                            
                            getTestDetailsTasksIDs:contestInstance.GetTestDetailsTasksIDs(contestInstance.contestPrintTaskTestDetails),
                            getResultSettings:result_settings,
                            getResultTitle:contestInstance.GetResultTitle(result_settings,false),
                            getLandingResultsFactor:contestInstance.contestLandingResultsFactor,
                            bestOfAnalysisTaskNum:contestInstance.bestOfAnalysisTaskNum,
                            
                            liveShowSummary:contestInstance.liveShowSummary,
                            liveRefreshSeconds:contestInstance.liveRefreshSeconds,
                            liveStylesheet:contestInstance.liveStylesheet,
                            livePositionCalculation:contestInstance.livePositionCalculation,
                            
                            printOrganizer:contestInstance.printOrganizer
                           ]
        
        List live_crews = []
        Map last_crew = [:]
        for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
            
            String aircraft_registration = ""
            if (crew_instance.aircraft) {
                aircraft_registration = crew_instance.aircraft.registration
            }
            
            String crew_class_name = ""
            String crew_class_shortname = ""
            if (crew_instance.resultclass) {
                crew_class_name = crew_instance.resultclass.name
                crew_class_shortname = crew_instance.resultclass.shortName
            }
            
            String team_name = ""
            Long team_id = null
            if (crew_instance.team) {
                team_name = crew_instance.team.name
                team_id = crew_instance.team.id
            }
            
            boolean active_class_crew = crew_instance.IsActiveCrew(ResultFilter.Contest)
                
            if (!crew_instance.disabled && !crew_instance.IsProvisionalCrew(result_settings) && active_class_crew && (!team_settings || (team_id in team_settings))) {
                List crew_tasks = []
                for (Task task_instance in result_tasks) {
                    Map task_values = GetTaskValues(task_instance, last_crew)
                    Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
                    Map test_values = GetTestValues(test_instance)
                    Map new_task = [id: task_values.id,
                                    isTaskPlanningTest:task_values.isTaskPlanningTest,
                                    isTaskFlightTest:task_values.isTaskFlightTest,
                                    isTaskObservationTest:task_values.isTaskObservationTest, 
                                    isTaskObservationTurnpointTest:task_values.isTaskObservationTurnpointTest,
                                    isTaskObservationEnroutePhotoTest:task_values.isTaskObservationEnroutePhotoTest, 
                                    isTaskObservationEnrouteCanvasTest:task_values.isTaskObservationEnrouteCanvasTest,
                                    isTaskLandingTest:task_values.isTaskLandingTest, 
                                    isTaskLanding1Test:task_values.isTaskLanding1Test,
                                    isTaskLanding2Test:task_values.isTaskLanding2Test, 
                                    isTaskLanding3Test:task_values.isTaskLanding3Test,
                                    isTaskLanding4Test:task_values.isTaskLanding4Test,
                                    isTaskSpecialTest:task_values.isTaskSpecialTest,
                                    isTaskIncreaseEnabled:task_values.isTaskIncreaseEnabled, 
                                    bestOfAnalysis:task_values.bestOfAnalysis,
                                    bestOfName:task_values.bestOfName,
                                    disabledCrew:test_instance.disabledCrew,
                                    isPlanningTest:test_values.isPlanningTest,
                                    isFlightTest:test_values.isFlightTest,
                                    isObservationTest:test_values.isObservationTest,
                                    isObservationTurnpointTest:test_values.isObservationTurnpointTest,
                                    isObservationEnroutePhotoTest:test_values.isObservationEnroutePhotoTest,
                                    isObservationEnrouteCanvasTest:test_values.isObservationEnrouteCanvasTest,
                                    isLandingTest:test_values.isLandingTest,
                                    isLanding1Test:test_values.isLanding1Test,
                                    isLanding2Test:test_values.isLanding2Test,
                                    isLanding3Test:test_values.isLanding3Test,
                                    isLanding4Test:test_values.isLanding4Test,
                                    isSpecialTest:test_values.isSpecialTest,
                                    isIncreaseEnabled:test_values.isIncreaseEnabled,                            
                                    planningTestPenalties:test_instance.planningTestPenalties,
                                    flightTestPenalties:test_instance.flightTestPenalties,
                                    observationTestTurnPointPhotoPenalties:test_instance.observationTestTurnPointPhotoPenalties,
                                    observationTestRoutePhotoPenalties:test_instance.observationTestRoutePhotoPenalties,
                                    observationTestGroundTargetPenalties:test_instance.observationTestGroundTargetPenalties,
                                    observationTestPenalties:test_instance.observationTestPenalties,
                                    landingTest1Penalties:test_instance.landingTest1Penalties,
                                    landingTest2Penalties:test_instance.landingTest2Penalties,
                                    landingTest3Penalties:test_instance.landingTest3Penalties,
                                    landingTest4Penalties:test_instance.landingTest4Penalties, 
                                    landingTestPenalties:test_instance.landingTestPenalties,
                                    specialTestPenalties:test_instance.specialTestPenalties,
                                    taskPenalties:test_instance.taskPenalties,
                                    taskPosition:0]
                    crew_tasks += new_task
                }
                Map new_crew = [startNum:crew_instance.startNum, name:crew_instance.name, registration:aircraft_registration, teamName:team_name, teamID:team_id, className:crew_class_name, classShortName:crew_class_shortname,
                                increaseFactor:crew_instance.GetIncreaseFactor(),
                                planningPenalties:0, flightPenalties:0, observationPenalties:0, landingPenalties:0, specialPenalties:0, contestPenalties:0, contestPosition:0, noContestPosition:false, contestEqualPosition:false, contestAddPosition:0, tasks: crew_tasks]
                live_crews += new_crew
                last_crew = new_crew
            }
        }
        
        for( Task result_task in result_tasks) {
            calculatelivepositions_task(result_task, result_settings, live_contest, live_crews)
        }
        
        calculatelive_crew_penalties(result_settings, result_task_ids, live_contest, live_crews)
        
        calculatelivepositions_contest(live_crews)
        
        contest.livecontest = live_contest
        contest.livecrews = live_crews
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }
    
    //--------------------------------------------------------------------------
    private Map GetTaskValues(Task taskInstance, Map lastCrew)
    {
        if (lastCrew) {
            Map live_task = null
            for (Map task in lastCrew.tasks) {
                if (task.id == taskInstance.id) {
                    live_task = task
                    break
                }
            }
            if (live_task) {
                Map new_task = [id: live_task.id,
                                isTaskPlanningTest:live_task.isTaskPlanningTest,
                                isTaskFlightTest:live_task.isTaskFlightTest,
                                isTaskObservationTest:live_task.isTaskObservationTest,
                                isTaskObservationTurnpointTest:live_task.isTaskObservationTurnpointTest,
                                isTaskObservationEnroutePhotoTest:live_task.isTaskObservationEnroutePhotoTest,
                                isTaskObservationEnrouteCanvasTest:live_task.isTaskObservationEnrouteCanvasTest,
                                isTaskLandingTest:live_task.isTaskLandingTest,
                                isTaskLanding1Test:live_task.isTaskLanding1Test,
                                isTaskLanding2Test:live_task.isTaskLanding2Test,
                                isTaskLanding3Test:live_task.isTaskLanding3Test,
                                isTaskLanding4Test:live_task.isTaskLanding4Test,
                                isTaskSpecialTest:live_task.isTaskSpecialTest,
                                isTaskIncreaseEnabled:live_task.isTaskIncreaseEnabled,
                                bestOfAnalysis:live_task.bestOfAnalysis,
                                bestOfName:live_task.bestOfName
                               ]
                return new_task
            }
        }
        Map new_task = [id: taskInstance.id,
                        isTaskPlanningTest:taskInstance.IsPlanningTestRun(),
                        isTaskFlightTest:taskInstance.IsFlightTestRun(),
                        isTaskObservationTest:taskInstance.IsObservationTestRun(),
                        isTaskObservationTurnpointTest:taskInstance.IsObservationTestTurnpointRun(),
                        isTaskObservationEnroutePhotoTest:taskInstance.IsObservationTestEnroutePhotoRun(),
                        isTaskObservationEnrouteCanvasTest:taskInstance.IsObservationTestEnrouteCanvasRun(),
                        isTaskLandingTest:taskInstance.IsLandingTestRun(),
                        isTaskLanding1Test:taskInstance.IsLandingTest1Run(),
                        isTaskLanding2Test:taskInstance.IsLandingTest2Run(),
                        isTaskLanding3Test:taskInstance.IsLandingTest3Run(),
                        isTaskLanding4Test:taskInstance.IsLandingTest4Run(),
                        isTaskSpecialTest:taskInstance.IsSpecialTestRun(),
                        isTaskIncreaseEnabled:taskInstance.IsIncreaseEnabled(),
                        bestOfAnalysis:taskInstance.bestOfAnalysis,
                        bestOfName:taskInstance.bestOfName()
                       ]
        return new_task
    }
    
    //--------------------------------------------------------------------------
    private Map GetTestValues(Test testInstance)
    {
        Map new_task = [isPlanningTest:testInstance.IsPlanningTestRun(),
                        isFlightTest:testInstance.IsFlightTestRun(),
                        isObservationTest:testInstance.IsObservationTestRun(),
                        isObservationTurnpointTest:testInstance.IsObservationTestTurnpointRun(),
                        isObservationEnroutePhotoTest:testInstance.IsObservationTestEnroutePhotoRun(),
                        isObservationEnrouteCanvasTest:testInstance.IsObservationTestEnrouteCanvasRun(),
                        isLandingTest:testInstance.IsLandingTestRun(),
                        isLanding1Test:testInstance.IsLandingTest1Run(),
                        isLanding2Test:testInstance.IsLandingTest2Run(),
                        isLanding3Test:testInstance.IsLandingTest3Run(),
                        isLanding4Test:testInstance.IsLandingTest4Run(),
                        isSpecialTest:testInstance.IsSpecialTestRun(),
                        isIncreaseEnabled:testInstance.IsIncreaseEnabled()
                       ]
        return new_task
    }
    
    //--------------------------------------------------------------------------
    Map calculatepositionsResultClass(ResultClass resultclassInstance, List contestTasks, List contestTeams)
    {
        printstart "calculatepositionsResultClass $resultclassInstance.name"
        
        Map resultclass = [:]
        
        if (contestTasks) {
            println "Set class result tasks with $contestTasks"
            resultclassInstance.contestTaskResults = ""
            for (Task task_instance in Task.findAllByContest(resultclassInstance.contest,[sort:"idTitle"])) {
                for (Map contest_task in contestTasks) {
                    if (contest_task.instance == task_instance) {
                        if (resultclassInstance.contestTaskResults) {
                            resultclassInstance.contestTaskResults += ","
                        }
                        resultclassInstance.contestTaskResults += "task_${task_instance.id}"
                    }
                }
            }
            resultclassInstance.save()
        }
        
        if (contestTeams) {
            println "Set class result teams with $contestTeams"
            resultclassInstance.contestTeamResults = ""
            for (Team team_instance in Team.findAllByContest(resultclassInstance.contest,[sort:"id"])) {
                for (Map contest_team in contestTeams) {
                    if (contest_team.instance == team_instance) {
                        if (resultclassInstance.contestTeamResults) {
                            resultclassInstance.contestTeamResults += ","
                        }
                        resultclassInstance.contestTeamResults += "team_${team_instance.id}"
                    }
                }
            }
            resultclassInstance.save()
        }
        
        if (!resultclassInstance.contestTaskResults) {
            resultclass.message = getMsg('fc.resultclass.resultsettings.notaskselected')
            resultclass.error = true
            printerror resultclass.message
            return resultclass
        }
        
        if (!resultclassInstance.contestTeamResults && Team.findByContest(resultclassInstance.contest)) {
            resultclass.message = getMsg('fc.resultclass.resultsettings.noteamselected')
            resultclass.error = true
            printerror resultclass.message
            return resultclass
        }
        
        println "Calculate class results with tasks: '${resultclassInstance.contest.GetResultTaskNames(resultclassInstance.contestTaskResults)}'"
        println "Calculate class results with teams: '${resultclassInstance.contest.GetResultTeamNames(resultclassInstance.contestTeamResults)}'"
        
        calculate_crew_penalties(resultclassInstance.contest,resultclassInstance,resultclassInstance.contest.GetResultTasks(resultclassInstance.contestTaskResults),resultclassInstance.contest.GetResultTeams(resultclassInstance.contestTeamResults),resultclassInstance.GetClassResultSettings(),ResultFilter.Contest)
        
        // calculate positions
        calculatepositions_contest(resultclassInstance.contest, resultclassInstance, resultclassInstance.contest.GetResultTeams(resultclassInstance.contestTeamResults), null, false)
        
        resultclass.message = getMsg('fc.results.positionscalculated')

        printdone resultclass.message      
        return resultclass
    }
    
    //--------------------------------------------------------------------------
    Map calculateteampositionsContest(Contest contestInstance, List teamClasses, List teamTasks)
    {
        printstart "calculateteampositionsContest"
        
        Map contest = [:]
        
        if (teamClasses && contestInstance.resultClasses) {
            println "Set team result classes with $teamClasses"
            contestInstance.teamClassResults = ""
            for (ResultClass resultclass_instance in ResultClass.findAllByContest(contestInstance,[sort:"id"])) {
                for (Map team_class in teamClasses) {
                    if (team_class.instance == resultclass_instance) {
                        if (contestInstance.teamClassResults) {
                            contestInstance.teamClassResults += ","
                        }
                        contestInstance.teamClassResults += "resultclass_${resultclass_instance.id}"
                    }
                }
            }
            contestInstance.save()
        }
        
        if (teamTasks) {
            println "Set team result tasks with $teamTasks"
            contestInstance.teamTaskResults = ""
            for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"idTitle"])) {
                for (Map team_task in teamTasks) {
                    if (team_task.instance == task_instance) {
                        if (contestInstance.teamTaskResults) {
                            contestInstance.teamTaskResults += ","
                        }
                        contestInstance.teamTaskResults += "task_${task_instance.id}"
                    }
                }
            }
            contestInstance.save()
        }
        
        if (contestInstance.resultClasses && !contestInstance.teamClassResults) {
            contest.message = getMsg('fc.contest.teamresultsettings.noclassselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        if (!contestInstance.teamTaskResults) {
            contest.message = getMsg('fc.contest.teamresultsettings.notaskselected')
            contest.error = true
            printerror contest.message
            return contest
        }
        
        println "Calculate team results with classes: '${contestInstance.GetResultClassNames(contestInstance.teamClassResults)}'"
        println "Calculate team results with tasks: '${contestInstance.GetResultTaskNames(contestInstance.teamTaskResults)}'"
        
        Map team_result_settings = contestInstance.GetTeamResultSettings()
        calculate_crew_penalties(contestInstance,null,contestInstance.GetResultTasks(contestInstance.teamTaskResults),null,team_result_settings,ResultFilter.Team)

        // calculate team penalties
        for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
            printstart team_instance.name
            team_instance.contestPenalties = 0
            int crew_num = 0
            int team_penalties = 0
            for (Crew crew_instance in Crew.findAllByTeamAndDisabledAndDisabledTeam(team_instance,false,false,[sort:"teamPenalties"])) {
                if (crew_instance.IsActiveCrew(ResultFilter.Team) && (crew_instance.teamPenalties != -1)) {
                    crew_num++
                    if (crew_num > contestInstance.teamCrewNum) {
                        break
                    }
                    team_penalties += crew_instance.teamPenalties
                    println "$crew_instance.name (${crew_instance.resultclass?.name}): $crew_instance.teamPenalties"
                }
            }
            if (crew_num >= contestInstance.teamCrewNum) {
                team_instance.contestPenalties = team_penalties
                team_instance.save()
                printdone "$team_instance.contestPenalties"
            } else {
                team_instance.contestPenalties = -1
                team_instance.save()
                printdone "$team_instance.contestPenalties (no team)"
            }
        }
        
        // calculate team positions
        calculatepositions_team(contestInstance)
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map addpositionResultClass(ResultClass resultclassInstance, long crewID)
    {
        Crew crew_instance = Crew.get(crewID)
        
        printstart "addpositionResultClass $crew_instance.name"
        
        Map contest = [:]
        
        crew_instance.classAddPosition++
        crew_instance.classPosition++
        crew_instance.save()
        
        set_resultclass_equalposition(resultclassInstance)
        
        println "New position: $crew_instance.contestPosition"
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }   
    
    //--------------------------------------------------------------------------
    Map subpositionResultClass(ResultClass resultclassInstance, long crewID)
    {
        Crew crew_instance = Crew.get(crewID)
        
        printstart "subpositionResultClass $crew_instance.name"
        
        Map contest = [:]
        
        crew_instance.classAddPosition--
        crew_instance.classPosition--
        crew_instance.save()
        
        set_resultclass_equalposition(resultclassInstance)
        
        println "New position: $crew_instance.contestPosition"
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }   
    
    //--------------------------------------------------------------------------
    Map addcontestpositionContest(Contest contestInstance, long crewID)
    {
        Crew crew_instance = Crew.get(crewID)
        
        printstart "addcontestpositionContest $crew_instance.name"
        
        Map contest = [:]
        
        crew_instance.contestAddPosition++
        crew_instance.contestPosition++
        crew_instance.save()
        
        set_contest_equalposition(contestInstance)
        
        println "New position: $crew_instance.contestPosition"
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }   
    
    //--------------------------------------------------------------------------
    Map subcontestpositionContest(Contest contestInstance, long crewID)
    {
        Crew crew_instance = Crew.get(crewID)
        
        printstart "subcontestpositionContest $crew_instance.name"
        
        Map contest = [:]
        
        crew_instance.contestAddPosition--
        crew_instance.contestPosition--
        crew_instance.save()
        
        set_contest_equalposition(contestInstance)
        
        println "New position: $crew_instance.contestPosition"
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }   
    
    //--------------------------------------------------------------------------
    Map addteampositionContest(Contest contestInstance, long teamID)
    {
        Team team_instance = Team.get(teamID)
        
        printstart "addteampositionContest $team_instance.name"
        
        Map contest = [:]
        
        team_instance.contestAddPosition++
        team_instance.contestPosition++
        team_instance.save()
        
        set_team_equalposition(contestInstance)
        
        println "New position: $team_instance.contestPosition"
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }   
    
    //--------------------------------------------------------------------------
    Map subteampositionContest(Contest contestInstance, long teamID)
    {
        Team team_instance = Team.get(teamID)
        
        printstart "addteampositionContest $team_instance.name"
        
        Map contest = [:]
        
        team_instance.contestAddPosition--
        team_instance.contestPosition--
        team_instance.save()
        
        set_team_equalposition(contestInstance)
        
        println "New position: $team_instance.contestPosition"
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }   
    
    //--------------------------------------------------------------------------
    Map runcalculatepositionsTask(Map task)
    {
        printstart "runcalculatepositionsTask"
        Map p = [:]
        p.id = task.instance.id 
        Map ret = calculatepositionsTask(p)
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculatecontestpositionsContest(Map contest, List contestClasses, List contestTasks, List contestTeams)
    {
        printstart "runcalculatecontestpositionsContest [$contestClasses] [$contestTasks] [$contestTeams]"
        Map ret = calculatecontestpositionsContest(contest.instance,contestClasses,contestTasks,contestTeams)
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculatepositionsResultClass(Map resultclass, List contestTasks, List contestTeams)
    {
        printstart "runcalculatepositionsResultClass"
        Map ret = calculatepositionsResultClass(resultclass.instance,contestTasks,contestTeams)
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    Map runcalculateteampositionsContest(Map contest, List teamClasses, List teamTasks)
    {
        printstart "runcalculateteampositionsContest [$teamClasses] [$teamTasks]"
        Map ret = calculateteampositionsContest(contest.instance,teamClasses,teamTasks)
        printdone ret
        return ret
    }

    //--------------------------------------------------------------------------
    private void calculatepositions_task(Task taskInstance, List resultclassInstances, Map resultSettings, boolean ignoreProvisional)
    {
        printstart "calculatepositions_task '${taskInstance.name()}' '${resultSettings}'"
        
        int act_penalty = -1
        int max_position = Test.countByTask(taskInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
                if (!test_instance.disabledCrew && !test_instance.crew.disabled) {
                    if (!resultclassInstances || (test_instance.crew.resultclass in resultclassInstances)) {
                        if (!ignoreProvisional || !test_instance.crew.IsProvisionalCrew(resultSettings)) {
                            int task_penalties = test_instance.taskPenalties
                            if (resultSettings) {
                                task_penalties = test_instance.GetResultPenalties(resultSettings)
                            }
                            if (task_penalties > act_penalty) {
                                if (task_penalties < min_penalty) {
                                    min_penalty = task_penalties 
                                }
                            }
                        }
                    }
                }
            }
            act_penalty = min_penalty 
            
            // set position
            int set_position = -1
            for (Test test_instance in Test.findAllByTask(taskInstance,[sort:"id"])) {
                if (test_instance.disabledCrew || test_instance.crew.disabled) {
                    test_instance.taskPosition = 0
                    test_instance.save()
                } else if (resultclassInstances && !test_instance.crew.resultclass) {
                    test_instance.taskPosition = 0
                    test_instance.save()
                } else if (ignoreProvisional && test_instance.crew.IsProvisionalCrew(resultSettings)) {
                    test_instance.taskPosition = 0
                    test_instance.save()
                } else if (!resultclassInstances || (test_instance.crew.resultclass in resultclassInstances)) {
                    int task_penalties = test_instance.taskPenalties
                    if (resultSettings) {
                        task_penalties = test_instance.GetResultPenalties(resultSettings)
                    }
                    if (task_penalties == act_penalty) {
                        test_instance.taskPosition = act_position
                        test_instance.save()
                        println "$test_instance.crew.name: taskPosition $test_instance.taskPosition"
                        set_position++
                    }
                }
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void calculatelivepositions_task(Task taskInstance, Map resultSettings, Map liveContest, List liveCrews)
    {
        printstart "calculatelivepositions_task '${taskInstance.name()}' '${resultSettings}'"
        
        int act_penalty = -1
        int max_position = liveCrews.size()
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Map live_crew in liveCrews) {
                Map live_task = null
                for (Map task in live_crew.tasks) {
                    if (task.id == taskInstance.id) {
                        live_task = task
                        break
                    }
                }
                if (live_task) {
                    if (!live_task.disabledCrew) {
                        int task_penalties = live_task.taskPenalties
                        if (resultSettings) {
                            task_penalties = EvaluationService.GetResultPenalties(resultSettings, live_crew, live_task, liveContest.getLandingResultsFactor)
                        }
                        if (task_penalties > act_penalty) {
                            if (task_penalties < min_penalty) {
                                min_penalty = task_penalties 
                            }
                        }
                    }
                }
            }
            act_penalty = min_penalty 
            
            // set position
            int set_position = -1
            for (Map live_crew in liveCrews) {
                Map live_task = null
                for (Map task in live_crew.tasks) {
                    if (task.id == taskInstance.id) {
                        live_task = task
                        break
                    }
                }
                if (live_task) {
                    if (live_task.disabledCrew) {
                        live_task.taskPosition = 0
                    } else {
                        int task_penalties = live_task.taskPenalties
                        if (resultSettings) {
                            task_penalties = EvaluationService.GetResultPenalties(resultSettings, live_crew, live_task, liveContest.getLandingResultsFactor)
                        }
                        if (task_penalties == act_penalty) {
                            live_task.taskPosition = act_position
                            println "$live_crew.name: Live taskPosition $live_task.taskPosition"
                            set_position++
                        }
                    }
                }
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        printdone ""
    }
    
    // --------------------------------------------------------------------------------------------------------------------
    static int GetResultPenalties(Map resultSettings, Map crew, Map task, BigDecimal landingResultsFactor)
    {
        int penalties = 0
        if (resultSettings["Planning"]) {
            if (task.isPlanningTest) {
                penalties += task.planningTestPenalties
            }
        }
        if (resultSettings["Flight"]) {
            if (task.isFlightTest) {
                penalties += task.flightTestPenalties
            }
        }
        if (resultSettings["Observation"]) {
            if (task.isObservationTest) {
                penalties += task.observationTestPenalties
            }
        }
        if (resultSettings["Landing"]) {
			if (task.isLandingTest1 || task.isLandingTest2 || task.isLandingTest3 || task.isLandingTest4) {
				if (task.isLandingTest1) {
					penalties += FcMath.GetLandingPenalties(landingResultsFactor, task.landingTest1Penalties)
				}
				if (task.isLandingTest2) {
					penalties += FcMath.GetLandingPenalties(landingResultsFactor, task.landingTest2Penalties)
				}
				if (task.isLandingTest3) {
					penalties += FcMath.GetLandingPenalties(landingResultsFactor, task.landingTest3Penalties)
				}
				if (task.isLandingTest4) {
					penalties += FcMath.GetLandingPenalties(landingResultsFactor, task.landingTest4Penalties)
				}
			} else {
				if (task.isLandingTest) {
					penalties += FcMath.GetLandingPenalties(landingResultsFactor, task.landingTestPenalties)
				}
			}
        }
        if (resultSettings["Special"]) {
            if (task.isSpecialTest) {
                penalties += task.specialTestPenalties
            }
        }
        if (task.isIncreaseEnabled) {
            penalties += crew.increaseFactor * penalties / 100
        }
        return penalties
    }
    
    //--------------------------------------------------------------------------
    private void calculatepositions_contest(Contest contestInstance, ResultClass resultclassInstance, List teamSettings, Map resultSettings, boolean ignoreProvisional)
    {
        printstart "calculatepositions_contest [Class:${resultclassInstance?.name}, Teams:${teamSettings}, resultSettings:${resultSettings}]"
        
        int act_penalty = -1
        int max_position = Crew.countByContest(contestInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
                if (!crew_instance.disabledContest && !crew_instance.disabled && (crew_instance.contestPenalties != -1)) {
                    if (!teamSettings || (crew_instance.team in teamSettings)) {
                        if (!ignoreProvisional || !crew_instance.IsProvisionalCrew(resultSettings)) {
                            if (resultclassInstance) {
                                if (crew_instance.resultclass) {
                                    if (resultclassInstance.id == crew_instance.resultclass.id) {
                                        if (crew_instance.contestPenalties > act_penalty) {
                                            if (crew_instance.contestPenalties < min_penalty) {
                                                min_penalty = crew_instance.contestPenalties 
                                            }
                                        }
                                    }
                                }
                            } else if (contestInstance.resultClasses) {
                                if (crew_instance.IsActiveCrew(ResultFilter.Contest)) {
                                    if (crew_instance.contestPenalties > act_penalty) {
                                        if (crew_instance.contestPenalties < min_penalty) {
                                            min_penalty = crew_instance.contestPenalties 
                                        }
                                    }
                                }
                            } else {
                                if (crew_instance.contestPenalties > act_penalty) {
                                    if (crew_instance.contestPenalties < min_penalty) {
                                        min_penalty = crew_instance.contestPenalties 
                                    }
                                }
                            }
                        }
                    }
                }
            }
            act_penalty = min_penalty 

            // set position
            int set_position = -1
            for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
                if (crew_instance.disabledContest || crew_instance.disabled || (crew_instance.contestPenalties == -1)) {
                    if (resultclassInstance) {
                        crew_instance.classPosition = 0
                        crew_instance.noClassPosition = true
                        crew_instance.classEqualPosition = false
                        crew_instance.classAddPosition = 0
                    } else {
                        crew_instance.contestPosition = 0
                        crew_instance.noContestPosition = true
                        crew_instance.contestEqualPosition = false
                        crew_instance.contestAddPosition = 0
                    }
                    crew_instance.save()
                } else if (teamSettings && !(crew_instance.team in teamSettings)) {
                    if (resultclassInstance) {
                        crew_instance.classPosition = 0
                        crew_instance.noClassPosition = true
                        crew_instance.classEqualPosition = false
                        crew_instance.classAddPosition = 0
                        println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): noClassPosition = true, classPosition = 0 (no team)"
                    } else {
                        crew_instance.contestPosition = 0
                        crew_instance.noContestPosition = true
                        crew_instance.contestEqualPosition = false
                        crew_instance.contestAddPosition = 0
                        println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): noContestPosition = true, contestPosition = 0 (no team)"
                    }
                    crew_instance.save()
                } else if (resultclassInstance && !crew_instance.resultclass) {
                    if (resultclassInstance) {
                        crew_instance.classPosition = 0
                        crew_instance.noClassPosition = false
                        crew_instance.classEqualPosition = false
                        crew_instance.classAddPosition = 0
                    } else {
                        crew_instance.contestPosition = 0
                        crew_instance.noContestPosition = false
                        crew_instance.contestEqualPosition = false
                        crew_instance.contestAddPosition = 0
                    }
                    crew_instance.save()
                } else if (ignoreProvisional && crew_instance.IsProvisionalCrew(resultSettings)) {
                    if (resultclassInstance) {
                        crew_instance.classPosition = 0
                        crew_instance.noClassPosition = false
                        crew_instance.classEqualPosition = false
                        crew_instance.classAddPosition = 0
                    } else {
                        crew_instance.contestPosition = 0
                        crew_instance.noContestPosition = false
                        crew_instance.contestEqualPosition = false
                        crew_instance.contestAddPosition = 0
                    }
                    crew_instance.save()
                } else if (contestInstance.resultClasses && !resultclassInstance && !crew_instance.IsActiveCrew(ResultFilter.Contest)) {
                    crew_instance.contestPosition = 0
                    if (!crew_instance.noContestPosition) {
                        println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): no contest position"
                    }
                    crew_instance.noContestPosition = true
                    crew_instance.contestEqualPosition = false
                    crew_instance.contestAddPosition = 0
                    crew_instance.save()
                } else if (!resultclassInstance || (resultclassInstance.id == crew_instance.resultclass.id)) {
                    if (crew_instance.contestPenalties == act_penalty) {
                        if (resultclassInstance) {
                            crew_instance.classPosition = act_position
                            crew_instance.noClassPosition = false
                            crew_instance.classEqualPosition = false
                            crew_instance.classAddPosition = 0
                            println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): classPosition = $act_position ($crew_instance.contestPenalties Punkte)"
                        } else if (contestInstance.resultClasses) {
                            crew_instance.contestPosition = act_position
                            crew_instance.noContestPosition = false
                            crew_instance.contestEqualPosition = false
                            crew_instance.contestAddPosition = 0
                            println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): contestPosition = $act_position ($crew_instance.contestPenalties Punkte)"
                        } else {
                            crew_instance.contestPosition = act_position
                            crew_instance.noContestPosition = false
                            crew_instance.contestEqualPosition = false
                            crew_instance.contestAddPosition = 0
                            println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): contestPosition = $act_position ($crew_instance.contestPenalties Punkte)"
                        }
                        crew_instance.save()
                        set_position++
                    }
                }
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        // set contestEqualPosition/classEqualPosition
        if (resultclassInstance) {
            set_resultclass_equalposition(resultclassInstance)
        } else {
            set_contest_equalposition(contestInstance)
        }
        
        printdone ""
    }

    //--------------------------------------------------------------------------
    private void calculatelivepositions_contest(List liveCrews)
    {
        
        printstart "calculatelivepositions_contest"
        
        int act_penalty = -1
        int max_position = liveCrews.size()
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Map live_crew in liveCrews) {
                if (live_crew.contestPenalties != -1) {
                    if (live_crew.contestPenalties > act_penalty) {
                        if (live_crew.contestPenalties < min_penalty) {
                            min_penalty = live_crew.contestPenalties
                        }
                    }
                }
            }
            act_penalty = min_penalty 

            // set position
            int set_position = -1
            for (Map live_crew in liveCrews) {
                if (live_crew.contestPenalties == -1) {
                    live_crew.contestPosition = 0
                    live_crew.noContestPosition = true
                    live_crew.contestEqualPosition = false
                    live_crew.contestAddPosition = 0
                } else {
                    if (live_crew.contestPenalties == act_penalty) {
                        live_crew.contestPosition = act_position
                        live_crew.noContestPosition = false
                        live_crew.contestEqualPosition = false
                        live_crew.contestAddPosition = 0
                        println "$live_crew.name: Live contestPosition = $act_position ($live_crew.contestPenalties Punkte)"
                        set_position++
                    }
                }
            }
            
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        // set contestEqualPosition/classEqualPosition
        sort_contestposition_set_contest_equalposition(liveCrews)
        
        printdone ""
    }

    //--------------------------------------------------------------------------
    private void set_resultclass_equalposition(ResultClass resultclassInstance)
    {
        Crew last_crew_instance = null
        for (Crew crew_instance in Crew.findAllByResultclassAndClassPositionNotEqual(resultclassInstance,0,[sort:"classPosition"])) {
            if (last_crew_instance) {
                if (last_crew_instance.classPosition == crew_instance.classPosition) {
                    last_crew_instance.classEqualPosition = true
                    last_crew_instance.save()
                    crew_instance.classEqualPosition = true
                    crew_instance.save()
                    println "Set classEqualPosition for position $crew_instance.classPosition: $last_crew_instance.name / $crew_instance.name"
                } else {
                    crew_instance.classEqualPosition = false
                    crew_instance.save()
                }
            } else {
                crew_instance.classEqualPosition = false
                crew_instance.save()
            }
            last_crew_instance = crew_instance
        }

    }
    
    //--------------------------------------------------------------------------
    private void set_contest_equalposition(Contest contestInstance)
    {
        Crew last_crew_instance = null
        for (Crew crew_instance in Crew.findAllByContestAndContestPositionNotEqual(contestInstance,0,[sort:"contestPosition"])) {
            if (last_crew_instance) {
                if (last_crew_instance.contestPosition == crew_instance.contestPosition) {
                    last_crew_instance.contestEqualPosition = true
                    last_crew_instance.save()
                    crew_instance.contestEqualPosition = true
                    crew_instance.save()
                    println "Set contestEqualPosition for position $crew_instance.contestPosition: $last_crew_instance.name / $crew_instance.name"
                } else {
                    crew_instance.contestEqualPosition = false
                    crew_instance.save()
                }
            } else {
                crew_instance.contestEqualPosition = false
                crew_instance.save()
            }
            last_crew_instance = crew_instance
        }

    }
    
    //--------------------------------------------------------------------------
    private void sort_contestposition_set_contest_equalposition(List liveCrews)
    {
        // sort contest position
        liveCrews.sort { p1, p2 ->
            p1.contestPosition.compareTo(p2.contestPosition)
        }
        
        // set contest equalposition
        Map last_live_crew = null
        for (Map live_crew in liveCrews) {
            if (live_crew.contestPosition != 0) {
                if (last_live_crew) {
                    if (last_live_crew.contestPosition == live_crew.contestPosition) {
                        last_live_crew.contestEqualPosition = true
                        live_crew.contestEqualPosition = true
                        println "Set Live contestEqualPosition for position $live_crew.contestPosition: $last_live_crew.name / $live_crew.name"
                    } else {
                        live_crew.contestEqualPosition = false
                    }
                } else {
                    live_crew.contestEqualPosition = false
                }
                last_live_crew = live_crew
            }
        }

    }
    
    //--------------------------------------------------------------------------
    static List GetLiveCrewSort(List liveCrews, int liveTaskID)
    {
        if (liveTaskID) {
            boolean found_id = false
            for (Map live_crew in liveCrews) {
                for (Map live_task in live_crew.tasks) {
                    if (live_task.id == liveTaskID) {
                        live_crew.taskPosition = live_task.taskPosition
                        found_id = true
                    }
                }
            }
            
            // sort contest position
            if (found_id) {
                liveCrews.sort { p1, p2 ->
                    p1.taskPosition.compareTo(p2.taskPosition)
                }
            }
        }
        return liveCrews
    }
    
    //--------------------------------------------------------------------------
    private void calculate_crew_penalties(Contest contestInstance, ResultClass resultclassInstance, List tastSettings, List teamSettings, Map resultSettings, ResultFilter resultFilter)
    {
        printstart "calculate_crew_penalties $resultFilter"
        BigDecimal landing_results_factor = contestInstance.contestLandingResultsFactor
        for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
            if (!resultclassInstance || (crew_instance.resultclass.id == resultclassInstance.id)) {
                if (!teamSettings || (crew_instance.team in teamSettings)) {
                    crew_instance.planningPenalties = 0
                    crew_instance.flightPenalties = 0
                    crew_instance.observationPenalties = 0
                    crew_instance.landingPenalties = 0
                    crew_instance.specialPenalties = 0
                    switch (resultFilter) {
                        case ResultFilter.Contest:
                            crew_instance.contestPenalties = 0
                            List bestofanalysis_task_penalties = []
                            boolean disable_contest_penalties = false
                            for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"idTitle"])) {
                                if (task_instance in tastSettings) {
                                    Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
                                    int task_penalties = 0
                                    if (test_instance && !test_instance.disabledCrew) {
                                        if (test_instance.IsPlanningTestRun()) {
                                            crew_instance.planningPenalties += test_instance.planningTestPenalties
                                            if (resultSettings["Planning"]) {
                                                task_penalties += test_instance.planningTestPenalties
                                            }
                                        }
                                        if (test_instance.IsFlightTestRun()) {
                                            crew_instance.flightPenalties += test_instance.flightTestPenalties
                                            if (resultSettings["Flight"]) {
                                                task_penalties += test_instance.flightTestPenalties
                                            }
                                        }
                                        if (test_instance.IsObservationTestRun()) {
                                            crew_instance.observationPenalties += test_instance.observationTestPenalties
                                            if (resultSettings["Observation"]) {
                                                task_penalties += test_instance.observationTestPenalties
                                            }
                                        }
                                        if (test_instance.IsLandingTestRun()) {
											int landing_test_penalties = 0
											if (test_instance.IsLandingTestAnyRun()) {
												if (test_instance.IsLandingTest1Run()) {
													landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest1Penalties)
												}
												if (test_instance.IsLandingTest2Run()) {
													landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest2Penalties)
												}
												if (test_instance.IsLandingTest3Run()) {
													landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest3Penalties)
												}
												if (test_instance.IsLandingTest4Run()) {
													landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest4Penalties)
												}
											} else {
												landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTestPenalties)
											}
                                            crew_instance.landingPenalties += landing_test_penalties
                                            if (resultSettings["Landing"]) {
                                                task_penalties += landing_test_penalties
                                            }
                                        }
                                        if (test_instance.IsSpecialTestRun()) {
                                            crew_instance.specialPenalties += test_instance.specialTestPenalties
                                            if (resultSettings["Special"]) {
                                                task_penalties += test_instance.specialTestPenalties
                                            }
                                        }
                                        if (test_instance.IsIncreaseEnabled()) {
                                            task_penalties += crew_instance.GetIncreaseFactor() * task_penalties / 100
                                        }
                                    } else {
                                        disable_contest_penalties = true
                                    }
                                    if ((contestInstance.bestOfAnalysisTaskNum > 0) && task_instance.bestOfAnalysis) {
                                        bestofanalysis_task_penalties += task_penalties
                                    } else {
                                        crew_instance.contestPenalties += task_penalties
                                    }
                                }
                            }
                            crew_instance.contestPenalties += get_bestofanalysis_task_penalties(contestInstance.bestOfAnalysisTaskNum, bestofanalysis_task_penalties)
                            if (disable_contest_penalties) {
                                crew_instance.contestPenalties = -1
                            }
                            println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): contestPenalties = $crew_instance.contestPenalties"
                            break
                        case ResultFilter.Team:
                            boolean disable_team_penalties = false
                            if (crew_instance.IsActiveCrew(ResultFilter.Team)) { 
                                crew_instance.teamPenalties = 0
                                List bestofanalysis_task_penalties = []
                                for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"idTitle"])) {
                                    if (task_instance in tastSettings) {
                                        Test test_instance = Test.findByCrewAndTask(crew_instance,task_instance)
                                        int task_penalties = 0
                                        if (test_instance && !test_instance.disabledCrew) {
                                            if (test_instance.IsPlanningTestRun()) {
                                                crew_instance.planningPenalties += test_instance.planningTestPenalties
                                                if (resultSettings["Planning"]) {
                                                    task_penalties += test_instance.planningTestPenalties
                                                }
                                            }
                                            if (test_instance.IsFlightTestRun()) {
                                                crew_instance.flightPenalties += test_instance.flightTestPenalties
                                                if (resultSettings["Flight"]) {
                                                    task_penalties += test_instance.flightTestPenalties
                                                }
                                            }
                                            if (test_instance.IsObservationTestRun()) {
                                                crew_instance.observationPenalties += test_instance.observationTestPenalties
                                                if (resultSettings["Observation"]) {
                                                    task_penalties += test_instance.observationTestPenalties
                                                }
                                            }
                                            if (test_instance.IsLandingTestRun()) {
												int landing_test_penalties = 0
												if (test_instance.IsLandingTestAnyRun()) {
													if (test_instance.IsLandingTest1Run()) {
														landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest1Penalties)
													}
													if (test_instance.IsLandingTest2Run()) {
														landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest2Penalties)
													}
													if (test_instance.IsLandingTest3Run()) {
														landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest3Penalties)
													}
													if (test_instance.IsLandingTest4Run()) {
														landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTest4Penalties)
													}
												} else {
													landing_test_penalties += FcMath.GetLandingPenalties(landing_results_factor, test_instance.landingTestPenalties)
												}
                                                crew_instance.landingPenalties += landing_test_penalties
                                                if (resultSettings["Landing"]) {
                                                    task_penalties += landing_test_penalties
                                                }
                                            }
                                            if (test_instance.IsSpecialTestRun()) {
                                                crew_instance.specialPenalties += test_instance.specialTestPenalties
                                                if (resultSettings["Special"]) {
                                                    task_penalties += test_instance.specialTestPenalties
                                                }
                                            }
                                            if (test_instance.IsIncreaseEnabled()) {
                                                task_penalties += crew_instance.GetIncreaseFactor() * task_penalties / 100
                                            }
                                        } else {
                                            disable_team_penalties = true
                                        }
                                        if ((contestInstance.bestOfAnalysisTaskNum > 0) && task_instance.bestOfAnalysis) {
                                            bestofanalysis_task_penalties += task_penalties
                                        } else {
                                            crew_instance.teamPenalties += task_penalties
                                        }
                                    }
                                }
                                crew_instance.teamPenalties += get_bestofanalysis_task_penalties(contestInstance.bestOfAnalysisTaskNum, bestofanalysis_task_penalties)
                            } else {
                                crew_instance.teamPenalties = 100000
                            }
                            if (disable_team_penalties) {
                                crew_instance.teamPenalties = -1
                            }
                            println "$crew_instance.name (${crew_instance.resultclass?.name},${crew_instance.team?.name}): teamPenalties = $crew_instance.teamPenalties"
                            break
                    }
                    crew_instance.save()
                }
            }
        }
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void calculatelive_crew_penalties(Map resultSettings, List tastSettings, Map liveContest, List liveCrews)
    {
        printstart "calculatelive_crew_penalties"
        
        for (Map live_crew in liveCrews) {
            live_crew.planningPenalties = 0
            live_crew.flightPenalties = 0
            live_crew.observationPenalties = 0
            live_crew.landingPenalties = 0
            live_crew.specialPenalties = 0
            live_crew.contestPenalties = 0
            List bestofanalysis_task_penalties = []
            boolean disable_contest_penalties = false
            for (Map live_task in live_crew.tasks) {
                if (live_task.id in tastSettings) {
                    Test test_instance
                    int task_penalties = 0
                    if (!live_task.disabledCrew) {
                        if (live_task.isPlanningTest) {
                            live_crew.planningPenalties += live_task.planningTestPenalties
                            if (resultSettings["Planning"]) {
                                task_penalties += live_task.planningTestPenalties
                            }
                        }
                        if (live_task.isFlightTest) {
                            live_crew.flightPenalties += live_task.flightTestPenalties
                            if (resultSettings["Flight"]) {
                                task_penalties += live_task.flightTestPenalties
                            }
                        }
                        if (live_task.isObservationTest) {
                            live_crew.observationPenalties += live_task.observationTestPenalties
                            if (resultSettings["Observation"]) {
                                task_penalties += live_task.observationTestPenalties
                            }
                        }
                        if (live_task.isLandingTest) {
							int landing_test_penalties = 0
							if (live_task.isLandingTest1 || live_task.isLandingTest2 || live_task.isLandingTest3 || live_task.isLandingTest4) {
								if (live_task.isLandingTest1) {
									landing_test_penalties += FcMath.GetLandingPenalties(liveContest.getLandingResultsFactor, live_task.landingTest1Penalties)
								}
								if (live_task.isLandingTest2) {
									landing_test_penalties += FcMath.GetLandingPenalties(liveContest.getLandingResultsFactor, live_task.landingTest2Penalties)
								}
								if (live_task.isLandingTest3) {
									landing_test_penalties += FcMath.GetLandingPenalties(liveContest.getLandingResultsFactor, live_task.landingTest3Penalties)
								}
								if (live_task.isLandingTest4) {
									landing_test_penalties += FcMath.GetLandingPenalties(liveContest.getLandingResultsFactor, live_task.landingTest4Penalties)
								}
							} else {
								landing_test_penalties += FcMath.GetLandingPenalties(liveContest.getLandingResultsFactor, live_task.landingTestPenalties)
							}
                            live_crew.landingPenalties += landing_test_penalties
                            if (resultSettings["Landing"]) {
                                task_penalties += landing_test_penalties
                            }
                        }
                        if (live_task.isSpecialTest) {
                            live_crew.specialPenalties += live_task.specialTestPenalties
                            if (resultSettings["Special"]) {
                                task_penalties += live_task.specialTestPenalties
                            }
                        }
                        if (live_task.isIncreaseEnabled) {
                            task_penalties += live_crew.increaseFactor * task_penalties / 100
                        }
                    } else {
                        disable_contest_penalties = true
                    }
                    if ((liveContest.bestOfAnalysisTaskNum > 0) && live_task.bestOfAnalysis) {
                        bestofanalysis_task_penalties += task_penalties
                    } else {
                        live_crew.contestPenalties += task_penalties
                    }
                }
            }
            live_crew.contestPenalties += get_bestofanalysis_task_penalties(liveContest.bestOfAnalysisTaskNum, bestofanalysis_task_penalties)
            if (disable_contest_penalties) {
                live_crew.contestPenalties = -1
            }
            println "$live_crew.name: Live contestPenalties = $live_crew.contestPenalties"
        }
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private int get_bestofanalysis_task_penalties(int bestOfAnalysisTaskNum, List bestOfAnalysisTaskPenalties)
    {
        if (bestOfAnalysisTaskPenalties.size() == 0) {
            return 0
        }
        
        int ret_penalties = 0
        List sorted_values = bestOfAnalysisTaskPenalties.sort()
        for (int i = 0; i < bestOfAnalysisTaskNum; i++) {
            if (i < sorted_values.size()) {
                ret_penalties += sorted_values[i]
            }
        }
        return ret_penalties
    }

    //--------------------------------------------------------------------------
    private void calculatepositions_team(Contest contestInstance)
    {
        printstart "calculatepositions_team"
        
        int act_penalty = -1
        int max_position = Team.countByContest(contestInstance)
        for (int act_position = 1; act_position <= max_position; act_position++) {
            
            // search lowest penalty
            int min_penalty = 100000
            for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
                if (team_instance.IsActiveTeam()) {
                    if (team_instance.contestPenalties > act_penalty) {
                        if (team_instance.contestPenalties < min_penalty) {
                            min_penalty = team_instance.contestPenalties 
                        }
                    }
                }
            }
            act_penalty = min_penalty 

            // set position
            int set_position = -1
            for (Team team_instance in Team.findAllByContest(contestInstance,[sort:"id"])) {
                if (team_instance.IsActiveTeam()) {
                    if (team_instance.contestPenalties == act_penalty) {
                        team_instance.contestPosition = act_position
                        team_instance.contestEqualPosition = false
                        team_instance.contestAddPosition = 0
                        println "$team_instance.name: contestPosition = $act_position ($team_instance.contestPenalties Punkte)"
                        team_instance.save()
                        set_position++
                    }
                }
            }
            if (set_position > 0) {
                act_position += set_position
            }
        }
        
        // set contestEqualPosition
        set_team_equalposition(contestInstance)
        
        printdone ""
    }
    
    //--------------------------------------------------------------------------
    private void set_team_equalposition(Contest contestInstance)
    {
        Team last_team_instance = null
        for (Team team_instance in Team.findAllByContestAndContestPositionNotEqual(contestInstance,0,[sort:"contestPosition"])) {
            if (last_team_instance) {
                if (last_team_instance.contestPosition == team_instance.contestPosition) {
                    last_team_instance.contestEqualPosition = true
                    last_team_instance.save()
                    team_instance.contestEqualPosition = true
                    team_instance.save()
                    println "Set contestEqualPosition for position $team_instance.contestPosition: $last_team_instance.name / $team_instance.name"
                } else {
                    team_instance.contestEqualPosition = false
                    team_instance.save()
                }
            } else {
                team_instance.contestEqualPosition = false
                team_instance.save()
            }
            last_team_instance = team_instance
        }

    }
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
    }
    
    //--------------------------------------------------------------------------
    void printstart(out)
    {
        logService.printstart out
    }

    //--------------------------------------------------------------------------
    void printerror(out)
    {
        if (out) {
            logService.printend "Error: $out"
        } else {
            logService.printend "Error."
        }
    }

    //--------------------------------------------------------------------------
    void printdone(out)
    {
        if (out) {
            logService.printend "Done: $out"
        } else {
            logService.printend "Done."
        }
    }

    //--------------------------------------------------------------------------
    void print(out)
    {
        logService.print out
    }

    //--------------------------------------------------------------------------
    void println(out)
    {
        logService.println out
    }
}
