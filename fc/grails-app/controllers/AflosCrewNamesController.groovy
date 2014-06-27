class AflosCrewNamesController 
{
    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
    	
        params.sort = "startnum"
		if (session?.lastContest) {
			session.lastContest.refresh()
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS crewnames (aflostest)"
				return [contestInstance:session.lastContest, aflosCrewNamesInstanceList: AflosCrewNames.aflostest.list( params ), aflosCrewNamesInstanceTotal: AflosCrewNames.aflostest.count()]
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS crewnames (aflosupload)"
				return [contestInstance:session.lastContest, aflosCrewNamesInstanceList: AflosCrewNames.aflosupload.list( params ), aflosCrewNamesInstanceTotal: AflosCrewNames.aflosupload.count()]
			}
		}
		fcService.println "List AFLOS crewnames (aflos)"
        return [aflosCrewNamesInstanceList: AflosCrewNames.aflos.list( params ), aflosCrewNamesInstanceTotal: AflosCrewNames.aflos.count()]
    }
}
