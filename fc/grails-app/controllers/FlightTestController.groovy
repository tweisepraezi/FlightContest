

class FlightTestController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def flighttest = fcService.getFlightTest(params) 
        if (flighttest.instance) {
        	return [flightTestInstance:flighttest.instance]
        } else {
            flash.message = flighttest.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def edit = {
        def flighttest = fcService.getFlightTest(params) 
        if (flighttest.instance) {
        	return [flightTestInstance:flighttest.instance]
        } else {
            flash.message = flighttest.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def update = {
        def flighttest = fcService.updateFlightTest(params) 
        if (flighttest.saved) {
        	flash.message = flighttest.message
        	redirect(action:show,id:flighttest.instance.id)
        } else if (flighttest.instance) {
        	render(view:'edit',model:[flightTestInstance:flighttest.instance])
        } else {
        	flash.message = flighttest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def flighttest = fcService.createFlightTest(params)
        if (flighttest.error) {
        	flash.message = flighttest.message
        	flash.error = flighttest.error
            if (flighttest.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:flighttest.taskid)
            } else if (flighttest.fromtask) {
            	redirect(controller:"task",action:show,id:flighttest.taskid)
            } else {
            	redirect(controller:"contest",action:"tasks")
            }
        } else {
        	return [flightTestInstance:flighttest.instance]
        }
    }

    def save = {
        def flighttest = fcService.saveFlightTest(params) 
        if (flighttest.saved) {
        	flash.message = flighttest.message
            if (flighttest.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:flighttest.taskid)
            } else if (flighttest.fromtask) {
            	redirect(controller:"task",action:show,id:flighttest.taskid)
            } else {
            	redirect(controller:"contest",action:"tasks")
            }
        } else {
            render(view:'create',model:[flightTestInstance:flighttest.instance])
        }
    }

    def delete = {
        def flighttest = fcService.deleteFlightTest(params)
        if (flighttest.deleted) {
        	flash.message = flighttest.message
        	redirect(controller:"contest",action:"tasks")
        } else if (flighttest.notdeleted) {
        	flash.message = flighttest.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = flighttest.message
        	redirect(controller:"contest",action:"tasks")
        }
    }

	def cancel = {
        if (params.fromlistplanning) {
            redirect(controller:"task",action:"listplanning",id:params.taskid)
        } else if (params.fromtask) {
            redirect(controller:"task",action:show,id:params.taskid)
        } else {
            redirect(controller:"contest",action:"tasks")
        }
	}
	
	def createflighttestwind = {
        def flighttest = fcService.getFlightTest(params) 
        redirect(controller:'flightTestWind',action:'create',params:['flightTest.id':flighttest.instance.id,'flighttestid':flighttest.instance.id])
	}
}
