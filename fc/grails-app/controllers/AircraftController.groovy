class AircraftController {

	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if (session.lastContest) {
            def aircraftList = Aircraft.findAllByContest(session.lastContest)
            params.sort = "registration"
            return [aircraftInstanceList:aircraftList]
        }
        return [:]
    }

    def show = {
        def aircraft = fcService.getAircraft(params) 
        if (aircraft.instance) {
        	return [aircraftInstance:aircraft.instance]
        } else {
            flash.message = aircraft.message
            redirect(action:list)
        }
    }

    def edit = {
        def aircraft = fcService.getAircraft(params) 
        if (aircraft.instance) {
        	return [aircraftInstance:aircraft.instance]
        } else {
            flash.message = aircraft.message
            redirect(action:list)
        }
    }

    def update = {
        def aircraft = fcService.updateAircraft(params) 
        if (aircraft.saved) {
        	flash.message = aircraft.message
        	redirect(action:show,id:aircraft.instance.id)
        } else if (aircraft.error) {
            flash.message = aircraft.message
            flash.error = true
            render(view:'edit',model:[aircraftInstance:aircraft.instance])
        } else if (aircraft.instance) {
        	render(view:'edit',model:[aircraftInstance:aircraft.instance])
        } else {
        	flash.message = aircraft.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def aircraft = fcService.createAircraft(params)
        return [aircraftInstance:aircraft.instance]
    }

    def save = {
		def aircraft = fcService.saveAircraft(params,session.lastContest) 
        if (aircraft.saved) {
        	flash.message = aircraft.message
        	redirect(action:show,id:aircraft.instance.id)
        } else if (aircraft.error) {
            flash.message = aircraft.message
            flash.error = true
            render(view:'create',model:[aircraftInstance:aircraft.instance])
        } else {
            render(view:'create',model:[aircraftInstance:aircraft.instance])
        }
    }

	def delete = {
        def aircraft = fcService.deleteAircraft(params)
        if (aircraft.deleted) {
        	flash.message = aircraft.message
        	redirect(action:list)
        } else if (aircraft.notdeleted) {
        	flash.message = aircraft.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = aircraft.message
        	redirect(action:list)
        }
    }

	def cancel = {
        redirect(action:list)
	}

	def listprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        if (session.lastContest) {
            def aircraftList = Aircraft.findAllByContest(session.lastContest)
            params.sort = "registration"
            return [aircraftInstanceList:aircraftList]
        }
        return [:]
    }

    def print = {
        def aircrafts = fcService.printAircrafts(params,GetPrintParams()) 
        if (aircrafts.error) {
            flash.message = aircrafts.message
            flash.error = true
            redirect(action:list)
        } else if (aircrafts.content) {
            fcService.WritePDF(response,aircrafts.content)
        } else {
            redirect(action:list)
        }
    }
    
    Map GetPrintParams() {
		return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
		        contest:session.lastContest,
		        lang:session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'
		       ]
    }
}
