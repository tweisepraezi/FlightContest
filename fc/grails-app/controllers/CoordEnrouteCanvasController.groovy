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
            long next_routeid = coordenroutecanvas.instance.route.GetNextID()
            if (next_routeid) {
                redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id,params:[next:next_routeid])
            } else {
                redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
            }
        } else if (coordenroutecanvas.error) {
            flash.message = coordenroutecanvas.message
            flash.error = true
            Map n = search_next_id(coordenroutecanvas.instance,"")
            if (n.i1) {
                redirect(action:edit, id:params.id, params:[name:n.no,next:n.i1.id])
            } else {
                redirect(action:edit, id:params.id)
            }
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
            Map n = search_next_id(coordenroutecanvas.instance,params.nextid)
            if (coordenroutecanvas.error) {
                if (n.i1) {
                    redirect(action:edit, id:params.id, params:[name:n.no,next:n.i1.id])
                } else {
                    redirect(action:edit, id:params.id)
                }
            } else {
                if (n.i2) {
                    redirect(action:edit,id:n.i1.id,params:[name:n.no,next:n.i2.id])
                } else if (n.i1) {
                    redirect(action:edit,id:n.i1.id,params:[name:n.no])
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
            Map n = search_next_id(coordenroutecanvas.instance,"")
			if (n.i2) {
				redirect(action:'edit',id:n.i1.id,params:[name:n.no,next:n.i2.id])
			} else if (n.i1) {
				redirect(action:'edit',id:n.i1.id,params:[name:n.no])
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
        long next_routeid = coordenroutecanvas_instance.route.GetNextID()
        def coordenroutecanvas = fcService.deleteCoordEnrouteCanvas(params)
        if (coordenroutecanvas.deleted) {
        	flash.message = coordenroutecanvas.message
            if (next_routeid) {
                redirect(controller:"route",action:show,id:coordenroutecanvas.routeid,params:[next:next_routeid])
            } else {
        	    redirect(controller:"route",action:show,id:coordenroutecanvas.routeid)
            }
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
            Map n = search_next_id(coordenroutecanvas.instance,"")
  			flash.message = coordenroutecanvas.message
            if (n.i1) {
                redirect(action:edit, id:params.id, params:[name:n.no,next:n.i1.id])
            } else {
            redirect(action:edit, id:params.id)
            }
               
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
            long next_routeid = coordenroutecanvas.instance.route.GetNextID()
            if (next_routeid) {
                redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id,params:[next:next_routeid])
            } else {
        	    redirect(controller:"route",action:show,id:coordenroutecanvas.instance.route.id)
            }
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
	}
    
    private Map search_next_id(CoordEnrouteCanvas coordEnrouteInstance, String nextID)
    {
        boolean set_next = false
        CoordEnrouteCanvas coordenroutecanvas_nextinstance = null
        CoordEnrouteCanvas coordenroutecanvas_nextinstance2 = null
        int leg_no = 0
        for (CoordEnrouteCanvas coordroute_instance in CoordEnrouteCanvas.findAllByRoute(coordEnrouteInstance.route,[sort:"enrouteViewPos"]) ) {
            if (!coordenroutecanvas_nextinstance) {
                leg_no++
            }
            if (set_next) {
                if (!coordenroutecanvas_nextinstance) {
                    coordenroutecanvas_nextinstance = coordroute_instance
                } else if (!coordenroutecanvas_nextinstance2) {
                    coordenroutecanvas_nextinstance2 = coordroute_instance
                    break
                }
            }
            if (nextID) {
                if (coordroute_instance.id == nextID.toLong()) {
                    coordenroutecanvas_nextinstance = coordroute_instance
                    set_next = true
                }
            } else {
                if (coordroute_instance == coordEnrouteInstance) {
                    set_next = true
                }
            }
        }
        return [i1:coordenroutecanvas_nextinstance, i2:coordenroutecanvas_nextinstance2, no:leg_no]
    }
}
