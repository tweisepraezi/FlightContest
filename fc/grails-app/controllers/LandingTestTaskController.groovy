

class LandingTestTaskController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def landingtesttask = fcService.getLandingTestTask(params) 
        if (landingtesttask.instance) {
        	return [landingTestTaskInstance:landingtesttask.instance]
        } else {
            flash.message = landingtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def landingtesttask = fcService.getLandingTestTask(params) 
        if (landingtesttask.instance) {
        	return [landingTestTaskInstance:landingtesttask.instance]
        } else {
            flash.message = landingtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def landingtesttask = fcService.updateLandingTestTask(params) 
        if (landingtesttask.saved) {
        	flash.message = landingtesttask.message
        	redirect(action:show,id:landingtesttask.instance.id)
        } else if (landingtesttask.instance) {
        	render(view:'edit',model:[landingTestTaskInstance:landingtesttask.instance])
        } else {
        	flash.message = landingtesttask.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def landingtesttask = fcService.createLandingTestTask(params)
       	return [landingTestInstance:landingtesttask.instance]
    }

    def save = {
        def landingtesttask = fcService.saveLandingTestTask(params) 
        if (landingtesttask.saved) {
        	flash.message = landingtesttask.message
        	redirect(controller:"landingTest",action:show,id:landingtesttask.landingtestid)
        } else {
            render(view:'create',model:[landingTestTaskInstance:landingtesttask.instance])
        }
    }

    def delete = {
        def landingtesttask = fcService.deleteLandingTestTask(params)
        if (landingtesttask.deleted) {
        	flash.message = landingtesttask.message
        	redirect(controller:"landingTest",action:show,id:landingtesttask.landingtestid)
        } else if (landingtesttask.notdeleted) {
        	flash.message = landingtesttask.message
        	redirect(action:show,id:params.id)
        } else {
        	flash.message = landingtesttask.message
        	redirect(controller:"contest",action:"start")
        }
    }

	def cancel = {
        redirect(controller:"landingTest",action:show,id:params.landingtestid)
	}
}
