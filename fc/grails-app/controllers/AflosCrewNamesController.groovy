class AflosCrewNamesController 
{
    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
    	
        def contest_instance = null
        def afloscrewnames_list = null
        int afloscrewnames_count = 0
		if (session?.lastContest) {
			session.lastContest.refresh()
            contest_instance = session.lastContest
			if (session.lastContest.aflosTest) {
				fcService.println "List AFLOS crewnames (aflostest)"
                afloscrewnames_list = AflosCrewNames.aflostest.findAllByPointsNotEqual(0,[sort:'startnum'])
                afloscrewnames_count = AflosCrewNames.aflostest.countByPointsNotEqual(0)
			} else if (session.lastContest.aflosUpload) {
				fcService.println "List AFLOS crewnames (aflosupload)"
                afloscrewnames_list = AflosCrewNames.aflosupload.findAllByPointsNotEqual(0,[sort:'startnum'])
                afloscrewnames_count = AflosCrewNames.aflosupload.countByPointsNotEqual(0)
			}
		}
        if (!afloscrewnames_list) {
            fcService.println "List AFLOS crewnames (aflos)"
            afloscrewnames_list = AflosCrewNames.aflos.findAllByPointsNotEqual(0,[sort:'startnum'])
            afloscrewnames_count = AflosCrewNames.aflos.countByPointsNotEqual(0)
        }
        return [contestInstance:contest_instance, aflosCrewNamesInstanceList:afloscrewnames_list, aflosCrewNamesInstanceTotal: afloscrewnames_count]
    }
}
