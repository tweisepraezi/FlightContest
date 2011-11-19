

class NavTestTaskLegController {
    
	def fcService
	
    def show = {
        def navtesttaskleg = fcService.getNavTestTaskLeg(params) 
        if (navtesttaskleg.instance) {
        	return [navTestTaskLegInstance:navtesttaskleg.instance]
        } else {
            flash.message = navtesttaskleg.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

}
