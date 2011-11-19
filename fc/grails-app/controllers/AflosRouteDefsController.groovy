class AflosRouteDefsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
		fcService.println "List AFLOS routedefs"
		
        session.lastAflosController = controllerName
        
        // params.sort = "registration"
        [ aflosRouteDefsInstanceList: AflosRouteDefs.list( params ), aflosRouteDefsInstanceTotal: AflosRouteDefs.count() ]
    }
}
