

class SpecialTestTaskController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def specialtesttask = fcService.getSpecialTestTask(params) 
        if (specialtesttask.instance) {
        	return [specialTestTaskInstance:specialtesttask.instance]
        } else {
            flash.message = specialtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def specialtesttask = fcService.getSpecialTestTask(params) 
        if (specialtesttask.instance) {
        	return [specialTestTaskInstance:specialtesttask.instance]
        } else {
            flash.message = specialtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def specialtesttask = fcService.updateSpecialTestTask(params) 
        if (specialtesttask.saved) {
        	flash.message = specialtesttask.message
        	redirect(action:show,id:specialtesttask.instance.id)
        } else if (specialtesttask.instance) {
        	render(view:'edit',model:[specialTestTaskInstance:specialtesttask.instance])
        } else {
        	flash.message = specialtesttask.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def specialtesttask = fcService.createSpecialTestTask(params)
       	return [specialTestTaskInstance:specialtesttask.instance]
    }

    def save = {
        def specialtesttask = fcService.saveSpecialTestTask(params) 
        if (specialtesttask.saved) {
        	flash.message = specialtesttask.message
        	redirect(controller:"specialTest",action:show,id:specialtesttask.specialtestid)
        } else {
            render(view:'create',model:[specialTestTaskInstance:specialtesttask.instance])
        }
    }

    def delete = {
        def specialtesttask = fcService.deleteSpecialTestTask(params)
        if (specialtesttask.deleted) {
        	flash.message = specialtesttask.message
        	redirect(controller:"specialTest",action:show,id:specialtesttask.specialtestid)
        } else if (specialtesttask.notdeleted) {
        	flash.message = specialtesttask.message
        	redirect(action:show,id:params.id)
        } else {
        	flash.message = specialtesttask.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
	def cancel = {
        redirect(controller:"specialTest",action:show,id:params.specialtestid)
	}
}
