

class RouteController {
    
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ routeInstanceList: Route.list( params ), routeInstanceTotal: Route.count() ]
    }

    def show = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def edit = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def update = {
        def route = fcService.updateRoute(params) 
        if (route.saved) {
        	flash.message = route.message
        	redirect(action:show,id:route.instance.id)
        } else if (route.instance) {
        	render(view:'edit',model:[routeInstance:route.instance])
        } else {
        	flash.message = route.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def route = fcService.createRoute(params)
        return [routeInstance:route.instance]
    }

    def save = {
        def route = fcService.saveRoute(params) 
        if (route.saved) {
        	flash.message = route.message
        	redirect(action:show,id:route.instance.id)
        } else {
            render(view:'create',model:[routeInstance:route.instance])
        }
    }

    def delete = {
        def route = fcService.deleteRoute(params)
        if (route.deleted) {
        	flash.message = route.message
        	redirect(action:list)
        } else if (route.notdeleted) {
        	flash.message = route.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = route.message
        	redirect(action:list)
        }
    }
	
	def cancel = {
        redirect(action:list)
	}
	
	def createroutecoords = {
        def route = fcService.getRoute(params) 
        redirect(controller:'routeCoord',action:'create',params:['route.id':route.instance.id,'routeid':route.instance.id])
	}
}
