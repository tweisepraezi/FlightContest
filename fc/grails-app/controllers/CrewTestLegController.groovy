

class CrewTestLegController {
    
	def fcService
	
    def show = {
        def crewtestleg = fcService.getCrewTestLeg(params) 
        if (crewtestleg.instance) {
        	return [crewTestLegInstance:crewtestleg.instance]
        } else {
            flash.message = crewtestleg.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

}
