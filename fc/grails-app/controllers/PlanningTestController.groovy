class PlanningTestController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def planningtest = fcService.getPlanningTest(params) 
        if (planningtest.instance) {
        	return [planningTestInstance:planningtest.instance]
        } else {
            flash.message = planningtest.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def planningtest = fcService.getPlanningTest(params) 
        if (planningtest.instance) {
        	return [planningTestInstance:planningtest.instance]
        } else {
            flash.message = planningtest.message
            redirect(controller:"contest",action:"start")
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
        def planningtest = fcService.createPlanningTest(params)
        if (planningtest.error) {
        	flash.message = planningtest.message
        	flash.error = planningtest.error
            if (planningtest.fromlistplanning) {
            	redirect(controller:"contestDayTask",action:"listplanning",id:planningtest.contestdaytaskid)
            } else if (planningtest.fromcontestdaytask) {
            	redirect(controller:"contestDayTask",action:show,id:planningtest.contestdaytaskid)
            } else {
            	redirect(controller:"contest",action:"start")
            }
        } else {
        	return [planningTestInstance:planningtest.instance]
        }
    }

    def save = {
        def planningtest = fcService.savePlanningTest(params) 
        if (planningtest.saved) {
        	flash.message = planningtest.message
            if (planningtest.fromlistplanning) {
            	redirect(controller:"contestDayTask",action:"listplanning",id:planningtest.contestdaytaskid)
            } else if (planningtest.fromcontestdaytask) {
            	redirect(controller:"contestDayTask",action:show,id:planningtest.contestdaytaskid)
            } else {
            	redirect(controller:"contest",action:"start")
            }
        } else {
            render(view:'create',model:[planningTestInstance:planningtest.instance])
        }
    }

    def delete = {
        def planningtest = fcService.deletePlanningTest(params)
        if (planningtest.deleted) {
        	flash.message = planningtest.message
        	redirect(controller:"contest",action:"start")
        } else if (planningtest.notdeleted) {
        	flash.message = planningtest.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = planningtest.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def cancel = {
        if (params.fromlistplanning) {
            redirect(controller:"contestDayTask",action:"listplanning",id:params.contestdaytaskid)
        } else if (params.fromcontestdaytask) {
            redirect(controller:"contestDayTask",action:show,id:params.contestdaytaskid)
        } else {
            redirect(controller:"contest",action:"start")
        }
    }
    
	def createplanningtesttask = {
        def planningtest = fcService.getPlanningTest(params) 
        redirect(controller:'planningTestTask',action:'create',params:['planningtestid':planningtest.instance.id])
	}
}
