class PlanningTestTaskController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def planningtesttask = fcService.getPlanningTestTask(params) 
        if (planningtesttask.instance) {
        	return [planningTestTaskInstance:planningtesttask.instance]
        } else {
            flash.message = planningtesttask.message
            redirect(controller:"contest",action:"tasks")
        }
    }

	def showprintable = {
        def planningtesttask = fcService.getPlanningTestTask(params) 
        if (planningtesttask.instance) {
        	return [planningTestTaskInstance:planningtesttask.instance]
        } else {
            flash.message = planningtesttask.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def edit = {
        def planningtesttask = fcService.getPlanningTestTask(params) 
        if (planningtesttask.instance) {
			// assign return action
			if (session.planningtesttaskReturnAction) {
				return [planningTestTaskInstance:planningtesttask.instance,contestInstance:session.lastContest,planningtesttaskReturnAction:session.planningtesttaskReturnAction,planningtesttaskReturnController:session.planningtesttaskReturnController,planningtesttaskReturnID:session.planningtesttaskReturnID]
			}
        	return [planningTestTaskInstance:planningtesttask.instance,contestInstance:session.lastContest]
        } else {
            flash.message = planningtesttask.message
            redirect(controller:"contest",action:"tasks")
        }
    }

    def update = {
        def planningtesttask = fcService.updatePlanningTestTask(params) 
        if (planningtesttask.saved) {
        	flash.message = planningtesttask.message
        	redirect(controller:"planningTest",action:show,id:planningtesttask.planningtestid)
        } else if (planningtesttask.instance) {
        	render(view:'edit',model:[planningTestTaskInstance:planningtesttask.instance,contestInstance:session.lastContest])
        } else {
        	flash.message = planningtesttask.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def planningtesttask = fcService.createPlanningTestTask(params,session.lastContest)
        if (planningtesttask.error) {
        	flash.message = planningtesttask.message
        	flash.error = planningtesttask.error
        	redirect(controller:"planningTest",action:show,id:planningtesttask.planningtestid)
        } else {
        	return [planningTestTaskInstance:planningtesttask.instance,contestInstance:session.lastContest]
        }
    }

    def save = {
        def planningtesttask = fcService.savePlanningTestTask(params) 
        if (planningtesttask.saved) {
        	flash.message = planningtesttask.message
            if (planningtesttask.fromlistplanning) {
            	redirect(controller:"task",action:"listplanning",id:planningtesttask.taskid)
            } else {
            	redirect(controller:"planningTest",action:show,id:planningtesttask.planningtestid)
            }
        } else {
            render(view:'create',model:[planningTestTaskInstance:planningtesttask.instance])
        }
    }

    def delete = {
        def planningtesttask = fcService.deletePlanningTestTask(params)
        if (planningtesttask.deleted) {
        	flash.message = planningtesttask.message
        	redirect(controller:"planningTest",action:show,id:planningtesttask.planningtestid)
        } else if (planningtesttask.notdeleted) {
			flash.error = true
        	flash.message = planningtesttask.message
        	redirect(action:edit,id:params.id)
        } else {
        	flash.message = planningtesttask.message
        	redirect(controller:"contest",action:"tasks")
        }
    }
	
	def cancel = {
		// process return action
		if (params.planningtesttaskReturnAction) {
			redirect(action:params.planningtesttaskReturnAction,controller:params.planningtesttaskReturnController,id:params.planningtesttaskReturnID)
		} else if (params.fromlistplanning) {
            redirect(controller:"task",action:"listplanning",id:params.taskid)
        } else {
            redirect(controller:"planningTest",action:show,id:params.planningtestid)
        }
	}
}
