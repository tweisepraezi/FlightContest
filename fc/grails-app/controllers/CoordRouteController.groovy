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

    def updatereturn = {
        def coordroute = fcService.updateCoordRoute(params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
            redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:edit,id:params.id)
        }
    }

    def updatenext = {
        def coordroute = fcService.updateCoordRoute(params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
			
			// search next id
			boolean set_next = false
			CoordRoute coordroute_nextinstance = null
			CoordRoute coordroute_nextinstance2 = null
			int leg_no = 0
			for ( CoordRoute coordroute_instance in CoordRoute.findAllByRoute(coordroute.instance.route,[sort:"id"]) ) {
				if (!coordroute_nextinstance) {
					leg_no++
				}
				if (set_next) {
					if (!coordroute_nextinstance) {
						coordroute_nextinstance = coordroute_instance
					} else if (!coordroute_nextinstance2) {
						coordroute_nextinstance2 = coordroute_instance
						break
					}
				}
				if (coordroute_instance == coordroute.instance) {
					set_next = true
				}
			}

			if (coordroute_nextinstance2) {
				redirect(action:'edit',id:coordroute_nextinstance.id,params:[name:leg_no,next:coordroute_nextinstance2.id])
			} else if (coordroute_nextinstance) {
				redirect(action:'edit',id:coordroute_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"route",action:show,id:coordroute.instance.route.id)
			}
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else if (coordroute.instance) {
        	render(view:'edit',model:[coordRouteInstance:coordroute.instance])
        } else {
        	flash.message = coordroute.message
            redirect(action:edit,id:params.id)
        }
    }
	
	def gotonext = {
        def coordroute = fcService.getCoordRoute(params)
		if (coordroute.instance) {
			
			// search next id
			boolean set_next = false
			CoordRoute coordroute_nextinstance = null
			CoordRoute coordroute_nextinstance2 = null
			int leg_no = 0
			for ( CoordRoute coordroute_instance in CoordRoute.findAllByRoute(coordroute.instance.route,[sort:"id"]) ) {
				if (!coordroute_nextinstance) {
					leg_no++
				}
				if (set_next) {
					if (!coordroute_nextinstance) {
						coordroute_nextinstance = coordroute_instance
					} else if (!coordroute_nextinstance2) {
						coordroute_nextinstance2 = coordroute_instance
						break
					}
				}
				if (coordroute_instance == coordroute.instance) {
					set_next = true
				}
			}

			if (coordroute_nextinstance2) {
				redirect(action:'edit',id:coordroute_nextinstance.id,params:[name:leg_no,next:coordroute_nextinstance2.id])
			} else if (coordroute_nextinstance) {
				redirect(action:'edit',id:coordroute_nextinstance.id,params:[name:leg_no])
			} else {
				redirect(controller:"route",action:show,id:coordroute.instance.route.id)
			}
		} else {
			flash.message = coordroute.message
			redirect(action:edit,id:params.id)
		}
	}

    def create = {
		def coordroute = fcService.createCoordRoute(params)
		if (coordroute.error) {
			flash.error = coordroute.error
            flash.message = coordroute.message
            redirect(controller:"route",action:show,id:params.routeid)
		} else {
			boolean exist_ifp = CoordRoute.findByRouteAndType(coordroute.instance.route,CoordType.iFP,[sort:"id", order:"desc"])
			return [coordRouteInstance:coordroute.instance,existiFP:exist_ifp]
		}
    }

    def save = {
        def coordroute = fcService.saveCoordRoute(params) 
        if (coordroute.saved) {
        	flash.message = coordroute.message
        	redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else {
            render(view:'create',model:[coordRouteInstance:coordroute.instance])
        }
    }

    def delete = {
        def coordroute = fcService.deleteCoordRoute(params)
        if (coordroute.deleted) {
        	flash.message = coordroute.message
        	redirect(controller:"route",action:show,id:coordroute.routeid)
        } else if (coordroute.notdeleted) {
        	flash.message = coordroute.message
            redirect(action:show,id:params.id)
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else {
        	flash.message = coordroute.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def reset = {
		def coordroute = fcService.resetmeasureCoordRoute(params)
		if (coordroute.saved) {
			flash.message = coordroute.message
			redirect(action:edit,id:params.id)
        } else if (coordroute.error) {
			flash.error = coordroute.error
			flash.message = coordroute.message
			redirect(controller:"route",action:show,id:coordroute.instance.route.id)
		} else if (coordroute.instance) {
			render(view:'edit',model:[coordRouteInstance:coordroute.instance])
		} else {
			flash.message = coordroute.message
			redirect(action:edit,id:params.id)
		}
	}
	
	def cancel = {
        def coordroute = fcService.getCoordRoute(params) 
        if (coordroute.instance) {
        	redirect(controller:"route",action:show,id:coordroute.instance.route.id)
        } else {
            redirect(controller:"route",action:show,id:params.routeid)
        }
	}
}
