

class FlightTestWindController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def flighttestwind = fcService.getFlightTestWind(params) 
        if (flighttestwind.instance) {
        	return [flightTestWindInstance:flighttestwind.instance]
        } else {
            flash.message = flighttestwind.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def edit = {
        def flighttestwind = fcService.getFlightTestWind(params) 
        if (flighttestwind.instance) {
			// assign return action
			if (session.flighttestwindReturnAction) {
				return [flightTestWindInstance:flighttestwind.instance,flighttestwindReturnAction:session.flighttestwindReturnAction,flighttestwindReturnController:session.flighttestwindReturnController,flighttestwindReturnID:session.flighttestwindReturnID]
			}
        	return [flightTestWindInstance:flighttestwind.instance]
        } else {
            flash.message = flighttestwind.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def update = {
        def flighttestwind = fcService.updateFlightTestWind(session.showLanguage, params) 
        if (flighttestwind.saved) {
        	flash.message = flighttestwind.message
            if (params.flighttestwindReturnAction) {
                redirect(action:params.flighttestwindReturnAction,controller:params.flighttestwindReturnController,id:params.flighttestwindReturnID)
            } else {
        	    redirect(controller:"flightTest",action:show,id:flighttestwind.flighttestid)
            }
        } else if (flighttestwind.instance) {
        	render(view:'edit',model:[flightTestWindInstance:flighttestwind.instance,flighttestwindReturnAction:session.flighttestwindReturnAction,flighttestwindReturnController:session.flighttestwindReturnController,flighttestwindReturnID:session.flighttestwindReturnID])
        } else {
        	flash.message = flighttestwind.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def flighttestwind = fcService.createFlightTestWind(params)
       	return [flightTestWindInstance:flighttestwind.instance]
    }

    def save = {
        def flighttestwind = fcService.saveFlightTestWind(session.showLanguage, params) 
        if (flighttestwind.saved) {
        	flash.message = flighttestwind.message
            if (flighttestwind.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:flighttestwind.taskid)
            } else {
            	redirect(controller:"flightTest",action:show,id:flighttestwind.flighttestid)
            }
        } else {
            render(view:'create',model:[flightTestWindInstance:flighttestwind.instance])
        }
    }

    def delete = {
        def flighttestwind = fcService.deleteFlightTestWind(params)
        if (flighttestwind.deleted) {
        	flash.message = flighttestwind.message
        	redirect(controller:"flightTest",action:show,id:flighttestwind.flighttestid)
        } else if (flighttestwind.notdeleted) {
			flash.error = true
        	flash.message = flighttestwind.message
            redirect(action:edit,id:params.id)
        } else {
        	flash.message = flighttestwind.message
        	redirect(controller:"contest",action:"tasks")
        }
    }

    def cancel = {
		// process return action
		if (params.flighttestwindReturnAction) {
			redirect(action:params.flighttestwindReturnAction,controller:params.flighttestwindReturnController,id:params.flighttestwindReturnID)
		} else if (params.fromlistplanning) {
            redirect(controller:"task",action:"listplanning",id:params.taskid)
        } else {
            redirect(controller:"flightTest",action:show,id:params.flighttestid)
        }
	}
}
