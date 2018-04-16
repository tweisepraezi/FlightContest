class CoordEnroutePhotoController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def coordenroutephoto = fcService.getCoordEnroutePhoto(params) 
        if (coordenroutephoto.instance) {
        	return [coordEnroutePhotoInstance:coordenroutephoto.instance]
        } else {
            flash.message = coordenroutephoto.message
            redirect(controller:"contest",action:"start")
        }
    }

    def create = {
        def coordenroutephoto = fcService.createCoordEnroutePhoto(params)
        if (coordenroutephoto.error) {
            flash.error = coordenroutephoto.error
            flash.message = coordenroutephoto.message
            redirect(controller:"route",action:show,id:params.routeid)
        } else {
            return [coordEnroutePhotoInstance:coordenroutephoto.instance]
        }
    }

    def save = {
        def coordenroutephoto = fcService.saveCoordEnroutePhoto(session.showLanguage, params) 
        if (coordenroutephoto.error) {
            render(view:'create',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
        } else if (coordenroutephoto.saved) {
            flash.message = coordenroutephoto.message
            redirect(controller:"route",action:show,id:params.routeid)
        } else {
            render(view:'create',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
        }
    }

    def saveadd = {
        def coordenroutephoto = fcService.saveCoordEnroutePhoto(session.showLanguage, params) 
        if (coordenroutephoto.error) {
            render(view:'create',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
        } else if (coordenroutephoto.saved) {
            flash.message = coordenroutephoto.message
            redirect(controller:'coordEnroutePhoto',action:'create',params:['route.id':params.routeid,'routeid':params.routeid])
        } else {
            render(view:'create',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
        }
    }
    
    def edit = {
        def coordenroutephoto = fcService.getCoordEnroutePhoto(params) 
        if (coordenroutephoto.instance) {
            if (coordenroutephoto.instance.route.enroutePhotoRoute == EnrouteRoute.InputmmFromTP && !coordenroutephoto.instance.measureDistance) {
                coordenroutephoto.instance.measureDistance = coordenroutephoto.instance.coordMeasureDistance
            }
            return [coordEnroutePhotoInstance:coordenroutephoto.instance]
        } else {
            flash.message = coordenroutephoto.message
            redirect(controller:"contest",action:"start")
        }
    }

    def updatereturn = {
        def coordenroutephoto = fcService.updateCoordEnroutePhoto(session.showLanguage, params) 
        if (coordenroutephoto.saved) {
        	flash.message = coordenroutephoto.message
            long next_routeid = coordenroutephoto.instance.route.GetNextID()
            if (next_routeid) {
                redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id,params:[next:next_routeid])
            } else {
                redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
            }
        } else if (coordenroutephoto.error) {
            flash.message = coordenroutephoto.message
            flash.error = true
            Map n = search_next_id(coordenroutephoto.instance,"")
            if (n.i1) {
                redirect(action:edit, id:params.id, params:[name:n.no,next:n.i1.id])
            } else {
                redirect(action:edit, id:params.id)
            }
        } else if (coordenroutephoto.instance) {
        	render(view:'edit',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
        } else {
        	flash.message = coordenroutephoto.message
            redirect(action:edit,id:params.id)
        }
    }

    def updatenext = {
        def coordenroutephoto = fcService.updateCoordEnroutePhoto(session.showLanguage, params)
        if (coordenroutephoto.saved || coordenroutephoto.error) {
            flash.message = coordenroutephoto.message
            flash.error = coordenroutephoto.error
            Map n = search_next_id(coordenroutephoto.instance,params.nextid)
            if (coordenroutephoto.error) {
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
    				redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
    			}
            }
        } else if (coordenroutephoto.instance) {
        	render(view:'edit',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
        } else {
        	flash.message = coordenroutephoto.message
            redirect(action:edit,id:params.id)
        }
    }

	def gotonext = {
        def coordenroutephoto = fcService.getCoordEnroutePhoto(params)
		if (coordenroutephoto.instance) {
            Map n = search_next_id(coordenroutephoto.instance,"")
			if (n.i2) {
				redirect(action:'edit',id:n.i1.id,params:[name:n.no,next:n.i2.id])
			} else if (n.i1) {
				redirect(action:'edit',id:n.i1.id,params:[name:n.no])
			} else {
				redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
			}
		} else {
			flash.message = coordenroutephoto.message
			redirect(action:edit,id:params.id)
		}
	}
    
    def delete = {
        CoordEnroutePhoto coordenroutephoto_instance = CoordEnroutePhoto.get(params.id)
        long next_routeid = coordenroutephoto_instance.route.GetNextID()
        def coordenroutephoto = fcService.deleteCoordEnroutePhoto(params)
        if (coordenroutephoto.deleted) {
        	flash.message = coordenroutephoto.message
            if (next_routeid) {
                redirect(controller:"route",action:show,id:coordenroutephoto.routeid,params:[next:next_routeid])
            } else {
        	    redirect(controller:"route",action:show,id:coordenroutephoto.routeid)
            }
        } else if (coordenroutephoto.notdeleted) {
        	flash.message = coordenroutephoto.message
            redirect(action:show,id:params.id)
        } else if (coordenroutephoto.error) {
			flash.error = coordenroutephoto.error
			flash.message = coordenroutephoto.message
			redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
        } else {
        	flash.message = coordenroutephoto.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def removeall = {
        def coordenroutephoto = fcService.removeallCoordEnroutePhoto(params)
        flash.message = coordenroutephoto.message
        redirect(controller:"route",action:show,id:params.routeid)
    }
    
    def reset = {
		def coordenroutephoto = fcService.resetmeasureCoordEnroutePhoto(params)
        if (coordenroutephoto.saved) {
            Map n = search_next_id(coordenroutephoto.instance,"")
  			flash.message = coordenroutephoto.message
            if (n.i1) {
                redirect(action:edit, id:params.id, params:[name:n.no,next:n.i1.id])
            } else {
                redirect(action:edit, id:params.id)
            }
        } else if (coordenroutephoto.error) {
            flash.error = coordenroutephoto.error
            flash.message = coordenroutephoto.message
            redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
		} else if (coordenroutephoto.instance) {
			render(view:'edit',model:[coordEnroutePhotoInstance:coordenroutephoto.instance])
		} else {
			flash.message = coordenroutephoto.message
			redirect(action:edit,id:params.id)
		}
	}
    
    def addposition = {
        def coordenroutephoto = fcService.addpositionCoordEnroutePhoto(params)
        flash.error = coordenroutephoto.error
        flash.message = coordenroutephoto.message
        redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
    }
    
    def subposition = {
        def coordenroutephoto = fcService.subpositionCoordEnroutePhoto(params)
        flash.error = coordenroutephoto.error
        flash.message = coordenroutephoto.message
        redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
    }
	
	def cancel = {
        def coordenroutephoto = fcService.getCoordEnroutePhoto(params) 
        if (coordenroutephoto.instance) {
            long next_routeid = coordenroutephoto.instance.route.GetNextID()
            if (next_routeid) {
                redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id,params:[next:next_routeid])
            } else {
        	    redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
            }
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
	}
    
    private Map search_next_id(CoordEnroutePhoto coordEnrouteInstance, String nextID)
    {
        boolean set_next = false
        CoordEnroutePhoto coordenroutephoto_nextinstance = null
        CoordEnroutePhoto coordenroutephoto_nextinstance2 = null
        int leg_no = 0
        for (CoordEnroutePhoto coordroute_instance in CoordEnroutePhoto.findAllByRoute(coordEnrouteInstance.route,[sort:"enrouteViewPos"]) ) {
            if (!coordenroutephoto_nextinstance) {
                leg_no++
            }
            if (set_next) {
                if (!coordenroutephoto_nextinstance) {
                    coordenroutephoto_nextinstance = coordroute_instance
                } else if (!coordenroutephoto_nextinstance2) {
                    coordenroutephoto_nextinstance2 = coordroute_instance
                    break
                }
            }
            if (nextID) {
                if (coordroute_instance.id == nextID.toLong()) {
                    coordenroutephoto_nextinstance = coordroute_instance
                    set_next = true
                }
            } else {
                if (coordroute_instance == coordEnrouteInstance) {
                    set_next = true
                }
            }
        }
        return [i1:coordenroutephoto_nextinstance, i2:coordenroutephoto_nextinstance2, no:leg_no]
    }
}
