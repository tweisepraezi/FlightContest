class CoordResultController 
{
    
	def fcService
	
    def edit = {
        def coordresult = fcService.getCoordResult(params) 
        if (coordresult.instance) {
            return [coordResultInstance:coordresult.instance]
        } else {
            flash.message = coordresult.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def editprocedureturn = {
        def coordresult = fcService.getCoordResult(params) 
        if (coordresult.instance) {
            return [coordResultInstance:coordresult.instance]
        } else {
            flash.message = coordresult.message
            redirect(controller:"task",action:"startresults")
        }
    }

    def update = {
        def coordresult = fcService.updateCoordResult(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }

    def reset = {
        def coordresult = fcService.resetCoordResult(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }

    def updateprocedureturn = {
        def coordresult = fcService.updateCoordResultProcedureTurn(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        } else if (coordresult.instance) {
            if (coordresult.error) {
                flash.message = coordresult.message
                flash.error = true
            }
            render(view:'edit',model:[coordResultInstance:coordresult.instance])
        } else {
            flash.message = coordresult.message
            redirect(controller:"test",action:'flightresults',id:params.testid)
        }
    }

    def cancel = {
        redirect(controller:"test",action:'flightresults',id:params.testid)
    }
}
