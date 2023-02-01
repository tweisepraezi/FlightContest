import org.springframework.web.multipart.MultipartFile

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
            redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
        } else if (coordenroutephoto.error) {
            flash.message = coordenroutephoto.message
            flash.error = true
            redirect(action:edit, id:params.id)
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
			long next_id = coordenroutephoto.instance.GetNextCoordEnroutePhotoID()
            if (coordenroutephoto.error) {
                redirect(action:edit, id:params.id)
            } else {
    			if (next_id) {
    				redirect(action:edit,id:next_id)
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
			long next_id = coordenroutephoto.instance.GetNextCoordEnroutePhotoID()
			if (next_id) {
				redirect(action:'edit',id:next_id)
			} else {
				redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
			}
		} else {
			flash.message = coordenroutephoto.message
			redirect(action:edit,id:params.id)
		}
	}
    
	def gotoprev = {
        def coordenroutephoto = fcService.getCoordEnroutePhoto(params)
		if (coordenroutephoto.instance) {
			long prev_id = coordenroutephoto.instance.GetPrevCoordEnroutePhotoID()
			if (prev_id) {
				redirect(action:'edit',id:prev_id)
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
        def coordenroutephoto = fcService.deleteCoordEnroutePhoto(params)
        if (coordenroutephoto.deleted) {
        	flash.message = coordenroutephoto.message
       	    redirect(controller:"route",action:show,id:coordenroutephoto.routeid)
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
    
    def assignnamealphabetical = {
        def coordenroutephoto = fcService.assignnamerandomlyCoordEnroutePhoto(params, false)
        flash.message = coordenroutephoto.message
        redirect(controller:"route",action:show,id:params.routeid)
    }
    
    def assignnamerandomly = {
        def coordenroutephoto = fcService.assignnamerandomlyCoordEnroutePhoto(params, true)
        flash.message = coordenroutephoto.message
        redirect(controller:"route",action:show,id:params.routeid)
    }
    
    def setposition = {
        def coordenroutephoto = fcService.setpositionCoordEnroutePhoto(params)
        flash.message = coordenroutephoto.message
        redirect(action:edit,id:params.id,params:[next:params.next])
    }
    
    def reset = {
		def coordenroutephoto = fcService.resetmeasureCoordEnroutePhoto(params)
        if (coordenroutephoto.saved) {
  			flash.message = coordenroutephoto.message
            redirect(action:edit, id:params.id)
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
       	    redirect(controller:"route",action:show,id:coordenroutephoto.instance.route.id)
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
	}
    
	def selectimagefilename = {
		[:]
    }
	
	def selectimagefilename_cancel = {
        redirect(action:"edit", id:params.id)
    }
    
	def loadimage = {
		MultipartFile image_file = request.getFile("imagefile")
		if (image_file && !image_file.isEmpty()) {
            fcService.importCoordEnroutePhoto(params, image_file) 
        }
        redirect(action:"edit", id:params.id)
    }
    
}
