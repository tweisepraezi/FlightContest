class AflosErrorsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
        
        params.sort = "id"
		if (session?.lastContest) {
			session.lastContest.refresh()
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS errors (aflostest)"
		        return [contestInstance:session.lastContest, aflosErrorsInstanceList: AflosErrors.aflostest.list(params)]
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS errors (aflosupload)"
		        return [contestInstance:session.lastContest, aflosErrorsInstanceList: AflosErrors.aflosupload.list(params)]
			}
		}
		fcService.println "List AFLOS errors (aflos)"
        return [aflosErrorsInstanceList: AflosErrors.aflos.list(params)]
    }
}
