class AflosErrorPointsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
		fcService.println "List AFLOS errorpoints"
		
        session.lastAflosController = controllerName
        
        params.sort = "id"
        [ aflosErrorPointsInstanceList: AflosErrorPoints.list( params ) ]
    }
}
