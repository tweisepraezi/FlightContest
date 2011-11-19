

class WindController {
    
	def fcService
	
    def show = {
        def wind = fcService.getWind(params) 
        if (wind.instance) {
        	return [windInstance:wind.instance]
        } else {
            flash.message = wind.message
            redirect(action:list)
        }
    }

}
