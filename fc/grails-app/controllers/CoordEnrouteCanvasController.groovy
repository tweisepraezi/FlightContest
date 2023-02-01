class CoordEnrouteCanvasController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def coordenroutecanvas = fcService.getCoordEnrouteCanvas(params) 
        if (coordenroutecanvas.instance) {
        	return [coordEnrouteCanvasInstance:coordenroutecanvas.instance]
        } else {
            flash.message = coordenroutecanvas.message
            redirect(controller:"contest",action:"start")
        }
    }

    def create = {
        def coordenroutecanvas = fcService.createCoordEnrouteCanvas(params)
        if (coordenroutecanvas.error) {
            flash.error = coordenroutecanvas.error
            flash.message = coordenroutecanvas.message
            redirect(controller:"route",action:show,id:params.routeid)
        } else {
            return [coordEnrouteCanvasInstance:coordenroutecanvas.instance]
        }
    }

    def save = {
        def coordenroutecanvas = fcService.saveCoordEnrouteCanvas(session.showLanguage, params) 
        if (coordenroutecanvas.saved) {
            flash.message = coordenroutecanvas.message
            if (coordenroutecanvas.error) {
                flash.error = true
            } 
            redirect(controller:"route",action:show,id:params.routeid)
        } else if (coordenroutecanvas.error) {
            render(view:'create',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
        } else {
            render(view:'create',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
        }
    }

    def saveadd = {
        def coordenroutecanvas = fcService.saveCoordEnrouteCanvas(session.showLanguage, params) 
        if (coordenroutecanvas.saved) {
            flash.message = coordenroutecanvas.message
            if (coordenroutecanvas.error) {
                flash.error = true
            } 
            redirect(controller:'coordEnrouteCanvas',action:'create',params:['route.id':params.routeid,'routeid':params.routeid])
        } else if (coordenroutecanvas.error) {
            render(view:'create',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
        } else {
            render(view:'create',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
        }
    }

    def edit = {
        def coordenroutecanvas = fcService.getCoordEnrouteCanvas(params) 
        if (coordenroutecanvas.instance) {
            if (coordenroutecanvas.instance.route.enrouteCanvasRoute == EnrouteRoute.InputmmFromTP && !coordenroutecanvas.instance.measureDistance) {
                coordenroutecanvas.instance.measureDistance = coordenroutecanvas.instance.coordMeasureDistance
            }
            return [coordEnrouteCanvasInstance:coordenroutecanvas.instance]
        } else {
            flash.message = coordenroutecanvas.message
            redirect(controller:"contest",action:"start")
        }
    }

    def updatereturn = {
        def coordenroutecanvas = fcService.updateCoordEnrouteCanvas(session.showLanguage, params)
        if (coordenroutecanvas.saved) {
            flash.message = coordenroutecanvas.message
            redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
        } else if (coordenroutecanvas.error) {
            flash.message = coordenroutecanvas.message
            flash.error = true
            redirect(action:edit, id:params.id)
        } else if (coordenroutecanvas.instance) {
            render(view:'edit',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
        } else {
            flash.message = coordenroutecanvas.message
            redirect(action:edit,id:params.id)
        }
    }

    def updatenext = {
        def coordenroutecanvas = fcService.updateCoordEnrouteCanvas(session.showLanguage, params)
        if (coordenroutecanvas.saved || coordenroutecanvas.error) {
            flash.message = coordenroutecanvas.message
            flash.error = coordenroutecanvas.error
            if (coordenroutecanvas.error) {
                redirect(action:edit, id:params.id)
            } else {
				long next_id = coordenroutecanvas.instance.GetNextCoordEnrouteCanvasID()
                if (next_id) {
                    redirect(action:edit,id:next_id)
                } else {
                    redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
                }
            }
        } else if (coordenroutecanvas.instance) {
            render(view:'edit',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
        } else {
            flash.message = coordenroutecanvas.message
            redirect(action:edit,id:params.id)
        }
    }

	def gotonext = {
        def coordenroutecanvas = fcService.getCoordEnrouteCanvas(params)
		if (coordenroutecanvas.instance) {
			long next_id = coordenroutecanvas.instance.GetNextCoordEnrouteCanvasID()
			if (next_id) {
				redirect(action:'edit',id:next_id)
			} else {
				redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
			}
		} else {
			flash.message = coordenroutecanvas.message
			redirect(action:edit,id:params.id)
		}
	}
    
	def gotoprev = {
        def coordenroutecanvas = fcService.getCoordEnrouteCanvas(params)
		if (coordenroutecanvas.instance) {
			long prev_id = coordenroutecanvas.instance.GetPrevCoordEnrouteCanvasID()
			if (prev_id) {
				redirect(action:'edit',id:prev_id)
			} else {
				redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
			}
		} else {
			flash.message = coordenroutecanvas.message
			redirect(action:edit,id:params.id)
		}
	}
    
    def delete = {
        CoordEnrouteCanvas coordenroutecanvas_instance = CoordEnrouteCanvas.get(params.id)
        def coordenroutecanvas = fcService.deleteCoordEnrouteCanvas(params)
        if (coordenroutecanvas.deleted) {
        	flash.message = coordenroutecanvas.message
      	    redirect(controller:"route",action:show,id:coordenroutecanvas.routeid)
        } else if (coordenroutecanvas.notdeleted) {
        	flash.message = coordenroutecanvas.message
            redirect(action:show,id:params.id)
        } else if (coordenroutecanvas.error) {
			flash.error = coordenroutecanvas.error
			flash.message = coordenroutecanvas.message
			redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
        } else {
        	flash.message = coordenroutecanvas.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def removeall = {
        def coordenroutecanvas = fcService.removeallCoordEnrouteCanvas(params)
        flash.message = coordenroutecanvas.message
        redirect(controller:"route",action:show,id:params.routeid)
    }
    
    def reset = {
		def coordenroutecanvas = fcService.resetmeasureCoordEnrouteCanvas(params)
        if (coordenroutecanvas.saved) {
			redirect(action:edit, id:params.id)
        } else if (coordenroutecanvas.error) {
            flash.error = coordenroutecanvas.error
            flash.message = coordenroutecanvas.message
            redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
		} else if (coordenroutecanvas.instance) {
			render(view:'edit',model:[coordEnrouteCanvasInstance:coordenroutecanvas.instance])
		} else {
			flash.message = coordenroutecanvas.message
			redirect(action:edit,id:params.id)
		}
	}
    
    def addposition = {
        def coordenroutecanvas = fcService.addpositionCoordEnrouteCanvas(params)
        flash.error = coordenroutecanvas.error
        flash.message = coordenroutecanvas.message
        redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
    }
    
    def subposition = {
        def coordenroutecanvas = fcService.subpositionCoordEnrouteCanvas(params)
        flash.error = coordenroutecanvas.error
        flash.message = coordenroutecanvas.message
        redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
    }
	
	def cancel = {
        def coordenroutecanvas = fcService.getCoordEnrouteCanvas(params) 
        if (coordenroutecanvas.instance) {
       	    redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
	}
}
