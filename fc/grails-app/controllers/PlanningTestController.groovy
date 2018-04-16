class PlanningTestController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def planningtest = fcService.getPlanningTest(params) 
        if (planningtest.instance) {
			// save return action
			session.planningtesttaskReturnAction = actionName
			session.planningtesttaskReturnController = controllerName
			session.planningtesttaskReturnID = params.id
			// assign return action
			if (session.planningtestReturnAction) {
				return [planningTestInstance:planningtest.instance,planningtestReturnAction:session.planningtestReturnAction,planningtestReturnController:session.planningtestReturnController,planningtestReturnID:session.planningtestReturnID]
			}
        	return [planningTestInstance:planningtest.instance]
        } else {
            flash.message = planningtest.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def edit = {
        def planningtest = fcService.getPlanningTest(params) 
        if (planningtest.instance) {
        	return [planningTestInstance:planningtest.instance]
        } else {
            flash.message = planningtest.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def update = {
        def planningtest = fcService.updatePlanningTest(params) 
        if (planningtest.saved) {
        	flash.message = planningtest.message
        	redirect(action:show,id:planningtest.instance.id)
        } else if (planningtest.instance) {
        	render(view:'edit',model:[planningTestInstance:planningtest.instance])
        } else {
        	flash.message = planningtest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def planningtest = fcService.createPlanningTest(params,session.lastContest)
        if (planningtest.error) {
        	flash.message = planningtest.message
        	flash.error = planningtest.error
            if (planningtest.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:planningtest.taskid)
            } else if (planningtest.fromtask) {
            	redirect(controller:"task",action:show,id:planningtest.taskid)
            } else {
            	redirect(controller:"contest",action:"tasks")
            }
        } else {
        	return [planningTestInstance:planningtest.instance]
        }
    }

    def save = {
        def planningtest = fcService.savePlanningTest(params,true) 
        flash.message = planningtest.message
        if (planningtest.saved) {
            if (planningtest.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:planningtest.taskid)
            } else if (planningtest.fromtask) {
            	redirect(controller:"task",action:show,id:planningtest.taskid)
            } else {
            	redirect(controller:"contest",action:"tasks")
            }
        } else {
            flash.error = true
            render(view:'create',model:[planningTestInstance:planningtest.instance])
        }
    }

    def delete = {
        def planningtest = fcService.deletePlanningTest(params)
        if (planningtest.deleted) {
        	flash.message = planningtest.message
        	redirect(controller:"contest",action:"tasks")
        } else if (planningtest.notdeleted) {
			flash.error = true
        	flash.message = planningtest.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = planningtest.message
        	redirect(controller:"contest",action:"tasks")
        }
    }
	
    def cancel = {
		if (params.planningtestReturnAction) {
			redirect(action:params.planningtestReturnAction,controller:params.planningtestReturnController,id:params.planningtestReturnID)
		} else if (params.fromlistplanning) {
            redirect(controller:"task",action:"listplanning",id:params.taskid)
        } else if (params.fromtask) {
            redirect(controller:"task",action:show,id:params.taskid)
        } else {
            redirect(controller:"contest",action:"tasks")
        }
    }
    
	def createplanningtesttask = {
        def planningtest = fcService.getPlanningTest(params) 
        redirect(controller:'planningTestTask',action:'create',params:['planningtestid':planningtest.instance.id])
	}
}
