

class CrewController {
    
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        params.sort = "name1"
        return [crewInstanceList:Crew.list(params), crewInstanceTotal:Crew.count()]
    }

    def show = {
        def crew = fcService.getCrew(params) 
        if (crew.instance) {
        	return [crewInstance:crew.instance]
        } else {
            flash.message = crew.message
            redirect(action:list)
        }
    }

    def edit = {
        def crew = fcService.getCrew(params) 
        if (crew.instance) {
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
        	redirect(action:show,id:crew.instance.id)
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
        def crew = fcService.saveCrew(params) 
        if (crew.saved) {
        	flash.message = crew.message
        	redirect(action:show,id:crew.instance.id)
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
            redirect(action:show,id:params.id)
        } else {
        	flash.message = crew.message
        	redirect(action:list)
        }
    }

	def cancel = {
        redirect(action:list)
	}
}
