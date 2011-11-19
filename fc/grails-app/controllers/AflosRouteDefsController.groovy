class AflosRouteDefsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
        
        // params.sort = "registration"
        [ aflosRouteDefsInstanceList: AflosRouteDefs.list( params ), aflosRouteDefsInstanceTotal: AflosRouteDefs.count() ]
    }
}
