

class RouteCoordController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def routecoord = fcService.getRouteCoord(params) 
        if (routecoord.instance) {
        	return [routeCoordInstance:routecoord.instance]
        } else {
            flash.message = routecoord.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def routecoord = fcService.getRouteCoord(params) 
        if (routecoord.instance) {
        	return [routeCoordInstance:routecoord.instance]
        } else {
            flash.message = routecoord.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def routecoord = fcService.updateRouteCoord(params) 
        if (routecoord.saved) {
        	flash.message = routecoord.message
        	redirect(action:show,id:routecoord.instance.id)
        } else if (routecoord.instance) {
        	render(view:'edit',model:[routeCoordInstance:routecoord.instance])
        } else {
        	flash.message = routecoord.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def routecoord = fcService.createRouteCoord(params)
        return [routeCoordInstance:routecoord.instance]
    }

    def save = {
        def routecoord = fcService.saveRouteCoord(params) 
        if (routecoord.saved) {
        	flash.message = routecoord.message
        	redirect(controller:"route",action:show,id:params.routeid)
        } else {
            render(view:'create',model:[routeCoordInstance:routecoord.instance])
        }
    }

    def delete = {
        def routecoord = fcService.deleteRouteCoord(params)
        if (routecoord.deleted) {
        	flash.message = routecoord.message
        	redirect(controller:"route",action:show,id:routecoord.routeid)
        } else if (routecoord.notdeleted) {
        	flash.message = routecoord.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = routecoord.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
	def cancel = {
        redirect(controller:"route",action:show,id:params.routeid)
	}
}
