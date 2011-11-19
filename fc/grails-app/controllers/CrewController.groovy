class CrewController {
    
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List crews"
        if (session?.lastContest) {
			// save return action
			session.crewReturnAction = actionName 
			session.crewReturnController = controllerName
			session.crewReturnID = params.id
			session.aircraftReturnAction = actionName
			session.aircraftReturnController = controllerName
			session.aircraftReturnID = params.id
            def crewList = Crew.findAllByContest(session.lastContest, [sort:'viewpos'])
			fcService.printdone "last contest"
            return [crewInstanceList:crewList]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def edit = {
        def crew = fcService.getCrew(params) 
        if (crew.instance) {
			// assign return action
			if (session.crewReturnAction) {
				return [crewInstance:crew.instance,crewReturnAction:session.crewReturnAction,crewReturnController:session.crewReturnController,crewReturnID:session.crewReturnID]
			}
        	return [crewInstance:crew.instance]
        } else {
            flash.message = crew.message
            redirect(action:list)
        }
    }

    def update = {
        def crew = fcService.updateCrew(params) 
        if (crew.saved) {
        	flash.message = crew.message
			// process return action
			if (params.crewReturnAction) {
				redirect(action:params.crewReturnAction,controller:params.crewReturnController,id:params.crewReturnID)
			} else {
				redirect(action:list)
			}
        } else if (crew.instance) {
        	render(view:'edit',model:[crewInstance:crew.instance])
        } else {
        	flash.message = crew.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def crew = fcService.createCrew(params) 
        return [crewInstance:crew.instance]
    }

    def save = {
        def crew = fcService.saveCrew(params,session.lastContest) 
        if (crew.saved) {
        	flash.message = crew.message
        	redirect(action:list)
        } else {
            render(view:'create',model:[crewInstance:crew.instance])
        }
    }

	def delete = {
        def crew = fcService.deleteCrew(params)
        if (crew.deleted) {
        	flash.message = crew.message
        	redirect(action:list)
        } else if (crew.notdeleted) {
        	flash.message = crew.message
            redirect(action:edit,id:params.id)
        } else {
        	flash.message = crew.message
        	redirect(action:list)
        }
    }

	def cancel = {
		// process return action
		if (params.crewReturnAction) {
			redirect(action:params.crewReturnAction,controller:params.crewReturnController,id:params.crewReturnID)
		} else {
			redirect(action:list)
		}
	}

	def selectfilename = {
		[:]
    }
	
	def importcrews = {
		def file = request.getFile('loadfile')
		if (file && !file.empty) {
			println file.getContentType()
			String load_file_name = "fcdb-crewlist-upload.xls"
			String file_name = file.getOriginalFilename()
			file.transferTo(new File(load_file_name))
	        def crews = fcService.importCrews(file_name, load_file_name, session.lastContest) 
	        if (crews.saved) {
	            flash.message = crews.message
	        } else if (crews.error) {
	        	flash.error = crews.error
	            flash.message = crews.message
	        }
		}
        redirect(action:list)
	}
	
    def listprintable = {
		if (params.contestid) {
			session.lastContest = Contest.get(params.contestid)
		}
		if (session?.lastContest) {
            def crewList = Crew.findAllByContestAndDisabled(session.lastContest,false)
            params.sort = "viewpos"
            return [crewInstanceList:crewList]
        }
        return [:]
    }

	def print = {
        def crews = fcService.printCrews(params,GetPrintParams()) 
        if (crews.error) {
            flash.message = crews.message
            flash.error = true
            redirect(action:list)
        } else if (crews.content) {
            fcService.WritePDF(response,crews.content)
        } else {
            redirect(action:list)
        }
    }
    
	def selectall = {
        def crew = fcService.selectallCrew(session.lastContest) 
       	flash.selectedCrewIDs = crew.selectedcrewids
       	redirect(action:list)
    }

    def deselectall = {
       	redirect(action:list)
    }
    
	def calculatesequence = {
        def crew = fcService.calculatesequenceCrew(session.lastContest,params) 
        flash.error = crew.error
        flash.message = crew.message
       	redirect(action:list)
	}

	def moveup = {
        def crew = fcService.moveupCrew(session.lastContest,params,session) 
        if (crew.borderreached) {
        	redirect(action:list)
        } else if (crew.error) {
       		flash.message = crew.message
            flash.error = true
        	redirect(action:list)
        } else { 
	        if (crew.selectedcrewids) {
	    		flash.selectedCrewIDs = crew.selectedcrewids
	    	}
        	redirect(action:list)
        }
	}
	
	def movedown = {
        def crew = fcService.movedownCrew(session.lastContest,params,session) 
        if (crew.borderreached) {
        	redirect(action:list)
        } else if (crew.error) {
       		flash.message = crew.message
            flash.error = true
        	redirect(action:list)
        } else {
	    	if (crew.selectedcrewids) {
	    		flash.selectedCrewIDs = crew.selectedcrewids
	    	}
        	redirect(action:list)
        }
	}
	
	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
               ]
    }
}
