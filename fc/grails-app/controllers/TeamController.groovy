class TeamController {

	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List teams"
        if (session?.lastContest) {
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
            def teamList = Team.findAllByContest(session.lastContest, [sort:'name'])
			fcService.printdone "last contest"
            return [teamInstanceList:teamList]
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

	def listprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        if (session?.lastContest) {
            def teamList = Team.findAllByContest(session.lastContest,[sort:"name"])
            return [teamInstanceList:teamList]
        }
        return [:]
    }

    def print = {
        def teams = fcService.printTeams(params,false,false,GetPrintParams()) 
        if (teams.error) {
            flash.message = teams.message
            flash.error = true
            redirect(action:list)
        } else if (teams.content) {
            fcService.WritePDF(response,teams.content,session.lastContest.GetPrintPrefix(),"teams",true,false,false)
        } else {
            redirect(action:list)
        }
    }
    
    Map GetPrintParams() {
		return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
		        contest:session.lastContest,
		        lang:session.printLanguage
		       ]
    }
}
