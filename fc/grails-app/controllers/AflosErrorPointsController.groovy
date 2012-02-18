class AflosErrorPointsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
        
        params.sort = "id"
		if (session?.lastContest) {
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS errorpoints (aflostest)"
		        return [contestInstance:session.lastContest, aflosErrorPointsInstanceList: AflosErrorPoints.aflostest.list(params)]
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS errorpoints (aflosupload)"
		        return [contestInstance:session.lastContest, aflosErrorPointsInstanceList: AflosErrorPoints.aflosupload.list(params)]
			}
		}
		fcService.println "List AFLOS errorpoints (aflos)"
        return [aflosErrorPointsInstanceList: AflosErrorPoints.aflos.list(params)]
    }
	
	def crew = {
		if (session?.lastContest) {
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS errorpoints of startnum $params.startnum and route '$params.routename' (aflostest)"
		        AflosRouteNames aflosroutenames_instance = AflosRouteNames.aflostest.findByName(params.routename) 
				return [contestInstance:session.lastContest, aflosErrorPointsInstanceList: AflosErrorPoints.aflostest.findAllByStartnumAndRoutename(params.startnum,aflosroutenames_instance)]
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS errorpoints of startnum $params.startnum and route '$params.routename' (aflosupload)"
		        AflosRouteNames aflosroutenames_instance = AflosRouteNames.aflosupload.findByName(params.routename) 
				return [contestInstance:session.lastContest, aflosErrorPointsInstanceList: AflosErrorPoints.aflosupload.findAllByStartnumAndRoutename(params.startnum,aflosroutenames_instance)]
			}
		}
		fcService.println "List AFLOS errorpoints of startnum $params.startnum and route '$params.routename' (aflos)"
        AflosRouteNames aflosroutenames_instance = AflosRouteNames.aflos.findByName(params.routename) 
		return [aflosErrorPointsInstanceList: AflosErrorPoints.aflos.findAllByStartnumAndRoutename(params.startnum,aflosroutenames_instance)]
	}
}
