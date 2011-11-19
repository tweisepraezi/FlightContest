

class NavTestController {
    
	def fcService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def show = {
        def navtest = fcService.getNavTest(params) 
        if (navtest.instance) {
        	return [navTestInstance:navtest.instance]
        } else {
            flash.message = navtest.message
            redirect(controller:"contest",action:"start")
        }
    }

    def edit = {
        def navtest = fcService.getNavTest(params) 
        if (navtest.instance) {
        	return [navTestInstance:navtest.instance]
        } else {
            flash.message = navtest.message
            redirect(controller:"contest",action:"start")
        }
    }

    def update = {
        def navtest = fcService.updateNavTest(params) 
        if (navtest.saved) {
        	flash.message = navtest.message
        	redirect(action:show,id:navtest.instance.id)
        } else if (navtest.instance) {
        	render(view:'edit',model:[navTestInstance:navtest.instance])
        } else {
        	flash.message = navtest.message
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def navtest = fcService.createNavTest(params)
        if (navtest.error) {
        	flash.message = navtest.message
        	flash.error = navtest.error
            if (navtest.fromlistcrewtests) {
            	redirect(controller:"contestDayTask",action:"listcrewtests",id:navtest.contestdaytaskid)
            } else if (navtest.fromcontestdaytask) {
            	redirect(controller:"contestDayTask",action:show,id:navtest.contestdaytaskid)
            } else {
            	redirect(controller:"contest",action:"start")
            }
        } else {
        	return [navTestInstance:navtest.instance]
        }
    }

    def save = {
        def navtest = fcService.saveNavTest(params) 
        if (navtest.saved) {
        	flash.message = navtest.message
            if (navtest.fromlistcrewtests) {
            	redirect(controller:"contestDayTask",action:"listcrewtests",id:navtest.contestdaytaskid)
            } else if (navtest.fromcontestdaytask) {
            	redirect(controller:"contestDayTask",action:show,id:navtest.contestdaytaskid)
            } else {
            	redirect(controller:"contest",action:"start")
            }
        } else {
            render(view:'create',model:[flightTestInstance:navtest.instance])
        }
    }

    def delete = {
        def navtest = fcService.deleteNavTest(params)
        if (navtest.deleted) {
        	flash.message = navtest.message
        	redirect(controller:"contest",action:"start")
        } else if (navtest.notdeleted) {
        	flash.message = navtest.message
            redirect(action:show,id:params.id)
        } else {
        	flash.message = navtest.message
        	redirect(controller:"contest",action:"start")
        }
    }
	
    def cancel = {
        if (params.fromlistcrewtests) {
            redirect(controller:"contestDayTask",action:"listcrewtests",id:params.contestdaytaskid)
        } else if (params.fromcontestdaytask) {
            redirect(controller:"contestDayTask",action:show,id:params.contestdaytaskid)
        } else {
            redirect(controller:"contest",action:"start")
        }
    }
    
	def createnavtesttask = {
        def navtest = fcService.getNavTest(params) 
        redirect(controller:'navTestTask',action:'create',params:['navTest.id':navtest.instance.id,'navtestid':navtest.instance.id])
	}
}
