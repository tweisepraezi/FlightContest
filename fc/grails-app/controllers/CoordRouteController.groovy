import java.util.Map
import org.springframework.web.multipart.MultipartFile

class CoordRouteController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	return [coordRouteInstance:coordroute.instance]
        } else {
            flash.message = coordroute.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	return [coordRouteInstance:coordroute.instance]
        } else {
            flash.message = coordroute.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit_object = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	return [coordRouteInstance:coordroute.instance]
        } else {
            flash.message = coordroute.message
            redirect(controller:"contest",action:"start")
        }
    }

    def updatereturn = {
        def coordroute = fcService.updateCoordRoute(session.showLanguage, params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
            long next_routeid = coordroute.instance.route.GetNextRouteID()
            if (next_routeid) {
                redirect(controller:"route",action:'show',id:coordroute.instance.route.id,params:[next:next_routeid])
            } else {
                redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
            }
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:'edit',id:params.id)
        }
    }

    def updatereturn_object = {
        def coordroute = fcService.updateCoordRouteObject(session.showLanguage, params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
            long next_routeid = coordroute.instance.route.GetNextRouteID()
            if (next_routeid) {
                redirect(controller:"route",action:'show',id:coordroute.instance.route.id,params:[next:next_routeid])
            } else {
                redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
            }
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit_object',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:'edit_object',id:params.id)
        }
    }

    def updatenext = {
        def coordroute = fcService.updateCoordRoute(session.showLanguage, params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
			long next_id = coordroute.instance.GetNextCoordRouteID(false)
			if (next_id) {
				redirect(action:'edit',id:next_id)
			} else {
				redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
			}
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:'edit',id:params.id)
        }
    }
	
    def updatenext_object = {
        def coordroute = fcService.updateCoordRouteObject(session.showLanguage, params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
			long next_id = coordroute.instance.GetNextCoordRouteID(true)
			if (next_id) {
				redirect(action:'edit_object',id:next_id)
			} else {
				redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
			}
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit_object',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:'edit_object',id:params.id)
        }
    }
	
	def gotonext = {
        def coordroute = fcService.getCoordRoute(params)
		if (coordroute.instance) {
			long next_id = coordroute.instance.GetNextCoordRouteID(false)
			if (next_id) {
				redirect(action:'edit',id:next_id)
			} else {
				redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
			}
		} else {
			flash.message = coordroute.message
			redirect(action:'edit',id:params.id)
		}
	}

	def gotoprev = {
        def coordroute = fcService.getCoordRoute(params)
		if (coordroute.instance) {
			long prev_id = coordroute.instance.GetPrevCoordRouteID(false)
			if (prev_id) {
				redirect(action:'edit',id:prev_id)
			} else {
				redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
			}
		} else {
			flash.message = coordroute.message
			redirect(action:'edit',id:params.id)
		}
	}

	def gotonext_object = {
        def coordroute = fcService.getCoordRoute(params)
		if (coordroute.instance) {
			long next_id = coordroute.instance.GetNextCoordRouteID(true)
			if (next_id) {
				redirect(action:'edit_object',id:next_id)
			} else {
				redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
			}
		} else {
			flash.message = coordroute.message
			redirect(action:'edit_object',id:params.id)
		}
	}

	def gotoprev_object = {
        def coordroute = fcService.getCoordRoute(params)
		if (coordroute.instance) {
			long prev_id = coordroute.instance.GetPrevCoordRouteID(true)
			if (prev_id) {
				redirect(action:'edit_object',id:prev_id)
			} else {
				redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
			}
		} else {
			flash.message = coordroute.message
			redirect(action:'edit_object',id:params.id)
		}
	}

    def create = {
		def coordroute = fcService.createCoordRoute(params)
		if (coordroute.error) {
			flash.error = coordroute.error
            flash.message = coordroute.message
            redirect(controller:"route",action:'show',id:params.routeid)
		} else {
			boolean exist_ifp = CoordRoute.findByRouteAndType(coordroute.instance.route,CoordType.iFP,[sort:"id", order:"desc"])
			return [coordRouteInstance:coordroute.instance,existiFP:exist_ifp]
		}
    }

    def save = {
        def coordroute = fcService.saveCoordRoute(session.showLanguage, params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
        	redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
        } else {
            render(view:'create',model:[coordRouteInstance:coordroute.instance])
        }
    }

    def delete = {
        CoordRoute coordroute_instance = CoordRoute.get(params.id)
        long next_routeid = coordroute_instance.route.GetNextRouteID()
        def coordroute = fcService.deleteCoordRoute(params)
        if (coordroute.deleted) {
        	flash.message = coordroute.message
            if (next_routeid) {
                redirect(controller:"route",action:'show',id:coordroute.routeid,params:[next:next_routeid])
            } else {
                redirect(controller:"route",action:'show',id:coordroute.routeid)
            }   
        } else if (coordroute.notdeleted) {
        	flash.message = coordroute.message
            redirect(action:'show',id:params.id)
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
        } else {
        	flash.message = coordroute.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def reset = {
		def coordroute = fcService.resetmeasureCoordRoute(params)
		if (coordroute.saved) {
			long next_id = coordroute.instance.GetNextCoordRouteID(false)
			flash.message = coordroute.message
            if (next_id) {
                redirect(action:'edit', id:params.id, params:[next:next_id])
            } else {
			    redirect(action:'edit', id:params.id)
            }
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
		} else if (coordroute.instance) {
			render(view:'edit',model:[coordRouteInstance:coordroute.instance])
		} else {
			flash.message = coordroute.message
			redirect(action:'edit',id:params.id)
		}
	}
	
	def cancel = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
            long next_routeid = coordroute.instance.route.GetNextRouteID()
            if (next_routeid) {
                redirect(controller:"route",action:'show',id:coordroute.instance.route.id,params:[next:next_routeid])
            } else {
        	    redirect(controller:"route",action:'show',id:coordroute.instance.route.id)
            }
        } else {
            redirect(controller:"route",action:'show',id:params.routeid)
        }
	}
    
    def setposition = {
        def coordenroutephoto = fcService.setpositionTurnpointPhoto(params)
        flash.message = coordenroutephoto.message
        redirect(action:'edit_object',id:params.id,params:[next:params.next])
    }
    
	def selectimagefilename = {
		[:]
    }
	
	def selectimagefilename_cancel = {
        redirect(action:"edit_object", id:params.id)
    }
    
	def loadimage = {
		MultipartFile image_file = request.getFile("imagefile")
		if (image_file && !image_file.isEmpty()) {
            fcService.importTurnpointPhoto(params, image_file) 
        }
        redirect(action:"edit_object", id:params.id)
    }
    
}
