class AflosErrorPointsController {

    def fcService
    
    def index = { redirect(action:list,params:params) }

    def list = {
		fcService.println "List AFLOS errorpoints"
		
        session.lastAflosController = controllerName
        
        params.sort = "id"
        [ aflosErrorPointsInstanceList: AflosErrorPoints.list( params ) ]
    }
	
	def crew = {
		fcService.println "List AFLOS errorpoints of startnum $params.startnum and route '$params.routename'"
		
        AflosRouteNames aflosroutenames_instance = AflosRouteNames.findByName(params.routename) 
		
		[ aflosErrorPointsInstanceList: AflosErrorPoints.findAllByStartnumAndRoutename(params.startnum,aflosroutenames_instance) ]
	}
}
