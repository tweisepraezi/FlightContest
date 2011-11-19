class CoordRouteController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	return [coordRouteInstance:coordroute.instance]
        } else {
            flash.message = coordroute.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	return [coordRouteInstance:coordroute.instance]
        } else {
            flash.message = coordroute.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def coordroute = fcService.updateCoordRoute(params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
            redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def coordroute = fcService.createCoordRoute(params)
		if (coordroute.error) {
			flash.error = coordroute.error
            flash.message = coordroute.message
            redirect(controller:"route",action:show,id:params.routeid)
		} else {
			return [coordRouteInstance:coordroute.instance]
		}
    }

    def save = {
        def coordroute = fcService.saveCoordRoute(params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
        	redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else {
            render(view:'create',model:[coordRouteInstance:coordroute.instance])
        }
    }

    def delete = {
        def coordroute = fcService.deleteCoordRoute(params)
        if (coordroute.deleted) {
        	flash.message = coordroute.message
        	redirect(controller:"route",action:show,id:coordroute.routeid)
        } else if (coordroute.notdeleted) {
        	flash.message = coordroute.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = coordroute.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
	def cancel = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
	}
}
