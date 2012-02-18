class AflosRouteDefsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
        
		if (session?.lastContest) {
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS routedefs (aflostest)"
				return [contestInstance:session.lastContest, aflosRouteDefsInstanceList: AflosRouteDefs.aflostest.list(params), aflosRouteDefsInstanceTotal: AflosRouteDefs.aflostest.count()]
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS routedefs (aflosupload)"
				return [contestInstance:session.lastContest, aflosRouteDefsInstanceList: AflosRouteDefs.aflosupload.list(params), aflosRouteDefsInstanceTotal: AflosRouteDefs.aflosupload.count()]
			}
		}
		fcService.println "List AFLOS routedefs (aflos)"
        return [aflosRouteDefsInstanceList: AflosRouteDefs.aflos.list(params), aflosRouteDefsInstanceTotal: AflosRouteDefs.aflos.count()]
    }
}
