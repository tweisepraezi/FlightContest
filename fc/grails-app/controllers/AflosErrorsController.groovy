class AflosErrorsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
		fcService.println "List AFLOS errors"
		
        session.lastAflosController = controllerName
        
        params.sort = "id"
        [ aflosErrorsInstanceList: AflosErrors.list( params ) ]
    }
}
