import java.util.List;
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
            for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
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
        for( Task result_task in result_tasks) {
            calculatepositions_task(result_task, result_classes, contestInstance.GetResultSettings(), true) // true - ignoreProvisional
        }
        
        calculate_crew_penalties(contestInstance,null,result_tasks,contestInstance.GetResultTeams(contestInstance.contestTeamResults),contestInstance.GetResultSettings(),ResultFilter.Contest)
        
        // calculate positions
        calculatepositions_contest(contestInstance, null, contestInstance.GetResultTeams(contestInstance.contestTeamResults), contestInstance.GetResultSettings(), true) // true - ignoreProvisional
        
        contest.message = getMsg('fc.results.positionscalculated')
        printdone contest.message      
        return contest
    }
    
    //--------------------------------------------------------------------------
    Map calculatepositionsResultClass(ResultClass resultclassInstance, List contestTasks, List contestTeams)
    {
        printstart "calculatepositionsResultClass $resultclassInstance.name"
        
        Map resultclass = [:]
        
        if (contestTasks) {
            println "Set class result tasks with $contestTasks"
            resultclassInstance.contestTaskResults = ""
            for (Task task_instance in Task.findAllByContest(resultclassInstance.contest,[sort:"id"])) {
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
            for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
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
    private void calculatepositions_contest(Contest contestInstance, ResultClass resultclassInstance, List teamSettings, Map resultSettings, boolean ignoreProvisional)
    {
        printstart "calculatepositions_contest [Class:${resultclassInstance?.name}, Teams:${teamSettings}]"
        
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
                                    if (resultclassInstance.id == crew_instance.resultclass.id) { // BUG: direkter Klassen-Vergleich geht nicht
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
                } else if (!resultclassInstance || (resultclassInstance.id == crew_instance.resultclass.id)) { // BUG: direkter Klassen-Vergleich geht nicht
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
    private void calculate_crew_penalties(Contest contestInstance, ResultClass resultclassInstance, List tastSettings, List teamSettings, Map resultSettings, ResultFilter resultFilter)
    {
        printstart "calculate_crew_penalties $resultFilter"
        for (Crew crew_instance in Crew.findAllByContest(contestInstance,[sort:"id"])) {
            if (!resultclassInstance || (crew_instance.resultclass == resultclassInstance)) {
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
                            for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
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
                                            crew_instance.landingPenalties += test_instance.landingTestPenalties
                                            if (resultSettings["Landing"]) {
                                                task_penalties += test_instance.landingTestPenalties
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
                            crew_instance.contestPenalties += get_bestofanalysis_task_penalties(contestInstance, bestofanalysis_task_penalties)
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
                                for (Task task_instance in Task.findAllByContest(contestInstance,[sort:"id"])) {
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
                                                crew_instance.landingPenalties += test_instance.landingTestPenalties
                                                if (resultSettings["Landing"]) {
                                                    task_penalties += test_instance.landingTestPenalties
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
                                crew_instance.teamPenalties += get_bestofanalysis_task_penalties(contestInstance, bestofanalysis_task_penalties)
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
    private int get_bestofanalysis_task_penalties(Contest contestInstance, List bestOfAnalysisTaskPenalties)
    {
        if (bestOfAnalysisTaskPenalties.size() == 0) {
            return 0
        }
        
        int ret_penalties = 0
        List sorted_values = bestOfAnalysisTaskPenalties.sort()
        for (int i = 0; i < contestInstance.bestOfAnalysisTaskNum; i++) {
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
