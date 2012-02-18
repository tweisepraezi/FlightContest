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

    def updatereturn = {
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

    def updatenext = {
        def coordresult = fcService.updateCoordResult(params) 
        if (coordresult.saved) {
            flash.message = coordresult.message

			// search next id
			boolean set_next = false
			CoordResult coordresult_nextinstance = null
			CoordResult coordresult_nextinstance2 = null
			int leg_no = 0
			for ( CoordResult coordresult_instance in CoordResult.findAllByTest(coordresult.instance.test) ) {
				if (!coordresult_nextinstance) {
					if (coordresult_instance.planProcedureTurn) {
						leg_no++
					}
					leg_no++
				}
				if (set_next) {
					if (!coordresult_nextinstance) {
						coordresult_nextinstance = coordresult_instance
					} else if (!coordresult_nextinstance2) {
						coordresult_nextinstance2 = coordresult_instance
						break
					}
				}
				if (coordresult_instance == coordresult.instance) {
					set_next = true
				}
			}
			
			if (coordresult_nextinstance2) {
				redirect(action:'edit',id:coordresult_nextinstance.id,params:[name:leg_no,next:coordresult_nextinstance2.id])
			} else if (coordresult_nextinstance) {
				redirect(action:'edit',id:coordresult_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"test",action:'flightresults',id:params.testid)
			}
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
