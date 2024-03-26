class AircraftController {

    def printService
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List aircrafts"
        if (session?.lastContest) {
            session.lastContest.refresh()
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
            def aircraft_list = Aircraft.findAllByContest(session.lastContest, [sort:'registration'])
			fcService.printdone "last contest"
            return [aircraftInstanceList:aircraft_list]
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

    def savesettings = {
        def aircraft = fcService.updateAircraft(params) 
        if (aircraft.saved) {
        	flash.message = aircraft.message
			redirect(action:edit,id:aircraft.instance.id)
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

    def updatenext = {
        def aircraft = fcService.updateAircraft(params)
        if (aircraft.instance) {
            long next_id = aircraft.instance.GetNextAircraftID()
            if (aircraft.saved) {
                flash.message = aircraft.message
                if (next_id) {
                    redirect(action:edit,id:next_id)
                } else {
                    redirect(controller:"aircraft",action:"list")
                }
            } else if (aircraft.error) {
                flash.message = aircraft.message
                flash.error = true
                render(view:'edit',model:[aircraftInstance:aircraft.instance])
            } else {
                if (next_id) {
                    render(view:'edit',model:[aircraftInstance:aircraft.instance,params:[next:next_id]])
                } else {
                    render(view:'edit',model:[aircraftInstance:aircraft.instance])
                }
            }
        } else {
            flash.message = test.message
            redirect(controller:"aircraft",action:"list")
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
            session.invalidate() // BUG: Löschen einer Aircraft beschädigt Session, Workaround: Session zurücksetzten
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

    def gotonext = {
        def aircraft = fcService.getAircraft(params)
        if (aircraft.instance) {
            long next_id = aircraft.instance.GetNextAircraftID()
            if (next_id) {
                redirect(action:edit,id:next_id)
            } else {
                redirect(controller:"aircraft",action:"list")
            }
        } else {
            flash.message = test.message
            redirect(controller:"aircraft",action:"list")
        }
    }
    
    def gotoprev = {
        def aircraft = fcService.getAircraft(params)
        if (aircraft.instance) {
            long prev_id = aircraft.instance.GetPrevAircraftID()
            if (prev_id) {
                redirect(action:edit,id:prev_id)
            } else {
                redirect(controller:"aircraft",action:"list")
            }
        } else {
            flash.message = test.message
            redirect(controller:"aircraft",action:"list")
        }
    }
    
	def listprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
            session.printLanguage = params.lang
        }
        if (session?.lastContest) {
			session.lastContest.refresh()
            def aircraft_list = Aircraft.findAllByContest(session.lastContest,[sort:"registration"])
            return [contestInstance:session.lastContest, aircraftInstanceList:aircraft_list]
        }
        return [:]
    }

    def print = {
        Map aircrafts = printService.printAircrafts(params,false, false,GetPrintParams()) 
        if (aircrafts.error) {
            flash.message = aircrafts.message
            flash.error = true
            redirect(action:list)
        } else if (aircrafts.content) {
            printService.WritePDF(response,aircrafts.content,session.lastContest.GetPrintPrefix(),"aircraft",true,false,false)
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
