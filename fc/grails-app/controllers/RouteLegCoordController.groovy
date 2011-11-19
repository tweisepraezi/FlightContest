class RouteLegCoordController {
    
	def fcService
	
    def show = {
        def routeleg = fcService.getRouteLegCoord(params) 
        if (routeleg.instance) {
        	return [routeLegInstance:routeleg.instance]
        } else {
            flash.message = routeleg.message
            redirect(controller:"contest",action:"start")
        }
    }

	def edit = {
        def routeleg = fcService.getRouteLegCoord(params) 
        if (routeleg.instance) {
            return [routeLegInstance:routeleg.instance]
        } else {
            flash.message = routeleg.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def routeleg = fcService.updateRouteLegCoord(params) 
        if (routeleg.saved) {
            flash.message = routeleg.message
            redirect(controller:"route",action:show,id:routeleg.instance.route.id)
        } else if (routeleg.instance) {
        	render(view:'edit',model:[routeLegInstance:routeleg.instance])
        } else {
            flash.message = routeleg.message
            redirect(action:edit,id:params.id)
        }
    }

	def cancel = {
        def routeleg = fcService.getRouteLegCoord(params) 
        if (routeleg.instance) {
            redirect(controller:"route",action:show,id:routeleg.instance.route.id)
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
    }
}
