class GlobalController {

	def fcService

    def index = { 
    	redirect(action:list,params:params) 
    }

    def list = {
		fcService.println "List extras"
		// save return action
		session.crewReturnAction = actionName 
		session.crewReturnController = controllerName
		session.crewReturnID = params.id
		session.aircraftReturnAction = actionName
		session.aircraftReturnController = controllerName
		session.aircraftReturnID = params.id
		session.teamReturnAction = actionName
		session.teamReturnController = controllerName
		session.teamReturnID = params.id
    	[globalInstance:BootStrap.global,contestInstance:session.lastContest]
    }

    def listall = {
		fcService.println "List extras (all)"
		// save return action
		session.crewReturnAction = actionName 
		session.crewReturnController = controllerName
		session.crewReturnID = params.id
		session.aircraftReturnAction = actionName
		session.aircraftReturnController = controllerName
		session.aircraftReturnID = params.id
		session.teamReturnAction = actionName
		session.teamReturnController = controllerName
		session.teamReturnID = params.id
        [globalInstance:BootStrap.global,contestInstance:session.lastContest]
    }

    def changeglobalsettings = {
		if (!session.showLanguage) {
			session.showLanguage = Languages.de.toString()
		}
		if (!session.printLanguage) {
			session.printLanguage = Languages.de.toString()
		}
		if (!session.showLimitCrewNum) {
			session.showLimitCrewNum = 10
		}
        [globalInstance:BootStrap.global]
    }

    def update = {
		session.showLanguage = params.showLanguage
		session.showLimitCrewNum = params.showLimitCrewNum.toInteger()
		if (params.lastShowLimitCrewNum != params.showLimitCrewNum) {
			session.showLimitStartPos = 0
		}
		
		fcService.SetCookie(response, "ShowLanguage",     params.showLanguage)
		fcService.SetCookie(response, "PrintLanguage",    params.printLanguage)
		fcService.SetCookie(response, "ShowLimitCrewNum", params.showLimitCrewNum)
		
    	redirect(controller:'contest',action:'start',params:[lang:params.showLanguage])
    }
    
    def cancel = {
        redirect(action:list)
    }
}
