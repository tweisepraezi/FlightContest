class AflosCheckPointsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
        
        params.sort = "id"
		if (session?.lastContest) {
			session.lastContest.refresh()
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS checkpoints (aflostest)"
				return [contestInstance:session.lastContest, aflosCheckPointsInstanceList: AflosCheckPoints.aflostest.list(params)]
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS checkpoints (aflosupload)"
				return [contestInstance:session.lastContest, aflosCheckPointsInstanceList: AflosCheckPoints.aflosupload.list(params)]
			}
		}
		fcService.println "List AFLOS checkpoints (aflos)"
        return [aflosCheckPointsInstanceList: AflosCheckPoints.aflos.list(params)]
    }
}
