class RouteLegTestController {
    
	def fcService
	
    def show = {
        def routeleg = fcService.getRouteLegTest(params) 
        if (routeleg.instance) {
        	return [routeLegInstance:routeleg.instance]
        } else {
            flash.message = routeleg.message
            redirect(controller:"contest",action:"start")
        }
    }

}
