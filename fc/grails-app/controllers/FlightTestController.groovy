import java.util.Map;
import groovy.json.*

class FlightTestController {
    
	def fcService
    def printService
    
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def flighttest = fcService.getFlightTest(params) 
        if (flighttest.instance) {
			// save return action
			session.flighttestwindReturnAction = actionName
			session.flighttestwindReturnController = controllerName
			session.flighttestwindReturnID = params.id
            session.routeReturnAction = actionName
            session.routeReturnController = controllerName
            session.routeReturnID = params.id
			// assign return action
			if (session.flighttestReturnAction) {
				return [flightTestInstance:flighttest.instance,flighttestReturnAction:session.flighttestReturnAction,flighttestReturnController:session.flighttestReturnController,flighttestReturnID:session.flighttestReturnID]
			}
        	return [flightTestInstance:flighttest.instance]
        } else {
            flash.message = flighttest.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def edit = {
        def flighttest = fcService.getFlightTest(params) 
        if (flighttest.instance) {
        	return [flightTestInstance:flighttest.instance]
        } else {
            flash.message = flighttest.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def update = {
        def flighttest = fcService.updateFlightTest(session.showLanguage, params) 
        if (flighttest.saved) {
        	flash.message = flighttest.message
        	redirect(action:show,id:flighttest.instance.id)
        } else if (flighttest.instance) {
        	render(view:'edit',model:[flightTestInstance:flighttest.instance])
        } else {
        	flash.message = flighttest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def flighttest = fcService.createFlightTest(params,session.lastContest)
        if (flighttest.error) {
        	flash.message = flighttest.message
        	flash.error = flighttest.error
            if (flighttest.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:flighttest.taskid)
            } else if (flighttest.fromtask) {
            	redirect(controller:"task",action:show,id:flighttest.taskid)
            } else {
            	redirect(controller:"contest",action:"tasks")
            }
        } else {
        	return [flightTestInstance:flighttest.instance]
        }
    }

    def save = {
        def flighttest = fcService.saveFlightTest(session.showLanguage, params)
        flash.message = flighttest.message
        if (flighttest.saved) {
            if (flighttest.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:flighttest.taskid)
            } else if (flighttest.fromtask) {
            	redirect(controller:"task",action:show,id:flighttest.taskid)
            } else {
            	redirect(controller:"contest",action:"tasks")
            }
        } else {
            flash.error = true
            render(view:'create',model:[flightTestInstance:flighttest.instance])
        }
    }

    def delete = {
        def flighttest = fcService.deleteFlightTest(params)
        if (flighttest.deleted) {
        	flash.message = flighttest.message
        	redirect(controller:"contest",action:"tasks")
        } else if (flighttest.notdeleted) {
			flash.error = true
        	flash.message = flighttest.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = flighttest.message
        	redirect(controller:"contest",action:"tasks")
        }
    }

	def cancel = {
		// process return action
		if (params.flighttestReturnAction) {
            println "YY1 $params"
			redirect(action:params.flighttestReturnAction,controller:params.flighttestReturnController,id:params.flighttestReturnID)
		} else if (params.fromlistplanning) {
            redirect(controller:"task",action:"listplanning",id:params.taskid)
        } else if (params.fromtask) {
            redirect(controller:"task",action:show,id:params.taskid)
        } else {
            redirect(controller:"contest", action:"tasks")
        }
	}
	
	def createflighttestwind = {
        def flighttest = fcService.getFlightTest(params) 
        redirect(controller:'flightTestWind',action:'create',params:['flightTest.id':flighttest.instance.id,'flighttestid':flighttest.instance.id])
	}
    
    def addobservations = {
        def flighttest = fcService.addobservationsFlightTest(params)
        if (flighttest.instance) {
            flash.message = flighttest.message
            redirect(action:show,id:params.id)
        } else {
            flash.message = flighttest.message
            redirect(action:edit,id:params.id)
        }
    }
    
    def removeobservations = {
        def flighttest = fcService.removeobservationsFlightTest(params)
        if (flighttest.instance) {
            flash.message = flighttest.message
            redirect(action:show,id:params.id)
        } else {
            flash.message = flighttest.message
            redirect(action:edit,id:params.id)
        }
    }
    
    def printneutralobservation = {
        def flighttest = printService.printneutralobservationFlightTest(false,params,false,false,GetPrintParams())
        if (!flighttest.instance) {
            flash.message = flighttest.message
            redirect(controller:"contest",action:"tasks")
        } else if (flighttest.error) {
            flash.message = flighttest.message
            flash.error = true
            redirect(action:show,id:flighttest.instance.id)
        } else if (flighttest.content) {
            printService.WritePDF(response,flighttest.content,session.lastContest.GetPrintPrefix(),"observation-task${flighttest.instance.task.idTitle}",true,false,false)
        } else {
            redirect(action:listplanning,id:flighttest.instance.id)
        }
    }
    
    def printobservationresults = {
        def flighttest = printService.printneutralobservationFlightTest(true,params,false,false,GetPrintParams())
        if (!flighttest.instance) {
            flash.message = flighttest.message
            redirect(controller:"contest",action:"tasks")
        } else if (flighttest.error) {
            flash.message = flighttest.message
            flash.error = true
            redirect(action:show,id:flighttest.instance.id)
        } else if (flighttest.content) {
            printService.WritePDF(response,flighttest.content,session.lastContest.GetPrintPrefix(),"observation-task${flighttest.instance.task.idTitle}",true,false,false)
        } else {
            redirect(action:listplanning,id:flighttest.instance.id)
        }
    }

    def getroutedata_ajax() {
        if (params.routeId) {
            Route route_instance = Route.get(params.routeId.toLong())
            if (route_instance) {
                StringWriter w = new StringWriter()
                def builder = new StreamingJsonBuilder(w)
                builder route_instance.GetFlightTestWindDirection()
                render(text: w.toString())
            } else {
                render(text: "")
            }
        } else {
            render(text: "")
        }
    }

    Map GetPrintParams() {
        return [baseuri:request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request),
                contest:session.lastContest,
                lang:session.printLanguage
               ]
    }
}
