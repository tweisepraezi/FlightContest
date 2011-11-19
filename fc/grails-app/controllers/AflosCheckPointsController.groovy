class AflosCheckPointsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
        session.lastAflosController = controllerName
        
        params.sort = "id"
        [ aflosCheckPointsInstanceList: AflosCheckPoints.list( params ) ]
    }
}
