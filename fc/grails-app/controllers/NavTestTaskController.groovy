

class NavTestTaskController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def navtesttask = fcService.getNavTestTask(params) 
        if (navtesttask.instance) {
        	return [navTestTaskInstance:navtesttask.instance]
        } else {
            flash.message = navtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

	def showprintable = {
        def navtesttask = fcService.getNavTestTask(params) 
        if (navtesttask.instance) {
        	return [navTestTaskInstance:navtesttask.instance]
        } else {
            flash.message = navtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def print = {
        def navtesttask = fcService.printNavTestTask(params,GetBaseURI()) 
        if (!navtesttask.instance) {
            flash.message = navtesttask.message
            redirect(controller:"contest",action:"start")
        } else if (navtesttask.error) {
        	flash.message = navtesttask.message
        	flash.error = true
        	redirect(action:show,id:navtesttask.instance.id)
        } else if (navtesttask.content) {
        	fcService.WritePDF(response,navtesttask.content)
        } else {
        	redirect(action:show,id:navtesttask.instance.id)
        }
    }

    def edit = {
        def navtesttask = fcService.getNavTestTask(params) 
        if (navtesttask.instance) {
        	return [navTestTaskInstance:navtesttask.instance]
        } else {
            flash.message = navtesttask.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def navtesttask = fcService.updateNavTestTask(params) 
        if (navtesttask.saved) {
        	flash.message = navtesttask.message
        	redirect(action:show,id:navtesttask.instance.id)
        } else if (navtesttask.instance) {
        	render(view:'edit',model:[navTestTaskInstance:navtesttask.instance])
        } else {
        	flash.message = navtesttask.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def navtesttask = fcService.createNavTestTask(params)
        if (navtesttask.error) {
        	flash.message = navtesttask.message
        	flash.error = navtesttask.error
        	redirect(controller:"navTest",action:show,id:navtesttask.navtestid)
        } else {
        	return [navTestTaskInstance:navtesttask.instance]
        }
    }

    def save = {
        def navtesttask = fcService.saveNavTestTask(params) 
        if (navtesttask.saved) {
        	flash.message = navtesttask.message
            if (navtesttask.fromlistcrewtests) {
            	redirect(controller:"contestDayTask",action:"listcrewtests",id:navtesttask.contestdaytaskid)
            } else {
            	redirect(controller:"navTest",action:show,id:navtesttask.navtestid)
            }
        } else {
            render(view:'create',model:[navTestTaskInstance:navtesttask.instance])
        }
    }

    def delete = {
        def navtesttask = fcService.deleteNavTestTask(params)
        if (navtesttask.deleted) {
        	flash.message = navtesttask.message
        	redirect(controller:"navTest",action:show,id:navtesttask.navtestid)
        } else if (navtesttask.notdeleted) {
        	flash.message = navtesttask.message
        	redirect(action:show,id:params.id)
        } else {
        	flash.message = navtesttask.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
	def cancel = {
        if (params.fromlistcrewtests) {
            redirect(controller:"contestDayTask",action:"listcrewtests",id:params.contestdaytaskid)
        } else {
            redirect(controller:"navTest",action:show,id:params.navtestid)
        }
	}

	String GetBaseURI() {
		return request.scheme + "://" + request.serverName + ":" + request.serverPort + grailsAttributes.getApplicationUri(request)
	}

}
