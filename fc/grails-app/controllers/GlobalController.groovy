class GlobalController {

	def fcService

    def index = { 
    	redirect(action:list,params:params) 
    }

    def list = {
    	[globalInstance:BootStrap.global,contestInstance:session.lastContest]
    }

    def listall = {
            [globalInstance:BootStrap.global,contestInstance:session.lastContest]
        }

    def changeglobalsettings = {
		if (!session.showLanguage) {
			session.showLanguage = Languages.de.toString()
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
		fcService.SetCookie(response, "ShowLimitCrewNum", params.showLimitCrewNum)
		
    	redirect(controller:'contest',action:'start',params:[lang:params.showLanguage])
    }
    
    def cancel = {
        redirect(action:list)
    }
}
