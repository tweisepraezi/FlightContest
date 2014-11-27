class RouteController {
    
	def fcService
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
		fcService.printstart "List routes"
        if (session?.lastContest) {
			session.lastContest.refresh()
            def routeList = Route.findAllByContest(session.lastContest,[sort:"id"])
			fcService.printdone "last contest"
            return [routeInstanceList:routeList]
        }
		fcService.printdone ""
        redirect(controller:'contest',action:'start')
    }

    def show = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def edit = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def update = {
        def route = fcService.updateRoute(params) 
        if (route.saved) {
        	flash.message = route.message
        	redirect(action:show,id:route.instance.id)
        } else if (route.instance) {
        	render(view:'edit',model:[routeInstance:route.instance])
        } else {
        	flash.message = route.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
		def route = fcService.createRoute(params)
        return [routeInstance:route.instance]
    }

    def save = {
        def route = fcService.saveRoute(params,session.lastContest) 
        if (route.saved) {
        	flash.message = route.message
        	redirect(action:show,id:route.instance.id)
        } else {
            render(view:'create',model:[routeInstance:route.instance])
        }
    }

    def delete = {
        def route = fcService.deleteRoute(params)
        if (route.deleted) {
        	flash.message = route.message
        	redirect(action:list)
        } else if (route.notdeleted) {
        	flash.message = route.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = route.message
        	redirect(action:list)
        }
    }
	
	def cancel = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
            redirect(action:show,id:route.instance.id)
        } else {
        	redirect(action:list)
        }
	}
	
	def createcoordroutes = {
        def route = fcService.getRoute(params) 
        redirect(controller:'coordRoute',action:'create',params:['route.id':route.instance.id,'routeid':route.instance.id])
	}

    def createsecretcoordroutes = {
        def route = fcService.getRoute(params) 
        redirect(controller:'coordRoute',action:'create',params:['secret':true,'route.id':route.instance.id,'routeid':route.instance.id])
    }

	def selectaflosroute = {
		[contestInstance:session.lastContest]
    }
	
	def importroute = {
		def route = fcService.existAnyAflosRoute(session.lastContest)
		if (route.error) {
			flash.error = route.error
            flash.message = route.message
	        redirect(action:list)
		} else {
	        redirect(action:selectaflosroute)
		}
	}
    
	def importaflosroute = {
        def route = fcService.importAflosRoute(params,session.lastContest,"",false,[]) // false - $curved wird nicht ignoriert 
        if (route.saved) {
            flash.message = route.message
			if (route.error) {
				flash.error = route.error
			}
        } else if (route.error) {
        	flash.error = route.error
            flash.message = route.message
        }
        redirect(action:list)
	}
	
	def calculateroutelegs = {
        def route = fcService.caculateroutelegsRoute(params) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:show,id:route.instance.id)
        } else if (route.instance) {
            flash.message = route.message
            redirect(action:show,id:route.instance.id)
        } else {
            flash.message = route.message
            redirect(action:list)
        }
	}
    
    def print = {
        def routes = fcService.printRoutes(params,false,false,GetPrintParams()) 
        if (routes.error) {
            flash.message = routes.message
            flash.error = true
            redirect(action:list)
        } else if (routes.found && routes.content) {
            fcService.WritePDF(response,routes.content,session.lastContest.GetPrintPrefix(),"routes",true,false,false)
        } else {
            redirect(action:list)
        }
    }
    
    def printroute = {
        def route = fcService.printRoute(params,false,false,GetPrintParams()) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            fcService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def printcoordall = {
        def route = fcService.printCoord(params,false,false,GetPrintParams(),"all") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            fcService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-allpoints",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def printcoordtp = {
        def route = fcService.printCoord(params,false,false,GetPrintParams(),"tp") 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else if (route.content) {
            fcService.WritePDF(response,route.content,session.lastContest.GetPrintPrefix(),"route-${route.instance.idTitle}-tppoints",true,false,false)
        } else {
            redirect(action:list)
        }
	}
	
    def copyroute = {
        def route = fcService.copyRoute(params) 
        if (route.error) {
            flash.message = route.message
            flash.error = true
            redirect(action:list)
        } else {
            redirect(action:list)
        }
	}
	
    def showprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def route = fcService.getRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showcoordallprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def route = fcService.getRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showcoordtpprintable = {
        if (params.contestid) {
            session.lastContest = Contest.get(params.contestid)
        }
        def route = fcService.getRoute(params) 
        if (route.instance) {
            return [contestInstance:session.lastContest,routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

    def showmap = {
        def route = fcService.getRoute(params) 
        if (route.instance) {
        	return [routeInstance:route.instance]
        } else {
            flash.message = route.message
            redirect(action:list)
        }
    }

	Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
