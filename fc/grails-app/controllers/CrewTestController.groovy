

class CrewTestController {
    
	def fcService
	
    def show = {
	        def crewtest = fcService.getCrewTest(params) 
	        if (crewtest.instance) {
	            return [crewTestInstance:crewtest.instance]
	        } else {
	            flash.message = crewtest.message
	            redirect(controller:"contest",action:"start")
	        }
	    }

    def flightplan = {
        def crewtest = fcService.getCrewTest(params) 
        if (crewtest.instance) {
        	return [crewTestInstance:crewtest.instance]
        } else {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

    def flightplanprintable = {
        def crewtest = fcService.getCrewTest(params) 
        if (crewtest.instance) {
        	return [crewTestInstance:crewtest.instance]
        } else {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

    def printflightplan = {
        def crewtest = fcService.printflightplanCrewTest(params,GetBaseURI()) 
        if (!crewtest.instance) {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        } else if (crewtest.error) {
        	flash.message = crewtest.message
        	flash.error = true
        	redirect(action:show,id:crewtest.instance.id)
        } else if (crewtest.content) {
        	fcService.WritePDF(response,crewtest.content)
        } else {
        	redirect(action:show,id:crewtest.instance.id)
        }
    }

    def results = {
        def crewtest = fcService.getCrewTest(params) 
        if (crewtest.instance) {
        	return [crewTestInstance:crewtest.instance]
        } else {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

    def resultsprintable = {
        def crewtest = fcService.getCrewTest(params) 
        if (crewtest.instance) {
        	return [crewTestInstance:crewtest.instance]
        } else {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

    def printresults = {
        def crewtest = fcService.printresultsCrewTest(params,GetBaseURI()) 
        if (!crewtest.instance) {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        } else if (crewtest.error) {
        	flash.message = crewtest.message
        	flash.error = true
        	redirect(action:show,id:crewtest.instance.id)
        } else if (crewtest.content) {
        	fcService.WritePDF(response,crewtest.content)
        } else {
        	redirect(action:show,id:crewtest.instance.id)
        }
    }

    def editresults = {
        def crewtest = fcService.getCrewTest(params) 
        if (crewtest.instance) {
        	return [crewTestInstance:crewtest.instance]
        } else {
            flash.message = crewtest.message
            redirect(controller:"contestDayTask",action:"start")
        }
    }

    def updateresults = {
        def crewtest = fcService.updateresultsCrewTest(params) 
        if (crewtest.saved) {
        	flash.message = crewtest.message
        	redirect(action:results,id:crewtest.instance.id)
        } else if (crewtest.instance) {
        	render(view:'editresults',model:[crewTestInstance:crewtest.instance])
        } else {
        	flash.message = crewtest.message
            redirect(action:editresults,id:params.id)
        }
    }

	String GetBaseURI() {
		return request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request)
	}

}
