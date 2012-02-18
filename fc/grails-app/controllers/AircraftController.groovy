class AircraftController {

	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List aircrafts"
        if (session?.lastContest) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
            def aircraftList = Aircraft.findAllByContest(session.lastContest, [sort:'registration'])
			fcService.printdone "last contest"
            return [aircraftInstanceList:aircraftList]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def edit = {
        def aircraft = fcService.getAircraft(params) 
        if (aircraft.instance) {
			// assign return action
			if (session.aircraftReturnAction) {
				return [aircraftInstance:aircraft.instance,aircraftReturnAction:session.aircraftReturnAction,aircraftReturnController:session.aircraftReturnController,aircraftReturnID:session.aircraftReturnID]
			}
        	return [aircraftInstance:aircraft.instance]
        } else {
            flash.message = aircraft.message
            redirect(action:list)
        }
    }

    def update = {
        def aircraft = fcService.updateAircraft(params) 
        if (aircraft.saved) {
        	flash.message = aircraft.message
			// process return action
			if (params.aircraftReturnAction) {
				redirect(action:params.aircraftReturnAction,controller:params.aircraftReturnController,id:params.aircraftReturnID)
			} else {
				redirect(action:list)
			}
        } else if (aircraft.error) {
            flash.message = aircraft.message
            flash.error = true
            render(view:'edit',model:[aircraftInstance:aircraft.instance])
        } else if (aircraft.instance) {
        	render(view:'edit',model:[aircraftInstance:aircraft.instance])
        } else {
        	flash.message = aircraft.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def aircraft = fcService.createAircraft(params)
        return [aircraftInstance:aircraft.instance]
    }

    def save = {
		def aircraft = fcService.saveAircraft(params,session.lastContest) 
        if (aircraft.saved) {
        	flash.message = aircraft.message
        	redirect(action:list)
        } else if (aircraft.error) {
            flash.message = aircraft.message
            flash.error = true
            render(view:'create',model:[aircraftInstance:aircraft.instance])
        } else {
            render(view:'create',model:[aircraftInstance:aircraft.instance])
        }
    }

	def delete = {
        def aircraft = fcService.deleteAircraft(params)
        if (aircraft.deleted) {
        	flash.message = aircraft.message
        	redirect(action:list)
        } else if (aircraft.notdeleted) {
        	flash.message = aircraft.message
            redirect(action:edit,id:params.id)
        } else {
        	flash.message = aircraft.message
        	redirect(action:list)
        }
    }

	def cancel = {
		if (params.aircraftReturnAction) {
			redirect(action:params.aircraftReturnAction,controller:params.aircraftReturnController,id:params.aircraftReturnID)
		} else {
        	redirect(action:list)
		}
	}

	def listprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        if (session?.lastContest) {
            def aircraftList = Aircraft.findAllByContest(session.lastContest)
            params.sort = "registration"
            return [aircraftInstanceList:aircraftList]
        }
        return [:]
    }

    def print = {
        def aircrafts = fcService.printAircrafts(params,GetPrintParams()) 
        if (aircrafts.error) {
            flash.message = aircrafts.message
            flash.error = true
            redirect(action:list)
        } else if (aircrafts.content) {
            fcService.WritePDF(response,aircrafts.content)
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
