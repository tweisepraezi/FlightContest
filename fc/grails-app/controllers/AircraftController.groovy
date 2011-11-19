

class AircraftController {

	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        params.sort = "registration"
        [ aircraftInstanceList: Aircraft.list( params ), aircraftInstanceTotal: Aircraft.count() ]
    }

    def show = {
        def aircraft = fcService.getAircraft(params) 
        if (aircraft.instance) {
        	return [aircraftInstance:aircraft.instance]
        } else {
            flash.message = aircraft.message
            redirect(action:list)
        }
    }

    def edit = {
        def aircraft = fcService.getAircraft(params) 
        if (aircraft.instance) {
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
        	redirect(action:show,id:aircraft.instance.id)
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
        def aircraft = fcService.saveAircraft(params) 
        if (aircraft.saved) {
        	flash.message = aircraft.message
        	redirect(action:show,id:aircraft.instance.id)
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
            redirect(action:show,id:params.id)
        } else {
        	flash.message = aircraft.message
        	redirect(action:list)
        }
    }

	def cancel = {
        redirect(action:list)
	}
}
