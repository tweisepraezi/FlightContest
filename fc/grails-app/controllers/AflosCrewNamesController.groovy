class AflosCrewNamesController 
{
    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
    	
        // params.sort = "registration"
        [ aflosCrewNamesInstanceList: AflosCrewNames.list( params ), aflosCrewNamesInstanceTotal: AflosCrewNames.count() ]
    }
}
