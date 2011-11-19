class AflosCheckPointsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
		fcService.println "List AFLOS checkpoints"
		
        session.lastAflosController = controllerName
        
        params.sort = "id"
        [ aflosCheckPointsInstanceList: AflosCheckPoints.list( params ) ]
    }
}
