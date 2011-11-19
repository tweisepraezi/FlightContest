

class RouteLegController {
    
	def fcService
	
    def show = {
        def routeleg = fcService.getRouteLeg(params) 
        if (routeleg.instance) {
        	return [routeLegInstance:routeleg.instance]
        } else {
            flash.message = routeleg.message
            redirect(controller:"contest",action:"start")
        }
    }

}
