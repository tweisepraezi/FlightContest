import org.springframework.web.multipart.MultipartFile

class CoordMapObjectController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def coordmapobject = fcService.getCoordMapObject(params) 
        if (coordmapobject.instance) {
        	return [coordMapObjectInstance:coordmapobject.instance]
        } else {
            flash.message = coordmapobject.message
            redirect(controller:"contest",action:"start")
        }
    }

    def create = {
        def coordmapobject = fcService.createCoordMapObject(params)
        if (coordmapobject.error) {
            flash.error = coordmapobject.error
            flash.message = coordmapobject.message
            redirect(controller:"route",action:"mapexportquestion",id:params.routeid)
        } else {
            return [coordMapObjectInstance:coordmapobject.instance]
        }
    }

    def save = {
        def coordmapobject = fcService.saveCoordMapObject(session.showLanguage, params) 
        if (coordmapobject.saved) {
            flash.message = coordmapobject.message
            if (coordmapobject.error) {
                flash.error = true
            } 
            redirect(controller:"route",action:"mapexportquestion",id:params.routeid)
        } else if (coordmapobject.error) {
            flash.error = true
            flash.message = coordmapobject.message
            render(view:'create',model:[coordMapObjectInstance:coordmapobject.instance])
        } else {
            render(view:'create',model:[coordMapObjectInstance:coordmapobject.instance])
        }
    }

    def saveadd = {
        def coordmapobject = fcService.saveCoordMapObject(session.showLanguage, params) 
        if (coordmapobject.saved) {
            flash.message = coordmapobject.message
            if (coordmapobject.error) {
                flash.error = true
            } 
            redirect(controller:'coordMapObject',action:'create',params:['route.id':params.routeid,'routeid':params.routeid])
        } else if (coordmapobject.error) {
            flash.error = true
            flash.message = coordmapobject.message
            render(view:'create',model:[coordMapObjectInstance:coordmapobject.instance])
        } else {
            render(view:'create',model:[coordMapObjectInstance:coordmapobject.instance])
        }
    }

    def edit = {
        def coordmapobject = fcService.getCoordMapObject(params) 
        if (coordmapobject.instance) {
            return [coordMapObjectInstance:coordmapobject.instance]
        } else {
            flash.message = coordmapobject.message
            redirect(controller:"contest",action:"start")
        }
    }

    def selectsymbolfilename = {
        [:]
    }
    
	def selectsymbolfilename_cancel = {
        redirect(action:"edit", id:params.id)
    }
    
	def loadsymbol = {
		MultipartFile symbol_file = request.getFile("symbolfile")
		if (symbol_file && !symbol_file.isEmpty()) {
            fcService.importCoordMapObjectSymbol(params, symbol_file)
        }
        redirect(action:"edit", id:params.id)
    }
    
    def importsymbol = {
        def coordmapobject = fcService.getCoordMapObject(params) 
        if (coordmapobject.instance) {
            redirect(controller:'coordMapObject',action:'edit',id:coordmapobject.instance.id)
        } else {
            render(view:'create',model:[coordMapObjectInstance:coordmapobject.instance])
        }
    }
    
    def updatereturn = {
        def coordmapobject = fcService.updateCoordMapObject(session.showLanguage, params)
        if (coordmapobject.saved) {
            flash.message = coordmapobject.message
            redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
        } else if (coordmapobject.error) {
            flash.message = coordmapobject.message
            flash.error = true
            redirect(action:edit, id:params.id)
        } else if (coordmapobject.instance) {
            render(view:'edit',model:[coordMapObjectInstance:coordmapobject.instance])
        } else {
            flash.message = coordmapobject.message
            redirect(action:edit,id:params.id)
        }
    }

    def updatenext = {
        def coordmapobject = fcService.updateCoordMapObject(session.showLanguage, params)
        if (coordmapobject.saved || coordmapobject.error) {
            flash.message = coordmapobject.message
            flash.error = coordmapobject.error
            if (coordmapobject.error) {
                redirect(action:edit, id:params.id)
            } else {
				long next_id = coordmapobject.instance.GetNextCoordMapObjectID()
                if (next_id) {
                    redirect(action:edit,id:next_id)
                } else {
                    redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
                }
            }
        } else if (coordmapobject.instance) {
            render(view:'edit',model:[coordMapObjectInstance:coordmapobject.instance])
        } else {
            flash.message = coordmapobject.message
            redirect(action:edit,id:params.id)
        }
    }

	def gotonext = {
        def coordmapobject = fcService.getCoordMapObject(params)
		if (coordmapobject.instance) {
			long next_id = coordmapobject.instance.GetNextCoordMapObjectID()
			if (next_id) {
				redirect(action:'edit',id:next_id)
			} else {
				redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
			}
		} else {
			flash.message = coordmapobject.message
			redirect(action:edit,id:params.id)
		}
	}
    
	def gotoprev = {
        def coordmapobject = fcService.getCoordMapObject(params)
		if (coordmapobject.instance) {
			long prev_id = coordmapobject.instance.GetPrevCoordMapObjectID()
			if (prev_id) {
				redirect(action:'edit',id:prev_id)
			} else {
				redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
			}
		} else {
			flash.message = coordmapobject.message
			redirect(action:edit,id:params.id)
		}
	}
    
    def delete = {
        def coordmapobject = fcService.deleteCoordMapObject(params)
        if (coordmapobject.deleted) {
        	flash.message = coordmapobject.message
      	    redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.routeid)
        } else if (coordmapobject.notdeleted) {
        	flash.message = coordmapobject.message
            redirect(action:"mapexportquestion",id:params.id)
        } else if (coordmapobject.error) {
			flash.error = coordmapobject.error
			flash.message = coordmapobject.message
			redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
        } else {
        	flash.message = coordmapobject.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def removeall = {
        def coordmapobject = fcService.removeallCoordMapObject(params)
        flash.message = coordmapobject.message
        redirect(controller:"route",action:"mapexportquestion",id:params.routeid)
    }
    
    def reset = {
		def coordmapobject = fcService.resetmeasureCoordMapObject(params)
        if (coordmapobject.saved) {
			redirect(action:edit, id:params.id)
        } else if (coordmapobject.error) {
            flash.error = coordmapobject.error
            flash.message = coordmapobject.message
            redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
		} else if (coordmapobject.instance) {
			render(view:'edit',model:[coordMapObjectInstance:coordmapobject.instance])
		} else {
			flash.message = coordmapobject.message
			redirect(action:edit,id:params.id)
		}
	}
    
    def addposition = {
        def coordmapobject = fcService.addpositionCoordMapObject(params)
        flash.error = coordmapobject.error
        flash.message = coordmapobject.message
        redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
    }
    
    def subposition = {
        def coordmapobject = fcService.subpositionCoordMapObject(params)
        flash.error = coordmapobject.error
        flash.message = coordmapobject.message
        redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
    }
	
	def cancel = {
        def coordmapobject = fcService.getCoordMapObject(params) 
        if (coordmapobject.instance) {
       	    redirect(controller:"route",action:"mapexportquestion",id:coordmapobject.instance.route.id)
        } else {
            redirect(controller:"route",action:"mapexportquestion",id:params.routeid)
        }
	}
}
