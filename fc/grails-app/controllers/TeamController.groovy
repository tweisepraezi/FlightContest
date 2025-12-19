class TeamController {

    def printService
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List teams"
        if (session?.lastContest) {
			session.lastContest.refresh()
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
			session.teamReturnAction = actionName
			session.teamReturnController = controllerName
			session.teamReturnID = params.id
            session.resultclassReturnAction = actionName
            session.resultclassReturnController = controllerName
            session.resultclassReturnID = params.id
            def team_list = Team.findAllByContest(session.lastContest, [sort:'name'])
			fcService.printdone "last contest"
            return [teamInstanceList:team_list, teamresultClasses:session.lastContest.resultClasses]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def edit = {
        def team = fcService.getTeam(params) 
        if (team.instance) {
			// assign return action
			if (session.teamReturnAction) {
				return [teamInstance:team.instance,teamReturnAction:session.teamReturnAction,teamReturnController:session.teamReturnController,teamReturnID:session.teamReturnID]
			}
        	return [teamInstance:team.instance]
        } else {
            flash.message = team.message
            redirect(action:list)
        }
    }

    def update = {
        def team = fcService.updateTeam(params) 
        if (team.saved) {
        	flash.message = team.message
			// process return action
			if (params.teamReturnAction) {
				redirect(action:params.teamReturnAction,controller:params.teamReturnController,id:params.teamReturnID)
			} else {
				redirect(action:list)
			}
        } else if (team.error) {
            flash.message = team.message
            flash.error = true
            render(view:'edit',model:[teamInstance:team.instance])
        } else if (team.instance) {
        	render(view:'edit',model:[teamInstance:team.instance])
        } else {
        	flash.message = team.message
            redirect(action:edit,id:params.id)
        }
    }

    def savesettings = {
        def team = fcService.updateTeam(params) 
        if (team.saved) {
        	flash.message = team.message
			redirect(action:edit,id:team.instance.id)
        } else if (team.error) {
            flash.message = team.message
            flash.error = true
            render(view:'edit',model:[teamInstance:team.instance])
        } else if (team.instance) {
        	render(view:'edit',model:[teamInstance:team.instance])
        } else {
        	flash.message = team.message
            redirect(action:edit,id:params.id)
        }
    }

    def updatenext = {
        def team = fcService.updateTeam(params)
        if (team.instance) {
            long next_id = team.instance.GetNextTeamID()
            if (team.saved) {
                flash.message = team.message
                if (next_id) {
                    redirect(action:edit,id:next_id)
                } else {
                    redirect(controller:"team",action:"list")
                }
            } else {
                if (next_id) {
                    render(view:'edit',model:[crewInstance:team.instance,params:[next:next_id]])
                } else {
                    render(view:'edit',model:[crewInstance:team.instance])
                }
            }
        } else {
            flash.message = test.message
            redirect(controller:"team",action:"list")
        }
    }
    
    def create = {
		def team = fcService.createTeam(params)
        return [teamInstance:team.instance]
    }

    def save = {
		def team = fcService.saveTeam(params,session.lastContest) 
        if (team.saved) {
        	flash.message = team.message
        	redirect(action:list)
        } else if (team.error) {
            flash.message = team.message
            flash.error = true
            render(view:'create',model:[teamInstance:team.instance])
        } else {
            render(view:'create',model:[teamInstance:team.instance])
        }
    }

	def delete = {
        def team = fcService.deleteTeam(params)
        if (team.deleted) {
        	flash.message = team.message
        	redirect(action:list)
        } else if (team.notdeleted) {
        	flash.message = team.message
            redirect(action:edit,id:params.id)
        } else {
        	flash.message = team.message
        	redirect(action:list)
        }
    }

	def cancel = {
		if (params.teamReturnAction) {
			redirect(action:params.teamReturnAction,controller:params.teamReturnController,id:params.teamReturnID)
		} else {
        	redirect(action:list)
		}
	}

    def gotonext = {
        def team = fcService.getTeam(params)
        if (team.instance) {
            long next_id = team.instance.GetNextTeamID()
            if (next_id) {
                redirect(action:edit,id:next_id)
            } else {
                redirect(controller:"team",action:"list")
            }
        } else {
            flash.message = test.message
            redirect(controller:"team",action:"list")
        }
    }
    
    def gotoprev = {
        def team = fcService.getTeam(params)
        if (team.instance) {
            long prev_id = team.instance.GetPrevTeamID()
            if (prev_id) {
                redirect(action:edit,id:prev_id)
            } else {
                redirect(controller:"team",action:"list")
            }
        } else {
            flash.message = test.message
            redirect(controller:"team",action:"list")
        }
    }
    
	def listprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        if (session?.lastContest) {
			session.lastContest.refresh()
            def team_list = Team.findAllByContest(session.lastContest,[sort:"name"])
            int team_num = 0
            for (def team_instance in team_list) {
                if (!team_instance.disabled && Crew.findByTeam(team_instance)) {
                    boolean crew_found = false
                    for (Crew crew_instance in Crew.findAllByTeam(team_instance,[sort:'name'])) {
                        if (!crew_instance.disabled && !crew_instance.disabledTeam) {
                            crew_found = true
                        }
                    }
                    if (crew_found) {
                        team_num++
                    }
                }
            }
            return [contestInstance:session.lastContest, teamInstanceList:team_list, teamNum:team_num]
        }
        return [:]
    }

    def print = {
        Map teams = printService.printTeams(params,false,session.lastContest.printTeamLandscape,GetPrintParams()) 
        if (teams.error) {
            flash.message = teams.message
            flash.error = true
            redirect(action:list)
        } else if (teams.content) {
            printService.WritePDF(response,teams.content,session.lastContest.GetPrintPrefix(),"teams",true,false,false)
        } else {
            redirect(action:list)
        }
    }
    
    def selectall = {
        def crew = fcService.selectallTeam(session.lastContest) 
        flash.selectedTeamIDs = crew.selectedteamids
        redirect(action:list)
    }

    def deselectall = {
        redirect(action:list)
    }
    
    def deleteteams = {
        def ret = fcService.deleteTeams(session.lastContest,params,session)
        if (ret.deleted) { 
            flash.message = ret.message
        }
        redirect(action:list)
    }
    
    def disableteams = {
        def ret = fcService.disableTeams(session.lastContest,params,session)
        flash.message = ret.message
        redirect(action:list)
    }
    
    def enableteams = {
        def ret = fcService.enableTeams(session.lastContest,params,session)
        flash.message = ret.message
        redirect(action:list)
    }
    
    Map GetPrintParams() {
		return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
		        contest:session.lastContest,
		        lang:session.printLanguage
		       ]
    }
}
